package io.hdmpedro.financeiro.models.enums;


public enum CategoriaTipo {
    TRANSPORTE("Transporte", "#2196F3"),
    ALIMENTACAO("Alimentação", "#FF9800"),
    MORADIA("Moradia", "#9C27B0"),
    SAUDE("Saúde", "#4CAF50"),
    EDUCACAO("Educação", "#3F51B5"),
    LAZER("Lazer", "#E91E63"),
    VESTUARIO("Vestuário", "#00BCD4"),
    SERVICOS("Serviços", "#795548"),
    OUTROS("Outros", "#607D8B"),
    SALARIO("Salário", "#4CAF50"),
    FREELANCE("Freelance", "#8BC34A"),
    INVESTIMENTOS("Investimentos", "#FFC107");

    private final String displayName;
    private final String color;

    CategoriaTipo(String displayName, String color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() { return displayName; }
    public String getColor() { return color; }
}