package com.onlineinkpot.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.onlineinkpot.R;
import com.onlineinkpot.adapters.FragmentOfferAdapter;
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.models.Offer;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfferFragment extends Fragment {
    private static final String TAG = "OfferFragment";
    public List<Offer> offerList;
    public PrefManager pref;
    private RecyclerView rv;
    private FragmentOfferAdapter mAdapter;
    private ProgressDialog progressDialog;

    public OfferFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Offer");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_offer, null, false);
        initializeLayoutContents(view);
        pref = new PrefManager(getContext());
        return view;
    }

    private void initializeLayoutContents(View view) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(false);
        offerList = new ArrayList<>();
        rv = (RecyclerView) view.findViewById(R.id.rv_offer);
        mAdapter = new FragmentOfferAdapter(getContext(), offerList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(mAdapter);
        prepareOfferData();
    }

    private void prepareOfferData() {
        if (Cons.isNetworkAvailable(getContext())) {
            final StringRequest offerr = new StringRequest(Request.Method.GET, Cons.URL_OFFER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            Offer offer = new Offer();
                            JSONObject object = array.getJSONObject(i);
                            offer.setCoupon_code(object.getString("coupon_code"));
                            offer.setCoupon_front_img(object.getString("coupon_fimg"));
                            offer.setCoupon_des(object.getString("coupon_sdes"));
                            offerList.add(offer);
                            Log.d(TAG, "offerList: " + offerList.toString());
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

            };
            AppController.getInstance().addToRequestQueue(offerr, TAG);
        } else {
            Toast.makeText(getContext(), "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }
}
