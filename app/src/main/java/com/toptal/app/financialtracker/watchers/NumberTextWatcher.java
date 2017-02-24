package com.toptal.app.financialtracker.watchers;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Number text Watcher.
 */
public class NumberTextWatcher implements TextWatcher {

    private EditText mEditText;
    private boolean isDeleting = false;
    private OnEditListener mEditListener;

    public NumberTextWatcher(EditText editText, OnEditListener editListener) {
        mEditListener = editListener;
        mEditText = editText;
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    isDeleting = true;
                } else {
                    isDeleting = false;
                }
                return false;
            }
        });
    }

    public NumberTextWatcher(EditText editText) {
        mEditText = editText;
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    isDeleting = true;
                } else {
                    isDeleting = false;
                }
                return false;
            }
        });
    }

    private String current = "0";

    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

        if (!charSequence.toString().equals(current)) {
            mEditText.removeTextChangedListener(this);

            String cleanString = charSequence.toString();

            if (count < 1) {
                String substring = cleanString.substring(cleanString.length() - 2);

                if (substring.contains(".") || substring.contains(",")) {
                    cleanString += "0";
                }
            }
            cleanString = cleanString.replace("$", "").trim();
            cleanString = cleanString.replaceAll("[,.]", "");

            if (isDeleting) {
                cleanString = cleanString.substring(0, cleanString.length() - 1);
            }

            double parsed = Double.parseDouble(cleanString);

            DecimalFormat df = new DecimalFormat("#,###,#00.00");
            String formatted = df.format((parsed / 100));


            mEditText.setText(formatted);
            mEditText.setSelection(formatted.length());
            current = formatted;
            mEditText.addTextChangedListener(this);
            if (mEditListener != null) {
                mEditListener.onEdit();
            }
        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void afterTextChanged(Editable s) {
    }
}
