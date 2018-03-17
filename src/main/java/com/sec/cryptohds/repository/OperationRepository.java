package com.sec.cryptohds.repository;

import com.sec.cryptohds.domain.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

    Operation findOperationById(Long id);
}