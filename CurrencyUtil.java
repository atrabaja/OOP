package com.mycompany.motorph.util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * A utility class for formatting currency values.
 * <p>
 * Supports internationalization (i18n) and allows formatting based on locale.
 * </p>
 * 
 * @author Lance
 */
public class CurrencyUtil {

    /**
     * Formats a currency value based on the system's default locale.
     *
     * @param currencyValue The currency value to be formatted
     * @return The formatted currency as a String
     */
    public static String formatCurrency(double currencyValue) {
        return formatCurrency(currencyValue, Locale.getDefault());
    }

    /**
     * Formats a currency value based on the specified locale.
     *
     * @param currencyValue The currency value to be formatted
     * @param locale The locale for currency formatting (e.g., Locale.US, Locale.JAPAN)
     * @return The formatted currency as a String
     */
    public static String formatCurrency(double currencyValue, Locale locale) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        return currencyFormatter.format(currencyValue);
    }

    /**
     * Formats a currency value without a currency symbol (e.g., "1,234.56").
     *
     * @param currencyValue The currency value to be formatted
     * @return The formatted currency as a String without the currency symbol
     */
    public static String formatPlainCurrency(double currencyValue) {
        NumberFormat plainFormatter = NumberFormat.getNumberInstance();
        plainFormatter.setMinimumFractionDigits(2);
        plainFormatter.setMaximumFractionDigits(2);
        return plainFormatter.format(currencyValue);
    }
}
