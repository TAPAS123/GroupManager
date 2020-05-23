package group.manager;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import group.manager.AdapterClasses.Adapter_RunningApps;
import group.manager.ModelClasses.RowItem_RunningApps;

public class Running_Apps_Check extends Activity {

	ListView LV1;
	TextView TVMember, TVSpouse, TVGuest, TVTotalAndroid, TVTotalIOS, TVTotalAllow, TVTotalDisallow;
	String Tab4name, ClientId, ClubName, MemberData = "", SpouseData = "", GuestData = "", WebResult = "";//Log,logid;
	private Context context = this;
	byte[] AppLogo;
	WebServiceCall webcall;
	ProgressDialog Progsdial;
	Thread networkThread;
	boolean InternetPresent = false;

	ArrayList<RowItem_RunningApps> ArrList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.running_apps_check);

		TextView TvHead = (TextView) findViewById(R.id.txtHead);
		LV1 = (ListView) findViewById(R.id.LV1);
		TVMember = (TextView) findViewById(R.id.txtMember);
		TVSpouse = (TextView) findViewById(R.id.txtSpouse);
		TVGuest = (TextView) findViewById(R.id.txtGuest);
		TVTotalAndroid = (TextView) findViewById(R.id.txtTotalAndroid);
		TVTotalIOS = (TextView) findViewById(R.id.txtTotalIOS);
		TVTotalAllow = (TextView) findViewById(R.id.txtTotalAllow);
		TVTotalDisallow = (TextView) findViewById(R.id.txtTotalDisallow);

		Intent menuIntent = getIntent();
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		ClientId = menuIntent.getStringExtra("UserClubName");
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");

		Tab4name = "C_" + ClientId + "_4";

		Set_App_Logo_Title(); // Set App Logo and Title

		Typeface face = Typeface.createFromAsset(getAssets(), "calibri.ttf");
		TvHead.setTypeface(face);

		TVMember.setVisibility(View.GONE);//Hide by Default
		TVSpouse.setVisibility(View.GONE);//Hide by Default
		TVGuest.setVisibility(View.GONE);//Hide by Default

		TVTotalAndroid.setText("");
		TVTotalIOS.setText("");
		TVTotalAllow.setText("");
		TVTotalDisallow.setText("");

		webcall = new WebServiceCall();//Webservice object

		Chkconnection chkconn = new Chkconnection();
		InternetPresent = chkconn.isConnectingToInternet(context);
		if (InternetPresent == true) {
			WebCallRApps();// Call a Webservice to Get All Running Apps
		} else {
			DispAlert("Connection Problem !", "No Internet Connection !");
		}

		TVMember.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				ShowListData(MemberData);// Show List Member
			}
		});

		TVSpouse.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				ShowListData(SpouseData);// Show List Spouse
			}
		});

		TVGuest.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				ShowListData(GuestData);// Show List Guest
			}
		});

		//ListView Click Event
		LV1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String MID = ArrList.get(position).getMid();
				String Name = ArrList.get(position).getName();
				String Mob = ArrList.get(position).getMob();
				String Iemi = ArrList.get(position).getIemi();
				//String Dtype=  ArrList.get(position).getDtype();
				String Allow = ArrList.get(position).getAllow();
				ShowAllowDialog(MID, Name, Mob, Iemi, Allow);
			}
		});
	}


	// Display Popup Screen of Activate or NonActivate Running Apps
	private void ShowAllowDialog(final String MID, String Name, String Mob, String Iemi, final String Allow) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
		dialog.setContentView(R.layout.runningappallow_dialog);
		dialog.setCancelable(false);
		dialog.show();

		TextView TvName = (TextView) dialog.findViewById(R.id.TVName);
		TextView TvMob = (TextView) dialog.findViewById(R.id.TVMob);
		TextView TvIemi = (TextView) dialog.findViewById(R.id.TVIemi);
		Button btnAllow = (Button) dialog.findViewById(R.id.btnAllow);
		Button btnDelete = (Button) dialog.findViewById(R.id.btnDelete1);
		Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel1);

		TvName.setText(Html.fromHtml("<b>" + Name + "</b>"));
		TvMob.setText(Html.fromHtml("Mob: <font color='#1C016B'><b>" + Mob + "</b></font>"));
		TvIemi.setText(Html.fromHtml("IEMI: <font color='#1C016B'><b>" + Iemi + "</b></font>"));

		if (Allow.equalsIgnoreCase("Yes")) {
			btnAllow.setText("DEACTIVATE");
			//btnAllow.setBackgroundColor(Color.parseColor("#FF6600"));
			Drawable img = context.getResources().getDrawable(R.drawable.close);
			img.setBounds(0, 0, 60, 60);  // set the image size
			btnAllow.setCompoundDrawables(img, null, null, null);

			btnCancel.setVisibility(View.GONE);//Hide Cancel Button
			btnDelete.setText("CANCEL");
		}

		btnAllow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String Activate = "Y";
				if (Allow.equalsIgnoreCase("Yes"))
					Activate = "N";

				dialog.dismiss();
				SetAllowDisallowdApps(MID, Activate);
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		btnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!Allow.equalsIgnoreCase("Yes")) {
					SetAllowDisallowdApps(MID, "D");
				}
				dialog.dismiss();
			}
		});
	}

	//Show ListView
	private void ShowListData(String ListData) {
		String Dtype, Allow, DDate = "", Version = "";
		int TotalAndroid = 0, TotalIOS = 0, TotalAllow = 0, TotalDisallow = 0;
		RowItem_RunningApps item;
		ArrList = new ArrayList<RowItem_RunningApps>();

		String[] Arr1 = ListData.trim().split("@");

		for (int i = 0; i < Arr1.length; i++) {
			String RData = Arr1[i].trim().replace("^", "#");
			RData = RData + " ";

			if (RData.contains("#")) {
				String[] Arr2 = RData.split("#");

				Dtype = Arr2[4].trim(); // Device Type i.e(Android/Apple)
				Allow = Arr2[7].trim(); //Allow Yes/No
				DDate = Arr2[8].trim();//DDate
				Version = Arr2[9].trim();//Version

				if (Dtype.equalsIgnoreCase("Android"))
					TotalAndroid = TotalAndroid + 1;
				else
					TotalIOS = TotalIOS + 1;

				if (Allow.equalsIgnoreCase("Yes"))
					TotalAllow = TotalAllow + 1;
				else
					TotalDisallow = TotalDisallow + 1;

				item = new RowItem_RunningApps(Arr2[0], Arr2[1], Arr2[2], Arr2[3], Dtype, Arr2[5], Arr2[6], Allow, DDate, Version);

				ArrList.add(item);// Adding item object to Arrlist
			}
		}
		Adapter_RunningApps adapter = new Adapter_RunningApps(context, R.layout.runningapps_listitem, ArrList);
		LV1.setAdapter(adapter);

		if (Arr1.length > 0) {
			TVTotalAndroid.setText(Html.fromHtml("Android: <font color='#1C016B'><b>" + TotalAndroid + "</b></font>"));
			TVTotalIOS.setText(Html.fromHtml("IOS: <font color='#1C016B'><b>" + TotalIOS + "</b></font>"));
			TVTotalAllow.setText(Html.fromHtml("Active: <font color='#1C016B'><b>" + TotalAllow + "</b></font>"));
			TVTotalDisallow.setText(Html.fromHtml("Inactive: <font color='#1C016B'><b>" + TotalDisallow + "</b></font>"));
		}
	}


	//Call A Webservice to get Data
	private void WebCallRApps() {
		progressdial();
		networkThread = new Thread() {
			public void run() {
				try {
					WebResult = webcall.RApps(ClientId);
					runOnUiThread(new Runnable() {
						public void run() {
							System.out.println(WebResult);
							if (WebResult.contains("#")) {
								WebResult = WebResult + " ";
								String[] Arr1 = WebResult.split("#");
								MemberData = Arr1[0];
								SpouseData = Arr1[1];
								GuestData = Arr1[2];
								TVMember.setVisibility(View.VISIBLE);//Visible Member in every case
								if (SpouseData.trim().length() > 1)
									TVSpouse.setVisibility(View.VISIBLE);//Visible Spouse

								if (GuestData.trim().length() > 1)
									TVGuest.setVisibility(View.VISIBLE);//Visible Guest

								ShowListData(MemberData);// Show List Member
							} else if (WebResult.equalsIgnoreCase("No Data")) {
								DispAlert("Result !", "No Record found !");
							} else {
								DispAlert("Error !", "Technical Problem.Please try later !");
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


	//Call A Webservice to Activate/Deactivate Running Apps 
	private void SetAllowDisallowdApps(final String MID, final String Activate) {
		progressdial();
		networkThread = new Thread() {
			public void run() {
				try {
					final String WebR = webcall.RApps_Allow(ClientId, MID, Activate);
					runOnUiThread(new Runnable() {
						public void run() {
							if (WebR.equals("Saved")) {
								WebCallRApps();// Call a Webservice to Get All Running Apps
							} else {
								DispAlert("Error !", "Technical Problem.Please try later !");
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

	private void DispAlert(String Title, String Msg) {
		AlertDialog ad = new AlertDialog.Builder(this).create();
		ad.setTitle(Html.fromHtml("<font color='#E3256B'>" + Title + "</font>"));
		ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>" + Msg + "</font>"));
		ad.setCancelable(false);
		ad.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
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

	private void GoBack() {
		Intent menuIntent = new Intent(getBaseContext(), MenuPage.class);
		menuIntent.putExtra("AppLogo", AppLogo);
		startActivity(menuIntent);
		finish();
	}
}
