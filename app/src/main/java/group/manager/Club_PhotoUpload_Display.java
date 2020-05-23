package group.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Club_PhotoUpload_Display extends Activity {

	String ClientId, ClubName, Auth_ClubName = "";
	private Context context = this;
	byte[] AppLogo;
	ProgressDialog Progsdial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery);

		WebView webview = (WebView) findViewById(R.id.webView1);

		Intent menuIntent = getIntent();
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		ClientId = menuIntent.getStringExtra("UserClubName");
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");
		Auth_ClubName = menuIntent.getStringExtra("Auth_ClubName");
		//setContentView(webview);

		Set_App_Logo_Title(); // Set App Logo and Title

		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);
		webview.setInitialScale(1);
		webview.getSettings().setUseWideViewPort(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(false);
		settings.setDomStorageEnabled(true);

		Chkconnection chkconn = new Chkconnection();
		boolean InternetPresent = chkconn.isConnectingToInternet(context); // Chk Internet
		if (InternetPresent == true) {

			//progressdial();//Start Progress Dialog
			webview.loadUrl("http://www.mybackup.co.in/GM_ClubPhotoDisplay.aspx?CClub=" + ClientId + "&AuthClub=" + Auth_ClubName);
			//webview.loadUrl("http://www.mybackup.co.in/livevideo.aspx");
		} else {
			DispAlert("Connection Problem !", "No Internet Connection ", true);
		}

		webview.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
				viewx.loadUrl(urlx);
				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

				//if(Progsdial.isShowing())
				//Progsdial.dismiss();//Progress dialog dismiss
				// do something
			}
		});
	}


	protected void progressdial() {
		Progsdial = new ProgressDialog(this, R.style.MyTheme);
		//Progsdial.setTitle("App Loading");
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
		finish();
	}
}