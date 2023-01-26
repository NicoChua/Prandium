//package com.example.ca1_assignment;
//
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.ProgressDialog;
//import android.content.ContentResolver;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.webkit.MimeTypeMap;
//import android.widget.EditText;
//import android.widget.Toast;
//import com.example.ca1_assignment.databinding.ActivityUploadPlacesBinding;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//public class uploadImage extends AppCompatActivity {
//    private EditText mEditTextFileName;
//    ActivityUploadPlacesBinding binding;
//    Uri imageUri;
//    StorageReference storageReference;
//    DatabaseReference databaseReference;
//    ProgressDialog progressDialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityUploadPlacesBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        mEditTextFileName = findViewById(R.id.edit_text_file_name);
//        binding.selectImagebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                selectImage();
//
//
//            }
//        });
//
//        binding.uploadimagebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                uploadImage(imageUri);
//
//            }
//        });
//
//    }
//
//    private void uploadImage(Uri uri) {
//
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Uploading File....");
//        progressDialog.show();
//        PasswordDB db = new PasswordDB(this);;
//        final String UId = "uId";
//        final String MyPREFERENCES = "MyPrefs";
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
//        Date now = new Date();
//        String fileName = formatter.format(now);
//        storageReference = FirebaseStorage.getInstance().getReference("images/"+fileName);
//        databaseReference = FirebaseDatabase.getInstance().getReference("");
//        final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
//        storageReference.putFile(imageUri)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//
//                                //setting new imageURL for profile picture
//                                Upload upload = new Upload(mEditTextFileName.getText().toString().trim(), uri.toString());
//                                String imageURL = upload.getImageUrl();
//                                SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
//                                Integer id = prefs.getInt(UId,0);
//                                LoginInfo userDetails = db.getLoginInfo(id);
//                                db.updateURL(userDetails, imageURL);
////                                String modelId = databaseReference.push().getKey();
////                                databaseReference.child(modelId).setValue(upload);
//                                Toast.makeText(uploadImage.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
//                                if (progressDialog.isShowing())
//                                    progressDialog.dismiss();
//                            }
//                        });
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//
//                        if (progressDialog.isShowing())
//                            progressDialog.dismiss();
//                        Toast.makeText(uploadImage.this,"Failed to Upload",Toast.LENGTH_SHORT).show();
//
//
//                    }
//                });
////                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
////                    @Override
////                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////
////                        binding.firebaseimage.setImageURI(null);
////                        Toast.makeText(uploadImage.this,"Successfully Uploaded",Toast.LENGTH_SHORT).show();
////                        Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),
////                                taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
////                        String uploadId = databaseReference.push().getKey();
////                        databaseReference.child(uploadId).setValue(upload);
////                        if (progressDialog.isShowing())
////                            progressDialog.dismiss();
////
////                    }
////                }).addOnFailureListener(new OnFailureListener() {
////                    @Override
////                    public void onFailure(@NonNull Exception e) {
////
////
////                        if (progressDialog.isShowing())
////                            progressDialog.dismiss();
////                        Toast.makeText(uploadImage.this,"Failed to Upload",Toast.LENGTH_SHORT).show();
////
////
////                    }
////                });
//
//    }
//
//    private String getFileExtension(Uri mUri){
//
//        ContentResolver cr = getContentResolver();
//        MimeTypeMap mime = MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(cr.getType(mUri));
//
//    }
//
//
//    private void selectImage() {
//
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent,100);
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 100 && data != null && data.getData() != null){
//
//            imageUri = data.getData();
//            binding.firebaseimage.setImageURI(imageUri);
//
//
//        }
//    }
//}