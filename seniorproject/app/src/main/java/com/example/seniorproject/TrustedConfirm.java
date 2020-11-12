package com.example.seniorproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class TrustedConfirm extends Fragment {

    TrustedDialog dialog;
    ViewPager pager;
    TextView nick,full,user;
    ParseUser parseUser;
    String nicko;


    public TrustedConfirm(ViewPager p,TrustedDialog d){
        dialog=d;
        pager=p;
    }

    public void setFields(ParseUser u,String n){
        nicko=n;
        parseUser=u;
        nick.setText(n);
        full.setText(u.get("firstname")+" "+u.get("lastname"));
        user.setText(u.getUsername());

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.trusedconfirm,container,false);
        nick=rootview.findViewById(R.id.confirmnick);
        full=rootview.findViewById(R.id.confirmfull);
        user=rootview.findViewById(R.id.confirmuser);
        Button button=rootview.findViewById(R.id.confirmbut);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (parseUser!=null){
                    ParseQuery<ParseUser> query=ParseUser.getQuery();
                    final ParseUser current=ParseUser.getCurrentUser();
                    query.getInBackground(current.getObjectId(), new GetCallback<ParseUser>() {
                        @Override
                        public void done(ParseUser object, ParseException e) {
                            if (e==null){
                                ParseObject trusted=new ParseObject("Contacts");
                                trusted.put("nickname",nicko);
                                trusted.put("user",ParseObject.createWithoutData("_User",parseUser.getObjectId()));
                                try {
                                    trusted.save();
                                    object.getRelation("trusted").add(trusted);
                                    object.save();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                    Log.i("osama",e1.getMessage());
                                }
                            }else{
                                Log.i("wajdi",e.getMessage());
                            }
                        }
                    });
                }
            }
        });
        return rootview;
    }


}
