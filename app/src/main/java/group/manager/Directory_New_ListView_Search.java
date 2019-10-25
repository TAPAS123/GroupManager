package group.manager;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Directory_New_ListView_Search extends Activity {
	String Str_user,Str_main,Log,ClubName,logid,sqlSearch="",Table2Name,Table4Name,TableMiscName,StrEdSrch,Query,SelectStrquey,STRFinalQury="";
	Intent menuIntent;
	SQLiteDatabase db;
	Cursor cursorT;
	Dialog dialog;
	byte[] AppLogo;
	final Context context=this;
	String MemDir="Member",JoinFinalqry="";
	ArrayList<String> stock_list;
	String Dir_Filter_Condition,Special_Dir_Condition;
	ListView LV1;
	List<RowEnvt> rowItems;
	String CCBYear="";//CCB Year Added 16-11-2018
	
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.directory_new_listview);
	        LV1 = (ListView)findViewById(R.id.Lv1);
	        
			menuIntent = getIntent(); 
			Log =  menuIntent.getStringExtra("Clt_Log");
			logid =  menuIntent.getStringExtra("Clt_LogID");
			ClubName =  menuIntent.getStringExtra("Clt_ClubName");
			Str_user =  menuIntent.getStringExtra("UserClubName");
			Query =  menuIntent.getStringExtra("Qury");
			//StrShrdCrival =  menuIntent.getStringExtra("StrCriteria");
			SelectStrquey =  menuIntent.getStringExtra("STRslct");
			AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
			Dir_Filter_Condition =  menuIntent.getStringExtra("Dir_Filter_Condition");//Directory Filter Condition
			Special_Dir_Condition=menuIntent.getStringExtra("Special_Dir_Condition");//Special Directory Condition with DirName
			CCBYear=menuIntent.getStringExtra("CCBYear");////CCB Year Added 16-11-2018
			
			stock_list = new ArrayList<String>();
			stock_list = menuIntent.getStringArrayListExtra("stock_list");
			
			Table2Name="C_"+Str_user+"_2";
	        Table4Name="C_"+Str_user+"_4";
	        TableMiscName="C_"+Str_user+"_MISC";
	        
			Set_App_Logo_Title(); // Set App Logo and Title
			
			Get_SharedPref_Values(); // Get Shared Pref Values of MemDir(Display Member/Spouse)
			
			boolean Flag=false;
			if(stock_list!=null){
				Flag=true;
				System.out.println("size:::T "+stock_list.size());
				for(int j=0;j<stock_list.size();j++){
					String val =stock_list.get(j).toString();
					String [] tt=val.split("#@");
					JoinFinalqry=JoinFinalqry+" Upper(cm."+tt[1].trim()+") like '%"+tt[0].trim().toUpperCase()+"%' And";
				}
				if(JoinFinalqry.length()!=0){
					JoinFinalqry=JoinFinalqry.substring(0, JoinFinalqry.length()-3);
				}
				System.out.println(JoinFinalqry);
			}
			
			/////////////////////////////////////////////////////	
	        if(Query.length()==0){
	           Toast.makeText(getBaseContext(), Query.length()+"  null ....", 1).show();
       	 	}else{
	       	   if(SelectStrquey.equals("1")){	
	       	 	  /*String LikeClause="",WhClause,OrderByClause;
	       	 	  String[] AArr=quey.split(" "); // Split with Space in name(quey)

	       		  if(MemDir.equals("Member"))
	       		  {
	       			 OrderByClause=" Order by c2.M_Name";// Order By Clause
	       			 
	       			 LikeClause="c2.M_Name like '%"+AArr[0].trim()+"%'";
	        		 for(int i=1;i<AArr.length;i++)
	        		 {
	        			LikeClause=LikeClause+" AND c2.M_Name like '%"+AArr[i].trim()+"%'";
	        		 }
	       		  }
	       		  else
	       		  {
	       			 OrderByClause=" Order by c2.S_Name"; // Order By Clause
	       			 
	       			 LikeClause="c2.S_Name like '%"+AArr[0].trim()+"%'";
	        		 for(int i=1;i<AArr.length;i++)
	        		 {
	        			LikeClause=LikeClause+" AND c2.S_Name like '%"+AArr[i].trim()+"%'";
	        		 }
	       		  }
	       		   
	       		  if(StrSqlRec.length()!=0){
	       			WhClause=" Where "+LikeClause+" AND "+StrSqlRec+StrShrdCrival+OrderByClause;
	       	 	  }else{
	       	 		WhClause=" Where "+LikeClause+StrShrdCrival+OrderByClause;	
	       	 	  }
	       		  
	       		sqlSearch="select M_id from "+Table2Name+WhClause;// Select Query
	       		//String pp="";*/
	       		
       	 	   }else if(SelectStrquey.equals("2")){	
       	 		    Query=Query.replace("#", "#")+" "; 
		       	 	String[] Arr1=Query.split("#");
		       	    String Name=Arr1[0].toString().trim();//Name
		       	    String Mob=Arr1[1].toString().trim();//Mobile OR Landline
	  				String MemNo=Arr1[2].toString().trim();//MemN0
	  				String Addr=Arr1[3].toString().trim();//Address
		  				
	  			    //Make Query For Name
	  				if(Name.length()!=0){
		       	 	  String[] AArr=Name.split(" "); // Split with Space in name
		       		  if(MemDir.equals("Member")){
		       			STRFinalQury="c2.M_Name like '%"+AArr[0].trim()+"%'";
		        	    for(int i=1;i<AArr.length;i++)
		        	    {
		        		   STRFinalQury=STRFinalQury+" AND c2.M_Name like '%"+AArr[i].trim()+"%'";
		        	    }
		       		  }
		       		  else{
		       			STRFinalQury="c2.S_Name like '%"+AArr[0].trim()+"%'";
		        	    for(int i=1;i<AArr.length;i++)
		        	    {
		        		   STRFinalQury=STRFinalQury+" AND c2.S_Name like '%"+AArr[i].trim()+"%'";
		        	    }
		       		  }
	  				}
		       		   
	  			    //Make Query For Mobile	/ Landline  				
		  			if(Mob.length()!=0){
		  				if(STRFinalQury.length()!=0){
			  				if(MemDir.equals("Member"))
					  		  STRFinalQury=STRFinalQury+" AND (c2.M_Mob like '%"+Mob+"%' OR  c2.M_SndMob like '%"+Mob+"%' OR  c2.M_Land1 like '%"+Mob+"%' OR  c2.M_Land2 like '%"+Mob+"%') ";
					  		else
					  		  STRFinalQury=STRFinalQury+" AND c2.S_Mob like '%"+Mob+"%'";
			  			}else{
			  				if(MemDir.equals("Member"))
				  			  STRFinalQury="(c2.M_Mob like '%"+Mob+"%' OR  c2.M_SndMob like '%"+Mob+"%' OR  c2.M_Land1 like '%"+Mob+"%' OR  c2.M_Land2 like '%"+Mob+"%') ";
				  			else
				  		      STRFinalQury="c2.S_Mob like '%"+Mob+"%'";
			  			}
		  			}
		  			
		  		    //Make Query For MemNo
		  			if(MemNo.length()!=0 && MemDir.equals("Member")){
		  				if(STRFinalQury.length()!=0){
		  				  STRFinalQury=STRFinalQury+" AND  c2.MemNo like '%"+MemNo+"%'";	
		  				}else{
		  				  STRFinalQury="c2.MemNo like '%"+MemNo+"%'";
		  				}
		       	 	}
		  			
		  		    //Make Query For Addr
		  			if(Addr.length()!=0){	
		  				if(STRFinalQury.length()!=0){
		  				  STRFinalQury=STRFinalQury+" AND  (c2.M_Add1  like '%"+Addr+"%' OR  c2.M_Add2  like '%"+Addr+"%'  OR  c2.M_Add3  like '%"+Addr+"%' OR c2.M_City like '%"+Addr+"%')";	
		  				}else{
		  				  STRFinalQury="(c2.M_Add1  like '%"+Addr+"%' OR  c2.M_Add2  like '%"+Addr+"%'  OR  c2.M_Add3  like '%"+Addr+"%' OR c2.M_City like '%"+Addr+"%')";
		  				}
		       	 	}
		  			
	  				if(Flag==false)
	  				{
	  					/////20-02-2017 Added Special_Dir_Condition Qry Condition
	  					if(Special_Dir_Condition.contains("A.M_Id"))
	  					{
	  						STRFinalQury=" AND "+STRFinalQury;
	  						STRFinalQury=STRFinalQury.replace("c2.", "A.");
	  						sqlSearch=Special_Dir_Condition.replace("ORDER BY B.num2",STRFinalQury);
	  					}
	  					else{
	  						STRFinalQury=" Where "+STRFinalQury;
	  					}
	  					
	  				}else
	  				{
	  					if(STRFinalQury.length()>1){
	  						STRFinalQury=" join "+TableMiscName+"  cm on c2.M_id=cm.MemId Where "+JoinFinalqry+" And "+STRFinalQury;
	  					}else{
	  						STRFinalQury=" join "+TableMiscName+"  cm on c2.M_id=cm.MemId Where "+JoinFinalqry;
	  					}
	  				}
	  				
	  				if(sqlSearch.length()==0)
	  				{
		  			   //Make Final Search Query
		  			   if(MemDir.equals("Member"))
		  				sqlSearch="select c2.M_id  from "+Table2Name+" c2" +STRFinalQury+Dir_Filter_Condition+Special_Dir_Condition+" ";
		  		       else
		  				sqlSearch="select c2.M_id  from "+Table2Name+"  c2" +STRFinalQury+Dir_Filter_Condition+Special_Dir_Condition+" ";
		               ///////////////////////////////
	  				}
		  			
	       	 	 }
	       	     String tt=sqlSearch;
	       	     System.out.println(sqlSearch);
	       	     //String tt=sqlSearch;
	       	     
	       	     String Mids="0";
	       	     db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);//Open Connection
	       	     cursorT = db.rawQuery(sqlSearch, null);
	       	     if (cursorT.moveToFirst()) {
	     		   do {
	     			   Mids=Mids+","+cursorT.getInt(0);
	     		   } while (cursorT.moveToNext());
	     	     }
	     	     cursorT.close();
	     	     
	     	     if(Mids.equals("0")){
	     			DisplayAlert("Result!","No Record found");
	     	     }
	     	     else{
	     	        FillFull_ListData(Mids);// Fill Main Data Display   
	     	     }
	       	     
	       	     db.close();//Close Connection
       	 	  }
	       	 	
	        
	        
	      //ListView Click Event
		  LV1.setOnItemClickListener(new OnItemClickListener() 
	      {
		    	 public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		    	    int MID=rowItems.get(position).getMid();
		    	    
		    	    String Special_Dir_Condition_str=" AND M_Id in ("+MID+")";
		    	    
		    	    menuIntent= new Intent(getBaseContext(),SwipeScreen.class);
		   		    menuIntent.putExtra("Clt_LogID",logid);
		   		    menuIntent.putExtra("Clt_Log",Log);
		   		    menuIntent.putExtra("Clt_ClubName",ClubName);
		   		    menuIntent.putExtra("UserClubName",Str_user);
		   		    menuIntent.putExtra("AppLogo",AppLogo);
		   		    menuIntent.putExtra("Special_Dir_Condition",Special_Dir_Condition_str);
		   		    menuIntent.putExtra("CFrom","DIR_LIST");//Comes From
		   		    menuIntent.putExtra("CCBYear",CCBYear);//CCB Year Added 16-11-2018
		   		    startActivity(menuIntent);
		       	    //finish();
		    	  }
		  });
	}
		
		
    //Get Shared Pref Values
	private void Get_SharedPref_Values() {
	   SharedPreferences sharedpreferences = context.getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
		if (sharedpreferences.contains("MemDir"))
		{
		   MemDir=sharedpreferences.getString("MemDir", "");
		}
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
	   		back();
	   	    return true;
	   	 }
	   	return super.onKeyDown(keyCode, event);
	 }
	
	
	public void back(){
    	finish();
	}


	
	public void FillFull_ListData(String Mids) {
		// TODO Auto-generated method stub
	  try{
		 String sql="",Name="",txt1="",txt2="",Mob="";
 		 
 		 if(MemDir.equals("Member")) 
			 sql="select M_id,M_Name,M_Mob,M_Pic from "+Table2Name+" where M_id in ("+Mids+") Order by C4_LName,Upper(M_Name)" ;//Member
		 else 
			 sql="select M_id,S_Name,S_Mob,S_Pic  from "+Table2Name+" where M_id in ("+Mids+") Order by Upper(S_Name)" ;//Spouse
 		 
		 cursorT = db.rawQuery(sql, null);
		 
		 rowItems = new ArrayList<RowEnvt>();
     	 RowEnvt item;
		 
		 while(cursorT.moveToNext()){
			int MId=cursorT.getInt(0);
			Name=Chkval(cursorT.getString(1));
			Mob=Chkval(cursorT.getString(2));
			byte[] imgP=cursorT.getBlob(3);
			item = new RowEnvt(MId,Name,txt1,txt2,Mob,imgP);	
 	        rowItems.add(item);
		 }
		 
		 cursorT.close();
		 
		 Adapter_Directory_New_ListView Adp1 = new Adapter_Directory_New_ListView(context,R.layout.directory_new_list_cell, rowItems);
         LV1.setAdapter(Adp1);
	    	
	   }catch(Exception ex){
   		 System.out.println(ex.getMessage());
   	   }
	}
	
	
	
	private void DisplayAlert(String title,String msg){
		AlertDialog ad=new AlertDialog.Builder(this).create();
		ad.setTitle( Html.fromHtml("<font color='#E3256B'>"+title+"</font>"));
    	ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>"+msg+"</font>"));
		ad.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	back();
            }
        });
        ad.show();	
    }
	
	
    
    private String Chkval(String DVal)
	{
    	if((DVal==null)||(DVal.equalsIgnoreCase("null"))){
			DVal="";
		}
		return DVal.trim();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	  switch (item.getItemId()) {
    	    // action with ID action_back was selected
    	    case R.id.action_back:
    	    	back();
    	        break;
    	    default:
    	        break;
    	    }
    	return true;
    }  
}
