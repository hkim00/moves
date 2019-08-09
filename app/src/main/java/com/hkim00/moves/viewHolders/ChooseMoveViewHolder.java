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

import com.hkim00.moves.MoveDetailsActivity;
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
            ParseUser.getCurrentUser().addAllUnique((move.moveType.equals("food")) ? "restaurantsCompleted" : "eventsCompleted", Arrays.asList(move.name));
            ParseUser.getCurrentUser().saveInBackground();

            showMoveWasChosen();

            move.didComplete = true;
            move.didSave = false;

            if (move.parseObject != null && move.didSave && !move.didComplete) {
                move.parseObject.put("didComplete", move.didComplete);
                move.parseObject.put("didSave", move.didSave);
                move.parseObject.saveInBackground();
            } else {
                move.saveToParse();
            }
        }
    }

    private void showMoveWasChosen() {
        tvChooseMove.setText("Move Chosen");
        tvChooseMove.setTextColor(ContextCompat.getColor(context, R.color.black));
        vChooseMoveView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_grey));

        MoveDetailsActivity.changeSaveToFav();
    }
}
