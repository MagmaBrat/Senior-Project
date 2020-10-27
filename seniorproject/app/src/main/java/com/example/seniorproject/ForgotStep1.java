package com.example.seniorproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class ForgotStep1 extends Fragment {

    CustomViewPager custom;

    public ForgotStep1(CustomViewPager pager){
        custom=pager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.forgot1,container,false);
        Button forgot=rootview.findViewById(R.id.forgotbut);
        final EditText emailfield=rootview.findViewById(R.id.forgotfield);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emailfield.getText().toString();
                if (email.equals("")){
                    Toast.makeText(getActivity(),"Please enter your email!",Toast.LENGTH_LONG).show();
                }else{
                    ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e==null){
                                Log.i("info","sent");
                            }else{
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
                    });
                }
            }
        });
        return rootview;
    }
}
