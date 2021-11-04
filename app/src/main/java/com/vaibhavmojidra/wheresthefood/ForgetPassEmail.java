package com.vaibhavmojidra.wheresthefood;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.vaibhavmojidra.wheresthefood.ReusableCodes.ConnectionClass;
import com.vaibhavmojidra.wheresthefood.ReusableCodes.ReusableFunctionsAndObjects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Pattern;

public class ForgetPassEmail extends AppCompatActivity {
    TextInputLayout emtb;
    Button b;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass_email);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        b=findViewById(R.id.Next);
        emtb=findViewById(R.id.EmailTB);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    try {
                        progressDialog.setMessage("Validating, Please wait....");
                        progressDialog.show();
                        ReusableFunctionsAndObjects.con = new ConnectionClass().CONN();
                        PreparedStatement ps = ReusableFunctionsAndObjects.con.prepareStatement("Select * from UserDetails where EmailAdd=?");
                        ps.setString(1, emtb.getEditText().getText().toString().trim());
                        ResultSet rs = ps.executeQuery();
                        if(rs.next()){
                            progressDialog.dismiss();
                            AlertDialog alertDialog = new AlertDialog.Builder(ForgetPassEmail.this).create();
                            alertDialog.setTitle("OTP Sent");
                            alertDialog.setMessage("OTP for reset password will be send to your email");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            Intent i = new Intent(getApplicationContext(), LoginOTP.class);
                                            i.putExtra("EMAIL",emtb.getEditText().getText().toString().trim());
                                            startActivity(i);
                                            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                                            finish();
                                        }
                                    });
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                        }
                        else{
                            progressDialog.dismiss();
                            ReusableFunctionsAndObjects.showMessageAlert(ForgetPassEmail.this, "User Not Found", "This email doesn't belong to any account", "Ok");
                        }
                    }catch (Exception e){
                        progressDialog.dismiss();
                        ReusableFunctionsAndObjects.showMessageAlert(ForgetPassEmail.this, "Network Error", "Unable to connect server", "Ok");
                    }
                }
            }
        });
        addKeyListenerToTextInputLayout(emtb);
    }
    protected boolean isValid() {
        boolean isvalid = false;

        if ((((emtb.getEditText().getText()).toString()).trim()).equals("")) {
            emtb.setErrorEnabled(true);
            emtb.setError("Please enter email address");
            emtb.requestFocus();
            isvalid = false;
        } else {
            if (Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$").matcher(((emtb.getEditText().getText()).toString()).trim()).matches()) {
                isvalid = true;
            } else {
                emtb.setErrorEnabled(true);
                emtb.setError("Please enter a valid email address");
                emtb.requestFocus();
                isvalid = false;
            }
        }

        return isvalid;
    }
    private void addKeyListenerToTextInputLayout(final TextInputLayout textInputLayout) {
        textInputLayout.getEditText().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                textInputLayout.setError(null);
                textInputLayout.setErrorEnabled(false);
                return false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(ForgetPassEmail.this, Login.class));
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }
}
