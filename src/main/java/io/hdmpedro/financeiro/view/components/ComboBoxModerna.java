package io.hdmpedro.financeiro.view.components;

import io.hdmpedro.financeiro.util.TemaCores;

import javax.swing.*;
import java.awt.*;

public class ComboBoxModerna<T> extends JComboBox<T> {

    public ComboBoxModerna() {
        initializeComboBox();
    }

    public ComboBoxModerna(T[] items) {
        super(items);
        initializeComboBox();
    }

    private void initializeComboBox() {
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setBackground(TemaCores.SURFACE);
        setForeground(TemaCores.TEXT_PRIMARY);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaCores.BORDER, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        setRenderer(new ModernComboBoxRenderer());
    }

    private class ModernComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {

            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

            if (isSelected) {
                setBackground(TemaCores.PRIMARY_LIGHT);
                setForeground(TemaCores.TEXT_PRIMARY);
            } else {
                setBackground(TemaCores.SURFACE);
                setForeground(TemaCores.TEXT_PRIMARY);
            }

            return this;
        }
    }
}