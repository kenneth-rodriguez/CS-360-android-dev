/*
 * Adapter to help display item cards and maintain button functions.
 *
 * For each item created by the InventoryDBHelper, the InventoryAdapter creates a card from the
 * inventory_card_layout template. Each card displays an item's name, quantity, and buttons to add
 * or remove items from that quantity value. A button has also been placed to allow for an item's
 * deletion. If an item's quantity is zero, the application will attempt to send an SMS message.
 */

package com.example.kenrodriguez_cs360_inventory.inventorymanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kenrodriguez_cs360_inventory.R;

import java.util.ArrayList;


public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.Viewholder> {

    // Create context and an array to hold inventory items
    private Context context;
    private ArrayList<InventoryItems> inventoryItemArray;

    // Constructor
    public InventoryAdapter(Context context, ArrayList<InventoryItems> inventoryItemArray) {
        this.context = context;
        this.inventoryItemArray = inventoryItemArray;
    }

    @NonNull
    @Override
    public InventoryAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_card_layout, parent, false);
        return new Viewholder(view);
    }

    /*
     *  Card information setting and general button usage lives here
     */
    @Override
    public void onBindViewHolder(@NonNull InventoryAdapter.Viewholder holder, int position) {
        // Set data to textview and imageview of each card
        InventoryItems thisItem = inventoryItemArray.get(position);
        holder.inventoryName.setText(thisItem.getItemName());
        holder.inventoryCount.setText("" + thisItem.getItemQuantity());

        // Create inventory helper to assist with card functions
        InventoryDBHelper inventoryDB = new InventoryDBHelper(context);

        // Disable "minus" button if card is created w/ quantity of 0
        if (thisItem.getItemQuantity() == 0){
            holder.reduceQuantity.setClickable(false);
            holder.reduceQuantity.setAlpha(.5f);
        }

        // addQuantity button to incrementally add to item's quantity
        holder.addQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if reduce quantity is unclickable, make it clickable again
                holder.reduceQuantity.setClickable(true);
                holder.reduceQuantity.setAlpha(1);

                // Update database quantity
                int updateValue = thisItem.getItemQuantity() + 1;
                inventoryDB.updateQuantity(thisItem.getItemName(), updateValue);

                // Update array quantity
                thisItem.setItemQuantity(updateValue);
                holder.inventoryCount.setText("" + updateValue);
            }
        });

        // reduceQuantity button to incrementally reduce the item's quantity
        holder.reduceQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // If number of objects is greater than zero, update item quantity & quantity text
                if (thisItem.getItemQuantity() > 0) {
                    // Update database quantity
                    int updateValue = thisItem.getItemQuantity() - 1;
                    inventoryDB.updateQuantity(thisItem.getItemName(), updateValue);

                    // Update array quantity
                    thisItem.setItemQuantity(updateValue);
                    holder.inventoryCount.setText("" + updateValue);

                    if ((holder.inventoryCount.getText().toString()).equals("0")) {
                        holder.reduceQuantity.setAlpha(.5f);
                        holder.reduceQuantity.setClickable(false);
                        InventoryActivity.sendSMSMessage(context, thisItem.getItemName());
                    }
                }
            }
        });

        // deleteItem button to remove item from database
        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove item from database
                inventoryDB.deleteItem(thisItem.getItemName());

                // Remove item from array
                inventoryItemArray.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }

    // Show the number of cards in the recycler view
    @Override
    public int getItemCount() {
        return inventoryItemArray.size();
    }

    //  Assign UI elements here
    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageView inventoryImageView;
        private TextView inventoryName, inventoryCount;
        private Button addQuantity;
        private Button reduceQuantity;
        private Button deleteItem;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            inventoryImageView = itemView.findViewById(R.id.itemThumbnail);
            inventoryName = itemView.findViewById(R.id.itemName);
            inventoryCount = itemView.findViewById(R.id.itemQuantity);
            addQuantity = itemView.findViewById(R.id.buttonAddQuantity);
            reduceQuantity = itemView.findViewById(R.id.buttonReduceQuantity);
            deleteItem = itemView.findViewById(R.id.buttonDeleteItem);
        }
    }
}