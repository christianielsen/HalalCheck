package com.chris.firebaseauth.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.chris.firebaseauth.APICall;
import com.chris.firebaseauth.BuildConfig;
import com.chris.firebaseauth.R;
import com.chris.firebaseauth.databinding.FragmentMapTabBinding;
import com.chris.firebaseauth.map.models.NearbyPlaces;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapTab extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    private FragmentMapTabBinding binding;
    GoogleMap mMap;
    Button btRestaurant;
    private String token = "";
    private FusedLocationProviderClient fusedLocationClient;
    private Location mCurrentLocation;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private MapViewModel viewModel;

    public MapTab() {
        // Required empty public constructor
    }

    private ActivityResultLauncher<String> locationPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            requestLocation();
        } else {
            Toast.makeText(getActivity(), "Location permission denied", Toast.LENGTH_SHORT).show();
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMapTabBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        supportMapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            requestLocation();
        }

        btRestaurant = root.findViewById(R.id.btRestaurant);
        btRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl("https://maps.googleapis.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                APICall apiCall = retrofit.create(APICall.class);

                String key = BuildConfig.MAPS_API_KEY;

                String lat = String.valueOf(mCurrentLocation.getLatitude());
                String lng = String.valueOf(mCurrentLocation.getLongitude());
                String loc = lat + "," + lng;
                String radius = "1000";
                String type = "restaurant";
                String keyword = "halal";

                getPlaces(apiCall, loc, radius, type, keyword, key);
            }
        });

        return root;
    }

    private void getPlaces(APICall apiCall, String loc, String radius, String type, String keyword, String key) {
        Call<NearbyPlaces> call = apiCall.getData(loc, radius, type, keyword, key);
        call.enqueue(new Callback<NearbyPlaces>() {
            @Override
            public void onResponse(Call<NearbyPlaces> call, Response<NearbyPlaces> response) {
                if(response.isSuccessful()) {
                    List<NearbyPlaces.Result> results = response.body().getResults();

                    if (results != null) {
                        viewModel.addResultsData(results);
                        processResults(results);
                    }
                }
            }

            @Override
            public void onFailure(Call<NearbyPlaces> call, Throwable t) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void processResults(List<NearbyPlaces.Result> results) {
        viewModel.setResultsData(results);
        Toast.makeText(getActivity(), "Found " + results.size() + " restaurants", Toast.LENGTH_LONG).show();

        for (NearbyPlaces.Result result : results) {
            addMarkerForPlace(result);
        }
    }


    private void addMarkerForPlace(NearbyPlaces.Result result) {
        Double lat = null;
        Double lng = null;
        if (result.getGeometry() != null &&
                result.getGeometry().getLocation() != null) {
            lat = result.getGeometry().getLocation().getLat();
            lng = result.getGeometry().getLocation().getLng();
        }

        String rName = null;
        if (result.getName() != null) {
            rName = result.getName().toString();
        }

        if (lat != null && lng != null && rName != null) {
            LatLng rLocation = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(rLocation).title(rName));
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Zoom to the device's current location
                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));
                        }
                    }
                });
        mMap.setPadding(0, 200, 0, 0);
        //Add zoom controls
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @SuppressLint("MissingPermission")
    @Override
    public boolean onMyLocationButtonClick() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Zoom to the device's current location
                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));
                        }
                    }
                });

        return false;
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        mCurrentLocation = location;
                    } else {
                        // Location is null
                        Toast.makeText(getActivity(), "Failed to retrieve location", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Failed to retrieve location
                    Toast.makeText(getActivity(), "Failed to retrieve location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


}