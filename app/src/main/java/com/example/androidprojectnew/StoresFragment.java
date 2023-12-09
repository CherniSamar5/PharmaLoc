package com.example.androidprojectnew;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StoresFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap googleMap;
    private Location currentLocation;
    private static final int REQUEST_CODE = 101;
    private String cityName;
    private DatabaseReference databaseReference;

    List<Pharmacy> pharmacies = new ArrayList<>();

    public interface DataCallback {
        void onDataLoaded(List<Pharmacy> pharmacies);
        void onError(String errorMessage);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stores, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Button click to get last location
        view.findViewById(R.id.btnCurrentLocation).setOnClickListener(v -> getLastLocation());
    }

    private void addMarker(GoogleMap googleMap, LatLng position, String title, String snippet, int titleColor) {
        if (title != null) {
            SpannableString spannableTitle = new SpannableString(title);
            spannableTitle.setSpan(new ForegroundColorSpan(titleColor), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            googleMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title("my pharmacy samar")
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
    }


    public void readData(final DataCallback callback) {
        // Read data from the "pharmacies" node
        databaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Callback with an error message
                callback.onError(databaseError.getMessage());
            }
        });
    }
    private void getPharmaciesFromDatabase() {
        FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-login-signup-69d6b-default-rtdb.firebaseio.com/").child(
                        "pharmacies")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Pharmacy> pharmacyList = new ArrayList<>();
                        for (DataSnapshot pharmacySnapshot : dataSnapshot.getChildren()) {
                            Pharmacy pharmacy = pharmacySnapshot.getValue(Pharmacy.class);
                            if (pharmacy != null) {
                                pharmacyList.add(pharmacy);
                            }
                        }

                        // Now you have the list of pharmacies, you can do whatever you want with it.
                        // For example, you can add markers on the map.
                        addMarkersForPharmacies(pharmacyList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle errors here
                    }
                });
    }

    private void addMarkersForPharmacies(List<Pharmacy> pharmacyList) {
        for (Pharmacy pharmacy : pharmacyList) {
            LatLng pharmacyLocation = new LatLng(pharmacy.getLatitude(), pharmacy.getLongitude());
            addMarker(googleMap, pharmacyLocation, pharmacy.getName(), "   ", Color.BLUE);
        }
    }
    private void getLastLocation() {

        // Simulation de données statiques
        //LatLng pharmacy1 = new LatLng(36.8065, 10.1815);  // Coordonnées pour une pharmacie à Tunis
        //LatLng pharmacy2 = new LatLng(36.7975, 10.1828);  // Coordonnées pour une autre pharmacie à Tunis
        getPharmaciesFromDatabase();

        //addMarker(googleMap, pharmacy1, "Pharmacy 1", "Address 1 in Tunis", Color.BLUE);
        //addMarker(googleMap, pharmacy2, "Pharmacy 2", "Address 2 in Tunis", Color.BLUE);
        // Déplacer la caméra pour montrer tous les marqueurs
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pharmacy1, 12));

        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    updateMap();
                }
            }
        });


    }

    private void updateMap() {
        if (googleMap != null && currentLocation != null) {
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Here");

            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
            googleMap.addMarker(markerOptions);
            googleMap.setOnMapClickListener(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        updateMap();
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        getCityName(latitude, longitude);

        Intent i = new Intent();
        i.putExtra("longitude", longitude);
        i.putExtra("latitude", latitude);
        i.putExtra("city", cityName);
        requireActivity().setResult(requireActivity().RESULT_OK, i);
    }

    private void getCityName(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                cityName = addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
