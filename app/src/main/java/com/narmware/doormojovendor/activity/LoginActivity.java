package com.narmware.doormojovendor.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.narmware.doormojovendor.helper.Constants;
import com.narmware.doormojovendor.helper.Endpoints;
import com.narmware.doormojovendor.helper.SharedPreferencesHelper;
import com.narmware.doormojovendor.helper.SupportFunctions;
import com.narmware.doormojovendor.pojo.Login2;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    RequestQueue mVolleyRequest;
    @BindView(R.id.edt_uname) protected TextView mUsername;
    @BindView(R.id.edt_pass)protected TextView mPassword;
    @BindView(R.id.btn_login) protected Button mLogin;
    Dialog mNoConnectionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        mVolleyRequest = Volley.newRequestQueue(LoginActivity.this);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(areFieldsFilled()) {
                    loginUser(mUsername.getText().toString(), mPassword.getText().toString());
                }
                else {
                    MyApplication.mt("Please fill all details", LoginActivity.this);
                }

            }
        });

    }

    private boolean areFieldsFilled() {
        boolean flag = true;
        if(mUsername.getText().toString().trim() == "" || mUsername.getText().toString().trim().isEmpty()) {
            flag = false;
            mUsername.setError("Cannot be left blank");
        }

        if(mPassword.getText().toString().trim() == "" || mPassword.getText().toString().trim().isEmpty()) {
            flag = false;
            mPassword.setError("Cannot be left blank");
        }
        return flag;
    }
    private void loginUser(String username, String password) {
        HashMap<String,String> param = new HashMap();
        param.put(Endpoints.LOGIN_USERNAME, username);
        param.put(Endpoints.LOGIN_PASSWORD, password);

        String url= SupportFunctions.appendParam(Endpoints.LOGIN_URL,param);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("Validating credentals");
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
                            Log.e("Login Json_string",response.toString());
                                switch (Integer.parseInt(data.getResponse())) {
                                    case Constants.LOGIN_SUCCESS:
                                        SharedPreferencesHelper.setIsLogin(true,LoginActivity.this);
                                        SharedPreferencesHelper.setUserName(data.getName(),LoginActivity.this);
                                        SharedPreferencesHelper.setUserMail(data.getEmail(),LoginActivity.this);
                                        SharedPreferencesHelper.setUserM1(data.m1,LoginActivity.this);
                                        SharedPreferencesHelper.setUserM2(data.m2,LoginActivity.this);
                                        SharedPreferencesHelper.setUserM3(data.m3,LoginActivity.this);
                                        SharedPreferencesHelper.setUserM4(data.m4,LoginActivity.this);
                                        SharedPreferencesHelper.setUserId(data.vendor_id,LoginActivity.this);

                                        Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                        break;

                                    case Constants.LOGIN_ACCOUNT_NOT_VERIFIED:
                                        break;

                                    case Constants.LOGIN_INVALID_CREDENTIALS:
                                        break;
                                }

                                MyApplication.mt(data.getMsg(), LoginActivity.this);
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
                        MyApplication.mt("Server not reachable", LoginActivity.this);
                        showNoConnectionDialog();
                        if(dialog.isShowing()) dialog.dismiss();
                    }
                }
        );
        mVolleyRequest.add(obreq);
    }
    private void showNoConnectionDialog() {
        mNoConnectionDialog = new Dialog(LoginActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        mNoConnectionDialog.setContentView(R.layout.dialog_no_internet);
        mNoConnectionDialog.setCancelable(false);
        mNoConnectionDialog.show();

        Button tryAgain = mNoConnectionDialog.findViewById(R.id.txt_retry);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(mUsername.getText().toString(), mPassword.getText().toString());
            }
        });
    }

}
