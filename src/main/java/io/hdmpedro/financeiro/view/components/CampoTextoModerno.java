package io.hdmpedro.financeiro.view.components;


import io.hdmpedro.financeiro.util.TemaCores;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CampoTextoModerno extends JTextField {
    private String placeholder;
    private boolean showingPlaceholder;

    public CampoTextoModerno() {
        initializeField();
    }

    public CampoTextoModerno(String placeholder) {
        this.placeholder = placeholder;
        initializeField();
        showPlaceholder();
    }

    private void initializeField() {
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setBorder(new ModernBorder());
        setBackground(TemaCores.SURFACE);
        setForeground(TemaCores.TEXT_PRIMARY);

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (showingPlaceholder) {
                    hidePlaceholder();
                }
                setBorder(new ModernBorder(true));
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty() && placeholder != null) {
                    showPlaceholder();
                }
                setBorder(new ModernBorder(false));
            }
        });
    }

    private void showPlaceholder() {
        if (placeholder != null) {
            setText(placeholder);
            setForeground(TemaCores.TEXT_DISABLED);
            showingPlaceholder = true;
        }
    }

    private void hidePlaceholder() {
        setText("");
        setForeground(TemaCores.TEXT_PRIMARY);
        showingPlaceholder = false;
    }

    @Override
    public String getText() {
        return showingPlaceholder ? "" : super.getText();
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        if (getText().isEmpty()) {
            showPlaceholder();
        }
    }

    private static class ModernBorder extends AbstractBorder {
        private final boolean isFocused;

        public ModernBorder() {
            this.isFocused = false;
        }

        public ModernBorder(boolean isFocused) {
            this.isFocused = isFocused;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(isFocused ? TemaCores.BORDER_FOCUS : TemaCores.BORDER);
            g2d.setStroke(new BasicStroke(isFocused ? 2 : 1));
            g2d.drawRoundRect(x, y, width - 1, height - 1, 6, 6);

            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(8, 12, 8, 12);
        }
    }
}