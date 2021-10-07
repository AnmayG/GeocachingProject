package com.example.geocachingapp.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geocachingapp.databinding.FragmentItemBinding;
import com.example.geocachingapp.ui.home.LocationInfoRecycler.LocationInfoContent.LocationInfo;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link LocationInfo}.
 * TODO: Replace the implementation with code for your data type.
 */
public class LocationInfoRecyclerViewAdapter extends RecyclerView.Adapter<LocationInfoRecyclerViewAdapter.ViewHolder> {

    private final List<LocationInfo> mValues;

    public LocationInfoRecyclerViewAdapter(List<LocationInfo> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public LocationInfo mItem;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}