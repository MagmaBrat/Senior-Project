package com.example.seniorproject.withdrawdialog;


import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
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

public class WithdrawDone extends Fragment {

    DialogFragment dialog;
    ConstraintLayout constraintLayout;


    public WithdrawDone(DialogFragment d){
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
                Log.i("wajdi","dono");
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
        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.withdrawdone,container,false);
        constraintLayout=rootview.findViewById(R.id.withdrawdconst);
        return rootview;
    }
}
