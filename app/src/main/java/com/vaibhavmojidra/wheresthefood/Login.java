package com.vaibhavmojidra.wheresthefood;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.vaibhavmojidra.wheresthefood.ReusableCodes.ConnectionClass;
import com.vaibhavmojidra.wheresthefood.ReusableCodes.ReusableFunctionsAndObjects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
 TextInputLayout emtb,ptb;
 TextView resL,forgetPass;
 private ProgressDialog progressDialog;
 Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emtb=(TextInputLayout)findViewById(R.id.EmailTB);
        ptb=(TextInputLayout)findViewById(R.id.PasswordTB);
        b=(Button) findViewById(R.id.LoginBtn);
        resL=(TextView)findViewById(R.id.Regis);
        forgetPass=(TextView)findViewById(R.id.ForgetPass);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        resL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,ForgetPassEmail.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    progressDialog.setMessage("Authenticating, Please wait....");
                    progressDialog.show();
                    try {
                        ReusableFunctionsAndObjects.con = new ConnectionClass().CONN();
                        PreparedStatement ps = ReusableFunctionsAndObjects.con.prepareStatement("Select * from UserDetails where EmailAdd=?");
                        ps.setString(1, emtb.getEditText().getText().toString().trim());
                        ResultSet rs = ps.executeQuery();
                        if(rs.next()){
                            if(ptb.getEditText().getText().toString().trim().equals(rs.getString(4))){
                                progressDialog.dismiss();
                                SharedPreferences pref = getApplicationContext().getSharedPreferences("LOGIND", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("LOGIN_STATUS","LOGGEDIN");
                                editor.putString("LOGIN_EMAIL",emtb.getEditText().getText().toString().trim());
                                editor.commit();
                                Intent intent = new Intent(Login.this,MainScreen.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                finish();
                            }else{
                                progressDialog.dismiss();
                                ReusableFunctionsAndObjects.showMessageAlert(Login.this, "Invalid Password", "Your password is incorrect.", "Ok");
                            }
                        }
                        else{
                            progressDialog.dismiss();
                            ReusableFunctionsAndObjects.showMessageAlert(Login.this, "User Not Found", "This email doesn't belong to any account", "Ok");
                        }
                    }catch (Exception e){
                        progressDialog.dismiss();
                        ReusableFunctionsAndObjects.showMessageAlert(Login.this, "Network Error", "Unable to connect server", "Ok");
                    }

                }
            }
        });
        addKeyListenerToTextInputLayout(emtb);
        addKeyListenerToTextInputLayout(ptb);
    }

    protected boolean isValid() {
        boolean isvalid = false;
        boolean ispass1fill = false,isemailvalid = false;

        if ((((ptb.getEditText().getText()).toString()).trim()).equals("")) {
            ptb.setErrorEnabled(true);
            ptb.setError("Please enter password");
            ptb.requestFocus();
            ispass1fill = false;
        } else {
            ispass1fill = true;
        }


        if ((((emtb.getEditText().getText()).toString()).trim()).equals("")) {
            emtb.setErrorEnabled(true);
            emtb.setError("Please enter email address");
            emtb.requestFocus();
            isemailvalid = false;
        } else {
            if (Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$").matcher(((emtb.getEditText().getText()).toString()).trim()).matches()) {
                isemailvalid = true;
            } else {
                emtb.setErrorEnabled(true);
                emtb.setError("Please enter a valid email address");
                emtb.requestFocus();
                isemailvalid = false;
            }
        }

        isvalid = (ispass1fill&& isemailvalid) ? true : false;
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
        startActivity(new Intent(Login.this, LROptionsPage.class));
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }
}
