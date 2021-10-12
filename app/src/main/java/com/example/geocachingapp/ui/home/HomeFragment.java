package com.example.geocachingapp.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geocachingapp.AppViewModel;
import com.example.geocachingapp.databinding.FragmentHomeBinding;
import com.example.geocachingapp.ui.home.LocationInfoRecycler.LocationInfoContent;
import com.example.geocachingapp.ui.search.SearchViewModel;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private AppViewModel mAppViewModel;
    public static SearchViewModel searchViewModel;
    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Empty fragment for free instantiation like orientation
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeViewModel.getText().observe(requireActivity(), s -> System.out.println("HomeFragment " + s));

        RecyclerView view = binding.list;
        searchViewModel =
                new ViewModelProvider(requireActivity()).get(SearchViewModel.class);

        LocationInfoRecyclerViewAdapter adapter =
                new LocationInfoRecyclerViewAdapter(new LocationInfoRecyclerViewAdapter.QRCodeDiff());
        view.setAdapter(adapter);
        Context context = view.getContext();
        view.setLayoutManager(new LinearLayoutManager(context));

        mAppViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        // Update the cached copy of the words in the adapter.
        mAppViewModel.getAllCodes().observe(requireActivity(), adapter::submitList);
        return root;
    }

    private static final String ARG_COLUMN_COUNT = "column-count";

    @SuppressWarnings("unused")
    public static HomeFragment newInstance(int columnCount) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            int mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}