package com.example.seniorproject.storeapp;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.seniorproject.OnSwipeTouchListener;
import com.example.seniorproject.R;
import com.example.seniorproject.SlidePageAdapter;
import com.example.seniorproject.afterlog.AfterloginActivity;
import com.example.seniorproject.payment.PaymentAdd;

import java.util.ArrayList;
import java.util.List;

public class TestDialog extends Dialog {

    AfterloginActivity activity;
    int source;
    ProgressBar bar;
    ImageView imageView;

    public TestDialog(AfterloginActivity a){
        super(a);
        activity=a;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setWindowAnimations(R.style.DialogAnimation);
        setContentView(R.layout.paymentdialog);
        Toolbar toolbar = findViewById(R.id.paymenttoolbar);
        toolbar.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeBottom() {
                dismiss();
            }
        });
        source=R.drawable.crosso;
        bar=findViewById(R.id.paymentbar);
        Window window = getWindow();
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        int screenWidth = ViewGroup.LayoutParams.MATCH_PARENT;
        int screenWidth2=(int) (metrics.widthPixels);
        int screenHeight = (int) (metrics.heightPixels * 0.8);
        window.setLayout(screenWidth, screenHeight);
        window.setGravity(Gravity.BOTTOM);

    }

    @Override
    public Bundle onSaveInstanceState() {
        final ViewPager addonsviewpager = (ViewPager) findViewById(R.id.paymentdialogslider);
        List<Fragment> fragments=new ArrayList<>();
        //fragments.add(new PaymentAdd());
        PagerAdapter pagerAdapter=new SlidePageAdapter(activity.getSupportFragmentManager(),fragments);
        addonsviewpager.setAdapter(pagerAdapter);
        imageView=findViewById(R.id.iconpayment);
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
        return super.onSaveInstanceState();
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
}
