package com.hkim00.moves.fragments;

import android.content.pm.ComponentInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.KeyEventDispatcher;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.litho.Column;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.LithoView;
import com.facebook.litho.sections.SectionContext;
import com.facebook.litho.sections.widget.RecyclerCollectionComponent;
import com.facebook.litho.widget.Card;
import com.facebook.litho.widget.LinearLayoutInfo;
import com.facebook.litho.widget.Progress;
import com.facebook.litho.widget.Recycler;
import com.facebook.litho.widget.RecyclerBinder;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaEdge;
import com.hkim00.moves.HomeActivity;
import com.hkim00.moves.R;
import com.hkim00.moves.adapters.MoveAdapter;
import com.hkim00.moves.models.Event;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.Restaurant;
import com.hkim00.moves.specs.MoveItem;
import com.hkim00.moves.specs.MoveSection;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {

    public final static String TAG = "HistoryFragment";

    private List<Move> pastMoves;

    private RecyclerCollectionComponent recyclerCollectionComponent;

    private ComponentContext componentContext;
    private RecyclerBinder recyclerBinder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        pastMoves = new ArrayList<>();

        componentContext = new ComponentContext(getContext());

        recyclerBinder =  new RecyclerBinder.Builder()
                .layoutInfo(new LinearLayoutInfo(getContext(), OrientationHelper.VERTICAL, false))
                .build(componentContext);

        Recycler recycler = Recycler.create(componentContext)
                .binder(recyclerBinder)
                .build();

        final Component component = Column.create(componentContext)
                .child(Text.create(componentContext)
                                .text("Past Moves")
                                .textStyle(3) //bold italic
                                .textSizeDip(24)
                                .paddingDip(YogaEdge.TOP, 15))
                .child(recycler)
                .build();


        return LithoView.create(componentContext, component);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getHistory();
    }


    private void getHistory() {
            ParseQuery<ParseObject> restaurantQuery = ParseQuery.getQuery("Restaurant");
            restaurantQuery.whereEqualTo("user", ParseUser.getCurrentUser());
            restaurantQuery.whereEqualTo("didComplete", true);
            restaurantQuery.orderByDescending("createdAt");

            restaurantQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        List<Move> moves = Restaurant.arrayFromParseObjects(objects);
                        pastMoves.addAll(moves);

                        addContents(moves);
                    } else {
                        Log.e(TAG, "Error finding saved restaurants.");
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error saving restaurant", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            ParseQuery<ParseObject> eventQuery = ParseQuery.getQuery("Event");
            eventQuery.whereEqualTo("user", ParseUser.getCurrentUser());
            eventQuery.whereEqualTo("didComplete", true);
            eventQuery.orderByDescending("createdAt");
            eventQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        List<Move> moves = Event.arrayFromParseObjects(objects);
                        pastMoves.addAll(moves);

                        addContents(moves);
                    } else {
                        Log.e(TAG, "Error finding saved restaurants.");
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error saving restaurant", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


        private void addContents(List<Move> moves) {
            if (moves.size() == 0) {
//                recyclerBinder.insertItemAt(0, Progress.create(componentContext)
//                        .scale(0.5f)
//                        .build());
                return;
            }

            for (int i = 0; i < moves.size(); i++) {
                Component component = MoveItem.create(componentContext).move(moves.get(i)).build();
                recyclerBinder.appendItem(component);
            }
        }
}
