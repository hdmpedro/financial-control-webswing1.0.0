package io.hdmpedro.financeiro.view.dialogs;

import io.hdmpedro.financeiro.controller.FechamentoMesController;
import io.hdmpedro.financeiro.models.RelatorioMensal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDate;

public class FecharMesDialog extends JDialog {
    private final FechamentoMesController fechamentoMesController;
    private final Runnable onSuccessCallback;

    private JComboBox<Integer> mesComboBox;
    private JComboBox<Integer> anoComboBox;
    private JLabel saldoMensalLabel;
    private JTextField contribuicaoReservaField;
    private JTextArea resumoArea;
    private JButton visualizarButton;
    private JButton fecharButton;
    private JButton cancelarButton;

    private RelatorioMensal previewRelatorio;

    public FecharMesDialog(Component parent, FechamentoMesController fechamentoMesController,
                           Runnable onSuccessCallback) {
        super(SwingUtilities.getWindowAncestor(parent), "Fechar Mês", Dialog.ModalityType.APPLICATION_MODAL);
        this.fechamentoMesController = fechamentoMesController;
        this.onSuccessCallback = onSuccessCallback;

        initializeComponents();
        setupLayout();
        setupEventListeners();
        setupDialog();
    }

    private void initializeComponents() {
        LocalDate hoje = LocalDate.now();

        mesComboBox = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            mesComboBox.addItem(i);
        }
        mesComboBox.setSelectedItem(hoje.getMonthValue());

        anoComboBox = new JComboBox<>();
        for (int i = hoje.getYear() - 2; i <= hoje.getYear() + 1; i++) {
            anoComboBox.addItem(i);
        }
        anoComboBox.setSelectedItem(hoje.getYear());

        saldoMensalLabel = new JLabel("Selecione um mês para visualizar o saldo");
        saldoMensalLabel.setFont(saldoMensalLabel.getFont().deriveFont(Font.BOLD, 14f));

        contribuicaoReservaField = new JTextField(10);
        contribuicaoReservaField.setText("0,00");

        resumoArea = new JTextArea(8, 40);
        resumoArea.setEditable(false);
        resumoArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        resumoArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Resumo do Fechamento"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        visualizarButton = new JButton("Visualizar");
        fecharButton = new JButton("Fechar Mês");
        cancelarButton = new JButton("Cancelar");

        fecharButton.setEnabled(false);

