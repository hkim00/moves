package com.hkim00.moves.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.OrientationHelper;

import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.LithoView;
import com.facebook.litho.widget.LinearLayoutInfo;
import com.facebook.litho.widget.RecyclerBinder;
import com.hkim00.moves.specs.SearchComponent;
import com.hkim00.moves.specs.UserItem;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private final static String TAG = "SearchFragment";

    public static Boolean isAddFriend;

    private ComponentContext componentContext;
    private RecyclerBinder recyclerBinder;

    private String searchText;
    private boolean isTimerRunning;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return setupRecycler();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchText = "";
        isTimerRunning = false;
    }


    private View setupRecycler() {
        componentContext = new ComponentContext(getContext());

        recyclerBinder =  new RecyclerBinder.Builder()
                .layoutInfo(new LinearLayoutInfo(getContext(), OrientationHelper.VERTICAL, false))
                .build(componentContext);

        final Component component = SearchComponent.create(componentContext)
                .hint("Search username")
                .binder(recyclerBinder)
                .listener(text -> searchTextUpdated(text))
                .build();

        return LithoView.create(componentContext, component);
    }

    private void searchTextUpdated(String text) {
        searchText = text;

        if (!isTimerRunning) {
            isTimerRunning = true;
            startTimer();
        }
    }

    private void startTimer() {
        new CountDownTimer(500, 500) { //0.5 seconds

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                findUsers(searchText);
                isTimerRunning = false;
            }
        }.start();
    }


    private void findUsers(String username) {
        if (username.equals("")) {
            recyclerBinder.removeRangeAt(0, recyclerBinder.getItemCount());
            return;
        }

        ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("_User");
        userQuery.whereContains("username", username.toLowerCase());
        userQuery.orderByDescending("createdAt");

        userQuery.findInBackground((objects, e) -> {
            if (e == null) {
                List<ParseUser> users = new ArrayList<>();

                for (int i = 0; i < objects.size(); i++) {
                    if (!objects.get(i).getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                        users.add((ParseUser) objects.get(i));
                    }
                }

                updateContent(users);

            } else {
                Log.e(TAG, "Error finding users with usernames' containing: " + username);
                e.printStackTrace();
                Toast.makeText(getContext(), "Error finding users", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateContent(List<ParseUser> users) {
        recyclerBinder.removeRangeAt(0, recyclerBinder.getItemCount());

        for (ParseUser user : users) {
            Component component = UserItem.create(componentContext).context(getContext()).user(user).isAddFriend(isAddFriend).build();
            recyclerBinder.appendItem(component);
        }
    }
}
