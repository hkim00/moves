package com.hkim00.moves.models;

import com.parse.ParseObject;

import org.json.JSONArray;
import org.parceler.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Event extends Move {
    public int count;
    public String name, id, text;
    public Boolean didSave, didFavorite, didComplete;

    public String moveType = "event";

    public Event() {}

    public static List<Move> arrayFromParseObjects(List<ParseObject> objects) {
        List<Move> events = new ArrayList<>();

        for (int i = 0; i < objects.size(); i++) {
            Event event = Event.fromParseObject(objects.get(i));
            events.add(event);
        }
        return events;
    }

    public static List<Move> arrayFromJSONArray(JSONArray objects) {
        List<Move> eventMoves = new ArrayList<>();

        try {
            for (int i = 0; i < objects.length(); i++) {
                Event event = Event.fromJSON(objects.getJSONObject(i));
                eventMoves.add(event);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return eventMoves;
    }


    public static Event fromJSON(JSONObject jsonObject) throws JSONException {
        Event event = new Event();

        event.name = jsonObject.getString("name");
        event.id = jsonObject.getString("id");

        return event;
    }


    public static Event fromParseObject(ParseObject parseObject) {
        Event event = new Event();

        event.name = parseObject.getString("name");
        event.id = parseObject.getString("placeId");

        event.didSave = parseObject.getBoolean("didSave");
        event.didFavorite = parseObject.getBoolean("didFavorite");
        event.didComplete = parseObject.getBoolean("didComplete");

        return event;
    }
}
