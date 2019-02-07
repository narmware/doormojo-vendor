package com.narmware.doormojovendor.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.narmware.doormojovendor.MyApplication;
import com.narmware.doormojovendor.R;
import com.narmware.doormojovendor.helper.Constants;
import com.narmware.doormojovendor.helper.Endpoints;
import com.narmware.doormojovendor.helper.SharedPreferencesHelper;
import com.narmware.doormojovendor.helper.SupportFunctions;
import com.narmware.doormojovendor.pojo.Order;
import com.narmware.doormojovendor.pojo.OrderResponse;
import com.narmware.doormojovendor.pojo.Profile;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.prof_name) TextView mTxtProfName;
    @BindView(R.id.prof_mail) TextView mTxtProfMail;
    @BindView(R.id.prof_addr) TextView mTxtProfAddr;
    @BindView(R.id.prof_reg_date) TextView mTxtProfRegDate;
    @BindView(R.id.txt_mobile1) TextView mTxtMob1;
    @BindView(R.id.txt_mobile2) TextView mTxtMob2;
    @BindView(R.id.txt_mobile3) TextView mTxtMob3;
    @BindView(R.id.txt_mobile4) TextView mTxtMob4;

    RequestQueue mVolleyRequest;
    Profile profileResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        mVolleyRequest = Volley.newRequestQueue(ProfileActivity.this);

        getProfile();

    }

    public void setData()
    {
            mTxtProfName.setText(profileResponse.getName());
            mTxtProfMail.setText(profileResponse.getEmail());
            mTxtProfRegDate.setText("Registered On : "+profileResponse.getReg_date());

        if(profileResponse.getAddress()!="") {
            mTxtProfAddr.setText(profileResponse.getAddress());
        }

            if(profileResponse.getMobile1()!="") {
                mTxtMob1.setText(profileResponse.getMobile1());
            }

        if(profileResponse.getMobile2()!="") {
            mTxtMob2.setText(profileResponse.getMobile2());
        }

        if(profileResponse.getMobile3()!="") {

            mTxtMob3.setText(profileResponse.getMobile3());
        }

        if(profileResponse.getMobile4()!="") {

            mTxtMob4.setText(profileResponse.getMobile4());
        }

    }

    private void getProfile() {
        HashMap<String,String> param = new HashMap();
        param.put(Endpoints.VENDOR_ID, SharedPreferencesHelper.getUserId(ProfileActivity.this));

        String url= SupportFunctions.appendParam(Endpoints.GET_VENDOR_PROFILE,param);
        final ProgressDialog dialog = new ProgressDialog(ProfileActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Getting Details");
        dialog.setTitle("Connecting ...");
        if(!dialog.isShowing()) dialog.show();

        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {

                        try
                        {

                            Gson gson = new Gson();
                            Log.e("Profile Json_string",response.toString());
                            profileResponse=gson.fromJson(response.toString(),Profile.class);
                            setData();
                            if(dialog.isShowing()) dialog.dismiss();

                        } catch (Exception e) {

                            e.printStackTrace();
                            if(dialog.isShowing()) dialog.dismiss();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        MyApplication.mt("Server not reachable", ProfileActivity.this);
                        if(dialog.isShowing()) dialog.dismiss();

                    }
                }
        );
        mVolleyRequest.add(obreq);
    }

}
