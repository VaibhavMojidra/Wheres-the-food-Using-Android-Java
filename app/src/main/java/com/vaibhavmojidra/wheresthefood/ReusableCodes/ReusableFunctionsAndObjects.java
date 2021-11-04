package com.vaibhavmojidra.wheresthefood.ReusableCodes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.widget.TextView;

import com.vaibhavmojidra.wheresthefood.DataRetrivalClasses.FoodItem;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ReusableFunctionsAndObjects {

    public static int foodcount=0;
    public static List<Bitmap> imageList= new ArrayList<>();
    public static List<String> imageURLS= new ArrayList<>();;
    public static Connection con;
    public static String UserEmail;
    public static int totall;
    public static TextView GTP;
    public static void showMessageAlert(Context context, String title, String message, String buttonstring) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, buttonstring, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
