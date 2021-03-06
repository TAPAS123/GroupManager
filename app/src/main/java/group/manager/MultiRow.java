package group.manager;

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
import android.widget.ListView;
import android.widget.TextView;

import group.manager.AdapterClasses.Adapter_MultiRow;

public class MultiRow extends Activity {

	ListView LV1;
	TextView txtHead;
	String ClientId, ClubName, MTitle, Table4Name;
	List<RowEnvt> rowItems;
	RowEnvt item;
	byte[] AppLogo;
	Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.committee_list);

		txtHead = (TextView) findViewById(R.id.txtHead);
		LV1 = (ListView) findViewById(R.id.LV1);

		Intent intent = getIntent();
		MTitle = intent.getStringExtra("MTitle");
		ClubName = intent.getStringExtra("Clt_ClubName");
		ClientId = intent.getStringExtra("UserClubName");
		AppLogo = intent.getByteArrayExtra("AppLogo");

		Table4Name = "C_" + ClientId + "_4";

		Set_App_Logo_Title(); // Set App Logo and Title

		txtHead.setText(MTitle);  //set heading
		Typeface face = Typeface.createFromAsset(getAssets(), "calibri.ttf");
		txtHead.setTypeface(face);

		FillListData();
	}


	public void FillListData() {
		SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);

		String Qry = "SELECT Text1,Text2,Text3,Text4 from " + Table4Name + " Where Rtype='MULTIROW' Order By Num1";
		Cursor cursorT = db.rawQuery(Qry, null);
		if (cursorT.getCount() == 0) {
			AlertDisplay("", "No Record Found !");
		} else {
			rowItems = new ArrayList<RowEnvt>();

			if (cursorT.moveToFirst()) {
				do {

					String t1 = ChkVal(cursorT.getString(0));
					String t2 = ChkVal(cursorT.getString(1));
					String t3 = ChkVal(cursorT.getString(2));
					String t4 = ChkVal(cursorT.getString(3));

					item = new RowEnvt(t1, t2, t3, t4);
					rowItems.add(item);
				} while (cursorT.moveToNext());
			}
			Adapter_MultiRow adp1 = new Adapter_MultiRow(context, R.layout.list_item_multirow, rowItems);
			LV1.setAdapter(adp1);
		}
		cursorT.close();

		db.close();
	}


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

	private void AlertDisplay(String head, String body) {
		AlertDialog ad = new AlertDialog.Builder(this).create();
		ad.setTitle(Html.fromHtml("<font color='#E3256B'>" + head + "</font>"));
		ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>" + body + "</font>"));
		ad.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
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

	public void GoBack() {
		Intent intent = new Intent(getBaseContext(), MenuPage.class);
		intent.putExtra("AppLogo", AppLogo);
		startActivity(intent);
		finish();
	}
}
