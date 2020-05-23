package group.manager;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateProfile extends Activity {
	ImageView imgPerson,imgLoc_Resident, imgLoc_Office,
			imgLoc_Res_Del, imgLoc_Off_Del;
	Spinner SpBlood, SpCountry, SpState, Spcity, SpProf;
	LinearLayout LLLoc_Resident, LLLoc_Office;
	WebView webVwLoc_Resident, webVwLoc_Office;
	EditText txttbName, TxttbMob, TxtMob2, TxtLand1, TxtLand2, TxttbEmail, TxtEmail2, tXTTBADD, tXTTBADD2,
			tXTTBADD3, Txttbday, Txttbmonth, Txttbyear, TxtAnn_D, TxtAnn_M,TxtAnn_Y, tXTADDCITY, Txtprof, txtPin;
	Button btnAdditionalData, btnLoc_Resident, btnLoc_Office,btnFamily,btnSave;

	String mainStr_user, Str_MEMid, mainValue, Strmob, Str_fletter, Table2name, Table4name, TableMiscName,
			logname, LOG, citytype = "",Additional_Data = "NODATA", Additional_Data2 = "NODATA", TableFamilyName,
			UserType, sql, proftype = "";

	int ic = 0, citycount = 0, profcount = 0, ip = 0;
	boolean ChkCountryState;
	double lati_Res = 0.0, longi_Res = 0.0, lati_Off = 0.0, longi_Off = 0.0;

	String[] bloodArr = {"- - Select Blood Group - -", "A+", "B+", "AB+", "O+", "A-", "B-", "AB-", "O-"};
	String[] Arr_Full_Add_Data = null,arrcity, arrprof;
	byte[] pic, AppLogo;

	List<EditText> allEds;
	List<Spinner> allspn;
	Context context = this;
	Intent menuIntent;
	Cursor cursorT;
	SQLiteDatabase db1;
	AlertDialog.Builder ad;
	ListAdapter1 boxAdapter;

	private static final int CAMERA_REQUEST = 1;
	private static final int PICK_FROM_GALLERY = 2;
	private String userChoosenTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updateprofile);

		TextView txtHead = (TextView) findViewById(R.id.txtHead);
		tXTTBADD = (EditText) findViewById(R.id.etaDD);
		tXTTBADD2 = (EditText) findViewById(R.id.etAdd2);
		tXTTBADD3 = (EditText) findViewById(R.id.etAdd3);
		tXTADDCITY = (EditText) findViewById(R.id.etAddCity);
		txtPin = (EditText) findViewById(R.id.etPin);////Edittext Mobile2 (20-10-2016)
		txttbName = (EditText) findViewById(R.id.etname);
		TxttbMob = (EditText) findViewById(R.id.etMobileTab);
		TxtMob2 = (EditText) findViewById(R.id.etMob2);//Edittext Mobile2 (21-03-2016)
		TxtLand1 = (EditText) findViewById(R.id.etLand1);//Edittext Landline1 (21-03-2016)
		TxtLand2 = (EditText) findViewById(R.id.etLand2);//Edittext Landline2 (21-03-2016)
		TxttbEmail = (EditText) findViewById(R.id.etmailid);
		TxtEmail2 = (EditText) findViewById(R.id.etEmail2);//Edittext Email2 (21-03-2016)
		Txtprof = (EditText) findViewById(R.id.etprof);
		Txttbday = (EditText) findViewById(R.id.etDayp);
		Txttbmonth = (EditText) findViewById(R.id.eddmonthP);
		Txttbyear = (EditText) findViewById(R.id.edyearP);
		TxtAnn_D = (EditText) findViewById(R.id.etAnn_D);
		TxtAnn_M = (EditText) findViewById(R.id.etAnn_M);
		TxtAnn_Y = (EditText) findViewById(R.id.etAnn_Y);

		SpBlood = (Spinner) findViewById(R.id.spinnerbloodUpdate);
		SpCountry = (Spinner) findViewById(R.id.Sp_Country);//Spinner Country (21-03-2016)
		SpState = (Spinner) findViewById(R.id.Sp_State);//Spinner State (21-03-2016)
		Spcity = (Spinner) findViewById(R.id.spinnercity);
		SpProf = (Spinner) findViewById(R.id.spinnerprof);

		LLLoc_Resident = (LinearLayout) findViewById(R.id.LLLoc_Resident);//Added on 25-10-2017
		LLLoc_Office = (LinearLayout) findViewById(R.id.LLLoc_Office);//Added on 25-10-2017
		webVwLoc_Resident = (WebView) findViewById(R.id.webVwLoc_Resident);//Added on 25-10-2017
		webVwLoc_Office = (WebView) findViewById(R.id.webVwLoc_Office);//Added on 25-10-2017

		btnLoc_Resident = (Button) findViewById(R.id.btnLoc_Resident);//Added on 25-10-2017
		btnLoc_Office = (Button) findViewById(R.id.btnLoc_Office);//Added on 25-10-2017
        btnAdditionalData = (Button) findViewById(R.id.btnAdditionalData);
        btnFamily = (Button) findViewById(R.id.btnFamily);
        btnSave = (Button) findViewById(R.id.btnSave);

        imgPerson = (ImageView) findViewById(R.id.imgPerson);
		imgLoc_Resident = (ImageView) findViewById(R.id.imgLoc_Resident);//Added on 25-10-2017
		imgLoc_Office = (ImageView) findViewById(R.id.imgLoc_Office);//Added on 25-10-2017
		imgLoc_Res_Del = (ImageView) findViewById(R.id.imgLoc_Res_Del);//Added on 25-10-2017
		imgLoc_Off_Del = (ImageView) findViewById(R.id.imgLoc_Off_Del);//Added on 25-10-2017

		menuIntent = getIntent();
		mainStr_user = menuIntent.getStringExtra("UserClubName");
		LOG = menuIntent.getStringExtra("Clt_Log");
		Str_MEMid = menuIntent.getStringExtra("Clt_LogID");
		logname = menuIntent.getStringExtra("Clt_ClubName");
		mainValue = menuIntent.getStringExtra("WebPers");
		pic = menuIntent.getByteArrayExtra("WebPersIMG");
		UserType = menuIntent.getStringExtra("UserType");
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");

		Table2name = "C_" + mainStr_user + "_2";
		Table4name = "C_" + mainStr_user + "_4";
		TableMiscName = "C_" + mainStr_user + "_MISC";
		TableFamilyName = "C_" + mainStr_user + "_Family";

		ad = new AlertDialog.Builder(context);

		Set_App_Logo_Title(); // Set App Logo and Title

		Typeface face = Typeface.createFromAsset(getAssets(), "calibri.ttf");
		txtHead.setTypeface(face);

		LLLoc_Resident.setVisibility(View.GONE);
		LLLoc_Office.setVisibility(View.GONE);
        btnFamily.setVisibility(View.GONE);
		btnAdditionalData.setVisibility(View.GONE);//By Default Additional Data button Hidden

		ArrayAdapter<String> adp1 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, bloodArr);
		adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		SpBlood.setAdapter(adp1);

		db1 = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);

		/// Check Family Update are Visible or Not
		if (Chk_Family()) {
            btnFamily.setVisibility(View.VISIBLE);
		}

		/// Check GPS Set Location are Visible or Not
		if (!ChkLocation_Displayed()) {
			btnLoc_Resident.setVisibility(View.GONE);
			btnLoc_Office.setVisibility(View.GONE);
		}

		// Check Country and State are Displayed or Not
		ChkCountryState = ChkCountry_State();
		if (ChkCountryState) {
			SpCountry.setVisibility(View.VISIBLE);
			Fill_Coun_State_Combos();
		}

		///////////////////check if citys spinner or edittext//////////////////////////
		sql = "Select Add1 from " + TableMiscName + " where Rtype='Combo_City'";
		cursorT = db1.rawQuery(sql, null);
		citycount = cursorT.getCount();
		if (cursorT.moveToFirst()) {
			citytype = cursorT.getString(0);
		}
		cursorT.close();

		if (citycount > 0) {
			if ((citytype == null) || (citytype.length() == 0)) {
				sql = "Select Distinct(M_City) from " + Table2name;
				cursorT = db1.rawQuery(sql, null);
				int length = cursorT.getCount();
				arrcity = new String[length];
				if (cursorT.moveToFirst()) {
					do {
						arrcity[ic] = cursorT.getString(0);
						ic = ic + 1;
					} while (cursorT.moveToNext());
				}
				cursorT.close();
			} else {
				if (citytype.contains(",")) {
					citytype = citytype.replace(",", "#");
				}
				arrcity = citytype.split("#");
			}
			Spcity.setVisibility(View.VISIBLE);
			tXTADDCITY.setVisibility(View.GONE);
			ArrayAdapter<String> adpcity = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arrcity);
			adpcity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			Spcity.setAdapter(adpcity);
		} else {
			Spcity.setVisibility(View.GONE);
			tXTADDCITY.setVisibility(View.VISIBLE);
		}

		sql = "Select Add1 from " + TableMiscName + " where Rtype='Combo_Pref'";
		cursorT = db1.rawQuery(sql, null);
		profcount = cursorT.getCount();
		if (cursorT.moveToFirst()) {
			proftype = cursorT.getString(0);
		}
		cursorT.close();

		if (profcount > 0) {
			if ((proftype == null) || (proftype.length() == 0)) {
				sql = "Select Distinct(M_City) from " + Table2name;
				cursorT = db1.rawQuery(sql, null);
				int length = cursorT.getCount();
				arrprof = new String[length];
				if (cursorT.moveToFirst()) {
					do {
						arrprof[ip] = cursorT.getString(0);
						ip = ip + 1;
					} while (cursorT.moveToNext());
				}
				cursorT.close();
			} else {
				arrprof = proftype.split("#");
			}
			SpProf.setVisibility(View.VISIBLE);
			Txtprof.setVisibility(View.GONE);
			ArrayAdapter<String> adpprof = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arrprof);
			adpprof.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			SpProf.setAdapter(adpprof);
		} else {
			SpProf.setVisibility(View.GONE);
			Txtprof.setVisibility(View.VISIBLE);
		}

		db1.close();///Close Connection


		//Hide Mob2,Land1,Land2,Email2 ON 'SPOUSE' Usertype
		if (UserType.equals("SPOUSE")) {
			TxtMob2.setVisibility(View.GONE); //Hide Mobile 2
			TxtLand1.setVisibility(View.GONE); //Hide Landline 1
			TxtLand2.setVisibility(View.GONE); //Hide Landline 2
			TxtEmail2.setVisibility(View.GONE);//Hide Email 2
		} else {
			Fill_Additional_data();// Fill Additional Data When UserType=Member
		}

		FillVal(mainValue);// Fill Values Of Update Profile


		TxttbMob.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				Strmob = TxttbMob.getText().toString().trim();
				Strmob = Strmob + " ";
				Str_fletter = String.valueOf(Strmob.charAt(0));
				if (Str_fletter.equals("0")) {
					ad.setTitle(Html.fromHtml("<font color='#FF7F27'>Error !</font>"));
					ad.setMessage("Don't use zero at first, it takes only 10 digit number");
					ad.setCancelable(false);
					ad.setNegativeButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							TxttbMob.setText("");
							dialog.dismiss();
						}
					});
					ad.show();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}

		});

		// Country Spinner On Item Selected Listener
		SpCountry.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				String Country = parentView.getItemAtPosition(position).toString();
				if (Country.equalsIgnoreCase("India")) {
					SpState.setVisibility(View.VISIBLE);// State Spinner Visible
					Spcity.setVisibility(View.VISIBLE);// City Spinner Visible
					tXTADDCITY.setVisibility(View.GONE);// City Edittext Hide
				} else {
					SpState.setVisibility(View.GONE);//State Spinner Hide
					SpState.setSelection(0); // sets the first item has selected item from State spinner.
					Spcity.setVisibility(View.GONE);// City Spinner Hide
					Spcity.setSelection(0); // sets the first item has selected item from City spinner.
					tXTADDCITY.setVisibility(View.VISIBLE);// City Edittext Visible
					//tXTADDCITY.setText("");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});


		//Show Additional Details
		btnAdditionalData.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Show_Additional_Data();
			}
		});

		//Show Family Details
        btnFamily.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				menuIntent = new Intent(context, AffiliationAPP.class);
				menuIntent.putExtra("Count", 888333);
				menuIntent.putExtra("POstion", Integer.parseInt(Str_MEMid));
				menuIntent.putExtra("Clt_LogID", Str_MEMid);
				menuIntent.putExtra("Clt_Log", LOG);
				menuIntent.putExtra("Clt_ClubName", logname);
				menuIntent.putExtra("UserClubName", mainStr_user);
				menuIntent.putExtra("AppLogo", AppLogo);
				startActivity(menuIntent);
				// finish();
			}
		});


        ///Save Button
        btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String Country = "", State = "", City = "", Mob2 = "", Land1 = "", Land2 = "", Email2 = "";
				String Name = txttbName.getText().toString().trim();
				String Mob1 = TxttbMob.getText().toString().trim();//Mobile 1
				String Email1 = TxttbEmail.getText().toString().trim();// Email1
				String Add1 = tXTTBADD.getText().toString().trim();
				String Add2 = tXTTBADD2.getText().toString().trim();
				String Add3 = tXTTBADD3.getText().toString().trim();
				String Pin = txtPin.getText().toString().trim();
				String BG = SpBlood.getSelectedItem().toString().trim();
				String DOB_D = Txttbday.getText().toString().trim();
				String DOB_M = Txttbmonth.getText().toString().trim();
				String DOB_Y = Txttbyear.getText().toString().trim();
				String Ann_D = TxtAnn_D.getText().toString().trim();
				String Ann_M = TxtAnn_M.getText().toString().trim();
				String Ann_Y = TxtAnn_Y.getText().toString().trim();

				if(BG.contains("Select"))
					BG="";

				if (Name.length() == 0) {
					DispAlert("Mandatory Field !", "Please enter Name ", false);
				}
				 /*else if((Mob1.length()!=10) || (Mob2.length()>0 && Mob2.length()!=10)){
					DispAlert("Invalid Mobile !","Please enter valid 10 digits Mobile number",false);
				 }*/
				else if (Pin.length() > 0 && Pin.length() != 6) {
					DispAlert("Invalid Pin !", "Please enter valid PinCode", false);
				} else if ((Email1.length() > 0 && !ChkEmail(Email1)) || (Email2.length() > 0 && !ChkEmail(Email2))) {
					DispAlert("Invalid Email !", "Please enter valid Email", false);
				} else {
					try {
						if (Name.contains("(")) {
							String s = Name.replace("(", "#") + " ";
							String[] sday = s.split("#");
							Name = sday[0].toString();
						}

						if (citycount > 0)
							City = Spcity.getSelectedItem().toString().trim();// Get City from Spinner
						else
							City = tXTADDCITY.getText().toString().trim();// Get City from Text

						if (ChkCountryState) {
							Country = SpCountry.getSelectedItem().toString();// Get Country
							if (!Country.equalsIgnoreCase("India"))
								City = tXTADDCITY.getText().toString().trim();// Get City from Text
							else
								State = SpState.getSelectedItem().toString();// Get State
						}

						if (!UserType.equals("SPOUSE")) {
							Mob2 = TxtMob2.getText().toString().trim(); //Get Mobile 2
							Land1 = TxtLand1.getText().toString().trim(); //Get Landline 1
							Land2 = TxtLand2.getText().toString().trim(); //Get Landline 2
							Email2 = TxtEmail2.getText().toString().trim();//Get Email 2
						}

						String Loc_Res = "", Loc_Off = "";
						Loc_Res = lati_Res + "$$" + longi_Res;
						Loc_Off = lati_Off + "$$" + longi_Off;

						DbHandler db = new DbHandler(UpdateProfile.this, Table2name);
						db.UpdatePersonProfile(UserType, Name, Add1, Add2, Add3, City, Email1, DOB_D, DOB_M, DOB_Y, Ann_D, Ann_M, Ann_Y, Mob1,
								BG, pic, Str_MEMid, Country, State, Mob2, Land1, Land2, Email2, Pin, Loc_Res, Loc_Off);//Str_MEMid

						//Update Additional Data
						if (Arr_Full_Add_Data != null && Arr_Full_Add_Data.length > 0) {
							String fieldName = "", fielddata = "", UpDateStr = "";
							for (int i = 0; i < Arr_Full_Add_Data.length; i++) {
								String[] s1 = Arr_Full_Add_Data[i].replace("^^", "#").split("#");
								fieldName = s1[1].trim();
								if (s1.length == 4)
									fielddata = "'" + s1[3].trim() + "'";
								else
									fielddata = "null";

								UpDateStr = UpDateStr + fieldName + "=" + fielddata + ",";
							}

							if (UpDateStr.contains("=")) {
								UpDateStr = UpDateStr.substring(0, UpDateStr.length() - 1).trim();
								String SqlQry = "Update " + TableMiscName + " Set " + UpDateStr + " Where Rtype='DATA' And Memid=" + Str_MEMid;
								db.QueryExecuted(SqlQry);
							}
						}

						int chk = db.Checkinsert(Table4name);
						if (chk == 0) {
							db.insert(Str_MEMid, Table4name);
						}
						db.close();// Close dbHandler Class object

						DispAlert("Result !", "Profile Saved Successfully !", true);
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
				}
			}
		});


        imgPerson.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//dial.show();
				SelectImage();
			}
		});


		btnLoc_Resident.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				GPSTracker gps = new GPSTracker(context);//Get GPS or Network Location Added on 25-10-2017
				if (gps.canGetLocation()) {
					lati_Res = gps.getLatitude();//Residence Latitude
					longi_Res = gps.getLongitude();//Residence Longitude
				}
				SetGPSLocation(lati_Res, longi_Res, 1);
			}
		});


		btnLoc_Office.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				GPSTracker gps = new GPSTracker(context);//Get GPS or Network Location Added on 25-10-2017
				if (gps.canGetLocation()) {
					lati_Off = gps.getLatitude();//Office Latitude
					longi_Off = gps.getLongitude();//Office Longitude
				}
				SetGPSLocation(lati_Off, longi_Off, 2);
			}
		});


		imgLoc_Resident.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				GPSTracker gps = new GPSTracker(context);//Get GPS or Network Location Added on 25-10-2017
				if (gps.canGetLocation()) {
					lati_Res = gps.getLatitude();//Residence Latitude
					longi_Res = gps.getLongitude();//Residence Longitude
				}
				SetGPSLocation(lati_Res, longi_Res, 1);
			}
		});


		imgLoc_Office.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				GPSTracker gps = new GPSTracker(context);//Get GPS or Network Location Added on 25-10-2017
				if (gps.canGetLocation()) {
					lati_Off = gps.getLatitude();//Office Latitude
					longi_Off = gps.getLongitude();//Office Longitude
				}
				SetGPSLocation(lati_Off, longi_Off, 2);
			}
		});

		///Delete Residence location if set
		imgLoc_Res_Del.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				lati_Res = 0.0;//Residence Latitude
				longi_Res = 0.0;//Residence Longitude
				LLLoc_Resident.setVisibility(View.GONE);
				btnLoc_Resident.setVisibility(View.VISIBLE);
			}
		});

		///Delete Residence location if set
		imgLoc_Off_Del.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				lati_Off = 0.0;//Office Latitude
				longi_Off = 0.0;//Office Longitude
				LLLoc_Office.setVisibility(View.GONE);
				btnLoc_Office.setVisibility(View.VISIBLE);
			}
		});

	}


	// Display Popup Screen of Additional data
	private void Show_Additional_Data()
	{
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
		dialog.setContentView(R.layout.updateprofile_additional);
		dialog.setCancelable(false);

        TextView txtHead = (TextView) findViewById(R.id.txtHead);
		LinearLayout LLMain = (LinearLayout) dialog.findViewById(R.id.LLMain);
		ImageView btnBack = (ImageView) dialog.findViewById(R.id.imgBtnBack);

        Typeface face = Typeface.createFromAsset(getAssets(), "calibri.ttf");
        txtHead.setTypeface(face);

		String[] Arr1, Arr2, Arr3, Arrval = null;
		String sql = "", Title = "", fieldName = "", fielddata = "", fieldtype = " ", fielddatachk = "";
		final List<String> Str_List = new ArrayList<String>();

		allEds = null;
		allEds = new ArrayList<EditText>();

		allspn = null;
		allspn = new ArrayList<Spinner>();

		if (Arr_Full_Add_Data != null && Arr_Full_Add_Data.length > 0) {
			for (int i = 0; i < Arr_Full_Add_Data.length; i++) {
				Arr1 = Arr_Full_Add_Data[i].replace("^^", "#").split("#");
				Title = Arr1[0].trim();
				fieldName = Arr1[1].trim();
				if (Arr1.length == 3)
					fieldtype = Arr1[2].trim();
				else
					fieldtype = " ";

				if (Arr1.length == 4)
					fielddata = Arr1[3].trim();
				else
					fielddata = "";
				fielddatachk = fielddata;

				SQLiteDatabase db1 = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
				sql = "Select Add1 from " + TableMiscName + " Where Rtype='Combo_" + Title + "'";//"+Title.trim()+"
				Cursor cursorT2 = db1.rawQuery(sql, null);
				if (cursorT2.moveToFirst()) {
					String val = cursorT2.getString(0);
					if (val == null) {
						val = "";
					}
					if (val.contains(",")) {
						fieldtype = "Combo";
						fielddata = val.replace(",", "#");
					}
				}
				cursorT2.close();

				///For Multi Combo selection
				sql = "Select Add1 from " + TableMiscName + " Where Rtype='MCombo_" + Title + "'";
				cursorT2 = db1.rawQuery(sql, null);
				if (cursorT2.moveToFirst()) {
					String val = cursorT2.getString(0);
					if (val == null) {
						val = "";
					}
					if (val.contains(",")) {
						fieldtype = "MCombo";
						fielddata = val.replace(",", "#");
					}
				}
				cursorT2.close();

				db1.close();

				Str_List.add(Title + "^^" + fieldName + "^^" + fieldtype); // Add Title and FieldName in String List
				LLMain.addView(NewView(i + 10, Title, fielddata, fieldtype, fielddatachk));
			}
		} else {
			//Open Db Connection
			SQLiteDatabase db1 = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
			//Set Additional Data 1
			if (!Additional_Data.equals("NODATA") && Additional_Data.contains("#")) {
				Arr1 = Additional_Data.split("#");
				Arr2 = Arr1[0].replace("^", "#").split("#");
				Arr3 = Arr1[1].split(",");
				if (Arr1.length == 3) {
					Arrval = Arr1[2].replace("^", "#").split("#");
				}

				sql = "Select " + Arr1[1] + " from " + TableMiscName + " Where Rtype='DATA' And Memid=" + Str_MEMid;
				Cursor cursorT = db1.rawQuery(sql, null);
				if (cursorT.moveToFirst()) {
					for (int i = 0; i < Arr2.length; i++) {
						Title = Arr2[i].trim();
						fieldName = Arr3[i].trim();
						if (Arrval == null) {
							fieldtype = " ";
						} else {
							fieldtype = Arrval[i].trim();
						}

						fielddatachk = ChkVal(cursorT.getString(i));
						fielddata = fielddatachk;
						//Toast.makeText(context, Title, 0).show();

						sql = "Select Add1 from " + TableMiscName + " Where Rtype='Combo_" + Title + "'";
						Cursor cursorT2 = db1.rawQuery(sql, null);
						if (cursorT2.moveToFirst()) {
							String val = cursorT2.getString(0);
							if (val == null) {
								val = "";
							}
							if (val.contains(",")) {
								fieldtype = "Combo";
								fielddata = val.replace(",", "#");
							}
						}
						cursorT2.close();

						///For Multi Combo selection
						sql = "Select Add1 from " + TableMiscName + " Where Rtype='MCombo_" + Title + "'";
						cursorT2 = db1.rawQuery(sql, null);
						if (cursorT2.moveToFirst()) {
							String val = cursorT2.getString(0);
							if (val == null) {
								val = "";
							}
							if (val.contains(",")) {
								fieldtype = "MCombo";
								fielddata = val.replace(",", "#");
							}
						}
						cursorT2.close();

						Str_List.add(Title + "^^" + fieldName + "^^" + fieldtype); // Add Title and FieldName in String List
						LLMain.addView(NewView(i + 10, Title, fielddata, fieldtype, fielddatachk));
					}
				}
				cursorT.close();
			}

			//Set Additional Data 2
			if (!Additional_Data2.equals("NODATA") && Additional_Data2.contains("#")) {
				Arr1 = Additional_Data2.split("#");
				Arr2 = Arr1[0].replace("^", "#").split("#");
				Arr3 = Arr1[1].split(",");
				if (Arr1.length == 3) {
					Arrval = Arr1[2].replace("^", "#").split("#");
				}
				sql = "Select " + Arr1[1] + " from " + TableMiscName + " Where Rtype='DATA' And Memid=" + Str_MEMid;
				Cursor cursorT = db1.rawQuery(sql, null);
				if (cursorT.moveToFirst()) {
					for (int i = 0; i < Arr2.length; i++) {
						Title = Arr2[i].trim();
						fieldName = Arr3[i].trim();
						if (Arrval == null) {
							fieldtype = " ";
						} else {
							fieldtype = Arrval[i].trim();
						}

						fielddatachk = ChkVal(cursorT.getString(i));

						sql = "Select Add1 from " + TableMiscName + " Where Rtype='Combo_" + Title + "'";
						Cursor cursorT2 = db1.rawQuery(sql, null);
						if (cursorT2.moveToFirst()) {
							String val = cursorT2.getString(0);
							if (val == null) {
								val = "";
							}
							if (val.contains(",")) {
								fieldtype = "Combo";
								fielddata = val.replace(",", "#");
							}
						}
						cursorT2.close();
						Str_List.add(Title + "^^" + fieldName + "^^" + fieldtype); // Add Title and FieldName in String List
						LLMain.addView(NewView(i + 20, Title, fielddata, fieldtype, fielddatachk));
					}
				}
				cursorT.close();
			}
			db1.close();// Close Db Connection
		}

		dialog.show();

		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int totalsize, j = 0, k = 0;
				totalsize = Str_List.size();
				Arr_Full_Add_Data = new String[totalsize];
				//System.out.println(totalsize);
				for (int i = 0; i < totalsize; i++) {
					System.out.println(Str_List.get(i).toString());
					String[] arr = Str_List.get(i).replace("^^", "#").split("#");
					System.out.println(arr[2] + "  " + arr[2].length());
					String valtype = arr[2];
					System.out.println(valtype);
					if ((valtype == null) || (valtype.trim().length() == 0)) {
						String FieldData = allEds.get(j).getText().toString();
						Arr_Full_Add_Data[i] = Str_List.get(i).trim() + "^^" + FieldData;
						j = j + 1;
					} else {
						if (valtype.equals("Combo")) {
							String FieldData = allspn.get(k).getSelectedItem().toString();
							Arr_Full_Add_Data[i] = Str_List.get(i).trim() + "^^" + FieldData;
							//System.out.println(Arr_Full_Add_Data[i]);
							k = k + 1;
						} else {
							String FieldData = allEds.get(j).getText().toString();
							Arr_Full_Add_Data[i] = Str_List.get(i).trim() + "^^" + FieldData;
							//System.out.println(Arr_Full_Add_Data[i]);
							j = j + 1;
						}
					}
				}
				dialog.dismiss();
			}
		});
	}


	///Set GPS Location Residence / Office
	private void SetGPSLocation(double Lati, double Longi, int Type) {
		if (Lati > 0 && Longi > 0) {
			String Loc = Lati + "," + Longi;

			String url = "http://maps.google.com/maps/api/staticmap?center=" + Loc + "&zoom=15&size=400x400&sensor=true";
			//url = "http://maps.google.com/maps?q="+Loc;

			if (Type == 1)//Set Residence Loctaion
			{
				LLLoc_Resident.setVisibility(View.VISIBLE);
				btnLoc_Resident.setVisibility(View.GONE);

				String text = "<html><body><p align=\"justify\"><b>Location Residence</b>";
				text += "</p><iframe width='100%' height='100%' src='https://maps.google.com/maps?q=" + Loc + "&hl=es;z=15&amp;output=embed'></iframe></body></html>";

				webVwLoc_Resident.loadDataWithBaseURL(null, text, "text/html", "UTF-8", null);
				webVwLoc_Resident.getSettings().setJavaScriptEnabled(true);
			} else if (Type == 2)//Set Office Location
			{
				LLLoc_Office.setVisibility(View.VISIBLE);
				btnLoc_Office.setVisibility(View.GONE);

				String text = "<html><body><p align=\"justify\"><b>Location Office</b>";
				text += "</p><iframe width='100%' height='100%' src='https://maps.google.com/maps?q=" + Loc + "&hl=es;z=15&amp;output=embed'></iframe></body></html>";

				webVwLoc_Office.loadDataWithBaseURL(null, text, "text/html", "UTF-8", null);
				webVwLoc_Office.getSettings().setJavaScriptEnabled(true);
			}
		}
	}


	//Select Image Using Camera or Gallery
	private void SelectImage() {
		final CharSequence[] items = {"Take from Camera", "Select from Gallery","Cancel"};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Add Photo !");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				boolean result = true;//Utility.checkPermission(this);

				if (items[item].equals("Take from Camera")) {
					userChoosenTask = "Take from Camera";
					if (result)
						cameraIntent();

				} else if (items[item].equals("Select from Gallery")) {
					userChoosenTask = "Select from Gallery";
					if (result)
						galleryIntent();

				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}


	@SuppressLint("Override")
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case 123:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					if (userChoosenTask.equals("Take from Camera"))
						cameraIntent();
					else if (userChoosenTask.equals("Select from Gallery"))
						galleryIntent();
				} else {
					//code for deny
				}
				break;
		}
	}


	//Gallery Intent
	private void galleryIntent() {
		//Intent intent = new Intent();
		//intent.setType("image/*");
		//intent.setAction(Intent.ACTION_GET_CONTENT);
		//intent.putExtra("crop", "true");
		//intent.putExtra("aspectX", 0);
		//intent.putExtra("aspectY", 0);
		//intent.putExtra("outputX", 200);
		//intent.putExtra("outputY", 150);
		//intent.putExtra("return-data", true);
		//startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FROM_GALLERY);

        /// Updated on 23-05-2020
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_FROM_GALLERY);
	}


	//Camera Intent
	private void cameraIntent() {
		//Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		//intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
		//intent.putExtra("crop", "true");
		//intent.putExtra("aspectX", 0);
		//intent.putExtra("aspectY", 0);
		//intent.putExtra("outputX", 200);
		//intent.putExtra("outputY", 150);
		//startActivityForResult(intent, CAMERA_REQUEST);

        /// Updated on 23-05-2020
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
        Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);//Added on 07-01-2018
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);//Added on 07-01-2018
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//Added on 07-01-2018
        startActivityForResult(intent, CAMERA_REQUEST);
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == PICK_FROM_GALLERY)
				SetImageByCamera_Gallery(data, 1);
			else if (requestCode == CAMERA_REQUEST)
				SetImageByCamera_Gallery(data, 2);
		}
	}


    //Set Image From Camera or Photo Gallery(////updated on 23-05-2020///)
    private void SetImageByCamera_Gallery(Intent intent, int Type) {
        Bitmap yourImage = null;
        if (Type == 1)//Gallery
        {
            try {
                final Uri imageUri = intent.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                yourImage = BitmapFactory.decodeStream(imageStream);

                //if(intent.getExtras()!=null){
                ///Comes from Gallery
                //  yourImage = intent.getExtras().getParcelable("data");
                //}else{
                //comes from media or other folders or memmory card
                //yourImage = MediaStore.Images.Media.getBitmap(this.getApplicationContext().getContentResolver(), intent.getData());
                //}

            } catch (OutOfMemoryError ex) {
                ex.printStackTrace();
                DispAlert("Pic Error", "Pic Error", false);
            } catch (Exception e) {
                e.printStackTrace();
                DispAlert("Pic Error", "Pic Error", false);
            }
        } else {
            //Take from Camera///
            File f = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }
            try {

                BitmapFactory.Options options = new BitmapFactory.Options();
                yourImage = BitmapFactory.decodeFile(f.getAbsolutePath(), options);

                /// Check Image Rotation Required or Not
                yourImage=Rotate_Image.rotateImageIfRequired(context,yourImage,Uri.fromFile(f));

                f.delete();//Delete image file
            } catch (Exception e) {
                DispAlert("Pic Error", "Pic Error", false);
            }
        }

        if (yourImage != null) {
            int w = yourImage.getWidth();
            int h = yourImage.getHeight();
            //int p = yourImage.getByteCount();
            //int d = yourImage.getDensity();

            int RWidth = 160, RHeight = 204;

            //Resize the Original Image
            Bitmap ResizeImg = ScaleDownBitmap(yourImage, RWidth, RHeight, true);
            //int w1 = ResizeImg.getWidth();
            //int h1 = ResizeImg.getHeight();
            //int p1=ResizeImg.getByteCount();
            //int d1=ResizeImg.getDensity();

            // convert bitmap to byte
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ResizeImg.compress(Bitmap.CompressFormat.PNG, 90, stream);

            int k = stream.size();
            int p2 = ResizeImg.getByteCount();
            pic = stream.toByteArray();
            imgPerson.setImageBitmap(ResizeImg);
        } else {
            DispAlert("Pic Error", "Pic Error", false);
        }
    }


	//Rezise Image Size Wise
	private Bitmap ScaleDownBitmap(Bitmap originalImage, float maxWidth, int maxHeight, boolean filter) {
		float ratio = Math.min((float) maxWidth / originalImage.getWidth(), (float) maxHeight / originalImage.getHeight());
		int width = (int) Math.round(ratio * (float) originalImage.getWidth());
		int height = (int) Math.round(ratio * (float) originalImage.getHeight());

		Bitmap newBitmap = Bitmap.createScaledBitmap(originalImage, width, height, filter);
		return newBitmap;
	}

	//Resize Image with Wanted Width And Wanted Height Wise
	private Bitmap ScaleBitmap(Bitmap originalImage, int wantedWidth, int wantedHeight) {
		Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		Matrix m = new Matrix();
		m.setScale((float) wantedWidth / originalImage.getWidth(), (float) wantedHeight / originalImage.getHeight());
		canvas.drawBitmap(originalImage, m, new Paint());
		return output;
	}


	//Check Display Family Data or Not
	private boolean Chk_Family()
	{
		boolean R = false;
		String s1="";
		sql = "Select Text1 from " + Table4name + " where Rtype='FAMILY'";
		cursorT = db1.rawQuery(sql, null);
		if (cursorT.moveToFirst()) {
			s1 = cursorT.getString(0);
		}
		cursorT.close();

		if (s1.equalsIgnoreCase("YES")) {
			R = true;
		}

		return R;
	}

	//Check Set GPS Location Option Are Displayed(Visible) Or Not
	private boolean ChkLocation_Displayed() {
		boolean R = false;
		String MID = "";
		sql = "Select M_ID from " + Table4name + " where Rtype='E_LOC'";
		cursorT = db1.rawQuery(sql, null);
		if (cursorT.moveToFirst()) {
			MID = cursorT.getString(0);
		}
		cursorT.close();
		if (MID.trim().length() > 0)
			R = true;

		return R;
	}

	//Check Country And State Combos Are Displayed(Visible) Or Not
	private boolean ChkCountry_State() {
		boolean R = false;
		String MID = "";
		sql = "Select M_ID from " + Table4name + " where Rtype='ADD_COUN'";
		cursorT = db1.rawQuery(sql, null);
		if (cursorT.moveToFirst()) {
			MID = cursorT.getString(0);
		}
		cursorT.close();
		if (MID.trim().length() > 0)
			R = true;

		return R;
	}

	//Fill Country And State Combos
	private void Fill_Coun_State_Combos() {
		String[] ArrCountry = null, ArrState = null;
		String S1;

		sql = "Select Add1 from " + TableMiscName + " where Rtype='Combo_Country'";
		cursorT = db1.rawQuery(sql, null);
		while (cursorT.moveToFirst()) {
			S1 = ChkVal(cursorT.getString(0));
			if (S1.contains(","))
				ArrCountry = S1.split(",");
			break;
		}
		cursorT.close();

		sql = "Select Add1 from " + TableMiscName + " where Rtype='Combo_State'";
		cursorT = db1.rawQuery(sql, null);
		while (cursorT.moveToFirst()) {
			S1 = ChkVal(cursorT.getString(0));
			if (S1.contains(","))
				ArrState = S1.split(",");
			break;
		}
		cursorT.close();

		//Set Country Combo
		if (ArrCountry != null) {
			ArrayAdapter adp_Country = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, ArrCountry);
			adp_Country.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			SpCountry.setAdapter(adp_Country);
		}

		//Set State Combo 
		if (ArrState != null) {
			ArrayAdapter adp_State = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, ArrState);
			adp_State.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			SpState.setAdapter(adp_State);
		}
	}


	private LinearLayout NewView(int id, String TvTitle, String DValue, String fieldtype, String fielddatachk) {
		LinearLayout L1 = new LinearLayout(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(15, 5, 5, 5);
		L1.setLayoutParams(params);

		L1.setId(id);
		L1.setOrientation(android.widget.LinearLayout.HORIZONTAL);

		//Add Textview
		TextView tv = new TextView(this);
		tv.setTextSize(16);
		tv.setText(TvTitle);
		L1.addView(tv);

		if (fieldtype.contains("MCombo"))
			L1.addView(editText_MultiCombo(id, TvTitle, DValue, fielddatachk));//Add Edittext for MultiCombo
		else if (fieldtype.contains("Combo"))
			L1.addView(Combos(id, TvTitle, DValue, fielddatachk));//Add Spinner
		else
			L1.addView(editText(id, TvTitle, DValue));//Add Edittext

		return L1;
	}

	private EditText editText(int id, String hint, String DValue) {
		final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		EditText ET = new EditText(this);
		allEds.add(ET);// Add EditText in Edittext List
		ET.setLayoutParams(lparams);
		ET.setId(id);
		ET.setHint(hint);
		ET.setTextSize(16);
		ET.setText(DValue);
		return ET;
	}

	private EditText editText_MultiCombo(int id, final String hint, final String DValue, final String fielddatachk) {
		final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		final EditText ET = new EditText(this);
		allEds.add(ET);// Add EditText in Edittext List
		ET.setLayoutParams(lparams);
		ET.setId(id);
		ET.setFocusable(false);
		ET.setClickable(true);
		//ET.setHint(hint);
		ET.setText(fielddatachk);

		ET.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//dial.show();
				multiCombo_Dialog(ET, hint, DValue, fielddatachk);
			}
		});

		return ET;
	}

	private void multiCombo_Dialog(final EditText ET, String FName, String DValue, String fielddatachk) {
		String[] arr = DValue.split("#");

		String[] Arr1 = null;
		if (fielddatachk.length() > 1)
			Arr1 = fielddatachk.split(",");

		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
		dialog.setContentView(R.layout.additional_data);
		dialog.setCancelable(false);

		TextView txtHead = (TextView) dialog.findViewById(R.id.tvTt);
		ListView LLMain = (ListView) dialog.findViewById(R.id.Lv1);
		Button btnBack = (Button) dialog.findViewById(R.id.btnBack);

		txtHead.setText(FName);
		Typeface face = Typeface.createFromAsset(getAssets(), "calibri.ttf");
		txtHead.setTypeface(face);

		ArrayList<Product> ProObj = new ArrayList<Product>();

		for (int i = 0; i < arr.length; i++) {
			boolean chk = false;
			String Name = arr[i];
			if (Arr1.length > 0) {
				for (int j = 0; j < Arr1.length; j++) {
					if (Name.equals(Arr1[j])) {
						chk = true;
						break;
					}
				}
			}

			ProObj.add(new Product(arr[i], i, chk));
		}

		if (ProObj.size() != 0) {
			boxAdapter = new ListAdapter1(this, ProObj);
			LLMain.setAdapter(boxAdapter);
		}

		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//dial.show();
				String RData = "";

				for (Product p : boxAdapter.getBox()) {
					if (p.box) {
						//p.mob = p.mob.substring(0, p.mob.length()-1).trim();
						if (p.name.length() != 0) {
							RData += p.name + ",";
						}
					}
				}
				if (RData.length() != 0) {
					RData = RData.substring(0, RData.length() - 1).trim();
				} else {
					Toast.makeText(getApplicationContext(), "No check box selected", 1).show();
				}

				ET.setText(RData);

				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private Spinner Combos(int id, String hint, String DValue, String fielddatachk) {
		String[] arr = DValue.split("#");
		ArrayAdapter<String> adp1 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arr);
		adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		Spinner spn = new Spinner(this);
		allspn.add(spn);// Add EditText in Edittext List
		spn.setLayoutParams(lparams);
		spn.setId(id);
		spn.setAdapter(adp1);
		if ((fielddatachk != null) && (fielddatachk.length() > 0)) {
			spn.setSelection(((ArrayAdapter<String>) spn.getAdapter()).getPosition(fielddatachk));
		}
		return spn;
	}


	@SuppressWarnings("unchecked")
	public void FillVal(String WResult) {
		String s, Strname, Stra1 = "", Stra2 = "", Stra3 = "", StrMob1 = "", StrMob2 = "", StrLand1 = "", StrLand2 = "", StrEmail1 = "", StrEmail2 = "", Strbg, Pin = "";
		String StrCity, StrCountry, StrState, DOB_D, DOB_M, DOB_Y, Ann_D, Ann_M, Ann_Y;
		s = WResult.replace("#", "#") + " ";
		String[] temp = s.split("#");
		//str_memid=temp[0].toString();
		Strname = temp[0].toString().trim();
		StrMob1 = temp[1].toString().trim();
		Stra1 = temp[2].toString().trim();
		Stra2 = temp[3].toString().trim();
		Stra3 = temp[4].toString().trim();
		StrCity = temp[5].toString().trim();
		StrEmail1 = temp[6].toString().trim();
		Strbg = temp[7].toString().trim();
		DOB_D = temp[8].toString().trim();
		DOB_M = temp[9].toString().trim();
		DOB_Y = temp[10].toString().trim();
		Ann_D = temp[11].toString().trim();
		Ann_M = temp[12].toString().trim();
		Ann_Y = temp[13].toString().trim();
		StrCountry = temp[14].toString().trim();
		StrState = temp[15].toString().trim();
		Pin = temp[16].toString().trim();
		String Loc_Res = temp[17].toString().trim();
		String Loc_Off = temp[18].toString().trim();

		if (!UserType.equals("SPOUSE")) {
			StrMob2 = temp[19].toString().trim();//Get Mobile2
			StrLand1 = temp[20].toString().trim();//Get Land1
			StrLand2 = temp[21].toString().trim();//Get Land2
			StrEmail2 = temp[22].toString().trim();//Get Email2
		}

		txttbName.setText(Strname);
		TxttbMob.setText(StrMob1);
		TxtMob2.setText(StrMob2);
		TxtLand1.setText(StrLand1);
		TxtLand2.setText(StrLand2);
		TxttbEmail.setText(StrEmail1);
		TxtEmail2.setText(StrEmail2);
		tXTTBADD.setText(Stra1);
		tXTTBADD2.setText(Stra2);
		tXTTBADD3.setText(Stra3);
		tXTADDCITY.setText(StrCity);
		txtPin.setText(Pin);

		if (citycount > 0)
			Spcity.setSelection(((ArrayAdapter<String>) Spcity.getAdapter()).getPosition(StrCity));

		if (ChkCountryState) {
			if (StrCountry.length() > 1)
				SpCountry.setSelection(((ArrayAdapter<String>) SpCountry.getAdapter()).getPosition(StrCountry));
			if (StrState.length() > 1)
				SpState.setSelection(((ArrayAdapter<String>) SpState.getAdapter()).getPosition(StrState));

			if (!StrCountry.equalsIgnoreCase("India")) {
				Spcity.setVisibility(View.GONE);// City Spinner Hide
				Spcity.setSelection(0);
				tXTADDCITY.setVisibility(View.VISIBLE);
			}
		}

		//Set DOB
		Txttbday.setText(DOB_D.trim());
		Txttbmonth.setText(DOB_M.trim());
		Txttbyear.setText(DOB_Y.trim());

		//Set Anniversary Date
		TxtAnn_D.setText(Ann_D);
		TxtAnn_M.setText(Ann_M);
		TxtAnn_Y.setText(Ann_Y);

		// Set Image
		if ((pic != null)) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(pic, 0, pic.length);
			if (bitmap != null) {
                imgPerson.setImageBitmap(bitmap);
			}
		}

		if(Strbg.length()>0 && !Strbg.contains("Select"))
		   SpBlood.setSelection(findStringIndex(bloodArr, Strbg));/// Set Blood Group

		//Set Residence Location
		if (Loc_Res.contains("$$")) {
			Loc_Res = Loc_Res.replace("$$", "#");
			String[] Arr1 = Loc_Res.split("#");
			lati_Res = Double.parseDouble(Arr1[0]);
			longi_Res = Double.parseDouble(Arr1[1]);

			SetGPSLocation(lati_Res, longi_Res, 1);
		}

		//Set Office Location
		if (Loc_Off.contains("$$")) {
			Loc_Off = Loc_Off.replace("$$", "#");
			String[] Arr1 = Loc_Off.split("#");
			lati_Off = Double.parseDouble(Arr1[0]);
			longi_Off = Double.parseDouble(Arr1[1]);

			SetGPSLocation(lati_Off, longi_Off, 2);
		}
	}

	private void Fill_Additional_data() {
		SQLiteDatabase db1 = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		//Get Additional Data
		String Qry = "Select Add1,Add2 from " + TableMiscName + " Where Rtype='MASTER'";
		Cursor cursorT = db1.rawQuery(Qry, null);
		while (cursorT.moveToNext()) {
			Additional_Data = cursorT.getString(0); //Additional Data main
			Additional_Data2 = cursorT.getString(1);//Additional Data 2

			if (Additional_Data == null || Additional_Data == "") {
				Additional_Data = "NODATA";
			}
			if (Additional_Data2 == null || Additional_Data2 == "") {
				Additional_Data2 = "NODATA";
			}
			break;
		}
		cursorT.close();
		db1.close();
		//////////////////////////////////////////

		if (!Additional_Data.equals("NODATA") || !Additional_Data2.equals("NODATA")) {
			btnAdditionalData.setVisibility(View.VISIBLE);
		} else {
			btnAdditionalData.setVisibility(View.GONE);
		}
	}


	private boolean ChkEmail(String Email) {
		//String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
		String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		// onClick of button perform this simplest code.
		if (Email.matches(emailPattern))
			return true;
		else
			return false;
	}

	private int findStringIndex(String[] array, String value) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].equalsIgnoreCase(value)) {
				return i;
			}
		}
		return 0;
	}


	private String ChkVal(String Val) {
		if (Val == null)
			Val = "";
		return Val.trim();
	}

	private void Set_App_Logo_Title() {
		setTitle(logname); // Set Title
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
		ad.setTitle(Html.fromHtml("<font color='#E3256B'>" + Title + "</font>"));
		ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>" + Msg + "</font>"));
		ad.setCancelable(false);
		ad.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (chk)
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
		Intent MainBtnIntent = new Intent(context, MenuPage.class);
		MainBtnIntent.putExtra("AppLogo", AppLogo);
		startActivity(MainBtnIntent);
		finish();
	}
}
		
