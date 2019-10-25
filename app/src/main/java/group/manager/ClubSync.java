package group.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.text.Html;
import android.view.KeyEvent;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class ClubSync  extends Activity{
	TextView txtProgg;
	Thread networkThread,background;
    Connection conn;
    String ClientId,Log,ClubName,logid,Tab2Name,Tab4Name,Tab5Name,TabMiscName,StrChkEny,TabFamilyName,TabOp1Name,TabOp2Name,UID="",ChkSync="";
    ProgressDialog dialog;
    ResultSet rs,RS1;
    SQLiteDatabase dbase;
    boolean Ftime;
    final Context context=this;
    Cursor cursorT;
    String TablesInfo;
    int Tab2Count,Tab4Count,TabMiscCount,Tab2Max_Mid,Tab4Max_Mid,TabMiscMax_Mid,Tab2Min_Sync,Tab4Min_Sync,TabMiscMin_Sync,Tab2Min_SyncDT,Tab4Min_SyncDT,TabMiscMin_SyncDT;
    String CurrentDT_Diff,SyncDT;
    byte[] AppLogo;
    int TabFamilyMax_Mid,TabFamilyCount,TabFamilyMin_Sync,TabFamilyMin_SyncDT;
    int TabOp1Max_Mid,TabOp1Count,TabOp1Min_Sync,TabOp1Min_SyncDT,TabOp2Max_Mid,TabOp2Count,TabOp2Min_Sync,TabOp2Min_SyncDT;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clubsync);
        
        Intent menuIntent = getIntent(); 
        Log =  menuIntent.getStringExtra("Clt_Log");
		logid =  menuIntent.getStringExtra("Clt_LogID");
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		ClientId =  menuIntent.getStringExtra("UserClubName");
		TablesInfo=  menuIntent.getStringExtra("StrTT");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		
		Set_App_Logo_Title(); // Set App Logo and Title
		
		Get_SharedPref_Values();
		
        String[] TabArr=TablesInfo.split("#");
        // 2nd Table Info
        Tab2Name="C_"+ClientId+"_2"; // 2nd Table Name
        Tab2Max_Mid=Integer.parseInt(TabArr[0]); // 2nd Table Max M_id
        Tab2Count=Integer.parseInt(TabArr[1]); // 2nd Table Total Records
        Tab2Min_Sync=Integer.parseInt(TabArr[2]); // 2nd Table Min Sync Id
        Tab2Min_SyncDT=Integer.parseInt(TabArr[3]); // 2nd Table Min Sync DateTimeDiff
        
        // 4th Table Info
        Tab4Name="C_"+ClientId+"_4"; // 4th table
        Tab4Max_Mid=Integer.parseInt(TabArr[4]); // 4th Table Max M_id
        Tab4Count=Integer.parseInt(TabArr[5]); // 4th Table Total Records
        Tab4Min_Sync=Integer.parseInt(TabArr[6]); // 4th Table Min Sync Id
        Tab4Min_SyncDT=Integer.parseInt(TabArr[7]); // 4th Table Min Sync DateTimeDiff
        
        // Misc Table Info
        TabMiscName="C_"+ClientId+"_MISC"; // Misc table
        TabMiscMax_Mid=Integer.parseInt(TabArr[8]); // Misc Table Max M_id
        TabMiscCount=Integer.parseInt(TabArr[9]); // Misc Table Total Records
        TabMiscMin_Sync=Integer.parseInt(TabArr[10]); // Misc Table Min Sync Id
        TabMiscMin_SyncDT=Integer.parseInt(TabArr[11]); // Misc Table Min Sync DateTimeDiff
        
        // Family Table Info
        TabFamilyName="C_"+ClientId+"_Family"; // Family table
        TabFamilyMax_Mid=Integer.parseInt(TabArr[12]); //Family Table Max M_id
        TabFamilyCount=Integer.parseInt(TabArr[13]); // Family Table Total Records
        TabFamilyMin_Sync=Integer.parseInt(TabArr[14]); // Family Table Min Sync Id
        TabFamilyMin_SyncDT=Integer.parseInt(TabArr[15]); // Family Table Min Sync DateTimeDiff
        
        // Opinion Poll 1 Table Info
        TabOp1Name="C_"+ClientId+"_OP1"; // OpinionPoll1 table
        TabOp1Max_Mid=Integer.parseInt(TabArr[16]); //OpinionPoll1 Table Max M_id
        TabOp1Count=Integer.parseInt(TabArr[17]); // OpinionPoll1 Table Total Records
        TabOp1Min_Sync=Integer.parseInt(TabArr[18]); // OpinionPoll1 Table Min Sync Id
        TabOp1Min_SyncDT=Integer.parseInt(TabArr[19]); // OpinionPoll1 Table Min Sync DateTimeDiff
        
        // Opinion Poll 2 Info
        TabOp2Name="C_"+ClientId+"_OP2"; // OpinionPoll2 table
        TabOp2Max_Mid=Integer.parseInt(TabArr[20]); //OpinionPoll2 Table Max M_id
        TabOp2Count=Integer.parseInt(TabArr[21]); // OpinionPoll2 Table Total Records
        TabOp2Min_Sync=Integer.parseInt(TabArr[22]); // OpinionPoll2 Table Min Sync Id
        TabOp2Min_SyncDT=Integer.parseInt(TabArr[23]); // OpinionPoll2 Table Min Sync DateTimeDiff
        
        //Table 5 is User for Delete Records from Local Db
        Tab5Name="C_"+ClientId+"_5"; // 5th table
        
        txtProgg=(TextView)findViewById(R.id.txtUpdateRecd);
        
        dialog = new ProgressDialog(this);
		dialog.setCancelable(false);
		dialog.setMessage(Html.fromHtml("<font color='#0892D0'>Loading...</font>"));
		dialog.setIcon(R.drawable.syncy);
		dialog.getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setTitle(Html.fromHtml("<font color='#008000'>Synchronization ( 1/7 )</font>"));
		  
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, Html.fromHtml("<font color='#FF2400'>Stop now!</font>"), new DialogInterface.OnClickListener() {
		     @Override
		     public void onClick(DialogInterface dialg, int which) {
		    	backs();
		     }
		});
		 
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		RInsert();
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
		if (ShPref.contains("userid"))
	    {
			UID=ShPref.getString("userid", "");
	    }
		if (ShPref.contains("ChkSync"))
	    {
			ChkSync=ShPref.getString("ChkSync", "");
	    }
	}
	
	 public void RInsert()
     {
		 networkThread = new Thread() {
		  @Override
			 public void run() {
			  try {
				  Looper.prepare();
				    String ConnectionString = "jdbc:jtds:sqlserver://103.21.58.192:1433/mda_clubs";
			        Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
			        conn = DriverManager.getConnection(ConnectionString,"mda_club", "MDA.1234_");
			        WebServiceCall webcall=new WebServiceCall();//Call a Webservice
			        CurrentDT_Diff=webcall.SyncDT_GetJullian();
			        
		        	runOnUiThread(new Runnable()
		            {
		        	  public void run()
		              {
		        		  if(CurrentDT_Diff!="CatchError")
		        		  {
		        			dialog.show();
		        			String SArr[]=CurrentDT_Diff.split("#");
		        			SyncDT=SArr[0].trim();
		        			//String t=SyncDT;
		        			//RecordInsertion(Tab2Name,Tab2Count,Tab2Max_Mid,Tab2Min_Sync,Tab2Min_SyncDT); // Insert For Table 2
		        			//RecordInsertion(Tab2Name,Tab2Count,4434,Tab2Min_Sync,Tab2Min_SyncDT);
		        			RecordInsertion(Tab4Name,Tab4Count,Tab4Max_Mid,Tab4Min_Sync,Tab4Min_SyncDT); // Insert For Table 4 First
		        		  }
		        		  else
		        		  {
		        			DisplayMsg("Connection Problem !","No Internet Connection"); 
		        		  }
		              }
		            });
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
	 
	 
	 private void RecordInsertion(String TableName,int TabCount,int Max_M_id,int Min_Sync,int Min_SyncDT)
	 {
		 String StrQry="",StrQryCount="";
		 int RCount=0;
		 try
		 {
		   Statement statement = conn.createStatement();
		   if(TabCount==0)
		   {
			   StrQryCount = "select count(M_id) from "+TableName; // For Count
			   StrQry = "select * from "+TableName+" order by M_id";
		   }
		   else
		   {
			   StrQryCount = "select count(M_id) from "+TableName+" where M_id>"+Max_M_id; // For Count
			   StrQry = "select * from "+TableName+" where M_id>"+Max_M_id+" order by M_id";
		   }
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
 
	 
	 private void RecordUpdation(String TableName,int Min_Sync,int Min_SyncDT)
	 {
		 String StrQry="",StrQryCount="";
		 int RCount=0;
		 try
		 {
		   Statement statement = conn.createStatement();
		   StrQryCount = "select count(M_id) from "+TableName+" where Syncid>"+Min_Sync+" AND SyncDT>"+Min_SyncDT; // For Count
		   StrQry = "select * from "+TableName+" where Syncid>"+Min_Sync+" AND SyncDT>"+Min_SyncDT+" order by M_id";
		   RS1 = statement.executeQuery(StrQryCount);
		   while(RS1.next())
		   {
		     RCount=RS1.getInt(1);
		     break;
		   }
		   RS1.close(); // Close Result Set(RS1) Of Count
	       rs = statement.executeQuery(StrQry);
	       ProgressBar(RCount,TableName); // Display Progress bar for Updation
	       Prog_Update(TableName);
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
		   StrQryCount = "select count(M_id) from "+Tab5Name+" Where Rtype='DEL'"; // For Count
		   StrQry = "select TEXT1,TEXT2 from "+Tab5Name+" Where Rtype='DEL' order by M_id";
		   
		   RS1 = statement.executeQuery(StrQryCount);
		   while(RS1.next())
		   {
		     RCount=RS1.getInt(1);
		     break;
		   }
		   RS1.close(); // Close Result Set(RS1) Of Count
	       rs = statement.executeQuery(StrQry);
	       ProgressBar(RCount,Tab5Name);// Display Progress bar for Delete Records of Local Database
	       Prog_Delete();
		 } catch (SQLException se) {
	  		  DisplayMsg("Connection Problem !","No Internet Connection"); 
			  se.printStackTrace();
		 }catch (Exception e) {
			  DisplayMsg("Connection Problem !","No Internet Connection"); 
			  e.printStackTrace();
		 }
	 }
	 
	 
	 Handler progressHandler = new Handler() {
	      public void handleMessage(Message msg) {
	         dialog.incrementProgressBy(1);
	      }
	 };

	 private void ProgressBar(int RecordCount,String TableName)
	 {
		 //Table 5 is User for Delete Records from Local Db
		 int i=0;
	     if(TableName==Tab4Name)
	    	i=1;
	     else if(TableName==Tab2Name)
	    	i=2;
	     else if(TableName==TabMiscName)
		    i=3;
	     else if(TableName==TabFamilyName)
			i=4;
	     else if(TableName==TabOp1Name)
			i=5;
	     else if(TableName==TabOp2Name)
			i=6;
	     else if(TableName==Tab5Name)
	    	i=7;
	     
	     dialog.setTitle(Html.fromHtml("<font color='#008000'>Synchronization ( "+i+"/7 )</font>"));
	     dialog.setProgress(0);
	     dialog.setMax(RecordCount);
	 }

	 
	 
	 private void RecordUpdate_DT(String TableName)
	 {
		 dbase = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		 String StrQry = "Update "+TableName +" Set SyncDT="+SyncDT;
		 dbase.execSQL(StrQry);
	     dbase.close();
	 }
	 
	 
	 private void Prog_Update(final String TableName)
	 {
		networkThread = new Thread() {
		@Override
		 public void run() {
		   try {
			   Looper.prepare();
			    String StrQry="";int j=0;
				if(TableName==Tab2Name)
				  j=96; // SyncId Position
				else if(TableName==Tab4Name)
				  j=23; // SyncId Position
				else if(TableName==TabMiscName)
				  j=59; // SyncId Position
				else if(TableName==TabFamilyName)
				  j=33; // SyncId Position
				else if(TableName==TabOp1Name)
				  j=11; // SyncId Position
				else if(TableName==TabOp2Name)
				  j=12; // SyncId Position
			    
				while (rs.next()) 
			    {
			      int M_Id=rs.getInt(1);
			 	  int SYNCID=rs.getInt(j);
			 	  
			 	  dbase = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
			 	  String sqlSearch = "select Syncid from "+TableName +" where M_id="+M_Id;
				  cursorT = dbase.rawQuery(sqlSearch, null); 
			 	    while(cursorT.moveToFirst()){
			 		   if(SYNCID!=cursorT.getInt(0)){
			 			 StrQry = "Delete from "+TableName+" where M_id="+M_Id;
			 			  dbase.execSQL(StrQry);
			 			  dbase.close();
			 				    
			 			   DbHandler db = new DbHandler(context,TableName);
			 			   if(TableName==Tab2Name)
			 			   {
			 				  // Insert Updated Record
		        	          String M_Name=rs.getString(2);
		        	          String M_FName=rs.getString(3);
	        	        	  String M_MName=rs.getString(4);
	        	        	  String M_LName=rs.getString(5);
	        	        	  String M_Add1=rs.getString(6);
	        	 			  String M_Add2=rs.getString(7);
	        	 			  String M_Add3=rs.getString(8);
	        	 			  String M_City=rs.getString(9);
	        	 			  String M_Email=rs.getString(10);
	        	 			  String M_Gender=rs.getString(11);
	        	 			  String M_MrgAnn_D=rs.getString(12);
	        	 			  String M_MrgAnn_M=rs.getString(13);
	        	 			  String M_MrgAnn_Y=rs.getString(14);
	        	 			  String M_DOB_D=rs.getString(15);
	        	 			  String M_DOB_M=rs.getString(16);
	        	 			  String M_DOB_Y=rs.getString(17);
	        	 			  String M_Mob=rs.getString(18);
	        	 			  String M_Pin=rs.getString(19);
	        	 			  String M_BG=rs.getString(20);
	        	 			  String M_BussNm=rs.getString(21);
	        	 			  String M_BussCate=rs.getString(22);
	        	 			  String M_MemSince_D=rs.getString(23);
	        	 			  String M_MemSince_M=rs.getString(24);
	        	 			  String M_MemSince_Y=rs.getString(25);
	        	 			  byte[] M_Pic=rs.getBytes(26);
	        	 			  String S_Name=rs.getString(27);
	        	 			  String S_FName=rs.getString(28);
	        	 			  String S_Mname=rs.getString(29);
	        	 			  String S_LName=rs.getString(30);
	        	 			  String S_Mob=rs.getString(31);
	        	 			  String S_Email=rs.getString(32);
	        	 			  String S_DOB_D=rs.getString(33);
	        	 			  String S_DOB_M=rs.getString(34);
	        	 			  String S_DOB_Y=rs.getString(35);
	        	 			  String S_BG=rs.getString(36);
	        	 			  byte[] S_Pic=rs.getBytes(37);
	        	 			  String C1_Name=rs.getString(38);
	        	 			  String C1_FName=rs.getString(39);
	        	 			  String C1_Mname=rs.getString(40);
	        	 			  String C1_LName=rs.getString(41);
	        	 			  String C1_Mob=rs.getString(42);
	        	 			  String C1_Email=rs.getString(43);
	        	 			  String C1_Gender=rs.getString(44);
	        	 			  String C1_DOB_D=rs.getString(45);
	        	 			  String C1_DOB_M=rs.getString(46);
	        	 			  String C1_DOB_Y=rs.getString(47);
	        	 			  String C1_BG=rs.getString(48);
	        	 			  byte[] C1_Pic=rs.getBytes(49);
	        	 			  String C2_Name=rs.getString(50);
	        	 			  String C2_FName=rs.getString(51);
	        	 			  String C2_Mname=rs.getString(52);
	        	 			  String C2_LName=rs.getString(53);
	        	 			  String C2_Mob=rs.getString(54);
	        	 			  String C2_Email=rs.getString(55);
	        	 			  String C2_Gender=rs.getString(56);
	        	 			  String C2_DOB_D=rs.getString(57);
	        	 			  String C2_DOB_M=rs.getString(58);
	        	 			  String C2_DOB_Y=rs.getString(59);
	        	 			  String C2_BG=rs.getString(60);
	        	 			  byte[] C2_Pic=rs.getBytes(61);
	        	 			  String C3_Name=rs.getString(62);
	        	 			  String C3_FName=rs.getString(63);
	        	 			  String C3_Mname=rs.getString(64);
	        	 			  String C3_LName=rs.getString(65);
	        	 			  String C3_Mob=rs.getString(66);
	        	 			  String C3_Email=rs.getString(67);
	        	 			  String C3_Gender=rs.getString(68);
	        	 			  String C3_DOB_D=rs.getString(69);
	        	 			  String C3_DOB_M=rs.getString(70);
	        	 			  String C3_DOB_Y=rs.getString(71);
	        	 			  String C3_BG=rs.getString(72);
	        	 			  byte[] C3_Pic=rs.getBytes(73);
	        	 			  String C4_Name=rs.getString(74);
	        	 			  String C4_FName=rs.getString(75);
	        	 			  String C4_Mname=rs.getString(76);
	        	 			  String C4_LName=rs.getString(77);
	        	 			  String C4_Mob=rs.getString(78);
	        	 			  String C4_Email=rs.getString(79);
	        	 			  String C4_Gender=rs.getString(80);
	        	 			  String C4_DOB_D=rs.getString(81);
	        	 			  String C4_DOB_M=rs.getString(82);
	        	 			  String C4_DOB_Y=rs.getString(83);
	        	 			  String C4_BG=rs.getString(84);
	        	 			  byte[] C4_Pic=rs.getBytes(85);
	        	 			  String Pass=rs.getString(86);
	        	 			  byte[] Photo1=rs.getBytes(87);
	        	 			  byte[] Photo2=rs.getBytes(88);
	        	 			  byte[] Photo3=rs.getBytes(89);
	        	 			  byte[] Photo4=rs.getBytes(90);
	        	 			  byte[] Photo5=rs.getBytes(91);
	        	 			  byte[] Photo6=rs.getBytes(92);
	        	 			  int Admin=rs.getInt(93);
	        	 			  String MemNo=rs.getString(94);
	        	 			  String Oth=rs.getString(95);
	        	 			
	        	 			  //Added Some Extra Fields On 16-03-2016
	        	 			  String[] ArrExtra=new String[14];
	        	 			  ArrExtra[0]=rs.getString(98);// M_SndAdd1
	        	 			  ArrExtra[1]=rs.getString(99);// M_SndAdd2
	        	 			  ArrExtra[2]=rs.getString(100);// M_SndAdd3
	        	 			  ArrExtra[3]=rs.getString(101);// M_SndAdd4
	        	 			  ArrExtra[4]=rs.getString(102);// M_SndPin
	        	 			  ArrExtra[5]=rs.getString(103);// M_SndMob
	        	 			  ArrExtra[6]=rs.getString(104);// M_SndMob1
	        	 			  ArrExtra[7]=rs.getString(105);// M_SndStd
	        	 			  ArrExtra[8]=rs.getString(106);// Oth1
	        	 			  ArrExtra[9]=rs.getString(107);// Oth2
	        	 			  ArrExtra[10]=rs.getString(108);// Oth3
	        	 			  ArrExtra[11]=rs.getString(109);// M_Land1
	        	 			  ArrExtra[12]=rs.getString(110);// M_Land2
	        	 			  ArrExtra[13]=rs.getString(111);// M_Email1
	        	 			  /////////////////////////////////////////
		        	 			
		        	          db.addContact(M_Id,M_Name,M_FName,M_MName,M_LName,M_Add1,
		        	        		M_Add2,M_Add3,M_City,M_Email,M_Gender,M_MrgAnn_D,
		        	        		M_MrgAnn_M,M_MrgAnn_Y,M_DOB_D,M_DOB_M,M_DOB_Y,M_Mob,M_Pin,
		        	        		M_BG,M_BussNm,M_BussCate,M_MemSince_D,M_MemSince_M,M_MemSince_Y,M_Pic,
        	        				S_Name,S_FName,S_Mname,S_LName,S_Mob,S_Email,S_DOB_D,S_DOB_M,
        	        				S_DOB_Y,S_BG,S_Pic,C1_Name,C1_FName,C1_Mname,
        	        				C1_LName,C1_Mob,C1_Email,C1_Gender,C1_DOB_D,C1_DOB_M,C1_DOB_Y,C1_BG,
        	        				C1_Pic,C2_Name,C2_FName,C2_Mname,C2_LName,C2_Mob,C2_Email,C2_Gender,
        	        				C2_DOB_D,C2_DOB_M,C2_DOB_Y,C2_BG,C2_Pic,C3_Name,C3_FName,C3_Mname,C3_LName,
        	        				C3_Mob,C3_Email,C3_Gender,C3_DOB_D,C3_DOB_M,C3_DOB_Y,C3_BG,C3_Pic,C4_Name,
        	        				C4_FName,C4_Mname,C4_LName,C4_Mob,C4_Email,C4_Gender,C4_DOB_D,C4_DOB_M,
        	        				C4_DOB_Y,C4_BG,C4_Pic,Pass,Photo1,Photo2,Photo3,Photo4,
        	        				Photo5,Photo6,Admin,MemNo,Oth,SYNCID,SyncDT,ArrExtra);
			 			   }
		            	   else if(TableName==Tab4Name)
		            	   {
		            		   db.Tab4AddContact(TableName,M_Id,rs.getString(2),rs.getString(3),rs.getString(4),
		            					 rs.getString(5), rs.getString(6), rs.getString(7),rs.getString(8),rs.getString(9),
		            					 rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13),rs.getString(14),
		            					 rs.getString(15),rs.getBytes(16),rs.getString(17),rs.getString(18),
		            					 rs.getString(19),rs.getString(20),rs.getString(21),rs.getString(22),SYNCID,SyncDT,
		            					 rs.getString(25),rs.getString(26),rs.getString(27),rs.getString(28),rs.getString(29));
		            	   }
		            	   else if(TableName==TabMiscName)
		            	   { 
		            		   db.Tab_Misc_AddContact(TableName,M_Id,rs.getString(2),rs.getString(3),
		            					 rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),
		            					 rs.getString(12),rs.getString(13),rs.getString(14),rs.getString(15),rs.getString(16),rs.getString(17),rs.getString(18),rs.getString(19),
		            					 rs.getString(20),rs.getString(21),rs.getString(22),rs.getString(23),rs.getString(24),rs.getString(25),rs.getString(26),rs.getString(27),
		            					 rs.getString(28),rs.getString(29),rs.getString(30),rs.getString(31),rs.getString(32),rs.getString(33),rs.getString(34),rs.getString(35),
		            					 rs.getString(36),rs.getString(37),rs.getString(38),rs.getString(39),rs.getString(40),rs.getString(41),rs.getString(42),rs.getString(43),
		            					 rs.getString(44),rs.getString(45),rs.getString(46),rs.getString(47),rs.getString(48),rs.getString(49), rs.getString(50),rs.getString(51),rs.getString(52),rs.getString(53),
		            					 rs.getString(54),rs.getString(55),rs.getString(56),rs.getString(57),rs.getString(58),SYNCID,SyncDT);
		            	   }
		            	   else if(TableName==TabFamilyName)
		            	   { 
		            		   db.Tab_Family_AddContact(TableName,M_Id, rs.getString(2), rs.getString(3),
		            					 rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),
		            					 rs.getString(12),rs.getString(13),rs.getString(14),rs.getString(15),rs.getString(16),rs.getString(17),rs.getString(18),rs.getString(19),
		            					 rs.getBytes(20),rs.getString(21),rs.getString(22),rs.getString(23),rs.getString(24),rs.getString(25),rs.getString(26),rs.getString(27),
		            					 rs.getString(28),rs.getString(29),rs.getString(30),rs.getString(31),rs.getString(32), SYNCID, SyncDT,rs.getString(35),rs.getString(36),
		            					 rs.getString(37),rs.getString(38),rs.getString(39),rs.getString(40),rs.getString(41),rs.getString(42),rs.getString(43),rs.getFloat(50));
		            	   }
		            	   else if(TableName==TabOp1Name)
	            		   {
	            			   String[] ArrData=new String[11];
	            			   ArrData[0]=rs.getString(2);// OP_Name
	            			   ArrData[1]=rs.getString(3);// OP_From
	            			   ArrData[2]=rs.getString(4);// OP_TO
	            			   ArrData[3]=rs.getString(5);// OP_From_1
	            			   ArrData[4]=rs.getString(6);// Op_To_1
	            			   ArrData[5]=rs.getString(7);// Cond1
	            			   ArrData[6]=rs.getString(8);// Cond2
	            			   ArrData[7]=rs.getString(9);// Co_Type
	            			   ArrData[8]=rs.getString(10);// Time_Req
	            			   ArrData[9]=rs.getString(13);// Op_Publish
	            			   ArrData[10]=rs.getString(14);// U_Ans
	            			   
	            			   db.Tab_Opinion1_InsertData(TableName,M_Id,ArrData,SYNCID,SyncDT);
	            		   }
	            		   else if(TableName==TabOp2Name)
	            		   {
                               String[] ArrData=new String[11];
	            			   ArrData[0]=rs.getString(2);// OP1_ID
	            			   ArrData[1]=rs.getString(3);// PSNO
	            			   ArrData[2]=rs.getString(4);// SNO
	            			   ArrData[3]=rs.getString(5);// Question
	            			   ArrData[4]=rs.getString(6);// Ans1
	            			   ArrData[5]=rs.getString(7);// Ans2
	            			   ArrData[6]=rs.getString(8);// Ans3
	            			   ArrData[7]=rs.getString(9);// Ans4
	            			   ArrData[8]=rs.getString(10);// Remark
	            			   ArrData[9]=rs.getString(11);// Answer
	            			   ArrData[10]=rs.getString(14);// Remark_Req
	            			   
	            			   db.Tab_Opinion2_InsertData(TableName,M_Id,ArrData,SYNCID,SyncDT);
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
			        	   if(TableName==Tab4Name)
	        			   {
	        				  RecordInsertion(Tab2Name,Tab2Count,Tab2Max_Mid,Tab2Min_Sync,Tab2Min_SyncDT); // Insert For Table 2
	        			   }
			        	   else if(TableName==Tab2Name)
	        			   {
	        				  RecordInsertion(TabMiscName,TabMiscCount,TabMiscMax_Mid,TabMiscMin_Sync,TabMiscMin_SyncDT); // Insert For Table Misc
	        			   } 
			        	   else if(TableName==TabMiscName)
		            	   { 
			        		  RecordInsertion(TabFamilyName,TabFamilyCount,TabFamilyMax_Mid,TabFamilyMin_Sync,TabFamilyMin_SyncDT); // Insert For Table Family
		            	   }
			        	   else if(TableName==TabFamilyName)
	        			   {
	        				  RecordInsertion(TabOp1Name,TabOp1Count,TabOp1Max_Mid,TabOp1Min_Sync,TabOp1Min_SyncDT); // Insert For Table Opinion1
	        			   }
	        			   else if(TableName==TabOp1Name)
	        			   {
	        				  RecordInsertion(TabOp2Name,TabOp2Count,TabOp2Max_Mid,TabOp2Min_Sync,TabOp2Min_SyncDT); // Insert For Table Opinion2
	        			   }
	        			   else
	        			   {
	        				  RecordDeletion(); // Delete Records After Updation
	        			   }
			           }
			        });
		   		}catch (SQLException se) {
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
	 
 	
	 public void Prog_Insert(final String TableName,final int TabCount,final int Min_Sync,final int Min_SyncDT){
	    	   background = new Thread (new Runnable() {
		           public void run() {
		               try {
		            	   Looper.prepare();
		            	    DbHandler db = new DbHandler(context,TableName);
		            	    while (rs.next()) 
		        		    {
		            		   if(TableName==Tab2Name)
		            		   {
		            		     int M_Id=rs.getInt(1);
		            		    //System.out.println("Mid:"+M_Id);
		        	        	String M_Name=rs.getString(2);
		        	        	String M_FName=rs.getString(3);
		        	        	String M_MName=rs.getString(4);
		        	        	String M_LName=rs.getString(5);
		        	        	String M_Add1=rs.getString(6);
		        	 			String M_Add2=rs.getString(7);
		        	 			String M_Add3=rs.getString(8);
		        	 			String M_City=rs.getString(9);
		        	 			String M_Email=rs.getString(10);
		        	 			String M_Gender=rs.getString(11);
		        	 			String M_MrgAnn_D=rs.getString(12);
		        	 			String M_MrgAnn_M=rs.getString(13);
		        	 			String M_MrgAnn_Y=rs.getString(14);
		        	 			String M_DOB_D=rs.getString(15);
		        	 			String M_DOB_M=rs.getString(16);
		        	 			String M_DOB_Y=rs.getString(17);
		        	 			String M_Mob=rs.getString(18);
		        	 			String M_Pin=rs.getString(19);
		        	 			String M_BG=rs.getString(20);
		        	 			String M_BussNm=rs.getString(21);
		        	 			String M_BussCate=rs.getString(22);
		        	 			String M_MemSince_D=rs.getString(23);
		        	 			String M_MemSince_M=rs.getString(24);
		        	 			String M_MemSince_Y=rs.getString(25);
		        	 			byte[] M_Pic=rs.getBytes(26);
		        	 			String S_Name=rs.getString(27);
		        	 			String S_FName=rs.getString(28);
		        	 			String S_Mname=rs.getString(29);
		        	 			String S_LName=rs.getString(30);
		        	 			String S_Mob=rs.getString(31);
		        	 			String S_Email=rs.getString(32);
		        	 			String S_DOB_D=rs.getString(33);
		        	 			String S_DOB_M=rs.getString(34);
		        	 			String S_DOB_Y=rs.getString(35);
		        	 			String S_BG=rs.getString(36);
		        	 			byte[] S_Pic=rs.getBytes(37);
		        	 			String C1_Name=rs.getString(38);
		        	 			String C1_FName=rs.getString(39);
		        	 			String C1_Mname=rs.getString(40);
		        	 			String C1_LName=rs.getString(41);
		        	 			String C1_Mob=rs.getString(42);
		        	 			String C1_Email=rs.getString(43);
		        	 			String C1_Gender=rs.getString(44);
		        	 			String C1_DOB_D=rs.getString(45);
		        	 			String C1_DOB_M=rs.getString(46);
		        	 			String C1_DOB_Y=rs.getString(47);
		        	 			String C1_BG=rs.getString(48);
		        	 			byte[] C1_Pic=rs.getBytes(49);
		        	 			String C2_Name=rs.getString(50);
		        	 			String C2_FName=rs.getString(51);
		        	 			String C2_Mname=rs.getString(52);
		        	 			String C2_LName=rs.getString(53);
		        	 			String C2_Mob=rs.getString(54);
		        	 			String C2_Email=rs.getString(55);
		        	 			String C2_Gender=rs.getString(56);
		        	 			String C2_DOB_D=rs.getString(57);
		        	 			String C2_DOB_M=rs.getString(58);
		        	 			String C2_DOB_Y=rs.getString(59);
		        	 			String C2_BG=rs.getString(60);
		        	 			byte[] C2_Pic=rs.getBytes(61);
		        	 			String C3_Name=rs.getString(62);
		        	 			String C3_FName=rs.getString(63);
		        	 			String C3_Mname=rs.getString(64);
		        	 			String C3_LName=rs.getString(65);
		        	 			String C3_Mob=rs.getString(66);
		        	 			String C3_Email=rs.getString(67);
		        	 			String C3_Gender=rs.getString(68);
		        	 			String C3_DOB_D=rs.getString(69);
		        	 			String C3_DOB_M=rs.getString(70);
		        	 			String C3_DOB_Y=rs.getString(71);
		        	 			String C3_BG=rs.getString(72);
		        	 			byte[] C3_Pic=rs.getBytes(73);
		        	 			String C4_Name=rs.getString(74);
		        	 			String C4_FName=rs.getString(75);
		        	 			String C4_Mname=rs.getString(76);
		        	 			String C4_LName=rs.getString(77);
		        	 			String C4_Mob=rs.getString(78);
		        	 			String C4_Email=rs.getString(79);
		        	 			String C4_Gender=rs.getString(80);
		        	 			String C4_DOB_D=rs.getString(81);
		        	 			String C4_DOB_M=rs.getString(82);
		        	 			String C4_DOB_Y=rs.getString(83);
		        	 			String C4_BG=rs.getString(84);
		        	 			byte[] C4_Pic=rs.getBytes(85);
		        	 			String Pass=rs.getString(86);
		        	 			byte[] Photo1=rs.getBytes(87);
		        	 			byte[] Photo2=rs.getBytes(88);
		        	 			byte[] Photo3=rs.getBytes(89);
		        	 			byte[] Photo4=rs.getBytes(90);
		        	 			byte[] Photo5=rs.getBytes(91);
		        	 			byte[] Photo6=rs.getBytes(92);
		        	 			int Admin=rs.getInt(93);
		        	 			String MemNo=rs.getString(94);
		        	 			String Oth=rs.getString(95);
		        	 			int SYNCID=rs.getInt(96);
		        	 			
		        	 			//Added Some Extra Fields On 16-03-2016
		        	 			String[] ArrExtra=new String[14];
		        	 		    ArrExtra[0]=rs.getString(98);// M_SndAdd1
		        	 			ArrExtra[1]=rs.getString(99);// M_SndAdd2
		        	 			ArrExtra[2]=rs.getString(100);// M_SndAdd3
		        	 			ArrExtra[3]=rs.getString(101);// M_SndAdd4
		        	 			ArrExtra[4]=rs.getString(102);// M_SndPin
		        	 			ArrExtra[5]=rs.getString(103);// M_SndMob
		        	 			ArrExtra[6]=rs.getString(104);// M_SndMob1
		        	 			ArrExtra[7]=rs.getString(105);// M_SndStd
		        	 			ArrExtra[8]=rs.getString(106);// Oth1
		        	 			ArrExtra[9]=rs.getString(107);// Oth2
		        	 			ArrExtra[10]=rs.getString(108);// Oth3
		        	 			ArrExtra[11]=rs.getString(109);// M_Land1
		        	 			ArrExtra[12]=rs.getString(110);// M_Land2
		        	 			ArrExtra[13]=rs.getString(111);// M_Email1
		        	 			/////////////////////////////////////////
		        	        	   
		        	        	db.addContact(M_Id,M_Name,M_FName,M_MName,M_LName,M_Add1,
		        	        				M_Add2,M_Add3,M_City,M_Email,M_Gender,M_MrgAnn_D,
		        	        				M_MrgAnn_M,M_MrgAnn_Y,M_DOB_D,M_DOB_M,M_DOB_Y,M_Mob,M_Pin,
		        	        				M_BG,M_BussNm,M_BussCate,M_MemSince_D,M_MemSince_M,M_MemSince_Y,M_Pic,
		        	        				S_Name,S_FName,S_Mname,S_LName,S_Mob,S_Email,S_DOB_D,S_DOB_M,
		        	        				S_DOB_Y,S_BG,S_Pic,C1_Name,C1_FName,C1_Mname,
		        	        				C1_LName,C1_Mob,C1_Email,C1_Gender,C1_DOB_D,C1_DOB_M,C1_DOB_Y,C1_BG,
		        	        				C1_Pic,C2_Name,C2_FName,C2_Mname,C2_LName,C2_Mob,C2_Email,C2_Gender,
		        	        				C2_DOB_D,C2_DOB_M,C2_DOB_Y,C2_BG,C2_Pic,C3_Name,C3_FName,C3_Mname,C3_LName,
		        	        				C3_Mob,C3_Email,C3_Gender,C3_DOB_D,C3_DOB_M,C3_DOB_Y,C3_BG,C3_Pic,C4_Name,
		        	        				C4_FName,C4_Mname,C4_LName,C4_Mob,C4_Email,C4_Gender,C4_DOB_D,C4_DOB_M,
		        	        				C4_DOB_Y,C4_BG,C4_Pic,Pass,Photo1,Photo2,Photo3,Photo4,
		        	        				Photo5,Photo6,Admin,MemNo,Oth,SYNCID,SyncDT,ArrExtra);
		            		   }
		            		   else if(TableName==Tab4Name)          
		            		   {
		            			   db.Tab4AddContact(TableName,rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),
		            					 rs.getString(5), rs.getString(6), rs.getString(7),rs.getString(8),rs.getString(9),
		            					 rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13),rs.getString(14),
		            					 rs.getString(15),rs.getBytes(16),rs.getString(17),rs.getString(18),
		            					 rs.getString(19),rs.getString(20),rs.getString(21),rs.getString(22),rs.getInt(23),SyncDT,
		            					 rs.getString(25),rs.getString(26),rs.getString(27),rs.getString(28),rs.getString(29));
		            		   }
		            		   else if(TableName==TabMiscName)
		            		   {
		            			   db.Tab_Misc_AddContact(TableName,rs.getInt(1),rs.getString(2),rs.getString(3),
		            					 rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),
		            					 rs.getString(12),rs.getString(13),rs.getString(14),rs.getString(15),rs.getString(16),rs.getString(17),rs.getString(18),rs.getString(19),
		            					 rs.getString(20),rs.getString(21),rs.getString(22),rs.getString(23),rs.getString(24),rs.getString(25),rs.getString(26),rs.getString(27),
		            					 rs.getString(28),rs.getString(29),rs.getString(30),rs.getString(31),rs.getString(32),rs.getString(33),rs.getString(34),rs.getString(35),
		            					 rs.getString(36),rs.getString(37),rs.getString(38),rs.getString(39),rs.getString(40),rs.getString(41),rs.getString(42),rs.getString(43),
		            					 rs.getString(44),rs.getString(45),rs.getString(46),rs.getString(47),rs.getString(48),rs.getString(49), rs.getString(50),rs.getString(51),rs.getString(52),rs.getString(53),
		            					 rs.getString(54),rs.getString(55),rs.getString(56),rs.getString(57),rs.getString(58),rs.getInt(59),SyncDT);
		            		   }
		            		   else if(TableName==TabFamilyName)
		            		   {
		            			   db.Tab_Family_AddContact(TableName,rs.getInt(1), rs.getString(2), rs.getString(3),
			            					 rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),
			            					 rs.getString(12),rs.getString(13),rs.getString(14),rs.getString(15),rs.getString(16),rs.getString(17),rs.getString(18),rs.getString(19),
			            					 rs.getBytes(20),rs.getString(21),rs.getString(22),rs.getString(23),rs.getString(24),rs.getString(25),rs.getString(26),rs.getString(27),
			            					 rs.getString(28),rs.getString(29),rs.getString(30),rs.getString(31),rs.getString(32), rs.getInt(33), SyncDT,rs.getString(35),rs.getString(36),
			            					 rs.getString(37),rs.getString(38),rs.getString(39),rs.getString(40),rs.getString(41),rs.getString(42),rs.getString(43),rs.getFloat(50));
		            		   }
		            		   else if(TableName==TabOp1Name)
		            		   {
		            			   String[] ArrData=new String[11];
		            			   ArrData[0]=rs.getString(2);// OP_Name
		            			   ArrData[1]=rs.getString(3);// OP_From
		            			   ArrData[2]=rs.getString(4);// OP_TO
		            			   ArrData[3]=rs.getString(5);// OP_From_1
		            			   ArrData[4]=rs.getString(6);// Op_To_1
		            			   ArrData[5]=rs.getString(7);// Cond1
		            			   ArrData[6]=rs.getString(8);// Cond2
		            			   ArrData[7]=rs.getString(9);// Co_Type
		            			   ArrData[8]=rs.getString(10);// Time_Req
		            			   ArrData[9]=rs.getString(13);// Op_Publish
		            			   ArrData[10]=rs.getString(14);// U_Ans
		            			   
		            			   db.Tab_Opinion1_InsertData(TableName,rs.getInt(1),ArrData,rs.getInt(11),SyncDT);
		            		   }
		            		   else if(TableName==TabOp2Name)
		            		   {
                                   String[] ArrData=new String[11];
		            			   ArrData[0]=rs.getString(2);// OP1_ID
		            			   ArrData[1]=rs.getString(3);// PSNO
		            			   ArrData[2]=rs.getString(4);// SNO
		            			   ArrData[3]=rs.getString(5);// Question
		            			   ArrData[4]=rs.getString(6);// Ans1
		            			   ArrData[5]=rs.getString(7);// Ans2
		            			   ArrData[6]=rs.getString(8);// Ans3
		            			   ArrData[7]=rs.getString(9);// Ans4
		            			   ArrData[8]=rs.getString(10);// Remark
		            			   ArrData[9]=rs.getString(11);// Answer
		            			   ArrData[10]=rs.getString(14);// Remark_Req
		            			   
		            			   db.Tab_Opinion2_InsertData(TableName,rs.getInt(1),ArrData,rs.getInt(12),SyncDT);
		            		   }
		        	           progressHandler.sendMessage(progressHandler.obtainMessage());
		                  }
		            	   rs.close(); // Close Result Set
		            	   db.close(); // Close Local DataBase
	       
		            	   if((TabCount==0)&&(TableName.equals(Tab4Name))){        //////////////////edit by tripti///////////
		            	       dbase = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
	    	   					String StrQry = "Update "+Tab4Name+" Set Num2='1' Where Rtype='News'";
	    	   					dbase.execSQL(StrQry);
	    	   					dbase.close();
		            	   }
	       
		            	   runOnUiThread(new Runnable()
				            {
				        	  public void run()
				              {
				        		  if(TabCount==0 && TableName==TabOp2Name)
				        		  {
				        			  RecordDeletion(); // Delete Records After Insertion Special Case
				        		  }
				        		  else
				        		  {
				        			  if(TabCount!=0)
				        			  {
				        				  RecordUpdation(TableName,Min_Sync,Min_SyncDT); // Records Updation 
				        			  }
				        			  else
				        			  {
				        				  if(TableName==Tab4Name)
				        				  {
				        					  RecordInsertion(Tab2Name,Tab2Count,Tab2Max_Mid,Tab2Min_Sync,Tab2Min_SyncDT); // Insert For Table 2
				        				  }
				        				  else if(TableName==Tab2Name)
				        				  {
				        					  RecordInsertion(TabMiscName,TabMiscCount,TabMiscMax_Mid,TabMiscMin_Sync,TabMiscMin_SyncDT); // Insert For Table Misc
				        				  }
				        				  else if(TableName==TabMiscName)
				        				  {
				        					  RecordInsertion(TabFamilyName,TabFamilyCount,TabFamilyMax_Mid,TabFamilyMin_Sync,TabFamilyMin_SyncDT); // Insert For Table Family
				        				  }
				        				  else if(TableName==TabFamilyName)
				        				  {
				        					  RecordInsertion(TabOp1Name,TabOp1Count,TabOp1Max_Mid,TabOp1Min_Sync,TabOp1Min_SyncDT); // Insert For Table Opinion1
				        				  }
				        				  else if(TableName==TabOp1Name)
				        				  {
				        					  RecordInsertion(TabOp2Name,TabOp2Count,TabOp2Max_Mid,TabOp2Min_Sync,TabOp2Min_SyncDT); // Insert For Table Opinion2
				        				  }
				        				  else
				        				  {
				        					  backs();
				        				  }
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
		        // start the background thread
		        background.start();
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
					   String M_Id=rs.getString(2);
					 			
					   StrQry = "Delete from "+TableName+" where M_id="+M_Id;
					   dbase.execSQL(StrQry);
					   progressHandler.sendMessage(progressHandler.obtainMessage());
					 }
					rs.close();
					
					//// Update Sync Required Val in LoginMain
					StrQry="Update LoginMain Set setting_extra_5='"+ChkSync+"' Where ClientID='"+ClientId.trim()+"' AND UID='"+UID.trim()+"' ";
					dbase.execSQL(StrQry);
					////////////////////////
					
					dbase.close(); // Close Local DataBase
					
					runOnUiThread(new Runnable()
					{
					     public void run()
					     {
			        	    backs();// End Synchronizaton
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
	 
	 public boolean onKeyDown(int keyCode, KeyEvent event) 
	 {
	   	if (keyCode == KeyEvent.KEYCODE_BACK) {
	   		//backs();
	   	    return true;
	   	}
	   	return super.onKeyDown(keyCode, event);
	 }
	 
	 
	 @SuppressWarnings("deprecation")
	private void DisplayMsg(String head,String body){
		 AlertDialog ad=new AlertDialog.Builder(this).create();	
		 ad.setTitle( Html.fromHtml("<font color='#E3256B'>"+head+"</font>"));
	     ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>"+body+"</font>"));
		 ad.setButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	backs();
	            }
	     });
	     ad.show();	
	}
	 
	 
	 public void backs(){
		//background.stop();
		try {
			conn.close(); // Close the Server Connection
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(Exception ex)
		{
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		dialog.dismiss(); // Dismiss Progress Bar
		Intent MainBtnIntent= new Intent(context,MenuPage.class);
		MainBtnIntent.putExtra("AppLogo", AppLogo);
    	startActivity(MainBtnIntent);
    	finish();
	}
}
