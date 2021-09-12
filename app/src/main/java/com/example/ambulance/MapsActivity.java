package com.example.ambulance;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FirebaseFirestore db;
    private Handler mHandler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng user = new LatLng(23,90);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(user));;

        runnable = new Runnable() {
            @Override
            public void run() {

                db = FirebaseFirestore.getInstance();
                mMap.clear();
                db.collection("driver_location")
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    String lat = document.getData().get("latitude").toString();
                                    String lng = document.getData().get("longitude").toString();

                                    LatLng sydney = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                                    mMap.addMarker(new MarkerOptions().position(sydney).title("document.getId()"));

                                }
                            } else {
                                Toast.makeText(MapsActivity.this, "Error getting documents: "+ task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        });

                mHandler.postDelayed(this, 3000);
            }
        };

        runnable.run();

    }
}