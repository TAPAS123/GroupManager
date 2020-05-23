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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity {
	EditText txtGroupCode, txtMob, txtPwd, txtOTP;
	TextView TxtHeadShow;
	String Str_IEMI, Str_club, Str_mobile, Str_pass, StrClub, StrMob, StrPass, WebRsp, val1, val2, ValOTP = "", val3, val4, val5, Strdtm, StrChkDT = "",
			StrOTP, WebRspOTP, WebRspOTPAgain, WebRegister, ValGuest = "";
	Context context = this;
	TelephonyManager tm;
	AlertDialog ad;
	Intent MainBtnIntent;
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
	WebServiceCall webcall;
	String[] temp;
	ProgressDialog Progsdial;
	Thread networkThread;
	boolean InternetPresent = false;
	Chkconnection chkconn;
	public final static long SECOND_MILLIS = 1000;
	public final static long MINUTE_MILLIS = SECOND_MILLIS * 60;
	public final static long HOUR_MILLIS = MINUTE_MILLIS * 60;
	public final static long DAY_MILLIS = HOUR_MILLIS * 24;
	String MenuList, AppSettings;
	byte[] AppLogo;
	String AddGrp, User;
	Button btnAddGuest, btnlogin, btnotp, btnRegistration;
	SimpleDateFormat df;
	String ClientID = "", UID = "";
	UnCommonProperties UnComObj;
	int otpchk = 0, guestchk = 0;
	Cursor cursorT;
	byte[] pic = null;
	SQLiteDatabase dbObj;
	String SharePre_GCMRegId = "";//For Shared Preference
	String AppVersion = "";
	int GroupAppCounter = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		TxtHeadShow = (TextView) findViewById(R.id.textViewshow);
		txtGroupCode = (EditText) findViewById(R.id.etclubcode);
		txtMob = (EditText) findViewById(R.id.etmobile);
		txtPwd = (EditText) findViewById(R.id.etPassword);
		txtOTP = (EditText) findViewById(R.id.etOTP);
		btnlogin = (Button) findViewById(R.id.btnlogin);
		btnotp = (Button) findViewById(R.id.btnOtp);
		btnRegistration = (Button) findViewById(R.id.btnRegistration);
		btnAddGuest = (Button) findViewById(R.id.btnAddGuest);

		Intent intent = getIntent();
		AddGrp = intent.getStringExtra("AddGrp");
		User = intent.getStringExtra("User");

		ad = new AlertDialog.Builder(this).create();
		webcall = new WebServiceCall();//Call a Webservice
		chkconn = new Chkconnection();

		Str_IEMI = new CommonClass().getIMEINumber(context);//Added On 15-02-2019

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
		String heading = UnComObj.GET_headName();
		TxtHeadShow.setText(heading);
		setTitle(AppTitle);

		Calendar c = Calendar.getInstance();
		df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		Strdtm = df.format(c.getTime());

		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE); // Initialise Shared Pref

		if (AddGrp.equals("NO")) {
			Get_SharedPref_Values(); // Get Shared Pref Values
			UpdateGroupOpenCounter();///Added On 09-07-2017 GroupWise App Counter
		} else {
			AppLogo = intent.getByteArrayExtra("AppLogo");
		}

		if (User.equals("Multi") || StrChkDT.length() != 0) {
			btnAddGuest.setVisibility(View.GONE);
		}

		////New Change 6 Oct 2015 'MainClientID' for Hide Client ID / Group Code Text Box
		String MainClientID = UnComObj.GET_MainClientID();
		if (MainClientID.length() > 1) {
			ClientID = MainClientID;// Set Global MainClientId In Client Id
			txtGroupCode.setText(ClientID);
			txtGroupCode.setVisibility(View.GONE);
			if (txtPwd.getText().length() == 0)
				txtPwd.setText(" ");
			txtPwd.setVisibility(View.GONE); // Hide Group Code TxtBox
		}
		//////////////////////////////////////////////////////////////////////////////

		InternetPresent = chkconn.isConnectingToInternet(context);

		if ((txtGroupCode.getText().length() != 0) && (txtMob.getText().length() != 0) && (txtPwd.getText().length() != 0)) {
			Txtval();
			if (InternetPresent == true) {
				CallWeb(); // Open App Online Mode
			} else if (InternetPresent == false) {
				Offline_Login(); // Open App Offline Mode
			} else {
				GoBack("Error!", "Some error please try later.", 1);
			}
		}

		btnotp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				RESendOTPAGAIN();
			}
		});

		btnlogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Txtval();
				String firstLetter = "";
				System.out.println(Str_mobile.length());
				if (Str_mobile.length() != 0) {
					firstLetter = String.valueOf(Str_mobile.charAt(0));
				}
				if (Str_club.length() == 0) {
					txtGroupCode.setError("Enter Group Name");
				} else if (Str_mobile.length() == 0) {
					txtMob.setError("Enter Mobile");
				} else if (Str_pass.length() == 0) {
					txtPwd.setError("Enter Password");
				} else {
					InternetPresent = chkconn.isConnectingToInternet(context);
					if (InternetPresent == false) {
						GoBack("Error!", "Not Authorised, Please Connect with Internet for Authorisation.", 1);
					} else {
						if (User.equals("Single")) {
							Edit_SharedPref_Values(Str_club, Str_mobile, Str_pass, "", "", "", "", "", "", "Member", "", "0", "0", "", ""); // Edit/Insert Shared Pref Values
						}
						if (otpchk == 2) {
							StrOTP = txtOTP.getText().toString();
							if (StrOTP.length() == 0) {
								txtOTP.setError("Enter OTP");
							} else if (StrOTP.equals(ValOTP)) {
								CallOTPEWBSERVICE();// Authenticate after successfull OTP Match
							} else {
								GoBack("Result!", "OTP does not Match.Please fill correct OTP", 0);
							}
						} else {
							CallWeb();
						}
					}
				}
			}
		});

		btnAddGuest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MainBtnIntent = new Intent(context, Guest_Registration.class);
				MainBtnIntent.putExtra("AddGrp", AddGrp);
				MainBtnIntent.putExtra("User", User);
				startActivity(MainBtnIntent);
				finish();
			}
		});

		btnRegistration.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				InternetPresent = chkconn.isConnectingToInternet(context);
				if (InternetPresent == true) {
					progressdial();
					networkThread = new Thread() {
						public void run() {
							try {
								WebRegister = webcall.Registration(Str_club, Str_IEMI);
								runOnUiThread(new Runnable() {
									public void run() {
										//WebRegister="Name^Text1#Address1^Text2#Address2^Text3#City^Text4#MobileNo.^Text5#EmailID^Text6#Batch^Text7#House^Text8";
										if (WebRegister.equalsIgnoreCase("Error")) {
											GoBack("Result!", "Some problem, please try later !", 1);
										} else if (WebRegister.contains("#")) {
											MainBtnIntent = new Intent(context, Registration.class);
											MainBtnIntent.putExtra("WebRegister", WebRegister);
											MainBtnIntent.putExtra("StrClub", Str_club);
											MainBtnIntent.putExtra("AppLogo", AppLogo);
											startActivity(MainBtnIntent);
											finish();
										} else {
											GoBack("Result!", WebRegister, 1);
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
				} else {
					GoBack("Result!", "Can't Register in offline mode.", 1);
				}
			}
		});
	}


	private void Txtval() {
		Str_club = txtGroupCode.getText().toString().trim();
		Str_mobile = txtMob.getText().toString().trim();
		Str_pass = txtPwd.getText().toString();
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
			txtPwd.setText(sharedpreferences.getString(pass, "").trim());
		}
		if (sharedpreferences.contains(datetm)) {
			StrChkDT = sharedpreferences.getString(datetm, "");
		}
		if (sharedpreferences.contains("SharePre_GCMRegId")) {
			SharePre_GCMRegId = sharedpreferences.getString("SharePre_GCMRegId", "");
		}

		//System.out.println(ClientID+UID+sharedpreferences.getString(pass, ""));
	}

	private void Edit_SharedPref_Values(String Club, String Mob, String Pass, String DT, String LogName, String LogId, String ClubName, String UserType, String MenuList, String Login, String AppSettings, String DOB_Disp, String timeout, String adminMenu, String ChkSync) {

		editor = sharedpreferences.edit(); // Edit Shared Pref Records

		String MemDir = "";
		System.out.println();
		if (UserType.equals("SPOUSE")) {
			MemDir = "Spouse";
		} else {
			MemDir = "Member";
		}

		String Cid = "", Uid = "";
		if (sharedpreferences.contains(cLid)) {
			Cid = sharedpreferences.getString(cLid, "");
		}
		if (sharedpreferences.contains(uid)) {
			Uid = sharedpreferences.getString(uid, "");
		}

		// Insert /Update Record in LoginMain
		SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		String SqlQry = "Select M_ID,setting_MemDir from LoginMain Where ClientID='" + Cid.trim() + "' AND UID='" + Uid.trim() + "'";
		Cursor cursorT = db.rawQuery(SqlQry, null);
		int RCount = cursorT.getCount();
		if (RCount > 0) {
			while (cursorT.moveToNext()) {
				int MID = cursorT.getInt(0);
				String Mem_Dir = cursorT.getString(1);
				String MemUpdate = "";
				if (Mem_Dir == null) {
					Mem_Dir = MemDir; ///tapas sir
					MemUpdate = ",setting_MemDir='" + MemDir.trim() + "'";
				}
				SqlQry = "UPDATE LoginMain SET ClientID='" + Club.trim() + "',UID='" + Mob.trim() + "',PWD='" + Pass.trim() + "',DDateTime='" + DT.trim() + "',LogName='" + LogName.trim() + "'," +
						"LogId='" + LogId.trim() + "',ClubName='" + ClubName.trim() + "',MenuList='" + MenuList.trim() + "',Login='" + Login.trim() + "',Appsettings='" + AppSettings.trim() + "',setting_extra_1='" + UserType.trim() + "',setting_extra_2='" + DOB_Disp.trim() + "' ,setting_extra_3='" + timeout.trim() + "',setting_extra_4='" + adminMenu.trim() + "'" +
						"" + MemUpdate + " Where M_ID=" + MID;
				//here setting_extra_1 in SqlQry is UserType
				//here setting_extra_2 in SqlQry is DOB_Disp
				editor.putString("MemDir", Mem_Dir);  //sunil sir
				break;
			}
			cursorT.close();
		} else {
			String Birday_Noti_Time = "0";///By Default display Bday and Anni Notification(Added on 29-08-2018)
			SqlQry = "Insert into LoginMain(ClientID,UID,PWD,DDateTime,LogName,LogId,ClubName,MenuList,Login,Appsettings,Tab2Count,setting_Callscrn,setting_Callscrn_posi,setting_Birday_noti_time,setting_extra_1,setting_extra_2,setting_extra_3,setting_extra_4) " +
					"Values('" + Club.trim() + "','" + Mob.trim() + "','" + Pass.trim() + "','" + DT.trim() + "','" + LogName.trim() + "','" + LogId.trim() + "','" + ClubName.trim() + "','" + MenuList.trim() + "','" + Login.trim() + "','" + AppSettings.trim() + "',0,'true','Top','" + Birday_Noti_Time + "','" + UserType.trim() + "','" + DOB_Disp.trim() + "','" + timeout.trim() + "','" + adminMenu.trim() + "')";
			//here setting_extra_1 in SqlQry is UserType
			//here setting_extra_2 in SqlQry is DOB_Disp

			editor.putString("TCount_Tab2", "0");
			editor.putBoolean("CallScreen", true);
			editor.putString("CallScreen_Position", "Top");
			editor.putString("Birday_Noti_Time", Birday_Noti_Time);
		}
		db.execSQL(SqlQry); //Insert/Update Exec Query
		db.close();

		editor.putString(cLid, Club.trim());
		editor.putString(uid, Mob.trim());
		editor.putString(pass, Pass.trim());
		editor.putString(datetm, DT.trim());
		////////////////////////////
		editor.putString(value2, LogName.trim());
		editor.putString(value3, LogId.trim());
		editor.putString(value4, ClubName.trim());
		editor.putString(ValueMenuList, MenuList.trim());
		editor.putString("Login", Login.trim());
		editor.putString("AppSettings", AppSettings.trim());
		editor.putString("UserType", UserType.trim());
		editor.putString("DOB_Disp", DOB_Disp.trim());
		editor.putString("TimeOut", timeout.trim());
		editor.putString("AdminMenu", adminMenu.trim());
		editor.putString("ChkSync", ChkSync.trim());
		editor.commit();
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

	private void CallOTPEWBSERVICE() {
		progressdial();
		networkThread = new Thread() {
			public void run() {
				try {
					WebRspOTP = webcall.loginOTP(Str_club, Str_mobile, Str_pass, Str_IEMI);
					runOnUiThread(new Runnable() {
						public void run() {
							// System.out.println(WebRspOTP);
							// Toast.makeText(getApplicationContext(),"Activated", 1).show();
							if (WebRspOTP.equalsIgnoreCase("Activated")) {
								CallWeb();
							} else if (WebRspOTP.contains("Error") || WebRspOTP.contains("TimeOut")) {
								GoBack("Result!", "Some problem, please try later!!!!", 1);
							} else {
								GoBack("Result!", WebRspOTP, 1);
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

	private void RESendOTPAGAIN() {
		progressdial();
		networkThread = new Thread() {
			public void run() {
				try {
					WebRspOTPAgain = webcall.ResendSMSOTP(Str_club, Str_mobile, Str_pass, Str_IEMI);
					runOnUiThread(new Runnable() {
						public void run() {
							String tt = WebRspOTPAgain;

							String[] Arr1 = WebRspOTPAgain.split("#");
							String v1 = Arr1[1].toString();
							if (v1.contains("OTP")) {
								ValOTP = Arr1[2].toString();
								if (ValOTP != null) {
									otpchk = 2;
								}
							}

							txtOTP.setText("");
							GoBack("Result!", v1, 5);
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


	private void CallWeb() {
		progressdial();
		networkThread = new Thread() {
			public void run() {
				try {
					//System.out.println(Str_club+":"+Str_mobile+":"+Str_pass+":"+Str_IEMI);
					WebRsp = webcall.loginD(Str_club, Str_mobile, Str_pass, Str_IEMI, SharePre_GCMRegId, AppVersion);
					runOnUiThread(new Runnable() {
						public void run() {
							//WebRsp="Y#ASHISH AGARWAL#1#JCI Kanpur#103.21.58.192^MDA_Clubs^MDA_Club^MDA.1234_^C_KJC_2#User#DIR^PRO^EVE^NEWS^GOV^PAST^JAYCEE^@ @1^1#SPOUSE#0#15#";
							System.out.println(WebRsp);
							//Toast.makeText(getApplicationContext(),"callweb"+"  "+WebRsp, 1).show();
							if (WebRsp.contains("#")) {
								temp = WebRsp.split("#");
								val1 = temp[0].toString();
								val2 = temp[1].toString();
								if (val2.contains("OTP")) {
									ValOTP = temp[2].toString();
									if (ValOTP != null) {
										otpchk = 2;
									}
								} else if (val2.contains("Please fill password correctly..")) {
									txtPwd.setVisibility(View.VISIBLE); // Hide Group Code TxtBox
									txtPwd.setText("");
									txtPwd.setHint("Enter password");
								} else {
									if (val1.equalsIgnoreCase("N") && (temp.length > 2)) {
										ValOTP = temp[2].toString();
										if (ValOTP.equals("Reg")) {
											otpchk = 3;
										} else {
											otpchk = 4;
										}
										if (temp.length == 4) {
											ValGuest = temp[3].toString();
											if (ValGuest.equalsIgnoreCase("Guest")) {
												guestchk = 1;
											} else {
												guestchk = 0;
											}
										}
									}
								}
								if (val1.equals("Y")) {
									val3 = temp[2].toString();
									val4 = temp[3].toString();
									val5 = temp[5].toString();// UserType(ie User/Admin/Spouse)
									MenuList = temp[6].toString();
									AppSettings = temp[7].toString();
									String DOB_Disp = temp[8].toString();//DOB_Disp is used for Display DOB or not(i.e [default] 0=DOB Display,1=DOB Not Display,2=DOB Display but DobYear Not Displayed)
									String timeout = temp[9].toString();//timeout if(15 then default value) else (given time)
									String adminMenu = temp[10].toString();//in admin login these item show (addmw^ghhj^) 18-12-15
									String ChkSyncVal = temp[11].toString();//ChkSync Required Value	28-10-2016

									// Create DataBase and Tables Of Authorised ClientID
									Create_All_Tables();
									////////////////////////////////////////////////////

									Edit_SharedPref_Values(Str_club, Str_mobile, Str_pass, Strdtm, val2, val3, val4, val5, MenuList, "Member", AppSettings, DOB_Disp, timeout, adminMenu, ChkSyncVal); // Edit/Insert Shared Pref Values

									CallNextActivity(Str_club);//Go To Next Activity

								} else if (val1.equals("N")) {
									txtPwd.setVisibility(View.VISIBLE);
									if (txtPwd.length() == 1) {
										txtPwd.setText("");
										txtPwd.setHint("Enter Password");
									}
									//System.out.println(Str_club+" "+Str_mobile+" "+Str_pass);
									//Edit_SharedPref_Values(Str_club,Str_mobile,Str_pass,"","","","","","Member"); // Edit/Insert Shared Pref Values
									GoBack("Result!", val2, otpchk);
								}
							} else if (WebRsp.contains("Connection TimeOut")) {
								Offline_Login(); // Open App Offline Mode
							} else {
								Offline_Login();
								//goback("Error2!","Some error please try later or fill correct entry.",0);
							}

							if (guestchk == 1) {
								btnAddGuest.setVisibility(View.VISIBLE);
							} else {
								btnAddGuest.setVisibility(View.GONE);
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
		dbObj.execSQL("CREATE TABLE IF NOT EXISTS C_" + Str_club + "_2 (M_Id INTEGER PRIMARY KEY,M_Name Text,M_FName Text,M_MName Text,M_LName Text,M_Add1 Text,M_Add2 Text,M_Add3 Text,M_City Text,M_Email Text,M_Gender Text,M_MrgAnn_D Text,M_MrgAnn_M Text,M_MrgAnn_Y Text,M_DOB_D Text,M_DOB_M Text,M_DOB_Y Text,M_Mob Text,M_Pin Text,M_BG Text,M_BussNm Text,M_BussCate Text,M_MemSince_D Text,M_MemSince_M Text,M_MemSince_Y Text,M_Pic BLOB,S_Name Text,S_FName Text,S_Mname Text,S_LName Text,S_Mob Text,S_Email Text,S_DOB_D Text,S_DOB_M Text,S_DOB_Y Text,S_BG Text,S_Pic BLOB,C1_Name Text,C1_FName Text,C1_Mname Text,C1_LName Text,C1_Mob Text,C1_Email Text,C1_Gender Text,C1_DOB_D Text,C1_DOB_M Text,C1_DOB_Y Text,C1_BG Text,C1_Pic BLOB,C2_Name Text,C2_FName     Text,C2_Mname Text,C2_LName Text,C2_Mob  Text,C2_Email Text,C2_Gender Text,C2_DOB_D Text,C2_DOB_M Text,C2_DOB_Y Text,C2_BG Text,C2_Pic BLOB,C3_Name Text,C3_FName Text,C3_Mname Text,C3_LName Text,C3_Mob Text,C3_Email Text,C3_Gender   Text,C3_DOB_D     Text,C3_DOB_M     Text,C3_DOB_Y     Text,C3_BG   Text,C3_Pic  BLOB,C4_Name Text,C4_FName     Text,C4_Mname     Text,C4_LName Text,C4_Mob Text,C4_Email Text,C4_Gender Text,C4_DOB_D Text,C4_DOB_M Text,C4_DOB_Y Text,C4_BG Text,C4_Pic BLOB,Pass Text,Photo1  BLOB,Photo2  BLOB,Photo3  BLOB,Photo4  BLOB,Photo5  BLOB,Photo6  BLOB,Admin Int,MemNo Text,Oth Text, SYNCID Int,SyncDT INTEGER)");

		//Create Table 4
		dbObj.execSQL("CREATE TABLE IF NOT EXISTS C_" + Str_club + "_4 (M_ID INTEGER PRIMARY KEY,Rtype Text,Text1 Text,Text2 Text,Text3 Text,Date1 Text,Date2 Text,Date3 Text,Date1_1 INTEGER,Date2_1 INTEGER,Date3_1 INTEGER,Num1 INTEGER,Num2 INTEGER,Num3 INTEGER,Add1 TEXT,Photo1 BLOB,Text4 Text,Text5 Text,Text6 Text,Text7 Text,Text8 Text,Add2 Text,SYNCID Int,SyncDT INTEGER)");

		//Create Table MISC
		dbObj.execSQL("CREATE TABLE IF NOT EXISTS C_" + Str_club + "_MISC (M_ID INTEGER PRIMARY KEY,Rtype Text,MemId INTEGER," +
				"Text1 Text,Text2 Text,Text3 Text,Text4 Text,Text5 Text,Text6 Text,Text7 Text,Text8 Text,Text9 Text," +
				"Text10 Text,Text11 Text,Text12 Text,Text13 Text,Text14 Text,Text15 Text,Text16 Text,Text17 Text,Text18 Text,Text19 Text," +
				"Text20 Text,Text21 Text,Text22 Text,Text23 Text,Text24 Text,Text25 Text,Text26 Text,Text27 Text,Text28 Text,Text29 Text," +
				"Text30 Text,Text31 Text,Text32 Text,Text33 Text,Text34 Text,Text35 Text,Text36 Text,Text37 Text,Text38 Text,Text39 Text," +
				"Text40 Text,Text41 Text,Text42 Text,Text43 Text,Text44 Text,Text45 Text,Text46 Text,Text47 Text,Text48 Text,Text49 Text,Text50 Text," +
				"Add1 Text,Add2 Text,Add3 Text,Add4 Text,Add5 Text,SYNCID INTEGER,SyncDT INTEGER)");

		//Create Table Family
		dbObj.execSQL("CREATE TABLE IF NOT EXISTS C_" + Str_club + "_Family (M_ID INTEGER PRIMARY KEY,MemId INTEGER," +
				"Name Text,Relation Text,Father Text,Mother Text,Address Text,Current_Loc Text,Mob_1 Text,Mob_2 Text,DOB_D Text," +
				"DOB_M Text,DOB_Y Text,EmailID Text,Age Text,Birth_Time Text,Birth_Place Text,Gotra Text,Remark Text,Pic BLOB,Education Text," +
				"Work_Profile Text,Text1 Text,Text2 Text,Text3 Text,Text4 Text,Text5 Text,Text6 Text,Text7 Text,Text8 Text,Text9 Text," +
				"Text10 Text,SYNCID INTEGER,SyncDT INTEGER)");

		//Create Table Event
		dbObj.execSQL("CREATE TABLE IF NOT EXISTS C_" + Str_club + "_Event (M_ID INTEGER PRIMARY KEY,MemId INTEGER," +
				"Num INTEGER,Num1 INTEGER,Num2 INTEGER,Num3 INTEGER,Num4 INTEGER,Num5 INTEGER,Rtype Text," +
				"Text1 Text,Text2 Text,Text3 Text,Text4 Text,Text5 Text,Sync INTEGER)");

		//Create Table OpinionPoll 1 (added on 08-04-2017)
		dbObj.execSQL("CREATE TABLE IF NOT EXISTS C_" + Str_club + "_OP1 (M_ID INTEGER PRIMARY KEY,OP_Name Text," +
				"OP_From Text,OP_TO Text,OP_From_1 INTEGER,Op_To_1 INTEGER,Cond1 Text,Cond2 Text,Co_Type Text," +
				"Time_Req INTEGER,SyncID INTEGER,SyncDt INTEGER)");

		//Create Table OpinionPoll 2 (added on 08-04-2017)
		dbObj.execSQL("CREATE TABLE IF NOT EXISTS C_" + Str_club + "_OP2 (M_ID INTEGER PRIMARY KEY,OP1_ID INTEGER," +
				"PSNO INTEGER,SNO Text,Question Text,Ans1 Text,Ans2 Text,Ans3 Text,Ans4 Text,Remark Text," +
				"Answer Text,SyncID INTEGER,SyncDt INTEGER,Remark_Req INTEGER)");

		//Create Table OpinionPoll 3 (added on 11-04-2017)
		dbObj.execSQL("CREATE TABLE IF NOT EXISTS C_" + Str_club + "_OP3 (ID INTEGER PRIMARY KEY,OP1_ID INTEGER," +
				"OP2_ID INTEGER,User_Ans Text,Remark Text,HoldForLater INTEGER,Submit INTEGER,Timeout INTEGER,SyncID INTEGER)");

		//Make Alter Table Columns in Table OpinionPoll 1 //Added on 15-04-2017
		String SqlQry = "ALTER TABLE C_" + Str_club + "_OP1 ADD COLUMN Op_Publish INTEGER";
		MakeAlterColumn(SqlQry);

		//Make Alter Table Columns in Table OpinionPoll 1 //Added on 24-04-2017
		SqlQry = "ALTER TABLE C_" + Str_club + "_OP1 ADD COLUMN U_Ans INTEGER";
		MakeAlterColumn(SqlQry);

		//Make Alter Table Columns in Table OpinionPoll 1 //Added on 24-04-2017
		SqlQry = "ALTER TABLE C_" + Str_club + "_OP1 ADD COLUMN Time_Remains INTEGER";
		MakeAlterColumn(SqlQry);

		//Make Alter Table Columns in Table 2
		SqlQry = "ALTER TABLE C_" + Str_club + "_2 ADD COLUMN M_SndAdd1 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_2 ADD COLUMN M_SndAdd2 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_2 ADD COLUMN M_SndAdd3 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_2 ADD COLUMN M_SndAdd4 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_2 ADD COLUMN M_SndPin Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_2 ADD COLUMN M_SndMob Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_2 ADD COLUMN M_SndMob1 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_2 ADD COLUMN M_SndStd Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_2 ADD COLUMN Oth1 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_2 ADD COLUMN Oth2 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_2 ADD COLUMN Oth3 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_2 ADD COLUMN M_Land1 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_2 ADD COLUMN M_Land2 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_2 ADD COLUMN M_Email1 Text";
		MakeAlterColumn(SqlQry);
		///////////////////////////////////////////////////

		//Make Alter Table Columns in Table 4 New Update 12-05-2016 by Tapas
		SqlQry = "ALTER TABLE C_" + Str_club + "_4 ADD COLUMN COND1 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_4 ADD COLUMN COND2 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_4 ADD COLUMN COND3 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_4 ADD COLUMN COND4 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_4 ADD COLUMN COND5 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_4 ADD COLUMN COND6 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_4 ADD COLUMN COND7 Text";
		MakeAlterColumn(SqlQry);
		////////////////////////////////////

		//Make Alter Table Columns in Table Family New Update 06-06-2016 by Tapas
		SqlQry = "ALTER TABLE C_" + Str_club + "_Family ADD COLUMN Text11 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_Family ADD COLUMN Text12 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_Family ADD COLUMN Text13 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_Family ADD COLUMN Text14 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_Family ADD COLUMN Text15 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_Family ADD COLUMN Text16 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_Family ADD COLUMN Text17 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_Family ADD COLUMN Text18 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_Family ADD COLUMN Text19 Text";
		MakeAlterColumn(SqlQry);
		SqlQry = "ALTER TABLE C_" + Str_club + "_Family ADD COLUMN Height Real";
		MakeAlterColumn(SqlQry);
		////////////////////////////////////

		dbObj.close();//Database Close
	}

	private void MakeAlterColumn(String SqlQry) {
		try {
			dbObj.execSQL(SqlQry);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
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
	private void Offline_Login() {
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
				GoBack("Error!", "Synchronisation is required so connection with internet.", 1);
			} else {
				CallNextActivity(Str_club);
			}
		} else {
			GoBack("Result!", "Authorisation Failed.Please check Internet Connection !", 1);
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

		int count = 0;
		Set_App_Logo("C_" + Str_club + "_4"); // Set App Logo
		SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		String StrQ = "select M_Id from " + "C_" + Str_club + "_4" + " where Rtype='SPLASH'";
		cursorT = db.rawQuery(StrQ, null);
		count = cursorT.getCount();
		cursorT.close();
		if (count > 0) {
			db.close();
			MainBtnIntent = new Intent(context, SplashScreen.class);
			MainBtnIntent.putExtra("AppLogo", AppLogo);
			startActivity(MainBtnIntent);
			finish();
		} else {
			StrQ = "select photo1 from " + "C_" + Str_club + "_4" + " where Rtype='SPLASH1'";
			cursorT = db.rawQuery(StrQ, null);
			count = cursorT.getCount();
			if (count > 0) {
				try {
					if (cursorT.moveToFirst()) {
						do {
							pic = cursorT.getBlob(0);
						} while (cursorT.moveToNext());
					}
				} catch (Exception ec) {
					System.out.println(ec.getMessage());
				}
				MainBtnIntent = new Intent(context, SplashScreen1.class);
				MainBtnIntent.putExtra("AppLogo", AppLogo);
				MainBtnIntent.putExtra("BgPic", pic);
				startActivity(MainBtnIntent);
				finish();
			} else {
				MainBtnIntent = new Intent(context, MenuPage.class);
				MainBtnIntent.putExtra("AppLogo", AppLogo);
				startActivity(MainBtnIntent);
				finish();
			}
			cursorT.close();
			db.close();
		}
	}


	///GroupWise Counter Add Locally
	private void UpdateGroupOpenCounter() {
		int RCounter = 0, MID = 0;
		try {
			SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
			String SqlQry = "Select M_Id,setting_extra_10 from LoginMain Where Upper(ClientID)='" + ClientID.toUpperCase() + "' AND DDateTime is not null AND length(DDateTime)<>0";
			Cursor cursorT = db.rawQuery(SqlQry, null);
			int RCount = cursorT.getCount();
			if (RCount > 0) {
				if (cursorT.moveToFirst()) {
					MID = cursorT.getInt(0);
					GroupAppCounter = cursorT.getInt(1);
				}
				RCounter = GroupAppCounter + 1;
			}
			cursorT.close();//Close Cursor

			SqlQry = "UPDATE LoginMain SET setting_extra_10='" + RCounter + "' Where M_ID=" + MID;
			db.execSQL(SqlQry);

			db.close();//Close database
		} catch (Exception e) {
			e.printStackTrace();
		}
		////////////////////////////
	}


	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			GoBack("Thankyou!", "We hope you enjoyed using it !!! We wait for your next use.", 1);
		}
		return super.onKeyDown(keyCode, event);
	}

	@SuppressWarnings("deprecation")
	private void GoBack(String head, String body, final int ckk) {
		ad.setTitle(Html.fromHtml("<font color='#E3256B'>" + head + "</font>"));
		ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>" + body + "</font>"));
		ad.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (ckk == 1) {
					txtOTP.setVisibility(View.GONE);
					btnotp.setVisibility(View.GONE);
					finish();
				} else if (ckk == 2) {
					txtOTP.setVisibility(View.VISIBLE);
					btnotp.setVisibility(View.VISIBLE);
					ad.dismiss();
				} else if (ckk == 3) {
					btnRegistration.setVisibility(View.VISIBLE);
					ad.dismiss();
				} else if (ckk == 4) {
					btnRegistration.setVisibility(View.GONE);
					ad.dismiss();
				} else {
					ad.dismiss();
				}

			}
		});
		ad.show();
	}

}
