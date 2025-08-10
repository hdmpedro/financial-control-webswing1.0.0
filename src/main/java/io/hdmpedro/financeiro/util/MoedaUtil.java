package io.hdmpedro.financeiro.util;


import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
public class MoedaUtil {
    private static final Locale LOCALE_PT_BR = new Locale("pt", "BR");
    private static final NumberFormat CURRENCY_FORMAT =
            NumberFormat.getCurrencyInstance(LOCALE_PT_BR);

    public static String format(BigDecimal amount) {
        return amount != null ? CURRENCY_FORMAT.format(amount) : "R$ 0,00";
    }

    public static String formatWithSign(BigDecimal amount) {
        if (amount == null || amount.equals(BigDecimal.ZERO)) {
            return "R$ 0,00";
        }

        String formatted = CURRENCY_FORMAT.format(amount.abs());
        return amount.compareTo(BigDecimal.ZERO) >= 0 ? "+ " + formatted : "- " + formatted;
    }

    public static BigDecimal parse(String value) {
        try {
            String cleanValue = value.replaceAll("[^\\d,.-]", "").replace(",", ".");
            return new BigDecimal(cleanValue);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    public static boolean isPositive(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isNegative(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) < 0;
    }
}
