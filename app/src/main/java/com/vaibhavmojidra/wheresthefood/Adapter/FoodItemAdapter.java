package com.vaibhavmojidra.wheresthefood.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vaibhavmojidra.wheresthefood.DataRetrivalClasses.FoodItem;
import com.vaibhavmojidra.wheresthefood.R;
import com.vaibhavmojidra.wheresthefood.ReusableCodes.ConnectionClass;
import com.vaibhavmojidra.wheresthefood.ReusableCodes.ReusableFunctionsAndObjects;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.ViewHolder> {

    private Context mContext;
    private List<FoodItem> foodlist;

    public FoodItemAdapter(Context context,List<FoodItem> mlist){
        this.foodlist=mlist;
        this.mContext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.food_itemfood,parent,false);
        return new FoodItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final FoodItem foodItem=foodlist.get(position);
        holder.foodname.setText(foodItem.getFoodName());
        holder.price.setText("Price: Rs "+foodItem.getPrice());
        try {
            Bitmap b=ReusableFunctionsAndObjects.imageList.get(position);
            holder.imageView.setImageBitmap(b);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ReusableFunctionsAndObjects.con = new ConnectionClass().CONN();
            PreparedStatement p = ReusableFunctionsAndObjects.con.prepareStatement("Select * from Cart where EmailAdd=? and FoodID=?");
            p.setString(1, ReusableFunctionsAndObjects.UserEmail);
            p.setString(2,foodItem.getFoodID());
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                holder.add.setText("Added To Cart");
            }
        } catch (Exception e)
        {
            ReusableFunctionsAndObjects.showMessageAlert(mContext,"Network Error"," Unable to connect server","Ok");
        }

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ReusableFunctionsAndObjects.con = new ConnectionClass().CONN();
                    PreparedStatement p = ReusableFunctionsAndObjects.con.prepareStatement("Select * from Cart where EmailAdd=? and FoodID=?");
                    p.setString(1, ReusableFunctionsAndObjects.UserEmail);
                    p.setString(2,foodItem.getFoodID());
                    ResultSet rs = p.executeQuery();
                    if(rs.next()){
                        ReusableFunctionsAndObjects.showMessageAlert(mContext,"ALREADY ADDED TO CART","The food item has been already add to the cart you can remove or update the item from cart","Ok");
                    }else{
                        final AlertDialog.Builder d = new AlertDialog.Builder(mContext);
                        final Activity activity = (Activity) mContext;
                        LayoutInflater inflater = activity.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.number_picker_dialog, null);
                        d.setTitle("Add To Cart");
                        d.setMessage("Select Quantity");
                        d.setView(dialogView);
                        final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.dialog_number_picker);
                        numberPicker.setMaxValue(10000);
                        numberPicker.setMinValue(1);
                        numberPicker.setWrapSelectorWheel(false);
                        d.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    ReusableFunctionsAndObjects.con = new ConnectionClass().CONN();
                                    PreparedStatement p = ReusableFunctionsAndObjects.con.prepareStatement("insert into Cart values(?,?,?,?,?)");
                                    p.setString(1, ReusableFunctionsAndObjects.UserEmail);
                                    p.setString(2,foodItem.getFoodID());
                                    p.setString(3,foodItem.getFoodName());
                                    p.setString(4,foodItem.getPrice());
                                    p.setInt(5,numberPicker.getValue());
                                    int c = p.executeUpdate();
                                    if(c==1){
                                        Toast.makeText(mContext, "Added To Cart", Toast.LENGTH_SHORT).show();
                                        holder.add.setText("Added to cart");
                                    }else{
                                        ReusableFunctionsAndObjects.showMessageAlert(mContext,"Network Error"," Unable to connect server","Ok");
                                    }
                                } catch (Exception e)
                                {
                                    ReusableFunctionsAndObjects.showMessageAlert(mContext,"Network Error"," Unable to connect server","Ok");
                                }
                            }
                        });
                        d.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        AlertDialog alertDialog = d.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }
                } catch (Exception e)
                {
                    ReusableFunctionsAndObjects.showMessageAlert(mContext,"Network Error"," Unable to connect server","Ok");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView price;
        public TextView foodname;
        public Button add;
        public ImageView imageView;
        public ViewHolder(View itemView){
            super(itemView);
            price=itemView.findViewById(R.id.Price);
            foodname=itemView.findViewById(R.id.ItemName);
            add=itemView.findViewById(R.id.addItemB);
            imageView=itemView.findViewById(R.id.foodpic);
        }
    }
}

