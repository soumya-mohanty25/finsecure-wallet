package com.finsecure.wallet.repository;

import com.finsecure.wallet.model.MailRecipantDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailRecipantsRepository extends JpaRepository<MailRecipantDetails, Long> {

}
