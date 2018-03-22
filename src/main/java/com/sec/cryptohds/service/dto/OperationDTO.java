package com.sec.cryptohds.service.dto;

import com.sec.cryptohds.domain.OperationType;

import java.util.Date;

public class OperationDTO {

    private Long id;

    private Date timestamp;

    private Long value;

    private Boolean committed;

    private OperationType type;

    private String originPublicKey;

    private String destinationPublicKey;

    public OperationDTO() {}

    public OperationDTO(String originPublicKey, String destinationPublicKey, Long value) {
        this.originPublicKey = originPublicKey;
        this.destinationPublicKey = destinationPublicKey;
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

    public String getOriginPublicKey() {
        return originPublicKey;
    }

    public void setOriginPublicKey(String originPublicKey) {
        this.originPublicKey = originPublicKey;
    }

    public String getDestinationPublicKey() {
        return destinationPublicKey;
    }

    public void setDestinationPublicKey(String destinationPublicKey) {
        this.destinationPublicKey = destinationPublicKey;
    }
}
