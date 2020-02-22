package group.manager;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
//import com.google.android.gcm.GCMRegistrar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuPage extends Activity{
	Intent menuIntent,MenuBtnIntent;
	TextView txtclub,txtname;//Txtcount;
	String Logname,Logclubid,LogclubName,Str_IEMI,ClientID,s,Str_main,str_memid,Webevent,StrChkDT,personal,StrCount,
	StrQry,Tab6Name,t2name,t2mob,t2add1,t2add2,t2add3,t2city,t2email,t2bg,t2day,t2mon,t2year,UserType,Ann_D,Ann_M,Ann_Y,Temp_M_S;
	Context context=this;
	Connection conn;
	AlertDialog ad;
	ProgressDialog Progsdial;
	Thread networkThread;
	WebServiceCall webcall;
	TelephonyManager tm;
	String [] temp;
	int id,counting=0,T2mid,s_count,Maxid,Minsycid,Pendcount;
	SQLiteDatabase db;
	String sqlSearch,Tab2Name,Tab4Name,TabMiscName,SyncImpData,TabFamilyName;
	Cursor cursorT;
	Chkconnection chkconn;
	boolean InternetPresent;
	LinearLayout llaydialog;
	SharedPreferences sharedpreferences,shrd;
	Editor editr,edits;
	Integer tempsize,i=0,j=0,TCount_Tab2=0;
	String [] arr1,arr2,CodeArr;//,arr3;
	String str1,str2,str3,Wepdata,mid,nottile,notdesc,notmid;
	byte[] imgP,t2pic=null;
	String bPic;
	List<RowItem_Menu> rowItems_Menu,rowItems_Menu2;
	ListView LVMenu,LVMenu2;
	String MenuList;
	byte[] AppLogo;
	String UID="";
	String Full_AdditionalData_UpdatePro="",packageName="";
	ImageView ivexport,ivadmin;
	String Name,Relation,Father,Mother,Current_Loc,Mob_1,Mob_2,DOB_D,DOB_M,DOB_Y,EmailId,Age,Education,Work_Profile,sHOWBirth="",
			Showanni="",TableNameEvent,ChkSync="",ChkSync_OneTime="1",TabOpinion1,TabOpinion2,Birday_Noti_Time="";
	AlertDialog.Builder alertDialogBuilder3;
	UnCommonProperties objuncm;
	
	///PUSH NOTIFICATION////////////////////////
	String SharePre_GCMRegId="";//For Shared Preference
	Controller aController;
	//AsyncTask<Void, Void, Void> mRegisterTask;// Asyntask
	//////////////////////////////////////
	String MemDir_View="1";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menupage);
		
		txtclub=(TextView)findViewById(R.id.tvClubname);
		txtname=(TextView)findViewById(R.id.tvMebernName);
		LVMenu = (ListView) findViewById(R.id.lVMenu);
		LVMenu2 = (ListView) findViewById(R.id.lVMenu2);
		ivexport = (ImageView) findViewById(R.id.imageViewExport);
		ivadmin = (ImageView) findViewById(R.id.imageViewAdminshow);
		ImageView imgAppShare=(ImageView)findViewById(R.id.imgShareApp);
		ImageView imgSearch=(ImageView)findViewById(R.id.imgSearch);
		
		ad=new AlertDialog.Builder(this).create();
		alertDialogBuilder3 = new AlertDialog.Builder(context);
		
		objuncm=new UnCommonProperties();
		
		Get_SharedPref_Values();// Get Stored Shared Pref Values of Login
		
		//add more member(if call this button call button click event & call alert function.)
		Set_App_Logo_Title(); // Set App Logo and Title
		
		txtname.setText(Logname);
		
		webcall=new WebServiceCall();//Intialise WebserviceCall Object
		chkconn=new Chkconnection();
		Str_IEMI = new CommonClass().getIMEINumber(context);//Added On 14-02-2019
		
		Tab2Name="C_"+ClientID+"_2";
		Tab4Name="C_"+ClientID+"_4";
		TabMiscName="C_"+ClientID+"_MISC";
		TabFamilyName="C_"+ClientID+"_Family";
		Tab6Name="C_"+ClientID+"_6";
		TableNameEvent="C_"+ClientID+"_Event";
		TabOpinion1="C_"+ClientID+"_OP1";//Table Opinion Poll 1
		TabOpinion2="C_"+ClientID+"_OP2";//Table Opinion Poll 2
		
        Set_Menu_items();// Set Menu Items
		
		if(UserType.equalsIgnoreCase("ADMIN")){
			ivadmin.setVisibility(View.VISIBLE);	
		}else{
			ivadmin.setVisibility(View.GONE);	
		}
	    
	    ///// PUSH NOTIFICATION REGISTRATION/////
        if(SharePre_GCMRegId.length()==0){
			Reg_PushNoti_FCM();// Registered Push FCM(Added on 15-05-2019)
        }
        /////////////////////////////////////////
        
        
        ///// Check and set ByDefault Bday and Anni Notification Time for first time installation(Default Setting) Added on 29-08-2018 //////
        if(Birday_Noti_Time.equals("0")){
        	Set_BdayAnni_Default_and_NotiAlarm();
        }
        /////////////////////////////////////////////////
		
		/// SYNC MOBILE TO SERVER(M-S)
		Sync_M_S ObjsyncMS=new Sync_M_S(context);
		ObjsyncMS.Sync_Add_News();// Sync M-S Add_News 
		ObjsyncMS.Sync_Add_Events();// Sync M-S Add_Events
		ObjsyncMS.Sync_ReadNewsEvent("Both");// Sync M-S ReadNewsEvent
		ObjsyncMS.Sync_EventAttend_Or_Not_Confirmation();// Sync (M-S) Event Attended or Not
		ObjsyncMS.Sync_OpPoll_Data();//Sync (M-S) Opinion Poll added on (13-04-2017)
		///////////////////////////////
		
		/////Check Sync Is Required Or Not
		InternetPresent = chkconn.isConnectingToInternet(context);
		if(InternetPresent==true  && !ChkSync_OneTime.equals("2"))
		{
		  ///Check App Updation is Required or Not
	      Check_AppStore_Version();
	      ///////////////////////////////////////
	        
		  String StrChkSync=Chk_Sync_Required();
		  if(!ChkSync.equals(StrChkSync)){
			 Set_SharedPref_Values();
			 ConfirmAlert_Sync();
		  }
		}
		/////////////////////////////////
		
		Sync_FamilyMemberDetails();// Sync (M-S) FamilyMembers Records
		
	    //ivexport.setVisibility(View.VISIBLE);//VISIBLE or GONE
	    
	    ivexport.setOnClickListener(new OnClickListener(){ 
            public void onClick(View arg0){
             alertDialogBuilder3
       		 .setMessage("Do you want export data!")
	                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog,int id) {
	                    	dialog.dismiss();
	                    	exportDB();//exportDB();////////////export internal data of phone in download folder
	                    }
	                })
	                .setNeutralButton("Cancel",new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog,int id) {
	                     	dialog.dismiss();
	                     }
	                });
       		 ad = alertDialogBuilder3.create();
	         ad.show();	
            }
	    });
	    
	  
	    //Global search Click Event
	    imgSearch.setOnClickListener(new OnClickListener(){ 
            public void onClick(View arg0){
            	GlobalSearch(); // Show Global Search
            }
	    });
	    
	    
	    ivadmin.setOnClickListener(new OnClickListener(){ 
            public void onClick(View arg0){
            	ShowUtility("2");
            }
	    });
	    
	    imgAppShare.setOnClickListener(new OnClickListener(){ 
	           public void onClick(View arg0){
	        	   App_Sharing(); // Share Your App 
	           }
		   });
		
	    LVMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parentAdapter, View view, int position,long id) {
	        	RowItem_Menu Obj=rowItems_Menu.get(position);
	        	String MenuName=Obj.getMenuName();
	        	if(MenuName.equals("DIR") || MenuName.contains("DIR_"))
	        	{
	        		Directory(MenuName);
	        	}
	        	else if(MenuName.equals("PRO"))
	        	{
	        		Update_Profile();
	        	}
	        	else if(MenuName.equals("GOV") || MenuName.equals("MAN")|| MenuName.equals("OFF") || MenuName.equals("EXEC") || MenuName.contains("GOVOTH"))
	        	{
	        		Govern_Body(MenuName); // MAN and GOV Are Same only Title Changed
	        	}
	        	else if(MenuName.equals("PAST") || MenuName.equals("CHAIR"))
	        	{
	        		Past_President(MenuName);// PAST and CHAIR Are Same only Title Changed
	        	}
	        	else if(MenuName.equals("EVE"))
	        	{
	        		Events();
	        	}
	        	else if(MenuName.equals("EVE_GAL"))
	        	{
	        		Gallery("Event");//Goto Events/News Gallery
	        	}
	        	else if(MenuName.equals("NEWS"))
	        	{
	        		Show_News();
	        	}
	        	else if(MenuName.equals("CPE"))
	        	{
	        		Cpe_Login(); // cpeicai.org Login 
	        	}
	        	else if(MenuName.contains("CPE_P"))
	        	{
	        		Cpe_Program(MenuName); // CPE Programmes updated on 04-03-2017
	        	}
	        	else if(MenuName.equals("ADDNEWS"))
	        	{
	        		AddNews(); //Add News
	        	}
	        	else if(MenuName.equals("UTIL"))
	        	{
	        		ShowUtility("1");
	        	}
	        	else if(MenuName.equals("JAYCEE"))
	        	{
	        		ShowUtility("3");
	        	}
	        	else if(MenuName.equals("IOCL"))
	        	{
	        		ShowUtility("4");
	        	}
	        	else if(MenuName.contains("ADVOTH!"))
	        	{
	        		ShowAdvisory(MenuName); 
	        	}
	        	else if(MenuName.contains("ICAI_"))
	        	{
	        		Committee(MenuName);// New Menu Added 11-03-2016
	        	}
	        	else if(MenuName.equals("SUGG"))
	        	{
	        		Suggestion(1);// New Menu Added 22-08-2016
	        	}
	        	else if(MenuName.contains("MATRI"))
	        	{
	        		Matrimony();// MATRI Added 25-10-2016
	        	}
	        	else if(MenuName.contains("PIC_"))
	        	{
	        		FullAdvertise(MenuName);  // New Menu Added 07-11-2016
	        	}
	        	else if(MenuName.contains("OPPOLL"))
	        	{
	        		OpinionPoll(MenuName); //Opinion Poll Menu Added On 11-04-2017 
	        	}
	        	else if(MenuName.contains("LMERATES"))
	        	{
	        		LMERates(MenuName); //LME Rates (Only for BME) Menu Added On 04-05-2017 
	        	}
	        	else if(MenuName.contains("RBIRATES"))
	        	{
	        		ExchangeRates(MenuName); //Exchange Rates (Only for BME) Menu Added On 29-05-2017 
	        	}
	        	else if(MenuName.contains("MCXRATES"))
	        	{
	        		MCXRates(MenuName); //MCX Market Watch  (Only for BME) Menu Added On 28-06-2017 
	        	}
	        	else if(MenuName.contains("NEWSL"))
	        	{
	        		NewsLetter(MenuName); //NEWs Letter Added On 08-05-2017 
	        	}
	        	else if(MenuName.contains("Booking"))
	        	{
	        		Booking(MenuName); //Booking Added On 24-05-2017 
	        	}
	        	else if(MenuName.contains("LEDGER") || MenuName.contains("MEDICLAIM"))
	        	{
	        		Ledger(MenuName); //Ledger On 24-05-2017 
	        	}
	        	else if(MenuName.contains("SONG"))
	        	{
	        		DistrictSong(MenuName); //District Song Added On 23-07-2017
	        	}
	        	else if(MenuName.equals("SUGG_PHOTO"))
	        	{
	        		Suggestion(2);// //Suggestion or Complaint with Photo Added in 31-03-2018
	        	}
	        	else if(MenuName.contains("CAM"))
	        	{
	        		Photo_UplaodForClub(MenuName); //Photo Upload for Club Added on 28-07-2018(Group lci321b1)
	        	}
	        	else if(MenuName.contains("MULTIROW"))
	        	{
	        		MultiRowOption(MenuName); //MULTIROW Added on 02-08-2018
	        	}
	        	else if(MenuName.contains("TREE"))
	        	{
	        		FamilyTreeOption(MenuName); //Family Tree Added on 05-09-2018(only for gadodia and chaparia group)
	        	}
				else if(MenuName.contains("EVENTACC"))//Added on 23-03-2019
				{
					EventConfirmation(); //Event confirmation
				}
				else if(MenuName.contains("DGCAL"))//Added on 21-02-2020
				{
					DG_Calendar(); //DG Calendar
				}
	        }
	     });


	    LVMenu2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parentAdapter, View view, int position,long id) {
	        	RowItem_Menu Obj=rowItems_Menu2.get(position);
	        	String MenuName=Obj.getMenuName();
	        	if(MenuName.equals("MESS"))
	        	{
	        		MessagesDesk(); //Messages Desk
	        	}
	        	else if(MenuName.contains("FULLAD"))
	        	{
	        		FullAdvertise("FULLAD"); //Full Advertisement 
	        	}
	        	else if(MenuName.equals("INFO"))
	        	{
	        		ShowInformation(); //Information 
	        	}
	        	else if(MenuName.equals("ABOUT"))
	        	{
	        		AboutUs(); //AboutUs Added on 11-07-2017 
	        	}
	        }
	     });
  }
	
	
	////Update and set preference for bday and anni notification settings (added on 29-08-2018) ////
	private void Set_BdayAnni_Default_and_NotiAlarm()
	{
		 Birday_Noti_Time="09:00@";///By Default Time for Bday and anni Notication setting
		 
		 SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		 String SqlQry="UPDATE LoginMain SET setting_Birday_noti_time='"+Birday_Noti_Time.trim()+"' " +     
				 "Where ClientID='"+ClientID.trim()+"' AND UID='"+UID.trim()+"'";
		 db.execSQL(SqlQry);
		 
		 SqlQry="Select M_ID from LoginMain Where ClientID='"+ClientID.trim()+"' AND UID='"+UID.trim()+"'";
		 Cursor cursorT = db.rawQuery(SqlQry, null);
		 int Mid=0;
	     if(cursorT.moveToFirst())
		 {
			Mid=cursorT.getInt(0);
		 }
	     cursorT.close();
	     /////////////////////
		 
		 db.close();///Close Connection
		  
		 ////Update Value in SharedPreference ////
		 editr = sharedpreferences.edit();
		 editr.putString("Birday_Noti_Time",Birday_Noti_Time); //Saved Birday_Noti_Time
		 editr.commit();
		 ////////////////////////
		 
		 ////// Set Alarm Manager with DateTime /////
		 
		 int ReqCode=Mid;//Get Unique MID As RequestCode of pendingIntent to Make Unique pendingIntent
		 Intent myIntent = new Intent(this, AlarmReceiver.class);
		 myIntent.putExtra("ClientID", ClientID);
		 myIntent.putExtra("LogclubName", LogclubName);
		 myIntent.putExtra("MID", ReqCode);
		 PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ReqCode, myIntent, 0);

		 ///Default Hrs and mins
		 int alarmHH=9;
		 int alarmMM=0;
		 
		 Calendar calendar = Calendar.getInstance();
		 int HOUROFDAY=Calendar.HOUR_OF_DAY;
		 int MINUTEss=Calendar.MINUTE;
		 
		 calendar.set(HOUROFDAY, alarmHH);
		 calendar.set(MINUTEss, alarmMM);
		 
		 AlarmManager Am = (AlarmManager) getSystemService(ALARM_SERVICE);
		 Am.cancel(pendingIntent);//Cancel alarm having this pending intent
		 int interval=24*60*60*1000;//Interval a Day(24hrs)
		 Am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, pendingIntent);
		 
		 ///////////////////////////////////////////
	 }
	
	
	
	//Set App Logo and Title
	private void Set_App_Logo_Title()
	 {
		 setTitle(LogclubName); // Set Title
		 menuIntent = getIntent(); 
		 AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		 // Set App LOGO
		 if(AppLogo==null)
		 {
			 getActionBar().setIcon(R.drawable.ic_launcher);
		 }
		 else
		 {
			Bitmap bitmap = BitmapFactory.decodeByteArray(AppLogo,0, AppLogo.length);
			BitmapDrawable icon = new BitmapDrawable(getResources(),bitmap);
			getActionBar().setIcon(icon);
		 }
	 }
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    String Addgroup_orNot=objuncm.GET_Addgroup();
	    MenuItem itemB = menu.findItem(R.id.birthdaygrp);
	    MenuItem itemAn = menu.findItem(R.id.anniver);
	    MenuItem itemgrMore = menu.findItem(R.id.Add_More_Group);
	    if(Addgroup_orNot.equals("Yes")){
	    	itemgrMore.setVisible(true);
	    }else{
	    	itemgrMore.setVisible(false);
	    }
	    
	    if(sHOWBirth.equals("1")){
	    	itemB.setVisible(true);
	    }else{
	    	itemB.setVisible(false);
	    }
	    if(Showanni.equals("1")){
	    	itemAn.setVisible(true);	
	    }else{
	    	itemAn.setVisible(false);
	    }
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.Add_More_Group:
	        	Add_Group(); // Add More Group for Login
	            return true;
	        case R.id.Sync:
	        	Synchronisation(); // Sync Data Here
	            return true;
	        case R.id.Settings:
	        	App_Settings(); // App Settings Here
	            return true;
	        case R.id.birthdaygrp:
	        	Showbirthday(); // Show bithday Here
	            return true;
	        case R.id.bloodgroup:
	        	ShowBloodgroup(); // Show blood group Here
	            return true;
	        case R.id.anniver:
	        	Showanniversary(); // Show anniver Here
	            return true;  
	        case R.id.changepass:
	        	ChangePassword(); // Show Change Password
	            return true;   
	        /*case R.id.contactus:
	        	ContactUs(); // Show Contact Us (Added on 10-07-2017)
	            return true;*/
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	//Get Data from Saved Shared Preference
	private void Get_SharedPref_Values()
	{
		 sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		 
		  if (sharedpreferences.contains("clientid"))
	      {
			  ClientID=sharedpreferences.getString("clientid", "");
	      }
		  if (sharedpreferences.contains("userid"))
	      {
			  UID=sharedpreferences.getString("userid", "");
	      }
	      if (sharedpreferences.contains("name"))
	      {
	    	  Logname=sharedpreferences.getString("name", "");
          } 
	      if (sharedpreferences.contains("cltid"))
	      {
	    	  Logclubid=sharedpreferences.getString("cltid", "");
          } 
	      if (sharedpreferences.contains("clubname"))
	      {
	    	  LogclubName=sharedpreferences.getString("clubname", "");
          } 
	      if (sharedpreferences.contains("MenuList"))
	      {
	    	  MenuList=sharedpreferences.getString("MenuList", "");
          } 
	      if (sharedpreferences.contains("TCount_Tab2"))
		  {
			  String Tab2Count=sharedpreferences.getString("TCount_Tab2", "");// Tab2Count is the Total Records of Table 2
			  TCount_Tab2=Integer.parseInt(Tab2Count); // Here Tab2Count Takes Only Integer Value
		  }
	      if (sharedpreferences.contains("UserType"))
		  {
			  UserType=sharedpreferences.getString("UserType", "");
			  System.out.println("member: "+UserType);
		  }
	      if (sharedpreferences.contains("SharePre_GCMRegId"))
		  {
		      SharePre_GCMRegId=sharedpreferences.getString("SharePre_GCMRegId", "");
	      }
	      if (sharedpreferences.contains("ChkSync"))
		  {
	    	  ChkSync=sharedpreferences.getString("ChkSync", "");
	      }
	      if (sharedpreferences.contains("ChkSync_OneTime"))
		  {
	    	  ChkSync_OneTime=sharedpreferences.getString("ChkSync_OneTime", "");
	      }
	      if (sharedpreferences.contains("MemDir_View"))
		  {
	    	  MemDir_View=sharedpreferences.getString("MemDir_View", "");
	      }
	      if (sharedpreferences.contains("Birday_Noti_Time"))//Added on 29-08-2018
		  {
	    	  Birday_Noti_Time=sharedpreferences.getString("Birday_Noti_Time", "");
	      }
	}
	
	//Set Data in SharedPref
	private void Set_SharedPref_Values()
	{
		 editr = sharedpreferences.edit();
		 editr.putString("ChkSync_OneTime","2"); //Saved ChkSync_OneTime
		 editr.commit();
	}
	
	
	// Set Menu Items Dynamically
	private void Set_Menu_items()
	{
		sHOWBirth="";Showanni="";
		String Menu1="",Menu2="",BAVAlue="";
		String[] sp,AB;
		System.out.println(MenuList);
		String s= MenuList.trim().replace("^", "#");
		//System.out.println("1: "+s);
		
		// Split s for 1st and 2nd menu list
		String[] MenuL=s.trim().split("@");
		Menu1=MenuL[0];
		if(MenuL.length>=2)///(MenuL.length==2)
		{
		  Menu2=MenuL[1].trim();
		}
		if(MenuL.length==3)
		{
			BAVAlue=MenuL[2];
			BAVAlue=BAVAlue.replace("^", "#");
			AB=BAVAlue.split("#");
			
			sHOWBirth=AB[0];
			Showanni=AB[1];
			if(sHOWBirth==null)
				sHOWBirth="";
			if(Showanni==null)
				Showanni="";
		}
		
		// Set Ist MenuList
		//System.out.println("2: "+Menu1);
		
		//System.out.println("3: "+Menu2+" "+Menu2.length());
		//Menu1=Menu1+"#DGCAL!DG Calendar";
        sp=Menu1.split("#");
        rowItems_Menu = new ArrayList<RowItem_Menu>();
        for(int i=0;i<sp.length;i++)
        {
          String MenuName=sp[i].trim();
          RowItem_Menu item = new RowItem_Menu(MenuName);
          rowItems_Menu.add(item);
        }
        Adapter_Menu adp1 = new Adapter_Menu(context,R.layout.list_item_menu, rowItems_Menu);
        LVMenu.setAdapter(adp1);
        
        //Set 2nd MenuList
        if(Menu2.length()!=0)
        {
          sp=Menu2.split("#");
          rowItems_Menu2 = new ArrayList<RowItem_Menu>();
          for(int i=0;i<sp.length;i++)
          {
            String MenuName=sp[i].trim();
            RowItem_Menu item1 = new RowItem_Menu(MenuName);
            rowItems_Menu2.add(item1);
          }
          Adapter_Menu2 adp2 = new Adapter_Menu2(context,R.layout.list_item_menu2, rowItems_Menu2);
          LVMenu2.setAdapter(adp2);
        }
	}
	
	
	private void ShowAdvisory(String MenuTitle)
	{ 
		String[] Arr1=MenuTitle.split("!");
		String MTitle=Arr1[1].trim();
		
	    menuIntent= new Intent(context,Advisory.class);
	    menuIntent.putExtra("Clt_ClubName", LogclubName);
	    menuIntent.putExtra("ClientID", ClientID);
	    menuIntent.putExtra("AppLogo", AppLogo);
	    menuIntent.putExtra("MTitle",MTitle);
		startActivity(menuIntent);
		finish();
	}
	
	
	private void Showbirthday()
	{
		menuIntent= new Intent(getBaseContext(),ShowBirthadayNotification.class);
		menuIntent.putExtra("ClientID", ClientID);
		menuIntent.putExtra("LogclubName", LogclubName);
		menuIntent.putExtra("Menu_Noti", "menu");
		menuIntent.putExtra("AppLogo", AppLogo);
		startActivity(menuIntent);
	    finish();
	}
	
	
	private void ShowUtility(String val)
	{
		menuIntent= new Intent(getBaseContext(),UlilitiesList.class);
		menuIntent.putExtra("AppLogo", AppLogo);
		menuIntent.putExtra("CondChk", val);
		startActivity(menuIntent);
	    finish();
	}
	
	
	private void ShowBloodgroup()
	{
		menuIntent= new Intent(getBaseContext(),ShowBloodGroup.class);
		menuIntent.putExtra("ClientID", ClientID);
		menuIntent.putExtra("LogclubName", LogclubName);
		menuIntent.putExtra("Menu_Noti", "menu");
		menuIntent.putExtra("AppLogo", AppLogo);
		startActivity(menuIntent);
	    finish();
	}
	
	
	private void Showanniversary()
	{
		menuIntent= new Intent(getBaseContext(),ShowAnniversary.class);
		menuIntent.putExtra("ClientID", ClientID);
		menuIntent.putExtra("LogclubName", LogclubName);
		menuIntent.putExtra("Menu_Noti", "menu");
		menuIntent.putExtra("AppLogo", AppLogo);
		startActivity(menuIntent);
	    finish();
	}
	
    
	private void Directory(String MenuTitle)
	{
		if(TCount_Tab2>0){
			GOTO_Directory(MenuTitle);
	    }else{
	    	Get_Sync_RData(0,MenuTitle);  
	    }
	}
	
	// Go To Directory(SwipeScreen) Activity
	private void GOTO_Directory(String MenuTitle)
	{
		String DirName="";
		if(MenuTitle.contains("DIR_")){
			String[] t=MenuTitle.split("!");
			DirName=t[0];
		}
		else{
			DirName=MenuTitle;
		}
	    ////Special Directory Condition with DirName
    	DbHandler dbhandlerObj = new DbHandler(context,Tab2Name);
    	dbhandlerObj.Chk_Filter_SavedPermanent(Tab4Name);// Saved Filter Permanent Or Temporary
    	String CCBYear=GetCCBYear();//Get CCB Cuurent Year
    	String Special_Dir_Condition=dbhandlerObj.Special_Dir_Condition(DirName, Tab4Name,CCBYear);
    	dbhandlerObj.close();//Close DbHandler
        ///////////////////////////////////////////
		
    	if(MemDir_View.equals("1"))
    	  menuIntent= new Intent(getBaseContext(),SwipeScreen.class); // Directory Activity DataView
    	else
    	   menuIntent= new Intent(getBaseContext(),Directory_New_ListView.class); // Directory New ListView
    	
    	menuIntent.putExtra("Special_Dir_Condition",Special_Dir_Condition);
    	menuIntent.putExtra("CFrom","MENU");//Comes From MenuPage
    	menuIntent.putExtra("CCBYear",CCBYear);//CCB Year Added 16-11-2018
    	nextactivty();//Go to Directory Page
	}
	
	//Go To Sync Activity
	private void GoTo_Sync()
	{
		InternetPresent = chkconn.isConnectingToInternet(context); // Chk Internet
		
		if(InternetPresent==true){
			menuIntent= new Intent(getBaseContext(),ClubSync.class);
			menuIntent.putExtra("StrTT",SyncImpData); // All Tables Count,MaxMId,MinSyncId
			nextactivty();  
		}else{
		   goback("Result!","Synchronisation is required so connection with internet.",1);
		}
	}
	
	 // Saved TCountTab2 in Table LaginMain AND Shared Preference
	 private void Set_SharedPref_TCountTab2(int count) {
		if(count>0 && TCount_Tab2==0)
		{
		  db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		  String SqlQry="UPDATE LoginMain SET Tab2Count="+count+" "+
		                "Where ClientID='"+ClientID.trim()+"' AND UID='"+UID.trim()+"'";
		  db.execSQL(SqlQry);
		  db.close();
		  
		  String Tab2Count=String.valueOf(count);
		  editr = sharedpreferences.edit();
		  editr.putString("TCount_Tab2",Tab2Count); //TCount_Tab2 is the Total Records of Table 2
		  editr.commit();
		}
     }
	
	// Saved SyncImpData(All Tables TCount/MaxMid/MinSyncDT/MinSyncId) in Shared Preference
	private void Set_SyncImpData(){
		SharedPreferences shrdcriteria = context.getSharedPreferences("MyCriteria",Context.MODE_PRIVATE);
		Editor editorCrit = shrdcriteria.edit(); // Edit Shared Pref critria Records
		editorCrit.putString("Critval", SyncImpData);
		editorCrit.commit();
	}
	
	//Modified 01-07-2015 'Tapas'
	private void Update_Profile()
	{
		String StrQ="",StrPend="";
		db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
		StrQ="select Count(M_id) from "+Tab4Name+" where Rtype='Pending'";
		cursorT = db.rawQuery(StrQ, null);
		if (cursorT.moveToFirst()) {
			int Rcount= cursorT.getInt(0);
			if(Rcount>0)
				StrPend="(Pending Update)";
		}
		cursorT.close();
		
		if(UserType.equals("SPOUSE")){
			StrQ="select S_Name,S_Mob,M_Add1,M_Add2,M_Add3,M_City,S_Email,S_BG,S_DOB_D,S_DOB_M,S_DOB_Y,M_MrgAnn_D,M_MrgAnn_M,M_MrgAnn_Y,C3_Name AS [Country],C3_Email AS [State],M_Pin,C1_FName,C2_FName,S_Pic from "+Tab2Name+" where M_id="+Logclubid;
		}
		else{
			StrQ="select M_Name,M_Mob,M_Add1,M_Add2,M_Add3,M_City,M_Email,M_BG,M_DOB_D,M_DOB_M,M_DOB_Y,M_MrgAnn_D,M_MrgAnn_M,M_MrgAnn_Y,C3_Name AS [Country],C3_Email AS [State],M_Pin,C1_FName,C2_FName,M_Pic,M_SndMob,M_Land1,M_Land2,M_Email1 from "+Tab2Name+" where M_id="+Logclubid;
		}
		
		Cursor cursorT1 = db.rawQuery(StrQ, null);
		if(cursorT1.getCount()==0){
			goback("Result!","No record found,please synchronize your data.",1);
		}else{
		    if (cursorT1.moveToFirst()) {
		    	try{
		    		//System.out.println(StrQ);
		    		personal= ChkVal(cursorT1.getString(0))+StrPend+"#"+ChkVal(cursorT1.getString(1))+"#"+ChkVal(cursorT1.getString(2))+"#"+
			    			  ChkVal(cursorT1.getString(3))+"#"+ChkVal(cursorT1.getString(4))+"#"+ChkVal(cursorT1.getString(5))+"#"+
			    			  ChkVal(cursorT1.getString(6))+"#"+ChkVal(cursorT1.getString(7))+"#"+ChkVal(cursorT1.getString(8))+"#"+
			    			  ChkVal(cursorT1.getString(9))+"#"+ChkVal(cursorT1.getString(10))+"#"+ChkVal(cursorT1.getString(11))+"#"+
			    			  ChkVal(cursorT1.getString(12))+"#"+ChkVal(cursorT1.getString(13))+"#"+ChkVal(cursorT1.getString(14))+"#"+
			    			  ChkVal(cursorT1.getString(15))+"#"+ChkVal(cursorT1.getString(16))+"#"+ChkVal(cursorT1.getString(17))+"#"+ChkVal(cursorT1.getString(18));
				  	imgP=cursorT1.getBlob(19);//Image
				  	
				  	if(!UserType.equals("SPOUSE")){
				  		personal=personal+"#"+ChkVal(cursorT1.getString(20))+"#"+ChkVal(cursorT1.getString(21))+"#"+ChkVal(cursorT1.getString(22))+"#"+ChkVal(cursorT1.getString(23));
				  	}
				  	
		    	}catch(Exception ex){
		    		System.out.println(ex.getMessage());
		    	}
		    }
		    cursorT1.close();
		    menuIntent= new Intent(getBaseContext(),UpdateProfile.class);
			menuIntent.putExtra("WebPers",personal);
			menuIntent.putExtra("WebPersIMG",imgP);
			menuIntent.putExtra("UserType",UserType);
			nextactivty();
		}
		cursorT.close();
		db.close();
	}

	//Goto Governing Body
	private void Govern_Body(String MenuTitle)
	{
		String Rtype="Govern";
		if(MenuTitle.equals("GOV"))
			MenuTitle="Governing Body";
		else if(MenuTitle.equals("MAN"))
			MenuTitle="Managing Committee";
		else if(MenuTitle.equals("OFF"))
			MenuTitle="Office Bearers";
		else if(MenuTitle.equals("EXEC"))
			MenuTitle="Executive Committee";
		else if(MenuTitle.contains("GOVOTH")){
			String[] Arr1=MenuTitle.split("!");
			MenuTitle=Arr1[1];
			if(MenuTitle.contains("GOVOTH1"))
			  Rtype=Arr1[2];
		}
		menuIntent= new Intent(getBaseContext(),GoverningBody.class);
		menuIntent.putExtra("MTitle",MenuTitle);
		menuIntent.putExtra("Rtype",Rtype);
		nextactivty();
	}
	
	//Committee(ICAI_COMM) / PastPresidentNew Option(ICAI_PP)
	private void Committee(String MenuTitle)
	{
		String CCBYear=GetCCBYear();//Get CCB Year
		
		String PgName="";
		if(MenuTitle.contains("!")){
			String[] Arr=MenuTitle.split("!");
			PgName=Arr[0].trim();
			MenuTitle=Arr[1].trim();
		}
		menuIntent= new Intent(getBaseContext(),Committee.class);
		menuIntent.putExtra("PgName",PgName);
		menuIntent.putExtra("MTitle",MenuTitle);
		
		if(PgName.equals("ICAI_COMM") || PgName.contains("ICAI_MULCOMM") || PgName.equals("ICAI_BRANCH") || PgName.equals("ICAI_CPE")){
			menuIntent.putExtra("PNo",1);
		}
		else if(PgName.equals("ICAI_PP")){
			menuIntent.putExtra("PNo",3);
			menuIntent.putExtra("ItemName","Past Presidents");
		}
		else if(PgName.equals("ICAI_QRY")){
			menuIntent.putExtra("PNo",3);
			menuIntent.putExtra("ItemName",MenuTitle);
		}
		
		 menuIntent.putExtra("CCBYear",CCBYear);//CCB Year Added 08-02-2017
		 menuIntent.putExtra("Clt_Log",Logname);
		 menuIntent.putExtra("Clt_LogID",Logclubid);
		 menuIntent.putExtra("Clt_ClubName",LogclubName);
		 menuIntent.putExtra("UserClubName",ClientID);
		 menuIntent.putExtra("AppLogo", AppLogo);
		 startActivity(menuIntent);
	}
	
	private void Past_President(String MenuTitle)
	{
		if(MenuTitle.equals("PAST"))
			MenuTitle="Past President";
		else if(MenuTitle.equals("CHAIR"))
			MenuTitle="Past Chairman";
		menuIntent= new Intent(getBaseContext(),PastPresi_Secretary.class);
		menuIntent.putExtra("MTitle",MenuTitle);
		menuIntent.putExtra("selectP_S","1");
		nextactivty();
	}
	
	private void Events()
	{
		menuIntent= new Intent(getBaseContext(),EventCalendar.class);
		menuIntent.putExtra("Eventschk","1");
		nextactivty();
	}


	//// Added on 23-03-2019
	private void EventConfirmation()
	{
		menuIntent= new Intent(getBaseContext(),EventCalendar.class);
		menuIntent.putExtra("Eventschk","2");
		nextactivty();
	}
	
	///Added on 11-07-2017
	private void AboutUs()
	{
		menuIntent= new Intent(getBaseContext(),AboutUs.class);
		nextactivty();
	}
	
	///Goto Event/News Gallery
	private void Gallery(String Type)
	{
		menuIntent= new Intent(getBaseContext(),Gallery.class);
		//menuIntent= new Intent(getBaseContext(),News_Letter.class);
		menuIntent.putExtra("NEType",Type);
		nextactivty();
	}
	
	private void Show_News()
	{
		//menuIntent= new Intent(getBaseContext(),AffiliationAPP.class);
		menuIntent= new Intent(getBaseContext(),NewsMain.class);
		menuIntent.putExtra("Count",999999);
		menuIntent.putExtra("POstion",0);
		nextactivty();
	}
	
	//Goto Suggestion
	private void Suggestion(int Type)
	{
		if(Type==1)
		    menuIntent= new Intent(getBaseContext(),Sug_Comp.class);
		else 
			menuIntent= new Intent(getBaseContext(),Suggesstion_with_photo.class);
		
		nextactivty();
	}


	//DG Calendar or Program New Menu Added on 21-02-2020
	private void DG_Calendar()
	{
		menuIntent= new Intent(getBaseContext(),DGCalendar.class);
		menuIntent.putExtra("Clt_Log",Logname);
		menuIntent.putExtra("Clt_LogID",Logclubid);
		menuIntent.putExtra("Clt_ClubName",LogclubName);
		menuIntent.putExtra("UserClubName",ClientID);
		menuIntent.putExtra("AppLogo", AppLogo);
		startActivity(menuIntent);
	}
	
	private void Matrimony()
	{
		menuIntent= new Intent(getBaseContext(),Wedding1.class);
		nextactivty();
	}
	
	private void Cpe_Login()
	{
		menuIntent= new Intent(getBaseContext(),CPE_Login.class);
		nextactivty();
	}
	
	private void Cpe_Program(String MenuTitle)
	{
		if(MenuTitle.equals("CPE_P"))
			MenuTitle="CPE Programmes";
		else if(MenuTitle.contains("CPE_P!")){
			String[] Arr1=MenuTitle.split("!");
			MenuTitle=Arr1[1];
		}
		menuIntent= new Intent(getBaseContext(),CPE_Program_1.class);
		menuIntent.putExtra("MTitle",MenuTitle);
		nextactivty();
	}
	
	private void MessagesDesk()
	{
		menuIntent= new Intent(getBaseContext(),AffiliationAPP.class);
		menuIntent.putExtra("Count",888888);
		menuIntent.putExtra("POstion",0);
		nextactivty();
	}
	
	private void FullAdvertise(String RType)
	{
		if(RType.contains("PIC_")){
			String[] Arr1=RType.split("!");
			RType=Arr1[0];
		}
		menuIntent= new Intent(getBaseContext(),FullAdvertisement.class);
		menuIntent.putExtra("Type","1");
		menuIntent.putExtra("RType",RType);
		nextactivty();
	}
	
    private void ShowInformation()
	{
		menuIntent= new Intent(getBaseContext(),Information.class);
		nextactivty();
	}
    
    
    //Goto Change Password
  	private void ChangePassword()
  	{
  		menuIntent= new Intent(getBaseContext(),ChangePassword.class);
  		nextactivty();
  	}

  	
    //Goto Contact Us (Added on 10-07-2017)
  	private void ContactUs()
  	{
  		menuIntent= new Intent(getBaseContext(),ContactUs.class);
  		nextactivty();
  	}
  	
  	
    private void OpinionPoll(String MenuTitle)
    {
    	MenuTitle=MenuTitle.split("!")[1];
    	
    	menuIntent= new Intent(getBaseContext(),OpinionPoll_MainScreen.class);
    	menuIntent.putExtra("MTitle",MenuTitle);
    	menuIntent.putExtra("ComeFrom","1");//ComeFrom MenuPage is 1
		nextactivty();
    }
    
    
    ///London Metal Rates Only for BME group Added on 04-05-2017
    private void LMERates(String MenuTitle)
    {
    	MenuTitle=MenuTitle.split("!")[1];
    	menuIntent= new Intent(getBaseContext(),LME_Rates.class);
    	menuIntent.putExtra("MTitle",MenuTitle);
		nextactivty();
    }
    
    
    ///Exchange Rates Only for BME group Added on 04-05-2017
    private void ExchangeRates(String MenuTitle)
    {
    	MenuTitle=MenuTitle.split("!")[1];
    	menuIntent= new Intent(getBaseContext(),Exchange_Rates.class);
    	menuIntent.putExtra("MTitle",MenuTitle);
		nextactivty();
    }
    
    
    ///MCX Rates Only for BME group Added on 28-06-2017
    private void MCXRates(String MenuTitle)
    {
    	MenuTitle=MenuTitle.split("!")[1];
    	menuIntent= new Intent(getBaseContext(),MCX_Rates.class);
    	menuIntent.putExtra("MTitle",MenuTitle);
		nextactivty();
    }
    
    
   ///News Letter Added on 08-05-2017
    private void NewsLetter(String MenuTitle)
    {
    	MenuTitle=MenuTitle.split("!")[1];
    	menuIntent= new Intent(getBaseContext(),Newsletter.class);
    	menuIntent.putExtra("MTitle",MenuTitle);
		nextactivty();
    }
    
    
    ///Ledger on 14-06-2017 (Update on 07-12-2017)
    private void Ledger(String MenuTitle)
    {
    	String Type=MenuTitle.split("!")[0];
    	MenuTitle=MenuTitle.split("!")[1];
    	
    	menuIntent= new Intent(getBaseContext(),DebtorLedger.class);
    	menuIntent.putExtra("MTitle",MenuTitle);
    	menuIntent.putExtra("Type",Type);
		nextactivty();
    }
    
    
    ///District Song on 23-03-2017
    private void DistrictSong(String MenuTitle)
    {
    	MenuTitle=MenuTitle.split("!")[1];
    	menuIntent= new Intent(getBaseContext(),District_Song.class);
    	menuIntent.putExtra("MTitle",MenuTitle);
		nextactivty();
    }
    
    
    ///Photo Upload for Club Added on 28-07-2018(Group lci321b1)
    private void Photo_UplaodForClub(String MenuTitle)
    {
    	MenuTitle=MenuTitle.split("!")[1];
    	menuIntent= new Intent(getBaseContext(),Club_PhotoUpload1.class);
    	menuIntent.putExtra("MTitle",MenuTitle);
		nextactivty();
    }
    
    
    ///MultiRowOption Added on 02-08-2018
    private void MultiRowOption(String MenuTitle)
    {
    	MenuTitle=MenuTitle.split("!")[1];
    	menuIntent= new Intent(getBaseContext(),MultiRow.class);
    	menuIntent.putExtra("MTitle",MenuTitle);
		nextactivty();
    }
    
    
    ///Family Tree Added on 05-09-2018(only for gadodia and chaparia group)
    private void FamilyTreeOption(String MenuTitle)
    {
    	MenuTitle=MenuTitle.split("!")[1];
    	menuIntent= new Intent(getBaseContext(),FamilyTree.class);
    	menuIntent.putExtra("MTitle",MenuTitle);
		nextactivty();
    }
    
    
    ///Booking Added on 24-05-2017
    private void Booking(String MenuTitle)
    {
    	 db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		  //db.execSQL("DROP TABLE C_" + UserClubnames + "_Item");
         db.execSQL("CREATE TABLE IF NOT EXISTS C_" + ClientID + "_Cate  (M_ID INTEGER PRIMARY KEY,U_ID  INTEGER,Name Text,Remark Text,SyncID INTEGER,SyncDT INTEGER,P_Order INTEGER,Image1 BLOB,Text1 Text,Float1 INTEGER)");
         db.execSQL("CREATE TABLE IF NOT EXISTS C_" + ClientID + "_Item  (M_ID INTEGER PRIMARY KEY,U_ID  INTEGER,Name Text,Cate_ID INTEGER,Remark Text,S_Tag Text,Book_Days   INTEGER,Book_Date   INTEGER,SyncID INTEGER,SyncDT INTEGER,Text1 Text,Float1 INTEGER,P_Order INTEGER,Text2 Text,Text3 Text,Text4 Text,Image1 BLOB)");
         db.execSQL("CREATE TABLE IF NOT EXISTS C_" + ClientID + "_Itemrate(M_ID INTEGER PRIMARY KEY,U_ID INTEGER,iTem_ID INTEGER,Rate INTEGER,wef Text,SyncID INTEGER,SyncDT INTEGER )");
         db.execSQL("CREATE TABLE IF NOT EXISTS C_" + ClientID + "_TimeSlot(M_ID INTEGER PRIMARY KEY,U_ID INTEGER,Item_ID  INTEGER,Slot Text,SyncID INTEGER,SyncDT INTEGER,P_Order INTEGER)");
         db.execSQL("CREATE TABLE IF NOT EXISTS C_" + ClientID + "_Exemtion(M_ID INTEGER PRIMARY KEY,U_ID INTEGER,Item_ID  INTEGER,Slot_ID  INTEGER,DOW  INTEGER,SyncID INTEGER,SyncDT INTEGER)");
         db.close();
         InternetPresent =chkconn.isConnectingToInternet(context);
		 if(InternetPresent==true){
			menuIntent = new Intent(getBaseContext(), BookingCawnporClub.class);
	        menuIntent.putExtra("ValChk","1");
	        nextactivty();
		 }else{
			 Toast.makeText(context, "No Internet", 1).show();
		 }
    }
    
	
	//Add News (05-06-2015)
	private void AddNews()
	{
		menuIntent= new Intent(getBaseContext(),Add_News.class);
		menuIntent.putExtra("addchk","1");
		nextactivty();
	}
	
	
	//Add Global Search (16-12-2016)
	private void GlobalSearch()
	{
		String CCBYear=GetCCBYear();//Get CCB Cuurent Year
		menuIntent= new Intent(getBaseContext(),Global_Search.class);
		menuIntent.putExtra("CCBYear",CCBYear);//CCB Year Added 16-11-2018
		nextactivty();
	}
	
	private void Synchronisation()
	{
		InternetPresent = chkconn.isConnectingToInternet(context);
		if(InternetPresent==true)
			Get_Sync_RData(1,"");
		else
			goback("No Internet Connection !","Please connect with Internet for Synchronization.",1);
	}
	
	private void Add_Group()
	{
		String cLid = "clientid"; 
		String uid = "userid"; 
		String pass = "passwordKey"; 
		String datetm = "DateTme"; 
		String value2 = "name"; 
		String value3 = "cltid"; 
		String value4 = "clubname"; 
		String ValueMenuList = "MenuList"; 
		// Set Shared Pref Saved Values Blank 
		editr = sharedpreferences.edit();
		editr.putString(cLid,"");
		editr.putString(uid,"");
		editr.putString(pass,"");
		editr.putString(datetm,"");
		 ////////////////////////////
		editr.putString(value2,"");
		editr.putString(value3,"");
		editr.putString(value4,"");
		editr.putString(ValueMenuList,"");
		editr.putString("TCount_Tab2","");
		editr.putString("Login","");
		editr.commit();
		/////////////////////////////////////
		Intent intent= new Intent(this,Login.class);
        intent.putExtra("AddGrp", "YES");
        intent.putExtra("AppLogo", AppLogo);
        intent.putExtra("User", "Single");
		startActivity(intent);
	    finish();
	}
	
	// App Settings
	private void App_Settings()
	{
		String HasCCB= Chk_YearSettingCCB();//Check Commitee,council,branch year wise setting enabled or not
		menuIntent= new Intent(getBaseContext(),App_Settings.class);
		menuIntent.putExtra("Clt_ClubName",LogclubName);
		menuIntent.putExtra("HasCCB",HasCCB);
		menuIntent.putExtra("AppLogo", AppLogo);
	    startActivity(menuIntent);
	    finish();
	}
	
	
	// Share App with friends and social networking
	private void App_Sharing()
	{
		 String locApp=objuncm.GET_AppLocation();
		 String Apptitle=objuncm.GET_AppTitle();
		 Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
  		 sharingIntent.setType("text/plain");
  		 sharingIntent.putExtra(Intent.EXTRA_TEXT, "Click on this link to download the app."+locApp);
  		 sharingIntent.putExtra(Intent.EXTRA_SUBJECT, Apptitle+" App(I want to share this app with you)");
  		 startActivity(Intent.createChooser(sharingIntent, "How do you want to share?"));
	}
	
	
	//Get Sync Prerequisite
	public void Get_Sync_RData(final int valchk,final String MTitle)
    {
		progressdial(); 
		networkThread = new Thread() {
		  @Override
			 public void run() {
			  try {
				     int Maxid2 = 0,Minsycid2 = 0,Maxid4 = 0,s_count4 = 0,Minsycid4 = 0,MinSyncDT2=0,MinSyncDT4=0;
				     int MaxidMisc=0,MinSyncidMisc=0,s_countMisc=0,MinSyncDTMisc=0;
				     int MaxidFamily=0,MinSyncidFamily=0,s_countFamily=0,MinSyncDTFamily=0;
				     int MaxidOpinion1=0,MinSyncidOpinion1=0,s_countOpinion1=0,MinSyncDTOpinion1=0;
				     int MaxidOpinion2=0,MinSyncidOpinion2=0,s_countOpinion2=0,MinSyncDTOpinion2=0;
				     
				     db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
				     
				     //Table2/////////////
				     sqlSearch = "select max(m_id),count(m_id),min(Syncid),min(SyncDT) from "+Tab2Name;
					 cursorT = db.rawQuery(sqlSearch, null);
					 while(cursorT.moveToNext())
					 {
				       Maxid2 =cursorT.getInt(0);
					   s_count=cursorT.getInt(1);
					   Minsycid2=cursorT.getInt(2);
					   MinSyncDT2=cursorT.getInt(3);
					   break;
					 }
					 //System.out.println(s_count);
					 cursorT.close();
					 
					 //Table4/////////////
					 sqlSearch = "select max(m_id),count(m_id),min(Syncid),min(SyncDT) from "+Tab4Name;
					 cursorT = db.rawQuery(sqlSearch, null);
					 while(cursorT.moveToNext())
					 {
					   Maxid4=cursorT.getInt(0);
					   s_count4 =cursorT.getInt(1);
					   Minsycid4=cursorT.getInt(2);
					   MinSyncDT4=cursorT.getInt(3);
					   break;
					 }
					 cursorT.close();
					 
					 //Table MISC/////////////
					 sqlSearch = "select max(m_id),count(m_id),min(Syncid),min(SyncDT) from "+TabMiscName;
					 cursorT = db.rawQuery(sqlSearch, null);
					 while(cursorT.moveToNext())
					 {
					   MaxidMisc=cursorT.getInt(0);
					   s_countMisc =cursorT.getInt(1);
					   MinSyncidMisc=cursorT.getInt(2);
					   MinSyncDTMisc=cursorT.getInt(3);
					   break;
					 }
					 cursorT.close();
					 
					 //Table Family/////////////
					 sqlSearch = "select max(m_id),count(m_id),min(Syncid),min(SyncDT) from "+TabFamilyName;
					 cursorT = db.rawQuery(sqlSearch, null);
					 while(cursorT.moveToNext())
					 {
					   MaxidFamily=cursorT.getInt(0);
					   s_countFamily =cursorT.getInt(1);
					   MinSyncidFamily=cursorT.getInt(2);
					   MinSyncDTFamily=cursorT.getInt(3);
					   break;
					 }
					 cursorT.close();
					 
					 //Table Opinion Poll 1/////////////
					 sqlSearch = "select max(m_id),count(m_id),min(Syncid),min(SyncDT) from "+TabOpinion1;
					 cursorT = db.rawQuery(sqlSearch, null);
					 while(cursorT.moveToNext())
					 {
					   MaxidOpinion1=cursorT.getInt(0);
					   s_countOpinion1 =cursorT.getInt(1);
					   MinSyncidOpinion1=cursorT.getInt(2);
					   MinSyncDTOpinion1=cursorT.getInt(3);
					   break;
					 }
					 cursorT.close();
					 
					 //Table Opinion Poll 2 /////////////
					 sqlSearch = "select max(m_id),count(m_id),min(Syncid),min(SyncDT) from "+TabOpinion2;
					 cursorT = db.rawQuery(sqlSearch, null);
					 while(cursorT.moveToNext())
					 {
					   MaxidOpinion2=cursorT.getInt(0);
					   s_countOpinion2 =cursorT.getInt(1);
					   MinSyncidOpinion2=cursorT.getInt(2);
					   MinSyncDTOpinion2=cursorT.getInt(3);
					   break;
					 }
					 cursorT.close();
					 
					 db.close();//Close Db
					 
					 SyncImpData=Maxid2+"#"+s_count+"#"+Minsycid2+"#"+MinSyncDT2+"#"+
					 Maxid4+"#"+s_count4+"#"+Minsycid4+"#"+MinSyncDT4+"#"+
					 MaxidMisc+"#"+s_countMisc+"#"+MinSyncidMisc+"#"+MinSyncDTMisc+"#"+
					 MaxidFamily+"#"+s_countFamily+"#"+MinSyncidFamily+"#"+MinSyncDTFamily+"#"+
					 MaxidOpinion1+"#"+s_countOpinion1+"#"+MinSyncidOpinion1+"#"+MinSyncDTOpinion1+"#"+
					 MaxidOpinion2+"#"+s_countOpinion2+"#"+MinSyncidOpinion2+"#"+MinSyncDTOpinion2;
					 
			       } catch (Exception e) {
		  				e.printStackTrace();
		  		   } 
					 
		        	runOnUiThread(new Runnable()
		            {
		        	  public void run()
		              {
		        		  if(valchk==1){
		        			  GoTo_Sync();// Go To Sync
		        		  }else if(valchk==0){
		        			  if(s_count==0){
				        		  GoTo_Sync();// Go To Sync 
				  			  }else{
				  				  Set_SharedPref_TCountTab2(s_count); // Set Shared Preference Value
				  				  Set_SyncImpData(); // Set SyncImpData in Shared Preference
				  				  GOTO_Directory(MTitle);// Go To Directory
				  			  }   
		        		  }
		                }
		            });
		        Progsdial.dismiss(); 
	  		 }
	  	   };
	  	 networkThread.start();
	}
	
	
	protected void progressdial()
    {
     	Progsdial = new ProgressDialog(this, R.style.MyTheme);
     	Progsdial.setMessage("Please Wait....");
     	Progsdial.setIndeterminate(true);
     	Progsdial.setCancelable(false);
     	Progsdial.getWindow().setGravity(Gravity.DISPLAY_CLIP_VERTICAL);
     	Progsdial.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
     	Progsdial.show();
    } 

	public void nextactivty()
	{
		 menuIntent.putExtra("Clt_Log",Logname);
		 menuIntent.putExtra("Clt_LogID",Logclubid);
		 menuIntent.putExtra("Clt_ClubName",LogclubName);
		 menuIntent.putExtra("UserClubName",ClientID);
		 menuIntent.putExtra("AppLogo", AppLogo);
	     startActivity(menuIntent);
	     finish();
    }
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	 {
	   	 if (keyCode == KeyEvent.KEYCODE_BACK) {
	   		ConfirmAlert();
	   	    return true;
	   	 }
	   	return super.onKeyDown(keyCode, event);
	 }
	
	///Display Confirmation dialog for app exit
	private void ConfirmAlert()
	{
		AlertDialog.Builder AdBuilder = new AlertDialog.Builder(context);
		AdBuilder.setMessage(Html.fromHtml("<font color='#1C1CF0'>Do you want to exit the app ?</font>"));
    	  AdBuilder
            .setPositiveButton("YES",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                    	dialog.dismiss();
                    	goback("Thankyou!","We hope you enjoyed using it !!! We wait for your next use.",0);
                    }
            })
            .setNegativeButton("NO",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                    	dialog.dismiss();
                    }
             });
 
    	AdBuilder.show();
	}
	
	
	///Display Confirmation dialog for Sync
	private void ConfirmAlert_Sync()
	{
		AlertDialog.Builder AdBuilder = new AlertDialog.Builder(context);
		AdBuilder.setMessage(Html.fromHtml("<font color='#E3256B'>Synchronization is required.<br/>Do you want to sync ?</font>"));
    	  AdBuilder
            .setPositiveButton("YES",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                    	dialog.dismiss();
                    	Synchronisation();//Go To Sync
                    }
            })
            .setNegativeButton("NO",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                    	dialog.dismiss();
                    }
             });
 
    	AdBuilder.show();
	}
	
	@SuppressWarnings("deprecation")
	private void goback(String head,String body,final int find){
    	ad.setTitle( Html.fromHtml("<font color='#E3256B'>"+head+"</font>"));
    	ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>"+body+"</font>"));
		ad.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	if(find==1){
            		dialog.dismiss();
            	}else{
            		finish();
            	}
            	
            }
        });
        ad.show();	
    }
	
	
	// This Function is used for Suggestion/complaint menu option
	/*private void  InserttoInternet(){
		 Cursor cursorT;
		    InternetPresent =chkconn.isConnectingToInternet(context);
			 if(InternetPresent==true){
				    db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
					//String sqlqury="select m_id,Text1,Add1,Text2 from "+Tab4Name+" where Rtype = 'T_SUG'";
					String sqlqury="select m_id,Text1,Add1 from "+Tab4Name+" where Rtype='T_SUG'";
					cursorT = db.rawQuery(sqlqury, null);
					tempsize=cursorT.getCount();
					if(tempsize!=0){
			        CodeArr=new String[tempsize];
			        arr1=new String[tempsize];
			        arr2=new String[tempsize];
			        //arr3=new String[tempsize];
			 		 if (cursorT.moveToFirst()) {
			 		  do {
					     CodeArr[i]=cursorT.getString(0);
					     arr1[i]=cursorT.getString(1);
					     arr2[i]=cursorT.getString(2);
					     //arr3[i]=cursorT.getString(3);
					     i++;
			 		     } while (cursorT.moveToNext());
			 		 }
					 cursorT.close();
					 db.close();
					 webInternet();
				 }else{
					 cursorT.close();
					 db.close(); 
				 }
			 }else{
				 countFortxt();
			 }
		}
	
	//recursive function(// This Function is used for Suggestion/complaint menu option)
	private void webInternet(){
		progressdial(); 
        networkThread = new Thread()
        {
         public void run()
         {
          try
          {
       	  final String  mid=CodeArr[j].toString();
       	  String str1=arr1[j].toString();
       	  String str2=arr2[j].toString();
       	  //String str3=arr3[j].toString();
       	  Wepdata=webcall.clubSugg(ClientID, Str_IEMI, Logclubid, str1, str2, "");
            runOnUiThread(new Runnable()
            {
          	  public void run()
          	  {
          		System.out.println(Wepdata);
          		db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
          		String sql="Delete from "+Tab4Name+" where M_id="+mid;
          		System.out.println(sql);
          		db.execSQL(sql);
          		db.close();
          		j=j+1;
          		if(j<tempsize){
          			webInternet();
          		}
          		countFortxt();
              }
            });
            Progsdial.dismiss();
            return;
           }
           catch (Exception localException)
           {
          	 System.out.println(localException.getMessage());
           }
         }
       };
       networkThread.start();		
	 }
	 
	// This Function is used for Suggestion/complaint menu option
	private void countFortxt(){
		 db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
	     sqlSearch = "select count(m_id) from "+Tab4Name+" where Rtype = 'T_SUG'" ;
		 cursorT = db.rawQuery(sqlSearch, null);
		 while(cursorT.moveToNext())
		 {
		   counting =cursorT.getInt(0);
		 }
		 cursorT.close();
		 db.close();
		 if(counting>0){
			StrCount=String.valueOf(counting);
			//Txtcount.setText(StrCount); 
		 }else{
			// Txtcount.setText(""); 
		 }
	 }*/
	
	
	//call function for get values from table 2 in update profile when get internet connection
	private void Sync_UpdateProfile(){
	   InternetPresent =chkconn.isConnectingToInternet(context);
	   String Mob2="",Land1="",Land2="",Email2="",Country="",State="",Pin="",Loc_Res="",Loc_Off="";
	   if(InternetPresent==true){
		 db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
		 String Sqlqry="Select Count(M_id) from "+Tab4Name+" Where Rtype='Pending'"; 
		 int RCount=0;
		 cursorT = db.rawQuery(Sqlqry, null);
		 if (cursorT.moveToFirst()) {
			 RCount= cursorT.getInt(0);
		 }
		 cursorT.close();
		 if(RCount>0){
		   if(UserType.equals("SPOUSE")){
			   Temp_M_S="S";
			   sqlSearch="select S_Name,S_Mob,M_Add1,M_Add2,M_Add3,M_City,S_Email,S_BG,S_DOB_D,S_DOB_M,S_DOB_Y,M_MrgAnn_D,M_MrgAnn_M,M_MrgAnn_Y,M_Pin,C1_FName,C2_FName,S_Pic from "+Tab2Name+" where M_id="+Logclubid;
		   }
		   else{
			   Temp_M_S="M";
			   sqlSearch="select M_Name,M_Mob,M_Add1,M_Add2,M_Add3,M_City,M_Email,M_BG,M_DOB_D,M_DOB_M,M_DOB_Y,M_MrgAnn_D,M_MrgAnn_M,M_MrgAnn_Y,M_Pin,C1_FName,C2_FName,M_Pic,M_SndMob,M_Land1,M_Land2,M_Email1,C3_Name,C3_Email from "+Tab2Name+" where M_id="+Logclubid;
		   }
		   cursorT = db.rawQuery(sqlSearch, null);
		   if(cursorT.moveToFirst())
		   {
	         T2mid =Integer.parseInt(Logclubid);
	         t2name=ChkVal(cursorT.getString(0));
	         t2mob=ChkVal(cursorT.getString(1));
	         t2add1=ChkVal(cursorT.getString(2));
	         t2add2=ChkVal(cursorT.getString(3));
	         t2add3=ChkVal(cursorT.getString(4));
	         t2city=ChkVal(cursorT.getString(5));
	         t2email=ChkVal(cursorT.getString(6));
	         t2bg=ChkVal(cursorT.getString(7));
	         t2day=ChkVal(cursorT.getString(8));
	         t2mon=ChkVal(cursorT.getString(9));
	         t2year=ChkVal(cursorT.getString(10));
	         Ann_D=ChkVal(cursorT.getString(11));
	         Ann_M=ChkVal(cursorT.getString(12));
	         Ann_Y=ChkVal(cursorT.getString(13));
	         Pin=ChkVal(cursorT.getString(14));//Pin
	         Loc_Res=ChkVal(cursorT.getString(15));//Location Residence
	         Loc_Off=ChkVal(cursorT.getString(16));//Location Office
	        
		     t2pic=cursorT.getBlob(17);
		     if(t2pic==null)
			   bPic="0";
		     else
			   bPic="1";
		    
		     if(!UserType.equals("SPOUSE"))
		     {
		    	 Mob2=ChkVal(cursorT.getString(18));// Mobile2
		    	 Land1=ChkVal(cursorT.getString(19));// Landline 1
		    	 Land2=ChkVal(cursorT.getString(20));// Landline 2
		    	 Email2=ChkVal(cursorT.getString(21));// Email2
		    	 Country=ChkVal(cursorT.getString(22));// Country
		    	 State=ChkVal(cursorT.getString(23));// State
		     }
		  }
		  cursorT.close();
		  
		 if(!UserType.equals("SPOUSE")){
		    GetAdditionalData_UpdateProfile();// Get Additional Data of Update Profile When UserType=Member
		 }
		 UProfile_ServerSaved(Mob2,Land1,Land2,Email2,Country,State,Pin,Loc_Res,Loc_Off);//Call a Webservice to Sync Update Profile Mobile to server
    	}else{
    	  cursorT.close();
    	  db.close();
    	}
	  }
	}
	
	
	private void GetAdditionalData_UpdateProfile()
	{
		//Get Additional Data
		String Additional_Data="NODATA",Additional_Data2="NODATA",AllFields="";
		String[] Arr1;
  		
		String Qry="Select Add1,Add2 from "+TabMiscName+" Where Rtype='MASTER'";
		cursorT = db.rawQuery(Qry, null);
		while(cursorT.moveToNext())
		{
		    Additional_Data=cursorT.getString(0); //Additional Data main
		    Additional_Data2=cursorT.getString(1);//Additional Data 2
		      
		    if(Additional_Data==null || Additional_Data=="")
		    {
		    	Additional_Data="NODATA";
		    }
		    if(Additional_Data2==null || Additional_Data2=="")
		    {
		    	Additional_Data2="NODATA";
		    }
			break;
	    }
		cursorT.close();
		///////////////////////////////////
		
		//Set Additional Data 1
		if(!Additional_Data.equals("NODATA") && Additional_Data.contains("#"))
		{
			Arr1=Additional_Data.split("#");
			AllFields=Arr1[1];
		}
		
	    //Set Additional Data 2
		if(!Additional_Data2.equals("NODATA") && Additional_Data2.contains("#"))
		{
			Arr1=Additional_Data2.split("#");
			if(AllFields.length()>0)
			   AllFields=AllFields+","+Arr1[1];
			else
			   AllFields=Arr1[1];
		}
		
		if(AllFields.contains(","))
		{
			Arr1=AllFields.split(",");
			Qry="Select "+AllFields+" from "+TabMiscName+" Where Rtype='DATA' And Memid="+Logclubid;
			cursorT = db.rawQuery(Qry, null);
			if(cursorT.moveToFirst()){
			  for(int i=0;i<Arr1.length;i++)
			  {
				 Full_AdditionalData_UpdatePro=Full_AdditionalData_UpdatePro+ChkVal(cursorT.getString(i))+"^^";
			  }
		    }
			cursorT.close();
		}
			
		if(Full_AdditionalData_UpdatePro.contains("^^") && AllFields.contains(","))
		{
			Full_AdditionalData_UpdatePro=Full_AdditionalData_UpdatePro.substring(0, Full_AdditionalData_UpdatePro.length()-2);
			AllFields=AllFields.replace(",", "^^");
			
			Full_AdditionalData_UpdatePro=AllFields.trim()+"###"+Full_AdditionalData_UpdatePro.trim()+" ";
		}
		else
		{
		    Full_AdditionalData_UpdatePro="";
		}
	}
	
	
	//call function for update data by webservice in update profile when get internet connection
	
	public void UProfile_ServerSaved(final String Mob2,final String Land1,final String Land2,final String Email2,
			final String Country,final String State,final String Pin,final String Loc_Res,final String Loc_Off)
    {
		progressdial();
        networkThread = new Thread()
        {
          public void run()
          {
            try
            {
              String mid=String.valueOf(T2mid);
              //System.out.println(Temp_M_S+"  "+ClientID+"  "+ Str_IEMI+"  "+ mid+"  "+ mid+"  "+ t2name+"  "+ t2add1+"  "+ t2add2+"  "+t2add3+"  "+t2city+"  "+t2email+"  "+t2day+"  "+t2mon+"  "+t2year+"  "+Ann_D+"  "+Ann_M+"  "+Ann_Y+"  "+t2mob+"  "+t2bg+"  "+t2pic+"  "+bPic+"  "+Full_AdditionalData_UpdatePro);
              Webevent=webcall.clubProp(Temp_M_S,ClientID, Str_IEMI, mid, mid, t2name, t2add1, t2add2,t2add3,t2city,Pin,t2email,t2day,t2mon,t2year,Ann_D,Ann_M,Ann_Y,
            		  t2mob,Mob2,Land1,Land2,Email2,Country,State,t2bg,t2pic,bPic,Loc_Res,Loc_Off,Full_AdditionalData_UpdatePro);
              runOnUiThread(new Runnable()
              {
           	    public void run()
           	    {
           		  if(Webevent.equals("Mail Sent"))
           		  {
           		    System.out.println(Webevent);
           		    String deleteqry="Delete from "+Tab4Name+" Where Rtype='Pending'"; 
           		    db.execSQL(deleteqry);
           		    db.close();
           		  }else{
           			System.out.println(Webevent);
           			db.close();
           		  }
                }
              });
             Progsdial.dismiss();
             return;
            }
            catch (Exception localException)
            {
              	// System.out.println("AAAAA  :@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@: ");
           	 System.out.println(localException.getMessage());
            }
          }
        };
        networkThread.start();
	}
	
	
	
	///Chk Year selection setting of Committee,Council,Branch is enabled or not in a group
	 private String Chk_YearSettingCCB()
	 {
		 String flag="N";
		 SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		 String SqlQry="Select M_ID from C_"+ClientID+"_4 Where Rtype='YearWiseCCB'";
		 Cursor cursorT = db.rawQuery(SqlQry, null);
	     if(cursorT.getCount()>0){
	    	 flag="Y";
	     }
	     cursorT.close();
		 db.close();
		 return flag;
	 }
	 
	///Get Year for Commitee,Council,Branch
	 private String GetCCBYear()
	 {
		 String CCBYear="";
			
		 String HasCCB= Chk_YearSettingCCB();//Check Commitee,council,branch year wise setting enabled or not
		
		 if(HasCCB.equals("Y"))
		 {
			  if (sharedpreferences.contains(ClientID+"_CCBYear"))
		      {
				  CCBYear=sharedpreferences.getString(ClientID+"_CCBYear", "");
		      }
			  
			  if(CCBYear.length()==0)
			  {
				 SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
				 String SqlQry="Select Num1 from C_"+ClientID+"_4 Where Rtype='Year' order by num1 Desc";
				 Cursor cursorT = db.rawQuery(SqlQry, null);
				 if (cursorT.moveToFirst()) {
		           CCBYear=cursorT.getString(0);
		         }
			     cursorT.close();
				 db.close();
			  }
		 }
		 return CCBYear;
	 }
	
	
	
	//call function for initialise blank if null is there
	private String ChkVal(String DVal)
	{
		if((DVal==null)||(DVal.equalsIgnoreCase("null"))){
			DVal="";
		}
		return DVal;
	}
	
	
	//This Function is used to Export Database
	 private void exportDB(){
		 try {
			    String pkg=objuncm.GET_PackageName();
		        File sd = Environment.getExternalStorageDirectory();
		        File data = Environment.getDataDirectory();
		        if (sd.canWrite()) {
		        	
		        	/////EXPORT DATA////////////
		            String currentDBPath = "/data/"+pkg+"/databases/MDA_Club";
		            String backupDBPath = "/Download/MDA_Club_BackUP1.db";
		        	File currentDB = new File(data, currentDBPath);
		            File backupDB = new File(sd, backupDBPath);
		        	///////////////////////////////////////////
		        	
		        	/////IMPORT DATA///////////////////////
		        	/*String currentDBPath = "/Download/MDA_Club_BackUP1.db";
		            String backupDBPath = "/data/"+pkg+"/databases/MDA_Club";
		            File currentDB = new File(sd, currentDBPath);
		            File backupDB = new File(data, backupDBPath);*/
		            ///////////////////////////
		            
		            //System.out.println(currentDB.toString()+"     "+backupDB.toString());
		            if (currentDB.exists()) {
		                FileChannel src = new FileInputStream(currentDB).getChannel();
		                FileChannel dst = new FileOutputStream(backupDB).getChannel();
		                System.out.println(src.size());
		                dst.transferFrom(src, 0, src.size());
		                src.close();
		                dst.close();
		                System.out.println("DB Exported!");
		                Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
		            }else{
		            	System.out.println(2);
		            }
		        }
		        System.out.println(1);
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
	 }
	 
	 
	 /// Sync(M-S) FamilyMember Details
	 private void Sync_FamilyMemberDetails()
	 {
	    networkThread = new Thread() {
		@Override
		public void run() {
		  try {
				InternetPresent =chkconn.isConnectingToInternet(context);
			    if(InternetPresent==true)
				{ 
				  sqlSearch="select M_Id,MemId,Name,Relation,Father,Mother,Current_Loc,Mob_1,Mob_2,DOB_D,DOB_M,DOB_Y,EmailId,Age,Education,Work_Profile," +
						    "Gotra,Birth_Time,Birth_Place,Height,Text2,Text3,Text4,Text5,Text6,Text7,Text8,Text9,Text10,Text11,Text12,Text13,Text14,Pic" +
						 	" from "+TabFamilyName+" where MemId="+Logclubid+" and Text1='PEND'"; 
				  db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
				  cursorT = db.rawQuery(sqlSearch, null);
				  System.out.println(cursorT.getCount());
				  if(cursorT.moveToFirst())
				  {
					do {
						 int mid=cursorT.getInt(0);
						 int memid= cursorT.getInt(1);
						 Name=ChkVal(cursorT.getString(2));
				         Relation=ChkVal(cursorT.getString(3));
				         Father=ChkVal(cursorT.getString(4));
				         Mother=ChkVal(cursorT.getString(5));
				         Current_Loc=ChkVal(cursorT.getString(6));
				         Mob_1=ChkVal(cursorT.getString(7));
				         Mob_2=ChkVal(cursorT.getString(8));
				         DOB_D=ChkVal(cursorT.getString(9));
				         DOB_M=ChkVal(cursorT.getString(10));
				         DOB_Y=ChkVal(cursorT.getString(11));
				         EmailId=ChkVal(cursorT.getString(12));
				         Age=ChkVal(cursorT.getString(13));
				         Education=ChkVal(cursorT.getString(14));
				         Work_Profile=ChkVal(cursorT.getString(15));
				         String Gotra=ChkVal(cursorT.getString(16));
				         String Birth_Time=ChkVal(cursorT.getString(17));
				         String Birth_Place=ChkVal(cursorT.getString(18));
				         String Height=ChkVal(cursorT.getString(19));
				         
				         String Gender=ChkVal(cursorT.getString(20)); // Text2 is used for GENDER
				         String ShareWith=ChkVal(cursorT.getString(21)); // Text3 is used for SharewithMatrimony
				         String Desig=ChkVal(cursorT.getString(22)); // Text4 is used for Designation
				         String NativePlace=ChkVal(cursorT.getString(23)); // Text5 is used for NativePlace
				         String Text6=ChkVal(cursorT.getString(24)); // Text6 is used for Manglik
				         String Text7=ChkVal(cursorT.getString(25)); // Text7 is used for AnnualIncome
				         String Text8=ChkVal(cursorT.getString(26)); // Text8 is used for WorkAfterMarriage
				         String Text9=ChkVal(cursorT.getString(27)); // Text9 is used for Diet
				         String Text10=ChkVal(cursorT.getString(28));// Text10 is used for Father's Status
				         String Text11=ChkVal(cursorT.getString(29));// Text11 is used for Mother's Status
				         String Text12=ChkVal(cursorT.getString(30));// Text12 is used for Brothers all Details
				         String Text13=ChkVal(cursorT.getString(31));// Text13 is used for Sisters all Details
				         String Text14=ChkVal(cursorT.getString(32));// Text14 is used for Marriage AdditionalInfo
						         
				         byte[] M_Pic=cursorT.getBlob(33);// Family Member Image
				         String PicB="0";
				         if(M_Pic!=null)
				          PicB="1";
				        
				         ////////////call ewbservice/////////////////
				         Webevent=webcall.familymemUpdate(ClientID, String.valueOf(mid), String.valueOf(memid), Name, Relation, Father, Mother, Current_Loc, Mob_1, Mob_2,
				        		 EmailId, DOB_D, DOB_M, DOB_Y, Education, Work_Profile,Gender,ShareWith,Desig,Gotra,Birth_Time,Birth_Place,NativePlace,
				        		 Height,Text6,Text7,Text8,Text9,Text10,Text11,Text12,Text13,Text14,M_Pic,PicB);
				         //System.out.println(Webevent);
				         if(!Webevent.equalsIgnoreCase("Error")){
				        	String sql= "update "+TabFamilyName+" Set M_id="+Integer.parseInt(Webevent)+",Text1='' where M_id="+mid;
				        	//System.out.println(sql); 
				        	db.execSQL(sql);
				         }
					}while (cursorT.moveToNext());
				 }
				 cursorT.close();
				 db.close();
			   }
					        
			   runOnUiThread(new Runnable()
			   {
				  public void run()
				  {
				     Sync_UpdateProfile(); // Sync(M-S) Update profile
				  }
			   });
		   }
		   catch (Exception e) {
			  System.out.println(e.getMessage()); 
			  e.printStackTrace();
		   }
	    }
	   };
	   networkThread.start();
	}
	
	 
	 //// Check Sync Is Required or Not
	 private String Chk_Sync_Required()
	 {
		 String V1="";
		 String sqlSearch="Select setting_extra_5 from LoginMain Where ClientID='"+ClientID.trim()+"' AND UID='"+UID.trim()+"'"; 
		 db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
		 cursorT = db.rawQuery(sqlSearch, null);
		 if(cursorT.moveToFirst())
		 {
			 V1 =ChkVal(cursorT.getString(0));
		 }
		 cursorT.close();
		 db.close();
		 
		 return V1.trim();
	 }


	 ///// SET Push Notification Registered with FCM  Added on 15-05-2019 //////
	 private void Reg_PushNoti_FCM()
	 {
		 //Get Global Controller Class object (see application tag in AndroidManifest.xml)
		 aController = (Controller) getApplicationContext();

		 try {
			 // Get FCM TokenId
			 String FCMTokenId = FirebaseInstanceId.getInstance().getToken();//Get FCM Token
			 if(FCMTokenId.length()>10) {

				 Log.i("GrpManager", "Device registered: FCMTokenId = " + FCMTokenId);

				 aController.Reg_UnReg_GCM(context, FCMTokenId, "Y");// Register FCMTokenId on our server
			 }
		 }
		 catch(Exception ex)
		 {
			 String tt="";
		 }
	 }
	///////////////////////////////////////

	 

	   
	////Check App Update Required Or Not
	 private void Check_AppStore_Version()
	 {
	    networkThread = new Thread() {
		@Override
		public void run() {
		  try {
			    packageName = objuncm.GET_PackageName();
				Webevent=webcall.Get_PlayStore_version(packageName);
				   
			   runOnUiThread(new Runnable()
			   {
				  public void run()
				  {
					  String currentVersion="";
					  try {
						  currentVersion = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
					   } catch (NameNotFoundException e) {
						 // TODO Auto-generated catch block
						 e.printStackTrace();
					   }
					  String LatestVersion=currentVersion;
              		   
					  if(!Webevent.contains("Error")){
						  LatestVersion=Webevent;//Get Latest Version from App Store  
					  }
					  
              		  if(!currentVersion.equals(LatestVersion))
           		      {
           			     //Toast.makeText(getApplicationContext(),"New update available " + LatestVersion + " from playstore ",Toast.LENGTH_LONG).show();
           			      AlertDialog.Builder AdBuilder = new AlertDialog.Builder(context);
           				  AdBuilder.setMessage(Html.fromHtml("<font color='#E3256B'>New update available. <br/>Do you want to update the app ?</font>"));
           		    	  AdBuilder
           		            .setPositiveButton("Update",new DialogInterface.OnClickListener() {
           		                    public void onClick(DialogInterface dialog,int id) {
           		                    	dialog.dismiss();
           		                    	Intent intent = new Intent(Intent.ACTION_VIEW);
           		                		//Try Google play
           		                		intent.setData(Uri.parse("market://details?id="+packageName));
           		                		startActivity(intent);
           		                    }
           		            })
           		            .setNegativeButton("Remind Later",new DialogInterface.OnClickListener() {
           		                    public void onClick(DialogInterface dialog,int id) {
           		                    	dialog.dismiss();
           		                    }
           		             });
           		 
           		    	AdBuilder.show();
           		      }
				  }
			   });
		   }
		   catch (Exception e) {
			  System.out.println(e.getMessage()); 
			  e.printStackTrace();
		   }
	    }
	   };
	   networkThread.start();
	}
	  
	 
}
