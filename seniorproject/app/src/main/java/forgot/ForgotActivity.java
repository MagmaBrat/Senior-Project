package forgot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import android.os.Bundle;

import com.example.seniorproject.R;
import com.example.seniorproject.SlidePageAdapter;
import com.example.seniorproject.signproc.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

public class ForgotActivity extends AppCompatActivity {

    CustomViewPager pager;
    PagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        pager=(CustomViewPager) findViewById(R.id.mypager2);
        List<Fragment> fragments=new ArrayList<>();
        fragments.add(new ForgotStep1(pager));
        pagerAdapter=new SlidePageAdapter(getSupportFragmentManager(),fragments);
        pager.setAdapter(pagerAdapter);
    }
}
