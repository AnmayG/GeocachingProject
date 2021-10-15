package com.example.geocachingapp.ui.search;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<LatLng> mLatLng;
    private MutableLiveData<Bitmap> mQrCode;
    private MutableLiveData<String> mReadData;

    public SearchViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Search fragment");

        mLatLng = new MutableLiveData<>();
        mLatLng.setValue(new LatLng(42.0363, -88.0620));

        mQrCode = new MutableLiveData<>();
        mQrCode.setValue(null);

        mReadData = new MutableLiveData<>();
        mReadData.setValue(null);
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<LatLng> getLatLng() {
        return mLatLng;
    }
    public LiveData<Bitmap> getQrCode() {
        return mQrCode;
    }
    public LiveData<String> getReadData() {
        return mReadData;
    }

    public void setLatLng(LatLng v) {
        mLatLng.setValue(v);
    }
    public void setQrCode(Bitmap value) {
        mQrCode.setValue(value);
    }
    public void setReadData(String value) {
        mReadData.setValue(value);
    }
}