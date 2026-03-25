package com.finsecure.wallet.repository;

import com.finsecure.wallet.model.UserRoleMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRoleMapRepository extends JpaRepository<UserRoleMap, Long> {
    UserRoleMap findByUserIdAndRoleId(Long userId, Long roleId);

    List<UserRoleMap> findByUserId(Long userId);

    @Query("From UserRoleMap Where roleId=:roleId")
    List<UserRoleMap> findUserRoleMapByRoleId(@Param("roleId") Long roleId);

    List<UserRoleMap> findByRoleId(Long roleId);
}
