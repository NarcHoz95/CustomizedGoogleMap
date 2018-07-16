package com.aranteknoloji.trainingfinalproject.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aranteknoloji.trainingfinalproject.MyViewModel;
import com.aranteknoloji.trainingfinalproject.R;
import com.aranteknoloji.trainingfinalproject.adapters.PlacesCircleRecyclerViewAdapter;
import com.aranteknoloji.trainingfinalproject.adapters.PlacesListRecyclerViewAdapter;
import com.aranteknoloji.trainingfinalproject.adapters.RealmPlacesAdapter;
import com.aranteknoloji.trainingfinalproject.models.MyClusteringItem;
import com.aranteknoloji.trainingfinalproject.models.PlacesDataModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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

public class GoogleMapsFragment extends Fragment implements OnMapReadyCallback,
        ClusterManager.OnClusterClickListener<MyClusteringItem> {

    private RecyclerView recyclerView;
    private PlacesCircleRecyclerViewAdapter recyclerViewAdapter;
    private GoogleMap mMap;
    private MapView mapView;
    private ClusterManager<MyClusteringItem> clusterManager;
    private MyViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MyViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

//        viewModel = ViewModelProviders.of(this).get(MyViewModel.class);
//        viewModel.getData().observe(this, new MyObserverObject());
        return view;
    }

    private void setRealmAdapter(RealmResults<PlacesDataModel> data) {
        RealmPlacesAdapter adapter = new RealmPlacesAdapter(data);
        recyclerViewAdapter.setRealmAdapter(adapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    public void setupRecycler(View view) {
        recyclerView = view.findViewById(R.id.circle_recycler);
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

        public MyIconRenderer() {
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
            Glide.with(GoogleMapsFragment.this).load(item.getIconUrl()).thumbnail(0.1f).into(imageView);
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
    }

    private LatLng convertStringToLatLng(String s) {
        String[] part = s.substring(10, s.length()-1).split(",");
        return new LatLng(Double.parseDouble(part[0]), Double.parseDouble(part[1]));
    }
}
