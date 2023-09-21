package me.azazeldev.coinui.utility;

public class MainUtils {

    public static String formatNumber(double number) {
        if (number < 1000) {
            return String.valueOf(number);
        } else if (number < 1000000) {
            return String.format("%.1fk", number / 1000.0);
        } else {
            return String.format("%.1fM", number / 1000000.0);
        }
    }
}