package com.example.barberease;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder> {

    private List<Service> servicesList;

    public ServicesAdapter(List<Service> servicesList) {
        this.servicesList = servicesList;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = servicesList.get(position);
        holder.serviceNameTextView.setText(service.getName());
        holder.servicePriceTextView.setText("$" + service.getPrice());
    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    public void updateServices(List<Service> newServices) {
        this.servicesList = newServices;
        notifyDataSetChanged();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView serviceNameTextView, servicePriceTextView;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceNameTextView = itemView.findViewById(R.id.service_name);
            servicePriceTextView = itemView.findViewById(R.id.service_price);
        }
    }
}
