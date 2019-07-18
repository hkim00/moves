package com.hkim00.moves;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;

public class Intro1Activity extends AppCompatActivity {
    private TextView tvWelcome;
    private EditText etLocation;
    private EditText etAge;
    private EditText etGender;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro1);
        tvWelcome = findViewById(R.id.welcome_tv) ;
        etLocation = findViewById(R.id.location_et);
        etAge = findViewById(R.id.age_et);
        etGender = findViewById(R.id.gender_et);
        btnNext = findViewById(R.id.next_btn);

        btnNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ParseUser currUser = ParseUser.getCurrentUser();
                // TODO: abstract out "put" commands

                // TODO: figure out how to store location for a ParseUser object
                currUser.put("location", etLocation.getText().toString());
                currUser.put("age", Integer.parseInt(etAge.getText().toString()));
                // TODO: checkboxes for gender?
                currUser.put("gender", etGender.getText().toString());
                currUser.saveInBackground();

                // switch to fragments?
                Intent intent = new Intent(Intro1Activity.this, Intro2Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
