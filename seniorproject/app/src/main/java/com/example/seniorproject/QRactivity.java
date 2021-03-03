package com.example.seniorproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.seniorproject.payment.PaymentDialog;
import com.example.seniorproject.withdrawdialog.WithdrawDialog;
import com.google.zxing.Result;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class QRactivity extends AppCompatActivity {

    public CodeScanner mCodeScanner;
    QRactivity activity;
    CodeScannerView scannerView;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                mCodeScanner = new CodeScanner(this, scannerView);
                mCodeScanner.setDecodeCallback(new DecodeCallback() {
                    @Override
                    public void onDecoded(@NonNull final Result result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String[] parts=result.getText().split("@");
                                if (parts.length==4){
                                    final String total=parts[1];
                                    final String id=parts[3];
                                    ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("Pending");
                                    query.getInBackground(id, new GetCallback<ParseObject>() {
                                        @Override
                                        public void done(ParseObject object, ParseException e) {
                                            if (e==null){
                                                PaymentDialog dialog=new PaymentDialog(activity,total,id);
                                                dialog.show(getSupportFragmentManager(),"PaymentDialog");
                                            }else{
                                                AlertDialog.Builder builder=new AlertDialog.Builder(activity, R.style.MyDialogTheme).setMessage("The QRCode that" +
                                                        "that you tries to scan is invalid or not clear")
                                                        .setTitle("Invalid QRCode!")
                                                        .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                mCodeScanner.startPreview();
                                                            }
                                                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.dismiss();
                                                            }
                                                        }).setIcon(android.R.drawable.ic_dialog_alert);
                                                AlertDialog dialog=builder.create();
                                                dialog.show();
                                            }
                                        }
                                    });
                                    PaymentDialog dialog=new PaymentDialog(activity,total,id);
                                    dialog.show(getSupportFragmentManager(),"PaymentDialog");
                                }else{
                                    AlertDialog.Builder builder=new AlertDialog.Builder(activity, R.style.MyDialogTheme).setMessage("The QRCode that" +
                                            "that you tries to scan is invalid or not clear")
                                            .setTitle("Invalid QRCode!")
                                            .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    mCodeScanner.startPreview();
                                                }
                                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            }).setIcon(android.R.drawable.ic_dialog_alert);
                                    AlertDialog dialog=builder.create();
                                    dialog.show();
                                }

                            }
                        });
                    }
                });
                scannerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCodeScanner.startPreview();
                    }
                });
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qractivity);
        activity=this;
        scannerView = findViewById(R.id.scanner_view);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.CAMERA}, 10);
        }else{
            mCodeScanner = new CodeScanner(this, scannerView);
            mCodeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull final Result result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String[] parts=result.getText().split("@");
                            if (parts.length==4){
                                final String total=parts[1];
                                final String id=parts[3];
                                ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("Pending");
                                query.getInBackground(id, new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject object, ParseException e) {
                                        if (e==null){
                                            PaymentDialog dialog=new PaymentDialog(activity,total,id);
                                            dialog.show(getSupportFragmentManager(),"PaymentDialog");
                                        }else{
                                            AlertDialog.Builder builder=new AlertDialog.Builder(activity, R.style.MyDialogTheme).setMessage("The QRCode that" +
                                                    "that you tries to scan is invalid or not clear")
                                                    .setTitle("Invalid QRCode!")
                                                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            mCodeScanner.startPreview();
                                                        }
                                                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.dismiss();
                                                        }
                                                    }).setIcon(android.R.drawable.ic_dialog_alert);
                                            AlertDialog dialog=builder.create();
                                            dialog.show();
                                        }
                                    }
                                });
                                PaymentDialog dialog=new PaymentDialog(activity,total,id);
                                dialog.show(getSupportFragmentManager(),"PaymentDialog");
                            }
                            else if (parts.length==3) {
                                final String amount=parts[1];
                                final String id=parts[2];
                                final String storename=parts[0];
                                ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("WithdrawPending");
                                query.getInBackground(id, new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject object, ParseException e) {
                                        if (e==null){
                                            WithdrawDialog dialog=new WithdrawDialog(activity,amount,id,storename);
                                            dialog.show(getSupportFragmentManager(),"WithdrawDialog");
                                        }else{
                                            AlertDialog.Builder builder=new AlertDialog.Builder(activity, R.style.MyDialogTheme).setMessage("The QRCode that" +
                                                    "that you tries to scan is invalid or not clear")
                                                    .setTitle("Invalid QRCode!")
                                                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            mCodeScanner.startPreview();
                                                        }
                                                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.dismiss();
                                                        }
                                                    }).setIcon(android.R.drawable.ic_dialog_alert);
                                            AlertDialog dialog=builder.create();
                                            dialog.show();
                                        }
                                    }
                                });

                            }
                            else{
                                AlertDialog.Builder builder=new AlertDialog.Builder(activity, R.style.MyDialogTheme).setMessage("The QRCode that" +
                                        "that you tries to scan is invalid or not clear")
                                        .setTitle("Invalid QRCode!")
                                        .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                mCodeScanner.startPreview();
                                            }
                                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        }).setIcon(android.R.drawable.ic_dialog_alert);
                                AlertDialog dialog=builder.create();
                                dialog.show();
                            }

                        }
                    });
                }
            });
            scannerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCodeScanner.startPreview();
                }
            });
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}
