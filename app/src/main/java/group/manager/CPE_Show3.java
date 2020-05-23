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
import android.widget.ListView;
import android.widget.TextView;

import group.manager.AdapterClasses.Adapter_CPE_Show3;
import group.manager.ModelClasses.RowItem_CPE_Show3;

public class CPE_Show3 extends Activity {

	ListView LV1;
	TextView txtHead;
	byte[] AppLogo;
	String ClubName, CPEResult;
	Context context = this;
	String Cpe_Year, Cpe_YearData, LTitle;
	List<RowItem_CPE_Show3> ListArrObj_CpeShow3;

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
		LTitle = menuIntent.getStringExtra("LTitle");

		Set_App_Logo_Title(); // Set App Logo and Title

		txtHead.setText(LTitle + " Attendance (" + Cpe_Year + ")");
		Typeface face=Typeface.createFromAsset(getAssets(), "calibri.ttf");
		txtHead.setTypeface(face);

		Display_ListData();
	}


	private void Display_ListData() {
		String[] Arr_List_Data = Cpe_YearData.split("##"); // List Wise Data
		String List_Data = "";
		if (LTitle.equals("SLA"))
			List_Data = Arr_List_Data[1].replace("L2:", "");
		else if (LTitle.equals("Certification Courses"))
			List_Data = Arr_List_Data[2].replace("L3:", "");
		else if (LTitle.equals("e-Learning Courses"))
			List_Data = Arr_List_Data[3].replace("L4:", "");

		String[] Arr_ListData_Values = List_Data.split("@@"); // List2 Data
		ListArrObj_CpeShow3 = new ArrayList<RowItem_CPE_Show3>();
		for (int i = 0; i < Arr_ListData_Values.length; i++) {
			String[] s1 = Arr_ListData_Values[i].trim().split("~");
			String LName = s1[0].trim();
			String LDate = s1[1].trim();
			String LTopic = s1[2].trim();
			String LHrs = s1[3].trim();
			RowItem_CPE_Show3 item = new RowItem_CPE_Show3(LName, LDate, LTopic, LHrs);
			ListArrObj_CpeShow3.add(item);
		}
		Adapter_CPE_Show3 adapter = new Adapter_CPE_Show3(context, R.layout.list_item_cpe_show3, ListArrObj_CpeShow3);
		LV1.setAdapter(adapter);
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
		Intent MainBtnIntent = new Intent(context, CPE_Show2.class);
		MainBtnIntent.putExtra("Clt_ClubName", ClubName);
		MainBtnIntent.putExtra("AppLogo", AppLogo);
		MainBtnIntent.putExtra("CPEResult", CPEResult);
		MainBtnIntent.putExtra("Cpe_Year", Cpe_Year);
		MainBtnIntent.putExtra("Cpe_YearData", Cpe_YearData);
		startActivity(MainBtnIntent);
		finish();
	}
}
