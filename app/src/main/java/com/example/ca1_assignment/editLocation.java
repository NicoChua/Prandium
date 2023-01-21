package com.example.ca1_assignment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class editLocation extends AppCompatActivity implements View.OnClickListener {
    EditText tName, tLoc;
    PasswordDB db ;
    private Button backButton;
    LoginInfo userDetails;

    SharedPreferences prefs;
    Integer id;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String UId = "uId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.edit_profile);
        db  = new PasswordDB(this);

        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        id = prefs.getInt(UId,0);
        userDetails = db.getLoginInfo(id);

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

                //From nico : u shouldnt make a new user, but get the user using getLoginInfo()
                //              so dont do the 2 lines below
                String userPassword = "test";
                userDetails = new LoginInfo(userName, userPassword, userLoc);
                //instead, use sharedpreferences code to get id, code below
//                prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
//                id = prefs.getInt(UId,0);
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
