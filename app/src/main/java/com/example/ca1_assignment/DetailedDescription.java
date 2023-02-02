package com.example.ca1_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

public class DetailedDescription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_description);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        String name = intent.getStringExtra("locationName");
        Log.d("intent","Intent1: " + name);
        String image = intent.getStringExtra("locationImage");
        Log.d("intent","Intent2: " + image);
        String description = intent.getStringExtra("locationDesc");
        String link = intent.getStringExtra("locationLink");
        TextView name2 = (TextView) findViewById(R.id.name2);
        name2.setText(name);
        ImageView img = (ImageView) findViewById(R.id.image);
        new ImageLoadTask(image, img).execute();
        TextView desc = (TextView) findViewById(R.id.d);
        desc.append("\n\n" + description);
        desc.setMovementMethod(new ScrollingMovementMethod());

        TextView updateLink = (TextView) findViewById(R.id.link);
        updateLink.append(link);
//        updateLink.setMovementMethod(LinkMovementMethod.getInstance());
        updateLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUrl(link);
            }
        });
    }

    private void goToUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
}