        visualizarButton.setPreferredSize(new Dimension(100, 30));
        fecharButton.setPreferredSize(new Dimension(100, 30));
        cancelarButton.setPreferredSize(new Dimension(100, 30));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout());
        headerPanel.setBorder(BorderFactory.createTitledBorder("Selecionar Período"));

        headerPanel.add(new JLabel("Mês:"));
        headerPanel.add(mesComboBox);
        headerPanel.add(new JLabel("Ano:"));
        headerPanel.add(anoComboBox);
        headerPanel.add(visualizarButton);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        infoPanel.add(saldoMensalLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        infoPanel.add(new JLabel("Contribuição para Reserva (R$):"), gbc);
        gbc.gridx = 1;
        infoPanel.add(contribuicaoReservaField, gbc);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(infoPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(resumoArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(fecharButton);
        buttonPanel.add(cancelarButton);

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupEventListeners() {
        visualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                visualizarPreview();
            }
        });

        fecharButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fecharMes();
            }
        });

        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        contribuicaoReservaField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (previewRelatorio != null) {
                    visualizarPreview();
                }
            }
        });
    }

    private void visualizarPreview() {
        try {
            Integer mes = (Integer) mesComboBox.getSelectedItem();
            Integer ano = (Integer) anoComboBox.getSelectedItem();

            if (fechamentoMesController.isMonthClosed(mes, ano)) {
                showErrorMessage("Este mês já foi fechado!");
                fecharButton.setEnabled(false);
                return;
            }

            previewRelatorio = fechamentoMesController.generatePreviewReport(mes, ano);

            saldoMensalLabel.setText(String.format("Saldo do Mês: R$ %.2f",
                    previewRelatorio.getBalance()));

            if (previewRelatorio.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                saldoMensalLabel.setForeground(Color.RED);
            } else {
                saldoMensalLabel.setForeground(new Color(0, 120, 0));
            }

            String contribuicaoText = contribuicaoReservaField.getText().trim().replace(",", ".");
            BigDecimal contribuicao = BigDecimal.ZERO;

            if (!contribuicaoText.isEmpty() && !contribuicaoText.equals("0") && !contribuicaoText.equals("0.00")) {
                try {
                    contribuicao = new BigDecimal(contribuicaoText);
                    if (contribuicao.compareTo(BigDecimal.ZERO) < 0) {
                        showErrorMessage("Contribuição para reserva não pode ser negativa");
                        return;
                    }
                    if (contribuicao.compareTo(previewRelatorio.getBalance()) > 0) {
                        showErrorMessage("Contribuição não pode ser maior que o saldo do mês");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    showErrorMessage("Valor da contribuição deve ser um número válido");
                    return;
                }
            }

            StringBuilder resumo = new StringBuilder();
            resumo.append(String.format("═══ FECHAMENTO DO MÊS %02d/%d ═══\n\n", mes, ano));
            resumo.append(String.format("Total de Receitas:      R$ %,10.2f\n", previewRelatorio.getTotalIncome()));
            resumo.append(String.format("Total de Despesas:      R$ %,10.2f\n", previewRelatorio.getTotalExpenses()));
            resumo.append("─".repeat(40)).append("\n");
            resumo.append(String.format("Saldo do Mês:           R$ %,10.2f\n\n", previewRelatorio.getBalance()));

            if (contribuicao.compareTo(BigDecimal.ZERO) > 0) {
                resumo.append(String.format("Contribuição p/ Reserva: R$ %,10.2f\n", contribuicao));
                BigDecimal saldoFinal = previewRelatorio.getBalance().subtract(contribuicao);
                resumo.append("─".repeat(40)).append("\n");
                resumo.append(String.format("Saldo Final:            R$ %,10.2f\n", saldoFinal));
            }

            resumo.append("\n═══ RESUMO POR CATEGORIA ═══\n");
            previewRelatorio.getExpensesByCategory().entrySet().stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                    .forEach(entry -> {
                        resumo.append(String.format("%-20s: R$ %,8.2f\n",
                                entry.getKey().toString(), entry.getValue()));
                    });

            resumoArea.setText(resumo.toString());
            resumoArea.setCaretPosition(0);

            fecharButton.setEnabled(true);

        } catch (Exception ex) {
            showErrorMessage("Erro ao gerar preview: " + ex.getMessage());
        }
    }

    private void fecharMes() {
        try {
            if (previewRelatorio == null) {
                showErrorMessage("Primeiro visualize o preview do fechamento");
                return;
            }

            Integer mes = (Integer) mesComboBox.getSelectedItem();
            Integer ano = (Integer) anoComboBox.getSelectedItem();

            String contribuicaoText = contribuicaoReservaField.getText().trim().replace(",", ".");
            BigDecimal contribuicao = null;

            if (!contribuicaoText.isEmpty() && !contribuicaoText.equals("0") && !contribuicaoText.equals("0.00")) {
                contribuicao = new BigDecimal(contribuicaoText);
            }

            int confirmation = JOptionPane.showConfirmDialog(
                    this,
                    String.format("Confirma o fechamento do mês %02d/%d?\n\nEsta operação não poderá ser desfeita!", mes, ano),
                    "Confirmar Fechamento",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirmation != JOptionPane.YES_OPTION) {
                return;
            }

            RelatorioMensal relatorio = fechamentoMesController.closeMonth(mes, ano, contribuicao);

            if (onSuccessCallback != null) {
                onSuccessCallback.run();
            }

            showSuccessMessage(String.format("Mês %02d/%d fechado com sucesso!\n\nSaldo final: R$ %.2f",
                    mes, ano, relatorio.getFinalBalance()));

            dispose();

        } catch (IllegalStateException ex) {
            showErrorMessage(ex.getMessage());
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
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}