package com.chris.firebaseauth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.chris.firebaseauth.history.HistoryFragment;
import com.chris.firebaseauth.home.HomeFragment;
import com.chris.firebaseauth.map.MapFragment;
import com.chris.firebaseauth.scan.ScanActivity;
import com.chris.firebaseauth.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private static final String SELECTED_FRAGMENT_KEY = "selected_fragment";

    BottomNavigationView navigationView;
    FloatingActionButton fab;

    HomeFragment homeFragment;
    MapFragment mapFragment;
    HistoryFragment historyFragment;
    SettingsFragment settingsFragment;

    Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        boolean isDarkModeEnabled = sharedPreferences.getBoolean("dark_mode", false);
        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.bottomNav);
        fab = findViewById(R.id.fab);

        homeFragment = new HomeFragment();
        mapFragment = new MapFragment();
        historyFragment = new HistoryFragment();
        settingsFragment = new SettingsFragment();

        if (savedInstanceState != null) {
            selectedFragment = getSupportFragmentManager().findFragmentByTag(savedInstanceState.getString(SELECTED_FRAGMENT_KEY));
        } else {
            selectedFragment = homeFragment; // Default fragment
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrame, selectedFragment, selectedFragment.getClass().getSimpleName())
                .commit();
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    selectedFragment = homeFragment;
                } else if (itemId == R.id.map) {
                    selectedFragment = mapFragment;
                } else if (itemId == R.id.history) {
                    selectedFragment = historyFragment;
                } else if (itemId == R.id.settings) {
                    selectedFragment = settingsFragment;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, selectedFragment, selectedFragment.getClass().getSimpleName()).commit();

                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the currently selected fragment
        getSupportFragmentManager().putFragment(outState, SELECTED_FRAGMENT_KEY, selectedFragment);
    }
}