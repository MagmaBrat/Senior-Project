package com.example.seniorproject.storeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.seniorproject.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class StoreLoginActivity extends AppCompatActivity {

    EditText user,pass;
    TextView alert1,alert2;

    public void signIn(View view){
        Log.i("wajdi","done");
        boolean isempty=false;
        String username=user.getText().toString();
        String password=pass.getText().toString();
        if (username.equals("")){
            isempty=true;
            alert1.setVisibility(View.VISIBLE);
        }else{
            alert1.setVisibility(View.INVISIBLE);
        }
        if (password.equals("")){
            isempty=true;
            alert2.setVisibility(View.VISIBLE);
        }else{
            alert2.setVisibility(View.INVISIBLE);
        }
        Log.i("wajdi",isempty+"");
        if (!isempty){
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    Log.i("wajdi","u wut m8");
                    if (e==null){
                        if (user.getString("type").equals("store")){
                            Intent intent=new Intent(StoreLoginActivity.this,StoreAfterLoginActivity.class);
                            startActivity(intent);
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(StoreLoginActivity.this);
                            builder.setMessage("The username you have just entered belongs to a user")
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
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(StoreLoginActivity.this);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ParseUser.getCurrentUser()!=null){
            ParseUser.logOutInBackground();
        }
        setContentView(R.layout.activity_store_login);
        user=findViewById(R.id.loginfield);
        pass=findViewById(R.id.storepassfield);
        alert1=findViewById(R.id.alertv1);
        alert2=findViewById(R.id.alertv2);
    }
}
