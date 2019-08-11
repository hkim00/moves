package com.hkim00.moves;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hkim00.moves.adapters.MoveAdapter;
import com.hkim00.moves.adapters.ProfileAdapter;
import com.hkim00.moves.fragments.ProfileFragment;
import com.hkim00.moves.models.Move;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private final static String TAG = "ProfileActivity";

    private RecyclerView rvProfileFragment;
    private static ProfileAdapter movesAdapter;

    private ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

        setupView();

        setupActionBar();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    private void setupActionBar() {
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_lt);
        getSupportActionBar().setElevation(2);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_grey)));

        Button btnLeft = findViewById(R.id.btnLeft);
        btnLeft.setOnClickListener(view -> onBackPressed());
    }

    private void setupView() {
        rvProfileFragment = findViewById(R.id.rvMoves);
        rvProfileFragment.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        movesAdapter = new ProfileAdapter(getApplicationContext(), new ArrayList<>(), user);
        rvProfileFragment.setAdapter(movesAdapter);
    }
}


