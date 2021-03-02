package com.example.seniorproject.storeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.seniorproject.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartFragment extends Fragment {

    ArrayList<String> temps;
    ArrayList<String> prodnames;
    HashMap<String,Product> hashMap;
    FragmentActivity activity;
    LinearLayout linearLayout;
    String storeid;
    String storename;
    TextView subview,totalview;
    public float subtotal=0f;

    public void doCalculations(){
        float total=0f;
        float sum=0f;
        LinearLayout v;
        EditText q;
        Spinner spinner;
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            v = (LinearLayout) linearLayout.getChildAt(i);
            q = (EditText) v.findViewWithTag("text"+i);
            int quantity=Integer.parseInt(q.getText().toString());
            spinner=v.findViewWithTag("spin"+i);
            if (hashMap.containsKey(spinner.getSelectedItem().toString())){
                sum+=hashMap.get(spinner.getSelectedItem().toString()).price*quantity;
            }
        }
        subtotal=sum;
        total=sum*1.16f;
        String subs=new DecimalFormat("#.##").format(sum);
        String totals=new DecimalFormat("#.##").format(total);
        subview.setText("Subtotal: $"+subs);
        totalview.setText("Total: $"+totals);
    }

    public void addRow(){
        int id=linearLayout.getChildCount();
        LinearLayout linearLayout1=new LinearLayout(activity);
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView textView1=new TextView(activity);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(60,5,30,5);
        textView1.setLayoutParams(params);
        textView1.setTextSize(18);
        textView1.setTextColor(Color.BLACK);
        textView1.setText("Item #"+(linearLayout.getChildCount()+1));
        //
        Spinner spinner=new Spinner(activity);
        spinner.setTag("spin"+id);
        LinearLayout.LayoutParams params1=new LinearLayout.LayoutParams((int)activity.getResources().getDisplayMetrics().density*220,(int)activity.getResources().getDisplayMetrics().density*60);
        params1.setMargins(70,5,30,5);
        spinner.setLayoutParams(params1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item,prodnames);
        spinner.setAdapter(adapter);
        //
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                doCalculations();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //
        EditText editText=new EditText(activity);
        editText.setTag("text"+id);
        LinearLayout.LayoutParams params2=new LinearLayout.LayoutParams((int)activity.getResources().getDisplayMetrics().density*80,(int)activity.getResources().getDisplayMetrics().density*60);
        params2.setMargins(70,5,30,5);
        editText.setLayoutParams(params2);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setText(0+"");
        //
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>0){
                    doCalculations();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //
        linearLayout1.addView(textView1);
        linearLayout1.addView(spinner);
        linearLayout1.addView(editText);
        linearLayout.addView(linearLayout1);
    }

    public void generateFixedQR(){
        Intent intent=new Intent(getActivity(),QrScanActivity.class);
        intent.putExtra("qrstuff",true);
        ArrayList<String> ids=new ArrayList<>();
        ids.add("XTqsa0JAcs");
        intent.putStringArrayListExtra("ids",ids);
        ArrayList<Integer> qs=new ArrayList<>();
        qs.add(2);
        intent.putIntegerArrayListExtra("quants",qs);
        float subtotal=2*255f;
        intent.putExtra("subtotal",subtotal);
        float total=subtotal*1.16f;
        intent.putExtra("total",total);
        intent.putExtra("store",storeid);
        intent.putExtra("storename",storename);
        startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.cartfragment,container,false);
        linearLayout=rootview.findViewById(R.id.cartlist);
        temps=new ArrayList<>();
        temps.add("");
        hashMap=new HashMap<>();
        prodnames=new ArrayList<>();
        prodnames.add("Select An Item");
        activity=getActivity();
        subview=rootview.findViewById(R.id.textView34);
        totalview=rootview.findViewById(R.id.textView35);


        ParseUser user=ParseUser.getCurrentUser();
        if (user!=null){
            final ParseQuery<ParseUser> query=ParseUser.getQuery();
            query.getInBackground(user.getObjectId(), new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser object, ParseException e) {
                    if (e==null){
                        try {
                            TextView textView=rootview.findViewById(R.id.storename);
                            ParseQuery<ParseObject> query1=new ParseQuery<ParseObject>("Store");
                            ParseObject obj=query1.get(object.getParseObject("storeid").getObjectId());
                            storeid=obj.getObjectId();
                            storename=obj.getString("storename");
                            textView.setText(obj.getString("storename"));
                            ParseQuery<ParseObject> query2=obj.getRelation("products").getQuery();
                            query2.whereEqualTo("available",true);
                            List<ParseObject> objects= query2.find();
                            for (ParseObject x:objects){
                                Product product=new Product(x.getString("name"),x.getString("category"),x.getNumber("price").floatValue(),x.getObjectId());
                                hashMap.put(x.getString("name"),product);
                                prodnames.add(x.getString("name"));
                            }
                            addRow();

                        }catch (ParseException e1){
                            e1.printStackTrace();
                        }
                    }else{
                        e.printStackTrace();
                    }
                }
            });
        }
        ImageView imageView=rootview.findViewById(R.id.addproduct);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRow();
            }
        });
        Button button=rootview.findViewById(R.id.cartsdonebut);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getActivity(),QrScanActivity.class);
                intent.putExtra("qrstuff",true);
                ArrayList<String> ids=new ArrayList<>();
                ArrayList<Integer> qs=new ArrayList<>();
                for (int i=0;i<linearLayout.getChildCount();i++){
                    Spinner spinner=linearLayout.getChildAt(0).findViewWithTag("spin"+i);
                    EditText editText=linearLayout.getChildAt(0).findViewWithTag("text"+i);
                    if (hashMap.containsKey(spinner.getSelectedItem().toString())){
                        if (!editText.getText().toString().equals("")){
                            ids.add(hashMap.get(spinner.getSelectedItem().toString()).objectID);
                            qs.add(Integer.parseInt(editText.getText().toString()));
                        }
                    }
                }
                intent.putStringArrayListExtra("ids",ids);
                intent.putIntegerArrayListExtra("quants",qs);
                intent.putExtra("subtotal",subtotal);
                float total=subtotal*1.16f;
                intent.putExtra("total",total);
                intent.putExtra("store",storeid);
                intent.putExtra("storename",storename);
                startActivity(intent);
            }
        });
        return rootview;
    }
}
