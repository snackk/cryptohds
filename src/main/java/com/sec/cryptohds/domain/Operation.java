package com.sec.cryptohds.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Operation {

    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private LocalDateTime timestamp;

    private Long value;

    private Boolean committed = false;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "origin_ledger_id")
	private Ledger originLedger;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "destination_ledger_id")
	private Ledger destinationLedger;

	@Column(length = 1000000)
	@Lob
	private String originSignature;

	@Column(length = 1000000)
	@Lob
	private String destinationSignature;

    public Operation() {
    }

	public Operation(Ledger originLedger, Ledger destinationLedger, Long value) {
		this.originLedger = originLedger;
		this.destinationLedger = destinationLedger;
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

	public Boolean isCommitted() {
        return committed;
    }

    public void setCommitted(Boolean committed) {
        this.committed = committed;
    }

	public Ledger getOriginLedger() {
		return originLedger;
	}

	public void setOriginLedger(Ledger originLedger) {
		this.originLedger = originLedger;
    }

	public Ledger getDestinationLedger() {
		return destinationLedger;
    }

	public void setDestinationLedger(Ledger destinationLedger) {
		this.destinationLedger = destinationLedger;
    }

	public String getOriginSignature() {
		return originSignature;
	}

	public void setOriginSignature(String originSignature) {
		this.originSignature = originSignature;
	}

	public String getDestinationSignature() {
		return destinationSignature;
	}

	public void setDestinationSignature(String destinationSignature) {
		this.destinationSignature = destinationSignature;
	}

	@Override
	public String toString() {
		return String.format("Operation[id=%d]", id);
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
