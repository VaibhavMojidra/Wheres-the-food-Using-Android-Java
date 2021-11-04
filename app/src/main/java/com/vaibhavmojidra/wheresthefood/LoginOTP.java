package com.vaibhavmojidra.wheresthefood;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vaibhavmojidra.wheresthefood.ReusableCodes.ConnectionClass;
import com.vaibhavmojidra.wheresthefood.ReusableCodes.ReusableFunctionsAndObjects;
import com.vaibhavmojidra.wheresthefood.ReusableCodes.SendMessageEmail;

import java.sql.PreparedStatement;
import java.util.Random;

public class LoginOTP extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private EditText OTPTB;
    private TextView countdowntv;
    private Button b, resendbutton;
    private String email;
    private static String OTP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);
        b = (Button) findViewById(R.id.VerifyBtn);
        resendbutton = (Button) findViewById(R.id.ResendBtn);
        countdowntv = (TextView) findViewById(R.id.countdownTV);
        OTPTB = (EditText) findViewById(R.id.OTPTBLYT);
        b.setEnabled(false);
        b.setBackgroundResource(R.drawable.disabledcustombutton);
        b.setTextColor(Color.parseColor("#A78F9F"));
        Intent intent = getIntent();
        email = intent.getStringExtra("EMAIL").trim();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        resendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
        checker(OTPTB);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OTPTB.getText().toString().trim().equals("")) {
                    OTPTB.setError("Please enter OTP");
                    OTPTB.requestFocus();
                    Toast.makeText(LoginOTP.this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
                } else {
                    if (OTPTB.getText().toString().trim().equalsIgnoreCase(OTP)) {
                        Intent i = new Intent(getApplicationContext(), ChangePassword.class);
                        i.putExtra("EMAIL",email);
                        startActivity(i);
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                        finish();
                    } else {
                        ReusableFunctionsAndObjects.showMessageAlert(LoginOTP.this, "Invalid OTP", "Please enter correct OTP.", "OK");
                    }
                }
            }
        });
        send();
    }

    private void send() {
        OTP = getRandomNumberString();
        SendMessageEmail e = new SendMessageEmail(email, "Reset Password", "Your OTP for password reset: " + OTP);
        e.execute();
        Toast.makeText(this, "OTP has been sent please check you email", Toast.LENGTH_SHORT).show();
        countdowntv.setVisibility(View.VISIBLE);
        resendbutton.setVisibility(View.GONE);
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                countdowntv.setText("Resend OTP in " + (millisUntilFinished / 1000) + " seconds");
            }

            public void onFinish() {
                countdowntv.setVisibility(View.GONE);
                resendbutton.setVisibility(View.VISIBLE);
                this.cancel();
            }
        }.start();
    }

    protected static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }


    protected void checker(final EditText current) {
        current.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (OTPTB.getText().toString().trim().length() == 6) {
                    b.setEnabled(true);
                    b.setBackgroundResource(R.drawable.registerbbg);
                    b.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    b.setEnabled(false);
                    b.setBackgroundResource(R.drawable.disabledcustombutton);
                    b.setTextColor(Color.parseColor("#A78F9F"));
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginOTP.this, ForgetPassEmail.class));
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }
}
