package com.example.geocachingapp.database;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {
    @TypeConverter
    public static ArrayList<Bitmap> fromString(String value) {
        Type listType = new TypeToken<ArrayList<Bitmap>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Bitmap> list) {
        Gson gson = new Gson();
        Log.d("Converters", gson.toJson(list));
        return gson.toJson(list);
    }
}
