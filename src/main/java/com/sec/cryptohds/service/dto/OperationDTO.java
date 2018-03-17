package com.sec.cryptohds.service.dto;

import com.sec.cryptohds.domain.OperationType;

import java.util.Date;

public class OperationDTO {

    private Long id;

    private Date timestamp;

    private Long value;

    private Boolean committed;

    private OperationType type;

    private LedgerDTO origin;

    public OperationDTO(LedgerDTO origin, OperationType operationType, Long value) {
        this.origin = origin;
        this.type = operationType;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
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

    public LedgerDTO getOrigin() {
        return origin;
    }

    public void setOrigin(LedgerDTO origin) {
        this.origin = origin;
    }
}
