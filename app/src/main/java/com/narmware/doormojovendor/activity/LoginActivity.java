package com.narmware.doormojovendor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import com.narmware.doormojovendor.R;
import com.narmware.doormojovendor.helper.Endpoints;
import com.narmware.doormojovendor.helper.SharedPreferencesHelper;
import com.narmware.doormojovendor.helper.SupportFunctions;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    RequestQueue mVolleyRequest;
    @BindView(R.id.edt_uname) protected TextView mUsername;
    @BindView(R.id.edt_pass)protected TextView mPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        mVolleyRequest = Volley.newRequestQueue(LoginActivity.this);
        loginUser();

    }

    private void loginUser() {
        Gson gson=new Gson();
        HashMap<String,String> param = new HashMap();


        //url with params
        String url= SupportFunctions.appendParam(Endpoints.LOGIN_URL,param);

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

                            Log.e("Login Json_string",response.toString());
                            Gson gson = new Gson();
                        } catch (Exception e) {

                            e.printStackTrace();
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
