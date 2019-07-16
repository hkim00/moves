package com.example.moves_login;

import android.app.Application;

import com.parse.Parse;

public class ParseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

       // ParseObject.registerSubclass(Post.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("moves-app")
                .clientKey("thirty-dolphins")
                .server("https://moves.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
