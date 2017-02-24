package com.toptal.app.financialtracker.entities;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

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
            return expense.amount.compareTo(anotherExpense.amount);
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

}
