package com.example.ca1_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Home extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
            case R.id.toProfile:
                Intent m = new Intent(this, Profile.class);
                startActivity(m);
                break;
        }
    }
}