package com.finsecure.wallet.repository;

import com.finsecure.wallet.model.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

    public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {
    }
