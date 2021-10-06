package com.example.ambulance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.ambulance.databinding.ActivitySelectAmbBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SelectAmb extends AppCompatActivity {

    ActivitySelectAmbBinding binding;
    FirebaseFirestore db;
    ArrayList<model> dataList;
    AmbAdapter adapter;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySelectAmbBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        db=FirebaseFirestore.getInstance();
        binding.listAmb.setLayoutManager(new LinearLayoutManager(this));
        dataList = new ArrayList<>();
        adapter = new AmbAdapter(dataList);
        binding.listAmb.setAdapter(adapter);

        type = getIntent().getStringExtra("type");

        if(TextUtils.equals(type,"covid")) {

            db.collection("driver").whereEqualTo("cOVID","yes").get().addOnSuccessListener(queryDocumentSnapshots -> {

                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list) {

                    model obj=d.toObject(model.class);
                    dataList.add(obj);
                }
                adapter.notifyDataSetChanged();
            });

        } else {

            db.collection("driver").whereEqualTo("type",type).get().addOnSuccessListener(queryDocumentSnapshots -> {

                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list) {

                    model obj=d.toObject(model.class);
                    dataList.add(obj);
                }
                adapter.notifyDataSetChanged();
            });

        }

    }

}
