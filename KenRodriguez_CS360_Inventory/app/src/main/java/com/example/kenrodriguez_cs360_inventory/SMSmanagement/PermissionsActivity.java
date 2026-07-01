/*
 * The permissions screen.
 *
 * Collects a phone number and enables permissions; a permission variable is stored with phone numbers
 * inside the SharedPreferences file. The permissions variable makes InventoryActivity's SMS service
 * a little cleaner and more consistent.
 */

package com.example.kenrodriguez_cs360_inventory.SMSmanagement;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kenrodriguez_cs360_inventory.R;

public class PermissionsActivity extends AppCompatActivity {

    // Initialize UI buttons
    Button notificationButton;
    Button addPhoneNumber;
    EditText phoneNumTxt;
    String phoneString;
    AlertDialog smsDialog = null;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        // Hook up UI elements
        notificationButton = findViewById(R.id.enableNotifications);
        addPhoneNumber = findViewById(R.id.addPhoneNumber);
        phoneNumTxt = findViewById(R.id.editTextPhone);

        // Create a phone number database helper
        PreferenceHelper preferenceHelper = new PreferenceHelper(this);

        // If notification permissions have been granted, disable permissions button.
        if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            notificationButton.setClickable(false);
            notificationButton.setAlpha(.5f);
        }

        // Listen for addPhoneNumber button to be clicked. If so, update the phone number saved in
        // the SharedPreferences database.
        addPhoneNumber.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                phoneString = phoneNumTxt.getText().toString();

                // If phone number field is blank, prompt input
                if(phoneString.equals("")){
                    Toast.makeText(PermissionsActivity.this, "Please input a phone number.", Toast.LENGTH_SHORT).show();
                }
                else if (phoneString.length() != 10){
                    Toast.makeText(PermissionsActivity.this, "Please input a 10-digit phone number.", Toast.LENGTH_SHORT).show();
                }
                // Otherwise, check if phone number exists in the phone number database
                else{
                    //Boolean checkPhoneNumber = phoneDB.checkPhoneNumber(phoneString);
                    String checkPhoneNumber =  preferenceHelper.getPhoneNumber(PermissionsActivity.this);
                    // If the phone number exists in the database, inform the user
                    if (checkPhoneNumber.equals(phoneString)){
                        Toast.makeText(PermissionsActivity.this, "Current phone number.", Toast.LENGTH_SHORT).show();
                    }
                    // If the number isn't already in the database, prompt user for permission
                    else {
                        preferenceHelper.setPhoneNumber(PermissionsActivity.this, phoneString);
                        Toast.makeText(PermissionsActivity.this, "Phone number successfully updated.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Listen for notificationButton to be clicked
        notificationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // On click, request SMS permissions from the user.
                if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                    Log.d("permission", "permission denied to SEND_SMS - requesting it");
                    String[] permissions = {Manifest.permission.SEND_SMS};
                    requestPermissions(permissions, 1);

                    // If user enables permissions, update the preferences file for greater notification
                    // precision.
                    if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        preferenceHelper.setPermission(PermissionsActivity.this, true);
                        notificationButton.setClickable(false);
                        notificationButton.setAlpha(.5f);
                    }
                }
                // If user enables permissions, update the preferences file for greater notification
                // precision.
                if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                    preferenceHelper.setPermission(PermissionsActivity.this, true);
                    notificationButton.setClickable(false);
                    notificationButton.setAlpha(.5f);
                }
            }
        });
    }
}