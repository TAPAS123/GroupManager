package group.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class EventDetailValue extends Activity {
	Customfamily adptfamily;
	ListView lvEvent;
	TextView Txthead,TXtdesc,TXTven,txtDate,txtTime;
	LinearLayout LLAYOYT1,LLAYOYT2,LLAYOYT3;
	RelativeLayout LLAYOYT4;
	Intent menuIntent;
	String sqlSearch="",Log,ClubName,logid,Str_user,StrClubName,Table2Name,Table4Name,StrEvntName,StrEvntDesc,StrMob,
			StrNUm1,Number,StrSql="",Str_chk="",TableNameEvent,StrNum,UserType="",EventMID="",Text8="",StrNum3="",StrEmail="";
	byte[] AppLogo;
	SQLiteDatabase db;
	Cursor cursorT;
	String [] temp;
	RowEnvt item;
	List<RowEnvt> rowItems;
	Context context=this;
	AlertDialog.Builder alertDialogBuilder3;
	AlertDialog ad;
	CheckBox chk1,chk2;
	int listcount=0;    //,evntCount;
	ImageView IvListShow,ImgVw_EventAd;
	int imginvert=0;

	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventdescription); 
        lvEvent = (ListView) findViewById(R.id.listVieweventlist);
        Txthead=(TextView)findViewById(R.id.tvnameofeventslist);
        TXtdesc=(TextView)findViewById(R.id.tvDeseventlist);		
        TXTven=(TextView)findViewById(R.id.tvvenueval);		
        txtDate=(TextView)findViewById(R.id.tvdateval);	
        txtTime=(TextView)findViewById(R.id.txtevtTime);	
        LLAYOYT1=(LinearLayout)findViewById(R.id.llvenue);		
        LLAYOYT2=(LinearLayout)findViewById(R.id.lldate);		
        LLAYOYT3=(LinearLayout)findViewById(R.id.lllist);	
        LLAYOYT4=(RelativeLayout)findViewById(R.id.llattain);	
        IvListShow=(ImageView)findViewById(R.id.imageViewlistshow);	 
        chk1=(CheckBox)findViewById(R.id.checkBoxattain1);	
        chk2=(CheckBox)findViewById(R.id.checkBoxattain2);
        ImgVw_EventAd=(ImageView)findViewById(R.id.imgVw_Ad); // ImageView for Event Ad
        
        
        menuIntent = getIntent(); 
        Log =  menuIntent.getStringExtra("Clt_Log");
		logid =  menuIntent.getStringExtra("Clt_LogID");
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		Str_user =  menuIntent.getStringExtra("UserClubName");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		
		EventMID=menuIntent.getStringExtra("EventMID");
		Str_chk =  menuIntent.getStringExtra("Eventschk");
		
		Table2Name="C_"+Str_user+"_2";
		Table4Name="C_"+Str_user+"_4";
		TableNameEvent="C_"+Str_user+"_Event";
		
		Get_SharedPref_Values();// Get Stored Shared Pref Values of Login
		
		Set_App_Logo_Title(); // Set App Logo and Title
		
		String EventName="",EventDesc="",EventVenue="",EventDT = "";
		
		//Open Database
		db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
		
		/// Get Event Details
		sqlSearch="SELECT Text1,Text2,Text3,Date1,Date2,Num1,Text8,Num3,Photo1 from "+Table4Name+" Where Rtype='Event' AND M_Id="+EventMID;
        cursorT = db.rawQuery(sqlSearch, null);
    	if (cursorT.moveToFirst())
    	{
		    EventName=ChkVal(cursorT.getString(0));
		    EventDesc=ChkVal(cursorT.getString(1));
		    EventVenue=ChkVal(cursorT.getString(2)); 
		    String StrDate1=ChkVal(cursorT.getString(3));
		    String StrDate2=ChkVal(cursorT.getString(4));
		    StrNUm1=ChkVal(cursorT.getString(5));
		    Text8=ChkVal(cursorT.getString(6));//Check for Read/unread Event
		    StrNum3=ChkVal(cursorT.getString(7));
		    byte[] ImgEventAd=cursorT.getBlob(8);//Get Event Ad Photo
		    
		    EventName=EventName.replace("&amp;", "&");
	
		    if(Text8.trim().length()==0)
		    	Text8="0";//for Unread Event
		    
		    temp=StrDate1.split(" ");
		    String date=temp[0].toString();//Get Only Date
		
		    temp=StrDate2.split(" ");
		    String time1=temp[1].toString();//Get Only Time
		    
		    time1 = time1.substring(0, time1.length() - 2);
		    try {
		    	EventDT=Convert24to12(time1,date);
		    } catch (java.text.ParseException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
		    }
		    
		    // Set Image for Event AD (17-02-2017)
			if(ImgEventAd==null)
			{
				ImgVw_EventAd.setVisibility(View.GONE);
			}
			else
			{
			  Bitmap bitmap = BitmapFactory.decodeByteArray(ImgEventAd , 0, ImgEventAd.length);
			  ImgVw_EventAd.setVisibility(View.VISIBLE);
			  ImgVw_EventAd.setImageBitmap(bitmap);
			}
    	}
        cursorT.close();
		
        
        ///Set Event Name/////
        if((EventName==null)||(EventName.length()==0)){
        	Txthead.setText("Event");
        }else{
        	Txthead.setText(EventName);
        }
        
        ///Set Event Desc
		String EDesc=EventDesc.trim();//Set Event Desc
		
        if((EDesc==null)||(EDesc.length()==0)){
        	TXtdesc.setVisibility(View.GONE);
        }else{
        	EDesc=EDesc.replace("&amp;", "&");
        	TXtdesc.setVisibility(View.VISIBLE);
        	TXtdesc.setText(EDesc);
        }
        
        filloremptyData(EventVenue,LLAYOYT1,TXTven);//Set Event Venue	
        
        ///// Set Event Date Time ////
        if(EventDT==null){
        	LLAYOYT2.setVisibility(View.GONE);
		}else if((EventDT!=null)&&(EventDT.length()!=0)){
			  LLAYOYT2.setVisibility(View.VISIBLE);
			  
			  String[] Arr=EventDT.split(" ");
			  txtDate.setText(Arr[0]);//Set Event date
			  txtTime.setText(Arr[1]+" "+Arr[2].toUpperCase());// Set Event Time
		}else{
			  LLAYOYT2.setVisibility(View.GONE);
		}
        ///////////////////
		
		///Get Event Directors
		sqlSearch="SELECT Text1,Text2,Num2,Text3 from "+Table4Name+" Where Num1="+StrNUm1 +" and Rtype='Dir_Event'";
        cursorT = db.rawQuery(sqlSearch, null);
        listcount=cursorT.getCount();
        
        //System.out.println("listshow::::::::::: "+listcount);
    	rowItems = new ArrayList<RowEnvt>();
    	 if (cursorT.moveToFirst()) {
		   do {
			  StrEvntName=ChkVal(cursorT.getString(0));
			  StrEvntDesc=ChkVal(cursorT.getString(1));
			  StrMob=ChkVal(cursorT.getString(2));
			  StrEmail=ChkVal(cursorT.getString(3));
			  item = new RowEnvt(StrEvntName,StrEvntDesc,StrMob,StrEmail);
			  rowItems.add(item); 
    		 } while (cursorT.moveToNext());
    	 }; 
        cursorT.close();
        ////////////////////////////////////
        
        ////////////show listview of directors///////////////////////////////
        if(listcount>0){
        	LLAYOYT3.setVisibility(View.VISIBLE);
        }else{
        	LLAYOYT3.setVisibility(View.GONE);
        }
        
         //// set Event Directors list adapter
      	 adptfamily = new Customfamily(context,R.layout.familylist, rowItems);
         lvEvent.setAdapter(adptfamily);
         /////////////////////////////
        
        
        LLAYOYT4.setVisibility(View.GONE);//By Default
        /////////////Display Event Attended Cofirmation Check or Not///////////////
        if(StrNum3.equals("1"))
        {
           String Condition="";
    	   if(UserType.equals("SPOUSE"))
    		   Condition=" AND (COND4 is NULL OR COND4='ALL' OR LENGTH(COND4)=0 OR COND4 like '%,"+logid+",%')";//Event Condition 14-05-2016 Updated by Tapas
    	   else 
    		   Condition=" AND (COND3 is NULL OR COND3='ALL' OR LENGTH(COND3)=0 OR COND3 like '%,"+logid+",%')";//Event Condition 14-05-2016 Updated by Tapas
 
    	   StrSql="select M_ID from "+Table4Name+" where M_Id="+EventMID+Condition;
           cursorT = db.rawQuery(StrSql, null);
           int RCount=cursorT.getCount();
           cursorT.close();
           
       	   if (RCount>0) {
       		 LLAYOYT4.setVisibility(View.VISIBLE);
       		 
       		 int EventConfirm=Chk_EventConfirmationStatus();//Check the person is Attending Or Not
       		 
      	     if(EventConfirm==1){
      		   chk1.setChecked(true);	
      	     }else if(EventConfirm==0){
      		   chk2.setChecked(true);	
      	     }
       	   }
        }
        
        
        // Update Unread Event
        if(Text8.equals("0"))
        {
          StrSql="UPDATE "+Table4Name+" SET Text8='1' Where M_Id="+EventMID;
          db.execSQL(StrSql);
        }
        
        db.close();//Close Database
        
        /// Sync M-S ReadEvent
		Sync_M_S ObjsyncMS=new Sync_M_S(context);
		ObjsyncMS.Sync_ReadNewsEvent("Event");
		/////////////////////////
   		
        ///By Default display event directors list opened
        lvEvent.setVisibility(View.VISIBLE);
        IvListShow.setImageResource(R.drawable.arrow_up);
        imginvert=1;
        /////////////////////
		
        IvListShow.setOnClickListener(new OnClickListener(){ 
            public void onClick(View arg0){
            	//Toast.makeText(context, "list.", 1).show();
            	if(imginvert==0){
            		lvEvent.setVisibility(View.VISIBLE);
                    IvListShow.setImageResource(R.drawable.arrow_up);
                    imginvert=1;
            	}else if(imginvert==1){
            		 lvEvent.setVisibility(View.GONE);
            		 IvListShow.setImageResource(R.drawable.arrow_down);
                     imginvert=0;
            	}
            }
        });
        
        lvEvent.setOnItemClickListener(new OnItemClickListener() {
    	   public void onItemClick(AdapterView<?> parent, View view, int position, long id){
    		 Number=rowItems.get(position).getEvtdate();
	    	    if((Number==null)||(Number.length()==0)||(Number.length()<5)){
	    	    	alertDialogBuilder3 = new AlertDialog.Builder(context);
	        		 alertDialogBuilder3
	        		 .setMessage("Wrong Mobile Number!!")
		                .setPositiveButton("Cancel",new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog,int id) {
		                    	dialog.dismiss();
		                    }
		                });
	        		ad = alertDialogBuilder3.create();
		            ad.show();	
	        	}else {
	        		alertDialogBuilder3 = new AlertDialog.Builder(context);
	        		Number=Number.substring(Number.length()-10, Number.length());
	        		//System.out.println("cut::  "+Number);
	        		Number= "0"+Number;
		                alertDialogBuilder3
		                .setPositiveButton("Call",new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog,int id) {
		                    	callOnphone(Number);
		                    }
		                })
		                .setNegativeButton("Sms",new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog,int id) {
		                    	callOnSms(Number);
		                    }
		                });
		                ad = alertDialogBuilder3.create();
		                ad.show();
	        	 }
    	      }
         });
        
        chk1.setOnClickListener(new OnClickListener(){ 
            public void onClick(View arg0){
            	boolean bv2;
            	boolean bv1=chk1.isChecked();
            	if(bv1==false){
            		chk2.setChecked(true);
            	}else{
            		chk2.setChecked(false);
            	}
            	bv1=chk1.isChecked();
        		bv2=chk2.isChecked();
        		
            	InsertUpdateEventAttend(bv1);//Insert Update Event Attend or Not Confirmation 
            	
            	// Sync (M-S) Event Attended or Not
            	Sync_M_S ObjsyncMS=new Sync_M_S(context);
            	ObjsyncMS.Sync_EventAttend_Or_Not_Confirmation();
            }
        });
        
        chk2.setOnClickListener(new OnClickListener(){ 
            public void onClick(View arg0){
            	boolean bv1;
            	boolean bv2=chk2.isChecked();
            	if(bv2==false){
            		chk1.setChecked(true);
            	}else{
            		chk1.setChecked(false);
            	}
            	bv1=chk1.isChecked();
        		bv2=chk2.isChecked();
        		
            	InsertUpdateEventAttend(bv1);//Insert Update Event Attend or Not Confirmation 
            	
            	// Sync (M-S) Event Attended or Not
            	Sync_M_S ObjsyncMS=new Sync_M_S(context);
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
	
	public void callOnphone(String MobCall) {
		try {
	        Intent callIntent = new Intent(Intent.ACTION_DIAL);
	        callIntent.setData(Uri.parse("tel:"+MobCall));
	        startActivity(callIntent);
	    } catch (ActivityNotFoundException activityException) {
	    	Toast.makeText(getBaseContext(), "Call failed", 0).show();
	    	//System.out.println("Call failed");
	    }
    }
 
    public void callOnSms(String MobCall) {
		try {
			String uri= "smsto:"+MobCall;
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
            intent.putExtra("compose_mode", true);
            startActivity(intent);
	       } catch (ActivityNotFoundException activityException) {
	    	Toast.makeText(getBaseContext(), "Sms failed", 0).show();
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
	 
    
    
    public boolean onKeyDown(int keyCode, KeyEvent event) 
	 {
	   	 if (keyCode == KeyEvent.KEYCODE_BACK) {
	   		backs();
	   	    return true;
	   	 }
	   	return super.onKeyDown(keyCode, event);
	 }
	
	 public void backs(){
		 if(Str_chk.equals("4")){
			 finish(); 
		 }else{
			 Intent MainBtnIntent= new Intent(getBaseContext(),EventCalendar.class);	
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
}
