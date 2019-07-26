package com.hkim00.moves;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hkim00.moves.fragments.HistoryFragment;
import com.hkim00.moves.fragments.HomeFragment;
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

    FragmentManager fragmentManager;
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

        getScreenWidth();
        setupNavBar();
    }

    private void setupNavBar() {
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
                Fragment fragment = new HomeFragment();


                switch (menuItem.getItemId()) {
                    case R.id.action_history:
                        fragment = new HistoryFragment();

                        if (currentFrag != HISTORY_TAG) {
                            fts.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                        }

                        currentFrag = HISTORY_TAG;
                        break;

                    case R.id.action_search:
                        fragment = new SearchFragment();

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
