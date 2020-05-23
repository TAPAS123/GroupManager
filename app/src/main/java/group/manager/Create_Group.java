package group.manager;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Create_Group extends Activity {

	final Context context = this;
	byte[] AppLogo;
	String ClubName, Str_user, Tab2Name, SType, GrpName_MSIds = "";
	EditText txtGroupName;
	AlertDialog ad;
	Button btnAddMember, btnAddSpouse, btnCreateGroup, btnDeleteGroup;
	SQLiteDatabase db;
	String Log, Logid;
	ProgressDialog Progsdial;
	ArrayList<Product> ArrList_Product1, ArrList_Product2;
	Adapter_News_Group AdpGroup1, AdpGroup2;
	private boolean InternetPresent;
	WebServiceCall webcall;
	String WebResult = "";
	String[] ArrM_ids, ArrS_ids;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_group);

		TextView TvHead = (TextView) findViewById(R.id.txtHead);
		txtGroupName = (EditText) findViewById(R.id.txtGroup);
		btnAddMember = (Button) findViewById(R.id.btnAddMember);
		btnAddSpouse = (Button) findViewById(R.id.btnAddSpouse);
		btnCreateGroup = (Button) findViewById(R.id.btnCreateGroup);
		btnDeleteGroup = (Button) findViewById(R.id.btnDeleteGroup);

		Intent menuIntent = getIntent();
		Log = menuIntent.getStringExtra("Clt_Log");
		Logid = menuIntent.getStringExtra("Clt_LogID");
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		Str_user = menuIntent.getStringExtra("UserClubName");
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");
		SType = menuIntent.getStringExtra("SType");
		GrpName_MSIds = menuIntent.getStringExtra("GrpName_MSIds");

		Tab2Name = "C_" + Str_user + "_2";

		webcall = new WebServiceCall();//Call a Webservice
		ad = new AlertDialog.Builder(this).create();

		Set_App_Logo_Title(); // Set App Logo and Title

		Typeface face = Typeface.createFromAsset(getAssets(), "calibri.ttf");
		TvHead.setTypeface(face);

		if (SType.equals("Edit")) {
			TvHead.setText("Update Group");
			btnCreateGroup.setText("Update Group");
			btnDeleteGroup.setVisibility(View.VISIBLE);

			String[] Arr = GrpName_MSIds.split("#");
			String GrpName = Arr[0].trim();// Get Group Name
			String MemIds = Arr[1].trim();// Get Member Ids
			String SpouseIds = Arr[2].trim();// Get Member Ids

			String Mtxt = "", Stxt = "";
			if (!MemIds.equals("0,"))
				Mtxt = " (" + MemIds.split(",").length + ")"; //Selected Member Mids Total

			if (!SpouseIds.equals("0,"))
				Stxt = " (" + SpouseIds.split(",").length + ")"; //Selected Spouse Mids Total

			btnAddMember.setText("Select Member" + Mtxt);
			btnAddSpouse.setText("Select Spouse" + Stxt);

			ArrM_ids = MemIds.split(",");
			ArrS_ids = SpouseIds.split(",");

			txtGroupName.setText(GrpName);//Set Group Name
			txtGroupName.setEnabled(false);//Diabled Text GroupName
		}


		SetArrayListAndAdpterMS();//Set ArrayList and Adapter of Member and Spouse Data


		//Add/Edit Group
		btnCreateGroup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				String GroupName = txtGroupName.getText().toString().trim();
				if (GroupName.length() == 0) {
					AlertDisplay("Mandatory !", "Please input Group Name !", false);
				} else {
					String MS_Ids = Selected_MS_Ids(); // Get Selected MS Ids

					if (MS_Ids.trim().equals("#")) {
						AlertDisplay("Mandatory !", "Please Select Group Members !", false);
						return;
					}

					MS_Ids = MS_Ids + " ";
					String[] Arr = MS_Ids.split("#");

					String GrpMS_Ids = "M^" + Arr[0].trim() + "#S^" + Arr[1].trim();

					String DType = "A";// For Add Group
					if (SType.equals("Edit")) {
						DType = "E";//// For Edit Group
					}
					Sync_AddEditDelete_Group(Str_user, GroupName, GrpMS_Ids, DType);// Sync M-S Add/Edit Group

				}
			}
		});


		//Delete Group
		btnDeleteGroup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String GroupName = txtGroupName.getText().toString().trim();
				Sync_AddEditDelete_Group(Str_user, GroupName, "", "D");// Sync M-S Delete Group
			}
		});

		//Select Member
		btnAddMember.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ShowSelectionDialog("M");
			}
		});

		//Select Spouse
		btnAddSpouse.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ShowSelectionDialog("S");
			}
		});

	}


	// Fill Member/Spouse ArrayList with Ids
	private void SetArrayListAndAdpterMS() {
		ArrList_Product1 = new ArrayList<Product>();//For Member
		ArrList_Product2 = new ArrayList<Product>();//For Spouse

		db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);

		//Get Member Name with Mids from Table2
		String qry = "Select M_ID,M_Name,(C4_BG || \"\" || ifnull(C4_DOB_Y,'')) from " + Tab2Name + " Where M_Name Is Not NULL AND LENGTH(M_Name)<>0 Order by Upper(M_Name)";
		Cursor cursorT = db.rawQuery(qry, null);
		int RCount = cursorT.getCount();
		if (RCount > 0) {
			if (cursorT.moveToFirst()) {
				do {
					int M_Id = cursorT.getInt(0);
					String MemName = cursorT.getString(1);
					String PrefName = cursorT.getString(2);
					if (PrefName == null)
						PrefName = "";

					if (PrefName.trim().length() > 0)
						MemName = PrefName.trim() + " " + MemName;

					boolean Chk = false;
					if (SType.equals("Edit")) {
						for (int i = 0; i < ArrM_ids.length; i++) {
							int MMid = Integer.parseInt(ArrM_ids[i].trim());
							if (M_Id == MMid) {
								Chk = true;
								break;
							}
						}
					}

					ArrList_Product1.add(new Product(MemName, M_Id, Chk));
				} while (cursorT.moveToNext());
			}
		}
		cursorT.close();


		//Get Spouse Name with Mids from Table2
		qry = "Select M_ID,S_Name,C3_BG from " + Tab2Name + " Where S_Name Is Not NULL AND LENGTH(S_Name)<>0 Order by Upper(S_Name)";
		cursorT = db.rawQuery(qry, null);
		RCount = cursorT.getCount();
		if (RCount > 0) {
			if (cursorT.moveToFirst()) {
				do {
					int M_Id = cursorT.getInt(0);
					String SName = cursorT.getString(1);
					String PrefName = cursorT.getString(2);
					if (PrefName == null)
						PrefName = "";

					if (PrefName.trim().length() > 0)
						SName = PrefName.trim() + " " + SName;

					boolean Chk = false;
					if (SType.equals("Edit")) {
						for (int i = 0; i < ArrS_ids.length; i++) {
							int SMid = Integer.parseInt(ArrS_ids[i].trim());
							if (M_Id == SMid) {
								Chk = true;
								break;
							}
						}
					}

					ArrList_Product2.add(new Product(SName, M_Id, Chk));
				} while (cursorT.moveToNext());
			}
		}
		cursorT.close();

		db.close();


		// Set Member/Spouse List Adapter
		if (ArrList_Product1.size() != 0) {
			AdpGroup1 = new Adapter_News_Group(this, ArrList_Product1);
		}

		if (ArrList_Product2.size() != 0) {
			AdpGroup2 = new Adapter_News_Group(this, ArrList_Product2);
		}
	}


	// Display Popup Screen of Member/Spouse Selection
	private void ShowSelectionDialog(final String Type) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
		dialog.setContentView(R.layout.additional_data);
		dialog.setCancelable(false);
		dialog.show();

		TextView TvHead = (TextView) dialog.findViewById(R.id.tvTt);
		ListView Lv1 = (ListView) dialog.findViewById(R.id.Lv1);
		Button btnBack = (Button) dialog.findViewById(R.id.btnBack);

		Typeface face = Typeface.createFromAsset(getAssets(), "calibri.ttf");
		TvHead.setTypeface(face);

		if (Type.equals("M")) {
			TvHead.setText(Html.fromHtml("<b>Select Member</b>"));

			if (AdpGroup1 != null) {
				Lv1.setVisibility(View.VISIBLE);
				Lv1.setAdapter(AdpGroup1);
			}
		} else {
			TvHead.setText(Html.fromHtml("<b>Select Spouse</b>"));

			if (AdpGroup2 != null) {
				Lv1.setVisibility(View.VISIBLE);
				Lv1.setAdapter(AdpGroup2);
			}
		}

		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String MS_Ids = Selected_MS_Ids(); // Get Selected MS Ids
				String Mtxt = "", Stxt = "";
				if (!MS_Ids.trim().equals("#")) {
					MS_Ids = MS_Ids + " ";
					String[] Arr = MS_Ids.split("#");
					String M_Ids = Arr[0].trim();
					String S_Ids = Arr[1].trim();

					if (M_Ids.length() > 0)
						Mtxt = " (" + M_Ids.split(",").length + ")"; //Selected Member Mids Total

					if (S_Ids.length() > 0)
						Stxt = " (" + S_Ids.split(",").length + ")"; //Selected Spouse Mids Total
				}
				btnAddMember.setText("Select Member" + Mtxt);
				btnAddSpouse.setText("Select Spouse" + Stxt);
				dialog.dismiss();

			}
		});
	}


	//Get Selected Member and Spouse Ids
	private String Selected_MS_Ids() {
		String Str_M_Ids = "", Str_S_Ids = "";

		//// Get Selected Members 
		if (AdpGroup1 != null) {
			for (Product p : AdpGroup1.getBox()) {
				if (p.box) {
					Str_M_Ids += p.GroupId + ",";
				}
			}
		}
		if (Str_M_Ids.contains(","))
			Str_M_Ids = Str_M_Ids.substring(0, Str_M_Ids.length() - 1);
		/////////////////////////


		//// Get Selected Spouses
		if (AdpGroup2 != null) {
			for (Product p : AdpGroup2.getBox()) {
				if (p.box) {
					Str_S_Ids += p.GroupId + ",";
				}
			}
		}
		if (Str_S_Ids.contains(","))
			Str_S_Ids = Str_S_Ids.substring(0, Str_S_Ids.length() - 1);
		/////////////////////////

		String Selected_MSIds = Str_M_Ids.trim() + "#" + Str_S_Ids.trim();

		return Selected_MSIds;
	}


	//Sync Add/Edit/Delete Group M-S ////
	public void Sync_AddEditDelete_Group(final String ClientId, final String GroupName, final String GrpIds, final String DType) {
		Chkconnection chkconn = new Chkconnection();//Intialise Chkconnection Object
		InternetPresent = chkconn.isConnectingToInternet(context);
		if (InternetPresent == true) {
			progressdial();
			Thread T2 = new Thread() {
				@Override
				public void run() {
					try {
						WebResult = webcall.Add_Edit_Delete_Group(ClientId, GroupName, GrpIds, DType);//Add/Edit/Delete Group

						runOnUiThread(new Runnable() {
							public void run() {
								if (DType.equals("A")) {
									if (WebResult.contains("Record Saved")) {
										AlertDisplay("Result", "Group Added Successfully !", true);
									} else if (WebResult.contains("Group Name Already")) {
										AlertDisplay("Result", "Group Name Already Exists !", false);
									} else {
										AlertDisplay("Result", "Group is not Added !", false);
									}
								} else if (DType.equals("E")) {
									if (WebResult.contains("Record Saved")) {
										AlertDisplay("Result", "Group Updated Successfully !", true);
									} else {
										AlertDisplay("Result", "Group is not Updated !", false);
									}
								}
								if (DType.equals("D")) {
									if (WebResult.contains("Deleted")) {
										AlertDisplay("Result", "Group Deleted Successfully !", true);
									} else {
										AlertDisplay("Result", "Group is not Deleted !", false);
									}
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
		} else {
			AlertDisplay("Connection Problem !", "No Internet Connection !", false);
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


	private void AlertDisplay(String head, String body, final boolean GoBack) {
		ad.setTitle(Html.fromHtml("<font color='#E3256B'>" + head + "</font>"));
		ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>" + body + "</font>"));
		ad.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (GoBack)
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
		Intent MainBtnIntent = new Intent(context, GroupManagement.class);
		MainBtnIntent.putExtra("Clt_Log", Log);
		MainBtnIntent.putExtra("Clt_LogID", Logid);
		MainBtnIntent.putExtra("Clt_ClubName", ClubName);
		MainBtnIntent.putExtra("UserClubName", Str_user);
		MainBtnIntent.putExtra("AppLogo", AppLogo);
		startActivity(MainBtnIntent);
		finish();
	}
}
