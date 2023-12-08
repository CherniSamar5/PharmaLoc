package com.example.androidprojectnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CurrentLocation extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener {

    Location currentLocation;
    Location lastLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    Button btnCurrentLocation;
    private GoogleMap googleMap;
    private LatLng originalCameraPosition;
    private float originalZoomLevel;
    private static final int REQUEST_CODE = 101;
    String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btnCurrentLocation = findViewById(R.id.btnCurrentLocation);
        btnCurrentLocation.setOnClickListener(view ->{
            getLastLocation();
        });




    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + " " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
                    supportMapFragment.getMapAsync(CurrentLocation.this);

                    getCityName(currentLocation.getLatitude(), currentLocation.getLongitude());

                }
            }
        });
    }
    private void getCityName(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                cityName = addresses.get(0).getLocality();
                // Here, 'cityName' contains the name of the city
                if (cityName != null && !cityName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "City: " + cityName, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "City name not found", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Here");

        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        googleMap.addMarker(markerOptions);
        googleMap.setOnMapClickListener(this);
        originalCameraPosition = googleMap.getCameraPosition().target;
        originalZoomLevel = googleMap.getCameraPosition().zoom;


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
                }
                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        System.out.println(latLng.longitude);
        getCityName(latitude, longitude);
        Intent i=new Intent();
        i.putExtra("longitude",longitude);
        i.putExtra("latitude",latitude);
        System.out.println(cityName);
        i.putExtra("city",cityName);
        setResult(RESULT_OK,i);


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

}