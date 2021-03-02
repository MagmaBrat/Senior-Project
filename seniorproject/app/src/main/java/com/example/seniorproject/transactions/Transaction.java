package com.example.seniorproject.transactions;

import android.graphics.Bitmap;

import java.util.Date;

public class Transaction {

    Bitmap logo;
    String store;
    Date create;
    String total;
    String id;

    public Transaction(Bitmap l,String s,Date c,String t,String i){
        logo=l;
        store=s;
        create=c;
        total=t;
        id=i;
    }
}
