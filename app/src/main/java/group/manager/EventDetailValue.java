package group.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.ParseException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import group.manager.AdapterClasses.Customfamily;

public class EventDetailValue extends Activity {

	TextView txtHead,txtName,txtDesc,txtVenue,txtDate,txtTime;
	ListView LV1;
	LinearLayout LLAYOYT1,LLAYOYT3;
	FrameLayout FLDate;
	RelativeLayout RLAttend;
	Intent menuIntent;
	String sqlSearch="",Log,ClubName,logid,Str_user,StrClubName,Table2Name,Table4Name,StrEvntName,StrEvntDesc,StrMob,
			StrNUm1,Number,StrSql="",Str_chk="",TableNameEvent,StrNum,UserType="",EventMID="",Text8="",StrNum3="",StrEmail="";
	byte[] AppLogo;
	SQLiteDatabase db;
	Cursor cursorT;
	RowEnvt item;
	List<RowEnvt> rowItems;
	Context context=this;
	AlertDialog.Builder AltDialogBldr;
	AlertDialog ad;
	CheckBox chk1,chk2;
	int listcount=0;    //,evntCount;
	ImageView IvListShow,ImgVw_EventAd;
	int imginvert=0;
	CommonClass ComCllObj;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventdescription);

		LV1 = (ListView) findViewById(R.id.LV1);
		txtHead = (TextView) findViewById(R.id.txtHead);
		txtName = (TextView) findViewById(R.id.txtName);
		txtDate = (TextView) findViewById(R.id.txtDate);
		txtDesc = (TextView) findViewById(R.id.txtDesc);
		txtVenue = (TextView) findViewById(R.id.txtVenue);
		txtTime = (TextView) findViewById(R.id.txtTime);

		LLAYOYT1 = (LinearLayout) findViewById(R.id.llvenue);
		FLDate = (FrameLayout) findViewById(R.id.lldate);
		LLAYOYT3 = (LinearLayout) findViewById(R.id.LLList);
		RLAttend = (RelativeLayout) findViewById(R.id.RLAttend);

		IvListShow = (ImageView) findViewById(R.id.imageViewlistshow);
		chk1 = (CheckBox) findViewById(R.id.checkBoxattain1);
		chk2 = (CheckBox) findViewById(R.id.checkBoxattain2);
		ImgVw_EventAd = (ImageView) findViewById(R.id.imgVw_Ad); // ImageView for Event Ad

		menuIntent = getIntent();
		Log = menuIntent.getStringExtra("Clt_Log");
		logid = menuIntent.getStringExtra("Clt_LogID");
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		Str_user = menuIntent.getStringExtra("UserClubName");
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");
		EventMID = menuIntent.getStringExtra("EventMID");
		Str_chk = menuIntent.getStringExtra("Eventschk");

		Table2Name = "C_" + Str_user + "_2";
		Table4Name = "C_" + Str_user + "_4";
		TableNameEvent = "C_" + Str_user + "_Event";

		ComCllObj = new CommonClass();
		AltDialogBldr = new AlertDialog.Builder(context);

		Get_SharedPref_Values();// Get Stored Shared Pref Values of Login

		Set_App_Logo_Title(); // Set App Logo and Title

		Typeface face = Typeface.createFromAsset(getAssets(), "calibri.ttf");
		txtHead.setTypeface(face);

		String EventName = "", EventDesc = "", EventVenue = "", EventDT = "";

		//Open Database
		db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);

		/// Get Event Details
		sqlSearch = "SELECT Text1,Text2,Text3,Date1,Date2,Num1,Text8,Num3,Photo1 from " + Table4Name + " Where Rtype='Event' AND M_Id=" + EventMID;
		cursorT = db.rawQuery(sqlSearch, null);
		if (cursorT.moveToFirst()) {
			EventName = ChkVal(cursorT.getString(0));
			EventDesc = ChkVal(cursorT.getString(1));
			EventVenue = ChkVal(cursorT.getString(2));
			String StrDate1 = ChkVal(cursorT.getString(3));
			String StrDate2 = ChkVal(cursorT.getString(4));
			StrNUm1 = ChkVal(cursorT.getString(5));
			Text8 = ChkVal(cursorT.getString(6));//Check for Read/unread Event
			StrNum3 = ChkVal(cursorT.getString(7));
			byte[] ImgEventAd = cursorT.getBlob(8);//Get Event Ad Photo

			EventName = EventName.replace("&amp;", "&");

			if (Text8.trim().length() == 0)
				Text8 = "0";//for Unread Event

			String[] temp = StrDate1.split(" ");
			String date = temp[0].toString();//Get Only Date

			temp = StrDate2.split(" ");
			String time1 = temp[1].toString();//Get Only Time

			time1 = time1.substring(0, time1.length() - 2);
			try {
				EventDT = Convert24to12(time1, date);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Set Image for Event AD (17-02-2017)
			if (ImgEventAd == null) {
				ImgVw_EventAd.setVisibility(View.GONE);
			} else {
				Bitmap bitmap = BitmapFactory.decodeByteArray(ImgEventAd, 0, ImgEventAd.length);
				ImgVw_EventAd.setVisibility(View.VISIBLE);
				ImgVw_EventAd.setImageBitmap(bitmap);
			}
			////////////////////////////
		}
		cursorT.close();

		txtName.setText(EventName);///Set Event Name/////

		String EDesc = EventDesc.trim();//Set Event Desc

		if ((EDesc == null) || (EDesc.length() == 0)) {
			txtDesc.setVisibility(View.GONE);
		} else {
			EDesc = EDesc.replace("&amp;", "&");
			txtDesc.setVisibility(View.VISIBLE);
			txtDesc.setText(EDesc);
		}

		filloremptyData(EventVenue, LLAYOYT1, txtVenue);//Set Event Venue

		///// Set Event Date Time ////
		if (EventDT == null) {
			FLDate.setVisibility(View.GONE);
		} else if ((EventDT != null) && (EventDT.length() != 0)) {
			FLDate.setVisibility(View.VISIBLE);

			String[] Arr = EventDT.split(" ");
			txtDate.setText(Arr[0]);//Set Event date
			txtTime.setText(Arr[1] + " " + Arr[2].toUpperCase());// Set Event Time
		} else {
			FLDate.setVisibility(View.GONE);
		}
		///////////////////

		///Get Event Directors
		sqlSearch = "SELECT Text1,Text2,Num2,Text3 from " + Table4Name + " Where Num1=" + StrNUm1 + " and Rtype='Dir_Event'";
		cursorT = db.rawQuery(sqlSearch, null);
		listcount = cursorT.getCount();

		//System.out.println("listshow::::::::::: "+listcount);
		rowItems = new ArrayList<RowEnvt>();
		if (cursorT.moveToFirst()) {
			do {
				StrEvntName = ChkVal(cursorT.getString(0));
				StrEvntDesc = ChkVal(cursorT.getString(1));
				StrMob = ChkVal(cursorT.getString(2));
				StrEmail = ChkVal(cursorT.getString(3));
				item = new RowEnvt(StrEvntName, StrEvntDesc, StrMob, StrEmail);
				rowItems.add(item);
			} while (cursorT.moveToNext());
		}
		;
		cursorT.close();
		////////////////////////////////////

		////////////show listview of directors///////////////////////////////
		if (listcount > 0) {
			LLAYOYT3.setVisibility(View.VISIBLE);
		} else {
			LLAYOYT3.setVisibility(View.GONE);
		}

		//// set Event Directors list adapter
		Customfamily Adp = new Customfamily(context, R.layout.familylist, rowItems);
		LV1.setAdapter(Adp);
		/////////////////////////////


		RLAttend.setVisibility(View.GONE);//By Default
		/////////////Display Event Attended Cofirmation Check or Not///////////////
		if (StrNum3.equals("1")) {
			String Condition = "";
			if (UserType.equals("SPOUSE"))
				Condition = " AND (COND4 is NULL OR COND4='ALL' OR LENGTH(COND4)=0 OR COND4 like '%," + logid + ",%')";//Event Condition 14-05-2016 Updated by Tapas
			else
				Condition = " AND (COND3 is NULL OR COND3='ALL' OR LENGTH(COND3)=0 OR COND3 like '%," + logid + ",%')";//Event Condition 14-05-2016 Updated by Tapas

			StrSql = "select M_ID from " + Table4Name + " where M_Id=" + EventMID + Condition;
			cursorT = db.rawQuery(StrSql, null);
			int RCount = cursorT.getCount();
			cursorT.close();

			if (RCount > 0) {
				RLAttend.setVisibility(View.VISIBLE);

				int EventConfirm = Chk_EventConfirmationStatus();//Check the person is Attending Or Not

				if (EventConfirm == 1) {
					chk1.setChecked(true);
				} else if (EventConfirm == 0) {
					chk2.setChecked(true);
				}
			}
		}

		// Update Unread Event
		if (Text8.equals("0")) {
			StrSql = "UPDATE " + Table4Name + " SET Text8='1' Where M_Id=" + EventMID;
			db.execSQL(StrSql);
		}

		db.close();//Close Database

		/// Sync M-S ReadEvent
		Sync_M_S ObjsyncMS = new Sync_M_S(context);
		ObjsyncMS.Sync_ReadNewsEvent("Event");
		/////////////////////////

		///By Default display event directors list opened
		LV1.setVisibility(View.VISIBLE);
		IvListShow.setImageResource(R.drawable.arrow_up);
		imginvert = 1;
		/////////////////////

		IvListShow.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				//Toast.makeText(context, "list.", 1).show();
				if (imginvert == 0) {
					LV1.setVisibility(View.VISIBLE);
					IvListShow.setImageResource(R.drawable.arrow_up);
					imginvert = 1;
				} else if (imginvert == 1) {
					LV1.setVisibility(View.GONE);
					IvListShow.setImageResource(R.drawable.arrow_down);
					imginvert = 0;
				}
			}
		});

		LV1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Number = rowItems.get(position).getEvtdate();
				if ((Number == null) || (Number.length() == 0) || (Number.length() < 5)) {
					AltDialogBldr
							.setMessage("Wrong Mobile Number!!")
							.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.dismiss();
								}
							});
				} else {
					Number = Number.substring(Number.length() - 10, Number.length());
					//System.out.println("cut::  "+Number);
					Number = "0" + Number;
					AltDialogBldr
							.setPositiveButton("CALL", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									ComCllObj.callOnPhone(context, Number);
								}
							})
							.setNegativeButton("SMS", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									ComCllObj.callOnSms(context, Number);
								}
							});
				}
				ad = AltDialogBldr.create();
				ad.show();
			}
		});

		chk1.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				boolean bv2;
				boolean bv1 = chk1.isChecked();
				if (bv1 == false) {
					chk2.setChecked(true);
				} else {
					chk2.setChecked(false);
				}
				bv1 = chk1.isChecked();
				bv2 = chk2.isChecked();

				InsertUpdateEventAttend(bv1);//Insert Update Event Attend or Not Confirmation

				// Sync (M-S) Event Attended or Not
				Sync_M_S ObjsyncMS = new Sync_M_S(context);
				ObjsyncMS.Sync_EventAttend_Or_Not_Confirmation();
			}
		});

		chk2.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				boolean bv1;
				boolean bv2 = chk2.isChecked();
				if (bv2 == false) {
					chk1.setChecked(true);
				} else {
					chk1.setChecked(false);
				}
				bv1 = chk1.isChecked();
				bv2 = chk2.isChecked();

				InsertUpdateEventAttend(bv1);//Insert Update Event Attend or Not Confirmation

				// Sync (M-S) Event Attended or Not
				Sync_M_S ObjsyncMS = new Sync_M_S(context);
				ObjsyncMS.Sync_EventAttend_Or_Not_Confirmation();
			}
		});

	}


	
	private void Get_SharedPref_Values()
	{
		SharedPreferences ShPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

	     if (ShPref.contains("UserType")){
	    	  UserType=ShPref.getString("UserType", "");
	     }
	}
	
	
	//Check Event Confirmation to Attend Or Not i.e 1/0
	private int Chk_EventConfirmationStatus()
	{
		int R=2;
		StrSql="select Num from "+TableNameEvent+" where Rtype='Eve_Acc' and Num1="+StrNUm1;
        cursorT = db.rawQuery(StrSql, null);
    	 if (cursorT.moveToFirst()) {
    	   R=cursorT.getInt(0);
    	 }
        cursorT.close();
        return R;
	}
	
	
	//Insert/Update Event Attended Or Not
 	public void InsertUpdateEventAttend(boolean bv1) {
		int count=0;
		try {
			if(bv1==true){
        		StrNum="1";	
        	}else{
        		StrNum="0";	
        	}
        	db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
        	StrSql="select count(Num) from "+TableNameEvent+"  where Rtype='Eve_Acc' and Num1="+StrNUm1;
        	cursorT=db.rawQuery(StrSql, null);
        	if(cursorT.moveToFirst()){
        		count=cursorT.getInt(0);
        	}
        	if(count==0){
        		StrSql="Insert into "+TableNameEvent+" (Num,Num1,Num2,Sync,Rtype) Values ("+StrNum+","+StrNUm1+","+logid+",1,'Eve_Acc')";
            	db.execSQL(StrSql);	
            	Toast.makeText(context, "Thankyou for confirmation.", 1).show();
        	}else{
        		StrSql="UPDATE "+TableNameEvent+" SET Num="+StrNum+",Sync=1 where Num1="+StrNUm1 +" and Rtype='Eve_Acc'";
            	db.execSQL(StrSql);	
            	Toast.makeText(context, "Thankyou for confirmation.", 1).show();
        	}
        	cursorT.close();
        	db.close();
	    } catch (Exception activityException) {
	    	Toast.makeText(context, "Not get.", 0).show();
	    	//System.out.println(activityException.getMessage());
	    }
    }
	

    public void filloremptyData(String str,LinearLayout ll,TextView txt){
		  if(str==null){
			ll.setVisibility(View.GONE);
		  }else if((str!=null)&&(str.length()!=0)){
			ll.setVisibility(View.VISIBLE);
			txt.setText(str);
		  }else{
			ll.setVisibility(View.GONE);
		  }
	}

	 
	 private void Set_App_Logo_Title()
	 {
		 setTitle(ClubName); // Set Title
		 if(AppLogo==null){    // Set App LOGO
			 getActionBar().setIcon(R.drawable.ic_launcher);
		 }else{
			Bitmap bitmap = BitmapFactory.decodeByteArray(AppLogo,0, AppLogo.length);
			BitmapDrawable icon = new BitmapDrawable(getResources(),bitmap);
			getActionBar().setIcon(icon);
		 }
	 }

	 
	 public static String Convert24to12(String time,String inpdat) throws java.text.ParseException
	 {
	    String convertedTime ="",finalDate;
	    java.util.Date date,myDate;
	    SimpleDateFormat displayFormat,parseFormat,dateFormat,dateCovrt;
	    try {
	        displayFormat = new SimpleDateFormat("hh:mm a");
	        parseFormat = new SimpleDateFormat("HH:mm:ss");
	        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        dateCovrt = new SimpleDateFormat("dd-MM-yyyy");
	        date =parseFormat.parse(time); 
	        myDate = dateFormat.parse(inpdat);
	        convertedTime=displayFormat.format(date);
	        finalDate = dateCovrt.format(myDate);
	        //System.out.println("convertedTime : "+convertedTime);
	        convertedTime=finalDate+" "+convertedTime;
	    } catch (final ParseException e) {
	        e.printStackTrace();
	    }
	    return convertedTime;
	    //Output will be 10:23 PM
	 }
	 
	 
	//call function for initialise blank if null is there
	private String ChkVal(String DVal)
	{
		if((DVal==null)||(DVal.equalsIgnoreCase("null"))){
			DVal="";
		}
		return DVal.trim();
	}


	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			GoBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void GoBack(){
		if(Str_chk.equals("4")){
			finish();
		}else{
			Intent MainBtnIntent= new Intent(getBaseContext(),Events.class);
			MainBtnIntent.putExtra("Clt_Log",Log);
			MainBtnIntent.putExtra("Clt_LogID",logid);
			MainBtnIntent.putExtra("Clt_ClubName",ClubName);
			MainBtnIntent.putExtra("UserClubName",Str_user);
			MainBtnIntent.putExtra("AppLogo", AppLogo);
			MainBtnIntent.putExtra("Eventschk","1");
			startActivity(MainBtnIntent);
			finish();
		}
	}
}
