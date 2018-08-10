package com.narmware.doormojovendor.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.narmware.doormojovendor.MyApplication;
import com.narmware.doormojovendor.R;
import com.narmware.doormojovendor.pojo.Order;

import java.util.ArrayList;

/**
 * Created by rohitsavant on 08/08/18.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderItem> {
    protected Context mContext;
    protected ArrayList<Order> mData;

    public OrderAdapter(Context mContext, ArrayList<Order> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    private void showDetaisDialog() {
        Dialog d = new Dialog(mContext);
        d.setContentView(R.layout.order_dialog);
        d.setTitle("Order Details");
        d.show();
    }

    @NonNull
    @Override
    public OrderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_order, parent, false );
        return new OrderItem(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItem holder, int position) {
        Order singleItem = mData.get(position);
        holder.id.setText(singleItem.getId());
        holder.name.setText(singleItem.getName());
        holder.category.setText(singleItem.getCategory());
        holder.datestamp.setText(singleItem.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class OrderItem extends RecyclerView.ViewHolder {
        TextView id, name, category, datestamp;
        ImageView image;
        Button details;

        public OrderItem(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.order_id);
            name = itemView.findViewById(R.id.order_name);
            category = itemView.findViewById(R.id.order_category);
            datestamp = itemView.findViewById(R.id.order_date_time);
            details = itemView.findViewById(R.id.order_bt_more);

            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.mt(id.getText().toString(), mContext);
                    showDetaisDialog();
                }
            });

        }
    }
}
