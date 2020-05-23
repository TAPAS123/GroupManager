package group.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Sug_Comp extends Activity {
	String sqlSearch, Table2Name, Table4Name, Log, ClubName, logid, Str_user, StrTitle, StrDesc, Str_IEMI, WebResult;//,StrEmail=""
	Intent menuIntent, MainBtnIntent;
	Context context = this;
	EditText txtTitle, txtDesc;
	Button btnSubmit;
	AlertDialog ad;
	//////////////////////////////////////
	WebServiceCall webcall;
	Chkconnection chkconn;
	ProgressDialog Progsdial;
	Thread networkThread;
	boolean InternetPresent = false;
	byte[] AppLogo;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.suggestion);

		txtTitle = (EditText) findViewById(R.id.edTitle);
		txtDesc = (EditText) findViewById(R.id.edDesc);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);

		menuIntent = getIntent();
		Log = menuIntent.getStringExtra("Clt_Log");
		logid = menuIntent.getStringExtra("Clt_LogID");
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		Str_user = menuIntent.getStringExtra("UserClubName");
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");
		Table2Name = "C_" + Str_user + "_2";
		Table4Name = "C_" + Str_user + "_4";

		Set_App_Logo_Title(); // Set App Logo and Title

		webcall = new WebServiceCall();//Call a Webservice
		chkconn = new Chkconnection();
		ad = new AlertDialog.Builder(this).create();

		Str_IEMI = new CommonClass().getIMEINumber(context);//Added On 14-02-2019

		btnSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				StrTitle = txtTitle.getText().toString().trim();
				StrDesc = txtDesc.getText().toString().trim();
				if (StrTitle.length() == 0) {
					DisplayAlert("Mandatory !", "Please input title", 0);
				} else if (StrDesc.length() == 0) {
					DisplayAlert("Mandatory !", "Please input description", 0);
				} else {
					InternetPresent = chkconn.isConnectingToInternet(context);

					if (InternetPresent == true) {
						CallWeb();
					} else {
						DisplayAlert("No Internet Connection !", "Please connect with Internet.", 0);
					}
				}
			}
		});
	}


	//Call A Webservice for Suggetion/complaint/feedback
	private void CallWeb() {
		progressdial();
		networkThread = new Thread() {
			public void run() {
				try {
					WebResult = webcall.clubSugg(Str_user, Str_IEMI, logid, StrTitle, StrDesc, "");
					runOnUiThread(new Runnable() {
						public void run() {
							if (WebResult.contains("Request Saved...")) {
								DisplayAlert("Result", "Your Suggestion/Feedback sent successfully.", 1);
							} else {
								DisplayAlert("Result", "Something went wrong !", 0);
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
	}

	protected void progressdial() {
		Progsdial = new ProgressDialog(this, R.style.MyTheme);
		Progsdial.setMessage("Please Wait....");
		Progsdial.setIndeterminate(true);
		Progsdial.setCancelable(false);
		Progsdial.getWindow().setGravity(Gravity.DISPLAY_CLIP_VERTICAL);
		Progsdial.show();
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

	private void DisplayAlert(String Title, String Msg, final int i) {
		ad.setTitle(Html.fromHtml("<font color='#E3256B'>" + Title + "</font>"));
		ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>" + Msg + "</font>"));
		ad.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (i == 1)
					GoBack();
				else
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
		MainBtnIntent = new Intent(getBaseContext(), MenuPage.class);
		MainBtnIntent.putExtra("AppLogo", AppLogo);
		startActivity(MainBtnIntent);
		finish();
	}
}
