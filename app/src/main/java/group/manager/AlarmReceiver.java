package group.manager;

import java.io.IOException;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class AlarmReceiver extends WakefulBroadcastReceiver {

	int NType=0;
	Context context;
	String NotiSound="0"; //0 for Default 1 for None else Others
	private static AlertDialog EventRem_AlertDialog;
	private MediaPlayer mMediaPlayer; 
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		  this.context=context;
		  String ClientID=intent.getStringExtra("ClientID");
		  String LogclubName=intent.getStringExtra("LogclubName");
		  int MID=intent.getIntExtra("MID", 0);
		  String EventData=intent.getStringExtra("EventData");//Only for Event Reminder

		  if(ClientID!=null && EventData==null)
		  {
			 String Tab2Name="C_"+ClientID+"_2";
			 
			 String Birday_Noti_Time=Get_BirthAnniNotiTime(context,MID);
			  
			 if(Birday_Noti_Time.trim().equals("@")){
				  NType=2;  //Only Anniversary
			 }else if((Birday_Noti_Time.trim().length()>1)&&(Birday_Noti_Time.trim().contains("@"))){
				  NType=3; //both Bday and Anniversary
			 }else if((Birday_Noti_Time.trim().length()>1)&&(!Birday_Noti_Time.trim().contains("@"))){
				  NType=1;  //Only BDay
			 }
		  
	        // Current Day and Month
	        String ddays="00",mmonths="00";
	        Calendar cal = Calendar.getInstance();// create object for calender
		    String days = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
	        if((days==null)||(days.length()==0)){
	        	days="00";
	        }
	        else if(days.length()==1){
	        	ddays="0"+days;
	        }
	        else{
	        	ddays=days;
	        }
	        
	        String months=String.valueOf(cal.get(Calendar.MONTH)+1);
	        if((months==null)||(months.length()==0)){
	        	months="00";
	        }
	        else if(months.length()==1){
	        	mmonths="0"+months;
	        }
	        else{
	        	mmonths=months;
	        }
	        //////////////////////////////////////////
	        
	        boolean flag=false;
	        
	        if(NType==1)
	        {
	        	// Check Birthday
	        	flag=ChkBDay(context,Tab2Name,days,ddays,months,mmonths);
		        if(flag){
		        	SendNoti(context,"BDAY",ClientID,LogclubName,MID);
		        }
	        }
	        else if(NType==2)
	        {
	        	// Check Anniversary
	        	flag=ChkAnni(context,Tab2Name,days,ddays,months,mmonths);
	        	if(flag){
	        		SendNoti(context,"ANNI",ClientID,LogclubName,MID);
	        	}
	        }
	        else if(NType==3)
	        {
	    	  // Check Birthday
	    	   flag=ChkBDay(context,Tab2Name,days,ddays,months,mmonths);
		       if(flag){
		    	  SendNoti(context,"BDAY",ClientID,LogclubName,MID);
		       }
		       
		       // Check Anniversary
		       flag=ChkAnni(context,Tab2Name,days,ddays,months,mmonths);
	           if(flag){
	        	   SendNoti(context,"ANNI",ClientID,LogclubName,MID);
	           }
		   }
		}
		else if(EventData!=null)
		{
			DisplayEventReminder(context,EventData,LogclubName);//Display Event Reminder
		}
			  
	}
	
	
	//Check Birthday of Member and Spouse both
	private boolean ChkBDay(Context context,String Tab2,String days,String ddays,String months,String mmonths)
	{
		boolean HasData=false;
		//  Check BDay of Member
        String Qry="Select M_Name from "+Tab2+" Where (M_DOB_D='"+days+"' or M_DOB_D='"+ddays+"') and (M_DOB_M='"+months+"' or M_DOB_M='"+mmonths+"')";
        HasData=chkData(context,Qry);
         
		if(!HasData)
		{
		    //  Check BDay of Spouse
			Qry="Select S_Name from "+Tab2+" Where (S_DOB_D='"+days+"' or S_DOB_D='"+ddays+"') and (S_DOB_M='"+months+"' or S_DOB_M='"+mmonths+"')";
			HasData=chkData(context,Qry);
	    }
	    return HasData;
	}
	
	
	//Check Anniversary of Member
	private boolean ChkAnni(Context context,String Tab2,String days,String ddays,String months,String mmonths)
	{
		boolean HasData=false;
		String Qry="Select M_Name from "+Tab2+" Where (M_MrgAnn_D ='"+days+"' or M_MrgAnn_D='"+ddays+"') and (M_MrgAnn_M='"+months+"' or M_MrgAnn_M='"+mmonths+"')";
		HasData=chkData(context,Qry);
		return HasData;
	}
	
	
	//Check there is any data  or not
	private boolean chkData(Context context,String Qry)
	{
		boolean Flag=false;
        SQLiteDatabase db = context.openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		 
		//  Check There is any record or not
		Cursor cursorT = db.rawQuery(Qry, null);
		int countdata=cursorT.getCount();
		cursorT.close();
		db.close();//Close Database
		if(countdata>0){
			Flag=true;
		}
		return Flag;
	}
	
	
	
	//Send a Notification with Type=BDAY/ANNI
	private void SendNoti(Context context,String Type,String GrpName,String LogclubName,int MID) 
	{	
		Get_SharedPref_Values();//Get Shared Preference Value NotiSound
		
		//String ns = Context.NOTIFICATION_SERVICE;
		String Msg="";
		Intent intent1=null;
		if(Type.equals("BDAY")){
			MID=MID+1122;//Unique Notification ID for BDay
			Msg="Today's Birthday";
			intent1= new Intent(context,ShowBirthadayNotification.class);  
		}
		else{
			MID=MID+2211;//Unique Notification ID for Anniversary
			Msg="Today's Anniversary";
			intent1= new Intent(context,ShowAnniversary.class);
		}
		intent1.putExtra("ClientID", GrpName);
		intent1.putExtra("LogclubName", LogclubName);
		intent1.putExtra("Menu_Noti", "Notific");
		
		PendingIntent Pend_Intent = PendingIntent.getActivity(context,MID,intent1, 0);
		
        Uri uri=null;
		    
		// Set notification sound
		if(NotiSound.equals("0"))
	    {
		    String Default_GrpMan_NotiSound="android.resource://" + context.getPackageName() + "/" + R.raw.notify;
		    uri = Uri.parse(Default_GrpMan_NotiSound);//Default GrpManager App sound
	    }
	    else if(NotiSound.length()>5){
	    	uri = Uri.parse(NotiSound);//Set Selected Notification Sound
	    }
	    else{
	    	uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	    }
		
		///Set Vibration
		long[] vibrate = new long[] { 1000, 1000, 1000, 1000, 1000 };
		
		//Get the bitmap to show in notification bar
		Bitmap LargeIcon_bitmap_image=null;
		DbHandler dbObj = new DbHandler(context,"");
		byte[] AppLogo=dbObj.Get_AppLogo("C_"+GrpName+"_4");
		dbObj.close();
		if(AppLogo==null){
			LargeIcon_bitmap_image= BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);;
		}else{
			LargeIcon_bitmap_image = BitmapFactory.decodeByteArray(AppLogo,0, AppLogo.length);	
		}
		/////////////////////////////////////////
		
		String Desc=Msg+" list for "+GrpName;
		
	    NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle().bigText(Desc);

		NotificationManager mNotificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	    
	    ////Added on Oreo Notification Problem (14-12-2018)
	    String channel_id = createNotificationChannel(context,uri,mNotificationManager);
	    ///////////////////////////////////////////////////////

	    NotificationCompat.Builder nb = new NotificationCompat.Builder(context,channel_id);

	           nb.setContentTitle(Msg)
	            .setContentText(Desc)
	            .setSmallIcon(R.drawable.ic_launcher)
	            .setLargeIcon(LargeIcon_bitmap_image)
	            .setContentIntent(Pend_Intent)
	            .setTicker(Msg)
	            .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setVibrate(vibrate)
	            .setSound(uri)
	            .setStyle(style)
	            .setPriority(Notification.FLAG_HIGH_PRIORITY);

	    mNotificationManager.notify(MID, nb.build());
    }


	////Added on Oreo Notification Problem Channel Id is Required (14-12-2018)
	public static String createNotificationChannel(Context context,Uri uriSound,NotificationManager notificationManager) {

        // NotificationChannels are required for Notifications on O (API 26) and above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        	String channelId = "MdaGroupManager-"+Math.random();// The id of the channel.
    	    String channelName = "Group Manager";// The user-visible name of the channel.
    	    String channelDescription = "Group Manager Alert";//
    	    int channelImportance = NotificationManager.IMPORTANCE_HIGH;

            // Initializes NotificationChannel.
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);

			AudioAttributes attributes = new AudioAttributes.Builder()
			        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
					.setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
					.setUsage(AudioAttributes.USAGE_NOTIFICATION)
					.build();

            notificationChannel.setDescription(channelDescription);
			notificationChannel.enableLights(true);
			notificationChannel.enableVibration(true);
			notificationChannel.setSound(uriSound, attributes); // This is IMPORTANT
