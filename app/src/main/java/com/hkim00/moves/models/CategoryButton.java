package com.hkim00.moves.models;


public class CategoryButton {
    public String type;
    public String typeImageURL;
    public Boolean isPref = false;

    public CategoryButton (String type, String typeImageURL) {
        this.type = type;
        this.typeImageURL = typeImageURL;
    }
}
