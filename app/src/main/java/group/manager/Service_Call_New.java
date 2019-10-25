package group.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class Service_Call_New extends Service{
	 String Sdatabase;
	 Context context=this;
	 SQLiteDatabase db;
     String sqlSearch,StrTotal;
     Cursor cursorT;
	 String CurrentDT_Diff,SyncDT;
	 ResultSet rs;
	 Thread networkThread;
	 Connection conn;
	 int Tab4Count,Tab4Max_Mid,Tab4Min_Sync,Tab4Min_SyncDT;
	 int TabOp1Count,TabOp1Max_Mid,TabOp1Min_Sync,TabOp1Min_SyncDT;//Added on 26-04-2017
	 int TabOp2Count,TabOp2Max_Mid,TabOp2Min_Sync,TabOp2Min_SyncDT;//Added on 26-04-2017
	 String ClientId="",NotiID="",NotiType="",NotiMsgMain="",LogName="",LogId="",ClubName="",UserType="";
	 String Tab4Name,TabOp1Name,TabOp2Name;
	 String NotiSound="0"; //0 for Default 1 for None else Others

	  @Override
	  public void onCreate()
	  {
	    super.onCreate();
	  }
	 
	  
	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId)
	  {
		 try{
		    ClientId= intent.getStringExtra("ClientID");//Get Client Id or GroupId
		    NotiID=intent.getStringExtra("NotiID");
		    if(!NotiID.equals("0"))
		    {
		      NotiType=intent.getStringExtra("NotiType");
		      NotiMsgMain=intent.getStringExtra("NotiMsgMain");
		      GetLoginDataWithClientID();///Get LoginData with Given ClientID when Noti Comes
		    }
		 }
		 catch(Exception ex) {
		 }
		 
		 if(ClientId.length()>2)
		 {
		   if(!NotiID.equals("0")){
			   Noti_Delivered();//Sent Information to Server that Noti has been delivered
		   }
		   Sync_Start(); // Sync for Table4/Op1/Op2 with Condition of NotiType
		 }
	     
		 return (START_STICKY);
	  }
	 
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		 System.out.println("Bing");
		return null;
	}

	
	////Login Data With ClientID 
	private void GetLoginDataWithClientID()
	{
       ///////////////////////////////////////
       try
       {
         SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
         String SqlQry="Select LogName,LogId,ClubName,setting_extra_1 from LoginMain Where Upper(ClientID)='"+ClientId.toUpperCase()+"' AND DDateTime is not null AND length(DDateTime)<>0";
         Cursor cursorT = db.rawQuery(SqlQry, null);
         int RCount=cursorT.getCount();
         if(RCount>0)
         {
           if(cursorT.moveToFirst())
           {
             LogName=cursorT.getString(0);
             LogId=cursorT.getString(1);
             ClubName=cursorT.getString(2);
             UserType=cursorT.getString(3);
           }
         }
         cursorT.close();//Close Cursor
         db.close();//Close database
       }
       catch (Exception e) {
         e.printStackTrace();
       } 
       ////////////////////////////
	}
	
	
	
   /////////////////////// Send Notification//////////////////////////////
   /*private void Show_Noti_old()
   {
	  Get_SharedPref_Values();//Get Shared Preference Value NotiSound
	 
	  if(!NotiID.equals("0"))
	  {	
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
        context.getSystemService(Context.NOTIFICATION_SERVICE);

        UnCommonProperties UnComObj=new UnCommonProperties();// Make An Object
        String AppTitle=UnComObj.GET_AppTitle();

        String Msg="";
        if(NotiType.equalsIgnoreCase("News"))
          Msg="News of "+AppTitle;
        else if(NotiType.equalsIgnoreCase("Event"))
          Msg="Event of "+AppTitle;
        else if(NotiType.equalsIgnoreCase("CPE"))
          Msg="CPE - "+AppTitle;
        else if(NotiType.equalsIgnoreCase("OPPOLL"))///Added On 26-04-2017
          Msg="OpinionPoll - "+AppTitle;
        else if(NotiType.equalsIgnoreCase("Other"))
          Msg=AppTitle+" -- Ad";

        Notification MyNoti = new Notification(icon, Msg, when);

        ////////////////////////
        int UniqueNotiId=Integer.parseInt(NotiID.trim());
        String[] Arr1=NotiMsgMain.split("@##@");
        String NotiTitle=Arr1[0];// Get Notification Title
        String NotiDesc=Arr1[1];// Get Notification Desc
        ////////////////////////

        Intent NotiIntent=null;

        if(NotiType.equalsIgnoreCase("News"))
        {
          //NotiIntent=new Intent(context, ShowAffiliation.class);
          NotiIntent=new Intent(context, NewsMain2.class);
         
          // set intent so it does not start a new activity
          NotiIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
          Intent.FLAG_ACTIVITY_SINGLE_TOP);
          //NotiIntent.putExtra("CHKg","@@@");
          NotiIntent.putExtra("Count", 88888);
          NotiIntent.putExtra("Positn", 0);
          NotiIntent.putExtra("MID",NotiID);
       }
       else if(NotiType.equalsIgnoreCase("Event"))
       {
	      NotiIntent=new Intent(context, EventDetailValue.class);
          // set intent so it does not start a new activity
          NotiIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
          Intent.FLAG_ACTIVITY_SINGLE_TOP);
          NotiIntent.putExtra("EventMID",NotiID);//Unique Event MID
	      NotiIntent.putExtra("Eventschk","4");
       }
       else if(NotiType.equalsIgnoreCase("CPE"))
       {
    	  NotiIntent=new Intent(context, CPE_Program_2.class);
          // set intent so it does not start a new activity
          NotiIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
          Intent.FLAG_ACTIVITY_SINGLE_TOP);
 	      NotiIntent.putExtra("CPE_Prog_Result",NotiID);
       }
       else if(NotiType.equalsIgnoreCase("OPPOLL"))
       {
    	  NotiIntent=new Intent(context, OpinionPoll_MainScreen.class);
          // set intent so it does not start a new activity
          NotiIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
          Intent.FLAG_ACTIVITY_SINGLE_TOP);
 	      NotiIntent.putExtra("MTitle","");
     	  NotiIntent.putExtra("ComeFrom","1");//ComeFrom Notification is 1
       }
       else if(NotiType.equalsIgnoreCase("Other"))
       {
          NotiIntent=new Intent(context, MainActivity.class);
       }

       NotiIntent.putExtra("Clt_Log",LogName);
       NotiIntent.putExtra("Clt_LogID",LogId);
       NotiIntent.putExtra("Clt_ClubName",ClubName);
       NotiIntent.putExtra("UserClubName",ClientId);

       PendingIntent Pend_Intent = PendingIntent.getActivity(context,UniqueNotiId+1000, NotiIntent,0);
       MyNoti.setLatestEventInfo(context, NotiTitle,NotiDesc, Pend_Intent);

       MyNoti.flags |= Notification.FLAG_AUTO_CANCEL;

       // Set notification sound
	   if(NotiSound.equals("0"))
       {
	      String Default_GrpMan_NotiSound="android.resource://" + context.getPackageName() + "/" + R.raw.notify;
	      MyNoti.sound = Uri.parse(Default_GrpMan_NotiSound);//Default GrpManager App sound
       }
       else if(NotiSound.length()>5){
    	  MyNoti.sound = Uri.parse(NotiSound);//Set Selected Notification Sound
       }

       // Vibrate if vibrate is enabled
       MyNoti.defaults |= Notification.DEFAULT_VIBRATE;

       notificationManager.notify(UniqueNotiId, MyNoti);// Notify Notification
       
       /////Add Event in AlarmManager And Calender (Added On 13-09-2017)////
       if( NotiType.equalsIgnoreCase("Event")){
    	   Add_Event_in_AlarmManager(NotiID,ClubName);///Set Event In Alarm Manager
           Add_Event_In_Calendar(NotiID,AppTitle);//Set Event in All Calender
       }
       ///////////////////////////////////////////
       
     }
   }*/
   
   
   ///New Type Notification Implimented on 07-05-2018
   private void Show_Noti()
	{
	   Get_SharedPref_Values();//Get Shared Preference Value NotiSound
	   
	   if(!NotiID.equals("0"))
	   {
	    String ns = Context.NOTIFICATION_SERVICE;
	    
		UnCommonProperties UnComObj=new UnCommonProperties();// Make An Object
        String AppTitle=UnComObj.GET_AppTitle();

        String Msg="";
        if(NotiType.equalsIgnoreCase("News"))
          Msg="News of "+AppTitle;
        else if(NotiType.equalsIgnoreCase("Event"))
          Msg="Event of "+AppTitle;
        else if(NotiType.equalsIgnoreCase("CPE"))
          Msg="CPE - "+AppTitle;
        else if(NotiType.equalsIgnoreCase("OPPOLL"))///Added On 26-04-2017
          Msg="OpinionPoll - "+AppTitle;
        else if(NotiType.equalsIgnoreCase("Other"))
          Msg=AppTitle+" -- Ad";

        ////////////////////////
        int UniqueNotiId=Integer.parseInt(NotiID.trim());
        String[] Arr1=NotiMsgMain.split("@##@");
        String NotiTitle=Arr1[0];// Get Notification Title
        String NotiDesc=Arr1[1];// Get Notification Desc
        ////////////////////////

        Intent NotiIntent=null;

        if(NotiType.equalsIgnoreCase("News"))
        {
          //NotiIntent=new Intent(context, ShowAffiliation.class);
          NotiIntent=new Intent(context, NewsMain2.class);
         
          // set intent so it does not start a new activity
          NotiIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
          Intent.FLAG_ACTIVITY_SINGLE_TOP);
          //NotiIntent.putExtra("CHKg","@@@");
          NotiIntent.putExtra("Count", 88888);
          NotiIntent.putExtra("Positn", 0);
          NotiIntent.putExtra("MID",NotiID);
       }
       else if(NotiType.equalsIgnoreCase("Event"))
       {
	      NotiIntent=new Intent(context, EventDetailValue.class);
          // set intent so it does not start a new activity
          NotiIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
          Intent.FLAG_ACTIVITY_SINGLE_TOP);
          NotiIntent.putExtra("EventMID",NotiID);//Unique Event MID
	      NotiIntent.putExtra("Eventschk","4");
       }
       else if(NotiType.equalsIgnoreCase("CPE"))
       {
    	  NotiIntent=new Intent(context, CPE_Program_2.class);
          // set intent so it does not start a new activity
          NotiIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
          Intent.FLAG_ACTIVITY_SINGLE_TOP);
 	      NotiIntent.putExtra("CPE_Prog_Result",NotiID);
       }
       else if(NotiType.equalsIgnoreCase("OPPOLL"))
       {
    	  NotiIntent=new Intent(context, OpinionPoll_MainScreen.class);
          // set intent so it does not start a new activity
          NotiIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
          Intent.FLAG_ACTIVITY_SINGLE_TOP);
 	      NotiIntent.putExtra("MTitle","");
     	  NotiIntent.putExtra("ComeFrom","1");//ComeFrom Notification is 1
       }
       else if(NotiType.equalsIgnoreCase("Other"))
       {
          NotiIntent=new Intent(context, MainActivity.class);
       }

       NotiIntent.putExtra("Clt_Log",LogName);
       NotiIntent.putExtra("Clt_LogID",LogId);
       NotiIntent.putExtra("Clt_ClubName",ClubName);
       NotiIntent.putExtra("UserClubName",ClientId);

       PendingIntent Pend_Intent = PendingIntent.getActivity(context,UniqueNotiId+1000, NotiIntent,0);
       
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
		byte[] AppLogo=dbObj.Get_AppLogo("C_"+ClientId+"_4");
		dbObj.close();
		if(AppLogo==null){
			LargeIcon_bitmap_image= BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);;
		}else{
			LargeIcon_bitmap_image = BitmapFactory.decodeByteArray(AppLogo,0, AppLogo.length);	
		}
		/////////////////////////////////////////
		

	    NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle()
	            .bigText(NotiDesc);

		   NotificationManager mNotificationManager =
				   (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


		   ////Added on Oreo Notification Problem (14-12-2018)
		   String channel_id = createNotificationChannel(context,uri,mNotificationManager);
		   ///////////////////////////////////////////////////////

	    NotificationCompat.Builder nb = new NotificationCompat.Builder(this,channel_id);

	            nb.setContentTitle(NotiTitle)
	            .setContentText(NotiDesc)
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

	    mNotificationManager.notify(UniqueNotiId, nb.build());

	     /////Add Event in AlarmManager And Calender (Added On 13-09-2017)////
	     if( NotiType.equalsIgnoreCase("Event")){
	    	   Add_Event_in_AlarmManager(NotiID,ClubName);///Set Event In Alarm Manager
	           Add_Event_In_Calendar(NotiID,AppTitle);//Set Event in All Calender
	     }
	     ///////////////////////////////////////////
	    
	   }
	   
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
   
   
   /////// Set Event Reminder in Alarm Manager ///////
   private void Add_Event_in_AlarmManager(String EventMid,String ClubName)
   {
	   try
	      {
	    	SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
	    	
	    	/// Get Event Details
	    	String SqlQry="SELECT Text1,Text2,Text3,Date1,Date2 from C_"+ClientId+"_4 Where Rtype='Event' AND M_Id="+EventMid;
	        Cursor cursorT = db.rawQuery(SqlQry, null);
	        
	        String EventName="",EventDesc="",EventVenue="",StrDate1 = "",StrDate2="";
	        if(cursorT.moveToFirst())
	        {
	    	  EventName=ChkVal(cursorT.getString(0));
		      EventDesc=ChkVal(cursorT.getString(1));
		      EventVenue=ChkVal(cursorT.getString(2)); 
		      StrDate1=ChkVal(cursorT.getString(3));
		      StrDate2=ChkVal(cursorT.getString(4));
	        }
	        cursorT.close();//Close Cursor
	        db.close();//Close database
	        
	        if(EventName.length()>3 && EventVenue.length()>3  && StrDate1.length()>10 && StrDate2.length()>10)
	        {
	        	String SDate=StrDate1.split(" ")[0].toString();//Get Only Date
	 		    String STime=StrDate2.split(" ")[1].toString();//Get Only Time;
	 		    
	 		    String[] Arr1=SDate.split("-");
	 		    int Year=Integer.parseInt(Arr1[0].trim());
	 		    int Month=Integer.parseInt(Arr1[1].trim());
	 		    int Day=Integer.parseInt(Arr1[2].trim());
	 		    
	 		    Arr1=STime.split(":");
	 		    int HourOfDay= Integer.parseInt(Arr1[0].trim());
	 		    int Min= Integer.parseInt(Arr1[1].trim());
	 		    
	 		    Calendar cal = Calendar.getInstance();  
	       	    cal.set(Year,Month-1, Day, HourOfDay, Min);///Set Event DateTime
	       	    long TotalTimeMili = cal.getTimeInMillis();//Get  DateTime Milliseconds
	       	    long ReminderTimeMili_24Hrs=TotalTimeMili-24*60*60*1000;//event Remineder before 24 hrs from Event startDatetime
	       	    long ReminderTimeMili_30Min=TotalTimeMili-30*60*1000;//event Remineder before 30 minutes from Event startDatetime
	       	    
	       	    SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy | hh:mm a");
	       	    String EventDateTime = format1.format(cal.getTime());///Set Event Date Time
	       	    
	       	    String EventData=EventName+"#"+EventDateTime+"#"+EventVenue+"#"+EventDesc;
	       	    
	       	    int ReqCode=Integer.parseInt(EventMid);
	       	    
	       	    Intent myIntent = new Intent(this, AlarmReceiver.class);
				myIntent.putExtra("ClientID", ClientId);
				myIntent.putExtra("LogclubName", ClubName);
				myIntent.putExtra("MID", ReqCode);
				myIntent.putExtra("EventData", EventData);//Added for Event Reminder Display in AlarmReceiver
				PendingIntent pendingIntent_24Hrs = PendingIntent.getBroadcast(this, ReqCode, myIntent, 0);
				PendingIntent pendingIntent_30Min = PendingIntent.getBroadcast(this, ReqCode+2000, myIntent, 0);
	       	    
				AlarmManager AMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
				AMgr.set(AlarmManager.RTC_WAKEUP, ReminderTimeMili_24Hrs, pendingIntent_24Hrs);//Alarm Set Before 24 Hrs from Event startDatetime
				AMgr.set(AlarmManager.RTC_WAKEUP, ReminderTimeMili_30Min, pendingIntent_30Min);//Alarm Set Before 30 Mins from Event startDatetime
				//AMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, pendingIntent);
	       	    
	        	/////////////////////////////
	        }
	        
	      }
	      catch (Exception e) {
	        e.printStackTrace();
	      } 
	      ////////////////////////////
   }
   
   
	
    //////// Add Event in Google Calendar or S planner or any Default Calendar installed (Added On 14-04-2017) /////
    private void Add_Event_In_Calendar(String EventMid,String AppTitle)
    {
      try
      {
    	SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
    	
    	/// Get Event Details
    	String SqlQry="SELECT Text1,Text2,Text3,Date1,Date2 from C_"+ClientId+"_4 Where Rtype='Event' AND M_Id="+EventMid;
        Cursor cursorT = db.rawQuery(SqlQry, null);
        
        String EventName="",EventDesc="",EventVenue="",StrDate1 = "",StrDate2="";
        if(cursorT.moveToFirst())
        {
    	  EventName=ChkVal(cursorT.getString(0));
	      EventDesc=ChkVal(cursorT.getString(1));
	      EventVenue=ChkVal(cursorT.getString(2)); 
	      StrDate1=ChkVal(cursorT.getString(3));
	      StrDate2=ChkVal(cursorT.getString(4));
        }
        cursorT.close();//Close Cursor
        db.close();//Close database
        
        if(EventName.length()>2 && EventVenue.length()>2  && StrDate1.length()>10 && StrDate2.length()>10)
        {
        	String SDate=StrDate1.split(" ")[0].toString();//Get Only Date
 		    String STime=StrDate2.split(" ")[1].toString();//Get Only Time;
 		    
 		    String[] Arr1=SDate.split("-");
 		    int Year=Integer.parseInt(Arr1[0].trim());
 		    int Month=Integer.parseInt(Arr1[1].trim());
 		    int Day=Integer.parseInt(Arr1[2].trim());
 		    
 		    Arr1=STime.split(":");
 		    int HourOfDay= Integer.parseInt(Arr1[0].trim());
 		    int Min= Integer.parseInt(Arr1[1].trim());
 		    
 		    Calendar cal = Calendar.getInstance();  
       	    cal.set(Year,Month-1, Day, HourOfDay, Min);///Set Event DateTime
       	    long EventStartDT = cal.getTimeInMillis();//Get Event Start DateTime Milliseconds
       	    long EventEndDT=EventStartDT+60*60*1000;//event ends 60 minutes from startDatetime
       	    
       	    String EventTitle=EventName+"("+AppTitle+")";
        	
        	////Add Event in Calendar ////
        	ContentResolver cr = getContentResolver();
        	
        	ContentValues values = new ContentValues();
        	values.put("calendar_id", 1);
        	values.put("title", EventTitle);
        	values.put("allDay", 0);////If it is bithday alarm or such // kind (which should remind me for whole day) 0 for false, 1 // for true
        	values.put("dtstart", EventStartDT); // Event Starts DateTime in Miliseconds
        	values.put("dtend", EventEndDT); // Event End DateTime in miliseconds
        	values.put("description",EventDesc);
        	values.put("eventLocation", EventVenue);
        	//values.put("visibility", 0);// default 0 confidential 1 private 2 public 3
        	//values.put("transparency", 0);// opaque 0 transparent 1
        	values.put("eventStatus", 1);// tentative 0, confirmed 1 canceled 2
        	values.put("hasAlarm", 1);// 0 false, 1 true
        	values.put("eventTimezone", "UTC/GMT +5:30");//Indian Time Zone
        	
        	Uri event =null; 
        	
        	try {
        		String EventUriString = "content://com.android.calendar/events";
        		event=cr.insert(Uri.parse(EventUriString), values);
        	}
        	catch(Exception ex)
        	{
        		System.out.print(ex.getMessage());
        		String tt="";
        	}
        	
        	////Set Remider in Event if want///
        	/*if (event != null)
            {
        	  long id = Long.parseLong( event.getLastPathSegment() );
        	  String reminderUriString = "content://com.android.calendar/reminders"; 
        	  Uri REMINDERS_URI = Uri.parse(reminderUriString);
        	  values = new ContentValues();
        	  values.put("event_id", id );
        	  values.put("method", 1 );// Alert Methods: Default(0), // Alert(1), Email(2),SMS(3) 
        	  values.put( "minutes", 30 );//set time in min which occur before event start (here 30 min.)
        	  cr.insert( REMINDERS_URI, values );
            }*/
        	
        	/////////////////////////////
        }
        
      }
      catch (Exception e) {
        e.printStackTrace();
      } 
      ////////////////////////////
    }
 
 
 
     /// Sent information to server that notification has been delivered(added on 29-03-2017)
     private void Noti_Delivered()
     {
    	 Thread T1 = new Thread()
         {
           public void run(){
             try
             {
               WebServiceCall webcall= new WebServiceCall();//Intialise WebserviceCall Object
               
               String TempMS="",TempNE="";
               if(UserType.equals("SPOUSE"))
      			 TempMS="S";
      		   else
      			 TempMS="M"; 
      		   
               if(NotiType.equalsIgnoreCase("News"))
            	   TempNE="NewsD";
               else if(NotiType.equalsIgnoreCase("Event"))
                   TempNE="EventD";
               
               String R=webcall.Read_NewsEvents(ClientId, LogId, TempMS, TempNE, NotiID);
             }
             catch (Exception ex){
            	 System.out.println(ex.getMessage());
             }
           }
         };
         T1.start();
     }
 
 
     
	 // Sync START ///////////
	 public void Sync_Start()
    {
	  	Runnable r = new Runnable() {
         public void run() {
           try
             {
		         WebServiceCall webcall=new WebServiceCall();//Call a Webservice
		         CurrentDT_Diff=webcall.SyncDT_GetJullian();
	             if(CurrentDT_Diff!="CatchError")
	             {
	        	   String SArr[]=CurrentDT_Diff.split("#");
	        	   SyncDT=SArr[0].trim();
	        	   //String t=SyncDT;
	        	   
	        	   Tab4Name="C_"+ClientId.trim()+"_4"; // Table 4
			       TabOp1Name="C_"+ClientId.trim()+"_OP1"; // OpinionPoll1 table
			       TabOp2Name="C_"+ClientId.trim()+"_OP2"; // OpinionPoll2 table
	        	   
	        	   db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
	        	   
	        	   //Table 4 for News/Event/Others /////////////
	        	   String sqlSearch = "select max(m_id),count(m_id),min(Syncid),min(SyncDT) from "+Tab4Name.trim();
				   cursorT = db.rawQuery(sqlSearch, null);
				   if(cursorT.moveToFirst())
				   {
				      Tab4Max_Mid=cursorT.getInt(0);
				      Tab4Count =cursorT.getInt(1);
				      Tab4Min_Sync=cursorT.getInt(2);
				      Tab4Min_SyncDT=cursorT.getInt(3);
				   }
				   cursorT.close();
				  
				   
			       //Table Opinion Poll 1/////////////
				   sqlSearch = "select max(m_id),count(m_id),min(Syncid),min(SyncDT) from "+TabOp1Name;
				   cursorT = db.rawQuery(sqlSearch, null);
				   if(cursorT.moveToFirst())
				   {
					 TabOp1Max_Mid=cursorT.getInt(0);
					 TabOp1Count =cursorT.getInt(1);
					 TabOp1Min_Sync=cursorT.getInt(2);
					 TabOp1Min_SyncDT=cursorT.getInt(3);
				   }
				   cursorT.close();
				
				   //Table Opinion Poll 2 /////////////
				   sqlSearch = "select max(m_id),count(m_id),min(Syncid),min(SyncDT) from "+TabOp2Name;
				   cursorT = db.rawQuery(sqlSearch, null);
				   if(cursorT.moveToFirst())
				   {
					 TabOp2Max_Mid=cursorT.getInt(0);
					 TabOp2Count =cursorT.getInt(1);
					 TabOp2Min_Sync=cursorT.getInt(2);
					 TabOp2Min_SyncDT=cursorT.getInt(3);
				   }
				   cursorT.close();
				  
				   db.close();///Close DataBase
				   
				   String ConnectionString = "jdbc:jtds:sqlserver://103.21.58.192:1433/mda_clubs";
		           Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
		           
		           if(conn==null)
		            conn = DriverManager.getConnection(ConnectionString,"mda_club", "MDA.1234_");
		           else if(conn.isClosed())
		        	conn = DriverManager.getConnection(ConnectionString,"mda_club", "MDA.1234_");
        	       
		           if(NotiType.equalsIgnoreCase("OPPOLL"))///Added On 26-04-2017
		        	   RecordInsertion(TabOp1Name,TabOp1Count,TabOp1Max_Mid,TabOp1Min_Sync,TabOp1Min_SyncDT);// Insert For Table Op1
		           else
		        	   RecordInsertion(Tab4Name,Tab4Count,Tab4Max_Mid,Tab4Min_Sync,Tab4Min_SyncDT);// Insert For Table 4
	             }
            }
	  		catch (Exception e) {
	  			try {
					conn.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	  			//e.printStackTrace();
	  			Show_Noti();//Display Notification
	  	    }
          }
       };
       Thread t = new Thread(r);
       t.start();
	}
	
	
	 private void RecordInsertion(String TableName,int TabCount,int Max_M_id,int Min_Sync,int Min_SyncDT)
	 {
		 String StrQry="";
		 try
		 {
		   Statement statement = conn.createStatement();
		   if(TabCount==0)
			   StrQry = "select * from "+TableName+" order by M_id";
		   else
			   StrQry = "select * from "+TableName+" where M_id>"+Max_M_id+" order by M_id";
	       rs = statement.executeQuery(StrQry);
	       Prog_Insert(TableName,TabCount,Min_Sync,Min_SyncDT);
	     } catch (Exception e) {
	    	 try {
				conn.close();
			 } catch (SQLException e1) {
				e1.printStackTrace();
			 }
		    //e.printStackTrace();
	    	Show_Noti();//Display Notification
		}
	 } 

	 public void Prog_Insert(final String TableName,final int TabCount,final int Min_Sync,final int Min_SyncDT)
	 {
	     Runnable r1 = new Runnable() {
	         public void run() {
	           try
	              { 
	        	      DbHandler db1 = new DbHandler(context,TableName);
	            	  while (rs.next()) 
	        		  {
	            		 if(TableName==Tab4Name)          
	            		 {
	            		    db1.Tab4AddContact(TableName,rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),
	            			 rs.getString(5), rs.getString(6), rs.getString(7),rs.getString(8),rs.getString(9),
	            			 rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13),rs.getString(14),
	            			 rs.getString(15),rs.getBytes(16),rs.getString(17),rs.getString(18),
	            			 rs.getString(19),rs.getString(20),rs.getString(21),rs.getString(22),rs.getInt(23),SyncDT,
	            			 rs.getString(25),rs.getString(26),rs.getString(27),rs.getString(28),rs.getString(29));
	            		 }
	            		 else if(TableName==TabOp1Name)
	            		 {
            			    String[] ArrData=new String[11];
            			    ArrData[0]=rs.getString(2);// OP_Name
            			    ArrData[1]=rs.getString(3);// OP_From
            			    ArrData[2]=rs.getString(4);// OP_TO
            			    ArrData[3]=rs.getString(5);// OP_From_1
            			    ArrData[4]=rs.getString(6);// Op_To_1
            			    ArrData[5]=rs.getString(7);// Cond1
            			    ArrData[6]=rs.getString(8);// Cond2
            			    ArrData[7]=rs.getString(9);// Co_Type
            			    ArrData[8]=rs.getString(10);// Time_Req
            			    ArrData[9]=rs.getString(13);// Op_Publish
            			    ArrData[10]=rs.getString(14);// U_Ans
            			   
            			    db1.Tab_Opinion1_InsertData(TableName,rs.getInt(1),ArrData,rs.getInt(11),SyncDT);
	            		 }
	            		 else if(TableName==TabOp2Name)
	            		 {
                             String[] ArrData=new String[11];
            			     ArrData[0]=rs.getString(2);// OP1_ID
            			     ArrData[1]=rs.getString(3);// PSNO
            			     ArrData[2]=rs.getString(4);// SNO
            			     ArrData[3]=rs.getString(5);// Question
            			     ArrData[4]=rs.getString(6);// Ans1
            			     ArrData[5]=rs.getString(7);// Ans2
            			     ArrData[6]=rs.getString(8);// Ans3
            			     ArrData[7]=rs.getString(9);// Ans4
            			     ArrData[8]=rs.getString(10);// Remark
            			     ArrData[9]=rs.getString(11);// Answer
            			     ArrData[10]=rs.getString(14);// Remark_Req
            			   
            			     db1.Tab_Opinion2_InsertData(TableName,rs.getInt(1),ArrData,rs.getInt(12),SyncDT);
	            		 }
	                  }
	            	  rs.close(); // Close Result Set
	            	  db1.close(); // Close Local DataBase 
	            	  
	            	  if(TabCount!=0){
		        		  RecordUpdation(TableName,Min_Sync,Min_SyncDT); // Records Updation 
		        	  }else{
		        		  
        				  if(TableName==TabOp1Name)
        				  {
        					  RecordInsertion(TabOp2Name,TabOp2Count,TabOp2Max_Mid,TabOp2Min_Sync,TabOp2Min_SyncDT); // Insert For Table Opinion2
        				  }
        				  else
        				  {
        					  RecordDeletion(); // Delete Records After Insertion Special Case
        				  }
		        	  }
	              }
	 	  		  catch (Exception e) {
	 	  			  try {
	 					   conn.close();
	 				  } catch (SQLException e1) {
	 					    e1.printStackTrace();
	 				  }
	 	  			  //e.printStackTrace();
	 	  		 }
	         }
	     };
	     Thread t1 = new Thread(r1);
	     t1.start();
    }
	
	
	 private void RecordUpdation(String TableName,int Min_Sync,int Min_SyncDT)
	 {
		 try
		 {
		   Statement statement = conn.createStatement();
		   String StrQry = "select * from "+TableName+" where Syncid>"+Min_Sync+" AND SyncDT>"+Min_SyncDT+" order by M_id";
	       rs = statement.executeQuery(StrQry);
	       Prog_Update(TableName);
	     } catch (Exception e) {
	    	 try {
					conn.close();
				 } catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				 }
	    	 Show_Noti();//Display Notification
	  		//e.printStackTrace();
		 }
	 }
	 
	 private void Prog_Update(final String TableName)
	 {
		Runnable r2 = new Runnable() {
	       public void run() {
	          try
	             {
	        	    String StrQry="";int j=0;
					if(TableName==Tab4Name)
					  j=23; // SyncId Position
					else if(TableName==TabOp1Name)
					  j=11; // SyncId Position
					else if(TableName==TabOp2Name)
					  j=12; // SyncId Position
	        	    
					while (rs.next()) 
				    {
				      int M_Id=rs.getInt(1);
				 	  int SYNCID=rs.getInt(j);
				 	  
				 	  db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
				 	  String sqlSearch = "select Syncid from "+TableName+" where M_id="+M_Id;
					  cursorT = db.rawQuery(sqlSearch, null); 
				 	    while(cursorT.moveToFirst()){
				 		   if(SYNCID!=cursorT.getInt(0)){
				 			 StrQry = "Delete from "+TableName+" where M_id="+M_Id;
				 			 db.execSQL(StrQry);
				 			 db.close();
				 				    
				 			 DbHandler db1 = new DbHandler(context,TableName);
				 			 
				 			 if(TableName==Tab4Name){
			            	   db1.Tab4AddContact(TableName,M_Id,rs.getString(2),rs.getString(3),rs.getString(4),
			            		 rs.getString(5), rs.getString(6), rs.getString(7),rs.getString(8),rs.getString(9),
			            		 rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13),rs.getString(14),
			            		 rs.getString(15),rs.getBytes(16),rs.getString(17),rs.getString(18),rs.getString(19),
			            		 rs.getString(20),rs.getString(21),rs.getString(22),SYNCID,SyncDT,rs.getString(25),
			            		 rs.getString(26),rs.getString(27),rs.getString(28),rs.getString(29));
				 			 }
				 			 else if(TableName==TabOp1Name)
		            		 {
	            			   String[] ArrData=new String[11];
	            			   ArrData[0]=rs.getString(2);// OP_Name
	            			   ArrData[1]=rs.getString(3);// OP_From
	            			   ArrData[2]=rs.getString(4);// OP_TO
	            			   ArrData[3]=rs.getString(5);// OP_From_1
	            			   ArrData[4]=rs.getString(6);// Op_To_1
	            			   ArrData[5]=rs.getString(7);// Cond1
	            			   ArrData[6]=rs.getString(8);// Cond2
	            			   ArrData[7]=rs.getString(9);// Co_Type
	            			   ArrData[8]=rs.getString(10);// Time_Req
	            			   ArrData[9]=rs.getString(13);// Op_Publish
	            			   ArrData[10]=rs.getString(14);// U_Ans
	            			   
	            			   db1.Tab_Opinion1_InsertData(TableName,M_Id,ArrData,SYNCID,SyncDT);
		            		 }
		            		 else if(TableName==TabOp2Name)
		            		 {
                               String[] ArrData=new String[11];
	            			   ArrData[0]=rs.getString(2);// OP1_ID
	            			   ArrData[1]=rs.getString(3);// PSNO
	            			   ArrData[2]=rs.getString(4);// SNO
	            			   ArrData[3]=rs.getString(5);// Question
	            			   ArrData[4]=rs.getString(6);// Ans1
	            			   ArrData[5]=rs.getString(7);// Ans2
	            			   ArrData[6]=rs.getString(8);// Ans3
	            			   ArrData[7]=rs.getString(9);// Ans4
	            			   ArrData[8]=rs.getString(10);// Remark
	            			   ArrData[9]=rs.getString(11);// Answer
	            			   ArrData[10]=rs.getString(14);// Remark_Req
	            			   
	            			   db1.Tab_Opinion2_InsertData(TableName,M_Id,ArrData,SYNCID,SyncDT);
		            		 }
				 			 
				 			 db1.close();
				 		   }
				 		   else
				 		   {
				 			  StrQry = "Update "+TableName+" Set SyncDT="+SyncDT+" where M_id="+M_Id;
				 			  db.execSQL(StrQry);
				 			  db.close();
				 		   }
				 		  break;
				 		}
				 	    cursorT.close();
				 	    db.close(); // Close Local DataBase
			          } 
					  rs.close(); 
					  RecordUpdate_DT(TableName); //Update DatetimeDiff To All Data  
					  
       			      if(TableName==TabOp1Name)
       			      {
       				     RecordInsertion(TabOp2Name,TabOp2Count,TabOp2Max_Mid,TabOp2Min_Sync,TabOp2Min_SyncDT); // Insert For Table Opinion2
       			      }
       			      else
					  {
       				     RecordDeletion(); // Delete Records After Updation
       			      }
	            }
	 	  		catch (Exception e) {
	 	  		    try {
	 	  		    	db.close(); // Close Local DataBase
	 					conn.close();
	 				} catch (SQLException e1) {
	 					 e1.printStackTrace();
	 				}
	 	  			//e.printStackTrace();
	 	  		}
	      }
	    };
	    Thread t2 = new Thread(r2);
	    t2.start();
	 }
	 
	 private void RecordUpdate_DT(String TableName)
	 {
		 db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		 String StrQry = "Update "+TableName+" Set SyncDT="+SyncDT;
		 db.execSQL(StrQry);
	     db.close();
	 }
	 
	 
	 private void RecordDeletion()
	 {
		 Show_Noti();//Display Notification
		 
		 try
		 {
		   Statement statement = conn.createStatement();
		   String Tab5Name="C_"+ClientId.trim()+"_5";
		   String StrQry = "select TEXT1,TEXT2 from "+Tab5Name+" Where Rtype='DEL' order by M_id";
	       rs = statement.executeQuery(StrQry);
	       Prog_Delete();
	     } catch (Exception e) {
	    	 try {
				conn.close();
			 } catch (SQLException e1) {
				e1.printStackTrace();
			 }
		}
	 }
	
	// Sync END///////////
	public void Prog_Delete()
	{
		Runnable r3 = new Runnable() {
		   public void run() {
		      try
		         { 
		    	    String StrQry="";
					db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
					while (rs.next()) 
					{
					   String TableName=rs.getString(1);
					   String M_Id=rs.getString(2);
					   StrQry = "Delete from "+TableName+" where M_id="+M_Id;
					   db.execSQL(StrQry);
					 }
					rs.close(); // Close ResultSet of Server db Table
					db.close(); // Close Local DataBase
					conn.close(); // Close Server DataBase
			     }
		 	     catch (Exception e) {
		 	    	 try {
		 	    		db.close(); // Close Local DataBase
		 				conn.close();
		 			  } catch (SQLException e1) {
		 				e1.printStackTrace();
		 			  }
		 	  		e.printStackTrace();
		 	     }
		   }
		};
		Thread t3 = new Thread(r3);
		t3.start();
	 }
	

	//call function for initialise blank if null is there
	private String ChkVal(String DVal)
	{
		if((DVal==null)||(DVal.equalsIgnoreCase("null"))){
			DVal="";
		}
		return DVal.trim();
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
