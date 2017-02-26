package com.toptal.app.financialtracker.entities;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

/**
 * This class represents a user entry.
 */
public class User extends BaseEntity{
    public static final String TYPE_USER = "USER";
    public static final String TYPE_MANAGER = "MANAGER";
    public static final String TYPE_ADMIN = "ADMIN";

    @SerializedName("_id")
    public String id;

    public String name;
    public String password;
    public String email;
    public String type;

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
     * Comparator to transactions (sort list)
     */
    public static class nameComparator implements Comparator<User> {
        public int compare(final User user, final User anotherUser) {
            return user.name.compareTo(anotherUser.name);
        }
    }

}
