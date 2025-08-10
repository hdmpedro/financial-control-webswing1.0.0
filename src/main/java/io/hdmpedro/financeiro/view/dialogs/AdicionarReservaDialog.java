package io.hdmpedro.financeiro.view.dialogs;

import io.hdmpedro.financeiro.controller.MainController;
import io.hdmpedro.financeiro.controller.ReservaController;
import io.hdmpedro.financeiro.models.Reserva;
import io.hdmpedro.financeiro.util.MoedaUtil;
import io.hdmpedro.financeiro.util.TemaCores;
import io.hdmpedro.financeiro.view.components.BotaoModerno;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class AdicionarReservaDialog extends JDialog {
    private final ReservaController reservaController;
    private final Runnable onSuccessCallback;

    private JTextField descricaoField;
    private JTextField valorField;
    private JButton adicionarButton;
    private JButton cancelarButton;
    private JLabel saldoAtualLabel;

    public AdicionarReservaDialog(Component parent, ReservaController reservaController,
                                  Runnable onSuccessCallback) {
        super(SwingUtilities.getWindowAncestor(parent), "Adicionar à Reserva", Dialog.ModalityType.APPLICATION_MODAL);
        this.reservaController = reservaController;
        this.onSuccessCallback = onSuccessCallback;

        initializeComponents();
        setupLayout();
        setupEventListeners();
        setupDialog();
    }

    private void initializeComponents() {
        descricaoField = new JTextField(25);
        valorField = new JTextField(15);

        adicionarButton = new JButton("Adicionar");
        cancelarButton = new JButton("Cancelar");

        adicionarButton.setPreferredSize(new Dimension(100, 30));
        cancelarButton.setPreferredSize(new Dimension(100, 30));

        updateSaldoAtual();
    }

    private void updateSaldoAtual() {
        BigDecimal saldoAtual = reservaController.getTotalReserve();
        saldoAtualLabel = new JLabel(String.format("Saldo atual da reserva: R$ %.2f", saldoAtual));
        saldoAtualLabel.setFont(saldoAtualLabel.getFont().deriveFont(Font.BOLD, 14f));
        saldoAtualLabel.setForeground(new Color(0, 120, 0));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        headerPanel.add(saldoAtualLabel);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(descricaoField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Valor (R$):"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(valorField, gbc);

        JPanel instructionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel instructionLabel = new JLabel("Digite o valor que deseja adicionar à sua reserva");
        instructionLabel.setFont(instructionLabel.getFont().deriveFont(Font.ITALIC, 12f));
        instructionLabel.setForeground(Color.GRAY);
        instructionPanel.add(instructionLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        buttonPanel.add(adicionarButton);
        buttonPanel.add(cancelarButton);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(instructionPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(instructionPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void setupEventListeners() {
        adicionarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarReserva();
            }
        });

        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        valorField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarReserva();
            }
        });
    }

    private void adicionarReserva() {
        try {
            String descricao = descricaoField.getText().trim();
            String valorText = valorField.getText().trim().replace(",", ".");

            if (descricao.isEmpty()) {
                showErrorMessage("Descrição é obrigatória");
                descricaoField.requestFocus();
                return;
            }

            if (valorText.isEmpty()) {
                showErrorMessage("Valor é obrigatório");
                valorField.requestFocus();
                return;
            }

            BigDecimal valor;
            try {
                valor = new BigDecimal(valorText);
                if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                    showErrorMessage("Valor deve ser maior que zero");
                    valorField.requestFocus();
                    return;
                }
            } catch (NumberFormatException ex) {
                showErrorMessage("Valor deve ser um número válido");
                valorField.requestFocus();
                return;
            }

            int confirmation = JOptionPane.showConfirmDialog(
                    this,
                    String.format("Deseja adicionar R$ %.2f à reserva?\nDescrição: %s", valor, descricao),
                    "Confirmar Adição à Reserva",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirmation != JOptionPane.YES_OPTION) {
                return;
            }

            Reserva reserva = reservaController.addToReserve(valor, descricao);

            if (onSuccessCallback != null) {
                onSuccessCallback.run();
            }

            showSuccessMessage(String.format("Valor adicionado à reserva com sucesso!\nNovo saldo: R$ %.2f",
                    reservaController.getTotalReserve()));

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

        SwingUtilities.invokeLater(() -> descricaoField.requestFocus());
    }
}