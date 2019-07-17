package com.hkim00.moves;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.hkim00.moves.QuestionnaireActivity;
import com.hkim00.moves.R;
import com.hkim00.moves.SignUpActivity;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import static com.parse.Parse.getApplicationContext;

public class Intro1Activity extends AppCompatActivity {
    private TextView tvWelcome;
    private EditText etLocation;
    private EditText etAge;
    private EditText etGender;
    private Button btnNext;

    public SignUpActivity signupActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro1);
        tvWelcome = findViewById(R.id.welcome_tv) ;
        etLocation = findViewById(R.id.location_et);
        etAge = findViewById(R.id.age_et);
        etGender = findViewById(R.id.gender_et);
        btnNext = findViewById(R.id.next_btn);

//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            Object obj = extras.get("user");
//
//        }

        btnNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ParseUser currUser = ParseUser.getCurrentUser();
                // TODO: abstract out "put" commands
                currUser.saveInBackground();
                // TODO: figure out how to store location for a ParseUser object
                currUser.put("location", etLocation.getText().toString());
                currUser.put("age", Integer.parseInt(etAge.getText().toString()));
                // TODO: checkboxes for gender?
                currUser.put("gender", etGender.getText().toString());

                ;
            }
        });

    }
}
