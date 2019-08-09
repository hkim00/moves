package com.hkim00.moves;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hkim00.moves.models.Trip;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class CalendarActivity extends AppCompatActivity {

    private final static String TAG = "TripActivity";

    private MaterialCalendarView calendarView;
    private TextView tvDates;
    private Button btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        getViewIds();

        setupActionBar();

        setupCalendar();

        setupSave();
    }

    private void setupActionBar() {
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_grey)));

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_lt);
        getSupportActionBar().setElevation(2);

        Button btnLeft = findViewById(R.id.btnLeft);
        btnLeft.setOnClickListener(view -> onBackPressed());
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }


    private void getViewIds() {
        calendarView = findViewById(R.id.calendarView);
        tvDates = findViewById(R.id.tvDates);
        btnSave = findViewById(R.id.btnSave);
    }


    private void setupCalendar() {
        calendarView.setOnRangeSelectedListener((materialCalendarView, list) -> {
            tvDates.setText(Trip.getDateRangeString(list.get(0), list.get(list.size() - 1)));
            btnSave.setBackgroundColor(getResources().getColor(R.color.selected_blue));
        });

        calendarView.setOnDateChangedListener((materialCalendarView, calendarDay, b) -> {
            if (!b) {
                tvDates.setText("");
                btnSave.setBackgroundColor(getResources().getColor(R.color.light_grey));
            }
        });
    }


    private void setupSave() {
        btnSave.setOnClickListener(view -> {
            if (calendarView.getSelectedDates().size() > 0) {
                TripActivity.dates = calendarView.getSelectedDates();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                onBackPressed();
            } else {
                Toast.makeText(getApplicationContext(), "Please select your trip dates", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
