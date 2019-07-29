package com.hkim00.moves.util;

import android.util.Log;

import com.hkim00.moves.models.Event;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.Restaurant;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ParseUtil {

    public static ParseQuery getParseQuery(String moveType, Move move) {
        ParseUser currUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(moveType);
        parseQuery.whereEqualTo("placeId", (move.getMoveType() == Move.RESTAURANT) ? ((Restaurant) move).id : ((Event) move).id);
        parseQuery.whereEqualTo("user", currUser);
        return parseQuery;
    }


    public static void getDidSave(String moveType, List<ParseObject> objects, Move move) {
        ParseUser currUser = ParseUser.getCurrentUser();
        if (objects.size() > 0) {
            for (int i = 0; i < objects.size(); i++) {
                objects.get(i).put("didSave", true);
                objects.get(i).saveInBackground();
            }
        } else {
            ParseObject currRest = new ParseObject(moveType);
            currRest.put("name", (move.getMoveType() == Move.RESTAURANT) ? ((Restaurant) move).name : ((Event) move).name);
            currRest.put("user", currUser);
            currRest.put("didSave", true);
            currRest.saveInBackground();
        }

        Log.d("MoveDetailsActivity", "saved move");
    }


    public static void getDidFavorite(String moveType, List<ParseObject> objects, Move move) {
        ParseUser currUser = ParseUser.getCurrentUser();
        if (objects.size()>0) {
            for (int i = 0; i < objects.size(); i++) {
                objects.get(i).put("didFavorite", true);
                objects.get(i).saveInBackground();
            }
        }
        else {
            ParseObject currRest = new ParseObject(moveType);
            currRest.put("name", (move.getMoveType() == Move.RESTAURANT) ? ((Restaurant) move).name : ((Event) move).name);
            currRest.put("user", currUser);
            currRest.put("didFavorite", true);
            currRest.saveInBackground();
            Log.d("MoveDetailsActivity", "added move");
        }

        Log.d("MoveDetailsActivity", "favorited move");
    }

    public static ParseQuery<ParseObject> getSpecificQuery(String moveType, String didFavOrSave) {
        ParseQuery<ParseObject> favQuery = ParseQuery.getQuery(moveType);
        favQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        favQuery.whereEqualTo(didFavOrSave, true);
        favQuery.orderByDescending("createdAt");
        return favQuery;
    }

}


