package group.manager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ShowBookHistory extends Activity{
	 AlertDialog ad;
	 Thread background;
     Chkconnection chkconn;
     Connection conn;
     final Context context = this;
     Cursor cursorT;
     SQLiteDatabase db;
     WebServiceCall webcall;
     Thread networkThread;
     ResultSet rs,RS1;
     boolean InternetPresent;
     String Tablebooking,Tableitem,Tabletimeslot,Str_user,memno,Log,logid,ClubName;
     RowItem item;
     List<RowItem> rowItems_Menu;
     ListView LVMenu;
     Intent menuIntent;
     Animation animFadein;
     Dialog dia;
     SimpleDateFormat sdf;
     
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.bookingcawnporclub);
	        LVMenu = (ListView)findViewById(R.id.lVMenus);
	        TextView txthd=(TextView)findViewById(R.id.tvHeadCard);
	        txthd.setText("My Booking");
	        ad=new AlertDialog.Builder(this).create();
	    	sdf= new SimpleDateFormat("dd-MM-yyyy"); // Simple Date Format 27-03-1999
	        chkconn=new Chkconnection();
	        menuIntent = getIntent(); 
	        Str_user =  menuIntent.getStringExtra("UserClubName");
			Log =  menuIntent.getStringExtra("Clt_Log");
			logid =  menuIntent.getStringExtra("Clt_LogID");
			ClubName =  menuIntent.getStringExtra("Clt_ClubName");
			memno =  menuIntent.getStringExtra("MemNo");
	        Tableitem="C_"+Str_user+"_Item";
	        Tablebooking="C_"+Str_user+"_BookingTran";
			Tabletimeslot="C_"+Str_user+"_TimeSlot";
			
	        rowItems_Menu = new ArrayList<RowItem>();
	        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
		    dia = new Dialog(context);
		    dia.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    dia.setContentView(R.layout.cust_dialog_box);
		    ImageView iv= (ImageView)dia.findViewById(R.id.imageViewbook);
		    iv.startAnimation(animFadein);
		    dia.setCancelable(false);
	        InternetPresent = chkconn.isConnectingToInternet(context);
	 	   	if (InternetPresent)
	 	    {
	 	   	  dia.show();
	     	  StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	 		  StrictMode.setThreadPolicy(policy);
	 		  RInsert();
	 	  }
	 	}
	 
	 public void RInsert()
	  {
	    networkThread = new Thread()
	    {
	      public void run()
	      {
	        try
	        {
	           Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
		       conn = DriverManager.getConnection("jdbc:jtds:sqlserver://103.21.58.192:1433/mda_bookings", "mdasoft_net", "MDA.1234");
	          runOnUiThread(new Runnable()
	          {
	            public void run()
	            {
	            	try {
						Statement statement = conn.createStatement();
						
						String StrQry ="Select Bt.U_ID,bt.Item_ID,bt.Slot_ID,Bt.BookDT,Bt.Confirm,I.Name,TS.Slot FROM (("+Tablebooking+" BT INNER JOIN "+Tableitem+" I ON BT.Item_ID = I.U_ID) INNER JOIN "+Tabletimeslot+" TS ON Bt.Slot_ID = TS.U_ID) WHERE BT.Mem_ID="+memno+" and BT.Item_ID = TS.Item_ID and cast(BT.Bookdt as date) >= cast(GETDATE() as date) ORDER BY BT.Bookdt"; 
						System.out.println(StrQry);
						rs = statement.executeQuery(StrQry);
						   while(rs.next())
						   {
						     String U_ID=rs.getString(1);
						     String Item_ID=rs.getString(2);
						     //String Slot_ID=rs.getString(3);
						     Date BookDT=rs.getDate(4);// Date DOB
		 		             String DOBk="";
		 		             if(BookDT!=null){
		 		               DOBk=sdf.format(BookDT);
		 		             }
						     String Confirm=rs.getString(5);
						     String Name=rs.getString(6);
						     String Slot=rs.getString(7);
						     ///////////////////////////////////////
						      int count=0;
						      Statement statement1 = conn.createStatement();
						      StrQry ="Select count(U_id) from "+Tabletimeslot+" where Item_ID="+Item_ID; 
						      ResultSet RS1 = statement1.executeQuery(StrQry);
							   while(RS1.next())
							   {
							     count=RS1.getInt(1);
							     break;
							   }
							   RS1.close(); // Close Result Set(RS1) Of Count*/
							 ////////////////////////////////////////////////
							 if(count>1){
								 item= new RowItem(Name,DOBk+" "+Slot,Confirm,U_ID,"");	 
							 }else{
								 item= new RowItem(Name,DOBk,Confirm,U_ID,"");
							 }
						     rowItems_Menu.add(item);
						   }
						   rs.close(); // Close Result Set(RS1) Of Count
						   dia.cancel();
						   Adapter_Booking localAdapter_Menu = new Adapter_Booking(context, R.layout.list_item_menu, rowItems_Menu);
						   LVMenu.setAdapter(localAdapter_Menu);	
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						 DisplayMsg("Connection Problem !","Try later."); 
					}
	            }
	          });
	          return;
	        } catch (SQLException se) {
  	  		  DisplayMsg("Connection Problem !","Check Internet Connection"); 
  			  se.printStackTrace();
	        } catch (ClassNotFoundException e) {
				  DisplayMsg("Connection Problem !","No Internet Connection"); 
	  			  e.printStackTrace();
		   } catch (Exception e) {
  			    DisplayMsg("Connection Problem !","No Internet Connection"); 
   			  e.printStackTrace();
  		    }
	      }
	    };
	    networkThread.start();
	  }
	 
	 @SuppressWarnings("deprecation")
	private void DisplayMsg(String title, String msg)
	  {
	    AlertDialog localAlertDialog = new AlertDialog.Builder(this).create();
	    localAlertDialog.setCancelable(false);
	    //localAlertDialog.setTitle(Html.fromHtml("<font color='#E3256B'>" + title + "</font>"));
	    localAlertDialog.setMessage(Html.fromHtml("<font color='#1C1CF0'>" + msg + "</font>"));
	    localAlertDialog.setButton("Ok", new DialogInterface.OnClickListener()
	    {
	      public void onClick(DialogInterface dialg, int which)
	      {
	        goback(0);
	      }
	    });
	    localAlertDialog.show();
	  }
	  
	
	public boolean onKeyDown(int keyCode,KeyEvent event) 
	 {
	   	 if (keyCode == KeyEvent.KEYCODE_BACK) {
	   		goback(0);
	   	    return true;
	   	 }
	   	return super.onKeyDown(keyCode, event);
	 }
	
	 private void goback(int val){
		 finish();
	}
	 
	 
}
