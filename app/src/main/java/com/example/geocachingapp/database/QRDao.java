package com.example.geocachingapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface QRDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(QRCode qrCode);

    @Update
    Completable update(QRCode qrCode);

    @Delete
    Completable delete(QRCode qrCode);

    @Query("SELECT * FROM qrcode")
    List<QRCode> loadAllQRCodes();

    @Query(("SELECT * FROM qrcode WHERE id = :id"))
    LiveData<QRCode> loadQRCodeById(int id);

    @Query("SELECT * FROM qrcode ORDER BY name")
    LiveData<List<QRCode>> getOrderedQRCodes();

    @Query("DELETE FROM qrcode")
    void deleteAll();
}
