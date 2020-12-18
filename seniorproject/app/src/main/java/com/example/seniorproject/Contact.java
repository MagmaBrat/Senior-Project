package com.example.seniorproject;

import com.parse.ParseObject;

public class Contact {

    String nickname;
    ParseObject user;

    public Contact(String n,ParseObject u){
        user=u;
        nickname=n;
    }
}
