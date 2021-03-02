package com.example.seniorproject.payment;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.seniorproject.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PaymentAdapter extends BaseAdapter {


    Context context;
    ArrayList<Card> cards;


    public PaymentAdapter(Context c,ArrayList<Card> ca){
        context=c;
        cards=ca;
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int i) {
        return cards.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Card card=cards.get(i);
        LayoutInflater inflater=LayoutInflater.from(context);
        view=inflater.inflate(R.layout.paymentrow,null);
        TextView type=view.findViewById(R.id.paymentdesc);
        TextView nummer=view.findViewById(R.id.paymentnumber);
        String typ="";
        if (card.type.equals("VISA")){
            typ="Visa";
        }else if (card.type.equals("MASTERCARD")){
            typ="Mastercard";
        }else{
            typ="Credit/Debit Card";
        }
        type.setText(typ);
        int len=card.number.length();
        nummer.setText("Card ****"+card.number.substring(len-5,len));
        return view;
    }
}
