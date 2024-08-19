package com.example.barberease;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {

    private Context context;
    private List<String> photosList;

    public PhotosAdapter(Context context, List<String> photosList) {
        this.context = context;
        this.photosList = photosList;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        String photoUrl = photosList.get(position);
        Glide.with(context).load(photoUrl).into(holder.photoImageView);
    }

    @Override
    public int getItemCount() {
        return photosList.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        public ImageView photoImageView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.photo_image_view);
        }
    }
}
