package com.example.geocachingapp.ui.qrcode;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class QRCodeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public QRCodeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is QR Code fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}