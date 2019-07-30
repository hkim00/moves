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

    // searches in Parse for a specific move (by id) that were done by current user
    public static ParseQuery getParseQuery(String moveType, ParseUser currUser, Boolean didComplete, Move move) {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Move");
        parseQuery.whereEqualTo("placeId", (moveType.equals("food")) ? ((Restaurant) move).id : ((Event) move).id);
        parseQuery.whereEqualTo("user", currUser);
        parseQuery.whereEqualTo("didComplete", didComplete);
        return parseQuery;
    }

    // if the move has not been done by current user, this method will save a new move
    // if it has been done before, then it will save/unsave the move
    public static void getDidSave(String moveType, List<ParseObject> objects, Move move) {
        ParseUser currUser = ParseUser.getCurrentUser();
        if (objects.size() > 0) {
            for (int i = 0; i < objects.size(); i++) {
                if (objects.get(i).getBoolean("didSave") == true){
                    objects.get(i).put("didSave", false);
                    objects.get(i).saveInBackground();
                } else {
                    objects.get(i).put("didSave", true);
                    objects.get(i).saveInBackground();
                }
            }
        } else {
            ParseObject currObj = new ParseObject("Move");
            currObj.put("name", (moveType.equals("food")) ? ((Restaurant) move).name : ((Event) move).name);
            currObj.put("user", currUser);
            currObj.put("didSave", true);
            currObj.put("didFavorite", false);
            currObj.put("moveType", moveType);
            currObj.put("placeId", (moveType.equals("food")) ? ((Restaurant) move).id : ((Event) move).id);
            currObj.put("didComplete", false);
            currObj.saveInBackground();
        }
    }

    // if the move has not been done by current user, this method will like a new move
    // if it has been done before, then it will like/unlike the move
    public static void getDidFavorite(String moveType, List<ParseObject> objects, Move move) {
        ParseUser currUser = ParseUser.getCurrentUser();
        if (objects.size() > 0) {
            for (int i = 0; i < objects.size(); i++) {
                if (objects.get(i).getBoolean("didFavorite") == true){
                    objects.get(i).put("didFavorite", false);
                    objects.get(i).saveInBackground();
                } else {
                    objects.get(i).put("didFavorite", true);
                    objects.get(i).saveInBackground();
                }
            }
        }
    }

    public static ParseQuery<ParseObject> getSpecificQuery(String moveType, String didFavOrSave) {
        ParseQuery<ParseObject> favQuery = ParseQuery.getQuery(moveType);
        favQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        favQuery.whereEqualTo(didFavOrSave, true);
        favQuery.orderByDescending("createdAt");
        return favQuery;
    }
}


