package com.sec.cryptohds.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Ledger {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Column(length=1000000)
    @Lob
    private String publicKey;

    private Long balance;
    
    private int seqNumber;

    public Ledger() {
        this.balance = 90L;
    }

    public Ledger(String name, String publicKey) {
        this.name = name;
        this.publicKey = publicKey;
        this.balance = 90L;
        this.seqNumber = 0;
    }

    public int getSeqNumber() {
    	return seqNumber;
    }
   
    public void setSeqNumber(int seqNumb) {
    	seqNumber = seqNumb;
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

    @Override
    public String toString() {
        return String.format(
                "Ledger[pubKey=%s, " +
                "id=%d, " +
                "name=%s]",
                publicKey, id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ledger )) return false;
        return id != null && id.equals(((Ledger) o).id);
    }
}
