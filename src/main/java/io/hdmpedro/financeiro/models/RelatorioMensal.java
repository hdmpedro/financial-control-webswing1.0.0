package io.hdmpedro.financeiro.models;


import io.hdmpedro.financeiro.models.enums.CategoriaTipo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class RelatorioMensal {
    private int mes;
    private int ano;
    private LocalDate fechamentoData;
    private BigDecimal rendaTotal;
    private BigDecimal despesasTotal;
    private BigDecimal balanco;
    private BigDecimal contribuicaoReserva;
    private Map<CategoriaTipo, BigDecimal> gastosPorCategoria;
    private List<BalancoSemanal> balancoSemanals;
    private boolean isClosed;

    public RelatorioMensal() {
        this.isClosed = false;
    }

    public RelatorioMensal(int mes, int ano) {
        this();
        this.mes = mes;
        this.ano = ano;
    }

    public BigDecimal getFinalBalance() {
        return balanco.subtract(contribuicaoReserva != null ? contribuicaoReserva : BigDecimal.ZERO);
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public List<BalancoSemanal> getWeeklyBalances() {
        return balancoSemanals;
    }

    public void setWeeklyBalances(List<BalancoSemanal> balancoSemanals) {
        this.balancoSemanals = balancoSemanals;
    }

    public Map<CategoriaTipo, BigDecimal> getGastosPorCategoria() {
        return gastosPorCategoria;
    }

    public void setGastosPorCategoria(Map<CategoriaTipo, BigDecimal> gastosPorCategoria) {
        this.gastosPorCategoria = gastosPorCategoria;
    }

    public BigDecimal getContribuicaoReserva() {
        return contribuicaoReserva;
    }

    public void setContribuicaoReserva(BigDecimal contribuicaoReserva) {
        this.contribuicaoReserva = contribuicaoReserva;
    }

    public BigDecimal getBalanco() {
        return balanco;
    }

    public void setBalanco(BigDecimal balanco) {
        this.balanco = balanco;
    }

    public BigDecimal getDespesasTotal() {
        return despesasTotal;
    }

    public void setDespesasTotal(BigDecimal despesasTotal) {
        this.despesasTotal = despesasTotal;
    }

    public BigDecimal getRendaTotal() {
        return rendaTotal;
    }

    public void setRendaTotal(BigDecimal rendaTotal) {
        this.rendaTotal = rendaTotal;
    }

    public LocalDate getFechamentoData() {
        return fechamentoData;
    }

    public void setFechamentoData(LocalDate fechamentoData) {
        this.fechamentoData = fechamentoData;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }
}
