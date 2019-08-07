package com.hkim00.moves.models;

import android.service.restrictions.RestrictionsReceiver;

import com.hkim00.moves.util.ParseUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Cuisine {


    public static String currCuisine;
    public static List<Move> results;
    public static HashMap<String, String> moveToType;


    public static HashMap<String,Integer> mostPrefCuisine = new HashMap<>();



    public static String getCuisine(String cuisine) {
        currCuisine = cuisine;
        return currCuisine;
    }

    public static HashMap<String, Integer> getMostPref(String cuisine) {
        if (!mostPrefCuisine.containsKey(cuisine)) {
            mostPrefCuisine.put(cuisine, 0);
        }
        mostPrefCuisine.put(cuisine, mostPrefCuisine.get(cuisine) + 1);

        HashMap<String, Integer> orderedPrefs = orderedHashMap(mostPrefCuisine);
        return orderedPrefs;
    }

    private static HashMap<String, Integer> orderedHashMap(HashMap<String, Integer> original) {
        List<Map.Entry<String, Integer>> list =
                new LinkedList<Map.Entry<String, Integer>>(original.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return (HashMap<String, Integer>) sortedMap;
    }

}
