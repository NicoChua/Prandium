package com.example.ca1_assignment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ImagesActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListener, View.OnClickListener {
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private ProgressBar mProgressCircle;
    PasswordDB db;
    LoginInfo userDetails;
    SharedPreferences prefs;
    Integer id;
    ArrayList<String> userFavourites;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String UId = "uId";
    public static final String UCount = "uCount";
    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;


    //Creating arraylist to store details
    ArrayList<String> images = new ArrayList<String>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> descriptions = new ArrayList<>();
    ArrayList<String> locations = new ArrayList<>();
    ArrayList<String> links = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        getSupportActionBar().hide();

        db = new PasswordDB(this);
        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        id = prefs.getInt(UId, 0);
        userDetails = db.getLoginInfo(id);

        mProgressCircle = findViewById(R.id.progress_circle);
        if (userDetails.getFavourites() == null || userDetails.getFavourites().equals("")) {
            new android.os.Handler(Looper.getMainLooper()).postDelayed(
                    new Runnable() {
                        public void run() {
                            // Make an alert for no liked places
                            new SweetAlertDialog(ImagesActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("No liked places yet")
                                    .setContentText("Add some to your favourites!")
                                    .setConfirmText("OK")
                                    .show();
                            mProgressCircle.setVisibility(View.INVISIBLE);
                        }
                    },
                    3000);
        } else {
            userFavourites = db.convertStringToArray(userDetails.getFavourites());
            mRecyclerView = findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


            mUploads = new ArrayList<>();

            mDatabaseRef = FirebaseDatabase.getInstance().getReference("Location").child(userDetails.getLocation());
            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String locationID = postSnapshot.getKey();
                        if (userFavourites.contains(locationID)) {
                            locations.add(locationID);
                            String name = postSnapshot.child("Name").getValue().toString(); //Name
                            names.add(name);
                            String description = postSnapshot.child("Description").getValue().toString(); //Description
                            descriptions.add(description);
                            String image = postSnapshot.child("Image").getValue().toString(); //Image
                            images.add(image);
                            String link = postSnapshot.child("Link").getValue().toString(); //Link
                            links.add(link);
                            Upload upload = new Upload(name, image, description, link);
                            upload.setKey(locationID);
//                            Log.d("testing21", "key:" + upload.getKey());
                            mUploads.add(upload);
                        }

                    }

                    mAdapter = new ImageAdapter(ImagesActivity.this, (ArrayList<Upload>) mUploads);

                    mRecyclerView.setAdapter(mAdapter);

                    mAdapter.setOnItemClickListener(ImagesActivity.this);

                    mProgressCircle.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(ImagesActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    mProgressCircle.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Normal click at position: " + position, Toast.LENGTH_SHORT).show();
        Intent h = new Intent(this, DetailedDescription.class);

        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        int count = prefs.getInt(UCount, 0);
        h.putExtra("locationName", names.get(position));
        h.putExtra("locationImage", images.get(position));
        h.putExtra("locationDesc", descriptions.get(position));
        h.putExtra("locationLink", links.get(position));

        Log.d(TAG, "\nlocation is: " + names.get(position));
        Log.d(TAG, "\nimages is: " + images.get(position));
        Log.d(TAG, "\ndescriptions is: " + descriptions.get(position));
        startActivity(h);
    }

    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(this, "Selected at position: " + position, Toast.LENGTH_SHORT).show();
        //intent
    }

    @Override
    public void onDeleteClick(int position) {
        // Prompt for delete confirmation
        new SweetAlertDialog(ImagesActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Remove Favourite?")
                .setContentText("Are you sure you want to remove it?")
                .setConfirmText("Confirm")
                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        // Removing item from favourites array
                        Upload selectedItem = mUploads.get(position);
                        String selectedKey = selectedItem.getKey();
                        db.deleteFavourites(userDetails, selectedKey);
                        userDetails = db.getLoginInfo(id);
                        sweetAlertDialog.dismissWithAnimation();
                        Toast.makeText(ImagesActivity.this, "Successfully Deleted, refreshing page...", Toast.LENGTH_SHORT).show();
                        Intent l = new Intent(ImagesActivity.this, ImagesActivity.class);
                        startActivity(l);
                    }
                })
                .show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toHome:
                Intent i = new Intent(this, Home.class);
                startActivity(i);
                break;
            case R.id.toProfile:
                Intent j = new Intent(this, Profile.class);
                startActivity(j);
                break;
        }
    }
}
