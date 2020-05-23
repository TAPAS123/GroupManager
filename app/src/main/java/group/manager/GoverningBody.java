package group.manager;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import group.manager.AdapterClasses.Adapter_GovBody;

public class GoverningBody extends Activity {
	List<RowItem> rowItems;
	ListView LV1;
	SQLiteDatabase db;
	Cursor cursorT, cursorT2;
	String sqlSearch = "", Table2Name, Table4Name, Log, ClubName, logid, Str_user, StrName, StrMob,
			StrDesrt, Stremail, Str_Year, MTitle, Strmemno, Strpref1, Strpref2, RType = "";
	Intent menuIntent;
	int MaxNum1, i = 0;
	RowItem item;
	byte[] imgP;
	AlertDialog ad;
	Context context = this;
	TextView txtHead;
	Spinner spChangeYear;
	Button btnChangeYearr;
	AlertDialog.Builder AltDialogBldr;
	String Number, email;
	String[] CodeArr, CodeArr_val;
	byte[] AppLogo;
	LinearLayout LLChangeYear;
	CommonClass ComCllObj;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.governing_body);

		LV1 = (ListView) findViewById(R.id.LV1);
		txtHead = (TextView) findViewById(R.id.txtHead);
		LLChangeYear = (LinearLayout) findViewById(R.id.LL1);
		spChangeYear = (Spinner) findViewById(R.id.spChangeYear);
		btnChangeYearr = (Button) findViewById(R.id.btnChangeYear);

		menuIntent = getIntent();
		Log = menuIntent.getStringExtra("Clt_Log");
		logid = menuIntent.getStringExtra("Clt_LogID");
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		Str_user = menuIntent.getStringExtra("UserClubName");
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");
		MTitle = menuIntent.getStringExtra("MTitle");
		RType = menuIntent.getStringExtra("Rtype");

		Table2Name = "C_" + Str_user + "_2";
		Table4Name = "C_" + Str_user + "_4";

		ComCllObj = new CommonClass();
		AltDialogBldr = new AlertDialog.Builder(context);

		Set_App_Logo_Title(); // Set App Logo and Title

		txtHead.setText(MTitle);
		Typeface face = Typeface.createFromAsset(getAssets(), "calibri.ttf");
		txtHead.setTypeface(face);
		btnChangeYearr.setTypeface(face);

		try {
			db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);

			sqlSearch = "Select text1,Num1 from " + Table4Name + " Where Rtype='Year' order by num1 Desc";
			cursorT = db.rawQuery(sqlSearch, null);

			int tempsize = cursorT.getCount();
			CodeArr = new String[tempsize];
			CodeArr_val = new String[tempsize];

			if (cursorT.moveToFirst()) {
				do {
					CodeArr[i] = cursorT.getString(0);
					CodeArr_val[i] = cursorT.getString(1);
					i++;
				} while (cursorT.moveToNext());
			}
			cursorT.close();

			sqlSearch = "Select Max(Num1) from " + Table4Name + " Where Rtype='" + RType + "'";
			cursorT = db.rawQuery(sqlSearch, null);
			if (cursorT.moveToFirst()) {
				do {
					MaxNum1 = cursorT.getInt(0);
					System.out.println(MaxNum1);
				} while (cursorT.moveToNext());
			}
			cursorT.close();
			db.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		/// call function for update data on year change...
		FillList(String.valueOf(MaxNum1));

		if (CodeArr.length != 0) {
			ArrayAdapter<String> Adp = new ArrayAdapter<String>(GoverningBody.this, android.R.layout.simple_spinner_item, CodeArr);
			Adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spChangeYear.setAdapter(Adp);

			spChangeYear.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					// TODO Auto-generated method stub
					Str_Year = CodeArr_val[arg2];
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
				}
			});
		}

		btnChangeYearr.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				//String text = spinChangeYear.getSelectedItem().toString();
				FillList(Str_Year);  /// call function for update data on year change...
			}
		});


		LV1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				email = ChkVal(rowItems.get(position).getGVemail());
				Number = ChkVal(rowItems.get(position).getGvMob());

				if (Number.length() < 10) {
					AltDialogBldr
							.setTitle(Html.fromHtml("<font color='#E32636'>Error!</font>"))
							.setMessage("Wrong Mobile Number")
							.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.dismiss();
								}
							});
					ad = AltDialogBldr.create();
					ad.show();
				} else if (Number.length() >= 10) {
					Number = Number.substring(Number.length() - 10, Number.length());
					Number = "0" + Number;
					AltDialogBldr
							.setPositiveButton("CALL", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									ComCllObj.callOnPhone(context, Number);
								}
							})
							.setNegativeButton("SMS", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									ComCllObj.callOnSms(context, Number);
								}
							})
							.setNeutralButton("EMAIL", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									ComCllObj.callEmail(context, email, ClubName);
								}
							});

					ad = AltDialogBldr.create();
					ad.show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.govern_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.changyear:
				LLChangeYear.setVisibility(View.VISIBLE);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}


	//Get Data and Set in the listview
	private void FillList(String StrGovern) {
		if ((StrGovern.length() == 0) || (StrGovern == null)) {
			DispAlert("Result","No Record Found !");
		} else {
			try {
				db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
				sqlSearch = "SELECT Text1,Text2,Num3,Photo1,Text3,Text4 from " + Table4Name + " Where Rtype='" + RType + "' AND Num1=" + StrGovern + " Order By Num2";
				cursorT = db.rawQuery(sqlSearch, null);
				if (cursorT.getCount() == 0) {
					DispAlert("Result","No Record Found !");
					LV1.setVisibility(View.GONE);
				} else {
					LV1.setVisibility(View.VISIBLE);

					Bitmap theImage = null;
					boolean HasMem = false;
					rowItems = new ArrayList<RowItem>();
					if (cursorT.moveToFirst()) {
						do {
							StrName = ChkVal(cursorT.getString(0));
							StrMob = ChkVal(cursorT.getString(2));
							StrDesrt = cursorT.getString(1);
							imgP = cursorT.getBlob(3);
							Stremail = ChkVal(cursorT.getString(4));
							Strmemno = ChkVal(cursorT.getString(5));

							if (StrDesrt == null) {
								StrDesrt = "";
							}

							//Check Record in Table2 with M_Mob (Member)
							if ((Strmemno == null) || (Strmemno.trim().length() != 0) || (StrMob == null) || (StrMob.length() != 0)) {
								HasMem = false;
								sqlSearch = "SELECT M_Name,M_Mob,M_Email,MemNo,M_Pic,C4_BG,C4_DOB_Y from " + Table2Name + " Where (MemNo='" + Strmemno + "' and MemNo<>'') or (M_Mob='" + StrMob + "' and M_Mob<>'')";
								cursorT2 = db.rawQuery(sqlSearch, null);
								while (cursorT2.moveToNext()) {
									HasMem = true;
									StrName = ChkVal(cursorT2.getString(0));
									StrMob = ChkVal(cursorT2.getString(1));
									Stremail = ChkVal(cursorT2.getString(2));
									Strmemno = cursorT2.getString(3);
									imgP = cursorT2.getBlob(4);
									Strpref1 = cursorT2.getString(5);
									Strpref2 = cursorT2.getString(6);
									if ((Strpref1 == null) || (Strpref1.trim().length() == 0)) {
										Strpref1 = "";
									}
									if ((Strpref2 == null) || (Strpref2.trim().length() == 0)) {
										Strpref2 = "";
									}
									StrName = Strpref1 + Strpref2 + " " + StrName;
									break;
								}

								//Check Record in Table2 with S_Mob (Spouse) if HasMem false
								if (!HasMem) {
									sqlSearch = "SELECT S_Name,S_Mob,S_Email,S_Pic,C3_BG from " + Table2Name + " Where (S_Mob='" + StrMob + "' and S_Mob<>'')";
									cursorT2 = db.rawQuery(sqlSearch, null);
									while (cursorT2.moveToNext()) {
										StrName = ChkVal(cursorT2.getString(0));
										StrMob = ChkVal(cursorT2.getString(1));
										Stremail = ChkVal(cursorT2.getString(2));
										imgP = cursorT2.getBlob(3);
										Strpref1 = cursorT2.getString(4);
										if ((Strpref1 == null) || (Strpref1.trim().length() == 0)) {
											Strpref1 = "";
										}
										StrName = Strpref1 + " " + StrName;
										break;
									}
								}
								cursorT2.close();

							}
							if (imgP == null) {
								theImage = null;
							} else if (imgP != null) {
								ByteArrayInputStream imageStream = new ByteArrayInputStream(imgP);
								theImage = BitmapFactory.decodeStream(imageStream);
							} else {
								theImage = null;
							}
							if (Stremail == null) {
								Stremail = "";
							}
							if (StrMob.equals("0")) {
								StrMob = "";
							}
							item = new RowItem(theImage, StrName, StrDesrt, StrMob, Stremail);
							rowItems.add(item);
						} while (cursorT.moveToNext());
					}
					Adapter_GovBody adp = new Adapter_GovBody(context, R.layout.item_governlist, rowItems);
					LV1.setAdapter(adp);
				}
				cursorT.close();
				db.close();
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
	}


	/// set App Title and Logo
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

	private String ChkVal(String Val) {
		if (Val == null) {
			Val = "";
		}
		return Val.trim();
	}

	private void DispAlert(String Title, String Msg) {
		AlertDialog ad = new AlertDialog.Builder(this).create();
		ad.setTitle(Html.fromHtml("<font color='#E3256B'>" + Title + "</font>"));
		ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>" + Msg + "</font>"));
		ad.setCancelable(false);
		ad.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
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
		Intent MainBtnIntent = new Intent(getBaseContext(), MenuPage.class);
		MainBtnIntent.putExtra("AppLogo", AppLogo);
		startActivity(MainBtnIntent);
		finish();
	}
}
