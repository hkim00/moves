package com.hkim00.moves.util;

import android.content.Context;
import android.util.Log;

import com.hkim00.moves.models.CategoryButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MoveCategoriesHelper {

    public static List<String> foodCategoriesList;
    public static List<String> eventCategoriesList;

    public static List<CategoryButton> mCategories;

    public void CategoryHelper(List<String> list) {
        mCategories = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            CategoryButton categoryButton = new CategoryButton(list.get(i), "");
            mCategories.add(categoryButton);
        }
    }

    public MoveCategoriesHelper() {
        foodCategoriesList = Arrays.asList(
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

        eventCategoriesList = Arrays.asList(
                "Music",
                "Sports",
                "Dance",
                "Broadway",
                "Theater",
                "Opera");
    }

    public static List<String> getPreferenceDiff(String type, List<String> preference) {
        List<String> c = type.equals("food") ? foodCategoriesList : eventCategoriesList;

        Collections.sort(c);
        Collections.sort(preference);

        List<String> nonPreferred = new ArrayList<>();

        int i = 0;
        int a = 0;
        while (i < c.size()) {
            if (!c.get(i).equals(preference.get(a))) {
                nonPreferred.add(c.get(i));
            } else {
                if (a < preference.size() - 1) {
                    a++;
                }
            }
            i++;

        }
        return nonPreferred;
    }


    public static List<String> JSONArrayToList(Context context, JSONArray jsonArray) {

        List<String> stringList = new ArrayList<String>();

        for(int i = 0; i < jsonArray.length(); i++){
            try {
                stringList.add((String) jsonArray.get(i));
            } catch (JSONException e) {
                Log.e(context.toString(), e.getMessage());
                e.printStackTrace();
            }
        }
        return stringList;
    }

}
