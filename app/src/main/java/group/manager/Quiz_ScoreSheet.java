package group.manager;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Quiz_ScoreSheet extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener {
   
    String ClubName,ClientId,LogId,MTitle,WebResult="";
    ListView LV1;
    ArrayList<Row_Item_QuizScoreSheet_Main> Arr_List;
    Context context = this;
    byte[] AppLogo;
    //String StrCurdate="";
    int Mid=0;
    ImageView ImgSummary;
    ProgressDialog Progsdial;
    private boolean InternetPresent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_scoresheet_list);
        
        TextView txtHead=(TextView) findViewById(R.id.txtHead);
        LV1 = (ListView) findViewById(R.id.LV1);
        ImgSummary = (ImageView) findViewById(R.id.imgSummary);
        
        ImgSummary.setOnClickListener(this);
        LV1.setOnItemClickListener(this);
        
        Intent menuIntent = getIntent(); 
        Mid = menuIntent.getIntExtra("Mid",0);
        MTitle=menuIntent.getStringExtra("MTitle");
        LogId =  menuIntent.getStringExtra("Clt_LogID");
        ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		ClientId =  menuIntent.getStringExtra("UserClubName");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		
		Set_App_Logo_Title(); // Set App Logo and Title
		
		txtHead.setText("Score Sheet");
		
		Chkconnection chkconn=new Chkconnection();//Intialise Chkconnection Object
	    InternetPresent =chkconn.isConnectingToInternet(context);
		if(InternetPresent==true)
		{
		    GetdataFromWebservice();
	    }
		else{
			AlertDisplay("Internet Connection","No Internet Connection !");
		}
    }
    
    
    //ScoreSheet Data From server ////
  	public void GetdataFromWebservice()
  	{
  		progressdial();
  		Thread T2 = new Thread() {
  		@Override
  		public void run() {
  		  try {
				WebServiceCall webcall=new WebServiceCall();
			    WebResult=webcall.Get_Quiz_ScoreSheet(ClientId, Mid+"");
  				 
  			   runOnUiThread(new Runnable()
  	           {
  	            	 public void run()
  	            	 {
  	            		 if(WebResult.contains("^"))
  	            		    FillListData();///Fill List Data
  	            		 else
  	            			AlertDisplay("Technical Issue","Something went wrong");
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
 
    
    

    private void FillListData() {
    	
    	 String[] WebResultArr = WebResult.split("#");
    	 
    	 Arr_List = new ArrayList<Row_Item_QuizScoreSheet_Main>();
    	
    	String Qry="select M_ID,Question,Answer,Ans1,Ans2,Ans3,Ans4 from C_"+ClientId+"_OP2 Where OP1_ID = "+Mid+" Order By PSNO";
    	
    	SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        Cursor cursor = db.rawQuery(Qry,null);
        int Sno=1;
        while (cursor.moveToNext()){
        	int Op1_Id=cursor.getInt(0);
            String Ques = chkVal(cursor.getString(1));
            String Ans = chkVal(cursor.getString(2));
            String Ans1 = chkVal(cursor.getString(3));
            String Ans2 = chkVal(cursor.getString(4));
            String Ans3 = chkVal(cursor.getString(5));
            String Ans4 = chkVal(cursor.getString(6));
            
            if(Ans.equals("1") || Ans.equals("A"))
            	Ans="("+Ans+") "+Ans1;
            if(Ans.equals("2") || Ans.equals("B"))
            	Ans="("+Ans+") "+Ans2;
            if(Ans.equals("3") || Ans.equals("C"))
            	Ans="("+Ans+") "+Ans3;
            if(Ans.equals("4") || Ans.equals("D"))
            	Ans="("+Ans+") "+Ans4;
            
            String CorrectIds="",InCorrectIds="";
            int TotalCorrect=0,TotalInCorrect=0,TotalMembers=0;
            for(int i = 0;i<WebResultArr.length;i++)
            {
                String temp[] = WebResultArr[i].split("\\^");
                int Ques_Id = Integer.parseInt(temp[0].trim());
                
                if(Op1_Id==Ques_Id)
                {
                	String[] temp1 = temp[1].split("@@");   //correct members
                    String[] temp2 = temp[2].split("@@");   //incorrect members
                    CorrectIds = temp1[0]+"#"+temp1[1];   //correct memberIds + spouseIds
                    TotalCorrect =  Integer.parseInt(temp1[2].trim());// Total correct ans
                    InCorrectIds = temp2[0]+"#"+temp2[1]; //incorrect memberName
                    TotalInCorrect = Integer.parseInt(temp2[2].trim());//// Total Incorrect ans
                    TotalMembers = TotalCorrect+TotalInCorrect;//Total Participaing Members
                	break;
                }
            }
            
            Arr_List.add(new Row_Item_QuizScoreSheet_Main(Sno,Ques,Ans,TotalCorrect,TotalInCorrect,TotalMembers,CorrectIds,InCorrectIds,MTitle,AppLogo,Mid));
            
            Sno++;
        }
        cursor.close();
        db.close();
        
        LV1.setAdapter(new Adapter_Quiz_ScoreSheetMain(context,R.layout.quiz_scoresheet_listitem,Arr_List));
    }

    

    @Override
    public void onClick(View v) {
        if(v == ImgSummary)
        {
        	Intent intent = new Intent(this,Quiz_Summary.class);
            intent.putExtra("Mid",Mid);
            intent.putExtra("MTitle",MTitle);
            intent.putExtra("Clt_LogID",LogId);
            intent.putExtra("Clt_ClubName",ClubName);
            intent.putExtra("UserClubName",ClientId);
            intent.putExtra("AppLogo", AppLogo);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*int Mid = arrayListTitle.get(position).Mid;
        String title = arrayListTitle.get(position).title;
        String TabType = arrayListTitle.get(position).Type;
        
        if(ComeFrom.equals("1"))
        {
          Intent intent = new Intent(this,OpinionPoll_QuestionAnswer.class);
          intent.putExtra("Mid",Mid);
          intent.putExtra("Title",title);
          intent.putExtra("TabType",TabType);
          intent.putExtra("MTitle",MTitle);
          intent.putExtra("Clt_LogID",LogId);
          intent.putExtra("Clt_ClubName",ClubName);
          intent.putExtra("UserClubName",ClientId);
          intent.putExtra("AppLogo", AppLogo);
          startActivity(intent);
          finish();
        }*/
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
    
    
    private String chkVal(String str) {
        if(str == null) {
            str = "";
        }
        return str;
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
	   	 intent.putExtra("ComeFrom","2");
	   	 intent.putExtra("Clt_LogID",LogId);
	   	 intent.putExtra("Clt_ClubName",ClubName);
         intent.putExtra("UserClubName",ClientId);
         intent.putExtra("AppLogo", AppLogo);
	     startActivity(intent);
	     finish();
	 }
}
