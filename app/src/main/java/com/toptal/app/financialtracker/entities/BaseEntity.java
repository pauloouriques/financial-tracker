package com.toptal.app.financialtracker.entities;

import com.google.gson.Gson;

/**
 * The base entity.
 */
public class BaseEntity {

    /**
     * Convert the entity to a json representation.
     *
     * @return the string o a json representation.
     */
    public String toJsonString() {
        return new Gson().toJson(this);
    }

}
