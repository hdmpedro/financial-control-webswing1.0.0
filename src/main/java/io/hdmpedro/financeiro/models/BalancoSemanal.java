package io.hdmpedro.financeiro.models;


import java.math.BigDecimal;
import java.time.LocalDate;

public class BalancoSemanal {
    private int numeroSemana;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private BigDecimal renda;
    private BigDecimal despesas;
    private BigDecimal balanco;

    public BalancoSemanal() {
        this.renda = BigDecimal.ZERO;
        this.despesas = BigDecimal.ZERO;
        this.balanco = BigDecimal.ZERO;
    }

    public BalancoSemanal(int numeroSemana, LocalDate dataInicio, LocalDate dataFim) {
        this();
        this.numeroSemana = numeroSemana;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public void calculateBalance() {
        this.balanco = renda.subtract(despesas);
    }

    public int getNumeroSemana() {
        return numeroSemana;
    }

    public void setNumeroSemana(int numeroSemana) {
        this.numeroSemana = numeroSemana;
    }

    public BigDecimal getBalanco() {
        return balanco;
    }

    public void setBalanco(BigDecimal balanco) {
        this.balanco = balanco;
    }

    public BigDecimal getRenda() {
        return renda;
    }

    public void setRenda(BigDecimal renda) {
        this.renda = renda;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public BigDecimal getDespesas() {
        return despesas;
    }

    public void setDespesas(BigDecimal despesas) {
        this.despesas = despesas;
    }
}