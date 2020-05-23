package group.manager;

import java.io.ByteArrayInputStream;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageDesk extends Activity {
	TextView txtHead, txtTitle, txtDesc;
	Intent menuIntent;
	String Table2Name, Table4Name, Log, ClubName, logid, Str_user, STRM_ID;
	byte[] AppLogo;
	ImageView img1;
	SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messagedesk);

		txtHead = (TextView) findViewById(R.id.txtHead);
		img1 = (ImageView) findViewById(R.id.img1);
		txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtDesc = (TextView) findViewById(R.id.txtDesc);

		menuIntent = getIntent();
		Log = menuIntent.getStringExtra("Clt_Log");
		logid = menuIntent.getStringExtra("Clt_LogID");
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		Str_user = menuIntent.getStringExtra("UserClubName");
		STRM_ID = menuIntent.getStringExtra("Pwd");
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");

		Table2Name = "C_" + Str_user + "_2";
		Table4Name = "C_" + Str_user + "_4";

		Set_App_Logo_Title(); // Set App Logo and Title

		Typeface face=Typeface.createFromAsset(getAssets(), "calibri.ttf");
		txtHead.setTypeface(face);
		txtTitle.setTypeface(face);

		FillData();///Set Data
	}


	private void FillData() {

		String title = "", message = "", messageby = "";
		byte[] pic = null;

		db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		String Qry = "SELECT Text1,Add1,Text2,Photo1 from " + Table4Name + " Where Rtype='MESS' and M_ID=" + STRM_ID + " Order By Num1 Desc";
		Cursor cursorT = db.rawQuery(Qry, null);
		if (cursorT.moveToFirst()) {
			do {
				title = ChkVal(cursorT.getString(0));
				message = ChkVal(cursorT.getString(1));
				messageby = ChkVal(cursorT.getString(2));
				pic = cursorT.getBlob(3);
			} while (cursorT.moveToNext());
		}
		cursorT.close();
		db.close();

		txtHead.setText(title);
		txtTitle.setText(message);
		txtDesc.setText(messageby);
		if (pic == null) {
			img1.setVisibility(View.GONE);
		} else {
			img1.setVisibility(View.VISIBLE);
			ByteArrayInputStream imageStream = new ByteArrayInputStream(pic);
			Bitmap theImage = BitmapFactory.decodeStream(imageStream);
			img1.setImageBitmap(theImage);
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


	private String ChkVal(String Val)
	{
		if(Val==null)
			Val="";
		return Val.trim();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			GoBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void GoBack() {
		Intent MainBtnIntent = new Intent(getBaseContext(), AffiliationAPP.class);
		MainBtnIntent.putExtra("POstion", 0);
		MainBtnIntent.putExtra("Count", 888888);
		MainBtnIntent.putExtra("Clt_Log", Log);
		MainBtnIntent.putExtra("Clt_LogID", logid);
		MainBtnIntent.putExtra("Clt_ClubName", ClubName);
		MainBtnIntent.putExtra("UserClubName", Str_user);
		MainBtnIntent.putExtra("AppLogo", AppLogo);
		startActivity(MainBtnIntent);
		finish();
	}
}
