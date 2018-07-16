package com.aranteknoloji.trainingfinalproject.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyClusteringItem implements ClusterItem {

    private LatLng location;
    private String title, snippet, iconUrl;

    public MyClusteringItem(LatLng location, String title, String snippet, String iconUrl) {
        this.location = location;
        this.title = title;
        this.snippet = snippet;
        this.iconUrl = iconUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    @Override
    public LatLng getPosition() {
        return location;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }
}
