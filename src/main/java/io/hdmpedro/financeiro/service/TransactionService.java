package io.hdmpedro.financeiro.service;


import io.hdmpedro.financeiro.models.Transaction;
import io.hdmpedro.financeiro.models.enums.TransactionType;
import io.hdmpedro.financeiro.models.enums.CategoryType;
import io.hdmpedro.financeiro.util.DateUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class TransactionService {
    private final Map<Long, Transaction> transactions = new LinkedHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Transaction createTransaction(String description, BigDecimal amount,
                                         TransactionType type, CategoryType category, LocalDate date) {
        Transaction transaction = new Transaction(description, amount, type, category, date);
        transaction.setId(idGenerator.getAndIncrement());
        transactions.put(transaction.getId(), transaction);
        return transaction;
    }

    public Transaction updateTransaction(Long id, String description, BigDecimal amount,
                                         TransactionType type, CategoryType category, LocalDate date) {
        Transaction transaction = transactions.get(id);
        if (transaction != null) {
            transaction.setDescription(description);
            transaction.setAmount(amount);
            transaction.setType(type);
            transaction.setCategory(category);
            transaction.setDate(date);
        }
        return transaction;
    }

    public boolean deleteTransaction(Long id) {
        return transactions.remove(id) != null;
    }

    public Transaction getTransactionById(Long id) {
        return transactions.get(id);
    }

    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions.values());
    }

    public List<Transaction> getTransactionsByMonth(int month, int year) {
        return transactions.values().stream()
                .filter(t -> t.getDate().getMonth().getValue() == month &&
                        t.getDate().getYear() == year)
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate) {
        return transactions.values().stream()
                .filter(t -> !t.getDate().isBefore(startDate) && !t.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByCategory(CategoryType category) {
        return transactions.values().stream()
                .filter(t -> t.getCategory() == category)
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalByType(TransactionType type, int month, int year) {
        return getTransactionsByMonth(month, year).stream()
                .filter(t -> t.getType() == type)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalByCategory(CategoryType category, int month, int year) {
        return getTransactionsByMonth(month, year).stream()
                .filter(t -> t.getCategory() == category)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<CategoryType, BigDecimal> getExpensesByCategory(int month, int year) {
        return getTransactionsByMonth(month, year).stream()
                .filter(t -> t.getType() == TransactionType.SAIDA)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));
    }

    public BigDecimal getMonthlyBalance(int month, int year) {
        BigDecimal income = getTotalByType(TransactionType.ENTRADA, month, year);
        BigDecimal expenses = getTotalByType(TransactionType.SAIDA, month, year);
        return income.subtract(expenses);
    }

    public List<Transaction> getTransactionsByDate(LocalDate date) {
        return transactions.values().stream()
                .filter(t -> t.getDate().equals(date))
                .collect(Collectors.toList());
    }
}