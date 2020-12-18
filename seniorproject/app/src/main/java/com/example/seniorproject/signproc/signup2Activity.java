package com.example.seniorproject.signproc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import android.content.Intent;
import android.os.Bundle;

import com.example.seniorproject.MainActivity;
import com.example.seniorproject.R;
import com.example.seniorproject.SlidePageAdapter;

import java.util.ArrayList;
import java.util.List;

public class signup2Activity extends AppCompatActivity {

    CustomViewPager pager;
    PagerAdapter pagerAdapter;


    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem()==1){
            pager.setCurrentItem(0);
        }else if (pager.getCurrentItem()==0){
            super.onBackPressed();
        }
        else{
            Intent intent = new Intent(signup2Activity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        pager=(CustomViewPager) findViewById(R.id.mypager);
        List<Fragment> fragments=new ArrayList<>();
        fragments.add(new Step1(pager));
        fragments.add(new Step2(pager));
        fragments.add(new Step3(pager));
        pagerAdapter=new SlidePageAdapter(getSupportFragmentManager(),fragments);
        pager.setAdapter(pagerAdapter);

    }
}
