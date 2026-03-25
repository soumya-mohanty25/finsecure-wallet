package com.finsecure.wallet.repository;

import com.finsecure.wallet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query("FROM User WHERE LOWER(userName) LIKE %:name% ")
    List<User> getUserDetailsByName(@Param("name") String name);

    @Query("FROM User WHERE primaryRole.roleCode = 'ROLE_ADMIN'")
    List<User> findAllAdmins();

    User findByUserName(String username);

    @Query("FROM User WHERE isLoggedIn = true")
    List<User> getLoggedInUsers();

    User findByUserId(Long userId);

    @Query("FROM User WHERE LOWER(userName) LIKE %:name% and isActive = true")
    List<User> searchForAutocomplete(@Param("name") String name);

    @Query("FROM User WHERE primaryRole.roleCode = :roleCode")
    List<User> findUserByRoleCode(@Param("roleCode") String roleCode);

    @Query(
            value = "select e.* from t_umt_user e join t_umt_user_role_map urm     on urm.user_id = e.user_id  join t_umt_user_role_right_level urrl      on urrl.user_role_id  = urm.user_role_map_id  join t_umt_role_level_map rlm      on rlm.role_level_map_id = urrl.role_level_id      and rlm.role_level_map_id = :roleLevelId where urrl.object_id = :entityId ; ",
            nativeQuery = true
    )
    List<User> findUserByLevelAndId(@Param("roleLevelId") Long roleLevelId, @Param("entityId") Long entityId);
}

