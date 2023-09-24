package com.chris.firebaseauth.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.chris.firebaseauth.R;
import com.chris.firebaseauth.databinding.FragmentMapBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class MapFragment extends Fragment {

    private FragmentMapBinding binding;
    ViewPager2 viewPager2;
    TabLayout tabLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TabPagerAdapter tabPager = new TabPagerAdapter(this);
        viewPager2 = view.findViewById(R.id.viewPager);
        viewPager2.setAdapter(tabPager);
        viewPager2.setUserInputEnabled(false);

        tabLayout = view.findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.select()).attach();
        tabLayout.getTabAt(0).setText("Map");
        tabLayout.getTabAt(1).setText("Results");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public class TabPagerAdapter extends FragmentStateAdapter {
        public TabPagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new MapTab();
                case 1:
                    return new ResultsTab();
                default:
                    return new MapTab();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}
