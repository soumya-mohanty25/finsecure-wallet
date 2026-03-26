package com.finsecure.wallet.service;

import java.util.Date;
import java.util.List;

import com.finsecure.wallet.common.ServiceOutcome;
import com.finsecure.wallet.model.Role;
import com.finsecure.wallet.model.User;
import com.finsecure.wallet.model.UserRoleMap;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    ServiceOutcome<User> save(User user);

    ServiceOutcome<User> findByUsername(String userName);

    ServiceOutcome<User> findByUserId(Long userId);

    ServiceOutcome<Page<User>> findUserList(String searchTerm, PageRequest pageRequest);

    ServiceOutcome<User> lockNUnlockUserById(Long id, Boolean status);

    ServiceOutcome<List<User>> searchUser(String name);

    List<Role> findActiveRole();

    ServiceOutcome<User> updateUser(Long userId, String userName, String firstName, String lastName, Date dateOfBirth, String mobile, String email, Long[] userRoleHcMapId, Long[] roleName, Long[] isPrimary, String[] status, String designation);

    List<UserRoleMap> findUserRoleMapByUserId(Long userId);

    ServiceOutcome<User> addUser(String username, String firstname, String lastname, Date dateOfbirth, String userMobile, String userEmail, Long[] roleId, Long[] isPrimary, String designation);

    ServiceOutcome<User> userRegistration(User user);

    ServiceOutcome<List<User>> getUsersByLevelAndId(Long roleLevelId, Long entityId);

    ServiceOutcome<List<User>> getAllUsers();

    ServiceOutcome<User> updateProfile(User user, MultipartFile userProfileImage);

    Boolean saveResetPassword(Long userId, String txtRePass);

//    ServiceOutcome<Boolean> createLoginHistory(User user, HttpServletRequest request);
//
//    ServiceOutcome<Boolean> createLogoutHistory(User user, HttpServletRequest request);
}
