package com.hkim00.moves;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hkim00.moves.fragments.HistoryFragment;
import com.hkim00.moves.fragments.HomeFragment;
import com.hkim00.moves.fragments.PastTripsFragment;
import com.hkim00.moves.fragments.ProfileFragment;
import com.hkim00.moves.util.UncaughtExceptionHandler;
import com.hkim00.moves.fragments.SearchFragment;
import com.loopj.android.http.AsyncHttpClient;

public class HomeActivity extends AppCompatActivity {

    private final static String TAG = "HomeActivity";
    private final static int HISTORY_TAG = 0;
    private final static int SEARCH_TAG = 1;
    private final static int HOME_TAG = 2;
    private final static int PROFILE_TAG = 2;

    public static int screenWidth;
    public static AsyncHttpClient client;
    public static AsyncHttpClient clientTM;

    public static FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigation;

    private int currentFrag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(this));
        setContentView(R.layout.activity_home);

        client = new AsyncHttpClient();
        clientTM = new AsyncHttpClient();

        fragmentManager = getSupportFragmentManager();
        bottomNavigation = findViewById(R.id.bottom_navigation);

        setupRightActionBar();

        getScreenWidth();

        setupNavBar();
    }

    private void setupClearActionBar() {
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_grey)));

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        getSupportActionBar().setElevation(2);
    }

    private void setupNavBar() {
        bottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {
            FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new HomeFragment();


            switch (menuItem.getItemId()) {
                case R.id.action_history:
                    setupClearActionBar();
                    fragment = new HistoryFragment();

                    if (currentFrag != HISTORY_TAG) {
                        fts.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                    }

                    currentFrag = HISTORY_TAG;
                    break;

                case R.id.action_past_trips:
                    setupAddTrip();
                    fragment = new PastTripsFragment();
                    if (currentFrag != SEARCH_TAG) {
                        if (currentFrag < SEARCH_TAG) {
                            fts.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                        } else {
                            fts.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                        }
                    }

                    currentFrag = SEARCH_TAG;
                    break;

                case R.id.action_home:
                    setupRightActionBar();
                    fragment = new HomeFragment();

                    if (currentFrag != HOME_TAG) {
                        if (currentFrag < HOME_TAG) {
                            fts.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                        } else {
                            fts.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                        }
                    }

                    currentFrag = HOME_TAG;
                    break;

                case R.id.action_profile:
                    setupClearActionBar();
                    fragment = new ProfileFragment();

                    if (currentFrag != PROFILE_TAG) {
                        fts.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    }

                    currentFrag = PROFILE_TAG;
                default:
                    break;
            }

            fts.replace(R.id.flContainer, fragment, "fragment");
            fts.commit();

            return true;
        });

        bottomNavigation.setSelectedItemId(R.id.action_home);
    }


    private void setupRightActionBar() {
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_grey)));

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_rt);
        getSupportActionBar().setElevation(2);

        Button btnRight = findViewById(R.id.btnRight);
        btnRight.setOnClickListener(view -> goToSearchActivity());
    }

    private void setupAddTrip() {
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_grey)));

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_at);
        getSupportActionBar().setElevation(2);

        Button btnRight = findViewById(R.id.btnRight);
        btnRight.setOnClickListener(view -> goToNewTrip());
    }
    private void goToSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    private void goToNewTrip() {
        Intent intent = new Intent(this, TripActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    private void getScreenWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

}
