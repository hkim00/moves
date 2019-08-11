package com.hkim00.moves;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.adapters.CatButtonsAdapter;
import com.hkim00.moves.models.CategoryButton;
import com.hkim00.moves.util.MoveCategoriesHelper;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Intro2Activity extends AppCompatActivity {
    private Button btnDone;

    List<CategoryButton> mCatButtons;
    List<String> mCategories;
    RecyclerView rvCategories;
    CatButtonsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro2);

        setupActionBar();

        getViewIds();

        setupRecyclerView();

        setupButtons();
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

    private void getViewIds() {
        rvCategories = findViewById(R.id.cat_rv);
        btnDone = findViewById(R.id.done_btn);
    }


    private void setupRecyclerView() {
        MoveCategoriesHelper helper = new MoveCategoriesHelper();

        mCatButtons = new ArrayList<>();
        mCategories = new ArrayList<>();
        adapter = new CatButtonsAdapter(getApplicationContext(), mCatButtons);

        rvCategories.setAdapter(adapter);
        rvCategories.setLayoutManager(new GridLayoutManager(this, 2));

        // populates a category button with cuisines from array declared in MoveCategoriesHelper
        helper.CategoryHelper(helper.foodCategoriesList);

        mCatButtons.addAll(helper.mCategories);
        adapter.notifyDataSetChanged();
    }


    private void setupButtons() {
        btnDone.setOnClickListener(v -> {
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
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}