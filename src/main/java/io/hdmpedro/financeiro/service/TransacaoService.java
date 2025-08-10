package io.hdmpedro.financeiro.service;


import io.hdmpedro.financeiro.models.Transacao;
import io.hdmpedro.financeiro.models.enums.CategoriaTipo;
import io.hdmpedro.financeiro.models.enums.TransacaoTipo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class TransacaoService {
    private final Map<Long, Transacao> transactions = new LinkedHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Transacao createTransaction(String description, BigDecimal amount,
                                       TransacaoTipo type, CategoriaTipo category, LocalDate date) {
        Transacao transacao = new Transacao(description, amount, type, category, date);
        transacao.setId(idGenerator.getAndIncrement());
        transactions.put(transacao.getId(), transacao);
        return transacao;
    }

    public Transacao updateTransaction(Long id, String description, BigDecimal amount,
                                       TransacaoTipo type, CategoriaTipo category, LocalDate date) {
        Transacao transacao = transactions.get(id);
        if (transacao != null) {
            transacao.setDescricao(description);
            transacao.setQuantia(amount);
            transacao.setTransacaoTipo(type);
            transacao.setCategoriaTipo(category);
            transacao.setData(date);
        }
        return transacao;
    }

    public boolean deleteTransaction(Long id) {
        return transactions.remove(id) != null;
    }

    public Transacao getTransactionById(Long id) {
        return transactions.get(id);
    }

    public List<Transacao> getAllTransactions() {
        return new ArrayList<>(transactions.values());
    }

    public List<Transacao> getTransactionsByMonth(int month, int year) {
        return transactions.values().stream()
                .filter(t -> t.getData().getMonth().getValue() == month &&
                        t.getData().getYear() == year)
                .collect(Collectors.toList());
    }

    public List<Transacao> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate) {
        return transactions.values().stream()
                .filter(t -> !t.getData().isBefore(startDate) && !t.getData().isAfter(endDate))
                .collect(Collectors.toList());
    }

    public List<Transacao> getTransactionsByCategory(CategoriaTipo category) {
        return transactions.values().stream()
                .filter(t -> t.getCategoriaTipo() == category)
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalByType(TransacaoTipo type, int month, int year) {
        return getTransactionsByMonth(month, year).stream()
                .filter(t -> t.getTransacaoTipo() == type)
                .map(Transacao::getQuantia)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalByCategory(CategoriaTipo category, int month, int year) {
        return getTransactionsByMonth(month, year).stream()
                .filter(t -> t.getCategoriaTipo() == category)
                .map(Transacao::getQuantia)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<CategoriaTipo, BigDecimal> getExpensesByCategory(int month, int year) {
        return getTransactionsByMonth(month, year).stream()
                .filter(t -> t.getTransacaoTipo() == TransacaoTipo.SAIDA)
                .collect(Collectors.groupingBy(
                        Transacao::getCategoriaTipo,
                        Collectors.reducing(BigDecimal.ZERO, Transacao::getQuantia, BigDecimal::add)
                ));
    }

    public BigDecimal getMonthlyBalance(int month, int year) {
        BigDecimal income = getTotalByType(TransacaoTipo.ENTRADA, month, year);
        BigDecimal expenses = getTotalByType(TransacaoTipo.SAIDA, month, year);
        return income.subtract(expenses);
    }

    public List<Transacao> getTransactionsByDate(LocalDate date) {
        return transactions.values().stream()
                .filter(t -> t.getData().equals(date))
                .collect(Collectors.toList());
    }
}