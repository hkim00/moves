package com.hkim00.moves;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hkim00.moves.adapters.MoveAdapter;
import com.hkim00.moves.models.Move;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private Button btnSaved;
    private Button btnFavorites;
    private Button btnLogout;
    private RecyclerView rvFavorites;
    private RecyclerView rvSaved;

    private TextView tvName;
    private TextView tvLocation;
    private TextView tvGender;
    private TextView tvAge;

    private MoveAdapter Faveadapter;
    private MoveAdapter Saveadapter;
    private List<Move> rFaveList;
    private List<Move> rSaveList;

    private ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        findViewIds();

        setupButtons();

        setupRecyclerView();

        fillUserInfo();
    }

    private void fillUserInfo() {
        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

        tvName.setText(user.getUsername());
        tvLocation.setText("Your location: " + user.getString("location"));
        tvGender.setText("Gender: " + user.getString("gender"));
        tvAge.setText("Age: " + user.getInt("age"));
    }

    private void findViewIds() {
        btnSaved = findViewById(R.id.btnSave);
        btnFavorites = findViewById(R.id.btnFavorite);
        btnLogout = findViewById(R.id.btnLogout);
        rvFavorites = findViewById(R.id.rvFavorites);
        rvSaved = findViewById(R.id.rvSaved);

        tvName = findViewById(R.id.tvName);
        tvLocation = findViewById(R.id.tvLocation);
        tvGender = findViewById(R.id.tvGender);
        tvAge = findViewById(R.id.tvAge);

        btnLogout.setVisibility(View.INVISIBLE);
    }

    private void setupButtons() {
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
    }

    private void setupRecyclerView() {
        //set grid view --> if bug, may be because of context retrieval method
        rvFavorites.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        rvSaved.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

        //TODO: need to account for two different adapters (Events vs. Restaurant)
        rFaveList = new ArrayList<>();
        Faveadapter = new MoveAdapter(getApplicationContext(), rFaveList);
        rvFavorites.setAdapter(Faveadapter);

        rSaveList = new ArrayList<>();
        Saveadapter = new MoveAdapter(getApplicationContext(), rSaveList);
        rvSaved.setAdapter(Saveadapter);
    }
}


