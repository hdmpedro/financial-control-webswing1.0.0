package io.hdmpedro.financeiro.view.panels;


import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.hdmpedro.financeiro.controller.MainController;
import io.hdmpedro.financeiro.models.RelatorioMensal;
import io.hdmpedro.financeiro.util.MoedaUtil;
import io.hdmpedro.financeiro.util.TemaCores;
import io.hdmpedro.financeiro.view.MainFrame;
import io.hdmpedro.financeiro.view.components.BotaoModerno;
import io.hdmpedro.financeiro.view.components.ChartPanelModerno;

public class RelatoriosPanel extends JPanel implements MainFrame.RefreshablePanel {
    private final MainController mainController;
    private JComboBox<Integer> yearCombo;
    private ChartPanelModerno chartPanel;
    private JPanel summaryPanel;

    public RelatoriosPanel(MainController mainController) {
        this.mainController = mainController;
        initializePanel();
        createComponents();
        layoutComponents();
        refresh();

    }

    private void initializePanel() {
        setLayout(new BorderLayout());
        setBackground(TemaCores.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
    }

    private void createComponents() {
        createYearSelector();
        createChartPanel();
        createSummaryPanel();
    }

    private void createYearSelector() {
        yearCombo = new JComboBox<>();
        int currentYear = LocalDate.now().getYear();
        for (int year = currentYear - 2; year <= currentYear; year++) {
            yearCombo.addItem(year);
        }
        yearCombo.setSelectedItem(currentYear);
        yearCombo.addActionListener(e -> refresh());
    }

    private void createChartPanel() {
        chartPanel = new ChartPanelModerno();
        chartPanel.setPreferredSize(new Dimension(600, 400));
    }

    private void createSummaryPanel() {
        summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBackground(TemaCores.BACKGROUND);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
    }

    private void layoutComponents() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(TemaCores.BACKGROUND);
        topPanel.add(new JLabel("Ano:"));
        topPanel.add(yearCombo);

        BotaoModerno generateReportBtn = new BotaoModerno("Gerar Relatório", TemaCores.PRIMARY);
        generateReportBtn.addActionListener(e -> refresh());
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(generateReportBtn);

        add(topPanel, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
        add(summaryPanel, BorderLayout.SOUTH);
    }

    @Override
    public void refresh() {
        int selectedYear = (Integer) yearCombo.getSelectedItem();
        updateCharts(selectedYear);
        updateSummary(selectedYear);
        revalidate();
        repaint();
    }

    private void updateCharts(int year) {
        List<RelatorioMensal> reports = mainController.getMonthlyClosureController()
                .getClosedMonthsByYear(year);

        if (reports.isEmpty()) {
            chartPanel.removeAll();
            JLabel noDataLabel = new JLabel("Nenhum relatório disponível para " + year,
                    SwingConstants.CENTER);
            noDataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            noDataLabel.setForeground(TemaCores.TEXT_SECONDARY);
            chartPanel.add(noDataLabel, BorderLayout.CENTER);
            return;
        }

        Map<String, BigDecimal> monthlyData = new HashMap<>();
        String[] monthNames = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun",
                "Jul", "Ago", "Set", "Out", "Nov", "Dez"};

        for (RelatorioMensal report : reports) {
            String monthName = monthNames[report.getMonth() - 1];
            monthlyData.put(monthName, report.getFinalBalance());
        }

        chartPanel.createBarChart("Saldo Mensal - " + year,
                "Mês", "Saldo (R$)", monthlyData);
    }

    private void updateSummary(int year) {
        summaryPanel.removeAll();

        List<RelatorioMensal> reports = mainController.getMonthlyClosureController()
                .getClosedMonthsByYear(year);

        if (reports.isEmpty()) {
            return;
        }

        BigDecimal totalIncome = reports.stream()
                .map(RelatorioMensal::getTotalIncome)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = reports.stream()
                .map(RelatorioMensal::getTotalExpenses)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalBalance = totalIncome.subtract(totalExpenses);

        BigDecimal totalReserveContributions = reports.stream()
                .map(r -> r.getReserveContribution() != null ? r.getReserveContribution() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        JPanel summaryCard = new JPanel(new GridLayout(2, 4, 20, 10));
        summaryCard.setBackground(Color.WHITE);
        summaryCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaCores.BORDER, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        summaryCard.add(createSummaryItem("Total de Entradas", MoedaUtil.format(totalIncome),
                TemaCores.SUCCESS));
        summaryCard.add(createSummaryItem("Total de Saídas", MoedaUtil.format(totalExpenses),
                TemaCores.ERROR));
        summaryCard.add(createSummaryItem("Saldo do Ano", MoedaUtil.format(totalBalance),
                TemaCores.getBalanceColor(totalBalance.compareTo(BigDecimal.ZERO) >= 0)));
        summaryCard.add(createSummaryItem("Reserva Acumulada", MoedaUtil.format(totalReserveContributions),
                TemaCores.PRIMARY));

        summaryCard.add(createSummaryItem("Meses Fechados", String.valueOf(reports.size()),
                TemaCores.TEXT_PRIMARY));
        summaryCard.add(createSummaryItem("Média Mensal",
                MoedaUtil.format(totalBalance.divide(BigDecimal.valueOf(Math.max(1, reports.size())), 2, java.math.RoundingMode.HALF_UP)),
                TemaCores.TEXT_PRIMARY));
        summaryCard.add(createSummaryItem("Maior Saldo",
                MoedaUtil.format(reports.stream().map(RelatorioMensal::getFinalBalance).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO)),
                TemaCores.SUCCESS));
        summaryCard.add(createSummaryItem("Menor Saldo",
                MoedaUtil.format(reports.stream().map(RelatorioMensal::getFinalBalance).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO)),
                TemaCores.ERROR));

        JLabel titleLabel = new JLabel("Resumo Anual - " + year);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TemaCores.TEXT_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        summaryPanel.add(titleLabel);
        summaryPanel.add(summaryCard);
    }

    private JPanel createSummaryItem(String label, String value, Color valueColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelComponent.setForeground(TemaCores.TEXT_SECONDARY);

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueComponent.setForeground(valueColor);

        panel.add(labelComponent, BorderLayout.NORTH);
        panel.add(valueComponent, BorderLayout.CENTER);

        return panel;
    }
}