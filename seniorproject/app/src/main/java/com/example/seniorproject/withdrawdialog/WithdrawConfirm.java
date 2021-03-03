package com.example.seniorproject.withdrawdialog;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.seniorproject.R;
import com.example.seniorproject.SlidePageAdapter;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class WithdrawConfirm extends Fragment {

    EditText wstore,wamount;
    WithdrawDialog dialog;
    String id;
    String amount,storename;

    public WithdrawConfirm(WithdrawDialog d,String a,String i,String s){
        id=i;
        dialog=d;
        amount=a;
        storename=s;
    }

    public void changeVisibility(int visibility,boolean clickable,ViewGroup r){
        CardView cardView=r.findViewById(R.id.paymentcard2);
        cardView.setClickable(clickable);
        dialog.changeVisibility(visibility,clickable);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.withdrawconfirm,container,false);
        wstore=rootview.findViewById(R.id.withdrawstore);
        wamount=rootview.findViewById(R.id.withdrawamount);
        wstore.setText(storename);
        wamount.setText("$"+amount);
        Button button=rootview.findViewById(R.id.withdrawcont);
        dialog.setIconGesture(0);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ParseUser user=ParseUser.getCurrentUser();
                if (user!=null){
                    ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("WithdrawPending");
                    dialog.setIconGesture(2);
                    query.getInBackground(id, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if (e==null){
                                object.put("userid",user.getObjectId());
                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e==null){
                                            new CountDownTimer(60000,1500){

                                                @Override
                                                public void onTick(long l) {
                                                    ParseQuery<ParseObject> query1=new ParseQuery<ParseObject>("WithdrawPending");
                                                    query1.getInBackground(id, new GetCallback<ParseObject>() {
                                                        @Override
                                                        public void done(ParseObject object, ParseException e) {
                                                            if (e==null){
                                                                if (object.getString("status").equals("S")){
                                                                    object.deleteInBackground();
                                                                    dialog.setIconGesture(3);
                                                                    dialog.addonsviewpager.setCurrentItem(1);
                                                                    cancel();
                                                                    SlidePageAdapter adapter=(SlidePageAdapter)dialog.addonsviewpager.getAdapter();
                                                                    ((WithdrawDone)adapter.getItem(1)).showPage();
                                                                }else if (object.getString("status").equals("F")){
                                                                    Toast.makeText(getActivity(),"Insufficient funds, please try again later"
                                                                            ,Toast.LENGTH_LONG).show();
                                                                    object.deleteInBackground();
                                                                    cancel();
                                                                    dialog.setIconGesture(0);
                                                                    dialog.dismiss();
                                                                }
                                                            }else{
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onFinish() {
                                                    ParseQuery<ParseObject> query1=new ParseQuery<ParseObject>("WithdrawPending");
                                                    query1.getInBackground(id, new GetCallback<ParseObject>() {
                                                        @Override
                                                        public void done(ParseObject object, ParseException e) {
                                                            if (e==null){
                                                                changeVisibility(View.VISIBLE,false,rootview);
                                                                new CountDownTimer(5000,1000){

                                                                    @Override
                                                                    public void onTick(long l) {

                                                                    }

                                                                    @Override
                                                                    public void onFinish() {
                                                                        dialog.dismiss();
                                                                    }
                                                                }.start();
                                                                object.deleteInBackground();
                                                            }else{
                                                                e.printStackTrace();
                                                            }
                                                            dialog.setIconGesture(0);
                                                        }
                                                    });
                                                }
                                            }.start();
                                        }else{
                                            e.printStackTrace();
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
        });
        return rootview;
    }
}
