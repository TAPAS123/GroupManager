package group.manager;

import java.util.ArrayList;

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
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Send_SMS extends Activity {

	String ClientId, ClubName, TabGroup, WebResult = "", Msg = "", StrGroupIds = "";
	private Context context = this;
	byte[] AppLogo;
	ProgressDialog Progsdial;
	Thread networkThread;
	WebServiceCall webcall;
	EditText txtMsg;
	TextView TVCount, TVMsgCount, TVGroup;
	CheckBox chkWithOutApp;
	ArrayList<Product> products;
	SQLiteDatabase db;
	Adapter_News_Group AdpGroup = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_sms);

		TextView TvHead = (TextView) findViewById(R.id.txtHead);
		txtMsg = (EditText) findViewById(R.id.ETMsg);
		TVCount = (TextView) findViewById(R.id.tVcount);
		TVMsgCount = (TextView) findViewById(R.id.tvMsgCount);
		TVGroup = (TextView) findViewById(R.id.TVGroup);
		chkWithOutApp = (CheckBox) findViewById(R.id.chkWithOutApp);
		Button btnSendSMS = (Button) findViewById(R.id.btnSend);

		Intent menuIntent = getIntent();
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		ClientId = menuIntent.getStringExtra("UserClubName");
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");

		TabGroup = "C_" + ClientId + "_Group";// Table Group
		webcall = new WebServiceCall();//Webservice object

		Set_App_Logo_Title(); // Set App Logo and Title

		Typeface face = Typeface.createFromAsset(getAssets(), "calibri.ttf");
		TvHead.setTypeface(face);

		WebCall_GetAllGroup();//Get All Group from Webservice


		txtMsg.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				Msg = txtMsg.getText().toString().trim();
				if ((Msg.contains("#")) || (Msg.contains("%")) || (Msg.contains("&")) || (Msg.contains("'")) || (Msg.contains("\""))) {
					DispAlert("Invalid Characters", "Message cannot contain following characters #,%,',\",&", 2);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {

				int MsgCount = 0, Max_Count = 158;
				int MLength = s.length();

				if (MLength > 0 && MLength <= 158) {
					MsgCount = 1;
					Max_Count = 158;
				} else if (MLength > 158) {
					MsgCount = 2;
					MLength = MLength - 158;
					Max_Count = 142;
				}

				int count = Max_Count - MLength;
				TVCount.setText(count + "");
				TVMsgCount.setText(MsgCount + "");
				TVCount.setTextColor(Color.BLACK);
				if (count < 10)
					TVCount.setTextColor(Color.BLUE);
				if (count == 0)
					TVCount.setTextColor(Color.RED);
				if (count < 0) {
					TVCount.setTextColor(Color.BLACK);
					TVCount.setText("158");
				}
			}
		});

		//Select Group
		TVGroup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ShowSelectGroupDialog();
			}
		});

		//Click Event button Send SMS
		btnSendSMS.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String TMsg = txtMsg.getText().toString().trim();
				if (TMsg.length() == 0) {
					DispAlert("Mandatory field", "Please input Message", 0);
				} else if (StrGroupIds.length() == 0) {
					DispAlert("Mandatory field", "Please Select Group", 0);
				} else {
					String WithOutApp = "";
					boolean Chk1 = chkWithOutApp.isChecked();
					if (Chk1) {
						WithOutApp = "N";
					}

					Send_Sms(TMsg, WithOutApp);/// SEND SMS
				}
			}
		});
	}


	// Send Sms from webservice
	private void Send_Sms(final String StrMsg, final String WithOutApp) {
		progressdial();
		networkThread = new Thread() {
			public void run() {
				try {
					WebResult = webcall.SEND_SMS(ClientId, StrMsg, StrGroupIds, WithOutApp);
					runOnUiThread(new Runnable() {
						public void run() {
							if (WebResult.contains("Send")) {
								DispAlert("Result !", "Message Sent Successfully !", 1);
							} else if (WebResult.contains("Error") || WebResult.contains("Try Later")) {
								DispAlert("Error !", "Technical Problem, Please try later !", 0);
							}
						}
					});
					Progsdial.dismiss();
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		};
		networkThread.start();
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

					String WResult = webcall.GetAllGroup(ClientId);
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
		Typeface face = Typeface.createFromAsset(getAssets(), "calibri.ttf");
		TvHead.setTypeface(face);

		if (products.size() != 0) {
			AdpGroup = new Adapter_News_Group(this, products);
			Lv1.setVisibility(View.VISIBLE);
			Lv1.setAdapter(AdpGroup);
		}

		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				//// Get Selected Groups Names and Ids
				String StrGroupNames = "";
				StrGroupIds = "";
				if (AdpGroup != null) {
					for (Product p : AdpGroup.getBox()) {
						if (p.box) {
							StrGroupNames += p.name + ",";
							StrGroupIds += p.GroupId + ",";
						}
					}
				}
				if (StrGroupNames.contains(","))
					StrGroupNames = StrGroupNames.substring(0, StrGroupNames.length() - 1);

				if (StrGroupIds.contains(","))
					StrGroupIds = StrGroupIds.substring(0, StrGroupIds.length() - 1);
				/////////////////////////

				TVGroup.setText(StrGroupNames);
				dialog.dismiss();
			}
		});
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


	protected void progressdial() {
		Progsdial = new ProgressDialog(this, R.style.MyTheme);
		Progsdial.setMessage("Please Wait....");
		Progsdial.setIndeterminate(true);
		Progsdial.setCancelable(false);
		Progsdial.getWindow().setGravity(Gravity.DISPLAY_CLIP_VERTICAL);
		Progsdial.show();
	}


	@SuppressWarnings("deprecation")
	private void DispAlert(String Title, String AlertMsg, final int type) {
		AlertDialog ad = new AlertDialog.Builder(this).create();
		ad.setTitle(Html.fromHtml("<font color='#E3256B'>" + Title + "</font>"));
		ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>" + AlertMsg + "</font>"));
		ad.setCancelable(false);
		ad.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (type == 0)
					dialog.dismiss();
				else if (type == 1)
					GoBack();
				else if (type == 2) {
					Msg = Msg.replace("#", "");
					Msg = Msg.replace("'", "");
					Msg = Msg.replace("%", "");
					Msg = Msg.replace("&", "");
					Msg = Msg.replace("\"", "");
					txtMsg.setText(Msg);
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

	private void GoBack() {
		Intent MainBtnIntent = new Intent(context, UlilitiesList.class);
		MainBtnIntent.putExtra("AppLogo", AppLogo);
		MainBtnIntent.putExtra("CondChk", "2");
		startActivity(MainBtnIntent);
		finish();
	}
}
