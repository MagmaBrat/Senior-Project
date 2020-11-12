package com.example.seniorproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class TrustedAdd extends Fragment {

    ViewPager custompager;
    TrustedDialog dialog;

    public TrustedAdd(ViewPager pager,TrustedDialog d){
        custompager=pager;
        dialog=d;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.trustedadd,container,false);
        Button add=rootview.findViewById(R.id.addcontact);
        final TextView alert=rootview.findViewById(R.id.userinvalid);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText cuser=rootview.findViewById(R.id.contactuser);
                final EditText cnick=rootview.findViewById(R.id.contactnick);
                final String user=cuser.getText().toString();
                if (!user.equals("")){
                    ParseQuery<ParseUser> query=ParseUser.getQuery();
                    query.whereEqualTo("username",user);
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if (objects.size()>0){
                                try {
                                    ParseQuery<ParseObject> query1=ParseUser.getCurrentUser().getRelation("trusted").getQuery();
                                    query1.whereEqualTo("username",user);
                                    List<ParseObject> objects1=query1.find();
                                    String nick="";
                                    if (!cnick.getText().toString().equals("")){
                                        nick=cnick.getText().toString();
                                    }
                                    SlidePageAdapter adapter=(SlidePageAdapter)custompager.getAdapter();
                                    ((TrustedConfirm)adapter.getItem(1)).setFields(objects.get(0),nick);
                                    custompager.setCurrentItem(1);
                                    alert.setVisibility(View.INVISIBLE);
                                }catch (ParseException e1){

                                }
                            }else{
                                alert.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        });
        return rootview;
    }
}
