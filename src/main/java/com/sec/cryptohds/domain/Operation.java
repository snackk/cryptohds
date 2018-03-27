package com.sec.cryptohds.domain;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Operation {

    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private LocalDateTime timestamp;

    private Long value;

    private Boolean committed = false;

    private OperationType type;

    private Long originOperationID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ledger_id")
    private Ledger ledger;

    public Operation() {
    }

    public Operation(Ledger ledger, OperationType operationType, Long value) {
        this.ledger = ledger;
        this.type = operationType;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Boolean getCommitted() {
        return committed;
    }

    public void setCommitted(Boolean committed) {
        this.committed = committed;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }


    public Ledger getLedger() {
        return ledger;
    }

    public void setLedger(Ledger ledger) {
        this.ledger = ledger;
    }

    @Override
    public String toString() {
        return String.format(
                "Operation[id=%d]",
                id);
    }

    public Long getOriginOperationID() {
        return originOperationID;
    }

    public void setOriginOperationID(Long originOperationID) {
        this.originOperationID = originOperationID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Operation )) return false;
        return id != null && id.equals(((Operation) o).id);
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }
}
