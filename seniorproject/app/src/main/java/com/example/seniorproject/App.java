package com.example.seniorproject;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this).applicationId("lGfptol5avcxhHLygiPj6yct4NY6zp6UvtH9i04U")
                .clientKey("v7LmLxTJtSBR9DCSsBftGMcOJ9bxkNhv58BtRwvj")
                .server("https://parseapi.back4app.com/").build());
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
