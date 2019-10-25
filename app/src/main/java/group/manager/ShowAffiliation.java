package group.manager;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowAffiliation extends Activity{
	SQLiteDatabase db;
	Cursor cursorT;
	String sqlSearch,Table2Name,Table4Name,Log,ClubName,logid,Str_user,StrClubName,StrCity,StrAdd,StrCont,STRM_ID,Str_chk,StrEmail;
	Intent menuIntent,MainBtnIntent;
	int MaxNum1,StrCount,Postn;
	Context context=this;
	TextView txtHead,Txtcity,txtaddress,Txtphone,TxtContact,Txtgrp,Txtadd;
	Button btndelete;
	AlertDialog ad;
	ImageView ImgVw_Ad;
	WebView webtxt;
	byte[] AppLogo;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showaffiliation);
        Txtgrp=(TextView)findViewById(R.id.textView1AFF);
        Txtadd=(TextView)findViewById(R.id.textView2AFF);
        txtHead=(TextView)findViewById(R.id.tvclubAFF);
        Txtcity=(TextView)findViewById(R.id.tvcityAff);
        txtaddress=(TextView)findViewById(R.id.tvAddressAff);
        Txtphone=(TextView)findViewById(R.id.tvContactAFF);
        webtxt=(WebView)findViewById(R.id.textContent);
        TxtContact=(TextView)findViewById(R.id.textView3AFF);
        btndelete=(Button)findViewById(R.id.btnDELETe);
        ImgVw_Ad=(ImageView)findViewById(R.id.imgVw_Ad); // ImageView for Ad for News only
        
        ad=new AlertDialog.Builder(this).create();
        menuIntent = getIntent(); 
		Log =  menuIntent.getStringExtra("Clt_Log");
		logid =  menuIntent.getStringExtra("Clt_LogID");
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		Str_user =  menuIntent.getStringExtra("UserClubName");
		STRM_ID =  menuIntent.getStringExtra("Pwd");
		Str_chk =  menuIntent.getStringExtra("CHKg");
		StrCount =  menuIntent.getIntExtra("Count", StrCount);
		Postn =  menuIntent.getIntExtra("Positn", Postn);
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
        Table2Name="C_"+Str_user+"_2";
		Table4Name="C_"+Str_user+"_4";
		Set_App_Logo_Title(); // Set App Logo and Title
		
		if(Str_chk.contains("1")){
			Txtgrp.setText("Name of club");
			Txtadd.setText("Address");
			TxtContact.setText("Contact No.");
	        btndelete.setVisibility(View.GONE);
	        sqlSearch="Select Text1,Text2,Add1,Add2 from "+Table4Name+" Where m_id= "+STRM_ID;
		}else if(Str_chk.equals("@@@")){
	        btndelete.setVisibility(View.GONE);
	        sqlSearch="Select Text1,Text2,Add1,Add2 from "+Table4Name+" Where m_id= "+STRM_ID;
		}else{
			TxtContact.setText("Email");
			Txtgrp.setText("Title");
			Txtadd.setText("Description");
	        btndelete.setVisibility(View.VISIBLE);
	        sqlSearch="Select Text1,Add1,Text2,Add2 from "+Table4Name+" Where m_id= "+STRM_ID;
		}
		GetDetails(sqlSearch);// Get Details
		
		// For News [Tapas (18-05-2016)]
		if(StrCount==999999 || StrCount==88888)
		{
			UpdateUnreadNews();//Update News after Read
			
			/// Sync M-S ReadNews
			Sync_M_S ObjsyncMS=new Sync_M_S(context);
			ObjsyncMS.Sync_ReadNewsEvent("News");
			/////////////////////////
		}
		/////////////////////////////////
		
        btndelete.setOnClickListener(new OnClickListener(){ 
            public void onClick(View arg0){
           	// iTax("CM");
            	db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
            	sqlSearch="Delete from "+Table4Name+" Where m_id= "+STRM_ID;
            	db.execSQL(sqlSearch);
            	db.close();
            	ad.setTitle( Html.fromHtml("<font color='#E3256B'>Result</font>"));
			    ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>Suggestion/Complain deleted.</font>"));
				ad.setButton("OK", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) {
			            	back();
			          }
			        });
			    ad.show();	
       	   }
         }); 
	}
	
	private void Set_App_Logo_Title()
	 {
		 setTitle(ClubName); // Set Title
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
	
	
	/// Update Unread News [ Tapas (18-05-2016)]
	private void UpdateUnreadNews()
	{
		db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
		String Qry="Select Num2 From "+Table4Name+" Where M_Id="+STRM_ID;
		cursorT = db.rawQuery(Qry, null);
		int Num2=6;
        if (cursorT.moveToFirst()) {
        	Num2=cursorT.getInt(0);
   	    }
        cursorT.close();
        
        if(Num2==0){
        	Qry="UPDATE "+Table4Name+" SET Num2=1 where M_Id="+STRM_ID;
            db.execSQL(Qry);
        }
        db.close();
	}
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	 {
	   	 if (keyCode == KeyEvent.KEYCODE_BACK) {
	   		back();
	   	   return true;
	   	 }
	   	return super.onKeyDown(keyCode, event);
	 }
	
    private void back(){
		if(StrCount==88888){
			finish();
		}
		else
		{
		  if(Str_chk.contains("1")){
			 MainBtnIntent= new Intent(getBaseContext(),AffiliationAPP.class);
			 MainBtnIntent.putExtra("POstion", Postn);
		  }else if(Str_chk.equals("@@@")){
			 MainBtnIntent= new Intent(getBaseContext(),AffiliationAPP.class);	
			 MainBtnIntent.putExtra("POstion", 0);
		  } else{
			 MainBtnIntent= new Intent(getBaseContext(),ListSuggestion.class);	
		  }
		  MainBtnIntent.putExtra("Count", StrCount);
		  MainBtnIntent.putExtra("Clt_Log",Log);
		  MainBtnIntent.putExtra("Clt_LogID",logid);
		  MainBtnIntent.putExtra("Clt_ClubName",ClubName);
		  MainBtnIntent.putExtra("UserClubName",Str_user);
		  MainBtnIntent.putExtra("AppLogo", AppLogo);
	      startActivity(MainBtnIntent);
	      finish();
	   }	  
	}
	
	private void GetDetails(String sqlS)
	{
		 db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
	     cursorT = db.rawQuery(sqlS, null);
	     int RCount=cursorT.getCount();
	        if(RCount==0){
	        	 Txtcity.setText("No Records");
	        	 txtHead.setVisibility(View.GONE);
             	 txtaddress.setVisibility(View.GONE);
             	 Txtphone.setVisibility(View.GONE);
             	 TxtContact.setVisibility(View.GONE);
             	 if(Str_chk.contains("@@@"))
             	 {
             	   Txtgrp.setText("");
             	   Txtadd.setText("");
             	   AlertDisplay("Connection problem","Internet Connection is not proper\n please open app again");
             	 }
	        }else{
        	     if (cursorT.moveToFirst()) {
       			   do {
       				   StrClubName=cursorT.getString(0);
       				   StrCity=cursorT.getString(1);
       				   StrEmail=cursorT.getString(2);
       				   StrCont=cursorT.getString(3); 
       	    		 } while (cursorT.moveToNext());
       	    	 }
	        	 
	        	 if(Str_chk.contains("1")){
	        		 Txtcity.setText(StrCity);
	             	 txtaddress.setText(StrEmail);
	             	 Txtphone.setText(StrCont);
	             	 txtHead.setText(StrClubName);
	        	 }else if(Str_chk.contains("@@@")){
	             	 Txtcity.setText("News");
	             	 txtHead.setVisibility(View.GONE);
	             	 txtaddress.setVisibility(View.GONE);
	             	 Txtphone.setVisibility(View.GONE);
	             	 TxtContact.setVisibility(View.GONE);
	             	 Txtgrp.setTextColor(Color.BLACK);
	             	 webtxt.setVisibility(View.VISIBLE);
	             	 webtxt.setBackgroundColor(Color.parseColor("#00ffffff"));
	             	 //webtxt.setBackgroundColor(Color.TRANSPARENT);
	             	 String text = "<html><body><p align=\"justify\">";
	             	 text+= StrEmail.replace("\n", "<br/>");
	             	 text+= "</p></body></html>";
	             	 webtxt.loadDataWithBaseURL(null, text, "text/html", "UTF-8", null);
	             	 //TxtContact.setText(StrEmail);
	             	 // TxtContact.setText(Html.fromHtml("<p align='justify'>"+StrEmail+"</p>"));
	    			 Txtgrp.setText(StrClubName);
	    			 //Txtadd.setText(StrCity);
	    			 Txtadd.setText(Html.fromHtml("<u>"+StrCity+"</u>"));
	    			 Display_Image_Ad();// Display Ad for News
	        	 }else{
	        		 txtHead.setText(StrClubName);
	             	 Txtcity.setText("Detail of Suggestion/Complain");
	             	 txtaddress.setText(StrCity);
	             	 Txtphone.setText(StrEmail);
	        	 }
	        }
	        cursorT.close();
	        db.close(); 
	  }
	
	// Display Ad in the ImageView for News
    private void Display_Image_Ad()
	{
    	//Rtype=Ad3 for News
    	String sql ="Select Photo1 from "+Table4Name+" WHERE Rtype='Ad3'";
    	cursorT = db.rawQuery(sql, null);
		byte[] ImgAd=null;
    	while(cursorT.moveToNext())
		{
    	   ImgAd=cursorT.getBlob(0);
		   break;
		}
		cursorT.close();
		
		// Set Image for AD
		if(ImgAd==null)
		{
		  ImgVw_Ad.setVisibility(View.GONE);
		}
		else
		{
		  Bitmap bitmap = BitmapFactory.decodeByteArray(ImgAd , 0, ImgAd.length);
		  ImgVw_Ad.setVisibility(View.VISIBLE);
		  ImgVw_Ad.setImageBitmap(bitmap);
		}
	}
    
    
  //call function for initialise blank if null is there
  	private String ChkVal(String DVal)
  	{
  		if((DVal==null)||(DVal.equalsIgnoreCase("null"))){
  			DVal="";
  		}
  		return DVal.trim();
  	}
  	
  	
  	
  	 private void AlertDisplay(String head,String body){
	    	ad.setTitle( Html.fromHtml("<font color='#E3256B'>"+head+"</font>"));
	    	ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>"+body+"</font>"));
			ad.setButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	dialog.dismiss();
	            	back();
	            }
	        });
	        ad.show();	
	}
	
}

