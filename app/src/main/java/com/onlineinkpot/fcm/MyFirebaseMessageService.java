package com.onlineinkpot.fcm;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.onlineinkpot.activity.ChatActivity;
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.helper.NotificationUtils;
import com.onlineinkpot.models.ChatUser;
import com.onlineinkpot.models.Message;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by USER on 7/21/2017.
 */

public class MyFirebaseMessageService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessageServic";
    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //Log.d(TAG, "remote Message: " + remoteMessage);
        // Check if message contains a data payload.

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        notifyUser(remoteMessage.getData().toString());
    }

    private void notifyUser(String s) {
        try {
            JSONObject object = new JSONObject(s);
            JSONObject userObj = object.getJSONObject("user");
            JSONObject messageObj = object.getJSONObject("message");
            String commentId = messageObj.getString("message_id");
            String commentText = messageObj.getString("message");
            String createdAt = messageObj.getString("created_at");
            String receiverId = messageObj.getString("Reciever_Id");
            Message message = new Message();
            message.setId(commentId);
            message.setMessage(commentText);
            message.setCreatedAt(createdAt);

            // skip the message if the message belongs to same user as
            // the user would be having the same message when he was sending
            // but it might differs in your scenario

            if (userObj.getString("from").equals(AppController.getInstance().getPref().getCustId())) {
                Log.e(TAG, "Skipping the push message as it belongs to same user");
                return;
            }
            String userId = userObj.getString("from");
            String userName = userObj.getString("name");
            ChatUser user = new ChatUser(userId, null, userName, null, null, null);
            message.setUser(user);

            // verifying whether the app is in background or foreground

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Cons.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                pushNotification.putExtra("chat_room_id", userId);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
            } else {

                // app is in background. show the message in notification try
                Intent resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
                resultIntent.putExtra("uId", userId);
                showNotificationMessage(getApplicationContext(), userName, user.getName() + " : " + message.getMessage(), message.getCreatedAt(), resultIntent);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }
}