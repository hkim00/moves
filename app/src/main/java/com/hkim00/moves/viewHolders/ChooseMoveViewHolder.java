package com.hkim00.moves.viewHolders;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.R;
import com.hkim00.moves.models.Event;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.Restaurant;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Arrays;

import static com.hkim00.moves.util.ParseUtil.getParseQuery;

public class ChooseMoveViewHolder extends RecyclerView.ViewHolder {
    private Context context;
    private View vChooseMoveView;
    private TextView tvChooseMove;
    private Button btnChooseMove;
    private Move move;


    public ChooseMoveViewHolder(@NonNull View itemView) {
        super(itemView);

        vChooseMoveView = itemView.findViewById(R.id.vChooseMoveView);
        tvChooseMove = itemView.findViewById(R.id.tvChooseMove);
        btnChooseMove = itemView.findViewById(R.id.btnChooseMove);

        setupButtons();
    }

    public void bind(Context context, Move move) {
        this.context = context;
        this.move = move;
    }

    private void setupButtons() {
        btnChooseMove.setOnClickListener(view -> chooseMove());
    }

    private void chooseMove() {
        if (move.lat != null) {
            ParseQuery<ParseObject> detailsQuery = getParseQuery(ParseUser.getCurrentUser(), move);
            detailsQuery.findInBackground((objects, e) -> {
                if (e == null) {

                    if (move.moveType.equals("food")) {
                        ParseUser.getCurrentUser().addAllUnique("restaurantsCompleted", Arrays.asList(move.name));
                    } else {
                        ParseUser.getCurrentUser().addAllUnique("eventsCompleted", Arrays.asList(move.name));
                    }
                    ParseUser.getCurrentUser().saveInBackground();

                    if (objects.size() == 0) {
                        ParseObject currObj = new ParseObject("Move");
                        currObj.put("name", move.name);
                        currObj.put("placeId", move.id);
                        currObj.put("moveType", (move.moveType));
                        currObj.put("user", ParseUser.getCurrentUser());
                        currObj.put("didComplete", true);
                        currObj.put("didSave", false);
                        currObj.put("count", 0);
                        currObj.put("lat", (move).lat);
                        currObj.put("lng", (move).lng);
                        if (move.moveType.equals("food")){
                            currObj.put("price_level", ((Restaurant)move).price_level);
                        } else {
                            currObj.put("genre", ((Event) move).genre);
                        }

                        showMoveWasChosen();
                        currObj.saveInBackground();

                    } else { // the user has already completed the move
                        for (int i = 0; i < objects.size(); i++) {
                            objects.get(i).put("didComplete", true);
                            objects.get(i).put("didSave", false); // user cannot save a move that has been done

                            showMoveWasChosen();
                            objects.get(i).saveInBackground();
                        }
                    }
                } else {
                    Log.d("Move", "Error: saving move to history");
                }
            });
        }
    }

    private void showMoveWasChosen() {
        tvChooseMove.setText("Move Chosen");
        tvChooseMove.setTextColor(ContextCompat.getColor(context, R.color.black));
        vChooseMoveView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_grey));
    }
}
