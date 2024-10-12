package com.example.barberease;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class BarberAppointmentsAdapter extends RecyclerView.Adapter<BarberAppointmentsAdapter.BarberAppointmentViewHolder> {

    private List<Appointment> appointmentList;

    public BarberAppointmentsAdapter(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public BarberAppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barber_appointment, parent, false);
        return new BarberAppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarberAppointmentViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);

        // Display the customer name if available, otherwise show the customer ID
        String customerDisplay = appointment.getCustomerName() != null && !appointment.getCustomerName().isEmpty()
                ? "Customer: " + appointment.getCustomerName()
                : "Customer ID: " + appointment.getCustomerId();
        holder.customerNameTextView.setText(customerDisplay);

        // Set the appointment details
        holder.appointmentDetailsTextView.setText("Date: " + appointment.getDate() + "\nTime: " + appointment.getTime());

        // Handle visibility and click listeners
        holder.itemView.setOnClickListener(v -> {
            holder.appointmentDetailsTextView.setVisibility(View.VISIBLE);
            holder.confirmButton.setVisibility(View.VISIBLE);
        });

        holder.confirmButton.setOnClickListener(v -> {
            confirmAppointment(holder, appointment);
        });
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    private void confirmAppointment(BarberAppointmentViewHolder holder, Appointment appointment) {
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("appointments")
                .child("barbers").child(appointment.getBarberId()).child("upcoming");

        appointmentsRef.child(appointment.getId()).child("status").setValue("Confirmed")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(holder.itemView.getContext(), "Appointment confirmed.", Toast.LENGTH_SHORT).show();
                    holder.confirmButton.setVisibility(View.GONE);

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(holder.itemView.getContext(), "Failed to confirm appointment.", Toast.LENGTH_SHORT).show();
                });
    }

    static class BarberAppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView customerNameTextView;
        TextView appointmentDetailsTextView;
        Button confirmButton;

        BarberAppointmentViewHolder(View itemView) {
            super(itemView);
            customerNameTextView = itemView.findViewById(R.id.customer_name_text_view);
            appointmentDetailsTextView = itemView.findViewById(R.id.appointment_details_text_view);
            confirmButton = itemView.findViewById(R.id.confirm_appointment_button);

            appointmentDetailsTextView.setVisibility(View.GONE);
            confirmButton.setVisibility(View.GONE);
        }
    }
}
