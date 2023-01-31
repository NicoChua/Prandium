package com.example.ca1_assignment;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    PasswordDB db ;
    Integer id;
    String loc;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String UId = "uId";
    public static final String ULoc = "uLoc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddContact:
                Intent intent = getIntent();
                db  = new PasswordDB(this);
                List<LoginInfo> LoginInfo = db.getAllUsers();


                EditText txtname = findViewById(R.id.username);
                String username =  txtname.getText().toString();
                EditText txtpassword = findViewById(R.id.password);
                String password =  txtpassword.getText().toString();
                Boolean userFound = false;
                for (LoginInfo cn : LoginInfo) {
                    if (cn.getName().equals(username) && cn.getPassword().equals(password)) {
                        id = cn.getID();
                        loc = cn.getLocation();
                        userFound = true;
                    }
                }
                if (userFound == true) {
                    SharedPreferences sp = getSharedPreferences(MyPREFERENCES, Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt(UId, id);
                    editor.putString(ULoc, loc);
                    editor.commit();
                    Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();

                    new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Message")
                            .setContentText("Log in successful!")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent l = new Intent(MainActivity.this , Home.class);
                                    startActivity(l);
                                }
                            })
                            .show();
                } else {
                    new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText("User not found, please try again!")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent l = new Intent(MainActivity.this , MainActivity.class);
                                    startActivity(l);
                                }
                            })
                            .show();
                }
                break;
            case R.id.btnListAll:
                Intent l = new Intent(this, Register.class);
                startActivity(l);
                break;
        }
    }

}