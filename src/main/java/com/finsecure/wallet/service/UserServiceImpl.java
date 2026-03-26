package com.finsecure.wallet.service;

import com.finsecure.wallet.common.ServiceOutcome;
import com.finsecure.wallet.model.Role;
import com.finsecure.wallet.model.User;
import com.finsecure.wallet.model.UserRoleMap;
import com.finsecure.wallet.repository.RoleRepository;
import com.finsecure.wallet.repository.UserRepository;
import com.finsecure.wallet.repository.UserRoleMapRepository;
import com.finsecure.wallet.utils.SecurityHelper;
import com.finsecure.wallet.utils.UploadFile;
import com.finsecure.wallet.utils.UserSpecification;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService, MessageSourceAware {
    private MessageSource messageSource;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSpecification userSpecification;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleMapRepository userRoleMapRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public ServiceOutcome<User> save(User user) {
        ServiceOutcome svcOutcome = new ServiceOutcome();

        try {
            user = (User)this.userRepository.saveAndFlush(user);
            svcOutcome.setData(user);
        } catch (Exception var4) {
            this.log.error(String.valueOf(var4));
            svcOutcome.setData((Object)null);
            svcOutcome.setOutcome(false);
            svcOutcome.setMessage(this.messageSource.getMessage("msg.error", (Object[])null, LocaleContextHolder.getLocale()));
        }

        return svcOutcome;
    }

    public ServiceOutcome<User> findByUsername(String userName) {
        ServiceOutcome svcOutcome = new ServiceOutcome();

        try {
            User user = this.userRepository.findByUserName(userName);
            svcOutcome.setData(user);
        } catch (Exception var4) {
            this.log.error(String.valueOf(var4));
            svcOutcome.setData((Object)null);
            svcOutcome.setOutcome(false);
            svcOutcome.setMessage(this.messageSource.getMessage("msg.error", (Object[])null, LocaleContextHolder.getLocale()));
        }

        return svcOutcome;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public ServiceOutcome<User> findByUserId(Long userId) {
        ServiceOutcome svcOutcome = new ServiceOutcome();

        try {
            User user = this.userRepository.findByUserId(userId);
            svcOutcome.setData(user);
        } catch (Exception var4) {
            this.log.error(String.valueOf(var4));
            svcOutcome.setData((Object)null);
            svcOutcome.setOutcome(false);
            svcOutcome.setMessage(this.messageSource.getMessage("msg.error", (Object[])null, LocaleContextHolder.getLocale()));
        }

        return svcOutcome;
    }

    public ServiceOutcome<Page<User>> findUserList(String searchTerm, PageRequest pageRequest) {
        ServiceOutcome serviceOutcome = new ServiceOutcome();

        try {
            Page<User> userList = null;
            if (searchTerm != null && !searchTerm.equals("")) {
                userList = this.userRepository.findAll(this.userSpecification.searchUser(searchTerm), pageRequest);
            } else {
                userList = this.userRepository.findAll(pageRequest);
            }

            serviceOutcome.setData(userList);
        } catch (Exception var5) {
            this.log.error(String.valueOf(var5));
            serviceOutcome.setData((Object)null);
            serviceOutcome.setOutcome(false);
            serviceOutcome.setMessage(this.messageSource.getMessage("msg.error", (Object[])null, LocaleContextHolder.getLocale()));
        }

        return serviceOutcome;
    }

    public ServiceOutcome<User> lockNUnlockUserById(Long userId, Boolean isActive) {
        ServiceOutcome serviceOutcome = new ServiceOutcome();

        try {
            if (userId != null) {
                User user = this.userRepository.findByUserId(userId);
                if (isActive) {
                    user.setIsActive(isActive);
                    serviceOutcome.setMessage(this.messageSource.getMessage("msg.success", (Object[])null, "User unlocked successfully", LocaleContextHolder.getLocale()));
                }

                if (!isActive) {
                    user.setIsActive(isActive);
                    serviceOutcome.setMessage(this.messageSource.getMessage("msg.success", (Object[])null, "User locked successfully", LocaleContextHolder.getLocale()));
                }

                user.setIsLocked(!isActive);
                user = (User)this.userRepository.save(user);
                serviceOutcome.setData(user);
            } else {
                serviceOutcome.setMessage(this.messageSource.getMessage("msg.error", (Object[])null, LocaleContextHolder.getLocale()));
                serviceOutcome.setData((Object)null);
                serviceOutcome.setOutcome(false);
            }
        } catch (Exception var5) {
            this.log.error(String.valueOf(var5));
            serviceOutcome.setData((Object)null);
            serviceOutcome.setOutcome(false);
            serviceOutcome.setMessage(this.messageSource.getMessage("msg.error", (Object[])null, LocaleContextHolder.getLocale()));
        }

        return serviceOutcome;
    }

    public ServiceOutcome<List<User>> searchUser(String name) {
        ServiceOutcome svcOutcome = new ServiceOutcome();

        try {
            List<User> lstUsers = this.userRepository.searchForAutocomplete(name.toLowerCase());
            lstUsers.stream().forEach((u) -> {
                u.setCreatedBy((User)null);
                u.setLastUpdatedBy((User)null);
                u.getPrimaryRole().setCreatedBy((User) null);
                u.getPrimaryRole().setLastUpdatedBy((User) null);
            });
            svcOutcome.setData(lstUsers);
        } catch (Exception var4) {
            this.log.error(String.valueOf(var4));
            svcOutcome.setData((Object)null);
            svcOutcome.setOutcome(false);
            svcOutcome.setMessage(this.messageSource.getMessage("msg.error", (Object[])null, LocaleContextHolder.getLocale()));
        }

        return svcOutcome;
    }

    public List<Role> findActiveRole() {
        List rolelist = null;

        try {
            rolelist = this.roleRepository.findUIRoles();
        } catch (Exception var3) {
            this.log.error(var3.getMessage());
        }

        return rolelist;
    }

    public ServiceOutcome<User> updateUser(Long userId, String userName, String firstName, String lastName, Date dateOfBirth, String mobile, String email, Long[] userRoleHcMapId, Long[] roleId, Long[] isPrimary, String[] status, String designation) {
        ServiceOutcome outcome = new ServiceOutcome();

        try {
            User curUser = SecurityHelper.getCurrentUser();
            User prvUserDtls = (User)this.userRepository.findById(userId).get();
            prvUserDtls.setFirstName(firstName);
            prvUserDtls.setLastName(lastName);
            prvUserDtls.setDateOfBirth(dateOfBirth);
            prvUserDtls.setUserName(userName);
            prvUserDtls.setMobile(mobile);
            prvUserDtls.setEmail(email);
            prvUserDtls.setLastUpdatedBy(curUser);
            prvUserDtls.setDesignation(designation);
            prvUserDtls = (User)this.userRepository.save(prvUserDtls);

            for(int i = 0; i < roleId.length; ++i) {
                UserRoleMap userRoleMap = null;
                if (userRoleHcMapId[i] != 0L) {
                    userRoleMap = (UserRoleMap)this.userRoleMapRepository.findById(userRoleHcMapId[i]).get();
                    userRoleMap.setRoleId(roleId[i]);
                    userRoleMap.setUserId(prvUserDtls.getUserId());
                    userRoleMap.setLastUpdatedBy(curUser);
                    userRoleMap.setLastUpdatedOn(new Date());
                    if (status[i].equals("0")) {
                        userRoleMap.setIsActive(false);
                    } else {
                        userRoleMap.setIsActive(true);
                    }

                    this.userRoleMapRepository.save(userRoleMap);
                } else {
                    StringBuffer sb = new StringBuffer();
                    List<Role> lstUIRoles = this.roleRepository.findByDisplayOnPage(true);
                    this.log.debug("Pass Step 1");
                    Long currRoleId = roleId[i];
                    this.log.debug("Current Role Id from UI is " + currRoleId);
                    Integer currentAllocations = this.userRoleMapRepository.findByRoleId(roleId[i]).size();
                    this.log.debug(String.valueOf(currentAllocations));
                    this.log.debug("-- 2.1");
                    if (currentAllocations == null) {
                        currentAllocations = 0;
                    }

                    Role theRole = (Role)lstUIRoles.stream().filter((p) -> {
                        return p.getRoleId() == currRoleId;
                    }).findAny().orElse((Role) null);
                    if (theRole != null) {
                        this.log.debug("-- 2.2");
                        if (theRole.getMaxAssignments() == -1L) {
                            this.log.debug("-- No limit on assignments");
                            userRoleMap = new UserRoleMap();
                            userRoleMap.setUserId(prvUserDtls.getUserId());
                            userRoleMap.setRoleId(roleId[i]);
                            userRoleMap.setIsActive(true);
                            this.log.debug("-- 2.4.a");
                            this.userRoleMapRepository.save(userRoleMap);
                            this.log.debug("-- 2.5.a");
                            if (isPrimary[i] == 1L) {
                                this.log.debug("-- 2.6.a");
                                prvUserDtls.setPrimaryRole(theRole);
                                prvUserDtls = (User)this.userRepository.save(prvUserDtls);
                                this.log.debug("-- 2.7.a");
                            }
                        } else {
                            this.log.debug("-- Limit on assignments : " + theRole.getMaxAssignments());
                            if ((long)currentAllocations < theRole.getMaxAssignments()) {
                                this.log.debug("-- 2.3");
                                userRoleMap = new UserRoleMap();
                                userRoleMap.setUserId(prvUserDtls.getUserId());
                                userRoleMap.setRoleId(roleId[i]);
                                userRoleMap.setIsActive(true);
                                this.log.debug("-- 2.4");
                                this.userRoleMapRepository.save(userRoleMap);
                                this.log.debug("-- 2.5");
                                if (isPrimary[i] == 1L) {
                                    this.log.debug("-- 2.6");
                                    prvUserDtls.setPrimaryRole(theRole);
                                    prvUserDtls = (User)this.userRepository.save(prvUserDtls);
                                    this.log.debug("-- 2.7");
                                }
                            } else {
                                this.log.debug("-- 3.1");
                                sb.append("The Role ").append(theRole.getDisplayName()).append(" could not be allocated as the maximum allocations for this role ").append(theRole.getMaxAssignments()).append(" have been met. <br/>");
                                this.log.error(sb.toString());
                            }
                        }
                    } else {
                        this.log.debug("-- 4.1");
                        sb.append("A Role could cannot be allocated from the UI. <br/> ");
                        this.log.error(sb.toString());
                    }
                }
            }

            outcome.setData(prvUserDtls);
            outcome.setMessage(this.messageSource.getMessage("msg.success", (Object[])null, "User Data Updated Successfully", LocaleContextHolder.getLocale()));
        } catch (Exception var23) {
            this.log.error(String.valueOf(var23));
            outcome.setData((Object)null);
            outcome.setOutcome(false);
            outcome.setMessage(this.messageSource.getMessage("msg.error", (Object[])null, LocaleContextHolder.getLocale()));
        }

        return outcome;
    }

    public List<UserRoleMap> findUserRoleMapByUserId(Long userId) {
        return this.userRoleMapRepository.findByUserId(userId);
    }

    @Transactional
    public ServiceOutcome<User> addUser(String username, String firstname, String lastname, Date dateOfbirth, String userMobile, String userEmail, Long[] roleId, Long[] isPrimary, String designation) {
        ServiceOutcome outcome = new ServiceOutcome();

        try {
            User currUser = SecurityHelper.getCurrentUser();
            String password = "123456";
            User userDtls = new User();
            userDtls.setFirstName(firstname);
            userDtls.setLastName(lastname);
            userDtls.setUserName(username);
            userDtls.setDateOfBirth(dateOfbirth);
            userDtls.setMobile(userMobile);
            userDtls.setEmail(userEmail);
            userDtls.setPassword(this.bCryptPasswordEncoder.encode(password));
            userDtls.setDesignation(designation);
            userDtls.setIsActive(true);
            userDtls.setIsEnabled(true);
            userDtls.setIsLocked(false);
            userDtls.setIsLoggedIn(false);
            userDtls.setWrongLoginCount(0);
            userDtls.setAllowMultipleSession(false);
            userDtls.setCreatedOn(new Date());
            userDtls.setCreatedBy(currUser);
            userDtls.setLastUpdatedOn(new Date());
            userDtls.setLastUpdatedBy(currUser);
            userDtls = (User)this.userRepository.save(userDtls);
            this.log.debug("Saved User in DB");
            StringBuffer sb = new StringBuffer();
            List<Role> lstUIRoles = this.roleRepository.findByDisplayOnPage(true);
            this.log.debug("Pass Step 1");

            for(int i = 0; i < roleId.length; ++i) {
                Long currRoleId = roleId[i];
                this.log.debug("Current Role Id from UI is " + currRoleId);
                Integer currentAllocations = this.userRoleMapRepository.findByRoleId(roleId[i]).size();
                this.log.debug(String.valueOf(currentAllocations));
                this.log.debug("-- 2.1");
                if (currentAllocations == null) {
                    currentAllocations = 0;
                }

                Role theRole = (Role)lstUIRoles.stream().filter((p) -> {
                    return p.getRoleId() == currRoleId;
                }).findAny().orElse((Role) null);
                if (theRole != null) {
                    this.log.debug("-- 2.2");
                    UserRoleMap userRoleMap;
                    if (theRole.getMaxAssignments() == -1L) {
                        this.log.debug("-- No limit on assignments");
                        userRoleMap = new UserRoleMap();
                        userRoleMap.setUserId(userDtls.getUserId());
                        userRoleMap.setRoleId(roleId[i]);
                        userRoleMap.setIsActive(true);
                        this.log.debug("-- 2.4.a");
                        this.userRoleMapRepository.save(userRoleMap);
                        this.log.debug("-- 2.5.a");
                        if (isPrimary[i] == 1L) {
                            this.log.debug("-- 2.6.a");
                            userDtls.setPrimaryRole(theRole);
                            userDtls = (User)this.userRepository.save(userDtls);
                            this.log.debug("-- 2.7.a");
                        }
                    } else {
                        this.log.debug("-- Limit on assignments : " + theRole.getMaxAssignments());
                        if ((long)currentAllocations < theRole.getMaxAssignments()) {
                            this.log.debug("-- 2.3");
                            userRoleMap = new UserRoleMap();
                            userRoleMap.setUserId(userDtls.getUserId());
                            userRoleMap.setRoleId(roleId[i]);
                            userRoleMap.setIsActive(true);
                            this.log.debug("-- 2.4");
                            this.userRoleMapRepository.save(userRoleMap);
                            this.log.debug("-- 2.5");
                            if (isPrimary[i] == 1L) {
                                this.log.debug("-- 2.6");
                                userDtls.setPrimaryRole(theRole);
                                userDtls = (User)this.userRepository.save(userDtls);
                                this.log.debug("-- 2.7");
                            }
                        } else {
                            this.log.debug("-- 3.1");
                            sb.append("The Role ").append(theRole.getDisplayName()).append(" could not be allocated as the maximum allocations for this role ").append(theRole.getMaxAssignments()).append(" have been met. <br/>");
                            this.log.error(sb.toString());
                        }
                    }
                } else {
                    this.log.debug("-- 4.1");
                    sb.append("A Role could cannot be allocated from the UI. <br/> ");
                    this.log.error(sb.toString());
                }
            }

            this.log.debug("-- 5.1");
            sb.append(this.messageSource.getMessage("msg.success", (Object[])null, "User Data Saved Successfully", LocaleContextHolder.getLocale()));
            this.log.debug("-- 5.2");
            outcome.setMessage(sb.toString());
            outcome.setData(userDtls);
        } catch (Exception var21) {
            this.log.error(String.valueOf(var21));
            outcome.setData((Object)null);
            outcome.setOutcome(false);
            outcome.setMessage(this.messageSource.getMessage("msg.error", (Object[])null, LocaleContextHolder.getLocale()));
        }

        return outcome;
    }

    public ServiceOutcome<User> userRegistration(User user) {
        ServiceOutcome outcome = new ServiceOutcome();

        try {
            User currUser = SecurityHelper.getCurrentUser();
            User userDtls = new User();
            userDtls.setFirstName(user.getFirstName());
            userDtls.setLastName(user.getLastName());
            userDtls.setUserName(user.getUserName());
            userDtls.setMobile(user.getMobile());
            userDtls.setEmail(user.getEmail());
            userDtls.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
            userDtls.setCreatedOn(new Date());
            userDtls.setCreatedBy(currUser);
            userDtls.setLastUpdatedOn(new Date());
            userDtls.setLastUpdatedBy(currUser);
            userDtls.setIsActive(true);
            userDtls.setPrimaryRole(this.roleRepository.findByRoleCode("ROLE_PUBLIC"));
            userDtls.setDesignation(user.getDesignation());
            userDtls = (User)this.userRepository.save(userDtls);
            outcome.setData(userDtls);
            outcome.setMessage(this.messageSource.getMessage("msg.success", (Object[])null, LocaleContextHolder.getLocale()));
        } catch (Exception var5) {
            this.log.error(String.valueOf(var5));
            outcome.setData((Object)null);
            outcome.setOutcome(false);
            outcome.setMessage(this.messageSource.getMessage("msg.error", (Object[])null, LocaleContextHolder.getLocale()));
        }

        return outcome;
    }

    public ServiceOutcome<List<User>> getUsersByLevelAndId(Long roleLevelId, Long entityId) {
        ServiceOutcome svcOutcome = new ServiceOutcome();

        try {
            List<User> lstUsers = this.userRepository.findUserByLevelAndId(roleLevelId, entityId);
            lstUsers.stream().forEach((u) -> {
                u.setCreatedBy((User)null);
                u.setLastUpdatedBy((User)null);
                u.getPrimaryRole().setCreatedBy((User) null);
                u.getPrimaryRole().setLastUpdatedBy((User) null);
            });
            svcOutcome.setData(lstUsers);
        } catch (Exception var5) {
            this.log.error(String.valueOf(var5));
            svcOutcome.setData((Object)null);
            svcOutcome.setOutcome(false);
            svcOutcome.setMessage(this.messageSource.getMessage("msg.error", (Object[])null, LocaleContextHolder.getLocale()));
        }

        return svcOutcome;
    }

    public ServiceOutcome<List<User>> getAllUsers() {
        ServiceOutcome svcOutcome = new ServiceOutcome();

        try {
            List<User> lstUsers = this.userRepository.findAll();
            svcOutcome.setData(lstUsers);
            svcOutcome.setOutcome(true);
        } catch (Exception var3) {
            this.log.error(String.valueOf(var3));
            svcOutcome.setData((Object)null);
            svcOutcome.setOutcome(false);
            svcOutcome.setMessage(this.messageSource.getMessage("msg.error", (Object[])null, LocaleContextHolder.getLocale()));
        }

        return svcOutcome;
    }

    public ServiceOutcome<User> updateProfile(User user, MultipartFile userProfileImage) {
        ServiceOutcome<User> outcome = new ServiceOutcome();
        User curUser = SecurityHelper.getCurrentUser();

        try {
            User userDtls = (User)this.userRepository.findById(curUser.getUserId()).get();
            userDtls.setFirstName(user.getFirstName());
            userDtls.setLastName(user.getLastName());
            userDtls.setUserName(user.getUserName());
            userDtls.setMobile(user.getMobile());
            userDtls.setEmail(user.getEmail());
            userDtls.setLastUpdatedOn(new Date());
            userDtls.setDateOfBirth(user.getDateOfBirth());
            userDtls.setDesignation(user.getDesignation());
            if (!userProfileImage.isEmpty()) {
                userDtls.setProfilePhoto(UploadFile.upload(userProfileImage, "profile picture", userDtls.getUserName(), "umt"));
            }

            userDtls.setLastUpdatedOn(new Date());
            userDtls.setLastUpdatedBy(curUser);
            userDtls = (User)this.userRepository.save(userDtls);
            outcome.setData(userDtls);
            outcome.setMessage(this.messageSource.getMessage("msg.success", (Object[])null, LocaleContextHolder.getLocale()));
        } catch (Exception var6) {
            this.log.error(String.valueOf(var6));
            outcome.setData((User) null);
            outcome.setOutcome(false);
            outcome.setMessage(this.messageSource.getMessage("msg.error", (Object[])null, LocaleContextHolder.getLocale()));
        }

        return outcome;
    }

    public Boolean saveResetPassword(Long userId, String txtRePass) {
        Boolean status = false;

        try {
            User user = (User)this.userRepository.findById(userId).get();
            user.setPassword(this.bCryptPasswordEncoder.encode(txtRePass));
            this.userRepository.save(user);
            status = true;
        } catch (Exception var5) {
            this.log.error(String.valueOf(var5));
            status = false;
        }

        return status;
    }

//    public ServiceOutcome<Boolean> createLoginHistory(User user, HttpServletRequest request) {
//        ServiceOutcome svcOutcome = new ServiceOutcome();
//
//        try {
//            UserLoginHistory ulHistory = new UserLoginHistory();
//            ulHistory.setBrowserDetails(ClientInfo.getClientBrowser(request));
//            ulHistory.setEmail(user.getEmail());
//            ulHistory.setFirstName(user.getFirstName());
//            ulHistory.setLastName(user.getLastName());
//            ulHistory.setLoggedInDate(new Date());
//            ulHistory.setLoggedOutDate((Date)null);
//            ulHistory.setLoginStatus("LOGIN");
//            ulHistory.setLoginType(user.getPrimaryRole().getRoleCode());
//            ulHistory.setMobile(user.getMobile());
//            ulHistory.setOsDetails(ClientInfo.getClientOS(request));
//            ulHistory.setUserName(user.getUserName());
//            ulHistory.setUserId(user.getUserId());
//            ulHistory.setIpAddress(ClientInfo.getClientIpAddr(request));
//            this.ulHistoryReporsitory.save(ulHistory);
//            svcOutcome.setData(true);
//        } catch (Exception var5) {
//            svcOutcome.setOutcome(false);
//            svcOutcome.setData(false);
//            svcOutcome.setMessage(var5.getMessage());
//        }
//
//        return svcOutcome;
//    }

//    public ServiceOutcome<Boolean> createLogoutHistory(User user, HttpServletRequest request) {
//        ServiceOutcome svcOutcome = new ServiceOutcome();
//
//        try {
//            UserLoginHistory ulHistory = new UserLoginHistory();
//            ulHistory.setBrowserDetails(ClientInfo.getClientBrowser(request));
//            ulHistory.setEmail(user.getEmail());
//            ulHistory.setFirstName(user.getFirstName());
//            ulHistory.setLastName(user.getLastName());
//            ulHistory.setLoggedInDate(new Date());
//            ulHistory.setLoggedOutDate((Date)null);
//            ulHistory.setLoginStatus("LOGOUT");
//            ulHistory.setLoginType(user.getPrimaryRole().getRoleCode());
//            ulHistory.setMobile(user.getMobile());
//            ulHistory.setOsDetails(ClientInfo.getClientOS(request));
//            ulHistory.setUserName(user.getUserName());
//            ulHistory.setUserId(user.getUserId());
//            this.ulHistoryReporsitory.save(ulHistory);
//            svcOutcome.setData(true);
//        } catch (Exception var5) {
//            svcOutcome.setOutcome(false);
//            svcOutcome.setData(false);
//            svcOutcome.setMessage(var5.getMessage());
//        }
//
//        return svcOutcome;
//    }
}
