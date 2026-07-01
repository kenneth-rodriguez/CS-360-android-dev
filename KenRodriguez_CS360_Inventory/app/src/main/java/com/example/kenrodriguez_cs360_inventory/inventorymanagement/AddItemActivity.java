package com.example.kenrodriguez_cs360_inventory.inventorymanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kenrodriguez_cs360_inventory.R;

public class AddItemActivity extends AppCompatActivity {

    // Declare variables for UI buttons
    EditText itemNameField;
    EditText quantityField;
    Button addItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Hook up Java variables to UI buttons
        itemNameField = findViewById(R.id.itemNameEditTxt);
        quantityField = findViewById(R.id.quantityEditTxt);
        addItem = findViewById(R.id.addItemBttn);

        // Initialize inventory database helper
        InventoryDBHelper inventoryDatabase = new InventoryDBHelper(this);

        // When the add item button is clicked, add item to inventory database
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemName = itemNameField.getText().toString();
                String quantityString = quantityField.getText().toString();

                // If the item name field is empty, prompt user for input.
                if(itemName.equals("")){
                    Toast.makeText(AddItemActivity.this, "Please input an item name.", Toast.LENGTH_SHORT).show();
                }
                // If the quantity field is empty, prompt user for input.
                else if(quantityString.equals("")){
                    Toast.makeText(AddItemActivity.this, "Please input an item quantity.", Toast.LENGTH_SHORT).show();
                }
                // Else, attempt to add item to database
                else{
                    // Check if item name is already in database. IF not, add item to database
                    Boolean checkItemName = inventoryDatabase.checkItemName(itemName);
                    if (!checkItemName){
                        // Convert edittext quantity string to integer, then insert into database
                        int quantity = Integer.parseInt(quantityString);
                        Boolean insert = inventoryDatabase.insertItem(itemName, quantity);
                        // IF insertion was successful, inform the user and return to the database screen.
                        if (insert){
                            Toast.makeText(AddItemActivity.this, "Successfully added item", Toast.LENGTH_SHORT).show();
                            inventoryDatabase.close();
                            Intent intent = new Intent(getApplicationContext(), InventoryActivity.class);
                            startActivity(intent);
                        }
                        // ELSE, report failure
                        else{
                            Toast.makeText(AddItemActivity.this, "Failed to add item.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(AddItemActivity.this, "Item already exists.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}