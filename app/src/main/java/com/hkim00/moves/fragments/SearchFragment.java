package com.hkim00.moves.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.hkim00.moves.HomeActivity;
import com.hkim00.moves.R;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.UserLocation;
import com.hkim00.moves.specs.MoveItem;
import com.hkim00.moves.specs.SearchComponent;
import com.hkim00.moves.specs.UserItem;
import com.hkim00.moves.util.StatusCodeHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchFragment extends Fragment {
    private final static String TAG = "SearchFragment";

    public static Boolean isAddFriend;

    private ComponentContext componentContext;
    private RecyclerBinder recyclerBinder;

    private String searchText;
    private boolean isTimerRunning;
    private UserLocation location;

    String type;


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
        type = "users";

        location = UserLocation.getCurrentLocation(getContext());
    }


    private View setupRecycler() {
        componentContext = new ComponentContext(getContext());

        recyclerBinder =  new RecyclerBinder.Builder()
                .layoutInfo(new LinearLayoutInfo(getContext(), OrientationHelper.VERTICAL, false))
                .build(componentContext);

        final Component component = SearchComponent.create(componentContext)
                .hint("Search by name")
                .binder(recyclerBinder)
                .listener(text -> searchTextUpdated(text))
                .build();

        return LithoView.create(componentContext, component);
    }

    private void searchTextUpdated(String text) {
        searchText = text.toLowerCase().trim();
        if (searchText.equals("")) {
            recyclerBinder.removeRangeAt(0, recyclerBinder.getItemCount());
            return;
        }

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


                if (type.equals("users")) {
                    findUsers();
                } else {
                    findMoves();
                }
                isTimerRunning = false;
            }
        }.start();
    }

    private void updateUsers(List<ParseUser> users) {
        recyclerBinder.removeRangeAt(0, recyclerBinder.getItemCount());

        for (ParseUser user : users) {
            Component component = UserItem.create(componentContext).context(getContext()).user(user).isAddFriend(isAddFriend).build();
            recyclerBinder.appendItem(component);
        }
    }

    private void updateMoves(List<Move> moves) {
        recyclerBinder.removeRangeAt(0, recyclerBinder.getItemCount());

        for (Move move: moves) {
            Component component = MoveItem.create(componentContext).move(move).build();
            recyclerBinder.appendItem(component);
        }
    }


    private void findUsers() {
        ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("_User");
        userQuery.whereContains("username", searchText.toLowerCase());
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
                Log.e(TAG, "Error finding users with usernames' containing: " + searchText.toLowerCase());
                e.printStackTrace();
                Toast.makeText(getContext(), "Error finding users", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void findMoves() {
        final String API_URL = (type.equals("food")) ? "https://maps.googleapis.com/maps/api/place/textsearch/json?" : "https://app.ticketmaster.com/discovery/v2/events.json";


        RequestParams params = new RequestParams();

        if (type.equals("food")) {
            params.put("key", getString(R.string.api_key));
            params.put("query", searchText);
            params.put("radius", 20000);
            params.put("location", location.lat + "," + location.lng);
        } else {
            params.put("apikey", getString(R.string.api_key_tm));
            params.put("postalCode", location.postalCode);
            params.put("sort", "date,asc");
            params.put("keyword", searchText);
        }

        HomeActivity.client.get(API_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                List<Move> moveResults = new ArrayList<>();
                try {
                    Move.arrayFromJSONArray(moveResults, response.getJSONArray("results"), type);
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }

                updateMoves(moveResults);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                new StatusCodeHandler(TAG, statusCode);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                new StatusCodeHandler(TAG, statusCode);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                new StatusCodeHandler(TAG, statusCode);
                throwable.printStackTrace();
            }
        });

    }



}
