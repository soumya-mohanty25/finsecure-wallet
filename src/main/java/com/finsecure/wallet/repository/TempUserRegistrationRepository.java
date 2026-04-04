package com.finsecure.wallet.repository;

import com.finsecure.wallet.model.TempUserRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TempUserRegistrationRepository extends JpaRepository<TempUserRegistration, Long> {

    TempUserRegistration findByPhoneNumber(String phoneNumber);

}
