package com.sec.cryptohds.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ledger {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String publicKey;

    private Long balance;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Operation> operations = new ArrayList<>();

    public Ledger() {
        this.balance = 90L;
    }

    public Ledger(String name, String publicKey) {
        this.name = name;
        this.publicKey = publicKey;
        this.balance = 90L;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public void addOperation(Operation operation) {
        this.operations.add(operation);
    }

    public List<Operation> getOperations() {
        return this.operations;
    }

    @Override
    public String toString() {
        return String.format(
                "Ledger[id=%d, " +
                "name=%s]",
                id, name);
    }
}
