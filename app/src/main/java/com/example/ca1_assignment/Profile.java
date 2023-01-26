package com.example.ca1_assignment;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Profile extends AppCompatActivity implements View.OnClickListener{
    private ImageButton editProfile;
    ImageView profilePic;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String UId = "uId";
    private PasswordDB db ;
    private Integer id;
    private LoginInfo userDetails;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        getSupportActionBar().hide();
        db  = new PasswordDB(this);
        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        id = prefs.getInt(UId,0);
        Log.d(TAG, "\nID is: " + id);
        userDetails = db.getLoginInfo(id);
        //updating profile pic using default hard code(MUST BE REMOVED)
//        db.updateURL(userDetails, "https://firebasestorage.googleapis.com/v0/b/sp-ande.appspot.com/o/images%2F2023_01_26_05_22_25?alt=media&token=dc8e276d-464f-42d1-9a51-caac283ddde9");
//        editProfile = (ImageButton) findViewById(R.id.editProfile);
//
//        editProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(Profile.this, editLocation.class);
//                startActivity(i);
//            }
//        });
        profilePic = findViewById(R.id.profilePic);
        Log.d(TAG,"\nprofile picture url is: " + userDetails.getImageURL());
//        new ImageLoadTask(userDetails.getImageURL(),profilePic);

//        Uri pictureUri = Uri.parse(userDetails.getImageURL());
//        profilePic.setImageURI(pictureUri);
        String value = userDetails.getImageURL();
        Picasso.get().load(value).into(profilePic);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editProfile:
                Intent i = new Intent(this, editLocation.class);
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