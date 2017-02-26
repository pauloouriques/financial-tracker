package com.toptal.app.financialtracker.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.toptal.app.financialtracker.R;

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

    /**
     * Show a generic ok/cancel button.
     * @param context context.
     * @param title title.
     * @param text text.
     * @param listener ok listener.
     */
    public static void showDialog(
            final Context context,
            final String title,
            final String text,
            final DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(text)
                .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                .setPositiveButton(android.R.string.ok, listener)
                .create()
                .show();
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
