package group.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Sync_M_S 
{
	private String ClientID,LogId,UserType,Tab2Name,Tab4Name,TableNameEvent;
	private Chkconnection chkconn;
	private boolean InternetPresent;
	private Context context;
	private Cursor cursorT;
	private WebServiceCall webcall;
	
	Sync_M_S(Context context)
	{
		this.context=context;
		Get_SharedPref_Values();//Get Values from Saved Shared Preference
		
		Tab2Name="C_"+ClientID+"_2";//Table 2
		Tab4Name="C_"+ClientID+"_4";//Table 4
		TableNameEvent="C_"+ClientID+"_Event";// Table Event where we save Event Attend or not Confirmation
		
		webcall=new WebServiceCall();//Intialise WebserviceCall Object
		chkconn=new Chkconnection();//Intialise Chkconnection Object
	}
	
	
	
	//Get Data from Saved Shared Preference
	private void Get_SharedPref_Values()
	{
		SharedPreferences ShPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		 
		  if (ShPref.contains("clientid"))
	      {
			  ClientID=ShPref.getString("clientid", "");
	      }
	      if (ShPref.contains("cltid"))
	      {
	    	  LogId=ShPref.getString("cltid", "");
          } 
	      if (ShPref.contains("UserType"))
		  {
			  UserType=ShPref.getString("UserType", "");
		  }
	}
	
	
	
	
	//Sync(M-S) Read News/Event/Both when get internet connection
	public void Sync_ReadNewsEvent(String TType)
	{
	   InternetPresent =chkconn.isConnectingToInternet(context);

	   if(InternetPresent==true)
	   {
		 try
		 {
		   SQLiteDatabase DBObj = context.openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
		    
		   String Sqlqry="",News_Mids="0",Event_Mids="0",TempMS="";
		  
		   if(UserType.equals("SPOUSE")){
			 TempMS="S";
		   }
		   else{
			 TempMS="M"; 
		   }
		 
		   if(TType.equals("News") || TType.equals("Both"))
		   {
		      ////Get All M_Ids of Read News 
		      Sqlqry="Select M_Id from "+Tab4Name+" Where Rtype='News' AND Num2=1"; 
		      cursorT = DBObj.rawQuery(Sqlqry, null);
		      if (cursorT.moveToFirst()) {
	            do {
	        	   News_Mids=News_Mids+","+ cursorT.getInt(0); 
	            }while (cursorT.moveToNext());
		      }
		      cursorT.close();
		      ////////////////////////////////
		   }
		 
		   
		   if(TType.equals("Event") || TType.equals("Both"))
		   {
		     ////Get All M_Ids of Read Event 
		     Sqlqry="Select M_Id from "+Tab4Name+" Where Rtype='Event' AND Text8='1'"; 
		     cursorT = DBObj.rawQuery(Sqlqry, null);
		     if (cursorT.moveToFirst()) {
		       do {
			      Event_Mids=Event_Mids+","+ cursorT.getInt(0);
		       }while (cursorT.moveToNext());
		     }
		     cursorT.close();
		     ////////////////////////////////
		   }

		   DBObj.close();//Close DataBase
		 
		  
		   //Sync News Read
		   if(News_Mids.contains(",")){
			 Sync_WebCallReadNewsEvent(TempMS,"News",News_Mids);
		   } 
		 
		   //Sync Event Read
		   if(Event_Mids.contains(",")){
			 Sync_WebCallReadNewsEvent(TempMS,"Event",Event_Mids);
		   }
	   }
	   catch(Exception ex){
		  String S=ex.getMessage();
	   }   
	  }
	}
	
	//Sync(M-S) Read News/Event Call WebService
	private void Sync_WebCallReadNewsEvent(final String TempMS,final String TempNE,final String Mids)
	{
		Thread T1 = new Thread()
        {
          public void run(){
            try
            {
              String R=webcall.Read_NewsEvents(ClientID, LogId, TempMS, TempNE, Mids);
              
              String Qry="";
              SQLiteDatabase DBObj = context.openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
       		  if(R.equals("SavedNews"))
       		  {
       		    Qry="Update "+Tab4Name+" Set Num2=2 Where Rtype='News' AND M_ID in ("+Mids+")"; 
       		    DBObj.execSQL(Qry);
       		  }else if(R.equals("SavedEvent")){
       			Qry="Update "+Tab4Name+" Set Text8='2' Where Rtype='Event' AND M_ID in ("+Mids+")"; 
       			DBObj.execSQL(Qry);
       		  }
       		  DBObj.close();//Close DataBase
            }
            catch (Exception ex){
              	// System.out.println("AAAAA  :@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@: ");
           	    System.out.println(ex.getMessage());
            }
          }
        };
        T1.start();
	}
	
	
	//call function for sync data Add_News(Added News by Admin) to mobile to server when User is ONLINE
	public void Sync_Add_News()
	{
		Thread T2 = new Thread() {
		@Override
		public void run() {
		  try {
			    InternetPresent =chkconn.isConnectingToInternet(context);
				if(InternetPresent==true)
				{
				  SQLiteDatabase DBObj = context.openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
				  String SqlQry="Select M_id,Text1,Text2,Add1,Text8,Text7 from "+Tab4Name+" Where Rtype='Add_News' order by M_id";
				  Cursor cursorT = DBObj.rawQuery(SqlQry, null);
				  int Mid=0;
				  String DDate,Title,Desc,GrpIds,SendSMS,WebResult;
				  while(cursorT.moveToNext())
				  {
					Mid =cursorT.getInt(0);
					DDate=cursorT.getString(1);
					Title=cursorT.getString(2);
					Desc=cursorT.getString(3);
					GrpIds=cursorT.getString(4);
					SendSMS=cursorT.getString(5);
					WebResult=webcall.Sync_Add_News(ClientID, DDate, Title, Desc,GrpIds,SendSMS);
					if(WebResult.contains("Record Saved"))
					{
						SqlQry="Delete from "+Tab4Name+" Where Rtype='Add_News' and M_id="+Mid;
						DBObj.execSQL(SqlQry);
					}
				 }
				 cursorT.close();
				 DBObj.close();
			   }
			}
		  	catch (Exception e) {
		  		//System.out.println(e.getMessage()); 
		  		e.printStackTrace();
		  	}
		}
	  };
	  T2.start();
	}
	
	
	//call function for sync data Add_Event(Added Event by Admin) to mobile to server when User is ONLINE
	public void Sync_Add_Events()
	{
		Thread T5 = new Thread() {
		@Override
		public void run() {
		  try {
			    InternetPresent =chkconn.isConnectingToInternet(context);
				if(InternetPresent==true)
				{
				  SQLiteDatabase DBObj = context.openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
				  
				  String SqlQry="Select M_id,Text1,Text2,Add1,Text7,Text8 from "+Tab4Name+" Where Rtype='Add_Event' order by M_id";
				  
				  Cursor cursorT = DBObj.rawQuery(SqlQry, null);
				  int Mid=0;
				  String EDateTime,EName,EDesc,EventAllOtherData,EVenue,GrpIds="",WebResult="",EventConfirmWithGroupIDs="",EmailFormat="",EventDirectrsData="";
				  while(cursorT.moveToNext())
				  {
					Mid =cursorT.getInt(0);
					EDateTime=cursorT.getString(1);
					EName=cursorT.getString(2);
					EDesc=cursorT.getString(3);
					EVenue=cursorT.getString(4);
					EventAllOtherData=cursorT.getString(5);
					
					if(EventAllOtherData.contains("#@#"))
					{
						String[] Arr1=EventAllOtherData.split("#@#");
						GrpIds=Arr1[0].trim();
						EventConfirmWithGroupIDs=Arr1[1].trim();
						EmailFormat=Arr1[2].trim();
						EventDirectrsData=Arr1[3].trim();
					}
					
					WebResult=webcall.Sync_Add_Events(ClientID, EDateTime, EName, EDesc,EVenue,GrpIds,EventConfirmWithGroupIDs,EmailFormat,EventDirectrsData);
					if(WebResult.contains("Saved"))
					{
						SqlQry="Delete from "+Tab4Name+" Where Rtype='Add_Event' and M_id="+Mid;
						DBObj.execSQL(SqlQry);
					}
				 }
				 cursorT.close();
				 DBObj.close();
			   }
			}
		  	catch (Exception e) {
		  		//System.out.println(e.getMessage()); 
		  		e.printStackTrace();
		  	}
		}
	  };
	  T5.start();
	}
	
	
	 /// Sync (M-S) Event Attended or Not (27-05-2016).
	 public void Sync_EventAttend_Or_Not_Confirmation()
	 {
		 Thread T3 = new Thread() {
		  @Override
			public void run() {
			  try {
			     InternetPresent =chkconn.isConnectingToInternet(context);
				 if(InternetPresent==true)
				 {
					 String TempMS;
					 if(UserType.equals("SPOUSE")){
						 TempMS="S";
					 }
					 else{
						 TempMS="M"; 
					 }
					 String sqlSearch="select Num,Num1 from "+TableNameEvent+" where Rtype='Eve_Acc' and Sync=1"; 
					 SQLiteDatabase db = context.openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
					 cursorT = db.rawQuery(sqlSearch, null);
					 
					 if(cursorT.moveToFirst())
					 {
						 do {
							 int TempAns= cursorT.getInt(0);
							 int TempEventID= cursorT.getInt(1);

					         String WebR=webcall.clubeventconfirm(ClientID,String.valueOf(TempEventID),LogId,String.valueOf(TempAns),TempMS);
					         
					         if(WebR.equalsIgnoreCase("Saved")){
					        	String sql= "update "+TableNameEvent+" Set Sync=0 where Num1="+TempEventID;
					        	db.execSQL(sql);
					         }
						 }while (cursorT.moveToNext());
					 }
					 cursorT.close();
					 db.close();
				 }
			}
		  	catch (Exception e) {
		  		System.out.println(e.getMessage()); 
		  		e.printStackTrace();
		  	}
		}
	  };
	  T3.start();
   } 
	 
	 
	 
	//Sync Opinion Poll Data M-S ////
	 public void Sync_OpPoll_Data()
	 {
	  	Thread T2 = new Thread() {
	  	@Override
	  	public void run() {
	  	 try {
	  		   InternetPresent =chkconn.isConnectingToInternet(context);
	  		   if(InternetPresent==true)
	  		   {
	  			  SQLiteDatabase db = context.openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
	  				  
	  			  String SqlQry="Select Distinct OP1_ID from C_"+ClientID+"_OP3 Where Submit=1 AND SyncID=1 Order by OP1_ID";
	  			  Cursor cursorT = db.rawQuery(SqlQry, null);
	  			  String Op1Ids="";
	  			  while(cursorT.moveToNext())
	  			  {
	  				Op1Ids=Op1Ids+cursorT.getInt(0)+"#";
	  			  }
	  			  cursorT.close();
	  				  
	  			  String[] ArrOp1Id=null;
	  			  if(Op1Ids.length()>1)
	  			  {
	  				Op1Ids=Op1Ids.substring(0,Op1Ids.length()-1);
	  				ArrOp1Id=Op1Ids.split("#");
	  			  }
	  				  
	  			  if(ArrOp1Id!=null)
	  			  {
	  				 for(int i=0;i<ArrOp1Id.length;i++) 
	  				 {
	  				     String Op1_Id=ArrOp1Id[i].trim();
	  					 
	  				     SqlQry="Select OP2_ID,User_Ans,Remark from C_"+ClientID+"_OP3 Where OP1_ID="+Op1_Id+" AND Submit=1 AND SyncID=1 Order by OP2_ID";
	  				     cursorT = db.rawQuery(SqlQry, null);
	  				     String UAns,Remark,SData="",WebResult;
	  				     int Op2Id;
	  				     while(cursorT.moveToNext())
	  				     {
	  					   Op2Id=cursorT.getInt(0);
	  					   UAns=cursorT.getString(1);
	  					   Remark=cursorT.getString(2);
	  					   SData=SData+Op2Id+"^"+UAns+"^"+Remark+"@@";
	  				     }
	  				     cursorT.close();
	  				 
	  				     if(SData.length()>2)
	  				     {
	  					   SData=SData.substring(0,SData.length()-2);
	  					
	  					   String TempMS="M";
	  					   if(UserType.equals("SPOUSE")){
	  					     TempMS="S";
	  					   }
	  					
	  					   SData=LogId+"#"+TempMS+"#"+Op1_Id+"#"+SData;
	  					
	  					   WebServiceCall webcall=new WebServiceCall();
	  				       WebResult=webcall.Sync_OpinionPoll_MS(ClientID, SData);
						   if(WebResult.contains("Saved"))
						   {
							  SqlQry="Update C_"+ClientID+"_OP3 Set SyncID=0 Where OP1_ID="+Op1_Id;
	  				          db.execSQL(SqlQry);
						   }
	  				     }
	  				}
	  			  }
				  db.close();
	  		  }
	  			  
	  		}
	  		catch (Exception e) {
	  		  //System.out.println(e.getMessage()); 
	  		  e.printStackTrace();
	  		}
	  	  }
	  	};
	  	T2.start();
	 }

}
