package com.example.geocachingapp.ui.qrcode;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class QRCodeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Bitmap> mQrCode;

    public QRCodeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is QR Code fragment");
        mQrCode = new MutableLiveData<>();
        mQrCode.setValue(null);
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<Bitmap> getQrCode() {
        return mQrCode;
    }

    public void setQrCode(Bitmap value) {
        mQrCode.setValue(value);
    }
}