package com.example.geocachingapp.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface QRDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(QRCode qrCode);

    @Update
    void update(QRCode qrCode);

    @Delete
    void delete(QRCode qrCode);

    @Query("SELECT * FROM qrcode")
    List<QRCode> loadAllQRCodes();

    @Query(("SELECT * FROM qrcode WHERE id = :id"))
    LiveData<QRCode> loadQRCodeById(String id);

    @Query("SELECT * FROM qrcode ORDER BY name")
    LiveData<List<QRCode>> getOrderedQRCodes();

    @Query("DELETE FROM qrcode")
    void deleteAll();
}
