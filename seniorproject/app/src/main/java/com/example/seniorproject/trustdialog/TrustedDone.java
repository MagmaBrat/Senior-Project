package com.example.seniorproject.trustdialog;

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
import androidx.fragment.app.Fragment;

import com.example.seniorproject.R;
import com.example.seniorproject.senddialog.SendDialog;
import com.example.seniorproject.afterlog.SendFragment;

public class TrustedDone extends Fragment {

    ConstraintLayout constraintLayout;
    TrustedDialog dialog;
    SendFragment fragment;
    SendDialog sendDialog;
    boolean istrusted=false;

    public TrustedDone(TrustedDialog d,SendFragment fr){
        istrusted=true;
        dialog=d;
        fragment=fr;
    }

    public TrustedDone(SendDialog d,SendFragment fr){
        istrusted=false;
        sendDialog=d;
        fragment=fr;
    }


    public void showPage(){
        Animation fadein=new AlphaAnimation(0.2f,1f);
        fadein.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                fragment.updateStuff();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (istrusted){
                    dialog.dismiss();
                }else{
                    sendDialog.dismiss();
                }
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
        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.trusteddone,container,false);
        constraintLayout=rootview.findViewById(R.id.trustedconst);
        return rootview;
    }

}
