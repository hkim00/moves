package com.hkim00.moves.models;

import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Move {
    public String name, id;

    public Move() {}

    public static Move fromJSON(JSONObject jsonObject) throws JSONException {
        Move move = new Move();

        move.name = jsonObject.getString("name");
        move.id = jsonObject.getString("id");

        return move;
    }

    public static Move fromParseObject(ParseObject parseObject) {
        Move move = new Move();

        move.name = parseObject.getString("name");
        move.id = parseObject.getString("id");

        return move;
    }

}
