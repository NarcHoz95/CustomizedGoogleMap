package com.aranteknoloji.trainingfinalproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aranteknoloji.trainingfinalproject.fragments.GoogleMapsFragment;
import com.aranteknoloji.trainingfinalproject.fragments.ItemsListFragment;
import com.aranteknoloji.trainingfinalproject.models.PlacesDataModel;
import com.aranteknoloji.trainingfinalproject.models.RetrofitGetModel;
import com.aranteknoloji.trainingfinalproject.realm.RealmController;

import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton google;
    private FloatingActionButton list;
    private int clickCounter = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleMapsFragment mapsFragment = new GoogleMapsFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrameLayout, mapsFragment);
        ft.setCustomAnimations(R.anim.fragment_list_in, android.R.anim.fade_out,
                        android.R.anim.fade_in, R.anim.fragment_list_out);
        ft.commit();

        setFloationActionButtonMenu();
    }

    private void setFloationActionButtonMenu() {
        FloatingActionButton main = findViewById(R.id.mainActionButton);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainActionButton:
                if (clickCounter % 2 == 0) setActionsVisible();
                else setActionsGone();
                clickCounter++;
                break;
            case R.id.googleMapAction:
                if (checkBackStack()) {
                    getSupportFragmentManager().popBackStack();
                }
                setActionsGone();
                break;
            case R.id.listActionButton:
                if (checkBackStack()) break;
                ItemsListFragment fragment = new ItemsListFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_list_in, android.R.anim.fade_out,
                        android.R.anim.fade_in, R.anim.fragment_list_out);
                ft.replace(R.id.mainFrameLayout, fragment);
//                ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft.addToBackStack(null);
                ft.commit();
                setActionsGone();
                break;
        }
    }

    private boolean checkBackStack() {
        return getSupportFragmentManager().getBackStackEntryCount() > 0;
    }
}