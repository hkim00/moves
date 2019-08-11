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
import com.hkim00.moves.TripActivity;
import com.hkim00.moves.models.Event;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.Restaurant;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;

import static com.hkim00.moves.util.ParseUtil.getParseQuery;

public class ChooseMoveViewHolder extends RecyclerView.ViewHolder {
    private Context context;
    private View vChooseMoveView;
    private TextView tvChooseMove;
    private Button btnChooseMove;
    private Move move;

    private List<Move> selectedMoves, newSelectedMoves, deleteFromServerMoves;

    private boolean isTrip;


    public ChooseMoveViewHolder(@NonNull View itemView) {
        super(itemView);

        vChooseMoveView = itemView.findViewById(R.id.vChooseMoveView);
        tvChooseMove = itemView.findViewById(R.id.tvChooseMove);
        btnChooseMove = itemView.findViewById(R.id.btnChooseMove);

        setupButtons();
    }

    public void bind(Context context, Move move, boolean isTrip) {
        this.context = context;
        this.move = move;
        this.isTrip = isTrip;

        tvChooseMove.setText(isTrip ? "Add to Trip" : "Choose Move");

        if (isTrip) {
            selectedMoves = TripActivity.selectedMoves;
            newSelectedMoves = TripActivity.newSelectedMoves;
            deleteFromServerMoves = TripActivity.deleteFromServerMoves;

            if (selectedMoves.contains(move)) {
                tvChooseMove.setText("Remove From Trip");
                vChooseMoveView.setBackgroundColor(ContextCompat.getColor(context, R.color.cancel_red));
            } else {
                tvChooseMove.setText("Add To Trip");
            }
        }
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
        if (!isTrip) {
            tvChooseMove.setText("Move Chosen");
            tvChooseMove.setTextColor(ContextCompat.getColor(context, R.color.black));
            vChooseMoveView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_grey));

            MoveDetailsActivity.changeSaveToFav();
        } else {
            saveToTrip();
        }
    }

    private void saveToTrip() {
        if (TripActivity.isEditingTrip) {
            if ((selectedMoves.contains(move) && newSelectedMoves.contains(move))) {
                selectedMoves.remove(move);
                newSelectedMoves.remove(move);
                tvChooseMove.setText("Add to Trip");
                vChooseMoveView.setBackgroundColor(ContextCompat.getColor(context, R.color.theme));
            } else if (selectedMoves.contains(move) && !newSelectedMoves.contains(move)) {
                selectedMoves.remove(move);
                deleteFromServerMoves.add(move);
                tvChooseMove.setText("Add to Trip");
                vChooseMoveView.setBackgroundColor(ContextCompat.getColor(context, R.color.theme));
            } else if (!selectedMoves.contains(move) && newSelectedMoves.contains(move)) {
                newSelectedMoves.remove(move);
                tvChooseMove.setText("Add to Trip");
                vChooseMoveView.setBackgroundColor(ContextCompat.getColor(context, R.color.theme));
            } else {
                selectedMoves.add(move);
                newSelectedMoves.add(move);
                tvChooseMove.setText("Remove From Trip");
                vChooseMoveView.setBackgroundColor(ContextCompat.getColor(context, R.color.cancel_red));
            }
        } else {

            if (!selectedMoves.contains(move)) {
                selectedMoves.add(move);
                tvChooseMove.setText("Remove From Trip");
                vChooseMoveView.setBackgroundColor(ContextCompat.getColor(context, R.color.cancel_red));
            } else {
                selectedMoves.remove(move);
                tvChooseMove.setText("Add to Trip");
                vChooseMoveView.setBackgroundColor(ContextCompat.getColor(context, R.color.theme));
            }
        }
    }
}
