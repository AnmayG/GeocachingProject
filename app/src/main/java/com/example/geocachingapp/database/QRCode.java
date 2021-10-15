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
import java.util.Locale;

@Entity
public class QRCode {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public String id = "";

    public QRCode() {}

    public QRCode(@NonNull String hash, String name, String desc,
                  Bitmap picture, double lat, double lon, ArrayList<String> pictureStorage, String addr) {
        this.id = hash;
        this.name = name;
        this.description = desc;
        this.picture = picture;
        this.pictureStorage = pictureStorage;
        this.latitude = lat;
        this.longitude = lon;
        this.address = addr;
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
    public Bitmap picture;

    public Bitmap getPicture() {
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
    public ArrayList<String> pictureStorage;

    public ArrayList<String> getPictureStorage() {
        return pictureStorage;
    }

    @ColumnInfo(name = "address")
    public String address = "No address provided";

    public String getAddress() {
        return address;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.US, "\n%s\n  %s\n  %s\n  %f\n  %f\n  %s",
                id, name, description, latitude, longitude, address);
    }
}
