package com.hkim00.moves.helpers;

import com.hkim00.moves.models.CategoryButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryHelper {

    public static List<CategoryButton> mCategories;

    public CategoryHelper(List<String> list) {

        mCategories = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            CategoryButton categoryButton = new CategoryButton(list.get(i), "");
            mCategories.add(categoryButton);
        }
    }

}
