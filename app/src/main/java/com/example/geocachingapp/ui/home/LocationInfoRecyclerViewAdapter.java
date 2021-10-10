package com.example.geocachingapp.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geocachingapp.R;
import com.example.geocachingapp.databinding.FragmentItemBinding;
import com.example.geocachingapp.ui.home.LocationInfoRecycler.LocationInfoContent.LocationInfo;
import com.example.geocachingapp.ui.search.SearchViewModel;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link LocationInfo}.
 */
public class LocationInfoRecyclerViewAdapter extends RecyclerView.Adapter<LocationInfoRecyclerViewAdapter.ViewHolder> {

    private final List<LocationInfo> mValues;
    private final SearchViewModel searchViewModel;

    public LocationInfoRecyclerViewAdapter(List<LocationInfo> items, SearchViewModel s) {
        mValues = items;
        searchViewModel = s;
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
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchViewModel.setLatLng(holder.mItem.location);
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_search);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public final ConstraintLayout mLayout;
        public LocationInfo mItem;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
            mLayout = binding.constraintLayout;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}