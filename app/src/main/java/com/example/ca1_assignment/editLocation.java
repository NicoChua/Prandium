package com.example.ca1_assignment;


import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ca1_assignment.databinding.ActivityEditProfileBinding;
import com.example.ca1_assignment.databinding.ActivityUploadPlacesBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class editLocation extends AppCompatActivity implements View.OnClickListener {
    EditText tName;
    TextView tLoc;
    PasswordDB db ;
    private Button backButton;
    LoginInfo userDetails;
    LoginInfo updatedUser;
    Spinner spinner;
    List<String> options;
    String location;
    SharedPreferences prefs;
    Integer id;
    ImageView profilePic;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String UId = "uId";
    Uri imageUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_edit_profile);
        profilePic = findViewById((R.id.profilePicture));
        db  = new PasswordDB(this);
        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        id = prefs.getInt(UId,0);
        userDetails = db.getLoginInfo(id);
//        db.updateURL(userDetails, "https://firebasestorage.googleapis.com/v0/b/sp-ande.appspot.com/o/images%2F2023_01_26_05_22_25?alt=media&token=dc8e276d-464f-42d1-9a51-caac283ddde9");
        Log.d("Log:",userDetails.getLocation());
        String value = userDetails.getImageURL();
        Picasso.get().load(value).into(profilePic);
        tName = (EditText) findViewById((R.id.updateName));
        tName.setText(userDetails.getName());
        tLoc = (TextView) findViewById((R.id.currentLoc));
        tLoc.setText("Current location: " + userDetails.getLocation().toString());
        spinner = findViewById(R.id.updateLocation);
        options = new ArrayList<>();
        options.add("North");
        options.add("South");
        options.add("East");
        options.add("West");
        spinner.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, options));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location = spinner.getSelectedItem().toString();
//                Toast.makeText(Register.this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        backButton = (Button) findViewById(R.id.updateBackButton);
//
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(editLocation.this, MainActivity.class);
//                startActivity(i);
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updateButton:
                String userName = tName.getText().toString();
                String userLoc = tLoc.getText().toString();


                prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                id = prefs.getInt(UId,0);
                db.editProfile(userDetails, userName, location);
                updatedUser = db.getLoginInfo(id);
                if (updatedUser.getName().equals(userName) && updatedUser.getLocation().equals(location)) {
                    Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();

                    new SweetAlertDialog(editLocation.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Successful")
                            .setContentText("Updated info successfully!")
                            .setConfirmText("OK")
//                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                    Intent l = new Intent(editLocation.this , Home.class);
//                                    startActivity(l);
//                                }
//                            })
                            .show();
                }
                else {
                    new SweetAlertDialog(editLocation.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText("Update Unsuccessful, please try again!")
                            .setConfirmText("OK")
//                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                    Intent l = new Intent(editLocation.this , MainActivity.class);
//                                    startActivity(l);
//                                }
//                            })
                            .show();
                }


                // Reading all contacts
                Log.d("Reading: ", "Reading all contacts..");
                List<LoginInfo> users = db.getAllUsers();

                for (LoginInfo cn : users) {
                    String log = "Id: " + cn.getID() + " ,Name: " + cn.getName() + " ,Location: " +
                            cn.getLocation();
                    // Writing Contacts to log
                    Log.d("Name: ", log);
                }
                break;
            case R.id.updateBackButton:
                Intent i = new Intent(editLocation.this, Profile.class);
                startActivity(i);
                break;
            case R.id.uploadimagebtn:
                selectImage();
                break;
            case R.id.saveImagebtn:
                uploadImage(imageUri);
                break;

        }


    }

    private void uploadImage(Uri uri) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading File....");
        progressDialog.show();
        PasswordDB db = new PasswordDB(this);;
        final String UId = "uId";
        final String MyPREFERENCES = "MyPrefs";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = formatter.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("images/"+fileName);
        databaseReference = FirebaseDatabase.getInstance().getReference("");
        final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                //setting new imageURL for profile picture
                                Upload upload = new Upload(uri.toString());
                                String imageURL = upload.getImageUrl();
                                LoginInfo userDetails = db.getLoginInfo(id);
                                db.updateURL(userDetails, imageURL);
                                Log.d(TAG, "\nImage URL is: " + imageURL);
                                Toast.makeText(editLocation.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                if (progressDialog.isShowing())
                                    progressDialog.dismiss();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(editLocation.this,"Failed to Upload",Toast.LENGTH_SHORT).show();


                    }
                });
    }

    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }


    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null){

            imageUri = data.getData();
            profilePic.setImageURI(imageUri);
        }
    }
}
