package group.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
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
import android.widget.TextView;

public class Add_Poll_Location extends Activity {
	String Log, ClubName, Str_user, logid, WebResult, TabType, Title, GPSLoc = "", SetLoc;//,StrEmail=""
	Intent menuIntent;
	Context context = this;
	TextView txtLocation;
	EditText txtRange;
	Button btnSubmit;
	AlertDialog ad;
	//////////////////////////////////////
	WebServiceCall webcall;
	Chkconnection chkconn;
	ProgressDialog Progsdial;
	Thread networkThread;
	boolean InternetPresent = false;
	byte[] AppLogo;
	int Mid = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_poll_location);

		TextView txtHead = (TextView) findViewById(R.id.txtHead);
		txtLocation = (TextView) findViewById(R.id.txtLoc);
		txtRange = (EditText) findViewById(R.id.txtRange);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);

		menuIntent = getIntent();
		Mid = menuIntent.getIntExtra("Mid", 0);
		TabType = menuIntent.getStringExtra("TabType");
		SetLoc = menuIntent.getStringExtra("SetLoc");
		Title = menuIntent.getStringExtra("Title");
		logid = menuIntent.getStringExtra("Clt_LogID");
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		Str_user = menuIntent.getStringExtra("UserClubName");
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");

		Set_App_Logo_Title(); // Set App Logo and Title

		webcall = new WebServiceCall();//Call a Webservice
		chkconn = new Chkconnection();
		ad = new AlertDialog.Builder(this).create();

		Typeface face=Typeface.createFromAsset(getAssets(), "calibri.ttf");
		txtHead.setTypeface(face);

		if (TabType.equals("PAST")) {
			btnSubmit.setVisibility(View.GONE);
		} else if (SetLoc.equals("N")) {
			btnSubmit.setVisibility(View.GONE);
		}

		///Set GPS location if set
		txtLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				GetGPS_Location();
			}
		});

		btnSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String Loc = txtLocation.getText().toString().trim();
				String Range = txtRange.getText().toString().trim();
				if (Loc.length() == 0) {
					DisplayAlert("Mandatory !", "Please set GPS Coordinates.", 0);
				} else if (Range.length() == 0) {
					DisplayAlert("Mandatory !", "Please input location range in meters", 0);
				} else {
					InternetPresent = chkconn.isConnectingToInternet(context);

					if (InternetPresent == true) {
						CallWeb(Loc, Range);
					} else {
						DisplayAlert("No Internet Connection !", "Please connect with Internet.", 0);
					}
				}
			}
		});
	}


	///Get GPS Location 
	private void GetGPS_Location() {
		GPSTracker gps = new GPSTracker(context);//Get GPS or Network Location Added on 25-10-2017
		double Lati = 0, Longi = 0;
		if (gps.canGetLocation()) {
			Lati = gps.getLatitude();//Residence Latitude
			Longi = gps.getLongitude();//Residence Longitude
		}
		if (Lati != 0 && Longi != 0)
			GPSLoc = Lati + "," + Longi;

		txtLocation.setText(GPSLoc);
	}


	//Call A Webservice to sync poll location from mobile to server
	private void CallWeb(final String Loc, final String Range) {
		progressdial();
		networkThread = new Thread() {
			public void run() {
				try {
					WebResult = webcall.club_Location_Poll(Str_user, Mid + "", Loc, Range);
					runOnUiThread(new Runnable() {
						public void run() {
							if (WebResult.contains("Saved")) {
								DisplayAlert("Result", "Poll Location has been saved successfully !", 1);
							} else {
								DisplayAlert("Technical Issue", "Something went wrong.Please try later !", 0);
							}
						}
					});
					Progsdial.dismiss();
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
		Intent MainBtnIntent = new Intent(getBaseContext(), MenuPage.class);
		MainBtnIntent.putExtra("AppLogo", AppLogo);
		startActivity(MainBtnIntent);
		finish();
	}

}
