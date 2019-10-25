package group.manager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

public class UlilitiesList extends Activity{
	AlertDialog ad;
	Intent menuIntent;
	TextView txtclubUtil;
	ListView LVMenuUtil;
	Context context=this;
	byte[] AppLogo;
	SharedPreferences sharedpreferences;
	String Logname,Logclubid,LogclubName,ClientID,UID,UserType,CondChk,AdminMenu="";
	List<RowItem_Menu> rowItems_Menu;
	AlertDialog.Builder alertDialogBuilder3;
	String[]sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.utilities);
		ad=new AlertDialog.Builder(this).create();
		txtclubUtil=(TextView)findViewById(R.id.tvUtilitiesName);
		LVMenuUtil = (ListView) findViewById(R.id.lVUtilitiesMenu);
		
		menuIntent = getIntent(); 
		CondChk =  menuIntent.getStringExtra("CondChk");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		
		Get_SharedPref_Values();// Get Stored Shared Pref Values of Login
		Set_App_Logo_Title(); // Set App Logo and Title
		
		txtclubUtil.setText(Logname);
		
		rowItems_Menu = new ArrayList<RowItem_Menu>();
		if(CondChk.equals("2")){
			if(AdminMenu.length()==0){
				 alertDialogBuilder3 = new AlertDialog.Builder(context);
	       		 alertDialogBuilder3
	       		 .setCancelable(false)
	       		 .setMessage("Wrong Mobile Number!!")
	             .setPositiveButton("Cancel",new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog,int id) {
	                	dialog.dismiss();
	                	BBack();
	                }
	             });
	       		 ad = alertDialogBuilder3.create();
		         ad.show();
			}else{
				AdminMenu=AdminMenu+"^RNOTI";
				//AdminMenu=AdminMenu+"^RNOTI^LOCPOLL!Set POLL Location";//For Testing
				sp=AdminMenu.replace("^", "#").split("#");
			}
		}else if(CondChk.equals("3")){
			String str="JAY1#JAY2#JAY3#JAY4";
			sp=str.split("#");
		}else if(CondChk.equals("4")){
			String str="Corporate Vision#Values#Safety at Retail Outlets#Do and Don'ts for Retail Outlets#TT Unloading#";
			sp=str.split("#");
		}else{
			String str="ShDir#";
			sp=str.split("#");
		}
		
		for(int i=0;i<sp.length;i++)
        {
          String MenuName=sp[i].trim();
          RowItem_Menu item = new RowItem_Menu(MenuName);
          rowItems_Menu.add(item);
        }
		
	    Adapter_Menu adp1 = new Adapter_Menu(context,R.layout.list_item_menu, rowItems_Menu);
	    LVMenuUtil.setAdapter(adp1);
	    
	    LVMenuUtil.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parentAdapter, View view, int position,long id) {
	        	RowItem_Menu Obj=rowItems_Menu.get(position);
	        	String MenuName=Obj.getMenuName();
	        	if(MenuName.equals("ShDir")){
	        		ShowDirectory();
	        	}
	        	else if(MenuName.equals("ADDNEWS")){
	        		AddNews();
	        	}
	        	else if(MenuName.equals("ADDEVENT")){
	        		AddEvent();//Added on 29-03-2017
	        	}
	        	else if(MenuName.equals("MGRP")){
	        		GroupManagement();//For Group Management
	        	}
	        	else if(MenuName.equals("SMSMGT")){
	        		SMS_Sub_Menu();//For SMS Management
	        	}
	        	else if(MenuName.equals("RAPP")){
	        		RunningApps();//For Check Running Apps in Admin
	        	}
	        	else if(MenuName.equals("EVENTACC")){
	        		ShowEventAttend_Admin();
	        	}
	        	else if(MenuName.equals("EVENT_READ")){
	        		ShowEventRead_Admin();
	        	}
	        	else if(MenuName.equals("NEWS_READ")){
	        		ShowNewsRead_Admin();
	        	}
	        	else if(MenuName.equals("RNOTI")){
	        		ResendNoti_SubMenu();// Resend Notification 
	        	}
	        	else if(MenuName.contains("OPPOLL")){
	        		OpinionPollAdmin();//Added on 15-04-2017
	        	}
	        	else if(MenuName.contains("LOCPOLL")){
	        		SetPollLocationAdmin();//Added on 16-05-2018
	        	}
	        	else if(MenuName.equals("JAY1")){
	        		FullAdvertise("2");
	        	}
	        	else if(MenuName.equals("JAY2")){
	        		FullAdvertise("10");
	        	}
	        	else if(MenuName.equals("JAY3")){
	        		FullAdvertise("11");
	        	}
	        	else if(MenuName.equals("JAY4")){
	        		FullAdvertise("12");
	        	}
	        	else if(MenuName.equals("Corporate Vision")){
	        		FullAdvertise("3");
	        	}
	        	else if(MenuName.equals("Values")){
	        		FullAdvertise("4");
	        	}
	        	else if(MenuName.equals("Safety at Retail Outlets")){
	        		FullAdvertise("6");
	        	}
	        	else if(MenuName.equals("TT Unloading")){
	        		FullAdvertise("5");
	        	}
	        	else if(MenuName.equals("Do and Don'ts for Retail Outlets")){
	        		FullAdvertise("7");
	        	}
	        }
	     });
	}
	
	private void FullAdvertise(String tr)
	{
		menuIntent= new Intent(getBaseContext(),FullAdvertisement.class);
		menuIntent.putExtra("Type",tr);
		nextactivty();
	}
	
	//Add News (18-12-2015)
	private void AddNews()
	{
		String SMSEnabled="NO"; 
		
		if(AdminMenu.contains("SMSMGT")){
			SMSEnabled="YES";
		}
		
		menuIntent= new Intent(getBaseContext(),Add_News.class);
		menuIntent.putExtra("addchk","2");
		menuIntent.putExtra("SMSEnabled",SMSEnabled);
		nextactivty();
	}
	
	
	//Add Event (29-03-2017)
	private void AddEvent()
	{
		menuIntent= new Intent(getBaseContext(),Add_Event.class);
		menuIntent.putExtra("addchk","2");
		nextactivty();
	}
		
	//Check Running Apps (11-05-2016)
	private void RunningApps()
	{
		menuIntent= new Intent(getBaseContext(),Running_Apps_Check.class);
		//menuIntent.putExtra("addchk","2");
		nextactivty();
	}	
	
	// Group Management for News/Event (01-06-2016)
	private void GroupManagement()
	{
		menuIntent= new Intent(getBaseContext(),GroupManagement.class);
		nextactivty();
	}	
	
		
	//Show Event Attend/Not Attended/Not Answered
	private void ShowEventAttend_Admin()
	{
		menuIntent= new Intent(getBaseContext(),EventCalendar.class);
		menuIntent.putExtra("Eventschk","2");
		nextactivty();
	}
	
	//Show Read/Unread Event
	private void ShowEventRead_Admin()
	{
		menuIntent= new Intent(getBaseContext(),EventCalendar.class);
		menuIntent.putExtra("Eventschk","3");
		nextactivty();
	}
	
	//Show Read/Unread News
	private void ShowNewsRead_Admin()
	{
		//menuIntent= new Intent(getBaseContext(),AffiliationAPP.class);
		menuIntent= new Intent(getBaseContext(),NewsMain.class);
		menuIntent.putExtra("Count",111);
		menuIntent.putExtra("POstion",0);
		nextactivty();
	}
		
	///Opinion Poll Admin to Check OpinionPoll/Quiz Result
	private void OpinionPollAdmin()
    {
    	menuIntent= new Intent(getBaseContext(),OpinionPoll_MainScreen.class);
    	menuIntent.putExtra("MTitle","Opinion Poll / Quiz Admin");
    	menuIntent.putExtra("ComeFrom","2");//ComeFrom Utilities is 2
		nextactivty();
    }
	
	
	///Set Poll Location Admin to set Poll booth Location
	private void SetPollLocationAdmin()
    {
    	menuIntent= new Intent(getBaseContext(),OpinionPoll_MainScreen.class);
    	menuIntent.putExtra("MTitle","Set Poll Location");
    	menuIntent.putExtra("ComeFrom","3");//ComeFrom Utilities but SomeChange
		nextactivty();
    }
	
	// Show Resend Notification Sub Menu Dialog (02-11-2016)
	private void ResendNoti_SubMenu()
	{
		final Dialog dialog = new Dialog(context);
	    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
		dialog.setContentView(R.layout.smssubmenu);
		//dialog.setCancelable(false);
		dialog.show();
		 
	    Button btnNews=(Button)dialog.findViewById(R.id.btnSendSMS);
	    Button btnEvent=(Button)dialog.findViewById(R.id.btnPendSMS);
	    Button btn3=(Button)dialog.findViewById(R.id.btnBalEnq);

	    btnNews.setText("Resend News Notification");
	    btnEvent.setText("Resend Event Notification");
	    btn3.setVisibility(View.GONE);
	    
	    btnNews.setOnClickListener(new OnClickListener() {    	
    	  @Override
    	  public void onClick(View v) {
    		  dialog.dismiss();
    		  //menuIntent= new Intent(getBaseContext(),AffiliationAPP.class);
    		  menuIntent= new Intent(getBaseContext(),NewsMain.class);
    		  menuIntent.putExtra("Count",222);
    		  menuIntent.putExtra("POstion",0);
    		  nextactivty();
    	   }
       });
			
	    btnEvent.setOnClickListener(new OnClickListener() {    	
          @Override
          public void onClick(View v) {
              dialog.dismiss();
              menuIntent= new Intent(getBaseContext(),EventCalendar.class);
      		  menuIntent.putExtra("Eventschk","4");
      		  nextactivty();
          }
       });
	}
	
	
	
	// Show SMS Management Sub Menu Dialog (14-06-2016)
	private void SMS_Sub_Menu()
	{
		final Dialog dialog = new Dialog(context);
	    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
		dialog.setContentView(R.layout.smssubmenu);
		//dialog.setCancelable(false);
		dialog.show();
		 
	    Button btnSendSMS=(Button)dialog.findViewById(R.id.btnSendSMS);
	    Button btnBalEnq=(Button)dialog.findViewById(R.id.btnBalEnq);
	    Button btnPendSMS=(Button)dialog.findViewById(R.id.btnPendSMS);

	    btnSendSMS.setOnClickListener(new OnClickListener() {    	
    	  @Override
    	  public void onClick(View v) {
    		  dialog.dismiss();
    		  menuIntent= new Intent(getBaseContext(),Send_SMS.class);
      		  nextactivty();
    	   }
       });
			
	    btnBalEnq.setOnClickListener(new OnClickListener() {    	
          @Override
          public void onClick(View v) {
              dialog.dismiss();
              menuIntent= new Intent(getBaseContext(),SMS_Balance.class);
      		  nextactivty();
          }
       });
	    
	    btnPendSMS.setOnClickListener(new OnClickListener() {    	
          @Override
          public void onClick(View v) {
              dialog.dismiss();
              menuIntent= new Intent(getBaseContext(),SMS_Pending.class);
      		  nextactivty();
          }
       });
	    
	}
	
	
    //////////////////////////////////
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
	      if (sharedpreferences.contains("UserType"))
		  {
			  UserType=sharedpreferences.getString("UserType", "");
			  //System.out.println("member: "+UserType);
		  } 
	      if (sharedpreferences.contains("AdminMenu"))
		  {
	    	  AdminMenu=sharedpreferences.getString("AdminMenu", "");
			  //System.out.println(AdminMenu);
		  } 
	}
	
	 //Set App Logo and Title
	 private void Set_App_Logo_Title()
	 {
		 setTitle(LogclubName); // Set Title
		 
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
	 
	 private void ShowDirectory()
	 {
		   menuIntent= new Intent(context,SwipeScreenContact.class);
		   nextactivty();   
	 }
	 
	
	 public void nextactivty(){
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
		   		BBack();
		   	    return true;
		   	 }
		   	return super.onKeyDown(keyCode, event);
		}
		
	 //Back 
	 private void BBack()
	 {
		Intent MainBtnIntent= new Intent(context,MenuPage.class);
   		MainBtnIntent.putExtra("AppLogo", AppLogo);
	    startActivity(MainBtnIntent);
	    finish();
	 }
}
