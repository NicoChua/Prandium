package com.example.ca1_assignment;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class favourites extends AppCompatActivity {
    PasswordDB db ;
    LoginInfo userDetails;
    SharedPreferences prefs;
    Integer id;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String UId = "uId";
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;

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
            Log.d(TAG,"Favourites array: " + favourites.get(i));
            //T.B.C TO IMPLEMENT RETRIEVE FROM FIREBASE AND DISPLAY OF FAVOURITES USING CARD/RECYCLER VIEW
        }

    }
}