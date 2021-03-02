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

public class TransactionAdapter extends BaseAdapter {

    public ArrayList<Transaction> transactions;
    Context context;

    public TransactionAdapter(Context c,ArrayList<Transaction> t){
        context=c;
        transactions=t;
    }

    @Override
    public int getCount() {
        return transactions.size();
    }

    @Override
    public Object getItem(int i) {
        return transactions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Transaction transaction=transactions.get(i);
        LayoutInflater inflater=LayoutInflater.from(context);
        view=inflater.inflate(R.layout.transactrow,null);
        ImageView logo=view.findViewById(R.id.translogo);
        TextView store=view.findViewById(R.id.transstore);
        TextView create=view.findViewById(R.id.transdate);
        TextView total=view.findViewById(R.id.transtotal);
        logo.setImageResource(R.drawable.store2);
        store.setText(transaction.store);
        SimpleDateFormat formatter;
        String strDate;
//        formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm z");
        formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm");
        strDate = formatter.format(transaction.create);
        create.setText(strDate);
        total.setText("Total: $"+transaction.total);
        return view;
    }
}
