package com.example.geocachingapp.ui.home;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geocachingapp.R;
import com.example.geocachingapp.databinding.FragmentItemBinding;
import com.example.geocachingapp.ui.customViews.CircleImageView;
import com.example.geocachingapp.ui.home.LocationInfoRecycler.LocationInfoContent;

public class ViewHolder extends RecyclerView.ViewHolder {
    public final TextView mIdView;
    public final TextView mContentView;
    public final CircleImageView mImageView;
    public final ConstraintLayout mLayout;
    public LocationInfoContent.LocationInfo mItem;

    public ViewHolder(View binding) {
        super(binding);
        mIdView = binding.findViewById(R.id.item_number);
        mContentView = binding.findViewById(R.id.content);
        mImageView = binding.findViewById(R.id.profile_image_view);
        mLayout = binding.findViewById(R.id.constraintLayout);
        mLayout.setOnClickListener(view -> {
            HomeFragment.searchViewModel.setLatLng(mItem.location);
            Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_search);
        });
    }

    public void bind(String name, String desc, String hash, byte[] picture, double lat, double lon) {
        mIdView.setText(name);
        mContentView.setText(desc);
        if(picture != null) {
            mImageView.setImageBitmap(BitmapFactory.decodeByteArray(picture, 0, picture.length));
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
