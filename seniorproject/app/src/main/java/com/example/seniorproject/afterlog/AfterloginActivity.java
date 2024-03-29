package com.example.seniorproject.afterlog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.seniorproject.AddCardFragment;
import com.example.seniorproject.App;
import com.example.seniorproject.ListCardsFragment;
import com.example.seniorproject.MainActivity;
import com.example.seniorproject.profile.ProfileFragment;
import com.example.seniorproject.R;

import com.example.seniorproject.transactions.TransactionSingleFragment;
import com.example.seniorproject.transactions.TransactionsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.material.navigation.NavigationView;
import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;


public class AfterloginActivity extends AppCompatActivity {

    public DrawerLayout drawer;
    Context context;
    public Fragment current;
    ConstraintLayout constraintLayout;
    PaymentsClient paymentsClient;
    ProgressBar progressBar;
    AfterloginActivity thisactivity;
    NavigationView navigation;
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;

    public void doScan(){
//        IntentIntegrator integrator=new IntentIntegrator(this);
//        integrator.setCaptureActivity(QRActivity.class);
//        integrator.setOrientationLocked(false);
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
//        integrator.setPrompt("Scanning Code");
//        integrator.initiateScan();
    }

    public static JSONObject basicConfigJson(){
        try {
            return new JSONObject().put("apiVersion",2)
                    .put("apiVersionMinor",0)
                    .put("allowedPaymentMethods",new JSONArray().put(getCardPaymentMethod()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void showPayment(){
        IsReadyToPayRequest readyToPayRequest=IsReadyToPayRequest.fromJson(basicConfigJson().toString());
        Task<Boolean> task=paymentsClient.isReadyToPay(readyToPayRequest);
        task.addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()){
                    if (task.getResult()){
//                        button.setText("meow");
//                        button.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                try {
//                                    final JSONObject paymentRequestJson=basicConfigJson();
//                                    paymentRequestJson.put("transactionInfo",new JSONObject()
//                                            .put("totalPrice","1.05")
//                                            .put("totalPriceStatus","FINAL")
//                                            .put("currencyCode","USD"));
//                                    paymentRequestJson.put("merchantInfo",new JSONObject()
//                                            .put("merchantId","01234567890123456789")
//                                            .put("merchantName","Example Merchant"));
//                                    final PaymentDataRequest request=PaymentDataRequest.fromJson(paymentRequestJson.toString());
//                                    AutoResolveHelper.resolveTask(paymentsClient.loadPaymentData(request),thisactivity,LOAD_PAYMENT_DATA_REQUEST_CODE);
//                                }catch (JSONException e){
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
                    }else{
                    }
                }else{
                }
            }
        });

    }

    public static JSONObject getCardPaymentMethod(){
        try {
            final String[] network=new String[]{"VISA"};
            final String[] authMethods=new String[]{"PAN_ONLY","CRYPTOGRAM_3DS"};
            JSONObject card=new JSONObject();
            card.put("type","CARD");
            card.put("tokenizationSpecifications",getGatewayTokenizationSpecification());
            card.put("parameters",new JSONObject()
                    .put("allowedAuthMethods",new JSONArray(network))
                    .put("allowedCardNetworks",new JSONArray(authMethods)));
            return card;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    private static JSONObject getGatewayTokenizationSpecification() throws JSONException {
        return new JSONObject() {{
            put("type", "PAYMENT_GATEWAY");
            put("parameters", new JSONObject() {{
                put("gateway", "example");
                put("gatewayMerchantId", "exampleGatewayMerchantId");
            }});
        }};
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    protected void onResume() {
        NavigationView navigation=findViewById(R.id.mynav);
        updateBalance(navigation);
        super.onResume();
    }

    public void updateBalance(NavigationView navigation){
        View headerview=navigation.getHeaderView(0);
        final TextView balance=headerview.findViewById(R.id.headerbalance);
        String user=ParseUser.getCurrentUser().getObjectId();
        ParseQuery<ParseUser> query=ParseUser.getQuery();
        query.getInBackground(user, new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if (e==null){
                    double bal=object.getNumber("balance").doubleValue();
                    String newbal=new DecimalFormat("#.##").format(bal);
                    balance.setText("Balance: $"+newbal);
                }else{
                    App.makeDialog(context,e);
                }
            }
        });
    }

    public void uncheckMenu(){
        navigation.getCheckedItem().setChecked(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afterlogin);
        context=this;
        thisactivity=this;
        Wallet.WalletOptions options=new Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                .build();
        paymentsClient=Wallet.getPaymentsClient(this,options);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        Toolbar toolbar=findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawer=findViewById(R.id.mydrawer);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        constraintLayout=findViewById(R.id.afterconst);
        toggle.syncState();
        navigation=findViewById(R.id.mynav);
        if (savedInstanceState ==null){
            current=new DashboardFragment(progressBar,thisactivity);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,current).commit();
            navigation.setCheckedItem(R.id.nav_home);
        }
        updateBalance(navigation);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_prof:
                        current=new ProfileFragment(thisactivity);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,current).commit();
                        break;
                    case R.id.nav_home:
                        current=new DashboardFragment(progressBar,thisactivity);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,current).commit();
                        break;
                    case R.id.nav_cardo:
                        current=new ListCardsFragment(thisactivity);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,current).commit();
                        break;
                    case R.id.nav_logout:
                        if (ParseUser.getCurrentUser()!=null){
                            getCurrentFragment();
                            App.makeClickable(View.VISIBLE,false,constraintLayout,progressBar);
                            ParseUser.logOutInBackground(new LogOutCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e==null){
                                        Intent intent=new Intent(AfterloginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        App.makeClickable(View.INVISIBLE,true,constraintLayout,progressBar);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }

    public  void makeClickable(int visibility, boolean clickable){
        progressBar.setVisibility(visibility);
        for (int i=0;i<constraintLayout.getChildCount();i++){
            constraintLayout.getChildAt(i).setClickable(clickable);
        }
    }

    public void getCurrentFragment(){
        if (current instanceof DashboardFragment){
            constraintLayout=((DashboardFragment) current).constraintLayout;
        }else if (current instanceof TotalFragment){
            constraintLayout=((TotalFragment) current).constraintLayout;
        }else if (current instanceof SendFragment){
            constraintLayout=((SendFragment) current).constraintLayout;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else if (current instanceof AddCardFragment){
            current=new ListCardsFragment(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,current).commit();
        }else if(current instanceof TransactionSingleFragment){
            current=new TransactionsFragment(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,current).commit();
        }
        else if(!(current instanceof DashboardFragment)){
            current=new DashboardFragment(progressBar,this);
            navigation.setCheckedItem(R.id.nav_home);
            navigation.getCheckedItem().setChecked(true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,current).commit();
        }
        else{
            this.finishAffinity();
        }
    }
}
