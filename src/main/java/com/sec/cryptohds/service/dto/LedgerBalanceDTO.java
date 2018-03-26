package com.sec.cryptohds.service.dto;

import com.sec.cryptohds.domain.Operation;

import java.util.List;

public class LedgerBalanceDTO {

    private Long balance;

    private List<Operation> pendingOperations;

    public LedgerBalanceDTO(Long balance, List<Operation> pendingOperations) {
        this.balance = balance;
        this.pendingOperations = pendingOperations;
    }

    public LedgerBalanceDTO() {}

    public List<Operation> getPendingOperations() {
        return pendingOperations;
    }

    public void setPendingOperations(List<Operation> pendingOperations) {
        this.pendingOperations = pendingOperations;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }
}
