package com.example.ambulance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AmbAdapter  extends RecyclerView.Adapter<AmbAdapter.MyViewHolder> {

    ArrayList<model> dataList;

    public AmbAdapter(ArrayList<model> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.name.setText(dataList.get(position).getuName());
        holder.phn.setText("Mbl: "+dataList.get(position).getPhn());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name,phn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.list_name);
            phn=itemView.findViewById(R.id.list_phn);

        }
    }

}

