package com.example.geocachingapp.ui.qrcode.parts;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.geocachingapp.R;

import java.util.ArrayList;

public class CustomGrid extends BaseAdapter{
    private Context mContext;
    private final ArrayList<Bitmap> imageId;

    public CustomGrid(Context c, ArrayList<Bitmap> imageId) {
        mContext = c;
        this.imageId = imageId;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imageId.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, parent);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            imageView.setImageBitmap(imageId.get(position));
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}