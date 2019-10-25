package group.manager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class Add_Event extends Activity{

	final Context context=this;
	byte[] AppLogo;
	EditText txtEventName,txtDesc,txtVenue;
	TextView txtDate,txtTime;
	CheckBox chkSendGroup,chkEventConfirmation,chkEmail;
	Spinner Sp_EmailFormat;
	ImageView btnSubmit;
	SimpleDateFormat df,df1;
	AlertDialog ad;
	Button btnPendingEvent,btnAddEventDirectors;
	String ClubName,ClientId,Tab4Name,TabGroup,Log,Logid,addchk,Msg="";
	WebServiceCall webcall;
	SQLiteDatabase db;
	ProgressDialog Progsdial;
	ArrayList<Product>products,products1;
	Adapter_News_Group AdpGroup=null,AdpConfirmationGroup=null;
	ArrayList<RowEnvt> List_EventDirectors;
	String WebResult="";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        
        txtDate=(TextView)findViewById(R.id.EdDate);
        txtTime=(TextView)findViewById(R.id.EdTime);
		txtEventName=(EditText)findViewById(R.id.EDEventName);
		txtDesc = (EditText) findViewById(R.id.EDDesc);
		txtVenue = (EditText) findViewById(R.id.EDVenue);
		btnSubmit=(ImageView)findViewById(R.id.imgSubmit);
		btnPendingEvent=(Button)findViewById(R.id.btnPendingEvent);
		btnAddEventDirectors=(Button)findViewById(R.id.btnAddEventDirectors);
        chkSendGroup = (CheckBox) findViewById(R.id.chkSendGroup);
		chkEventConfirmation = (CheckBox) findViewById(R.id.chkEventConfirmation);
		chkEmail = (CheckBox) findViewById(R.id.chkEmail);
		Sp_EmailFormat=(Spinner) findViewById(R.id.Sp_EmailFormat);
		
		String[] Arr={"format-1","format-2","format-3","format-4"};
		ArrayAdapter<String> adp1= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Arr);
 		adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 		Sp_EmailFormat.setAdapter(adp1);
 		
 		Sp_EmailFormat.setVisibility(View.GONE);//hide Email format spinner
		
        Intent menuIntent = getIntent(); 
        addchk =  menuIntent.getStringExtra("addchk");
        Log =  menuIntent.getStringExtra("Clt_Log");
        Logid =  menuIntent.getStringExtra("Clt_LogID");
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		ClientId =  menuIntent.getStringExtra("UserClubName");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		
		Tab4Name="C_"+ClientId+"_4";
		TabGroup="C_"+ClientId+"_Group";// Table Group
		
		//chkSendGroup.setVisibility(View.GONE);//
		
		webcall=new WebServiceCall();//Call a Webservice
        ad=new AlertDialog.Builder(this).create();
		
		products =  new ArrayList<Product>();
		products1= new ArrayList<Product>();
		List_EventDirectors = new ArrayList<RowEnvt>();
		
		Set_App_Logo_Title(); // Set App Logo and Title
		
		df = new SimpleDateFormat("dd-MM-yyyy");///Date Format
		df1 = new SimpleDateFormat("hh:mm a");//Time Format
		
		// set Current Date
		Date CurDt = new Date();
		txtDate.setText(df.format(CurDt));// set Current Date
		txtTime.setText(df1.format(CurDt));// set Current Time
		/////////////////////
		
		//Check Event Pending(NotUpdated) or Uptodate(Updated) 
		ChkPendingEvents();
		
	    ////// Show Email CheckBox or Not
		boolean ShowEmail=false;
		db=openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		String Qry="Select M_Id from "+Tab4Name+" Where Rtype='ShowEmail'";
		Cursor cursorT = db.rawQuery(Qry, null);
		if (cursorT.moveToFirst()) { 
			ShowEmail=true;
		}
		cursorT.close();
		db.close();// Close Connection
		
		if(!ShowEmail)
		  chkEmail.setVisibility(View.INVISIBLE);
		/////////////////////////////////
		
		
		WebCall_GetAllGroup();//Get All Group from Webservice
		
		txtDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,int count) {
                    // TODO Auto-generated method stub
            	Msg = txtDesc.getText().toString().trim();
				if ((txtDesc.getText().toString().contains("\""))||(txtDesc.getText().toString().contains("'"))){
					SpecialCharAlert();
				}
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
                // TODO Auto-generated method stub 
            }
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub	
			}
    });
		
		
		txtDate.setOnClickListener(new OnClickListener()
        { 
			@Override
			public void onClick(View arg0) {
				 final Dialog dialog = new Dialog(context);
			       dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
		  		   dialog.setContentView(R.layout.setddate);
		  		   dialog.setCancelable(false);
		  		   dialog.show();
		  		   final DatePicker Datepic=(DatePicker)dialog.findViewById(R.id.datePicker1);
		  		   //Set Date in the DatePicker
		  		   Calendar cal=Calendar.getInstance();
		  		   int year=cal.get(Calendar.YEAR);
		  		   int month=cal.get(Calendar.MONTH);
		  		   int day=cal.get(Calendar.DAY_OF_MONTH);
		  		   Datepic.updateDate(year, month, day);
		  		   ////////////////////////////////////
		  		   Button BtnOk=(Button)dialog.findViewById(R.id.btnOK);
		  		   Button BtnCancel=(Button)dialog.findViewById(R.id.btnCancel);
		  		   
		  		   BtnOk.setOnClickListener(new OnClickListener() {    	
		          	 @Override
		          	 public void onClick(View v) {
		          	  Date PicDate=new Date((Datepic.getYear()-1900),Datepic.getMonth(),Datepic.getDayOfMonth());
		              String Setdate = df.format(PicDate);
		              txtDate.setText(Setdate); // Set Date 
		          	  dialog.dismiss();
		          	 }
		           });
		  			
		  		   BtnCancel.setOnClickListener(new OnClickListener() {    	
		             @Override
		             public void onClick(View v) {
		              dialog.dismiss();
		             }
		           });
			}
		});
		
		///Event Time 
		txtTime.setOnClickListener(new OnClickListener()
        { 
			@Override
			public void onClick(View arg0) {
				Set_EventTimeDialog();
			}
		});
		
		
		btnSubmit.setOnClickListener(new OnClickListener()
        { 
			@Override
			public void onClick(View arg0) {
				
				String EDate= txtDate.getText().toString().trim();
				String ETime= txtTime.getText().toString().trim();
			    String EName= txtEventName.getText().toString().trim();
				String EDesc= txtDesc.getText().toString().trim();
				String EVenue= txtVenue.getText().toString().trim();
				if(EDate.length()==0){
					txtDate.setError("Input Date");	
				}else if(ETime.length()==0){
					txtTime.setError("Input Time");	
				}else if(EName.length()==0){
					txtEventName.setError("Input Event Name");	
				}else if(EDesc.length()==0){
					txtDesc.setError("Input Description");
				}else if(EVenue.length()==0){
					txtVenue.setError("Input Venue");
				}else{
					
					//// Get Selected Groups and Confirmation Group
					String EEmailFormat=" ", StrGroupIds=" ",StrConfirmationGroupIds=" ",EventDirData=" ";
					int ChkEConfirmation=0;
					
					if(AdpGroup!=null)
					{
				       for (Product p : AdpGroup.getBox()) 
				       {
				         if (p.box)
				    	   StrGroupIds+=p.GroupId+","; 
				       }
					}
					
					if(AdpConfirmationGroup!=null)
					{
				       for (Product p : AdpConfirmationGroup.getBox()) 
				       {
				         if (p.box)
				    	   StrConfirmationGroupIds+=p.GroupId+","; 
				       }
					}
				    
				    if(StrGroupIds.contains(","))
				    	StrGroupIds=StrGroupIds.substring(0,StrGroupIds.length()-1);
				    
				    if(StrConfirmationGroupIds.contains(","))
				    	StrConfirmationGroupIds=StrConfirmationGroupIds.substring(0,StrConfirmationGroupIds.length()-1);
				    /////////////////
				    
				    //// Check Event Confirmation is true/false
				    if(chkEventConfirmation.isChecked()){
				    	ChkEConfirmation=1;
					}
				    
				    ////If Email Checkbox checked then get Email Format
				    if(chkEmail.isChecked()){
				    	EEmailFormat=Sp_EmailFormat.getSelectedItem().toString();
				    	EEmailFormat=EEmailFormat.replace("format-", "").trim();
					}
				    
				    
				    ////Get Event Directors Data if have
				    if(List_EventDirectors.size()!=0)
				    {
				    	String DName="",DDesig="",DEmail="",DMob="";
				    	for(int i=0;i<List_EventDirectors.size();i++)
				    	{
				    		DName=List_EventDirectors.get(i).EvtName.toString().trim();
				    		DDesig=List_EventDirectors.get(i).EvtDesc.toString().trim();
				    		DEmail=List_EventDirectors.get(i).Evtdate.toString().trim();
				    		DMob=List_EventDirectors.get(i).EvtVenue.toString().trim();
				    		EventDirData=EventDirData+DName+"^"+DDesig+"^"+DEmail+"^"+DMob+"#";
				    	}
				    	EventDirData=EventDirData.substring(0,EventDirData.length()-1);
				    }
				    
					//Insert Data in local Table
					db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
					String Qry="Select Min(M_id) from "+Tab4Name+"";
					Cursor cursorT = db.rawQuery(Qry, null);
					int Min_Mid=0;
					while(cursorT.moveToNext())
					{
					   Min_Mid =cursorT.getInt(0);
					   break;
					}
					cursorT.close();
					
					Min_Mid=Min_Mid-1000;
						
					
					//////Convert Date in 01-Jan-1900(dd-MMM-yyyy) format////
					SimpleDateFormat df_New = new SimpleDateFormat("dd-MMM-yyyy");
			        Date Ddate=null;
					try {
						Ddate = df.parse(EDate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
			        EDate=df_New.format(Ddate);
					/////////////////////////////////////////
					
					String EDateTime=EDate+" "+ETime;
					
					String EventConfirmWithGroupIDs=ChkEConfirmation+"^"+StrConfirmationGroupIds.trim();
					
					String AllOtherData=StrGroupIds+"#@#"+EventConfirmWithGroupIDs+"#@#"+EEmailFormat+"#@#"+EventDirData;
					
					Qry="Insert into "+Tab4Name+"(M_id,Rtype,Text1,Text2,Add1,Text7,Text8) Values("+Min_Mid+",'Add_Event','"+EDateTime+"','"+EName+"','"+EDesc+"','"+EVenue+"','"+AllOtherData+"')";
           		    db.execSQL(Qry);
           		    db.close();
           		    
           		    Sync_Add_Event(Min_Mid+"");// Sync M-S Add Event 
				}
			}
        });
		
		
		
		//Add Event Directors Click Button 
		btnAddEventDirectors.setOnClickListener(new OnClickListener() {    	
             @Override
             public void onClick(View v) {
            	 Show_AddEventDirectors();
             }
        });
		
		
		//chkSendGroup Click event
		chkSendGroup.setOnClickListener(new OnClickListener() {    	
             @Override
             public void onClick(View v) {
            	 if(chkSendGroup.isChecked())
            		ShowSelectGroupDialog();
 				else
 					AdpGroup=null;
             }
        });
		
		//chkEventConfirmation Click event
		chkEventConfirmation.setOnClickListener(new OnClickListener()
        { 
			@Override
			public void onClick(View arg0) {
				if(chkEventConfirmation.isChecked())
					ShowEventConfirmationSelectionDialog();
				else
					AdpConfirmationGroup=null;
			}
        });
		
		
		//chkEmail Click event
		chkEmail.setOnClickListener(new OnClickListener()
        { 
			@Override
			public void onClick(View arg0) {
				if(chkEmail.isChecked())
					Sp_EmailFormat.setVisibility(View.VISIBLE);//Visible Email format spinner
				else
					Sp_EmailFormat.setVisibility(View.GONE);//hide Email format spinner
			}
        });
		
		
		btnPendingEvent.setOnClickListener(new OnClickListener()
        { 
			@Override
			public void onClick(View arg0) {
				//Intent iIntent= new Intent(getBaseContext(),AffiliationAPP.class);
				/*Intent iIntent= new Intent(getBaseContext(),NewsMain.class);
				iIntent.putExtra("Count",22);
				iIntent.putExtra("POstion",0);
				iIntent.putExtra("Clt_Log",Log);
				iIntent.putExtra("Clt_LogID",Logid);
				iIntent.putExtra("Clt_ClubName",ClubName);
				iIntent.putExtra("UserClubName",Str_user);
				iIntent.putExtra("AppLogo", AppLogo);
			    startActivity(iIntent);
			    finish();*/
			}
        });
	}
	

	
	//Show Set Event Time Dialog box 
	private void Set_EventTimeDialog()
	{
	   final Dialog dialog = new Dialog(context);
       dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
	   dialog.setContentView(R.layout.settime);
	   dialog.setCancelable(false);
	   dialog.show();
	   
	   final TimePicker TP=(TimePicker)dialog.findViewById(R.id.timePicker1);
	   Button BtnOk=(Button)dialog.findViewById(R.id.btnOK);
	   Button BtnCancel=(Button)dialog.findViewById(R.id.btnCancel);

	   Calendar cal=Calendar.getInstance();
 	   int hhs=cal.get(Calendar.HOUR_OF_DAY);
 	   int mms=cal.get(Calendar.MINUTE);
		  
	   TP.setCurrentHour(hhs);
	   TP.setCurrentMinute(mms);

	   BtnOk.setOnClickListener(new OnClickListener() {    	
   	    @Override
   	    public void onClick(View v) {
   	    	
   	       Calendar cal = Calendar.getInstance();
   	       cal.set(Calendar.HOUR_OF_DAY,TP.getCurrentHour());
   	       cal.set(Calendar.MINUTE,TP.getCurrentMinute());
   	       cal.set(Calendar.SECOND,0);

   	       Date DT = cal.getTime();	
   	    	
          String SetTime = df1.format(DT);
          txtTime.setText(SetTime); // Set Time 
      	  dialog.dismiss();
   	   }
      });
		
	  BtnCancel.setOnClickListener(new OnClickListener() {    	
        @Override
        public void onClick(View v) {
          dialog.dismiss();
        }
      });
	}
	
	
	
	//Check Event Pending(NotUpdated) or Uptodate(Updated) 
	private void ChkPendingEvents()
	{
		db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
		String SqlQry="Select Count(M_id) from "+Tab4Name+" Where Rtype='Add_Event'";
		Cursor cursorT = db.rawQuery(SqlQry, null);
		int TCount=0;
		while(cursorT.moveToNext())
		{
		   TCount =cursorT.getInt(0);
		   break;
		}
		cursorT.close();
		db.close();
		
		if(TCount==0){
			btnPendingEvent.setVisibility(View.GONE);
		}
		else{
			btnPendingEvent.setVisibility(View.VISIBLE);
			btnPendingEvent.setText(Html.fromHtml("Pending Event (<font color='#FFFF00'><b>"+TCount+"</b></font>)"));
		}
	}
	
	
	 // Get All GroupName with Id ///////////
	 public void WebCall_GetAllGroup()
     {
		progressdial();
		Thread T1 = new Thread() {
		 @Override
		 public void run() {
            try
              {
		         db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
	             
		         //Create Local Table Group for Add News to Selected Group
		  	     db.execSQL("CREATE TABLE IF NOT EXISTS "+TabGroup+" (M_ID INTEGER PRIMARY KEY,GroupId INTEGER,GroupName Text,A1 Text,A2 Text,A3 Text)");
		         
		         String WResult=webcall.GetAllGroup(ClientId);
		         if(WResult.contains("^"))
	             {
	        	   String[] SArr=WResult.split("#");
	        	   
	        	   String Qry = "Delete from "+TabGroup;
        		   db.execSQL(Qry);
        		   
	        	   for(int i=0;i<SArr.length;i++)
	        	   {
	        		   String[] Arr=SArr[i].replace("^", "#").split("#");
	        		   String GroupName=Arr[0].trim();
	        		   String GroupId=Arr[1].trim();
	        		   
	        		   Qry = "Insert into "+TabGroup+"(GroupId,GroupName) Values("+GroupId+",'"+GroupName+"')";
	        		   db.execSQL(Qry);
	        	   }
	             }
		         else if(WResult.contains("No Record Found")){
		        	 String Qry = "Delete from "+TabGroup;
	        		 db.execSQL(Qry);
		         }
	     		 db.close();//Close DB
	     		 
	     		runOnUiThread(new Runnable()
		        {
	            	 public void run()
	            	 {
	            		 GetGroupList();
	            	 }
		        });
             }
	  		 catch (Exception e) {
	  			e.printStackTrace();
	  		 }
             Progsdial.dismiss();
           }
        };
        T1.start();
	}
	
	 
	 // Get/Fill GroupName with Ids from Local Table Group
	 private void GetGroupList()
	 {
		 db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		 
 	     //Get Group Table Data
 		 String qry="Select GroupId,GroupName from "+TabGroup+" Order by Upper(GroupName)";
 		 Cursor cursorT = db.rawQuery(qry, null);
 		 int RCount=cursorT.getCount();
 		 if(RCount>0)
 		 {
 			//chkSendGroup.setVisibility(View.VISIBLE);
		    if (cursorT.moveToFirst()) {
			   do {
					int GroupId =cursorT.getInt(0);
					String GroupName =cursorT.getString(1);
					products.add(new Product(GroupName,GroupId,false)); 
			    } while (cursorT.moveToNext());
 		    }
 		 }
 		 cursorT.close();
 		 db.close();
	 }
	 
	
	// Display Popup Screen of Group List
	private void ShowSelectGroupDialog()
	{
   	     final Dialog dialog = new Dialog(context);
	     dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
 		 dialog.setContentView(R.layout.additional_data);
 		 dialog.setCancelable(false);
 		 dialog.show();
 		 
 		 TextView TvHead=(TextView)dialog.findViewById(R.id.tvTt);
 		 ListView Lv1=(ListView)dialog.findViewById(R.id.Lv1);
 		 Button btnBack=(Button)dialog.findViewById(R.id.btnBack);
        
 		 TvHead.setText(Html.fromHtml("<b>Select Group</b>"));
 		 		 
		 if(products.size()!=0)
		 {
			AdpGroup = new Adapter_News_Group(this, products);
			Lv1.setVisibility(View.VISIBLE);
			Lv1.setAdapter(AdpGroup);
	     }
 		
 		btnBack.setOnClickListener(new OnClickListener() {    	
            @Override
            public void onClick(View v) {
             
             if(AdpGroup!=null)
       		 {
       		   products1.clear();	 
       	       for (Product p : AdpGroup.getBox()) 
       	       {
       	         if (p.box) 
       				products1.add(new Product(p.name,p.GroupId,true)); 
       	       }
       		 }
            	
              dialog.dismiss();
            }
        });	
	}
	
	
	// Display Popup Screen of Add Event Directors
	private void Show_AddEventDirectors()
	{
   	     final Dialog dialog = new Dialog(context);
	     dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
 		 dialog.setContentView(R.layout.add_event_directors);
 		 dialog.setCancelable(false);
 		 dialog.show();
 		 
 		 final EditText txtDirName=(EditText)dialog.findViewById(R.id.EDDirectorName);
 		 final EditText txtDesig=(EditText)dialog.findViewById(R.id.EDDesig);
 		 final EditText txtEmail=(EditText)dialog.findViewById(R.id.EDEmail);
 		 final EditText txtMob=(EditText)dialog.findViewById(R.id.EDMob);
 		 final ListView Lv1=(ListView)dialog.findViewById(R.id.Lv1);
 		 Button btnAdd=(Button)dialog.findViewById(R.id.btnAdd);
 		 Button btnBack=(Button)dialog.findViewById(R.id.btnBack);
 		 
 		 if(List_EventDirectors.size()!=0){
 			CustomAffil Adp = new CustomAffil(context,R.layout.affiliationlist,List_EventDirectors);
			Lv1.setAdapter(Adp); 
 		 }
        
 		 btnAdd.setOnClickListener(new OnClickListener() {    	
            @Override
            public void onClick(View v) {
            	String DirName= txtDirName.getText().toString().trim();
				String Desig= txtDesig.getText().toString().trim();
			    String Email= txtEmail.getText().toString().trim();
				String Mob= txtMob.getText().toString().trim();
				if(DirName.length()==0){
					txtDirName.setError("Input Name");	
				}else{
					RowEnvt item = new RowEnvt(DirName,Desig,Email,Mob);
					List_EventDirectors.add(item);
					    
					CustomAffil Adp = new CustomAffil(context,R.layout.affiliationlist,List_EventDirectors);
					Lv1.setAdapter(Adp); 
					
					txtDirName.setText("");
					txtDesig.setText("");
					txtEmail.setText("");
					txtMob.setText("");
				}
            }
         });
 		 
 		 btnBack.setOnClickListener(new OnClickListener() {    	
            @Override
            public void onClick(View v) {
              dialog.dismiss();
            }
         });	
	}
	
	
	// Display Popup Screen of Event Confirmation selection
	private void ShowEventConfirmationSelectionDialog()
	{
   	     final Dialog dialog = new Dialog(context);
	     dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
 		 dialog.setContentView(R.layout.additional_data);
 		 dialog.setCancelable(false);
 		 dialog.show();
 		 
 		 TextView TvHead=(TextView)dialog.findViewById(R.id.tvTt);
 		 ListView Lv1=(ListView)dialog.findViewById(R.id.Lv1);
 		 Button btnBack=(Button)dialog.findViewById(R.id.btnBack);
        
 		 TvHead.setText(Html.fromHtml("<b>Select Group</b>"));
 		 		
		 if(products1.size()!=0)
		 {
			AdpConfirmationGroup = new Adapter_News_Group(this, products1);
			Lv1.setVisibility(View.VISIBLE);
			Lv1.setAdapter(AdpConfirmationGroup);
	     }
 		
 		btnBack.setOnClickListener(new OnClickListener() {    	
            @Override
            public void onClick(View v) {
              dialog.dismiss();
            }
        });	
	}
	
	
	
	//Sync Add Event M-S ////
	public void Sync_Add_Event(final String M_Id)
	{
		progressdial();
		Thread T2 = new Thread() {
		@Override
		public void run() {
		  try {
			    Chkconnection chkconn=new Chkconnection();//Intialise Chkconnection Object
			    boolean InternetPresent =chkconn.isConnectingToInternet(context);
				if(InternetPresent==true)
				{
				  SQLiteDatabase DBObj = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
				  String SqlQry="Select Text1,Text2,Add1,Text7,Text8 from "+Tab4Name+" Where Rtype='Add_Event' AND M_Id="+M_Id;
				  Cursor cursorT = DBObj.rawQuery(SqlQry, null);
				  String EDateTime,EName,EDesc,EventAllOtherData,EVenue,GrpIds="",EventConfirmWithGroupIDs="",EmailFormat="",EventDirectrsData="";
				  if(cursorT.moveToFirst())
				  {
					EDateTime=cursorT.getString(0);
					EName=cursorT.getString(1);
					EDesc=cursorT.getString(2);
					EVenue=cursorT.getString(3);
					EventAllOtherData=cursorT.getString(4);
					
					if(EventAllOtherData.contains("#@#"))
					{
						String[] Arr1=EventAllOtherData.split("#@#");
						GrpIds=Arr1[0].trim();
						EventConfirmWithGroupIDs=Arr1[1].trim();
						EmailFormat=Arr1[2].trim();
						EventDirectrsData=Arr1[3].trim();
					}
					
					WebResult=webcall.Sync_Add_Events(ClientId, EDateTime, EName, EDesc,EVenue,GrpIds,EventConfirmWithGroupIDs,EmailFormat,EventDirectrsData);
					if(WebResult.contains("Saved"))
					{
						SqlQry="Delete from "+Tab4Name+" Where Rtype='Add_Event' and M_id="+M_Id;
						DBObj.execSQL(SqlQry);
					}
				 }
				 cursorT.close();
				 DBObj.close();
			   }
				
			   runOnUiThread(new Runnable()
	           {
	            	 public void run()
	            	 {
	            		 //if(WebResult.contains("Record Saved"))
	            		     AlertDisplay("Result","Event Added Successfully !",true);
	            		 //else
	            			// AlertDisplay("Result","Something went wrong to add event.\n Please try later",false);
	            			 
	            	 }
	           });
			}
		  	catch (Exception e) {
		  		//System.out.println(e.getMessage()); 
		  		e.printStackTrace();
		  	}
		  Progsdial.dismiss();
		}
	  };
	  T2.start();
	}
	
	
	protected void progressdial()
    {
    	Progsdial = new ProgressDialog(this, R.style.MyTheme);
    	Progsdial.setMessage("Please Wait....");
    	Progsdial.setIndeterminate(true);
    	Progsdial.setCancelable(false);
    	Progsdial.getWindow().setGravity(Gravity.DISPLAY_CLIP_VERTICAL);
    	Progsdial.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    	Progsdial.show();
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
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
	   	if (keyCode == KeyEvent.KEYCODE_BACK) {
	   		backs();
	   	    return true;
	   	}
	   	return super.onKeyDown(keyCode, event);
	 }
	  
	 
	 public void backs(){
		 if(addchk.equals("2")){
			 Intent MainBtnIntent= new Intent(context,UlilitiesList.class);
				MainBtnIntent.putExtra("AppLogo", AppLogo);
				MainBtnIntent.putExtra("CondChk", "2");
		   	    startActivity(MainBtnIntent);
		   	    finish();    
		 }else{
			Intent MainBtnIntent= new Intent(context,MenuPage.class);
			MainBtnIntent.putExtra("AppLogo", AppLogo);
	   	    startActivity(MainBtnIntent);
	   	    finish(); 
		 }
	}
	 
	 private void AlertDisplay(String head,String body,final boolean flag){
	    	ad.setTitle( Html.fromHtml("<font color='#E3256B'>"+head+"</font>"));
	    	ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>"+body+"</font>"));
			ad.setButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	
	            	if(flag)
	            	{
	            	  Date CurDt = new Date();
	        		  txtDate.setText(df.format(CurDt));// set Current Date
	        		  txtTime.setText(df1.format(CurDt));// set Current Time
	        		
	            	  txtEventName.setText("");
	            	  txtDesc.setText("");
	            	  txtVenue.setText("");
	            	
	            	  chkSendGroup.setChecked(false);
	            	  chkEventConfirmation.setChecked(false);
	            	  chkEmail.setChecked(false);
	            	  Sp_EmailFormat.setVisibility(View.GONE);//hide Email format spinner
	            	
	            	  AdpGroup=null;
	            	  AdpConfirmationGroup=null;
	            	  List_EventDirectors.clear();
	             	  products1.clear();
	            	  dialog.dismiss();
	            	  backs();
	            	}
	            	else{
	            		dialog.dismiss();
	            	}
	            	
	            }
	        });
	        ad.show();	
	}
	
	
	 //Display An Alert of Special Characters not allowed
	 public void SpecialCharAlert()
	  {
		   ad.setTitle(Html.fromHtml("<font color='#FF7F27'>Error!</font>"));
	       ad.setMessage("Event Description cannot contains following characters ',\"");
	       ad.setButton("OK", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int which) {
	        	  Msg=Msg.replace("'", "");
	        	  Msg=Msg.replace("\"", ""); 
	        	// Message  = Message.substring( 0, Message.length() - 1 ); 
	        	 txtDesc.setText(Msg);
	             dialog.dismiss();
	            }
	          });
	        ad.show();
	   }
}
