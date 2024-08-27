package com.example.barberease;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private RecyclerView recommendedBarbersRecyclerView;
    private TextView noBarbersTextView, weatherTextView;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;
    private List<Barber> barberList;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("barbers");

        recommendedBarbersRecyclerView = findViewById(R.id.recommended_barbers_recycler_view);
        noBarbersTextView = findViewById(R.id.no_barbers_text_view);
        weatherTextView = findViewById(R.id.weatherTextView);
        progressBar = findViewById(R.id.progressBar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        fetchWeatherData("Montreal");

        setupRecyclerViews();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
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
        });

        fetchDataFromDatabase();
    }

    private void fetchWeatherData(String cityName) {
        String apiKey = "3f100d26845afee479d24d125b80c106"; // Replace with your OpenWeather API key
        String units = "metric"; // Use "imperial" for Fahrenheit

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherService weatherService = retrofit.create(WeatherService.class);

        Call<WeatherResponse> call = weatherService.getCurrentWeather(cityName, apiKey, units);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();
                    double temp = weatherResponse.getMain().getTemp();
                    String description = weatherResponse.getWeather().get(0).getDescription();
                    String weatherInfo = "Temp: " + temp + "°C, " + description;

                    weatherTextView.setText(weatherInfo);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to get weather data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.d(TAG, "No current user, redirecting to LoginActivity");
            startActivity(new Intent(MainActivity.this, loginActivity.class));
            finish();
        } else {
            Log.d(TAG, "Current user: " + currentUser.getEmail());
        }
    }

    private void setupRecyclerViews() {
        recommendedBarbersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void fetchDataFromDatabase() {
        progressBar.setVisibility(View.VISIBLE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                barberList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Barber barber = dataSnapshot.getValue(Barber.class);
                    if (barber != null) {
                        String userId = dataSnapshot.getKey();
                        barber.setUserId(userId);
                        barberList.add(barber);
                        Log.d(TAG, "Barber added: " + barber.getName());
                    } else {
                        Log.e(TAG, "Barber is null for snapshot: " + dataSnapshot.toString());
                    }
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
                Log.e(TAG, "Database error: " + error.getMessage());
            }
        });
    }
}
