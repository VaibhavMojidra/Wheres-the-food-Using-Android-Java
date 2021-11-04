package com.vaibhavmojidra.wheresthefood.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vaibhavmojidra.wheresthefood.Adapter.CartAdapter;
import com.vaibhavmojidra.wheresthefood.Adapter.FoodItemAdapter;
import com.vaibhavmojidra.wheresthefood.CardDetails;
import com.vaibhavmojidra.wheresthefood.ChangePassword;
import com.vaibhavmojidra.wheresthefood.DataRetrivalClasses.CartItem;
import com.vaibhavmojidra.wheresthefood.DataRetrivalClasses.FoodItem;
import com.vaibhavmojidra.wheresthefood.LROptionsPage;
import com.vaibhavmojidra.wheresthefood.MainScreen;
import com.vaibhavmojidra.wheresthefood.R;
import com.vaibhavmojidra.wheresthefood.ReusableCodes.ConnectionClass;
import com.vaibhavmojidra.wheresthefood.ReusableCodes.ReusableFunctionsAndObjects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<CartItem> cartitemList;
    private Button removeAll,Placeorder;
    private ProgressDialog progressDialog;
    private LinearLayout tbtns;
    private TextView tv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.cart_fragment,null);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartitemList= new ArrayList<>();
        ReusableFunctionsAndObjects.GTP=view.findViewById(R.id.GT);
        tbtns=view.findViewById(R.id.TotalBtns);
        tv=view.findViewById(R.id.EmptyMessage);
        removeAll=view.findViewById(R.id.RM);
        Placeorder=view.findViewById(R.id.PO);
        getCartItems();
        ReusableFunctionsAndObjects.totall=0;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        removeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Clear Cart");
                builder.setMessage("Are you sure? You want to remove all items from cart?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            progressDialog.setTitle("Removing from cart");
                            progressDialog.setMessage("Please wait...");
                            progressDialog.show();
                            ReusableFunctionsAndObjects.con = new ConnectionClass().CONN();
                            PreparedStatement p = ReusableFunctionsAndObjects.con.prepareStatement("Delete from Cart where EmailAdd=?");
                            p.setString(1, ReusableFunctionsAndObjects.UserEmail);
                            int c = p.executeUpdate();
                            if(c!=0){
                                progressDialog.dismiss();
                                Toast.makeText(view.getContext(), "All items Removed from Cart", Toast.LENGTH_SHORT).show();
                                CartFragment cartFragment= new CartFragment();
                                ((MainScreen)view.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, cartFragment,"OptionsFragment").addToBackStack(null).commit();
                            }else{
                                progressDialog.dismiss();
                                ReusableFunctionsAndObjects.showMessageAlert(view.getContext(),"Network Error","Unable to connect server","Ok");
                            }
                        } catch (Exception e)
                        {
                            progressDialog.dismiss();
                            ReusableFunctionsAndObjects.showMessageAlert(view.getContext(),"Network Error","Unable to connect server","Ok");
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        Placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), CardDetails.class));
            }
        });

        return view;
    }
    public void getCartItems() {
        try {
            ReusableFunctionsAndObjects.con=new ConnectionClass().CONN();
            Statement statement=ReusableFunctionsAndObjects.con.createStatement();
            ResultSet r=statement.executeQuery("Select count(*) from cart where EmailAdd='"+ReusableFunctionsAndObjects.UserEmail+"'");
            if(r.next()){
                if(r.getString(1).trim().equalsIgnoreCase("0")){
                    tbtns.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    tv.setVisibility(View.VISIBLE);
                }
                else{
                    tbtns.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.GONE);
                    ResultSet rs=statement.executeQuery("Select * from Cart where EmailAdd='"+ReusableFunctionsAndObjects.UserEmail+"'");
                    while (rs.next()){
                        CartItem f=new CartItem(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getInt(5));
                        cartitemList.add(f);
                    }
                }
            }
            adapter=new CartAdapter(getContext(),cartitemList);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            ReusableFunctionsAndObjects.showMessageAlert(getContext(), "", e.getMessage(), "OK");
        }
    }
}
