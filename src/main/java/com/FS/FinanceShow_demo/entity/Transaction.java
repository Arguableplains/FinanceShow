/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.FS.FinanceShow_demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "transactions")
public class Transaction {
    public Transaction(double amount, Instant happenedOn, User user) {
        this.amount = amount;
        this.happenedOn = happenedOn;
        this.createdOn = Instant.now();
        this.user = user;
    }
    
    public Transaction() {}
    
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "AMOUNT", nullable = false)
    private double amount;
    @Column(name = "HAPPENED_ON", nullable = false)
    private Instant happenedOn;
    @Column(name = "CREATED_ON", nullable = false)
    private Instant createdOn;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    public User getUser() {
        return user;
    }
    
    public Long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Instant getHappenedOn() {
        return happenedOn;
    }

    public void setHappenedOn(Instant happenedOn) {
        this.happenedOn = happenedOn;
    }
    
    public Instant getCreatedOn() {
        return createdOn;
    }
}
