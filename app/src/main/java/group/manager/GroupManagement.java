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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class GroupManagement extends Activity {

	final Context context=this;
	byte[] AppLogo;
	String ClubName,Str_user,WResult="";
	AlertDialog ad;
	SQLiteDatabase db;
	String Log,Logid;
	ProgressDialog Progsdial;
	private boolean InternetPresent;
	WebServiceCall webcall;
	ArrayList<Product> ArrList_Product1;
	Adapter_GroupManager AdpGroup;
	ListView Lv1;
	Button btnAddGroup;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.additional_data);
        
        TextView TvHead=(TextView)findViewById(R.id.tvTt);
		Lv1=(ListView)findViewById(R.id.Lv1);
		btnAddGroup=(Button)findViewById(R.id.btnBack);
        
        Intent menuIntent = getIntent(); 
        Log =  menuIntent.getStringExtra("Clt_Log");
        Logid =  menuIntent.getStringExtra("Clt_LogID");
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		Str_user =  menuIntent.getStringExtra("UserClubName");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		
		webcall=new WebServiceCall();//Call a Webservice
		ad=new AlertDialog.Builder(this).create();
		
		Set_App_Logo_Title(); // Set App Logo and Title
		
		TvHead.setText("Groups");
		btnAddGroup.setText("Add New Group");
		
		Chkconnection chkconn=new Chkconnection();//Intialise Chkconnection Object
		InternetPresent =chkconn.isConnectingToInternet(context);
		if(InternetPresent==true){
			WebCall_GetAllGroup();// Get All Group 
		}
		else{
		   AlertDisplay("Connection Problem !","No Internet Connection !",true);
		}
		
		
		//Create Group
		btnAddGroup.setOnClickListener(new OnClickListener()
        { 
			@Override
			public void onClick(View arg0) {
				GotoCreate_Group("Add","");
			}
        });
		
		
		 //ListView Click Event
		Lv1.setOnItemClickListener(new OnItemClickListener() 
        {
	    	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
	    		
	    		String GrpName=ArrList_Product1.get(position).name.toString().trim();
	    		String MSIds=ArrList_Product1.get(position).MSIds.toString().trim();//Member Spouse Ids
	    		String GrpName_MemSpouseIds=GrpName+"#"+MSIds;
	    		
	    		if(GrpName.equals("ALL") || GrpName.equals("ALL Members") || GrpName.equals("ALL Spouses") || GrpName.equals("Governing Body"))
	    		{
	    			AlertDisplay("Default Group !","Can't Edit Default group  !",false);
	    		}
	    		else
	    		{
	    		    GotoCreate_Group("Edit",GrpName_MemSpouseIds);
	    		}
	    	}
	    });
		
	}
	
	
	 // Get All GroupName with Id ///////////
	 public void WebCall_GetAllGroup()
     {
		progressdial();
		Thread T1 = new Thread() {
		 @Override
		 public void run() {
            try
              {
		         WResult=webcall.GetAllGroup(Str_user);
		         
	     		 runOnUiThread(new Runnable()
		         {
	            	 public void run()
	            	 {
	            		 ArrList_Product1 =  new ArrayList<Product>();
	            		 if(WResult.contains("^"))
	    	             {
	    	        	   String[] SArr=WResult.split("#");
	    	        	   
	    	        	   for(int i=0;i<SArr.length;i++)
	    	        	   {
	    	        		   String[] Arr=SArr[i].replace("^", "#").split("#");
	    	        		   String GroupName=Arr[0].trim();
	    	        		   //int GroupId=Integer.parseInt(Arr[1].trim());// Get Group Id
	    	        		   String MemIds=Arr[2].trim();// Get Member Ids
	    	        		   String SpouseIds=Arr[3].trim();// Get Spouse Ids
	    	        		   String MSIds=MemIds+"#"+SpouseIds;
	    	        		   ArrList_Product1.add(new Product(GroupName,MSIds)); 
	    	        	   }
	    	        	   
	    	        	   if(ArrList_Product1.size()!=0){
		            			AdpGroup = new Adapter_GroupManager(context, ArrList_Product1);
		            			Lv1.setAdapter(AdpGroup);
		            	   } 
	    	             }
	            		 else if(WResult.contains("Error"))
	            		 {
	            			 AlertDisplay("Network Problem !","Some Problem in Network Connection, Try Later !",true);
	            		 }
	            		 
	            	 }
		        });
             }
	  		 catch (Exception e) {
	  			e.printStackTrace();
	  		 }
             Progsdial.dismiss();
           }
        };
        T1.start();
	}
	
	 
	 //Goto Add/Edit/Delete Group Activity
	 private void GotoCreate_Group(String SType,String GrpName_MemSpouseIds)
	 {
		 Intent iIntent= new Intent(getBaseContext(),Create_Group.class);
		 iIntent.putExtra("Clt_Log",Log);
		 iIntent.putExtra("Clt_LogID",Logid);
		 iIntent.putExtra("Clt_ClubName",ClubName);
		 iIntent.putExtra("UserClubName",Str_user);
		 iIntent.putExtra("AppLogo", AppLogo);
		 iIntent.putExtra("SType", SType);
		 iIntent.putExtra("GrpName_MSIds", GrpName_MemSpouseIds);
	     startActivity(iIntent);
	     finish();
	 }
	
	
	protected void progressdial()
    {
    	Progsdial = new ProgressDialog(this, R.style.MyTheme);
    	Progsdial.setMessage("Please Wait....");
    	Progsdial.setIndeterminate(true);
    	Progsdial.setCancelable(false);
    	Progsdial.getWindow().setGravity(Gravity.DISPLAY_CLIP_VERTICAL);
    	Progsdial.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    	Progsdial.show();
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
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
	   	if (keyCode == KeyEvent.KEYCODE_BACK) {
	   		backs();
	   	    return true;
	   	}
	   	return super.onKeyDown(keyCode, event);
	 }
	  
	 
	 public void backs(){
	    Intent MainBtnIntent= new Intent(context,UlilitiesList.class);
		MainBtnIntent.putExtra("AppLogo", AppLogo);
		MainBtnIntent.putExtra("CondChk", "2");
		startActivity(MainBtnIntent);
		finish(); 
	}
	 
	 private void AlertDisplay(String head,String body,final boolean GoBack){
	    	ad.setTitle( Html.fromHtml("<font color='#E3256B'>"+head+"</font>"));
	    	ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>"+body+"</font>"));
			ad.setButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	dialog.dismiss();
	            	if(GoBack)
	            	  backs();
	            }
	        });
	        ad.show();	
	}
}
