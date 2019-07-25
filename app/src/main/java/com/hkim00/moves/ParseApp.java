package com.hkim00.moves;

import android.app.Application;

import com.facebook.soloader.SoLoader;
import com.parse.Parse;

public class ParseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //for Parse
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("moves-app")
                .clientKey("thirty-dolphins")
                .server("https://moves.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);

        //for Litho
        SoLoader.init(this, false);
    }
}
