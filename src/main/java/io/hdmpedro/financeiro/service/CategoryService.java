package io.hdmpedro.financeiro.service;


import io.hdmpedro.financeiro.models.Category;
import io.hdmpedro.financeiro.models.enums.CategoryType;

import java.math.BigDecimal;
import java.util.*;

public class CategoryService {
    private final Map<CategoryType, Category> categories = new EnumMap<>(CategoryType.class);

    public CategoryService() {
        initializeDefaultCategories();
    }

    private void initializeDefaultCategories() {
        for (CategoryType type : CategoryType.values()) {
            categories.put(type, new Category(type, BigDecimal.ZERO));
        }
    }

    public Category updateCategoryBudget(CategoryType type, BigDecimal budgetLimit) {
        Category category = categories.get(type);
        if (category != null) {
            category.setBudgetLimit(budgetLimit);
        }
        return category;
    }

    public void updateCategorySpent(CategoryType type, BigDecimal amount) {
        Category category = categories.get(type);
        if (category != null) {
            category.setCurrentSpent(category.getCurrentSpent().add(amount));
        }
    }

    public Category getCategoryByType(CategoryType type) {
        return categories.get(type);
    }

    public List<Category> getAllCategories() {
        return new ArrayList<>(categories.values());
    }

    public List<Category> getActiveCategories() {
        return categories.values().stream()
                .filter(Category::isActive)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public void resetMonthlySpending() {
        categories.values().forEach(category ->
                category.setCurrentSpent(BigDecimal.ZERO));
    }

    public List<Category> getOverBudgetCategories() {
        return categories.values().stream()
                .filter(c -> c.getCurrentSpent().compareTo(c.getBudgetLimit()) > 0 &&
                        c.getBudgetLimit().compareTo(BigDecimal.ZERO) > 0)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public Map<CategoryType, Double> getBudgetUsagePercentages() {
        Map<CategoryType, Double> usage = new EnumMap<>(CategoryType.class);
        categories.forEach((type, category) ->
                usage.put(type, category.getBudgetUsagePercentage()));
        return usage;
    }
}