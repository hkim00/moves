package com.hkim00.moves;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.hkim00.moves.util.UncaughtExceptionHandler;
import com.parse.ParseUser;

public class SignUpActivity extends AppCompatActivity {

    private String TAG = "SignUpActivity";

    private Button btnGetSarted;
    private EditText etUsername;
    private EditText etPassword;
    public ParseUser user = new ParseUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(this));
        setContentView(R.layout.activity_signup);

        getViewIds();

        setupButtons();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    private void getViewIds() {
        btnGetSarted = findViewById(R.id.btnGetStarted);
        etUsername = findViewById(R.id.etUsername2);
        etPassword = findViewById(R.id.etPassword2);
    }


    private void setupButtons() {
        btnGetSarted.setOnClickListener(view -> {
            user.setUsername(etUsername.getText().toString().toLowerCase());
            user.setPassword(etPassword.getText().toString());

            user.signUpInBackground(e -> {
                if (e == null) {
                    Log.i(TAG, "Sign Up Worked!");
                    Intent intent = new Intent(SignUpActivity.this, Intro2Activity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e(TAG, e.getMessage());
                }
            });
        });
    }
}
