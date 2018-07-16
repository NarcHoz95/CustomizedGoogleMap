package com.aranteknoloji.trainingfinalproject;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.aranteknoloji.trainingfinalproject.models.PlacesDataModel;
import com.aranteknoloji.trainingfinalproject.realm.RealmController;

import io.realm.RealmResults;

public class MyViewModel extends AndroidViewModel {

    private static final String TAG = "MyViewModel";
    private MutableLiveData<RealmResults<PlacesDataModel>> data;

    public MyViewModel(@NonNull Application application) {
        super(application);
        Log.e(TAG, "MyViewModel: contructor is running");
        data = new MutableLiveData<>();
        RealmController.get().refresh();
        data.postValue(RealmController.get().getAllPlaces());
    }

    public MutableLiveData<RealmResults<PlacesDataModel>> getData() {
        Log.e(TAG, "MyViewModel: getData: is running");
        return data;
    }
}
