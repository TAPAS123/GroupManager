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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import group.manager.AdapterClasses.Customfamily;

public class News_EventReadUnread extends Activity {
	Customfamily adptfamily;
	Context context = this;
	GridView gridView;
	ListView LV1;
	TextView txtHead, txtTitle, txtDate, txtEvent;
	String[] GrdArr = new String[]{"Read", "Unread", "Delivered"};
	Intent menuIntent;
	String Log, ClubName, logid, StrClubName, Table2Name, Table4Name, Str_user, PType, Number, WebResult = "", MID;
	byte[] AppLogo;
	SQLiteDatabase db;
	Cursor cursorT;
	RowEnvt item;
	List<RowEnvt> rowItems;
	AlertDialog.Builder alertDialogBuilder3;
	AlertDialog ad;
	ProgressDialog Progsdial;
	Thread networkThread;
	WebServiceCall webcall;
	Chkconnection chkconn;
	boolean InternetPresent;
	ArrayList<Product> products;
	String[] ArrReadMids, ArrUnReadMids, ArrDeliveredMids;
	String NEDesc = "", NEDate = "", NEVenue = "";
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
		PType = menuIntent.getStringExtra("PType");
		MID = menuIntent.getStringExtra("MID");
		NEVenue = menuIntent.getStringExtra("VAL2"); //Event/News (Venue)
		NEDate = menuIntent.getStringExtra("VAL3");  //Event/News (Date)
		NEDesc = menuIntent.getStringExtra("VAL4");  //Event/News (Desc)

		Table2Name = "C_" + Str_user + "_2";
		Table4Name = "C_" + Str_user + "_4";

		ComCllObj = new CommonClass();
		webcall = new WebServiceCall();//Call a Webservice
		chkconn = new Chkconnection();

		Set_App_Logo_Title();

		Typeface face=Typeface.createFromAsset(getAssets(), "calibri.ttf");
		txtHead.setTypeface(face);

		txtHead.setText(NEDesc.replace(":", ""));//Set Desc
		txtTitle.setText(NEVenue);//Set Venue
		txtDate.setText(NEDate);//Set Date

		WebCall_EventNews();//Call a WebService To Get Report of Event/News ReadUnread

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

				if (position == 0) {
					txtEvent.setText("Read");
					String ReadMids = ArrReadMids[0].trim() + "," + ArrReadMids[1].trim();
					FillListView(" Where M_ID in (" + ReadMids + ") order by M_Name", "Read");
				} else if (position == 1) {
					txtEvent.setText("Unread");
					String UnReadMids = ArrUnReadMids[0].trim() + "," + ArrUnReadMids[1].trim();
					FillListView(" Where M_ID in (" + UnReadMids + ") order by M_Name", "UnRead");
				} else if (position == 2) {
					txtEvent.setText("Delivered");
					String DeliveredMids = ArrDeliveredMids[0].trim() + "," + ArrDeliveredMids[1].trim();
					FillListView(" Where M_ID in (" + DeliveredMids + ") order by M_Name", "Delivered");
				} else {
					Toast.makeText(context, "Try other", Toast.LENGTH_SHORT).show();
				}
			}
		});


		//ListView Click event
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
		String Qry = "SELECT M_ID,M_Name,M_Mob,S_Name,S_Mob,(C4_BG || \"\" || ifnull(C4_DOB_Y,'')) as [M_Prefix],C3_BG as [S_Prefix]" +
				" from " + Table2Name + Condition;
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
				String MemMids = "", SpouseMids = "";

				if (Type.equals("Read")) {
					MemMids = ArrReadMids[0].trim();//Member Mids Read
					SpouseMids = ArrReadMids[1].trim();//Spouse Mids Read
				} else if (Type.equals("UnRead")) {
					MemMids = ArrUnReadMids[0].trim();//Member Mids UnRead
					SpouseMids = ArrUnReadMids[1].trim();//Spouse Mids UnRead
				} else if (Type.equals("Delivered")) {
					MemMids = ArrDeliveredMids[0].trim();//Member Mids Delivered
					SpouseMids = ArrDeliveredMids[1].trim();//Spouse Mids Delivered
				}

				String[] Arr_Mids_Member = MemMids.split(",");
				String[] Arr_Mids_Spouse = SpouseMids.split(",");

				isMember = ChkMemberSpouse(MID, Arr_Mids_Member);// check Mid in Member mids Array
				isSpouse = ChkMemberSpouse(MID, Arr_Mids_Spouse);// check Mid in Spouse mids Array

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


	//Call Webservice of Event/News ReadUnRead Report
	private void WebCall_EventNews() {
		InternetPresent = chkconn.isConnectingToInternet(context);
		if (InternetPresent == true) {
			progressdial();
			networkThread = new Thread() {
				public void run() {
					try {
						WebResult = webcall.Read_NewsEventsRPT(Str_user, PType, MID);
						runOnUiThread(new Runnable() {
							public void run() {
								if (WebResult.contains("#")) {
									String[] Arr1 = WebResult.split("@");

									// Total Read Mids
									ArrReadMids = Arr1[0].trim().split("#");
									String Read_MidsMembers = ArrReadMids[0].trim();// Member Mids Read News/Event
									String Read_MidsSpouse = ArrReadMids[1].trim();// Spouse Mids Read News/Event
									String TotalRead = ArrReadMids[2].trim();// Total (Member & Spouse) Mids Read News/Event

									// Total UnRead Mids
									ArrUnReadMids = Arr1[1].trim().split("#");
									String UnRead_MidsMembers = ArrUnReadMids[0].trim();// Member Mids UnRead News/Event
									String UnRead_MidsSpouse = ArrUnReadMids[1].trim();// Spouse Mids UnRead News/Event
									String TotalUnRead = ArrUnReadMids[2].trim();// Total (Member & Spouse) Mids UnRead News/Event

									//Total Delivered Mids
									ArrDeliveredMids = Arr1[2].trim().split("#");
									String Delivered_MidsMembers = ArrDeliveredMids[0].trim();// Member Mids Delivered News/Event
									String Delivered_MidsSpouse = ArrDeliveredMids[1].trim();// Spouse Mids Delivered News/Event
									String TotalDelivered = ArrDeliveredMids[2].trim();// Total (Member & Spouse) Mids Delivered News/Event

									String[] Arr2 = new String[3];
									Arr2[0] = TotalRead;
									Arr2[1] = TotalUnRead;
									Arr2[2] = TotalDelivered;

									gridView.setAdapter(new ImageAdapter(context, GrdArr, Arr2));
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
			MainBtnIntent.putExtra("Eventschk", "3");
		} else if (PType.equals("News")) {
			MainBtnIntent = new Intent(getBaseContext(), NewsMain.class);
			MainBtnIntent.putExtra("Count", 111);
			MainBtnIntent.putExtra("POstion", 0);
		}
		MainBtnIntent.putExtra("Clt_Log", Log);
		MainBtnIntent.putExtra("Clt_LogID", logid);
		MainBtnIntent.putExtra("Clt_ClubName", ClubName);
		MainBtnIntent.putExtra("UserClubName", Str_user);
		MainBtnIntent.putExtra("AppLogo", AppLogo);
		startActivity(MainBtnIntent);
		finish();
	}
}
