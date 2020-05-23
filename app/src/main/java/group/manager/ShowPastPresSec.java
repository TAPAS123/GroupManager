package group.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class ShowPastPresSec extends Activity {
	String ClubName, Str_user, Year, Name, mobile = "", email = "", Chk;
	Intent menuIntent;
	Context context = this;
	TextView txtHead, txtNameTitle, txtYearTitle, txtEmailTitle, txtMobTitle, txtName, txtYear, txtEmail, txtMob;
	AlertDialog ad;
	byte[] AppLogo;
	String MTitle;
	AlertDialog.Builder AlrtBlder;
	CommonClass ComClassObj;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showpastpressecretary);

		txtHead = (TextView) findViewById(R.id.txtHead);
		txtNameTitle = (TextView) findViewById(R.id.txtNameTitle);
		txtYearTitle = (TextView) findViewById(R.id.txtYearTitle);
		txtMobTitle = (TextView) findViewById(R.id.txtMobTitle);
		txtEmailTitle = (TextView) findViewById(R.id.txtEmailTitle);
		txtName = (TextView) findViewById(R.id.txtName);
		txtYear = (TextView) findViewById(R.id.txtYear);
		txtMob = (TextView) findViewById(R.id.txtMob);
		txtEmail = (TextView) findViewById(R.id.txtEmail);

		menuIntent = getIntent();
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		Str_user = menuIntent.getStringExtra("UserClubName");
		Year = menuIntent.getStringExtra("Pyear");
		Name = menuIntent.getStringExtra("Pname");
		mobile = menuIntent.getStringExtra("Pmob");
		email = menuIntent.getStringExtra("Pemail");
		Chk = menuIntent.getStringExtra("StrChk");
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");
		MTitle = menuIntent.getStringExtra("MTitle");

		Set_App_Logo_Title(); // Set App Logo and Title

		ad = new AlertDialog.Builder(this).create();
		AlrtBlder = new AlertDialog.Builder(ShowPastPresSec.this);
		ComClassObj = new CommonClass();

		Typeface face=Typeface.createFromAsset(getAssets(), "calibri.ttf");
		txtHead.setTypeface(face);
		txtName.setTypeface(face);
		txtYear.setTypeface(face);
		txtMob.setTypeface(face);
		txtEmail.setTypeface(face);

		if (Chk.equals("2")) {
			txtHead.setText("Past Secretary Detail");
		} else {
			txtHead.setText(MTitle + " Detail");
		}

		SetValue(Name, txtNameTitle, txtName);
		SetValue(Year, txtYearTitle, txtYear);
		SetValue(mobile, txtMobTitle, txtMob);
		SetValue(email, txtEmailTitle, txtEmail);

		if (mobile.length() != 0) {
			txtMob.setTextColor(Color.BLUE);
			txtMob.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {

					if (mobile.length() == 10) {
						mobile = "0" + mobile;
					}

					AlrtBlder.setPositiveButton("CALL", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							ComClassObj.callOnPhone(context, mobile);
						}
					})
							.setNegativeButton("SMS", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									ComClassObj.callOnSms(context, mobile);
								}
							});
					ad = AlrtBlder.create();
					ad.show();
				}
			});
		}

		if (email.length() != 0) {
			txtEmail.setTextColor(Color.BLUE);
			txtEmail.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					AlrtBlder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					})
							.setPositiveButton("EMAIL", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									ComClassObj.callEmail(context, email,ClubName);
								}
							});
					ad = AlrtBlder.create();
					ad.show();
				}
			});
		}
	}

	private void SetValue(String Val, TextView txtcaption, TextView txtvalue) {
		if (Val.length() == 0) {
			txtcaption.setVisibility(View.GONE);
			txtvalue.setVisibility(View.GONE);
		} else {
			txtcaption.setVisibility(View.VISIBLE);
			txtvalue.setVisibility(View.VISIBLE);
			txtvalue.setText(Val);
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			GoBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void GoBack() {
		menuIntent = new Intent(getBaseContext(), PastPresi_Secretary.class);
		menuIntent.putExtra("Clt_ClubName", ClubName);
		menuIntent.putExtra("UserClubName", Str_user);
		menuIntent.putExtra("AppLogo", AppLogo);
		menuIntent.putExtra("MTitle", MTitle);
		menuIntent.putExtra("selectP_S", Chk);
		startActivity(menuIntent);
		finish();
	}
}

