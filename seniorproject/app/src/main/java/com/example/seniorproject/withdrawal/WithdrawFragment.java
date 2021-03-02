package com.example.seniorproject.withdrawal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.seniorproject.QRactivity;
import com.example.seniorproject.R;
import com.example.seniorproject.afterlog.AfterloginActivity;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WithdrawFragment extends Fragment {

    ArrayList<Withdrawal> withdrawals;
    WithdrawAdapter adapter;
    AfterloginActivity activity;
    ImageView imageView;
    Spinner spinner;
    public WithdrawFragment(AfterloginActivity a){
        activity=a;
    }

    public void getTransactions(int code){
        withdrawals.clear();
        String userid= ParseUser.getCurrentUser().getObjectId();
        ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("Withdraw");
        query.whereEqualTo("userid",userid);
        if (code==0){
            query.orderByDescending("createdAt");
        }else if (code==1){
            query.orderByAscending("storename");
        }else if (code==2){
            query.orderByAscending("amount");
        }
        activity.makeClickable(View.VISIBLE,false);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e==null){
                    try {
                        for(ParseObject x:objects){
                            Withdrawal withdrawal=new Withdrawal();
                            String storeid=x.getParseObject("storeid").getObjectId();
                            Date date=x.getCreatedAt();
                            SimpleDateFormat df=new SimpleDateFormat("E");
                            SimpleDateFormat df1=new SimpleDateFormat("dd");
                            SimpleDateFormat df2=new SimpleDateFormat("MM/yyyy");
                            String days= df.format(date);
                            String tags=df1.format(date);
                            String mys=df2.format(date);
                            withdrawal.day=days;
                            withdrawal.tag=tags;
                            withdrawal.year=mys;
                            float am=x.getNumber("amount").floatValue();
                            String newam=new DecimalFormat("#.##").format(am);
                            withdrawal.amount="$"+newam;
                            ParseQuery<ParseObject> query1=new ParseQuery<ParseObject>("Store");
                            ParseObject object=query1.get(storeid);
                            String sn=object.getString("storename");
                            String branch=object.getString("branch");
                            withdrawal.storename=sn;
                            withdrawal.storebranch=branch;
                            withdrawals.add(withdrawal);
                        }
                    }catch (ParseException e1){
                        e1.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                }else{
                    e.printStackTrace();
                }
                activity.makeClickable(View.INVISIBLE,true);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.withdrawsfragment,container,false);
        imageView=rootview.findViewById(R.id.withdrawaddbut);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity, QRactivity.class);
                startActivity(intent);
            }
        });
        spinner=rootview.findViewById(R.id.withdrawspinner);
        ArrayList<String> factos=new ArrayList<>();
        factos.add("Date");
        factos.add("Store's name");
        factos.add("Total");
        ArrayAdapter<String> simpleadapter = new ArrayAdapter<String>(activity, R.layout.spinner_item,factos);
        spinner.setAdapter(simpleadapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getTransactions(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        withdrawals=new ArrayList<>();
        adapter=new WithdrawAdapter(getActivity(),withdrawals);
        ListView listView=rootview.findViewById(R.id.withdrawlist);
        listView.setAdapter(adapter);
        return rootview;
    }
}
