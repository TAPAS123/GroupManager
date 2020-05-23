package group.manager;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import group.manager.AdapterClasses.CustomAffil;
import group.manager.AdapterClasses.CustomNews;
import group.manager.AdapterClasses.Customfamily1;

public class AffiliationAPP extends Activity {
    EditText myFilter;
    ImageView imgsrchaff;
    Button btnAddMember;
    TextView txtHead;
	List<RowEnvt> rowItems;
	ListView LV1;
	SQLiteDatabase db;
	Cursor cursorT;
	String sqlQry, Table2Name, Table4Name, Log, ClubName, logid, Str_user, StrClubName, StrCity,
			StrM_id, Strsrch, TableFamilyName, PName = "",UserType = "";
	Intent menuIntent, MainBtnIntent;
	String[] temp;
	RowEnvt item;
	Context context = this;
	int StrCount, post;
	byte[] AppLogo;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventcalendar);

		txtHead = (TextView) findViewById(R.id.txtevnthead);
		LV1 = (ListView) findViewById(R.id.listViewEvnt);
		myFilter = (EditText) findViewById(R.id.edCitysechaff);
		imgsrchaff = (ImageView) findViewById(R.id.imgSerchAff);
		btnAddMember = (Button) findViewById(R.id.btnAddMember);

		menuIntent = getIntent();
		Log = menuIntent.getStringExtra("Clt_Log");
		logid = menuIntent.getStringExtra("Clt_LogID");
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		Str_user = menuIntent.getStringExtra("UserClubName");
		StrCount = menuIntent.getIntExtra("Count", StrCount);
		post = menuIntent.getIntExtra("POstion", post);
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");

		Table2Name = "C_" + Str_user + "_2";
		Table4Name = "C_" + Str_user + "_4";
		TableFamilyName = "C_" + Str_user + "_Family";

		Set_App_Logo_Title(); // Set App Logo and Title

		Get_SharedPref_Values();// Get Stored Shared Pref Values of Login

		Typeface face = Typeface.createFromAsset(getAssets(), "calibri.ttf");
		txtHead.setTypeface(face);

		if (StrCount == 888333)
			btnAddMember.setVisibility(View.VISIBLE);
		else
			btnAddMember.setVisibility(View.GONE);

		imgsrchaff.setVisibility(View.GONE);

		if (StrCount == 0) {
			imgsrchaff.setVisibility(View.VISIBLE);
			sqlQry = "SELECT Text2,Text1,m_id from " + Table4Name + " Where Rtype='AFFI' Order By Text2,Text1";
		} else if (StrCount == 999999 || StrCount == 111 || StrCount == 222) {
			txtHead.setText("News");
			String Cond = "";//News Condition 12-05-2016 Updated by Tapas
			if (StrCount == 999999) {
				if (UserType.equals("SPOUSE"))
					Cond = " AND (COND2 is NULL OR COND2='ALL' OR LENGTH(COND2)=0 OR COND2 like '%," + logid + ",%')";//News Condition 12-05-2016 Updated by Tapas
				else
					Cond = " AND (COND1 is NULL OR COND1='ALL' OR LENGTH(COND1)=0 OR COND1 like '%," + logid + ",%')";//News Condition 12-05-2016 Updated by Tapas
			}
			sqlQry = "SELECT Text2,Text1,M_ID from " + Table4Name + " Where Rtype='News' " + Cond + " Order By Date1_1 DESC";
		} else if (StrCount == 22) {
			txtHead.setText("Pending News");
			sqlQry = "SELECT Text2,Text1,m_id from " + Table4Name + " Where Rtype='Add_News' Order By m_id DESC";
		} else if (StrCount == 888888) {
			txtHead.setText("Messages");
			sqlQry = "SELECT Text2,Text1,m_id from " + Table4Name + " Where Rtype='MESS' Order By Num1 Desc";
		} else if (StrCount == 888222 || StrCount == 888333) {
			if (StrCount == 888222)
				PName = menuIntent.getStringExtra("PName");
			else
				PName = Log;

			txtHead.setText(PName + " (Family)");
			sqlQry = "SELECT Name,Relation,Mob_1,M_id,DOB_D,DOB_M,DOB_Y,Pic from " + TableFamilyName + " Where MemId=" + post + " Order By Name";
		}

		FillList(sqlQry);/// Fill List Data

		myFilter.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//imgsrchaff.setVisibility(View.VISIBLE);
				Strsrch = myFilter.getText().toString();
				sqlQry = "SELECT Text2,Text1,m_id from " + Table4Name + " Where Rtype='AFFI' AND TEXT2 like '" + Strsrch + "%'  Order By Text2,Text1";
				FillList(sqlQry);
			}
		});

		imgsrchaff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				myFilter.setVisibility(View.VISIBLE);
				myFilter.setHint("Search city here.");
				myFilter.requestFocus();
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(myFilter, InputMethodManager.SHOW_FORCED);
				myFilter.setFocusable(true);
			}
		});

		btnAddMember.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				menuIntent = new Intent(context, UpdateMemberProfile.class);
				menuIntent.putExtra("Clt_LogID", logid);
				menuIntent.putExtra("Clt_Log", Log);
				menuIntent.putExtra("Clt_ClubName", ClubName);
				menuIntent.putExtra("UserClubName", Str_user);
				menuIntent.putExtra("Pwd", 0);
				menuIntent.putExtra("POstion", -1);
				menuIntent.putExtra("AppLogo", AppLogo);
				startActivity(menuIntent);
				finish();
			}
		});


		//ListView Click Event
		LV1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String Pwd = rowItems.get(position).getEvtdate();
				String mid = rowItems.get(position).getEvtVenue();
				if (StrCount == 888888) {
					menuIntent = new Intent(context, MessageDesk.class);
					menuIntent.putExtra("Pwd", Pwd);
				} else if (StrCount == 888222) {
					menuIntent = new Intent(context, FamilyDetailvalue.class);
					menuIntent.putExtra("Pwd", mid);
					menuIntent.putExtra("POstion", post);
					menuIntent.putExtra("PName", PName);
				} else if (StrCount == 888333) {
					menuIntent = new Intent(context, UpdateMemberProfile.class);
					menuIntent.putExtra("Pwd", mid);
					menuIntent.putExtra("POstion", post);
				} else if (StrCount == 111 || StrCount == 222) {
					//Read/UnRead News for Admin
					String V4 = rowItems.get(position).getEvtName();//Get News Title
					String V3 = rowItems.get(position).getEvtDesc();//Get News date

					if (StrCount == 111)
						menuIntent = new Intent(context, News_EventReadUnread.class);
					else if (StrCount == 222)
						menuIntent = new Intent(context, Resend_Notification.class);
					menuIntent.putExtra("PType", "News");
					menuIntent.putExtra("MID", Pwd);
					menuIntent.putExtra("VAL2", "");//Event/News (Venue)
					menuIntent.putExtra("VAL3", V3);//Event/News (Date)
					menuIntent.putExtra("VAL4", V4);//Event/News (Title)
				} else {
					menuIntent = new Intent(context, ShowAffiliation.class);
					if (StrCount == 0) {
						menuIntent.putExtra("CHKg", "1");
					} else {
						menuIntent.putExtra("CHKg", "@@@");
					}
					menuIntent.putExtra("Pwd", Pwd);
					menuIntent.putExtra("Count", StrCount);
					menuIntent.putExtra("Positn", position);
				}
				menuIntent.putExtra("Clt_Log", Log);
				menuIntent.putExtra("Clt_LogID", logid);
				menuIntent.putExtra("Clt_ClubName", ClubName);
				menuIntent.putExtra("UserClubName", Str_user);
				menuIntent.putExtra("AppLogo", AppLogo);
				startActivity(menuIntent);
				finish();
			}
		});

	}


	private void Get_SharedPref_Values() {
		SharedPreferences ShPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

		if (ShPref.contains("UserType")) {
			UserType = ShPref.getString("UserType", "");
		}
	}


	private void FillList(String sql) {
		db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		cursorT = db.rawQuery(sql, null);
		if (cursorT.getCount() == 0) {
			if (StrCount == 999999 || StrCount == 111) {
				txtHead.setText("No News");
			} else if (StrCount == 888888) {
				txtHead.setText("No Message");
			} else {
				txtHead.setText("No Record");
			}
		} else {
			String mid;
			rowItems = new ArrayList<RowEnvt>();
			if (cursorT.moveToFirst()) {
				do {
					StrCity = cursorT.getString(0);
					StrClubName = cursorT.getString(1);
					StrM_id = cursorT.getString(2);

					if (StrCount == 888222 || StrCount == 888333) {
						mid = cursorT.getString(3);
						String StrDD = ChkVal(cursorT.getString(4));
						String StrMM = ChkVal(cursorT.getString(5));
						String StrYY = ChkVal(cursorT.getString(6));
						byte[] imgP = cursorT.getBlob(7);

						String DOB = "";
						if (StrDD.length() > 0 && StrMM.length() > 0) {
							if (StrYY.length() == 0) {
								StrMM = getMonthForInt(Integer.parseInt(StrMM));
								DOB = StrDD + " " + StrMM;
							} else {
								DOB = StrDD + "-" + StrMM + "-" + StrYY;
							}
						}
						item = new RowEnvt(StrCity, StrClubName, StrM_id, mid, DOB, imgP);
					} else {
						item = new RowEnvt(StrCity, StrClubName, StrM_id, "");
					}
					rowItems.add(item);
				} while (cursorT.moveToNext());
			}
			cursorT.close();
			db.close();

			if ((StrCount == 999999) || StrCount == 111 || (StrCount == 888888) || (StrCount == 22) || (StrCount == 222)) {
				CustomNews adpNews = new CustomNews(context, R.layout.newslist, rowItems);
				LV1.setAdapter(adpNews);
				LV1.setSelection(post);
			} else if (StrCount == 888222 || StrCount == 888333) {
				Customfamily1 adpFamily1 = new Customfamily1(context, R.layout.familylist1, rowItems);
				LV1.setAdapter(adpFamily1);
			} else {
				CustomAffil adpAffi = new CustomAffil(context, R.layout.affiliationlist, rowItems);
				LV1.setAdapter(adpAffi);
				LV1.setSelection(post);
			}
		}
	}

	private String getMonthForInt(int num) {
		String month = "wrong";
		num = num - 1;
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		if (num >= 0 && num <= 11) {
			month = months[num];
		}
		return month;
	}

	//call function for initialise blank if null is there
	private String ChkVal(String DVal) {
		if ((DVal == null) || (DVal.equalsIgnoreCase("null"))) {
			DVal = "";
		}
		return DVal.trim();
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			GoBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void GoBack() {
		if (StrCount == 22) {
			MainBtnIntent = new Intent(getBaseContext(), Add_News.class);
			MainBtnIntent.putExtra("Clt_Log", Log);
			MainBtnIntent.putExtra("Clt_LogID", logid);
			MainBtnIntent.putExtra("Clt_ClubName", ClubName);
			MainBtnIntent.putExtra("UserClubName", Str_user);
			MainBtnIntent.putExtra("AppLogo", AppLogo);
			MainBtnIntent.putExtra("addchk", "2");
			startActivity(MainBtnIntent);
			finish();
		} else if (StrCount == 888222 || StrCount == 888333) {
			finish();
		} else {
			MainBtnIntent = new Intent(getBaseContext(), MenuPage.class);
			MainBtnIntent.putExtra("AppLogo", AppLogo);
			startActivity(MainBtnIntent);
			finish();
		}
	}
}
