package com.example.geocachingapp.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geocachingapp.R;
import com.example.geocachingapp.database.QRCode;
import com.example.geocachingapp.databinding.FragmentItemBinding;
import com.example.geocachingapp.ui.home.LocationInfoRecycler.LocationInfoContent.LocationInfo;
import com.example.geocachingapp.ui.search.SearchViewModel;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link LocationInfo}.
 */
public class LocationInfoRecyclerViewAdapter extends ListAdapter<QRCode, ViewHolder> {

    public LocationInfoRecyclerViewAdapter(@NonNull DiffUtil.ItemCallback<QRCode> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        QRCode current = getItem(position);
        holder.bind(current.getName(), current.getDescription(), current.getId(), current.getPicture(),
                current.getLatitude(), current.getLongitude());
    }

    static class QRCodeDiff extends DiffUtil.ItemCallback<QRCode> {

        @Override
        public boolean areItemsTheSame(@NonNull QRCode oldItem, @NonNull QRCode newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull QRCode oldItem, @NonNull QRCode newItem) {
            return oldItem.getId().equals(newItem.getId());
        }
    }
}