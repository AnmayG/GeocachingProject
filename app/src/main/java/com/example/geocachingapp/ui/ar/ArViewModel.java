package com.example.geocachingapp.ui.ar;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ArViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> arAllowed;

    public ArViewModel(@NonNull Application application) {
        super(application);
        arAllowed = new MutableLiveData<>(true);
    }

    public LiveData<Boolean> getArAllowed() {
        return arAllowed;
    }

    public void setArAllowed(boolean arAllowed) {
        this.arAllowed.setValue(arAllowed);
    }
}
