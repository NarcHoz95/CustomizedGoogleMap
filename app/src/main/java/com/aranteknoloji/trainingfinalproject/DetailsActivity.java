package com.aranteknoloji.trainingfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (getIntent() != null) {
            setContentViews();
        }
    }

    private void setContentViews() {
        ImageView imageView = findViewById(R.id.detailIconView);
        TextView title, location, address, rating;
        title = findViewById(R.id.detailTitleView);
        location = findViewById(R.id.detailLocView);
        address = findViewById(R.id.detailaddressView);
        rating = findViewById(R.id.detailsRatingView);

        Intent intent = getIntent();
        title.setText(intent.getStringExtra("title"));
        location.setText(intent.getStringExtra("location"));
        address.setText(intent.getStringExtra("address"));
        rating.setText(String.valueOf(intent.getFloatExtra("rating", 0.0f)));
        Picasso.get()
                .load(intent.getStringExtra("icon"))
                .placeholder(R.drawable.ic_camera_alt_black_24dp)
                .into(imageView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, R.anim.activity_slide_right_out);
    }
}