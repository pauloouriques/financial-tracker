package com.toptal.app.financialtracker.rest;

/**
 * This class represents an API error.
 */
public class ApiError {

    private int code;
    private String message;

    /**
     * Constructor method.
     * @param code - Error code.
     * @param message - Error message.
     */
    public ApiError(int code, String message){
        this.code = code;
        this.message = message;
    }

    // GETTERS AND SETTERS - BEGIN

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


    // GETTERS AND SETTERS - END
}
