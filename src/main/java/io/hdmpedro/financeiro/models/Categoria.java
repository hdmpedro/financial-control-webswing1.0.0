package io.hdmpedro.financeiro.models;


import io.hdmpedro.financeiro.models.enums.CategoriaTipo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Categoria {
    private CategoriaTipo tipo;
    private BigDecimal orcamentoLimite;
    private BigDecimal gastoAtual;
    private boolean isActive;
    private LocalDate lastUpdated;

    public Categoria() {
        this.isActive = true;
        this.gastoAtual = BigDecimal.ZERO;
        this.lastUpdated = LocalDate.now();
    }

    public Categoria(CategoriaTipo tipo, BigDecimal orcamentoLimite) {
        this();
        this.tipo = tipo;
        this.orcamentoLimite = orcamentoLimite;
    }

    public BigDecimal getRemainingBudget() {
        return orcamentoLimite.subtract(gastoAtual);
    }

    public double getBudgetUsagePercentage() {
        if (orcamentoLimite.equals(BigDecimal.ZERO)) return 0;
        return gastoAtual.divide(orcamentoLimite, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)).doubleValue();
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public BigDecimal getGastoAtual() {
        return gastoAtual;
    }

    public void setGastoAtual(BigDecimal gastoAtual) {
        this.gastoAtual = gastoAtual;
    }

    public BigDecimal getOrcamentoLimite() {
        return orcamentoLimite;
    }

    public void setOrcamentoLimite(BigDecimal orcamentoLimite) {
        this.orcamentoLimite = orcamentoLimite;
    }

    public CategoriaTipo getTipo() {
        return tipo;
    }

    public void setTipo(CategoriaTipo tipo) {
        this.tipo = tipo;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}