package group.manager;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class Resend_Notification extends Activity {

	TextView txtHead, txtDate, txtTitle;
	EditText ETDesc;
	CheckBox chkNoti, chkEmail;
	Spinner Sp_EmailFormat;
	Button btnSend;
	RadioGroup rdgrpSendTo;
	RadioButton rdbtn1, rdbtn2, rdbtn3;
	Intent menuIntent;
	String Qry, Log, ClubName, logid, StrClubName, Table2Name, Table4Name, ClientId, PType,
			Number, WebResult = "", Num1, MID, NEDesc = "", NEDate = "",EVenue = "", NETitle = "";
	byte[] AppLogo;
	ProgressDialog Progsdial;
	Thread networkThread;
	WebServiceCall webcall;
	boolean ShowEmail;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resend_notification);

		txtHead = (TextView) findViewById(R.id.txtHead);
		txtDate = (TextView) findViewById(R.id.tvDate);
		txtTitle = (TextView) findViewById(R.id.tvTitle);
		ETDesc = (EditText) findViewById(R.id.EDDesc);
		ETDesc.setKeyListener(null);///editable false
		btnSend = (Button) findViewById(R.id.btnSend);
		chkNoti = (CheckBox) findViewById(R.id.chkNoti);
		chkEmail = (CheckBox) findViewById(R.id.chkEmail);
		Sp_EmailFormat = (Spinner) findViewById(R.id.Sp_EmailFormat);
		rdgrpSendTo = (RadioGroup) findViewById(R.id.rdgrpSendTo);
		rdbtn1 = (RadioButton) findViewById(R.id.rdbtn1);
		rdbtn2 = (RadioButton) findViewById(R.id.rdbtn2);
		rdbtn3 = (RadioButton) findViewById(R.id.rdbtn3);

		menuIntent = getIntent();
		Log = menuIntent.getStringExtra("Clt_Log");
		logid = menuIntent.getStringExtra("Clt_LogID");
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		ClientId = menuIntent.getStringExtra("UserClubName");
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");
		PType = menuIntent.getStringExtra("PType");
		MID = menuIntent.getStringExtra("MID");
		EVenue = menuIntent.getStringExtra("VAL2"); //Event (Venue)
		NEDate = menuIntent.getStringExtra("VAL3");  //Event/News (Date)
		NETitle = menuIntent.getStringExtra("VAL4");  //Event/News (Title)

		Table2Name = "C_" + ClientId + "_2";
		Table4Name = "C_" + ClientId + "_4";

		webcall = new WebServiceCall();//Webservice object

		Set_App_Logo_Title();

		Typeface face = Typeface.createFromAsset(getAssets(), "calibri.ttf");
		txtHead.setTypeface(face);

		String[] Arr = {"format-1", "format-2", "format-3", "format-4"};
		ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Arr);
		adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Sp_EmailFormat.setAdapter(adp1);
		Sp_EmailFormat.setVisibility(View.GONE);//hide Email format spinner

		if (PType.equals("News")) {
			txtHead.setText("Resend News Notification");
			Qry = "Select Add1,Num1 from " + Table4Name + " Where Rtype='News' AND  M_Id= " + MID;
			rdbtn2.setText("Send only to Not Read");
			rdbtn3.setVisibility(View.GONE);
		} else {
			txtHead.setText("Resend Event Notification");
			Qry = "Select Text2,Num1 from " + Table4Name + " Where Rtype='Event' AND M_Id= " + MID;
		}

		SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);

		Cursor cursorT = db.rawQuery(Qry, null);
		if (cursorT.moveToFirst()) {
			NEDesc = ChkVal(cursorT.getString(0));
			Num1 = ChkVal(cursorT.getString(1));
		}
		cursorT.close();

		////// Show Email CheckBox or Not
		Qry = "Select M_Id from " + Table4Name + " Where Rtype='ShowEmail'";
		cursorT = db.rawQuery(Qry, null);
		if (cursorT.moveToFirst()) {
			ShowEmail = true;
		}
		cursorT.close();

		db.close();// Close Connection

		if (!ShowEmail)
			chkEmail.setVisibility(View.INVISIBLE);

		txtDate.setText(NEDate);//Set Date
		txtTitle.setText(NETitle.replace(":", ""));//Set Title
		ETDesc.setText(NEDesc);//Set Desc


		//chkEmail Click event
		chkEmail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (chkEmail.isChecked())
					Sp_EmailFormat.setVisibility(View.VISIBLE);//Visible Email format spinner
				else
					Sp_EmailFormat.setVisibility(View.GONE);//hide Email format spinner
			}
		});


		//btnSend Click event
		btnSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				String TempNoti = "0", TempEmail = "0", TempEmailFormat = "", TempSendTo = "";

				if (chkNoti.isChecked())
					TempNoti = "1";
				if (chkEmail.isChecked()) {
					TempEmail = "1";
					TempEmailFormat = Sp_EmailFormat.getSelectedItem().toString();
					TempEmailFormat = TempEmailFormat.replace("format-", "").trim();
				}

				if (TempNoti.equals("0") && TempEmail.equals("0")) {
					DispAlert("Mandatory field", "Please select Notification or Email or both", 0);
				} else {
					int chkId = rdgrpSendTo.getCheckedRadioButtonId();//SendTo
					// find the radiobutton(1 or 2 0r 3) by returned id
					RadioButton rdbtn = (RadioButton) findViewById(chkId);
					String txt1 = rdbtn.getText().toString().trim();
					if (txt1.contains("Send to All"))
						TempSendTo = "1";
					else if (txt1.contains("Send only to Not"))
						TempSendTo = "2";
					else if (txt1.contains("Send only to who"))
						TempSendTo = "3";

					ResendNotiEmailNE(TempNoti, TempEmail, TempEmailFormat, TempSendTo);
				}
			}
		});
	}


	private void Set_App_Logo_Title() {
		setTitle(ClubName); // Set Title
		if (AppLogo == null) {    // Set App LOGO
			getActionBar().setIcon(R.drawable.ic_launcher);
		} else {
			Bitmap bitmap = BitmapFactory.decodeByteArray(AppLogo, 0, AppLogo.length);
			BitmapDrawable icon = new BitmapDrawable(getResources(), bitmap);
			getActionBar().setIcon(icon);
		}
	}


	// Resend Notification News/Event
	private void ResendNotiEmailNE(final String TempNoti, final String TempEmail, final String TempFormat, final String SendTo) {
		progressdial();
		networkThread = new Thread() {
			public void run() {
				try {
					WebResult = webcall.ResendNotificationNE(ClientId, ClubName, PType, Num1, TempNoti, TempEmail, TempFormat, SendTo);
					runOnUiThread(new Runnable() {
						public void run() {
							if (WebResult.contains("Done")) {
								DispAlert("Result !", "Successfully Resend " + PType + " !", 1);
							} else {
								DispAlert("Error !", "Technical Problem, Try Later !", 0);
							}
						}
					});
					Progsdial.dismiss();
					return;
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		};
		networkThread.start();
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


	@SuppressWarnings("deprecation")
	private void DispAlert(String Title, String Msg, final int chk) {
		AlertDialog ad = new AlertDialog.Builder(this).create();
		ad.setTitle(Html.fromHtml("<font color='#1C1CF0'>" + Title + "</font>"));
		ad.setMessage(Msg);
		ad.setCancelable(false);
		ad.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (chk == 1)
					GoBack();
				else
					dialog.dismiss();
			}
		});
		ad.show();
	}


	//call function for initialise blank if null is there
	private String ChkVal(String DVal) {
		if ((DVal == null) || (DVal.equalsIgnoreCase("null"))) {
			DVal = "";
		}
		return DVal.trim();
	}


	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			GoBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void GoBack() {
		Intent MainBtnIntent = null;
		if (PType.equals("Event")) {
			MainBtnIntent = new Intent(getBaseContext(), Events.class);
			MainBtnIntent.putExtra("Eventschk", "4");
		} else if (PType.equals("News")) {
			MainBtnIntent = new Intent(getBaseContext(), NewsMain.class);
			MainBtnIntent.putExtra("Count", 222);
			MainBtnIntent.putExtra("POstion", 0);
		}
		MainBtnIntent.putExtra("Clt_Log", Log);
		MainBtnIntent.putExtra("Clt_LogID", logid);
		MainBtnIntent.putExtra("Clt_ClubName", ClubName);
		MainBtnIntent.putExtra("UserClubName", ClientId);
		MainBtnIntent.putExtra("AppLogo", AppLogo);
		startActivity(MainBtnIntent);
		finish();
	}

}
