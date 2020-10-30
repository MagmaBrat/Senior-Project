package com.example.seniorproject;

import android.app.AlertDialog;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        final ConstraintLayout constraintLayout=rootview.findViewById(R.id.dashcon);
        final ProgressBar progressBar=rootview.findViewById(R.id.dashbar);
        progressBar.setVisibility(View.INVISIBLE);
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
        return rootview;
    }
}
