package forgot;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.seniorproject.App;
import com.example.seniorproject.R;
import com.example.seniorproject.SlidePageAdapter;
import com.example.seniorproject.senddialog.SendConfirm;
import com.example.seniorproject.signproc.CustomViewPager;
import com.example.seniorproject.signproc.Step3;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class ForgotStep1 extends Fragment {

    CustomViewPager custom;

    public ForgotStep1(CustomViewPager pager){
        custom=pager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.forgot1,container,false);
        Button forgot=rootview.findViewById(R.id.forgotbut);
        final EditText emailfield=rootview.findViewById(R.id.forgotfield);
        final ConstraintLayout constraintLayout=rootview.findViewById(R.id.forgotcon);
        final ProgressBar progressBar=rootview.findViewById(R.id.forgotbar);
        progressBar.setVisibility(View.INVISIBLE);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emailfield.getText().toString();
                if (email.equals("")){
                    Toast.makeText(getActivity(),"Please enter your email!",Toast.LENGTH_LONG).show();
                }else{
                    App.makeClickable(View.VISIBLE,false,constraintLayout,progressBar);
                    ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e==null){
                                SlidePageAdapter adapter=(SlidePageAdapter)custom.getAdapter();
                                ((Step3)adapter.getItem(1)).changeTexts("we have sent an email to your mailbox\nmake sure to check it","Email sent Successfully");
                                custom.setCurrentItem(1);
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);
                                builder.setMessage(e.getMessage())
                                        .setTitle("Alert")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        }).setIcon(android.R.drawable.ic_dialog_alert);
                                AlertDialog alertDialog=builder.create();
                                alertDialog.show();
                            }
                            App.makeClickable(View.INVISIBLE,true,constraintLayout,progressBar);
                        }
                    });
                }
            }
        });
        return rootview;
    }
}
