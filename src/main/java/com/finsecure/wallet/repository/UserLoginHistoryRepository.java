package com.finsecure.wallet.repository;

import com.finsecure.wallet.model.UserLoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface UserLoginHistoryRepository extends JpaRepository<UserLoginHistory, Long> {
    @Query("FROM UserLoginHistory WHERE loggedInDate between :fromDate and :toDate")
    List<UserLoginHistory> findHistoryBetweenLoggedinDate(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
}