package group.manager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ParseException;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Guest_Registration extends Activity {

	EditText txtGroupCode, txtGuestName, txtMob, txtEmail;
	Button btnSubmit;
	String Cid, GuestName, Mob, Email, WebResp;
	boolean InternetPresent = false;
	Chkconnection chkconn;
	Context context = this;
	AlertDialog ad;
	WebServiceCall webcall;
	String[] temp;
	ProgressDialog Progsdial;
	Thread networkThread;
	String val1, val2, val3, val4, MenuList, AppSettings;
	public static final String MyPREFERENCES = "MyPrefs";
	public static final String cLid = "clientid";
	public static final String uid = "userid";
	public static final String pass = "passwordKey";
	public static final String datetm = "DateTme";
	public static final String value2 = "name";
	public static final String value3 = "cltid";
	public static final String value4 = "clubname";
	public static final String ValueMenuList = "MenuList";
	SharedPreferences sharedpreferences;
	Editor editor;
	byte[] AppLogo;
	String AddGrp, User, StrChkDT = "";
	public final static long SECOND_MILLIS = 1000;
	public final static long MINUTE_MILLIS = SECOND_MILLIS * 60;
	public final static long HOUR_MILLIS = MINUTE_MILLIS * 60;
	public final static long DAY_MILLIS = HOUR_MILLIS * 24;
	String Strdtm, Str_IEMI;
	TelephonyManager tm;
	Intent MainBtnIntent;
	SimpleDateFormat df;
	String ClientID = "", UID = "", Guest_Email = "";
	UnCommonProperties UnComObj;
	SQLiteDatabase dbObj;
	String SharePre_GCMRegId = "";//For Shared Preference
	String AppVersion = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guest_registration);

		txtGroupCode = (EditText) findViewById(R.id.etGroupCode);
		txtGuestName = (EditText) findViewById(R.id.etName);
		txtMob = (EditText) findViewById(R.id.etMob);
		txtEmail = (EditText) findViewById(R.id.etEmail);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);

		Intent intent = getIntent();
		AddGrp = intent.getStringExtra("AddGrp");
		User = intent.getStringExtra("User");

		webcall = new WebServiceCall();//Call a Webservice
		chkconn = new Chkconnection(); // Intialise Net Connection Class Object
		ad = new AlertDialog.Builder(this).create();

		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE); // Initialise Shared Pref

		Str_IEMI = new CommonClass().getIMEINumber(context);//Added On 14-02-2019

		////// Get Version Name /////////////////
		PackageInfo pInfo = null;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AppVersion = pInfo.versionName;
		//////////////////////////////////////////

		UnComObj = new UnCommonProperties();// Make An Object
		String AppTitle = UnComObj.GET_AppTitle();

		setTitle(AppTitle);
		Calendar c = Calendar.getInstance();
		df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		Strdtm = df.format(c.getTime());

		if (AddGrp.equals("NO")) {
			Get_SharedPref_Values(); // Get Shared Pref Values
		} else {
			AppLogo = intent.getByteArrayExtra("AppLogo");
		}

		////New Change 6 Oct 2015 'MainClientID' for Hide Client ID / Group Code Text Box
		String MainClientID = UnComObj.GET_MainClientID();
		if (MainClientID.length() > 1) {
			ClientID = MainClientID;// Set Global MainClientId In Client Id
			txtGroupCode.setText(ClientID);
			txtGroupCode.setVisibility(View.INVISIBLE); // Hide Group Code TxtBox
		}
		//////////////////////////////////////////////////////////////////////////////


		if ((txtGroupCode.getText().length() != 0) && (txtMob.getText().length() != 0) && (txtEmail.getText().length() != 0)) {
			TxtVals();// // Get Values of EditText
			InternetPresent = chkconn.isConnectingToInternet(context);
			if (InternetPresent == true) {
				CallWeb();
			} else if (InternetPresent == false) {
				Offline_Guest_Login(); // Open App Offline Mode
			} else {
				Disp_Alert("Error!", "Some error please try later.", 1);
			}
		}


		btnSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				TxtVals();// // Get Values of EditText
				if (Cid.length() == 0) {
					txtGroupCode.setError("Enter Group Name");
				} else if (GuestName.length() == 0) {
					txtGuestName.setError("Enter Guest Name");
				} else if (Mob.length() == 0) {
					txtMob.setError("Enter Mobile");
				} else if (Email.length() == 0) {
					txtEmail.setError("Enter Email");
				} else {
					InternetPresent = chkconn.isConnectingToInternet(context);
					if (InternetPresent == false) {
						Disp_Alert("Error!", "Not Authorised, Please Connect with Internet for Authorisation.", 1);
					} else {
						if (User.equals("Single")) {
							String Guest_Email = GuestName + "@_@" + Email;
							Edit_SharedPref_Values(Cid, Mob, Guest_Email, "", "", "", "", "", "Guest", "", "0", "0", ""); // Edit/Insert Shared Pref Values
						}
						CallWeb(); // Call a Webservice for Registration
					}
				}
			}
		});
	}


	// Get Values of EditText
	private void TxtVals() {
		Cid = txtGroupCode.getText().toString().trim();
		GuestName = txtGuestName.getText().toString().trim();
		Mob = txtMob.getText().toString().trim();
		Email = txtEmail.getText().toString().trim();
	}


	protected void progressdial() {
		Progsdial = new ProgressDialog(this, R.style.MyTheme);
		//Progsdial.setTitle("App Loading");
		Progsdial.setMessage("Please Wait....");
		Progsdial.setIndeterminate(true);
		Progsdial.setCancelable(false);
		Progsdial.getWindow().setGravity(Gravity.DISPLAY_CLIP_VERTICAL);
		Progsdial.show();
	}


	private void CallWeb() {
		progressdial();
		networkThread = new Thread() {
			public void run() {
				try {
					WebResp = webcall.login_Guest(Cid, GuestName, Mob, Email, Str_IEMI, SharePre_GCMRegId, AppVersion);
					runOnUiThread(new Runnable() {
						public void run() {
							if (WebResp.contains("#")) {
								temp = WebResp.split("#");
								val1 = temp[0].toString();
								val2 = temp[1].toString();

								if (val1.equals("Y")) {
									val3 = temp[2].toString();
									val4 = temp[3].toString();
									MenuList = temp[6].toString();
									AppSettings = temp[7].toString();
									String DOB_Disp = temp[8].toString();//DOB_Disp is used for Display DOB or not(i.e [default] 0=DOB Display,1=DOB Not Display,2=DOB Display but DobYear Not Displayed)
									String timeout = temp[9].toString();//timeout if(15 then default value) else (given time)
									String ChkSyncVal = temp[11].toString();//ChkSync Required Value	24-02-2017

									// Create DataBase and Tables Of Authorised ClientID Of Guest Login
									Create_All_Tables();
									////////////////////////////////////////////////////

									String Guest_Email = GuestName + "@_@" + Email;
									Edit_SharedPref_Values(Cid, Mob, Guest_Email, Strdtm, val2, val3, val4, MenuList, "Guest", AppSettings, DOB_Disp, timeout, ChkSyncVal); // Edit/Insert Shared Pref Values

									CallNextActivity(Cid);//Go To Next Activity

								} else if (val1.equals("N")) {
									Disp_Alert("Result!", val2, 0);
								} else {
									Disp_Alert("Result!", WebResp, 0);
								}
							} else if (WebResp.contains("Connection TimeOut")) {
								Offline_Guest_Login(); // Open App Offline Mode
							} else {
								Disp_Alert("Error2!", "Some error please try later or fill correct entry.", 0);
							}
						}
					});
					Progsdial.dismiss();
					return;
				} catch (Exception localException) {
					System.out.println(localException.getMessage());
				}
			}
		};
		networkThread.start();
	}


	//Create All Tables(Create DataBase and Tables Of Authorised ClientID)
	private void Create_All_Tables() {
		dbObj = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);

		//Create Table 2
		dbObj.execSQL("CREATE TABLE IF NOT EXISTS C_" + Cid + "_2 (M_Id INTEGER PRIMARY KEY,M_Name  Text,M_FName Text,M_MName Text,M_LName Text,M_Add1  Text,M_Add2  Text,M_Add3 Text,M_City Text,M_Email Text,M_Gender Text,M_MrgAnn_D Text,M_MrgAnn_M Text,M_MrgAnn_Y Text,M_DOB_D Text,M_DOB_M Text,M_DOB_Y Text,M_Mob Text,M_Pin Text,M_BG Text,M_BussNm Text,M_BussCate Text,M_MemSince_D Text,M_MemSince_M Text,M_MemSince_Y Text,M_Pic BLOB,S_Name Text,S_FName Text,S_Mname Text,S_LName Text,S_Mob Text,S_Email Text,S_DOB_D Text,S_DOB_M Text,S_DOB_Y Text,S_BG Text,S_Pic BLOB,C1_Name Text,C1_FName Text,C1_Mname Text,C1_LName Text,C1_Mob Text,C1_Email Text,C1_Gender    Text,C1_DOB_D     Text,C1_DOB_M     Text,C1_DOB_Y     Text,C1_BG   Text,C1_Pic  BLOB,C2_Name Text,C2_FName     Text,C2_Mname     Text,C2_LName     Text,C2_Mob  Text,C2_Email     Text,C2_Gender    Text,C2_DOB_D     Text,C2_DOB_M     Text,C2_DOB_Y     Text,C2_BG   Text,C2_Pic  BLOB,C3_Name Text,C3_FName    Text,C3_Mname     Text,C3_LName    Text,C3_Mob  Text,C3_Email     Text,C3_Gender   Text,C3_DOB_D     Text,C3_DOB_M     Text,C3_DOB_Y     Text,C3_BG   Text,C3_Pic  BLOB,C4_Name Text,C4_FName     Text,C4_Mname     Text,C4_LName     Text,C4_Mob  Text,C4_Email     Text,C4_Gender    Text,C4_DOB_D Text,C4_DOB_M Text,C4_DOB_Y Text,C4_BG Text,C4_Pic BLOB,Pass Text,Photo1  BLOB,Photo2  BLOB,Photo3  BLOB,Photo4  BLOB,Photo5  BLOB,Photo6  BLOB,Admin Int,MemNo Text,Oth Text, SYNCID Int,SyncDT INTEGER)");

		//Create Table 4
		dbObj.execSQL("CREATE TABLE IF NOT EXISTS C_" + Cid + "_4 (M_ID INTEGER PRIMARY KEY,Rtype Text,Text1 Text,Text2 Text,Text3 Text,Date1 Text,Date2 Text,Date3 Text,Date1_1 INTEGER,Date2_1 INTEGER,Date3_1 INTEGER,Num1 INTEGER,Num2 INTEGER,Num3 INTEGER,Add1 TEXT,Photo1 BLOB,Text4 Text,Text5 Text,Text6 Text,Text7 Text,Text8 Text,Add2 Text,SYNCID Int,SyncDT INTEGER)");

		//Create Table MISC
		dbObj.execSQL("CREATE TABLE IF NOT EXISTS C_" + Cid + "_MISC (M_ID INTEGER PRIMARY KEY,Rtype Text,MemId INTEGER," +
				"Text1 Text,Text2 Text,Text3 Text,Text4 Text,Text5 Text,Text6 Text,Text7 Text,Text8 Text,Text9 Text," +
				"Text10 Text,Text11 Text,Text12 Text,Text13 Text,Text14 Text,Text15 Text,Text16 Text,Text17 Text,Text18 Text,Text19 Text," +
				"Text20 Text,Text21 Text,Text22 Text,Text23 Text,Text24 Text,Text25 Text,Text26 Text,Text27 Text,Text28 Text,Text29 Text," +
				"Text30 Text,Text31 Text,Text32 Text,Text33 Text,Text34 Text,Text35 Text,Text36 Text,Text37 Text,Text38 Text,Text39 Text," +
				"Text40 Text,Text41 Text,Text42 Text,Text43 Text,Text44 Text,Text45 Text,Text46 Text,Text47 Text,Text48 Text,Text49 Text,Text50 Text," +
				"Add1 Text,Add2 Text,Add3 Text,Add4 Text,Add5 Text,SYNCID INTEGER,SyncDT INTEGER)");

		//Create Table Family
		dbObj.execSQL("CREATE TABLE IF NOT EXISTS C_" + Cid + "_Family (M_ID INTEGER PRIMARY KEY,MemId INTEGER," +
				"Name Text,Relation Text,Father Text,Mother Text,Address Text,Current_Loc Text,Mob_1 Text,Mob_2 Text,DOB_D Text," +
				"DOB_M Text,DOB_Y Text,EmailID Text,Age Text,Birth_Time Text,Birth_Place Text,Gotra Text,Remark Text,Pic BLOB,Education Text," +
				"Work_Profile Text,Text1 Text,Text2 Text,Text3 Text,Text4 Text,Text5 Text,Text6 Text,Text7 Text,Text8 Text,Text9 Text," +
				"Text10 Text,SYNCID INTEGER,SyncDT INTEGER)");

		//Create Table Event
		dbObj.execSQL("CREATE TABLE IF NOT EXISTS C_" + Cid + "_Event (M_ID INTEGER PRIMARY KEY,MemId INTEGER," +
				"Num INTEGER,Num1 INTEGER,Num2 INTEGER,Num3 INTEGER,Num4 INTEGER,Num5 INTEGER,Rtype Text," +
				"Text1 Text,Text2 Text,Text3 Text,Text4 Text,Text5 Text,Sync INTEGER)");

		//Create Table OpinionPoll 1 (added on 08-04-2017)
		dbObj.execSQL("CREATE TABLE IF NOT EXISTS C_" + Cid + "_OP1 (M_ID INTEGER PRIMARY KEY,OP_Name Text," +
				"OP_From Text,OP_TO Text,OP_From_1 INTEGER,Op_To_1 INTEGER,Cond1 Text,Cond2 Text,Co_Type Text," +
				"Time_Req INTEGER,SyncID INTEGER,SyncDt INTEGER)");

		//Create Table OpinionPoll 2 (added on 08-04-2017)
		dbObj.execSQL("CREATE TABLE IF NOT EXISTS C_" + Cid + "_OP2 (M_ID INTEGER PRIMARY KEY,OP1_ID INTEGER," +
				"PSNO INTEGER,SNO Text,Question Text,Ans1 Text,Ans2 Text,Ans3 Text,Ans4 Text,Remark Text," +
				"Answer Text,SyncID INTEGER,SyncDt INTEGER,Remark_Req INTEGER)");

		//Create Table OpinionPoll 3 (added on 11-04-2017)
		dbObj.execSQL("CREATE TABLE IF NOT EXISTS C_" + Cid + "_OP3 (ID INTEGER PRIMARY KEY,OP1_ID INTEGER," +
				"OP2_ID INTEGER,User_Ans Text,Remark Text,HoldForLater INTEGER,Submit INTEGER,Timeout INTEGER,SyncID INTEGER)");

		//Make Alter Table Columns in Table OpinionPoll 1 //Added on 15-04-2017
		String SqlQry = "ALTER TABLE C_" + Cid + "_OP1 ADD COLUMN Op_Publish INTEGER";
		MakeAlterColumn(SqlQry);

		//Make Alter Table Columns in Table OpinionPoll 1 //Added on 24-04-2017
		SqlQry = "ALTER TABLE C_" + Cid + "_OP1 ADD COLUMN U_Ans INTEGER";
		MakeAlterColumn(SqlQry);

		//Make Alter Table Columns in Table OpinionPoll 1 //Added on 24-04-2017
		SqlQry = "ALTER TABLE C_" + Cid + "_OP1 ADD COLUMN Time_Remains INTEGER";
		MakeAlterColumn(SqlQry);

		//Make Alter Table Columns in Table 2
		SqlQry = "ALTER TABLE C_" + Cid + "_2 ADD COLUMN M_SndAdd1 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_2 ADD COLUMN M_SndAdd2 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_2 ADD COLUMN M_SndAdd3 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_2 ADD COLUMN M_SndAdd4 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_2 ADD COLUMN M_SndPin Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_2 ADD COLUMN M_SndMob Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_2 ADD COLUMN M_SndMob1 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_2 ADD COLUMN M_SndStd Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_2 ADD COLUMN Oth1 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_2 ADD COLUMN Oth2 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_2 ADD COLUMN Oth3 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_2 ADD COLUMN M_Land1 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_2 ADD COLUMN M_Land2 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_2 ADD COLUMN M_Email1 Text";
		MakeAlterColumn(SqlQry);
		///////////////////////////////////////////////////

		//Make Alter Table Columns in Table 4 New Update 12-05-2016 by Tapas
		SqlQry = "ALTER TABLE C_" + Cid + "_4 ADD COLUMN COND1 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_4 ADD COLUMN COND2 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_4 ADD COLUMN COND3 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_4 ADD COLUMN COND4 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_4 ADD COLUMN COND5 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_4 ADD COLUMN COND6 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_4 ADD COLUMN COND7 Text";
		MakeAlterColumn(SqlQry);
		////////////////////////////////////


		//Make Alter Table Columns in Table Family New Update 06-06-2016 by Tapas
		SqlQry = "ALTER TABLE C_" + Cid + "_Family ADD COLUMN Text11 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_Family ADD COLUMN Text12 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_Family ADD COLUMN Text13 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_Family ADD COLUMN Text14 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_Family ADD COLUMN Text15 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_Family ADD COLUMN Text16 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_Family ADD COLUMN Text17 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_Family ADD COLUMN Text18 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_Family ADD COLUMN Text19 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Cid + "_Family ADD COLUMN Height Real";
		MakeAlterColumn(SqlQry);
		////////////////////////////////////

		dbObj.close();//Database Close
	}


	private void MakeAlterColumn(String SqlQry) {
		try {
			dbObj.execSQL(SqlQry);
		} catch (Exception e) {
			//System.out.println(e.getMessage());
		}
	}


	private void Get_SharedPref_Values() {
		if (sharedpreferences.contains(cLid)) {
			ClientID = sharedpreferences.getString(cLid, "");
			txtGroupCode.setText(ClientID);
		}
		if (sharedpreferences.contains(uid)) {
			UID = sharedpreferences.getString(uid, "");
			txtMob.setText(UID);
		}
		if (sharedpreferences.contains(pass)) {
			// Here Pass contains GuestName And Email
			Guest_Email = sharedpreferences.getString(pass, "");
			if (Guest_Email.contains("@_@")) {
				String s[] = Guest_Email.split("@_@");
				GuestName = s[0].trim();
				Email = s[1].trim();
				txtGuestName.setText(GuestName); // Set GuestName
				txtEmail.setText(Email); // Set Email
			}
		}
		if (sharedpreferences.contains(datetm)) {
			StrChkDT = sharedpreferences.getString(datetm, "");
		}
		if (sharedpreferences.contains("SharePre_GCMRegId")) {
			SharePre_GCMRegId = sharedpreferences.getString("SharePre_GCMRegId", "");
		}
	}


	private void Edit_SharedPref_Values(String Club, String mob, String GuestName_And_Email, String DT, String LogName, String LogId, String ClubName, String MenuList, String Login, String AppSettings, String DOB_Disp, String timeout, String ChkSync) {

		editor = sharedpreferences.edit(); // Edit Shared Pref Records

		String MemDir = "Member";/// For Guest MemDir always Member

		String Cid = "", Uid = "", Pwd = "";
		if (sharedpreferences.contains(cLid)) {
			Cid = sharedpreferences.getString(cLid, "");
		}
		if (sharedpreferences.contains(uid)) {
			Uid = sharedpreferences.getString(uid, "");
		}
		if (sharedpreferences.contains(pass)) {
			// Here Pass contains GuestName And Email
			Pwd = sharedpreferences.getString(pass, "");
		}

		// Insert /Update Record in LoginMain
		SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		String SqlQry = "Select M_ID from LoginMain Where ClientID='" + Cid.trim() + "' AND UID='" + Uid.trim() + "'";
		Cursor cursorT = db.rawQuery(SqlQry, null);
		int RCount = cursorT.getCount();
		if (RCount > 0) {
			while (cursorT.moveToNext()) {
				int MID = cursorT.getInt(0);

				SqlQry = "UPDATE LoginMain SET ClientID='" + Club.trim() + "',UID='" + mob.trim() + "',PWD='" + GuestName_And_Email.trim() + "',DDateTime='" + DT.trim() + "',LogName='" + LogName.trim() + "'," +
						"LogId='" + LogId.trim() + "',ClubName='" + ClubName.trim() + "',MenuList='" + MenuList.trim() + "',Login='" + Login.trim() + "',Appsettings='" + AppSettings.trim() + "',setting_extra_2='" + DOB_Disp.trim() + "',setting_extra_3='" + timeout.trim() + "' " +
						",setting_MemDir='" + MemDir.trim() + "' Where M_ID=" + MID;
				//here setting_extra_2 in SqlQry is DOB_Disp
				editor.putString("MemDir", MemDir);  //sunil sir
				break;
			}
			cursorT.close();
		} else {
			String Birday_Noti_Time = "0";///By Default display Bday and Anni Notification (Added on 29-08-2018)
			SqlQry = "Insert into LoginMain(ClientID,UID,PWD,DDateTime,LogName,LogId,ClubName,MenuList,Login,Appsettings,Tab2Count,setting_Callscrn,setting_Callscrn_posi,setting_Birday_noti_time,setting_MemDir,setting_extra_2,setting_extra_3) " +
					"Values('" + Club.trim() + "','" + mob.trim() + "','" + GuestName_And_Email.trim() + "','" + DT.trim() + "','" + LogName.trim() + "','" + LogId.trim() + "','" + ClubName.trim() + "','" + MenuList.trim() + "','" + Login.trim() + "','" + AppSettings.trim() + "',0,'true','Top','" + Birday_Noti_Time + "','Member','" + DOB_Disp.trim() + "','" + timeout.trim() + "')";

			//here setting_extra_2 in SqlQry is DOB_Disp

			editor.putString("TCount_Tab2", "0");
			editor.putBoolean("CallScreen", true);
			editor.putString("CallScreen_Position", "Top");
			editor.putString("Birday_Noti_Time", Birday_Noti_Time);
		}
		db.execSQL(SqlQry); //Insert/Update Exec Query
		db.close();

		editor.putString(cLid, Club.trim());
		editor.putString(uid, mob.trim());
		editor.putString(pass, GuestName_And_Email.trim());
		editor.putString(datetm, DT.trim());
		////////////////////////////
		editor.putString(value2, LogName.trim());
		editor.putString(value3, LogId.trim());
		editor.putString(value4, ClubName.trim());
		editor.putString(ValueMenuList, MenuList.trim());
		editor.putString("Login", Login.trim());
		editor.putString("AppSettings", AppSettings.trim());
		editor.putString("DOB_Disp", DOB_Disp.trim());
		editor.putString("TimeOut", timeout.trim());
		editor.putString("ChkSync", ChkSync.trim());
		editor.commit();
	}


	// Set App Logo
	private void Set_App_Logo(String Tab4Name) {
		DbHandler dbObj = new DbHandler(context, "");
		AppLogo = dbObj.Get_AppLogo(Tab4Name);
		dbObj.close();

		// Set App LOGO
		if (AppLogo == null) {
			getActionBar().setIcon(R.drawable.ic_launcher);
		} else {
			Bitmap bitmap = BitmapFactory.decodeByteArray(AppLogo, 0, AppLogo.length);
			BitmapDrawable icon = new BitmapDrawable(getResources(), bitmap);
			getActionBar().setIcon(icon);
		}
	}


	// Open App Offline Mode
	private void Offline_Guest_Login() {
		if (StrChkDT.length() != 0) {
			Date dateearl = null, datelarg = null;
			try {
				dateearl = df.parse(Strdtm);
				datelarg = df.parse(StrChkDT);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.getMessage());
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			int dd = daysDiff(dateearl, datelarg);
			if (dd > 30) {
				Disp_Alert("Error!", "Synchronisation is required so connection with internet.", 1);
			} else {
				CallNextActivity(Cid);//Go To Next Activity
			}
		} else {
			Disp_Alert("Result!", "Authorisation Failed.Please check Internet Connection !", 1);
		}
	}


	public int daysDiff(Date dateearl, Date datelarg) {
		if (dateearl == null || datelarg == null) return 0;
		return (int) ((dateearl.getTime() / DAY_MILLIS) - (datelarg.getTime() / DAY_MILLIS));
	}

	// Start a service ServiceCallNew for Sync Table4
	public void Start_ServiceCallNew(String ClientId) {
		//boolean chkst=isMyServiceRunning();
		//if(!chkst)
		//{
		//Intent intent = new Intent(getBaseContext(),Service_call.class);
		Intent intent = new Intent(context, Service_Call_New.class);
		intent.putExtra("ClientID", ClientId);
		intent.putExtra("NotiID", "0");
		startService(intent);
		//}
	}

	// Check Service Is Running Or Not
	private boolean isMyServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			String Service_PackageName = UnComObj.GET_Service_PackageName();
			if (Service_PackageName.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}


	//Go to Next Activity after successfull Login
	public void CallNextActivity(String ClientId) {

		Start_ServiceCallNew(ClientId); // Start Background Service

		Set_App_Logo("C_" + Cid + "_4"); // Set App Logo

		MainBtnIntent = new Intent(context, MenuPage.class);
		MainBtnIntent.putExtra("AppLogo", AppLogo);
		startActivity(MainBtnIntent);
		finish();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Disp_Alert("Thankyou!", "We hope you enjoyed using it !!! We wait for your next use.", 1);
		}
		return super.onKeyDown(keyCode, event);
	}

	private void Disp_Alert(String head, String body, final int ckk) {
		ad.setTitle(Html.fromHtml("<font color='#E3256B'>" + head + "</font>"));
		ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>" + body + "</font>"));
		ad.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (ckk == 1) {
					finish();
				} else {
					ad.dismiss();
				}
			}
		});
		ad.show();
	}

}
