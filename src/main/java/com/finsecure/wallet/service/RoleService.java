package com.finsecure.wallet.service;

import com.finsecure.wallet.common.ServiceOutcome;
import com.finsecure.wallet.model.Role;

import java.util.List;

public interface RoleService {
    ServiceOutcome<List<Role>> getAllRoles(Boolean includeInactive);

    ServiceOutcome<List<Role>> getRoleForUser(Long userId);

    ServiceOutcome<Role> getRoleByCode(String roleCode);

    ServiceOutcome<Role> findByRoleId(Long roleId);

    ServiceOutcome<Role> lockNUnlockRoleById(Long roleId, Boolean isActive);

    ServiceOutcome<Role> addNupdateRole(Role role);

    ServiceOutcome<List<Role>> findRoleList();

    ServiceOutcome<List<Role>> getRolePagedList();

    ServiceOutcome<Role> save(Role role);

    ServiceOutcome<Role> delete(Long roleId);
}
