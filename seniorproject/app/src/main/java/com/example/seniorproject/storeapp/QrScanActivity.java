package com.example.seniorproject.storeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seniorproject.R;
import com.example.seniorproject.SlidePageAdapter;
import com.example.seniorproject.trustdialog.TrustedDone;
import com.google.zxing.WriterException;
import com.parse.FunctionCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QrScanActivity extends AppCompatActivity {

    QRGEncoder qrgEncoder;
    ImageView imageView;
    CountDownTimer timer;
    ArrayList<String> ids;
    ArrayList<Integer> quants;
    float subtotal;
    float total;
    String store;
    String storename;
    String clerk="Clerk1";
    ParseObject temp;
    boolean isrunning=false;
    TextView textView;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);
        textView=findViewById(R.id.qrcounntdown);
        timer=new CountDownTimer(60000,1000) {

            @Override
            public void onTick(long l) {
                isrunning=true;
                Log.i("wajdi",l+"");
                if (l%2000<1000){
                    try {
                        Log.i("wajdi","reading");
                        ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("Pending");
                        final ParseObject object=query.get(temp.getObjectId());
                        String userid=object.getString("userid");
                        if (userid!=null){
                            if (object.getString("payment").equals("Alpha")){
                                ParseUser destuser=ParseUser.getQuery().get(userid);
                                final String username=destuser.getUsername();
                                Log.i("monther","its done "+username);
                                float balance=destuser.getNumber("balance").floatValue();
                                if ((balance-total)>=0){
                                    Log.i("monther","sss "+total);
                                    Map<String, String> parameters = new HashMap<String, String>();
                                    parameters.put("userid",userid);
                                    parameters.put("val",String.valueOf(total*-1));
                                    ParseCloud.callFunctionInBackground("updatebal", parameters, new FunctionCallback<Map<String, Object>>() {
                                        @Override
                                        public void done(Map<String, Object> mapObject, ParseException e) {
                                            if (e == null) {
                                                try {
                                                    ParseObject receipt = new ParseObject("Receipt");
                                                    receipt.put("username",username);
                                                    receipt.put("storeid",store);
                                                    receipt.put("store",storename);
                                                    object.put("status","S");
                                                    Toast.makeText(getApplicationContext(),"Payment Success!",Toast.LENGTH_LONG).show();
                                                    receipt.put("payment","Alpha");
                                                    object.save();
                                                    ParseRelation<ParseObject> relation = receipt.getRelation("items");
                                                    for (int i = 0; i < ids.size(); i++) {
                                                        ParseObject newreceipt = new ParseObject("ReceiptItem");
                                                        newreceipt.put("item", ParseObject.createWithoutData("Product", ids.get(i)));
                                                        newreceipt.put("quantity", quants.get(i));
                                                        relation.add(newreceipt);
                                                        newreceipt.save();
                                                    }
                                                    receipt.put("total", total);
                                                    receipt.put("subtotal", subtotal);
                                                    receipt.save();
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
                            }else if (object.getString("payment") !=null){
                                ParseUser destuser=ParseUser.getQuery().get(userid);
                                final String username=destuser.getUsername();
                                ParseQuery<ParseObject> q=new ParseQuery<ParseObject>("Card");
                                ParseObject card=q.get(object.getString("payment"));
                                float cardb=card.getNumber("balance").floatValue();
                                if ((total-cardb)>=0){
                                    card.add("balance",(-1*total));
                                    ParseObject receipt = new ParseObject("Receipt");
                                    receipt.put("username",username);
                                    receipt.put("storeid",store);
                                    receipt.put("store",storename);
                                    receipt.put("payment",object.getString("payment"));
                                    object.put("status","S");
                                    Toast.makeText(getApplicationContext(),"Payment Success!",Toast.LENGTH_LONG).show();
                                    object.save();
                                    ParseRelation<ParseObject> relation = receipt.getRelation("items");
                                    for (int i = 0; i < ids.size(); i++) {
                                        ParseObject newreceipt = new ParseObject("ReceiptItem");
                                        newreceipt.put("item", ParseObject.createWithoutData("Product", ids.get(i)));

                                        newreceipt.put("quantity", quants.get(i));
                                        relation.add(newreceipt);
                                        newreceipt.save();
                                    }
                                    receipt.put("total", total);
                                    receipt.put("subtotal", subtotal);
                                    receipt.save();

                                }else{
                                    object.put("status","F");
                                    Toast.makeText(getApplicationContext(),"Payment failed!",Toast.LENGTH_LONG).show();
                                    object.save();
                                }
                                timer.cancel();
                            }
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
                    ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("Pending");
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
            ids=intent.getStringArrayListExtra("ids");
            quants=intent.getIntegerArrayListExtra("quants");
            subtotal=intent.getFloatExtra("subtotal",0f);
            total=intent.getFloatExtra("total",0f);
            Log.i("monther",total+"");
            store=intent.getStringExtra("store");
            storename=intent.getStringExtra("storename");
            imageView=findViewById(R.id.qrscancode);
                final ParseObject parseObject=new ParseObject("Pending");
                parseObject.put("total",total);
                parseObject.put("storename",store);
                parseObject.put("clerk",clerk);
                temp=parseObject;
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e==null){
                            try {
                                textView.setVisibility(View.VISIBLE);
                                timer.start();
                                String inputValue=store+"@"+total+"@"+ clerk+"@"+parseObject.getObjectId();
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
}
