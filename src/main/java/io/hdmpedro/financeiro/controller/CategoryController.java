package io.hdmpedro.financeiro.controller;


import io.hdmpedro.financeiro.models.Category;
import io.hdmpedro.financeiro.models.enums.CategoryType;
import io.hdmpedro.financeiro.service.CategoryService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void updateCategoryBudget(CategoryType type, BigDecimal budgetLimit) {
        if (budgetLimit == null || budgetLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Orçamento deve ser maior ou igual a zero");
        }

        categoryService.updateCategoryBudget(type, budgetLimit);
    }

    public Category getCategoryByType(CategoryType type) {
        return categoryService.getCategoryByType(type);
    }

    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    public List<Category> getOverBudgetCategories() {
        return categoryService.getOverBudgetCategories();
    }

    public Map<CategoryType, Double> getBudgetUsagePercentages() {
        return categoryService.getBudgetUsagePercentages();
    }

    public void resetMonthlySpending() {
        categoryService.resetMonthlySpending();
    }
}