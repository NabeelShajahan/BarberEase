package com.example.barberease;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Navigate to LoginActivity after the splash screen duration
                    Intent intent = new Intent(SplashActivity.this,loginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
    }
}