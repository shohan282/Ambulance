package com.example.ambulance;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ambulance.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FirebaseFirestore db;
    String uLat,uLng,mbl;
    ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        uLat = getIntent().getStringExtra("lat");
        uLng = getIntent().getStringExtra("lng");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { return; }

        mMap.setMyLocationEnabled(true);
        LatLng latLng = new LatLng(Double.parseDouble(uLat),Double.parseDouble(uLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        showAmbulance();

        mMap.setOnInfoWindowClickListener(marker -> {

            binding.card.setVisibility(View.VISIBLE);
            showInfo(marker.getTitle());

        });

    }

    private void showInfo(String title) {

        db = FirebaseFirestore.getInstance();
        mMap.clear();

        db.collection("driver").whereEqualTo("uName",title).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {

                    mbl = document.getData().get("phn").toString();

                    binding.phnNumShow.setText("Mbl: "+mbl);
                    binding.uName.setText(title);

                }
            } else {
                Toast.makeText(MapsActivity.this, "error: "+task.getException(), Toast.LENGTH_SHORT).show();
            }


        });

    }

    public void Refresh(View view) {

        showAmbulance();

    }

    public void Gone(View view) {

        binding.card.setVisibility(View.GONE);

    }

    void showAmbulance(){

        mMap.clear();
        db = FirebaseFirestore.getInstance();

        db.collection("driver").get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                    String lat = documentSnapshot.getData().get("latitude").toString();
                    String lng = documentSnapshot.getData().get("longitude").toString();
                    String title = documentSnapshot.getData().get("uName").toString();

                    LatLng sydney = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                    mMap.addMarker(new MarkerOptions().position(sydney).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.ambulance)));

                }

            } else {

                Toast.makeText(MapsActivity.this, "Error getting documents: "+ task.getException(), Toast.LENGTH_SHORT).show();

            }

        });

    }

    public void call(View view) {

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mbl));
        startActivity(intent);

    }

    public void clickNum(View view) {

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mbl));
        startActivity(intent);

    }

    public void Track(View view) {

        Intent intent = new Intent(MapsActivity.this,TrackAmb.class);
        intent.putExtra("lat",uLat);
        intent.putExtra("lng",uLng);
        intent.putExtra("mbl",mbl);
        startActivity(intent);

    }

}