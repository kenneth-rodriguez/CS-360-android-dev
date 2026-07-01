/*
 * SQLite user database helper.
 *
 * This will create a database to store user logins that will persist between the application's
 * uses.
 *
 * Methods are included to insert new username/password combinations, verify whether a username
 * exists in the database, and verify whether a username/password combination exists in the database.
 */

package com.example.kenrodriguez_cs360_inventory.usermanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


// Create a helper to set up SQLite database management
public class UserDBHelper extends SQLiteOpenHelper{
    public static final String DBNAME = "userDatabase.db";
    public UserDBHelper(Context context){
        super(context, "userDatabase.db", null, 1);
    }


    // onCreate, use SQLite to create table users with username and password pairs
    @Override
    public void onCreate(SQLiteDatabase userDatabase){
        userDatabase.execSQL("CREATE TABLE users(username TEXT PRIMARY KEY, password TEXT)");
    }

    // onUpgrade, upgrade userDatabase and relaunch it.
    @Override
    public void onUpgrade(SQLiteDatabase userDatabase, int i, int i1){
        userDatabase.execSQL("DROP Table IF EXISTS users");
        onCreate(userDatabase);
    }


    // Insert new user into the database
    public Boolean insertUser(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // place username and password into their respective table and columns
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = MyDB.insert("users", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }


    // Method to check if username is stored in database
    public Boolean checkUsername(String username){
        // Set database, then check if the username is in the user database
        SQLiteDatabase userDatabase = this.getWritableDatabase();
        Cursor cursor = userDatabase.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});

        // If the username already exists, return true. If not, return false.
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }


    // Method to check if an inputted password is correct
    public Boolean checkPassword(String username, String password){
        // Set database, then check all users to see if a username/password pair exists.
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[] {username, password});

        // If a username and password pair exists, return true. If not, return false.
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }
}
