package com.finsecure.wallet.repository;

import com.finsecure.wallet.model.OtpForUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpForUserRepository extends JpaRepository<OtpForUser, Long> {

    @Query(value="select * from t_gaas_otp_for_user otp where otp.user_id = :userId and otp.is_valid =true order by otp.id desc limit 1",nativeQuery = true)
    OtpForUser findByUserAndisActiveTrueOrderByOtpIdDescLimit1(@Param("userId") Long userId);

    @Modifying
    @Query(value="update t_gaas_otp_for_user set is_valid= :isValid where user_id = :userId",nativeQuery = true)
    void updateRecordsByUserIdAndIsValid(@Param("userId")Long userId,@Param("isValid") Boolean isValid);
}