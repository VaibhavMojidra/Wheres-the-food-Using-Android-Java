package com.vaibhavmojidra.wheresthefood;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.vaibhavmojidra.wheresthefood.ReusableCodes.ConnectionClass;
import com.vaibhavmojidra.wheresthefood.ReusableCodes.ReusableFunctionsAndObjects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private TextInputLayout fntb, lntb, emtb, ptb, cptb;
    private Button res;
    private TextView lo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fntb = findViewById(R.id.FNTB);
        lntb = findViewById(R.id.LNTB);
        emtb = findViewById(R.id.EmailTB);
        ptb = findViewById(R.id.PasswordTB);
        cptb = findViewById(R.id.ConfirmP);
        res = findViewById(R.id.RES);
        lo = findViewById(R.id.LoginL);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        lo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,Login.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            }
        });
        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    try {
                        progressDialog.setMessage("Registering Please Wait..");
                        progressDialog.show();
                        ReusableFunctionsAndObjects.con = new ConnectionClass().CONN();
                        PreparedStatement ps = ReusableFunctionsAndObjects.con.prepareStatement("Select * from UserDetails where EmailAdd=?");
                        ps.setString(1, emtb.getEditText().getText().toString().trim());
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            progressDialog.dismiss();
                            ReusableFunctionsAndObjects.showMessageAlert(Register.this, "Already Registered", "This Email is already registered", "Ok");
                        } else {
                            progressDialog.dismiss();
                            AlertDialog alertDialog = new AlertDialog.Builder(Register.this).create();
                            alertDialog.setTitle("Verification in process");
                            alertDialog.setMessage("Partially Registered.Please check email for verification OTP");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            Intent i = new Intent(getApplicationContext(), RegistrationOTP.class);
                                            i.putExtra("FN",fntb.getEditText().getText().toString().trim());
                                            i.putExtra("LN",lntb.getEditText().getText().toString().trim());
                                            i.putExtra("EMAIL",emtb.getEditText().getText().toString().trim());
                                            i.putExtra("PASS",cptb.getEditText().getText().toString().trim());
                                            startActivity(i);
                                            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                                            Register.this.finish();
                                        }
                                    });
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        ReusableFunctionsAndObjects.showMessageAlert(Register.this, "Network Error", "Unable to connect server", "Ok");
                    }

                }
            }
        });
        addKeyListenerToTextInputLayout(fntb);
        addKeyListenerToTextInputLayout(lntb);
        addKeyListenerToTextInputLayout(emtb);
        addKeyListenerToTextInputLayout(ptb);
        addKeyListenerToTextInputLayout(cptb);

    }

    protected boolean isValid() {
        boolean isvalid = false;
        boolean ispass1fill = false, ispass2fill = false;
        boolean isfnvalid = false, islnvalid = false, isemailvalid = false, ispasswordsvalid = false;


        if ((((cptb.getEditText().getText()).toString()).trim()).equals("")) {
            cptb.setErrorEnabled(true);
            cptb.setError("Please enter password confirmation");
            cptb.requestFocus();
            ispass2fill = false;
        } else {
            ispass2fill = true;
        }

        if ((((ptb.getEditText().getText()).toString()).trim()).equals("")) {
            ptb.setErrorEnabled(true);
            ptb.setError("Please enter password");
            ptb.requestFocus();
            ispass1fill = false;
        } else {
            if (Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,50})").matcher(((ptb.getEditText().getText()).toString()).trim()).matches()) {
                ispass1fill = true;
            } else {
                ptb.setErrorEnabled(true);
                ptb.setError("Password must be at least 6 characters containing at least 1 Uppercase, 1 lowercase & 1 digit");
                ptb.requestFocus();
                ispass1fill = false;
            }
        }

        if (ispass1fill && ispass2fill) {
            if ((((ptb.getEditText().getText()).toString()).trim()).equals(((cptb.getEditText().getText()).toString()).trim())) {
                ispasswordsvalid = true;
            } else {
                cptb.setErrorEnabled(true);
                cptb.setError("Password doesn't match");
                cptb.requestFocus();
                ispasswordsvalid = false;
            }
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

        if ((((lntb.getEditText().getText()).toString()).trim()).equals("")) {
            lntb.setErrorEnabled(true);
            lntb.setError("Please enter last name");
            lntb.requestFocus();
            islnvalid = false;
        } else {
            islnvalid = true;
        }

        if ((((fntb.getEditText().getText()).toString()).trim()).equals("")) {
            fntb.setErrorEnabled(true);
            fntb.setError("Please enter first name");
            fntb.requestFocus();
            isfnvalid = false;
        } else {
            isfnvalid = true;
        }

        isvalid = (isfnvalid && isemailvalid && islnvalid && ispasswordsvalid) ? true : false;
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
        startActivity(new Intent(Register.this, LROptionsPage.class));
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }
}
