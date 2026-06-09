package com.finsecure.wallet.repository;

import com.finsecure.wallet.model.WalletBlockHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletBlockHistoryRepository extends JpaRepository<WalletBlockHistory, Long> {
}
