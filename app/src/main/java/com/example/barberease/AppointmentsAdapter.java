package com.example.barberease;

import android.content.Context;
import android.graphics.Color;
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
        String status = appointment.get("status");

        holder.barberNameTextView.setText(appointment.get("barberName"));
        holder.dateTextView.setText("Date: " + appointment.get("date"));
        holder.timeTextView.setText("Time: " + appointment.get("time"));

        if ("Confirmed".equals(status)) {
            holder.statusTextView.setText("Confirmed");
            holder.statusTextView.setTextColor(Color.GREEN); // Green for confirmed
        } else if ("Pending".equals(status)) {
            holder.statusTextView.setText("Pending");
            holder.statusTextView.setTextColor(Color.parseColor("#FFA500")); // Orange for pending
        } else if ("Upcoming".equals(status)) {
            holder.statusTextView.setText("Upcoming");
            holder.statusTextView.setTextColor(Color.RED); // Red for upcoming
        }

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(pos);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView barberNameTextView, dateTextView, timeTextView, statusTextView;

        public AppointmentViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            barberNameTextView = itemView.findViewById(R.id.barber_name);
            dateTextView = itemView.findViewById(R.id.appointment_date);
            timeTextView = itemView.findViewById(R.id.appointment_time);
            statusTextView = itemView.findViewById(R.id.appointment_status);

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
