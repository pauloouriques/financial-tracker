package com.toptal.app.financialtracker.entities;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * This class represents a user entry.
 */
public class User extends BaseEntity{
    @SerializedName("_id")
    private String id;

    private String name;
    private String password;
    private String email;

    public User(final String name, final String id, final String email) {
        this.name = name;
        this.email = email;
        this.id = id;
    }

    /**
     * Get entity from json.
     *
     * @param jsonString json object.
     * @return the entity object.
     */
    public static User getFromJson(String jsonString) {
        return new Gson().fromJson(jsonString, User.class);
    }

    /**
     * Get user name.
     * @return user name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set user name.
     * @param name user name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get user id.
     * @return user id.
     */
    public String getId() {
        return id;
    }

    /**
     * Set user id.
     * @param id the new user id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get user email.
     * @return user email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set user email.
     * @param email user email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get user password.
     * @return user password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set user password.
     * @param password user password.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
