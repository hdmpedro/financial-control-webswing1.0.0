package io.hdmpedro.financeiro.view.panels;


import io.hdmpedro.financeiro.controller.MainController;
import io.hdmpedro.financeiro.models.Categoria;
import io.hdmpedro.financeiro.models.enums.CategoriaTipo;
import io.hdmpedro.financeiro.util.MoedaUtil;
import io.hdmpedro.financeiro.util.TemaCores;
import io.hdmpedro.financeiro.view.MainFrame;
import io.hdmpedro.financeiro.view.components.BotaoModerno;
import io.hdmpedro.financeiro.view.components.CampoTextoModerno;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class CategoriaPanel extends JPanel implements MainFrame.RefreshablePanel {
    private final MainController mainController;
    private JPanel categoriesPanel;

    public CategoriaPanel(MainController mainController) {
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
        categoriesPanel = new JPanel();
        categoriesPanel.setLayout(new BoxLayout(categoriesPanel, BoxLayout.Y_AXIS));
        categoriesPanel.setBackground(TemaCores.BACKGROUND);
    }

    private void layoutComponents() {
        JScrollPane scrollPane = new JScrollPane(categoriesPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void updateCategories() {
        categoriesPanel.removeAll();

        List<Categoria> categories = mainController.getCategoryController().getAllCategories();

        for (Categoria categoria : categories) {
            categoriesPanel.add(createCategoryCard(categoria));
            categoriesPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        categoriesPanel.revalidate();
        categoriesPanel.repaint();
    }

    private JPanel createCategoryCard(Categoria categoria) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaCores.BORDER, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(categoria.getType().getDisplayName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(TemaCores.TEXT_PRIMARY);

        JPanel colorIndicator = new JPanel();
        colorIndicator.setBackground(Color.decode(categoria.getType().getColor()));
        colorIndicator.setPreferredSize(new Dimension(4, 20));

        headerPanel.add(colorIndicator, BorderLayout.WEST);
        headerPanel.add(Box.createHorizontalStrut(10), BorderLayout.CENTER);
        headerPanel.add(nameLabel, BorderLayout.CENTER);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(new JLabel("Orçamento:"), gbc);

        CampoTextoModerno budgetField = new CampoTextoModerno(
                MoedaUtil.format(categoria.getBudgetLimit()));
        budgetField.setPreferredSize(new Dimension(150, 35));
        gbc.gridx = 1;
        contentPanel.add(budgetField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(new JLabel("Gasto:"), gbc);

        JLabel spentLabel = new JLabel(MoedaUtil.format(categoria.getCurrentSpent()));
        spentLabel.setForeground(TemaCores.ERROR);
        gbc.gridx = 1;
        contentPanel.add(spentLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        contentPanel.add(new JLabel("Restante:"), gbc);

        JLabel remainingLabel = new JLabel(MoedaUtil.format(categoria.getRemainingBudget()));
        remainingLabel.setForeground(TemaCores.getBalanceColor(
                categoria.getRemainingBudget().compareTo(BigDecimal.ZERO) >= 0));
        gbc.gridx = 1;
        contentPanel.add(remainingLabel, gbc);

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue((int) categoria.getBudgetUsagePercentage());
        progressBar.setStringPainted(true);
        progressBar.setString(String.format("%.1f%%", categoria.getBudgetUsagePercentage()));

        Color progressColor = categoria.getBudgetUsagePercentage() > 100 ? TemaCores.ERROR :
                categoria.getBudgetUsagePercentage() > 80 ? TemaCores.WARNING :
                        TemaCores.SUCCESS;
        progressBar.setForeground(progressColor);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(progressBar, gbc);

        BotaoModerno updateButton = new BotaoModerno("Atualizar Orçamento", TemaCores.PRIMARY);
        updateButton.addActionListener(e -> updateCategoryBudget(categoria.getType(), budgetField));

        gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.EAST;
        contentPanel.add(updateButton, gbc);

        card.add(headerPanel, BorderLayout.NORTH);
        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private void updateCategoryBudget(CategoriaTipo categoriaTipo, CampoTextoModerno budgetField) {
        try {
            BigDecimal budget = MoedaUtil.parse(budgetField.getText());
            mainController.getCategoryController().updateCategoryBudget(categoriaTipo, budget);
            refresh();
            JOptionPane.showMessageDialog(this, "Orçamento atualizado com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar orçamento: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void refresh() {
        updateCategories();
    }
}