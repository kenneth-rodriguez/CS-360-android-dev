/*
 * Class to more easily modify the SMS permission level and store the user's phone number.
 *
 * Methods are included to write a new permission level to the SharedPreferences file and
 * retrieve the information stored on that SharedPreferences file. Methods are also included to
 * add a user's phone number and update that user's phone number, also stored in the SharedPreferences
 * file.
 */

package com.example.kenrodriguez_cs360_inventory.SMSmanagement;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {

    // Load a preference file to track user permissions
    public static final String PREFS_NAME = "UserPermissions";

    // Default constructor
    public PreferenceHelper(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Get the current SMS permission level. Defaults to false and can be changed by the user.
    public boolean getPermission(Context context){
        SharedPreferences SMSPreferences = context.getSharedPreferences(PREFS_NAME, 0);
        boolean smsPreference = SMSPreferences.getBoolean("smsPermission", false);

        return smsPreference;
    }

    // Set SMS permission level to user-specified value.
    public void setPermission(Context context, boolean newPermission){
        // Save data to  SharedPreferences file
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("smsPermission", newPermission);
        editor.apply();
    }

    // Get the user's current phone number
    public String getPhoneNumber(Context context){
        SharedPreferences phonePreferences = context.getSharedPreferences(PREFS_NAME, 0);

        return phonePreferences.getString("phoneNumber", "-1");
    }

    // Set the user's current phone number
    public void setPhoneNumber(Context context, String newNumber){
        // Save the updated number to the SharedPreferences file
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("phoneNumber", newNumber);
        editor.apply();
    }
}
