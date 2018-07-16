package com.aranteknoloji.trainingfinalproject.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aranteknoloji.trainingfinalproject.R;
import com.aranteknoloji.trainingfinalproject.models.PlacesDataModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class PlacesCircleRecyclerViewAdapter extends RealmRecyclerViewAdapter<PlacesDataModel> {

    private GoogleMap mMap;

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.circle_recycler_item, parent, false);
        return new MyCirclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final PlacesDataModel model = getItem(position);
        final MyCirclerViewHolder viewHolder = (MyCirclerViewHolder) holder;

        viewHolder.title.setText(model.getTitle());
        viewHolder.rating.setText(String.valueOf(model.getRating()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        convertStringToLatLng(model.getLocation()), 15));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    public class MyCirclerViewHolder extends RecyclerView.ViewHolder {

        TextView title, rating;
        View itemView;

        public MyCirclerViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            title = itemView.findViewById(R.id.circle_title);
            rating = itemView.findViewById(R.id.circle_rating);
        }
    }

    private LatLng convertStringToLatLng(String s) {
        String[] part = s.substring(10, s.length()-1).split(",");
        return new LatLng(Double.parseDouble(part[0]), Double.parseDouble(part[1]));
    }
}
