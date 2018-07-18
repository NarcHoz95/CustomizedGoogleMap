package com.aranteknoloji.trainingfinalproject;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.aranteknoloji.trainingfinalproject.models.PlacesDataModel;
import com.aranteknoloji.trainingfinalproject.models.QueryHelper;
import com.aranteknoloji.trainingfinalproject.models.RetrofitGetModel;
import com.aranteknoloji.trainingfinalproject.realm.RealmController;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyViewModel extends AndroidViewModel {

    private static final String TAG = "MyViewModel";
    private MutableLiveData<RealmResults<PlacesDataModel>> data;

    public MyViewModel(@NonNull Application application) {
        super(application);
        Log.e(TAG, "MyViewModel: contructor is running");
        data = new MutableLiveData<>();
    }

    public void setData(String query) {
        if (TextUtils.isEmpty(query)) return;
        Log.d(TAG, "setData: query = " + query);
        RealmController.get().refresh();
        RealmResults<PlacesDataModel> listOfItems = RealmController.get().queryedBooks(query);
        if (listOfItems.size() != 0) data.postValue(listOfItems);
        else makeNetworkCall();
    }

    public MutableLiveData<RealmResults<PlacesDataModel>> getData() {
        Log.e(TAG, "MyViewModel: getData: is running");
        return data;
    }

    private void makeNetworkCall() {
        Log.e(TAG, "setData: There is no result");
//        lets start network call
        String key = getApplication().getString(R.string.google_maps_key);
        RetroDataService service = RetrofitClintInstance.getRetrofitInstance().create(RetroDataService.class);
        Call<RetrofitGetModel> call = service.getPlaces(QueryHelper.getQueryWithPlus(), key);
        call.enqueue(new Callback<RetrofitGetModel>() {
            @Override
            public void onResponse(@NonNull Call<RetrofitGetModel> call, @NonNull Response<RetrofitGetModel> response) {
                addAllItemToRealm(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<RetrofitGetModel> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), "Something went wrong...Please try later!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addAllItemToRealm(RetrofitGetModel body) {
        Realm realm = RealmController.get().getRealm();
        List<RetrofitGetModel.PlacesHolder> list = body.getResults();
        if (list.size() == 0) {
            Toast.makeText(getApplication(), "There is no result for that query",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i=0; i<list.size(); i++) {
            PlacesDataModel data = new PlacesDataModel();
            data.setId(RealmController.getInstance().getAllPlaces().size() + 1);
            data.setAddress(list.get(i).getFormatted_address());
            data.setRating(list.get(i).getRating());
            data.setLocation(list.get(i).getGeometry().getLocation().getLatLng().toString());
            data.setTitle(list.get(i).getName());
            data.setIconUrl(list.get(i).getIconUrl());
            realm.beginTransaction();
            realm.copyToRealm(data);
            realm.commitTransaction();
        }
        Log.d(TAG, "addAllItemToRealm: all items were added to realm");
        RealmController.get().refresh();
        RealmResults<PlacesDataModel> listOfItems = RealmController.get().queryedBooks(QueryHelper.getQuery());
        if (listOfItems.size() != 0) data.postValue(listOfItems);
    }
}
