package io.hdmpedro.financeiro.controller;


import io.hdmpedro.financeiro.models.Transacao;
import io.hdmpedro.financeiro.models.enums.CategoriaTipo;
import io.hdmpedro.financeiro.models.enums.TransacaoTipo;
import io.hdmpedro.financeiro.service.CategoriaService;
import io.hdmpedro.financeiro.service.TransacaoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TransacaoController {
    private final TransacaoService transacaoService;
    private final CategoriaService categoriaService;

    public TransacaoController(TransacaoService transacaoService,
                               CategoriaService categoriaService) {
        this.transacaoService = transacaoService;
        this.categoriaService = categoriaService;
    }

    public Transacao createTransaction(String description, BigDecimal amount,
                                       TransacaoTipo type, CategoriaTipo category, LocalDate date) {
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

        Transacao transacao = transacaoService.createTransaction(
                description.trim(), amount, type, category, date);

        if (type == TransacaoTipo.SAIDA) {
            categoriaService.updateCategorySpent(category, amount);
        }

        return transacao;
    }

    public Transacao updateTransaction(Long id, String description, BigDecimal amount,
                                       TransacaoTipo type, CategoriaTipo category, LocalDate date) {
        Transacao existingTransacao = transacaoService.getTransactionById(id);
        if (existingTransacao == null) {
            throw new IllegalArgumentException("Transação não encontrada");
        }

        if (existingTransacao.getType() == TransacaoTipo.SAIDA) {
            categoriaService.updateCategorySpent(
                    existingTransacao.getCategory(),
                    existingTransacao.getAmount().negate());
        }

        Transacao updatedTransacao = transacaoService.updateTransaction(
                id, description.trim(), amount, type, category, date);

        if (type == TransacaoTipo.SAIDA) {
            categoriaService.updateCategorySpent(category, amount);
        }

        return updatedTransacao;
    }

    public void deleteTransaction(Long id) {
        Transacao transacao = transacaoService.getTransactionById(id);
        if (transacao == null) {
            throw new IllegalArgumentException("Transação não encontrada");
        }

        if (transacao.getType() == TransacaoTipo.SAIDA) {
            categoriaService.updateCategorySpent(
                    transacao.getCategory(),
                    transacao.getAmount().negate());
        }

        if (!transacaoService.deleteTransaction(id)) {
            throw new RuntimeException("Erro ao deletar transação");
        }
    }

    public List<Transacao> getTransactionsByMonth(int month, int year) {
        return transacaoService.getTransactionsByMonth(month, year);
    }

    public List<Transacao> getTransactionsByDate(LocalDate date) {
        return transacaoService.getTransactionsByDate(date);
    }

    public BigDecimal getMonthlyBalance(int month, int year) {
        return transacaoService.getMonthlyBalance(month, year);
    }

    public BigDecimal getCurrentBalance() {
        LocalDate now = LocalDate.now();
        return transacaoService.getMonthlyBalance(now.getMonthValue(), now.getYear());
    }
}
