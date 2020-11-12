package com.example.seniorproject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class TrustedDialog extends DialogFragment {


    AfterloginActivity activity;
    ParseUser user;
    String nickname="";
    private View view;

    public TrustedDialog(AfterloginActivity a) {
        activity = a;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.trusteddialog, container);
        Toolbar toolbar = view.findViewById(R.id.contacttoolbar);
        toolbar.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeBottom() {
                dismiss();
            }
        });
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM);
//        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,(int) (getScreenHeight(getActivity()) * 0.8));
//
//        ViewPager viewPager=view.findViewById(R.id.dialogslider);
//        List<Fragment> fragments=new ArrayList<>();
////        fragments.add(new TrustedAdd());
//        fragments.add(new TrustedConfirm());
//        PagerAdapter pagerAdapter=new SlidePageAdapter(activity.getSupportFragmentManager(),fragments);
//        viewPager.setAdapter(pagerAdapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE,R.style.DialogSlideAnim);
    }


    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = ViewGroup.LayoutParams.MATCH_PARENT;
        int screenHeight = (int) (metrics.heightPixels * 0.8);

        getDialog().getWindow().setLayout(screenWidth, screenHeight);

        ViewPager addonsviewpager = (ViewPager) view.findViewById(R.id.dialogslider);
        List<Fragment> fragments=new ArrayList<>();
        fragments.add(new TrustedAdd(addonsviewpager,this));
        fragments.add(new TrustedConfirm(addonsviewpager,this));
        PagerAdapter pagerAdapter=new SlidePageAdapter(getChildFragmentManager(),fragments);
        addonsviewpager.setAdapter(pagerAdapter);


    }
}
