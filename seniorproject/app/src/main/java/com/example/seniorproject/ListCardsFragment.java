package com.example.seniorproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seniorproject.afterlog.AfterloginActivity;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ListCardsFragment extends Fragment {

    ArrayList<String> nums;
    ArrayList<String> exps;
    ArrayList<String> types;
    CardsAdapter adapter;
    AfterloginActivity activity;

    public ListCardsFragment(AfterloginActivity a){
        activity=a;
    }


    public void updateStuff(){
        ParseUser user=ParseUser.getCurrentUser();
        if (user!=null){
            nums.clear();
            exps.clear();
            types.clear();
            final ParseQuery<ParseUser> query=ParseUser.getQuery();
            activity.makeClickable(View.VISIBLE,false);
            query.getInBackground(user.getObjectId(), new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser object, ParseException e) {
                    if (e==null){
                        ParseQuery<ParseObject> query1=object.getRelation("cards").getQuery();
                        query1.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e==null){
                                    for (ParseObject x:objects){
                                        nums.add(x.getString("cardnumber"));
                                        exps.add(x.getString("expdate"));
                                        types.add(x.getString("type"));
                                    }
                                    adapter.notifyDataSetChanged();
                                }else {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }else{
                        e.printStackTrace();
                    }
                    activity.makeClickable(View.INVISIBLE,true);
                }
            });
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.listcardfragment,container,false);
        RecyclerView recyclerView=rootview.findViewById(R.id.cardsrecycler);
        nums=new ArrayList<>();
        exps=new ArrayList<>();
        types=new ArrayList<>();
        adapter=new CardsAdapter(getActivity(),nums,exps,types);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        updateStuff();
        ImageView button=rootview.findViewById(R.id.addcardbut);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.current=new AddCardFragment(activity);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,activity.current).commit();
            }
        });
        return rootview;
    }
}
