package io.hdmpedro.financeiro.view.dialogs;

import io.hdmpedro.financeiro.controller.MainController;
import io.hdmpedro.financeiro.controller.TransacaoController;
import io.hdmpedro.financeiro.models.Transacao;
import io.hdmpedro.financeiro.models.enums.CategoriaTipo;
import io.hdmpedro.financeiro.models.enums.TransacaoTipo;
import io.hdmpedro.financeiro.util.TemaCores;
import io.hdmpedro.financeiro.view.components.BotaoModerno;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class NovaTransacaoDialog extends JDialog {
    private final TransacaoController transacaoController;
    private final Runnable onSuccessCallback;

    private JTextField descricaoField;
    private JTextField valorField;
    private JComboBox<TransacaoTipo> tipoComboBox;
    private JComboBox<CategoriaTipo> categoriaComboBox;
    private JTextField dataField;
    private JButton salvarButton;
    private JButton cancelarButton;

    public NovaTransacaoDialog(Component parent, TransacaoController transacaoController,
                               Runnable onSuccessCallback) {
        super(SwingUtilities.getWindowAncestor(parent), "Nova Transação", Dialog.ModalityType.APPLICATION_MODAL);
        this.transacaoController = transacaoController;
        this.onSuccessCallback = onSuccessCallback;

        initializeComponents();
        setupLayout();
        setupEventListeners();
        setupDialog();
    }

    private void initializeComponents() {
        descricaoField = new JTextField(20);
        valorField = new JTextField(10);
        tipoComboBox = new JComboBox<>(TransacaoTipo.values());
        categoriaComboBox = new JComboBox<>(CategoriaTipo.values());

        dataField = new JTextField(10);
        dataField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        salvarButton = new JButton("Salvar");
        cancelarButton = new JButton("Cancelar");

        salvarButton.setPreferredSize(new Dimension(100, 30));
        cancelarButton.setPreferredSize(new Dimension(100, 30));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(descricaoField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Valor (R$):"), gbc);
        gbc.gridx = 1;
        mainPanel.add(valorField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(tipoComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(categoriaComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Data:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(dataField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(salvarButton);
        buttonPanel.add(cancelarButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupEventListeners() {
        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarTransacao();
            }
        });

        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        tipoComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCategoriaComboBox();
            }
        });
    }

    private void updateCategoriaComboBox() {
        TransacaoTipo selectedType = (TransacaoTipo) tipoComboBox.getSelectedItem();
        categoriaComboBox.removeAllItems();

        for (CategoriaTipo categoria : CategoriaTipo.values()) {
            if (selectedType == TransacaoTipo.ENTRADA && isReceitaCategory(categoria) ||
                    selectedType == TransacaoTipo.SAIDA && !isReceitaCategory(categoria)) {
                categoriaComboBox.addItem(categoria);
            }
        }
    }

    private boolean isReceitaCategory(CategoriaTipo categoria) {
        return categoria == CategoriaTipo.SALARIO ||
                categoria == CategoriaTipo.FREELANCE ||
                categoria == CategoriaTipo.INVESTIMENTOS;
               // categoria == CategoriaTipo.OUTROS_RECEITAS;
    }

    private void salvarTransacao() {
        try {
            String descricao = descricaoField.getText().trim();
            String valorText = valorField.getText().trim().replace(",", ".");
            TransacaoTipo tipo = (TransacaoTipo) tipoComboBox.getSelectedItem();
            CategoriaTipo categoria = (CategoriaTipo) categoriaComboBox.getSelectedItem();
            String dataText = dataField.getText().trim();

            if (descricao.isEmpty()) {
                showErrorMessage("Descrição é obrigatória");
                return;
            }

            if (valorText.isEmpty()) {
                showErrorMessage("Valor é obrigatório");
                return;
            }

            BigDecimal valor;
            try {
                valor = new BigDecimal(valorText);
                if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                    showErrorMessage("Valor deve ser maior que zero");
                    return;
                }
            } catch (NumberFormatException ex) {
                showErrorMessage("Valor deve ser um número válido");
                return;
            }

            LocalDate data;
            try {
                data = LocalDate.parse(dataText, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException ex) {
                showErrorMessage("Data deve estar no formato dd/MM/yyyy");
                return;
            }

            Transacao transacao = transacaoController.createTransaction(
                    descricao, valor, tipo, categoria, data);

            if (onSuccessCallback != null) {
                onSuccessCallback.run();
            }

            showSuccessMessage("Transação criada com sucesso!");
            dispose();

        } catch (IllegalArgumentException ex) {
            showErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            showErrorMessage("Erro inesperado: " + ex.getMessage());
        }
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void setupDialog() {
        pack();
        setLocationRelativeTo(getParent());
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        updateCategoriaComboBox();

        SwingUtilities.invokeLater(() -> descricaoField.requestFocus());
    }
}