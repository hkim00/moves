package com.hkim00.moves.helpers;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Helper {

    public static List<String> foodCategoriesList;
    public static List<String> eventCategoriesList;

    public Helper() {
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
                e.printStackTrace();
            }
        }

        return stringList;
    }

}
