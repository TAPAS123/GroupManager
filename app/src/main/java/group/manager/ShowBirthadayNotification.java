package group.manager;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.ParseException;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowBirthadayNotification extends Activity {
	Button btnSendSMS, btnSubmitDate;
	String smsbody = "", StrmobNo = "", smsbody2 = "", smsbody3 = "", yyyy;
	Dialog dialog;
	ImageView IvPrevdate, IvNextdate;
	EditText etDay, etMonth, EtremarkDialog, EtremarkDialog2, EtremarkDialog3;
	ListView LV1;
	LinearLayout LLckhbox;
	ArrayList<Product> products;
	ListAdapter2 boxAdapter;
	static Calendar c;
	SQLiteDatabase db;
	String sqlSearch, Tab2Name, Tab4Name, StrTotal;
	Cursor cursorT;
	int countdata = 0, chk = 0;
	String dd = "00", mm = "00";
	CheckBox cHkboxall, Chkboxremrk, Chkboxremrk2, Chkboxremrk3;
	String strETDay, strETMonth;
	String STR_Remark = "", STR_Remark2 = "", STR_Remark3 = "";
	byte[] AppLogo, imgP;
	SharedPreferences sharedpreferences, shrd;
	Editor edits;
	String checkcomingfrom = "0";
	static String resultdate;
	Intent menuIntent;
	Context context = this;
	//used for register alarm manager
	PendingIntent pendingIntent;
	//used to store running alarmmanager instance
	AlarmManager alarmManager;
	//Callback function for Alarmmanager event
	BroadcastReceiver mReceiver;
	TextView txtnodisplay;
	String ClientID = "", ClubName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bday_anni_activity);

		TextView txtHead = (TextView) findViewById(R.id.txtHead);
		IvPrevdate = (ImageView) findViewById(R.id.imageprevdate);
		IvNextdate = (ImageView) findViewById(R.id.imageNextdate);
		btnSubmitDate = (Button) findViewById(R.id.btnDateSubmit);
		btnSendSMS = (Button) findViewById(R.id.buttonListalrt);
		etDay = (EditText) findViewById(R.id.editTextDay);
		etMonth = (EditText) findViewById(R.id.editTextMonth);
		LV1 = (ListView) findViewById(R.id.LV1);
		txtnodisplay = (TextView) findViewById(R.id.textViewNoDisplay);
		cHkboxall = (CheckBox) findViewById(R.id.cHkboxall);
		LLckhbox = (LinearLayout) findViewById(R.id.llckhbox);

		menuIntent = getIntent();
		ClientID = menuIntent.getStringExtra("ClientID");
		ClubName = menuIntent.getStringExtra("LogclubName");
		checkcomingfrom = menuIntent.getStringExtra("Menu_Noti");//come from notification or menu page

		Set_App_Logo_Title(); // Set App Logo and Title

		Tab2Name = "C_" + ClientID + "_2";

		Typeface face = Typeface.createFromAsset(getAssets(), "calibri.ttf");
		txtHead.setTypeface(face);
		etDay.setTypeface(face);
		etMonth.setTypeface(face);
		txtnodisplay.setTypeface(face);
		btnSubmitDate.setTypeface(face);

		c = Calendar.getInstance();// create object for calender
		dd = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		if ((dd == null) || (dd.length() == 0)) {
			dd = "00";
		}
		mm = String.valueOf(c.get(Calendar.MONTH) + 1);
		if ((mm == null) || (mm.length() == 0)) {
			mm = "00";
		}
		yyyy = String.valueOf(c.get(Calendar.YEAR));

		//txtHead.setText("Birthday\n(" + ClubName + ")");
		txtHead.setText("Birthday");
		etDay.setText(dd);
		etMonth.setText(mm);
		btnSendSMS.setText("Send SMS");
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		///////////select means default=0, select all=1, select met=2, select Tr=3//////////////////////////////
		FillList(false, dd, mm);

		IvPrevdate.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				cHkboxall.setChecked(false);
				strETDay = etDay.getText().toString().trim();
				strETMonth = etMonth.getText().toString().trim();
				if (strETDay.length() == 0) {
					LV1.setVisibility(View.GONE);
					etDay.setError("Please select day");
				} else if (strETMonth.length() == 0) {
					LV1.setVisibility(View.GONE);
					etMonth.setError("Please select Month");
				} else {
					etDay.setText(strETDay);
					etMonth.setText(strETMonth);
					String storeddate = strETDay + "-" + strETMonth + "-" + yyyy;
					showdateandcalllist(previousDateString(storeddate));
				}
			}
		});

		IvNextdate.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				cHkboxall.setChecked(false);
				strETDay = etDay.getText().toString().trim();
				strETMonth = etMonth.getText().toString().trim();
				if (strETDay.length() == 0) {
					LV1.setVisibility(View.GONE);
					etDay.setError("Please select day");
				} else if (strETMonth.length() == 0) {
					LV1.setVisibility(View.GONE);
					etMonth.setError("Please select Month");
				} else {
					etDay.setText(strETDay);
					etMonth.setText(strETMonth);
					if ((strETDay.equals("29")) && (strETMonth.equals("2")) && (!yyyy.equals("2016") || (!yyyy.equals("2020") || !yyyy.equals("2024")))) {
						strETDay = "28";
						etDay.setText(strETDay);
						Toast.makeText(context, "not leap year", Toast.LENGTH_LONG).show();
					}
					String storeddate = strETDay + "-" + strETMonth + "-" + yyyy;
					showdateandcalllist(NextDateString(storeddate));
				}
			}
		});


		btnSubmitDate.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				cHkboxall.setChecked(false);
				chk = 1;
				strETDay = etDay.getText().toString().trim();
				strETMonth = etMonth.getText().toString().trim();
				if (strETDay.length() == 0) {
					LV1.setVisibility(View.GONE);
					etDay.setError("Please select day");
				} else if (strETMonth.length() == 0) {
					LV1.setVisibility(View.GONE);
					etMonth.setError("Please select Month");
				} else {
					etDay.setText(strETDay);
					etMonth.setText(strETMonth);
					FillList(false, strETDay, strETMonth);
				}
			}
		});

		btnSendSMS.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				StrmobNo = "";
				for (Product p : boxAdapter.getBox()) {
					if (p.box) {
						//p.mob = p.mob.substring(0, p.mob.length()-1).trim();
						if (p.mob.length() != 0) {
							p.mob = p.mob.substring(p.mob.length() - 10);
							p.mob = "0" + p.mob;
							StrmobNo += p.mob + ",";
						}
					}
				}
				if (StrmobNo.length() != 0) {
					StrmobNo = StrmobNo.substring(0, StrmobNo.length() - 1).trim();
					ShowRemark();
				} else {
					Toast.makeText(getApplicationContext(), "No check box selected", Toast.LENGTH_LONG).show();
				}
			}
		});


		cHkboxall.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (cHkboxall.isChecked() == true) {
					if (chk == 0) {
						FillList(true, dd, mm);
					} else if (chk == 1) {
						FillList(true, strETDay, strETMonth);
					}
				} else {
					if (chk == 0) {
						FillList(false, dd, mm);
					} else if ((chk == 1) && (strETDay.length() != 0) && (strETMonth.length() != 0)) {
						FillList(false, strETDay, strETMonth);
					} else {
						LV1.setVisibility(View.GONE);
						Toast.makeText(getApplicationContext(), "Select date for tick all..", Toast.LENGTH_LONG).show();
					}
				}
			}
		});

	}


	private void ShowRemark() {
		dialog = new Dialog(context);
		dialog.setContentView(R.layout.bday_anni_activity);
		dialog.setTitle("Type Message here...");

		GetShPref();//Get Shared Preference
		ScrollView scremark = (ScrollView) dialog.findViewById(R.id.scrollViewRemark);
		scremark.setVisibility(View.VISIBLE);

		EtremarkDialog = (EditText) dialog.findViewById(R.id.editTextEmark1);
		EtremarkDialog.setGravity(Gravity.LEFT | Gravity.TOP);
		EtremarkDialog2 = (EditText) dialog.findViewById(R.id.editTextEmark2);
		EtremarkDialog2.setGravity(Gravity.LEFT | Gravity.TOP);
		EtremarkDialog3 = (EditText) dialog.findViewById(R.id.editTextEmark3);
		EtremarkDialog3.setGravity(Gravity.LEFT | Gravity.TOP);
		Chkboxremrk = (CheckBox) dialog.findViewById(R.id.checkBoxRemark1);
		Chkboxremrk2 = (CheckBox) dialog.findViewById(R.id.checkBoxRemark2);
		Chkboxremrk3 = (CheckBox) dialog.findViewById(R.id.checkBoxRemark3);
		Button btnBack = (Button) dialog.findViewById(R.id.buttonListalrt);
		Button btnSendSMS = (Button) dialog.findViewById(R.id.buttonSave);

		btnBack.setText("Cancel");
		btnSendSMS.setText("Send SMS");
		btnSendSMS.setVisibility(View.VISIBLE);

		EtremarkDialog.setText(smsbody);
		EtremarkDialog2.setText(smsbody2);
		EtremarkDialog3.setText(smsbody3);

		dialog.show();

		Chkboxremrk.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				STR_Remark = EtremarkDialog.getText().toString().trim();
				if ((Chkboxremrk.isChecked() == true) && (STR_Remark.length() == 0)) {
					EtremarkDialog.setError("Select first remark.");
					Chkboxremrk2.setChecked(false);
					Chkboxremrk3.setChecked(false);
					Chkboxremrk.setChecked(false);

				} else if ((Chkboxremrk.isChecked() == true) && (STR_Remark.length() != 0)) {
					Chkboxremrk2.setChecked(false);
					Chkboxremrk3.setChecked(false);
				}
			}
		});

		Chkboxremrk2.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				STR_Remark2 = EtremarkDialog2.getText().toString().trim();
				if ((Chkboxremrk2.isChecked() == true) && (STR_Remark2.length() == 0)) {
					EtremarkDialog2.setError("Select second remark.");
					Chkboxremrk.setChecked(false);
					Chkboxremrk3.setChecked(false);
					Chkboxremrk2.setChecked(false);

				} else if ((Chkboxremrk2.isChecked() == true) && (STR_Remark2.length() != 0)) {
					Chkboxremrk.setChecked(false);
					Chkboxremrk3.setChecked(false);
				}
			}
		});

		Chkboxremrk3.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				STR_Remark3 = EtremarkDialog3.getText().toString().trim();
				if ((Chkboxremrk3.isChecked() == true) && (STR_Remark3.length() == 0)) {
					EtremarkDialog3.setError("Select third remark.");
					Chkboxremrk.setChecked(false);
					Chkboxremrk2.setChecked(false);
					Chkboxremrk3.setChecked(false);

				} else if ((Chkboxremrk3.isChecked() == true) && (STR_Remark3.length() != 0)) {
					Chkboxremrk.setChecked(false);
					Chkboxremrk2.setChecked(false);
				}
			}
		});

		btnBack.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		btnSendSMS.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				STR_Remark = EtremarkDialog.getText().toString().trim();
				STR_Remark2 = EtremarkDialog2.getText().toString().trim();
				STR_Remark3 = EtremarkDialog3.getText().toString().trim();
				if (StrmobNo.length() == 0) {
					Toast.makeText(getApplicationContext(), "Select atleast one number!", 1).show();
				} else if (Chkboxremrk.isChecked() == true) {
					callOnSms(StrmobNo, STR_Remark);
					editorValue(STR_Remark, STR_Remark2, STR_Remark3);
					dialog.cancel();
				} else if (Chkboxremrk2.isChecked() == true) {
					callOnSms(StrmobNo, STR_Remark2);
					editorValue(STR_Remark, STR_Remark2, STR_Remark3);
					dialog.cancel();
				} else if (Chkboxremrk3.isChecked() == true) {
					callOnSms(StrmobNo, STR_Remark3);
					editorValue(STR_Remark, STR_Remark2, STR_Remark3);
					dialog.cancel();
				} else {
					editorValue(STR_Remark, STR_Remark2, STR_Remark3);
					Toast.makeText(getApplicationContext(), "No message is select..", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	private void GetShPref() {
		shrd = getSharedPreferences("SmsPref", Context.MODE_PRIVATE);
		if (shrd.contains("SmsSd")) {
			smsbody = shrd.getString("SmsSd", "");
		}
		if (shrd.contains("SmsSd2")) {
			smsbody2 = shrd.getString("SmsSd2", "");
		}
		if (shrd.contains("SmsSd3")) {
			smsbody3 = shrd.getString("SmsSd3", "");
		}
		System.out.println("@" + smsbody);
	}

	private void editorValue(String smsbody, String smsbody2, String smsbody3) {
		edits = shrd.edit();
		edits.putString("SmsSd", smsbody);
		edits.putString("SmsSd2", smsbody2);
		edits.putString("SmsSd3", smsbody3);
		edits.commit();
	}


	public static String previousDateString(String dateString) throws ParseException {
		// Create a date formatter using your format string
		SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yy");
		// Parse the given date string into a Date object.
		// Note: This can throw a ParseException.
		java.util.Date myDate = null;
		try {
			myDate = dateFormat.parse(dateString);
			c.setTime(myDate);
			c.add(Calendar.DAY_OF_YEAR, -1);
			// Use the date formatter to produce a formatted date string
			java.util.Date previousDate = (java.util.Date) c.getTime();
			resultdate = dateFormat.format(previousDate);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return resultdate;
	}

	public static String NextDateString(String dateString) throws ParseException {
		// Create a date formatter using your format string
		SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yy");
		// Parse the given date string into a Date object.
		// Note: This can throw a ParseException.
		java.util.Date myDate = null;
		try {
			myDate = dateFormat.parse(dateString);
			c.setTime(myDate);
			c.add(Calendar.DAY_OF_YEAR, +1);
			// Use the date formatter to produce a formatted date string
			java.util.Date previousDate = (java.util.Date) c.getTime();
			resultdate = dateFormat.format(previousDate);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return resultdate;
	}

	private void showdateandcalllist(String Strdates) {
		Strdates = Strdates.replace("-", "#") + " ";
		String[] temp = Strdates.split("#");
		dd = temp[0].toString().trim();
		mm = temp[1].toString().trim();
		etDay.setText(dd);
		etMonth.setText(mm);
		FillList(false, dd, mm);
	}

	private void FillList(boolean tick, String days, String months) {
		try {
			String Dobname = null, Dobnamob = null, StrCity = null, ddays = "00", mmonths = "00", PrefName = "";
			if (days.length() == 1) {
				ddays = "0" + days;
			} else {
				ddays = days;
			}
			if (months.length() == 1) {
				mmonths = "0" + months;
			} else {
				mmonths = months;
			}
			products = new ArrayList<Product>();
			db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);

			//Get Member Data
			String qry = "Select M_Name,M_Mob,M_City,M_Pic,(C4_BG || \"\" || ifnull(C4_DOB_Y,'')) from " + Tab2Name + " Where (M_DOB_D='" + days + "' or M_DOB_D='" + ddays + "') and (M_DOB_M='" + months + "' or M_DOB_M='" + mmonths + "') and M_Mob IS NOT NULL and Length(M_Mob)<>0 order by M_City,M_Name";
			Cursor cursorT = db.rawQuery(qry, null);
			Bitmap theImage = null;
			//System.out.println(qry);
			countdata = cursorT.getCount();
			if (countdata > 0) {
				if (cursorT.moveToFirst()) {
					do {
						Dobname = cursorT.getString(0);
						Dobnamob = cursorT.getString(1);
						StrCity = cursorT.getString(2);
						imgP = cursorT.getBlob(3);
						PrefName = cursorT.getString(4);
						if (PrefName == null)
							PrefName = "";

						if (PrefName.trim().length() > 0)
							Dobname = PrefName.trim() + " " + Dobname;

						if (StrCity == null)
							StrCity = "";
						if (Dobnamob == null)
							Dobnamob = "";
						else if (Dobnamob.length() != 10)
							Dobnamob = "";

						if (imgP == null) {
							theImage = null;
						} else if (imgP != null) {
							ByteArrayInputStream imageStream = new ByteArrayInputStream(imgP);
							theImage = BitmapFactory.decodeStream(imageStream);
						} else {
							theImage = null;
						}
						products.add(new Product(Dobname, StrCity, Dobnamob, tick, theImage, true));
					} while (cursorT.moveToNext());
				}
			}
			cursorT.close();

			//Get Spouse Data
			qry = "Select S_Name,S_Mob,M_City,S_Pic,C3_BG from " + Tab2Name + " Where (S_DOB_D='" + days + "' or S_DOB_D='" + ddays + "') and (S_DOB_M='" + months + "' or S_DOB_M='" + mmonths + "') and S_Mob IS NOT NULL and Length(S_Mob)<>0 order by M_City,S_Name";
			cursorT = db.rawQuery(qry, null);
			countdata = cursorT.getCount();
			if (countdata > 0) {
				theImage = null;
				if (cursorT.moveToFirst()) {
					do {
						Dobname = cursorT.getString(0);
						Dobnamob = cursorT.getString(1);
						StrCity = cursorT.getString(2);
						imgP = cursorT.getBlob(3);
						PrefName = cursorT.getString(4);
						if (PrefName == null)
							PrefName = "";

						if (PrefName.trim().length() > 0)
							Dobname = PrefName.trim() + " " + Dobname;

						if (StrCity == null)
							StrCity = "";
						if (Dobnamob == null)
							Dobnamob = "";
						else if (Dobnamob.length() != 10)
							Dobnamob = "";

						if (imgP == null) {
							theImage = null;
						} else if (imgP != null) {
							ByteArrayInputStream imageStream = new ByteArrayInputStream(imgP);
							theImage = BitmapFactory.decodeStream(imageStream);
						} else {
							theImage = null;
						}
						products.add(new Product(Dobname, StrCity, Dobnamob, tick, theImage, true));
					} while (cursorT.moveToNext());
				}
			}
			cursorT.close();
			db.close();//Close DB

			if (products.size() != 0) {
				boxAdapter = new ListAdapter2(this, products);
				LLckhbox.setVisibility(View.VISIBLE);
				LV1.setVisibility(View.VISIBLE);
				LV1.setAdapter(boxAdapter);
				txtnodisplay.setVisibility(View.GONE);
				btnSendSMS.setVisibility(View.VISIBLE);
			} else {
				LLckhbox.setVisibility(View.GONE);
				LV1.setVisibility(View.GONE);
				txtnodisplay.setVisibility(View.VISIBLE);
				btnSendSMS.setVisibility(View.GONE);
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}


	/// Updated On 14-03-2019
	public void callOnSms(String MobNos, String messageBody) {
		new CommonClass().SEND_SMS_WithBody(context, MobNos, messageBody);
	}

	private void Set_App_Logo_Title()
	{
		setTitle(ClubName); // Set Title

		if (checkcomingfrom.equalsIgnoreCase("menu")) {
			AppLogo = menuIntent.getByteArrayExtra("AppLogo");
		}
		// Set App LOGO
		if (AppLogo == null) {
			getActionBar().setIcon(R.drawable.ic_launcher);
		} else {
			Bitmap bitmap = BitmapFactory.decodeByteArray(AppLogo, 0, AppLogo.length);
			BitmapDrawable icon = new BitmapDrawable(getResources(), bitmap);
			getActionBar().setIcon(icon);
		}
	}


	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			GoBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void GoBack() {
		if (checkcomingfrom.equalsIgnoreCase("menu")) {
			Intent MainBtnIntent = new Intent(getBaseContext(), MenuPage.class);
			MainBtnIntent.putExtra("AppLogo", AppLogo);
			startActivity(MainBtnIntent);
		}
		finish();
	}

}
