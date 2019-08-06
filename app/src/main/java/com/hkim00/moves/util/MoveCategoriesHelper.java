package com.hkim00.moves.util;

import android.content.Context;
import android.util.Log;

import com.hkim00.moves.models.CategoryButton;
import com.parse.ParseUser;

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



    public static List<String> JSONArrayToList(JSONArray jsonArray) {

        List<String> stringList = new ArrayList<String>();

        for(int i = 0; i < jsonArray.length(); i++){
            try {
                stringList.add((String) jsonArray.get(i));
            } catch (JSONException e) {
                Log.e("MoveCategoriesHelper", e.getMessage());
                e.printStackTrace();
            }
        }
        return stringList;
    }


    public static String getUserFoodPreferenceString(List<String> nonPreferredList) {
        if (ParseUser.getCurrentUser().getJSONArray("foodPrefList") == null || ParseUser.getCurrentUser().getJSONArray("foodPrefList").length() == 0) {
            return "";
        }

        List<String> preferredList;

        if (nonPreferredList.size() == 0) {
            preferredList = MoveCategoriesHelper.JSONArrayToList(ParseUser.getCurrentUser().getJSONArray("foodPrefList"));
        } else {
            preferredList = nonPreferredList;
        }

        String userFoodPref = "";
        for (int i = 0; i < preferredList.size(); i++) {
            userFoodPref += preferredList.get(i);
            userFoodPref += "+";
        }

        userFoodPref = userFoodPref.substring(0, userFoodPref.length() -1);

        return userFoodPref;
    }

    public static int milesToMeters(float miles) {
        return (int) (miles/0.000621317);

}
