package com.hkim00.moves;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.hkim00.moves.adapters.UserAdapter;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SelectUsersActivity extends AppCompatActivity {

    private final static String TAG = "SelectUsersActivity";

    private RecyclerView rvUsers;
    private UserAdapter adapter;
    private List<ParseUser> users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_users);

        setupRecyclerView();

        getFriends();
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }


    private void setupRecyclerView() {
        rvUsers = findViewById(R.id.rvUsers);
        rvUsers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        users = new ArrayList<>();

        adapter = new UserAdapter(SelectUsersActivity.this, users);
        rvUsers.setAdapter(adapter);
    }


    private void getFriends() {
        ParseQuery<ParseObject> friendQuery = ParseQuery.or(getSenderOrReceiverQueries());
        friendQuery.include("receiver");
        friendQuery.include("sender");
        friendQuery.findInBackground(((objects, e) -> {
            if (e == null) {
                for (int i = 0; i < objects.size(); i++) {
                    ParseUser sender = objects.get(i).getParseUser("sender");
                    ParseUser receiver = objects.get(i).getParseUser("receiver");

                    if (receiver.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                        users.add(sender);
                    } else {
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

}
