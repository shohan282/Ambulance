package com.example.ambulance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ambulance.databinding.ActivitySelectTypeBinding;

public class SelectType extends AppCompatActivity implements View.OnClickListener{

    ActivitySelectTypeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectTypeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.covid.setOnClickListener(this);
        binding.freezer.setOnClickListener(this);
        binding.icu.setOnClickListener(this);
        binding.multiple.setOnClickListener(this);
        binding.nurse.setOnClickListener(this);
        binding.other.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.covid:
                Intent intent =new Intent(this,SelectAmb.class);
                intent.putExtra("type","covid");
                startActivity(intent);
                break;

            case R.id.freezer:
                Intent intent2 =new Intent(this,SelectAmb.class);
                intent2.putExtra("type","freezer");
                startActivity(intent2);
                break;

            case R.id.icu:
                Intent intent3 =new Intent(this,SelectAmb.class);
                intent3.putExtra("type","icu");
                startActivity(intent3);
                break;

            case R.id.multiple:
                Intent intent4 =new Intent(this,SelectAmb.class);
                intent4.putExtra("type","multiple");
                startActivity(intent4);
                break;

            case R.id.other:
                Intent intent5 =new Intent(this,SelectAmb.class);
                intent5.putExtra("type","other");
                startActivity(intent5);
                break;

            case R.id.nurse:
                Intent intent6 =new Intent(this,SelectAmb.class);
                intent6.putExtra("type","nurse");
                startActivity(intent6);
                break;

        }

    }
}