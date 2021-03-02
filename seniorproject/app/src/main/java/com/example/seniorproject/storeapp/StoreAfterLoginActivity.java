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

import com.example.seniorproject.MainActivity;
import com.example.seniorproject.R;
import com.google.android.material.navigation.NavigationView;
import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class StoreAfterLoginActivity extends AppCompatActivity {

    public DrawerLayout drawer;
    public Fragment current;
    ProgressBar progressBar;
    String storeid;
    StoreAfterLoginActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_after_login);
        Toolbar toolbar=findViewById(R.id.storetoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        activity=this;
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
            ParseUser user=ParseUser.getCurrentUser();
            if (user!=null){
                ParseQuery<ParseUser> query=ParseUser.getQuery();
                query.getInBackground(user.getObjectId(), new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser object, ParseException e) {
                        if (e==null){
                            ParseQuery<ParseObject> query1=new ParseQuery<ParseObject>("Store");
                            String id=object.getParseObject("storeid").getObjectId();
                            storeid=id;
                        }else{
                            e.printStackTrace();
                        }
                    }
                });
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
                        current=new AddItemFragment(activity);
                        getSupportFragmentManager().beginTransaction().replace(R.id.storefragment_container,current).commit();
                        break;
                    case R.id.nav_withdraw:
                        current=new StoreWithdrawFragment(activity);
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

    @Override
    public void onBackPressed() {

        if (!(current instanceof CartFragment)){
            current=new CartFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.storefragment_container,current).commit();
        }else{
            this.finishAffinity();
        }
    }
}
