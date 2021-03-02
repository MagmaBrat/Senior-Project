package com.example.seniorproject.storeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.seniorproject.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class StoreWithdrawFragment extends Fragment {

    EditText editText;
    StoreAfterLoginActivity activity;

    public StoreWithdrawFragment(StoreAfterLoginActivity a){
        activity=a;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.storewithdrawfragment,container,false);
        Button button=rootview.findViewById(R.id.storewithbut);
        editText=rootview.findViewById(R.id.storeamount);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String amount=editText.getText().toString();
                if (!amount.equals("")){
                    ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("Store");
                    query.getInBackground(activity.storeid, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if (e==null){
                                Intent intent=new Intent(getActivity(),QrWithdrawActivity.class);
                                intent.putExtra("storeid",activity.storeid);
                                intent.putExtra("storename",object.getString("storename"));
                                intent.putExtra("amount",Float.parseFloat(amount));
                                intent.putExtra("qrstuff",true);
                                startActivity(intent);
                            }else{
                                e.printStackTrace();
                            }
                        }
                    });

                }else{
                    Toast.makeText(getActivity(),"Please fill the blank!",Toast.LENGTH_LONG).show();
                }
            }
        });
        return rootview;

    }
}
