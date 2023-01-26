package com.example.ca1_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class favourites extends AppCompatActivity {
    PasswordDB db ;
    LoginInfo userDetails;
    SharedPreferences prefs;
    Integer id;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String UId = "uId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        getSupportActionBar().hide();

        db  = new PasswordDB(this);
        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        id = prefs.getInt(UId,0);
        userDetails = db.getLoginInfo(id);

        ArrayList<String> favourites = db.convertStringToArray(userDetails.getFavourites());
        for (int i = 0 ; i < favourites.size() ; i++) {
            //T.B.C TO IMPLEMENT RETRIEVE FROM FIREBASE AND DISPLAY OF FAVOURITES USING CARD/RECYCLER VIEW
        }
    }
}