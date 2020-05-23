package group.manager;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import group.manager.AdapterClasses.Adapter_CPE_Show1;
import group.manager.ModelClasses.RowItem_CPE_Show1;

public class CPE_Show2 extends Activity {

	ListView LV1;
	TextView txtHead;
	byte[] AppLogo;
	String ClubName, CPEResult;
	Context context = this;
	List<RowItem_CPE_Show1> ListArrObj_CpeShow1;
	String Cpe_Year, Cpe_YearData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cpe_show1);

		txtHead = (TextView) findViewById(R.id.txtHead);
		LV1 = (ListView) findViewById(R.id.LV1);

		Intent menuIntent = getIntent();
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");
		CPEResult = menuIntent.getStringExtra("CPEResult");
		Cpe_Year = menuIntent.getStringExtra("Cpe_Year");
		Cpe_YearData = menuIntent.getStringExtra("Cpe_YearData");

		Set_App_Logo_Title(); // Set App Logo and Title

		txtHead.setText("CPE Attendance (" + Cpe_Year + ")");
		Typeface face=Typeface.createFromAsset(getAssets(), "calibri.ttf");
		txtHead.setTypeface(face);

		Display_ListData(); // Display List Data

		LV1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
				RowItem_CPE_Show1 item = ListArrObj_CpeShow1.get(position);
				String List_Title = item.getYear();
				int Hrs = Integer.parseInt(item.getTotalHrs());
				if ((Hrs > 0) && (List_Title.equals("SLA") || List_Title.equals("Certification Courses") || List_Title.equals("e-Learning Courses"))) {
					Intent MainBtnIntent = new Intent(context, CPE_Show3.class);
					MainBtnIntent.putExtra("Clt_ClubName", ClubName);
					MainBtnIntent.putExtra("AppLogo", AppLogo);
					MainBtnIntent.putExtra("CPEResult", CPEResult);
					MainBtnIntent.putExtra("Cpe_Year", Cpe_Year);
					MainBtnIntent.putExtra("Cpe_YearData", Cpe_YearData);
					MainBtnIntent.putExtra("LTitle", List_Title);
					startActivity(MainBtnIntent);
					finish();
				}
			}
		});
	}


	private void Display_ListData() {
		String[] Arr_List_Data = Cpe_YearData.split("##"); // List Wise Data
		String List5Data_Refine = GetList5Data_Refine(Arr_List_Data[0], Cpe_Year);
		String[] Arr_List5Values = List5Data_Refine.split("#");
		ListArrObj_CpeShow1 = new ArrayList<RowItem_CPE_Show1>();
		for (int i = 0; i < Arr_List5Values.length; i++) {
			String[] s1 = Arr_List5Values[i].trim().split(":");
			String L5Title = s1[0].trim();
			String Hrs = s1[1].trim();
			RowItem_CPE_Show1 item = new RowItem_CPE_Show1(L5Title, Hrs, "");
			ListArrObj_CpeShow1.add(item);
		}
		Adapter_CPE_Show1 adapter = new Adapter_CPE_Show1(context, R.layout.list_item_cpe_show1, ListArrObj_CpeShow1);
		LV1.setAdapter(adapter);
	}

	private String GetList5Data_Refine(String List5Data, String Year) {
		int TotalHrs = 0, S1 = 0, S2 = 0, S3 = 0, S4 = 0, S5 = 0;
		if (List5Data.contains("~")) {
			String[] s1 = List5Data.split("~");
			S1 = Integer.parseInt(s1[0].replace("L5:SLA:", "").trim());
			S2 = Integer.parseInt(s1[1].replace("Certification Courses:", "").trim());
			S3 = Integer.parseInt(s1[2].replace("e-Learning Courses:", "").trim());
			S4 = 0;//Integer.parseInt(s1[3].trim().replace("Year  "+Year+"  Other CPE Hours:", ""));
			S5 = Integer.parseInt(s1[4].replace("ULA:", "").trim());
			TotalHrs = S1 + S2 + S3 + S4 + S5;
		}
		String RData = "SLA:" + S1 + "#Certification Courses:" + S2 + "#e-Learning Courses:" + S3 + "#Other CPE Hours:" + S4 + "#ULA:" + S5;
		return RData;
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
		Intent MainBtnIntent = new Intent(context, CPE_Show1.class);
		MainBtnIntent.putExtra("Clt_ClubName", ClubName);
		MainBtnIntent.putExtra("AppLogo", AppLogo);
		MainBtnIntent.putExtra("CPEResult", CPEResult);
		startActivity(MainBtnIntent);
		finish();
	}
}
