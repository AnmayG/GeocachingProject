package com.example.geocachingapp.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geocachingapp.R;
import com.example.geocachingapp.databinding.FragmentItemBinding;
import com.example.geocachingapp.ui.customViews.CircleImageView;
import com.example.geocachingapp.ui.home.LocationInfoRecycler.LocationInfoContent;

import java.util.Objects;

public class ViewHolder extends RecyclerView.ViewHolder {
    public final TextView mIdView;
    public final TextView mContentView;
    public final CircleImageView mImageView;
    public final ConstraintLayout mLayout;
    public LocationInfoContent.LocationInfo mItem;
    public Bitmap img;
    public String address;

    public ViewHolder(View binding) {
        super(binding);
        mIdView = binding.findViewById(R.id.item_number);
        mContentView = binding.findViewById(R.id.content);
        mImageView = binding.findViewById(R.id.profile_image_view);
        mLayout = binding.findViewById(R.id.constraintLayout);
        mLayout.setOnClickListener(view -> {
            InfoDialog bottomSheet = new InfoDialog(this);
            bottomSheet.show(((AppCompatActivity) binding.getContext()).getSupportFragmentManager(), "ModalBottomSheet");
        });
    }

    public void bind(String name, String desc, String hash, byte[] picture, double lat, double lon, String addr) {
        mIdView.setText(name);
        mContentView.setText(addr);
        address = addr;
        if(picture != null) {
            img = BitmapFactory.decodeByteArray(picture, 0, picture.length);
            mImageView.setImageBitmap(img);
        }
        mItem = new LocationInfoContent.LocationInfo(hash, name, desc, lat, lon);
    }

    static ViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " '" + mContentView.getText() + "'";
    }
}
