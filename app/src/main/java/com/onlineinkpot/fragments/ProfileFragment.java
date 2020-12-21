package com.onlineinkpot.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.onlineinkpot.R;
import com.onlineinkpot.activity.MainActivity;
import com.onlineinkpot.adapters.FragmentProfileAdapter;
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.helper.PicassoClient;
import com.onlineinkpot.helper.Utility;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ProfileFragment";
    protected PrefManager pref;
    public TextView tvProfileName, tvProfileType;
    public ImageView ivProfileImage, ivUpload;
    public HashMap<String, String> user, changeimage;
    public HashMap<String, String> userDetail;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String imageString;
    Bitmap bitmap;
    private android.support.v7.app.AlertDialog alertDialog;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profile");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Menu");
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        pref = new PrefManager(getContext());
        user = pref.getUserDetail();
        userDetail = pref.getUpdatedData();
        initializeLayoutContents(view);
        String forFinalInt = pref.getInt();
        if (forFinalInt == "0") {
            PicassoClient.downloadImage(getContext(), Cons.URL_IMAGE + user.get(Cons.KEY_USER_IMAGE), ivProfileImage);
        } else {
            pref = new PrefManager(getContext());
            changeimage = pref.getImageNew();
            String imageString = changeimage.get(Cons.KEY_VIEW_USER_IMAGE);
            if (imageString == null) {
                PicassoClient.downloadImage(getContext(), Cons.URL_IMAGE + user.get(Cons.KEY_USER_IMAGE), ivProfileImage);
            } else {
                StringToBitMap(imageString);
            }
        }

        // Inflate the layout for this fragment
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.menu_tab);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.vp_menu);
        tabLayout.addTab(tabLayout.newTab().setText("General Details"));
        tabLayout.addTab(tabLayout.newTab().setText("Password"));
        final FragmentProfileAdapter adapter = new FragmentProfileAdapter(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                adapter.refreshFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }
        });

        return view;
    }

    private void initializeLayoutContents(View view) {
        ivUpload = (ImageView) view.findViewById(R.id.iv_upload);
        tvProfileName = (TextView) view.findViewById(R.id.tv_profile_std_name);
        tvProfileType = (TextView) view.findViewById(R.id.tv_profile_sub_name);
        ivProfileImage = (ImageView) view.findViewById(R.id.iv_profile_pic);
        tvProfileType.setText(user.get(Cons.KEY_USER_REGTYPE));
        tvProfileName.setText("" + userDetail.get(Cons.KEY_VIEW_USER_FNAME) + " " + userDetail.get(Cons.KEY_VIEW_USER_LNAME));
        Log.d(TAG, "initializeLayoutContents: " + tvProfileName);
        Log.d(TAG, "cons: " + user.get(Cons.KEY_USER_IMAGE));
        ivUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_upload:
                selectImage();
                break;
        }
    }

    private void selectImage() {
   /*     final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Profile Pic");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getContext());
                if (items[item].equals("Take Photo")) {
                    if (result) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }
                } else if (items[item].equals("Choose from Library")) {
                    if (result) {
                        Intent intent = new Intent();
                        intent.setType("image");
                        intent.setAction(Intent.ACTION_GET_CONTENT);//
                        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }*/
      android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_updat_profile_pic, null);
        TextView cancell=(TextView)dialogView.findViewById(R.id.tv_alert_cancel);
        LinearLayout llgalary=(LinearLayout)dialogView.findViewById(R.id.ll_alert_gallery);
        LinearLayout llcamera=(LinearLayout)dialogView.findViewById(R.id.ll_alert_camera);
        llcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = Utility.checkPermission(getContext());
                if (result) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }

            }
        });
        llgalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = Utility.checkPermission(getContext());
                if (result) {
                        Intent intent = new Intent();
                        intent.setType("image");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                    }
                }

        });
        cancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA) {
                bitmap = (Bitmap) data.getExtras().get("data");
                BitMapToString(bitmap);
                sendDataToServer();
            }
        }
    }

    public String BitMapToString(Bitmap thumbnail) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        imageString = Base64.encodeToString(b, Base64.DEFAULT);
        pref.setImage(imageString);
        String changeFinal = "1";
        pref.setInt(changeFinal);
        StringToBitMap(imageString);
        return imageString;
    }

    public Bitmap StringToBitMap(String temp) {
        try {
            byte[] encodeByte = Base64.decode(temp, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            ivProfileImage.setImageBitmap(bitmap);
            ((MainActivity) getActivity()).StringToBitMapMain(temp);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            Toast.makeText(getContext(), "Hello temp " + temp, Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(), "Please Try Again", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                BitMapToString(bm);
                sendDataToServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendDataToServer() {
        if (Cons.isNetworkAvailable(getContext())) {
            StringRequest profile = new StringRequest(Request.Method.POST, Cons.URL_UPDATE_PROFILE_IMAGE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "Response : " + response.toString());
                            try {
                                JSONObject object = new JSONObject(response);
                                String msg = object.getString("message");
                                String status = object.getString("status");
                                if (status.equals("1")) {
                                    Toast.makeText(getContext(), "" + msg, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("studentId", user.get(Cons.KEY_USER_ID));
                    params.put("userImage", imageString);
                    Log.d(TAG, "getParams: " + params);
                    return params;
                }
            };
            profile.setRetryPolicy(new DefaultRetryPolicy(0, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(profile);
        } else {
            Toast.makeText(getContext(), "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    public void profileChange() {
        pref = new PrefManager(getContext());
        userDetail = pref.getUserUpdatedDetail();
        tvProfileName.setText("" + userDetail.get(Cons.KEY_VIEW_USER_FNAME) + " " + userDetail.get(Cons.KEY_VIEW_USER_LNAME));
    }
}