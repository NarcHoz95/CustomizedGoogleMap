package com.aranteknoloji.trainingfinalproject.adapters;

import android.support.annotation.Nullable;

import com.aranteknoloji.trainingfinalproject.models.PlacesDataModel;

import io.realm.OrderedRealmCollection;

/**
 * This class adds the places to the Realm Database
 * */
public class RealmPlacesAdapter extends RealmModelAdapter<PlacesDataModel> {

    public RealmPlacesAdapter(@Nullable OrderedRealmCollection<PlacesDataModel> data) {
        super(data);
    }
}
