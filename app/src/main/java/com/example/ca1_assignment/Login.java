package com.example.ca1_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {

    PasswordDB db ;
    TextView txtListContact ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        db  = new PasswordDB(this);
        List<LoginInfo> LoginInfo = db.getAllUsers();

        String username = intent.getStringExtra("name");
        String password = intent.getStringExtra("password");
        Boolean userFound = false;


        for (LoginInfo cn : LoginInfo) {
            if (cn.getName().equals(username) && cn.getPassword().equals(password)) {
                userFound = true;
            }
        }
        if (userFound == true) {
            new SweetAlertDialog(Login.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Message")
                    .setContentText("Log in successful!")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            Intent l = new Intent(Login.this , Home.class);
                            startActivity(l);
                        }
                    })
                    .show();
        } else {
            new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Error")
                    .setContentText("User not found, please try again!")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            Intent l = new Intent(Login.this , MainActivity.class);
                            startActivity(l);
                        }
                    })
                    .show();
        }
    }
}