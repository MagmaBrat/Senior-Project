package com.example.seniorproject.payment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
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

public class PaymentConfirm extends Fragment {

    EditText metod;
    EditText total;
    String objid;
    PaymentDialog paymentDialog;
    String payment;

    public PaymentConfirm(String id,PaymentDialog dialog){
        objid=id;
        paymentDialog=dialog;
    }

    public void fillStuff(String method,String totall,String p){
        metod.setText(method);
        total.setText(totall);
        payment=p;
    }

    public void changeVisibility(int visibility,boolean clickable,ViewGroup r){
        CardView cardView=r.findViewById(R.id.paymentcard2);
        cardView.setClickable(clickable);
        paymentDialog.changeVisibility(visibility,clickable);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.paymentconfirm,container,false);
        metod=rootview.findViewById(R.id.paymentmetod);
        total=rootview.findViewById(R.id.paymenttotal);
        Button button=rootview.findViewById(R.id.paymentcont);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ParseUser user=ParseUser.getCurrentUser();
                if (user!=null){
                    ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("Pending");
                    paymentDialog.setIconGesture(2);
                    query.getInBackground(objid, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if (e==null){
                                object.put("userid",user.getObjectId());
                                object.put("payment",payment);
                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e==null){
                                            new CountDownTimer(60000,1500){

                                                @Override
                                                public void onTick(long l) {
                                                    ParseQuery<ParseObject> query1=new ParseQuery<ParseObject>("Pending");
                                                    query1.getInBackground(objid, new GetCallback<ParseObject>() {
                                                        @Override
                                                        public void done(ParseObject object, ParseException e) {
                                                            if (e==null){
                                                                if (object.getString("status").equals("S")){
                                                                    object.deleteInBackground();
                                                                    paymentDialog.setIconGesture(3);
                                                                    paymentDialog.addonsviewpager.setCurrentItem(2);
                                                                    cancel();
                                                                    paymentDialog.setIconGesture(1);
                                                                    SlidePageAdapter adapter=(SlidePageAdapter)paymentDialog.addonsviewpager.getAdapter();
                                                                    ((PaymentDone)adapter.getItem(2)).showPage();
                                                                }else if (object.getString("status").equals("F")){
                                                                    Toast.makeText(getActivity(),"Insufficient funds, please try again later"
                                                                    ,Toast.LENGTH_LONG).show();
                                                                    object.deleteInBackground();
                                                                    cancel();
                                                                    paymentDialog.setIconGesture(1);
                                                                    paymentDialog.dismiss();
                                                                }
                                                            }else{
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onFinish() {
                                                    ParseQuery<ParseObject> query1=new ParseQuery<ParseObject>("Pending");
                                                    query1.getInBackground(objid, new GetCallback<ParseObject>() {
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
                                                                        paymentDialog.dismiss();
                                                                    }
                                                                }.start();
                                                                object.deleteInBackground();
                                                            }else{
                                                                e.printStackTrace();
                                                            }
                                                            paymentDialog.setIconGesture(1);
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
