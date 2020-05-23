package group.manager;

import java.util.ArrayList;
import java.util.Date;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import group.manager.AdapterClasses.CustomAffil;

public class PastPresi_Secretary extends Activity {
	ArrayList<RowEnvt> contact_list;
	ListView LV1;
	TextView txtPastPresident, txtPastSecratory;
	String Tab4name, Str_user, ClubName, ChkPS;
	private Context context = this;
	byte[] AppLogo;
	String MTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pastpresisecretary);

		TextView txtHead = (TextView) findViewById(R.id.txtHead);
		txtPastPresident = (TextView) findViewById(R.id.txtPP);
		txtPastSecratory = (TextView) findViewById(R.id.txtPS);
		LV1 = (ListView) findViewById(R.id.LV1);

		Intent menuIntent = getIntent();
		ChkPS = menuIntent.getStringExtra("selectP_S");
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		Str_user = menuIntent.getStringExtra("UserClubName");
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");
		MTitle = menuIntent.getStringExtra("MTitle");

		Tab4name = "C_" + Str_user + "_4";

		Set_App_Logo_Title(); // Set App Logo and Title

		Typeface face=Typeface.createFromAsset(getAssets(), "calibri.ttf");
		txtHead.setTypeface(face);

		txtHead.setText(MTitle+" / Secretary");
		txtPastPresident.setText(MTitle);

		FillListData();//get data from database and show in listview.

		txtPastPresident.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {

				ChkPS="1";
				FillListData();//get data from database and show in listview.
			}
		});

		txtPastSecratory.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {

				ChkPS="2";
				FillListData();//get data from database and show in listview.
			}
		});

		LV1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String year = contact_list.get(position).getEvtName();
				String name = contact_list.get(position).getEvtDesc();
				String mob = contact_list.get(position).getEvtdate();
				String email = contact_list.get(position).getEvtVenue();

				Intent menuIntent = new Intent(context, ShowPastPresSec.class);
				menuIntent.putExtra("Clt_ClubName", ClubName);
				menuIntent.putExtra("UserClubName", Str_user);
				menuIntent.putExtra("Pyear", year);
				menuIntent.putExtra("Pname", name);
				menuIntent.putExtra("Pmob", mob);
				menuIntent.putExtra("Pemail", email);
				menuIntent.putExtra("AppLogo", AppLogo);
				menuIntent.putExtra("StrChk", ChkPS);
				menuIntent.putExtra("MTitle", MTitle);
				startActivity(menuIntent);
				finish();
			}
		});
	}

	private void FillListData() {
		SQLiteDatabase db;
		Cursor cursorT;
		RowEnvt item;
		String name, year, mobile, email,Qry;
		contact_list = new ArrayList<RowEnvt>();

		if (ChkPS.equals("1")) {
			Qry = "Select Text1,Text2,Text3,Text4 from " + Tab4name + " where Rtype='PASTP' Order by Num2 desc";
		} else {
			Qry = "Select Text1,Text2,Text3,Text4 from " + Tab4name + " where Rtype='PASTS' Order by Num2 desc";
		}

		db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);

		cursorT = db.rawQuery(Qry, null);
		if(cursorT.getCount()>0) {
			if (cursorT.moveToFirst()) {
				do {
					name = ChkVal(cursorT.getString(0));
					year = ChkVal(cursorT.getString(1));
					mobile = ChkVal(cursorT.getString(2));
					email = ChkVal(cursorT.getString(3));

					item = new RowEnvt(year, name, mobile, email, "", "");
					contact_list.add(item);// Adding contact to list
				} while (cursorT.moveToNext());
			}
			cursorT.close();
			db.close();
			CustomAffil adapter = new CustomAffil(context, R.layout.affiliationlist, contact_list);
			LV1.setAdapter(adapter);
		}else {
			AlertDisplay("No Record Found !");
		}
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

    private String ChkVal(String Val) {
		if (Val == null)
			Val = "";
		return Val.trim();
	}

	private void AlertDisplay(String body) {
		AlertDialog ad = new AlertDialog.Builder(this).create();
		ad.setTitle(Html.fromHtml("<font color='#E3256B'>Result</font>"));
		ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>" + body + "</font>"));
		ad.setCancelable(false);
		ad.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				GoBack();
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
		Intent menuIntent = new Intent(getBaseContext(), MenuPage.class);
		menuIntent.putExtra("AppLogo", AppLogo);
		startActivity(menuIntent);
		finish();
	}
}
