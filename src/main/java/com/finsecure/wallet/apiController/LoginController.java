package com.finsecure.wallet.apiController;

import com.finsecure.wallet.common.ServiceOutcome;
import com.finsecure.wallet.dto.AuthRequest;
import com.finsecure.wallet.model.*;
import com.finsecure.wallet.repository.UnqIDStoreRepo;
import com.finsecure.wallet.security.CustomAuthenticationSuccessHandler;
import com.finsecure.wallet.service.CommonService;
import com.finsecure.wallet.service.LoginService;
import com.finsecure.wallet.service.RoleService;
import com.finsecure.wallet.service.UserService;
import com.finsecure.wallet.utils.AesUtil;
import com.finsecure.wallet.utils.ClientInfo;
import com.finsecure.wallet.utils.SecurityHelper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/umt")
public class LoginController implements MessageSourceAware {

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    private MessageSource messageSource;

    @Autowired
    private UserService userService;

    @Value("${post.login.url}")
    private String POST_LOGIN_URL;

    @Value("${captcha.options}")
    private String CAPTCHA_OPTIONS;

    @Autowired
    private RoleService roleService;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private LoginService loginService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private UnqIDStoreRepo unqIDStoreRepo;

    @PostMapping(path = {"/umt/login"}, name = "Login Processing POST")
    public String doLogin(HttpServletRequest request, HttpServletResponse httpServletResponse, AuthRequest authRequest, RedirectAttributes model) {
        String returnPath = "redirect:" + this.POST_LOGIN_URL;
        Boolean isOK = Boolean.valueOf(true);
        String realPass = "";
        String realUsername = "";
        httpServletResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        httpServletResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0
        httpServletResponse.setDateHeader("Expires", 0);
        try {
            String decryptedUserName =  new String(java.util.Base64.getDecoder().decode(authRequest.getUserName()));
            String psk1 = request.getParameter("_csrf");
            psk1 = psk1.substring(0, 16);
            AesUtil aesUtil = new AesUtil(128, 1000);
            if (decryptedUserName != null && decryptedUserName.split("::").length == 3) {
                realUsername = aesUtil.decrypt(decryptedUserName.split("::")[1], decryptedUserName.split("::")[0], psk1, decryptedUserName.split("::")[2]);
                authRequest.setUserName(realUsername);
            }
            ServiceOutcome<User> svcOutcome = this.userService.findByUsername(authRequest.getUserName());
            if (svcOutcome.isOutcome()) {
                User user = (User)svcOutcome.getData();
                if (user != null) {
                    if (user.getIsEnabled().booleanValue()) {
                        if (user.getIsLocked().booleanValue() || !user.getPrimaryRole().getIsActive()) {
                            model.addFlashAttribute("error_msg", this.messageSource.getMessage("login.account.locked", null, LocaleContextHolder.getLocale()));
                            log.debug("Account Locked");
                            isOK = Boolean.valueOf(false);
//                            loginService.createFailedLoginHistory(user, request,httpServletResponse);
                        }
//	            if (user.getIsLoggedIn().booleanValue() && !user.getAllowMultipleSession().booleanValue()) {
//	              model.addFlashAttribute("error_msg", this.messageSource.getMessage("login.account.loggedin", null, LocaleContextHolder.getLocale()));
//	              log.debug("Already Logged In");
//	              isOK = Boolean.valueOf(false);
//	              loginService.createFailedLoginHistory(user, request,httpServletResponse);
//	            }
                        if (isOK.booleanValue()) {
                            String svrCaptcha = (String)request.getSession().getAttribute("CAPTCHA_CODE");
                            Boolean verifiyCaptcha = Boolean.valueOf(true);
                            try {
                                if (this.CAPTCHA_OPTIONS.toLowerCase().contains("i"))
                                    verifiyCaptcha = Boolean.valueOf(false);
                            } catch (Exception ex) {
                                log.error("CAPTCHA_OPTIONS may not be defined ");
                                log.error(ex.getMessage());
                            }
                            if (verifiyCaptcha.booleanValue()) {
                                log.debug("verifiying captcha");
                                log.debug("User sent : " + authRequest.getCaptcha());
                                log.debug("Server has : " + svrCaptcha);
                                if (!svrCaptcha.equals(authRequest.getCaptcha())) {
                                    model.addFlashAttribute("error_msg", this.messageSource.getMessage("login.account.badcaptcha", null, LocaleContextHolder.getLocale()));
                                    log.debug("Captcha Mismatch");
                                    isOK = Boolean.valueOf(false);
                                    loginService.createFailedLoginHistory(user, request,httpServletResponse);
                                }
                            } else {
                                log.debug("ignoring captcha");
                            }
                        }
//                        List<String> accessList = commonService.getWebUserAccessListByIsActiveAndIsWebAccess(true,true);
//                        if(!accessList.contains(user.getPrimaryRole().getRoleCode())) {
//                            isOK = Boolean.valueOf(false);
//                            model.addFlashAttribute("error_msg", "Web access is forbidden.");
//                            loginService.createFailedLoginHistory(user, request,httpServletResponse);
//                        }
//                        ServiceOutcome<String> checkOtpAccessForUser = commonService.checkOtpAccessForUser(user,true,false);
//                        if(!checkOtpAccessForUser.getOutcome()) {
//                            isOK = Boolean.valueOf(false);
//                            model.addFlashAttribute("error_msg", "You are not allowed to login using password");
//                            loginService.createFailedLoginHistory(user, request,httpServletResponse);
//                        }
                        if (isOK.booleanValue()) {
                            String decryptedPassword =  new String(java.util.Base64.getDecoder().decode(authRequest.getPassword()));
                            String psk = request.getParameter("_csrf");
                            psk = psk.substring(0, 16);
                            if (decryptedPassword != null && decryptedPassword.split("::").length == 3) {
                                realPass = aesUtil.decrypt(decryptedPassword.split("::")[1], decryptedPassword.split("::")[0], psk, decryptedPassword.split("::")[2]);
                            }
                            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                            String existingPassword = realPass;
                            String dbPassword = user.getPassword();
                            isOK = Boolean.valueOf(passwordEncoder.matches(existingPassword, dbPassword));
                            if (!isOK.booleanValue()) {
                                user.setWrongLoginCount(Integer.valueOf(((user.getWrongLoginCount() == null) ? 0 : user.getWrongLoginCount().intValue()) + 1));
                                if (user.getWrongLoginCount().intValue() >= 5) {
                                    user.setIsLocked(Boolean.valueOf(true));
                                    user.setIsLoggedIn(Boolean.valueOf(false));
                                }
                                this.userService.save(user);
                                model.addFlashAttribute("error_msg", this.messageSource.getMessage("login.account.badcredentials", null, LocaleContextHolder.getLocale()));
                                log.debug("Bad Credentials - Invalid password");

                                loginService.createFailedLoginHistory(user, request,httpServletResponse);
                                if(user.getPrimaryRole().getRoleCode().equals("ROLE_PUBLIC")) {
                                    return "redirect:/login";
                                }else {
                                    return "redirect:/officialLogin";
                                }

                            }
                        }
                        if (isOK.booleanValue()) {
                            /* Code for Session Hijacking */
                            HttpSession currentSession = request.getSession();
                            currentSession.invalidate();

                            HttpSession newSession = request.getSession(true);
                            newSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                                    SecurityContextHolder.getContext());

                            String token = UUID.randomUUID().toString();
                            newSession.setAttribute("tokenVal", token);

                            HttpSession newSession2 = request.getSession();

                            String secretToken = java.util.UUID.randomUUID().toString();

                            newSession2.setAttribute("SESSION_SECRET_TOKEN", secretToken);

                            Cookie cookie = new Cookie("SESSION_SECRET_TOKEN", secretToken);
                            cookie.setPath("/");
                            cookie.setHttpOnly(false);
                            httpServletResponse.addCookie(cookie);

                            String userAgent = request.getHeader("User-Agent");
                            String ipAddress = request.getRemoteAddr();

                            newSession.setAttribute("UA", userAgent);
                            newSession.setAttribute("IP", ipAddress);

                            authorizeUser(request, user,httpServletResponse);
                            loginService.createSuccessLoginHistory(user, request, httpServletResponse);

                        }
                    } else {
                        model.addFlashAttribute("error_msg", this.messageSource.getMessage("login.account.disabled", null, LocaleContextHolder.getLocale()));
                        log.debug("Account Disabled");
                        isOK = Boolean.valueOf(false);
                        loginService.createFailedLoginHistory(user, request,httpServletResponse);
                    }
                } else {
                    int count =  checkdDosAttack(request,authRequest.getUserName(),model,realPass);

                    //model.addFlashAttribute("error_msg", this.messageSource.getMessage("login.account.badcredentials", null, LocaleContextHolder.getLocale()));
                    //log.debug("Bad Credentials - No user found");
                    isOK = Boolean.valueOf(false);

                    loginService.createNoUserLoginHistory(authRequest.getUserName(), request,httpServletResponse);
                }
            } else {
                model.addFlashAttribute("error_msg", svcOutcome.getMessage());
                log.debug("System Issue");
                isOK = Boolean.valueOf(false);
                loginService.createNoUserLoginHistory(authRequest.getUserName(), request,httpServletResponse);
                return "redirect:/officialLogin";
            }
        } catch (Exception ex) {
            model.addFlashAttribute("error_msg", this.messageSource.getMessage("msg.error", null, LocaleContextHolder.getLocale()));
            log.error(String.valueOf(ex));
            isOK = Boolean.valueOf(false);
            return "redirect:/officialLogin";
        }
        if (isOK.booleanValue()) {
            return returnPath;
        }

