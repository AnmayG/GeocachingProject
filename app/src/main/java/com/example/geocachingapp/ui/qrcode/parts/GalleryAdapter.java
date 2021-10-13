package com.example.geocachingapp.ui.qrcode.parts;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geocachingapp.R;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    ArrayList<Bitmap> bitmapArray;
    Context context;

    public GalleryAdapter(Context ct, ArrayList<Bitmap> bit){
        bitmapArray = bit;
        context = ct;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.gallery_item, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        holder.imageView.setImageBitmap(bitmapArray.get(position));
    }

    @Override
    public int getItemCount() {
        return bitmapArray.size();
    }

    public static class GalleryViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}