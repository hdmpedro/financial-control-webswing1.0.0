package io.hdmpedro.financeiro.service;


import io.hdmpedro.financeiro.models.Categoria;
import io.hdmpedro.financeiro.models.enums.CategoriaTipo;

import java.math.BigDecimal;
import java.util.*;

public class CategoriaService {
    private final Map<CategoriaTipo, Categoria> categories = new EnumMap<>(CategoriaTipo.class);

    public CategoriaService() {
        initializeDefaultCategories();
    }

    private void initializeDefaultCategories() {
        for (CategoriaTipo type : CategoriaTipo.values()) {
            categories.put(type, new Categoria(type, BigDecimal.ZERO));
        }
    }

    public Categoria updateCategoryBudget(CategoriaTipo type, BigDecimal budgetLimit) {
        Categoria categoria = categories.get(type);
        if (categoria != null) {
            categoria.setBudgetLimit(budgetLimit);
        }
        return categoria;
    }

    public void updateCategorySpent(CategoriaTipo type, BigDecimal amount) {
        Categoria categoria = categories.get(type);
        if (categoria != null) {
            categoria.setCurrentSpent(categoria.getCurrentSpent().add(amount));
        }
    }

    public Categoria getCategoryByType(CategoriaTipo type) {
        return categories.get(type);
    }

    public List<Categoria> getAllCategories() {
        return new ArrayList<>(categories.values());
    }

    public List<Categoria> getActiveCategories() {
        return categories.values().stream()
                .filter(Categoria::isActive)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public void resetMonthlySpending() {
        categories.values().forEach(categoria ->
                categoria.setCurrentSpent(BigDecimal.ZERO));
    }

    public List<Categoria> getOverBudgetCategories() {
        return categories.values().stream()
                .filter(c -> c.getCurrentSpent().compareTo(c.getBudgetLimit()) > 0 &&
                        c.getBudgetLimit().compareTo(BigDecimal.ZERO) > 0)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public Map<CategoriaTipo, Double> getBudgetUsagePercentages() {
        Map<CategoriaTipo, Double> usage = new EnumMap<>(CategoriaTipo.class);
        categories.forEach((type, categoria) ->
                usage.put(type, categoria.getBudgetUsagePercentage()));
        return usage;
    }
}