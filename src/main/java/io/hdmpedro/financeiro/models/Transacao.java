package io.hdmpedro.financeiro.models;



import io.hdmpedro.financeiro.models.enums.CategoriaTipo;
import io.hdmpedro.financeiro.models.enums.TransacaoTipo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

public class Transacao {
    private Long id;
    private String descricao;
    private BigDecimal quantia;
    private TransacaoTipo transacaoTipo;
    private CategoriaTipo categoriaTipo;
    private LocalDate data;
    private LocalDateTime criadoEm;
    private String notas;

    public Transacao() {
        this.criadoEm = LocalDateTime.now();
    }

    public Transacao(String descricao, BigDecimal quantia, TransacaoTipo transacaoTipo,
                     CategoriaTipo categoriaTipo, LocalDate data) {
        this();
        this.descricao = descricao;
        this.quantia = quantia;
        this.transacaoTipo = transacaoTipo;
        this.categoriaTipo = categoriaTipo;
        this.data = data;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
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

    public CategoriaTipo getCategoriaTipo() {
        return categoriaTipo;
    }

    public void setCategoriaTipo(CategoriaTipo categoriaTipo) {
        this.categoriaTipo = categoriaTipo;
    }

    public TransacaoTipo getTransacaoTipo() {
        return transacaoTipo;
    }

    public void setTransacaoTipo(TransacaoTipo transacaoTipo) {
        this.transacaoTipo = transacaoTipo;
    }

    public BigDecimal getQuantia() {
        return quantia;
    }

    public void setQuantia(BigDecimal quantia) {
        this.quantia = quantia;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
