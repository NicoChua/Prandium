package com.example.ca1_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {

    PasswordDB db ;
    TextView txtListContact ;
    //Storing ID in SharedPreferences
    Integer id;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String UId = "uId";


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


        //Note from de xun: Once i logout, there will be a 'no user found' alert
        //as it actvates it onCreate. Maybe you can change to onClick instead of onCreate?
        //If you're unclear, you can try logging in and logout to see. Because currently
        //everytime you click login, you create a new page together with the alert. Instead of this,
        //just do onClick so it doesnt direct the user to a new page with the prompt itself.
        for (LoginInfo cn : LoginInfo) {
            if (cn.getName().equals(username) && cn.getPassword().equals(password)) {
                id = cn.getID();
                userFound = true;
            }
        }
        if (userFound == true) {
            SharedPreferences sp = getSharedPreferences(MyPREFERENCES, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(UId, id);
            editor.commit();
            //show id, not necessary
            Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();

            new SweetAlertDialog(Login.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Message")
                    .setContentText("Log in successful!")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            Intent l = new Intent(Login.this , uploadImage.class);
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