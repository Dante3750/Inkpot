package com.onlineinkpot.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.onlineinkpot.R;
import com.onlineinkpot.adapters.ChatThreadAdapter;
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.helper.NotificationUtils;
import com.onlineinkpot.models.ChatUser;
import com.onlineinkpot.models.Message;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";
    private String chatRoomId;
    private RecyclerView recyclerView;
    private ChatThreadAdapter mAdapter;
    private ArrayList<Message> messageArrayList;
    public BroadcastReceiver mRegistrationBroadcastReceiver;
    private EditText inputMessage;
    private ImageButton btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setSupportActionBar(toolbar);
        chatRoomId = getIntent().getStringExtra("uId");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inputMessage = (EditText) findViewById(R.id.message);
        btnSend = (ImageButton) findViewById(R.id.btn_send);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        messageArrayList = new ArrayList<>();
        mAdapter = new ChatThreadAdapter(this, messageArrayList, AppController.getInstance().getPref().getCustId());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Cons.PUSH_NOTIFICATION)) {
                    // new push message is received
                    handlePushNotification(intent);
                    Log.d(TAG, "Broadcast run");
                }
            }
        };
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(inputMessage.getText().toString().trim());
            }
        });

        /*fetch all chat data*/
        fetchChatDataThread();
    }

    private void sendMessage(final String msg) {
        if (Cons.isNetworkAvailable(this)) {
            if (TextUtils.isEmpty(msg)) {
                Toast.makeText(getApplicationContext(), "Enter a message", Toast.LENGTH_SHORT).show();
                return;
            }
            this.inputMessage.setText("");
            StringRequest strReq = new StringRequest(Request.Method.POST, Cons.SEND_MESSAGE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e(TAG, "response: " + response);
                            try {
                                JSONObject obj = new JSONObject(response);

                                // check for error
                                if (obj.getBoolean("error") == false) {
                                    JSONObject commentObj = obj.getJSONObject("message");
                                    String commentId = commentObj.getString("message_id");
                                    String commentText = commentObj.getString("message");
                                    String createdAt = commentObj.getString("created_at");
                                    JSONObject userObj = obj.getJSONObject("user");
                                    String userId = userObj.getString("user_id");
                                    String userName = userObj.getString("name");
                                    ChatUser user = new ChatUser(userId, null, userName, null, null, null);
                                    Message message = new Message();
                                    message.setId(commentId);
                                    message.setMessage(commentText);
                                    message.setCreatedAt(createdAt);
                                    message.setUser(user);
                                    messageArrayList.add(message);
                                    mAdapter.notifyDataSetChanged();
                                    if (mAdapter.getItemCount() > 1) {
                                        // scrolling to bottom of the recycler view
                                        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                Log.e(TAG, "json parsing error: " + e.getMessage());
                                Toast.makeText(getApplicationContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                    Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    inputMessage.setText(msg);
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("to", chatRoomId);
                    params.put("from", AppController.getInstance().getPref().getCustId());
                    params.put("type", AppController.getInstance().getPref().getType());
                    params.put("message", msg);
                    Log.e(TAG, "Params: " + params.toString());
                    return params;
                }
            };
            int socketTimeout = 0;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            strReq.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(strReq, TAG);
        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    private void handlePushNotification(Intent intent) {
        Message message = (Message) intent.getSerializableExtra("message");
        String chatRoomId = intent.getStringExtra("chat_room_id");
        Log.d(TAG, "message data : " + message.getMessage());
        if (message != null && chatRoomId != null) {
            messageArrayList.add(message);
            mAdapter.notifyDataSetChanged();
            if (mAdapter.getItemCount() > 1) {
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
            }
        }
    }

    private void fetchChatDataThread() {
        if (Cons.isNetworkAvailable(this)) {
            StringRequest strReq = new StringRequest(Request.Method.POST, Cons.GET_ALL_MESSAGE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e(TAG, "response: " + response);
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (obj.getBoolean("error") == false) {
                                    JSONArray commentsObj = obj.getJSONArray("messages");
                                    for (int i = 0; i < commentsObj.length(); i++) {
                                        JSONObject commentObj = (JSONObject) commentsObj.get(i);
                                        String commentId = commentObj.getString("message_id");
                                        String commentText = commentObj.getString("message");
                                        String createdAt = commentObj.getString("created_at");
                                        JSONObject userObj = commentObj.getJSONObject("user");
                                        String userId = userObj.getString("user_id");
                                        String userName = userObj.getString("username");
                                        ChatUser user = new ChatUser(userId, null, userName, null, null, null);
                                        Message message = new Message();
                                        message.setId(commentId);
                                        message.setMessage(commentText);
                                        message.setCreatedAt(createdAt);
                                        message.setUser(user);
                                        messageArrayList.add(message);
                                    }
                                    mAdapter.notifyDataSetChanged();
                                    if (mAdapter.getItemCount() > 1) {
                                        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, "json parsing error: " + e.getMessage());
                                Toast.makeText(getApplicationContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                    Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("to", chatRoomId);
                    params.put("from", AppController.getInstance().getPref().getCustId());
                    Log.d(TAG, "chat params : " + params.toString());
                    return params;
                }
            };
            //Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq);
        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register the receiver for new notification.
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Cons.PUSH_NOTIFICATION));
        NotificationUtils.clearNotifications();
    }

    @Override
    protected void onPause() {
        // unregister the receiver for new notification.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}