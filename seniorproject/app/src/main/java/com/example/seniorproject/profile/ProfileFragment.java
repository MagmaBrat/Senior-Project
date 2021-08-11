package com.example.seniorproject.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.seniorproject.R;
import com.example.seniorproject.afterlog.AfterloginActivity;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProfileFragment extends Fragment {

    ArrayList<EditText> texts;
    ParseUser user;
    TextView save,cancel;
    ArrayList<String> buffer;
    AfterloginActivity activity;
    ImageView imageView;
    boolean enabled=false;

    public ProfileFragment(AfterloginActivity a){
        activity=a;
    }

    public boolean isEmpty(){
        boolean isempty=false;
        for (EditText x:texts){
            if (x.getText().toString().equals("")){
                isempty=true;
            }
        }
        return isempty;
    }

    public void saveChanges(){
        ParseQuery<ParseUser> query=ParseUser.getQuery();
        if (user!=null){
            activity.makeClickable(View.VISIBLE,false);
            query.getInBackground(user.getObjectId(), new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser object, ParseException e) {
                    if (e==null){
                        if (!isEmpty()){
                            String firstname=texts.get(0).getText().toString();
                            String lastname=texts.get(1).getText().toString();
                            String email=texts.get(2).getText().toString();
                            String phone=texts.get(3).getText().toString();
                            if (!object.getString("firstname").equals(firstname)){
                                object.put("firstname",firstname);
                            }
                            if (!object.getString("lastname").equals(lastname)){
                                object.put("lastname",lastname);
                            }
                            if (!object.getEmail().equals(email)){
                                object.setEmail(email);
                            }
                            if (!object.getString("phone").equals(phone)){
                                object.put("phone",phone);
                            }
                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e==null){
                                        Toast.makeText(getActivity(),"Changes saved successfully",Toast.LENGTH_LONG).show();
                                        buttonVisibility(View.INVISIBLE);
                                        for (int i=0;i<texts.size();i++){
                                            texts.get(i).getBackground().setTint(getResources().getColor(R.color.backgroundo));
                                            texts.get(i).setEnabled(false);
                                            buffer.set(i,texts.get(i).getText().toString());
                                        }
                                        buttonVisibility(View.GONE);
                                        imageView.setEnabled(true);
                                        imageView.setAlpha(1f);
                                    }else{
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
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
                            });
                        }
                    }else{
                        e.printStackTrace();
                    }
                    activity.makeClickable(View.INVISIBLE,true);
                }
            });
        }
    }

    public void buttonVisibility(int visibility){
        cancel.setVisibility(visibility);
        save.setVisibility(visibility);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.profilefragment,container,false);
        final TextView profilepic=rootview.findViewById(R.id.profilepic);
        texts=new ArrayList<>();
        buffer=new ArrayList<>();
        texts.add((EditText) rootview.findViewById(R.id.firstprof));
        texts.add((EditText) rootview.findViewById(R.id.lastprof));
        texts.add((EditText) rootview.findViewById(R.id.emailprof));
        texts.add((EditText) rootview.findViewById(R.id.phoneprof));

        TextView changepass=rootview.findViewById(R.id.profilechangepass);
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity,PasswordActivity.class);
                startActivity(intent);
            }
        });
        final EditText editText=rootview.findViewById(R.id.birthprof);
        user=ParseUser.getCurrentUser();
        if (user!=null){
            ParseQuery<ParseUser> query=ParseUser.getQuery();
            activity.makeClickable(View.VISIBLE,false);
            query.getInBackground(user.getObjectId(), new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser object, ParseException e) {
                    if (e==null){
                        char firsto=object.getString("firstname").charAt(0);
                        char lastoo=object.getString("lastname").charAt(0);
                        profilepic.setText(String.valueOf(firsto)+String.valueOf(lastoo));
                        texts.get(0).setText(object.getString("firstname"));
                        texts.get(1).setText(object.getString("lastname"));
                        texts.get(2).setText(object.getEmail());
                        texts.get(3).setText(object.getString("phone"));
                        for (EditText x:texts){
                            buffer.add(x.getText().toString());
                        }
                        Date date=object.getDate("birth");
                        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
                        String birth=format.format(date);
                        editText.setText(birth);
                    }else{
                        e.printStackTrace();
                    }
                    activity.makeClickable(View.INVISIBLE,true);
                }
            });
        }
        imageView=rootview.findViewById(R.id.editprof);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enabled){
                    enabled=false;
                    for (int i=0;i<texts.size();i++){
                        texts.get(i).getBackground().setTint(getResources().getColor(R.color.backgroundo));
                        texts.get(i).setEnabled(false);
                        texts.get(i).setText(buffer.get(i));
                    }
                    buttonVisibility(View.GONE);
                    imageView.setEnabled(true);
                    imageView.setAlpha(1f);
                }else{
                    enabled=true;
                    for (EditText x:texts){
                        x.getBackground().setTint(getResources().getColor(R.color.textocolaro));
                        x.setEnabled(true);
                    }
                    buttonVisibility(View.VISIBLE);
                    view.setAlpha(0.4f);
                }

            }
        });
        save=rootview.findViewById(R.id.savecprofbut);
        cancel=rootview.findViewById(R.id.cancelprofbut);
        save.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0;i<texts.size();i++){
                    texts.get(i).getBackground().setTint(getResources().getColor(R.color.backgroundo));
                    texts.get(i).setEnabled(false);
                    texts.get(i).setText(buffer.get(i));
                }
                buttonVisibility(View.GONE);
                imageView.setEnabled(true);
                imageView.setAlpha(1f);
            }
        });
        return rootview;
    }
}
