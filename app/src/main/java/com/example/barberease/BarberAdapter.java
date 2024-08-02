package com.example.barberease;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BarberAdapter extends RecyclerView.Adapter<BarberAdapter.BarberViewHolder> {

    private List<Barber> barberList;
    private Context context;

    public BarberAdapter(List<Barber> barberList, Context context) {
        this.barberList = barberList;
        this.context = context;
    }

    @NonNull
    @Override
    public BarberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_barber_adapter, parent, false);
        return new BarberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarberViewHolder holder, int position) {
        Barber barber = barberList.get(position);
        holder.nameTextView.setText(barber.getName());
        holder.detailsTextView.setText(barber.getDetails());
        holder.itemView.setOnClickListener(v -> {
            String barberId = barber.getUserId();
            if (barberId != null) {
                Intent intent = new Intent(context, BarberDetailsActivity.class);
                intent.putExtra("barberId", barberId);
                context.startActivity(intent);
            } else {
                Log.e("BarberAdapter", "barberId is null");
            }
        });
    }

    @Override
    public int getItemCount() {
        return barberList.size();
    }

    public static class BarberViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView detailsTextView;

        public BarberViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            detailsTextView = itemView.findViewById(R.id.details_text_view);
        }
    }
}
