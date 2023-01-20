package com.example.ca1_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import cn.pedant.SweetAlert.SweetAlertDialog;
import android.graphics.Color;

import java.util.List;

public class Register extends AppCompatActivity implements View.OnClickListener{

    EditText tName, tPhone ;
    PasswordDB db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db  = new PasswordDB(this);
        tName = (EditText) findViewById((R.id.edtext_name));
        tPhone = (EditText) findViewById((R.id.edtext_password));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddNewContactInfo:
                String contactName = tName.getText().toString();
                String password = tPhone.getText().toString();

                if (!contactName.isEmpty() && !password.isEmpty()) {
                    db.addContact(new LoginInfo(contactName, password));
                    new SweetAlertDialog(Register.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Message")
                            .setContentText("You are Registered!")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent l = new Intent(Register.this , Login.class);
                                    startActivity(l);
                                }
                            })
                            .show();
                }
        }


    }

}