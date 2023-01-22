package com.example.ca1_assignment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class editLocation extends AppCompatActivity implements View.OnClickListener {
    EditText tName;
    TextView tLoc;
    PasswordDB db ;
    private Button backButton;
    LoginInfo userDetails;
    Spinner spinner;
    List<String> options;
    String location;
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
        Log.d("Log:",userDetails.getLocation());

        tName = (EditText) findViewById((R.id.updateName));
        tName.setText(userDetails.getName());
        tLoc = (TextView) findViewById((R.id.currentLoc));
        tLoc.setText("Current location: " + userDetails.getLocation().toString());
        spinner = findViewById(R.id.updateLocation);
        options = new ArrayList<>();
        options.add("North");
        options.add("South");
        options.add("East");
        options.add("West");
        spinner.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, options));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location = spinner.getSelectedItem().toString();
//                Toast.makeText(Register.this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
//                String userLoc = tLoc.getText().toString();


                prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                id = prefs.getInt(UId,0);
                db.updateContact(userDetails, userName, location);

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
                Intent i = new Intent(editLocation.this, Profile.class);
                startActivity(i);
                break;
        }


    }
}
