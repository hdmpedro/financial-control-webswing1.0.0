package io.hdmpedro.financeiro.view;

// teste commit
import io.hdmpedro.financeiro.controller.MainController;
import io.hdmpedro.financeiro.util.TemaCores;
import io.hdmpedro.financeiro.view.components.BotaoModerno;
import io.hdmpedro.financeiro.view.panels.*;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final MainController mainController;
    private JPanel contentPanel;
    private JPanel sidebarPanel;

    private DashboardPanel dashboardPanel;
  //  private TransactionPanel transactionPanel;
    private CalendarioPanel calendarioPanel;
    private CategoriaPanel categoriaPanel;
    private ReservaPanel reservaPanel;
    private RelatoriosPanel relatoriosPanel;

    private BotaoModerno currentSelectedButton;

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

        getContentPane().setBackground(TemaCores.BACKGROUND);
    }

    private void createComponents() {
        dashboardPanel = new DashboardPanel(mainController);
        //transactionPanel = new TransactionPanel(mainController);
        calendarioPanel = new CalendarioPanel(mainController);
        categoriaPanel = new CategoriaPanel(mainController);
        reservaPanel = new ReservaPanel(mainController);
        relatoriosPanel = new RelatoriosPanel(mainController);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(TemaCores.BACKGROUND);

        createSidebar();
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(TemaCores.PRIMARY);
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
        versionLabel.setForeground(TemaCores.withAlpha(Color.WHITE, 150));
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebarPanel.add(versionLabel);
    }

    private void addSidebarButton(String text, Runnable action) {
        BotaoModerno button = new BotaoModerno(text, TemaCores.PRIMARY);
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

    private void setSelectedButton(BotaoModerno button) {
        if (currentSelectedButton != null) {
            currentSelectedButton.setBackground(TemaCores.PRIMARY);
        }

        currentSelectedButton = button;
        button.setBackground(TemaCores.PRIMARY_LIGHT);
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
        setContentPanel(calendarioPanel, "Calendário Financeiro");
    }

    private void showCategories() {
        setContentPanel(categoriaPanel, "Gerenciar Categorias");
    }

    private void showReserve() {
        setContentPanel(reservaPanel, "Reserva de Emergência");
    }

    private void showReports() {
        setContentPanel(relatoriosPanel, "Relatórios e Análises");
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
                BorderFactory.createMatteBorder(0, 0, 1, 0, TemaCores.BORDER),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TemaCores.TEXT_PRIMARY);

        headerPanel.add(titleLabel, BorderLayout.WEST);

        return headerPanel;
    }

    public void refreshAllPanels() {
        if (dashboardPanel != null) dashboardPanel.refresh();
     //   if (transactionPanel != null) transactionPanel.refresh();
        if (calendarioPanel != null) calendarioPanel.refresh();
        if (categoriaPanel != null) categoriaPanel.refresh();
        if (reservaPanel != null) reservaPanel.refresh();
        if (relatoriosPanel != null) relatoriosPanel.refresh();
    }

    public interface RefreshablePanel {
        void refresh();
    }
}