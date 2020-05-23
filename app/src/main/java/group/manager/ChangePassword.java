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
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ChangePassword extends Activity {

	EditText txtNewPwd, txtConfirmPwd;
	Button btnSubmit;
	String ClubName, ClientId, Log, Logid, WebResult;
	final Context context = this;
	byte[] AppLogo;
	ProgressDialog Progsdial;
	private boolean InternetPresent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changepassword);

		TextView txtHead = (TextView) findViewById(R.id.txtHead);
		txtNewPwd = (EditText) findViewById(R.id.txtNewPwd);
		txtConfirmPwd = (EditText) findViewById(R.id.txtConfirmPwd);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);

		Intent menuIntent = getIntent();
		Logid = menuIntent.getStringExtra("Clt_LogID");
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		ClientId = menuIntent.getStringExtra("UserClubName");
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");

		Set_App_Logo_Title(); // Set App Logo and Title

		Typeface face = Typeface.createFromAsset(getAssets(), "calibri.ttf");
		txtHead.setTypeface(face);

		btnSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				String NewPass = txtNewPwd.getText().toString().trim();
				String ConfirmPass = txtConfirmPwd.getText().toString().trim();

				if (NewPass.length() == 0) {
					txtNewPwd.setError("Input New Password");
				} else if (ConfirmPass.length() == 0) {
					txtConfirmPwd.setError("Input Confirm Password");
				} else if (!NewPass.equals(ConfirmPass)) {
					AlertDisplay("", "Password not Matched !", false);
				} else {

					//SQLiteDatabase db=openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);

					//String Qry="Update LoginMain SET PWD=PWD='"+NewPass.trim()+"' Where ClientID='"+ClientId+"' ";
					//db.execSQL(Qry);
					//db.close();

					Chkconnection chkconn = new Chkconnection();//Intialise Chkconnection Object
					InternetPresent = chkconn.isConnectingToInternet(context);
					if (InternetPresent == true) {
						WebCall_ChangePass(NewPass);
					} else {
						AlertDisplay("Internet Connection", "No Internet Connection !", false);
					}
				}
			}
		});
	}


	//Change Pass from Mobile to Server Call WebService ////
	public void WebCall_ChangePass(final String NewPass) {
		progressdial();
		Thread T2 = new Thread() {
			@Override
			public void run() {
				try {
					WebServiceCall webcall = new WebServiceCall();
					WebResult = webcall.Change_Password(ClientId, Logid, NewPass);

					runOnUiThread(new Runnable() {
						public void run() {
							if (WebResult.equals("Saved")) {
								AlertDisplay("Result", "Password has been changed Successfully !", true);
							} else {
								AlertDisplay("Technical Issue", "Something went wrong !", false);
							}
						}
					});
				} catch (Exception e) {
					//System.out.println(e.getMessage());
					e.printStackTrace();
				}
				Progsdial.dismiss();
			}
		};
		T2.start();
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


	private void AlertDisplay(String head, String body, final boolean flag) {

		AlertDialog ad = new AlertDialog.Builder(this).create();
		ad.setTitle(Html.fromHtml("<font color='#E3256B'>" + head + "</font>"));
		ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>" + body + "</font>"));
		ad.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (flag) {
					GoBack();
				} else {
					dialog.dismiss();
				}
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
		Intent MainBtnIntent = new Intent(context, MenuPage.class);
		MainBtnIntent.putExtra("AppLogo", AppLogo);
		startActivity(MainBtnIntent);
		finish();
	}
}
