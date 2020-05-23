package group.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class News_Letter  extends Activity {

	String ClientId, ClubName, NEType = "";
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
		NEType = menuIntent.getStringExtra("NEType");
		//setContentView(webview);
		
		/*String pdf_url="http://easy-sms.in/circ1.pdf";
		
		Intent intent = new Intent(Intent.ACTION_VIEW);

		intent.setDataAndType(Uri.parse( "http://docs.google.com/viewer?url=" + pdf_url), "text/html");

		startActivity(intent);*/

		//Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf_url));
		//startActivity(browserIntent);

		Set_App_Logo_Title(); // Set App Logo and Title

		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);
		//settings.setPluginsEnabled(true);
		webview.setInitialScale(1);
		webview.getSettings().setUseWideViewPort(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(false);
		settings.setDomStorageEnabled(true);

		Chkconnection chkconn = new Chkconnection();
		boolean InternetPresent = chkconn.isConnectingToInternet(context); // Chk Internet
		if (InternetPresent == true) {

			//progressdial();//Start Progress Dialog
			//webview.loadUrl("http://www.mybackup.co.in/EventNewsGallary.aspx?CClub="+ClientId+"&Rtype="+Rtype);
			//webview.loadUrl("http://docs.google.com/gview?embedded=true&url=http://easy-sms.in/circ1.pdf"); 
			webview.loadUrl("http://easy-sms.in/test.html");

			//webview.loadUrl("https://docs.google.com/viewer?url="+pdf_url);
		} else {
			DispAlert("Connection Problem !", "No Internet Connection ", true);
		}
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
					Goback();
				else
					dialog.dismiss();
			}
		});
		ad.show();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Goback();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void Goback() {
		Intent MainBtnIntent = new Intent(context, MenuPage.class);
		MainBtnIntent.putExtra("AppLogo", AppLogo);
		startActivity(MainBtnIntent);
		finish();
	}
}
