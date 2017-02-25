package com.toptal.app.financialtracker.entities;

import android.content.Context;
import android.content.res.TypedArray;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.toptal.app.financialtracker.R;

import java.util.Comparator;

/**
 * Class to represent PaymentDetailsActivity payment placeholder.
 */
public class Expense extends BaseEntity {
    @SerializedName("_id")
    private String id;
    public String description;
    public Double amount = 0d;
    public String comment;
    public String date;
    public String category;

    /**
     * Comparator to transactions (sort list)
     */
    public static class TransactionComparator implements Comparator<Expense> {
        public int compare(final Expense expense, final Expense anotherExpense) {
            return expense.date.compareTo(anotherExpense.date);
        }
    }

    /**
     * Get entity from json.
     *
     * @param jsonString json object.
     * @return the entity object.
     */
    public static Expense getFromJson(String jsonString) {
        return new Gson().fromJson(jsonString, Expense.class);
    }

    /**
     * Get the corresponding icon res id.
     * @param context app context.
     * @return the correspondin icon res id.
     */
    public int getExpenseIconResId(final Context context) {
        String[] categories = context.getResources()
                .getStringArray(R.array.expense_categories);
        TypedArray categoriesIcons = context.getResources()
                .obtainTypedArray(R.array.expense_categories_icons);
        for (int i = 0; i < categories.length; i++) {
            if (this.category.toLowerCase().equals(categories[i].toLowerCase())) {
                return categoriesIcons.getResourceId(i, 0);
            }
        }
        return 0;
    }

}
