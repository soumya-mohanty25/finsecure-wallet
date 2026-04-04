package com.finsecure.wallet.repository;

import com.finsecure.wallet.model.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenderRepository extends JpaRepository<Gender, Long> {

    List<Gender> findAllByIsActiveTrueOrderByGenderIdAsc();

    Gender findByGenderCode(String genderCode);

}
