package com.example.geocachingapp.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

@Entity
public class QRCode {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public String id = "";

    public QRCode() {}

    public QRCode(@NonNull String hash, String name, String desc,
                  byte[] picture, double lat, double lon, ArrayList<Bitmap> pictureStorage) {
        this.id = hash;
        this.name = name;
        this.description = desc;
        this.picture = picture;
        this.pictureStorage = pictureStorage;
        this.latitude = lat;
        this.longitude = lon;
    }

    public QRCode(@NonNull String hash) {
        this.id = hash;
        this.name = "No name provided";
        this.description = "No description provided";
        this.picture = null;
        this.latitude = 0;
        this.longitude = 0;
    }

    @NonNull
    public String getId() { return this.id; }

    @ColumnInfo(name = "name")
    public String name;

    public String getName() {
        return name;
    }

    @ColumnInfo(name = "description")
    public String description;

    public String getDescription() {
        return description;
    }

    @ColumnInfo(name = "picture")
    public byte[] picture;

    public byte[] getPicture() {
        return picture;
    }

    @ColumnInfo(name = "latitude")
    public double latitude;

    @ColumnInfo(name = "longitude")
    public double longitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @ColumnInfo(name = "picture_storage")
    public ArrayList<Bitmap> pictureStorage;

    public ArrayList<Bitmap> getPictureStorage() {
        return pictureStorage;
    }
}
