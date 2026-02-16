package com.rewardSystem.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class CustomerTranscation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int customerId;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private LocalDate date;

    public CustomerTranscation() {

    }

    public CustomerTranscation(int customerId, double amount, LocalDate date) {
        super();
        this.customerId = customerId;
        this.amount = amount;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Transactions [id=" + id + ", customerId=" + customerId + ", amount=" + amount + ", date=" + date + "]";
    }

}
