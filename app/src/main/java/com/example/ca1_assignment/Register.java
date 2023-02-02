package com.example.ca1_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import cn.pedant.SweetAlert.SweetAlertDialog;

import android.graphics.Color;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    EditText tName, tPhone;
    PasswordDB db;
    Spinner spinner;
    List<String> options;
    String location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new PasswordDB(this);
        tName = (EditText) findViewById((R.id.edtext_name));
        tPhone = (EditText) findViewById((R.id.edtext_password));
        spinner = findViewById(R.id.location);
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
                Toast.makeText(Register.this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddNewContactInfo:
                String contactName = tName.getText().toString();
                String password = tPhone.getText().toString();

                if (!contactName.isEmpty() && !password.isEmpty()) {
                    LoginInfo newUser = new LoginInfo(contactName, password, location, "", "");

                    // we are use add value event listener method
                    // which is called with database reference.
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // inside the method of on Data change we are setting
                            // our object class to our database reference.
                            // data base reference will sends data to firebase.
                            mDatabase.setValue(newUser);

                            // after adding this data we are showing toast message.
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // if the data is not added or it is cancelled then
                            // we are displaying a failure toast message.
                        }
                    });

                    db.addContact(new LoginInfo(contactName, password, location, null, null));
                    new SweetAlertDialog(Register.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Message")
                            .setContentText("You are Registered!")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent l = new Intent(Register.this, MainActivity.class);
                                    startActivity(l);
                                }
                            })
                            .show();

                } else {
                    new SweetAlertDialog(Register.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText("Username and Password required!")
                            .setConfirmText("OK")
                            .show();
                }
        }
    }

}