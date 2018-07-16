package com.aranteknoloji.trainingfinalproject.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aranteknoloji.trainingfinalproject.R;
import com.aranteknoloji.trainingfinalproject.models.PlacesDataModel;
import com.aranteknoloji.trainingfinalproject.realm.RealmController;
import com.squareup.picasso.Picasso;

import io.realm.Realm;

public class PlacesListRecyclerViewAdapter extends RealmRecyclerViewAdapter<PlacesDataModel> {

    //to set click or that kind of stuff
//    private Realm realm;

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

        Picasso.get().load(model.getIconUrl()).into(viewHolder.icon);
        viewHolder.title.setText(model.getTitle());
        viewHolder.location.setText(model.getLocation());
    }

    @Override
    public int getItemCount() {
        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView title, location;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.iconView);
            title = itemView.findViewById(R.id.titleView);
            location = itemView.findViewById(R.id.locationView);
        }
    }
}
