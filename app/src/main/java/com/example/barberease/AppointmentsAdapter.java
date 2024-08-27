package com.example.barberease;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.AppointmentViewHolder> {

    private List<Map<String, String>> appointmentList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public AppointmentsAdapter(List<Map<String, String>> appointmentList, Context context) {
        this.appointmentList = appointmentList;
        this.context = context;
    }

    // Interface for click events
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Map<String, String> appointment = appointmentList.get(position);
        holder.barberNameTextView.setText(appointment.get("barberName"));
        holder.dateTextView.setText("Date: " + appointment.get("date"));
        holder.timeTextView.setText("Time: " + appointment.get("time"));
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView barberNameTextView, dateTextView, timeTextView;

        public AppointmentViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            barberNameTextView = itemView.findViewById(R.id.barber_name);
            dateTextView = itemView.findViewById(R.id.appointment_date);
            timeTextView = itemView.findViewById(R.id.appointment_time);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
