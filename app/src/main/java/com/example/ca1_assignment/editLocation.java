package com.example.ca1_assignment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class editLocation extends AppCompatActivity implements View.OnClickListener {
    EditText tName, tLoc ;
    int userID;
    PasswordDB db ;
    private Button backButton;
    LoginInfo userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.edit_profile);
        db  = new PasswordDB(this);
        userDetails = db.getLoginInfo(int id)
        tName = (EditText) findViewById((R.id.updateName));
        tLoc = (EditText) findViewById((R.id.updateLocation));

//        backButton = (Button) findViewById(R.id.updateBackButton);
//
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(editLocation.this, MainActivity.class);
//                startActivity(i);
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updateButton:
                String userName = tName.getText().toString();
                String userLoc = tLoc.getText().toString();
                //REMEMBER CHANGE TO EDIT
//                db.addUser(new User(userName, userLoc));
                userDetails = new LoginInfo(userID, userName, userLoc)
                db.updateContact(userDetails);

                // Reading all contacts
                Log.d("Reading: ", "Reading all contacts..");
                List<LoginInfo> users = db.getAllUsers();

                for (LoginInfo cn : users) {
                    String log = "Id: " + cn.getID() + " ,Name: " + cn.getName() + " ,Location: " +
                            cn.getLocation();
                    // Writing Contacts to log
                    Log.d("Name: ", log);
                }
                break;
            case R.id.updateBackButton:
                Intent i = new Intent(editLocation.this, MainActivity.class);
                startActivity(i);
                break;
        }


    }
}
