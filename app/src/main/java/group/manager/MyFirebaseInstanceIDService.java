package group.manager;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by TAPAS on 02/04/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String FCMTokenId = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed FCMTokenId: " + FCMTokenId);

        //Get Global Controller Class object (see application tag in AndroidManifest.xml)
        Controller aController = (Controller) getApplicationContext();

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        aController.Reg_UnReg_GCM(getApplicationContext(), FCMTokenId, "Y");// Register FCMTokenId on our server
    }
    // [END refresh_token]


}
