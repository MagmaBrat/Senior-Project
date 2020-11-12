package com.example.seniorproject;

public class Member {

    String name;
    String country;
    public Member(String n,String c){
        name=n;
        country=c;
    }
    public Member(){

    }

//    String trans=result.getContents();
//    String[] parts=trans.split("@");
//                if (parts.length==8){
//        String store=parts[0];
//        String items=parts[1].substring(1,parts[1].length()-1);
//        String quants=parts[2].substring(1,parts[2].length()-1);
//        String prices=parts[3].substring(1,parts[3].length()-1);
//        float total=Float.parseFloat(parts[4]);
//        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//        SimpleDateFormat format2=new SimpleDateFormat("HH:mm:ss");
//        try {
//            Date date = format.parse(parts[4]);
//            Date time=format2.parse(parts[5]);
//
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
}
