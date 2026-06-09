package com.finsecure.wallet.repository;

import com.finsecure.wallet.model.WalletTopup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletTopupRepository extends JpaRepository<WalletTopup, Long> {
}
