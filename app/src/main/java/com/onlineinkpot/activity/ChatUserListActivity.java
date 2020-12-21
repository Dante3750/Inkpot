package com.onlineinkpot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;


import com.onlineinkpot.R;
import com.onlineinkpot.adapters.ChatRoomAdapter;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.helper.SimpleDividerItemDecoration;
import com.onlineinkpot.models.ChatRoom;
import com.onlineinkpot.models.ChatUser;

import java.util.ArrayList;

public class ChatUserListActivity extends AppCompatActivity {

    private static final String TAG = "ChatUserListActivity";
    private RecyclerView rvUsers;
    private ArrayList<ChatRoom> roomsList;
    private ChatRoomAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_user_list_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rvUsers = (RecyclerView) findViewById(R.id.rv_chat_users);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        roomsList = new ArrayList<>();
        mAdapter = new ChatRoomAdapter(roomsList);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        rvUsers.setItemAnimator(new DefaultItemAnimator());
        rvUsers.setAdapter(mAdapter);
        rvUsers.addOnItemTouchListener(new Cons.RecyclerTouchListener(getApplicationContext(), rvUsers, new Cons.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent(getApplicationContext(), ChatActivity.class);
                i.putExtra("uId", MainActivity.chatList.get(position).getId());
                i.putExtra("uName", MainActivity.chatList.get(position).getName());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        fetchChatRooms();
    }

    private void fetchChatRooms() {
        for (int i = 0; i < MainActivity.chatList.size(); i++) {
            ChatUser chat = MainActivity.chatList.get(i);
            ChatRoom cr = new ChatRoom();
            cr.setId(chat.getId());
            cr.setName(chat.getName());
            cr.setLastMessage("");
            cr.setUnreadCount(0);
            cr.setTimestamp("");

            roomsList.add(cr);
        }
        mAdapter.notifyDataSetChanged();
    }

}
