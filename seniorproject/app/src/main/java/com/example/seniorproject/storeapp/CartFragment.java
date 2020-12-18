package com.example.seniorproject.storeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.seniorproject.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartFragment extends Fragment {

    CartAdapter adapter;
    ArrayList<String> temps;
    ArrayList<String> prodnames;
    HashMap<String,Product> hashMap;
    FragmentActivity activity;
    ListView listView;

    public void doCalculations(){
        float sum=0f;
        float total;
        View v;
        EditText q;
        Spinner spinner;
        for (int i = 0; i < listView.getCount(); i++) {
            v = listView.getChildAt(i);
            q = (EditText) v.findViewById(R.id.cartquantity);
            int quantity=Integer.parseInt(q.getText().toString());
            spinner=v.findViewById(R.id.cartspinner);
            sum+=hashMap.get(spinner.getSelectedItem().toString()).price*quantity;
        }
        total=sum*0.16f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.cartfragment,container,false);
        listView=rootview.findViewById(R.id.cartlist);
        temps=new ArrayList<>();
        temps.add("");
        hashMap=new HashMap<>();
        prodnames=new ArrayList<>();
        activity=getActivity();
        ParseUser user=ParseUser.getCurrentUser();
        if (user!=null){
            final ParseQuery<ParseUser> query=ParseUser.getQuery();
            query.getInBackground(user.getObjectId(), new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser object, ParseException e) {
                    if (e==null){
                        try {
                            TextView textView=rootview.findViewById(R.id.storename);
                            ParseQuery<ParseObject> query1=new ParseQuery<ParseObject>("Store");
                            ParseObject obj=query1.get(object.getParseObject("storeid").getObjectId());
                            textView.setText(obj.getString("storename"));
                            ParseQuery<ParseObject> query2=obj.getRelation("products").getQuery();
                            query2.whereEqualTo("available",true);
                            List<ParseObject> objects= query2.find();
                            for (ParseObject x:objects){
                                Product product=new Product(x.getString("name"),x.getString("category"),x.getNumber("price").floatValue());
                                hashMap.put(x.getString("name"),product);
                                prodnames.add(x.getString("name"));
                            }
                            adapter=new CartAdapter(activity,temps,prodnames);
                            listView.setAdapter(adapter);
                        }catch (ParseException e1){
                            e1.printStackTrace();
                        }
                    }else{
                        e.printStackTrace();
                    }
                }
            });
        }
        ImageView imageView=rootview.findViewById(R.id.addproduct);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temps.add("");
                adapter.notifyDataSetChanged();
            }
        });
        return rootview;
    }
}
