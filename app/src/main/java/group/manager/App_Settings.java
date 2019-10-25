package group.manager;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class App_Settings extends Activity {

	final Context context=this;
	byte[] AppLogo;
	String ClubName;
	Switch TogBtn_MsgDisplay,BDay_AlarmToggle,AnniveralarmToggle;
	RelativeLayout RL_MsgPosition,RL_MemDir,RL_BirNotiTime,RL_CCBYearChange;
	RadioGroup rdbtnGrp_MsgPosition,rdbtnGrp_MemDir,rdbtnGrp_MemDir_View;
	RadioButton rdbtn_Top,rdbtn_Center,rdbtn_Member,rdbtn_Spouse,rdbtn_MemDirView1,rdbtn_MemDirView2;
	Button btnSave;
	boolean tog_Val=true;
	SharedPreferences sharedpreferences; 
	Editor editr;
	String Str_CallScreen_Position;
	AlarmManager alarmManager;
    String Birday_Noti_Time="";
    int ChangeTime=0;
    final static int interval=24*60*60*1000;//Interval a Day(24hrs)
    //final static int interval=2*60*60*1000;//Interval a 2hrs(1hrs)
    boolean DirDisplay=false;
    String ClientID="",UID="",LogclubName="";
    TextView txtBirNoti_Time;
    PendingIntent pendingIntent1,pendingIntent;
    Intent myIntent1,myIntent;
    Spinner sp_notisound,sp_YearChangeCCB;
    String[] Arr_NotiSound={"Default","Others","None"};
    String NotiSound="0"; //0 for Default 1 for None else Others
    String NotiUri=null;
    boolean flag=true;
    String[] CCBYearArr,CCBYearArr_val;
    String CCBYear="";
    String MemDir_View="1";
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_screen_setting);
       
		RL_MsgPosition=(RelativeLayout)findViewById(R.id.RL_MsgPosition);
		RL_MemDir=(RelativeLayout)findViewById(R.id.RL_MemDir);
		RL_BirNotiTime = (RelativeLayout) findViewById(R.id.relativeNotiTextview);
		RL_CCBYearChange = (RelativeLayout) findViewById(R.id.RL_CCBYearChange);
		
		rdbtnGrp_MsgPosition = (RadioGroup) findViewById(R.id.rdbtnGrpMsg_Position);
		rdbtn_Top = (RadioButton) findViewById(R.id.rdbtn_Top);
		rdbtn_Center = (RadioButton) findViewById(R.id.rdbtn_Center);
		
		rdbtnGrp_MemDir = (RadioGroup) findViewById(R.id.rdbtnMemDir);
		rdbtn_Member = (RadioButton) findViewById(R.id.rdbtn_Member);
		rdbtn_Spouse = (RadioButton) findViewById(R.id.rdbtn_Spouse);
		
		rdbtnGrp_MemDir_View = (RadioGroup) findViewById(R.id.rdbtnGrp_MemDir_View);
		rdbtn_MemDirView1 = (RadioButton) findViewById(R.id.rdbtn_MemDirView1);
		rdbtn_MemDirView2 = (RadioButton) findViewById(R.id.rdbtn_MemDirView2);
		
		txtBirNoti_Time=(TextView) findViewById(R.id.txtBirNotiTime);
		
		TogBtn_MsgDisplay=(Switch)findViewById(R.id.togbtn_ScreenMsg);
		BDay_AlarmToggle = (Switch) findViewById(R.id.alarmToggleSwitchBirthday);
	    AnniveralarmToggle = (Switch) findViewById(R.id.alarmToggleSwitchAnniversary);
		btnSave = (Button) findViewById(R.id.btnSave);
		sp_notisound = (Spinner) findViewById(R.id.sp_notisound);
		sp_YearChangeCCB = (Spinner) findViewById(R.id.sp_YearChangeCCB);
		
		ArrayAdapter<String> adpSp = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Arr_NotiSound );
		adpSp.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sp_notisound.setAdapter(adpSp); 
		
		RL_BirNotiTime.setVisibility(View.GONE);
	
		Intent menuIntent = getIntent(); 
	    ClubName =  menuIntent.getStringExtra("Clt_ClubName");
	    String HasCCB =  menuIntent.getStringExtra("HasCCB");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
			
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		
		Set_App_Logo_Title(); // Set App Logo and Title
		
		Get_SharedPref_Values(); // Get Shared Pref Values of Call Screen Msg
		
		SetNotiSound_SpinnerSelectedVal();//Set Noti Sound Spinner selected value
		
		
		////Change Year setting for Commitee,Council,Branch
		RL_CCBYearChange.setVisibility(View.GONE);
		if(HasCCB.equals("Y"))
		{
			RL_CCBYearChange.setVisibility(View.VISIBLE);
			Set_YearChangeCCB();//Set Year Change CCB Spinner 
			SetCCBYear_SpinnerSelectedVal();//Set If Selected value Saved 
		}
		
		// Display MemDir Settings Option or Not
		if(DirDisplay){
			RL_MemDir.setVisibility(View.VISIBLE);
		}
		else{
			RL_MemDir.setVisibility(View.GONE);
		}
		///////////////////////////////////////
		
		
		///////Show anniversary notification with @ and anniversary notification have not Noti_Time so it use Birthday Noti_Time//
		if(Birday_Noti_Time.trim().length()==0){
			/// NO BirthDay and NO Anniversary Alarm ////
			BDay_AlarmToggle.setChecked(false);
			AnniveralarmToggle.setChecked(false);
			RL_BirNotiTime.setVisibility(View.GONE);
		}else if(Birday_Noti_Time.trim().equals("@")){
			/// Only Anniversary Alarm ////
			BDay_AlarmToggle.setChecked(false);
			AnniveralarmToggle.setChecked(true);
			RL_BirNotiTime.setVisibility(View.GONE);
		}else if((Birday_Noti_Time.trim().length()>1)&&(Birday_Noti_Time.trim().contains("@"))){
			/// BirthDay and  Anniversary both Alarm ////
			txtBirNoti_Time.setText(Birday_Noti_Time.replace("@", ""));
			BDay_AlarmToggle.setChecked(true);
			AnniveralarmToggle.setChecked(true);
			RL_BirNotiTime.setVisibility(View.VISIBLE);
		}else if((Birday_Noti_Time.trim().length()>1)&&(!Birday_Noti_Time.trim().contains("@"))){
			/// Only BirthDay Alarm ////
			txtBirNoti_Time.setText(Birday_Noti_Time);
			BDay_AlarmToggle.setChecked(true);
			AnniveralarmToggle.setChecked(false);
			RL_BirNotiTime.setVisibility(View.VISIBLE);
		}
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		txtBirNoti_Time.setOnClickListener(new OnClickListener()
        { 
			@Override
			public void onClick(View arg0) {
				SetBday_AnniNotiTime();
			}
		});
		
		TogBtn_MsgDisplay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked) {
		        	RL_MsgPosition.setVisibility(View.VISIBLE);
		        	tog_Val=true;
		        } else {
		        	RL_MsgPosition.setVisibility(View.GONE);
		        	tog_Val=false;
		        	rdbtn_Top.setChecked(true);
		        }
		    }
		});
		
		
		BDay_AlarmToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		            if (isChecked) {
		            	RL_BirNotiTime.setVisibility(View.VISIBLE);
		            	if(Birday_Noti_Time.trim().length()>1){
		            		txtBirNoti_Time.setText(Birday_Noti_Time.replace("@", ""));
		        		}else{
		        			//Get Current Time (hh mm)
		        		  	Calendar cal=Calendar.getInstance();
		        		  	int Cur_hh=cal.get(Calendar.HOUR_OF_DAY);
		        		  	int Cur_mm=cal.get(Calendar.MINUTE);
		        		  	////////////////////////////////////
		        		  	String AlarmTime=chkval(String.valueOf(Cur_hh))+":"+chkval(String.valueOf(Cur_mm));
		        		  	txtBirNoti_Time.setText(AlarmTime);
		        		}
					}else{
						RL_BirNotiTime.setVisibility(View.GONE);
					}
			   }
		 });
		
		
		
		///Notification Sound Spinner OnItemSelected Listener
		sp_notisound.setOnItemSelectedListener(new OnItemSelectedListener()
	    { 
	  		  @Override
	  		  public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long arg3) {
	  			// TODO Auto-generated method stub
	  			  
	  			if(flag)
	  			{
	  			  if(position==0){
	  				NotiSound="0";//Default
	  				String Default_GrpMan_NotiSound="android.resource://" + context.getPackageName() + "/" + R.raw.notify;
	  				Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), Uri.parse(Default_GrpMan_NotiSound));
	  				r.play();
	  			  }
	  			  else if(position==1){
	  				  ///Others////
	                 Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
	                 intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALL);
	                 intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Notification Ringtone");
	                 intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);//Not Diplayed Silent in Ringtone List
	                 //intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);//Not Diplayed Default in Ringtone List
	                 
	                 if(NotiUri!=null)
	                   intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(NotiUri));
	                 else
	                   intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri)null);
	                 
	                 startActivityForResult(intent,413);
	  			  }
	  			  else{
	  				NotiSound="1";//None
	  			  }
	  			}
	  			
	  			flag=true;
	  		  }
	  		 
	  		  @Override
	  		  public void onNothingSelected(AdapterView<?> arg0) {}
	    });
		
		
		
		sp_YearChangeCCB.setOnItemSelectedListener(new OnItemSelectedListener()
        {  
   		   @Override
   		   public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
   			// TODO Auto-generated method stub
   			CCBYear=CCBYearArr_val[arg2];
   		   }
   		   @Override
   		   public void onNothingSelected(AdapterView<?> arg0) {
   			// TODO Auto-generated method stub	
   		   }
         });
		
		
		
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			    // get selected radio button from radioGroup
				int chkId = rdbtnGrp_MsgPosition.getCheckedRadioButtonId();//Msg Display 
				// find the radiobutton(Top or Center) by returned id
				RadioButton  radioSexButton = (RadioButton) findViewById(chkId);
				
				int chkId1 = rdbtnGrp_MemDir.getCheckedRadioButtonId();//Member Dir 
				// find the radiobutton(Member or Spouse) by returned id
				RadioButton  rdbtnMemDir = (RadioButton) findViewById(chkId1);
				String MemDir=rdbtnMemDir.getText().toString().trim();
				
				//Member Dir  View
				int chkId2 = rdbtnGrp_MemDir_View.getCheckedRadioButtonId();
				// find the radiobutton(View1 or View2) by returned id
				RadioButton  rdbtn1 = (RadioButton) findViewById(chkId2);
				String MemDir_Vw=rdbtn1.getText().toString().trim();
				if(!MemDir_Vw.equals("List"))
					MemDir_Vw="1";
				else
					MemDir_Vw="2";
				//////////////////////////////////
				
				String Msg_Postion="Top";
				if(tog_Val)
				  Msg_Postion=radioSexButton.getText().toString();
				
				int ReqCode=Get_Mid_LoginMain();//Get Unique MID As RequestCode of pendingIntent to Make Unique pendingIntent
				
				myIntent = new Intent(App_Settings.this, AlarmReceiver.class);
				myIntent.putExtra("ClientID", ClientID);
				myIntent.putExtra("LogclubName", LogclubName);
				myIntent.putExtra("MID", ReqCode);
				pendingIntent = PendingIntent.getBroadcast(App_Settings.this, ReqCode, myIntent, 0);

				String AnniAdd="",BDayNotiTime="";
				int alarmHH,alarmMM;
				Calendar calendar = Calendar.getInstance();
				int HOUROFDAY=Calendar.HOUR_OF_DAY;
				int MINUTEss=Calendar.MINUTE;
				
				if(BDay_AlarmToggle.isChecked())
				{
					 alarmManager.cancel(pendingIntent);
					 String AlarmTime=txtBirNoti_Time.getText().toString().trim();
					 String[] temp=AlarmTime.replace(":", "#").split("#");
					 alarmHH=Integer.parseInt(temp[0]);
					 alarmMM=Integer.parseInt(temp[1]);
					 
					 calendar.set(HOUROFDAY, alarmHH);
					 calendar.set(MINUTEss, alarmMM);
					 
					 if(AnniveralarmToggle.isChecked()){
						 AnniAdd="@";
					 }
					 
					 //alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
					 alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, pendingIntent);
					 
					 Birday_Noti_Time=AlarmTime;
					 BDayNotiTime=Birday_Noti_Time+AnniAdd.trim();
				}
				else
				{
					 alarmManager.cancel(pendingIntent);
					 txtBirNoti_Time.setText("");
					 
					 /////////////////////////////////////////////
					 alarmHH=9;
					 alarmMM=0;
					 
					 calendar.set(HOUROFDAY, alarmHH);
					 calendar.set(MINUTEss, alarmMM);
					 if(AnniveralarmToggle.isChecked()){
						AnniAdd="@";
						alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, pendingIntent); 
					 }
					 BDayNotiTime=AnniAdd.trim();
				}
				Edit_SharedPref_Values(tog_Val,Msg_Postion,BDayNotiTime,MemDir,NotiSound,CCBYear,MemDir_Vw);
				Toast.makeText(context,"Settings Saved", 1).show();
				backs();
			}
		});
		
	}

	
	private void SetNotiSound_SpinnerSelectedVal()
	{
	    ////// Set Notification Sound Spinner Value ///////
		int i=1;
		if(NotiSound.equals("0"))//Default
			i=0;
		else if(NotiSound.equals("1"))//None
			i=2;
		sp_notisound.setSelection(i);
		//////////////////////////////////////////////////
	}
	
	
	private void SetCCBYear_SpinnerSelectedVal()
	{
	    ////// Set CCB Current Year Spinner Value ///////
		for(int i=0;i<CCBYearArr_val.length;i++)
		{
			if(CCBYear.equals(CCBYearArr_val[i]))
			{
				sp_YearChangeCCB.setSelection(i);
				break;
			}
		}
		//////////////////////////////////////////////////
	}
	
	
	
	//Show Set Noti Time Dialog box 
	private void SetBday_AnniNotiTime()
	{
		final Dialog dialog = new Dialog(context);
	       dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
		   dialog.setContentView(R.layout.settime);
		   dialog.setCancelable(false);
		   dialog.show();
		   final TimePicker alarmTimePicker=(TimePicker)dialog.findViewById(R.id.timePicker1);
		   Button BtnOk=(Button)dialog.findViewById(R.id.btnOK);
		   Button BtnCancel=(Button)dialog.findViewById(R.id.btnCancel);

		    String GetAlarmTime=txtBirNoti_Time.getText().toString().trim();
		    if(GetAlarmTime.length()!=0)
		    {
		      String pick =GetAlarmTime.replace(":", "#");
			  String[] temp=pick.split("#");
			  int hhs=Integer.parseInt(temp[0]);
			  int mms=Integer.parseInt(temp[1]);
			  alarmTimePicker.setCurrentHour(hhs);
			  alarmTimePicker.setCurrentMinute(mms);
		    }

		   BtnOk.setOnClickListener(new OnClickListener() {    	
       	    @Override
       	    public void onClick(View v) {
       	      int alarmHH= alarmTimePicker.getCurrentHour();
			  int alarmMM= alarmTimePicker.getCurrentMinute();
			  String AlarmTime=chkval(String.valueOf(alarmHH))+":"+chkval(String.valueOf(alarmMM));
			  txtBirNoti_Time.setText(AlarmTime);
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
		 sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		 if (sharedpreferences.contains("clientid"))
	     {
			ClientID=sharedpreferences.getString("clientid", "");
	     }
		 if (sharedpreferences.contains("userid"))
	     {
			UID=sharedpreferences.getString("userid", "");
	     }
		 if (sharedpreferences.contains("clubname"))
	     {
	    	LogclubName=sharedpreferences.getString("clubname", "");
         } 
		 if (sharedpreferences.contains("CallScreen"))
		 {
			 tog_Val=sharedpreferences.getBoolean("CallScreen",true);
			 TogBtn_MsgDisplay.setChecked(tog_Val);
			 if(tog_Val)
				 RL_MsgPosition.setVisibility(View.VISIBLE);
			 else
				 RL_MsgPosition.setVisibility(View.GONE);
		 } 
		 if (sharedpreferences.contains("CallScreen_Position"))
		 {
			 Str_CallScreen_Position=sharedpreferences.getString("CallScreen_Position", "");
			 if(Str_CallScreen_Position.equals("Top"))
				rdbtn_Top.setChecked(true);
			 else 
				rdbtn_Center.setChecked(true);
		 } 
		 if (sharedpreferences.contains("MemDir"))
		 {
			 String MemDir=sharedpreferences.getString("MemDir", "");
			 if(MemDir.equals("Member"))
				rdbtn_Member.setChecked(true);
			 else 
				rdbtn_Spouse.setChecked(true);
		 } 
		 
		 if (sharedpreferences.contains("Birday_Noti_Time"))
		 {
			 Birday_Noti_Time=sharedpreferences.getString("Birday_Noti_Time", "");
		 }
		 if (sharedpreferences.contains("AppSettings"))
		 {
			 String AppSettings=sharedpreferences.getString("AppSettings", "");
			 if(AppSettings.equals("SPOUSE"))
				 DirDisplay=true;
			 else
				 DirDisplay=false;
		 }
		 if (sharedpreferences.contains("NotiSound"))
	     {
			 flag=false;
			 NotiSound=sharedpreferences.getString("NotiSound", "");
			 if(NotiSound.length()>5)
				 NotiUri=NotiSound;
         }
		 if (sharedpreferences.contains(ClientID+"_CCBYear"))
	     {
			 CCBYear=sharedpreferences.getString(ClientID+"_CCBYear", "");
         }
		 if (sharedpreferences.contains("MemDir_View"))
	     {
			 MemDir_View=sharedpreferences.getString("MemDir_View", "");
         }
		 
		 ///// For Member Directory View 
		 if(MemDir_View.equals("1"))
			 rdbtn_MemDirView1.setChecked(true);//By Default Value
	     else 
	    	 rdbtn_MemDirView2.setChecked(true);
	 }
	 
	 
	 private void Edit_SharedPref_Values(boolean CallScreen,String Screen_Position,String Birday_Noti_Time,String MemDir,String Noti_Sound,String CCBYear,String MemDir_Vw) {
	     
		 SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		 String SqlQry="UPDATE LoginMain SET setting_Callscrn='"+CallScreen+"',setting_Callscrn_posi='"+Screen_Position.trim()+"',"+
		         "setting_Birday_noti_time='"+Birday_Noti_Time.trim()+"',setting_MemDir='"+MemDir.trim()+"' "+      
				 "Where ClientID='"+ClientID.trim()+"' AND UID='"+UID.trim()+"'";
		 db.execSQL(SqlQry);
		 db.close();
		  
		 editr = sharedpreferences.edit();
	     editr.putBoolean("CallScreen",CallScreen);//Saved CallScreen Display
		 editr.putString("CallScreen_Position",Screen_Position); //Saved CallScreen_Position
		 editr.putString("Birday_Noti_Time",Birday_Noti_Time); //Saved Birday_Noti_Time
		 editr.putString("MemDir",MemDir); //Saved Directory(Member or Spouse)
		 editr.putString("MemDir_View",MemDir_Vw); //Saved Directory View(Data or List)
		 editr.putString("NotiSound",Noti_Sound); //Set Notification Sound
		 editr.putString(ClientID+"_CCBYear",CCBYear); //Set CCB Year ClientID wise
		 editr.commit();
	 }
	
	 // Get User Mid from LoginMain
	 private int Get_Mid_LoginMain()
	 {
		 int Mid=0;
		 SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		 String SqlQry="Select M_ID from LoginMain Where ClientID='"+ClientID.trim()+"' AND UID='"+UID.trim()+"'";
		 Cursor cursorT = db.rawQuery(SqlQry, null);
	     int RCount=cursorT.getCount();
	     if(RCount>0)
	     {
			while(cursorT.moveToNext())
			{
			  Mid=cursorT.getInt(0);
			  break;
			}
	     }
	     cursorT.close();
		 db.close();
		 return Mid;
	 }
	
	 
	 ///Set CCB(Committee,Council,Branch) Year Change Setting Spinner
	 private void Set_YearChangeCCB()
	 {
		 int i=0;
		 
		 SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		 String SqlQry="Select text1,Num1 from C_"+ClientID+"_4 Where Rtype='Year' order by num1 Desc";
		 Cursor cursorT = db.rawQuery(SqlQry, null);
         int tempsize=cursorT.getCount();
         CCBYearArr=new String[tempsize];
         CCBYearArr_val=new String[tempsize];
         if (cursorT.moveToFirst()) {
             do{
            	 CCBYearArr[i]=cursorT.getString(0);
            	 CCBYearArr_val[i]=cursorT.getString(1);
        		 i++;	
              }while(cursorT.moveToNext());
         }
	     cursorT.close();
	     
	     if(CCBYearArr.length!=0){
	    	 ArrayAdapter<String> adp1 =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,CCBYearArr);
	    	 adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	 sp_YearChangeCCB.setAdapter(adp1);
	     }
		 db.close();
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
		Intent MainBtnIntent= new Intent(context,MenuPage.class);
		MainBtnIntent.putExtra("AppLogo", AppLogo);
   	    startActivity(MainBtnIntent);
   	    finish();
	 }
	
	 public String chkval(String val){
		 if(val.length()==1){
			 val="0"+val;
		 }
		return val;
	 }
	 
	 
	 ////////////////Others Noti Sound selection call Back Func.
	 @Override
	 protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent)
	 {
		 if(resultCode!=0){
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            if (uri != null)
            {
            	NotiUri = uri.toString();
            	NotiSound=NotiUri;
            }
            else
            {
            	NotiSound="1";//None
            }
	    }
	 }
	
}
