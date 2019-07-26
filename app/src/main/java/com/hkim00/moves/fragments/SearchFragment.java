package com.hkim00.moves.fragments;

import android.content.pm.ComponentInfo;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.OrientationHelper;

import com.facebook.litho.Column;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.LithoView;
import com.facebook.litho.widget.LinearLayoutInfo;
import com.facebook.litho.widget.Recycler;
import com.facebook.litho.widget.RecyclerBinder;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaEdge;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.specs.MoveItem;
import com.hkim00.moves.specs.SearchComponent;
import com.hkim00.moves.specs.SearchComponentSpec;
import com.hkim00.moves.specs.UserItem;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    public final static String TAG = "SearchFragment";

    private List<Move> pastMoves;
    private ComponentContext componentContext;
    private RecyclerBinder recyclerBinder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return setupRecycler();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private View setupRecycler() {
        componentContext = new ComponentContext(getContext());

        recyclerBinder =  new RecyclerBinder.Builder()
                .layoutInfo(new LinearLayoutInfo(getContext(), OrientationHelper.VERTICAL, false))
                .build(componentContext);

        final Component component = SearchComponent.create(componentContext)
                .hint("Search username")
                .binder(recyclerBinder)
                .listener(new SearchComponentSpec.OnQueryUpdateListener() {
                    @Override
                    public void onQueryUpdated(String username) {
                        findUsers(username);
                    }
                })
                .build();

        return LithoView.create(componentContext, component);
    }


    private void findUsers(String username) {
        if (username.equals("")) {
            recyclerBinder.removeRangeAt(0, recyclerBinder.getItemCount());
            return;
        }

        ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("_User");
        userQuery.whereContains("username", username);
        userQuery.orderByDescending("createdAt");

        userQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    List<ParseUser> users = new ArrayList<>();

                    for (int i = 0; i < objects.size(); i++) {
                        users.add((ParseUser) objects.get(i));
                    }

                    updateContent(users);

                } else {
                    Log.e(TAG, "Error finding users with usernames containing: " + username);
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error finding users", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void updateContent(List<ParseUser> users) {
        recyclerBinder.removeRangeAt(0, recyclerBinder.getItemCount());

        for (ParseUser user : users) {
            Component component = UserItem.create(componentContext).user(user).build();
            recyclerBinder.appendItem(component);
        }
    }

}
