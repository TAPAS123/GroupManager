package group.manager;

import group.manager.R;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

//import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService  {

	/*private static final String TAG = "GroupManagerGCMService";
	
	private Controller aController = null;

    public GCMIntentService() {
    	// Call extended class Constructor GCMBaseIntentService
        super(Config.GOOGLE_SENDER_ID);
    }

    *//*** Method called on device registered**//*
    @Override
    protected void onRegistered(Context context, String registrationId) {
    	
	     //Get Global Controller Class object (see application tag in AndroidManifest.xml)
	     if(aController == null)
           aController = (Controller) getApplicationContext();
	
         Log.i(TAG, "Device registered: regId = " + registrationId);
         //aController.displayMessageOnScreen(context, "Your device registred with GCM");
         //Log.d("NAME", MainActivity.name);
         //aController.register(context, registrationId);
         aController.Reg_UnReg_GCM(context, registrationId,"Y");// Register on our server
    }
    
    *//*** Method called on device unregistred ****//*
    @Override
    protected void onUnregistered(Context context, String registrationId) {
    	 if(aController == null)
            aController = (Controller) getApplicationContext();
         Log.i(TAG, "Device unregistered");
         //aController.displayMessageOnScreen(context, getString(R.string.gcm_unregistered));
         //aController.unregister(context, registrationId);
         aController.Reg_UnReg_GCM(context, registrationId,"N");// UnRegister on our server
    }

    *//*** Method called on Receiving a new message from GCM server* *//*
    @Override
    protected void onMessage(Context context, Intent intent) {
    	
    	if(aController == null)
            aController = (Controller) getApplicationContext();
    	
        Log.i(TAG, "Received message");
        
        String ClientID = intent.getStringExtra("CID");// Group Id Or Client Id
        String NotiID = intent.getStringExtra("NID");// Unique Notification Id
        String MsgType = intent.getStringExtra("type");// MsgType ie.News/Event/Other
        String MsgMain = intent.getStringExtra("message");// MainMsg has Title and Desc
        
        //aController.displayMessageOnScreen(context, message);
        
        // notifies user
        //generateNotification(context, message);
        //Show_Noti(context,ClientID,NotiID,MsgType,MsgMain);
        Start_ServiceCallNew(context,ClientID,NotiID,MsgType,MsgMain);//Start a service ServiceCallNew
    }

    *//*** Method called on receiving a deleted message* *//*
    @Override
    protected void onDeletedMessages(Context context, int total) {
    	
	    if(aController == null)
          aController = (Controller) getApplicationContext();
	
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        //aController.displayMessageOnScreen(context, message);
        // notifies user
        //generateNotification(context, message);
    }

    *//*** Method called on Error* *//*
    @Override
    public void onError(Context context, String errorId) {
    
	    if(aController == null)
          aController = (Controller) getApplicationContext();
	
        Log.i(TAG, "Received error: " + errorId);
        //aController.displayMessageOnScreen(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
	   if(aController == null)
          aController = (Controller) getApplicationContext();
	
       // log message
       Log.i(TAG, "Received recoverable error: " + errorId);
       //aController.displayMessageOnScreen(context, getString(R.string.gcm_recoverable_error,errorId));
    
       return super.onRecoverableError(context, errorId);
    }


    
	// Start a service ServiceCallNew for Sync Table4
	private void Start_ServiceCallNew(Context context,String ClientId,String NotiID,String NotiType,String NotiMsgMain)
	{
		Intent intent = new Intent(context,Service_Call_New.class);
		intent.putExtra("ClientID",ClientId);
		intent.putExtra("NotiID",NotiID);
		intent.putExtra("NotiType",NotiType);
		intent.putExtra("NotiMsgMain",NotiMsgMain);
	    startService(intent);
	}*/
	
}
