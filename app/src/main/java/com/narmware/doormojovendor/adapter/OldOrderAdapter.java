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
import com.narmware.doormojovendor.fragment.CurrentOrderFragment;
import com.narmware.doormojovendor.fragment.PreviousOrderFragment;
import com.narmware.doormojovendor.pojo.Order;

import java.util.ArrayList;

/**
 * Created by rohitsavant on 08/08/18.
 */

public class OldOrderAdapter extends RecyclerView.Adapter<OldOrderAdapter.OrderItem> {
    protected Context mContext;
    protected ArrayList<Order> mData;

    public OldOrderAdapter(Context mContext, ArrayList<Order> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    private void showDetaisDialog(String order_id,String cust_name,String service,String date,String address,String desc) {
        Dialog d = new Dialog(mContext);
        d.setContentView(R.layout.order_dialog);

        TextView mTxtOrderId=d.findViewById(R.id.txt_order_id);
        TextView mTxtName=d.findViewById(R.id.txt_cust_name);
        TextView mTxtOrderCat=d.findViewById(R.id.txt_order_cat);
        TextView mTxtOrderDate=d.findViewById(R.id.txt_order_date);
        TextView mTxtOrderAddr=d.findViewById(R.id.txt_order_addr);
        TextView mTxtOrderDesc=d.findViewById(R.id.txt_order_desc);

        mTxtOrderId.setText(order_id);
        mTxtName.setText(cust_name);
        mTxtOrderCat.setText(service);
        mTxtOrderDate.setText(date);
        mTxtOrderAddr.setText(address);
        mTxtOrderDesc.setText(desc);

        d.setTitle("Order Details");
        d.show();
    }

    @NonNull
    @Override
    public OrderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_order_old, parent, false );
        return new OrderItem(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItem holder, int position) {
        Order singleItem = mData.get(position);
        holder.mItem=singleItem;

        holder.id.setText(singleItem.getOrder_id());
        holder.name.setText(singleItem.getName());
        holder.category.setText(singleItem.getService());
        holder.datestamp.setText(singleItem.getOrder_date());
    }

    @Override
    public int getItemCount() {
        if(mData.size()==0)
        {
            PreviousOrderFragment.mEmptyLay.setVisibility(View.VISIBLE);
        }
        else{
            PreviousOrderFragment.mEmptyLay.setVisibility(View.INVISIBLE);
        }
        return mData.size();
    }

    class OrderItem extends RecyclerView.ViewHolder {
        TextView id, name, category, datestamp;
        ImageView image;
        Button details;
        Order mItem;

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
                    showDetaisDialog(mItem.getOrder_id(),mItem.getName(),mItem.getService(),mItem.getOrder_date(),mItem.getAddress(),mItem.getDescription());
                }
            });

        }
    }
}
