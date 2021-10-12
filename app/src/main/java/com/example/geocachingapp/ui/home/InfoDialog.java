package com.example.geocachingapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.geocachingapp.R;
import com.example.geocachingapp.ui.customViews.CircleImageView;
import com.example.geocachingapp.ui.qrcode.QRCodeViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class InfoDialog extends BottomSheetDialogFragment {

    private com.example.geocachingapp.ui.qrcode.QRCodeViewModel QRCodeViewModel;

    private final ViewHolder viewHolder;

    public InfoDialog(ViewHolder v) {
        viewHolder = v;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.bottom_modal_info_sheet,
                container, false);

        Button trackingButton = v.findViewById(R.id.start_tracking_button);
        CircleImageView imageView = v.findViewById(R.id.profile_image_view);
        imageView.setImageBitmap(viewHolder.img);
        TextView nameView = v.findViewById(R.id.item_number);
        nameView.setText(viewHolder.mItem.content);
        TextView addressView = v.findViewById(R.id.content);
        addressView.setText(viewHolder.address);
        TextView locationView = v.findViewById(R.id.location);
        locationView.setText(viewHolder.mItem.location.toString().toUpperCase());
        TextView descView = v.findViewById(R.id.desc);
        descView.setText(viewHolder.mItem.details);

        QRCodeViewModel =
                new ViewModelProvider(requireActivity()).get(QRCodeViewModel.class);

        trackingButton.setOnClickListener(view -> {
            HomeFragment.searchViewModel.setLatLng(viewHolder.mItem.location);
            Navigation.findNavController((requireActivity().findViewById(R.id.nav_host_fragment_activity_main)))
                    .navigate(R.id.action_navigation_home_to_navigation_search);
            dismiss();
        });
        return v;
    }
}
