package com.example.seniorproject.transactions;

public class TransactionItem {

    String price,name,quant;
    boolean istax=false;
    public TransactionItem(){

    }

    public TransactionItem(String p,String n,String q){
        price=p;
        name=n;
        quant=q;
    }

}
