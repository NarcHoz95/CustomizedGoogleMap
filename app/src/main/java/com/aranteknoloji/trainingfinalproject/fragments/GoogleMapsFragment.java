package com.aranteknoloji.trainingfinalproject.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aranteknoloji.trainingfinalproject.DetailsActivity;
import com.aranteknoloji.trainingfinalproject.MyViewModel;
import com.aranteknoloji.trainingfinalproject.R;
import com.aranteknoloji.trainingfinalproject.adapters.PlacesCircleRecyclerViewAdapter;
import com.aranteknoloji.trainingfinalproject.adapters.RealmPlacesAdapter;
import com.aranteknoloji.trainingfinalproject.models.MyClusteringItem;
import com.aranteknoloji.trainingfinalproject.models.PlacesDataModel;
import com.aranteknoloji.trainingfinalproject.models.QueryHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.RealmResults;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class GoogleMapsFragment extends Fragment implements OnMapReadyCallback,
        ClusterManager.OnClusterClickListener<MyClusteringItem>, GoogleMap.OnInfoWindowClickListener,
        TextView.OnEditorActionListener, View.OnClickListener {

    private EditText search;
    private PlacesCircleRecyclerViewAdapter recyclerViewAdapter;
    private GoogleMap mMap;
    private MapView mapView;
    private ClusterManager<MyClusteringItem> clusterManager;
    private MyViewModel viewModel;

    private AppCompatActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        activity = (AppCompatActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!TextUtils.isEmpty(QueryHelper.getQuery())) viewModel.setData(QueryHelper.getQuery());
        View view = inflater.inflate(R.layout.fragment_googlemaps, container, false);
        mapView = view.findViewById(R.id.fragmentMap);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(Objects.requireNonNull(getActivity()).getApplicationContext());
        } catch (Exception e){
            e.printStackTrace();
        }
        mapView.getMapAsync(this);

        setupRecycler(view);
        setToolbar(view);
        setHasOptionsMenu(true);
        return view;
    }

    private void setToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.mainToolbar);
        activity.setSupportActionBar(toolbar);
        search = view.findViewById(R.id.searchEditText);
        search.setOnEditorActionListener(this);
        toolbar.setOnClickListener(this);
    }

    private void setRealmAdapter(RealmResults<PlacesDataModel> data) {
        RealmPlacesAdapter adapter = new RealmPlacesAdapter(data);
        recyclerViewAdapter.setRealmAdapter(adapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    public void setupRecycler(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.circle_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(lm);
        recyclerViewAdapter = new PlacesCircleRecyclerViewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        recyclerViewAdapter.setmMap(mMap);

        clusterManager = new ClusterManager<>(getContext(), mMap);
        clusterManager.setOnClusterClickListener(this);
        clusterManager.setRenderer(new MyIconRenderer());

        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);
        mMap.setOnInfoWindowClickListener(this);
        mMap.getUiSettings().setCompassEnabled(false);
//        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        viewModel.getData().observe(this, new MyObserverObject());
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public boolean onClusterClick(Cluster<MyClusteringItem> cluster) {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        final LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("icon", "https://maps.gstatic.com/mapfiles/place_api/icons/atm-71.png");
        intent.putExtra("location", marker.getPosition().toString());
        intent.putExtra("address", marker.getSnippet());
        intent.putExtra("title", marker.getTitle());
        intent.putExtra("rating", "0.0");
        getContext().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_slide_right_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            QueryHelper.setQuery(search.getText().toString());
            viewModel.setData(QueryHelper.getQuery());
            search.setVisibility(View.INVISIBLE);
            hideSoftKeyboard();
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainToolbar:
                search.setVisibility(View.VISIBLE);
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                break;
            case android.R.id.home:
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                hideSoftKeyboard();
                search.setVisibility(View.INVISIBLE);
                break;
        }
        return true;
    }

    private class MyObserverObject implements Observer<RealmResults<PlacesDataModel>> {
        @Override
        public void onChanged(@Nullable RealmResults<PlacesDataModel> placesDataModels) {
            addItemsToClusterManager(placesDataModels);
            setRealmAdapter(placesDataModels);
        }
    }

    private class MyIconRenderer extends DefaultClusterRenderer<MyClusteringItem> {

        private IconGenerator iconGenerator = new IconGenerator(getContext());
        private ImageView imageView;
        private final int dimens;

        MyIconRenderer() {
            super(GoogleMapsFragment.this.getContext(), mMap, clusterManager);

            dimens = (int) getResources().getDimension(R.dimen.imageViewDimes);
            int padding = (int) getResources().getDimension(R.dimen.imageViewPadd);

            imageView = new ImageView(getContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(dimens, dimens));
            imageView.setPadding(padding, padding, padding, padding);
            iconGenerator.setContentView(imageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(MyClusteringItem item, MarkerOptions markerOptions) {
            //draw the pin
            Glide.with(GoogleMapsFragment.this)
                    .load(item.getIconUrl())
                    .thumbnail(0.1f)
                    .apply(new RequestOptions().placeholder(R.drawable.ic_location_on_black_24dp))
                    .into(imageView);
            Bitmap icon = iconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }
    }

    private void addItemsToClusterManager(RealmResults<PlacesDataModel> placesDataModels) {
        clusterManager.clearItems();
        List<MyClusteringItem> items = new ArrayList<>();
        RealmPlacesAdapter adapter = new RealmPlacesAdapter(placesDataModels);
        for (int i=0; i<adapter.getCount(); i++) {
            items.add(new MyClusteringItem(convertStringToLatLng(adapter.getItem(i).getLocation()),
                    adapter.getItem(i).getTitle(), adapter.getItem(i).getAddress(), adapter.getItem(i).getIconUrl()));
        }
        clusterManager.addItems(items);
        clusterManager.cluster();
    }

    private LatLng convertStringToLatLng(String s) {
        String[] part = s.substring(10, s.length()-1).split(",");
        return new LatLng(Double.parseDouble(part[0]), Double.parseDouble(part[1]));
    }

    private void hideSoftKeyboard() {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
