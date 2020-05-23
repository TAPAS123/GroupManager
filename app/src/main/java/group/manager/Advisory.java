package group.manager;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import group.manager.AdapterClasses.Adapter_CPE_Show1;
import group.manager.ModelClasses.RowItem_CPE_Show1;

public class Advisory extends Activity {

	ListView LV1;
	TextView txtHead;
	byte[] AppLogo;
	String ClubName, ClientID, Tab2Name, Tab4Name, MobNo = "";
	Context context = this;
	List<RowItem_CPE_Show1> ListArrObj_CpeShow1;
	AlertDialog.Builder AlrtBuilder;
	AlertDialog ad;
	RowItem_CPE_Show1 item;
	SQLiteDatabase db;
	Cursor cursorT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cpe_show1);

		txtHead = (TextView) findViewById(R.id.txtHead);
		LV1 = (ListView) findViewById(R.id.LV1);

		Intent menuIntent = getIntent();
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");
		ClientID = menuIntent.getStringExtra("ClientID");
		String MTitle = menuIntent.getStringExtra("MTitle");

		Tab2Name = "C_" + ClientID + "_2";
		Tab4Name = "C_" + ClientID + "_4";

		Set_App_Logo_Title(); // Set App Logo and Title

		txtHead.setText(MTitle.toUpperCase());
		Typeface face = Typeface.createFromAsset(getAssets(), "calibri.ttf");
		txtHead.setTypeface(face);

		Display_ListData();// Display List Data

		LV1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
				RowItem_CPE_Show1 item = ListArrObj_CpeShow1.get(position);
				String Name = item.getYear();
				MobNo = item.getTotalHrs();

				byte[] Photo1 = GetPhoto(Name, MobNo);

				if (Photo1 != null) {
					DisplayPhotoDialog(Photo1);//Display Photo if Have
				} else {
					if (MobNo.length() != 0) {
						AlrtBuilder = new AlertDialog.Builder(context);
						//Number=Number.substring(Number.length()-10, Number.length());
						//System.out.println("cut::  "+Number);
						//Number= "0"+Number;
						AlrtBuilder
								.setPositiveButton("CALL", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										new CommonClass().callOnPhone(context, MobNo);
									}
								})
								.setNegativeButton("SMS", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										new CommonClass().callOnSms(context, MobNo);
									}
								});
						ad = AlrtBuilder.create();
						ad.show();
					}
				}
			}
		});
	}


	private void Display_ListData() {
		try {
			String name, StrQ, mob;
			ListArrObj_CpeShow1 = new ArrayList<RowItem_CPE_Show1>();

			db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
			StrQ = "select Text1,Num3 from " + Tab4Name + " where Rtype='ADV' order by Num2";
			cursorT = db.rawQuery(StrQ, null);
			int count = cursorT.getCount();
			if (cursorT.moveToFirst()) {
				do {
					name = ChkVal(cursorT.getString(0));
					mob = ChkVal(cursorT.getString(1));
					item = new RowItem_CPE_Show1(name, mob);
					ListArrObj_CpeShow1.add(item);
				} while (cursorT.moveToNext());
			}
			cursorT.close();
			db.close();

			if (count == 0) {
				AlrtBuilder = new AlertDialog.Builder(context);
				AlrtBuilder.setTitle("No record found !")
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								GoBack();
							}
						});
				ad = AlrtBuilder.create();
				ad.show();
			} else {
				Adapter_CPE_Show1 adapter = new Adapter_CPE_Show1(context, R.layout.list_item_cpe_show1, ListArrObj_CpeShow1);
				LV1.setAdapter(adapter);
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	//// Get Photo From Table4 Conditional
	private byte[] GetPhoto(String V1, String V2) {
		byte[] Photo1 = null;

		db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		String StrQ = "select Photo1 from " + Tab4Name + " where Rtype='ADV' AND  Text1='" + V1 + "' AND Num3='" + V2 + "' ";
		cursorT = db.rawQuery(StrQ, null);
		if (cursorT.moveToFirst()) {
			Photo1 = cursorT.getBlob(0);
		}
		cursorT.close();
		db.close();

		return Photo1;
	}

	/// Display photo dialog
	private void DisplayPhotoDialog(byte[] Photo1) {
		try {
			Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
			dialog.setContentView(R.layout.zoomimage);

			RelativeLayout RL = (RelativeLayout) dialog.findViewById(R.id.rr2);
			WebView WebVw = (WebView) dialog.findViewById(R.id.imageViewzoom);

			RL.getLayoutParams().height = LayoutParams.MATCH_PARENT;  // replace 100 with your dimensions
			RL.getLayoutParams().width = LayoutParams.MATCH_PARENT;

			//WebVw.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

			//// Image Compress ////
    		/*ByteArrayInputStream imageStream = new ByteArrayInputStream(Photo1);
    		Bitmap theImage = BitmapFactory.decodeStream(imageStream);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			theImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
			byte[] byteArray = byteArrayOutputStream.toByteArray();*/
			///////////////////////////

			//String imgageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

			String imgageBase64 = Base64.encodeToString(Photo1, Base64.DEFAULT);
			String image = "data:image/png;base64," + imgageBase64;
			String html = "<html><body><img src='{IMAGE_URL}' width='350' height='600' /></body></html>";

			// Use image for the img src parameter in your html and load to webview
			html = html.replace("{IMAGE_URL}", image);
			WebVw.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", "");
			WebVw.getSettings().setSupportZoom(true);
			WebVw.getSettings().setBuiltInZoomControls(true);
			WebVw.setBackgroundColor(Color.DKGRAY);
			dialog.show();
		} catch (Exception ex) {
			String tt = "";
		}
	}


	private String ChkVal(String Val) {
		if (Val == null)
			Val = "";
		return Val.trim();
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
