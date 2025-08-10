package io.hdmpedro.financeiro.service;

import io.hdmpedro.financeiro.models.RelatorioMensal;
import io.hdmpedro.financeiro.models.Transacao;
import io.hdmpedro.financeiro.models.BalancoSemanal;
import io.hdmpedro.financeiro.models.enums.TransacaoTipo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class FechamentoMesService {
    private final Map<String, RelatorioMensal> closedMonths = new HashMap<>();
    private final TransacaoService transacaoService;
    private final ReservaController reservaController;

    public FechamentoMesService(TransacaoService transacaoService,
                                ReservaController reservaController) {
        this.transacaoService = transacaoService;
        this.reservaController = reservaController;
    }

    public RelatorioMensal closeMonth(int month, int year, BigDecimal reserveContribution) {
        String key = month + "/" + year;

        if (closedMonths.containsKey(key)) {
            throw new IllegalStateException("Mês já foi fechado");
        }

        RelatorioMensal report = generateMonthlyReport(month, year);

        if (reserveContribution != null && reserveContribution.compareTo(BigDecimal.ZERO) > 0) {
            if (reserveContribution.compareTo(report.getBalance()) > 0) {
                throw new IllegalArgumentException("Valor para reserva não pode ser maior que o saldo");
            }
            report.setReserveContribution(reserveContribution);
            reservaController.addMonthlyBalanceToReserve(reserveContribution, month, year);
        }

        report.setClosed(true);
        report.setClosureDate(LocalDate.now());
        closedMonths.put(key, report);

        return report;
    }

    public RelatorioMensal generateMonthlyReport(int month, int year) {
        RelatorioMensal report = new RelatorioMensal(month, year);

        BigDecimal totalIncome = transacaoService.getTotalByType(
                TransacaoTipo.ENTRADA, month, year);
        BigDecimal totalExpenses = transacaoService.getTotalByType(
                TransacaoTipo.SAIDA, month, year);

        report.setTotalIncome(totalIncome);
        report.setTotalExpenses(totalExpenses);
        report.setBalance(totalIncome.subtract(totalExpenses));
        report.setExpensesByCategory(transacaoService.getExpensesByCategory(month, year));
        report.setWeeklyBalances(generateWeeklyBalances(month, year));

        return report;
    }

    private List<BalancoSemanal> generateWeeklyBalances(int month, int year) {
        List<BalancoSemanal> balancoSemanals = new ArrayList<>();
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        LocalDate currentDate = startOfMonth;
        int weekNumber = 1;

        while (!currentDate.isAfter(endOfMonth)) {
            LocalDate weekStart = currentDate;
            LocalDate weekEnd = currentDate.plusDays(6);
            if (weekEnd.isAfter(endOfMonth)) {
                weekEnd = endOfMonth;
            }

            BalancoSemanal balancoSemanal = new BalancoSemanal(weekNumber, weekStart, weekEnd);

            BigDecimal weekIncome = transacaoService.getTransactionsByDateRange(weekStart, weekEnd)
                    .stream()
                    .filter(t -> t.getType() == TransacaoTipo.ENTRADA)
                    .map(Transacao::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal weekExpenses = transacaoService.getTransactionsByDateRange(weekStart, weekEnd)
                    .stream()
                    .filter(t -> t.getType() == TransacaoTipo.SAIDA)
                    .map(Transacao::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            balancoSemanal.setIncome(weekIncome);
            balancoSemanal.setExpenses(weekExpenses);
            balancoSemanal.calculateBalance();

            balancoSemanals.add(balancoSemanal);

            currentDate = weekEnd.plusDays(1);
            weekNumber++;
        }

        return balancoSemanals;
    }

    public boolean isMonthClosed(int month, int year) {
        return closedMonths.containsKey(month + "/" + year);
    }

    public RelatorioMensal getClosedMonth(int month, int year) {
        return closedMonths.get(month + "/" + year);
    }

    public List<RelatorioMensal> getAllClosedMonths() {
        return new ArrayList<>(closedMonths.values());
    }

    public List<RelatorioMensal> getClosedMonthsByYear(int year) {
        return closedMonths.values().stream()
                .filter(r -> r.getYear() == year)
                .sorted(Comparator.comparing(RelatorioMensal::getMonth))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}