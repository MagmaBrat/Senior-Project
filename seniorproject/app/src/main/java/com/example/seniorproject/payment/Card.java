package com.example.seniorproject.payment;

public class Card {

    String number;
    String holder;
    String exp;
    String type;
    String id;

    public Card(String n,String h,String e,String t,String i){
        number=n;
        holder=h;
        exp=e;
        type=t;
        id=i;
    }
}
