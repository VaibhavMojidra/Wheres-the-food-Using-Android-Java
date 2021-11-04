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

public class RegistrationOTP extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private EditText OTPTB;
    private TextView countdowntv;
    private Button b, resendbutton;
    private String fname, lname, email, pass;
    private static String OTP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_otp);
        b = (Button) findViewById(R.id.VerifyBtn);
        resendbutton = (Button) findViewById(R.id.ResendBtn);
        countdowntv = (TextView) findViewById(R.id.countdownTV);
        OTPTB = (EditText) findViewById(R.id.OTPTBLYT);
        b.setEnabled(false);
        b.setBackgroundResource(R.drawable.disabledcustombutton);
        b.setTextColor(Color.parseColor("#A78F9F"));
        Intent intent = getIntent();
        fname = intent.getStringExtra("FN").trim();
        lname = intent.getStringExtra("LN").trim();
        email = intent.getStringExtra("EMAIL").trim();
        pass = intent.getStringExtra("PASS").trim();
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
                    Toast.makeText(RegistrationOTP.this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
                } else {
                    if (OTPTB.getText().toString().trim().equalsIgnoreCase(OTP)) {
                        try {
                            progressDialog.setMessage("Registering Please Wait..");
                            progressDialog.show();
                            ReusableFunctionsAndObjects.con = new ConnectionClass().CONN();
                            PreparedStatement p = ReusableFunctionsAndObjects.con.prepareStatement("insert into UserDetails values(?,?,?,?)");
                            p.setString(1, fname);
                            p.setString(2, lname);
                            p.setString(3, email);
                            p.setString(4, pass);
                            int i = p.executeUpdate();
                            if (i == 1) {
                                progressDialog.dismiss();
                                SharedPreferences pref = getApplicationContext().getSharedPreferences("LOGIND", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("LOGIN_STATUS","LOGGEDIN");
                                editor.putString("LOGIN_EMAIL", email);
                                editor.commit();
                                Intent intent = new Intent(RegistrationOTP.this,MainScreen. class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            } else {
                                progressDialog.dismiss();
                                ReusableFunctionsAndObjects.showMessageAlert(RegistrationOTP.this, "Network Error", "Unable to connect server", "Ok");
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                            ReusableFunctionsAndObjects.showMessageAlert(RegistrationOTP.this, "Network Error", "Unable to connect server", "Ok");
                        }
                    } else {
                        ReusableFunctionsAndObjects.showMessageAlert(RegistrationOTP.this, "Invalid OTP", "Please enter correct OTP.", "OK");
                    }
                }
            }
        });
        send();
    }

    private void send() {
        OTP = getRandomNumberString();
        SendMessageEmail e = new SendMessageEmail(email, "Verify your registration", "Your OTP for Registration is " + OTP);
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
        startActivity(new Intent(RegistrationOTP.this, Register.class));
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        RegistrationOTP.this.finish();
    }
}
