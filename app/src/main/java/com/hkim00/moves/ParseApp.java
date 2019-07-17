package com.hkim00.moves;

import android.app.Application;

import com.parse.Parse;

public class ParseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

       // ParseObject.registerSubclass(xxx.class); --> will need this line to create model for users and categories

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("moves-app")
                .clientKey("thirty-dolphins")
                .server("https://moves.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
