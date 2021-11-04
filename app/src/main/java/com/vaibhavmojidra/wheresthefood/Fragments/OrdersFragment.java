package com.vaibhavmojidra.wheresthefood.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vaibhavmojidra.wheresthefood.Adapter.FoodItemAdapter;
import com.vaibhavmojidra.wheresthefood.Adapter.OrderAdapter;
import com.vaibhavmojidra.wheresthefood.DataRetrivalClasses.CartItem;
import com.vaibhavmojidra.wheresthefood.DataRetrivalClasses.FoodItem;
import com.vaibhavmojidra.wheresthefood.DataRetrivalClasses.OrderItem;
import com.vaibhavmojidra.wheresthefood.R;
import com.vaibhavmojidra.wheresthefood.ReusableCodes.ConnectionClass;
import com.vaibhavmojidra.wheresthefood.ReusableCodes.ReusableFunctionsAndObjects;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private List<OrderItem> orderList;
    private TextView tv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.orders_fragment,null);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        ReusableFunctionsAndObjects.totall=0;
        ReusableFunctionsAndObjects.GTP=view.findViewById(R.id.GT);
        tv=view.findViewById(R.id.EmptyMessage);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderList= new ArrayList<>();
        getOrders();
        return view;
    }

    public void getOrders() {
        try {
            ReusableFunctionsAndObjects.con=new ConnectionClass().CONN();
            Statement statement=ReusableFunctionsAndObjects.con.createStatement();
            ResultSet r=statement.executeQuery("Select count(*) from Orders where EmailAdd='"+ReusableFunctionsAndObjects.UserEmail+"'");
            if(r.next()){
                if(r.getString(1).trim().equalsIgnoreCase("0")){
                    ReusableFunctionsAndObjects.GTP.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    tv.setVisibility(View.VISIBLE);
                }
                else{
                    ReusableFunctionsAndObjects.GTP.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.GONE);
                    ResultSet rs=statement.executeQuery("Select * from Orders where EmailAdd='"+ReusableFunctionsAndObjects.UserEmail+"'");
                    while (rs.next()){
                        OrderItem o=new OrderItem(rs.getString(4),rs.getString(5),rs.getInt(6),rs.getInt(7),rs.getString(8));
                        orderList.add(o);
                    }
                }
            }
            adapter=new OrderAdapter(getContext(),orderList);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            ReusableFunctionsAndObjects.showMessageAlert(getContext(), "", e.getMessage(), "OK");
        }
    }

}
