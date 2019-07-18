package com.hkim00.moves;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.models.CatButtonsAdapter;
import com.hkim00.moves.models.CategoryButton;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Intro2Activity extends AppCompatActivity {
    private TextView tvInstructions;
    private Button btnDone;

     List<CategoryButton> mCatButtons;
     List<String> mCategories;
     RecyclerView rvCategories;
     CatButtonsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro2);

        tvInstructions = findViewById(R.id.instructions_tv) ;
        rvCategories = findViewById(R.id.cat_rv);
        btnDone = findViewById(R.id.done_btn);

        mCatButtons = new ArrayList<>();
        adapter = new CatButtonsAdapter(getApplicationContext(), mCatButtons);

        rvCategories.setAdapter(adapter);
        rvCategories.setLayoutManager(new GridLayoutManager(this, 2));

        // populates a category button with cuisines from array declared in CategoryHelper
        CategoryHelper categoryHelper = new CategoryHelper();

        mCatButtons.addAll(categoryHelper.mCategories);
        adapter.notifyDataSetChanged();

        btnDone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ParseUser currUser = ParseUser.getCurrentUser();
                currUser.saveInBackground();

                Intent intent = new Intent(Intro2Activity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
