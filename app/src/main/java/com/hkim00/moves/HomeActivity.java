package com.hkim00.moves;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    String time;
    String numberOfPeople;
    String radius;
    String price;

    TextView tvLocation;
    Button btnTime;
    Button btnPeople;
    Button btnPlace;
    Button btnPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvLocation = findViewById(R.id.tvLocation);
        btnTime = findViewById(R.id.btnTime);
        btnPeople = findViewById(R.id.btnPeople);
        btnPlace = findViewById(R.id.btnPlace);
        btnPrice = findViewById(R.id.btnPrice);

    }



}
