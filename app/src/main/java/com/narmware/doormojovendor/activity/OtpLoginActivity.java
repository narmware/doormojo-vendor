package com.narmware.doormojovendor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.narmware.doormojovendor.MyApplication;
import com.narmware.doormojovendor.R;
import com.narmware.doormojovendor.helper.Constants;
import com.narmware.doormojovendor.helper.SharedPreferencesHelper;
import com.narmware.doormojovendor.helper.SupportFunctions;
import com.narmware.doormojovendor.pojo.Login;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OtpLoginActivity extends AppCompatActivity {

    @BindView(R.id.btn_signin) Button mbtnSignIn;
    @BindView(R.id.btn_submit_otp) Button mbtnSubmitOtp;
    @BindView(R.id.btn_resend) Button mbtnResendOtp;

    @BindView(R.id.login_card)
    CardView mCardLogin;
    @BindView(R.id.otp_card)
    CardView mCardOtp;
    public static EditText mEdtOtp,mEdtName,mEdtMobile;
    RequestQueue mVolleyRequest;
    String name,mobile,otp,server_otp,user_id;
    int validFlag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_login);
        ButterKnife.bind(this);
        mVolleyRequest = Volley.newRequestQueue(OtpLoginActivity.this);

        getSupportActionBar().hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mEdtOtp=findViewById(R.id.edt_otp);
        mEdtName=findViewById(R.id.edt_name);
        mEdtMobile=findViewById(R.id.edt_mobile);

        mbtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validFlag=0;

                name=mEdtName.getText().toString().trim();
                mobile=mEdtMobile.getText().toString().trim();

                if(name.equals(""))
                {
                    validFlag=1;
                    mEdtName.setError("Enter name");
                }
                if(name.equals(""))
                {
                    validFlag=1;
                    mEdtName.setError("Enter name");
                }

                if(validFlag==0)
                {
                    mCardLogin.setVisibility(View.GONE);
                    mCardOtp.setVisibility(View.VISIBLE);

                    YoYo.with(Techniques.SlideInRight)
                            .duration(400)
                            .playOn(mCardOtp);

                    LoginUser();
                }
            }
        });

        mbtnSubmitOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp=mEdtOtp.getText().toString().trim();
                if(otp.equals(""))
                {
                    mEdtOtp.setError("Enter OTP");
                }
                else{
                    if(otp.equals(server_otp))
                    {
                        SharedPreferencesHelper.setUserId(user_id,OtpLoginActivity.this);
                        SharedPreferencesHelper.setUserName(name,OtpLoginActivity.this);
/*
                        SharedPreferencesHelper.setUserMobile(mobile,OtpLoginActivity.this);
*/
                        SharedPreferencesHelper.setIsLogin(true,OtpLoginActivity.this);

                        /*Intent intent=new Intent(OtpLoginActivity.this,LocationActivity.class);
                        intent.putExtra("ActivityCall",Constants.CALL_FROM_LOGIN);
                        startActivity(intent);
                        finish();*/

                        Toast.makeText(OtpLoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mbtnResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResendOtp();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(mCardOtp.getVisibility()==View.VISIBLE)
        {
            mCardLogin.setVisibility(View.VISIBLE);
            mCardOtp.setVisibility(View.GONE);

            YoYo.with(Techniques.SlideInLeft)
                    .duration(400)
                    .playOn(mCardLogin);
        }
        else{
            super.onBackPressed();
        }
    }


    private void LoginUser() {


        Gson gson=new Gson();

        HashMap<String,String> param = new HashMap();
        param.put(Constants.MOBILE_NUMBER,mobile);
        param.put(Constants.NAME,name);

        //url with params
        String url= SupportFunctions.appendParam(MyApplication.LOGIN_USER,param);

        //url without params
        //String url= MyApplication.GET_CATEGORIES;

        Log.e("Login url",url);
        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET,url,null,
                // The third parameter Listener overrides the method onResponse() and passes
                //JSONObject as a parameter
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {

                        try
                        {
                            //getting test master array
                            // testMasterDetails = testMasterArray.toString();

                            Log.e("Login Json_string",response.toString());
                            Gson gson = new Gson();

                            Login loginResponse= gson.fromJson(response.toString(), Login.class);
                            if(loginResponse.getResponse().equals("100") || loginResponse.getResponse().equals("200") )
                            {

                                server_otp=loginResponse.getOTP();
                                Toast.makeText(OtpLoginActivity.this,"Your OTP is" + server_otp,Toast.LENGTH_LONG).show();
                                mEdtOtp.setText(server_otp);
                                user_id=loginResponse.getUser_id();
                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                            //Toast.makeText(NavigationActivity.this, "Invalid album id", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Test Error");
                        // showNoConnectionDialog();

                    }
                }
        );
        mVolleyRequest.add(obreq);
    }

    private void ResendOtp() {


        Gson gson=new Gson();

        HashMap<String,String> param = new HashMap();
        param.put(Constants.MOBILE_NUMBER,mobile);

        //url with params
        String url= SupportFunctions.appendParam(MyApplication.LOGIN_USER,param);

        //url without params
        //String url= MyApplication.GET_CATEGORIES;

        Log.e("Login url",url);
        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET,url,null,
                // The third parameter Listener overrides the method onResponse() and passes
                //JSONObject as a parameter
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {

                        try
                        {
                            //getting test master array
                            // testMasterDetails = testMasterArray.toString();

                            Log.e("Login Json_string",response.toString());
                            Gson gson = new Gson();

                            Login loginResponse= gson.fromJson(response.toString(), Login.class);
                            if(loginResponse.getResponse().equals("100") || loginResponse.getResponse().equals("200") )
                            {
                                server_otp=loginResponse.getOTP();
                                Toast.makeText(OtpLoginActivity.this,server_otp,Toast.LENGTH_LONG).show();
                                user_id=loginResponse.getUser_id();
                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                            //Toast.makeText(NavigationActivity.this, "Invalid album id", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Test Error");
                        // showNoConnectionDialog();

                    }
                }
        );
        mVolleyRequest.add(obreq);
    }



}
