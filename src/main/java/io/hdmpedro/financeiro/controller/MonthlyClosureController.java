package io.hdmpedro.financeiro.controller;


import io.hdmpedro.financeiro.models.MonthlyReport;
import io.hdmpedro.financeiro.service.MonthlyClosureService;
import io.hdmpedro.financeiro.service.ReserveService;
import io.hdmpedro.financeiro.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MonthlyClosureController {
    private final MonthlyClosureService monthlyClosureService;
    private final ReserveService reserveService;
    private final TransactionService transactionService;

    public MonthlyClosureController(MonthlyClosureService monthlyClosureService,
                                    ReserveService reserveService,
                                    TransactionService transactionService) {
        this.monthlyClosureService = monthlyClosureService;
        this.reserveService = reserveService;
        this.transactionService = transactionService;
    }

    public MonthlyReport closeMonth(int month, int year, BigDecimal reserveContribution) {
        if (monthlyClosureService.isMonthClosed(month, year)) {
            throw new IllegalStateException("Este mês já foi fechado");
        }

        if (reserveContribution != null && reserveContribution.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor para reserva não pode ser negativo");
        }

        BigDecimal monthlyBalance = transactionService.getMonthlyBalance(month, year);
        if (reserveContribution != null && reserveContribution.compareTo(monthlyBalance) > 0) {
            throw new IllegalArgumentException("Valor para reserva não pode ser maior que o saldo do mês");
        }

        return monthlyClosureService.closeMonth(month, year, reserveContribution);
    }

    public MonthlyReport generatePreviewReport(int month, int year) {
        return monthlyClosureService.generateMonthlyReport(month, year);
    }

    public boolean canCloseMonth(int month, int year) {
        return !monthlyClosureService.isMonthClosed(month, year);
    }

    public boolean isMonthClosed(int month, int year) {
        return monthlyClosureService.isMonthClosed(month, year);
    }

    public MonthlyReport getClosedMonth(int month, int year) {
        return monthlyClosureService.getClosedMonth(month, year);
    }

    public List<MonthlyReport> getAllClosedMonths() {
        return monthlyClosureService.getAllClosedMonths();
    }

    public List<MonthlyReport> getClosedMonthsByYear(int year) {
        return monthlyClosureService.getClosedMonthsByYear(year);
    }

    public BigDecimal calculateYearlyBalance(int year) {
        return getClosedMonthsByYear(year).stream()
                .map(MonthlyReport::getFinalBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}