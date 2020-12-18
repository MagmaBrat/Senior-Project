package com.example.seniorproject.storeapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.seniorproject.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CartAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> temps;
    HashMap<String,Float> hashMap;
    ArrayList<String> names;


    public CartAdapter(Context c,ArrayList<String> t,ArrayList<String> n){
        context=c;
        temps=t;
        names=new ArrayList<>();
        names.add("Select an Item");
        names.addAll(n);
    }

    @Override
    public int getCount() {
        return temps.size();
    }

    @Override
    public Object getItem(int i) {
        return temps.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=LayoutInflater.from(context);
        view=inflater.inflate(R.layout.cartsrow,null);
        TextView textView=view.findViewById(R.id.cartitem);
        textView.setText("Item#"+(i+1));
        Spinner spinner=view.findViewById(R.id.cartspinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,names);
        spinner.setAdapter(adapter);
        return view;
    }
}
