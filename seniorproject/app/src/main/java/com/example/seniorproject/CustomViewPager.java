package com.example.seniorproject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {

    public boolean allowed;

    public CustomViewPager(@NonNull Context context) {
        super(context);
        allowed=false;
    }

    public CustomViewPager(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (allowed){
            return super.onTouchEvent(ev);
        }else{
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (allowed){
            return super.onInterceptTouchEvent(ev);
        }else{
            return false;
        }
    }


}
