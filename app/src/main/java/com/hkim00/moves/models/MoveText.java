package com.hkim00.moves.models;

import org.parceler.Parcel;

@Parcel
public class MoveText extends Move {

    public String Cuisine;

    public MoveText() {
        super();
        Cuisine = "";
        super.moveType = "moveText";
    }

    public MoveText(String text) {
        this.Cuisine = text;
    }


}
