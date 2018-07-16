package com.aranteknoloji.trainingfinalproject.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetrofitGetModel {

    @SerializedName("next_page_token")
    private String token_next;

    //    @SerializedName("result")
    private List<PlacesHolder> results;

    public String status;

    public List<PlacesHolder> getResults() {
        return results;
    }

    public class PlacesHolder {

        //        @SerializedName("formatted_address")
        private String formatted_address;

        @SerializedName("icon")
        private String iconUrl;

        private String name;

        private Geometry geometry;

        private float rating;

        public float getRating() {
            return rating;
        }

        public String getStringRating() {
            return String.valueOf(rating);
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {

            this.geometry = geometry;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFormatted_address() {

            return formatted_address;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public String getName() {
            return name;
        }
    }

    public class Geometry {

        private Location location;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }

    public class Location {

        private double lat;
        private double lng;

        public Location(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public String getStringLat() {
            return String.valueOf(lat);
        }

        public String getStringLng() {
            return String.valueOf(lng);
        }

        public double getLng() {
            return lng;
        }

        public LatLng getLatLng() {
            return new LatLng(lat, lng);
        }
    }
}
