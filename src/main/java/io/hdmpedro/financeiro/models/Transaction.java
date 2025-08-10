package io.hdmpedro.financeiro.models;



import io.hdmpedro.financeiro.models.enums.CategoryType;
import io.hdmpedro.financeiro.models.enums.TransactionType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

public class Transaction {
    private Long id;
    private String description;
    private BigDecimal amount;
    private TransactionType type;
    private CategoryType category;
    private LocalDate date;
    private LocalDateTime createdAt;
    private String notes;

    public Transaction() {
        this.createdAt = LocalDateTime.now();
    }

    public Transaction(String description, BigDecimal amount, TransactionType type,
                       CategoryType category, LocalDate date) {
        this();
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public CategoryType getCategory() {
        return category;
    }

    public void setCategory(CategoryType category) {
        this.category = category;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
