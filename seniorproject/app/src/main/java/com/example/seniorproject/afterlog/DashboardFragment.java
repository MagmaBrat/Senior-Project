package com.example.seniorproject.afterlog;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seniorproject.App;
import com.example.seniorproject.ListCardsFragment;
import com.example.seniorproject.QRactivity;
import com.example.seniorproject.R;
import com.example.seniorproject.analysis.AnalysisFragment;
import com.example.seniorproject.transactions.TransactionsFragment;
import com.example.seniorproject.withdrawal.WithdrawFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    ArrayList<MainModel> models;
    RecyclerView recycler;
    MainAdapter mainAdapter;
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;
    AfterloginActivity activity;

    public DashboardFragment(ProgressBar bar,AfterloginActivity a){
        progressBar=bar;
        activity=a;
    }

    public void doScan(){
//        IntentIntegrator integrator=new IntentIntegrator(getActivity());
//        integrator.setCaptureActivity(QRActivity.class);
//        integrator.setOrientationLocked(false);
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
//        integrator.setPrompt("Scanning Code");
//        integrator.initiateScan();
        Intent intent=new Intent(activity, QRactivity.class);
        startActivity(intent);
    }



    public void setListeners(GridLayout gridLayout){
        gridLayout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.uncheckMenu();
                doScan();
//                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//                startActivityForResult(intent, 0);
            }
        });
        gridLayout.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.current=new AnalysisFragment();
                activity.uncheckMenu();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,activity.current).commit();
            }
        });
        gridLayout.getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.current=new SendFragment(activity);
                activity.uncheckMenu();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,activity.current).commit();
            }
        });
        gridLayout.getChildAt(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.current=new ListCardsFragment(activity);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,activity.current).commit();
            }
        });
        gridLayout.getChildAt(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.current=new TransactionsFragment(activity);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,activity.current).commit();
            }
        });
        gridLayout.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.current=new WithdrawFragment(activity);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,activity.current).commit();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.dashboard,container,false);
        recycler=rootview.findViewById(R.id.myrecycler);
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Sale");
        query.whereEqualTo("activated",true);
        query.orderByDescending("createdAt");
        query.setLimit(6);
        models=new ArrayList<>();
        constraintLayout=rootview.findViewById(R.id.dashcon);
        App.makeClickable(View.VISIBLE,false,constraintLayout,progressBar);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e==null){
                    for (ParseObject object:objects){
                        ParseFile pic=object.getParseFile("picture");
                        final String desc=object.getString("description");
                        if (pic!=null){
                            byte[] data= new byte[0];
                            try {
                                data = pic.getData();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            Bitmap bmp = BitmapFactory
                                    .decodeByteArray(
                                            data, 0,
                                            data.length);
                            models.add(new MainModel(bmp,desc));
                        }
                    }
                    LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
                    recycler.setLayoutManager(layoutManager);
                    recycler.setItemAnimator(new DefaultItemAnimator());
                    mainAdapter=new MainAdapter(getActivity(),models);
                    recycler.setAdapter(mainAdapter);
                }else{
                    AlertDialog dialog=App.makeDialog(getActivity(),e).create();
                    dialog.show();
                }
                App.makeClickable(View.INVISIBLE,true,constraintLayout,progressBar);
            }
        });
        GridLayout gridLayout=rootview.findViewById(R.id.gridLayout);
        setListeners(gridLayout);
        return rootview;
    }
}
