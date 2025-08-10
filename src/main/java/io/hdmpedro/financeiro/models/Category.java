package io.hdmpedro.financeiro.models;


import io.hdmpedro.financeiro.models.enums.CategoryType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Category {
    private CategoryType type;
    private BigDecimal budgetLimit;
    private BigDecimal currentSpent;
    private boolean isActive;
    private LocalDate lastUpdated;

    public Category() {
        this.isActive = true;
        this.currentSpent = BigDecimal.ZERO;
        this.lastUpdated = LocalDate.now();
    }

    public Category(CategoryType type, BigDecimal budgetLimit) {
        this();
        this.type = type;
        this.budgetLimit = budgetLimit;
    }

    public BigDecimal getRemainingBudget() {
        return budgetLimit.subtract(currentSpent);
    }

    public double getBudgetUsagePercentage() {
        if (budgetLimit.equals(BigDecimal.ZERO)) return 0;
        return currentSpent.divide(budgetLimit, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)).doubleValue();
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public BigDecimal getCurrentSpent() {
        return currentSpent;
    }

    public void setCurrentSpent(BigDecimal currentSpent) {
        this.currentSpent = currentSpent;
    }

    public BigDecimal getBudgetLimit() {
        return budgetLimit;
    }

    public void setBudgetLimit(BigDecimal budgetLimit) {
        this.budgetLimit = budgetLimit;
    }

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}