//            notificationChannel.setLockscreenVisibility(channelLockscreenVisibility);

            // Adds NotificationChannel to system. Attempting to create an existing notification
            // channel with its original values performs no operation, so it's safe to perform the
            // below sequence.
            //NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
           // assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);

            return channelId;
        } else {
            // Returns null for pre-O (26) devices.
            return null;
        }
    }
	

	// Get setting_Birday_noti_time from LoginMain
	 private String Get_BirthAnniNotiTime(Context context,int MID)
	 {
		 String AlarmNotiTime="";
		 SQLiteDatabase db = context.openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		 String SqlQry="Select setting_Birday_noti_time from LoginMain Where M_ID="+MID;
		 Cursor cursorT = db.rawQuery(SqlQry, null);
	     int RCount=cursorT.getCount();
	     if(RCount>0)
	     {
			while(cursorT.moveToNext())
			{
				AlarmNotiTime=cursorT.getString(0);
			    break;
			}
	     }
	     cursorT.close();
		 db.close();
		 return AlarmNotiTime;
	 }
	 
	 
	 
	 
	///Display Event Reminder 
 public void DisplayEventReminder(Context context,String EventData,String GrpName){
		// TODO Auto-generated method stub
	 
	   if(EventRem_AlertDialog==null)
	   {
		 //try{  
		   
		AlertDialog.Builder alertadd = new AlertDialog.Builder(context);
		final FrameLayout frameView = new FrameLayout(context);
		alertadd.setView(frameView);
		EventRem_AlertDialog = alertadd.create();
		LayoutInflater inflater = EventRem_AlertDialog.getLayoutInflater();
		View dialoglayout = inflater.inflate(R.layout.event_reminder_alertdialog, frameView);
			TextView txtEventName= (TextView) dialoglayout.findViewById(R.id.txtEventName);
			TextView txtGroupName= (TextView) dialoglayout.findViewById(R.id.txtGroupName);
			TextView txtDT= (TextView) dialoglayout.findViewById(R.id.txtDT);
			TextView txtVenue= (TextView) dialoglayout.findViewById(R.id.txtVenue);
			ImageView imgDismiss= (ImageView) dialoglayout.findViewById(R.id.imgDismiss);
			
			String[] Arr1=EventData.split("#");
			txtEventName.setText(Arr1[0]);
			txtGroupName.setText(GrpName);
			txtDT.setText(Arr1[1]);
			txtVenue.setText(Arr1[2]);
			
			imgDismiss.setOnClickListener(new OnClickListener(){ 
	            @Override 
	            public void onClick(View arg0){
	            	mMediaPlayer.stop();///Stop Sound
	            	EventRem_AlertDialog.dismiss();
	            	EventRem_AlertDialog=null;
	            }
			});
			
			
			alertadd.setView(dialoglayout);
			EventRem_AlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			/*WindowManager.LayoutParams params = new WindowManager.LayoutParams(
			        LayoutParams.MATCH_PARENT,
			        LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.TYPE_SYSTEM_ALERT |
			        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
			        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
			        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
			        PixelFormat.TRANSPARENT);*/
			
			
			WindowManager.LayoutParams params=null;
			
			if(android.os.Build.VERSION.SDK_INT >= 26)///Andriod version 26(Oreo)
			{
				params = new WindowManager.LayoutParams(
				        0,0,2038,
				        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
				        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				        PixelFormat.TRANSPARENT);
			}
			else
			{
				params = new WindowManager.LayoutParams(
				        0,0,WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
				        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
				        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				        PixelFormat.TRANSPARENT);
			}
			
		    //params.format = PixelFormat.TRANSLUCENT;
			
			//if(CallScreenPosition.equals("Top"))
			//{
				params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
			//}
			//else
			//{
				params.gravity = Gravity.CENTER;
			//}
	        ////////Set Flags to make touchable calling screen//////////////
		    //params.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
		    //params.x = 50;   //x position
		    //params.y = 50; //y position
			
			EventRem_AlertDialog.getWindow().setAttributes(params);
		    //WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
			//wmlp.gravity = Gravity.TOP | Gravity.RIGHT;
		    //wmlp.x = 50;   //x position
			//wmlp.y = 50;  
			////////Set Flags to make touchable calling screen//////////////
			//wmlp.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
			
			
			if(android.os.Build.VERSION.SDK_INT >= 26)///Andriod version 26(Oreo)
			{
				EventRem_AlertDialog.getWindow().setType(2038);
			}
			else if(android.os.Build.VERSION.SDK_INT == 25 )///Andriod version 25(Oreo)
			{
				EventRem_AlertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			}
			else{
				EventRem_AlertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
			}
			
			/*mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			    //@Override
				public void onDismiss(DialogInterface dialog) {
			    	dialog.cancel();
			    }
			});*/
			
			/*mDialog.setButton("Close", new DialogInterface.OnClickListener() { // define the 'Close' button
			    public void onClick(DialogInterface dialog, int which) {
			        //Either of the following two lines should work.
			        dialog.cancel();
			        //dialog.dismiss();
			    } 
			});*/
			
			if(!EventRem_AlertDialog.isShowing()){
			 EventRem_AlertDialog.show();
			 playSound(context, getAlarmUri());///PLay Sound
			}
		 //}
		 /*catch(Exception ex)
		 {
			 String tt="";
		 }*/
	   }
	}
		 
	 
	 
	 
	 ////Play Alarm Receiver Sound
	 private void playSound(Context context, Uri alert) {
	        mMediaPlayer = new MediaPlayer();
	        try {
	            mMediaPlayer.setDataSource(context, alert);
	            final AudioManager audioManager = (AudioManager) context
	                    .getSystemService(Context.AUDIO_SERVICE);
	            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
	                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
	                mMediaPlayer.prepare();
	                mMediaPlayer.start();
	            }
	        } catch (IOException e) {
	            System.out.println("OOPS");
	        }
	    }

	    //Get an alarm sound. Try for an alarm. If none set, try notification, 
	    //Otherwise, ringtone.
	    private Uri getAlarmUri() {
	        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
	        if (alert == null) {
	            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	            if (alert == null) {
	              alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
	            }
	        }
	        return alert;
	    }
	 
	 
	 
	 private void Get_SharedPref_Values()
	 {
		 SharedPreferences ShaPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		 if (ShaPref.contains("NotiSound"))
		 {
		    NotiSound=ShaPref.getString("NotiSound", "");
	     }
	 }
}
