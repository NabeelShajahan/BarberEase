package com.example.barberease;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_barber, parent, false);
        return new BarberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarberViewHolder holder, int position) {
        Log.d("in barber adapter","barber count" + barberList.size());
        Barber barber = barberList.get(position);
        holder.barberNameTextView.setText(barber.getName());

        if (barber.getImageUrl() != null && !barber.getImageUrl().isEmpty()) {
            Picasso.get().cancelRequest(holder.barberImageView);

            Picasso.get()
                    .load(barber.getImageUrl())
                    .placeholder(R.drawable.ic_profile) // Set a placeholder image while loading
                    .error(R.drawable.ic_profile) // Set default image in case of error
                    .into(holder.barberImageView);
        } else {
            holder.barberImageView.setImageResource(R.drawable.ic_profile);
        }


        holder.itemView.setOnClickListener(v -> {
            Log.d("BarberAdapter", "Item clicked: " + barber.getName());
            Intent intent = new Intent(context, BarberDetailsActivity.class);
            intent.putExtra("barberId", barber.getUserId());
            intent.putExtra("barberImageUrl", barber.getImageUrl());
            context.startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {

        return barberList.size();
    }

    public static class BarberViewHolder extends RecyclerView.ViewHolder {
        TextView barberNameTextView;
        ImageView barberImageView;

        public BarberViewHolder(@NonNull View itemView) {
            super(itemView);
            barberNameTextView = itemView.findViewById(R.id.barber_name_text_view);
            barberImageView = itemView.findViewById(R.id.barber_image_view);
        }
    }
}
