package com.example.seniorproject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class SlidePageAdapter  extends FragmentStatePagerAdapter {

   List<Fragment> fragments;

    public SlidePageAdapter(FragmentManager fm,List<Fragment> f){
        super(fm);
        fragments=f;

    }




    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
