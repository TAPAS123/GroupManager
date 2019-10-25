package group.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

public class CPE_Program_1 extends Activity{

	ExpandableListView LV_Expandable;
	byte[] AppLogo;
	String ClubName,Str_user,Tab4Name,Cur_format_Date="",MTitle="";
	Context context=this;
	List<RowItem_CPE_Program_1> ListArrObj_CpeProg_1;
	ArrayList<Category> categories;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cpe_program_1);
		Intent menuIntent = getIntent();
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		Str_user =  menuIntent.getStringExtra("UserClubName");
		MTitle =  menuIntent.getStringExtra("MTitle");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		
		Tab4Name="C_"+Str_user+"_4"; // 4th Table Name
		
		TextView TvCpeTitle=(TextView) findViewById(R.id.tvCpeTitle);
		LV_Expandable = (ExpandableListView) findViewById(R.id.Lv);
		
		TvCpeTitle.setText(MTitle);//set Cpe title 
		
		Set_App_Logo_Title(); // Set App Logo and Title
		
        ///////////////////get current date////////////////////////////
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Cur_format_Date = ChkVal(df.format(c.getTime()));
        /////////////////////////////////////////////////////////////
         
		Display_ListData();// Set and Display Expandable List Data
		
		LV_Expandable.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener(){
			@Override
			public void onGroupCollapse(int groupPosition) {
				// TODO Auto-generated method stub
				//System.out.println("cp");
				ImageView img=(ImageView)findViewById(R.id.imageView1st);
				img.setImageResource(R.drawable.down);
			}
	    });
		    
		LV_Expandable.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener(){
			@Override
			public void onGroupExpand(int groupPosition) {
				// TODO Auto-generated method stub
				//System.out.println("ex");
				ImageView img=(ImageView)findViewById(R.id.imageView1st);
				img.setImageResource(R.drawable.up);
			}
	    });
		
		
		
		LV_Expandable.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent,View v, int groupPosition, int childPosition,long id) {
				// TODO Auto-generated method stub
				//System.out.println("2: "+groupPosition+"  @@:  "+childPosition);
				
				String CPE_Prog_Result=categories.get(groupPosition).child_cpe.get(childPosition).getLFull_Data();// Get Full Data 
				
	    	    Intent MainBtnIntent= new Intent(context,CPE_Program_2.class);
	        	MainBtnIntent.putExtra("Clt_ClubName", ClubName);
	       	    MainBtnIntent.putExtra("UserClubName", Str_user);
	       	    MainBtnIntent.putExtra("AppLogo", AppLogo);
  				MainBtnIntent.putExtra("CPE_Prog_Result",CPE_Prog_Result);
  				MainBtnIntent.putExtra("MTitle",MTitle);
  			    startActivity(MainBtnIntent);
  			    //finish();
			    return true;
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
		String[] ArrGrp={"Forth Coming Program","Past Program"};
		String Cond1=" AND DateTime(date1)>=DateTime('"+Cur_format_Date+"') Order By Date1_1 asc";
		String Cond2=" AND DateTime(date1)<DateTime('"+Cur_format_Date+"') Order By Date1_1 Desc";
		String[] ArrCond={Cond1,Cond2};
		
		categories = new ArrayList<Category>();
  	    for( int i = 0 ; i < ArrGrp.length ; ++i ){
  		   String s1=ArrGrp[i].toString().trim();
  		   String Cond=ArrCond[i].toString();
  		   Category cat = new Category(s1,"");
	       generateChildren(cat,Cond);//Set List Child
	       categories.add(cat);
  	    }
		
  	    Adapter_CPE_Program_1 adapter = new Adapter_CPE_Program_1(context,categories, LV_Expandable);
		LV_Expandable.setAdapter(adapter);
		LV_Expandable.expandGroup(0);
  	    
		///////////////////////////////////////////////////////////
		
		/*SQLiteDatabase dbase = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
	 	String sqlSearch = "Select Text1,Text2,Text3,Text4,Text5,Text6,Text7,Text8,Add1,Num1 from "+Tab4Name+" where Rtype='CPE' Order by Date1_1 Desc";
		Cursor cursorT = dbase.rawQuery(sqlSearch, null);
		if (cursorT.moveToFirst()) {
		  do {
			  String LProg_Name=ChkVal(cursorT.getString(0));
			  String LOrg_By=ChkVal(cursorT.getString(1));
			  String LVenue=ChkVal(cursorT.getString(2));
			  String LCPE_Hrs=ChkVal(cursorT.getString(3));
			  String LFees=ChkVal(cursorT.getString(4));
			  String LDate=ChkVal(cursorT.getString(5));
			  String LTime_From=ChkVal(cursorT.getString(6));
			  String LTime_To=ChkVal(cursorT.getString(7));
			  String LAdd=ChkVal(cursorT.getString(8));
			  String LNum1=ChkVal(cursorT.getString(9));
			  String LTime_FromTo=LTime_From+"-"+LTime_To;
			  if(LTime_FromTo.trim().equals("-"))
				  LTime_FromTo="";
			  
			  String LFull_Data=LProg_Name+"~"+LOrg_By+"~"+LVenue+"~"+LCPE_Hrs+"~"+LFees+"~"+LDate+"~"+LTime_FromTo+"~"+LAdd+"~"+LNum1;
			  item = new RowItem_CPE_Program_1(LProg_Name,LVenue,LDate,LTime_FromTo,LCPE_Hrs,LFull_Data);
	          ListArrObj_CpeProg_1.add(item);
		   } while (cursorT.moveToNext());
		}
		cursorT.close(); // close cursor
 	    dbase.close(); // Close Local DataBase
        Adapter_CPE_Program_1 adapter = new Adapter_CPE_Program_1(context,R.layout.list_item_cpe_program_1, ListArrObj_CpeProg_1);
        LV.setAdapter(adapter);*/
	}
	
	
	
	
	private void generateChildren(Category cat,String Qry_Cond) 
	{
	   try{
		 
		  SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		  String sqlSearch = "Select Text1,Text2,Text3,Text4,Text5,Text6,Text7,Text8,Add1,Num1,M_Id from "+Tab4Name+" where Rtype='CPE' "+Qry_Cond;
		 
		  Cursor cursorT = db.rawQuery(sqlSearch, null);
		  RowItem_CPE_Program_1 item;
		  if (cursorT.moveToFirst()) {
		    do {
			      String LProg_Name=ChkVal(cursorT.getString(0));
				  String LOrg_By=ChkVal(cursorT.getString(1));
				  String LVenue=ChkVal(cursorT.getString(2));
				  String LCPE_Hrs=ChkVal(cursorT.getString(3));
				  String LFees=ChkVal(cursorT.getString(4));
				  String LDate=ChkVal(cursorT.getString(5));
				  String LTime_From=ChkVal(cursorT.getString(6));
				  String LTime_To=ChkVal(cursorT.getString(7));
				  String LAdd=ChkVal(cursorT.getString(8));
				  String LNum1=ChkVal(cursorT.getString(9));
				  String MId=ChkVal(cursorT.getString(10));
				  String LTime_FromTo=LTime_From+"-"+LTime_To;
				  if(LTime_FromTo.trim().equals("-"))
					  LTime_FromTo="";
				  
				  String LFull_Data=LProg_Name+"~"+LOrg_By+"~"+LVenue+"~"+LCPE_Hrs+"~"+LFees+"~"+LDate+"~"+LTime_FromTo+"~"+LAdd+"~"+LNum1+"~"+MId;
				  item = new RowItem_CPE_Program_1(LProg_Name,LVenue,LDate,LTime_FromTo,LCPE_Hrs,LFull_Data);
			   
			      //Category cat1 = new Category(EventName+" :",timeF,EventVenue,EDesc_Num1_Text8,Num3,EventMID);
			      cat.child_cpe.add(item);
		    } while (cursorT.moveToNext());
	       }
	       cursorT.close();
	       db.close();
		}
		catch(Exception ex)
		{
		  String tt="";
		}
	}
	
	
	
	
	private String ChkVal(String Val)
	{
		if(Val==null){
			Val="";
		}
		return Val.trim();
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
