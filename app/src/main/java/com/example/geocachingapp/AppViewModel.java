package com.example.geocachingapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.geocachingapp.database.AppRepository;
import com.example.geocachingapp.database.QRCode;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class AppViewModel extends AndroidViewModel {
    private AppRepository mRepository;
    private final LiveData<List<QRCode>> mAllCodes;

    public AppViewModel(@NonNull Application application) {
        super(application);
        mRepository = new AppRepository(application);
        mAllCodes = mRepository.getAllCodes();
    }

    public LiveData<List<QRCode>> getAllCodes() { return mAllCodes; }

    public void insert(QRCode code) { mRepository.insert(code); }

    public LiveData<QRCode> getCode(String hash) {
        return mRepository.loadFromId(hash);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }
}
