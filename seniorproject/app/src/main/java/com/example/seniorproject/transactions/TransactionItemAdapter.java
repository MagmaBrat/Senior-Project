package com.example.seniorproject.transactions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seniorproject.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TransactionItemAdapter extends BaseAdapter {

    ArrayList<TransactionItem> items;
    Context context;

    public TransactionItemAdapter(Context c, ArrayList<TransactionItem> i){
        context=c;
        items=i;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TransactionItem transaction=items.get(i);
        LayoutInflater inflater=LayoutInflater.from(context);
        view=inflater.inflate(R.layout.transitemrow,null);
        TextView desc=view.findViewById(R.id.transactname);
        TextView price=view.findViewById(R.id.transactprice);
        if (!transaction.istax){
            desc.setText(transaction.quant+" x "+transaction.name);
        }else{
            desc.setText(transaction.name);
        }
        price.setText("$"+transaction.price);
        return view;
    }
}
