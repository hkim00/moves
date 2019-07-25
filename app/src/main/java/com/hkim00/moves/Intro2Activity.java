package com.hkim00.moves;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.adapters.CatButtonsAdapter;
import com.hkim00.moves.util.CategoryHelper;
import com.hkim00.moves.util.MoveCategories;
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

        MoveCategories helper = new MoveCategories();

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
        CategoryHelper categoryHelper = new CategoryHelper(helper.foodCategoriesList);

        mCatButtons.addAll(categoryHelper.mCategories);
        adapter.notifyDataSetChanged();

        mCategories = new ArrayList<>();

        btnDone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // loop through catButtons, put all "preferred" cuisines, add to -PrefList array in Parse
                for (int i = 0; i < mCatButtons.size(); i++) {
                    CategoryButton catButton = mCatButtons.get(i);
                    if (catButton.isPref == true) {
                        mCategories.add(catButton.type);
                    }
                }

                ParseUser currUser = ParseUser.getCurrentUser();
                currUser.put("foodPrefList", mCategories);
                currUser.saveInBackground();

                Intent intent = new Intent(Intro2Activity.this, Intro3Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}