package com.example.seniorproject.transactions;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.seniorproject.R;
import com.example.seniorproject.afterlog.AfterloginActivity;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TransactionSingleFragment extends Fragment {

    String objid;
    ArrayList<TransactionItem> items;
    ListView listView;
    TransactionItemAdapter adapter;
    AfterloginActivity activity;


    public TransactionSingleFragment(String o, AfterloginActivity a){
        objid=o;
        activity=a;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.transactionsinglefragment,container,false);
        listView=rootview.findViewById(R.id.transacsinglelist);
        items=new ArrayList<>();
        adapter=new TransactionItemAdapter(getActivity(),items);
        listView.setAdapter(adapter);
        final TextView cardnum=rootview.findViewById(R.id.transactcardnum);
        final TextView cardexp=rootview.findViewById(R.id.transactcardexp);
        final ImageView cardtype=rootview.findViewById(R.id.transactcardtype);
        TextView storename=rootview.findViewById(R.id.transactstorename);
        TextView storebranch=rootview.findViewById(R.id.transactbranch);
        TextView storeaddress=rootview.findViewById(R.id.transactaddress);
        final ParseUser user=ParseUser.getCurrentUser();
        if (user!=null){
            ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("Receipt");
            activity.makeClickable(View.VISIBLE,false);
            query.getInBackground(objid, new GetCallback<ParseObject>() {
                @Override
                public void done(final ParseObject object, ParseException e) {
                    if (e==null){
                        if (object.getString("payment").equals("Alpha")){
                            float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());
                            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) cardtype.getLayoutParams();

                            params.height=(int) pixels;
                            params.width=(int) pixels;
                            cardtype.setLayoutParams(params);
                            cardnum.setText("Alpha Balance");
                            ParseQuery<ParseUser> q=ParseUser.getQuery();
                            q.getInBackground(user.getObjectId(), new GetCallback<ParseUser>() {
                                @Override
                                public void done(ParseUser object, ParseException e) {
                                    if (e==null){
                                        float bal=object.getNumber("balance").floatValue();
                                        String newbal=new DecimalFormat("#.##").format(bal);
                                        cardexp.setText("Balance: $"+newbal);
                                        cardtype.setImageResource(R.drawable.alphalogo);
                                    }else{
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }else{
                            ParseQuery<ParseObject> q=new ParseQuery<ParseObject>("Card");
                            q.getInBackground(object.getString("payment"), new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject object, ParseException e) {
                                    if (e==null){
                                        int len=object.getString("cardnumber").length();
                                        String num="";
                                        String exp="Expires in "+object.getString("expdate");
                                        if (object.getString("type").equals("VISA")){
                                            cardtype.setImageResource(R.drawable.visa);
                                            num="Visa ****"+object.getString("cardnumber").substring(len-5,len);
                                        }else{
                                            cardtype.setImageResource(R.drawable.mastercard);
                                            num="Mastercard ****"+object.getString("cardnumber").substring(len-5,len);
                                        }
                                        cardnum.setText(num);
                                        cardexp.setText(exp);
                                    }else{
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        ParseQuery<ParseObject> query1=object.getRelation("items").getQuery();
                        query1.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e==null){
                                    for(ParseObject x:objects){
                                        try {
                                            TransactionItem item=new TransactionItem();
                                            item.quant=x.getNumber("quantity").toString();
                                            item.istax=false;
                                            String id=x.getParseObject("item").getObjectId();
                                            ParseQuery<ParseObject> query2= new ParseQuery<ParseObject>("Product");
                                            ParseObject y=query2.get(id);
                                            item.name=y.getString("name");
                                            item.price=y.getNumber("price").toString();
                                            items.add(item);
                                        }catch (ParseException e1){
                                            e1.printStackTrace();
                                        }
                                    }
                                    float subtotal=object.getNumber("subtotal").floatValue();
                                    float tax=subtotal*0.16f;
                                    String sub1=new DecimalFormat("#.##").format(subtotal);
                                    String tax1=new DecimalFormat("#.##").format(tax);
                                    TransactionItem sub=new TransactionItem();
                                    sub.name="Subtotal ";
                                    sub.price=sub1;
                                    sub.istax=true;
                                    sub.quant="0";
                                    TransactionItem taxes=new TransactionItem();
                                    taxes.name="Tax 16%";
                                    taxes.price=tax1;
                                    taxes.istax=true;
                                    taxes.quant="0";
                                    TransactionItem total=new TransactionItem();
                                    total.name="Total ";
                                    float tot=object.getNumber("total").floatValue();
                                    String tot1=new DecimalFormat("#.##").format(tot);
                                    total.price=tot1;
                                    total.istax=true;
                                    total.quant="0";
                                    items.add(sub);
                                    items.add(taxes);
                                    items.add(total);
                                    adapter.notifyDataSetChanged();
                                }
                                else{
                                    e.printStackTrace();
                                }
                            }
                        });
                    }else{
                        e.printStackTrace();
                    }
                    activity.makeClickable(View.INVISIBLE,true);
                }
            });
        }
        return rootview;
    }
}
