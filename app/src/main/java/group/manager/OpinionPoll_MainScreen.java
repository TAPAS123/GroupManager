package group.manager;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class OpinionPoll_MainScreen extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener {
    Button livebtn,pastbtn;
    String ClubName,ClientId,LogId,MTitle,ComeFrom;
    ListView lvmaintitle;
    ArrayList<RowItem_OpPoll_Main> arrayListTitle;
    Context context = this;
    TextView tvnoresultmsg;
    byte[] AppLogo;
    String StrCurdate="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion_poll__main_screen);
        
        TextView txtHead=(TextView) findViewById(R.id.txtHead);
        livebtn = (Button) findViewById(R.id.livebtn);
        pastbtn = (Button) findViewById(R.id.pastbtn);
        lvmaintitle = (ListView) findViewById(R.id.lvmaintitle);
        tvnoresultmsg = (TextView) findViewById(R.id.tvnoresultmsg);
        livebtn.setOnClickListener(this);
        pastbtn.setOnClickListener(this);
        lvmaintitle.setOnItemClickListener(this);
        tvnoresultmsg.setVisibility(View.GONE);
        
        Intent menuIntent = getIntent(); 
        MTitle=menuIntent.getStringExtra("MTitle");
        ComeFrom=menuIntent.getStringExtra("ComeFrom");
        LogId =  menuIntent.getStringExtra("Clt_LogID");
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		ClientId =  menuIntent.getStringExtra("UserClubName");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		
		Set_App_Logo_Title(); // Set App Logo and Title
		
		txtHead.setText(MTitle);
		
        ///Get Current date ///
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date CurDt = new Date();
        StrCurdate = df.format(CurDt);// Current Date in yyyy-MM-dd HH:mm:ss format
		
        //currdatediff = datedifference();////Get Date Difference from Currant date and 01/01/2001
        
        arrayListTitle = new ArrayList<RowItem_OpPoll_Main>();
        
        fillList_LivePast("LIVE");
    }

    
    
    private void fillList_LivePast(String tabType) {
    	
    	arrayListTitle.clear();
    	
    	String Qry="";
    	int[] Arr_OP1_Ids=null;
    	
    	SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
    	
    	if(tabType.equals("LIVE"))
    	{
    		 Qry = "Select Distinct OP1_ID From C_"+ClientId+"_OP3 Order By OP1_ID";
    		 Cursor cursor1 = db.rawQuery(Qry,null);
    		 Arr_OP1_Ids=new int[cursor1.getCount()];
    		 int i=0;
    		 while(cursor1.moveToNext())
    		 {
    			 Arr_OP1_Ids[i]= cursor1.getInt(0);
    			 i++;
    		 }
    	}
    	
    	String OnlyPublish="";
    	if(ComeFrom.equals("1")){
    		OnlyPublish=" AND Op_Publish=1";///Take Only Published List
    	}
    	
    	if(tabType.equals("LIVE"))
    		Qry="Select M_ID,OP_Name,Co_Type,OP_From,OP_To,Time_Req,Time_Remains,U_Ans from C_"+ClientId+"_OP1 Where (OP_From <='"+StrCurdate+"' AND Op_To >='"+StrCurdate+"')"+OnlyPublish;
    	else
    		Qry="Select M_ID,OP_Name,Co_Type,OP_From,OP_To,Time_Req,Time_Remains,U_Ans from C_"+ClientId+"_OP1 Where Op_To < '"+StrCurdate+"'"+OnlyPublish;
    	
        Cursor cursor = db.rawQuery(Qry,null);
        cursor.moveToFirst();
        if(cursor.getCount()>0)
        {
            tvnoresultmsg.setVisibility(View.GONE);
            lvmaintitle.setVisibility(View.VISIBLE);
            do
            {
                int M_ID = cursor.getInt(0);
                String OP_Name = chkVal(cursor.getString(1));
                String Co_Type = chkVal(cursor.getString(2));//OpinionPoll Or Quiz
                String Date_From=chkVal(cursor.getString(3));//Date From
                String Date_To=chkVal(cursor.getString(4));//Date To
                String DT_FromTo=DateTime_FromTo(Date_From,Date_To);
                int Time_Req=cursor.getInt(5);
                int Time_Remains =cursor.getInt(6);
                int U_Ans =cursor.getInt(7);
                
                boolean ChkNewPoll=false;
                if(tabType.equals("LIVE"))
            	{
                	if(Arr_OP1_Ids!=null)
                	{
                	  for(int i=0;i<Arr_OP1_Ids.length;i++)
                	  {
                		 ChkNewPoll=true;
                		 if(M_ID==Arr_OP1_Ids[i])
                		 {
                			 ChkNewPoll=false;
                			 break;
                		 }
                	  }
                	}
            	}
                
                arrayListTitle.add(new RowItem_OpPoll_Main(M_ID,OP_Name,tabType,Co_Type,DT_FromTo,Time_Req,Time_Remains,U_Ans,ChkNewPoll));
            }while (cursor.moveToNext());
            lvmaintitle.setAdapter(new Adapter_OpPoll_Main(context,R.layout.opinionpoll_mainquestionlistrow,arrayListTitle));
        }
        else
        {
            tvnoresultmsg.setVisibility(View.VISIBLE);
            lvmaintitle.setVisibility(View.GONE);
            lvmaintitle.setAdapter(new Adapter_OpPoll_Main(context,R.layout.opinionpoll_mainquestionlistrow,arrayListTitle));
            tvnoresultmsg.setText("No data available");
        }
    }

    
    private int datedifference() {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        
        ///Get Current date ///
        Date CurDt = new Date();
        String currdate = df.format(CurDt);
        
        String fromdate = "01/01/2001";
        String todate = currdate;
        long diffms = 0;
        try {

            Date startDate = df.parse(fromdate);   //Convert to Date
            Calendar c1 = Calendar.getInstance();
            c1.setTime(startDate);  //Change to Calendar Date

            Date endDate = df.parse(todate); //Convert to Date
            Calendar c2 = Calendar.getInstance();
            c2.setTime(endDate);    //Change to Calendar Date

            long ms1 = c1.getTimeInMillis(); //get Time in milli seconds
            long ms2 = c2.getTimeInMillis();
            diffms = ms2 - ms1;   //get difference in milli seconds

        } catch (ParseException e) {
            e.printStackTrace();
        }
        int diffindays = (int) (diffms / (24 * 60 * 60 * 1000));         //Find number of days by dividing the mili seconds
        
        ////Include the End Day also
        diffindays=diffindays+1;
        
        return diffindays;
    }



    @Override
    public void onClick(View v) {
        if(v == livebtn)
        {
            livebtn.setBackgroundResource(R.drawable.tabblue);
            livebtn.setTextColor(Color.parseColor("#ffffff"));
            pastbtn.setBackgroundResource(R.drawable.btn_tab);
            pastbtn.setTextColor(Color.parseColor("#062a78"));
            fillList_LivePast("LIVE");
        }
        else if(v == pastbtn)
        {
            livebtn.setBackgroundResource(R.drawable.btn_tab);
            livebtn.setTextColor(Color.parseColor("#062a78"));
            pastbtn.setBackgroundResource(R.drawable.tabblue);
            pastbtn.setTextColor(Color.parseColor("#ffffff"));
            fillList_LivePast("PAST");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int Mid = arrayListTitle.get(position).Mid;
        String title = arrayListTitle.get(position).title;
        String TabType = arrayListTitle.get(position).Type;
        String RType = arrayListTitle.get(position).RType;
        int TimerTime=arrayListTitle.get(position).Time_Req;
        int Time_Remains=arrayListTitle.get(position).Time_Remains;
        int U_Ans=arrayListTitle.get(position).U_Ans;
        
        Intent intent=null;
        if(ComeFrom.equals("1")) {
           if(RType.equals("Judge"))
        	   intent = new Intent(this,Judgement.class);//Go To Judgement
           else if(RType.equals("Election"))
        	   intent = new Intent(this,Voting.class);//Go To election Voting 
           else
        	   intent = new Intent(this,OpinionPoll_QuestionAnswer.class);
        }
        else if(ComeFrom.equals("2")){
        	if(RType.equals("Quiz"))
               intent = new Intent(this,Quiz_ScoreSheet.class);//Go To Quiz Score Sheet
        	else if(RType.equals("Judge"))
         	   intent = new Intent(this,JudgementReport.class);//Go To Judgement Report
        	else if(RType.equals("Election"))
         	   intent = new Intent(this,VotingReport.class);//Go To Election Voting Report
        	else
        	   intent = new Intent(this,OpinionPoll_ScoreSheet.class);//Go To Opinion poll Score Sheet
        }
        else{
        	
        	String SetLoc="N";
        	String StartDate=arrayListTitle.get(position).DateFromTo.split("\n")[0].replace("Start:", "").trim();
        	StartDate=StartDate.replace("(", "").replace(")", "");
        	
        	int diffMin=Sp_datedifference(StartDate);
        	if(diffMin<=0){
        		SetLoc="Y";
        	}
        	intent = new Intent(this,Add_Poll_Location.class);//Go To Add_Poll_Location
        	intent.putExtra("SetLoc",SetLoc);
        }
        intent.putExtra("Mid",Mid);
        intent.putExtra("Title",title);
        intent.putExtra("TabType",TabType);
        intent.putExtra("MTitle",MTitle);
        intent.putExtra("RType",RType);
        intent.putExtra("U_Ans",U_Ans);
        intent.putExtra("TimerTime",TimerTime);
        intent.putExtra("Time_Remains",Time_Remains);
        intent.putExtra("Clt_LogID",LogId);
        intent.putExtra("Clt_ClubName",ClubName);
        intent.putExtra("UserClubName",ClientId);
        intent.putExtra("AppLogo", AppLogo);
        startActivity(intent);
        finish();
       
    }
    
    
    
    private String chkVal(String str) {
        if(str == null) {
            str = "";
        }
        return str;
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
	   	 if(ComeFrom.equals("1")){//ComeFrom Menu Or Notification
		   	intent= new Intent(context,MenuPage.class);
		   	intent.putExtra("AppLogo", AppLogo);
		 }else{//ComeFrom Utilities
			 intent= new Intent(context,UlilitiesList.class);
			 intent.putExtra("AppLogo", AppLogo);
			 intent.putExtra("CondChk", "2");    
		 }
	   	startActivity(intent);
   	    finish(); 
	 }
	 
	 
	 ////Convert DateTime FromTo in Specific Format 
	 private String DateTime_FromTo(String DFrom,String DTo)
	 {
		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy (hh:mm a)");
	        
	     Date startDT=null,EndDT=null;
	        
	        try {
				startDT = df.parse(DFrom);//Convert to Date
				EndDT = df.parse(DTo);   //Convert to Date
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}   //Convert to Date
	        
	        
	        DFrom="Start: "+df1.format(startDT);//New formated Start DateTime
	        DTo="End  : "+df1.format(EndDT);//New formated End DateTime
	        
	      return DFrom+"\n"+DTo;  
	 }
	 
	 
	 private int Sp_datedifference(String D1)
	 {
		 DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
		 
		 ///Get Current date ///
	        Date CurDt = new Date();
	        
	        long diffms = 0;
	        try {

	            Date startDate = df1.parse(D1);   //Convert to Date
	            Calendar c1 = Calendar.getInstance();
	            c1.setTime(startDate);  //Change to Calendar Date

	            Calendar c2 = Calendar.getInstance();
	            c2.setTime(CurDt);    //Change to Calendar Date

	            long ms1 = c1.getTimeInMillis(); //get Time in milli seconds
	            long ms2 = c2.getTimeInMillis();
	            diffms = ms2 - ms1;   //get difference in milli seconds

	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	        int diffMin = (int) (diffms / (60 * 1000));         //Find number of minutes by dividing the mili seconds
	        
	        return diffMin;
	    }
}
