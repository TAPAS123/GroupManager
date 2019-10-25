package group.manager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class CPE_Show1 extends Activity{

	ListView LV;
	byte[] AppLogo;
	String ClubName,CPEResult;
	Context context=this;
	List<RowItem_CPE_Show1> ListArrObj_CpeShow1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cpe_show1);
		Intent menuIntent = getIntent();
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		CPEResult =  menuIntent.getStringExtra("CPEResult");
		
		LV = (ListView) findViewById(R.id.Lv);
		
		Set_App_Logo_Title(); // Set App Logo and Title
		
		Display_ListData();// Display List Data
		
		LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parentAdapter, View view, int position,long id) {
	        	RowItem_CPE_Show1 item=ListArrObj_CpeShow1.get(position);
	        	String Cpe_Year=item.getYear();
	        	String Cpe_YearData=item.getYearData();
	        	if(!Cpe_YearData.contains("Prob:Year"))
	        	{
	        	  Intent MainBtnIntent= new Intent(context,CPE_Show2.class);
    			  MainBtnIntent.putExtra("Clt_ClubName", ClubName);
  				  MainBtnIntent.putExtra("AppLogo", AppLogo);
  				  MainBtnIntent.putExtra("CPEResult",CPEResult);
  				  MainBtnIntent.putExtra("Cpe_Year",Cpe_Year);
  				  MainBtnIntent.putExtra("Cpe_YearData",Cpe_YearData);
  			      startActivity(MainBtnIntent);
  			      finish();
	        	}
	        }
	     });
		
	}
	
	private void Set_App_Logo_Title()
	 {
		 setTitle(ClubName); // Set Title
		 // Set App LOGO
		 if(AppLogo==null)
		 {
			 getActionBar().setIcon(R.drawable.ic_launcher);
		 }
		 else
		 {
			Bitmap bitmap = BitmapFactory.decodeByteArray(AppLogo,0, AppLogo.length);
			BitmapDrawable icon = new BitmapDrawable(getResources(),bitmap);
			getActionBar().setIcon(icon);
		 }
	 }
	
	
	private void Display_ListData()
	{
		String[] Arr_Year_Data=CPEResult.split("###"); // Year Wise Data
		ListArrObj_CpeShow1 = new ArrayList<RowItem_CPE_Show1>();
		String Year="";
        for(int i=0;i<Arr_Year_Data.length;i++)
        {
          if(i==0)
        	  Year="2018";
          else if(i==1)
        	  Year="2017";
          else if(i==2)
        	  Year="2016";
          else if(i==3)
        	  Year="2015";
          else if(i==4)
        	  Year="2014";
          else if(i==5)
        	  Year="2013";
          String YData=Arr_Year_Data[i].trim();
          int TotalHours=0;
          if(YData.contains("##"))
          {
        	  String[] Arr_List_Data=YData.split("##");
              TotalHours=GetTotalHours(Arr_List_Data[0],Year);
          }
          RowItem_CPE_Show1 item = new RowItem_CPE_Show1(Year,String.valueOf(TotalHours),YData);
          ListArrObj_CpeShow1.add(item);
        }
        Adapter_CPE_Show1 adapter = new Adapter_CPE_Show1(context,R.layout.list_item_cpe_show1, ListArrObj_CpeShow1);
        LV.setAdapter(adapter);
	}
	
	
	private int GetTotalHours(String List5Data,String Year)
	{
		int TotalHrs=0;
		if(List5Data.contains("~"))
		{
		  String[] s1=List5Data.split("~");
		  int S1=Integer.parseInt(s1[0].replace("L5:SLA:", "").trim());
		  int S2=Integer.parseInt(s1[1].replace("Certification Courses:", "").trim());
		  int S3=Integer.parseInt(s1[2].replace("e-Learning Courses:", "").trim());
		  //String[] tmp=s1[3].split(":");
		  int S4=0;//Integer.parseInt(tmp[1].trim());
		  int S5=Integer.parseInt(s1[4].replace("ULA:", ""));
		  TotalHrs=S1+S2+S3+S4+S5;
		}
		return TotalHrs;
	}
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
	   	if (keyCode == KeyEvent.KEYCODE_BACK) {
	   		backs();
	   	    return true;
	   	}
	   	return super.onKeyDown(keyCode, event);
	}
	
	
	// To go Back
	public void backs(){
	  Intent MainBtnIntent= new Intent(context,MenuPage.class);
	  MainBtnIntent.putExtra("AppLogo", AppLogo);
	  startActivity(MainBtnIntent);
	  finish();
   }
	
	
}
