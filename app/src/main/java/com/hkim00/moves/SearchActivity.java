package com.hkim00.moves;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hkim00.moves.adapters.MoveAdapter;
import com.hkim00.moves.adapters.UserAdapter;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.util.ParseUtil;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private final static String TAG = "SearchActivity";

    private TextView tvType;
    private EditText etSearch;
    private Button btnType;
    private RecyclerView rvSearchResults;

    private List<Move> moveResults;
    private List<ParseUser> userResults;

    private MoveAdapter moveAdapter;
    private UserAdapter userAdapter;

    private boolean isTimerRunning;
    private String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupActionBar();

        getViewIds();

        setupRecyclerView();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }


    private void getViewIds() {
        tvType = findViewById(R.id.tvType);
        etSearch = findViewById(R.id.etSearch);
        btnType = findViewById(R.id.btnType);
        rvSearchResults = findViewById(R.id.rvSearchResults);
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


    private void setupRecyclerView() {
        etSearch.addTextChangedListener(charTextWatcher);

        type = "users";
        isTimerRunning = false;

        moveResults = new ArrayList<>();
        userResults = new ArrayList<>();

        moveAdapter = new MoveAdapter(this, moveResults);
        userAdapter = new UserAdapter(this, userResults);

        rvSearchResults.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvSearchResults.setAdapter(userAdapter);
    }


    private void searchTextUpdated() {
        if (etSearch.getText().toString().toLowerCase().trim().equals("")) {
            checkIfNeedToClearRecyclerView();
            return;
        }

        if (!isTimerRunning) {
            isTimerRunning = true;
            startTimer();
        }
    }

    private void startTimer() {
        new CountDownTimer(500, 500) { //0.5 seconds

            public void onTick(long millisUntilFinished) { }

            public void onFinish() {


                if (type.equals("users")) {
                    findUsers();
                } else {
                    //findMoves();
                }
                isTimerRunning = false;
            }
        }.start();
    }

    private void findUsers() {
        ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("_User");
        userQuery.whereContains("username", etSearch.getText().toString().toLowerCase().trim());
        userQuery.orderByDescending("createdAt");

        userQuery.findInBackground((objects, e) -> {
            if (e == null) {
                List<ParseUser> users = new ArrayList<>();

                for (int i = 0; i < objects.size(); i++) {
                    if (!objects.get(i).getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                        users.add((ParseUser) objects.get(i));
                    }
                }

                updateUsers(users);

            } else {
                Log.e(TAG, "Error finding users with usernames' containing: " + etSearch.getText().toString().toLowerCase().trim());
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error finding users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkIfNeedToClearRecyclerView() {
        if (etSearch.getText().toString().toLowerCase().trim().equals("")) {
            if (type.equals("users")) {
                userResults.clear();
                userAdapter.notifyDataSetChanged();
            } else {
                moveResults.clear();
                moveAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateUsers(List<ParseUser> users) {
        userResults.clear();
        userResults.addAll(users);

        rvSearchResults.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();

        checkIfNeedToClearRecyclerView();
    }

    private final TextWatcher charTextWatcher= new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        public void onTextChanged(CharSequence s, int start, int before, int count) { searchTextUpdated(); }

        @Override
        public void afterTextChanged(Editable s) { }
    };
}
