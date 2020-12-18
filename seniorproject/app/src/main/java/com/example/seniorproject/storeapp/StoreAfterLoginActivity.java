package com.example.seniorproject.storeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.seniorproject.AddCardFragment;
import com.example.seniorproject.App;
import com.example.seniorproject.MainActivity;
import com.example.seniorproject.ProfileFragment;
import com.example.seniorproject.R;
import com.example.seniorproject.afterlog.AfterloginActivity;
import com.example.seniorproject.afterlog.DashboardFragment;
import com.google.android.material.navigation.NavigationView;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class StoreAfterLoginActivity extends AppCompatActivity {

    public DrawerLayout drawer;
    public Fragment current;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_after_login);
        Toolbar toolbar=findViewById(R.id.storetoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        progressBar=findViewById(R.id.storebar);
        progressBar.setVisibility(View.INVISIBLE);
        drawer=findViewById(R.id.storedrawer);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigation=findViewById(R.id.storenav);
            if (savedInstanceState ==null){
            current=new CartFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.storefragment_container,current).commit();
            navigation.setCheckedItem(R.id.nav_cart);
        }
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_cart:
                        current=new CartFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.storefragment_container,current).commit();
                        break;
                    case R.id.nav_itempackage:
                        current=new AddItemFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.storefragment_container,current).commit();
                        break;
                    case R.id.nav_storelogout:
                        if (ParseUser.getCurrentUser()!=null){
                            ParseUser.logOutInBackground(new LogOutCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e==null){
                                        Intent intent=new Intent(StoreAfterLoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }else {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
}
