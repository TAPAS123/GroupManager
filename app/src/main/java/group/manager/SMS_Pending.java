package group.manager;

import java.util.ArrayList;

import android.app.ActionBar;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class SMS_Pending extends Activity {

	String ClientId, ClubName, WebResult = "";
	private Context context = this;
	byte[] AppLogo;
	ProgressDialog Progsdial;
	Thread networkThread;
	WebServiceCall webcall;
	ArrayList<Product> products = null;
	Adapter_Pending_SMS AdpPendSMS = null;
	ListView LV1;
	Button btnDelete;
	ActionBar actionBar;
	CheckBox ChkSelectAll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_pending);

		TextView TvHead = (TextView) findViewById(R.id.txtHead);
		LV1 = (ListView) findViewById(R.id.Lv1);
		btnDelete = (Button) findViewById(R.id.btnDelete);

		Intent menuIntent = getIntent();
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		ClientId = menuIntent.getStringExtra("UserClubName");
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");

		webcall = new WebServiceCall();//Webservice object

		Set_App_Logo_Title(); // Set App Logo and Title

		Typeface face = Typeface.createFromAsset(getAssets(), "calibri.ttf");
		TvHead.setTypeface(face);

		btnDelete.setVisibility(View.GONE);

		Chkconnection chkconn = new Chkconnection();//Intialise Chkconnection Object
		boolean InternetPresent = chkconn.isConnectingToInternet(context);
		if (InternetPresent == true) {
			Get_Sms_PendingList();//Call a webservice
		} else {
			DispAlert("Internet Connection", "No Internet Connection !",true);
		}

		//Click Event Delete SMS
		btnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				//// Get Selected SMS UIds
				String SMSUIds = "";
				if (AdpPendSMS != null) {
					for (Product p : AdpPendSMS.getBox()) {
						if (p.box) {
							SMSUIds += p.GroupId + ",";
						}
					}
				}

				if (SMSUIds.contains(","))
					SMSUIds = SMSUIds.substring(0, SMSUIds.length() - 1);
				/////////////////////////

				if (SMSUIds.length() == 0) {
					DispAlert("Mandatory !", "Please Select SMS to Delete", false);
				} else {
					String[] Arr = SMSUIds.split(",");
					ConfirmAlert(Arr.length + "", SMSUIds); //Delete confirmation Dialog
				}
			}
		});
	}


	// Get Pending Sms from webservice
	private void Get_Sms_PendingList() {
		progressdial();
		networkThread = new Thread() {
			public void run() {
				try {
					WebResult = webcall.Get_PendingSMS(ClientId);
					runOnUiThread(new Runnable() {
						public void run() {
							if (WebResult.contains("SMS Account")) {
								DispAlert("Result !", WebResult, true);
							} else if (WebResult.contains("No Record")) {
								DispAlert("Result !", "No Pending SMS", true);
							} else if (WebResult.contains("Error") || WebResult.contains("try later")) {
								DispAlert("Error !", "Technical Problem. Please try later !", true);
							} else if (WebResult.contains("^")) {
								FillListView(false);
								SetCustActionBar();//Add Some Customise Action Bar
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


	///Fill ListView of Pending SMS
	private void FillListView(boolean chk1) {
		products = new ArrayList<Product>();

		String[] SArr = WebResult.split("#");
		for (int i = 0; i < SArr.length; i++) {
			String[] Arr = SArr[i].replace("^", "#").split("#");
			int GroupId = Integer.parseInt(Arr[0].trim());
			String Mob = Arr[1].trim();
			String Msg = Arr[2].trim();
			products.add(new Product(Mob, Msg, GroupId, chk1));
		}

		if (products.size() != 0) {
			btnDelete.setVisibility(View.VISIBLE);
			AdpPendSMS = new Adapter_Pending_SMS(SMS_Pending.this, products);
			LV1.setAdapter(AdpPendSMS);
		}
	}


	///Display Confirmation dialog for Delete Msg
	private void ConfirmAlert(String TotalSMS, final String SMSUids) {
		AlertDialog.Builder AdBuilder = new AlertDialog.Builder(context);
		AdBuilder.setTitle("Delete SMS");
		AdBuilder.setMessage(TotalSMS + " SMS will be deleted");

		AdBuilder
				.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
						DeleteSMS(SMSUids);
					}
				})
				.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});

		AdBuilder.show();
	}


	// Get Pending Sms from webservice
	private void DeleteSMS(final String SMSUids) {
		progressdial();
		networkThread = new Thread() {
			public void run() {
				try {
					WebResult = webcall.Delete_PendingSMS(ClientId, SMSUids);
					runOnUiThread(new Runnable() {
						public void run() {
							if (WebResult.contains("Deleted")) {
								DispAlert("Result !", "Messages Deleted !", true);
							} else {
								DispAlert("Result !", "Could not Delete Messages", false);
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
		//Progsdial.setTitle("App Loading");
		Progsdial.setMessage("Please Wait....");
		Progsdial.setIndeterminate(true);
		Progsdial.setCancelable(false);
		Progsdial.getWindow().setGravity(Gravity.DISPLAY_CLIP_VERTICAL);
		Progsdial.show();
	}


	// Set customise Action Bar
	public void SetCustActionBar() {
		actionBar = getActionBar();
		// add the custom view to the action bar
		actionBar.setCustomView(R.layout.br_an_layout);
		ChkSelectAll = (CheckBox) actionBar.getCustomView().findViewById(R.id.checkBoxAnBrall);
		TextView txt = (TextView) actionBar.getCustomView().findViewById(R.id.textViewnane);
		txt.setText(ClubName);

		ChkSelectAll.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (ChkSelectAll.isChecked() == true) {
					FillListView(true);
				} else {
					FillListView(false);
				}
			}
		});
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
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
