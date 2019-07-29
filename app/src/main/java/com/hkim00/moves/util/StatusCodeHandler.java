package com.hkim00.moves.util;

import android.util.Log;

public class StatusCodeHandler {

    public StatusCodeHandler(String TAG, int statusCode) {
        if (statusCode == 400) {
            Log.e(TAG, "Bad request: are you missing a required param?");
        } else if (statusCode == 401) {
            Log.e(TAG, "Invalid API key or credentials: you may not be authorized to access this content.");
        } else if (statusCode == 404) {
            Log.e(TAG, "You are searching for something that does not exist.");
        } else {
            Log.e(TAG, "Unknown error: please check log for details.");
        }
    }
}
