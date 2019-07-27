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

        btnGetSarted = findViewById(R.id.btnGetStarted);
        etUsername = findViewById(R.id.etUsername2);
        etPassword = findViewById(R.id.etPassword2);

        btnGetSarted.setOnClickListener(view -> {
            user.setUsername(etUsername.getText().toString().toLowerCase());
            user.setPassword(etPassword.getText().toString());

            user.signUpInBackground(e -> {
                if (e == null) {
                    Log.i("SignUpActivity", "Sign Up Worked!");
                    Intent intent = new Intent(SignUpActivity.this, Intro1Activity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e(TAG, e.getMessage());
                }
            });
        });
    }
}
