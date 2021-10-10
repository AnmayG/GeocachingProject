package com.example.geocachingapp.ui.qrcode;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geocachingapp.R;
import com.example.geocachingapp.databinding.FragmentQrPagerBinding;
import com.example.geocachingapp.databinding.FragmentQrScanBinding;

/**
 * A simple {@link Fragment} subclass.
 * This contains the scan and generation fragments.
 */
public class QrPagerFragment extends Fragment {

    private FragmentQrPagerBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQrPagerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inflate the layout for this fragment
        return root;
    }
}