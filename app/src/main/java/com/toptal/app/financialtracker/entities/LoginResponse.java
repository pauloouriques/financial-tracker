package com.toptal.app.financialtracker.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Class that represents the response of login.
 */
public class LoginResponse {

    /**
     * Login - user.
     */
    @SerializedName("user")
    private User user;

    /**
     * Get user.
     * @return user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Set user.
     * @param user new user.
     */
    public void setUser(User user) {
        this.user = user;
    }
}

