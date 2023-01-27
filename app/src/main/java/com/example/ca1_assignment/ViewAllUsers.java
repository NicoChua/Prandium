package com.example.ca1_assignment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import java.util.List;

public class ViewAllUsers extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PasswordDB db = new PasswordDB(this);

//        // Inserting Contacts
//        Log.d("Insert Data : ", "Inserting ..");
//        db.addContact(new LoginInfo("Mary Jane", "86552288"));
//        db.addContact(new Contact("Nina Kim", "91198800"));
//        db.addContact(new Contact("Tommy Tan", "92681122"));
//        db.addContact(new Contact("Koji Lim", "95334321"));

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<LoginInfo> LoginInfo = db.getAllUsers();

        for (LoginInfo cn : LoginInfo) {
            String log = "Id: " + cn.getID() + " ,Name: " + cn.getName() + " ,Favourites: "
                    + cn.getFavourites();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }
    }

}
