package com.finsecure.wallet.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.finsecure.wallet.common.ServiceOutcome;
import com.finsecure.wallet.model.Role;
import com.finsecure.wallet.model.User;
import com.finsecure.wallet.repository.RoleRepository;
import com.finsecure.wallet.utils.LocaleSpecificSorter;
import com.finsecure.wallet.utils.SecurityHelper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService, MessageSourceAware {
    private final Logger log = Logger.getLogger(this.getClass());
    private MessageSource messageSource;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private RoleRepository roleRepository;

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public ServiceOutcome<List<Role>> getAllRoles(Boolean includeInactive) {
        ServiceOutcome svcOutcome = new ServiceOutcome();

        try {
            List<Role> lstRoles = null;
            if (!includeInactive) {
                lstRoles = this.roleRepository.findByIsActiveOrderByDisplayNameAsc(true);
            } else {
                lstRoles = this.roleRepository.findAll();
            }

            lstRoles = (List)lstRoles.stream().sorted((first, second) -> {
                return first.getDisplayName().compareTo(second.getDisplayName());
            }).collect(Collectors.toList());
            svcOutcome.setData(lstRoles);
        } catch (Exception var4) {
            this.log.error(var4);
            svcOutcome.setData((Object)null);
            svcOutcome.setOutcome(false);
            svcOutcome.setMessage(this.messageSource.getMessage("msg.error", (Object[])null, LocaleContextHolder.getLocale()));
        }

        return svcOutcome;
    }

    public ServiceOutcome<List<Role>> getRoleForUser(Long userId) {
        ServiceOutcome svcOutcome = new ServiceOutcome();

        try {
            List<Role> lstRoles = null;
            StringBuilder query = new StringBuilder();
            query.append("select r.*  ");
            query.append("from t_umt_role r ");
            query.append("join t_umt_user_role_map urm ");
            query.append("  on urm.role_id = r.role_id ");
            query.append("  and urm.user_id = :userId ");
            Query rs = this.entityManager.createNativeQuery(query.toString(), Role.class);
            rs.setParameter("userId", userId);
            lstRoles = rs.getResultList();
            lstRoles = (new LocaleSpecificSorter(Role.class)).sort(lstRoles);
            svcOutcome.setData(lstRoles);
        } catch (Exception var6) {
            this.log.error(var6);
            svcOutcome.setData((Object)null);
            svcOutcome.setOutcome(false);
            svcOutcome.setMessage(this.messageSource.getMessage("msg.error", (Object[])null, LocaleContextHolder.getLocale()));
        }

        return svcOutcome;
    }

    public ServiceOutcome<Role> getRoleByCode(String roleCode) {
        ServiceOutcome svcOutcome = new ServiceOutcome();

        try {
            Role role = this.roleRepository.findByRoleCode(roleCode);
            svcOutcome.setData(role);
        } catch (Exception var4) {
            this.log.error(var4);
            svcOutcome.setData((Object)null);
            svcOutcome.setOutcome(false);
            svcOutcome.setMessage(this.messageSource.getMessage("msg.error", (Object[])null, LocaleContextHolder.getLocale()));
        }

        return svcOutcome;
    }

    public ServiceOutcome<Role> findByRoleId(Long roleId) {
        ServiceOutcome svcOutcome = new ServiceOutcome();

        try {
            Role role = (Role)this.roleRepository.findById(roleId).get();
            svcOutcome.setData(role);
        } catch (Exception var4) {
            this.log.error(var4);
            svcOutcome.setData((Object)null);
            svcOutcome.setOutcome(false);
            svcOutcome.setMessage(this.messageSource.getMessage("msg.error", (Object[])null, LocaleContextHolder.getLocale()));
        }

        return svcOutcome;
    }

    public ServiceOutcome<Role> lockNUnlockRoleById(Long roleId, Boolean isActive) {
        ServiceOutcome serviceOutcome = new ServiceOutcome();

        try {
            if (roleId != null) {
                Role role = (Role)this.roleRepository.findById(roleId).get();
                if (isActive) {
                    role.setIsActive(isActive);
                    serviceOutcome.setMessage(this.messageSource.getMessage("msg.success", (Object[])null, "role unlocked successfully", LocaleContextHolder.getLocale()));
                }

                if (!isActive) {
                    role.setIsActive(isActive);
                    serviceOutcome.setMessage(this.messageSource.getMessage("msg.success", (Object[])null, "role locked successfully", LocaleContextHolder.getLocale()));
                }

                role = (Role)this.roleRepository.save(role);
                serviceOutcome.setData(role);
            } else {
                serviceOutcome.setMessage(this.messageSource.getMessage("msg.error", (Object[])null, LocaleContextHolder.getLocale()));
                serviceOutcome.setData((Object)null);
                serviceOutcome.setOutcome(false);
            }
        } catch (Exception var5) {
            this.log.error(var5);
            serviceOutcome.setData((Object)null);
            serviceOutcome.setOutcome(false);
            serviceOutcome.setMessage(this.messageSource.getMessage("msg.error", (Object[])null, LocaleContextHolder.getLocale()));
        }

        return serviceOutcome;
    }

    public ServiceOutcome<Role> addNupdateRole(Role role) {
        ServiceOutcome outcome = new ServiceOutcome();

        try {
            User user = SecurityHelper.getCurrentUser();
            if (role.getRoleId() != null) {
                Role prvRole = (Role)this.roleRepository.findById(role.getRoleId()).get();
                prvRole.setLastUpdatedOn(new Date());
                prvRole.setLastUpdatedBy(user);
                prvRole.setDescription(role.getDescription());
                prvRole.setRoleCode(role.getRoleCode());
                prvRole.setDisplayName(role.getDisplayName());
                prvRole.setMaxAssignments(role.getMaxAssignments());
                prvRole = (Role)this.roleRepository.save(prvRole);
                outcome.setData(prvRole);
                outcome.setMessage(this.messageSource.getMessage("msg.success", (Object[])null, "Role updated successfully", LocaleContextHolder.getLocale()));
            } else {
                role.setCreatedBy(user);
                role.setCreatedOn(new Date());
                role.setIsActive(true);
                role.setDisplayOnPage(true);
                role = (Role)this.roleRepository.save(role);
                outcome.setData(role);
                outcome.setMessage(this.messageSource.getMessage("msg.success", (Object[])null, "Role saved successfully", LocaleContextHolder.getLocale()));
            }
        } catch (Exception var5) {
            this.log.error(var5);
            outcome.setData((Object)null);
            outcome.setOutcome(false);
            outcome.setMessage(this.messageSource.getMessage("msg.error", (Object[])null, LocaleContextHolder.getLocale()));
        }

        return outcome;
    }

    public ServiceOutcome<List<Role>> findRoleList() {
        ServiceOutcome serviceOutcome = new ServiceOutcome();

        try {
            List<Role> roles = this.roleRepository.findAll();
            serviceOutcome.setData(roles);
        } catch (Exception var3) {
            this.log.error(var3);
            serviceOutcome.setData((Object)null);
            serviceOutcome.setOutcome(false);
            serviceOutcome.setMessage(this.messageSource.getMessage("msg.error", (Object[])null, LocaleContextHolder.getLocale()));
        }

        return serviceOutcome;
    }

    public ServiceOutcome<List<Role>> getRolePagedList() {
        ServiceOutcome serviceOutcome = new ServiceOutcome();

        try {
            List<Role> userList = this.roleRepository.findAll();
            serviceOutcome.setData(userList);
        } catch (Exception var3) {
            this.log.error(var3.getMessage());
            serviceOutcome.setData((Object)null);
            serviceOutcome.setOutcome(false);
            serviceOutcome.setMessage(this.messageSource.getMessage("msg.error", (Object[])null, LocaleContextHolder.getLocale()));
        }

        return serviceOutcome;
    }

    public ServiceOutcome<Role> save(Role role) {
        return this.addNupdateRole(role);
    }

    public ServiceOutcome<Role> delete(Long roleId) {
        return this.lockNUnlockRoleById(roleId, true);
    }
}
