package group.manager;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Directory_New_ListView extends Activity implements OnClickListener{
	
	String Log,Str_user,ClubName,logid,Table2Name,StrEdSrch,Table4Name,TableMiscName;
	//ImageView ImgSrchCrit;
	Intent menuIntent;
	AlertDialog ad;
	SQLiteDatabase db;
	Cursor cursorT;
	Dialog dialog;
	EditText ET_Search_Name, ET_Search_Mob,ET_Search_MemNo,ET_Search_Addr;
	ActionBar actionBar;
	LinearLayout llaydialog,llaysetting,llaySrchCrit,LLaySearchCriteriashow;
	private static final int MY_BUTTON = 9000;
	CheckBox cb;
	byte[] AppLogo;
	final Context context=this;
	String MemDir="Member";
	int CHKsrhcri=0;
	List<Spinner> allSps;
	String strSrchcrival="",StrCaptionvale="",StrTable="";
	Button BtnSrchcrit;
	Editor editorCrit;
	LinearLayout LLMain;
	List<EditText> allEds;
	ArrayList<String> listarr,TextVal;
	String Special_Dir_Condition;
	String Dir_Filter_Condition,CFrom;
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
			AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
			Special_Dir_Condition=menuIntent.getStringExtra("Special_Dir_Condition");//Special Directory Condition with DirName
			CFrom=menuIntent.getStringExtra("CFrom");//Comes From
			CCBYear=menuIntent.getStringExtra("CCBYear");////CCB Year Added 16-11-2018
			
	        Table2Name="C_"+Str_user+"_2";
			Table4Name="C_"+Str_user+"_4";
			TableMiscName="C_"+Str_user+"_MISC";
			
			allSps = new ArrayList<Spinner>();
			
			Set_App_Logo_Title(); // Set App Logo and Title
			
			Get_SharedPref_Values(); // Get Shared Pref Values of MemDir(Display Member/Spouse)
			
	        db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
	      
	        Add_DefaultFilter_Condition();//Add Default Filter if Have (Added on 10-07-2017)
	        Dir_Filter_Condition=DirFilter_Condition();//Directory Filter Condional Query
	        
	        CHKsrhcri=getSearchCriteria();
	        
	        ActionCall();//Action Bar Display
	       
	        FillFull_ListData(); // Fill Main Data Display   
	        
	 		db.close();//Close Connection
		    
		    
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
		
   
	private void DisplayAlert(String title,String msg){
		AlertDialog ad=new AlertDialog.Builder(this).create();
		ad.setTitle( Html.fromHtml("<font color='#E3256B'>"+title+"</font>"));
    	ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>"+msg+"</font>"));
		ad.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	BBack();
            }
        });
        ad.show();	
}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
	   	 if (keyCode == KeyEvent.KEYCODE_BACK) {
	   		BBack();
	   	    return true;
	   	 }
	   	return super.onKeyDown(keyCode, event);
	}
	
	//Back 
	private void BBack()
	{
		if(CFrom.equals("MENU")){
		  Intent MainBtnIntent= new Intent(getBaseContext(),MenuPage.class);
   		  MainBtnIntent.putExtra("AppLogo", AppLogo);
	      startActivity(MainBtnIntent);
	      finish();
		}
		else{
		  finish();
		}
	}

	
	
	public void FillFull_ListData() {
		// TODO Auto-generated method stub
		try{	
		 String sql="",Name="",txt1="",txt2="",Mob="";
		
		 if(Special_Dir_Condition.contains("A.M_Id"))
		 {
			 sql=Special_Dir_Condition.replace("SELECT A.M_Id","SELECT A.M_Id,A.M_Name,A.M_Mob,A.M_Pic");
		 }
		 else
		 {
		   if(MemDir.equals("Member")) 
 			  sql="select M_id,M_Name,M_Mob,M_Pic from "+Table2Name+" Where M_Name Is Not NULL "+Dir_Filter_Condition+Special_Dir_Condition+" Order by C4_LName,Upper(M_Name)";//member
		   else 
			  sql="select M_id,S_Name,S_Mob,S_Pic from "+Table2Name+" Where S_Name Is Not NULL AND LENGTH(S_Name)<>0 "+Dir_Filter_Condition+Special_Dir_Condition+" Order by Upper(S_Name)";//Spouse
		 }
		
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
		 if(rowItems.size()>0){
			 Adapter_Directory_New_ListView Adp1 = new Adapter_Directory_New_ListView(context,R.layout.directory_new_list_cell, rowItems);
	         LV1.setAdapter(Adp1);
		 }
		 else
		 {
		    DisplayAlert("Result!","No Record found");
		 }
		 
	  }catch(Exception ex){
   		 System.out.println(ex.getMessage());
	  }
   }
	
	
	

    public void filloremptyData(String str,LinearLayout ll,TextView txt){
	  if(str==null){
		ll.setVisibility(View.GONE);
	  }else if((str!=null)&&(str.trim().length()>0)){
		ll.setVisibility(View.VISIBLE);
		txt.setText(str);
	  }else{
		ll.setVisibility(View.GONE);
	  }
    }
	
    private String Chkval(String DVal)
	{
    	if((DVal==null)||(DVal.equalsIgnoreCase("null"))){
			DVal="";
		}
		return DVal.trim();
	}
	
	public void ActionCall(){
	    actionBar = getActionBar();
	    setTitle("Clubmanager");
        // add the custom view to the action bar
        actionBar.setCustomView(R.layout.search_layout);
       
        ImageView ImgVwSearch = (ImageView) actionBar.getCustomView().findViewById(R.id.imageSerchMenu);
        ImageView ImgSrchCrit = (ImageView) actionBar.getCustomView().findViewById(R.id.imagesearchcritri);
        ImageView ImgVwFilter= (ImageView) actionBar.getCustomView().findViewById(R.id.imageSetting);
       
        ImgVwSearch.setOnClickListener(new OnClickListener(){ 
            @Override 
              public void onClick(View arg0){
            	
            	ActionBarDisp_Alert(1);
            	 /*if(CHKsrhcri==1){
                 	ImgSrchCrit.setVisibility(View.VISIBLE);
                 }else{
                 	ImgSrchCrit.setVisibility(View.GONE);
                 }*/
            }
        });
        
        ImgVwFilter.setOnClickListener(new OnClickListener(){ 
            @Override 
              public void onClick(View arg0){
            	ActionBarDisp_Alert(2);
            }
        });
        
        ImgSrchCrit.setOnClickListener(new OnClickListener(){ 
            @Override 
              public void onClick(View arg0){
            	ActionBarDisp_Alert(3);
            }
        });
        
        
       actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM |ActionBar.DISPLAY_SHOW_HOME);
     }
	
	 public void ActionBarDisp_Alert(final int serchck) {
		 try{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			final FrameLayout frameView = new FrameLayout(context);
			builder.setView(frameView);
			final AlertDialog alertDialog = builder.create();
			LayoutInflater inflater = alertDialog.getLayoutInflater();
			
			View dialoglayout = inflater.inflate(R.layout.view_dialog_box, frameView);
			llaydialog= (LinearLayout) dialoglayout.findViewById(R.id.llViewbox);
			LinearLayout ll = new LinearLayout(this);
			llaydialog.addView(ll, new LinearLayout.LayoutParams(100, LayoutParams.WRAP_CONTENT));
			
			Button btnSearch= (Button) dialoglayout.findViewById(R.id.btnSearch);
			llaySrchCrit=(LinearLayout) dialoglayout.findViewById(R.id.linearLayoutSCrhcri);
			LinearLayout LLSettingMain = (LinearLayout) dialoglayout.findViewById(R.id.linearLayoutMM);
			
			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    WindowManager.LayoutParams wmlp = alertDialog.getWindow().getAttributes();
			wmlp.gravity = Gravity.TOP | Gravity.RIGHT;
			wmlp.x = 50;   //x position
			wmlp.y = 50;   //y position
			alertDialog.show(); 
			
			if(serchck==1){
				
				llaySrchCrit.setVisibility(View.GONE);
				
				String val="",Title="",fielddata="";
				allEds = new ArrayList<EditText>();
				TextVal= new ArrayList<String>();
				
				LinearLayout LL_Binocular = (LinearLayout) dialoglayout.findViewById(R.id.llaybinosrch);
				LinearLayout LL_add = (LinearLayout) dialoglayout.findViewById(R.id.addresssrch);
				LinearLayout LL_categ = (LinearLayout) dialoglayout.findViewById(R.id.catecontact);
				
				LL_Binocular.setVisibility(View.VISIBLE);
				LL_add.setVisibility(View.VISIBLE);
				LL_categ.setVisibility(View.GONE);
				
				ET_Search_Name = (EditText) dialoglayout.findViewById(R.id.etName);
				ET_Search_Mob = (EditText) dialoglayout.findViewById(R.id.etMobileBino);
				ET_Search_MemNo = (EditText) dialoglayout.findViewById(R.id.etMemberBino);
				ET_Search_Addr = (EditText) dialoglayout.findViewById(R.id.etAddBino);
				LLMain=(LinearLayout)dialoglayout.findViewById(R.id.LLMain);
				
				try{
					db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
					
					////Get MemNo textbox caption from table 4 (Added on 07-06-2017)
					String sql="Select Text1 from "+Table4Name+" Where Rtype='Memno_Cap'";
					cursorT = db.rawQuery(sql, null);
					if(cursorT.moveToFirst()){
						val=cursorT.getString(0);
						if(val==null)
							val="";
					}
					cursorT.close();
					
					if(val.length()>0)
						ET_Search_MemNo.setHint(val);
					///////////////////////////////////
					
					/////Additional Data search
					sql="Select Add1 from "+TableMiscName+" Where Rtype='Search_Add'";//"+Title.trim()+"
					cursorT = db.rawQuery(sql, null);
					if(cursorT.moveToFirst()){
						val=cursorT.getString(0);
						if(val==null)
							val="";
					}
					cursorT.close();
					db.close();
					if(val.contains("^")){
						 String [] temp=val.split("#");
						 for(int i=0;i<temp.length;i++){
							String s= temp[i].replace("^", "#")+" ";
							String []arr=s.split("#");
							Title=arr[0].toString();
							fielddata=arr[1].toString();
							LLMain.addView(NewView(i+10,Title,fielddata));
						 }
					}	
				}catch(Exception ex){
					System.out.println(ex.getMessage());
				}
				
			}else if(serchck==2){
				llaySrchCrit.setVisibility(View.GONE);
				LLSettingMain.setVisibility(View.VISIBLE);
				llaysetting = (LinearLayout) dialoglayout.findViewById(R.id.llayMenucheck);
				Button btnSaveFilter = (Button) dialoglayout.findViewById(R.id.btnSaveFilter);
				btnSaveFilter.setId(MY_BUTTON);
				btnSaveFilter.setOnClickListener(this);
				
				try{
					String sql="";
					if(Special_Dir_Condition.contains("A.M_Id"))
				    {
					   Special_Dir_Condition="";//Set Blank if Special_Dir_Condition contains a Query
				    }
					sql ="select distinct(Upper(oth)) from "+ Table2Name+" where oth is not null and  length(oth) <> 0 "+Special_Dir_Condition+" Order By Upper(oth)";
					db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 	
					cursorT = db.rawQuery(sql, null);
					int i=0;
					if(cursorT.getCount()==0){
						alertDialog.cancel();
						Toast.makeText(getBaseContext(), "No Setting Available", 0).show();
					}else{
						 if (cursorT.moveToFirst()) {
							   do {
								   cb = new CheckBox(this);
								   sql="";
								   sql ="select Text1 from "+ Table4Name +" Where rtype = 'RecShow' and text1 = '"+cursorT.getString(0)+"'";
								   Cursor cursorT2 = db.rawQuery(sql, null);
								   if(cursorT2.getCount()==0){
									   cb.setText(cursorT.getString(0));
								   }else{
									   cb.setText(cursorT.getString(0));
									   cb.setChecked(true);
								   }
								   cursorT2.close();
						           cb.setId(i+10);
						           llaysetting.addView(cb);
						    	   i++;
					    		 } while (cursorT.moveToNext());
					    	 }
					}
					cursorT.close();
					db.close();
				}catch(Exception ex){
					System.out.println(ex.toString());
				}
			}
			else if(serchck==3){
					String []sp=null;
					String caption="",value="",Tablename="";
					llaySrchCrit.setVisibility(View.VISIBLE);
					LLaySearchCriteriashow = (LinearLayout) dialoglayout.findViewById(R.id.llaySrchcriteria);
					BtnSrchcrit=(Button)dialoglayout.findViewById(R.id.butnSaveSechCrt);
					String [] tempsrch = strSrchcrival.split("#"); 
		            for(int i=0;i<tempsrch.length; i++){
			         	 String s= tempsrch[i].toString().replace("^", "#")+" ";
			             sp=s.trim().split("#");   
			             caption=sp[0].toString().trim();
			             value=sp[1].toString().trim();
			             Tablename=sp[2].toString().trim();
			             String[] Arr1=null;
			             try{
			 				String sql="";
			 				int j=1;
			 				sql ="select distinct("+value+") from "+"C_"+Str_user+"_"+Tablename+" Where "+value+" Is Not NULL AND LENGTH("+value+")<>0  order by "+value;
			 				db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 	
			 				cursorT = db.rawQuery(sql, null);
			 				int temp_size=cursorT.getCount();
			 				Arr1=new String[temp_size+1];
			 				Arr1[0]="";
		 					if (cursorT.moveToFirst()) {
		 						do {
		 							Arr1[j]=cursorT.getString(0);
					    		    j++;	
		 						} while (cursorT.moveToNext());
		 				    }	 
			 				cursorT.close();
			 				db.close();
			             }catch(Exception ex){
			            	 System.out.println(ex.getMessage());
			             }
			             
			             if(Arr1.length==0){
			            	 Arr1[0]=""; 
			             }
			             StrCaptionvale=StrCaptionvale+value+"#";
			             StrTable=StrTable+Tablename+"#";
			             LLaySearchCriteriashow.addView(NewView(i+1000,caption,Arr1));
		              }
		            //System.out.println(allSps.size());
		            
		            BtnSrchcrit.setOnClickListener(new OnClickListener(){ 
		                @Override 
		                public void onClick(View arg0){
		                	/*String StrF=" and M_id in (",StrM="",StrL=")",StrTotal="",StrQuery="";
		                	String []ttemp=StrCaptionvale.split("#");
		                	String []tbtemp=StrTable.split("#");
		                	for(int i=0; i < allSps.size(); i++){
		     	            	 String FieldData = allSps.get(i).getSelectedItem().toString().trim();
		     	            	 if(FieldData.length()!=0){
		     	            		StrQuery="select memid from c_"+Str_user+"_"+tbtemp[i]+" where "+ttemp[i]+" = '"+FieldData+"'";
		     	            		StrM=StrF+StrQuery+StrL;
		     	            		StrTotal=StrTotal+StrM;
		     	            		System.out.println("v "+StrTotal+" "+StrTotal.length());
		     	            	 } 
		                	}
		                	if(StrTotal.length()!=0){
		                		//StrTotal = StrTotal.substring(0, StrTotal.length()-1).trim();
		                		System.out.println("fi:: "+StrTotal+" "+StrTotal.length());
		                		 editorCrit = shrdcriteria.edit(); // Edit Shared Pref critria Records
		                		 editorCrit.putString("Critval", StrTotal);
		                		 editorCrit.commit();
		                		 callitself();
	     	            	 }else{
	     	            		Toast.makeText(context, "Slelect any field", 1).show();
	     	            	 }*/
		                }
		            });	
			}
			
			btnSearch.setOnClickListener(new OnClickListener(){ 
				@Override 
	            public void onClick(View arg0){
	            	    
	            	    String StrName=ET_Search_Name.getText().toString();
		            	String StrMob=ET_Search_Mob.getText().toString();
		            	String StrMemNo=ET_Search_MemNo.getText().toString();
		            	String StrAddr=ET_Search_Addr.getText().toString();
		            	
		            	listarr = new ArrayList<String>();
		            	for(int j=0;j<allEds.size();j++){
		            	   String FieldData = allEds.get(j).getText().toString();
		            	   String DValue = TextVal.get(j);
		            	   if((FieldData!=null)&&(FieldData.length()!=0)){
		            		   listarr.add(FieldData+"#@"+DValue); 
							}
		            	}
		            	int count=listarr.size();
		            	
		            	if((StrName.length()==0)&&(StrMob.length()==0)&&(StrMemNo.length()==0)&&(StrAddr.length()==0)&&(count==0)){
		            		Toast.makeText(getBaseContext(), "input atleast one field!", 0).show();
		            	}else{
		            		alertDialog.dismiss();
		            		//waiting();
		            		
		            		String query=StrName+"#"+StrMob+"#"+StrMemNo+"#"+StrAddr;
		            		menuIntent= new Intent(getBaseContext(),Directory_New_ListView_Search.class);
		            		menuIntent.putExtra("Clt_LogID",logid);
		            		menuIntent.putExtra("Clt_Log",Log);
		            		menuIntent.putExtra("Clt_ClubName",ClubName);
		            		menuIntent.putExtra("UserClubName",Str_user);
		            		menuIntent.putExtra("STRslct","2");
		            		//menuIntent.putExtra("StrCriteria",StrShrdCrival);
		            		menuIntent.putExtra("Qury",query);
		            		menuIntent.putExtra("AppLogo",AppLogo);
			        		menuIntent.putExtra("Dir_Filter_Condition",Dir_Filter_Condition);
			        		menuIntent.putExtra("Special_Dir_Condition",Special_Dir_Condition);
			        		menuIntent.putExtra("CCBYear",CCBYear);//CCB Year Added 16-11-2018
			        		
			        		if(count==0){
			        			menuIntent.putStringArrayListExtra("stock_list", null);
			        		}else{
			        			menuIntent.putStringArrayListExtra("stock_list", listarr);
			        		}
		                	startActivity(menuIntent);
		                	//finish();
		            		//Toast.makeText(getBaseContext(),Str_etmobsrch+"  "+Str_etmemNosrch+"  "+Str_etAddsrch, 1).show();
		            	}
	             }
	        });
		 }catch(Exception ex){
			 System.out.println("msg:: "+ex.getMessage());
		 }
	 }

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//boolean bl=false;
		String sql= null;
		String aa = null;
        switch (v.getId()) {
        case MY_BUTTON:
        	//if(aa==)
        	db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
    		sql ="delete from "+Table4Name+" where Rtype = 'RecShow'";	
			db.execSQL(sql);
			
			for(int i=0; i<llaysetting.getChildCount(); i++) {
                View nextChild = llaysetting.getChildAt(i);
				if(nextChild instanceof CheckBox) {
                  CheckBox check = (CheckBox) nextChild;
                  if (check.isChecked()) {
                    aa=  check.getText().toString();
                    sql = "insert into "+Table4Name+"(Rtype,Text1) values('RecShow','"+aa+"')";
                    db.execSQL(sql); 
                  }    
               }
	        } 
		   db.close();
		   Toast.makeText(getBaseContext(), "Filter Applied", 0).show();
		   callitself();
       }
	}
	
	 public void callitself() {
		 menuIntent= new Intent(getBaseContext(),Directory_New_ListView.class);
		 menuIntent.putExtra("Clt_LogID",logid);
		 menuIntent.putExtra("Clt_Log",Log);
		 menuIntent.putExtra("Clt_ClubName",ClubName);
		 menuIntent.putExtra("UserClubName",Str_user);
		 menuIntent.putExtra("AppLogo",AppLogo);
		 menuIntent.putExtra("Special_Dir_Condition",Special_Dir_Condition);
		 menuIntent.putExtra("CFrom",CFrom);//Comes From
		 menuIntent.putExtra("CCBYear",CCBYear);//CCB Year Added 16-11-2018
		 startActivity(menuIntent);
    	 finish();
	 }
	 
	 
	 private LinearLayout NewView(int id,String TvTitle,String DValue)
		{
			LinearLayout L1=new LinearLayout(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(2,3,2,18);
			L1.setLayoutParams(params);
			L1.setBackgroundResource(R.drawable.edit_text_transparent_new);
			
			L1.setId(id);
			L1.setOrientation(android.widget.LinearLayout.HORIZONTAL);
			
			//Add Textview
			/*TextView tv=new TextView(this);
			tv.setTextSize(19);
			tv.setText(TvTitle);
			L1.addView(tv);*/
			
			L1.addView(editText(id,TvTitle,DValue));
			
			return L1;
		}
		
	 private EditText editText(int id,String hint,final String DValue) {
		final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		final EditText ET = new EditText(this);
		allEds.add(ET);// Add EditText in Edittext List
		TextVal.add(DValue);
		ET.setLayoutParams(lparams);
        ET.setId(id);
        ET.setHint(hint);
        ET.setText("");
        ET.setBackgroundColor(Color.WHITE);
        
        return ET;
     }
	
		
  	 // This function is used to get Directory Filter Condition 
	 public String DirFilter_Condition() {
		 String Result="";
		 String sqlSearch = "SELECT Text1 FROM "+Table4Name+" Where Rtype = 'RecShow'";
		 cursorT = db.rawQuery(sqlSearch, null);
		 int Rcount=cursorT.getCount();
		 if(Rcount!=0){
	    	   if (cursorT.moveToFirst()) {
				   do {
					   Result=Result+"'"+cursorT.getString(0)+"'"+",";
		    		 } while (cursorT.moveToNext());
		       }
	    	    if (Result.endsWith(",")) {
	    		   Result = Result.substring(0, Result.length() - 1);
	    		}
	    	    Result=" AND Oth in ("+Result+")";
		 }
		 cursorT.close(); 
	     return Result;
	 }
	 
	 
	 
	 
	// This function is used to get Default Filter  (Added on 10-07-2017)
	 public void Add_DefaultFilter_Condition() {
		 String DFilterColumnName="";
		 String sqlSearch = "SELECT Text1 FROM "+Table4Name+" Where Rtype = 'D_Filter'";
		 cursorT = db.rawQuery(sqlSearch, null);
		 int Rcount=cursorT.getCount();
		 if(Rcount!=0){
	    	   if (cursorT.moveToFirst()) {
				  DFilterColumnName=Chkval(cursorT.getString(0));
		       }
		 }
		 cursorT.close(); 
		 
		 if(DFilterColumnName.length()>0)
		 {
			 String ColVal="";
			 sqlSearch = "SELECT "+DFilterColumnName+" FROM "+Table2Name+" Where M_id="+logid;
			 cursorT = db.rawQuery(sqlSearch, null);
			 Rcount=cursorT.getCount();
			 if(Rcount!=0){
		    	   if (cursorT.moveToFirst()) {
		    		   ColVal=Chkval(cursorT.getString(0));
			       }
			 }
			 cursorT.close(); 
			 
			 if(ColVal.length()>0)
			 {
			   sqlSearch = "Insert into "+Table4Name+"(Rtype,Text1) values('RecShow','"+ColVal+"')";
               db.execSQL(sqlSearch); 
			 }
		 }
	 }
	 
	 
	 
	 public int getSearchCriteria() {
		 int returnval=0;
		 String sqlSearch = "SELECT Text2 FROM "+Table4Name+" Where Rtype='Sea_Cri'";
		   cursorT = db.rawQuery(sqlSearch, null);
		    int Sc_count=cursorT.getCount();
		     if(Sc_count>0){
		    	 returnval=1;
		    	 if (cursorT.moveToFirst()) {
				   do {
					   strSrchcrival=cursorT.getString(0);
			    	  } while (cursorT.moveToNext());
			     }
		     }else{
		    	 returnval=0;
		     }
		    cursorT.close(); 
			return returnval;
	 }
	 
	 
	 private LinearLayout NewView(int id,String TvTitle,String[] DValue)
		{
			LinearLayout L1=new LinearLayout(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(5,5,5,5);
			L1.setLayoutParams(params);
			L1.setId(id);
			L1.setOrientation(android.widget.LinearLayout.HORIZONTAL);
			
			//Add Textview
			TextView tv=new TextView(this);
			tv.setTextSize(18);
			tv.setText(TvTitle);
			tv.setTextColor(Color.BLUE);
			tv.setTypeface(Typeface.DEFAULT_BOLD);
			L1.addView(tv);
			L1.addView(spinneradd(id,TvTitle,DValue));
			return L1;
		}
	 
	 private Spinner spinneradd(int id,String hint,String[] DValue) {
			final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			Spinner SP = new Spinner(this);
			allSps.add(SP);// Add EditText in Edittext List
			SP.setLayoutParams(lparams);
	        SP.setId(id);
	        SP.setLeft(5);
	        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item, DValue);
	        SP.setAdapter(spinnerArrayAdapter);
	        return SP;
	    }
	 
	 /*public void waiting(){
		 final ProgressDialog progressDialog = ProgressDialog.show(context, "",  "Loading......");
         progressDialog.setCancelable(false);
         new Thread(new Runnable() {
             @Override
             public void run() {
                 try {
                     //Do some stuff that take some time...
                     Thread.sleep(3000); // Let's wait for some time
                 } catch (Exception e) {
                     System.out.println(e.getMessage()); 
                 }
                 progressDialog.dismiss();
             }
         }).start();
	 }*/
}
