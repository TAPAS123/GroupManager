package group.manager;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Global_Search extends Activity{

	EditText txtMemNo,txtPwd;
	String MemNo,Pwd;
	Context context=this;
	byte[] AppLogo;
	String Log,ClubName,logid,Str_user,TableMiscName,Table4Name,Additional_Data,Additional_Data2;
	EditText ET_Search_Name,ET_Search_Mob,ET_Search_MemNo,ET_Search_Addr;
	List<EditText> allEds;
	ArrayList<String> listarr,TextVal;
	String New_Op_Title="";
	String MemDir_View="1";
	String CCBYear="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_dialog_box);
		
		LinearLayout LL_Binocular = (LinearLayout) findViewById(R.id.llaybinosrch);
		LinearLayout LL_add = (LinearLayout) findViewById(R.id.addresssrch);
		LinearLayout LL_categ = (LinearLayout) findViewById(R.id.catecontact);
		
		ET_Search_Name = (EditText) findViewById(R.id.etName);
		ET_Search_Mob = (EditText) findViewById(R.id.etMobileBino);
	    ET_Search_MemNo = (EditText) findViewById(R.id.etMemberBino);
		ET_Search_Addr = (EditText) findViewById(R.id.etAddBino);
		LinearLayout LLMain=(LinearLayout)findViewById(R.id.LLMain);
		Button btnSearch= (Button) findViewById(R.id.btnSearch);
		
		LL_Binocular.setVisibility(View.VISIBLE);
		LL_add.setVisibility(View.VISIBLE);
		LL_categ.setVisibility(View.GONE);
		
		Intent menuIntent = getIntent();
		Log =  menuIntent.getStringExtra("Clt_Log");
		logid =  menuIntent.getStringExtra("Clt_LogID");
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		Str_user =  menuIntent.getStringExtra("UserClubName");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		CCBYear=menuIntent.getStringExtra("CCBYear");////CCB Year Added 16-11-2018
		
		TableMiscName="C_"+Str_user+"_MISC";
		Table4Name="C_"+Str_user+"_4";
		
		Set_App_Logo_Title(); // Set App Logo and Title
		Get_SharedPref_Values();
		
		String val="",Title="",fielddata="";
		allEds = new ArrayList<EditText>();
		TextVal= new ArrayList<String>();
		
		try{
			SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);//Conn Open
			
			GetAdditionalData(db);//Get Additional Data
			
			Get_New_Op(db);////Added in 10-02-2017
			
			
		     ////Get MemNo textbox caption from table 4 (Added on 07-06-2017)
			String sql="Select Text1 from "+Table4Name+" Where Rtype='Memno_Cap'";
			Cursor cursorT = db.rawQuery(sql, null);
			if(cursorT.moveToFirst()){
				val=cursorT.getString(0);
				if(val==null)
					val="";
			}
			cursorT.close();
			
			if(val.length()>0)
				ET_Search_MemNo.setHint(val);
			///////////////////////////////////
			
			sql="Select Add1 from "+TableMiscName+" Where Rtype='Search_Add'";//"+Title.trim()+"
			cursorT = db.rawQuery(sql, null);
			if(cursorT.moveToFirst()){
				val=cursorT.getString(0);
				if(val==null)
					val="";
			}
			cursorT.close();
			db.close();///Connection Close
			
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
		
		
		//// BtnGlobal Search
		btnSearch.setOnClickListener(new OnClickListener()
        { 
			@Override
			public void onClick(View arg0) {
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
            		//waiting();
            		
            		Intent menuIntent=null;
            		String query=StrName+"#"+StrMob+"#"+StrMemNo+"#"+StrAddr;
            		if(MemDir_View.equals("1"))
            	    	menuIntent= new Intent(getBaseContext(),SearchDisplay.class); // Directory Search DataView
            	    else
            	    	menuIntent= new Intent(getBaseContext(),Directory_New_ListView_Search.class); // Directory Search ListView
            		
            		menuIntent.putExtra("Clt_LogID",logid);
            		menuIntent.putExtra("Clt_Log",Log);
            		menuIntent.putExtra("Clt_ClubName",ClubName);
            		menuIntent.putExtra("UserClubName",Str_user);
            		menuIntent.putExtra("STRslct","2");
            		//menuIntent.putExtra("StrCriteria",StrShrdCrival);
            		menuIntent.putExtra("Qury",query);
            		menuIntent.putExtra("AppLogo",AppLogo);
            		menuIntent.putExtra("AddData1",Additional_Data);
	        		menuIntent.putExtra("AddData2",Additional_Data2);
	        		menuIntent.putExtra("Dir_Filter_Condition","");
	        		menuIntent.putExtra("Special_Dir_Condition","");
	        		menuIntent.putExtra("New_Op_Title",New_Op_Title);//Added in 10-02-2017
	        		menuIntent.putExtra("CCBYear",CCBYear);////CCB Year Added 16-11-2018
	        		
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
	
	
	//Get Shared Pref Values
    private void Get_SharedPref_Values() {
      SharedPreferences sharedpreferences = context.getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
      if (sharedpreferences.contains("MemDir_View"))
	  {
    	  MemDir_View=sharedpreferences.getString("MemDir_View", "");
      }
	}
	
	
	private void GetAdditionalData(SQLiteDatabase db)
	{
		Additional_Data="NODATA"; 
		Additional_Data2="NODATA";
		    
		//Get Additional Data for MainScreen and PopupScreen
		String Qry="Select Add1,Add2 from "+TableMiscName+" Where Rtype='MASTER'";
		Cursor cursorT = db.rawQuery(Qry, null);
		while(cursorT.moveToNext())
		{
		      Additional_Data=cursorT.getString(0);
		      Additional_Data2=cursorT.getString(1);
		      if(Additional_Data==null || Additional_Data=="")
		      {
		    	Additional_Data="NODATA";
		      }
		      if(Additional_Data2==null || Additional_Data2=="")
		      {
		    	Additional_Data2="NODATA";
		      }
		      break;
		}
		cursorT.close();
	}
	
	 
	
   ///////Get New Option Caption or Title which is used to display some list in popup screen
    private void Get_New_Op(SQLiteDatabase db)
    { 
      ///////Get New Option Caption or Title which is used to display some list in popup screen
		String Qry="Select Text1 from "+Table4Name+" Where Rtype='ADDL'";
		Cursor cursorT = db.rawQuery(Qry, null);
		if(cursorT.moveToFirst())
		{
		   New_Op_Title=cursorT.getString(0);
		}
		cursorT.close();
		////////////////////////////////////////////////
		//db.close(); 
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
	
   private EditText editText(int id,String hint,final String DValue) 
   {
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
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
	   	if (keyCode == KeyEvent.KEYCODE_BACK) {
	   		backs();
	   	    return true;
	   	}
	   	return super.onKeyDown(keyCode, event);
	}
	
	 private void DisplayMsg(String head,String body,final int ckk){
		 AlertDialog ad=new AlertDialog.Builder(this).create();	
		  ad.setTitle(Html.fromHtml("<font color='#E3256B'>"+head+"</font>"));
	      ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>"+body+"</font>"));
		  ad.setButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	              if(ckk==1)
	            	  backs();
	              else
	            	  dialog.dismiss();
	            }
	        });
	      ad.show();	
	}
	 
	 // To go Back
	 public void backs(){
		Intent MainBtnIntent= new Intent(context,MenuPage.class);
		MainBtnIntent.putExtra("AppLogo", AppLogo);
	    startActivity(MainBtnIntent);
	    finish();
	 }

}
