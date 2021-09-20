package com.example.ambulance;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.ambulance.databinding.ActivityTrackAmbBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class TrackAmb extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "ok";
    private GoogleMap mMap;
    FirebaseFirestore db;
    String uLat, uLng, mbl;
    ActivityTrackAmbBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrackAmbBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        uLat = getIntent().getStringExtra("lat");
        uLng = getIntent().getStringExtra("lng");
        mbl = getIntent().getStringExtra("mbl");

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { return; }

        mMap.setMyLocationEnabled(true);
        LatLng latLng = new LatLng(Double.parseDouble(uLat),Double.parseDouble(uLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        Handler handler = new Handler();

        Runnable mToastRunnable = new Runnable() {
            @Override
            public void run() {

                getLocation();

                handler.postDelayed(this, 3000);

            }
        };

        mToastRunnable.run();

    }

    void getLocation(){

        db = FirebaseFirestore.getInstance();
        mMap.clear();

        db.collection("driver").whereEqualTo("phn",mbl).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {

                    String dLat = document.getData().get("latitude").toString();
                    String dLng = document.getData().get("longitude").toString();
                    String title = document.getData().get("uName").toString();

                    LatLng driver = new LatLng(Double.parseDouble(dLat),Double.parseDouble(dLng));

                    mMap.addMarker(new MarkerOptions().position(driver).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.ambulance)));


                }
            } else {
                Toast.makeText(TrackAmb.this, "error: "+task.getException(), Toast.LENGTH_SHORT).show();
            }


        });

    }

}