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
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.TextView;

public class SMS_Balance extends Activity {

	String ClientId, ClubName, WebResult = "";
	private Context context = this;
	byte[] AppLogo;
	ProgressDialog Progsdial;
	Thread networkThread;
	WebServiceCall webcall;
	TextView txtBal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_bal_enquiry);

		TextView TvHead = (TextView) findViewById(R.id.txtHead);
		txtBal = (TextView) findViewById(R.id.txtBal);

		Intent menuIntent = getIntent();
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		ClientId = menuIntent.getStringExtra("UserClubName");
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");

		Set_App_Logo_Title(); // Set App Logo and Title

		webcall = new WebServiceCall();//Webservice object

		Typeface face = Typeface.createFromAsset(getAssets(), "calibri.ttf");
		TvHead.setTypeface(face);

		Chkconnection chkconn = new Chkconnection();//Intialise Chkconnection Object
		boolean InternetPresent = chkconn.isConnectingToInternet(context);
		if (InternetPresent == true) {
			Get_Sms_Balance();//Call a webservice
		} else {
			DispAlert("Internet Connection", "No Internet Connection !",true);
		}
	}


	// Get Sms Balance  from webservice
	private void Get_Sms_Balance() {
		progressdial();
		networkThread = new Thread() {
			public void run() {
				try {
					WebResult = webcall.Get_Sms_Bal(ClientId);
					runOnUiThread(new Runnable() {
						public void run() {
							if (WebResult.contains("SMS")) {
								DispAlert("Result !", WebResult,false);
							} else if (WebResult.contains("Error") || WebResult.contains("try later")) {
								DispAlert("Error !", "Technical Problem. Please try later !",false);
							} else {
								txtBal.setText(WebResult);
							}
						}
					});
					Progsdial.dismiss();
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
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


	private void DispAlert(String Title, String Msg, final boolean chk) {
		AlertDialog ad = new AlertDialog.Builder(this).create();
		ad.setTitle(Html.fromHtml("<font color='#E3256B'>" + Title + "</font>"));
		ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>" + Msg + "</font>"));
		ad.setCancelable(false);
		ad.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (chk)
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
		Intent MainBtnIntent = new Intent(context, UlilitiesList.class);
		MainBtnIntent.putExtra("AppLogo", AppLogo);
		MainBtnIntent.putExtra("CondChk", "2");
		startActivity(MainBtnIntent);
		finish();
	}

}
