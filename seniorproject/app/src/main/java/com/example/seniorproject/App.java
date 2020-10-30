package com.example.seniorproject;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ProgressBar;


import androidx.constraintlayout.widget.ConstraintLayout;

import com.parse.Parse;
import com.parse.ParseException;
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

    public static AlertDialog.Builder makeDialog(Context context, ParseException e){
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MyDialogTheme);
        builder.setMessage(e.getMessage())
                .setTitle("Alert")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert);
        return builder;
    }

    public static void makeClickable(int visibility, boolean clickable, ConstraintLayout constraintLayout, ProgressBar mypro){
        mypro.setVisibility(visibility);
        for (int i=0;i<constraintLayout.getChildCount();i++){
            constraintLayout.getChildAt(i).setClickable(clickable);
        }
    }

}
