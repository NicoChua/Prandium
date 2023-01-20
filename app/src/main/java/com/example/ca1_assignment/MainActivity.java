package com.example.ca1_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        PasswordDB db = new PasswordDB(this);
//
//        // Inserting Contacts
//        Log.d("Insert Data : ", "Inserting ..");
//        db.addContact(new LoginInfo("Mary", "1"));
//        db.addContact(new LoginInfo("Nina", "2"));
//        db.addContact(new LoginInfo("Tommy", "3"));
//        db.addContact(new LoginInfo("Koji", "4"));
//
//        // Reading all contacts
//        Log.d("Reading: ", "Reading all contacts..");
//        List<LoginInfo> contacts = db.getAllContacts();
//
//        for (LoginInfo cn : contacts) {
//            String log = "Id: " + cn.getID() + " ,Name: " + cn.getName() + " ,Phone: " +
//                    cn.getPassword();
//            // Writing Contacts to log
//            Log.d("Name: ", log);
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

        }
    }

}