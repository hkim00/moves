package com.hkim00.moves;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.hkim00.moves.util.UncaughtExceptionHandler;
import com.parse.ParseUser;

public class LogInActivity extends AppCompatActivity {

    private String TAG = "LogInActivity";

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogIn;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(this));

        setContentView(R.layout.activity_login);

        checkForCurrentUser();

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogIn = findViewById(R.id.btnlogIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        setupButtons();
    }


    private void checkForCurrentUser() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setupButtons() {
        btnLogIn.setOnClickListener(view -> {
            final String username = etUsername.getText().toString();
            final String password = etPassword.getText().toString();

            login(username, password);
        });

        btnSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void login (String username, String password){
        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (e == null) {
                Log.d(TAG, "Login successful!");
                final Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

            } else {
                Log.e(TAG, e.getMessage());
            }
        });
    }
}
