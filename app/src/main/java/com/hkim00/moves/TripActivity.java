package com.hkim00.moves;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.hkim00.moves.models.UserLocation;
import com.hkim00.moves.util.StatusCodeHandler;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class TripActivity extends AppCompatActivity {

    public final static String TAG = "TripActivity";
    public static final int LOCATION_REQUEST_CODE = 20;

    private EditText etTrip;
    private TextView tvLocation;
    private Button btnLocation;
    private TextView tvCalendar;
    private Button btnCalendar;

    private UserLocation location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        getViewIds();

        setupButtons();

        setupView();
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }


    private void getViewIds() {
        etTrip = findViewById(R.id.etTripName);
        tvLocation = findViewById(R.id.tvLocation);
        btnLocation = findViewById(R.id.btnLocation);
        tvCalendar = findViewById(R.id.tvCalendar);
        btnCalendar = findViewById(R.id.btnCalendar);
    }


    private void setupButtons() {
        btnLocation.setOnClickListener(view -> goToLocationActivity());

        btnCalendar.setOnClickListener(view -> goToCalendarActivity());
    }


    private void setupView() {
        tvLocation.setTextColor(getResources().getColor(R.color.selected_blue));
        tvCalendar.setTextColor(getResources().getColor(R.color.selected_blue));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == LOCATION_REQUEST_CODE ) {
            checkForCurrentLocation();
        }
    }


    private void checkForCurrentLocation() {
        location = UserLocation.getCurrentTripLocation(this);

        if (location.lat.equals("0.0") && location.name.equals("")) {
            tvLocation.setText("Where we going?");
            tvLocation.setTextColor(getResources().getColor(R.color.selected_blue));
        } else {
            tvLocation.setText(location.name);
            tvLocation.setTextColor(getResources().getColor(R.color.black));
        }
    }


    private void goToLocationActivity() {
        Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
        intent.putExtra("isTrip", true);
        startActivityForResult(intent, LOCATION_REQUEST_CODE);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    private void goToCalendarActivity() {
        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
}