        return "redirect:/officialLogin";
    }

    @PostMapping(path = {"/umt/logout"}, name = "Logout")
    public String doLogout(HttpServletRequest request, HttpServletResponse response) throws MalformedURLException {
        try {
            HttpSession session = request.getSession(false);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (session == null || auth == null || !auth.isAuthenticated()
                    || "anonymousUser".equals(auth.getPrincipal())) {

                return "redirect:" + request.getContextPath() + POST_LOGIN_URL;
            }
            User currentUser = SecurityHelper.getCurrentUser();
            if (currentUser != null) {
                ServiceOutcome<User> svcOutcome = this.userService.findByUsername(currentUser.getUserName());
                User user = (User)svcOutcome.getData();
                if (user != null) {
                    user.setWrongLoginCount(Integer.valueOf(0));
                    user.setIsLocked(Boolean.valueOf(false));
                    user.setIsLoggedIn(Boolean.valueOf(false));
                    // this.loginService.createLogoutHistory(user, request);
                    this.userService.save(user);
                }
                Cookie[] cookies = request.getCookies();
                String[] cookieNames = new String[cookies.length];
                for (int i = 0; i < cookies.length; i++)
                    cookieNames[i] = cookies[i].getName();
                CookieClearingLogoutHandler cookieClearingLogoutHandler = new CookieClearingLogoutHandler(cookieNames);
                SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
                cookieClearingLogoutHandler.logout(request, response, null);
                securityContextLogoutHandler.logout(request, response, null);
            }
        } catch (Exception ex) {
            log.error(String.valueOf(ex));
        }
        String contextPath = request.getContextPath() + this.POST_LOGIN_URL;
        contextPath = contextPath.replace("//", "/");
        URL url = ServletUriComponentsBuilder.fromCurrentRequest().path(contextPath).build().toUri().toURL();
        return "redirect:" + url.toExternalForm();
    }

    private void authorizeUser(HttpServletRequest request, User user,HttpServletResponse httpServletResponse) throws IOException {
        List<UserRoleMap> userRoleMaps = userService.findUserRoleMapByUserId(user.getUserId());
        userRoleMaps = (List<UserRoleMap>)userRoleMaps.stream().filter(r -> r.getIsActive().booleanValue()).collect(Collectors.toList());
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        List<Role> lstRoles = new ArrayList<>();
        for (UserRoleMap urm : userRoleMaps) {
            Role role = (Role)this.roleService.findByRoleId(urm.getRoleId()).getData();
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleCode()));
            if (role.getIsActive().booleanValue())
                lstRoles.add(role);
        }
        user.setRoles(lstRoles);
        LoggedInUser userDetails = new LoggedInUser(user.getUserName(), user.getPassword(), true, true, true, true, grantedAuthorities, user.getPrimaryRole(), user);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, grantedAuthorities);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication((Authentication)usernamePasswordAuthenticationToken);
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", sc);
        ClientInfo.getClientBrowser(request);
        String ipAddress= request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        String token = UUID.randomUUID().toString();
        session.setAttribute("unqToken", token);
        //Optional<UnqIDStore> optionalUnq = Optional.ofNullable(unqIDStoreRepo.findByUserId(user.getUserId()));
        UnqIDStore unq = new UnqIDStore();
        unq.setUserId(user.getUserId());
        unq.setIsActive(true);
        unq.setToken_time(new Date());
        unq.setUnqTokenID(token.trim());
        unq.setIpAddress(ipAddress);
        unq.setUserAgent(userAgent);

        unqIDStoreRepo.save(unq);

        addSameSiteCookieAttribute(httpServletResponse); // add SameSite=strict cookie attribute
        Authentication authentication = (Authentication)usernamePasswordAuthenticationToken;
        if (authentication.isAuthenticated()) {
            customAuthenticationSuccessHandler.onAuthenticationSuccess(request, httpServletResponse, authentication);
        }

    }

    private void addSameSiteCookieAttribute(HttpServletResponse response) {
        Collection<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);
        boolean firstHeader = true;
        for (String header : headers) { // there can be multiple Set-Cookie attributes
            if (firstHeader) {
                response.setHeader(HttpHeaders.SET_COOKIE, String.format("%s; %s", header, "SameSite=Strict"));
                firstHeader = false;
                continue;
            }
            response.addHeader(HttpHeaders.SET_COOKIE, String.format("%s; %s", header, "SameSite=Strict"));
        }
    }

    private int checkdDosAttack(HttpServletRequest request,String username,RedirectAttributes attr,String password) {
        HttpSession session = request.getSession();
        int loginAttempt;
        if (session.getAttribute("loginCount") == null){
            session.setAttribute("loginCount", 0);
            loginAttempt = 0;
        }else{
            loginAttempt = (Integer) session.getAttribute("loginCount");
        }

        //this is 5 attempt counting from 0,1,2
        if (loginAttempt >= 5){
            long lastAccessedTime = session.getLastAccessedTime();
            Date date = new Date();
            long currentTime = date.getTime();
            long timeDiff = currentTime - lastAccessedTime;
            // 20 minutes in milliseconds
            if (timeDiff >= 600000){
                //invalidate user session, so they can try again
                session.invalidate();
            }
            else{
                // Error message
                loginAttempt++;
                attr.addFlashAttribute("error_msg","You have exceeded the 3 failed login attempt. Please contact your system admin for correct username and password.");
                //loginService.createNoUserLoginHistory(username, request);
            }

        }else{
            //userService.saveFailedLoginHistory(request, username,password);
            loginAttempt++;
            int allowLogin = 5-loginAttempt;
            attr.addFlashAttribute("error_msg","loginAttempt= "+loginAttempt+". Invalid username or password. You have "+allowLogin+" attempts remaining. Please try again!");
            //loginService.createNoUserLoginHistory(username, request);
        }
        session.setAttribute("loginCount",loginAttempt);
        session.setAttribute("lastAccessTime",session.getLastAccessedTime());
        return loginAttempt;
    }
}
