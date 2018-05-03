package com.sec.cryptohds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sec.cryptohds.domain.Operation;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

    Operation findOperationById(Long id);
}