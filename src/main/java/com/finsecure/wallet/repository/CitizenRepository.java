package com.finsecure.wallet.repository;

import com.finsecure.wallet.model.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CitizenRepository extends JpaRepository<Citizen, Long> {

    Citizen findByEmailIdAndIdNotIn(String upperCase, List<Long> ids);

    Citizen findByEmailId(String upperCase);

    Citizen findByIdProofNoAndIdNotIn(String trim, List<Long> ids);

    Citizen findByIdProofNo(String upperCase);

    Citizen findByPhoneNumberAndIdNotIn(String trim, List<Long> ids);

    boolean existsByEmailId(String emailId);

    boolean existsByIdProofNo(String idProofNo);

    boolean existsByphoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String fieldValue);

    Citizen findByPhoneNumber(String phoneNumber);
}
