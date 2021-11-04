package com.vaibhavmojidra.wheresthefood.Fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.vaibhavmojidra.wheresthefood.Adapter.FoodItemAdapter;
import com.vaibhavmojidra.wheresthefood.DataRetrivalClasses.FoodItem;
import com.vaibhavmojidra.wheresthefood.R;
import com.vaibhavmojidra.wheresthefood.ReusableCodes.ConnectionClass;
import com.vaibhavmojidra.wheresthefood.ReusableCodes.ReusableFunctionsAndObjects;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FoodFragment extends Fragment {
    private RecyclerView recyclerView;
    private FoodItemAdapter adapter;
    private List<FoodItem> foodItemList;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.food_fragment, null);
        recyclerView = view.findViewById(R.id.recycler_view);
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        foodItemList= new ArrayList<>();
        swipeRefreshLayout=view.findViewById(R.id.my_refresh);
        getFoodNames();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView = view.findViewById(R.id.recycler_view);
                progressDialog = new ProgressDialog(view.getContext());
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                foodItemList= new ArrayList<>();
                getFoodNames();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    public void getFoodNames() {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        int count;
        try {
            ReusableFunctionsAndObjects.con=new ConnectionClass().CONN();
            Statement statement=ReusableFunctionsAndObjects.con.createStatement();
            ResultSet rsc=statement.executeQuery("select Count(*) from FoodItems");
            if(rsc.next()){
                 count=Integer.parseInt(rsc.getString(1));
                 if(count==ReusableFunctionsAndObjects.foodcount){
                     int c=0;
                     ResultSet rs=statement.executeQuery("Select * from fooditems");
                     while (rs.next()){
                         FoodItem f=new FoodItem(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4));
                         foodItemList.add(f);
                         if(!(f.getImageURL().trim().equalsIgnoreCase(ReusableFunctionsAndObjects.imageURLS.get(c)))){
                             ReusableFunctionsAndObjects.imageURLS.set(c,f.getImageURL().trim());
                             try {
                                 InputStream in = new BufferedInputStream(new URL(rs.getString(4).trim()).openStream(), 2000);
                                 Bitmap mIcon11 = BitmapFactory.decodeStream(in);
                                 ReusableFunctionsAndObjects.imageList.set(c,mIcon11);
                             } catch (IOException e) {
                                 progressDialog.dismiss();
                                 ReusableFunctionsAndObjects.showMessageAlert(getContext(), "", e.getMessage(), "OK");
                             }
                         }
                         c++;
                     }
                 }
                 else{
                     ResultSet rs=statement.executeQuery("Select * from fooditems");
                     ReusableFunctionsAndObjects.imageList.clear();
                     ReusableFunctionsAndObjects.imageURLS.clear();
                     foodItemList.clear();
                     while (rs.next()){
                         FoodItem f=new FoodItem(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4));
                         foodItemList.add(f);
                         ReusableFunctionsAndObjects.imageURLS.add(f.getImageURL().trim());
                         try {
                             InputStream in = new BufferedInputStream(new URL(rs.getString(4).trim()).openStream(), 2000);
                             Bitmap mIcon11 = BitmapFactory.decodeStream(in);
                             ReusableFunctionsAndObjects.imageList.add(mIcon11);
                         } catch (IOException e) {
                             progressDialog.dismiss();
                             ReusableFunctionsAndObjects.showMessageAlert(getContext(), "", e.getMessage(), "OK");
                         }
                     }
                     ReusableFunctionsAndObjects.foodcount=count;
                 }
            }
            adapter=new FoodItemAdapter(getContext(),foodItemList);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            progressDialog.dismiss();
            ReusableFunctionsAndObjects.showMessageAlert(getContext(), "", e.getMessage(), "OK");
        }
        progressDialog.dismiss();
    }
}
