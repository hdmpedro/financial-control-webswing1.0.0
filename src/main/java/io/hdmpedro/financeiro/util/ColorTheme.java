package io.hdmpedro.financeiro.util;


import java.awt.Color;

public class ColorTheme {
    public static final Color PRIMARY = new Color(103, 58, 183);
    public static final Color PRIMARY_DARK = new Color(81, 45, 144);
    public static final Color PRIMARY_LIGHT = new Color(179, 157, 219);

    public static final Color ACCENT = new Color(255, 193, 7);
    public static final Color ACCENT_DARK = new Color(255, 160, 0);

    public static final Color SUCCESS = new Color(76, 175, 80);
    public static final Color SUCCESS_LIGHT = new Color(129, 199, 132);

    public static final Color ERROR = new Color(244, 67, 54);
    public static final Color ERROR_LIGHT = new Color(239, 154, 154);

    public static final Color WARNING = new Color(255, 152, 0);
    public static final Color WARNING_LIGHT = new Color(255, 204, 128);

    public static final Color BACKGROUND = new Color(250, 250, 250);
    public static final Color SURFACE = Color.WHITE;
    public static final Color SURFACE_VARIANT = new Color(245, 245, 245);

    public static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    public static final Color TEXT_SECONDARY = new Color(117, 117, 117);
    public static final Color TEXT_DISABLED = new Color(189, 189, 189);

    public static final Color BORDER = new Color(224, 224, 224);
    public static final Color BORDER_FOCUS = PRIMARY;

    public static Color withAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static Color getIncomeColor() {
        return SUCCESS;
    }

    public static Color getExpenseColor() {
        return ERROR;
    }

    public static Color getBalanceColor(boolean isPositive) {
        return isPositive ? SUCCESS : ERROR;
    }
}
