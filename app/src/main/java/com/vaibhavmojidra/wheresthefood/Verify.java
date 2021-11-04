package com.vaibhavmojidra.wheresthefood;

import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.vaibhavmojidra.wheresthefood.ReusableCodes.ConnectionClass;
import com.vaibhavmojidra.wheresthefood.ReusableCodes.ReusableFunctionsAndObjects;
import com.vaibhavmojidra.wheresthefood.ReusableCodes.SendMessageEmail;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

public class Verify extends AppCompatActivity {
    String Token;
    int orderno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        setTitle("Verify OTP");
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ReusableFunctionsAndObjects.con=new ConnectionClass().CONN();
                    Statement statement=ReusableFunctionsAndObjects.con.createStatement();
                    ResultSet rs=statement.executeQuery("Select * from  Orders where EmailAdd='"+ReusableFunctionsAndObjects.UserEmail+"'");
                    if(rs.next()){
                        ResultSet rs2=statement.executeQuery("select * from Orders where EmailAdd='"+ReusableFunctionsAndObjects.UserEmail+"' order by OrderNo DESC ");
                        if(rs2.next()){
                            orderno=rs2.getInt(2);
                            orderno=orderno+1;
                            checkForUnique();
                        }
                    }
                    else{
                        orderno=1;
                        checkForUnique();
                    }
                }
                catch (Exception e){

                }
            }
        });
    }

    private void checkForUnique(){
        String ran=getRandomNumberString();
        boolean bb=false;
        try {
            ReusableFunctionsAndObjects.con=new ConnectionClass().CONN();
            Statement statement=ReusableFunctionsAndObjects.con.createStatement();
            ResultSet rs=statement.executeQuery("SELECT DISTINCT Token FROM Orders where EmailAdd='"+ReusableFunctionsAndObjects.UserEmail+"'");
            while(rs.next()){
                if(rs.getString(1).trim().equals(ran)){
                    bb=true;
                    break;
                }
            }
            if(bb){
                checkForUnique();
            }
            else{
                Token=ran;
                insertAndDelete();
            }
        }
        catch (Exception e){

        }
    }
    private void insertAndDelete(){
        try {
            ReusableFunctionsAndObjects.con=new ConnectionClass().CONN();
            Statement statement=ReusableFunctionsAndObjects.con.createStatement();
            ResultSet rs=statement.executeQuery("Select * from  Cart where EmailAdd='"+ReusableFunctionsAndObjects.UserEmail+"'");
            while(rs.next()){
                PreparedStatement p=ReusableFunctionsAndObjects.con.prepareStatement("Insert into Orders values(?,?,?,?,?,?,?,?)");
                p.setString(1,ReusableFunctionsAndObjects.UserEmail);
                p.setInt(2,orderno);
                p.setString(3,rs.getString(2));
                p.setString(4,rs.getString(3));
                p.setString(5,rs.getString(4));
                p.setInt(6,rs.getInt(5));
                p.setInt(7,(Integer.parseInt(rs.getString(4).trim())*rs.getInt(5)));
                p.setString(8,Token);
                int cc=p.executeUpdate();
                if(cc==1){
                    try{
                        PreparedStatement p2 = ReusableFunctionsAndObjects.con.prepareStatement("Delete from Cart where EmailAdd=? and FoodID=?");
                        p2.setString(1, ReusableFunctionsAndObjects.UserEmail);
                        p2.setString(2,rs.getString(2));
                        p2.executeUpdate();
                    } catch (Exception ee)
                    {}
                }
            }
            SendMessageEmail e = new SendMessageEmail(ReusableFunctionsAndObjects.UserEmail, "Order Confirmed", "Your order have been placed.\nAnd your order Token is " + Token+"\nPlease do not share this token with any one for more details about the order you can see in the app");
            e.execute();
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(Verify.this).create();
            alertDialog.setTitle("Order Confirmed");
            alertDialog.setMessage("Your have been placed.");
            alertDialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(Verify.this,MainScreen.class);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            finish();
                        }
                    });
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
        catch (Exception e){
        }
    }
    protected static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }
}
