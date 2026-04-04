package com.finsecure.wallet.repository;

import com.finsecure.wallet.model.UnqIDStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnqIDStoreRepo extends JpaRepository<UnqIDStore,Long> {

    UnqIDStore findByUnqTokenID(String storedAsToken);

    UnqIDStore findByUnqTokenIDAndIpAddress(String storedAsToken, String ipAddress);

    UnqIDStore findByUnqTokenIDAndIpAddressAndIsActiveTrue(String storedAsToken, String ipAddress);

    UnqIDStore findByUserId(Long userId);

}

