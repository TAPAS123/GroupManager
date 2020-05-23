package group.manager;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import tt.in.ca.CallGetCPE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class CPE_Login extends Activity {

	EditText txtMemNo, txtPwd;
	Button btnLogin;
	String MemNo, Pwd,ClubName, CpeLoginResult = "";
	boolean InternetPresent = false;
	ProgressDialog Progsdial;
	Chkconnection chkconn;
	Context context = this;
	byte[] AppLogo;
	Thread networkThread;
	SharedPreferences ShPref;
	Editor editr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cpe_login);

		txtMemNo = (EditText) findViewById(R.id.etMemNo);
		txtPwd = (EditText) findViewById(R.id.etPwd);
		btnLogin = (Button) findViewById(R.id.btnLogin);

		Intent menuIntent = getIntent();
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");

		Get_SharedPref_Values();// Get Stored Shared Pref Values of CpeLogin

		Set_App_Logo_Title(); // Set App Logo and Title

		chkconn = new Chkconnection(); // Make an Object chkconnection class to check Internet Connection on/off

		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				///////////////
				MemNo = txtMemNo.getText().toString().trim();
				Pwd = txtPwd.getText().toString().trim();
				if (MemNo.length() == 0) {
					DisplayMsg("Mandatory", "Please input Membership No !",0);
				} else if (Pwd.length() == 0) {
					DisplayMsg("Mandatory", "Please input Password !",0);
				} else {
					InternetPresent = chkconn.isConnectingToInternet(context);
					if (InternetPresent == true) {
						CallWeb();
					} else {
						DisplayMsg("Internet Connection", "No Internet Connection !",0);
					}
				}
			}
		});
	}


	private void Get_SharedPref_Values() {
		ShPref = getSharedPreferences("MyPref_CpeLogin", Context.MODE_PRIVATE);
		if (ShPref.contains("CPE_MemNo")) {
			MemNo = ShPref.getString("CPE_MemNo", "");
			txtMemNo.setText(MemNo);
		}
		if (ShPref.contains("CPE_Pwd")) {
			Pwd = ShPref.getString("CPE_Pwd", "");
			txtPwd.setText(Pwd);
		}
	}

	private void Edit_SharedPref_Values(String Cpe_MemNo, String Cpe_Pwd) {
		editr = ShPref.edit();
		editr.putString("CPE_MemNo", Cpe_MemNo); //Saved Cpelogin MemNo
		editr.putString("CPE_Pwd", Cpe_Pwd); //Saved Cpelogin Pwd
		editr.commit();
	}


	private void CallWeb() {
		progressdial();
		networkThread = new Thread() {
			public void run() {
				try {

					String cCode = GetCCode();
					CallGetCPE CpeObj = new CallGetCPE();
					CpeLoginResult = CpeObj.GetCPE(MemNo, Pwd, "@786@892@123@tt", cCode, "@67pp@ddyy236");

					runOnUiThread(new Runnable() {
						public void run() {
							String tt = CpeLoginResult;
							if (CpeLoginResult.contains("###")) {

								Edit_SharedPref_Values(MemNo, Pwd); // Edit or Saved Shared Pref

								GoToNext();//Go To Next Activity

							} else {
								DisplayMsg("Login Error !", CpeLoginResult, 0);
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

	private String GetCCode() {
		Date Cdate = new Date(); // Current date
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyddHHMM");// Date format2
		String Datestr = sdf.format(Cdate);
		double SysTimeCode = (double) (Double.parseDouble(Datestr) * 131.327); // Special Date Condition
		DecimalFormat df = new DecimalFormat("#.###"); // Decimal format
		String CC = df.format(SysTimeCode);
		return CC;
	}

	protected void progressdial() {
		Progsdial = new ProgressDialog(this, R.style.MyTheme);
		Progsdial.setMessage("Please Wait....");
		Progsdial.setIndeterminate(true);
		Progsdial.setCancelable(false);
		Progsdial.getWindow().setGravity(Gravity.DISPLAY_CLIP_VERTICAL);
		Progsdial.show();
	}

	///Go To Next Activity
	private void GoToNext()
	{
		Intent MainBtnIntent = new Intent(context, CPE_Show1.class);
		MainBtnIntent.putExtra("Clt_ClubName", ClubName);
		MainBtnIntent.putExtra("AppLogo", AppLogo);
		MainBtnIntent.putExtra("CPEResult", CpeLoginResult);
		startActivity(MainBtnIntent);
		finish();
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

	private void DisplayMsg(String head, String body, final int ckk) {
		AlertDialog ad = new AlertDialog.Builder(this).create();
		ad.setTitle(Html.fromHtml("<font color='#E3256B'>" + head + "</font>"));
		ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>" + body + "</font>"));
		ad.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (ckk == 1)
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

	public void GoBack() {
		Intent MainBtnIntent = new Intent(context, MenuPage.class);
		MainBtnIntent.putExtra("AppLogo", AppLogo);
		startActivity(MainBtnIntent);
		finish();
	}
}
