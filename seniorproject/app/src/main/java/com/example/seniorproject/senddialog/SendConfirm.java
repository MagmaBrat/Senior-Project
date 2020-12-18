package com.example.seniorproject.senddialog;

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

import com.example.seniorproject.R;
import com.example.seniorproject.SlidePageAdapter;
import com.example.seniorproject.trustdialog.TrustedDone;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.Map;

public class SendConfirm extends Fragment {

    ParseObject user;
    SendDialog dialog;
    String reason,nick;
    float value;
    ViewPager viewPager;
    TextView nicko,amounto,reasono;
    ParseUser myuser;

    public void setStuff(String r, String n, float v,ParseUser u){
        reason=r;
        nick=n;
        value=v;
        myuser=u;
        nicko.setText(nick);
        amounto.setText("$"+value);
        reasono.setText(reason);
    }

    public SendConfirm(ParseObject object, SendDialog d, ViewPager pager){
        user=object;
        dialog=d;
        viewPager=pager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.sendconfirm,container,false);
        nicko=rootview.findViewById(R.id.sendconfirmnick);
        amounto=rootview.findViewById(R.id.confirmamount);
        reasono=rootview.findViewById(R.id.confirmreason);
        Button button=rootview.findViewById(R.id.sendconfirmbut);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseQuery<ParseUser> query=ParseUser.getQuery();
                query.getInBackground(myuser.getObjectId(), new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser object, ParseException e) {
                        try {
                            if (e==null){
                                double number=object.getNumber("balance").doubleValue();
                                object.increment("balance",(value*-1));
//                                object.put("balance",(number-value));
                                object.save();
                                Map<String, String> parameters = new HashMap<String, String>();
                                parameters.put("userid",user.getObjectId());
                                parameters.put("val",String.valueOf(value));
                                ParseCloud.callFunctionInBackground("updatebal", parameters, new FunctionCallback<Map<String, Object>>() {
                                    @Override
                                    public void done(Map<String, Object> mapObject, ParseException e) {
                                        if (e == null) {
                                            Log.i("monther","success");
                                            dialog.setIconGesture(3);
                                            viewPager.setCurrentItem(2);
                                            SlidePageAdapter adapter=(SlidePageAdapter)viewPager.getAdapter();
                                            ((TrustedDone)adapter.getItem(2)).showPage();
                                        }
                                        else {
                                            Log.i("monther",e.getMessage());
                                        }
                                    }
                                });

                            }else{
                                Log.i("info",e.getMessage());
                            }

                        }catch (ParseException e1){
                            Log.i("info",e1.getMessage());
                        }
                    }
                });

            }
        });
        return rootview;
    }
}
