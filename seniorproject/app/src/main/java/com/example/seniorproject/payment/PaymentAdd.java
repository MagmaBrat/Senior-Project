package com.example.seniorproject.payment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.seniorproject.CardsAdapter;
import com.example.seniorproject.R;
import com.example.seniorproject.SlidePageAdapter;
import com.example.seniorproject.storeapp.CartAdapter;
import com.example.seniorproject.trustdialog.TrustedConfirm;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PaymentAdd extends Fragment {

    PaymentAdapter adapter;
    ListView listView;
    String purchase;
    PaymentDialog paymentDialog;
    ConstraintLayout constraintLayout;
    public PaymentAdd(String t,PaymentDialog d){
        purchase=t;
        paymentDialog=d;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.paymentadd,container,false);
        final TextView balance=rootview.findViewById(R.id.textView37);
        final TextView total=rootview.findViewById(R.id.textView39);
        total.setText("$"+purchase);
        listView=rootview.findViewById(R.id.paymentslist);
        final ArrayList<Card> cards=new ArrayList<>();
        constraintLayout=rootview.findViewById(R.id.constraintLayout);
        adapter=new PaymentAdapter(getActivity(),cards);
        listView.setAdapter(adapter);
        ParseUser user=ParseUser.getCurrentUser();
        if (user!=null){
            ParseQuery<ParseUser> query=ParseUser.getQuery();
            paymentDialog.setIconGesture(2);
            query.getInBackground(user.getObjectId(), new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser object, ParseException e) {
                    if (e==null){
                        double bal=object.getNumber("balance").doubleValue();
                        String newbal=new DecimalFormat("#.##").format(bal);
                        balance.setText("$"+newbal);
                        ParseQuery<ParseObject> query1=object.getRelation("cards").getQuery();
                        query1.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e==null){
                                    for (ParseObject x:objects){
                                        Card c=new Card(x.getString("cardnumber"),x.getString("cardholer"),
                                        x.getString("expdate"),
                                        x.getString("type"),x.getObjectId());
                                        cards.add(c);
                                    }
                                    adapter.notifyDataSetChanged();
                                }else{
                                    e.printStackTrace();
                                }
                            }
                        });
                    }else{
                        e.printStackTrace();
                    }
                    paymentDialog.setIconGesture(0);
                }
            });
        }
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentDialog.setIconGesture(1);
                paymentDialog.addonsviewpager.setCurrentItem(1);
                SlidePageAdapter adapter=(SlidePageAdapter)paymentDialog.addonsviewpager.getAdapter();
                ((PaymentConfirm)adapter.getItem(1)).fillStuff("Alpha balance","$"+purchase,"Alpha");
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Card card=cards.get(i);
                paymentDialog.setIconGesture(1);
                paymentDialog.addonsviewpager.setCurrentItem(1);
                SlidePageAdapter adapter=(SlidePageAdapter)paymentDialog.addonsviewpager.getAdapter();
                int len=card.number.length();
                String desc=card.type.substring(0,1)+card.type.substring(1).toLowerCase()+" Ending with "+card.number.substring(len-5,len);
                ((PaymentConfirm)adapter.getItem(1)).fillStuff(desc,"$"+purchase,card.id);
            }
        });


        return rootview;
    }
}
