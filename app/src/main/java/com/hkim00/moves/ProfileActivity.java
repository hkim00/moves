package com.hkim00.moves;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hkim00.moves.R;
import com.hkim00.moves.adapters.ProfileAdapter;
import com.hkim00.moves.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private Button btnSaved;
    private Button btnFavorites;
    private RecyclerView rvFavorites;
    private RecyclerView rvSaved;
    //TODO create fragments instead of activities(??)
    //TODO create lists for saved and favorites moves
    private ProfileAdapter Faveadapter;
    private ProfileAdapter Saveadapter;
    private List<Restaurant> rFaveList;
    private List<Restaurant> rSaveList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnSaved =  findViewById(R.id.btnSaved);
        btnFavorites = findViewById(R.id.btnFavorites);
        rvFavorites = findViewById(R.id.rvFavorites);
        rvSaved = findViewById(R.id.rvSaved);

        //set grid view --> if bug, may be because of context retrieval method
        rvFavorites.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        rvSaved.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

        btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show favorites recycler view
                rvSaved.setVisibility(View.INVISIBLE);
                rvFavorites.setVisibility(View.VISIBLE);
            }
        });

        btnSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show saved recycler view
                rvFavorites.setVisibility(View.INVISIBLE);
                rvSaved.setVisibility(View.VISIBLE);
            }
        });

       rFaveList = new ArrayList<>();
       Faveadapter = new ProfileAdapter(getApplicationContext(), rFaveList);
       rvFavorites.setAdapter(Faveadapter);

       rSaveList = new ArrayList<>();
       Saveadapter = new ProfileAdapter(getApplicationContext(), rSaveList);
       rvSaved.setAdapter(Saveadapter);

    }
}
