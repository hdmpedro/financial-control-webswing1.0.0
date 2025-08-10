package io.hdmpedro.financeiro.models;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reserva {
    private Long id;
    private BigDecimal quantia;
    private String descricao;
    private LocalDate data;
    private LocalDateTime criadoEm;
    private boolean isFromMonthlyBalance;

    public Reserva() {
        this.criadoEm = LocalDateTime.now();
        this.isFromMonthlyBalance = false;
    }

    public Reserva(BigDecimal quantia, String descricao, LocalDate data) {
        this();
        this.quantia = quantia;
        this.descricao = descricao;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isFromMonthlyBalance() {
        return isFromMonthlyBalance;
    }

    public void setFromMonthlyBalance(boolean fromMonthlyBalance) {
        isFromMonthlyBalance = fromMonthlyBalance;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getQuantia() {
        return quantia;
    }

    public void setQuantia(BigDecimal quantia) {
        this.quantia = quantia;
    }

}