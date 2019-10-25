package group.manager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Spinner;

public class Timeslot  extends Activity{
	Intent menuIntent;
	String Table2Name,Table4Name,Log,ClubName,logid,Str_user,Tablecate,Tableitem,Tableitemrate,UID,TableSlot,strqry,MainUID,TableExempt,
	StrMonth="0",StrYear="";
	ListView LVMenu;
	Chkconnection chkconn;
	 WebServiceCall webcall;
    final Context context = this;
    Cursor cursorT;
    SQLiteDatabase db;
    List<RowItem> rowItems_Menu;
    RowItem item;
    byte[] btimg=null;
    Spinner spmonth,spyear;
    String[] Sp_year={"2015","2016","2017","2018","2019","2020","2021","2022","2023","2024","2025","2026","2027"},
    		 month_val={"0","1","2","3","4","5","6","7","8","9","10","11","12"},
    		 Sp_Month = {"Select","January","Febuary","March","April","May","June","July","Augest","September","October","November","December"};
    ArrayAdapter<String> adapter;
    Calendar calendar;
    int year,month,cy;
    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookingcawnporclub);
        LVMenu = (ListView) findViewById(R.id.lVMenus);
        LinearLayout LLayoutMonth=(LinearLayout)findViewById(R.id.llaymonth);
        LLayoutMonth.setVisibility(View.VISIBLE);
        spmonth=(Spinner)findViewById(R.id.spinnermonth);
        spyear=(Spinner)findViewById(R.id.spinneryear);
        
        menuIntent = getIntent(); 
        Str_user =  menuIntent.getStringExtra("UserClubName");
		Log =  menuIntent.getStringExtra("Clt_Log");
		logid =  menuIntent.getStringExtra("Clt_LogID");
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		UID =  menuIntent.getStringExtra("UID");
		MainUID =  menuIntent.getStringExtra("MainUID");
		
        Table2Name="C_"+Str_user+"_2";
		Table4Name="C_"+Str_user+"_4";
		Tablecate="C_"+Str_user+"_Cate";
		Tableitem="C_"+Str_user+"_Item";
		Tableitemrate="C_"+Str_user+"_Itemrate";
		TableSlot="C_"+Str_user+"_TimeSlot";
		TableExempt="C_"+Str_user+"_Exemtion";
		System.out.println(Log+" "+ClubName+" "+Table2Name+" "+Table4Name);
		
		webcall=new WebServiceCall();//Call a Webservice
		chkconn=new Chkconnection();
		btimg=imgconvert();
		calendar = Calendar.getInstance();
  	    year = calendar.get(Calendar.YEAR);
	    month = calendar.get(Calendar.MONTH);
	    cy=year-2015;
	    
	    adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Sp_year );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spyear.setAdapter(adapter); 
        spyear.setSelection(cy);
        
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Sp_Month );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spmonth.setAdapter(adapter);  
        spmonth.setSelection(month+1);
        spmonth.setOnItemSelectedListener(new OnItemSelectedListener()
        {  
  		@Override
  		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
  			// TODO Auto-generated method stub
  			StrMonth=month_val[arg2];
  		 }
  		@Override
  		public void onNothingSelected(AdapterView<?> arg0) {
  			// TODO Auto-generated method stub	
  		  }
          });
	    
	    //////////////////////set values from database//////////////////////////////////
		db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		rowItems_Menu = new ArrayList<RowItem>();
		strqry = "Select * from " + TableSlot + " Where Item_ID="+UID+" AND P_order<>0 Order By P_order";
		 cursorT = db.rawQuery(strqry, null);
		    if (cursorT.moveToFirst()) {
		      do
		      {
		       int uid = cursorT.getInt(1);
		       String name = cursorT.getString(3);
		       item= new RowItem(uid, name,btimg,"","");
		       rowItems_Menu.add(item);
		      } while (cursorT.moveToNext());
		    }
		    cursorT.close();
	    db.close();
	    if(rowItems_Menu.size()==0){
	    	DisplayMsg("No record found!");
	    }else{
	    	Adapter_Booking localAdapter_Menu = new Adapter_Booking(context, R.layout.list_item_booking, rowItems_Menu);
		    LVMenu.setAdapter(localAdapter_Menu);	
	    }
	    ////////////////////////////////////////////////////////////////
	    
	    LVMenu.setOnItemClickListener(new OnItemClickListener() {
    	  public void onItemClick(AdapterView<?> parent, View view, int position, long id){
    		  RowItem obj = rowItems_Menu.get(position);
              int SlotId = obj.uid;
              //System.out.println(uid);
              StrYear= spyear.getSelectedItem().toString().trim();	
              //System.out.println(StrYear+"  "+StrMonth);
              
              if(StrMonth.equals("0")){
            	  Toast.makeText(context, "Please select Month", 1).show(); 
              }else{
            	  
            	  //String RestrictDays=GetRestrictDayNumbers(SlotId);
            	  
            	  NextActivity(SlotId,StrYear,StrMonth);
              }
              
	  	    }
	    });
	    
	}
	
	
	
	///get Restricted Days numbers (Not Allowed to book on these day i.e saturday,sunday)
	private String GetRestrictDayNumbers(int SlotId)
	{
		String RDays="";
		db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
	    String str = "Select DOW from " + TableExempt +" Where Item_ID="+UID+" AND Slot_ID="+SlotId;
	    cursorT = db.rawQuery(str, null);
	    while (cursorT.moveToNext()) {
	    	RDays= RDays+cursorT.getInt(0)+","; 
	    }
	    cursorT.close();
	    db.close();
	    
	    if(RDays.contains(","))
	    	RDays=RDays.substring(0, RDays.length()-1);
	    
	    return RDays.trim();
	}
	
	
	
	
	public void NextActivity(int SlotId,String StrYear,String StrMonth)
	{
	    Intent localIntent = new Intent(context, CalendarviewPg.class);
	    localIntent.putExtra("Clt_LogID", logid);
	    localIntent.putExtra("Clt_Log", Log);
	    localIntent.putExtra("Clt_ClubName", ClubName);
	    localIntent.putExtra("UserClubName", Str_user);
	    localIntent.putExtra("UID", String.valueOf(SlotId));
	    localIntent.putExtra("SUID", UID);
	    localIntent.putExtra("MainUID", MainUID);
	    localIntent.putExtra("year", StrYear);
	    localIntent.putExtra("month", StrMonth);
	    startActivity(localIntent);
	    finish();
    }
	
	 private void goback()
	  {
	    Intent localIntent = new Intent(context, BookingsubCate.class);
	    localIntent.putExtra("Clt_LogID", logid);
	    localIntent.putExtra("Clt_Log", Log);
	    localIntent.putExtra("Clt_ClubName", ClubName);
	    localIntent.putExtra("UserClubName", Str_user);
	    localIntent.putExtra("UID",MainUID);
	    localIntent.putExtra("Type", "M");
	    startActivity(localIntent);
	    finish();
	  }
	 
	 public boolean onKeyDown(int keyCode,KeyEvent event) 
	 {
	   	 if (keyCode == KeyEvent.KEYCODE_BACK) {
	   		goback();
	   	    return true;
	   	 }
	   	return super.onKeyDown(keyCode, event);
	 }
	 
	 @SuppressWarnings("deprecation")
	private void DisplayMsg(String msg)
	  {
	    AlertDialog localAlertDialog = new AlertDialog.Builder(this).create();
	    localAlertDialog.setCancelable(false);
	    //localAlertDialog.setTitle(Html.fromHtml("<font color='#E3256B'>" + title + "</font>"));
	    localAlertDialog.setMessage(Html.fromHtml("<font color='#1C1CF0'>" + msg + "</font>"));
	    localAlertDialog.setButton("Ok", new DialogInterface.OnClickListener()
	    {
	      public void onClick(DialogInterface dialg, int which)
	      {
	        goback();
	      }
	    });
	    localAlertDialog.show();
	  }
	 
	 
   private byte[] imgconvert(){
	  Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tm);
	  ByteArrayOutputStream stream = new ByteArrayOutputStream();
	  bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
	  byte[] bitmapdata = stream.toByteArray();
	  return bitmapdata;
   }
   
}
