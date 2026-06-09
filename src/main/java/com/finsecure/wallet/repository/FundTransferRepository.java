package com.finsecure.wallet.repository;

import com.finsecure.wallet.model.FundTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundTransferRepository extends JpaRepository<FundTransfer, Long> {
}
