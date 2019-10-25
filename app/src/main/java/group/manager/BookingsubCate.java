package group.manager;

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
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class BookingsubCate extends Activity{
	Intent menuIntent;
	String Table2Name,Table4Name,Log,ClubName,logid,Str_user,Tablecate,Tableitem,Tableitemrate,UID,TableSlot,Type="",strqry="",webrsp="",
			dateval="",UserType="";
	ListView LVMenu;
	Chkconnection chkconn;
	 WebServiceCall webcall;
    final Context context = this;
    Cursor cursorT;
    SQLiteDatabase db;
    List<RowItem> rowItems_Menu;
    RowItem item;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookingcawnporclub);
        LVMenu = (ListView) findViewById(R.id.lVMenus);
        LinearLayout LLayoutMonth=(LinearLayout)findViewById(R.id.llaymonth);
        TextView txthd=(TextView)findViewById(R.id.tvHeadCard);
        LLayoutMonth.setVisibility(View.GONE);
        rowItems_Menu = new ArrayList<RowItem>();
        
        SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        if (sharedpreferences.contains("UserType"))
        {
    	  UserType=sharedpreferences.getString("UserType", "");
        } 
        
        menuIntent = getIntent(); 
        Type =  menuIntent.getStringExtra("Type");
        if(Type.equalsIgnoreCase("M")){
        	Str_user =  menuIntent.getStringExtra("UserClubName");
    		Log =  menuIntent.getStringExtra("Clt_Log");
    		logid =  menuIntent.getStringExtra("Clt_LogID");
    		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
    		UID =  menuIntent.getStringExtra("UID");
    		
            Table2Name="C_"+Str_user+"_2";
    		Table4Name="C_"+Str_user+"_4";
    		Tablecate="C_"+Str_user+"_Cate";
    		Tableitem="C_"+Str_user+"_Item";
    		Tableitemrate="C_"+Str_user+"_Itemrate";
    		TableSlot="C_"+Str_user+"_TimeSlot";
    		
    		System.out.println(Log+" "+ClubName+" "+Table2Name+" "+Table4Name);
    		
    		webcall=new WebServiceCall();//Call a Webservice
    		chkconn=new Chkconnection();
    		db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
    		strqry = "Select * from " + Tableitem + " where Cate_Id=" + UID;
    		cursorT = db.rawQuery(strqry, null);
    		    if (cursorT.moveToFirst()) {
    		      do
    		      {
    		       int uid = cursorT.getInt(1);
    		       String name = cursorT.getString(2);
    		       String txt1 = cursorT.getString(10);
    		       if(txt1==null){
    		    	   txt1="";
    		       }
    		       String txt2 = cursorT.getString(13);
    		       if(txt2==null){
    		    	   txt2="";
    		       }
    		       byte[] img=cursorT.getBlob(16);
    		       item= new RowItem(uid, name,img,txt1,txt2);
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
        }else if(Type.equalsIgnoreCase("Adm")){
        	webrsp= menuIntent.getStringExtra("webadmin");
        	dateval= menuIntent.getStringExtra("date");
        	txthd.setText("Booking for "+dateval);
        	if((webrsp.contains("^"))){
        		String[] temp=webrsp.split("#");
        		for(int i=0;i<temp.length;i++){
        			String s=temp[i].replace("^", "#")+" ";
        			String [] sp=s.split("#");
        			String mem=sp[0].toString().trim();
        			String person=sp[1].toString().trim();
        			String mob=sp[2].toString().trim();
        			String confirm=sp[3].toString().trim();
        			String BookingType=sp[4].toString().trim();
        			String BookingSlot=sp[5].toString().trim();
        			
        			if(confirm==null){
        				confirm="";	
        			}
        			if(mob==null){
        				mob="";	
        			}
        			if(confirm.equalsIgnoreCase("P")){
        				confirm="Pending";	
        			}
        			if(confirm.equalsIgnoreCase("C")){
        				confirm="Confirmed";	
        			}
        			if(confirm.equalsIgnoreCase("A")){
        				confirm="Admin";	
        			}
        			
        			if(UserType.equalsIgnoreCase("admin"))
        			{
        				item= new RowItem(mem, person,mob+" ("+confirm+")","N",BookingType+" ("+BookingSlot+")");
        			}
        			else
        			{
        				item= new RowItem("", "",confirm,"N",BookingSlot);
        			}
     		        rowItems_Menu.add(item);
        		}
        		Adapter_Booking localAdapter_Menu = new Adapter_Booking(context, R.layout.list_item_booking, rowItems_Menu);
    		    LVMenu.setAdapter(localAdapter_Menu);	
    		    LVMenu.setClickable(false);
        	}else{
        		DisplayMsg("No record found!");	
        	}
        }else{
        	DisplayMsg("No record found!");
        }
        
	    
	    LVMenu.setOnItemClickListener(new OnItemClickListener() {
    	  public void onItemClick(AdapterView<?> parent, View view, int position, long id){
    		  RowItem obj = rowItems_Menu.get(position);
              int uid = obj.uid;
              if(Type.equalsIgnoreCase("M")){
            	  NextActivity(uid); 
              }else{
            	  String mob=obj.getGvMob();
            	  mob=mob.replace("(", "#")+" ";
            	  String [] tt=mob.split("#");
            	  final String no=tt[0].toString().trim();
                  if(no.length()!=0){
                	  System.out.println(no+"  cont::::");
                	  AlertDialog.Builder alertDialogBuilder3 = new AlertDialog.Builder(context);
                	  alertDialogBuilder3
    	                .setPositiveButton("Call",new DialogInterface.OnClickListener() {
    	                    public void onClick(DialogInterface dialog,int id) {
    	                    	callOnphone(no);
    	                    }
    	                })
    	                .setNegativeButton("Sms",new DialogInterface.OnClickListener() {
    	                    public void onClick(DialogInterface dialog,int id) {
    	                    	callOnSms(no);
    	                    }
    	                });
                	  AlertDialog ad = alertDialogBuilder3.create();
    	              ad.show();  
                  }  
              }
             /* int countuid=0;
              db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
              strqry = "Select Count(U_ID) from " + TableSlot + " where Item_ID=" + uid;
              cursorT = db.rawQuery(strqry, null);
      	    	if (cursorT.moveToFirst()) {
      	    	  countuid=cursorT.getInt(0);
      	    	  System.out.println(countuid+"  cont::::");
      	    	}
              cursorT.close();
      	      db.close();
      	      if(countuid>1){
      	    	NextActivity(uid); 
      	      }else  if(countuid==1){
      	    	  Toast.makeText(context, "1", 0).show();
      	      }else{
      	    	Toast.makeText(context, "full", 0).show(); 
      	      }*/
    		  
	  	    }
	    });
	    
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
	
	public void NextActivity(int uid)
	  {
			Intent localIntent = new Intent(context, Timeslot.class);
		    localIntent.putExtra("Clt_LogID", logid);
		    localIntent.putExtra("Clt_Log", Log);
		    localIntent.putExtra("Clt_ClubName", ClubName);
		    localIntent.putExtra("UserClubName", Str_user);
		    localIntent.putExtra("UID", String.valueOf(uid));
		    localIntent.putExtra("MainUID", UID);
		    //localIntent.putExtra("Type", "2"); 
		    startActivity(localIntent);
		    finish(); 
	  }
	
	 private void goback()
	  {
		 if(Type.equalsIgnoreCase("M")){
			Intent localIntent = new Intent(context, BookingCawnporClub.class);
		    localIntent.putExtra("Clt_LogID", logid);
		    localIntent.putExtra("Clt_Log", Log);
		    localIntent.putExtra("Clt_ClubName", ClubName);
		    localIntent.putExtra("UserClubName", Str_user);
		    localIntent.putExtra("ValChk","2");
		    localIntent.putExtra("AdmChk","N");
		    startActivity(localIntent);
		    finish(); 
		 }else{
			 finish();  
		 }
	   
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
}
