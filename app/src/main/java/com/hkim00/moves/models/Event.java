package com.hkim00.moves.models;

import com.parse.ParseObject;

import org.parceler.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

@Parcel
public class Event implements Move {
    public String name, id, text;

    public Event() {}

    @Override
    public int getMoveType() {
        return Move.EVENT;
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
        event.id = parseObject.getString("id");

        return event;
    }
}
