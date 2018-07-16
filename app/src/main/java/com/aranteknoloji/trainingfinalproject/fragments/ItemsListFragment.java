package com.aranteknoloji.trainingfinalproject.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aranteknoloji.trainingfinalproject.R;
import com.aranteknoloji.trainingfinalproject.adapters.PlacesListRecyclerViewAdapter;
import com.aranteknoloji.trainingfinalproject.adapters.RealmPlacesAdapter;
import com.aranteknoloji.trainingfinalproject.models.PlacesDataModel;
import com.aranteknoloji.trainingfinalproject.realm.RealmController;

import io.realm.RealmResults;

public class ItemsListFragment extends Fragment {

    private RecyclerView recycler;
    private PlacesListRecyclerViewAdapter recyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items_list, container, false);
        RealmController.get().refresh();
        setupRecycler(view);
        setRealmAdapter(RealmController.get().getAllPlaces());
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
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewAdapter = new PlacesListRecyclerViewAdapter();
        recycler.setAdapter(recyclerViewAdapter);
    }
}
