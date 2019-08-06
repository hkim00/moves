package com.hkim00.moves.models;

import org.parceler.Parcel;

@Parcel
public class MoveText implements Move {

    public String Cuisine;

    public MoveText() {
        Cuisine = "";
    }

    public MoveText(String text) {
        this.Cuisine = text;
    }

    @Override
    public int getMoveType() {
        return 3;
    }
}
