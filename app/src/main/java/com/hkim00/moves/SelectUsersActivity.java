package com.hkim00.moves;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hkim00.moves.adapters.UserAdapter;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.INVISIBLE;

public class SelectUsersActivity extends AppCompatActivity {

    private final static String TAG = "SelectUsersActivity";

    private RecyclerView rvUsers;
    private UserAdapter adapter;
    private List<ParseUser> users;

    private TextView tvNoFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_users);

        setupActionBar();

        setupRecyclerView();

        getFriends();
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


    private void setupRecyclerView() {
        rvUsers = findViewById(R.id.rvUsers);
        rvUsers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        users = new ArrayList<>();

        adapter = new UserAdapter(SelectUsersActivity.this, users);
        rvUsers.setAdapter(adapter);
    }


    private void getFriends() {
        tvNoFriends = findViewById(R.id.tvNoFriends);
        tvNoFriends.setVisibility(INVISIBLE);
        ParseQuery<ParseObject> friendQuery = ParseQuery.or(getSenderOrReceiverQueries());
        friendQuery.include("receiver");
        friendQuery.include("sender");
        friendQuery.addDescendingOrder("createdAt");
        friendQuery.findInBackground(((objects, e) -> {
            if (e == null) {
                if (objects.size() == 0) {
                    tvNoFriends.setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < objects.size(); i++) {
                    ParseUser sender = objects.get(i).getParseUser("sender");
                    ParseUser receiver = objects.get(i).getParseUser("receiver");

                    if (receiver == ParseUser.getCurrentUser() && !checkIfInSelectedFriends(sender)) {
                        users.add(sender);
                    } else if (!checkIfInSelectedFriends(receiver)) {
                        users.add(receiver);
                    }
                }

                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getApplicationContext(), "error getting friends", Toast.LENGTH_SHORT).show();
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        }));
    }

    private List<ParseQuery<ParseObject>> getSenderOrReceiverQueries() {
        ParseQuery<ParseObject> senderQuery = ParseQuery.getQuery("Friend");
        senderQuery.whereEqualTo("sender", ParseUser.getCurrentUser());
        senderQuery.whereEqualTo("isPending", false);

        ParseQuery<ParseObject> receiverQuery = ParseQuery.getQuery("Friend");
        receiverQuery.whereEqualTo("receiver", ParseUser.getCurrentUser());
        receiverQuery.whereEqualTo("isPending", false);

        List<ParseQuery<ParseObject>> friendQueries = new ArrayList<>();
        friendQueries.add(senderQuery);
        friendQueries.add(receiverQuery);

        return friendQueries;
    }


    /*
     * function to see if user is already selected. Need this because .contains on a list of ParseUsers does not work
     */
    private boolean checkIfInSelectedFriends(ParseUser user) {
        List<ParseUser> selectedFriends = TripActivity.selectedFriends;

        for (int i = 0; i < selectedFriends.size(); i++) {
            ParseUser friend = selectedFriends.get(i);
            if (friend.getObjectId().equals(user.getObjectId())) {
                return true;
            }
        }

        return false;
    }
}
