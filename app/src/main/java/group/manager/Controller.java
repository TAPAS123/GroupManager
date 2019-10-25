package group.manager;

/*import group.manager.R;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;*/

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
//import com.google.android.gcm.GCMRegistrar;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

public class Controller extends Application{
	
	
	//private  final int MAX_ATTEMPTS = 5;
    //private  final int BACKOFF_MILLI_SECONDS = 2000;
    //private  final Random random = new Random();
	
	public static final String TAG = Controller.class.getSimpleName();
	
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	private static Controller mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		//Firebase.setAndroidContext(this);
		//FirebaseApp.initializeApp(this);
		//FirebaseDatabase database = FirebaseDatabase.getInstance();
	}

	public static synchronized Controller getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	/*public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,new LruBitmapCache());
		}
		return this.mImageLoader;
	}*/


	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}
	
    
	 // Register this account with the server.
     /*void register(final Context context, final String regId) {
    	 
        Log.i(Config.TAG, "registering device (regId = " + regId + ")");
        
        String serverUrl = Config.YOUR_SERVER_URL;
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        
        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        
        // Once GCM returns a registration id, we need to register on our server
        // As the server might be down, we will retry it a couple
        // times.
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
        	
            Log.d(Config.TAG, "Attempt #" + i + " to register");
            
            try {
            	//Send Broadcast to Show message on screen
            	displayMessageOnScreen(context, context.getString(
                        R.string.server_registering, i, MAX_ATTEMPTS));
                
                // Post registration values to web server
                post(serverUrl, params);
                
                GCMRegistrar.setRegisteredOnServer(context, true);
                
                //Send Broadcast to Show message on screen
                String message = context.getString(R.string.server_registered);
                displayMessageOnScreen(context, message);
                
                return;
            } catch (IOException e) {
            	
                // Here we are simplifying and retrying on any error; in a real
                // application, it should retry only on unrecoverable errors
                // (like HTTP error code 503).
            	
                Log.e(Config.TAG, "Failed to register on attempt " + i + ":" + e);
                
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                	
                    Log.d(Config.TAG, "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                    
                } catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                    Log.d(Config.TAG, "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return;
                }
                
                // increase backoff exponentially
                backoff *= 2;
            }
        }
        
        String message = context.getString(R.string.server_register_error,
                MAX_ATTEMPTS);
        
        //Send Broadcast to Show message on screen
        displayMessageOnScreen(context, message);
    }

     // Unregister this account/device pair within the server.
     void unregister(final Context context, final String regId) {
    	 
        Log.i(Config.TAG, "unregistering device (regId = " + regId + ")");
        
        String serverUrl = Config.YOUR_SERVER_URL + "/unregister";
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        
        try {
            post(serverUrl, params);
            GCMRegistrar.setRegisteredOnServer(context, false);
            String message = context.getString(R.string.server_unregistered);
            displayMessageOnScreen(context, message);
        } catch (IOException e) {
        	
            // At this point the device is unregistered from GCM, but still
            // registered in the our server.
            // We could try to unregister again, but it is not necessary:
            // if the server tries to send a message to the device, it will get
            // a "NotRegistered" error message and should unregister the device.
        	
            String message = context.getString(R.string.server_unregister_error,
                    e.getMessage());
            displayMessageOnScreen(context, message);
        }
    }

    // Issue a POST request to the server.
    private static void post(String endpoint, Map<String, String> params)
            throws IOException {   	
        
        URL url;
        try {
        	
            url = new URL(endpoint);
            
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        
        String body = bodyBuilder.toString();
        
        Log.v(Config.TAG, "Posting '" + body + "' to " + url);
        
        byte[] bytes = body.getBytes();
        
        HttpURLConnection conn = null;
        try {
        	
        	Log.e("URL", "> " + url);
        	
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            
            // handle the response
            int status = conn.getResponseCode();
            
            // If response is not success
            if (status != 200) {
            	
              throw new IOException("Post failed with error code " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
      }*/
    
    
    
    // Register or UnRegistered RegId  with the server.
    void Reg_UnReg_GCM(final Context context, final String regId,final String Registered) {
   	 
    	Thread networkThread = new Thread() {
   		  @Override
   			 public void run() {
   			  try {
   			        String ClientId="";
   			        
   			        // Get ClientId from Shared Pref //////////////////
   			        SharedPreferences shPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
   				 
   			        if (shPref.contains("clientid")){
   			        	ClientId=shPref.getString("clientid", "");
   		            }
   			        ///////////////////////
   			        
   			        /// Get Iemi /////////////
				    String IEMI = new CommonClass().getIMEINumber(context);//Added On 14-02-2019
   				    
   				    //Call a Webservice
   				    WebServiceCall WebObj=new WebServiceCall();
   				    String WebResp=WebObj.GCMReg_Server(ClientId,IEMI,regId,Registered);
   				    
   				    if(WebResp.equalsIgnoreCase("Saved"))
   				    {
   				      if(Registered.equals("Y"))
   			          {	
   			             // Registered RegId On Server
   			             SaveGCMRegID(regId); //Save GCMRegId in shared preference
   			             //GCMRegistrar.setRegisteredOnServer(context, true);//To Registered On GCM Server
   			          }
   			          else
   			          {
   			             // UNRegistered RegId On Server
   			             SaveGCMRegID(""); //Save GCMRegId in shared preference
   			             //GCMRegistrar.setRegisteredOnServer(context, false);//To UnRegistered On GCM Server
   			          }
   				    }
   			        
   				} catch (Exception e) {
   		  			  e.printStackTrace();
   				}
   	  		  }
   	  	   };
   	  	networkThread.start();
   }
    
    
    // Save Shared Preference Value for GCM registration id for PUSH
    private void SaveGCMRegID(String GCMRegID)
    {
      SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
      Editor editor = sharedpreferences.edit();
 	  editor.putString("SharePre_GCMRegId", GCMRegID.trim());
 	  editor.commit();
 	  ///////////////////////////////
    }
    
    
    
	// Checking for all possible internet providers
    public boolean isConnectingToInternet(){
    	
        ConnectivityManager connectivity = 
        	                 (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null)
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null)
                  for (int i = 0; i < info.length; i++)
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      }
          }
          return false;
    }
	
   // Notifies UI to display a message.
   void displayMessageOnScreen(Context context, String message) {
    	 
        Intent intent = new Intent(Config.DISPLAY_MESSAGE_ACTION);
        intent.putExtra(Config.EXTRA_MESSAGE, message);
        
        // Send Broadcast to Broadcast receiver with message
        context.sendBroadcast(intent);
    }
    
    
   //Function to display simple Alert Dialog
   public void showAlertDialog(Context context, String title, String message,Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Set Dialog Title
		alertDialog.setTitle(title);

		// Set Dialog Message
		alertDialog.setMessage(message);

		if(status != null)
			// Set alert dialog icon
			//alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

		// Set OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});

		// Show Alert Message
		alertDialog.show();
	}
    
    private PowerManager.WakeLock wakeLock;
    
    public  void acquireWakeLock(Context context) {
        if (wakeLock != null) wakeLock.release();

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "WakeLock");
        
        wakeLock.acquire();
    }

    public  void releaseWakeLock() {
        if (wakeLock != null) wakeLock.release(); wakeLock = null;
    }
   
}
