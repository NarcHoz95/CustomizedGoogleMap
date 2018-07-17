package com.aranteknoloji.trainingfinalproject.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aranteknoloji.trainingfinalproject.DetailsActivity;
import com.aranteknoloji.trainingfinalproject.R;
import com.aranteknoloji.trainingfinalproject.models.PlacesDataModel;
import com.aranteknoloji.trainingfinalproject.realm.RealmController;
import com.squareup.picasso.Picasso;

import io.realm.Realm;

public class PlacesListRecyclerViewAdapter extends RealmRecyclerViewAdapter<PlacesDataModel> {

    //to set click or that kind of stuff
//    private Realm realm;
    private Activity context;

    public PlacesListRecyclerViewAdapter(Activity context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.places_list_recycler_item_layout, parent, false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        realm = RealmController.getInstance().getRealm();

        final PlacesDataModel model = getItem(position);
        final ListItemViewHolder viewHolder = (ListItemViewHolder) holder;

        Picasso.get()
                .load(model.getIconUrl())
                .placeholder(R.drawable.ic_camera_alt_black_24dp)
                .into(viewHolder.icon);
        viewHolder.title.setText(model.getTitle());
        viewHolder.location.setText(model.getLocation());
        viewHolder.address.setText(model.getAddress());
        viewHolder.rating.setText(String.valueOf(model.getRating()));
    }

    @Override
    public int getItemCount() {
        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView icon;
        private TextView title, location, address, rating;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            icon = itemView.findViewById(R.id.iconView);
            title = itemView.findViewById(R.id.titleView);
            location = itemView.findViewById(R.id.locationView);
            address = itemView.findViewById(R.id.addressView);
            rating = itemView.findViewById(R.id.ratingView);
        }

        @Override
        public void onClick(View v) {
            final PlacesDataModel model = getItem(getAdapterPosition());
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("icon", model.getIconUrl());
            intent.putExtra("location", model.getLocation());
            intent.putExtra("address", model.getAddress());
            intent.putExtra("title", model.getTitle());
            intent.putExtra("rating", model.getRating());
            context.startActivity(intent);
            context.overridePendingTransition(R.anim.activity_slide_right_in, android.R.anim.fade_out);
        }
    }
}
