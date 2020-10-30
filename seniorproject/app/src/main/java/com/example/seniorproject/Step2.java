package com.example.seniorproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Step2 extends Fragment {

    //#2EB62C

    public CustomViewPager custom;
    Bundle bundle;
    boolean[] containsth;
    ArrayList<EditText> texts;
    boolean allowed=false;
    ImageView nexto,prev,step1,step2;
    RadioGroup radioGroup;
    TextView alert1,alert2,alert3,alert4;

    public void setTextFields(ViewGroup group){
        for(final EditText text: texts){
            text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    int index=Integer.parseInt(text.getTag().toString());
                    if ((index !=2 && charSequence.length()==1) || (index==2 && charSequence.length()==10) ){
                        containsth[index]=true;
                        int counta=0;
                        for (int c=0;c<containsth.length;c++){
                            if (containsth[c]){
                                counta++;
                            }else{
                                break;
                            }
                        }
                        if (counta==3){
                            allowed=true;
                            nexto.setAlpha(1f);
                            custom.allowed=true;
                        }else{
                            if (allowed){
                                allowed=false;
                                nexto.setAlpha(0.25f);
                                custom.allowed=false;
                            }
                        }
                    }else if (charSequence.length()<=0){
                        containsth[index]=false;
                        if (allowed){
                            allowed=false;
                            nexto.setAlpha(0.25f);
                            custom.allowed=false;
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }
    }

    public Step2(CustomViewPager pager){
        custom=pager;
    }

    public boolean checkFields(){
        String phone=texts.get(2).getText().toString();
        String email=texts.get(0).getText().toString();
        String repeat=texts.get(1).getText().toString();
        String ext=phone.substring(0,3);
        String msg2="";
        String msg3="";
        String msg1="";
        if (phone.length()!=10){
            msg2="*Invalid Phone number: must contain 10 digits";
        }else if (!ext.equals("079")&& !ext.equals("078") && !ext.equals("077")){
            msg2="*Invalid Phone number";
        }else{
            alert1.setVisibility(View.INVISIBLE);
        }
        if (radioGroup.getCheckedRadioButtonId()==-1){
            msg3="*No Gender is specified";
        }else{
            alert2.setVisibility(View.INVISIBLE);
        }
        if (!email.equals(repeat)){
            msg1="*The repeat email doesn't match";
        }else{
            alert3.setVisibility(View.INVISIBLE);
        }
        if (msg1.equals("") && msg2.equals("") && msg3.equals("")){
            alert1.setVisibility(View.INVISIBLE);
            alert2.setVisibility(View.INVISIBLE);
            alert3.setVisibility(View.INVISIBLE);
            return true;
        }else{
            if (!msg1.equals("")){
                alert1.setVisibility(View.VISIBLE);
                alert1.setText(msg1);
            }
            if (!msg2.equals("")){
                alert2.setVisibility(View.VISIBLE);
                alert2.setText(msg2);
            }
            if (!msg3.equals("")){
                alert3.setVisibility(View.VISIBLE);
                alert3.setText(msg3);
            }
            return false;
        }
    }

    public void initFunc(Bundle b){
        bundle=b;
    }

    public void clearStuff(){
        for(int i=0;i<containsth.length;i++){
            containsth[i]=false;
        }
        if (allowed){
            allowed=false;
            nexto.setAlpha(0.25f);
            alert1.setVisibility(View.INVISIBLE);
            alert2.setVisibility(View.INVISIBLE);
            alert3.setVisibility(View.INVISIBLE);
            alert4.setVisibility(View.INVISIBLE);
        }
        for(int i=0;i<texts.size();i++){
            texts.get(i).setText("");
        }

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.step2,container,false);
        texts=new ArrayList<>();
        texts.add((EditText) rootview.findViewById(R.id.mailo));
        texts.add((EditText) rootview.findViewById(R.id.repeato));
        texts.add((EditText) rootview.findViewById(R.id.phono));
        containsth=new boolean[]{false,false,false};
        nexto=rootview.findViewById(R.id.step2nextschritt);
        prev=rootview.findViewById(R.id.step2prevschritt);
        step1=rootview.findViewById(R.id.step2schritt1);
        step2=rootview.findViewById(R.id.step2schritt2);
        alert1=rootview.findViewById(R.id.step2alert1);
        alert2=rootview.findViewById(R.id.step2alert2);
        alert3=rootview.findViewById(R.id.step2alert3);
        alert4=rootview.findViewById(R.id.step2alert4);
        radioGroup=rootview.findViewById(R.id.gendero);
        setTextFields(rootview);
        nexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allowed){
                    if (checkFields()){
                        ParseUser user=new ParseUser();
                        user.setUsername(bundle.getString("username"));
                        user.setPassword(bundle.getString("pass"));
                        user.setEmail(texts.get(0).getText().toString());
                        user.put("firstname",bundle.getString("firstname"));
                        user.put("lastname",bundle.getString("lastname"));
                        String date=bundle.getString("birth");
                        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            Date d=format.parse(date);
                            user.put("birth",d);
                            user.put("balance",0f);
                            if (radioGroup.getCheckedRadioButtonId()==R.id.malo){
                                user.put("gender","Male");
                            }else if (radioGroup.getCheckedRadioButtonId()==R.id.femalo){
                                user.put("gender","Female");
                            }
                            user.put("phone",texts.get(2).getText().toString());
                            final ConstraintLayout constraintLayout=rootview.findViewById(R.id.signcon);
                            final ProgressBar progressBar=rootview.findViewById(R.id.signbar);
                            progressBar.setVisibility(View.INVISIBLE);
                            App.makeClickable(View.VISIBLE,false,constraintLayout,progressBar);
                            user.signUpInBackground(new SignUpCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e==null){
                                        custom.setCurrentItem(2);
                                    }else{
                                        if (e.getMessage().equals("Email address format is invalid.")){
                                            alert4.setText("*"+e.getMessage());
                                            alert4.setVisibility(View.VISIBLE);
                                        }else{
                                            alert4.setVisibility(View.INVISIBLE);
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);
                                            builder.setMessage(e.getMessage())
                                                    .setTitle("Alert")
                                                    .setCancelable(false)
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.dismiss();
                                                        }
                                                    }).setIcon(android.R.drawable.ic_dialog_alert);
                                            AlertDialog alertDialog=builder.create();
                                            alertDialog.show();
                                        }
                                    }
                                    App.makeClickable(View.INVISIBLE,true,constraintLayout,progressBar);
                                }
                            });
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
        step2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allowed){

                }
            }
        });
        step1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearStuff();
                custom.setCurrentItem(0);
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearStuff();
                custom.setCurrentItem(0);
            }
        });
        return rootview;
    }
}
