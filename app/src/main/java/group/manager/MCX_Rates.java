package group.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.ListView;
import android.widget.TextView;

public class MCX_Rates  extends Activity {

	ListView LV1;
	TextView txtTitle;
	String ClientId, ClubName, MTitle, WebResult = "";
	List<RowEnvt> rowItems;
	RowEnvt item;
	byte[] AppLogo;
	ProgressDialog Progsdial;
	private boolean InternetPresent;
	Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opinionpoll_member_correct_ans);

		txtTitle = (TextView) findViewById(R.id.txtTitle);
		LV1 = (ListView) findViewById(R.id.LV1);

		Intent intent = getIntent();
		MTitle = intent.getStringExtra("MTitle");
		ClubName = intent.getStringExtra("Clt_ClubName");
		ClientId = intent.getStringExtra("UserClubName");
		AppLogo = intent.getByteArrayExtra("AppLogo");

		Set_App_Logo_Title(); // Set App Logo and Title

		Typeface face = Typeface.createFromAsset(getAssets(), "calibri.ttf");
		txtTitle.setTypeface(face);

		txtTitle.setText(MTitle);  //set heading

		Chkconnection chkconn = new Chkconnection();//Intialise Chkconnection Object
		InternetPresent = chkconn.isConnectingToInternet(context);
		if (InternetPresent == true) {
			WebCall();
		} else {
			AlertDisplay("Internet Connection", "No Internet Connection !");
		}
	}


	//ScoreSheet Data From server ////
	public void WebCall() {
		progressdial();
		Thread T2 = new Thread() {
			@Override
			public void run() {
				try {
					WebServiceCall webcall = new WebServiceCall();
					WebResult = webcall.Get_MCXRates();

					runOnUiThread(new Runnable() {
						public void run() {
							if (WebResult.contains("Data")) {
								FillList(WebResult);
							} else {
								AlertDisplay("Technical Issue", "Something went wrong");
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

	private void FillList(String jsonData) {

		if (jsonData != null) {
			rowItems = new ArrayList<RowEnvt>();
			try {
				JSONObject jsonObj = new JSONObject(jsonData);

				JSONObject Obj1 = jsonObj.getJSONObject("d");


				// Getting JSON Array node
				JSONArray Arr1 = Obj1.getJSONArray("Data");

				// looping through All Students
				for (int i = 0; i < Arr1.length(); i++) {
					JSONObject c = Arr1.getJSONObject(i);

					String ProductCode = c.getString("ProductCode");
					String PreviousClose = c.getString("PreviousClose");
					String ExpiryDate = c.getString("ExpiryDate");

					if (ProductCode.equalsIgnoreCase("ALUMINIUM") || ProductCode.equalsIgnoreCase("COPPER")
							|| ProductCode.equalsIgnoreCase("LEAD") || ProductCode.equalsIgnoreCase("NICKEL")
							|| ProductCode.equalsIgnoreCase("ZINC") || ProductCode.equalsIgnoreCase("TIN")) {
						item = new RowEnvt(ProductCode.trim() + " (" + ExpiryDate + ")", PreviousClose.trim());
						rowItems.add(item);
					}
				}
				LV1.setAdapter(new Adapter_LMERates(this, R.layout.list_lme_rates, rowItems));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
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