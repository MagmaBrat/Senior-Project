package com.example.seniorproject.signproc;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.seniorproject.MainActivity;
import com.example.seniorproject.R;

public class Step3  extends Fragment {

    CustomViewPager custom;
    TextView textView,check;

    public Step3(CustomViewPager pager){
        custom=pager;
    }


    public Step3(){

    }

    public void changeTexts(String body,String title){
        check.setText(title);
        textView.setText(body);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.step3,container,false);
        check=rootview.findViewById(R.id.textView12);
        textView=rootview.findViewById(R.id.textView13);
        Button button=rootview.findViewById(R.id.gologbut);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        return rootview;
    }
}
