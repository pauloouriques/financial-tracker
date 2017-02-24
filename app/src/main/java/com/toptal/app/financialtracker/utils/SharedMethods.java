package com.toptal.app.financialtracker.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Class to organize static shared methods.
 */
public class SharedMethods {

    /**
     * Add the decimal format to given string.
     *
     * @param toFormat string to format.
     * @return formatted string.
     */
    public static String formatMonetaryValue(double toFormat) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(toFormat);
    }
}
