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
import java.util.regex.Pattern;

public class ChangePassword extends AppCompatActivity {
    String email,password,confirmpassword;
    private TextInputLayout passtb, confirmpasstb;
    private Button changepass;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        passtb = (TextInputLayout) findViewById(R.id.PasswordTB);
        confirmpasstb = (TextInputLayout) findViewById(R.id.CPasswordTB);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        changepass = (Button) findViewById(R.id.ChangeBtn);
        Intent intent = getIntent();
        email = intent.getStringExtra("EMAIL").trim();
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    progressDialog.setTitle("Updating Password");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    try {
                        ReusableFunctionsAndObjects.con = new ConnectionClass().CONN();
                        PreparedStatement p = ReusableFunctionsAndObjects.con.prepareStatement("UPDATE Userdetails SET Pass = ? WHERE EmailAdd= ?");
                        p.setString(1, password);
                        p.setString(2, email);
                        int i = p.executeUpdate();
                        if (i == 1) {
                            progressDialog.dismiss();
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("LOGIND", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("LOGIN_STATUS","LOGGEDOUT");
                            editor.putString("LOGIN_EMAIL", "EMPTY");
                            editor.commit();
                            AlertDialog alertDialog = new AlertDialog.Builder(ChangePassword.this).create();
                            alertDialog.setTitle("Password Updated");
                            alertDialog.setMessage("Your password has been reset, try login with new password.");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            Intent intent = new Intent(ChangePassword.this,LROptionsPage.class);
                                            startActivity(intent);
                                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                            finish();
                                        }
                                    });
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                        } else {
                            progressDialog.dismiss();
                            ReusableFunctionsAndObjects.showMessageAlert(ChangePassword.this, "Network Error", "Unable to connect server", "Ok");
                        }
                    }catch (Exception e){
                        progressDialog.dismiss();
                        ReusableFunctionsAndObjects.showMessageAlert(ChangePassword.this, "Network Error", "Unable to connect server", "Ok");
                    }
                }
            }
        });
    }

    private boolean isValid() {
        boolean isvalid = false;
        boolean ispassvalid = false;
        boolean isconpassvalid = false;
        password = passtb.getEditText().getText().toString().trim();
        confirmpassword = confirmpasstb.getEditText().getText().toString().trim();

        if (confirmpassword.equals("")) {
            confirmpasstb.setErrorEnabled(true);
            confirmpasstb.setError("Please re-enter password");
            confirmpasstb.requestFocus();
            isconpassvalid = false;
        } else {
            isconpassvalid = true;
        }

        if (password.equals("")) {
            passtb.setErrorEnabled(true);
            passtb.setError("Please enter password");
            passtb.requestFocus();
            ispassvalid = false;
        } else {
            if (Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,50})").matcher(((passtb.getEditText().getText()).toString()).trim()).matches()) {
                ispassvalid = true;
            } else {
                passtb.setErrorEnabled(true);
                passtb.setError("Password must be at least 6 characters containing at least 1 Uppercase, 1 lowercase & 1 digit");
                passtb.requestFocus();
                ispassvalid = false;
            }
        }
        if (ispassvalid && isconpassvalid) {
            if (confirmpassword.equals(password)) {
                isvalid = true;
            } else {
                confirmpasstb.setErrorEnabled(true);
                confirmpasstb.setError("Password doesn't match");
                confirmpasstb.requestFocus();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
        builder.setTitle("Cancel Password Reset");
        builder.setMessage("Are you sure? You want to cancel the password reset?");
        builder.setPositiveButton("Don't want to reset password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(ChangePassword.this, LROptionsPage.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            }
        });
        builder.setNegativeButton("No, i want to reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
