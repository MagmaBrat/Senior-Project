package com.example.seniorproject.storeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.seniorproject.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddItemFragment extends Fragment {

    ArrayList<Product> products;
    StoreAdapter adapter;
    HashMap<String,Product> hashMap;
    StoreAfterLoginActivity activity;

    public AddItemFragment(StoreAfterLoginActivity a){
        activity=a;
    }

    public void getProducts(){
        products.clear();
        ParseUser user=ParseUser.getCurrentUser();
        if (user!=null){
            final ParseQuery<ParseUser> query=ParseUser.getQuery();
            query.getInBackground(user.getObjectId(), new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser object, ParseException e) {
                    if (e==null){
                        ParseQuery<ParseObject> query1=new ParseQuery<ParseObject>("Store");
                        query1.getInBackground(object.getParseObject("storeid").getObjectId(), new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object1, ParseException e1) {
                                if (e1==null){
                                    ParseQuery<ParseObject> query2=object1.getRelation("products").getQuery();
                                    try {
                                        List<ParseObject> objects=query2.find();
                                        for (ParseObject obj:objects){

                                            Product product=new Product(
                                                    obj.getString("name"),obj.getString("category")
                                                    ,obj.getNumber("price").floatValue(),obj.getObjectId()
                                            );
                                            products.add(product);
                                        }
                                        adapter.notifyDataSetChanged();
                                    }catch (ParseException e2){
                                        e2.printStackTrace();
                                    }
                                }else{
                                    e1.printStackTrace();
                                }
                            }
                        });
                    }else{
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.additemfragment,container,false);
        ListView listView=rootview.findViewById(R.id.productslist);
        products=new ArrayList<>();
        adapter=new StoreAdapter(getActivity(),products);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(),AddProductFragment.class);
                intent.putExtra("mode","EDIT");
                intent.putExtra("name",products.get(i).name);
                intent.putExtra("price",String.valueOf(products.get(i).price));
                intent.putExtra("category",products.get(i).category);
                intent.putExtra("id",products.get(i).objectID);
                intent.putExtra("storeid",activity.storeid);
                startActivity(intent);
            }
        });
        getProducts();
        ImageView button=rootview.findViewById(R.id.addprobut);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(),AddProductFragment.class);
                intent.putExtra("mode","ADD");
                intent.putExtra("storeid",activity.storeid);
                startActivity(intent);
            }
        });
//        listView.setAdapter(adapter);
        return rootview;
    }
}
