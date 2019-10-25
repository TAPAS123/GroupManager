package group.manager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsMain2 extends Activity{
	SQLiteDatabase db;
	Cursor cursorT;
	String sqlSearch,Table2Name,Table4Name,Log,ClubName,logid,Str_user,NewsDate,NewsTitle,NewsDesc,StrCont,STRM_ID;
	Intent menuIntent,MainBtnIntent;
	int MaxNum1,StrCount,Postn;
	Context context=this;
	TextView txtHead,txtDate;
	AlertDialog ad;
	ImageView ImgVw_Ad;
	WebView webtxt;
	byte[] AppLogo;
	private static final Pattern urlPattern = Pattern.compile(
			"(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
					+ "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
					+ "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsmain2);
        
        txtHead=(TextView)findViewById(R.id.txthead);
        txtDate=(TextView)findViewById(R.id.txtDate);
        webtxt=(WebView)findViewById(R.id.textContent);
        ImgVw_Ad=(ImageView)findViewById(R.id.imgVw_Ad); // ImageView for Ad for News only
        
        menuIntent = getIntent(); 
		Log =  menuIntent.getStringExtra("Clt_Log");
		logid =  menuIntent.getStringExtra("Clt_LogID");
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		Str_user =  menuIntent.getStringExtra("UserClubName");
		STRM_ID =  menuIntent.getStringExtra("MID");
		StrCount =  menuIntent.getIntExtra("Count", StrCount);
		Postn =  menuIntent.getIntExtra("Positn", Postn);
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		
        Table2Name="C_"+Str_user+"_2";
		Table4Name="C_"+Str_user+"_4";
		Set_App_Logo_Title(); // Set App Logo and Title
		
		ad=new AlertDialog.Builder(this).create();

	    sqlSearch="Select Text1,Text2,Add1,Add2 from "+Table4Name+" Where m_id= "+STRM_ID;
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
		  MainBtnIntent= new Intent(getBaseContext(),NewsMain.class);	
		  MainBtnIntent.putExtra("POstion", 0);
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
	        	 txtHead.setText("No Records");
	        	 txtDate.setText("");
             	 AlertDisplay("Connection problem","Internet Connection is not proper\n please open app again");
	        }else{
        	     if (cursorT.moveToFirst()) {
        	    	  NewsDate=cursorT.getString(0);
        	    	  NewsTitle=cursorT.getString(1);
       				  NewsDesc=cursorT.getString(2);
       				  StrCont=cursorT.getString(3); 
       	    	 }
	        	 
        	     txtHead.setText(NewsTitle);///Set News Title
        	     txtDate.setText(NewsDate);// Set News date
        	     
        	     NewsDesc=urlify(NewsDesc);//Detect Url a make url a hyperlink clickable (Added on 30-01-2018)
             	
             	 webtxt.setVisibility(View.VISIBLE);
             	 webtxt.setBackgroundColor(Color.parseColor("#00ffffff"));
             	 //webtxt.setBackgroundColor(Color.TRANSPARENT);
             	 
             	 String text = "<html><body><p align=\"justify\">";
             	 text+= NewsDesc.replace("\n", "<br/>");
             	 text+= "</p></body></html>";
             	 webtxt.loadDataWithBaseURL(null, text, "text/html", "UTF-8", null);
             	 
    			 Display_Image_Ad();// Display Ad for News
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
  	
  	
  	private String urlify(String text) {
		String UUrl="";
		Matcher matcher = urlPattern.matcher(text);
		while (matcher.find()) {
			int matchStart = matcher.start(1);
			int matchEnd = matcher.end();
			// now you have the offsets of a URL match
			UUrl=text.substring(matchStart,matchEnd);
			String tt="";
		}
		if(UUrl.length()>1){
			  String NUrl="<a href='"+UUrl+"' target='_blank'> "+ UUrl + "</a>";
		  	  text=text.replace(UUrl,NUrl);
		}
		return text;
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
