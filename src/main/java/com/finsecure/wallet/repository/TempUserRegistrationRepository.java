package com.finsecure.wallet.repository;

import com.finsecure.wallet.model.TempUserRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TempUserRegistrationRepository extends JpaRepository<TempUserRegistration, Long> {

    TempUserRegistration findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmailId(String emailId);

    TempUserRegistration findByEmailId(String emailId);
}
