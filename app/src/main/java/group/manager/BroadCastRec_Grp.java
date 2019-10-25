package group.manager;

import java.io.ByteArrayInputStream;
import java.util.Calendar;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BroadCastRec_Grp extends BroadcastReceiver{
	 String sqlSearch,s1,City,incomingNumber,state;//,Sdatabase;
	 private static AlertDialog mDialog;
	 SQLiteDatabase db;
	 Cursor cursorT,cursorT2;
	 int Countspouse=0;
	 byte[] imgP,ImgAd,ImgLogo;
	 SharedPreferences sharedpreferences; 
	 String[] Arr_Client;
	 boolean[] Arr_CallScreen;
	 String[] Arr_CallScreen_Position,Arr_ClubName;
	 private static String mLastState;
	 
	 
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		try
		{
		  if(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED))
		  {
			 // Start a Service on Boot Complete
			 //Start_ServiceCallNew(context);
		     // System.out.println("Boot Complete");
			  
		     // Set ON Boot Complete (Alarm Birthday Notification with Saved Time)
		     SetAlarm_Birday_Noti_Time(context);
		  }
		  else
		  {
		    imgP=null; // Image Person Initialisation
		    ImgAd=null; // Image ad initialisation
		    ImgLogo=null; // // Image Logo initialisation
		    s1="No Record";
		
		      //System.out.println(MultiClient); // MultiClients or C_Ids
		      state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

			  if(state!=null && (!state.equals(mLastState) || android.os.Build.VERSION.SDK_INT>=28))
		      {
		       mLastState=state;
		       //System.out.println("@ "+state);
	           if(TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
	        	  
	        	   if(mDialog!=null)
		    	   {
			           //mDialog.dismiss();
			           mDialog.cancel();
		    	   }

				   incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
		    	   if(incomingNumber!=null) {
					   String MobNo = incomingNumber.substring(3);//It will Remove +91(Length 3)
					   //System.out.println(MobNo);
					   if (s1.equals("No Record")) {
						   record(context, MobNo); // Get Records from local Database
					   }
				   }

	          }else if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)){ 
	    	     if(mDialog!=null)
	    	     {
		           //mDialog.dismiss();
		           mDialog.cancel();
	    	     }
	          }
		    }
	     }
		}
		catch(Exception ex)
		{
			//System.out.println("Tapas"+ex.getMessage());
			Toast.makeText(context,"Boot Error:"+ex.getMessage(), 1).show();
		}
   }

	
	// Start a service ServiceCallNew for Sync Table4
	/*private void Start_ServiceCallNew(Context context,String ClientId)
	{
		Intent intent = new Intent(context,Service_Call_New.class);
		intent.putExtra("ClientID",ClientId);
		context.startService(intent);
	}*/
	
	
	private void record(Context context,String MobNo){
		// Sdatabase=Environment.getExternalStorageDirectory()+"/Download/MDA_Club";
		try{
			 db = context.openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
			 String SqlQry="Select ClientID,setting_Callscrn,setting_Callscrn_posi,ClubName from LoginMain Where DDateTime is not null AND length(DDateTime)<>0";
			 Cursor cursorT = db.rawQuery(SqlQry, null);
			 int RCount=cursorT.getCount();
			 //Initialise All Array
			 Arr_Client=new String[RCount];
			 Arr_CallScreen=new boolean[RCount];
			 Arr_CallScreen_Position=new String[RCount];
			 Arr_ClubName=new String[RCount];
			 int j=0;
			 while(cursorT.moveToNext())
			 {
				 Arr_Client[j]=cursorT.getString(0).trim();
				 Arr_CallScreen[j]=Boolean.parseBoolean(cursorT.getString(1).trim());
				 Arr_CallScreen_Position[j]=cursorT.getString(2).trim();
				 Arr_ClubName[j]=cursorT.getString(3).trim();
				 j++;
			 }
		     cursorT.close();
			 
			 if(Arr_Client!=null)
			 {
			   for(int i=0;i<Arr_Client.length;i++)
			   {
				   String ClientId=Arr_Client[i].trim();
				   boolean  CallScreen=Arr_CallScreen[i];
				   String CallScreen_Position=Arr_CallScreen_Position[i].trim();
				   String ClubName=Arr_ClubName[i].trim();
				   if(CallScreen)
				   {
				     CallDbValues(MobNo.trim(),ClientId);
				   }
				   if(s1!=null && !s1.equals("No Record"))
				   {
					  alert(context,CallScreen_Position,ClubName);
					  break;
				   }
			   }
			 }
			 db.close();
		 }catch(Exception e){
			 System.out.println(e.toString());
			 db.close();
		 }
    }

	// Connect with Db and get values to display in alert    ////////////////////sunil sir want this...
	private void CallDbValues(String MobNo,String ClientId)
	{
		 sqlSearch = "select M_Name,M_Pic,C4_BG,C4_DOB_Y,S_Name,S_Pic,C3_BG,M_Mob,M_SndMob,M_Land1,M_Land2,S_Mob,Oth1,M_City from C_"+ClientId+"_2"+
	                 " Where M_Mob='"+MobNo+"' OR M_SndMob='"+MobNo+"' OR M_Land1='"+MobNo+"' OR M_Land2='"+MobNo+"' OR S_Mob='"+MobNo+"' "; 
		 System.out.println(sqlSearch);
 		 cursorT = db.rawQuery(sqlSearch, null);
	     if (cursorT.moveToFirst()) 
 	     {
	    	String MemMob1= ChkVal(cursorT.getString(7));//Member Mob1
	    	String MemMob2= ChkVal(cursorT.getString(8));//Member Mob2
	    	String MemPh1= ChkVal(cursorT.getString(9));//Member Landline 1
	    	String MemPh2= ChkVal(cursorT.getString(10));//Member Landline 2
	    	String Smob= ChkVal(cursorT.getString(11));//Spouse Mob
	    	City= ChkVal(cursorT.getString(12));//City
	    	
	        if(MemMob1.equals(MobNo) || MemMob2.equals(MobNo) || MemPh1.equals(MobNo) || MemPh2.equals(MobNo)){
	    	   String s=""+ChkVal(cursorT.getString(2))+ChkVal(cursorT.getString(3));
	 	       s1=(s+" "+cursorT.getString(0)).trim();
	 	       String Desig=ChkVal(cursorT.getString(12));//Member Desig
	 	       if(Desig.length()>1)
	 	         s1=s1+" , "+Desig;
	 	       imgP=cursorT.getBlob(1);
	        }
	        else if(Smob.equals(MobNo)){
		    	String s=""+ChkVal(cursorT.getString(6));
		 	    s1=(s+" "+cursorT.getString(4)).trim();
		 	    imgP=cursorT.getBlob(5);
		    }
         } 
		 cursorT.close();
		 
		if(s1!=null)
		{
		  if(!s1.equals("No Record"))
		  {
			// Retreive Ad Image from db
			sqlSearch = "Select Photo1 from C_"+ClientId+"_4 WHERE Rtype='Ad1'";
		    cursorT = db.rawQuery(sqlSearch, null);
		    if (cursorT.moveToFirst()) 
	 	    {
		       ImgAd=cursorT.getBlob(0);
	        }
		    cursorT.close();
		    
		    // Retreive Logo Image from db
		    sqlSearch = "Select Photo1 from C_"+ClientId+"_4 WHERE Rtype='LOGO'";
		    cursorT = db.rawQuery(sqlSearch, null);
		    if (cursorT.moveToFirst()) 
	 	    {
		       ImgLogo=cursorT.getBlob(0);
	        }
		    cursorT.close();
		  }
		}
	}
	
	 private String ChkVal(String str){
		 if(str==null){
			 str="";
		 }
		return str.trim();
	 }
	
	// Display Ad or Logo in the ImageView in Calling Detail Screen
    private void Display_Image(ImageView ImgVw,byte[] Img)
	{
		if(Img==null)
		{
			ImgVw.setVisibility(View.GONE);
		}
		else
		{
		  Bitmap bitmap = BitmapFactory.decodeByteArray(Img,0,Img.length);
		  ImgVw.setVisibility(View.VISIBLE);
		  ImgVw.setImageBitmap(bitmap);
		}
	}
    
    
	
	public void alert(Context context,String CallScreenPosition,String ClubName){
		// TODO Auto-generated method stub
		AlertDialog.Builder alertadd = new AlertDialog.Builder(context);
		final FrameLayout frameView = new FrameLayout(context);
		alertadd.setView(frameView);
		mDialog = alertadd.create();
		LayoutInflater inflater = mDialog.getLayoutInflater();
		View dialoglayout = inflater.inflate(R.layout.dialog_main, frameView);
			TextView text= (TextView) dialoglayout.findViewById(R.id.textView);
			TextView GroupName= (TextView) dialoglayout.findViewById(R.id.TVGrpName);
			TextView txtCity= (TextView) dialoglayout.findViewById(R.id.txtCity);
			ImageView ImgPerson= (ImageView) dialoglayout.findViewById(R.id.imgPerson);
			ImageView ImgVw_Ad= (ImageView) dialoglayout.findViewById(R.id.imgVw_Ad);
			ImageView ImgLoc= (ImageView) dialoglayout.findViewById(R.id.imgLoc);
			ImageView ImgVw_logo= (ImageView) dialoglayout.findViewById(R.id.imgVwLogo);
			ImageView imgClose= (ImageView) dialoglayout.findViewById(R.id.imgClose);
			
			imgClose.setOnClickListener(new OnClickListener(){ 
	            @Override 
	            public void onClick(View arg0){
	            	mDialog.dismiss();
	            }
			});
			
			text.setText(s1);//Set Name
			GroupName.setText(ClubName);//Set ClientId or ClubCode
			
			ImgLoc.setVisibility(View.GONE);
			if(City.length()>0)
				ImgLoc.setVisibility(View.VISIBLE);
			
			txtCity.setText(City);//set City
			
			if(imgP!=null){
				ByteArrayInputStream imageStream = new ByteArrayInputStream(imgP);
				Bitmap theImage = BitmapFactory.decodeStream(imageStream);
				ImgPerson.setImageBitmap(theImage);
			}else{
				ImgPerson.setImageResource(R.drawable.person1);
			}
			
			Display_Image(ImgVw_Ad,ImgAd); // Display Ad for Calling Detail Screen
			Display_Image(ImgVw_logo,ImgLogo); // Display Logo for Calling Detail Screen
			
			alertadd.setView(dialoglayout);
			mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
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
				        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
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
			
			if(CallScreenPosition.equals("Top"))
			{
				params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
			}
			else
			{
				params.gravity = Gravity.CENTER;
			}
	        ////////Set Flags to make touchable calling screen//////////////
		    //params.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
		    //params.x = 50;   //x position
		    //params.y = 50; //y position
			
			mDialog.getWindow().setAttributes(params);
		    //WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
			//wmlp.gravity = Gravity.TOP | Gravity.RIGHT;
		    //wmlp.x = 50;   //x position
			//wmlp.y = 50;  
			////////Set Flags to make touchable calling screen//////////////
			//wmlp.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
			
			if(android.os.Build.VERSION.SDK_INT >= 26 )///Andriod version 26(Oreo)
			{
				mDialog.getWindow().setType(2038);
			}
			else if(android.os.Build.VERSION.SDK_INT == 25 )///Andriod version 25(Oreo)
			{
				mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			}
			else{
				mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
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
			
			
			mDialog.show();
	}
	
	
	private void SetAlarm_Birday_Noti_Time(Context context)
	{
		int interval=24*60*60*1000;//Interval a Day(24hrs)
		//int interval=2*60*60*1000;//Interval a 2hrs(1hrs)
		int HOUROFDAY,alarmHH,MINUTEss,alarmMM;
		Calendar calendar = Calendar.getInstance();
		HOUROFDAY=Calendar.HOUR_OF_DAY;
		MINUTEss=Calendar.MINUTE;
				
		AlarmManager alarmManager = (AlarmManager)context.getSystemService("alarm");
		try
		{
		  db = context.openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		  String SqlQry="Select M_ID,ClientID,ClubName,setting_Birday_noti_time from LoginMain Where DDateTime is not null AND length(DDateTime)<>0";
		  Cursor cursorT = db.rawQuery(SqlQry, null);
	      int RCount=cursorT.getCount();
	      if(RCount>0)
	      {
			while(cursorT.moveToNext())
			{
			  int Mid=cursorT.getInt(0);
			  String ClientID=cursorT.getString(1);
			  String LogclubName=cursorT.getString(2);
			  String Birday_Noti_Time=cursorT.getString(3);
			  if(Birday_Noti_Time!=null)
			  {
				 int NType=0;
				 Birday_Noti_Time=Birday_Noti_Time.trim();
				 
				 if(Birday_Noti_Time.equals("@")){
					  NType=2;  //Only Anniversary
				 }else if((Birday_Noti_Time.length()>1)&&(Birday_Noti_Time.contains("@"))){
					  NType=3; //both Bday and Anniversary
				 }else if((Birday_Noti_Time.length()>1)&&(!Birday_Noti_Time.contains("@"))){
					  NType=1;  //Only BDay
				 }
				  
			    if(NType!=0)
			    {
			    	Intent myIntent = new Intent(context, AlarmReceiver.class);
					myIntent.putExtra("ClientID", ClientID);
					myIntent.putExtra("LogclubName", LogclubName);
					myIntent.putExtra("MID", Mid);
					PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Mid, myIntent, 0);
			    	
					Birday_Noti_Time=Birday_Noti_Time.replace("@", "").replace(":", "#")+" ";
				    
                    if(NType==2){
                    	 alarmHH=9;
    					 alarmMM=0;
                    }
                    else{
                    	String [] temp= Birday_Noti_Time.split("#");
     				    String hh=temp[0].toString().trim();
     					String mm=temp[1].toString().trim();
     					alarmHH=Integer.parseInt(hh); 
     					alarmMM=Integer.parseInt(mm);  
                    }
                     
					calendar.set(HOUROFDAY, alarmHH);
				    calendar.set(MINUTEss, alarmMM);
				     
				    //alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
				    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, pendingIntent);	
				    //Toast.makeText(context,RCount+"#"+Mid+"#"+Birday_Noti_Time+"#"+alarmHH+"#"+alarmMM, 1).show();
			    }	
			  }
			}
	     }
	     cursorT.close();
		 db.close();
	  }
	  catch(Exception ex)
	  {
		 db.close();
		 System.out.println(ex.getMessage());
	  } 
	}
	 
}
