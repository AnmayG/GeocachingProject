package com.example.geocachingapp.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<LatLng> mLatLng;

    public SearchViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Search fragment");
        mLatLng = new MutableLiveData<>();
        mLatLng.setValue(new LatLng(42.0363, -88.0620));
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<LatLng> getLatLng() {
        return mLatLng;
    }
}