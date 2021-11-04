package com.vaibhavmojidra.wheresthefood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vaibhavmojidra.wheresthefood.Fragments.CartFragment;
import com.vaibhavmojidra.wheresthefood.Fragments.FoodFragment;
import com.vaibhavmojidra.wheresthefood.Fragments.OrdersFragment;
import com.vaibhavmojidra.wheresthefood.ReusableCodes.ConnectionClass;
import com.vaibhavmojidra.wheresthefood.ReusableCodes.ReusableFunctionsAndObjects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Pattern;

public class MainScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        BottomNavigationView navigationView=findViewById(R.id.studentbnav);
        navigationView.setOnNavigationItemSelectedListener(MainScreen.this);
        loadFragment(new FoodFragment(),"Food Items");
        SharedPreferences pref = getApplicationContext().getSharedPreferences("LOGIND", Context.MODE_PRIVATE);
        String lst=pref.getString("LOGIN_STATUS", null);
        if(lst!=null){
            if(lst.equalsIgnoreCase("LOGGEDIN")){
                String eml=pref.getString("LOGIN_EMAIL", null);
                if(eml!=null){
                    if (Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$").matcher(eml.trim()).matches()) {
                        try {
                            ReusableFunctionsAndObjects.con = new ConnectionClass().CONN();
                            PreparedStatement ps = ReusableFunctionsAndObjects.con.prepareStatement("Select * from UserDetails where EmailAdd=?");
                            ps.setString(1, eml.trim());
                            ResultSet rs = ps.executeQuery();
                            if(rs.next()){
                                ReusableFunctionsAndObjects.UserEmail=eml.trim();
                            }else{
                                logout();
                            }
                        }catch (Exception e){}
                    }
                    else{
                        logout();
                    }
                }
                else{
                    logout();
                }
            }
            else{
                logout();
            }
        }else{
            logout();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.myoptionsmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logoutop){
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean loadFragment(Fragment fragment, String title){
        if(fragment!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
            getSupportActionBar().setTitle(Html.fromHtml("<font>"+title+"</font>"));
            return true;
        }
        return  false;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment=null;
        String t="";
        switch (menuItem.getItemId()){
            case R.id.fooditem:
                fragment= new FoodFragment();
                t="Food Items";
                break;
            case R.id.cartitem:
                fragment= new CartFragment();
                t="Cart";
                break;
            case R.id.orderitem:
                fragment= new OrdersFragment();
                t="Orders";
                break;
        }
        return loadFragment(fragment,t);
    }
    private void logout(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("LOGIND", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("LOGIN_STATUS","LOGGEDOUT");
        editor.putString("LOGIN_EMAIL", "EMPTY");
        editor.commit();
        Intent intent = new Intent(MainScreen.this,LROptionsPage.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }
}
