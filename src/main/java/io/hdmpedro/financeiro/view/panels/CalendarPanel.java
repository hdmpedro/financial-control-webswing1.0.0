package io.hdmpedro.financeiro.view.panels;


import io.hdmpedro.financeiro.controller.MainController;
import io.hdmpedro.financeiro.models.DailyBalance;
import io.hdmpedro.financeiro.util.ColorTheme;
import io.hdmpedro.financeiro.util.CurrencyUtil;
import io.hdmpedro.financeiro.view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class CalendarPanel extends JPanel implements MainFrame.RefreshablePanel {
    private final MainController mainController;
    private JPanel calendarGrid;
    private JComboBox<String> monthCombo;
    private JComboBox<Integer> yearCombo;
    private LocalDate selectedDate;

    public CalendarPanel(MainController mainController) {
        this.mainController = mainController;
        this.selectedDate = LocalDate.now();
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
        createDateSelectors();
        createCalendarGrid();
    }

    private void createDateSelectors() {
        String[] months = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
                "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        monthCombo = new JComboBox<>(months);
        monthCombo.setSelectedIndex(selectedDate.getMonthValue() - 1);

        yearCombo = new JComboBox<>();
        int currentYear = LocalDate.now().getYear();
        for (int year = currentYear - 2; year <= currentYear + 1; year++) {
            yearCombo.addItem(year);
        }
        yearCombo.setSelectedItem(selectedDate.getYear());

        monthCombo.addActionListener(this::onDateChanged);
        yearCombo.addActionListener(this::onDateChanged);
    }

    private void createCalendarGrid() {
        calendarGrid = new JPanel(new GridLayout(7, 7, 2, 2));
        calendarGrid.setBackground(ColorTheme.BACKGROUND);

        String[] dayNames = {"Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"};
        for (String dayName : dayNames) {
            JLabel label = new JLabel(dayName, SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setForeground(ColorTheme.TEXT_SECONDARY);
            label.setBackground(ColorTheme.SURFACE_VARIANT);
            label.setOpaque(true);
            label.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
            calendarGrid.add(label);
        }
    }

    private void layoutComponents() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(ColorTheme.BACKGROUND);
        topPanel.add(new JLabel("Mês:"));
        topPanel.add(monthCombo);
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(new JLabel("Ano:"));
        topPanel.add(yearCombo);

        add(topPanel, BorderLayout.NORTH);
        add(calendarGrid, BorderLayout.CENTER);
    }

    private void onDateChanged(ActionEvent e) {
        int month = monthCombo.getSelectedIndex() + 1;
        int year = (Integer) yearCombo.getSelectedItem();
        selectedDate = LocalDate.of(year, month, 1);
        updateCalendar();
    }

    private void updateCalendar() {
        calendarGrid.removeAll();

        String[] dayNames = {"Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"};
        for (String dayName : dayNames) {
            JLabel label = new JLabel(dayName, SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setForeground(ColorTheme.TEXT_SECONDARY);
            label.setBackground(ColorTheme.SURFACE_VARIANT);
            label.setOpaque(true);
            label.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
            calendarGrid.add(label);
        }

        YearMonth yearMonth = YearMonth.of(selectedDate.getYear(), selectedDate.getMonth());
        LocalDate firstDay = yearMonth.atDay(1);
        int startDayOfWeek = firstDay.getDayOfWeek().getValue() % 7;

        for (int i = 0; i < startDayOfWeek; i++) {
            calendarGrid.add(createEmptyDayPanel());
        }

        List<DailyBalance> dailyBalances = mainController.getCalendarService()
                .getMonthlyCalendar(selectedDate.getMonthValue(), selectedDate.getYear());

        for (DailyBalance dailyBalance : dailyBalances) {
            calendarGrid.add(createDayPanel(dailyBalance));
        }

        while (calendarGrid.getComponentCount() < 49) {
            calendarGrid.add(createEmptyDayPanel());
        }

        calendarGrid.revalidate();
        calendarGrid.repaint();
    }

    private JPanel createDayPanel(DailyBalance dailyBalance) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(ColorTheme.BORDER, 1));
        panel.setPreferredSize(new Dimension(100, 80));

        JLabel dayLabel = new JLabel(String.valueOf(dailyBalance.getDate().getDayOfMonth()),
                SwingConstants.CENTER);
        dayLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        if (dailyBalance.getDate().equals(LocalDate.now())) {
            dayLabel.setForeground(Color.WHITE);
            dayLabel.setOpaque(true);
            dayLabel.setBackground(ColorTheme.PRIMARY);
        } else {
            dayLabel.setForeground(ColorTheme.TEXT_PRIMARY);
        }

        JPanel balancePanel = new JPanel();
        balancePanel.setLayout(new BoxLayout(balancePanel, BoxLayout.Y_AXIS));
        balancePanel.setBackground(Color.WHITE);

        if (!dailyBalance.getBalance().equals(java.math.BigDecimal.ZERO)) {
            JLabel balanceLabel = new JLabel(CurrencyUtil.format(dailyBalance.getBalance()),
                    SwingConstants.CENTER);
            balanceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            balanceLabel.setForeground(ColorTheme.getBalanceColor(
                    dailyBalance.getBalance().compareTo(java.math.BigDecimal.ZERO) >= 0));
            balancePanel.add(balanceLabel);
        }

        boolean hasTransactions = mainController.getCalendarService()
                .hasTransactionsOnDate(dailyBalance.getDate());
        if (hasTransactions) {
            panel.setBackground(ColorTheme.withAlpha(ColorTheme.PRIMARY, 30));
        }

        panel.add(dayLabel, BorderLayout.NORTH);
        panel.add(balancePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createEmptyDayPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(ColorTheme.SURFACE_VARIANT);
        panel.setBorder(BorderFactory.createLineBorder(ColorTheme.BORDER, 1));
        panel.setPreferredSize(new Dimension(100, 80));
        return panel;
    }

    @Override
    public void refresh() {
        updateCalendar();
    }
}
