package com.hkim00.moves;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hkim00.moves.fragments.HomeFragment;
import com.hkim00.moves.models.Restaurant;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class HomeActivity extends AppCompatActivity {

    private final static String TAG = "HomeActivity";

    public static int screenWidth;
    public static AsyncHttpClient client;

    FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Places.initialize(getApplicationContext(), getString(R.string.api_key));
        PlacesClient placesClient = Places.createClient(this);

        client = new AsyncHttpClient();

        fragmentManager = getSupportFragmentManager();
        bottomNavigation = findViewById(R.id.bottom_navigation);

        getScreenWidth();
        setupNavBar();
    }


    private void setupNavBar() {
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = new HomeFragment();

                switch (menuItem.getItemId()) {
                    case R.id.action_history:
                        fragment = new HomeFragment();
                        Toast.makeText(HomeActivity.this, "history", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.action_profile:
                        fragment = new HomeFragment();
                        Toast.makeText(HomeActivity.this, "profile", Toast.LENGTH_SHORT).show();
                    default:
                        break;
                }

                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

        bottomNavigation.setSelectedItemId(R.id.action_home);
    }

    private void getScreenWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

}
