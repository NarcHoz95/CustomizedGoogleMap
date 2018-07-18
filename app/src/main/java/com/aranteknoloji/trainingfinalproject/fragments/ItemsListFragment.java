package com.aranteknoloji.trainingfinalproject.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.aranteknoloji.trainingfinalproject.MyViewModel;
import com.aranteknoloji.trainingfinalproject.R;
import com.aranteknoloji.trainingfinalproject.adapters.PlacesListRecyclerViewAdapter;
import com.aranteknoloji.trainingfinalproject.adapters.RealmPlacesAdapter;
import com.aranteknoloji.trainingfinalproject.models.PlacesDataModel;
import com.aranteknoloji.trainingfinalproject.models.QueryHelper;
import com.aranteknoloji.trainingfinalproject.realm.RealmController;

import io.realm.RealmResults;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ItemsListFragment extends Fragment implements TextView.OnEditorActionListener,
        View.OnClickListener {

    private static final String TAG = "ItemsListFragment";

    private EditText search;
    private RecyclerView recycler;
    private PlacesListRecyclerViewAdapter recyclerViewAdapter;
    private MyViewModel viewModel;

    private AppCompatActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        activity = (AppCompatActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!TextUtils.isEmpty(QueryHelper.getQuery())) viewModel.setData(QueryHelper.getQuery());
        View view = inflater.inflate(R.layout.fragment_items_list, container, false);
        setupRecycler(view);
        viewModel.getData().observe(this, new MyObservableObject());
        setToolbar(view);
        setHasOptionsMenu(true);
        return view;
    }

    private void setToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.mainToolbar);
        activity.setSupportActionBar(toolbar);
        search = view.findViewById(R.id.searchEditText);
        search.setOnEditorActionListener(this);
        toolbar.setOnClickListener(this);
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

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            QueryHelper.setQuery(search.getText().toString());
            viewModel.setData(QueryHelper.getQuery());
            search.setVisibility(View.INVISIBLE);
            hideSoftKeyboard();
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainToolbar:
                search.setVisibility(View.VISIBLE);
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                break;
            case android.R.id.home:
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                hideSoftKeyboard();
                search.setVisibility(View.INVISIBLE);
                break;
        }
        return true;
    }

    private class MyObservableObject implements Observer<RealmResults<PlacesDataModel>> {
        @Override
        public void onChanged(@Nullable RealmResults<PlacesDataModel> placesDataModels) {
            setRealmAdapter(placesDataModels);
            Log.e(TAG, "onChanged: run");
        }
    }

    private void hideSoftKeyboard() {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
