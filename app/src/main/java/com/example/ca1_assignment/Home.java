package com.example.ca1_assignment;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements View.OnClickListener{

    TextView name2;
    ImageView image2;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String UId = "uId";
    public static final String ULoc = "uLoc";
    int count = 0;

    //Creating arraylist to store details
    ArrayList<String> images = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> descriptions = new ArrayList<>();
    ArrayList<String> locations = new ArrayList<>();


    public void retrieveData() {
        //Gets location of user
    SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    String userLocation = prefs.getString(ULoc,""); //temporary for now

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Location").child(userLocation);
//         Read from the database
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String value = snapshot.getKey(); //locationID
                    locations.add(value);
                    String name = snapshot.child("Name").getValue().toString(); //Name
                    names.add(name);
                    String description = snapshot.child("Description").getValue().toString(); //Description
                    descriptions.add(description);
                    String image = snapshot.child("Image").getValue().toString(); //Image
                    images.add(image);
                    Log.d(TAG, "\nName is: " + names.get(count));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        name2 = (TextView) findViewById(R.id.name2);
        retrieveData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.favourite:
                int count2 = count-1;
                if (count2 >= 0 &&  count2 < locations.size()) {
                    Toast.makeText(this, String.valueOf(locations.get(count2)), Toast.LENGTH_SHORT).show();
                    SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                    int id = prefs.getInt(UId,0);
                    PasswordDB db  = new PasswordDB(Home.this);
                    LoginInfo user = db.getLoginInfo(id);
                    db.addFavourite(user, locations.get(count2));
                }

                //intent to favourites page
                //
                break;
            case R.id.next:
                if (count < names.size()) {
                    name2 = (TextView) findViewById(R.id.name2);
                    name2.setText(names.get(count));

                    image2 = (ImageView) findViewById(R.id.image);
                    String photo_url = images.get(count);
                    new ImageLoadTask(photo_url, image2).execute();

                    count++;
                }
                break;
            case R.id.btnAddContact:
                EditText txtname = findViewById(R.id.username);
                String name =  txtname.getText().toString();
                EditText txtpassword = findViewById(R.id.password);
                String password =  txtpassword.getText().toString();

                Intent i = new Intent(this, Login.class);

                i.putExtra("name", name);
                i.putExtra("password", password);

                startActivity(i);
                break;
            case R.id.btnListAll:
                Intent l = new Intent(this, Register.class);
                startActivity(l);
                break;
            case R.id.toProfile:
                Intent m = new Intent(this, Profile.class);
                startActivity(m);
                break;
        }
    }
}