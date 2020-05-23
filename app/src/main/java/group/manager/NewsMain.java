package group.manager;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import group.manager.AdapterClasses.Adapter_News;
import group.manager.AdapterClasses.Adapter_NewsMain;

public class NewsMain extends Activity {

	TextView txtHead;
	ListView LV1;
	String Table2Name,Table4Name,Log,ClubName,logid,Str_user,UserType="";
	int StrCount,post;
	byte[] AppLogo;
	Intent menuIntent;
	List<RowEnvt> rowItems;
	Context context=this;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newsmain);

		txtHead = (TextView) findViewById(R.id.txtHead);
		LV1 = (ListView) findViewById(R.id.LV1);

		menuIntent = getIntent();
		Log = menuIntent.getStringExtra("Clt_Log");
		logid = menuIntent.getStringExtra("Clt_LogID");
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		Str_user = menuIntent.getStringExtra("UserClubName");
		StrCount = menuIntent.getIntExtra("Count", StrCount);
		post = menuIntent.getIntExtra("POstion", post);
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");

		Table2Name = "C_" + Str_user + "_2";
		Table4Name = "C_" + Str_user + "_4";

		Set_App_Logo_Title(); // Set App Logo and Title

		Get_SharedPref_Values();// Get Stored Shared Pref Values of Login

		Typeface face=Typeface.createFromAsset(getAssets(), "calibri.ttf");
		txtHead.setTypeface(face);

		FillListData();

		//ListView Click Event
		LV1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String MID = rowItems.get(position).getEvtdate();

				if (StrCount == 111 || StrCount == 222) {
					//Read/UnRead News for Admin

					String V4 = rowItems.get(position).getEvtName();//Get News Title
					String V3 = rowItems.get(position).getEvtDesc();//Get News date

					if (StrCount == 111)
						menuIntent = new Intent(context, News_EventReadUnread.class);
					else if (StrCount == 222)
						menuIntent = new Intent(context, Resend_Notification.class);

					menuIntent.putExtra("PType", "News");
					menuIntent.putExtra("MID", MID);
					menuIntent.putExtra("VAL2", "");//Event/News (Venue)
					menuIntent.putExtra("VAL3", V3);//Event/News (Date)
					menuIntent.putExtra("VAL4", V4);//Event/News (Title)
				} else {
					menuIntent = new Intent(context, NewsMain2.class);
					//menuIntent.putExtra("CHKg","@@@");
					menuIntent.putExtra("MID", MID);
					menuIntent.putExtra("Count", StrCount);
					menuIntent.putExtra("Positn", position);
				}
				menuIntent.putExtra("Clt_Log", Log);
				menuIntent.putExtra("Clt_LogID", logid);
				menuIntent.putExtra("Clt_ClubName", ClubName);
				menuIntent.putExtra("UserClubName", Str_user);
				menuIntent.putExtra("AppLogo", AppLogo);
				startActivity(menuIntent);
				finish();
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
	
	 
	 private void Get_SharedPref_Values()
	 {
		SharedPreferences ShPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

	     if (ShPref.contains("UserType")){
	    	  UserType=ShPref.getString("UserType", "");
	     }
	 }
	
	 
	////Fill List/// 
	private void FillListData()
	{
		String Qry="";
		if(StrCount==999999 || StrCount==111 || StrCount==222)
		{
			txtHead.setText("News");
			String Cond="";//News Condition 12-05-2016 Updated by Tapas
			if(StrCount==999999)
			{
				if(UserType.equals("SPOUSE"))
					Cond=" AND (COND2 is NULL OR COND2='ALL' OR LENGTH(COND2)=0 OR COND2 like '%,"+logid+",%')";//News Condition 12-05-2016 Updated by Tapas
				else
					Cond=" AND (COND1 is NULL OR COND1='ALL' OR LENGTH(COND1)=0 OR COND1 like '%,"+logid+",%')";//News Condition 12-05-2016 Updated by Tapas
			}
			Qry="SELECT Text2,Text1,M_ID,Add1 from "+Table4Name+" Where Rtype='News' "+Cond+" Order By Date1_1 DESC";
		}
		else if(StrCount==22)
		{
			txtHead.setText("Pending News");
			Qry="SELECT Text2,Text1,M_ID,Add1 from "+Table4Name+" Where Rtype='Add_News' Order By m_id DESC";
		}

		SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
		  
	    Cursor cursorT = db.rawQuery(Qry, null);
	    int RCount=cursorT.getCount();
	    
	    if(RCount==0){
        	if(StrCount==999999 || StrCount==111){
        		txtHead.setText("No News");
        	}else{
        		txtHead.setText("No Record");	
        	}
	    }else{
        	rowItems = new ArrayList<RowEnvt>();
        	RowEnvt item;
        	if (cursorT.moveToFirst()) {
  			   do {
  				   String NewsTitle=ChkVal(cursorT.getString(0));
  				   String NewsDate =ChkVal(cursorT.getString(1));
  				   String mid =ChkVal(cursorT.getString(2));
				   String Desc =  ChkVal(cursorT.getString(3));
				   if(Desc.length()>80) {
					   Desc = Desc.substring(0, 80);
					   Desc=Desc+"...";
				   }

	  			   item = new RowEnvt(NewsTitle,NewsDate,mid,Desc);
        	       rowItems.add(item); 
  	    		 } while (cursorT.moveToNext());
  	    	}
			Adapter_News Adp1 = new Adapter_News(context,R.layout.item_news_list, rowItems,"");
            LV1.setAdapter(Adp1); 
            LV1.setSelection(post);
	     }
	     cursorT.close();
	     db.close();  
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


	private void GoBack()
	{
		Intent intent;
		if(StrCount==22)
		{
			intent= new Intent(getBaseContext(),Add_News.class);
			intent.putExtra("Clt_Log", Log);
			intent.putExtra("Clt_LogID", logid);
			intent.putExtra("Clt_ClubName", ClubName);
			intent.putExtra("UserClubName", Str_user);
			intent.putExtra("AppLogo", AppLogo);
			intent.putExtra("addchk", "2");
			startActivity(intent);
		}
		else{
			intent= new Intent(getBaseContext(),MenuPage.class);
			intent.putExtra("AppLogo", AppLogo);
			startActivity(intent);
		}
		finish();
	}

}
