package com.hkim00.moves;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private Button btnGetSarted;
    private EditText etUsername;
    private EditText etPassword;
    public ParseUser user = new ParseUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnGetSarted = findViewById(R.id.btnGetStarted);
        etUsername = findViewById(R.id.etUsername2);
        etPassword = findViewById(R.id.etPassword2);

        btnGetSarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setUsername(etUsername.getText().toString());
                user.setPassword(etPassword.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("SignUpActivity", "Sign Up Worked!");
                            Intent intent = new Intent(SignUpActivity.this, Intro1Activity.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                            finish();
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
