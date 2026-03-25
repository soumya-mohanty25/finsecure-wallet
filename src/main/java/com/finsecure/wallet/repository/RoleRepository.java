package com.finsecure.wallet.repository;

import com.finsecure.wallet.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleCode(String roleCode);

    List<Role> findByIsActiveOrderByDisplayNameAsc(Boolean isActive);

    @Query("FROM Role where isActive=true")
    List<Role> findRoleList();

    @Query("FROM Role WHERE displayOnPage = false")
    List<Role> findNonSystemRoles();

    @Query("FROM Role where  roleId=:id")
    Role findByRoleID(@Param("id") Long id);

    List<Role> findByDisplayOnPage(Boolean display);

    @Query("FROM Role where isActive=true and displayOnPage = true order by displayName")
    List<Role> findUIRoles();

    @Query("FROM Role where isActive = true")
    List<Role> findByIsActive(boolean b);
}
