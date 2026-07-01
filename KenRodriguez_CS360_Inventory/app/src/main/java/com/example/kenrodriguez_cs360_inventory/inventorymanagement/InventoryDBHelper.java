/*
 * SQLite inventory database helper.
 *
 * Will create and read a SQLite database to persistently store inventory item data.
 *
 * Methods are included to insert new items, delete existing items, update item quantities, find
 * existing item names (primary keys), and convert the database to an ArrayList for easy modification.
 */

package com.example.kenrodriguez_cs360_inventory.inventorymanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

// Create a helper to set up SQLite database management
public class InventoryDBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "inventoryDatabase.db";
    public InventoryDBHelper(Context context){
        super(context, "inventoryDatabase.db", null, 1);
    }

    // onCreate, use SQLite to create the table items with item name, quantity, and image sets
    @Override
    public void onCreate(SQLiteDatabase inventoryDatabase){
        inventoryDatabase.execSQL("CREATE TABLE items(itemName TEXT PRIMARY KEY, itemCount INTEGER, itemImage INTEGER)");
    }


    // onUpgrade, upgrade inventoryDatabase and relaunch it.
    @Override
    public void onUpgrade(SQLiteDatabase inventoryDatabase, int i, int i1) {
        inventoryDatabase.execSQL("DROP TABLE IF EXISTS users");
        onCreate(inventoryDatabase);
    }


    // method to add new items to the inventoryDatabase database
    public Boolean insertItem(String name, int quantity){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // pass inputted variables into each section's value, then insert item into database
        contentValues.put("itemName", name);
        contentValues.put("itemCount", quantity);
        //contentValues.put("itemImage", image);
        MyDB.insert("items", null, contentValues);

        // After adding the new item, close the database and return true.
        MyDB.close();
        return true;
    }

    // method to delete items from the inventoryDatabase database
    public void deleteItem(String name){
        // Open writable inventory database
        SQLiteDatabase MyDB = this.getWritableDatabase();

        // Delete item from database using itemName as its primary key, then close the database.
        MyDB.delete("items", "itemName=?", new String[]{name});
        MyDB.close();
    }

    // method to update item quantity
    public void updateQuantity(String name, int quantity){
        // Open writable inventory database
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("itemCount", quantity);

        // Find item
        MyDB.update("items", contentValues, "itemName=?", new String[]{name});
    }

    // Method to check if item is already stored in database
    public Boolean checkItemName(String itemName){
        // Set database, then check if the username is in the user database
        SQLiteDatabase inventoryDatabase = this.getWritableDatabase();
        Cursor cursor = inventoryDatabase.rawQuery("SELECT * FROM items WHERE itemName = ?", new String[]{itemName});

        // If the item already exists, return true. If not, return false.
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }


    // Method to convert inventory database to arraylist
    public ArrayList<InventoryItems> readInventoryItems(){
        // Read the InventoryDB and create a cursor to query that database
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM items", null);

        // Create a new ArrayList to hold inventory items
        ArrayList<InventoryItems> inventoryItemsArrayList = new ArrayList<>();

        // Place the cursor to the first position in the database
        if (cursor.moveToFirst()){
            do {
                //Move data from the cursor to the array
                inventoryItemsArrayList.add(new InventoryItems(cursor.getString(0),
                        cursor.getInt(1)));
            }
            // After placing data in the array, move to the next item
            while(cursor.moveToNext());
            }
        // After collecting data, close the cursor and return array list
        cursor.close();
        return inventoryItemsArrayList;
    }
}