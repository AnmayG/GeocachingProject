package com.example.geocachingapp.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.geocachingapp.databinding.FragmentQrPagerBinding;
import com.example.geocachingapp.ui.qrcode.QRCodeViewModel;
import com.example.geocachingapp.ui.qrcode.parts.QrBuildFragment;
import com.example.geocachingapp.ui.qrcode.parts.QrMakeFragment;
import com.example.geocachingapp.ui.qrcode.parts.QrScanFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 * This contains the scan and generation fragments.
 */
public class SearchPagerFragment extends Fragment {

    private FragmentQrPagerBinding binding;
    private com.example.geocachingapp.ui.search.SearchViewModel mSearchViewModel;
    private com.example.geocachingapp.ui.qrcode.QRCodeViewModel mQrViewModel;

    private static ViewPager2 viewPager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQrPagerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mSearchViewModel =
                new ViewModelProvider(requireActivity()).get(SearchViewModel.class);

        mQrViewModel =
                new ViewModelProvider(requireActivity()).get(QRCodeViewModel.class);

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = binding.pager;
        FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(requireActivity());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(pagerAdapter.getItemCount());

        TabLayout tabLayout = binding.tabLayout;
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    String s = "";
                    switch (position) {
                        case 0: s = "Search"; break;
                        case 1: s = "Scan"; break;
                        case 2: s = "Store"; break;
                        case 3: s = "AR View"; break;
                    }
                    tab.setText(s);
                }
        ).attach();

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mQrViewModel.setReadData(null);
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    public static class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0: return SearchFragment.newInstance();
                case 1: return QrScanFragment.newInstance(true);
                case 2: return QrBuildFragment.newInstance(true);
                case 3:
                    // intent to switch code
                    break;
            }
            return QrMakeFragment.newInstance();
        }

        @Override
        public int getItemCount() {
            return 4;
        }

        public static void switchFragment(int target) {
            viewPager.setCurrentItem(target);
        }
    }
}