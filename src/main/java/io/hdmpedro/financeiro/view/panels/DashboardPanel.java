package io.hdmpedro.financeiro.view.panels;


import io.hdmpedro.financeiro.controller.MainController;
import io.hdmpedro.financeiro.models.Categoria;
import io.hdmpedro.financeiro.models.enums.TransacaoTipo;
import io.hdmpedro.financeiro.util.DataUtil;
import io.hdmpedro.financeiro.util.TemaCores;
import io.hdmpedro.financeiro.util.MoedaUtil;
import io.hdmpedro.financeiro.view.MainFrame;
import io.hdmpedro.financeiro.view.components.BotaoModerno;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class DashboardPanel extends JPanel implements MainFrame.RefreshablePanel {
    private final MainController mainController;

    private JLabel currentBalanceLabel;
    private JLabel totalReserveLabel;
    private JLabel monthlyIncomeLabel;
    private JLabel monthlyExpensesLabel;

    private JPanel alertsPanel;
    private JPanel quickActionsPanel;
    private JPanel summaryCardsPanel;

    public DashboardPanel(MainController mainController) {
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
        createSummaryCards();
        createQuickActions();
        createAlertsPanel();
    }

    private void createSummaryCards() {
        summaryCardsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        summaryCardsPanel.setBackground(TemaCores.BACKGROUND);

        summaryCardsPanel.add(createSummaryCard("Saldo Atual", "R$ 0,00",
                TemaCores.PRIMARY, currentBalanceLabel = new JLabel()));
        summaryCardsPanel.add(createSummaryCard("Reserva Total", "R$ 0,00",
                TemaCores.SUCCESS, totalReserveLabel = new JLabel()));
        summaryCardsPanel.add(createSummaryCard("Entradas do Mês", "R$ 0,00",
                TemaCores.ACCENT, monthlyIncomeLabel = new JLabel()));
        summaryCardsPanel.add(createSummaryCard("Saídas do Mês", "R$ 0,00",
                TemaCores.ERROR, monthlyExpensesLabel = new JLabel()));
    }

    private JPanel createSummaryCard(String title, String initialValue, Color color, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaCores.BORDER, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(TemaCores.TEXT_SECONDARY);

        valueLabel.setText(initialValue);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);

        JPanel colorBar = new JPanel();
        colorBar.setBackground(color);
        colorBar.setPreferredSize(new Dimension(0, 4));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(colorBar, BorderLayout.SOUTH);

        return card;
    }

    private void createQuickActions() {
        quickActionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        quickActionsPanel.setBackground(TemaCores.BACKGROUND);

        BotaoModerno addTransactionBtn = new BotaoModerno("+ Nova Transação", TemaCores.PRIMARY);
        BotaoModerno addToReserveBtn = new BotaoModerno("+ Adicionar à Reserva", TemaCores.SUCCESS);
        BotaoModerno closeMonthBtn = new BotaoModerno("Fechar Mês", TemaCores.WARNING);

        addTransactionBtn.addActionListener(e -> showAddTransactionDialog());
        addToReserveBtn.addActionListener(e -> showAddToReserveDialog());
        closeMonthBtn.addActionListener(e -> showCloseMonthDialog());

        quickActionsPanel.add(addTransactionBtn);
        quickActionsPanel.add(Box.createHorizontalStrut(10));
        quickActionsPanel.add(addToReserveBtn);
        quickActionsPanel.add(Box.createHorizontalStrut(10));
        quickActionsPanel.add(closeMonthBtn);
    }

    private void createAlertsPanel() {
        alertsPanel = new JPanel();
        alertsPanel.setLayout(new BoxLayout(alertsPanel, BoxLayout.Y_AXIS));
        alertsPanel.setBackground(TemaCores.BACKGROUND);
    }

    private void layoutComponents() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(TemaCores.BACKGROUND);
        topPanel.add(summaryCardsPanel, BorderLayout.CENTER);

        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.setBackground(TemaCores.BACKGROUND);
        middlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        middlePanel.add(quickActionsPanel, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(alertsPanel, BorderLayout.SOUTH);
    }

    private void showAddTransactionDialog() {

    }

    private void showAddToReserveDialog() {

    }

    private void showCloseMonthDialog() {

    }

    @Override
    public void refresh() {
        updateSummaryCards();
        updateAlerts();
        revalidate();
        repaint();
    }

    private void updateSummaryCards() {
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();

        BigDecimal currentBalance = mainController.getTransactionController()
                .getMonthlyBalance(currentMonth, currentYear);
        BigDecimal totalReserve = mainController.getReserveController().getTotalReserve();

        BigDecimal monthlyIncome = mainController.getTransactionService()
                .getTotalByType(TransacaoTipo.ENTRADA,
                        currentMonth, currentYear);
        BigDecimal monthlyExpenses = mainController.getTransactionService()
                .getTotalByType(TransacaoTipo.SAIDA,
                        currentMonth, currentYear);

        currentBalanceLabel.setText(MoedaUtil.format(currentBalance));
        currentBalanceLabel.setForeground(TemaCores.getBalanceColor(
                currentBalance.compareTo(BigDecimal.ZERO) >= 0));

        totalReserveLabel.setText(MoedaUtil.format(totalReserve));
        monthlyIncomeLabel.setText(MoedaUtil.format(monthlyIncome));
        monthlyExpensesLabel.setText(MoedaUtil.format(monthlyExpenses));
    }

    private void updateAlerts() {
        alertsPanel.removeAll();

        List<Categoria> overBudgetCategories = mainController.getCategoryController()
                .getOverBudgetCategories();

        if (!overBudgetCategories.isEmpty()) {
            JPanel alertCard = createAlertCard("Atenção: Orçamento Excedido",
                    overBudgetCategories.size() + " categoria(s) excederam o orçamento",
                    TemaCores.WARNING);
            alertsPanel.add(alertCard);
            alertsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        LocalDate now = LocalDate.now();
        if (now.getDayOfMonth() >= 25 && !mainController.getMonthlyClosureController()
                .isMonthClosed(now.getMonthValue(), now.getYear())) {
            JPanel alertCard = createAlertCard("Lembrete",
                    "Considere fechar o mês de " + DataUtil.formatFullMonthYear(now),
                    TemaCores.PRIMARY);
            alertsPanel.add(alertCard);
        }
    }

    private JPanel createAlertCard(String title, String message, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(TemaCores.withAlpha(color, 50));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(color);

        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageLabel.setForeground(TemaCores.TEXT_SECONDARY);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(TemaCores.withAlpha(color, 50));
        textPanel.add(titleLabel);
        textPanel.add(messageLabel);

        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }
}
