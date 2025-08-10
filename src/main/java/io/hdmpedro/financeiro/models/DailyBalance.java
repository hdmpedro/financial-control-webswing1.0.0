package io.hdmpedro.financeiro.models;


import java.math.BigDecimal;
import java.time.LocalDate;

public class DailyBalance {
    private LocalDate date;
    private BigDecimal income;
    private BigDecimal expenses;
    private BigDecimal balance;
    private BigDecimal runningBalance;

    public DailyBalance() {
        this.income = BigDecimal.ZERO;
        this.expenses = BigDecimal.ZERO;
        this.balance = BigDecimal.ZERO;
    }

    public DailyBalance(LocalDate date) {
        this();
        this.date = date;
    }

    public void calculateBalance() {
        this.balance = income.subtract(expenses);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getRunningBalance() {
        return runningBalance;
    }

    public void setRunningBalance(BigDecimal runningBalance) {
        this.runningBalance = runningBalance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getExpenses() {
        return expenses;
    }

    public void setExpenses(BigDecimal expenses) {
        this.expenses = expenses;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }
}