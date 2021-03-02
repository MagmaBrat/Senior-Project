package com.example.seniorproject.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.seniorproject.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class PasswordActivity extends AppCompatActivity {

    TextView neww,repeat;
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;

    public  void makeClickable(int visibility, boolean clickable){
        progressBar.setVisibility(visibility);
        for (int i=0;i<constraintLayout.getChildCount();i++){
            constraintLayout.getChildAt(i).setClickable(clickable);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passwordactivity);
        constraintLayout=findViewById(R.id.passconst);
        neww=findViewById(R.id.passnew);
        repeat=findViewById(R.id.passrepeat);
        Button button=findViewById(R.id.passbutton);
        progressBar=findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeClickable(View.VISIBLE,false);
                final String newpass=neww.getText().toString();
                String reppass=repeat.getText().toString();
                if (!newpass.equals("") && !reppass.equals("")){
                    if (newpass.equals(reppass)){
                        ParseUser user=ParseUser.getCurrentUser();
                        if (user!=null){
                            ParseQuery<ParseUser> query=ParseUser.getQuery();
                            query.getInBackground(user.getObjectId(), new GetCallback<ParseUser>() {
                                @Override
                                public void done(ParseUser object, ParseException e) {
                                    if (e==null){
                                        try {
                                            object.setPassword(newpass);
                                            object.save();
                                            Toast.makeText(getApplicationContext(),"Saved Successfully!",Toast.LENGTH_LONG).show();
                                        }catch (ParseException e1){
                                            Toast.makeText(getApplicationContext(),e1.getMessage(),Toast.LENGTH_LONG).show();
                                            e1.printStackTrace();
                                        }
                                    }else{
                                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"The repeated password and the desired password do not match",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"One of the blanks are missing",Toast.LENGTH_LONG).show();
                }
                makeClickable(View.INVISIBLE,true);
            }
        });
    }
}
