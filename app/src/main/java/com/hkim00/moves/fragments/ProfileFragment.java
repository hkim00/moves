package com.hkim00.moves.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.LogInActivity;
import com.hkim00.moves.R;
import com.hkim00.moves.adapters.MoveAdapter;
//import com.hkim00.moves.adapters.RestaurantAdapter;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.Restaurant;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    public final static String TAG = "ProfileFragment";
    ParseUser currUser;

    private Button btnSaved;
    private Button btnFavorites;
    private Button btnLogout;
    private RecyclerView rvFavorites;
    private RecyclerView rvSaved;

    private TextView tvName;
    private TextView tvLocation;
    private TextView tvGender;
    private TextView tvAge;

    //TODO create lists for saved and favorites moves
    private MoveAdapter Faveadapter;
    private MoveAdapter Saveadapter;
    private List<Move> rFaveList;
    private List<Move> rSaveList;

    private MoveAdapter favAdapter;
    private MoveAdapter saveAdapter;

    private List<Move> favList;
    private List<Move> saveList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currUser = ParseUser.getCurrentUser();

        tvName = view.findViewById(R.id.tvName);
        tvLocation = view.findViewById(R.id.tvLocation);
        tvGender = view.findViewById(R.id.tvGender);
        tvAge = view.findViewById(R.id.tvAge);

        btnLogout = view.findViewById(R.id.btnLogout);
        btnSaved =  view.findViewById(R.id.btnSave);
        btnFavorites = view.findViewById(R.id.btnFavorite);
        rvFavorites = view.findViewById(R.id.rvFavorites);
        rvSaved = view.findViewById(R.id.rvSaved);

        tvName.setText(currUser.getUsername());
        tvLocation.setText("Your location: " + currUser.getString("location"));
        tvGender.setText("Gender: " + currUser.getString("gender"));
        tvAge.setText("Age: " + currUser.getInt("age"));
      
        rvFavorites.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSaved.setLayoutManager(new LinearLayoutManager(getContext()));

        rFaveList = new ArrayList<>();
        Faveadapter = new MoveAdapter(getContext(), rFaveList);
        rvFavorites.setAdapter(Faveadapter);

        rSaveList = new ArrayList<>();
        Saveadapter = new MoveAdapter(getContext(), rSaveList);
        rvSaved.setAdapter(Saveadapter);

        setupButtons();
      
        setupRecyclerViews();

        setupButtons();

        rvSaved.setVisibility(View.INVISIBLE);

        getFavoriteRestaurants();
        getSavedRestaurants();
    }

    private void setupRecyclerViews() {
        rvFavorites.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSaved.setLayoutManager(new LinearLayoutManager(getContext()));

        favList = new ArrayList<>();
        favAdapter = new MoveAdapter(getContext(), favList);
        rvFavorites.setAdapter(favAdapter);

        saveList = new ArrayList<>();
        saveAdapter = new MoveAdapter(getContext(), saveList);
        rvSaved.setAdapter(saveAdapter);
    }

    private void getSavedRestaurants() {
        ParseQuery<ParseObject> restaurantQuery = ParseQuery.getQuery("Restaurant");
        restaurantQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        restaurantQuery.whereEqualTo("didSave", true);
        restaurantQuery.orderByDescending("createdAt");

        restaurantQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (int i = 0; i < objects.size(); i++) {
                    if (e == null) {
                        Restaurant restaurant = Restaurant.fromParseObject(objects.get(i));

                        saveList.add(restaurant);
                        saveAdapter.notifyItemInserted(saveList.size() - 1);
                    } else {
                        Log.e(TAG, "Error finding saved restaurants.");
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error saving profile", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void getFavoriteRestaurants() {
        ParseQuery<ParseObject> restaurantQuery = ParseQuery.getQuery("Restaurant");
        restaurantQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        restaurantQuery.whereEqualTo("didFavorite", true);
        restaurantQuery.orderByDescending("createdAt");

        restaurantQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (int i = 0; i < objects.size(); i++) {
                    if (e == null) {
                        Restaurant restaurant = Restaurant.fromParseObject(objects.get(i));

                        favList.add(restaurant);
                        favAdapter.notifyItemInserted(saveList.size() - 1);
                    } else {
                        Log.e(TAG, "Error finding saved restaurants.");
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error saving profile", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void setupButtons() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("location", 0); //0 for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                final Intent intent = new Intent(getContext(), LogInActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

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
}
