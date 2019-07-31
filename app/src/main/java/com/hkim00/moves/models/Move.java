package com.hkim00.moves.models;

public interface Move {

    int RESTAURANT = 1;
    int EVENT = 2;
    int getMoveType();
    String getName();
    String getId();
    Boolean getDidComplete();
    Boolean getDidSave();
    Boolean getDidFavorite();

}
