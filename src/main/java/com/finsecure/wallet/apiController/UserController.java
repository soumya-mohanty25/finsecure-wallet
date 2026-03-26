package com.finsecure.wallet.apiController;

import java.util.Date;
import java.util.List;

import com.finsecure.wallet.common.ServiceOutcome;
import com.finsecure.wallet.model.Role;
import com.finsecure.wallet.model.User;
import com.finsecure.wallet.model.UserRoleMap;
import com.finsecure.wallet.service.UserService;
import com.finsecure.wallet.utils.PasswordValidator;
import com.finsecure.wallet.utils.SecurityHelper;
import com.finsecure.wallet.utils.ViewDocuments;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jboss.logging.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController {
    private final Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserService userService;

    @GetMapping(
            path = {"/registration"},
            name = "User List"
    )
    public String userRegistration(Model model) {
        return "site.umt.public.registration";
    }

    @PostMapping(
            path = {"/registration"},
            name = "User Registration"
    )
    public String userRegistration(RedirectAttributes attr, User user) {
        ServiceOutcome<User> userServiceOutcome = this.userService.userRegistration(user);
        if (userServiceOutcome.isOutcome()) {
            attr.addFlashAttribute("success_msg", userServiceOutcome.getMessage());
        } else {
            attr.addFlashAttribute("error_msg", userServiceOutcome.getMessage());
        }

        return "redirect:/public/umt/user/registration";
    }

    @GetMapping(
            path = {"/change/password"},
            name = "Change Password"
    )
    public String changePassword(Model model) {
        User user = SecurityHelper.getCurrentUser();
        ServiceOutcome<User> outcome = this.userService.findByUserId(user.getUserId());
        model.addAttribute("userDetails", outcome.getData());
        return "site.umt.changePassword";
    }

    @PostMapping(
            path = {"/change/password"},
            name = "Change Password"
    )
    public String changePassword(RedirectAttributes attr, HttpSession session, String userId, String txtPass, String txtRePass) {
        try {
            User user = null;
            if (userId != null) {
                user = (User)this.userService.findByUsername(userId).getData();
            }

            boolean chk = false;
            if (txtRePass != null && !txtRePass.trim().isEmpty() && txtPass != null && !txtPass.trim().isEmpty()) {
                chk = PasswordValidator.checkString(txtPass);
                if (chk) {
                    if (txtPass.equals(txtRePass)) {
                        Boolean isSuccess = this.userService.saveResetPassword(user.getUserId(), txtRePass);
                        if (isSuccess) {
                            session.invalidate();
                            return "redirect:/umt/login?logout";
                        } else {
                            return "redirect:/user/change/password";
                        }
                    } else {
                        attr.addFlashAttribute("error_msg", "Password not match with password!");
                        this.log.error("Error Msg : Password not match with password!");
                        return "redirect:/user/change/password";
                    }
                } else {
                    attr.addFlashAttribute("error_msg", "Password not match with password policy !");
                    this.log.error("Error Msg : Password not match with password policy !");
                    return "redirect:/user/change/password";
                }
            } else {
                attr.addFlashAttribute("error_msg", "Password not match with password policy !");
                this.log.error("Error Msg : Password not match with password policy !");
                return "redirect:/user/change/password";
            }
        } catch (Exception var9) {
            this.log.error(var9.getMessage());
            attr.addFlashAttribute("error_msg", "Unable to update profile");
            return "redirect:/user/change/password";
        }
    }

    @PostMapping(
            path = {"/profile/update"},
            name = "Update Profile"
    )
    public String updateProfile(RedirectAttributes attr, User user, MultipartFile userProfileImage) {
        ServiceOutcome<User> userServiceOutcome = this.userService.updateProfile(user, userProfileImage);
        if (userServiceOutcome.isOutcome()) {
            attr.addFlashAttribute("success_msg", userServiceOutcome.getMessage());
        } else {
            attr.addFlashAttribute("error_msg", userServiceOutcome.getMessage());
        }

        return "redirect:/user/profile";
    }

    @GetMapping(
            path = {"/image/view"},
            name = "View User Image"
    )
    public void viewImage(Model model, HttpServletRequest request, HttpServletResponse response) {
        User user = SecurityHelper.getCurrentUser();
        ServiceOutcome<User> userDtls = this.userService.findByUserId(user.getUserId());
        if (((User)userDtls.getData()).getProfilePhoto() != null) {
            ViewDocuments.viewUploadedDocument(((User)userDtls.getData()).getProfilePhoto(), request, response);
        }

    }

    @GetMapping(
            path = {"/list"},
            name = "User List"
    )
    public String userList(Model model, @RequestParam(required = false) String searchTerm, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        if (page == null) {
            page = 0;
        }

        if (size == null) {
            size = 10;
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        ServiceOutcome<Page<User>> outcome = this.userService.findUserList(searchTerm, pageRequest);
        model.addAttribute("userList", outcome.getData());
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "site.umt.userList";
    }

    @PostMapping(
            path = {"/list"},
            name = "Search User List"
    )
    public String postUserList(Model model, @RequestParam(required = false) String searchTerm, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        if (page == null) {
            page = 0;
        }

        if (size == null) {
            size = 10;
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        ServiceOutcome<Page<User>> outcome = this.userService.findUserList(searchTerm, pageRequest);
        model.addAttribute("userList", outcome.getData());
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "site.umt.userList";
    }

    @GetMapping(
            path = {"/profile"},
            name = "User Profile"
    )
    public String userProfile(Model model) {
        User user = SecurityHelper.getCurrentUser();
        ServiceOutcome<User> outcome = this.userService.findByUserId(user.getUserId());
        model.addAttribute("userDetails", outcome.getData());
        return "site.umt.userProfile";
    }

    @GetMapping(
            path = {"/add"},
            name = "Add user"
    )
    public String addUser(Model model) {
        List<Role> roleList = this.userService.findActiveRole();
        model.addAttribute("roleList", roleList);
        return "site.umt.addUser";
    }

    @GetMapping(
            path = {"/edit/{id}"},
            name = "Edit User"
    )
    public String userEdit(Model model, @PathVariable("id") Long userId) {
        ServiceOutcome<User> outcome = this.userService.findByUserId(userId);
        model.addAttribute("serviceOutcome", outcome);
        List<UserRoleMap> userRoleMaps = this.userService.findUserRoleMapByUserId(userId);
        model.addAttribute("userRoleHcMapList", userRoleMaps);
        List<Role> roleList = this.userService.findActiveRole();
        model.addAttribute("roleList", roleList);
        return "site.umt.userEdit";
    }

    @GetMapping(
            path = {"/view/{id}"},
            name = "View User"
    )
    public String userView(Model model, @PathVariable("id") Long userId) {
        ServiceOutcome<User> outcome = this.userService.findByUserId(userId);
        model.addAttribute("serviceOutcome", outcome);
        List<UserRoleMap> userRoleMaps = this.userService.findUserRoleMapByUserId(userId);
        model.addAttribute("userRoleHcMapList", userRoleMaps);
        List<Role> roleList = this.userService.findActiveRole();
        model.addAttribute("roleList", roleList);
        return "site.umt.userView";
    }

    @PostMapping(
            path = {"/isActive"},
            name = "Active And Inactive"
    )
    public String lockNUnlock(RedirectAttributes attr, Long userId, Boolean isActive) {
        ServiceOutcome<User> serviceOutcome = this.userService.lockNUnlockUserById(userId, isActive);
        if (serviceOutcome.isOutcome()) {
            attr.addFlashAttribute("success_msg", serviceOutcome.getMessage());
        } else {
            attr.addFlashAttribute("error_msg", serviceOutcome.getMessage());
        }

        return "redirect:/user/list";
    }

    @PostMapping(
            path = {"/update"},
            name = "Update User"
    )
    public String userUpdate(RedirectAttributes attr, Long userid, String username, String firstname, String lastname, Date dateOfbirth, String userMobile, String userEmail, @RequestParam("roleName[]") Long[] roleName, @RequestParam("isPrimary[]") Long[] isPrimary, @RequestParam(value = "status[]",required = false) String[] status, @RequestParam("userRoleHcMapId[]") Long[] userRoleHcMapId, @RequestParam String designation) {
        ServiceOutcome<User> serviceOutcome = this.userService.updateUser(userid, username, firstname, lastname, dateOfbirth, userMobile, userEmail, userRoleHcMapId, roleName, isPrimary, status, designation);
        if (serviceOutcome.isOutcome()) {
            attr.addFlashAttribute("success_msg", serviceOutcome.getMessage());
            return "redirect:/user/edit/" + ((User)serviceOutcome.getData()).getUserId();
        } else {
            attr.addFlashAttribute("error_msg", serviceOutcome.getMessage());
            return "redirect:/user/add";
        }
    }

    @PostMapping(
            path = {"/add"},
            name = "Add user"
    )
    public String addUser(RedirectAttributes attr, String username, String firstname, String lastname, Date dateOfbirth, String userMobile, String userEmail, @RequestParam("roleName[]") Long[] roleName, @RequestParam("isPrimary[]") Long[] isPrimary, @RequestParam String designation) {
        ServiceOutcome<User> serviceOutcome = this.userService.addUser(username, firstname, lastname, dateOfbirth, userMobile, userEmail, roleName, isPrimary, designation);
        if (serviceOutcome.isOutcome()) {
            attr.addFlashAttribute("success_msg", serviceOutcome.getMessage());
            return "redirect:/user/edit/" + ((User)serviceOutcome.getData()).getUserId();
        } else {
            attr.addFlashAttribute("error_msg", serviceOutcome.getMessage());
            return "redirect:/user/add";
        }
    }

    @GetMapping(
            path = {"/validate-user-name"},
            name = "Validate User Name"
    )
    @ResponseBody
    public String validateUser(String userName) {
        JSONObject jsonObject = new JSONObject();
        ServiceOutcome<User> userOutcome = this.userService.findByUsername(userName.trim());
        if (userOutcome.getData() != null) {
            jsonObject.put("isDuplicate", true);
        } else {
            jsonObject.put("isDuplicate", false);
        }

        return jsonObject.toString();
    }
}

