package com.example.seniorproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class AfterloginActivity extends AppCompatActivity {

    public DrawerLayout drawer;

    public void doScan(){
        IntentIntegrator integrator=new IntentIntegrator(this);
        integrator.setCaptureActivity(QRActivity.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }

    public void goScan(View v){
        doScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result!=null){
            if (result.getContents()!=null){
                AlertDialog.Builder builder=new AlertDialog.Builder(this).setMessage(result.getContents())
                        .setTitle("Scanning Result")
                        .setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                doScan();
                            }
                        }).setNegativeButton("Finish", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                AlertDialog dialog=builder.create();
                dialog.show();
            }else{
                Toast.makeText(this,"No Results Found!",Toast.LENGTH_LONG).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afterlogin);
        Toolbar toolbar=findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawer=findViewById(R.id.mydrawer);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigation=findViewById(R.id.mynav);
        if (savedInstanceState ==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragment()).commit();
            navigation.setCheckedItem(R.id.nav_msg);
        }
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_msg:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragment()).commit();
                        break;
                    case R.id.nav_music:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TotalFragment()).commit();
                        break;
                    case R.id.nav_logout:
                        if (ParseUser.getCurrentUser()!=null){
                            ParseUser.logOutInBackground(new LogOutCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e==null){
                                        Intent intent=new Intent(AfterloginActivity.this,MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
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
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            this.finishAffinity();
        }
    }
}
