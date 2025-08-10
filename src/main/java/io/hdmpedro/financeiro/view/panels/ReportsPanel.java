package io.hdmpedro.financeiro.view.panels;


import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.hdmpedro.financeiro.controller.MainController;
import io.hdmpedro.financeiro.models.Category;
import io.hdmpedro.financeiro.models.MonthlyReport;
import io.hdmpedro.financeiro.models.Reserve;
import io.hdmpedro.financeiro.models.enums.CategoryType;
import io.hdmpedro.financeiro.util.ColorTheme;
import io.hdmpedro.financeiro.util.DateUtils;
import io.hdmpedro.financeiro.view.MainFrame;
import io.hdmpedro.financeiro.controller.MainController;
import io.hdmpedro.financeiro.models.DailyBalance;
import io.hdmpedro.financeiro.util.ColorTheme;
import io.hdmpedro.financeiro.util.CurrencyUtil;
import io.hdmpedro.financeiro.view.MainFrame;
import io.hdmpedro.financeiro.view.components.ModernButton;
import io.hdmpedro.financeiro.view.components.ModernChartPanel;
import io.hdmpedro.financeiro.view.components.ModernTextField;

public class ReportsPanel extends JPanel implements MainFrame.RefreshablePanel {
    private final MainController mainController;
    private JComboBox<Integer> yearCombo;
    private ModernChartPanel chartPanel;
    private JPanel summaryPanel;

    public ReportsPanel(MainController mainController) {
        this.mainController = mainController;
        initializePanel();
        createComponents();
        layoutComponents();
        refresh();

    }

    private void initializePanel() {
        setLayout(new BorderLayout());
        setBackground(ColorTheme.BACKGROUND);
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
        chartPanel = new ModernChartPanel();
        chartPanel.setPreferredSize(new Dimension(600, 400));
    }

    private void createSummaryPanel() {
        summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBackground(ColorTheme.BACKGROUND);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
    }

    private void layoutComponents() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(ColorTheme.BACKGROUND);
        topPanel.add(new JLabel("Ano:"));
        topPanel.add(yearCombo);

        ModernButton generateReportBtn = new ModernButton("Gerar Relatório", ColorTheme.PRIMARY);
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
        List<MonthlyReport> reports = mainController.getMonthlyClosureController()
                .getClosedMonthsByYear(year);

        if (reports.isEmpty()) {
            chartPanel.removeAll();
            JLabel noDataLabel = new JLabel("Nenhum relatório disponível para " + year,
                    SwingConstants.CENTER);
            noDataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            noDataLabel.setForeground(ColorTheme.TEXT_SECONDARY);
            chartPanel.add(noDataLabel, BorderLayout.CENTER);
            return;
        }

        Map<String, BigDecimal> monthlyData = new HashMap<>();
        String[] monthNames = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun",
                "Jul", "Ago", "Set", "Out", "Nov", "Dez"};

        for (MonthlyReport report : reports) {
            String monthName = monthNames[report.getMonth() - 1];
            monthlyData.put(monthName, report.getFinalBalance());
        }

        chartPanel.createBarChart("Saldo Mensal - " + year,
                "Mês", "Saldo (R$)", monthlyData);
    }

    private void updateSummary(int year) {
        summaryPanel.removeAll();

        List<MonthlyReport> reports = mainController.getMonthlyClosureController()
                .getClosedMonthsByYear(year);

        if (reports.isEmpty()) {
            return;
        }

        BigDecimal totalIncome = reports.stream()
                .map(MonthlyReport::getTotalIncome)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = reports.stream()
                .map(MonthlyReport::getTotalExpenses)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalBalance = totalIncome.subtract(totalExpenses);

        BigDecimal totalReserveContributions = reports.stream()
                .map(r -> r.getReserveContribution() != null ? r.getReserveContribution() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        JPanel summaryCard = new JPanel(new GridLayout(2, 4, 20, 10));
        summaryCard.setBackground(Color.WHITE);
        summaryCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorTheme.BORDER, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        summaryCard.add(createSummaryItem("Total de Entradas", CurrencyUtil.format(totalIncome),
                ColorTheme.SUCCESS));
        summaryCard.add(createSummaryItem("Total de Saídas", CurrencyUtil.format(totalExpenses),
                ColorTheme.ERROR));
        summaryCard.add(createSummaryItem("Saldo do Ano", CurrencyUtil.format(totalBalance),
                ColorTheme.getBalanceColor(totalBalance.compareTo(BigDecimal.ZERO) >= 0)));
        summaryCard.add(createSummaryItem("Reserva Acumulada", CurrencyUtil.format(totalReserveContributions),
                ColorTheme.PRIMARY));

        summaryCard.add(createSummaryItem("Meses Fechados", String.valueOf(reports.size()),
                ColorTheme.TEXT_PRIMARY));
        summaryCard.add(createSummaryItem("Média Mensal",
                CurrencyUtil.format(totalBalance.divide(BigDecimal.valueOf(Math.max(1, reports.size())), 2, java.math.RoundingMode.HALF_UP)),
                ColorTheme.TEXT_PRIMARY));
        summaryCard.add(createSummaryItem("Maior Saldo",
                CurrencyUtil.format(reports.stream().map(MonthlyReport::getFinalBalance).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO)),
                ColorTheme.SUCCESS));
        summaryCard.add(createSummaryItem("Menor Saldo",
                CurrencyUtil.format(reports.stream().map(MonthlyReport::getFinalBalance).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO)),
                ColorTheme.ERROR));

        JLabel titleLabel = new JLabel("Resumo Anual - " + year);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(ColorTheme.TEXT_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        summaryPanel.add(titleLabel);
        summaryPanel.add(summaryCard);
    }

    private JPanel createSummaryItem(String label, String value, Color valueColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelComponent.setForeground(ColorTheme.TEXT_SECONDARY);

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueComponent.setForeground(valueColor);

        panel.add(labelComponent, BorderLayout.NORTH);
        panel.add(valueComponent, BorderLayout.CENTER);

        return panel;
    }
}