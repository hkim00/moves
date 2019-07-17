package com.hkim00.moves.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hkim00.moves.R;

public class Intro1Fragment extends Fragment {
    private TextView tvWelcome;
    private EditText etLocation;
    private EditText etAge;
    private EditText etGender;

    private Button btnNext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_intro1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        super.onViewCreated(view, savedInstanceState);
        tvWelcome = view.findViewById(R.id.welcome_tv) ;
        etLocation = view.findViewById(R.id.location_et);
        etAge = view.findViewById(R.id.age_et);
        etGender = view.findViewById(R.id.gender_et);

    }
}
