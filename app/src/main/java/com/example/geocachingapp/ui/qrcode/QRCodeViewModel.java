package com.example.geocachingapp.ui.qrcode;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class QRCodeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Bitmap> mQrCode;
    private MutableLiveData<String> mReadData;

    public QRCodeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is QR Code fragment");

        mQrCode = new MutableLiveData<>();
        mQrCode.setValue(null);

        mReadData = new MutableLiveData<>();
        mReadData.setValue(null);
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<Bitmap> getQrCode() {
        return mQrCode;
    }
    public LiveData<String> getReadData() {
        return mReadData;
    }

    public void setQrCode(Bitmap value) {
        mQrCode.setValue(value);
    }

    public void setReadData(String value) {
        mReadData.setValue(value);
    }
}