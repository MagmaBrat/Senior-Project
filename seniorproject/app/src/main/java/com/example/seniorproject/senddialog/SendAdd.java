package com.example.seniorproject.senddialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.seniorproject.afterlog.AfterloginActivity;
import com.example.seniorproject.R;
import com.example.seniorproject.SlidePageAdapter;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

public class SendAdd extends Fragment {


    EditText editText;
    Spinner spinner;
    AfterloginActivity activity;
    String nick;
    ViewPager pager;
    SendDialog dialog;
    ParseObject user;

    public SendAdd(ParseObject o, AfterloginActivity a, String n, SendDialog d, ViewPager p){
        activity=a;
        nick=n;
        user=o;
        pager=p;
        dialog=d;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.sendadd,container,false);
        Button button=rootview.findViewById(R.id.sendaddbut);
        editText=rootview.findViewById(R.id.sendfield);
        spinner=rootview.findViewById(R.id.sendspinner);
        ArrayList<String> reasons=new ArrayList<>();
        reasons.add("Select Reason");
        final TextView alert=rootview.findViewById(R.id.sendalert);
        reasons.add("Personal Donation");
        reasons.add("Debt");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setIconGesture(2);
                String text=editText.getText().toString();
                if (!text.equals("")){
                    final float val=Float.valueOf(text);
                    ParseUser myuser=ParseUser.getCurrentUser();
                    if (myuser!=null){
                        ParseQuery<ParseUser> query=ParseUser.getQuery();
                        query.getInBackground(myuser.getObjectId(), new GetCallback<ParseUser>() {
                            @Override
                            public void done(ParseUser object, ParseException e) {
                                if (e==null){
                                    double bal=object.getNumber("balance").doubleValue();
                                    if ((bal-val)>=0){
                                        String reason=spinner.getSelectedItem().toString();
                                        if (reason.equals("Select Reason")){
                                            reason="Not Specified";
                                        }
                                        dialog.setIconGesture(1);
                                        alert.setVisibility(View.INVISIBLE);
                                        SlidePageAdapter adapter=(SlidePageAdapter)pager.getAdapter();
                                        ((SendConfirm)adapter.getItem(1)).setStuff(reason,nick,val,object);
                                        pager.setCurrentItem(1);
                                    }else{
                                        alert.setVisibility(View.VISIBLE);
                                        alert.setText("*no sufficient balance");
                                        dialog.setIconGesture(0);
                                    }
                                }
                            }
                        });
                    }
                }else{
                    dialog.setIconGesture(0);
                    alert.setVisibility(View.VISIBLE);
                    alert.setText("required");

                }
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.spinner_item,reasons);
        spinner.setAdapter(adapter);

        return rootview;
    }
}
