package com.example.seniorproject.senddialog;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.example.seniorproject.afterlog.AfterloginActivity;
import com.example.seniorproject.OnSwipeTouchListener;
import com.example.seniorproject.R;
import com.example.seniorproject.afterlog.SendFragment;
import com.example.seniorproject.SlidePageAdapter;
import com.example.seniorproject.trustdialog.TrustedDone;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class SendDialog extends DialogFragment {


    AfterloginActivity activity;
    ImageView imageView;
    ProgressBar bar;
    private View view;
    int source;
    ParseObject user;
    SendFragment fragment;
    String nick;

    public SendDialog(AfterloginActivity a,ParseObject object,String n,SendFragment fr) {
        activity = a;
        user=object;
        nick=n;
        fragment=fr;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.senddialog, container);
        Toolbar toolbar = view.findViewById(R.id.sendtoolbar);
        toolbar.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeBottom() {
                dismiss();
            }
        });
        source=R.drawable.crosso;
        bar=view.findViewById(R.id.sendbar);
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

    public void setIconGesture(int icon){

        switch (icon){
            case 0:
                imageView.setVisibility(View.VISIBLE);
                bar.setVisibility(View.GONE);
                source=R.drawable.crosso;
                imageView.setImageResource(R.drawable.crosso);
                break;
            case 1:
                imageView.setVisibility(View.VISIBLE);
                bar.setVisibility(View.GONE);
                source=R.drawable.prevspec;
                imageView.setImageResource(R.drawable.prevspec);
                break;
            case 2:
                imageView.setVisibility(View.GONE);
                bar.setVisibility(View.VISIBLE);
                break;
            case 3:
                imageView.setVisibility(View.GONE);
                bar.setVisibility(View.GONE);
        }
    }


    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = ViewGroup.LayoutParams.MATCH_PARENT;
        int screenHeight = (int) (metrics.heightPixels * 0.8);
        getDialog().getWindow().setLayout(screenWidth, screenHeight);
        final ViewPager addonsviewpager = (ViewPager) view.findViewById(R.id.senddialogslider);
        List<Fragment> fragments=new ArrayList<>();
        fragments.add(new SendAdd(user,activity,nick,this,addonsviewpager));
        fragments.add(new SendConfirm(user,this,addonsviewpager));
        fragments.add(new TrustedDone(this,fragment));
        PagerAdapter pagerAdapter=new SlidePageAdapter(getChildFragmentManager(),fragments);
        addonsviewpager.setAdapter(pagerAdapter);
        imageView=view.findViewById(R.id.iconsend);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (source== R.drawable.crosso){
                    dismiss();
                }else if (source ==R.drawable.prevspec){
                    setIconGesture(0);
                    addonsviewpager.setCurrentItem(0);
                }
            }
        });

    }
}
