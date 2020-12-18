package com.example.seniorproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactsAdapter extends BaseAdapter {

    ArrayList<Contact> contacts;
    Context context;

    public ContactsAdapter(Context c, ArrayList<Contact> co){
        context=c;
        contacts=co;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int i) {
        return contacts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Contact contact=contacts.get(i);
        LayoutInflater inflater=LayoutInflater.from(context);
        view=inflater.inflate(R.layout.contactsrow,null);
        TextView prof=view.findViewById(R.id.profpic);
        TextView nicko=view.findViewById(R.id.nickview);
        char firsto=contact.user.getString("firstname").charAt(0);
        char lastoo=contact.user.getString("lastname").charAt(0);
        String name=String.valueOf(firsto)+String.valueOf(lastoo);
        Log.i("wajdi",contact.user.getString("firstname"));
        prof.setText(name);
        nicko.setText(contact.nickname);
        return view;
    }
}
