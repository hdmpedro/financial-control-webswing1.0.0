package io.hdmpedro.financeiro.view;


import io.hdmpedro.financeiro.controller.MainController;
import io.hdmpedro.financeiro.util.ColorTheme;
import io.hdmpedro.financeiro.view.components.ModernButton;
import io.hdmpedro.financeiro.view.panels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {
    private final MainController mainController;
    private JPanel contentPanel;
    private JPanel sidebarPanel;

    private DashboardPanel dashboardPanel;
  //  private TransactionPanel transactionPanel;
    private CalendarPanel calendarPanel;
    private CategoryPanel categoryPanel;
    private ReservePanel reservePanel;
    private ReportsPanel reportsPanel;

    private ModernButton currentSelectedButton;

    public MainFrame(MainController mainController) {
        this.mainController = mainController;
        initializeFrame();
        createComponents();
        layoutComponents();
        setDefaultView();
    }

    private void initializeFrame() {
        setTitle("Controle Financeiro Pessoal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1200, 800));

        getContentPane().setBackground(ColorTheme.BACKGROUND);
    }

    private void createComponents() {
        dashboardPanel = new DashboardPanel(mainController);
        //transactionPanel = new TransactionPanel(mainController);
        calendarPanel = new CalendarPanel(mainController);
        categoryPanel = new CategoryPanel(mainController);
        reservePanel = new ReservePanel(mainController);
        reportsPanel = new ReportsPanel(mainController);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(ColorTheme.BACKGROUND);

        createSidebar();
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(ColorTheme.PRIMARY);
        sidebarPanel.setPreferredSize(new Dimension(250, 0));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel titleLabel = new JLabel("Controle Financeiro");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        sidebarPanel.add(titleLabel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        addSidebarButton("Dashboard", this::showDashboard);
      //  addSidebarButton("Transações", this::showTransactions);
        addSidebarButton("Calendário", this::showCalendar);
        addSidebarButton("Categorias", this::showCategories);
        addSidebarButton("Reserva", this::showReserve);
        addSidebarButton("Relatórios", this::showReports);

        sidebarPanel.add(Box.createVerticalGlue());

        JLabel versionLabel = new JLabel("v1.0.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        versionLabel.setForeground(ColorTheme.withAlpha(Color.WHITE, 150));
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebarPanel.add(versionLabel);
    }

    private void addSidebarButton(String text, Runnable action) {
        ModernButton button = new ModernButton(text, ColorTheme.PRIMARY);
        button.setMaximumSize(new Dimension(200, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        button.addActionListener(e -> {
            setSelectedButton(button);
            action.run();
        });

        sidebarPanel.add(button);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        if (currentSelectedButton == null) {
            setSelectedButton(button);
        }
    }

    private void setSelectedButton(ModernButton button) {
        if (currentSelectedButton != null) {
            currentSelectedButton.setBackground(ColorTheme.PRIMARY);
        }

        currentSelectedButton = button;
        button.setBackground(ColorTheme.PRIMARY_LIGHT);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void setDefaultView() {
        showDashboard();
    }

    private void showDashboard() {
        setContentPanel(dashboardPanel, "Dashboard");
    }

  //  private void showTransactions() {
  //      setContentPanel(transactionPanel, "Transações");
  ///  }

    private void showCalendar() {
        setContentPanel(calendarPanel, "Calendário Financeiro");
    }

    private void showCategories() {
        setContentPanel(categoryPanel, "Gerenciar Categorias");
    }

    private void showReserve() {
        setContentPanel(reservePanel, "Reserva de Emergência");
    }

    private void showReports() {
        setContentPanel(reportsPanel, "Relatórios e Análises");
    }

    private void setContentPanel(JPanel panel, String title) {
        contentPanel.removeAll();

        JPanel headerPanel = createHeaderPanel(title);
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(panel, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();

        if (panel instanceof RefreshablePanel) {
            ((RefreshablePanel) panel).refresh();
        }
    }

    private JPanel createHeaderPanel(String title) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, ColorTheme.BORDER),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ColorTheme.TEXT_PRIMARY);

        headerPanel.add(titleLabel, BorderLayout.WEST);

        return headerPanel;
    }

    public void refreshAllPanels() {
        if (dashboardPanel != null) dashboardPanel.refresh();
     //   if (transactionPanel != null) transactionPanel.refresh();
        if (calendarPanel != null) calendarPanel.refresh();
        if (categoryPanel != null) categoryPanel.refresh();
        if (reservePanel != null) reservePanel.refresh();
        if (reportsPanel != null) reportsPanel.refresh();
    }

    public interface RefreshablePanel {
        void refresh();
    }
}