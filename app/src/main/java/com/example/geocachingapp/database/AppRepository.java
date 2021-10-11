package com.example.geocachingapp.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AppRepository {

    private QRDao mQrDao;
    private LiveData<List<QRCode>> mAllCodes;

    // Note that in order to unit test the AppRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mQrDao = db.qrDao();
        mAllCodes = mQrDao.getOrderedQRCodes();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<QRCode>> getAllCodes() {
        return mAllCodes;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(QRCode code) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mQrDao.insert(code);
        });
    }
}