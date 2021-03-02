package com.example.seniorproject.afterlog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.seniorproject.App;
import com.example.seniorproject.Contact;
import com.example.seniorproject.ContactsAdapter;
import com.example.seniorproject.R;
import com.example.seniorproject.senddialog.SendDialog;
import com.example.seniorproject.trustdialog.TrustedDialog;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SendFragment extends Fragment {

    AfterloginActivity activity;
    ArrayList<Contact> contacts;
    ContactsAdapter adapter;
    ListView listView;
    ConstraintLayout constraintLayout;
    ArrayList<ParseObject> parseObjects;
    TextView balance;
    ArrayList<String> nicknames;
    SendFragment fragment;
    public SendFragment(AfterloginActivity a){
        activity=a;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public void updateStuff(){
        parseObjects.clear();
        contacts.clear();
        nicknames.clear();
        activity.getCurrentFragment();
        App.makeClickable(View.VISIBLE,false,activity.constraintLayout,activity.progressBar);
        ParseUser thisuser=ParseUser.getCurrentUser();
        if (thisuser!=null){
            ParseQuery<ParseUser> query= ParseUser.getQuery();
            query.getInBackground(thisuser.getObjectId(), new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser object, ParseException e) {
                    if (e==null){
                        double bal=object.getNumber("balance").doubleValue();
                        String newbal=new DecimalFormat("#.##").format(bal);
                        balance.setText("$"+newbal);
                        ParseQuery<ParseObject> query1=object.getRelation("trusted").getQuery();
                        try {
                            List<ParseObject> objects=query1.find();
                            if (objects!=null){
                                for (int i=0;i<objects.size();i++){
                                    ParseObject obj=objects.get(i).getParseObject("user").fetchIfNeeded();
                                    parseObjects.add(obj);
                                    nicknames.add(objects.get(i).getString("nickname"));
                                    contacts.add(new Contact(objects.get(i).getString("nickname"),  obj));
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }
                    App.makeClickable(View.INVISIBLE,true,activity.constraintLayout,activity.progressBar);
                }
            });
        }else{
            App.makeClickable(View.INVISIBLE,true,activity.constraintLayout,activity.progressBar);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.sendfragment,container,false);
        constraintLayout=rootview.findViewById(R.id.sendconst);
        contacts=new ArrayList<>();
        parseObjects=new ArrayList<>();
        nicknames=new ArrayList<>();
        adapter=new ContactsAdapter(activity,contacts);
        listView=rootview.findViewById(R.id.contactslist);
        fragment=this;
        listView.setAdapter(adapter);
        balance=rootview.findViewById(R.id.sendbal);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SendDialog custom=new SendDialog(activity,parseObjects.get(i),nicknames.get(i),fragment);
                custom.show(getFragmentManager(),"addons_fragment");
            }
        });
        ImageView imageView=rootview.findViewById(R.id.adduser);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrustedDialog custom=new TrustedDialog(activity,fragment);
                custom.show(getFragmentManager(),"addons_fragment");


            }
        });
        updateStuff();

        return rootview;
    }
}
