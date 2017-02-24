package com.toptal.app.financialtracker.rest;

import com.google.gson.annotations.SerializedName;

/**
 * This class represents an API error.
 */
public class APIError {

    @SerializedName("ErrorCode")
    private int code;

    @SerializedName("AccessToken")
    private String token;

    @SerializedName("Description")
    private String description;


    // Getters and Setters - Begin

    public final String getToken() {
        return token;
    }

    public final String getDescription() {
        return description;
    }

    public final int getCode() {
        return code;
    }

    // Getters and Setters - End


    /**
     * Constructor method.
     * @param code - Error code.
     * @param description - Error description.
     */
    public APIError(final int code, final String description) {
        this.description = description;
    }
}
