package io.hdmpedro.financeiro.models;


import io.hdmpedro.financeiro.models.enums.CategoriaTipo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class RelatorioMensal {
    private int month;
    private int year;
    private LocalDate closureDate;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal balance;
    private BigDecimal reserveContribution;
    private Map<CategoriaTipo, BigDecimal> expensesByCategory;
    private List<BalancoSemanal> balancoSemanals;
    private boolean isClosed;

    public RelatorioMensal() {
        this.isClosed = false;
    }

    public RelatorioMensal(int month, int year) {
        this();
        this.month = month;
        this.year = year;
    }

    public BigDecimal getFinalBalance() {
        return balance.subtract(reserveContribution != null ? reserveContribution : BigDecimal.ZERO);
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public List<BalancoSemanal> getWeeklyBalances() {
        return balancoSemanals;
    }

    public void setWeeklyBalances(List<BalancoSemanal> balancoSemanals) {
        this.balancoSemanals = balancoSemanals;
    }

    public Map<CategoriaTipo, BigDecimal> getExpensesByCategory() {
        return expensesByCategory;
    }

    public void setExpensesByCategory(Map<CategoriaTipo, BigDecimal> expensesByCategory) {
        this.expensesByCategory = expensesByCategory;
    }

    public BigDecimal getReserveContribution() {
        return reserveContribution;
    }

    public void setReserveContribution(BigDecimal reserveContribution) {
        this.reserveContribution = reserveContribution;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public LocalDate getClosureDate() {
        return closureDate;
    }

    public void setClosureDate(LocalDate closureDate) {
        this.closureDate = closureDate;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
