package io.hdmpedro.financeiro.controller;


import io.hdmpedro.financeiro.models.Transaction;
import io.hdmpedro.financeiro.models.enums.CategoryType;
import io.hdmpedro.financeiro.models.enums.TransactionType;
import io.hdmpedro.financeiro.service.CategoryService;
import io.hdmpedro.financeiro.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TransactionController {
    private final TransactionService transactionService;
    private final CategoryService categoryService;

    public TransactionController(TransactionService transactionService,
                                 CategoryService categoryService) {
        this.transactionService = transactionService;
        this.categoryService = categoryService;
    }

    public Transaction createTransaction(String description, BigDecimal amount,
                                         TransactionType type, CategoryType category, LocalDate date) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição é obrigatória");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }

        if (type == null) {
            throw new IllegalArgumentException("Tipo de transação é obrigatório");
        }

        if (category == null) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }

        if (date == null) {
            throw new IllegalArgumentException("Data é obrigatória");
        }

        Transaction transaction = transactionService.createTransaction(
                description.trim(), amount, type, category, date);

        if (type == TransactionType.SAIDA) {
            categoryService.updateCategorySpent(category, amount);
        }

        return transaction;
    }

    public Transaction updateTransaction(Long id, String description, BigDecimal amount,
                                         TransactionType type, CategoryType category, LocalDate date) {
        Transaction existingTransaction = transactionService.getTransactionById(id);
        if (existingTransaction == null) {
            throw new IllegalArgumentException("Transação não encontrada");
        }

        if (existingTransaction.getType() == TransactionType.SAIDA) {
            categoryService.updateCategorySpent(
                    existingTransaction.getCategory(),
                    existingTransaction.getAmount().negate());
        }

        Transaction updatedTransaction = transactionService.updateTransaction(
                id, description.trim(), amount, type, category, date);

        if (type == TransactionType.SAIDA) {
            categoryService.updateCategorySpent(category, amount);
        }

        return updatedTransaction;
    }

    public void deleteTransaction(Long id) {
        Transaction transaction = transactionService.getTransactionById(id);
        if (transaction == null) {
            throw new IllegalArgumentException("Transação não encontrada");
        }

        if (transaction.getType() == TransactionType.SAIDA) {
            categoryService.updateCategorySpent(
                    transaction.getCategory(),
                    transaction.getAmount().negate());
        }

        if (!transactionService.deleteTransaction(id)) {
            throw new RuntimeException("Erro ao deletar transação");
        }
    }

    public List<Transaction> getTransactionsByMonth(int month, int year) {
        return transactionService.getTransactionsByMonth(month, year);
    }

    public List<Transaction> getTransactionsByDate(LocalDate date) {
        return transactionService.getTransactionsByDate(date);
    }

    public BigDecimal getMonthlyBalance(int month, int year) {
        return transactionService.getMonthlyBalance(month, year);
    }

    public BigDecimal getCurrentBalance() {
        LocalDate now = LocalDate.now();
        return transactionService.getMonthlyBalance(now.getMonthValue(), now.getYear());
    }
}
