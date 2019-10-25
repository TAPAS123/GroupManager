package group.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BookingCawnporClub extends Activity{
	Intent menuIntent;
	String Table2Name,Table4Name,Log,ClubName,logid,Str_user,Tablecate,Tableitem,Tableitemrate,ValChk,sqlq="",CurrentDT_Diff,
			SyncDTstr,TableSlot,TableExempt,StrMemNo,UserType="";
	ListView LVMenu;
	Button btnBookingTermCond;
	String sqlSearch,Tab2Name,Tab4Name,TabMiscName,StrTotal,TabFamilyName,TableLocal,Table5,WebRsp="",Str_IEMI;
	ProgressDialog dialog;
	Animation animFadein;
    Thread background;
    Chkconnection chkconn;
    Connection conn;
    int counttabcate,counttabitem,counttabitemrate,countTableSlot,SyncDT,countTableExm;
    Cursor cursorT;
    SQLiteDatabase db,dbase;
    Dialog dia;
    WebServiceCall webcall;
    SimpleDateFormat sdf;
    Thread networkThread;
    ResultSet rs,RS1;
    boolean InternetPresent;
    List<RowItem> rowItems_Menu;
    RowItem item;
    int maxtabcate,maxtabitem,maxtabitemrate,maxTableSlot,maxTableExm,mintabcatesdt,mintabcatesyc,mintabitemratesdt,mintabitemratesyc,mintabitemsdt,
    mintabitemsyc,minTableSlotsdt,minTableSlotsyc,minTableExmsdt,minTableExmsyc,Strsynbook=0;
    TelephonyManager tm;
    TextView TxtHead;
    byte[] img_BookingTermCond=null;
    final Context context=this;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookingcawnporclub);
        LVMenu = (ListView) findViewById(R.id.lVMenus);
        TxtHead=(TextView) findViewById(R.id.tvHeadCard);
        LinearLayout LLayoutMonth=(LinearLayout)findViewById(R.id.llaymonth);
        LLayoutMonth.setVisibility(View.GONE);
        btnBookingTermCond=(Button)findViewById(R.id.btnBookingTermCond);
       
        menuIntent = getIntent(); 
        Str_user =  menuIntent.getStringExtra("UserClubName");
		Log =  menuIntent.getStringExtra("Clt_Log");
		logid =  menuIntent.getStringExtra("Clt_LogID");
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		ValChk =  menuIntent.getStringExtra("ValChk");
		
        Table2Name="C_"+Str_user+"_2";
		Table4Name="C_"+Str_user+"_4";
		Tablecate="C_"+Str_user+"_Cate";
		Tableitem="C_"+Str_user+"_Item";
		Tableitemrate="C_"+Str_user+"_Itemrate";
		TableSlot="C_"+Str_user+"_TimeSlot";
		TableExempt="C_"+Str_user+"_Exemtion";
		TableLocal="C_"+Str_user+"_Local";
		Table5="C_"+Str_user+"_5";
		
		System.out.println(Log+" "+ClubName+" "+Table2Name+" "+Table4Name);
		
		webcall=new WebServiceCall();//Call a Webservice
		chkconn=new Chkconnection();
		sdf = new SimpleDateFormat("dd-MM-yyyy");

		Str_IEMI = new CommonClass().getIMEINumber(context);//Added On 14-02-2019
		
		callValue();
	
		dialog = new ProgressDialog(this);
		dialog.setCancelable(false);
		dialog.setMessage(Html.fromHtml("<font color='#0892D0'>Loading...</font>"));
		dialog.setIcon(R.drawable.syncy);
		dialog.getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setTitle(Html.fromHtml("<font color='#008000'>Synchronization ( 1/2 )</font>"));
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, Html.fromHtml("<font color='#FF2400'>Stop now!</font>"), new DialogInterface.OnClickListener() {
		     @Override
		     public void onClick(DialogInterface dialg, int which) {
		    	goback(0);
		     }
		});
		
		db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		sqlq="Select MemNo from "+Table2Name+" where M_Id="+logid;
		cursorT=db.rawQuery(sqlq, null);
		while(cursorT.moveToNext())
		 {
		  StrMemNo=cursorT.getString(0);
		  break;
		}
		cursorT.close();
	    sqlq = "select Max(U_id),count(U_id),Min(SyncID),Min(SyncDT) from " + Tablecate;
	    cursorT = db.rawQuery(sqlq, null);
	    if (cursorT.moveToFirst())
	    {
	      maxtabcate = cursorT.getInt(0);
	      counttabcate = cursorT.getInt(1);
	      mintabcatesyc = cursorT.getInt(2);
	      mintabcatesdt = cursorT.getInt(3);
	    }
	    cursorT.close();
	    sqlq = "select Max(U_id),count(U_id),Min(SyncID),Min(SyncDT) from " + Tableitem;
	    cursorT = db.rawQuery(sqlq, null);
	    if (cursorT.moveToFirst())
	    {
	      maxtabitem = cursorT.getInt(0);
	      counttabitem = cursorT.getInt(1);
	      mintabitemsyc = cursorT.getInt(2);
	      mintabitemsdt = cursorT.getInt(3);
	    }
	    cursorT.close();
	    sqlq = "select Max(U_id),count(U_id),Min(SyncID),Min(SyncDT) from " + Tableitemrate;
	    cursorT = db.rawQuery(sqlq, null);
	    if (cursorT.moveToFirst())
	    {
	      maxtabitemrate = cursorT.getInt(0);
	      counttabitemrate = cursorT.getInt(1);
	      mintabitemratesyc = cursorT.getInt(2);
	      mintabitemratesdt = cursorT.getInt(3);
	    }
	    cursorT.close();
	    
	    sqlq = "select Max(U_id),count(U_id),Min(SyncID),Min(SyncDT) from " + TableSlot;
	    cursorT = db.rawQuery(sqlq, null);
	    if (cursorT.moveToFirst())
	    {
	      maxTableSlot = cursorT.getInt(0);
	      countTableSlot = cursorT.getInt(1);
	      minTableSlotsyc = cursorT.getInt(2);
	      minTableSlotsdt = cursorT.getInt(3);
	    }
	    cursorT.close();
	    
	    sqlq ="select Max(U_id),count(U_id),Min(SyncID),Min(SyncDT) from " + TableExempt;
	    cursorT = db.rawQuery(sqlq, null);
	    if (cursorT.moveToFirst())
	    {
	      maxTableExm = cursorT.getInt(0);
	      countTableExm = cursorT.getInt(1);
	      minTableExmsyc = cursorT.getInt(2);
	      minTableExmsdt = cursorT.getInt(3);
	    }
	    cursorT.close();
	    try{
	    	sqlq ="select Num from " + TableLocal+" where Rtype='SyncBooking'";
		    cursorT = db.rawQuery(sqlq, null);
		    if (cursorT.moveToFirst())
		    {
		    	Strsynbook = cursorT.getInt(0);
		    }
		    cursorT.close();	
	     }catch (SQLiteException e){
	    	System.out.println(e.getMessage());
	        if (e.getMessage().toString().contains("no such table")){
	        	db.execSQL("CREATE TABLE IF NOT EXISTS C_"+Str_user+"_Local (M_ID INTEGER PRIMARY KEY,MemId INTEGER,Num INTEGER,Num1 INTEGER,Num2 INTEGER,Rtype Text,Text1 Text,Text2 Text,Text3 Text)");	
	        }
	    }
	    
	    
	    //// Check Booking Terms and Condition image in table 4
	    
	    sqlq ="select Photo1 from " + Table4Name+" where Rtype='Book_Cond'";
	    cursorT = db.rawQuery(sqlq, null);
	    if (cursorT.moveToFirst())
	    {
	    	img_BookingTermCond=cursorT.getBlob(0);
	    }
	    cursorT.close();	
	    
	    if(img_BookingTermCond!=null)
	    	btnBookingTermCond.setVisibility(View.VISIBLE);
	    ///////////////////////////////////////////
	    
	    db.close();///Close Db Conection 
	   
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
    	 if(ValChk.equals("1")){
    		 CheckSync() ;
	     }else{
	    	 calllistview();
	     }
	  }
	   
       LVMenu.setOnItemClickListener(new OnItemClickListener() {
    	  public void onItemClick(AdapterView<?> parent, View view, int position, long id){
    		  RowItem obj = rowItems_Menu.get(position);
              int uid = obj.uid;
              NextActivity(uid);  
  	      }
       });
      
       
       ///Btn Booking Term and Condition
       btnBookingTermCond.setOnClickListener(new OnClickListener(){ 
	        @Override public void onClick(View arg0){
	        	
	        	WebView IVzoomimage;
        		Bitmap theImage = null;
        		ByteArrayInputStream imageStream = new ByteArrayInputStream(img_BookingTermCond);
    			theImage = BitmapFactory.decodeStream(imageStream);
    			Dialog dialog1 = new Dialog(context);
	    		dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    		dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
				dialog1.setContentView(R.layout.display_zoomimage);
				IVzoomimage = (WebView)dialog1.findViewById(R.id.imageViewzoom);
				/*theImage=Bitmap.createScaledBitmap(theImage, 300, 320, true);
				IVzoomimage.setImageBitmap(theImage);
				//.setWidth(120);
				bmpWidth = theImage.getWidth();
		        bmpHeight = theImage.getHeight();
		        System.out.println(bmpWidth+" :h, w: "+bmpWidth);
		        distCurrent = 1; //Dummy default distance
		        dist0 = 1;   //Dummy default distance
		        drawMatrix();
		        IVzoomimage.setOnTouchListener(MyOnTouchListener);
		        touchState = IDLE;*/
			    
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				theImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
			    byte[] byteArray = byteArrayOutputStream.toByteArray();
			    String imgageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
			    String image = "data:image/png;base64," + imgageBase64;
			    String html="<html><body><img src='{IMAGE_URL}' width='330' height='650' /></body></html>";
			    
			    // Use image for the img src parameter in your html and load to webview
			    html = html.replace("{IMAGE_URL}", image);
			    IVzoomimage.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", "");
			    IVzoomimage.getSettings().setSupportZoom(true);
			    IVzoomimage.getSettings().setBuiltInZoomControls(true);
			    IVzoomimage.setBackgroundColor(Color.DKGRAY);
				dialog1.show();
	        }
       });
       
	}
	
	public void NextActivity(int uid)
	  {
	    Intent localIntent = new Intent(context, BookingsubCate.class);
	    localIntent.putExtra("Clt_LogID", logid);
	    localIntent.putExtra("Clt_Log", Log);
	    localIntent.putExtra("Clt_ClubName", ClubName);
	    localIntent.putExtra("UserClubName", Str_user);
	    localIntent.putExtra("UID", String.valueOf(uid));
	    localIntent.putExtra("Type", "M");
	    startActivity(localIntent);
	    finish();
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
	          WebServiceCall localWebServiceCall = new WebServiceCall();
	          CurrentDT_Diff = localWebServiceCall.SyncDT_GetJullian();
	          runOnUiThread(new Runnable()
	          {
	            public void run()
	            {
	              if (CurrentDT_Diff != "CatchError")
	              {
	                String[] arrayOfString = CurrentDT_Diff.split("#");
	                SyncDTstr = arrayOfString[0].trim();
	                String str = SyncDTstr;
	                SyncDT = Integer.parseInt(str);
	                RecordInsertion(Tablecate, counttabcate, maxtabcate, mintabcatesyc, mintabcatesdt);
	              }else{
	            	 DisplayMsg("Connection Problem !", "No Internet Connection");  
	              }
	            }
	          });
	          return;
	        } catch (SQLException se) {
   	  		  DisplayMsg("Connection Problem !","No Internet Connection"); 
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
	    localAlertDialog.setTitle(Html.fromHtml("<font color='#E3256B'>" + title + "</font>"));
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
		 if(val==1){
			 dia.cancel();
			 db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
			 String sqlqry="";
			 if(Strsynbook==0){
				 sqlqry="Insert into "+TableLocal+"(Num,Rtype) Values("+WebRsp+",'SyncBooking')"; 
			 }else{
				 sqlqry= "UPDATE "+TableLocal+" SET Num="+WebRsp+" Where Rtype='SyncBooking'";
			 }
			 db.execSQL(sqlqry);
			 db.close();
			 calllistview(); 
		 }else{
			dia.cancel();
			Intent	MainBtnIntent= new Intent(getBaseContext(),MenuPage.class);
	   		MainBtnIntent.putExtra("Clt_LogID",logid);
	   		MainBtnIntent.putExtra("Clt_Log",Log);
	   		MainBtnIntent.putExtra("Clt_ClubName",ClubName);
	   		MainBtnIntent.putExtra("UserClubName",Str_user);
		    startActivity(MainBtnIntent);
		    finish(); 
		 }	
	}
	 
	 private void RecordInsertion(String TableName,int TabCount,int Max_M_id,int Min_Sync,int Min_SyncDT)
	  {
	    try
	    {
			 String StrQry="",StrQryCount="";
			 int RCount=0;
			   Statement statement = conn.createStatement();
			   if(TabCount==0)
			   {
				   StrQryCount = "select count(U_id) from "+TableName; // For Count
				   StrQry = "select * from "+TableName+" order by U_id";
			   }
			   else
			   {
				   StrQryCount = "select count(U_id) from "+TableName+" where U_id>"+Max_M_id; // For Count
				   StrQry = "select * from "+TableName+" where U_id>"+Max_M_id+" order by U_id";
			   }
			  // System.out.println(StrQry);
			   RS1 = statement.executeQuery(StrQryCount);
			   while(RS1.next())
			   {
			     RCount=RS1.getInt(1);
			     break;
			   }
			   RS1.close(); // Close Result Set(RS1) Of Count
		       rs = statement.executeQuery(StrQry);
		       ProgressBar(RCount,TableName);// Display Progress bar for Insertion
		       Prog_Insert(TableName,TabCount,Min_Sync,Min_SyncDT);
	    } catch (SQLException se) {
	  		  DisplayMsg("Connection Problem !","No Internet Connection"); 
			  se.printStackTrace();
		  }catch (Exception e) {
			  DisplayMsg("Connection Problem !","No Internet Connection"); 
			  e.printStackTrace();
		 }
	  } 
	 
	  private void ProgressBar(int RecordCount,String TableName)
	  {
	    int i=0;
	    if(TableName==Tablecate)
	    	i=1;
	     else if(TableName==Tableitem)
	    	i=2;
	     else if(TableName==Tableitemrate)
	    	i=3;
	     else if(TableName==TableSlot)
		    i=4;
	     else if(TableName==TableExempt)
		    i=5;
	     else if(TableName==Table5)
			i=6;
	     dialog.setTitle(Html.fromHtml("<font color='#008000'>Synchronization ( "+i+"/6 )</font>"));
	     dialog.setProgress(0);
	     dialog.setMax(RecordCount);
	  }
	  
	  
	 public void Prog_Insert(final String TableName,final int TabCount,final int Min_Sync,final int Min_SyncDT){
	       background = new Thread (new Runnable() {
	           public void run() {
	               try {
	            	    DbHandler db = new DbHandler(context,TableName);
	            	    while (rs.next()) 
	        		    {
	            		   if(TableName==Tablecate)
	            		   {
	            			  int U_ID = rs.getInt(1);
            	              String Name = rs.getString(2);
            	              String Remark = rs.getString(3);
            	              int SyncID = rs.getInt(4);
            	              int P_Order = rs.getInt(6);
            	              byte[] Image1 =rs.getBytes(7);
            	              String Text1 =rs.getString(8);
            	              int Float1 =rs.getInt(9);
	        	        	   
	        	        	db.Tabcate(TableName, U_ID, Name, Remark, SyncID, SyncDT, P_Order, Image1, Text1, Float1);
	            		   }
	            		   else if(TableName==Tableitem)
	            		   {
            			   	  int U_ID = rs.getInt(1);
            			   	  String Name=rs.getString(2);
            		          int Cate_ID = rs.getInt(3);
            		          String Remark = rs.getString(4);
            		          String S_Tag = rs.getString(5);
            		          int Book_Days = rs.getInt(6);
            		          int Book_Date = rs.getInt(7);
            		          int SyncID = rs.getInt(8);
            		          String Text1 = rs.getString(10);
            		          int Float1 = rs.getInt(11);
            		          int p_order = rs.getInt(12);
            		          String Text2 = rs.getString(13);
            		          String Text3 = rs.getString(14);
            		          String Text4 = rs.getString(15);
            		          byte[] Image1 =rs.getBytes(16);
            		          
	            			  db.Tabitem(TableName, U_ID, Name, Cate_ID, Remark, S_Tag, Book_Days, Book_Date, SyncID, SyncDT, Text1, Float1,p_order,Text2,Text3,Text4,Image1);
	            		   }
	            		   else if(TableName==Tableitemrate)
	            		   {
	            			   int U_ID = rs.getInt(1);
	            	            int iTem_ID = rs.getInt(2);
	            	            int Rate = rs.getInt(3);
	            	            Date localDate = rs.getDate(4);
	            	            String wef = null;
	            	            if (localDate != null) {
	            	              wef = sdf.format(localDate);
	            	            }
	            	            int SyncID = rs.getInt(5);
	            			   db.Tabitemrate(TableName, U_ID, iTem_ID, Rate, wef, SyncID, SyncDT);
	            		   }
	            		   else if(TableName==TableSlot)
	            		   {
	            			   int U_ID = rs.getInt(1);
	            	            int Item_ID = rs.getInt(2);
	            	            String slot = rs.getString(3);
	            	            int SyncID = rs.getInt(4);
	            	            int porder = rs.getInt(6);
	            			   db.Tabslot(TableName, U_ID, Item_ID, slot, SyncID, SyncDT,porder);
	            		   }
	            		   else if(TableName==TableExempt)
	            		   {
	            			   int U_ID = rs.getInt(1);
	            	            int iTem_ID = rs.getInt(2);
	            	            int slot_id = rs.getInt(3);
	            	            int Dow = rs.getInt(4);
	            	            int SyncID = rs.getInt(6);
	            			   db.TabExemt(TableName, U_ID, iTem_ID, slot_id,Dow, SyncID, SyncDT);
	            		   }
	            		   
	        	        progressHandler.sendMessage(progressHandler.obtainMessage());
	                   }
	            	   rs.close(); // Close Result Set
	            	   db.close(); // Close Local DataBase
	            	   
	            	   runOnUiThread(new Runnable()
			            {
			        	  public void run()
			              {
			        		  if(TabCount==0 && TableName==TableExempt)
			        		  {
			        			  RecordDeletion(); // Delete Records After Insertion Special Case
			        		  }
			        		  else
			        		  {
		        			     if(TabCount!=0){
		        				    RecordUpdation(TableName,Min_Sync,Min_SyncDT); // Records Updation 
		        			     }else{
		        				   if(TableName==Tablecate)
					        		   RecordInsertion(Tableitem, counttabitem, maxtabitem, mintabitemsyc, mintabitemsdt);
			        			   else if(TableName==Tableitem)
			        				   RecordInsertion(Tableitemrate, counttabitemrate, maxtabitemrate, mintabitemratesyc, mintabitemratesdt);
			        			   else if(TableName==Tableitemrate)
			        				   RecordInsertion(TableSlot, countTableSlot, maxTableSlot,minTableSlotsyc,minTableSlotsdt);
			        			   else if(TableName==TableSlot)
			        				   RecordInsertion(TableExempt, countTableExm, maxTableExm,minTableExmsyc,minTableExmsdt);
			        			   else
			        				   goback(1);
		        			     }
			        		  }
			              }
			           });
	            	   
	               } catch (SQLException se) {
		     	  		  DisplayMsg("Connection Problem !","No Internet Connection"); 
		     			  se.printStackTrace();
		     		  }catch (Exception e) {
		     			  DisplayMsg("Connection Problem !","No Internet Connection"); 
		      			  e.printStackTrace();
		     		 }
	               
	           }
	        });
	        background.start();
   }
	 
	 Handler progressHandler = new Handler() {
	      public void handleMessage(Message msg) {
	         dialog.incrementProgressBy(1);
	      }
	 };
	 
	 private void RecordUpdation(String TableName, int Min_Sync, int Min_SyncDT)
	  {
	    try
	    {
	     String StrQry="",StrQryCount="";
		 int RCount=0;
	      Statement statement = conn.createStatement();
	       StrQryCount = "select count(U_id) from "+TableName+" where SyncDT>"+Min_SyncDT; // For Count
		   StrQry = "select * from "+TableName+" where SyncDT>"+Min_SyncDT+" order by U_id";
		   RS1 = statement.executeQuery(StrQryCount);
		   while(RS1.next())
		   {
		     RCount=RS1.getInt(1);
		     break;
		   }
		   RS1.close(); // Close Result Set(RS1) Of Count
		   //System.out.println(StrQry);
	       rs = statement.executeQuery(StrQry);
	       ProgressBar(RCount,TableName); // Display Progress bar for Updation
	       Prog_Update(TableName);
	      return;
	    } catch (SQLException se) {
	  		  DisplayMsg("Connection Problem !","No Internet Connection"); 
			  se.printStackTrace();
		  }catch (Exception e) {
			  DisplayMsg("Connection Problem !","No Internet Connection"); 
			  e.printStackTrace();
		 }
	  }
	 
	 
	 private void RecordDeletion()
	 {
		 String StrQry="",StrQryCount="";
		 int RCount=0;
		 try
		 {
		   Statement statement = conn.createStatement();
		   StrQryCount = "select count(U_id) from "+Table5+" Where Ttype='DEL'"; // For Count
		   StrQry = "select TEXT1,TEXT2 from "+Table5+" Where Ttype='DEL' order by U_id";
		   
		   RS1 = statement.executeQuery(StrQryCount);
		   while(RS1.next())
		   {
		     RCount=RS1.getInt(1);
		     break;
		   }
		   RS1.close(); // Close Result Set(RS1) Of Count
	       rs = statement.executeQuery(StrQry);
	       ProgressBar(RCount,Table5);// Display Progress bar for Delete Records of Local Database
	       Prog_Delete();
		 } catch (SQLException se) {
	  		  DisplayMsg("Connection Problem !","No Internet Connection"); 
			  se.printStackTrace();
		 }catch (Exception e) {
			  DisplayMsg("Connection Problem !","No Internet Connection"); 
			  e.printStackTrace();
		 }
	 }
	 
	 
	 private void Prog_Update(final String TableName)
	 {
		networkThread = new Thread() {
		@Override
		 public void run() {
		   try {
			    String StrQry="";int j=0;
				if(TableName==Tablecate)
				  j=4; // SyncId Position
				else if(TableName==Tableitem)
				  j=8; // SyncId Position
				else if(TableName==Tableitemrate)
				  j=5; // SyncId Position
				else if(TableName==TableSlot)
				  j=4; // SyncId Position
				else if(TableName==TableExempt)
				  j=6; // SyncId Position
				
				while (rs.next()) 
			    {
			      int M_Id=rs.getInt(1);
			 	  int SYNCID=rs.getInt(j);
			 	  
			 	 SQLiteDatabase  dbase = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
			 	  String sqlSearch = "select Syncid from "+TableName +" where M_id="+M_Id;
				  cursorT = dbase.rawQuery(sqlSearch, null); 
			 	    while(cursorT.moveToFirst()){
			 		   if(SYNCID!=cursorT.getInt(0)){
			 			 StrQry = "Delete from "+TableName+" where M_id="+M_Id;
			 			 dbase.execSQL(StrQry);
			 			 dbase.close();
			 				    
			 			   DbHandler db = new DbHandler(context,TableName);
				 			   if(TableName==Tablecate)
				 			   {
				 				  int U_ID = rs.getInt(1);
	            	              String Name = rs.getString(2);
	            	              String Remark = rs.getString(3);
	            	              int P_Order = rs.getInt(6);
	            	              byte[] Image1 =rs.getBytes(7);
	            	              String Text1 =rs.getString(8);
	            	              int Float1 =rs.getInt(9);
		        	        	   
		        	        	db.Tabcate(TableName, U_ID, Name, Remark, SYNCID, SyncDT, P_Order, Image1, Text1, Float1);
				 			   }
				 			   else if(TableName==Tableitem)
		            		   {
	           			   	    int U_ID = rs.getInt(1);
	           			   	    String Name=rs.getString(2);
	           		            int Cate_ID = rs.getInt(3);
	           		            String Remark = rs.getString(4);
	           		            String S_Tag = rs.getString(5);
	           		            int Book_Days = rs.getInt(6);
	           		            int Book_Date = rs.getInt(7);
	           		            String Text1 = rs.getString(10);
	           		            int Float1 = rs.getInt(11);
	           		            int p_order = rs.getInt(12);
	           		            String Text2 = rs.getString(13);
	           		            String Text3 = rs.getString(14);
	           		            String Text4 = rs.getString(15);
	           		            byte[] Image1 =rs.getBytes(16);
	           		          
	           		         db.Tabitem(TableName, U_ID, Name, Cate_ID, Remark, S_Tag, Book_Days, Book_Date, SYNCID, SyncDT, Text1, Float1,p_order,Text2,Text3,Text4,Image1);
		            		  }
				 			  else if(TableName==Tableitemrate)
	            		      {
	            			    int U_ID = rs.getInt(1);
	            	            int iTem_ID = rs.getInt(2);
	            	            int Rate = rs.getInt(3);
	            	            Date localDate = rs.getDate(4);
	            	            String wef = null;
	            	            if (localDate != null) {
	            	              wef = sdf.format(localDate);
	            	            }
	            			   db.Tabitemrate(TableName, U_ID, iTem_ID, Rate, wef, SYNCID, SyncDT);
	            		     }
				 			 else if(TableName==TableSlot)
		            		   {
		            			   int U_ID = rs.getInt(1);
		            	            int Item_ID = rs.getInt(2);
		            	            String slot = rs.getString(3);
		            	            int porder = rs.getInt(6);
		            			   db.Tabslot(TableName, U_ID, Item_ID, slot, SYNCID, SyncDT,porder);
		            		   }
				 			 else if(TableName==TableExempt)
		            		   {
		            			    int U_ID = rs.getInt(1);
		            	            int Item_ID = rs.getInt(2);
		            	            int Slot_Id = rs.getInt(3);
		            	            int DOW = rs.getInt(4);
		            			   db.TabExemt(TableName, U_ID, Item_ID,Slot_Id, DOW, SYNCID, SyncDT);
		            		   }
		        	    db.close();
			 		   }
			 		   else
			 		   {
			 			  StrQry = "Update "+TableName+" Set SyncDT="+SyncDT+" where M_id="+M_Id;
				 		  dbase.execSQL(StrQry);
				 		  dbase.close();
			 		   }
			 		  break;
			 		}
			 	    cursorT.close();
			 	    dbase.close(); // Close Local DataBase
			 	    progressHandler.sendMessage(progressHandler.obtainMessage());
		          } 
				  rs.close(); 
				  RecordUpdate_DT(TableName); //Update DatetimeDiff To All Data
			      runOnUiThread(new Runnable()
			      {
			           public void run()
			           {
			        	   if(TableName==Tablecate){
			        		   RecordInsertion(Tableitem, counttabitem, maxtabitem, mintabitemsyc, mintabitemsdt);
	        			   }else if(TableName==Tableitem){
	        				   RecordInsertion(Tableitemrate, counttabitemrate, maxtabitemrate, mintabitemratesyc, mintabitemratesdt);
	        			   }else if(TableName==Tableitemrate){
	        				   RecordInsertion(TableSlot, countTableSlot, maxTableSlot,minTableSlotsyc,minTableSlotsdt);
	        			   }else if(TableName==TableSlot){
	        				   RecordInsertion(TableExempt, countTableExm, maxTableExm,minTableExmsyc,minTableExmsdt);
	        			   }
	        			   else{
	        				   RecordDeletion(); // Delete Records After Updation
	        			   }
			           }
			        });
		   } catch (SQLException se) {
  	  		  DisplayMsg("Connection Problem !","No Internet Connection"); 
  			  se.printStackTrace();
  		  }catch (Exception e) {
  			  DisplayMsg("Connection Problem !","No Internet Connection"); 
   			  e.printStackTrace();
  		 }
		   }
		};
		networkThread.start();
	 }
	 
	 private void RecordUpdate_DT(String TableName)
	 {
		 dbase = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		 String StrQry = "Update "+TableName +" Set SyncDT="+SyncDT;
		 dbase.execSQL(StrQry);
	     dbase.close();
	 }
	 
	 
	 
	 public void Prog_Delete()
	 {
		 networkThread = new Thread() {
			@Override
			public void run() {
			 try {
					String StrQry="";
					dbase = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
					while (rs.next()) 
					{
					   String TableName=rs.getString(1);
					   String U_Id=rs.getString(2);
					 			
					   StrQry = "Delete from "+TableName+" where U_Id="+U_Id;
					   dbase.execSQL(StrQry);
					   progressHandler.sendMessage(progressHandler.obtainMessage());
					 }
					rs.close();
					dbase.close(); // Close Local DataBase
					
					runOnUiThread(new Runnable()
					{
					     public void run()
					     {
					    	 goback(1);// End Synchronizaton
					     }
					});
			 } catch (SQLException se) {
		  		  DisplayMsg("Connection Problem !","No Internet Connection"); 
				  se.printStackTrace();
			 }catch (Exception e) {
				  DisplayMsg("Connection Problem !","No Internet Connection"); 
	 			  e.printStackTrace();
			 }

		    }
		   };
		   networkThread.start();
	 }
	 
	 
	 
	 private void calllistview()
	  {
	    db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
	    rowItems_Menu = new ArrayList<RowItem>();
	    String str = "Select * from " + Tablecate +" order by P_Order";
	    cursorT = db.rawQuery(str, null);
	    if (cursorT.moveToFirst()) {
	      do
	      {
	    	int uid=  cursorT.getInt(1);
	    	String name= cursorT.getString(2);
	    	byte[] img=cursorT.getBlob(7);
	    	item= new RowItem(uid, name, img,"","");
	        rowItems_Menu.add(item);
	      } while (cursorT.moveToNext());
	    }
	    cursorT.close();
	    db.close();
	    Adapter_Booking localAdapter_Menu = new Adapter_Booking(context, R.layout.list_item_booking, rowItems_Menu);
	    LVMenu.setAdapter(localAdapter_Menu);
	  }
	 
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.booking_menu, menu);
		    MenuItem itemB = menu.findItem(R.id.action_report);
			if (UserType.equalsIgnoreCase("Admin")){
		    	itemB.setVisible(true);
		    }else{
		    	itemB.setVisible(false);
		    }
		    return true;
		}
		
	 @Override
		public boolean onOptionsItemSelected(MenuItem item) {
		    // Handle item selection
		    switch (item.getItemId()) {
		        case R.id.action_back:
		        	Intent localIntent = new Intent(context, ShowBookHistory.class);
		   		    localIntent.putExtra("Clt_LogID", logid);
		   		    localIntent.putExtra("Clt_Log", Log);
		   		    localIntent.putExtra("Clt_ClubName", ClubName);
		   		    localIntent.putExtra("UserClubName", Str_user);
		   		    localIntent.putExtra("MemNo",StrMemNo);
		   		    startActivity(localIntent); 
		            return true;
		        case R.id.action_report:
		        	Intent localIntent1 = new Intent(context, ReportMain.class);
	          	    localIntent1.putExtra("Clt_LogID", logid);
	          	    localIntent1.putExtra("Clt_Log", Log);
	          	    localIntent1.putExtra("Clt_ClubName", ClubName);
	          	    localIntent1.putExtra("UserClubName", Str_user);
	          	    //localIntent1.putExtra("UID", String.valueOf(uid));
	          	    startActivity(localIntent1);
	          	    finish();
		            return true;
		        default:
		            return super.onOptionsItemSelected(item);
		    }
		}
		
		private void CheckSync() 
		 { 
			 dia.show();
	         networkThread = new Thread()
	         {
	           public void run()
	           {
	             try
	             {
	              WebRsp=webcall.clubbookingSync(Str_user, Str_IEMI);
	              runOnUiThread(new Runnable()
	              {
            	   public void run(){
            		System.out.println(WebRsp);
            		if((WebRsp==null)||(WebRsp.length()==0)){
            			dia.cancel();
            			DisplayMsg("Connection Problem !", "Please try later.");
            		}
            		
            		if((WebRsp.equalsIgnoreCase("Error"))||(WebRsp.equalsIgnoreCase("Unauthorised Access"))){
            			dia.cancel();
            			DisplayMsg("Connection Problem !", "Please try later.");
            		}else{
            			if(Integer.valueOf(WebRsp)==Strsynbook){
            				dia.cancel();
            				calllistview();
            			}else{
            				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                			StrictMode.setThreadPolicy(policy);
                			RInsert();
            			}
            		}
                   }
	              }); 
	              return;
	             }
	             catch (Exception localException)
	             {
	              System.out.println(localException.getMessage());
	              dia.cancel();
	              DisplayMsg("Connection Problem !", "Please try later.");  
	             }
	           }
	         };
	         networkThread.start();
			 
		 }
		
		 private void callValue() {
			 SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
			 if (sharedpreferences.contains("UserType"))
			 {
				  UserType=sharedpreferences.getString("UserType", "");
				  System.out.println("member: "+UserType);
			 } 
	    }
			 
 }
