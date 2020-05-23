package group.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ParseException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;

import group.manager.AdapterClasses.CustomEvt;

public class EventCalendar extends Activity {
	List<RowEnvt> rowItems;
	CustomEvt adapter;
	ListView lvEvent;
	SQLiteDatabase db;
	Cursor cursorT;
	String sqlSearch, Table2Name, Table4Name, Log, ClubName, logid, Str_user, Str_chk = "", UserType = "";
	Intent menuIntent;
	String[] temp, arrgp = {"Forth Coming Event", "Past Event"};
	RowEnvt item;
	Context context = this;
	TextView txtHeadent;
	byte[] AppLogo;
	ExpandableListView ExpandList;
	ArrayList<Category> categories;
	EventSettingsListAdapter ExpAdapter;
	CheckBox chkShowAll;
	boolean ShowAll = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventcalendar);

		chkShowAll = (CheckBox) findViewById(R.id.chkShowAll);
		txtHeadent = (TextView) findViewById(R.id.txtevnthead);
		lvEvent = (ListView) findViewById(R.id.listViewEvnt);
		ExpandList = (ExpandableListView) findViewById(R.id.expandableListViewat);

		menuIntent = getIntent();
		Log = menuIntent.getStringExtra("Clt_Log");
		logid = menuIntent.getStringExtra("Clt_LogID");
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		Str_user = menuIntent.getStringExtra("UserClubName");
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");
		Str_chk = menuIntent.getStringExtra("Eventschk");

		txtHeadent.setText("Events");

		Table2Name = "C_" + Str_user + "_2";
		Table4Name = "C_" + Str_user + "_4";
		Set_App_Logo_Title(); // Set App Logo and Title

		Get_SharedPref_Values();// Get Stored Shared Pref Values of Login

		////Show All Event CheckBox Displayed Or not
		UnCommonProperties Obj1 = new UnCommonProperties();
		if (Str_chk.equals("1") && Obj1.ShowAllEventOption)
			chkShowAll.setVisibility(View.VISIBLE);
		//////////////////////////////////

		SetExpandableListData();///Set Expandable List Data

		//chkShowAll Click event
		chkShowAll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (chkShowAll.isChecked())
					ShowAll = true;
				else
					ShowAll = false;

				SetExpandableListData();
			}
		});


		ExpandList.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				//System.out.println("2: "+groupPosition+"  @@:  "+childPosition);

				String Name = categories.get(groupPosition).children.get(childPosition).name;
				String DateTm = categories.get(groupPosition).children.get(childPosition).val2;
				String EVenue = categories.get(groupPosition).children.get(childPosition).val3;
				String EDesc_Num1_Text8 = categories.get(groupPosition).children.get(childPosition).val4;
				String Num3 = categories.get(groupPosition).children.get(childPosition).val5;
				String EventMID = categories.get(groupPosition).children.get(childPosition).EventMID;

				if (Str_chk.equals("1")) {
					menuIntent = new Intent(context, EventDetailValue.class);
					menuIntent.putExtra("EventMID", EventMID);//Unique Event MID Update 14-05-2016
					menuIntent.putExtra("Eventschk", Str_chk);
				} else if ((Str_chk.equals("2"))) {
					menuIntent = new Intent(context, AdminEventList.class);
					menuIntent.putExtra("Pwd", EDesc_Num1_Text8);
					menuIntent.putExtra("VAL2", EVenue);
					menuIntent.putExtra("VAL3", DateTm);
					menuIntent.putExtra("VAL4", Name);
					menuIntent.putExtra("Mid", Num3);
				} else if ((Str_chk.equals("3")) || (Str_chk.equals("4"))) {
					if (Str_chk.equals("3"))
						menuIntent = new Intent(context, News_EventReadUnread.class);
					else
						menuIntent = new Intent(context, Resend_Notification.class);
					menuIntent.putExtra("MID", EventMID);//Unique Event MID Update 14-05-2016
					menuIntent.putExtra("PType", "Event");
					menuIntent.putExtra("Pwd", EDesc_Num1_Text8);
					menuIntent.putExtra("VAL2", EVenue);
					menuIntent.putExtra("VAL3", DateTm);
					menuIntent.putExtra("VAL4", Name);
					menuIntent.putExtra("Mid", Num3);
				}
				menuIntent.putExtra("Clt_Log", Log);
				menuIntent.putExtra("Clt_LogID", logid);
				menuIntent.putExtra("Clt_ClubName", ClubName);
				menuIntent.putExtra("UserClubName", Str_user);
				menuIntent.putExtra("AppLogo", AppLogo);
				startActivity(menuIntent);
				finish();
				return true;
			}
		});
	}


	private void Get_SharedPref_Values() {
		SharedPreferences ShPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

		if (ShPref.contains("UserType")) {
			UserType = ShPref.getString("UserType", "");
		}
	}

	//Set App Logo and title
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


	public static String Convert24to12(String time, String inpdat) throws java.text.ParseException {
		String convertedTime = "", finalDate;
		java.util.Date date, myDate;
		SimpleDateFormat displayFormat, parseFormat, dateFormat, dateCovrt;
		try {
			displayFormat = new SimpleDateFormat("hh:mm a");
			parseFormat = new SimpleDateFormat("HH:mm:ss");
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateCovrt = new SimpleDateFormat("dd-MM-yyyy");
			date = parseFormat.parse(time);
			myDate = dateFormat.parse(inpdat);
			convertedTime = displayFormat.format(date);
			finalDate = dateCovrt.format(myDate);
			//System.out.println("convertedTime : "+convertedTime);
			convertedTime = finalDate + " " + convertedTime;
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return convertedTime;
		//Output will be 10:23 PM
	}


	////Fill Expandalble List with Child List
	private void SetExpandableListData() {
		categories = new ArrayList<Category>();
		categories.clear();
		for (int i = 0; i < arrgp.length; ++i) {
			String StrGrp = arrgp[i].toString().trim();
			Category cat = new Category(StrGrp, "");
			generateChildren(StrGrp, cat);
			categories.add(cat);
		}
		ExpAdapter = new EventSettingsListAdapter(context, categories, ExpandList);
		ExpandList.setAdapter(ExpAdapter);
		ExpandList.expandGroup(0);
	}


	private void generateChildren(String StrGrp, Category cat) {
		String evntcond = "", selectevnt = "", OrderBy = "";

		if (Str_chk.equals("1") && !ShowAll) {
			if (UserType.equals("SPOUSE"))
				selectevnt = " AND (COND2 is NULL OR COND2='ALL' OR LENGTH(COND2)=0 OR COND2 like '%," + logid + ",%')";//Event Condition 12-05-2016 Updated by Tapas
			else
				selectevnt = " AND (COND1 is NULL OR COND1='ALL' OR LENGTH(COND1)=0 OR COND1 like '%," + logid + ",%')";//Event Condition 12-05-2016 Updated by Tapas
		} else if (Str_chk.equals("2")) {
			selectevnt = " AND Num3=1 ";
		}
		///////////////////get current date////////////////////////////
		String formattedDate = "";
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		formattedDate = df.format(c.getTime());
		if ((formattedDate == null) || (formattedDate.length() == 0)) {
			formattedDate = "";
		}

		if (StrGrp.equalsIgnoreCase("Forth Coming Event")) {
			evntcond = " AND DateTime(date1)>=DateTime('" + formattedDate + "') ";
			OrderBy = " Order By Date1_1 asc";
		} else if (StrGrp.equalsIgnoreCase("Past Event")) {
			evntcond = " AND DateTime(date1)<DateTime('" + formattedDate + "') ";
			OrderBy = " Order By Date1_1 Desc";
		} else {
			evntcond = "";
		}

		try {

			db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
			sqlSearch = "SELECT Text1,Text2,Text3,Date1,Date2,Num1,Text8,Num3,M_ID,COND1,COND2 from " + Table4Name + " Where Rtype='Event' " + evntcond + selectevnt + OrderBy;
			cursorT = db.rawQuery(sqlSearch, null);

			String EventName = "", EventVenue = "", EventDesc = "", StrDate1 = "", StrDate2 = "";
			int CountR = cursorT.getCount();
			if (cursorT.moveToFirst()) {
				do {
					String timeF = null;
					EventName = ChkVal(cursorT.getString(0));
					EventDesc = ChkVal(cursorT.getString(1));
					EventVenue = ChkVal(cursorT.getString(2));
					StrDate1 = ChkVal(cursorT.getString(3));
					StrDate2 = ChkVal(cursorT.getString(4));
					String Num1 = ChkVal(cursorT.getString(5));
					String Text8 = ChkVal(cursorT.getString(6));//Check for Read/unread Event
					String Num3 = ChkVal(cursorT.getString(7));
					String EventMID = ChkVal(cursorT.getString(8));//Unique Id
					String COND1 = ChkVal(cursorT.getString(9));//COND1 for Member
					String COND2 = ChkVal(cursorT.getString(10));//COND2 for Spouse

					boolean IsUserEvent = true;

					///New Option Updated on 04-04-2017(To Check Event is for logged user or not)
					if (ShowAll) {
						String s1 = "";
						if (UserType.equals("SPOUSE"))
							s1 = COND2;
						else
							s1 = COND1;

						if (s1.length() > 0 && !s1.equals("ALL") && !s1.contains("," + logid + ","))
							IsUserEvent = false;
					}

					EventName = EventName.replace("&amp;", "&");

					if (Text8.trim().length() == 0)
						Text8 = "0";//for Unread Event

					String EDesc_Num1_Text8 = EventDesc + "#" + Num1 + "#" + Text8;

					temp = StrDate1.split(" ");
					String date = temp[0].toString();//Get Only Date

					temp = StrDate2.split(" ");
					String time1 = temp[1].toString();//Get Only Time

					time1 = time1.substring(0, time1.length() - 2);
					try {
						timeF = Convert24to12(time1, date);
					} catch (java.text.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Category cat1 = new Category(EventName, timeF, EventVenue, EDesc_Num1_Text8, Num3, EventMID, IsUserEvent);
					cat.children.add(cat1);
				} while (cursorT.moveToNext());
			}
			cursorT.close();
			db.close();
		} catch (Exception ex) {
			String tt = "";
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

	public void GoBack() {
		if (Str_chk.equals("2") || Str_chk.equals("3")) {
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
