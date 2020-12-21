package com.onlineinkpot.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class PromoCodeFragment extends Fragment {
    private static final String TAG = "PromoCodeFragment";
    private ProgressDialog progressDialog;
    public PrefManager pref;
    private HashMap<String, String> userdetail;
    private TextView promocodeLabelTv;
    private TextView promocodeTv;
    private TextView despLabelTv;
    private TextView desTv;
    private Button promoShareBtn;
    public String message;

    public PromoCodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Refer and Earn");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promo_code, null, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(true);
        pref = new PrefManager(getContext());
        userdetail = pref.getUserDetail();
        String user_id = userdetail.get(Cons.KEY_USER_ID);
        String userRegType = userdetail.get(Cons.KEY_USER_REGTYPE);
        getPromocodeApi(Cons.check_promocode_from_server_url, user_id, userRegType);
        initializeLayoutContents(view);
        setListenerView();
        // Inflate the layout for this fragment
        return view;
    }

    private void initializeLayoutContents(View view) {
        promocodeLabelTv = (TextView) view.findViewById(R.id.promocode_first_lable_tv);
        promocodeTv = (TextView) view.findViewById(R.id.promocode_first_tv);
        despLabelTv = (TextView) view.findViewById(R.id.promocode_discription_lable_tv);
        desTv = (TextView) view.findViewById(R.id.promocode_discription_tv);
        promoShareBtn = (Button) view.findViewById(R.id.promocode_share_btn);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Chalkduster.ttf");
        promocodeLabelTv.setTypeface(face);
        promocodeTv.setTypeface(face);
        despLabelTv.setTypeface(face);
        desTv.setTypeface(face);
        promoShareBtn.setTypeface(face);
    }

    private void setListenerView() {
        promoShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String promocode = promocodeTv.getText().toString();
                String app_link = "https://play.google.com/store/apps/details?id=com.onlineinkpot&hl=en";
                String message = " I am giving you ₹100. Sign up and use my code : " + promocode + " to avail the discount on your next course buy at InkPot Online. Download: " + app_link;
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });
    }

    private void getPromocodeApi(String url, final String user_id, final String user_type) {
        if (Cons.isNetworkAvailable(getContext())) {
            final RequestQueue rq = Volley.newRequestQueue(getActivity());
            final StringRequest postReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("promocode res ", "= " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.has("cou")) {
                            String cou = jsonObject.getString("cou");
                            if (cou.equals("0")) {
                                generatePromocodeApi(Cons.generate_promocode_from_server_url, user_id, user_type);
                            } else {
                                if (jsonObject.has("couponcode")) {
                                    String couponcode = jsonObject.getString("couponcode");
                                    String t_description = jsonObject.getString("t_description");
                                    promocodeTv.setText(couponcode);
                                    message = "Friend Register, Friend earn \\u20B9 100; Friend Buys, you earn \\u20B9 100.";
                                    desTv.setText(message);
                                    Log.d(TAG, "onResponse: " + message);
                                }
                            }
                        }

                        if (jsonObject.has("couponcode")) {
                            String couponcode = jsonObject.getString("couponcode");
                            String t_description = jsonObject.getString("t_description");
                            promocodeTv.setText(couponcode);
                            desTv.setText(message);
                            message = "Friend Register, Friend earn \u20B9 100; Friend Buys, you earn \u20B9 100.";
                            desTv.setText(message);
                            Log.d(TAG, "onResponse: " + message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    VolleyLog.d("INKPOT", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Log.d("error ocurred", "TimeoutError");
                        //  Toast.makeText(getApplication(), "TimeoutError", Toast.LENGTH_LONG).show();
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
                    String[] tag = {"p_userid", "p_promoservicetype", "p_description"};
                    String[] value = {user_id, user_type, ""};
                    for (int i = 0; i < tag.length; i++)
                        params.put(tag[i], value[i]);
                    return params;
                }
            };
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    800000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rq.add(postReq);

        } else {
            Toast.makeText(getContext(), "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    protected void generatePromocodeApi(String url, final String user_id, final String user_type) {
        if (Cons.isNetworkAvailable(getContext())) {
            final RequestQueue rq = Volley.newRequestQueue(getActivity());
            final StringRequest postReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("promocode res ", "= " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.has("couponcode")) {
                            String couponcode = jsonObject.getString("couponcode");
                            String t_description = jsonObject.getString("t_description");
                            promocodeTv.setText(couponcode);
                            desTv.setText(t_description);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    VolleyLog.d("INKPOT", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Log.d("error ocurred", "TimeoutError");
                        //  Toast.makeText(getApplication(), "TimeoutError", Toast.LENGTH_LONG).show();
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
                    String[] tag = {"p_userid", "p_promoservicetype", "p_description"};
                    String[] value = {user_id, user_type, ""};
                    for (int i = 0; i < tag.length; i++)
                        params.put(tag[i], value[i]);
                    return params;
                }
            };
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    800000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rq.add(postReq);
        } else {
            Toast.makeText(getContext(), "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }
}
