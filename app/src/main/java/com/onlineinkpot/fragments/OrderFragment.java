package com.onlineinkpot.fragments;


import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.onlineinkpot.R;
import com.onlineinkpot.activity.OrderHistoryActivity;
import com.onlineinkpot.adapters.FragmentOrderListAdapter;
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.models.Order;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {
    private static final String TAG = "OrderFragment";
    public PrefManager pref;
    private RecyclerView rvOrder;
    public List<Order> orderList;
    private FragmentOrderListAdapter mAdapter;
    private HashMap<String, String> userdetail;
    private ProgressDialog progressDialog;
    private TextView noData;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Order History");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, null, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(false);
        pref = new PrefManager(getContext());
        userdetail = pref.getUserDetail();
        initializeLayoutContents(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void initializeLayoutContents(View view) {
        noData = (TextView) view.findViewById(R.id.tv_noorder);
        orderList = new ArrayList<>();
        rvOrder = (RecyclerView) view.findViewById(R.id.rv_order);
        mAdapter = new FragmentOrderListAdapter(getContext(), orderList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvOrder.setLayoutManager(mLayoutManager);
        rvOrder.setAdapter(mAdapter);
        orderListProcess();
        rvOrder.addOnItemTouchListener(new Cons.RecyclerTouchListener(getContext(), rvOrder, new Cons.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Order order = orderList.get(position);
                Intent i = new Intent(getActivity(), OrderHistoryActivity.class);
                i.putExtra("price", order.getOrder_price());
                i.putExtra("orderId", order.getOrder_id());
                Bundle bundle1 = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.push_left_in, R.anim.push_left_out).toBundle();
                getActivity().startActivity(i, bundle1);

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    private void orderListProcess() {
        if (Cons.isNetworkAvailable(getContext())) {
            StringRequest orderRqest = new StringRequest(Request.Method.POST, Cons.URL_ORDER_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "orderResponse: " + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                String status = object.getString("status");
                                if (status.equals("0")) {
                                    noData.setVisibility(View.VISIBLE);
                                } else {
                                    JSONArray array = object.getJSONArray("orderList");
                                    for (int i = 0; i < array.length(); i++) {
                                        Order order = new Order();
                                        JSONObject objdata = array.getJSONObject(i);
                                        order.setOrder_id(objdata.getString("order_id"));
                                        order.setOrder_number(objdata.getString("order_number"));
                                        order.setOrder_price(objdata.getString("price"));
                                        order.setCourse_name(objdata.getString("course_name"));
                                        order.setCourse_valid(objdata.getString("course_valid"));
                                        order.setPayment_mode(objdata.getString("payment_mode"));
                                        order.setPayment_status(objdata.getString("payment_status"));
                                        order.setOrder_status(objdata.getString("order_status"));
                                        order.setPurchase_date(objdata.getString("purchase_date"));
                                        orderList.add(order);
                                    }
                                }
                                progressDialog.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Log.d(TAG, "Error : " + error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("studentid", userdetail.get(Cons.KEY_USER_ID));
                    Log.d(TAG, "getParams: " + params);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(orderRqest, TAG);
        } else {
            Toast.makeText(getContext(), "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }
}