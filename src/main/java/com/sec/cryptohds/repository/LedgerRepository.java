package com.sec.cryptohds.repository;

import com.sec.cryptohds.domain.Ledger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LedgerRepository extends JpaRepository<Ledger, Long> {

    List<Ledger> findLedgerByPublicKey(String publicKey);
}
