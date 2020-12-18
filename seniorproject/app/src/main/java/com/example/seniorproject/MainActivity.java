package com.example.seniorproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.seniorproject.afterlog.AfterloginActivity;
import com.example.seniorproject.signproc.signup2Activity;
import com.example.seniorproject.storeapp.StoreAfterLoginActivity;
import com.example.seniorproject.storeapp.StoreLoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import forgot.ForgotActivity;

public class MainActivity extends AppCompatActivity {

    DatabaseReference db;
    ConstraintLayout constraintLayout;
    ProgressBar progressBar;

    public void goStore(View view){
        Intent intent=new Intent(MainActivity.this, StoreLoginActivity.class);
        startActivity(intent);
    }

    public void saveFire(View v){
        Member member=new Member("Omen","Void");
        db.push().setValue(member);
    }

    public void saveData(View view){
        ParseObject agent=new ParseObject("Test");
        agent.put("name","Phoenix");
        agent.put("country","UK");
        agent.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i("info","done");
            }
        });
    }

    public void goForgot(View view){
        Intent intent=new Intent(MainActivity.this, ForgotActivity.class);
        startActivity(intent);
    }

    public void signIn(View view){
        boolean problem=false;
        EditText userfield=(EditText) findViewById(R.id.userfield);
        EditText passfield=(EditText) findViewById(R.id.passfield);
        String user=userfield.getText().toString();
        String pass=passfield.getText().toString();
        TextView alert1=(TextView) findViewById(R.id.alertv1);
        TextView alert2=(TextView) findViewById(R.id.alertv2);
        if (user.equals("")){
            problem=true;
            alert1.setVisibility(View.VISIBLE);
        }else{
            alert1.setVisibility(View.INVISIBLE);
        }
        if (pass.equals("")){
            problem=true;
            alert2.setVisibility(View.VISIBLE);
        }else{
            alert2.setVisibility(View.INVISIBLE);
        }
        if (!problem){
            App.makeClickable(View.VISIBLE,false,constraintLayout,progressBar);
            ParseUser.logInInBackground(user, pass, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user!=null){
                        Intent intent=new Intent(MainActivity.this, AfterloginActivity.class);
                        startActivity(intent);
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.MyDialogTheme);
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
                    App.makeClickable(View.INVISIBLE,true,constraintLayout,progressBar);
                }
            });
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.MyDialogTheme);
            builder.setMessage("Make sure to fill the username and password fields!")
                    .setTitle("The username or password are missing")
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


    public void goTemp2(View view){
        Intent intent=new Intent(MainActivity.this, signup2Activity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        constraintLayout=findViewById(R.id.maincon);
        progressBar=findViewById(R.id.mainbar);
        progressBar.setVisibility(View.INVISIBLE);
        db= FirebaseDatabase.getInstance().getReference().child("Test");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot da:snapshot.getChildren()){
                    Member member=da.getValue(Member.class);
                    Log.i("info",member.name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query query=db.orderByChild("name").equalTo("Phoenix");
        EditText passfield=(EditText) findViewById(R.id.passfield);
        passfield.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    signIn(view);
                    return true;
                }
                return false;
            }
        });
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser!=null) {
            if (currentUser.getString("type").equals("customer")){
                Intent intent=new Intent(MainActivity.this,AfterloginActivity.class);
                startActivity(intent);
            }else{
                Intent intent=new Intent(MainActivity.this, StoreAfterLoginActivity.class);
                startActivity(intent);
            }
            // do stuff with the user
        }


    }
}
