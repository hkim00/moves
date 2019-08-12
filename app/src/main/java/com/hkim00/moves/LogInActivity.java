package com.hkim00.moves;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        getSupportActionBar().hide();

        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(this));
        setContentView(R.layout.activity_login);

        checkForCurrentUser();

        getViewIds();

        setupButtons();
    }


    private void getViewIds() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogIn = findViewById(R.id.btnlogIn);
        btnSignUp = findViewById(R.id.btnSignUp);
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
            final String username = etUsername.getText().toString().toLowerCase();
            final String password = etPassword.getText().toString();

            login(username, password);
        });

        btnSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        });
    }

    private void login (String username, String password){
        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (e == null) {
                Log.d(TAG, "Login successful!");
                final Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else {
                Log.e(TAG, e.getMessage());
                if (username.equals("") || password.equals("")) {
                    Toast.makeText(this, "Please type in a username/password!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
