package com.example.barberease;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, confirmPasswordEditText, fullNameEditText, phoneEditText;
    private Button registerButton;
    private TextView loginLink;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        fullNameEditText = findViewById(R.id.fullname);
        phoneEditText = findViewById(R.id.phone);
        registerButton = findViewById(R.id.register_button);
        loginLink = findViewById(R.id.login_link);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setupTextWatchers();

        registerButton.setOnClickListener(v -> registerUser());

        loginLink.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this, loginActivity.class)));
    }

    private void setupTextWatchers() {
        emailEditText.addTextChangedListener(new ValidationTextWatcher(emailEditText));
        passwordEditText.addTextChangedListener(new ValidationTextWatcher(passwordEditText));
        confirmPasswordEditText.addTextChangedListener(new ValidationTextWatcher(confirmPasswordEditText));
        fullNameEditText.addTextChangedListener(new ValidationTextWatcher(fullNameEditText));
        phoneEditText.addTextChangedListener(new ValidationTextWatcher(phoneEditText));
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String fullName = fullNameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        if (!validateInputs(email, password, confirmPassword, fullName, phone)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            saveUserToDatabase(user.getUid(), fullName, phone);
                        } else {
                            showToast("Failed to register user.");
                        }
                    } else {
                        showToast("Authentication failed.");
                        Log.w("RegisterActivity", "createUserWithEmail:failure", task.getException());
                    }
                });
    }

    private boolean validateInputs(String email, String password, String confirmPassword, String fullName, String phone) {
        boolean isValid = true;

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required.");
            isValid = false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required.");
            isValid = false;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordEditText.setError("Confirm Password is required.");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match.");
            isValid = false;
        }

        if (TextUtils.isEmpty(fullName)) {
            fullNameEditText.setError("Full Name is required.");
            isValid = false;
        }

        if (TextUtils.isEmpty(phone)) {
            phoneEditText.setError("Phone number is required.");
            isValid = false;
        }

        return isValid;
    }

    private void saveUserToDatabase(String userId, String fullName, String phone) {
        User user = new User(fullName, phone);

        mDatabase.child("users").child(userId).setValue(user)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        showToast("Successfully Registered");
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    } else {
                        showToast("Failed to save user data.");
                        Log.w("RegisterActivity", "saveUserToDatabase:failure", task.getException());
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private class ValidationTextWatcher implements TextWatcher {
        private final View view;

        private ValidationTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // No action needed
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            switch (view.getId()) {
                case R.id.email:
                    validateEmail();
                    break;
                case R.id.password:
                    validatePassword();
                    break;
                case R.id.confirm_password:
                    validateConfirmPassword();
                    break;
                case R.id.fullname:
                    validateFullName();
                    break;
                case R.id.phone:
                    validatePhone();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            // No action needed
        }

        private void validateEmail() {
            String email = emailEditText.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                emailEditText.setError("Email is required.");
            }
        }

        private void validatePassword() {
            String password = passwordEditText.getText().toString().trim();
            if (TextUtils.isEmpty(password)) {
                passwordEditText.setError("Password is required.");
            }
        }

        private void validateConfirmPassword() {
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();
            if (!password.equals(confirmPassword)) {
                confirmPasswordEditText.setError("Passwords do not match.");
            }
        }

        private void validateFullName() {
            String fullName = fullNameEditText.getText().toString().trim();
            if (TextUtils.isEmpty(fullName)) {
                fullNameEditText.setError("Full Name is required.");
            }
        }

        private void validatePhone() {
            String phone = phoneEditText.getText().toString().trim();
            if (TextUtils.isEmpty(phone)) {
                phoneEditText.setError("Phone number is required.");
            }
        }
    }

    public static class User {
        public String fullName;
        public String phone;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String fullName, String phone) {
            this.fullName = fullName;
            this.phone = phone;
        }
    }
}
