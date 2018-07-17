package com.aranteknoloji.trainingfinalproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    private FloatingActionButton google;
    private FloatingActionButton list;
    private int clickCounter = 0;
    private Realm realm;
    private EditText search;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = RealmController.get().getRealm();

        //lets start network call
//        String key = getString(R.string.google_maps_key);
//        RetroDataService service = RetrofitClintInstance.getRetrofitInstance().create(RetroDataService.class);
//        Call<RetrofitGetModel> call = service.getPlaces("BBVA+compass+loan+in+US", key);
//        call.enqueue(new Callback<RetrofitGetModel>() {
//            @Override
//            public void onResponse(@NonNull Call<RetrofitGetModel> call, @NonNull Response<RetrofitGetModel> response) {
//                addAllItemToRealm(response.body());
//            }
//            @Override
//            public void onFailure(@NonNull Call<RetrofitGetModel> call, @NonNull Throwable t) {
//                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });

        GoogleMapsFragment mapsFragment = new GoogleMapsFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrameLayout, mapsFragment);
        ft.setCustomAnimations(R.anim.fragment_list_in, android.R.anim.fade_out,
                        android.R.anim.fade_in, R.anim.fragment_list_out);
        ft.commit();

        setFloationActionButtonMenu();
        setToolbar();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        search = findViewById(R.id.searchEditText);
        search.setOnEditorActionListener(this);
        toolbar.setOnClickListener(this);
    }

    private void addAllItemToRealm(RetrofitGetModel body) {
        List<RetrofitGetModel.PlacesHolder> list = body.getResults();
        for (int i=0; i<list.size(); i++) {
            PlacesDataModel data = new PlacesDataModel();
            data.setId(RealmController.getInstance().getAllPlaces().size() + 1);
            data.setAddress(list.get(i).getFormatted_address());
            data.setRating(list.get(i).getRating());
            data.setLocation(list.get(i).getGeometry().getLocation().getLatLng().toString());
            data.setTitle(list.get(i).getName());
            data.setIconUrl(list.get(i).getIconUrl());
            realm.beginTransaction();
            realm.copyToRealm(data);
            realm.commitTransaction();
        }
        Toast.makeText(this, "all items are added", Toast.LENGTH_SHORT).show();
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
                ft.add(R.id.mainFrameLayout, fragment);
//                ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft.addToBackStack(null);
                ft.commit();
                setActionsGone();
                break;
            case R.id.mainToolbar:
                search.setVisibility(View.VISIBLE);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
        }
    }

    private boolean checkBackStack() {
        return getSupportFragmentManager().getBackStackEntryCount() > 0;
    }

    private void hideSoftKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        if (search.isShown()) {
            search.setVisibility(View.INVISIBLE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                break;
            case android.R.id.home:
                onBackPressed();
                hideSoftKeyboard();
                break;
        }
        return true;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            search.setVisibility(View.INVISIBLE);
            hideSoftKeyboard();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        return true;
    }
}
