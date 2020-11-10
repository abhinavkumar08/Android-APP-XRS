package com.xpedite.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.xpedite.activity.MainActivity;
import com.xpedite.activity.login.SignInActivity;

/**
 * Created by abhinkum on 9/12/17.
 * User session for default login.
 */

public class UserSession {
    // Shared Preferences reference
    private SharedPreferences pref;

    // Editor reference for Shared preferences
    private SharedPreferences.Editor editor;

    // Context
    private Context _context;

    // Shared preferences mode
    private static final int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREFER_NAME = "Reg";

    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    // User name (make variable public to access from outside)
    // public static final String KEY_NAME = "Name";

    // Email address (make variable public to access from outside)
    private static final String KEY_PHONE = "phoneNumber";

    // password
    private static final String KEY_PASSWORD = "password";

    // Constructor
    public UserSession(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create login session
    public void createUserLoginSession(long uName, String uPassword) {
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);

        //Storing name in preferences
        //editor.putString(KEY_NAME, name);

        // Storing phone in preferences
        editor.putLong(KEY_PHONE, uName);

        // Storing email in preferences
        editor.putString(KEY_PASSWORD, uPassword);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     */
    public boolean checkLogin() {
        // Check login status
        if (!this.isUserLoggedIn()) {

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, SignInActivity.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

            return true;
        }
        return false;
    }


    /**
     * Get stored session data
     * */
  /*  public HashMap<String, String> getUserDetails(){

        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_PHONE, pref.getString(KEY_PHONE, null));

        // return user
        return user;
    }*/

    /**
     * Clear session details
     */
    public void logoutUser() {

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to MainActivity
        Intent i = new Intent(_context, MainActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }


    // Check for login
    private boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}
