package com.example.geocachingapp.ui.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geocachingapp.R;
import com.example.geocachingapp.databinding.FragmentArBinding;
import com.example.geocachingapp.databinding.FragmentSearchPagerBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchPagerFragment extends Fragment {

    public SearchPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentSearchPagerBinding binding = FragmentSearchPagerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}