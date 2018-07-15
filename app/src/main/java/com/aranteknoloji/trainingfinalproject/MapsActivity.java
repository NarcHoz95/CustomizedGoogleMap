package com.aranteknoloji.trainingfinalproject;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    //FloatingActionMenu's action button's initiliaze
    private FloatingActionButton main, google, list;
    private int clickCounter = 0;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setFloationActionButtonMenu();
    }

    private void setFloationActionButtonMenu() {
        main = findViewById(R.id.mainActionButton);
        google = findViewById(R.id.googleMapAction);
        list = findViewById(R.id.listActionButton);
        main.setOnClickListener(this);
        google.setOnClickListener(this);
        list.setOnClickListener(this);
    }

    private void setActionsVisible() {
        Animation animGoogleIn = AnimationUtils.loadAnimation(this, R.anim.google_action_in);
        Animation animListIn = AnimationUtils.loadAnimation(this, R.anim.list_action_in);
        google.setAnimation(animGoogleIn);
        list.setAnimation(animListIn);
        google.setVisibility(View.VISIBLE);
        list.setVisibility(View.VISIBLE);
    }

    private void setActionsGone() {
        Animation animGoogleOut = AnimationUtils.loadAnimation(this, R.anim.google_action_out);
        Animation animListOut = AnimationUtils.loadAnimation(this, R.anim.list_action_out);
        google.setAnimation(animGoogleOut);
        list.setAnimation(animListOut);
        google.setVisibility(View.GONE);
        list.setVisibility(View.GONE);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainActionButton:
                if (clickCounter % 2 == 0) setActionsVisible();
                else setActionsGone();
                clickCounter++;
                break;
            case R.id.googleMapAction:
                break;
            case R.id.listActionButton:
                break;
        }
    }
}
