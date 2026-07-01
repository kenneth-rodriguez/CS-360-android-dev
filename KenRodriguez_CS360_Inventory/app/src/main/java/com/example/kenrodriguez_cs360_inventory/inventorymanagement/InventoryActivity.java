/*
 * Class to maintain all inventory activity.
 *
 * More specifically, this class "holds" the cardview inventory adapter and drives the "main"
 * screen of the application.
 */

package com.example.kenrodriguez_cs360_inventory.inventorymanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kenrodriguez_cs360_inventory.R;
import com.example.kenrodriguez_cs360_inventory.SMSmanagement.PermissionsActivity;
import com.example.kenrodriguez_cs360_inventory.SMSmanagement.PreferenceHelper;
import com.example.kenrodriguez_cs360_inventory.inventorymanagement.AddItemActivity;
import com.example.kenrodriguez_cs360_inventory.inventorymanagement.InventoryAdapter;
import com.example.kenrodriguez_cs360_inventory.inventorymanagement.InventoryDBHelper;
import com.example.kenrodriguez_cs360_inventory.inventorymanagement.InventoryItems;

import java.util.ArrayList;


// Display inventory screen/view all cards
public class InventoryActivity extends AppCompatActivity {

    // Declare variables
    private RecyclerView recyclerView;
    private ArrayList<InventoryItems> inventoryItemArray;
    public static final String PREFS_NAME = "UserPermissions";
    SharedPreferences preferences;

    // On create, add items to the inventory array and prepare the recyclerView for inventory item cards
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        recyclerView = findViewById(R.id.inventoryRecyclerView);

        // Collect SharedPreferences data.
        preferences = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);

        // Initialize inventory database helper
        InventoryDBHelper inventoryDBHelper = new InventoryDBHelper(this);

        // Create new inventory item array with database
        inventoryItemArray = new ArrayList<>();
        inventoryItemArray = inventoryDBHelper.readInventoryItems();

        // Initialize the adapter class (InventoryAdapter) and send it the inventoryItemArray
        InventoryAdapter inventoryAdapter = new InventoryAdapter(this, inventoryItemArray);

        // Set up a linear layout manager for a vertically scrolling list
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // send the inventory adapter and layout manager to the recycler view
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(inventoryAdapter);
    }


    // On create, also set up the top bar options for adding items, searching for items, and settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.mainmenu,menu);
        return true;
    }

    // Check if any top bar options have been pressed, then perform the associated actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // If add item button is pressed, bring the user to the AddItemActivity screen
        if (item.getItemId() == R.id.add_item){
            Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
            startActivity(intent);
        }

        // If options gear button is pressed, bring the user to the settings screen
        if (item.getItemId() == R.id.settings){
            Intent intent = new Intent(getApplicationContext(), PermissionsActivity.class);
            startActivity(intent);
        }
        return true;
    }

    // Send SMS message if permission is granted
    public static void sendSMSMessage(Context context, String itemName){
        PreferenceHelper preferenceHelper = new PreferenceHelper(context);
        final boolean permissionLevel = preferenceHelper.getPermission(context);
        String phoneNumber = preferenceHelper.getPhoneNumber(context).toString();
        String depletedMessage = "Stock of the item '" + itemName + "' is now at 0.";

        // Check if SMS permissions have been granted. If so, text the saved user phone number.
        try {
            SmsManager textManager = SmsManager.getDefault();
            textManager.sendTextMessage(phoneNumber, null, depletedMessage, null, null);
            Toast.makeText(context, "Message sent.", Toast.LENGTH_SHORT).show();
        }
        catch (Exception exception){
            if (permissionLevel) {
                Toast.makeText(context, "Error sending message.", Toast.LENGTH_SHORT).show();
            }
            exception.printStackTrace();
        }
    }
}