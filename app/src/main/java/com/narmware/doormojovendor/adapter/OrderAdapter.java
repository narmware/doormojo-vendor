package com.narmware.doormojovendor.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.narmware.doormojovendor.MyApplication;
import com.narmware.doormojovendor.R;
import com.narmware.doormojovendor.activity.HomeActivity;
import com.narmware.doormojovendor.activity.LoginActivity;
import com.narmware.doormojovendor.fragment.CurrentOrderFragment;
import com.narmware.doormojovendor.helper.Constants;
import com.narmware.doormojovendor.helper.Endpoints;
import com.narmware.doormojovendor.helper.SharedPreferencesHelper;
import com.narmware.doormojovendor.helper.SupportFunctions;
import com.narmware.doormojovendor.pojo.Login2;
import com.narmware.doormojovendor.pojo.Order;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by rohitsavant on 08/08/18.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderItem> {
    protected Context mContext;
    protected ArrayList<Order> mData;
    RequestQueue mVolleyRequest;

    public OrderAdapter(Context mContext, ArrayList<Order> mData) {
        this.mContext = mContext;
        this.mData = mData;

        mVolleyRequest = Volley.newRequestQueue(mContext);

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

    private void showCallDialog(final String number) {
        final Dialog d = new Dialog(mContext);
        d.setContentView(R.layout.call_dialog);
        d.setCancelable(false);

        Button mBtnCall=d.findViewById(R.id.btn_call);
        Button mBtnCancel=d.findViewById(R.id.btn_cancel);

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        mBtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+number));
                mContext.startActivity(intent);

                d.dismiss();
            }
        });

        d.show();
    }


    private void showRemarkDialog(final String order_id, final String vm_id, final String status) {
        final Dialog d = new Dialog(mContext);
        d.setContentView(R.layout.remark_dialog);
        d.setCancelable(false);

        final EditText mEdtRemark=d.findViewById(R.id.edt_remark);
        Button mBtnSubmit=d.findViewById(R.id.btn_submit_remark);
        Button mBtnCancel=d.findViewById(R.id.btn_cancel);

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendCompleteOrder(order_id,vm_id,status,mEdtRemark.getText().toString().trim());
                d.dismiss();
            }
        });


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
        holder.mItem=singleItem;

        holder.id.setText(singleItem.getBook_no());
        holder.name.setText(singleItem.getName());
        holder.category.setText(singleItem.getService());
        holder.datestamp.setText(singleItem.getOrder_date());
        holder.mTxtAddress.setText(singleItem.getAddress());

        if(singleItem.booking_status.equals(Constants.STATUS_START))
        {
            holder.mBtnCompleteOrder.setVisibility(View.GONE);
            holder.mBtnStopOrder.setVisibility(View.VISIBLE);
        }
        if(singleItem.booking_status.equals(Constants.STATUS_STOP))
        {
            holder.mBtnCompleteOrder.setVisibility(View.GONE);
            holder.mBtnStopOrder.setVisibility(View.GONE);
        }
        if(singleItem.booking_status.equals(Constants.STATUS_NEW))
        {
            holder.mBtnCompleteOrder.setVisibility(View.VISIBLE);
            holder.mBtnStopOrder.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(mData.size()==0)
        {
            CurrentOrderFragment.mEmptyLay.setVisibility(View.VISIBLE);
        }
        else{
            CurrentOrderFragment.mEmptyLay.setVisibility(View.INVISIBLE);
        }
        return mData.size();
    }

    class OrderItem extends RecyclerView.ViewHolder {
        TextView id, name, category, datestamp,mTxtAddress;
        ImageView image;
        Button details,mBtnCompleteOrder,mBtnStopOrder;
        ImageButton mBtnCall;
        Order mItem;

        public OrderItem(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.order_id);
            name = itemView.findViewById(R.id.order_name);
            category = itemView.findViewById(R.id.order_category);
            datestamp = itemView.findViewById(R.id.order_date_time);
            details = itemView.findViewById(R.id.order_bt_more);
            mBtnCall = itemView.findViewById(R.id.order_bt_call);
            mBtnCompleteOrder = itemView.findViewById(R.id.btn_complete_order);
            mBtnStopOrder = itemView.findViewById(R.id.btn_stop_order);
            mTxtAddress = itemView.findViewById(R.id.order_address);

            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // MyApplication.mt(mItem.getOrder_id(), mContext);
                    showDetaisDialog(mItem.getOrder_id(),mItem.getName(),mItem.getService(),mItem.getOrder_date(),mItem.getAddress(),mItem.getDescription());
                }
            });

            mBtnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showCallDialog(mItem.getMobile());

                }
            });

            mBtnCompleteOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendCompleteOrder(mItem.getOrder_id(),Constants.STATUS_START);
                    //showRemarkDialog(mItem.getOrder_id(),mItem.getVm_id(),"Close");
                }
            });

            mBtnStopOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure")
                            .setContentText("You want to close this order")
                            .setConfirmText("Yes")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sendCompleteOrder(mItem.getOrder_id(),Constants.STATUS_STOP);
                                }
                            })
                            .showCancelButton(true)
                            .setCancelText("Cancel")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                    //showRemarkDialog(mItem.getOrder_id(),mItem.getVm_id(),"Close");
                }
            });

        }
    }


    private void sendCompleteOrder(String order_id, final String status) {
        final HashMap<String,String> param = new HashMap();
        param.put("book_id", order_id);
        param.put(Endpoints.VENDOR_ID, SharedPreferencesHelper.getUserId(mContext));
        String url=null;

        if(status.equals(Constants.STATUS_START))
        {
             url= SupportFunctions.appendParam(Endpoints.START_ORDER,param);
        }
        if(status.equals(Constants.STATUS_STOP))
        {
             url= SupportFunctions.appendParam(Endpoints.STOP_ORDER,param);
        }
        Log.e("Order Json_string",url);

        final ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setCancelable(false);
        dialog.setTitle("Updating status");
        dialog.setTitle("Connecting ...");
        if(!dialog.isShowing()) dialog.show();

        //Log.e("Login url",url);
        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET,url,null,
                // The third parameter Listener overrides the method onResponse() and passes
                //JSONObject as a parameter
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {

                        try
                        {

                            Gson gson = new Gson();
                            Login2 data = gson.fromJson(response.toString(), Login2.class);
                            Log.e("Order Json_string",response.toString());

                            if(data.response.equals("100"))
                            {

                                if(status.equals(Constants.STATUS_START))
                                {
                                    Toast.makeText(mContext, "Service started", Toast.LENGTH_SHORT).show();
                                }
                                if(status.equals(Constants.STATUS_STOP))
                                {
                                    Toast.makeText(mContext, "Service stopped", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{

                                if(status.equals(Constants.STATUS_START))
                                {
                                    Toast.makeText(mContext, "Service already started", Toast.LENGTH_SHORT).show();
                                }
                                if(status.equals(Constants.STATUS_STOP))
                                {
                                    Toast.makeText(mContext, "Service already stopped", Toast.LENGTH_SHORT).show();
                                }

                            }
                            //MyApplication.mt(data.getMsg(), LoginActivity.this);
                            if(dialog.isShowing()) dialog.dismiss();


                        } catch (Exception e) {

                            e.printStackTrace();
                            if(dialog.isShowing()) dialog.dismiss();
                        }
                    }
                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("Volley", error.getMessage());
                        MyApplication.mt("Server not reachable", mContext);
                        if(dialog.isShowing()) dialog.dismiss();
                    }
                }
        );
        mVolleyRequest.add(obreq);
    }

}
