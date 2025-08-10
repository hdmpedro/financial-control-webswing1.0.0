package io.hdmpedro.financeiro.util;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class DateUtils {
    public static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter MONTH_YEAR_FORMAT = DateTimeFormatter.ofPattern("MM/yyyy");
    public static final DateTimeFormatter FULL_MONTH_YEAR = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("pt", "BR"));

    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DISPLAY_FORMAT) : "";
    }

    public static String formatMonthYear(LocalDate date) {
        return date != null ? date.format(MONTH_YEAR_FORMAT) : "";
    }

    public static String formatFullMonthYear(LocalDate date) {
        return date != null ? date.format(FULL_MONTH_YEAR) : "";
    }

    public static int getWeekOfMonth(LocalDate date) {
        WeekFields weekFields = WeekFields.of(new Locale("pt", "BR"));
        return date.get(weekFields.weekOfMonth());
    }

    public static LocalDate getFirstDayOfMonth(int month, int year) {
        return LocalDate.of(year, month, 1);
    }

    public static LocalDate getLastDayOfMonth(int month, int year) {
        return LocalDate.of(year, month, 1).withDayOfMonth(
                LocalDate.of(year, month, 1).lengthOfMonth());
    }

    public static boolean isCurrentMonth(LocalDate date) {
        LocalDate now = LocalDate.now();
        return date.getMonth() == now.getMonth() && date.getYear() == now.getYear();
    }
}