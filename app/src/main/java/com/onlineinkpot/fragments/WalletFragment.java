package com.onlineinkpot.fragments;


import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onlineinkpot.R;
import com.onlineinkpot.activity.MainActivity;
import com.onlineinkpot.adapters.FragmentWalletAdapter;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.models.WalletHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WalletFragment extends Fragment {
    private static final String TAG = "WalletFragment";
    private HashMap<String, String> user;
    public PrefManager pref;
    public TabLayout chapTab;
    private RecyclerView walletRecylerv;
    private ProgressDialog progressDialog;
    private ArrayList<WalletHistory> alllist, paidlist, receivedList;
    private TextView passbookTv;
    private TextView walletBalanceLabelTv;
    public static TextView walletBalanceTv;
    private TextView detailNotFoundTv;
    public WalletFragment() {
    }
    public void refreshApi() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        getActivity().setTitle("Wallet");
        initView(view);
        setListenerView();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(true);
        pref = new PrefManager(getContext());
        user = pref.getUserDetail();
        String user_id = user.get(Cons.KEY_USER_ID);
        Log.d(TAG, "onCreateView: " + user_id);
        getWalletApi(Cons.get_wallete_hostory, user_id, 0);
        return view;
    }

    private void setListenerView() {
        chapTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                String user_id = user.get(Cons.KEY_USER_ID);
                getWalletApi(Cons.get_wallete_hostory, user_id, position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    private void initView(View view) {
        chapTab = (TabLayout) view.findViewById(R.id.wallet_tab);
        pref = new PrefManager(getContext());
        walletRecylerv = (RecyclerView) view.findViewById(R.id.wallet_tab_recyler_view);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getContext());
        walletRecylerv.setHasFixedSize(true);
        walletRecylerv.setLayoutManager(mLayoutManager1);
        walletRecylerv.setItemAnimator(new DefaultItemAnimator());
        passbookTv = (TextView) view.findViewById(R.id.wallet_passbook_tv);
        walletBalanceLabelTv = (TextView) view.findViewById(R.id.wallet_available_lable_tv);
        walletBalanceTv = (TextView) view.findViewById(R.id.wallet_available_tv);
        walletBalanceTv.setText(MainActivity.walleteTv.getText());
        detailNotFoundTv = (TextView) view.findViewById(R.id.wallet_not_found_tv);
        chapTab.addTab(chapTab.newTab().setText("All"));
        chapTab.setTabGravity(TabLayout.GRAVITY_FILL);
        chapTab.addTab(chapTab.newTab().setText("Paid"));
        chapTab.setTabGravity(TabLayout.GRAVITY_FILL);
        chapTab.addTab(chapTab.newTab().setText("Received"));
        chapTab.setTabGravity(TabLayout.GRAVITY_FILL);
        chapTab.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Chalkduster.ttf");
        passbookTv.setTypeface(face);
        walletBalanceLabelTv.setTypeface(face);
        walletBalanceTv.setTypeface(face);
        detailNotFoundTv.setTypeface(face);
    }
    protected void getWalletApi(String url, final String user_id, final int position) {
        if (Cons.isNetworkAvailable(getContext())) {
            final RequestQueue rq = Volley.newRequestQueue(getActivity());
            final StringRequest wallet = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            Log.d(TAG, "Data: " + response);
                            try {
                                JSONArray array = new JSONArray(response);
                                alllist = new ArrayList<>();
                                paidlist = new ArrayList<>();
                                receivedList = new ArrayList<>();
                                for (int i = 0; i < array.length(); i++) {
                                    try {
                                        JSONObject jsonObject = array.getJSONObject(i);
                                        WalletHistory wallet_hostory_all = new WalletHistory();
                                        wallet_hostory_all.setOrderNumber(jsonObject.getString("order_number"));
                                        wallet_hostory_all.setCId(jsonObject.getString("c_id"));
                                        wallet_hostory_all.setOrderId(jsonObject.getString("order_id"));
                                        wallet_hostory_all.setStudentId(jsonObject.getString("student_id"));
                                        wallet_hostory_all.setPromocId(jsonObject.getString("promoc_id"));
                                        wallet_hostory_all.setCbType(jsonObject.getString("cb_type"));
                                        wallet_hostory_all.setCbAmt(jsonObject.getString("cb_amt"));
                                        wallet_hostory_all.setCbStatus(jsonObject.getString("cb_status"));
                                        wallet_hostory_all.setCreatedon(jsonObject.getString("createdon"));
                                        if (!jsonObject.getString("order_number").equals("null")) {
                                            alllist.add(wallet_hostory_all);
                                            if (jsonObject.getString("cb_type").equals("Dr")) {
                                                paidlist.add(wallet_hostory_all);
                                            } else if (jsonObject.getString("cb_type").equals("Cr")) {
                                                receivedList.add(wallet_hostory_all);
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (position == 0) {
                                    FragmentWalletAdapter topicDataAdapter = new FragmentWalletAdapter(getContext(), alllist);
                                    walletRecylerv.setAdapter(topicDataAdapter);
                                    topicDataAdapter.notifyDataSetChanged();
                                    if (alllist.size() > 0) {
                                        detailNotFoundTv.setVisibility(View.GONE);
                                    } else {
                                        detailNotFoundTv.setVisibility(View.VISIBLE);
                                        detailNotFoundTv.setText("No all found");
                                    }
                                } else if (position == 1) {
                                    FragmentWalletAdapter topicDataAdapter = new FragmentWalletAdapter(getContext(), paidlist);
                                    walletRecylerv.setAdapter(topicDataAdapter);
                                    topicDataAdapter.notifyDataSetChanged();
                                    if (paidlist.size() > 0) {
                                        detailNotFoundTv.setVisibility(View.GONE);
                                    } else {
                                        detailNotFoundTv.setVisibility(View.VISIBLE);
                                        detailNotFoundTv.setText("No paid found");
                                    }
                                } else if (position == 2) {
                                    FragmentWalletAdapter topicDataAdapter = new FragmentWalletAdapter(getContext(), receivedList);
                                    walletRecylerv.setAdapter(topicDataAdapter);
                                    topicDataAdapter.notifyDataSetChanged();
                                    if (receivedList.size() > 0) {
                                        detailNotFoundTv.setVisibility(View.GONE);
                                    } else {
                                        detailNotFoundTv.setVisibility(View.VISIBLE);
                                        detailNotFoundTv.setText("No received found");
                                    }
                                }
                                progressDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    if (position == 0) {
                        detailNotFoundTv.setVisibility(View.VISIBLE);
                        detailNotFoundTv.setText("No all found");
                    } else if (position == 1) {
                        detailNotFoundTv.setVisibility(View.VISIBLE);
                        detailNotFoundTv.setText("No paid found");
                    } else if (position == 2) {
                        detailNotFoundTv.setVisibility(View.VISIBLE);
                        detailNotFoundTv.setText("No received found");
                    }
                    VolleyLog.d("INKPOT", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Log.d("error ocurred", "TimeoutError");
                    } else if (error instanceof AuthFailureError) {
                        Log.d("error ocurred", "AuthFailureError");
                        Toast.makeText(getActivity(), "AuthFailureError", Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        Log.d("error ocurred", "ServerError");
                        //   Toast.makeText(getApplication(), "ServerError", Toast.LENGTH_LONG).show();
                    } else if (error instanceof NetworkError) {
                        Log.d("error ocurred", "NetworkError");
                        Toast.makeText(getActivity(), "NetworkError", Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        Log.d("error ocurred", "ParseError");
                        Toast.makeText(getActivity(), "ParseError", Toast.LENGTH_LONG).show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    String[] tag = {"userid"};
                    String[] value = {user_id};
                    for (int i = 0; i < tag.length; i++)
                        params.put(tag[i], value[i]);
                    return params;
                }
            };
            wallet.setRetryPolicy(new DefaultRetryPolicy(
                    800000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rq.add(wallet);
        } else {
            Toast.makeText(getContext(), "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

}
