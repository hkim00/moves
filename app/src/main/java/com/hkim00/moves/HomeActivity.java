package com.hkim00.moves;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hkim00.moves.fragments.HistoryFragment;
import com.hkim00.moves.fragments.HomeFragment;
import com.hkim00.moves.fragments.ProfileFragment;
import com.loopj.android.http.AsyncHttpClient;

public class HomeActivity extends AppCompatActivity {

    private final static String TAG = "HomeActivity";

    public static int screenWidth;
    public static AsyncHttpClient client;
    public static AsyncHttpClient clientTM;

    FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                Fragment fragment = new HomeFragment();

                switch (menuItem.getItemId()) {
                    case R.id.action_history:
                        fragment = new HistoryFragment();
                        break;
                    case R.id.action_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.action_profile:
                        fragment = new ProfileFragment();
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
