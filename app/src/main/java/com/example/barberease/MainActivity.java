package com.example.barberease;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private RecyclerView recommendedShopsRecyclerView, recommendedBarbersRecyclerView;
    private TextView noShopsTextView, noBarbersTextView;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;
    private List<BarberShop> shopList;
    private List<Barber> barberList;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        recommendedShopsRecyclerView = findViewById(R.id.recommended_shops_recycler_view);
        recommendedBarbersRecyclerView = findViewById(R.id.recommended_barbers_recycler_view);
        noShopsTextView = findViewById(R.id.no_shops_text_view);
        noBarbersTextView = findViewById(R.id.no_barbers_text_view);
        progressBar = findViewById(R.id.progressBar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        setupRecyclerViews();
        fetchDataFromDatabase();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    // Stay in MainActivity
                    return true;
                } else if (itemId == R.id.nav_calendar) {
                    Intent calendarIntent = new Intent(MainActivity.this, AppointmentsActivity.class);
                    startActivity(calendarIntent);
                    return true;
                } else if (itemId == R.id.nav_list) {
                    Intent listIntent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(listIntent);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.d(TAG, "No current user, redirecting to LoginActivity");
            Intent intent = new Intent(MainActivity.this, loginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Log.d(TAG, "Current user: " + currentUser.getEmail());
        }
    }

    private void setupRecyclerViews() {
        recommendedShopsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recommendedBarbersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void fetchDataFromDatabase() {
        progressBar.setVisibility(View.VISIBLE);

        databaseReference.child("shops").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shopList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BarberShop shop = dataSnapshot.getValue(BarberShop.class);
                    shopList.add(shop);
                }
                if (shopList.isEmpty()) {
                    noShopsTextView.setVisibility(View.VISIBLE);
                } else {
                    noShopsTextView.setVisibility(View.GONE);
                    ImageAdapter shopAdapter = new ImageAdapter((List<Object>)(List<?>) shopList, MainActivity.this);
                    recommendedShopsRecyclerView.setAdapter(shopAdapter);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

        databaseReference.child("barbers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                barberList = new ArrayList<>();
                barberList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Barber barber = dataSnapshot.getValue(Barber.class);
                    barberList.add(barber);
                }
                if (barberList.isEmpty()) {
                    noBarbersTextView.setVisibility(View.VISIBLE);
                } else {
                    noBarbersTextView.setVisibility(View.GONE);
                    BarberAdapter barberAdapter = new BarberAdapter(barberList, MainActivity.this);
                    recommendedBarbersRecyclerView.setAdapter(barberAdapter);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}
