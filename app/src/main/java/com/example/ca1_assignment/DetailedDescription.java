package com.example.ca1_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailedDescription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_description);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        String name = intent.getStringExtra("locationName");
        String image = intent.getStringExtra("locationImage");
        String description = intent.getStringExtra("locationDesc");
        TextView name2 = (TextView) findViewById(R.id.name2);
        name2.setText(name);
        ImageView img = (ImageView) findViewById(R.id.image);
        new ImageLoadTask(image, img).execute();
        TextView desc = (TextView) findViewById(R.id.d);
        desc.append("\n" + description);

    }
}