package com.example.barberease;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<Object> itemList;
    private Context context;

    public ImageAdapter(List<Object> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Object item = itemList.get(position);
        String imageUrl = null;

        if (item instanceof Barber) {
            Barber barber = (Barber) item;
            imageUrl = barber.getImageUrl();
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, Barber.class);
                intent.putExtra("barber", barber);
                context.startActivity(intent);
            });
        } else if (item instanceof BarberShop) {
            BarberShop shop = (BarberShop) item;
            imageUrl = shop.getImageUrl();
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, BarberShop.class);
                intent.putExtra("shop", shop);
                context.startActivity(intent);
            });
        }

        Glide.with(context).load(imageUrl).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}
