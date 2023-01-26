package com.example.ca1_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Profile extends AppCompatActivity implements View.OnClickListener{
    private ImageButton editProfile;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String UId = "uId";
    private PasswordDB db ;
    private Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        getSupportActionBar().hide();

//        editProfile = (ImageButton) findViewById(R.id.editProfile);
//
//        editProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(Profile.this, editLocation.class);
//                startActivity(i);
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editProfile:
                Intent i = new Intent(this, Home.class);
                startActivity(i);
                break;
            case R.id.logoutAcc:
                new SweetAlertDialog(Profile.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Logout?")
                        .setContentText("Are you sure you want to logout?")
                        .setConfirmText("Logout")
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.clear();
                                editor.apply();
                                sweetAlertDialog.dismissWithAnimation();
                                Intent l = new Intent(Profile.this , Login.class);
                                startActivity(l);
                            }
                        })
                        .show();
                break;
            case R.id.deleteAcc:
                new SweetAlertDialog(Profile.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Delete Account?")
                        .setContentText("You won't be able to recover this account!")
                        .setConfirmText("Delete")
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                                id = prefs.getInt(UId,0);
                                db  = new PasswordDB(Profile.this);
                                db.deleteContact(id);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.clear();
                                editor.apply();
                                sweetAlertDialog.dismissWithAnimation();
                                Intent l = new Intent(Profile.this , Login.class);
                                startActivity(l);
                            }
                        })
                        .show();
                break;
        }
    }
}