package com.hkim00.moves.models;

public class CategoryButton {
    public String cuisine;
    public String cuisineImageURL;
    public Boolean isPref = false;

    public CategoryButton (String cuisine, String cuisineImageURL) {
        this.cuisine = cuisine;
        this.cuisineImageURL = cuisineImageURL;
    }
}

