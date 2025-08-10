package io.hdmpedro.financeiro.models.enums;


public enum TransacaoTipo {
    ENTRADA("Entrada", "#4CAF50"),
    SAIDA("Saída", "#F44336");

    private final String displayName;
    private final String color;

    TransacaoTipo(String displayName, String color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() { return displayName; }
    public String getColor() { return color; }
}