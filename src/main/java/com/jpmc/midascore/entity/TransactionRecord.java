package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserRecord sender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserRecord recipient;

    @Column(nullable = false)
    private float amount;

    @Column(nullable = false)
    private float incentiveAmount; // <-- NEW FIELD

    protected TransactionRecord() {
    }

    // UPDATED Constructor
    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount, float incentiveAmount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.incentiveAmount = incentiveAmount;
    }

    // Getters for Task 3 validation
    public Long getId() {
        return id;
    }
    public UserRecord getSender() {
        return sender;
    }
    public UserRecord getRecipient() {
        return recipient;
    }
    public float getAmount() {
        return amount;
    }

    public float getIncentiveAmount() {
        return incentiveAmount;
    }
}