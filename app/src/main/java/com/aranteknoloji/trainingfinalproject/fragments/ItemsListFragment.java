package com.aranteknoloji.trainingfinalproject.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aranteknoloji.trainingfinalproject.MyViewModel;
import com.aranteknoloji.trainingfinalproject.R;
import com.aranteknoloji.trainingfinalproject.adapters.PlacesListRecyclerViewAdapter;
import com.aranteknoloji.trainingfinalproject.adapters.RealmPlacesAdapter;
import com.aranteknoloji.trainingfinalproject.models.PlacesDataModel;
import com.aranteknoloji.trainingfinalproject.realm.RealmController;

import io.realm.RealmResults;

public class ItemsListFragment extends Fragment {

    private static final String TAG = "ItemsListFragment";

    private RecyclerView recycler;
    private PlacesListRecyclerViewAdapter recyclerViewAdapter;
    private MyViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MyViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items_list, container, false);

        setupRecycler(view);

        viewModel.getData().observe(this, new MyObservableObject());


//        RealmController.get().refresh();
//        setupRecycler(view);
//        setRealmAdapter(RealmController.get().getAllPlaces());
        return view;
    }

    private void setRealmAdapter(RealmResults<PlacesDataModel> data) {
        RealmPlacesAdapter adapter = new RealmPlacesAdapter(data);
        recyclerViewAdapter.setRealmAdapter(adapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    public void setupRecycler(View view) {
        recycler = view.findViewById(R.id.item_list_recycler);
        recycler.setHasFixedSize(true);
//        recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        StaggeredGridLayoutManager lm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recycler.setLayoutManager(lm);
        recyclerViewAdapter = new PlacesListRecyclerViewAdapter(getActivity());
        recycler.setAdapter(recyclerViewAdapter);
    }

    private class MyObservableObject implements Observer<RealmResults<PlacesDataModel>> {
        @Override
        public void onChanged(@Nullable RealmResults<PlacesDataModel> placesDataModels) {
            setRealmAdapter(placesDataModels);
            Log.e(TAG, "onChanged: run");
        }
    }
}
