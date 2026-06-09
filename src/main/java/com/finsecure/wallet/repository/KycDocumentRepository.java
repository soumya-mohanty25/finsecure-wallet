package com.finsecure.wallet.repository;

import com.finsecure.wallet.model.KycDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KycDocumentRepository extends JpaRepository<KycDocument, Long> {
}
