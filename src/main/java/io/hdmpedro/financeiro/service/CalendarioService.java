package io.hdmpedro.financeiro.service;

import io.hdmpedro.financeiro.models.BalancoDiario;
import io.hdmpedro.financeiro.models.Transacao;
import io.hdmpedro.financeiro.models.enums.TransacaoTipo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class CalendarioService {
    private final TransacaoService transacaoService;

    public CalendarioService(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    public List<BalancoDiario> getMonthlyCalendar(int month, int year) {
        List<BalancoDiario> balancoDiarios = new ArrayList<>();
        YearMonth yearMonth = YearMonth.of(year, month);

        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = LocalDate.of(year, month, day);
            BalancoDiario balancoDiario = calculateDailyBalance(date);
            balancoDiarios.add(balancoDiario);
        }

        calculateRunningBalances(balancoDiarios);
        return balancoDiarios;
    }

    private BalancoDiario calculateDailyBalance(LocalDate date) {
        BalancoDiario balancoDiario = new BalancoDiario(date);

        BigDecimal dayIncome = transacaoService.getTransactionsByDate(date).stream()
                .filter(t -> t.getTransacaoTipo() == TransacaoTipo.ENTRADA)
                .map(Transacao::getQuantia)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal dayExpenses = transacaoService.getTransactionsByDate(date).stream()
                .filter(t -> t.getTransacaoTipo() == TransacaoTipo.SAIDA)
                .map(Transacao::getQuantia)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        balancoDiario.setIncome(dayIncome);
        balancoDiario.setExpenses(dayExpenses);
        balancoDiario.calculateBalance();

        return balancoDiario;
    }

    private void calculateRunningBalances(List<BalancoDiario> balancoDiarios) {
        BigDecimal runningTotal = BigDecimal.ZERO;

        for (BalancoDiario balancoDiario : balancoDiarios) {
            runningTotal = runningTotal.add(balancoDiario.getBalance());
            balancoDiario.setRunningBalance(runningTotal);
        }
    }

    public BalancoDiario getDailyBalance(LocalDate date) {
        return calculateDailyBalance(date);
    }

    public boolean hasTransactionsOnDate(LocalDate date) {
        return !transacaoService.getTransactionsByDate(date).isEmpty();
    }
}
