package com.example.ambulance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ambulance.databinding.ActivityHomeBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;


public class Home extends AppCompatActivity {

    ActivityHomeBinding binding;
    FirebaseAuth mAuth;
    Double latitude,longitude;
    FusedLocationProviderClient fusedLocationClient;
    FirebaseFirestore db;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLocation();

        binding.map.setOnClickListener(v -> {

            Intent intent = new Intent(Home.this, MapsActivity.class);
            intent.putExtra("lat",latitude.toString());
            intent.putExtra("lng",longitude.toString());
            startActivity(intent);

        });

        binding.find.setOnClickListener(v -> {

            startActivity(new Intent(this,SelectType.class));

        });

    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { return; }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {

                    if (location != null) {

                        longitude = location.getLongitude();
                        latitude = location.getLatitude();

                        Map<String, Object> user = new HashMap<>();
                        user.put("latitude",latitude);
                        user.put("longitude",longitude);

                        db.collection("users").document(uid).set(user, SetOptions.merge()).addOnSuccessListener(aVoid -> { });

                    } else {

                        latitude = 23.723519;
                        longitude = 90.360677;

                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.logout:

                mAuth.signOut();

                Intent intent=new Intent(Home.this,MainActivity.class);
                startActivity(intent);
                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void callNow(View view) {

        binding.wait.setVisibility(View.VISIBLE);

        db.collection("driver").whereGreaterThan("latitude",latitude).limit(1).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {

                    String north = document.getData().get("latitude").toString();


                    db.collection("driver").whereLessThan("latitude",latitude).orderBy("latitude", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(task2 -> {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document2 : task.getResult()) {

                                String south = document2.getData().get("latitude").toString();

                                db.collection("driver").whereLessThan("longitude",longitude).orderBy("longitude", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(task3 -> {

                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document3 : task.getResult()) {

                                            String west = document3.getData().get("longitude").toString();

                                            db.collection("driver").whereGreaterThan("longitude",longitude).limit(1).get().addOnCompleteListener(task4 -> {

                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document4 : task.getResult()) {

                                                        String east = document4.getData().get("longitude").toString();

                                                        double dEast,dWest,dNorth,dSouth;

                                                        dEast = Double.parseDouble(east);
                                                        dNorth = Double.parseDouble(north);
                                                        dWest = Double.parseDouble(west);
                                                        dSouth = Double.parseDouble(south);

                                                        if(dEast+dWest < dNorth+dSouth) {

                                                            if(dEast<dWest) {

                                                                FireUpLng(dEast);

                                                            } else {

                                                                FireUpLng(dWest);

                                                            }


                                                        } else {

                                                            if (dNorth<dSouth) {

                                                                FireUpLat(dNorth);

                                                            } else {

                                                                FireUpLat(dSouth);

                                                            }

                                                        }

                                                    }
                                                } else {

                                                    Toast.makeText(Home.this, "error: "+task4.getException(), Toast.LENGTH_SHORT).show();

                                                }

                                            });

                                        }
                                    } else {

                                        Toast.makeText(Home.this, "error: "+task3.getException(), Toast.LENGTH_SHORT).show();

                                    }

                                });

                            }
                        } else {

                            Toast.makeText(Home.this, "error: "+task2.getException(), Toast.LENGTH_SHORT).show();

                        }

                    });

                }
            } else {

                Toast.makeText(Home.this, "error: "+task.getException(), Toast.LENGTH_SHORT).show();

            }

        });

    }

    private void FireUpLat(double latitude) {

        db.collection("driver").whereEqualTo("latitude",latitude).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {

                   String mbl = document.getData().get("phn").toString();

                   Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mbl));
                   startActivity(intent);
                   binding.wait.setVisibility(View.INVISIBLE);

                }
            } else {
                Toast.makeText(Home.this, "error: "+task.getException(), Toast.LENGTH_SHORT).show();
            }


        });
        
    }

    private void FireUpLng(double longitude) {

        db.collection("driver").whereEqualTo("longitude",longitude).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {

                    String mbl = document.getData().get("phn").toString();

                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mbl));
                    startActivity(intent);
                    binding.wait.setVisibility(View.INVISIBLE);

                }
            } else {
                Toast.makeText(Home.this, "error: "+task.getException(), Toast.LENGTH_SHORT).show();
            }


        });
        
    }

}