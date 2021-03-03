package com.example.seniorproject.withdrawdialog;


import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.seniorproject.OnSwipeTouchListener;
import com.example.seniorproject.QRactivity;
import com.example.seniorproject.R;
import com.example.seniorproject.SlidePageAdapter;

import java.util.ArrayList;
import java.util.List;

public class WithdrawDialog extends DialogFragment {

    private View view;
    int source;
    ProgressBar bar;
    ImageView imageView;
    QRactivity qRactivity;
    String amount;
    String id;
    String storename;
    public TextView timeout;
    public ViewPager addonsviewpager;

    public WithdrawDialog(QRactivity a,String t,String i,String s){
        qRactivity=a;
        amount=t;
        id=i;
        storename=s;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.withdrawdialog, container);
        timeout=view.findViewById(R.id.withdrawtimeout);
        Toolbar toolbar = view.findViewById(R.id.withdrawtoolbar);
        toolbar.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeBottom() {
                qRactivity.mCodeScanner.startPreview();
                dismiss();
            }
        });
        source=R.drawable.crosso;
        bar=view.findViewById(R.id.withdrawbar);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM);
        return view;
    }

    public void changeVisibility(int visibility,boolean clickable){
        timeout.setVisibility(visibility);
        ConstraintLayout constraintLayout=view.findViewById(R.id.paymentdialogconst);
        for (int i=0;i<constraintLayout.getChildCount();i++){
            constraintLayout.getChildAt(i).setClickable(clickable);
        }
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
        int screenHeight = (int) (metrics.heightPixels * 0.87);
        getDialog().getWindow().setLayout(screenWidth, screenHeight);
        addonsviewpager = (ViewPager) view.findViewById(R.id.withdrawslider);
        List<Fragment> fragments=new ArrayList<>();
        fragments.add(new WithdrawConfirm(this,amount,id,storename));
        fragments.add(new WithdrawDone(this));
        PagerAdapter pagerAdapter=new SlidePageAdapter(getChildFragmentManager(),fragments);
        addonsviewpager.setAdapter(pagerAdapter);
        imageView=view.findViewById(R.id.iconwithdraw);
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

