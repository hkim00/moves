package com.hkim00.moves;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.hkim00.moves.util.ExceptionHandler;

public class ShowErrorActivity extends Activity {

    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_main);

        error = findViewById(R.id.error);
        error.setText(getIntent().getStringExtra("error"));
    }
}