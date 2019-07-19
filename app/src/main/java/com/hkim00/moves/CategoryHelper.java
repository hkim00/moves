package com.hkim00.moves;

import com.hkim00.moves.models.CategoryButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryHelper {

    public static List<CategoryButton> mCategories;

    public CategoryHelper() {
        List<String> mCategoriesStrings;
        mCategoriesStrings = Arrays.asList(
                "Italian",
                "Mexican",
                "Thai",
                "Chinese",
                "Vegan",
                "Indian",
                "American",
                "Greek",
                "LatinAmerican",
                "French",
                "Japanese",
                "Vietnamese",
                "African",
                "Halal",
                "German",
                "German",
                "Korean",
                "Lebanese",
                "Ethiopian",
                "Pakistani",
                "Spanish",
                "Turkish",
                "Caribbean",
                "Indonesian");

        mCategories = new ArrayList<>();
        for (int i = 0; i < mCategoriesStrings.size(); i++) {
            CategoryButton categoryButton = new CategoryButton(mCategoriesStrings.get(i), "");
            mCategories.add(categoryButton);
        }
    }

}
