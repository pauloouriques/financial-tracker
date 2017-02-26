package com.toptal.app.financialtracker.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Class that represents all system constants.
 */
public class Constants {

    // ------------- Rest - BEGIN --------------- //
    public static int HTTP_CLIENT_CONNECTION_TIMEOUT = 20000;
    public static int HTTP_CLIENT_SOCKET_TIMEOUT = 20000;
// -------------- Rest - END ---------------- //

// ----------- API Messages - BEGIN -------------- //

    public static final String ERROR_UNAUTHORIZED = "401 Unauthorized";
    public static final String EMPTY_STRING = "";

// ----------- API Messages - END -------------- //

// ---------- SharedPreferences - BEGIN ------------- //
    /**
     * Hash to indicate the Shared Preferences.
     **/
    public static final String SHARED_PREFERENCES = "vre_android_prefs";
    /**
     * Hash to indicate the agent's username field in shared preferences.
     **/
    public static final String PREFS_CURRENT_USER = "prefs_current_user";

    /**
     * Tutorial exhibition flag.
     */
    public static final String PREFS_SAW_TUTORIAL_KEY = "swTt";

    /**
     * User in shared preference.
     */
    public static final String PREFS_USER = "prefs_user";

    /**
     * Login cookie in shared preference.
     */
    public static final String PREFS_COOKIES = "prefs_cookies";

    // ----------- SharedPreferences - END -------------- //


    /**
     * Date format.
     */
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("E, dd/MM/yyyy");


    /**
     * Time format.
     */
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm a");

    /**
     * Format iso.
     */
    public static final SimpleDateFormat DATE_FORMAT_FROM_SERVER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * Expense to edit.
     */
    public static final String EXPENSE_TO_EDIT = "expense_to_edit";

    /**
     * Expense to edit.
     */
    public static final String USER_TO_EDIT = "expense_to_edit";

}
