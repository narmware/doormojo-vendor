package com.narmware.doormojovendor.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.narmware.doormojovendor.MyApplication;
import com.narmware.doormojovendor.R;
import com.narmware.doormojovendor.adapter.OrderAdapter;
import com.narmware.doormojovendor.helper.Endpoints;
import com.narmware.doormojovendor.helper.SharedPreferencesHelper;
import com.narmware.doormojovendor.helper.SupportFunctions;
import com.narmware.doormojovendor.pojo.Order;
import com.narmware.doormojovendor.pojo.OrderResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllOrderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllOrderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    RequestQueue mVolleyRequest;

    private View mView;
    private OrderAdapter mAdapter;
    private RecyclerView mRecycler;
    private ArrayList<Order> mData = new ArrayList<>();
    Dialog mNoConnectionDialog;
    public static LinearLayout mEmptyLay;

    private void init() {
        mVolleyRequest = Volley.newRequestQueue(getContext());

        mEmptyLay=mView.findViewById(R.id.lin_empty_order);
        mAdapter = new OrderAdapter(getActivity(), mData);
        mRecycler = mView.findViewById(R.id.recycler_current_order);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mRecycler.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        liveOrders();
    }
    public AllOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CurrentOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllOrderFragment newInstance(String param1, String param2) {
        AllOrderFragment fragment = new AllOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =inflater.inflate(R.layout.fragment_current_order, container, false);

        init();

        return mView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void liveOrders() {
        HashMap<String,String> param = new HashMap();
        param.put(Endpoints.VENDOR_ID, SharedPreferencesHelper.getUserId(getContext()));

        String url= SupportFunctions.appendParam(Endpoints.GET_ALL_ORDERS,param);
        final ProgressDialog dialog = new ProgressDialog(getContext());
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
                            Log.e("LiveOrder Json_string",response.toString());
                            OrderResponse orderResponse=gson.fromJson(response.toString(),OrderResponse.class);
                            Order[] orders=orderResponse.getData();
                            mData.clear();

                            for(Order item:orders)
                            {
                                mData.add(item);
                            }
                            mAdapter.notifyDataSetChanged();
                            if(dialog.isShowing()) dialog.dismiss();
                            //if(mNoConnectionDialog.isShowing()) mNoConnectionDialog.dismiss();

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
                        MyApplication.mt("Server not reachable", getContext());
                        showNoConnectionDialog();
                        if(dialog.isShowing()) dialog.dismiss();

                    }
                }
        );
        mVolleyRequest.add(obreq);
    }

    private void showNoConnectionDialog() {
        mNoConnectionDialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        mNoConnectionDialog.setContentView(R.layout.dialog_no_internet);
        mNoConnectionDialog.setCancelable(false);
        mNoConnectionDialog.show();

        Button tryAgain = mNoConnectionDialog.findViewById(R.id.txt_retry);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                liveOrders();
            }
        });
    }

}
