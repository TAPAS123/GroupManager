package group.manager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import group.manager.AdapterClasses.Adapter_CPE_Program_2;
import group.manager.ModelClasses.RowItem_CPE_Program_2;

public class CPE_Program_2 extends Activity {

	ListView LV1;
	TextView txtHead, txt_LDate, txt_LTime, txt_LVenue, txt_LOrg_By, txt_LCpe_Hrs, txt_LFees, txt_LRemark;
	ImageView ImgVw_Ad;
	byte[] AppLogo;
	String ClubName, Str_user, Tab4Name, MTitle = "";
	Context context = this;
	List<RowItem_CPE_Program_2> ListArrObj_CpeProg_2;
	boolean flag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cpe_program_2);

		txtHead = (TextView) findViewById(R.id.txtHead);
		txt_LDate = (TextView) findViewById(R.id.txtCpe_LDate);
		txt_LTime = (TextView) findViewById(R.id.txtCpe_LTime);
		txt_LVenue = (TextView) findViewById(R.id.txtCpe_LVenue);
		txt_LOrg_By = (TextView) findViewById(R.id.txtCpe_LOrgBy);
		txt_LCpe_Hrs = (TextView) findViewById(R.id.txtCpe_LHrs);
		txt_LFees = (TextView) findViewById(R.id.txtCpe_LFees);
		txt_LRemark = (TextView) findViewById(R.id.txtCpe_LRemarks);
		LV1 = (ListView) findViewById(R.id.LV1);
		RelativeLayout RL_CpeCreditFees = (RelativeLayout) findViewById(R.id.RL_CpeCreditFees);
		ImgVw_Ad = (ImageView) findViewById(R.id.imgVw_Ad); // ImageView for  Ad (04-03-2017)

		Intent menuIntent = getIntent();
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		Str_user = menuIntent.getStringExtra("UserClubName");
		MTitle = menuIntent.getStringExtra("MTitle");
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");
		String CPE_Prog_Result = menuIntent.getStringExtra("CPE_Prog_Result");

		Tab4Name = "C_" + Str_user + "_4"; // 4th Table Name

		Set_App_Logo_Title(); // Set App Logo and Title

		Typeface face = Typeface.createFromAsset(getAssets(), "calibri.ttf");
		txtHead.setTypeface(face);

		SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);

		///CPE_Prog_Result contains M_Id Comes from CPE Notification New Option(28-01-2107)
		if (!CPE_Prog_Result.contains("~")) {
			flag = true;

			String sqlSearch = "Select Text1,Text2,Text3,Text4,Text5,Text6,Text7,Text8,Add1,Num1 from " + Tab4Name + " where Rtype='CPE' AND M_Id=" + CPE_Prog_Result;

			Cursor cursorT = db.rawQuery(sqlSearch, null);
			if (cursorT.moveToFirst()) {
				String LProg_Name = ChkVal(cursorT.getString(0));
				String LOrg_By = ChkVal(cursorT.getString(1));
				String LVenue = ChkVal(cursorT.getString(2));
				String LCPE_Hrs = ChkVal(cursorT.getString(3));
				String LFees = ChkVal(cursorT.getString(4));
				String LDate = ChkVal(cursorT.getString(5));
				String LTime_From = ChkVal(cursorT.getString(6));
				String LTime_To = ChkVal(cursorT.getString(7));
				String LAdd = ChkVal(cursorT.getString(8));
				String LNum1 = ChkVal(cursorT.getString(9));
				String LTime_FromTo = LTime_From + "-" + LTime_To;

				if (LTime_FromTo.trim().equals("-"))
					LTime_FromTo = "";

				CPE_Prog_Result = LProg_Name + "~" + LOrg_By + "~" + LVenue + "~" + LCPE_Hrs + "~" + LFees + "~" + LDate + "~" + LTime_FromTo + "~" + LAdd + "~" + LNum1;
			}
			cursorT.close();
		} else {
			String sqlSearch = "Select Photo1 from " + Tab4Name + " where Rtype='CPE' AND M_Id=" + CPE_Prog_Result.split("~")[9];

			Cursor cursorT = db.rawQuery(sqlSearch, null);
			if (cursorT.moveToFirst()) {
				byte[] ImgAd = cursorT.getBlob(0);//Get Ad Photo

				// Set Image for AD (04-03-2017)
				if (ImgAd == null) {
					ImgVw_Ad.setVisibility(View.GONE);
				} else {
					Bitmap bitmap = BitmapFactory.decodeByteArray(ImgAd, 0, ImgAd.length);
					ImgVw_Ad.setVisibility(View.VISIBLE);
					ImgVw_Ad.setImageBitmap(bitmap);
				}
			}
			cursorT.close();
		}

		db.close();// Close Connection

		// Get CPE Data Split with '~'
		String[] Arr_Cpe = CPE_Prog_Result.split("~");
		String LProg_Name = Arr_Cpe[0].trim();
		String LOrg_By = Arr_Cpe[1].trim();
		String LVenue = Arr_Cpe[2].trim();
		String LCPE_Hrs = Arr_Cpe[3].trim();
		String LFees = Arr_Cpe[4].trim();
		String LDate = Arr_Cpe[5].trim();
		String LTime_FromTo = Arr_Cpe[6].trim();
		String LAdd = Arr_Cpe[7].trim();
		String LNum1 = Arr_Cpe[8].trim();
		////////////////////////////////

		txtHead.setText(LProg_Name); // Set Program Name
		txt_LDate.setText(LDate);
		txt_LTime.setText(LTime_FromTo);
		txt_LVenue.setText(LVenue);
		txt_LOrg_By.setText(LOrg_By);

		if (LOrg_By.length() > 0)
			txt_LOrg_By.setText(Html.fromHtml("<strong>  Organised By: </strong>" + LOrg_By));

		txt_LCpe_Hrs.setText(LCPE_Hrs);
		txt_LFees.setText(LFees);
		txt_LRemark.setText(LAdd);

		if (LCPE_Hrs.length() == 0 && LFees.length() == 0) {
			RL_CpeCreditFees.setVisibility(View.GONE);
		}

		Display_ListData(LNum1);// Display List Data
	}


	private void Display_ListData(String Num1) {
		ListArrObj_CpeProg_2 = new ArrayList<RowItem_CPE_Program_2>();
		SQLiteDatabase dbase = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		String sqlSearch = "Select Text1,Text2,Add1,Text3,Text4 from " + Tab4Name + " Where Rtype='CPE_S' and Num1=" + Num1 + " Order by Num3";
		Cursor cursorT = dbase.rawQuery(sqlSearch, null);
		if (cursorT.moveToFirst()) {
			do {
				String Sess_Name = ChkVal(cursorT.getString(0));
				String Sess_Time = ChkVal(cursorT.getString(1));
				String Sess_Topic = ChkVal(cursorT.getString(2));
				String Sess_Speaker = ChkVal(cursorT.getString(3));
				String Sess_Chairman = ChkVal(cursorT.getString(4));
				RowItem_CPE_Program_2 item = new RowItem_CPE_Program_2(Sess_Name, Sess_Time, Sess_Topic, Sess_Speaker, Sess_Chairman);
				ListArrObj_CpeProg_2.add(item);
			} while (cursorT.moveToNext());
		}
		cursorT.close(); // close cursor
		dbase.close(); // Close Local DataBase
		Adapter_CPE_Program_2 adapter = new Adapter_CPE_Program_2(context, R.layout.list_item_cpe_program_2, ListArrObj_CpeProg_2);
		LV1.setAdapter(adapter);
	}


	private String ChkVal(String Val) {
		if (Val == null) {
			Val = "";
		}
		return Val.trim();
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

	public void GoBack() {
		finish();
	}
}
