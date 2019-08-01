package com.hkim00.moves.fragments;

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
import com.hkim00.moves.models.Event;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.Restaurant;
import com.hkim00.moves.specs.MoveItem;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {

    public final static String TAG = "HistoryFragment";

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
        getHistory();
    }


    private View setupRecycler() {
        pastMoves = new ArrayList<>();

        componentContext = new ComponentContext(getContext());

        recyclerBinder =  new RecyclerBinder.Builder()
                .layoutInfo(new LinearLayoutInfo(getContext(), OrientationHelper.VERTICAL, false))
                .build(componentContext);

        Recycler recycler = Recycler.create(componentContext)
                .binder(recyclerBinder)
                .paddingDip(YogaEdge.LEFT, 10)
                .paddingDip(YogaEdge.RIGHT, 10)
                .build();

        final Component component = Column.create(componentContext)
                .child(Text.create(componentContext)
                        .text("Past Moves")
                        .textStyle(3) //bold italic
                        .textSizeDip(24)
                        .textAlignment(Layout.Alignment.ALIGN_CENTER)
                        .paddingDip(YogaEdge.BOTTOM, 10)
                        .paddingDip(YogaEdge.TOP, 15))
                .child(recycler)
                .build();


        return LithoView.create(componentContext, component);
    }


    private void getHistory() {
        ParseQuery<ParseObject> moveQuery = ParseQuery.getQuery("Move");
        moveQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        moveQuery.whereEqualTo("didComplete", true);
        moveQuery.orderByDescending("createdAt");

        moveQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    List<Move> moves = new ArrayList<>();

                    for (int i = 0; i < objects.size(); i++) {
                        if (objects.get(i).getString("moveType").equals("food")) {
                            moves.add(Restaurant.fromParseObject(objects.get(i)));
                        } else {
                            moves.add(Event.fromParseObject(objects.get(i)));
                        }
                    }
                    pastMoves.addAll(moves);
                    addContents(moves);
                } else {
                    Log.e(TAG, "Error finding past moves.");
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error past moves", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void addContents(List<Move> moves) {
        for (int i = 0; i < moves.size(); i++) {
            Component component = MoveItem.create(componentContext).move(moves.get(i)).build();
            recyclerBinder.appendItem(component);
        }
    }
}
