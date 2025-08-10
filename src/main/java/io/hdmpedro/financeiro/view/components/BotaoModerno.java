package io.hdmpedro.financeiro.view.components;


import io.hdmpedro.financeiro.util.TemaCores;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BotaoModerno extends JButton {
    private Color backgroundColor;
    private Color hoverColor;
    private Color pressedColor;
    private boolean isHovered = false;
    private boolean isPressed = false;

    public BotaoModerno(String text) {
        super(text);
        initializeButton();
    }

    public BotaoModerno(String text, Color backgroundColor) {
        super(text);
        this.backgroundColor = backgroundColor;
        this.hoverColor = backgroundColor.darker();
        this.pressedColor = backgroundColor.brighter();
        initializeButton();
    }

    private void initializeButton() {
        if (backgroundColor == null) {
            backgroundColor = TemaCores.PRIMARY;
            hoverColor = TemaCores.PRIMARY_DARK;
            pressedColor = TemaCores.PRIMARY_LIGHT;
        }

        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color currentColor = backgroundColor;
        if (isPressed) {
            currentColor = pressedColor;
        } else if (isHovered) {
            currentColor = hoverColor;
        }

        g2d.setColor(currentColor);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

        g2d.dispose();
        super.paintComponent(g);
    }
}