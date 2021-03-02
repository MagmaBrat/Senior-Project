package com.example.seniorproject.transactions;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.seniorproject.R;
import com.example.seniorproject.afterlog.AfterloginActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TransactionsFragment extends Fragment {

    TransactionAdapter adapter;
    ArrayList<Transaction> transactions;
    AfterloginActivity activity;
    Spinner spinner;

    public TransactionsFragment(AfterloginActivity a){
        activity=a;
    }


    public void getTransactions(int code){
        transactions.clear();
        ParseUser user=ParseUser.getCurrentUser();
        if (user!=null){
            ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("Receipt");
            query.whereEqualTo("username",user.getUsername());
            if (code==0){
                query.orderByDescending("createdAt");
            }else if (code==1){
                query.orderByAscending("store");
            }else if (code==2){
                query.orderByAscending("total");
            }
            activity.makeClickable(View.VISIBLE,false);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e==null){
                        for (ParseObject x:objects){
                            byte[] data= new byte[0];
                            Bitmap bmp = BitmapFactory
                                    .decodeByteArray(
                                            data, 0,
                                            data.length);
                            float total=x.getNumber("total").floatValue();
                            String newbal=new DecimalFormat("#.##").format(total);
                            Transaction transaction=new Transaction(bmp,x.getString("store"),x.getCreatedAt(),newbal,x.getObjectId());
                            transactions.add(transaction);
                        }
                        adapter.notifyDataSetChanged();
                    }else{
                        e.printStackTrace();
                    }
                    activity.makeClickable(View.INVISIBLE,true);
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.transactragment,container,false);
        ListView listView=rootview.findViewById(R.id.transactlist);
        transactions=new ArrayList<>();
        spinner=rootview.findViewById(R.id.spinner);
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
        adapter=new TransactionAdapter(getActivity(),transactions);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String x=((Transaction)adapter.getItem(i)).id;
                activity.current=new TransactionSingleFragment(x,activity);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,activity.current).commit();
            }
        });
        return rootview;
    }
}
