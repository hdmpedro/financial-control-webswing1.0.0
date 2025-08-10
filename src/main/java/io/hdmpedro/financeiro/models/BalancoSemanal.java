package io.hdmpedro.financeiro.models;


import java.math.BigDecimal;
import java.time.LocalDate;

public class BalancoSemanal {
    private int weekNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal income;
    private BigDecimal expenses;
    private BigDecimal balance;

    public BalancoSemanal() {
        this.income = BigDecimal.ZERO;
        this.expenses = BigDecimal.ZERO;
        this.balance = BigDecimal.ZERO;
    }

    public BalancoSemanal(int weekNumber, LocalDate startDate, LocalDate endDate) {
        this();
        this.weekNumber = weekNumber;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void calculateBalance() {
        this.balance = income.subtract(expenses);
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getExpenses() {
        return expenses;
    }

    public void setExpenses(BigDecimal expenses) {
        this.expenses = expenses;
    }
}