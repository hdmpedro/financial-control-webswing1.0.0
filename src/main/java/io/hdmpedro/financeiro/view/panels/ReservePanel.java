package io.hdmpedro.financeiro.view.panels;


import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import io.hdmpedro.financeiro.controller.MainController;
import io.hdmpedro.financeiro.models.Category;
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
import io.hdmpedro.financeiro.view.components.ModernTextField;

public class ReservePanel extends JPanel implements MainFrame.RefreshablePanel {
    private final MainController mainController;
    private JLabel totalReserveLabel;
    private JTable reserveTable;
    private ReserveTableModel tableModel;
    private ModernTextField amountField;
    private ModernTextField descriptionField;

    public ReservePanel(MainController mainController) {
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
        createSummaryPanel();
        createAddReserveForm();
        createReserveTable();
    }

    private void createSummaryPanel() {
        totalReserveLabel = new JLabel("R$ 0,00");
        totalReserveLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        totalReserveLabel.setForeground(ColorTheme.SUCCESS);
    }

    private void createAddReserveForm() {
        amountField = new ModernTextField("Valor");
        descriptionField = new ModernTextField("Descrição");
    }

    private void createReserveTable() {
        tableModel = new ReserveTableModel();
        reserveTable = new JTable(tableModel);
        reserveTable.setRowHeight(40);
        reserveTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        reserveTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        reserveTable.getTableHeader().setBackground(ColorTheme.SURFACE_VARIANT);
        reserveTable.setGridColor(ColorTheme.BORDER);

        reserveTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    setBackground(row % 2 == 0 ? Color.WHITE : ColorTheme.SURFACE_VARIANT);
                }

                if (column == 1) {
                    setForeground(isSelected ? Color.WHITE : ColorTheme.SUCCESS);
                    setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    setForeground(isSelected ? Color.WHITE : ColorTheme.TEXT_PRIMARY);
                    setHorizontalAlignment(SwingConstants.LEFT);
                }

                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return this;
            }
        });
    }

    private void layoutComponents() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(ColorTheme.BACKGROUND);

        JPanel summaryCard = new JPanel(new BorderLayout());
        summaryCard.setBackground(Color.WHITE);
        summaryCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorTheme.BORDER, 1),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        JLabel titleLabel = new JLabel("Total da Reserva");
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        titleLabel.setForeground(ColorTheme.TEXT_SECONDARY);

        summaryCard.add(titleLabel, BorderLayout.NORTH);
        summaryCard.add(totalReserveLabel, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorTheme.BORDER, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Valor:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(amountField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(descriptionField, gbc);

        ModernButton addButton = new ModernButton("Adicionar à Reserva", ColorTheme.SUCCESS);
        addButton.addActionListener(e -> addToReserve());

        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE; gbc.insets = new Insets(15, 5, 5, 5);
        formPanel.add(addButton, gbc);

        JPanel topLeftPanel = new JPanel(new BorderLayout());
        topLeftPanel.setBackground(ColorTheme.BACKGROUND);
        topLeftPanel.add(summaryCard, BorderLayout.CENTER);

        JPanel topRightPanel = new JPanel(new BorderLayout());
        topRightPanel.setBackground(ColorTheme.BACKGROUND);
        topRightPanel.add(formPanel, BorderLayout.CENTER);

        topPanel.add(topLeftPanel, BorderLayout.WEST);
        topPanel.add(Box.createHorizontalStrut(20), BorderLayout.CENTER);
        topPanel.add(topRightPanel, BorderLayout.EAST);

        JScrollPane scrollPane = new JScrollPane(reserveTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorTheme.BORDER, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(topPanel, BorderLayout.NORTH);
        add(Box.createVerticalStrut(20), BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }

    private void addToReserve() {
        try {
            BigDecimal amount = CurrencyUtil.parse(amountField.getText());
            String description = descriptionField.getText();

            mainController.getReserveController().addToReserve(amount, description);

            amountField.setText("");
            descriptionField.setText("");

            refresh();
            mainController.refreshAllViews();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao adicionar à reserva: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void refresh() {
        BigDecimal totalReserve = mainController.getReserveController().getTotalReserve();
        totalReserveLabel.setText(CurrencyUtil.format(totalReserve));

        List<Reserve> reserves = mainController.getReserveController().getAllReserves();
        tableModel.setReserves(reserves);

        revalidate();
        repaint();
    }

    private static class ReserveTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Data", "Valor", "Descrição"};
        private List<Reserve> reserves = new java.util.ArrayList<>();

        public void setReserves(List<Reserve> reserves) {
            this.reserves = reserves != null ? reserves : new java.util.ArrayList<>();
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return reserves.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Reserve reserve = reserves.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> DateUtils.formatDate(reserve.getDate());
                case 1 -> CurrencyUtil.format(reserve.getAmount());
                case 2 -> reserve.getDescription();
                default -> null;
            };
        }
    }
}