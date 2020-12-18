package com.example.seniorproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> numbers;
    ArrayList<String> exps;
    ArrayList<String> types;

    public CardsAdapter(Context c,ArrayList<String> n,ArrayList<String> e,ArrayList<String> t){
        context=c;
        numbers=n;
        exps=e;
        types=t;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.cardrow,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (types.get(position).equals("VISA")){
            holder.type.setImageResource(R.drawable.visa);
        }else if (types.get(position).equals("MASTERCARD")){
            holder.type.setImageResource(R.drawable.mastercard);
        }
        int len=numbers.get(position).length();
        holder.card.setText("Card ****"+numbers.get(position).substring(len-5,len));
        holder.exp.setText("Expires in "+exps.get(position));
    }



    @Override
    public int getItemCount() {
        return numbers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView card,exp;
        ImageView type;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card=itemView.findViewById(R.id.cardnumview);
            exp=itemView.findViewById(R.id.cardexpview);
            type=itemView.findViewById(R.id.cardtypeview);
        }
    }
}
