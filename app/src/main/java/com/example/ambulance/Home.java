package com.example.ambulance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ambulance.databinding.ActivityHomeBinding;
import com.example.ambulance.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    ActivityHomeBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();

        binding.map.setOnClickListener(v -> {

            Intent intent = new Intent(Home.this,MapsActivity.class);
            startActivity(intent);

        });

        binding.logout.setOnClickListener(v -> {

            mAuth.signOut();
            Intent intent = new Intent(Home.this,MainActivity.class);
            startActivity(intent);

        });

    }
}