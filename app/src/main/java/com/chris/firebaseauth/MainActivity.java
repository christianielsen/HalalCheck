package com.chris.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;

import com.chris.firebaseauth.home.HomeFragment;
import com.chris.firebaseauth.map.MapFragment;
import com.chris.firebaseauth.profile.ProfileFragment;
import com.chris.firebaseauth.scan.ScanActivity;
import com.chris.firebaseauth.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.bottomNav);
        fab = findViewById(R.id.fab);

        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame, homeFragment, "");
        fragmentTransaction.commit();

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    FragmentTransaction home = getSupportFragmentManager().beginTransaction();
                    home.replace(R.id.mainFrame, homeFragment, "");
                    home.commit();
                } else if (itemId == R.id.map) {
                    MapFragment mapFragment = new MapFragment();
                    FragmentTransaction map = getSupportFragmentManager().beginTransaction();
                    map.replace(R.id.mainFrame, mapFragment, "");
                    map.commit();
                } else if (itemId == R.id.profile) {
                    ProfileFragment profileFragment = new ProfileFragment();
                    FragmentTransaction profile = getSupportFragmentManager().beginTransaction();
                    profile.replace(R.id.mainFrame, profileFragment, "");
                    profile.commit();
                } else if (itemId == R.id.settings) {
                    SettingsFragment settingsFragment = new SettingsFragment();
                    FragmentTransaction settings = getSupportFragmentManager().beginTransaction();
                    settings.replace(R.id.mainFrame, settingsFragment, "");
                    settings.commit();
                }
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



}