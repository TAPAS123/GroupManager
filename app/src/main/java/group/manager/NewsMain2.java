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
import android.graphics.Typeface;
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
	String Table2Name,Table4Name,Log,ClubName,logid,Str_user,STRM_ID;
	Intent menuIntent,MainBtnIntent;
	int StrCount,Postn;
	Context context=this;
	TextView txtHead,txtDatenews,txtNTitle;
	AlertDialog ad;
	ImageView ImgVw_Ad;
	WebView webtxt;
	byte[] AppLogo;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsmain2);
        
        txtHead=(TextView)findViewById(R.id.txthead);
		txtDatenews=(TextView)findViewById(R.id.txtDatenews);
		txtNTitle = (TextView) findViewById(R.id.txtNTitle);
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
		ad=new AlertDialog.Builder(this).create();

		Set_App_Logo_Title(); // Set App Logo and Title

		Typeface face=Typeface.createFromAsset(getAssets(), "calibri.ttf");
		txtHead.setTypeface(face);

		FillData();// Fill Details
		
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

	
	private void FillData()
	{
		String StrDate = "", Desc = "", Title = "";
		db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		String Qry = "Select Text1,Text2,Add1 from " + Table4Name + " Where m_id= " + STRM_ID;
		cursorT = db.rawQuery(Qry, null);
		if (cursorT.moveToFirst()) {
			StrDate = ChkVal(cursorT.getString(0));
			Title = ChkVal(cursorT.getString(1));
			Desc = ChkVal(cursorT.getString(2));
		}
		cursorT.close();

		webtxt.setVisibility(View.VISIBLE);
		webtxt.setBackgroundColor(Color.parseColor("#00ffffff"));
		//webtxt.setBackgroundColor(Color.TRANSPARENT);

		txtDatenews.setText(StrDate);
		txtNTitle.setText(Html.fromHtml("<u>" + Title + "</u>"));

		String NewsDesc = new CommonClass().Url_hyperlink(Desc);//Detect Url a make url a hyperlink clickable (Added on 30-01-2018)

		String text = "<html><body><p align=\"justify\">";
		text += NewsDesc.replace("\n", "<br/>");
		text += "</p></body></html>";
		webtxt.loadDataWithBaseURL(null, text, "text/html", "UTF-8", null);

		Display_Image_Ad();// Display Ad for News

		db.close();//Close Connection
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


	//call function for initialise blank if null is there
	private String ChkVal(String DVal)
	{
		if((DVal==null)||(DVal.equalsIgnoreCase("null"))){
			DVal="";
		}
		return DVal.trim();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			GoBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void GoBack() {
		if (StrCount == 88888) {
			finish();
		} else {
			MainBtnIntent = new Intent(getBaseContext(), NewsMain.class);
			MainBtnIntent.putExtra("POstion", 0);
			MainBtnIntent.putExtra("Count", StrCount);
			MainBtnIntent.putExtra("Clt_Log", Log);
			MainBtnIntent.putExtra("Clt_LogID", logid);
			MainBtnIntent.putExtra("Clt_ClubName", ClubName);
			MainBtnIntent.putExtra("UserClubName", Str_user);
			MainBtnIntent.putExtra("AppLogo", AppLogo);
			startActivity(MainBtnIntent);
			finish();
		}
	}

}
