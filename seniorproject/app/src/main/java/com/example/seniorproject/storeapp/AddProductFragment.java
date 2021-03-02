package com.example.seniorproject.storeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.seniorproject.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class AddProductFragment extends AppCompatActivity {

    EditText prodname,prodpric;
    String[] categories = { "Select one","Electronics", "Soft Drinks", "Chips", "Beverages", "Other"};
    Spinner spinner;
    Switch aSwitch;
    String storeid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addproductfragment);
        prodname=findViewById(R.id.editText);
        prodpric=findViewById(R.id.editText2);
        spinner=findViewById(R.id.spinner2);
        aSwitch=findViewById(R.id.switch1);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,categories);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
        Button button=findViewById(R.id.button4);
        Intent intent=getIntent();
        if (intent.getStringExtra("mode").equals("EDIT")){
            String iname=intent.getStringExtra("name");
            String iprice=intent.getStringExtra("price");
            final String id=intent.getStringExtra("id");
            String icategory=intent.getStringExtra("category");
            prodname.setText(iname);
            prodpric.setText(iprice);
            int index=0;
            for (int i=0;i<categories.length;i++){
                if (categories[i].equals(icategory)){
                    index=i;
                    break;
                }
            }
            spinner.setSelection(index);
            aSwitch.setChecked(true);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String name=prodname.getText().toString();
                    final String price=prodpric.getText().toString();
                    final String cat=spinner.getSelectedItem().toString();
                    if (name.equals("") || price.equals("") || cat.equals("Select One")){
                        Toast.makeText(getApplicationContext(),"One or more of the blanks are missing..",Toast.LENGTH_LONG).show();
                    }else{
                        ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("Product");
                        query.getInBackground(id, new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                if (e==null){
                                    object.put("name",name);
                                    object.put("price",Float.parseFloat(price));
                                    object.put("category",cat);
                                    object.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e==null){
                                                Toast.makeText(getApplicationContext(),"Item saved successfully",Toast.LENGTH_LONG).show();
                                            }else{
                                                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }else{
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });
        }else{
            storeid=intent.getStringExtra("storeid");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String name=prodname.getText().toString();
                    final String price=prodpric.getText().toString();
                    final String cat=spinner.getSelectedItem().toString();
                    if (name.equals("") || price.equals("") || cat.equals("Select One")){
                        Toast.makeText(getApplicationContext(),"One or more of the blanks are missing..",Toast.LENGTH_LONG).show();
                    }else{
                        final ParseUser usa=ParseUser.getCurrentUser();
                        if (usa!=null){
                            ParseQuery<ParseUser> query=ParseUser.getQuery();
                            query.getInBackground(usa.getObjectId(), new GetCallback<ParseUser>() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    if (e==null){
                                        try {
                                            ParseQuery<ParseObject> query1=new ParseQuery<ParseObject>("Store");
                                            ParseObject store=query1.get(user.getParseObject("storeid").getObjectId());
                                            final ParseObject object=new ParseObject("Product");
                                            object.put("name",name);
                                            float num=Float.parseFloat(price);
                                            object.put("price",num);
                                            object.put("category",cat);
                                            object.put("available",aSwitch.isChecked());
                                            object.put("storeid",storeid);
                                            object.save();
                                            store.getRelation("products").add(object);
                                            store.save();
                                            Toast.makeText(getApplicationContext(),"Item added successfully",Toast.LENGTH_LONG).show();
                                        }catch (ParseException e1){
                                            Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
                                            e1.printStackTrace();
                                        }
                                    }else{
                                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }
                                }
                            });


                        }
                    }
                }
            });
        }
    }
}
