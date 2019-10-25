package group.manager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by TAPAS on 02/04/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //String DataFrom=remoteMessage.getFrom();
        //Log.d(TAG, "From: " + DataFrom);

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Map PayData=remoteMessage.getData();
            String NotiID=PayData.get("NID").toString();// Unique Notification Id
            String ClientID=PayData.get("CID").toString();// Group Id Or Client Id
            String MsgType=PayData.get("type").toString();// MsgType ie.News/Event/Other
            String MsgMain=PayData.get("message").toString();// MainMsg has Title and Desc

            //sendNotification(MsgMain,NotiID); ///Send Notification
            Start_ServiceCallNew(getApplicationContext(),ClientID,NotiID,MsgType,MsgMain);//Start a service ServiceCallNew
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            String DataBody=remoteMessage.getNotification().getBody();
            Log.d(TAG, "Message Notification Body: " + DataBody);
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    // Start a service ServiceCallNew for Sync Table4
    private void Start_ServiceCallNew(Context context,String ClientId,String NotiID,String NotiType,String NotiMsgMain)
    {
        Intent intent = new Intent(context,Service_Call_New.class);
        intent.putExtra("ClientID",ClientId);
        intent.putExtra("NotiID",NotiID);
        intent.putExtra("NotiType",NotiType);
        intent.putExtra("NotiMsgMain",NotiMsgMain);
        startService(intent);
    }
}
