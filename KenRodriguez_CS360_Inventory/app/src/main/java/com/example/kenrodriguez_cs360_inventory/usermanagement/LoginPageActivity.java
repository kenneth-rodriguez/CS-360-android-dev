package com.example.kenrodriguez_cs360_inventory.usermanagement;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.kenrodriguez_cs360_inventory.R;
import com.example.kenrodriguez_cs360_inventory.inventorymanagement.InventoryActivity;

public class LoginPageActivity extends AppCompatActivity {

    // Declare variables for UI buttons
    EditText usernameField;
    EditText passwordField;
    Button login;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        // Hook up Java variables to UI buttons on LoginPageActivity
        usernameField = findViewById(R.id.editTxtUsername);
        passwordField = findViewById(R.id.editTxtPassword);
        login = findViewById(R.id.bttnLogin);
        register = findViewById(R.id.bttnRegister);

        // Set up a database helper for the user database
        UserDBHelper userDatabase = new UserDBHelper(this);

        // When the register button is clicked, add a valid username and password to the user database
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();

                // IF the username field is empty, prompt user to input a username
                if (username.equals("")){
                    Toast.makeText(LoginPageActivity.this, "Please input a username.", Toast.LENGTH_SHORT).show();
                }
                // ELSE, IF the password field is empty, prompt user to input a password
                else if (password.equals("")) {
                    Toast.makeText(LoginPageActivity.this, "Please input a password.", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Check the database to see if the attempted username already exists.
                    // IF the username doesn't exist, try to add entry to the user database.
                    Boolean checkUsername = userDatabase.checkUsername(username);
                    if(!checkUsername){
                        Boolean insert = userDatabase.insertUser(username, password);
                        // IF data is successfully added to the user database, inform user, then go to the main Inventory page
                        if (insert) {
                            Toast.makeText(LoginPageActivity.this, "Successful Registration", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), InventoryActivity.class);
                            startActivity(intent);
                        }
                        // ELSE, report failure
                        else {
                            Toast.makeText(LoginPageActivity.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                    // If the username already exists, inform the user.
                    else{
                        Toast.makeText(LoginPageActivity.this, "Username already exists.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // When login button is clicked, compare credentials with the user database.
        // If everything checks out, bring the user to the InventoryActivity page.
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // initialize username/password variables
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();

                // IF the username field is empty, prompt user to input a username
                if (username.equals("")){
                    Toast.makeText(LoginPageActivity.this, "Please input a username.", Toast.LENGTH_SHORT).show();
                }
                // ELSE, IF the password field is empty, prompt user to input a password
                else if (password.equals("")) {
                    Toast.makeText(LoginPageActivity.this, "Please input a password.", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Check if the username and password exist in the database.
                    Boolean checkPassword = userDatabase.checkPassword(username, password);

                    // if combination exists, login and bring user to inventory activity page.
                    if (checkPassword){
                        Toast.makeText(LoginPageActivity.this, "Successful Login.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), InventoryActivity.class);
                        startActivity(intent);
                    }
                    // if combination is not found, inform user
                    else{
                        Toast.makeText(LoginPageActivity.this, "Login not found.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}