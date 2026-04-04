package com.finsecure.wallet.service;

import com.finsecure.wallet.common.ServiceOutcome;
import com.finsecure.wallet.model.*;
import com.finsecure.wallet.repository.UserLoginHistoryRepository;
import com.finsecure.wallet.utils.ClientInfo;
import com.finsecure.wallet.utils.SecurityHelper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService{

    @Autowired
    private UserLoginHistoryRepository ulHistoryReporsitory;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public ServiceOutcome<Boolean> createFailedLoginHistory(User user, HttpServletRequest request, HttpServletResponse response) {
        ServiceOutcome<Boolean> svcOutcome = new ServiceOutcome<>();
        try
        {
            Boolean result = false;
            UserLoginHistory ulHistory = new UserLoginHistory();

            ulHistory.setBrowserDetails(ClientInfo.getClientBrowser(request));
            ulHistory.setEmail(user.getEmail());
            ulHistory.setFirstName(user.getFirstName());
            ulHistory.setLastName(user.getLastName());
            ulHistory.setLoggedInDate(new Date());
            ulHistory.setLoggedOutDate(null);
            ulHistory.setLoginStatus("LOGIN");
            ulHistory.setLoginType(user.getPrimaryRole().getRoleCode());
            ulHistory.setMobile(user.getMobile());
            ulHistory.setOsDetails(ClientInfo.getClientOS(request));
            ulHistory.setUserName(user.getUserName());
            ulHistory.setUserId(user.getUserId());
            ulHistory.setIpAddress(ClientInfo.getClientIpAddr(request));

            if(SecurityHelper.getCurrentUser()== null) {
                ServiceOutcome<User> user1 = userService.findByUsername("system");
                authorizedUser(user1.getData());
                result=true;
            }

            ulHistoryReporsitory.save(ulHistory);

            if(result) {
                Cookie[] cookies = request.getCookies();
                String[] cookieNames = new String[cookies.length];

                for(int i = 0; i < cookies.length; i++)
                {
                    cookieNames[i] = cookies[i].getName();
                }


                CookieClearingLogoutHandler cookieClearingLogoutHandler = new CookieClearingLogoutHandler(cookieNames);
                SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
                cookieClearingLogoutHandler.logout(request, response, null);
                securityContextLogoutHandler.logout(request, response, null);
            }

            svcOutcome.setData(true);
        }
        catch(Exception ex)
        {
            svcOutcome.setOutcome(false);
            svcOutcome.setData(false);
            svcOutcome.setMessage(ex.getMessage());
            log.error("Exception occured in createFailedLoginHistory in LoginServiceImpl-->",ex);
        }

        return svcOutcome;
    }

    @Override
    public ServiceOutcome<Boolean> createNoUserLoginHistory(String userName, HttpServletRequest request,HttpServletResponse response) {
        ServiceOutcome<Boolean> svcOutcome = new ServiceOutcome<>();
        try
        {
            ServiceOutcome<User> user = userService.findByUsername("system");

            UserLoginHistory ulHistory = new UserLoginHistory();

            ulHistory.setBrowserDetails(ClientInfo.getClientBrowser(request));
            ulHistory.setEmail("failedLogin@ormas.in");
            ulHistory.setFirstName("Failed");
            ulHistory.setLastName("Login");
            ulHistory.setLoggedInDate(new Date());
            ulHistory.setLoggedOutDate(null);
            ulHistory.setLoginStatus("FAILED LOGIN");
            ulHistory.setLoginType("NO ROLE");
            ulHistory.setMobile("9876543210");
            ulHistory.setOsDetails(ClientInfo.getClientOS(request));
            ulHistory.setUserName(userName);
            ulHistory.setIpAddress(ClientInfo.getClientIpAddr(request));

            authorizedUser(user.getData());
            ulHistoryReporsitory.save(ulHistory);

            Cookie[] cookies = request.getCookies();
            String[] cookieNames = new String[cookies.length];

            for(int i = 0; i < cookies.length; i++)
            {
                cookieNames[i] = cookies[i].getName();
            }


            CookieClearingLogoutHandler cookieClearingLogoutHandler = new CookieClearingLogoutHandler(cookieNames);
            SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
            cookieClearingLogoutHandler.logout(request, response, null);
            securityContextLogoutHandler.logout(request, response, null);
            svcOutcome.setData(true);
        }
        catch(Exception ex)
        {
            svcOutcome.setOutcome(false);
            svcOutcome.setData(false);
            svcOutcome.setMessage(ex.getMessage());
            log.error("Exception occured in createNoUserLoginHistory in LoginServiceImpl-->",ex);
        }

        return svcOutcome;
    }

    public void authorizedUser(User user) {
        List<UserRoleMap> userRoleMaps = this.userService.findUserRoleMapByUserId(user.getUserId());
        userRoleMaps = (List<UserRoleMap>)userRoleMaps.stream().filter(r -> r.getIsActive().booleanValue()).collect(Collectors.toList());
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        List<Role> lstRoles = new ArrayList<>();
        for (UserRoleMap urm : userRoleMaps) {
            Role role = roleService.findByRoleId(urm.getRoleId()).getData();
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleCode()));
            if (role.getIsActive().booleanValue())
                lstRoles.add(role);
        }
        user.setRoles(lstRoles);
        LoggedInUser userDetails = new LoggedInUser(user.getUserName(), user.getPassword(), true, true, true, true, grantedAuthorities, user.getPrimaryRole(), user);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, grantedAuthorities);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(usernamePasswordAuthenticationToken);
    }

    @Override
    public ServiceOutcome<Boolean> createSuccessLoginHistory(User user, HttpServletRequest request,HttpServletResponse response) {
        ServiceOutcome<Boolean> svcOutcome = new ServiceOutcome<>();
        try
        {
            Boolean result = false;
            UserLoginHistory ulHistory = new UserLoginHistory();

            ulHistory.setBrowserDetails(ClientInfo.getClientBrowser(request));
            ulHistory.setEmail(user.getEmail());
            ulHistory.setFirstName(user.getFirstName());
            ulHistory.setLastName(user.getLastName());
            ulHistory.setLoggedInDate(new Date());
            ulHistory.setLoggedOutDate(null);
            ulHistory.setLoginStatus("LOGIN_SUCCESS");
            ulHistory.setLoginType(user.getPrimaryRole().getRoleCode());
            ulHistory.setMobile(user.getMobile());
            ulHistory.setOsDetails(ClientInfo.getClientOS(request));
            ulHistory.setUserName(user.getUserName());
            ulHistory.setUserId(user.getUserId());
            ulHistory.setIpAddress(ClientInfo.getClientIpAddr(request));

            if(SecurityHelper.getCurrentUser()== null) {
                ServiceOutcome<User> user1 = userService.findByUsername("system");
                authorizedUser(user1.getData());
                result=true;
            }

            ulHistoryReporsitory.save(ulHistory);

            if(result) {
                Cookie[] cookies = request.getCookies();
                String[] cookieNames = new String[cookies.length];

                for(int i = 0; i < cookies.length; i++)
                {
                    cookieNames[i] = cookies[i].getName();
                }


                CookieClearingLogoutHandler cookieClearingLogoutHandler = new CookieClearingLogoutHandler(cookieNames);
                SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
                cookieClearingLogoutHandler.logout(request, response, null);
                securityContextLogoutHandler.logout(request, response, null);
            }

            svcOutcome.setData(true);
        }
        catch(Exception ex)
        {
            svcOutcome.setOutcome(false);
            svcOutcome.setData(false);
            svcOutcome.setMessage(ex.getMessage());
            log.error("Exception occured in createFailedLoginHistory in LoginServiceImpl-->",ex);
        }

        return svcOutcome;
    }

    @Override
    public Boolean isFirstLogin() {
        Boolean flag=false;
        try {
            Long userId = SecurityHelper.getCurrentUser().getUserId();

            List<UserLoginHistory> findAll = ulHistoryReporsitory.findAll();
            List<UserLoginHistory> collect = findAll.stream()
                    .filter(e -> e.getUserId() != null)   // skip null userId
                    .filter(e -> e.getUserId().equals(userId))
                    .filter(e -> "LOGIN_SUCCESS".equals(e.getLoginStatus()))
                    .collect(Collectors.toList());

            if (collect == null || collect.size() <= 1 || passwordEncoder.matches("123456", SecurityHelper.getCurrentUser().getPassword())	) {
                flag = true;
            }

        } catch (Exception e) {
            log.error("Error occured while forward",e);
        }
        return flag;
    }
}
