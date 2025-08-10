package io.hdmpedro.financeiro.controller;


import io.hdmpedro.financeiro.models.Categoria;
import io.hdmpedro.financeiro.models.enums.CategoriaTipo;
import io.hdmpedro.financeiro.service.CategoriaService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class CategoriaController {
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    public void updateCategoryBudget(CategoriaTipo type, BigDecimal budgetLimit) {
        if (budgetLimit == null || budgetLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Orçamento deve ser maior ou igual a zero");
        }

        categoriaService.updateCategoryBudget(type, budgetLimit);
    }

    public Categoria getCategoryByType(CategoriaTipo type) {
        return categoriaService.getCategoryByType(type);
    }

    public List<Categoria> getAllCategories() {
        return categoriaService.getAllCategories();
    }

    public List<Categoria> getOverBudgetCategories() {
        return categoriaService.getOverBudgetCategories();
    }

    public Map<CategoriaTipo, Double> getBudgetUsagePercentages() {
        return categoriaService.getBudgetUsagePercentages();
    }

    public void resetMonthlySpending() {
        categoriaService.resetMonthlySpending();
    }
}