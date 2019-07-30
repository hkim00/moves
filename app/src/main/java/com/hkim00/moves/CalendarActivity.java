package com.hkim00.moves;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;

import org.parceler.Parcels;

import java.text.DateFormatSymbols;
import java.util.Date;
import java.util.List;

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

        setupCalendar();

        setupSave();
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
        calendarView.setOnRangeSelectedListener(new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull List<CalendarDay> list) {
                String[] months = new DateFormatSymbols().getShortMonths();

                tvDates.setText(months[list.get(0).getMonth() - 1] + " " + list.get(0).getDay() + " - " + months[list.get(list.size() - 1).getMonth() - 1] + " " + list.get(list.size() - 1).getDay());

                btnSave.setBackgroundColor(getResources().getColor(R.color.selected_blue));
            }
        });

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
                if (!b) {
                    tvDates.setText("");
                    btnSave.setBackgroundColor(getResources().getColor(R.color.light_grey));
                }
            }
        });
    }


    private void setupSave() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (calendarView.getSelectedDates().size() > 0) {
                    TripActivity.dates = calendarView.getSelectedDates();
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    onBackPressed();
                } else {
                    Toast.makeText(getApplicationContext(), "Please select your trip dates", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
