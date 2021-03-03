package com.example.seniorproject.storeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.seniorproject.R;
import com.google.zxing.WriterException;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QrWithdrawActivity extends AppCompatActivity {

    TextView textView;
    CountDownTimer timer;
    boolean isrunning=false;
    String storename;
    String storeid;
    ParseObject temp;
    float amount;
    ImageView imageView;
    QRGEncoder qrgEncoder;

    @Override
    protected void onPause() {
        if (isrunning){
            temp.deleteInBackground();
            timer.cancel();
            isrunning=false;
            textView.setVisibility(View.INVISIBLE);
        }
        super.onPause();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrwithdrawactivity);
        textView=findViewById(R.id.qrcounntdown);
        timer=new CountDownTimer(60000,1000) {

            @Override
            public void onTick(long l) {
                isrunning=true;
                if (l%2000<1000){
                    try {
                        Log.i("wajdi","reading");
                        ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("WithdrawPending");
                        final ParseObject object=query.get(temp.getObjectId());
                        final String userid=object.getString("userid");
                        if (userid!=null){
                            ParseUser destuser=ParseUser.getQuery().get(userid);
//                            final String username=destuser.getUsername();
                            float balance=destuser.getNumber("balance").floatValue();
                            if ((balance-amount)>=0){
                                Map<String, String> parameters = new HashMap<String, String>();
                                parameters.put("userid",userid);
                                parameters.put("val",String.valueOf(amount*-1));
                                ParseCloud.callFunctionInBackground("updatebal", parameters, new FunctionCallback<Map<String, Object>>() {
                                    @Override
                                    public void done(Map<String, Object> mapObject, ParseException e) {
                                        if (e == null) {
                                            try {
                                                ParseObject withdraw = new ParseObject("Withdraw");
                                                withdraw.put("userid",userid);
                                                withdraw.put("storeid", ParseObject.createWithoutData("Store", storeid) );
                                                withdraw.put("storename",storename);
                                                object.put("status","S");
                                                Toast.makeText(getApplicationContext(),"Payment Success!",Toast.LENGTH_LONG).show();
                                                object.save();
                                                withdraw.put("amount", amount);
                                                withdraw.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e==null){
                                                            Log.i("info","Sucessful");
                                                        }else{
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                            }catch (ParseException e1){
                                                e1.printStackTrace();
                                            }
                                        }
                                        else {
                                            e.printStackTrace();
                                            object.put("status","F");
                                            Toast.makeText(getApplicationContext(),"Payment failed!",Toast.LENGTH_LONG).show();
                                            object.saveInBackground();
                                        }
                                    }
                                });
                            }else{
                                object.put("status","F");
                                Toast.makeText(getApplicationContext(),"Payment failed!",Toast.LENGTH_LONG).show();
                                object.save();
                            }
                            timer.cancel();
                        }
                    }catch (ParseException e){
                        e.printStackTrace();
                    }
                }
                textView.setText("Expires in "+(l/1000)+"s");

            }

            @Override
            public void onFinish() {
                isrunning=false;
                try {
                    ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("WithdrawPending");
                    ParseObject object=query.get(temp.getObjectId());
                    object.delete();
                    textView.setVisibility(View.INVISIBLE);
                }catch (ParseException e){
                    e.printStackTrace();
                }
            }
        };
        Intent intent=getIntent();
        if (intent.hasExtra("qrstuff")){
            amount=intent.getFloatExtra("amount",0f);
            storeid=intent.getStringExtra("storeid");
            storename=intent.getStringExtra("storename");
            imageView=findViewById(R.id.withdrawqrscancode);
            final ParseObject parseObject=new ParseObject("WithdrawPending");
            parseObject.put("storeid",storeid);
            parseObject.put("status","NA");
            temp=parseObject;
            parseObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e==null){
                        try {
                            textView.setVisibility(View.VISIBLE);
                            timer.start();
                            String inputValue=storename+"@"+amount+"@"+parseObject.getObjectId();
                            qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT,800);
                            Bitmap bitmap=qrgEncoder.encodeAsBitmap();
                            imageView.setImageBitmap(bitmap);
                        }catch (WriterException e1){
                            e1.printStackTrace();
                        }
                    }else{
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
