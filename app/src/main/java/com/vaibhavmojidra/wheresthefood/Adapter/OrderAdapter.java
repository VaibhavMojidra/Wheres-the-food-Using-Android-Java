package com.vaibhavmojidra.wheresthefood.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vaibhavmojidra.wheresthefood.DataRetrivalClasses.OrderItem;
import com.vaibhavmojidra.wheresthefood.R;
import com.vaibhavmojidra.wheresthefood.ReusableCodes.ReusableFunctionsAndObjects;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private Context mContext;
    private List<OrderItem> orderlist;
    public OrderAdapter(Context context, List<OrderItem> cartlist){
        this.orderlist=cartlist;
        this.mContext=context;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.order_item,parent,false);
        return new OrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OrderItem orderItem=orderlist.get(position);
        holder.foodname.setText(orderItem.getFoodName());
        holder.token.setText("Order Token: "+orderItem.getToken());
        holder.TotalPrice.setText("Rs "+orderItem.getPrice()+" x "+orderItem.getQuantity()+"    Total: "+orderItem.getTotalPrice());
        ReusableFunctionsAndObjects.totall+=(orderItem.getTotalPrice());
        ReusableFunctionsAndObjects.GTP.setText("Grand Total: Rs "+ReusableFunctionsAndObjects.totall);
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView token;
        public TextView foodname;
        public TextView TotalPrice;

        public ViewHolder(View itemView){
            super(itemView);
            foodname=itemView.findViewById(R.id.ItemName);
            TotalPrice=itemView.findViewById(R.id.TotalPrice);
            token=itemView.findViewById(R.id.Token);
        }
    }
}
