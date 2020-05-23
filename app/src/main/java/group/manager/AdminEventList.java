package group.manager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
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
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import group.manager.AdapterClasses.Customfamily;

public class AdminEventList extends Activity {
	Customfamily adptfamily;
	Context context = this;
	GridView gridView;
	ListView LV1;
	TextView txtHead, txtTitle, txtDate, txtEvent;
	final String[] GrdArr = new String[]{"Attending", "Not Attending", "Not Answered"};
	Intent menuIntent;
	String sqlSearch = "", Log, ClubName, logid, Str_user, StrClubName, Table2Name, Table4Name, Str_Mid,
			StrNUm1, val2, val3, val4, desc, Number, StrNum3, StrSql = "", TableNameEvent, StrNum, Strnumval = "", Wepdata;
	byte[] AppLogo;
	SQLiteDatabase db;
	Cursor cursorT;
	String[] temp, sp;
	RowEnvt item;
	List<RowEnvt> rowItems;
	AlertDialog.Builder alertDialogBuilder3;
	AlertDialog ad;
	int attcount, NTattcount, templen = 0;
	ProgressDialog Progsdial;
	Thread networkThread;
	WebServiceCall webcall;
	Chkconnection chkconn;
	boolean InternetPresent;
	ArrayList<Product> products;
	String[] ArrEventAttendMids, ArrEventNotAttendMids, ArrEventUnAnswerMids;
	CommonClass ComCllObj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admineventlist);

		txtHead = (TextView) findViewById(R.id.txtHead);
		gridView = (GridView) findViewById(R.id.gridView1);
		txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtDate = (TextView) findViewById(R.id.txtDate);
		txtEvent = (TextView) findViewById(R.id.txtEvent);
		LV1 = (ListView) findViewById(R.id.LV1);

		menuIntent = getIntent();
		Log = menuIntent.getStringExtra("Clt_Log");
		logid = menuIntent.getStringExtra("Clt_LogID");
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		Str_user = menuIntent.getStringExtra("UserClubName");
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");

		Str_Mid = menuIntent.getStringExtra("Pwd");
		val2 = menuIntent.getStringExtra("VAL2");
		val3 = menuIntent.getStringExtra("VAL3");
		val4 = menuIntent.getStringExtra("VAL4");
		StrNum3 = menuIntent.getStringExtra("Mid");

		Table2Name = "C_" + Str_user + "_2";
		Table4Name = "C_" + Str_user + "_4";
		TableNameEvent = "C_" + Str_user + "_Event";

		ComCllObj = new CommonClass();
		webcall = new WebServiceCall();//Call a Webservice
		chkconn = new Chkconnection();

		Set_App_Logo_Title();

		val4 = val4.replace(":", "");
		String[] temp1 = Str_Mid.split("#");
		desc = temp1[0];
		StrNUm1 = temp1[1];

		Typeface face=Typeface.createFromAsset(getAssets(), "calibri.ttf");
		txtHead.setTypeface(face);

		txtHead.setText(val4);
		txtTitle.setText(val2);//Set Venue
		txtDate.setText(val3);//Set EventDatetime

		/////////call web service./////////////////////
		webInternet();
		///////////////////////////////////

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

				if (position == 0) {
					txtEvent.setText("Attending");
					String AttendMids = ArrEventAttendMids[0].trim() + "," + ArrEventAttendMids[1].trim();
					FillListView(" Where M_ID in (" + AttendMids + ") order by M_Name", "Attend");
				} else if (position == 1) {
					txtEvent.setText("Not Attending");
					String NotAttendMids = ArrEventNotAttendMids[0].trim() + "," + ArrEventNotAttendMids[1].trim();
					FillListView(" Where M_ID in (" + NotAttendMids + ") order by M_Name", "NotAttend");
				} else if (position == 2) {
					txtEvent.setText("Not Answered");
					String UnAnswerMids = ArrEventUnAnswerMids[0].trim() + "," + ArrEventUnAnswerMids[1].trim();
					FillListView(" Where M_ID in (" + UnAnswerMids + ") order by M_Name", "UnAnswer");
				} else {
					Toast.makeText(context, "Try other", Toast.LENGTH_SHORT).show();
				}
			}
		});

		LV1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Number = rowItems.get(position).getEvtdate();
				alertDialogBuilder3 = new AlertDialog.Builder(context);
				if ((Number == null) || (Number.length() == 0) || (Number.length() < 10)) {
					alertDialogBuilder3
							.setMessage("Wrong Mobile Number!!")
							.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.dismiss();
								}
							});

				} else {
					Number = Number.substring(Number.length() - 10, Number.length());
					//System.out.println("cut::  "+Number);
					Number = "0" + Number;

					alertDialogBuilder3
							.setPositiveButton("CALL", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									ComCllObj.callOnPhone(context, Number);
								}
							})

							.setNegativeButton("SMS", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									ComCllObj.callOnSms(context, Number);
								}
							});

				}
				ad = alertDialogBuilder3.create();
				ad.show();
			}
		});
	}


	//Fill Listview
	private void FillListView(String Condition, String Type) {
		products = new ArrayList<Product>();
		rowItems = new ArrayList<RowEnvt>();

		db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		String Qry = "SELECT M_ID,M_Name,M_Mob,S_Name,S_Mob,(C4_BG || \"\" || ifnull(C4_DOB_Y,'')) as [M_Prefix],C3_BG as [S_Prefix] from " + Table2Name + Condition;
		cursorT = db.rawQuery(Qry, null);

		int RCount = cursorT.getCount();

		if (cursorT.moveToFirst()) {
			do {
				int MID = cursorT.getInt(0);
				String MName = ChkVal(cursorT.getString(1));//Member Name
				String MMob = ChkVal(cursorT.getString(2)); //Member Mobile
				String SName = ChkVal(cursorT.getString(3)); //Spouse Name
				String SMob = ChkVal(cursorT.getString(4)); //Spouse Mobile
				String M_Prefix = ChkVal(cursorT.getString(5)); //Member Prefix
				String S_Prefix = ChkVal(cursorT.getString(6)); //Spouse Prefix

				if (M_Prefix.trim().length() > 0)
					MName = M_Prefix.trim() + " " + MName;

				if (S_Prefix.trim().length() > 0)
					SName = S_Prefix.trim() + " " + SName;

				boolean isMember = false, isSpouse = false;

				if (Type.equals("Attend")) {
					String EA_Mids_M = ArrEventAttendMids[0].trim();//Member Mids Event Attended
					String EA_Mids_S = ArrEventAttendMids[1].trim();//Spouse Mids Event Attended

					String[] Arr_EA_Mids_M = EA_Mids_M.split(",");
					String[] Arr_EA_Mids_S = EA_Mids_S.split(",");

					isMember = ChkMemberSpouse(MID, Arr_EA_Mids_M);// check Mid in Member mids Array
					isSpouse = ChkMemberSpouse(MID, Arr_EA_Mids_S);// check Mid in Spouse mids Array
				} else if (Type.equals("NotAttend")) {
					String NEA_Mids_M = ArrEventNotAttendMids[0].trim();//Member Mids Event Not Attended
					String NEA_Mids_S = ArrEventNotAttendMids[1].trim();//Spouse Mids Event Not Attended

					String[] Arr_ENA_Mids_M = NEA_Mids_M.split(",");
					String[] Arr_ENA_Mids_S = NEA_Mids_S.split(",");

					isMember = ChkMemberSpouse(MID, Arr_ENA_Mids_M);// check Mid in Member mids Array
					isSpouse = ChkMemberSpouse(MID, Arr_ENA_Mids_S);// check Mid in Spouse mids Array
				} else if (Type.equals("UnAnswer")) {
					String UA_Mids_M = ArrEventUnAnswerMids[0].trim();//Member Mids Event UnAnswered
					String UA_Mids_S = ArrEventUnAnswerMids[1].trim();//Spouse Mids Event UnAnswered

					String[] Arr_UA_Mids_M = UA_Mids_M.split(",");
					String[] Arr_UA_Mids_S = UA_Mids_S.split(",");

					isMember = ChkMemberSpouse(MID, Arr_UA_Mids_M);// check Mid in Member mids Array
					isSpouse = ChkMemberSpouse(MID, Arr_UA_Mids_S);// check Mid in Spouse mids Array
				}

				// Add Member Details In List
				if (isMember) {
					item = new RowEnvt(MName, "0", MMob, "");
					rowItems.add(item);
				}

				//Add Spouse Details In List
				if (isSpouse) {
					item = new RowEnvt(SName, "0", SMob, "");
					rowItems.add(item);
				}

			} while (cursorT.moveToNext());
		}
		;
		cursorT.close();
		db.close();
		if (RCount == 0) {
			txtEvent.setText("No Record Found !");
		}
		adptfamily = new Customfamily(context, R.layout.familylist, rowItems);
		LV1.setAdapter(adptfamily);
	}


	/// Check Mid in Member/Spouse Mids Array
	private boolean ChkMemberSpouse(int MID, String[] Arr) {
		boolean HasMid = false;
		if (Arr != null) {
			int ChkMid = 0;
			for (int i = 0; i < Arr.length; i++) {
				ChkMid = Integer.parseInt(Arr[i].trim());
				if (ChkMid == MID) {
					HasMid = true;
					break;
				}
			}
		}
		return HasMid;
	}


	protected void progressdial() {
		Progsdial = new ProgressDialog(this, R.style.MyTheme);
		Progsdial.setMessage("Please Wait....");
		Progsdial.setIndeterminate(true);
		Progsdial.setCancelable(false);
		Progsdial.getWindow().setGravity(Gravity.DISPLAY_CLIP_VERTICAL);
		Progsdial.show();
	}

	private void webInternet() {
		InternetPresent = chkconn.isConnectingToInternet(context);
		if (InternetPresent == true) {
			progressdial();
			networkThread = new Thread() {
				public void run() {
					try {
						Wepdata = webcall.clubEvents(Str_user, StrNUm1);
						runOnUiThread(new Runnable() {
							public void run() {
								if (Wepdata.contains("#")) {
									String[] Arr = Wepdata.split("#");

									// Total Event Attended Mids
									ArrEventAttendMids = Arr[0].trim().replace("^", "#").split("#");
									String EAttend_MidsM = ArrEventAttendMids[0].trim();// Member Mids Event Attended
									String EAttend_MidsS = ArrEventAttendMids[1].trim();// Spouse Mids Event Attended
									String EAttend_Total = ArrEventAttendMids[2].trim();// Total (Member & Spouse) Mids Event Attended

									// Total Event Not Attended Mids
									ArrEventNotAttendMids = Arr[1].trim().replace("^", "#").split("#");
									String ENotAttend_MidsM = ArrEventNotAttendMids[0].trim();// Member Mids Event Not Attended
									String ENotAttend_MidsS = ArrEventNotAttendMids[1].trim();// Spouse Mids Event Not Attended
									String ENotAttend_Total = ArrEventNotAttendMids[2].trim();// Total (Member & Spouse) Mids Event Not Attended

									// Total UnAnswered Mids
									ArrEventUnAnswerMids = Arr[2].trim().replace("^", "#").split("#");
									String EUnAns_MidsM = ArrEventUnAnswerMids[0].trim();// Member Mids Event UnAnswered
									String EUnAns_MidsS = ArrEventUnAnswerMids[1].trim();// Spouse Mids Event UnAnswered
									String EUnAns_Total = ArrEventUnAnswerMids[2].trim();// Total (Member & Spouse) Mids Event UnAnswered

									String[] Arr1 = new String[3];
									Arr1[0] = EAttend_Total;
									Arr1[1] = ENotAttend_Total;
									Arr1[2] = EUnAns_Total;

									gridView.setAdapter(new ImageAdapter(context, GrdArr, Arr1));

								} else {
									DisplayMsg("No Record found !");
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
			DisplayMsg("No Internet connection found,Try Later!");
		}
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


	//Display Msg
	private void DisplayMsg(String Msg) {
		alertDialogBuilder3 = new AlertDialog.Builder(context);
		alertDialogBuilder3
				.setMessage(Msg)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
						GoBack();
					}
				});
		ad = alertDialogBuilder3.create();
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
		Intent MainBtnIntent = new Intent(getBaseContext(), Events.class);
		MainBtnIntent.putExtra("Eventschk", "2");
		MainBtnIntent.putExtra("Clt_Log", Log);
		MainBtnIntent.putExtra("Clt_LogID", logid);
		MainBtnIntent.putExtra("Clt_ClubName", ClubName);
		MainBtnIntent.putExtra("UserClubName", Str_user);
		MainBtnIntent.putExtra("AppLogo", AppLogo);
		startActivity(MainBtnIntent);
		finish();
	}
}
