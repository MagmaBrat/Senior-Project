package com.example.seniorproject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class SendFragment extends Fragment {

    AfterloginActivity activity;

    public SendFragment(AfterloginActivity a){
        activity=a;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.sendfragment,container,false);
        ImageView imageView=rootview.findViewById(R.id.adduser);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrustedDialog custom=new TrustedDialog(activity);
                custom.show(getFragmentManager(),"addons_fragment");
//                Dialog  dialog=new TrustedDialog((AfterloginActivity) getActivity());
//                dialog.setContentView(R.layout.trusteddialog);
//                dialog.show();
//                Window window = dialog.getWindow();
//                window.setGravity(Gravity.BOTTOM);
//                window.setTitle("woof");
//                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,(int) (getScreenHeight(getActivity()) * 0.8));

            }
        });
        return rootview;
    }
}
