package com.finsecure.wallet.repository;

import com.finsecure.wallet.model.MailQueued;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MailQueuedRepository extends JpaRepository<MailQueued, Long> {

    @Query("FROM MailQueued WHERE status=:status")
    List<MailQueued> findEmailList(@Param("status") String status);

}