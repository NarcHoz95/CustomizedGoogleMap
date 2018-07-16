package com.aranteknoloji.trainingfinalproject.models;

import com.google.android.gms.maps.model.LatLng;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PlacesDataModel extends RealmObject {

    @PrimaryKey
    private long id;
    private String title;
    private String iconUrl;
    private String location;
    private String address;
    private float rating;

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getLocation() {
        return location;
    }

    public String getAddress() {
        return address;
    }

    public float getRating() {
        return rating;
    }
}
