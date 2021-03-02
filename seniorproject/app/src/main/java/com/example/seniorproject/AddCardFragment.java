package com.example.seniorproject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.seniorproject.afterlog.AfterloginActivity;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.vinaygaba.creditcardview.CardType;
import com.vinaygaba.creditcardview.CreditCardView;

import java.util.ArrayList;

public class AddCardFragment extends Fragment {

    AfterloginActivity activity;
    CreditCardView cardView;
    ArrayList<EditText> texts;
    ArrayList<TextView> alerts;

    public AddCardFragment(AfterloginActivity a){
        activity=a;
    }

    public void checkFields(){
        int[] visibilities=new int[4];
        boolean isempty=false;
        if (texts.get(0).toString().equals("")){
            isempty=true;
            visibilities[0]=View.VISIBLE;
        }else{
            visibilities[0]=View.INVISIBLE;
        }
        if (texts.get(1).toString().equals("")){
            isempty=true;
            visibilities[1]=View.VISIBLE;
        }else{
            visibilities[1]=View.INVISIBLE;
        }
        if (texts.get(2).toString().equals("")||texts.get(3).toString().equals("")){
            isempty=true;
            visibilities[2]=View.VISIBLE;
        }else{
            visibilities[2]=View.INVISIBLE;
        }
        if (texts.get(4).toString().equals("")){
            isempty=true;
            visibilities[3]=View.VISIBLE;
        }else{
            visibilities[3]=View.INVISIBLE;
        }
        if (!isempty){
            ParseUser user=ParseUser.getCurrentUser();
            if (user!=null){
                ParseQuery<ParseUser> query=ParseUser.getQuery();
                query.getInBackground(user.getObjectId(), new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser object, ParseException e) {
                        if (e==null){
                            try {
                                ParseObject obj=new ParseObject("Card");
                                obj.put("cardnumber",cardView.getCardNumber());
                                obj.put("cardholder",cardView.getCardName());
                                obj.put("expdate",cardView.getExpiryDate());
                                obj.put("cvv",texts.get(4).getText().toString());
                                if (cardView.getType()== CardType.VISA){
                                    obj.put("type","VISA");
                                }else if (cardView.getType()== CardType.MASTERCARD){
                                    obj.put("type","MASTERCARD");
                                }else{
                                    obj.put("type","VISA");
                                }
                                obj.save();
                                object.getRelation("cards").add(obj);
                                object.save();
                                Toast.makeText(activity,"Card added successfully",Toast.LENGTH_LONG).show();
                                activity.current=new ListCardsFragment(activity);
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,activity.current).commit();
                            }catch (ParseException e1){
                                e1.printStackTrace();
                            }
                        }else{

                        }
                    }
                });
            }
        }else {
            for (int i=0;i<alerts.size();i++){
                alerts.get(i).setText("*required");
                alerts.get(i).setVisibility(visibilities[i]);
            }
        }
    }

    public void setFields(){
        for(final EditText x:texts){
            x.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Log.i("monther",cardView.getType()+"");
                    int index=Integer.parseInt(x.getTag().toString());
                    String str=charSequence.toString();
                    if (index==0){
                        cardView.setCardNumber(str);
                    }else if (index==1){
                        cardView.setCardName(str);
                    }
                    else if (index==2){
//                        int ind=charSequence.length();
//                        char c=charSequence.charAt(ind-1);
//                        boolean twodig=false;
//                        if (ind==1 && ( c>='2' && c<='9')){
//                            texts.get(i).setText("0"+c);
//                        }else  if (ind==1 && c=='1'){
//                            twodig=true;
//                        }
                        String dato=cardView.getExpiryDate();
                        String[] div=dato.split("/");
                        if (div.length==0){
                            cardView.setExpiryDate(str+"/");
                        }else if (div.length==1){
                            cardView.setExpiryDate(str+"/");
                        }else {
                            cardView.setExpiryDate(str+"/"+div[1]);
                        }
                    }else if (index==3){
                        String dato=cardView.getExpiryDate();
                        String[] div=dato.split("/");
                        if (div.length==0){
                            cardView.setExpiryDate("00/"+str);
                        }else {
                            cardView.setExpiryDate(div[0]+"/"+str);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.cardfragment,container,false);
        cardView=rootview.findViewById(R.id.card1);
        texts=new ArrayList<>();
        texts.add((EditText) rootview.findViewById(R.id.cardnumview));
        texts.add((EditText) rootview.findViewById(R.id.cardholder));
        texts.add((EditText) rootview.findViewById(R.id.cardm));
        texts.add((EditText) rootview.findViewById(R.id.cardy));
        texts.add((EditText) rootview.findViewById(R.id.cardcvv));
        alerts=new ArrayList<>();
        alerts.add((TextView) rootview.findViewById(R.id.cardalert1));
        alerts.add((TextView) rootview.findViewById(R.id.cardalert2));
        alerts.add((TextView) rootview.findViewById(R.id.cardalert3));
        alerts.add((TextView) rootview.findViewById(R.id.cardalert4));
        setFields();
        Button button=rootview.findViewById(R.id.addcard);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFields();
            }
        });
        return rootview;
    }
}
