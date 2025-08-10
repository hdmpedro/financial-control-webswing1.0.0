package io.hdmpedro.financeiro.controller;


import io.hdmpedro.financeiro.models.RelatorioMensal;
import io.hdmpedro.financeiro.service.FechamentoMesService;
import io.hdmpedro.financeiro.service.ReservaController;
import io.hdmpedro.financeiro.service.TransacaoService;

import java.math.BigDecimal;
import java.util.List;

public class FechamentoMesController {
    private final FechamentoMesService fechamentoMesService;
    private final ReservaController reservaController;
    private final TransacaoService transacaoService;

    public FechamentoMesController(FechamentoMesService fechamentoMesService,
                                   ReservaController reservaController,
                                   TransacaoService transacaoService) {
        this.fechamentoMesService = fechamentoMesService;
        this.reservaController = reservaController;
        this.transacaoService = transacaoService;
    }

    public RelatorioMensal closeMonth(int month, int year, BigDecimal reserveContribution) {
        if (fechamentoMesService.isMonthClosed(month, year)) {
            throw new IllegalStateException("Este mês já foi fechado");
        }

        if (reserveContribution != null && reserveContribution.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor para reserva não pode ser negativo");
        }

        BigDecimal monthlyBalance = transacaoService.getMonthlyBalance(month, year);
        if (reserveContribution != null && reserveContribution.compareTo(monthlyBalance) > 0) {
            throw new IllegalArgumentException("Valor para reserva não pode ser maior que o saldo do mês");
        }

        return fechamentoMesService.closeMonth(month, year, reserveContribution);
    }

    public RelatorioMensal generatePreviewReport(int month, int year) {
        return fechamentoMesService.generateMonthlyReport(month, year);
    }

    public boolean canCloseMonth(int month, int year) {
        return !fechamentoMesService.isMonthClosed(month, year);
    }

    public boolean isMonthClosed(int month, int year) {
        return fechamentoMesService.isMonthClosed(month, year);
    }

    public RelatorioMensal getClosedMonth(int month, int year) {
        return fechamentoMesService.getClosedMonth(month, year);
    }

    public List<RelatorioMensal> getAllClosedMonths() {
        return fechamentoMesService.getAllClosedMonths();
    }

    public List<RelatorioMensal> getClosedMonthsByYear(int year) {
        return fechamentoMesService.getClosedMonthsByYear(year);
    }

    public BigDecimal calculateYearlyBalance(int year) {
        return getClosedMonthsByYear(year).stream()
                .map(RelatorioMensal::getFinalBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}