package com.example.seniorproject.payment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.seniorproject.R;

public class PaymentDone extends Fragment {

    DialogFragment dialog;
    ConstraintLayout constraintLayout;


    public PaymentDone(DialogFragment d){
        dialog=d;
    }

    public void showPage(){
        Animation fadein=new AlphaAnimation(0.2f,1f);
        fadein.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dialog.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fadein.setInterpolator(new DecelerateInterpolator());
        fadein.setDuration(2000);
        constraintLayout.setAnimation(fadein);
        constraintLayout.getAnimation().start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.paymentdone,container,false);
        constraintLayout=rootview.findViewById(R.id.paymentconst);
        return rootview;
    }
}
