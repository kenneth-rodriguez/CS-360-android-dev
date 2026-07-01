/*
 * Class to contain inventory card information.
 * Each field corresponds to a different field on the inventory card (and one removed one):
 * item name, quantity, and icon image.
 */
package com.example.kenrodriguez_cs360_inventory.inventorymanagement;

import com.example.kenrodriguez_cs360_inventory.R;

// Class to contain inventory card information (for the time being)
// Each field corresponds to a different field on the inventory card: name, quantity, and icon image.
public class InventoryItems {

    private String itemName;
    private int itemQuantity;
    private int itemImage;

    public InventoryItems(String inventoryName, int inventoryCount){
        this.itemName = inventoryName;
        this.itemQuantity = inventoryCount;
        this.itemImage = R.drawable.testimg;
    }

    public String getItemName(){
        return itemName;
    }

    public void setItemName(String itemName){
        this.itemName = itemName;
    }

    public int getItemQuantity(){
        return itemQuantity;
    }

    public void setItemQuantity(int count){
        this.itemQuantity = count;
    }

    public int getItemImage(){
        return itemImage;
    }

    public void setItemImage(int image){
        this.itemImage = image;
    }
}
