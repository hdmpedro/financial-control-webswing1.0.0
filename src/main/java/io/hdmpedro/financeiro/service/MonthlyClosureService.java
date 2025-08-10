package io.hdmpedro.financeiro.service;

import io.hdmpedro.financeiro.models.MonthlyReport;
import io.hdmpedro.financeiro.models.WeeklyBalance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class MonthlyClosureService {
    private final Map<String, MonthlyReport> closedMonths = new HashMap<>();
    private final TransactionService transactionService;
    private final ReserveService reserveService;

    public MonthlyClosureService(TransactionService transactionService,
                                 ReserveService reserveService) {
        this.transactionService = transactionService;
        this.reserveService = reserveService;
    }

    public MonthlyReport closeMonth(int month, int year, BigDecimal reserveContribution) {
        String key = month + "/" + year;

        if (closedMonths.containsKey(key)) {
            throw new IllegalStateException("Mês já foi fechado");
        }

        MonthlyReport report = generateMonthlyReport(month, year);

        if (reserveContribution != null && reserveContribution.compareTo(BigDecimal.ZERO) > 0) {
            if (reserveContribution.compareTo(report.getBalance()) > 0) {
                throw new IllegalArgumentException("Valor para reserva não pode ser maior que o saldo");
            }
            report.setReserveContribution(reserveContribution);
            reserveService.addMonthlyBalanceToReserve(reserveContribution, month, year);
        }

        report.setClosed(true);
        report.setClosureDate(LocalDate.now());
        closedMonths.put(key, report);

        return report;
    }

    public MonthlyReport generateMonthlyReport(int month, int year) {
        MonthlyReport report = new MonthlyReport(month, year);

        BigDecimal totalIncome = transactionService.getTotalByType(
                io.hdmpedro.financeiro.models.enums.TransactionType.ENTRADA, month, year);
        BigDecimal totalExpenses = transactionService.getTotalByType(
                io.hdmpedro.financeiro.models.enums.TransactionType.SAIDA, month, year);

        report.setTotalIncome(totalIncome);
        report.setTotalExpenses(totalExpenses);
        report.setBalance(totalIncome.subtract(totalExpenses));
        report.setExpensesByCategory(transactionService.getExpensesByCategory(month, year));
        report.setWeeklyBalances(generateWeeklyBalances(month, year));

        return report;
    }

    private List<WeeklyBalance> generateWeeklyBalances(int month, int year) {
        List<WeeklyBalance> weeklyBalances = new ArrayList<>();
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

            WeeklyBalance weeklyBalance = new WeeklyBalance(weekNumber, weekStart, weekEnd);

            BigDecimal weekIncome = transactionService.getTransactionsByDateRange(weekStart, weekEnd)
                    .stream()
                    .filter(t -> t.getType() == io.hdmpedro.financeiro.models.enums.TransactionType.ENTRADA)
                    .map(io.hdmpedro.financeiro.models.Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal weekExpenses = transactionService.getTransactionsByDateRange(weekStart, weekEnd)
                    .stream()
                    .filter(t -> t.getType() == io.hdmpedro.financeiro.models.enums.TransactionType.SAIDA)
                    .map(io.hdmpedro.financeiro.models.Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            weeklyBalance.setIncome(weekIncome);
            weeklyBalance.setExpenses(weekExpenses);
            weeklyBalance.calculateBalance();

            weeklyBalances.add(weeklyBalance);

            currentDate = weekEnd.plusDays(1);
            weekNumber++;
        }

        return weeklyBalances;
    }

    public boolean isMonthClosed(int month, int year) {
        return closedMonths.containsKey(month + "/" + year);
    }

    public MonthlyReport getClosedMonth(int month, int year) {
        return closedMonths.get(month + "/" + year);
    }

    public List<MonthlyReport> getAllClosedMonths() {
        return new ArrayList<>(closedMonths.values());
    }

    public List<MonthlyReport> getClosedMonthsByYear(int year) {
        return closedMonths.values().stream()
                .filter(r -> r.getYear() == year)
                .sorted(Comparator.comparing(MonthlyReport::getMonth))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}