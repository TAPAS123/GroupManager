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
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class OpinionPoll_QuestionAnswer extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener
{
    TextView tvquestion,tvshowquestatus,tvtitle,tvtimer,txtAnsStatus;
    ListView lvanswer;
    Button prevbtn,nextbtn,submitbtn,markreviewbtn,btnReview;
    EditText edtremark;
    SQLiteDatabase db;
    String selectqry="";
    int Sno;
    ArrayList<RowItem_OpPoll_QAns> arrayListAns;
    Context context = this;
    String User_ans="",getremark="",Title="",ClubName,ClientId,LogId,UserType,TabType,MTitle,RType="";
    int Remark_Req,PSNO,OP2_ID;
    int Mid,TotalQuestion=0,markreview=0;
    byte[] AppLogo;
    int ChkSubmit=0;
    ProgressDialog Progsdial;
    private boolean InternetPresent;
    int TimerTime=0,U_Ans=0;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L,TimerTimeMilSec,ActualTimerTime,Time_Remains ;

    Handler handler;

    int Seconds, Minutes, MilliSeconds ;
    Thread Thrd_UpdateTimerTime;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion_poll__question_answer);

        tvtitle = (TextView) findViewById(R.id.tvtitle);
        tvtimer = (TextView) findViewById(R.id.tvtimer);
        tvquestion = (TextView) findViewById(R.id.tvquestion);
        txtAnsStatus = (TextView) findViewById(R.id.txtAnsStatus);
        lvanswer = (ListView) findViewById(R.id.lvanswer);
        tvshowquestatus = (TextView) findViewById(R.id.tvshowquestatus);
        prevbtn = (Button) findViewById(R.id.prevbtn);
        nextbtn = (Button) findViewById(R.id.nextbtn);
        edtremark = (EditText) findViewById(R.id.edtremark);
        submitbtn = (Button) findViewById(R.id.submitbtn);
        markreviewbtn = (Button) findViewById(R.id.markreviewbtn);
        btnReview = (Button) findViewById(R.id.btnReview);
        
        Intent intent = getIntent();
        Mid = intent.getIntExtra("Mid",0);
        Title = intent.getStringExtra("Title");
        TabType = intent.getStringExtra("TabType");
        MTitle=intent.getStringExtra("MTitle");
        RType=intent.getStringExtra("RType");
        U_Ans = intent.getIntExtra("U_Ans",0);
        LogId =  intent.getStringExtra("Clt_LogID");
        ClubName =  intent.getStringExtra("Clt_ClubName");
		ClientId =  intent.getStringExtra("UserClubName");
		AppLogo =  intent.getByteArrayExtra("AppLogo");
		
		handler = new Handler() ;
		
		db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);//Open database
		
        prevbtn.setOnClickListener(this);
        nextbtn.setOnClickListener(this);
        submitbtn.setOnClickListener(this);
        markreviewbtn.setOnClickListener(this);
        btnReview.setOnClickListener(this);
        btnReview.setVisibility(View.GONE);
        lvanswer.setOnItemClickListener(this);
        edtremark.setVisibility(View.GONE);
        tvtitle.setText(Title);
        
        arrayListAns = new ArrayList<RowItem_OpPoll_QAns>();
        
        Set_App_Logo_Title(); // Set App Logo and Title
        
        Get_SharedPref_Values();///Get Stored Shared Pref Values

        selectqry = "select PSNO  from C_"+ClientId+"_OP2 where OP1_ID = "+Mid+"" ;
        Cursor cursor1 = db.rawQuery(selectqry,null);
        TotalQuestion = cursor1.getCount();
        cursor1.close();
        
        ////Check the Question Paper Submitted or Not
        selectqry="Select Submit From C_"+ClientId+"_OP3 Where OP1_ID="+Mid;
        cursor1 = db.rawQuery(selectqry,null);
        if(cursor1.moveToFirst()){
        	ChkSubmit=cursor1.getInt(0);
        }
        cursor1.close();
         
        if(ChkSubmit==1 || TabType.equals("PAST")){
          	lvanswer.setEnabled(false);
        }else{
          	lvanswer.setEnabled(true);
          	//selectqry = "Delete from C_"+ClientId+"_OP3 where OP1_ID="+Mid;
          	//db.execSQL(selectqry);
        }
        //////////////////////////////////
        
        PSNO=1;
        func_set_quesans();
        
        tvtimer.setVisibility(View.GONE);
		if(TabType.equals("LIVE")  && ChkSubmit==0)
		{
			if(RType.equals("Quiz"))
			{
			  TimerTime = intent.getIntExtra("TimerTime",0);
			  Time_Remains = intent.getIntExtra("Time_Remains",0);
			
			  TimerTimeMilSec=TimerTime*60*1000;///Timer Time In MiliSeconds
			
			  ActualTimerTime=TimerTimeMilSec-Time_Remains;///Actual Timer Time In MiliSeconds
			
			  /////Implement Quiz Timer/////
			  if(ActualTimerTime>0){
				tvtimer.setVisibility(View.VISIBLE);
				
				TimeBuff=ActualTimerTime;

				Seconds = (int) (TimeBuff / 1000);
		        Minutes = Seconds / 60;
		        
		        tvtimer.setText("" + Minutes + ":" + String.format("%02d", Seconds) );

				StartTime = SystemClock.uptimeMillis();
	            handler.postDelayed(runnable, 0);
		      }
			}
			else
			{
				lvanswer.setEnabled(false);
			}
		}
    }
    
    
    private void Get_SharedPref_Values()
	{
		SharedPreferences ShPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		if (ShPref.contains("UserType"))
	    {
			UserType=ShPref.getString("UserType", "");
	    }
	}

    public  void func_set_quesans()
    {
    	if(PSNO==TotalQuestion && ChkSubmit==0 && TabType.equals("LIVE"))
    		submitbtn.setVisibility(View.VISIBLE);
    	else
    		submitbtn.setVisibility(View.GONE);
    	
    	if(!db.isOpen())
    	  db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
    	
    	selectqry = "select Question,Ans1,Ans2,Ans3,Ans4,Remark_Req,Remark,Answer,M_ID,SNO from C_"+ClientId+"_OP2 where OP1_ID="+Mid+" AND PSNO="+PSNO;
        Cursor cursor = db.rawQuery(selectqry,null);
        int RCount=cursor.getCount();

        boolean DisAnswerStatus=false;
        int CorrectAnsSno=0;
        
        if(cursor.moveToFirst())
        {
            arrayListAns.clear();
            String question = chkVal(cursor.getString(0));
            String Ans1 = chkVal(cursor.getString(1));
            String Ans2 = chkVal(cursor.getString(2));
            String Ans3 = chkVal(cursor.getString(3));
            String Ans4 = chkVal(cursor.getString(4));
            int Remark_Req = cursor.getInt(5);
            String Remark = chkVal(cursor.getString(6));
            String CorrectAns = chkVal(cursor.getString(7));
            OP2_ID = cursor.getInt(8);
            String Sno = chkVal(cursor.getString(9));
            
            ////Display Correct Ans with Condition////
            if((U_Ans==1 && ChkSubmit==1) || (U_Ans==2 && TabType.equals("PAST")))
            {
            	if(CorrectAns.equals("1") || CorrectAns.equals("A"))
            		CorrectAnsSno=1;
                else if(CorrectAns.equals("2") || CorrectAns.equals("B"))
                	CorrectAnsSno=2;
                else if(CorrectAns.equals("3") || CorrectAns.equals("C"))
                	CorrectAnsSno=3;
                else if(CorrectAns.equals("4") || CorrectAns.equals("D"))
                	CorrectAnsSno=4;
            	
            	DisAnswerStatus=true;
            }
            /////////////////////////////////
            
            if (!Ans1.equals("")) {
                arrayListAns.add(new RowItem_OpPoll_QAns("1.", Ans1, CorrectAns,false,CorrectAnsSno));
            }
            if (!Ans2.equals("")) {
                arrayListAns.add(new RowItem_OpPoll_QAns("2.", Ans2, CorrectAns,false,CorrectAnsSno));
            }
            if (!Ans3.equals("")) {
                arrayListAns.add(new RowItem_OpPoll_QAns("3.", Ans3, CorrectAns,false,CorrectAnsSno));
            }
            if (!Ans4.equals("")) {
                arrayListAns.add(new RowItem_OpPoll_QAns("4.", Ans4, CorrectAns,false,CorrectAnsSno));
            }
            
            if(Remark_Req == 1) {
                edtremark.setVisibility(View.VISIBLE);
            }
            else{
                edtremark.setVisibility(View.GONE);
            }
            lvanswer.setAdapter(new Adapter_OpPoll_QAns(context, R.layout.opinionpoll_answerlistrow, arrayListAns));
            tvquestion.setText(Sno+". "+question);
            tvshowquestatus.setText(PSNO + " out of " + TotalQuestion);
        }
        cursor.close();
        
        ////Check user answered given question or not////
        if(RCount>0)
        {
          String UserAns="",Remark="";
          String Qry="Select User_Ans,Remark From C_"+ClientId+"_OP3 Where OP1_ID="+Mid+" AND OP2_ID="+OP2_ID;
          Cursor cursor1 = db.rawQuery(Qry,null);
          if(cursor1.moveToFirst()){
        	UserAns=chkVal(cursor1.getString(0));
        	Remark=chkVal(cursor1.getString(1));
          }
          cursor1.close();
        
          if(UserAns.length()>0)
          {
        	for(int i=0;i<arrayListAns.size();i++)
            {
        		if(i==Integer.parseInt(UserAns)-1)
        			arrayListAns.get(i).Setflag(true);
        		else
        			arrayListAns.get(i).Setflag(false);
            }
            
        	Adapter_OpPoll_QAns adp1=new Adapter_OpPoll_QAns(context, R.layout.opinionpoll_answerlistrow, arrayListAns);
        	adp1.notifyDataSetChanged();
        	lvanswer.setAdapter(adp1);
          }
        
          if(Remark.length()>0)
          {
        	edtremark.setText(Remark.trim());
          }
          
          ///To Display Answer Status(User given correct/incorrect/not answered)
          if(DisAnswerStatus)
          {
        	  txtAnsStatus.setVisibility(View.VISIBLE);//Display Answer Status
        	  
        	  String AnsStatus="Not Attempted";
			  if(UserAns.length()>0)
			  {
				  if(Integer.parseInt(UserAns)==CorrectAnsSno)
					  AnsStatus="Correct Answer";
				  else
					  AnsStatus="Incorrect Answer";
			  }
			  txtAnsStatus.setText(AnsStatus);
          }
         }
        ///////////
    }
    
    
    private String chkVal(String str) {
        if(str == null) {
            str = "";
        }
        return str;
    }

    @Override
    public void onClick(View v) {
        if(v == prevbtn)
        {
            if(PSNO>1){
            	if(ChkSubmit==0 && TabType.equals("LIVE")){
            	  save_in_op3(false);
            	}
                User_ans = "";
            	PSNO=PSNO-1;
                func_set_quesans();
            }
            else{
            	Toast.makeText(context, "No Previous Question", 0).show();
            }
        }
        else if(v == nextbtn)
        {
            if(PSNO!=TotalQuestion)
            {
               if(ChkSubmit==0 && TabType.equals("LIVE")){
              	  save_in_op3(false);
               }
               User_ans = "";
               PSNO=PSNO+1;
               func_set_quesans();
            }
            else{
            	Toast.makeText(context, "No Next Question", 0).show();
            }
        }
        else if(v == submitbtn)
        {
        	if(ChkSubmit==0 && TabType.equals("LIVE")){
              save_in_op3(true);
            }
            User_ans = "";
        }
    }

    public void save_in_op3(boolean flagSubmit)
    {
        if(Remark_Req == 1){
            getremark = edtremark.getText().toString().trim();
        }
        else{
            getremark = "";
        }
        int SubmitVal=0;
        
        if(!db.isOpen())
          db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        
        String Qry="Select Submit From C_"+ClientId+"_OP3 Where OP1_ID="+Mid+" AND OP2_ID="+OP2_ID;
        Cursor cursor1 = db.rawQuery(Qry,null);
        int RCount=cursor1.getCount();
        if(cursor1.moveToFirst()){
        	SubmitVal=cursor1.getInt(0);
        }
        cursor1.close();
        
        if(SubmitVal==0)
        {
           if(RCount>0)
           {
        	   if(User_ans.length()>0){
        	     Qry = "Update C_"+ClientId+"_OP3 Set User_Ans='"+User_ans+"',Remark='"+getremark+"' Where OP1_ID="+Mid+" AND OP2_ID="+OP2_ID;
                 db.execSQL(Qry);
        	   }
           }
           else{
        	 Qry = "Insert into C_"+ClientId+"_OP3 (OP1_ID,OP2_ID,User_Ans,Remark,Submit) values ("+Mid+","+OP2_ID+",'"+User_ans+"','"+getremark+"',0)";
             db.execSQL(Qry);
           }
        
           if(flagSubmit)
           {
        	  Qry = "Update C_"+ClientId+"_OP3 Set Submit=1,SyncID=1 Where OP1_ID="+Mid;
              db.execSQL(Qry);
              Sync_OpPoll_Data();
           }
        }
    }

    
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {	
    	for(int i=0;i<arrayListAns.size();i++)
        {
    		if(i==position)
    			arrayListAns.get(i).Setflag(true);
    		else
    			arrayListAns.get(i).Setflag(false);
        }
        
    	Adapter_OpPoll_QAns adp1=new Adapter_OpPoll_QAns(context, R.layout.opinionpoll_answerlistrow, arrayListAns);
    	adp1.notifyDataSetChanged();
    	lvanswer.setAdapter(adp1);
    	
        int ans = position;
        User_ans = String.valueOf(ans + 1);
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
  					UAns=chkVal(cursorT.getString(1));
  					Remark=chkVal(cursorT.getString(2));
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
  	            		tvtimer.setVisibility(View.GONE);
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
	  
   
   ///// Quiz Timer Handler Runnable 
   public Runnable runnable = new Runnable() {

       public void run() {

           MillisecondTime = SystemClock.uptimeMillis() - StartTime;

           UpdateTime = TimeBuff - MillisecondTime;
           
           if(UpdateTime>0)
           {
        	   long Diff=TimeBuff-UpdateTime;
               int DiffSec=(int) (Diff / 1000);
               
               if((DiffSec%10)==0)
               {
            	   if(Time_Remains>0)
            	   {
            		   Diff=Time_Remains+Diff;
            	   }
            	   Update_Timer_ConsumedTime(Diff);//Update Timer Consumed Time
               }  
        	   
             Seconds = (int) (UpdateTime / 1000);

             Minutes = Seconds / 60;

             Seconds = Seconds % 60;

             tvtimer.setText("" + String.format("%02d", Minutes) + ":" + String.format("%02d", Seconds) );

             handler.postDelayed(this, 0);
           }
           else
           {
        	   lvanswer.setEnabled(false);
        	   Update_Timer_ConsumedTime(TimerTimeMilSec);//Update Timer Consumed Time
           }
       }

   };
   
   
	 
	 public void backs(){
	   	    
		 if(db.isOpen())
			 db.close();///Close DataBase
		 
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
	 
	 
	 
	 //Update Consumed Time of Quiz Timer ////
	 public void Update_Timer_ConsumedTime(final long Diff)
	 {
		 Thrd_UpdateTimerTime = new Thread() {
	  		@Override
	  		public void run() {
	  		  try { 
	  			    if(!db.isOpen())
	  				  db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
	         	    String Qry = "Update C_"+ClientId+"_OP1 Set Time_Remains="+Diff+" Where M_ID="+Mid;
	                db.execSQL(Qry);
	  			}
	  		  	catch (Exception e) {
	  		  		//System.out.println(e.getMessage()); 
	  		  		e.printStackTrace();
	  		  	}
	  		}
	  	  };
	  	Thrd_UpdateTimerTime.start();
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
