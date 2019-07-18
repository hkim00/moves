package com.hkim00.moves;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.models.CatButtonsAdapter;

import java.util.ArrayList;
import java.util.List;

public class Intro2Activity extends AppCompatActivity {
    private TextView tvInstructions;
    private Button btnNext;

    List<ImageButton> mImageButtons;
    public RecyclerView rvCategories;
    protected CatButtonsAdapter adapter;

    public SignUpActivity signupActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro1);

        tvInstructions = findViewById(R.id.instructions_tv) ;
        rvCategories = findViewById(R.id.cat_rv);

        btnNext = findViewById(R.id.next_btn);

        rvCategories.setLayoutManager(new GridLayoutManager(this, 2));
        mImageButtons = new ArrayList<>();
        adapter = new CatButtonsAdapter(Intro2Activity.this, mImageButtons);

        btnNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO: make sure HomeActivity is integrated properly
                Intent intent = new Intent(Intro2Activity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
