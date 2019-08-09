package com.hkim00.moves.models;


import java.util.List;

public class MoveCategory {

    public String category;
    public List<Move> moves;

    public MoveCategory(String category, List<Move> moves) {
        this.category = category;
        this.moves = moves;
    }
}
