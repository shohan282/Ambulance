package com.example.ambulance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ambulance.databinding.ActivityHomeBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;



public class Home extends AppCompatActivity {

    ActivityHomeBinding binding;
    FirebaseAuth mAuth;
    Double latitude,longitude;
    FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { return; }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {

                    if (location != null) {

                        longitude = location.getLongitude();
                        latitude = location.getLatitude();

                    } else {

                        latitude = 23.723519;
                        longitude = 90.360677;

                    }
                });

        binding.map.setOnClickListener(v -> {

            Intent intent = new Intent(Home.this, MapsActivity.class);
            intent.putExtra("lat",latitude.toString());
            intent.putExtra("lng",longitude.toString());
            startActivity(intent);

        });

        binding.logout.setOnClickListener(v -> {

            mAuth.signOut();
            Intent intent = new Intent(Home.this, MainActivity.class);
            startActivity(intent);

        });

    }

}