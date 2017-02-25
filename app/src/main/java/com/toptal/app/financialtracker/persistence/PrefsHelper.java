package com.toptal.app.financialtracker.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import com.toptal.app.financialtracker.entities.User;
import com.toptal.app.financialtracker.utils.Constants;

import java.util.HashSet;


/**
 * Class that handles the persistent data.
 */
public class PrefsHelper {

    /**
     * Create shared preferences and return it.
     *
     * @param context the context.
     * @return shared preferences.
     */
    private static SharedPreferences getPreferences(final Context context) {
        return context.getSharedPreferences(Constants.SHARED_PREFERENCES, 0);
    }

    /**
     * This method can be used to clear all the preferences.
     * @param context - The context.
     */
    public static void clearPrefs(Context context){
        clearCookie(context);
        getPreferences(context).edit().putString(Constants.PREFS_USER, Constants.EMPTY_STRING).commit();
    }

    /**
     * This method can be used to clear all Cookie preferences.
     *
     * @param context - The context.
     */
    public static final void clearCookie(final Context context) {
        getPreferences(context).edit().putString(Constants.PREFS_COOKIES, Constants.EMPTY_STRING)
                .commit();
    }

    /**
     * Saves user cookie.
     *
     * @param context - the context.
     * @param cookie  - The user cookie.
     */
    public static final void putCookies(final Context context, final String cookie) {
        getPreferences(context).edit().putString(Constants.PREFS_COOKIES, cookie).commit();
    }

    /**
     * Gets user cookie.
     *
     * @param context - the context.
     * @return - user cookie.
     */
    public static final String getCookie(final Context context) {
        return getPreferences(context).getString(Constants.PREFS_COOKIES, "");
    }


    /**
     * This method can be used to clear all Cookie preferences.
     *
     * @param context - The context.
     */
    public static final void clearUser(final Context context) {
        getPreferences(context).edit().putString(Constants.PREFS_USER, Constants.EMPTY_STRING)
                .commit();
    }

    /**
     * Saves user entity.
     *
     * @param context - the context.
     * @param user  - The user entity.
     */
    public static final void putUser(final Context context, final User user) {
        getPreferences(context).edit().putString(Constants.PREFS_USER, user.toJsonString()).commit();
    }

    /**
     * Gets user entity.
     *
     * @param context - the context.
     * @return - user entity.
     */
    public static final User getUser(final Context context) {
        return User.getFromJson(getPreferences(context).getString(Constants.PREFS_USER, Constants.EMPTY_STRING));
    }


    /**
     * Sets if the user already saw the tutorial.
     * @param context - the context.
     * @param sawTutorial - True or false.
     */
    public static void putSawTutorial(Context context, boolean sawTutorial){
        getPreferences(context).edit().putBoolean(Constants.PREFS_SAW_TUTORIAL_KEY, sawTutorial).commit();
    }

    /**
     * Gets if the user already saw the tutorial.
     * @param context - the context.
     * @return - True or false.
     */
    public static boolean getSawTutorial(Context context){
        return getPreferences(context).getBoolean(Constants.PREFS_SAW_TUTORIAL_KEY, false);

    }

}
