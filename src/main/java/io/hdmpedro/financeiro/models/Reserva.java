package io.hdmpedro.financeiro.models;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reserva {
    private Long id;
    private BigDecimal amount;
    private String description;
    private LocalDate date;
    private LocalDateTime createdAt;
    private boolean isFromMonthlyBalance;

    public Reserva() {
        this.createdAt = LocalDateTime.now();
        this.isFromMonthlyBalance = false;
    }

    public Reserva(BigDecimal amount, String description, LocalDate date) {
        this();
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isFromMonthlyBalance() {
        return isFromMonthlyBalance;
    }

    public void setFromMonthlyBalance(boolean fromMonthlyBalance) {
        isFromMonthlyBalance = fromMonthlyBalance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}