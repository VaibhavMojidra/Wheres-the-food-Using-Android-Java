package com.vaibhavmojidra.wheresthefood.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.vaibhavmojidra.wheresthefood.DataRetrivalClasses.CartItem;
import com.vaibhavmojidra.wheresthefood.DataRetrivalClasses.FoodItem;
import com.vaibhavmojidra.wheresthefood.Fragments.CartFragment;
import com.vaibhavmojidra.wheresthefood.MainScreen;
import com.vaibhavmojidra.wheresthefood.R;
import com.vaibhavmojidra.wheresthefood.ReusableCodes.ConnectionClass;
import com.vaibhavmojidra.wheresthefood.ReusableCodes.ReusableFunctionsAndObjects;

import java.sql.PreparedStatement;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context mContext;
    private List<CartItem> cartlist;

    public CartAdapter(Context context, List<CartItem> cartlist){
        this.cartlist=cartlist;
        this.mContext=context;
    }
    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.cartitem_item,parent,false);
        return new CartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        final CartItem cartItem=cartlist.get(position);
        holder.foodname.setText(cartItem.getFoodname());
        holder.qty.setText("× "+cartItem.getQuantity());
        holder.price.setText("Price: Rs "+cartItem.getPrice());
        holder.TotalPrice.setText("Total: Rs "+(Integer.parseInt(cartItem.getPrice().trim())*cartItem.getQuantity()));
        ReusableFunctionsAndObjects.totall+=(Integer.parseInt(cartItem.getPrice().trim())*cartItem.getQuantity());
        ReusableFunctionsAndObjects.GTP.setText("Grand Total: Rs "+ReusableFunctionsAndObjects.totall);
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ReusableFunctionsAndObjects.con = new ConnectionClass().CONN();
                    PreparedStatement p = ReusableFunctionsAndObjects.con.prepareStatement("Delete from Cart where EmailAdd=? and FoodID=?");
                    p.setString(1, ReusableFunctionsAndObjects.UserEmail);
                    p.setString(2,cartItem.getFoodID());
                    int c = p.executeUpdate();
                    if(c==1){
                        Toast.makeText(mContext, "Removed From Cart", Toast.LENGTH_SHORT).show();
                        CartFragment cartFragment= new CartFragment();
                        ((MainScreen)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, cartFragment,"OptionsFragment").addToBackStack(null).commit();
                    }else{
                        ReusableFunctionsAndObjects.showMessageAlert(mContext,"Network Error"," 1 Unable to connect server","Ok");
                    }
                } catch (Exception e)
                {
                    ReusableFunctionsAndObjects.showMessageAlert(mContext,"Network Error","2 Unable to connect server","Ok");
                }
            }
        });
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                numberPicker.setValue(cartItem.getQuantity());
                numberPicker.setWrapSelectorWheel(false);
                d.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            ReusableFunctionsAndObjects.con = new ConnectionClass().CONN();
                            PreparedStatement p = ReusableFunctionsAndObjects.con.prepareStatement("Update Cart Set Quantity=? where EmailAdd=? and FoodID=?");
                            p.setInt(1,numberPicker.getValue());
                            p.setString(2, ReusableFunctionsAndObjects.UserEmail);
                            p.setString(3,cartItem.getFoodID());
                            int c = p.executeUpdate();
                            if(c==1){
                                Toast.makeText(mContext, "Cart Updated", Toast.LENGTH_SHORT).show();
                                CartFragment cartFragment= new CartFragment();
                                ((MainScreen)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, cartFragment,"OptionsFragment").addToBackStack(null).commit();
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
        });
    }

    @Override
    public int getItemCount() {
        return cartlist.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView price;
        public TextView qty;
        public TextView foodname;
        public TextView TotalPrice;
        public Button remove,update;
        public ViewHolder(View itemView){
            super(itemView);
            price=itemView.findViewById(R.id.Price);
            qty=itemView.findViewById(R.id.QTY);
            foodname=itemView.findViewById(R.id.ItemName);
            TotalPrice=itemView.findViewById(R.id.TotalPrice);
            remove=itemView.findViewById(R.id.Remove);
            update=itemView.findViewById(R.id.Update);
        }
    }
}
