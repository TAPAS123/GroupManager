package group.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class Judgement extends Activity {
    SQLiteDatabase db;
    TextView tvtitle;
    Button btnSubmit;
    String selectqry = "";
    String ClubName,ClientId,LogId,MTitle,Title,UserType; 
    String text="",range="",type="",names="";
    ArrayList<JudgementModel> arraylist;
    ListView listview;
    String total;
    int Sno,OptionMid;
    byte[] AppLogo;
    int Mid=0;
    ProgressDialog Progsdial;
    private boolean InternetPresent;
    Context context = this;
    int ChkSubmit=0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judgement);
        tvtitle = (TextView) findViewById(R.id.tvjdgtitle);
        listview = (ListView) findViewById(R.id.listview);
        btnSubmit = (Button) findViewById(R.id.submitbtn);
        
        Intent menuIntent = getIntent(); 
        Mid = menuIntent.getIntExtra("Mid",0);
        Title=menuIntent.getStringExtra("Title");
        MTitle=menuIntent.getStringExtra("MTitle");
        LogId =  menuIntent.getStringExtra("Clt_LogID");
        ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		ClientId =  menuIntent.getStringExtra("UserClubName");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		
		tvtitle.setText(Title);
		
		Set_App_Logo_Title(); // Set App Logo and Title
		
		Get_SharedPref_Values();///Get Stored Shared Pref Values
		
        arraylist = new ArrayList<JudgementModel>();

        db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        selectqry = "Select M_ID,SNO, Question ,(select user_ans from C_"+ClientId+"_OP3 where OP1_ID = "+Mid+" AND OP2_ID=0 AND Remark = C_"+ClientId+"_OP2.M_ID) AS [Total] from C_"+ClientId+"_OP2 where OP1_ID="+Mid+" AND Ans2 ='Options'";
        // selectqry = "Select M_ID,SNO, Question from C_"+Str_club+"_OP2 where OP1_ID = "+1069+" AND Ans2 ='Options'";
        Cursor cursor = db.rawQuery(selectqry,null);
        if(cursor.moveToFirst())
        {
            do
            {
                OptionMid = cursor.getInt(0);
                Sno =  cursor.getInt(1);
                names = chkval(cursor.getString(2));
                total = chkval(cursor.getString(3));
                arraylist.add(new JudgementModel(OptionMid,Sno,names,total));
            }while(cursor.moveToNext());
            listview.setAdapter(new AdapterJudgement(this,R.layout.rowitem_judgement,arraylist));

        }
        cursor.close();
        
        
        ////Check the Question Paper Submitted or Not
        selectqry="Select Submit From C_"+ClientId+"_OP3 Where OP1_ID="+Mid;
        cursor = db.rawQuery(selectqry,null);
        if(cursor.moveToFirst()){
        	ChkSubmit=cursor.getInt(0);
        }
        cursor.close();
        
        btnSubmit.setVisibility(View.GONE);
        listview.setEnabled(false);
        if(ChkSubmit==0){
        	btnSubmit.setVisibility(View.VISIBLE);
        	listview.setEnabled(true);
        }
        
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String name =  arraylist.get(position).names;
               int mid = arraylist.get(position).OptionMid;
                Intent intent = new Intent(context,JudgementRating.class);
                intent.putExtra("OptionMid",String.valueOf(mid));
                intent.putExtra("Participant",name);
                intent.putExtra("Mid",Mid);
                intent.putExtra("MTitle",MTitle);
                intent.putExtra("Clt_LogID",LogId);
                intent.putExtra("Clt_ClubName",ClubName);
                intent.putExtra("UserClubName",ClientId);
                intent.putExtra("AppLogo", AppLogo);
                startActivity(intent);
                finish();
            }
        });
        
        
        btnSubmit.setOnClickListener(new OnClickListener()
        { 
			@Override
			public void onClick(View arg0) {
				String Qry = "Update C_"+ClientId+"_OP3 Set Submit=1,SyncID=1 Where OP1_ID="+Mid;
	            db.execSQL(Qry);
	            Sync_OpPoll_Data();
			}
        });
        
    }

    
    private void Get_SharedPref_Values()
	{
		SharedPreferences ShPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		if (ShPref.contains("UserType"))
	    {
			UserType=ShPref.getString("UserType", "");
	    }
	}
    
    
    private String chkval(String str) {
        if(str == null)
            str = "";
        
        return  str;
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
    
    
    
    //Sync Opinion Poll Data M-S ////
  	public void Sync_OpPoll_Data()
  	{
  		progressdial();
  		Thread T2 = new Thread() {
  		@Override
  		public void run() {
  		  try {
  			    Chkconnection chkconn=new Chkconnection();//Intialise Chkconnection Object
  			    InternetPresent =chkconn.isConnectingToInternet(context);
  				if(InternetPresent==true)
  				{
  				  if(!db.isOpen())
  				    db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
  				  
  				  String SqlQry="Select OP2_ID,User_Ans,Remark from C_"+ClientId+"_OP3 Where OP1_ID="+Mid+" AND Submit=1 AND SyncID=1 Order by OP2_ID";
  				  Cursor cursorT = db.rawQuery(SqlQry, null);
  				  String UAns,Remark,SData="",WebResult;
  				  int Op2Id;
  				  while(cursorT.moveToNext())
  				  {
  					Op2Id=cursorT.getInt(0);
  					UAns=chkval(cursorT.getString(1));
  					Remark=chkval(cursorT.getString(2));
  					SData=SData+Op2Id+"^"+UAns+"^"+Remark+"@@";
  				 }
  				 cursorT.close();
  				 
  				 
  				 if(SData.length()>2)
  				 {
  					SData=SData.substring(0,SData.length()-2);
  					
  					String TempMS="M";
  					if(UserType.equals("SPOUSE")){
  					   TempMS="S";
  					}
  					
  					SData=LogId+"#"+TempMS+"#"+Mid+"#"+SData;
  					
  					WebServiceCall webcall=new WebServiceCall();
  				    WebResult=webcall.Sync_OpinionPoll_MS(ClientId, SData);
					if(WebResult.contains("Saved"))
					{
						SqlQry="Update C_"+ClientId+"_OP3 Set SyncID=0 Where OP1_ID="+Mid;
  				        db.execSQL(SqlQry);
					}
  				 }
				 db.close();
  			   }
  				
  			   runOnUiThread(new Runnable()
  	           {
  	            	 public void run()
  	            	 {
  	            		AlertDisplay("Result","Submitted Successfully !");
  	            	 }
  	           });
  			}
  		  	catch (Exception e) {
  		  		//System.out.println(e.getMessage()); 
  		  		e.printStackTrace();
  		  	}
  		  Progsdial.dismiss();
  		}
  	  };
  	  T2.start();
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
		 Intent intent = new Intent(this,OpinionPoll_MainScreen.class);
	   	 intent.putExtra("MTitle",MTitle);
	   	 intent.putExtra("ComeFrom","1");
	   	 intent.putExtra("Clt_LogID",LogId);
	   	 intent.putExtra("Clt_ClubName",ClubName);
         intent.putExtra("UserClubName",ClientId);
         intent.putExtra("AppLogo", AppLogo);
	     startActivity(intent);
	     finish();
	 }
    
    
    private void AlertDisplay(String head,String body){
		 AlertDialog ad=new AlertDialog.Builder(this).create();
	    	ad.setTitle( Html.fromHtml("<font color='#E3256B'>"+head+"</font>"));
	    	ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>"+body+"</font>"));
			ad.setButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	
	            	  dialog.dismiss();
	            	  backs();
	            	
	            }
	        });
	        ad.show();	
	}
    
}
