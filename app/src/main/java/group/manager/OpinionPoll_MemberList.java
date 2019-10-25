package group.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class OpinionPoll_MemberList extends Activity {

    ListView Lv1;
    TextView tvtitle;
    String MemMids,SpouseMids,Head,ClientId,ClubName,LogId,MTitle,CFrom;
    List<RowEnvt> rowItems;
    RowEnvt item;
    byte[] AppLogo;
    int Mid=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinionpoll_member_correct_ans);
        
        tvtitle = (TextView) findViewById(R.id.tvtitle) ;
        Lv1 = (ListView) findViewById(R.id.lvmembercorrectAns);

        Intent intent = getIntent();
        String Mids =  intent.getStringExtra("Mids");
        Head = intent.getStringExtra("Head");
        MTitle =  intent.getStringExtra("MTitle");
        AppLogo =  intent.getByteArrayExtra("AppLogo");
        Mid = intent.getIntExtra("Mid",0);
        CFrom =  intent.getStringExtra("CFrom");
        
		Get_SharedPref_Values();//Get Shared Pref Values
		
		Set_App_Logo_Title(); // Set App Logo and Title
		
        String[] Arr1=Mids.split("#") ;
        
        MemMids=Arr1[0].trim();//Members Mids
        SpouseMids=Arr1[1].trim();//Spouse Mids

        tvtitle.setText(Head);  //set heading
        
        int Sno=1;
        rowItems = new ArrayList<RowEnvt>();

        String TotalMids=MemMids.trim()+","+SpouseMids.trim();
        
        SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        String qry = "Select M_ID,M_Name,S_Name from C_" + ClientId + "_2  Where M_ID in ("+TotalMids+") order by M_Name";
        Cursor cursor = db.rawQuery(qry, null);
        
        while (cursor.moveToNext()) {
        	int MID=cursor.getInt(0);
		    String MName=ChkVal(cursor.getString(1));//Member Name
		    String SName=ChkVal(cursor.getString(2)); //Spouse Name
		    
		    boolean isMember=false,isSpouse=false;
		    
		    String[] Arr_Mids_Member= MemMids.split(",");
		    String[] Arr_Mids_Spouse= SpouseMids.split(",");
		       
		    isMember=ChkMemberSpouse(MID,Arr_Mids_Member);// check Mid in Member mids Array
		    isSpouse=ChkMemberSpouse(MID,Arr_Mids_Spouse);// check Mid in Spouse mids Array
		    
		    // Add Member Details In List
		    if(isMember) {
		    	item = new RowEnvt(MName,Sno+"","","");
		        rowItems.add(item);
		        Sno++;
		    }
		      
		    //Add Spouse Details In List
		    if(isSpouse){
		    	item = new RowEnvt(SName,Sno+"","","");
		    	rowItems.add(item);
		    	Sno++;
		    }
		    
        }
        cursor.close();
        db.close();
       
        Lv1.setAdapter(new Adapter_OpinionPoll_MemberList(this,R.layout.list_opinionpoll_memberlist,rowItems));
    }
    
    
    
    
  //Get Data from Saved Shared Preference
  	private void Get_SharedPref_Values()
  	{
  		SharedPreferences ShPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
  		 
  		  if (ShPref.contains("clientid"))
  	      {
  			ClientId=ShPref.getString("clientid", "");
  	      }
  		  if (ShPref.contains("cltid"))
	      {
  			  LogId=ShPref.getString("cltid", "");
          } 
  	      if (ShPref.contains("clubname"))
  	      {
  	    	ClubName=ShPref.getString("clubname", "");
          } 
  	}
    
    
    
    /// Check Mid in Member/Spouse Mids Array
  	private boolean ChkMemberSpouse(int MID,String[] Arr)
  	{
  		boolean HasMid=false;
  	    if(Arr!=null)
  	    {
  	    	int ChkMid=0;
              for(int i=0;i<Arr.length;i++)
              {
      	      ChkMid=Integer.parseInt(Arr[i].trim());
      	      if(ChkMid==MID)
      	      {
      	    	HasMid=true;
      		    break;
      	      }
             }
  	    }
  	    return HasMid;
  	}
  	
  	
  	//call function for initialise blank if null is there
   	private String ChkVal(String DVal)
  	{
  		if((DVal==null)||(DVal.equalsIgnoreCase("null"))){
  			DVal="";
  		}
  		return DVal.trim();
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
   
   
   
   public void backs()
   {
	   Intent intent=null;
	   if(CFrom.equals("1"))
		   intent = new Intent(this,Quiz_ScoreSheet.class);
	   else if(CFrom.equals("2"))
		   intent = new Intent(this,Quiz_Summary.class);
	   //else if(CFrom.equals("3"))
		 //  intent = new Intent(this,OpinionPoll_ScoreSheet.class);
		  
	   if(!CFrom.equals("3"))
	   {
		 intent.putExtra("Mid", Mid);
	   	 intent.putExtra("MTitle",MTitle);
	   	 intent.putExtra("Clt_LogID",LogId);
	   	 intent.putExtra("Clt_ClubName",ClubName);
         intent.putExtra("UserClubName",ClientId);
         intent.putExtra("AppLogo", AppLogo);
	     startActivity(intent);
	   }
	   finish();
   }
	  
}
