package io.hdmpedro.financeiro.service;

import io.hdmpedro.financeiro.models.DailyBalance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class CalendarService {
    private final TransactionService transactionService;

    public CalendarService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public List<DailyBalance> getMonthlyCalendar(int month, int year) {
        List<DailyBalance> dailyBalances = new ArrayList<>();
        YearMonth yearMonth = YearMonth.of(year, month);

        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = LocalDate.of(year, month, day);
            DailyBalance dailyBalance = calculateDailyBalance(date);
            dailyBalances.add(dailyBalance);
        }

        calculateRunningBalances(dailyBalances);
        return dailyBalances;
    }

    private DailyBalance calculateDailyBalance(LocalDate date) {
        DailyBalance dailyBalance = new DailyBalance(date);

        BigDecimal dayIncome = transactionService.getTransactionsByDate(date).stream()
                .filter(t -> t.getType() == io.hdmpedro.financeiro.models.enums.TransactionType.ENTRADA)
                .map(io.hdmpedro.financeiro.models.Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal dayExpenses = transactionService.getTransactionsByDate(date).stream()
                .filter(t -> t.getType() == io.hdmpedro.financeiro.models.enums.TransactionType.SAIDA)
                .map(io.hdmpedro.financeiro.models.Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        dailyBalance.setIncome(dayIncome);
        dailyBalance.setExpenses(dayExpenses);
        dailyBalance.calculateBalance();

        return dailyBalance;
    }

    private void calculateRunningBalances(List<DailyBalance> dailyBalances) {
        BigDecimal runningTotal = BigDecimal.ZERO;

        for (DailyBalance dailyBalance : dailyBalances) {
            runningTotal = runningTotal.add(dailyBalance.getBalance());
            dailyBalance.setRunningBalance(runningTotal);
        }
    }

    public DailyBalance getDailyBalance(LocalDate date) {
        return calculateDailyBalance(date);
    }

    public boolean hasTransactionsOnDate(LocalDate date) {
        return !transactionService.getTransactionsByDate(date).isEmpty();
    }
}
