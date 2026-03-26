package com.finsecure.wallet.apiController;

import java.util.List;

import com.finsecure.wallet.common.ServiceOutcome;
import com.finsecure.wallet.model.Role;
import com.finsecure.wallet.service.RoleService;
import org.jboss.logging.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping({"/admin/role"})
public class RoleController {
    private final Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private RoleService roleService;

    @GetMapping(
            path = {"/list"},
            name = "Role List"
    )
    public String userList(Model model) {
        ServiceOutcome<List<Role>> outcome = this.roleService.getAllRoles(true);
        if (outcome.isOutcome()) {
            model.addAttribute("roleList", outcome.getData());
        } else {
            model.addAttribute("error_msg", outcome.getMessage());
        }

        return "site.admin.roleList";
    }

    @GetMapping(
            path = {"/edit/{id}"},
            name = "Edit Role"
    )
    public String userEdit(Model model, @PathVariable("id") Long roleId) {
        ServiceOutcome<Role> outcome = this.roleService.findByRoleId(roleId);
        if (outcome.isOutcome()) {
            model.addAttribute("serviceOutcome", outcome);
        } else {
            model.addAttribute("error_msg", outcome.getMessage());
        }

        return "site.admin.roleEdit";
    }

    @GetMapping(
            path = {"/view/{id}"},
            name = "Role View"
    )
    public String userView(Model model, @PathVariable("id") Long roleId) {
        ServiceOutcome<Role> outcome = this.roleService.findByRoleId(roleId);
        if (outcome.isOutcome()) {
            model.addAttribute("serviceOutcome", outcome);
        } else {
            model.addAttribute("error_msg", outcome.getMessage());
        }

        return "site.admin.roleView";
    }

    @PostMapping(
            path = {"/isActive"},
            name = "Active And InActive Role"
    )
    public String lockNUnlock(RedirectAttributes attr, Boolean isActive, Long roleId) {
        ServiceOutcome<Role> serviceOutcome = this.roleService.lockNUnlockRoleById(roleId, isActive);
        if (serviceOutcome.isOutcome()) {
            attr.addFlashAttribute("success_msg", serviceOutcome.getMessage());
        } else {
            attr.addFlashAttribute("error_msg", serviceOutcome.getMessage());
        }

        return "redirect:/admin/role/list";
    }

    @PostMapping(
            path = {"/addNupdate"},
            name = "Add And Update Role"
    )
    public String userUpdate(RedirectAttributes attr, Role role) {
        ServiceOutcome<Role> serviceOutcome = this.roleService.addNupdateRole(role);
        if (serviceOutcome.isOutcome()) {
            attr.addFlashAttribute("success_msg", serviceOutcome.getMessage());
        } else {
            attr.addFlashAttribute("error_msg", serviceOutcome.getMessage());
        }

        return "redirect:/admin/role/list";
    }

    @GetMapping(
            path = {"/validate-role-code"},
            name = "Validate Role Code"
    )
    @ResponseBody
    public String validateUser(String roleCode) {
        JSONObject jsonObject = new JSONObject();
        ServiceOutcome<Role> roleOutcome = this.roleService.getRoleByCode(roleCode);
        if (roleOutcome.getData() != null) {
            jsonObject.put("isDuplicate", true);
        } else {
            jsonObject.put("isDuplicate", false);
        }

        return jsonObject.toString();
    }
}

