package com.finsecure.wallet.repository;

import com.finsecure.wallet.model.TransactionLimit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionLimitRepository extends JpaRepository<TransactionLimit, Long> {
}
