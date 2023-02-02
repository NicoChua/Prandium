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
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    public static final String UCount = "uCount";
    int count = 0;

    //Creating arraylist to store details
    ArrayList<String> images = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> descriptions = new ArrayList<>();
    ArrayList<String> locations = new ArrayList<>();
    ArrayList<String> links = new ArrayList<>();


    public void retrieveData() {
        //Gets location of user
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String userLocation = prefs.getString(ULoc, "");

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Location").child(userLocation);
//         Read from the database
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String value = snapshot.getKey(); //locationID
                    Log.d(TAG, "\nName is: " + value);
                    locations.add(value); //locationid
                    String name = snapshot.child("Name").getValue().toString(); //Name
                    names.add(name);
                    String description = snapshot.child("Description").getValue().toString(); //Description
                    descriptions.add(description);
                    String image = snapshot.child("Image").getValue().toString(); //Image
                    images.add(image);
                    String link = snapshot.child("Link").getValue().toString(); //Link
                    links.add(link);
                    name2.setText(names.get(count));
                    image2 = (ImageView) findViewById(R.id.image);
                    String photo_url = images.get(count);
                    new ImageLoadTask(photo_url, image2).execute();

                    //check if user already add location to favourites
                    SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                    int id = prefs.getInt(UId, 0);
                    PasswordDB db = new PasswordDB(Home.this);
                    LoginInfo user = db.getLoginInfo(id);
                    Button button = (Button) findViewById(R.id.favourite);
                    if (user.getFavourites() != null) {
                        ArrayList<String> currentFavourites = db.convertStringToArray(user.getFavourites());
                        for (int j = 0; j < currentFavourites.size(); j++) {
                            if (currentFavourites.get(j).equals(locations.get(count))) {
                                button.setText("Added");
                                button.setBackgroundColor(Color.GREEN);
                            }
                        }
                    }
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
        getSupportActionBar().hide();
        name2 = (TextView) findViewById(R.id.name2);
        retrieveData();

        SharedPreferences sp = getSharedPreferences(MyPREFERENCES, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(UCount, count);
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.favourite:
                SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                int id = prefs.getInt(UId, 0);
                PasswordDB db = new PasswordDB(Home.this);
                LoginInfo user = db.getLoginInfo(id);
                Boolean added = false;
                if (user.getFavourites() == null) {
                    if (count >= 0 && count < locations.size()) {
                        db.addFavourite(user, locations.get(count));
                        user.setFavourites(locations.get(count));

                        Button button = (Button) findViewById(R.id.favourite);
                        button.setText("Added");
                        button.setBackgroundColor(Color.GREEN);
                    }
                } else {
                    //check if user already added location to favourites
                    ArrayList<String> currentFavourites = db.convertStringToArray(user.getFavourites());
                    for (int i = 0; i < currentFavourites.size(); i++) {
                        if (currentFavourites.get(i).equals(locations.get(count))) {
                            added = true;
                            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
                            Button button = (Button) findViewById(R.id.favourite);
                            button.setText("Added");
                            button.setBackgroundColor(Color.GREEN);
                        }
                    }
                    if (added == false) {
                        if (count >= 0 && count < locations.size()) {
                            db.addFavourite(user, locations.get(count));
                            user.setFavourites(locations.get(count));

                            Button button = (Button) findViewById(R.id.favourite);
                            button.setText("Added");
                            button.setBackgroundColor(Color.GREEN);
                        }
                    }
                }
                break;
            case R.id.next:
                if ((count + 1) < names.size()) {
                    count++;
                    name2 = (TextView) findViewById(R.id.name2);
                    name2.setText(names.get(count));

                    image2 = (ImageView) findViewById(R.id.image);
                    String photo_url = images.get(count);
                    new ImageLoadTask(photo_url, image2).execute();
                    Log.d(TAG, "\nName is: " + names.get(count));

                    Button button = (Button) findViewById(R.id.favourite);
                    button.setText("Favourite");
                    button.setBackgroundColor(Color.parseColor("#a4cc44"));

                    SharedPreferences sp = getSharedPreferences(MyPREFERENCES, Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt(UCount, count);
                    editor.commit();
                }
                break;
            case R.id.linearLayout:
                Intent h = new Intent(this, DetailedDescription.class);
                h.putExtra("locationName", names.get(count));
                h.putExtra("locationImage", images.get(count));
                h.putExtra("locationDesc", descriptions.get(count));
                h.putExtra("locationLink", links.get(count));
//                h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(h);
                break;
            case R.id.toProfile:
                Intent m = new Intent(this, Profile.class);
                startActivity(m);
                break;
        }
    }
}