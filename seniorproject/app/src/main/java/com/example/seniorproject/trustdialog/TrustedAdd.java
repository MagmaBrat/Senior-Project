package com.example.seniorproject.trustdialog;

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

import com.example.seniorproject.R;
import com.example.seniorproject.SlidePageAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
                    dialog.setIconGesture(2);
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if (objects.size()>0){
                                try {
                                    ParseQuery<ParseObject> query1=ParseUser.getCurrentUser().getRelation("trusted").getQuery();
                                    ParseObject obj = ParseObject.createWithoutData("_User",objects.get(0).getObjectId());
                                    query1.whereEqualTo("user",obj);
                                    List<ParseObject> objects1=query1.find();
                                    Log.i("wajdi",objects1.size()+"");
                                    if (objects1.size()==0){
                                        String nick="";
                                        if (!cnick.getText().toString().equals("")){
                                            nick=cnick.getText().toString();
                                        }else{
                                            nick=objects.get(0).getString("firstname");
                                        }
                                        SlidePageAdapter adapter=(SlidePageAdapter)custompager.getAdapter();
                                        ((TrustedConfirm)adapter.getItem(1)).setFields(objects.get(0),nick);
                                        dialog.setIconGesture(1);
                                        custompager.setCurrentItem(1);
                                        alert.setVisibility(View.INVISIBLE);
                                    }else if (objects1.size()>0){
                                        dialog.setIconGesture(0);
                                        alert.setText("*contact already exists");
                                        alert.setVisibility(View.VISIBLE);
                                    }else{
                                        dialog.setIconGesture(0);
                                        Toast.makeText(dialog.activity,"An erro has occured try again later",Toast.LENGTH_LONG).show();
                                    }
                                }catch (ParseException e1){

                                }
                            }else{
                                alert.setText("*invalid user");
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
