package com.example.seniorproject;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

public class Step1 extends Fragment {

    boolean[] containsth;
    final int MINPASS=1;
    boolean allowed=false;
    ArrayList<EditText> texts;
    ImageView nexto;
    public CustomViewPager custom;
    TextView alert1,alert2;

    public Step1(CustomViewPager pager){
        custom=pager;
    }



    public void setTextFields(ViewGroup group){
        for(final EditText text: texts){
            text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    int index=Integer.parseInt(text.getTag().toString());
                    if ((index !=6 && charSequence.length()==1) || (index==6 && charSequence.length()==4)){
                        containsth[index]=true;
                        int counta=0;
                        for (int c=0;c<containsth.length;c++){
                            if (containsth[c]){
                                counta++;
                            }else{
                                break;
                            }
                        }
                        if (counta==7){
                            allowed=true;
                            nexto.setAlpha(1f);
                            //custom.allowed=true;
                        }else{
                            if (allowed){
                                allowed=false;
                                nexto.setAlpha(0.25f);
                                //custom.allowed=false;
                            }
                        }
                    }else if (charSequence.length()<=0){
                        containsth[index]=false;
                        if (allowed){
                            allowed=false;
                            nexto.setAlpha(0.25f);
                            //custom.allowed=false;
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }

        for(int c=0;c<3;c++){
            texts.get(c).setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    int len=((EditText)view).getText().toString().length();
                        if (keyEvent.getAction()==KeyEvent.ACTION_DOWN) {
                            if (len == 0) {
                                char ch = (char) keyEvent.getUnicodeChar(keyEvent.getMetaState());
                                if (!Character.isLetter(ch)) {
                                    return true;
                                }
                                return false;
                            }
                        }
                    return false;
                }
            });
        }
    }

    public String checkFields(){
        int day=Integer.parseInt(texts.get(4).getText().toString());
        int month=Integer.parseInt(texts.get(5).getText().toString());
        int year=Integer.parseInt(texts.get(6).getText().toString());
        String msg1="";
        String msg2="";
        if (texts.get(3).getText().length()<MINPASS){
            msg1="the password must have at least 8 characters";
        }
        if (day>31 && month>12){
            msg2="Invalid Date";
        }else{
            LocalDate birth=LocalDate.of(year,month,day);
            LocalDate now=LocalDate.now();
            int years=Period.between(birth,now).getYears();
            if (years<18 && years>=0){
                msg2="Must be 18+ to create an account";
            }else if(years<0 || years>120){
                msg2="Invalid year";
            }
        }

        if (msg1.equals("") && msg2.equals("")){
            alert1.setVisibility(View.INVISIBLE);
            alert2.setVisibility(View.INVISIBLE);
            return day+"/"+month+"/"+year;
        }else{
            if (!msg1.equals("")){
                alert1.setText(msg1);
                alert1.setVisibility(View.VISIBLE);
            }else{
                alert1.setVisibility(View.INVISIBLE);
            }
            if (!msg2.equals("")){
                alert2.setText(msg2);
                alert2.setVisibility(View.VISIBLE);
            }else{
                alert2.setVisibility(View.INVISIBLE);
            }
            return "";
        }
    }

    public void getInterval(View view){
        int day=Integer.parseInt(texts.get(4).getText().toString());
        int month=Integer.parseInt(texts.get(5).getText().toString());
        int year=Integer.parseInt(texts.get(6).getText().toString());
        LocalDate birth=LocalDate.of(year,month,day);
        LocalDate now=LocalDate.now();
        String years=Period.between(birth,now).getYears()+"";
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.step1,container,false);
        texts=new ArrayList<>();
        texts.add((EditText) rootview.findViewById(R.id.firsto));
        texts.add((EditText) rootview.findViewById(R.id.lasto));
        texts.add((EditText) rootview.findViewById(R.id.usero));
        texts.add((EditText) rootview.findViewById(R.id.passo));
        texts.add((EditText) rootview.findViewById(R.id.dato1));
        texts.add((EditText) rootview.findViewById(R.id.dato2));
        texts.add((EditText) rootview.findViewById(R.id.dato3));
        alert1=rootview.findViewById(R.id.step1alert1);
        alert2=rootview.findViewById(R.id.step1alert2);
        containsth=new boolean[]{false,false,false,false,false,false,false};
        ImageView step2=(ImageView) rootview.findViewById(R.id.schritt2);
        nexto=(ImageView) rootview.findViewById(R.id.nextschritt);
        Button button=(Button) rootview.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInterval(view);
            }
        });
        nexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allowed){
                    String d=checkFields();
                    if (!d.equals("")){
                        Bundle bundle=new Bundle();
                        String first=texts.get(0).getText().toString();
                        String last=texts.get(1).getText().toString();
                        bundle.putString("firstname",first.substring(0,1).toUpperCase()+first.substring(1));
                        bundle.putString("lastname",last.substring(0,1).toUpperCase()+last.substring(1));
                        bundle.putString("username",texts.get(2).getText().toString());
                        bundle.putString("pass",texts.get(3).getText().toString());
                        bundle.putString("birth",d);
                        SlidePageAdapter adapter=(SlidePageAdapter)custom.getAdapter();
                        ((Step2)adapter.getItem(1)).initFunc(bundle);
                        custom.setCurrentItem(1);
                    }
                }
            }
        });
        step2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allowed){
                    String d=checkFields();
                    if (!d.equals("")){
                        Bundle bundle=new Bundle();
                        bundle.putString("firstname",texts.get(0).getText().toString());
                        bundle.putString("lastname",texts.get(1).getText().toString());
                        bundle.putString("username",texts.get(2).getText().toString());
                        bundle.putString("pass",texts.get(3).getText().toString());
                        bundle.putString("birth",d);
                        SlidePageAdapter adapter=(SlidePageAdapter)custom.getAdapter();
                        ((Step2)adapter.getItem(1)).initFunc(bundle);
                        custom.setCurrentItem(1);
                    }
                }
            }
        });
        setTextFields(rootview);
        return rootview;
    }
}
