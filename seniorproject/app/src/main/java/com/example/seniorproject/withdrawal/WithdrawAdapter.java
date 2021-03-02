package com.example.seniorproject.withdrawal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.seniorproject.R;
import java.util.ArrayList;

public class WithdrawAdapter extends BaseAdapter {

    ArrayList<Withdrawal> withdrawals;
    Context context;

    public WithdrawAdapter(Context c,ArrayList<Withdrawal> w){
        withdrawals=w;
        context=c;
    }

    @Override
    public int getCount() {
        return withdrawals.size();
    }

    @Override
    public Object getItem(int i) {
        return withdrawals.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Withdrawal withdrawal=withdrawals.get(i);
        LayoutInflater inflater=LayoutInflater.from(context);
        view=inflater.inflate(R.layout.withdrawrow,null);
        final TextView day=view.findViewById(R.id.withday);
        final TextView d=view.findViewById(R.id.withdayy);
        final TextView year=view.findViewById(R.id.withdate);
        final TextView storename=view.findViewById(R.id.withstore);
        final TextView storebranch=view.findViewById(R.id.withbranch);
        final TextView amount=view.findViewById(R.id.withamount);
        day.setText(withdrawal.day);
        d.setText(withdrawal.tag);
        year.setText(withdrawal.year);
        storename.setText(withdrawal.storename);
        storebranch.setText(withdrawal.storebranch+" Branch");
        amount.setText(withdrawal.amount);
        return view;
    }
}
