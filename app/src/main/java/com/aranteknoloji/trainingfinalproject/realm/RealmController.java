package com.aranteknoloji.trainingfinalproject.realm;

import com.aranteknoloji.trainingfinalproject.models.PlacesDataModel;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController() {
        this.realm = Realm.getDefaultInstance();
    }

    public static RealmController get() {
        if (instance == null) {
            instance = new RealmController();
        }
        return instance;
    }

    public static RealmController getInstance() {
        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

    //to refresh realm
    public void refresh() {
        realm.refresh();
    }

    //clear all objects from Book.class
    public void clearAll() {
        realm.beginTransaction();
        realm.delete(PlacesDataModel.class);
        realm.commitTransaction();
    }

    //find all objects in the Book.class
    public RealmResults<PlacesDataModel> getAllPlaces() {
        return realm.where(PlacesDataModel.class).findAll();
    }

    //query a single item with the given id
    public PlacesDataModel getPlaceWithId(String id) {
        return realm.where(PlacesDataModel.class).equalTo("id", id).findFirst();
    }

    //check if PlacesDataModel is empty
    public boolean hasPlaces() {
        return !realm.where(PlacesDataModel.class).findAll().isLoaded();
    }

    //query example
    public RealmResults<PlacesDataModel> queryedBooks(String keyword) {
        return realm.where(PlacesDataModel.class)
                .contains("address", keyword)
                .or()
                .contains("title", keyword)
                .findAll();

    }
}
