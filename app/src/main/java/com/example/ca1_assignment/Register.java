package com.example.ca1_assignment;

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

public class Register extends AppCompatActivity implements View.OnClickListener{

    EditText tName, tPhone ;
    PasswordDB db ;
    Spinner spinner;
    List<String> options;
    String location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db  = new PasswordDB(this);
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
                    db.addContact(new LoginInfo(contactName, password, location));
                    new SweetAlertDialog(Register.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Message")
                            .setContentText("You are Registered!")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent l = new Intent(Register.this , MainActivity.class);
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