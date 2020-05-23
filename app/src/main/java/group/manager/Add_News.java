package group.manager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Add_News extends Activity {

	final Context context = this;
	byte[] AppLogo;
	String ClubName, Str_user, Tab2Name, Tab4Name, TabGroup, addchk, SMSEnabled, Msg = "",Log, Logid;
	SimpleDateFormat df;
	AlertDialog ad;
	Button btnSubmit,btnPendingNews, btnGroup, btnSelectMember;
	EditText txtTitle, txtDesc;
	TextView txtDate;
	SQLiteDatabase db;
	ProgressDialog Progsdial;
	ArrayList<Product> products;
	Adapter_News_Group AdpGroup = null;
	private boolean InternetPresent;
	WebServiceCall webcall;
	RadioGroup rdGrpSendSMS;
	RadioButton rdbtnSMSAll, rdbtnSMSWithOutApp;
	LinearLayout LLSMS;
	ArrayList<Product> ArrList_Product1, ArrList_Product2;
	String[] ArrM_ids, ArrS_ids;
	Adapter_News_Group AdpGroup1, AdpGroup2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_news);

		TextView txtHead = (TextView) findViewById(R.id.txtHead);
		txtDate = (TextView) findViewById(R.id.EdDate);
		txtTitle = (EditText) findViewById(R.id.EDTitle);
		txtDesc = (EditText) findViewById(R.id.EDDesc);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		btnPendingNews = (Button) findViewById(R.id.btnPendingNews);
		btnGroup = (Button) findViewById(R.id.btnGroup);
		btnSelectMember = (Button) findViewById(R.id.btnSelectMember);
		LLSMS = (LinearLayout) findViewById(R.id.LLSMS);

		rdGrpSendSMS = (RadioGroup) findViewById(R.id.rdGrpSendSMS);
		rdbtnSMSAll = (RadioButton) findViewById(R.id.rdbtnSMSAll);
		rdbtnSMSWithOutApp = (RadioButton) findViewById(R.id.rdbtnSMSWithOutApp);

		Intent menuIntent = getIntent();
		addchk = menuIntent.getStringExtra("addchk");
		SMSEnabled = menuIntent.getStringExtra("SMSEnabled");
		Log = menuIntent.getStringExtra("Clt_Log");
		Logid = menuIntent.getStringExtra("Clt_LogID");
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		Str_user = menuIntent.getStringExtra("UserClubName");
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");

		Tab2Name = "C_" + Str_user + "_2";
		Tab4Name = "C_" + Str_user + "_4";
		TabGroup = "C_" + Str_user + "_Group";// Table Group

		btnGroup.setVisibility(View.GONE);//Default Button Group Hide

		webcall = new WebServiceCall();//Call a Webservice
		ad = new AlertDialog.Builder(this).create();

		Set_App_Logo_Title(); // Set App Logo and Title

		Typeface face=Typeface.createFromAsset(getAssets(), "calibri.ttf");
		txtHead.setTypeface(face);

		if (SMSEnabled.equals("NO")) {
			LLSMS.setVisibility(View.GONE);
		}

		// set Current Date
		Date CurDt = new Date();
		df = new SimpleDateFormat("dd-MM-yyyy");
		txtDate.setText(df.format(CurDt));

		//Check News Pending(NotUpdated) or Uptodate(Updated) 
		//Insert Data in local Table
		db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		String SqlQry = "Select Count(M_id) from " + Tab4Name + " Where Rtype='Add_News'";
		Cursor cursorT = db.rawQuery(SqlQry, null);
		int TCount = 0;
		while (cursorT.moveToNext()) {
			TCount = cursorT.getInt(0);
			break;
		}
		cursorT.close();
		db.close();

		if (TCount == 0) {
			btnPendingNews.setVisibility(View.GONE);
		} else {
			btnPendingNews.setVisibility(View.VISIBLE);
			btnPendingNews.setText(Html.fromHtml("Pending News (<font color='#FFFF00'><b>" + TCount + "</b></font>)"));
		}

		WebCall_GetAllGroup();//Get All Group from Webservice

		txtDesc.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				Msg = txtDesc.getText().toString().trim();
				if ((txtDesc.getText().toString().contains("\"")) || (txtDesc.getText().toString().contains("'"))) {
					SpecialCharAlert();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub	
			}
		});


		txtDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Show_Date_Dialog(txtDate);
			}
		});

		btnSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				String DDate = txtDate.getText().toString().trim();
				String Title = txtTitle.getText().toString().trim();
				String Desc = txtDesc.getText().toString().trim();
				if (Title.length() == 0) {
					txtTitle.setError("Input Title");
				} else if (Desc.length() == 0) {
					txtDesc.setError("Input Description");
				} else {

					//// Get Selected Groups 
					String StrGroupIds = "";

					if (AdpGroup != null) {
						for (Product p : AdpGroup.getBox()) {
							if (p.box) {
								StrGroupIds += p.GroupId + ",";
							}
						}
					}
					/////////////////////////

					if (StrGroupIds.contains(","))
						StrGroupIds = StrGroupIds.substring(0, StrGroupIds.length() - 1);


					if (StrGroupIds.length() == 0) {
						String MS_Ids = Selected_MS_Ids(); // Get Selected MS Ids
						if (!MS_Ids.equals("#")) {
							StrGroupIds = MS_Ids;//Here GroupIds Contains Member and Spouse Ids
						}
					}


					// get selected radio button from radioGroup rdGrpSendSMS
					int chkId = rdGrpSendSMS.getCheckedRadioButtonId();
					// find the radiobutton(All Seleted or WithOutApp) by returned id
					RadioButton rdbtnSMS = (RadioButton) findViewById(chkId);

					//Send SMS 0='Not Send Sms', 1="Send SMs to All Selected", 2="Send SMS withoutApp"
					String SendSMS = "0";
					if (rdbtnSMS != null) {
						SendSMS = rdbtnSMS.getText().toString().trim();
						if (SendSMS.contains("All Selected")) {
							SendSMS = "1";
						} else {
							SendSMS = "2";
						}
					}

					//Insert Data in local Table
					db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
					String Qry = "Select Min(M_id) from " + Tab4Name + "";
					Cursor cursorT = db.rawQuery(Qry, null);
					int Min_Mid = 0;
					while (cursorT.moveToNext()) {
						Min_Mid = cursorT.getInt(0);
						break;
					}
					cursorT.close();

					Min_Mid = Min_Mid - 1000;

					Qry = "Insert into " + Tab4Name + "(M_id,Rtype,Text1,Text2,Add1,Text8,Text7) Values(" + Min_Mid + ",'Add_News','" + DDate + "','" + Title + "','" + Desc + "','" + StrGroupIds + "','" + SendSMS + "')";
					db.execSQL(Qry);
					db.close();

					Sync_Add_News(Str_user, Min_Mid + "");// Sync M-S Add_News
				}
			}
		});

		//Select Group
		btnGroup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String MS_Ids = Selected_MS_Ids(); // Get Selected MS Ids

				if (!MS_Ids.equals("#")) {
					AlertDisplay("", "Please Choose any one Group / Member !", false);
					return;
				}

				ShowSelectGroupDialog();
			}
		});

		btnPendingNews.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent iIntent = new Intent(getBaseContext(), NewsMain.class);
				iIntent.putExtra("Count", 22);
				iIntent.putExtra("POstion", 0);
				iIntent.putExtra("Clt_Log", Log);
				iIntent.putExtra("Clt_LogID", Logid);
				iIntent.putExtra("Clt_ClubName", ClubName);
				iIntent.putExtra("UserClubName", Str_user);
				iIntent.putExtra("AppLogo", AppLogo);
				startActivity(iIntent);
				finish();
			}
		});


		btnSelectMember.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				//// Get Selected Groups
				String StrGroupIds = "";

				if (AdpGroup != null) {
					for (Product p : AdpGroup.getBox()) {
						if (p.box) {
							StrGroupIds += p.GroupId + ",";
						}
					}
				}
				/////////////////////////

				if (StrGroupIds.length() > 0) {
					AlertDisplay("", "Please Choose any one Group / Member !", false);
					return;
				}

				String MemIds = "", SpouseIds = "";
				String MS_Ids = Selected_MS_Ids(); // Get Selected MS Ids
				String[] Arr = MS_Ids.split("#");

				if (Arr.length > 0)
					MemIds = Arr[0];// Get Member Ids

				if (Arr.length > 1)
					SpouseIds = Arr[1];// Get Member Ids
				
				/*String Mtxt="",Stxt="";
				if(!MemIds.equals("0,"))
					Mtxt=" ("+MemIds.split(",").length+")"; //Selected Member Mids Total
					  
				if(!SpouseIds.equals("0,"))
					Stxt=" ("+SpouseIds.split(",").length+")"; //Selected Spouse Mids Total*/

				//btnAddMember.setText("Select Member"+Mtxt);
				//btnAddSpouse.setText("Select Spouse"+Stxt);

				if (MemIds.length() > 1)
					ArrM_ids = MemIds.split(",");

				if (SpouseIds.length() > 1)
					ArrS_ids = SpouseIds.split(",");

				SetArrayListAndAdpterMS();//Set ArrayList and Adapter of Member and Spouse Data
				ShowMSDialog();
			}
		});
	}



	/// Added on 08-03-2019
	private void Show_Date_Dialog(final TextView txt1)
	{
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog datePickerDialog = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
										  int monthOfYear, int dayOfMonth) {
						txt1.setText(convertDateToString(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
					}
				}, year, month, day);
		datePickerDialog.show();
	}

	private String convertDateToString(String str_dt) {
		Date d = convertStringToDate(str_dt);
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String str_date = formatter.format(d);
		return str_date;
	}


	private Date convertStringToDate(String str_date) {
		DateFormat formatter;
		Date date = null;
		try {
			formatter = new SimpleDateFormat("dd-MM-yyyy");
			date = formatter.parse(str_date);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}


	// Get All GroupName with Id ///////////
	public void WebCall_GetAllGroup() {
		progressdial();
		Thread T1 = new Thread() {
			@Override
			public void run() {
				try {
					db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);

					//Create Local Table Group for Add News to Selected Group
					db.execSQL("CREATE TABLE IF NOT EXISTS " + TabGroup + " (M_ID INTEGER PRIMARY KEY,GroupId INTEGER,GroupName Text,A1 Text,A2 Text,A3 Text)");

					String WResult = webcall.GetAllGroup(Str_user);
					if (WResult.contains("^")) {
						String[] SArr = WResult.split("#");

						String Qry = "Delete from " + TabGroup;
						db.execSQL(Qry);

						for (int i = 0; i < SArr.length; i++) {
							String[] Arr = SArr[i].replace("^", "#").split("#");
							String GroupName = Arr[0].trim();
							String GroupId = Arr[1].trim();

							Qry = "Insert into " + TabGroup + "(GroupId,GroupName) Values(" + GroupId + ",'" + GroupName + "')";
							db.execSQL(Qry);
						}
					} else if (WResult.contains("No Record Found")) {
						String Qry = "Delete from " + TabGroup;
						db.execSQL(Qry);
					}
					db.close();//Close DB

					runOnUiThread(new Runnable() {
						public void run() {
							GetGroupList();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				Progsdial.dismiss();
			}
		};
		T1.start();
	}


	// Get/Fill GroupName with Ids from Local Table Group
	private void GetGroupList() {
		products = new ArrayList<Product>();

		db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);

		//Get Group Table Data
		String qry = "Select GroupId,GroupName from " + TabGroup + " Order by Upper(GroupName)";
		Cursor cursorT = db.rawQuery(qry, null);
		int RCount = cursorT.getCount();
		if (RCount > 0) {
			btnGroup.setVisibility(View.VISIBLE);
			if (cursorT.moveToFirst()) {
				do {
					int GroupId = cursorT.getInt(0);
					String GroupName = cursorT.getString(1);
					products.add(new Product(GroupName, GroupId, false));
				} while (cursorT.moveToNext());
			}
		}
		cursorT.close();
		db.close();
	}


	// Display Popup Screen of Member/Spouse Selection
	private void ShowMSDialog() {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
		dialog.setContentView(R.layout.member_spouse_list);
		dialog.setCancelable(false);
		dialog.show();

		final Button btnAddMember = (Button) dialog.findViewById(R.id.btnAddMember);
		final Button btnAddSpouse = (Button) dialog.findViewById(R.id.btnAddSpouse);
		final ListView Lv1 = (ListView) dialog.findViewById(R.id.Lv1);
		Button btnBack = (Button) dialog.findViewById(R.id.btnBack);

		if (AdpGroup1 != null) {
			Lv1.setVisibility(View.VISIBLE);
			Lv1.setAdapter(AdpGroup1);
		}

		btnAddMember.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (AdpGroup1 != null) {
					Lv1.setVisibility(View.VISIBLE);
					Lv1.setAdapter(AdpGroup1);
				}
			}
		});


		btnAddSpouse.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (AdpGroup2 != null) {
					Lv1.setVisibility(View.VISIBLE);
					Lv1.setAdapter(AdpGroup2);
				}
			}
		});


		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
             
              /*String MS_Ids=Selected_MS_Ids(); // Get Selected MS Ids
			  String Mtxt="",Stxt="";
			  if(!MS_Ids.trim().equals("#")){
				  MS_Ids=MS_Ids+" ";
				  String[] Arr=MS_Ids.split("#");
				  String M_Ids=Arr[0].trim();
				  String S_Ids=Arr[1].trim();
				  
				  if(M_Ids.length()>0)
					  Mtxt=" ("+M_Ids.split(",").length+")"; //Selected Member Mids Total
				  
				  if(S_Ids.length()>0)
				     Stxt=" ("+S_Ids.split(",").length+")"; //Selected Spouse Mids Total
			  }*/
				//btnAddMember.setText("Select Member"+Mtxt);
				//btnAddSpouse.setText("Select Spouse"+Stxt);
				dialog.dismiss();

			}
		});
	}


	// Fill Member/Spouse ArrayList with Ids
	private void SetArrayListAndAdpterMS() {
		ArrList_Product1 = new ArrayList<Product>();//For Member
		ArrList_Product2 = new ArrayList<Product>();//For Spouse

		db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);

		//Get Member Name with Mids from Table2
		String qry = "Select M_ID,M_Name,(C4_BG || \"\" || ifnull(C4_DOB_Y,'')) from " + Tab2Name + " Where M_Name Is Not NULL AND LENGTH(M_Name)<>0 Order by Upper(M_Name)";
		Cursor cursorT = db.rawQuery(qry, null);
		int RCount = cursorT.getCount();
		if (RCount > 0) {
			if (cursorT.moveToFirst()) {
				do {
					int M_Id = cursorT.getInt(0);
					String MemName = cursorT.getString(1);
					String PrefName = cursorT.getString(2);
					if (PrefName == null)
						PrefName = "";

					if (PrefName.trim().length() > 0)
						MemName = PrefName.trim() + " " + MemName;

					boolean Chk = false;
					if (ArrM_ids != null) {
						for (int i = 0; i < ArrM_ids.length; i++) {
							int MMid = Integer.parseInt(ArrM_ids[i].trim());
							if (M_Id == MMid) {
								Chk = true;
								break;
							}
						}
					}

					ArrList_Product1.add(new Product(MemName, M_Id, Chk));
				} while (cursorT.moveToNext());
			}
		}
		cursorT.close();


		//Get Spouse Name with Mids from Table2
		qry = "Select M_ID,S_Name,C3_BG from " + Tab2Name + " Where S_Name Is Not NULL AND LENGTH(S_Name)<>0 Order by Upper(S_Name)";
		cursorT = db.rawQuery(qry, null);
		RCount = cursorT.getCount();
		if (RCount > 0) {
			if (cursorT.moveToFirst()) {
				do {
					int M_Id = cursorT.getInt(0);
					String SName = cursorT.getString(1);
					String PrefName = cursorT.getString(2);
					if (PrefName == null)
						PrefName = "";

					if (PrefName.trim().length() > 0)
						SName = PrefName.trim() + " " + SName;

					boolean Chk = false;
					if (ArrS_ids != null) {
						for (int i = 0; i < ArrS_ids.length; i++) {
							int SMid = Integer.parseInt(ArrS_ids[i].trim());
							if (M_Id == SMid) {
								Chk = true;
								break;
							}
						}
					}

					ArrList_Product2.add(new Product(SName, M_Id, Chk));
				} while (cursorT.moveToNext());
			}
		}
		cursorT.close();

		db.close();

		// Set Member/Spouse List Adapter
		if (ArrList_Product1.size() != 0) {
			AdpGroup1 = new Adapter_News_Group(this, ArrList_Product1);
		}

		if (ArrList_Product2.size() != 0) {
			AdpGroup2 = new Adapter_News_Group(this, ArrList_Product2);
		}
	}


	//Get Selected Member and Spouse Ids
	private String Selected_MS_Ids() {
		String Str_M_Ids = "", Str_S_Ids = "";

		//// Get Selected Members 
		if (AdpGroup1 != null) {
			for (Product p : AdpGroup1.getBox()) {
				if (p.box) {
					Str_M_Ids += p.GroupId + ",";
				}
			}
		}
		if (Str_M_Ids.contains(","))
			Str_M_Ids = Str_M_Ids.substring(0, Str_M_Ids.length() - 1);
		/////////////////////////


		//// Get Selected Spouses
		if (AdpGroup2 != null) {
			for (Product p : AdpGroup2.getBox()) {
				if (p.box) {
					Str_S_Ids += p.GroupId + ",";
				}
			}
		}
		if (Str_S_Ids.contains(","))
			Str_S_Ids = Str_S_Ids.substring(0, Str_S_Ids.length() - 1);
		/////////////////////////

		String Selected_MSIds = Str_M_Ids.trim() + "#" + Str_S_Ids.trim();

		return Selected_MSIds;
	}


	// Display Popup Screen of Group List
	private void ShowSelectGroupDialog() {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
		dialog.setContentView(R.layout.additional_data);
		dialog.setCancelable(false);
		dialog.show();

		TextView TvHead = (TextView) dialog.findViewById(R.id.tvTt);
		ListView Lv1 = (ListView) dialog.findViewById(R.id.Lv1);
		Button btnBack = (Button) dialog.findViewById(R.id.btnBack);

		TvHead.setText(Html.fromHtml("<b>Select Group</b>"));
		Typeface face=Typeface.createFromAsset(getAssets(), "calibri.ttf");
		TvHead.setTypeface(face);

		if (products.size() != 0) {
			AdpGroup = new Adapter_News_Group(this, products);
			Lv1.setVisibility(View.VISIBLE);
			Lv1.setAdapter(AdpGroup);
		}

		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}


	//Sync Add News M-S ////
	public void Sync_Add_News(final String ClientId, final String M_Id) {
		progressdial();
		Thread T2 = new Thread() {
			@Override
			public void run() {
				try {
					Chkconnection chkconn = new Chkconnection();//Intialise Chkconnection Object
					InternetPresent = chkconn.isConnectingToInternet(context);
					if (InternetPresent == true) {
						SQLiteDatabase DBObj = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
						String SqlQry = "Select Text1,Text2,Add1,Text8,Text7 from " + Tab4Name + " Where Rtype='Add_News' AND M_Id=" + M_Id;
						Cursor cursorT = DBObj.rawQuery(SqlQry, null);
						String DDate, Title, Desc, GrpIds, SendSMS, WebResult;
						while (cursorT.moveToNext()) {
							DDate = cursorT.getString(0);
							Title = cursorT.getString(1);
							Desc = cursorT.getString(2);
							GrpIds = cursorT.getString(3);
							SendSMS = cursorT.getString(4);
							WebResult = webcall.Sync_Add_News(ClientId, DDate, Title, Desc, GrpIds, SendSMS);
							if (WebResult.contains("Record Saved")) {
								SqlQry = "Delete from " + Tab4Name + " Where Rtype='Add_News' and M_id=" + M_Id;
								DBObj.execSQL(SqlQry);
							}
							break;
						}
						cursorT.close();
						DBObj.close();
					}

					runOnUiThread(new Runnable() {
						public void run() {
							AlertDisplay("Result", "News Added Successfully !", true);
						}
					});
				} catch (Exception e) {
					//System.out.println(e.getMessage());
					e.printStackTrace();
				}
				Progsdial.dismiss();
			}
		};
		T2.start();
	}

	protected void progressdial() {
		Progsdial = new ProgressDialog(this, R.style.MyTheme);
		Progsdial.setMessage("Please Wait....");
		Progsdial.setIndeterminate(true);
		Progsdial.setCancelable(false);
		Progsdial.getWindow().setGravity(Gravity.DISPLAY_CLIP_VERTICAL);
		Progsdial.show();
	}

	private void Set_App_Logo_Title() {
		setTitle(ClubName); // Set Title
		// Set App LOGO
		if (AppLogo == null) {
			getActionBar().setIcon(R.drawable.ic_launcher);
		} else {
			Bitmap bitmap = BitmapFactory.decodeByteArray(AppLogo, 0, AppLogo.length);
			BitmapDrawable icon = new BitmapDrawable(getResources(), bitmap);
			getActionBar().setIcon(icon);
		}
	}


	//Display An Alert of Special Characters not allowed
	public void SpecialCharAlert() {
		ad.setTitle(Html.fromHtml("<font color='#FF7F27'>Error!</font>"));
		ad.setMessage("News Message cannot contains following characters ',\"");
		ad.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Msg = Msg.replace("'", "");
				Msg = Msg.replace("\"", "");
				// Message  = Message.substring( 0, Message.length() - 1 );
				txtDesc.setText(Msg);
				dialog.dismiss();
			}
		});
		ad.show();
	}


	private void AlertDisplay(String head, String body, final boolean flag) {
		ad.setTitle(Html.fromHtml("<font color='#E3256B'>" + head + "</font>"));
		ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>" + body + "</font>"));
		ad.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (flag) {
					Date CurDt = new Date();
					txtDate.setText(df.format(CurDt));
					txtTitle.setText("");
					txtDesc.setText("");
					dialog.dismiss();
					GoBack();
				} else {
					dialog.dismiss();
				}
			}
		});
		ad.show();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			GoBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void GoBack() {
		if (addchk.equals("2")) {
			Intent MainBtnIntent = new Intent(context, UlilitiesList.class);
			MainBtnIntent.putExtra("AppLogo", AppLogo);
			MainBtnIntent.putExtra("CondChk", "2");
			startActivity(MainBtnIntent);
			finish();
		} else {
			Intent MainBtnIntent = new Intent(context, MenuPage.class);
			MainBtnIntent.putExtra("AppLogo", AppLogo);
			startActivity(MainBtnIntent);
			finish();
		}
	}

}
