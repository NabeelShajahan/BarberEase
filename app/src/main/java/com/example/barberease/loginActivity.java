package com.example.barberease;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class loginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.login_button);
        TextView forgotPasswordText = findViewById(R.id.forgot_password);
        TextView registerNewUserText = findViewById(R.id.register_new_user);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle login logic here
                // For now, just navigate to the main activity
                Intent intent = new Intent(loginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Forgot Password activity
                Intent intent = new Intent(loginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        registerNewUserText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Register activity
                Intent intent = new Intent(loginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
