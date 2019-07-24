package com.hkim00.moves;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hkim00.moves.models.UserLocation;
import com.parse.ParseUser;

public class Intro1Activity extends AppCompatActivity {
    public static final int LOCATION_REQUEST_CODE = 20;

    private TextView tvWelcome;
    private TextView tvLocation;
    private Button btnLocation;
    private EditText etAge;
    private EditText etGender;
    private Button btnNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro1);

        tvWelcome = findViewById(R.id.welcome_tv) ;
        tvLocation = findViewById(R.id.tvLocation);
        btnLocation = findViewById(R.id.btnLocation);
        etAge = findViewById(R.id.age_et);
        etGender = findViewById(R.id.gender_et);
        btnNext = findViewById(R.id.next_btn);

        setupButtons();
    }


    private void setupButtons() {
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLocation();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                goToCategories();
            }
        });
    }


    private void goToLocation() {
        Intent intent = new Intent(Intro1Activity.this, LocationActivity.class);
        startActivityForResult(intent, LOCATION_REQUEST_CODE );
    }


    private void goToCategories() {
        ParseUser currUser = ParseUser.getCurrentUser();
        // TODO: abstract out "put" commands

        // TODO: figure out how to store location for a ParseUser object
        //currUser.put("location", etLocation.getText().toString());
        currUser.put("age", Integer.parseInt(etAge.getText().toString()));
        // TODO: checkboxes for gender?
        currUser.put("gender", etGender.getText().toString());
        currUser.saveInBackground();

        // switch to fragments?
        Intent intent = new Intent(Intro1Activity.this, Intro2Activity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == LOCATION_REQUEST_CODE ) {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("location", 0); //0 for private mode

            String name = sharedPreferences.getString("name", "");
            String lat = sharedPreferences.getString("lat", "0.0");

            if (lat.equals("0.0") && name.equals("")) {
                tvLocation.setText("Location");
                tvLocation.setTextColor(getResources().getColor(R.color.selected_blue));
            } else {
                tvLocation.setText(name);
                tvLocation.setTextColor(getResources().getColor(R.color.black));
            }
        }
    }
}
