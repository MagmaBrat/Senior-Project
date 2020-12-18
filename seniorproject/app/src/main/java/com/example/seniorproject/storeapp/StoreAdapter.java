package com.example.seniorproject.storeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.seniorproject.R;

import java.util.ArrayList;

public class StoreAdapter  extends BaseAdapter {

    ArrayList<Product> products;
    Context context;

    public StoreAdapter(Context c, ArrayList<Product> p){
        context=c;
        products=p;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Product product=products.get(i);
        LayoutInflater inflater=LayoutInflater.from(context);
        view=inflater.inflate(R.layout.productsrow,null);
        TextView desc=view.findViewById(R.id.productdesc);
        TextView price=view.findViewById(R.id.productprice);
        TextView category=view.findViewById(R.id.productcat);
        desc.setText(product.name);
        price.setText("Price: "+product.price);
        category.setText(product.category);
        return view;
    }
}
