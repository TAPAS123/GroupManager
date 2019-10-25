package group.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class Quiz_Summary extends Activity implements AdapterView.OnItemClickListener {
    String ClubName,ClientId,LogId,MTitle,WebResult="",Mids="";
    Context context = this;
    String correctAnsOfQueNo="",corrAnsOfTotalPeople="";
    List<RowEnvt> rowItems;
    RowEnvt item;
    byte[] AppLogo;
    int Mid=0;
    ProgressDialog Progsdial;
    private boolean InternetPresent;
    ListView Lv1;
    TextView tvtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinionpoll_member_correct_ans);
        
        tvtitle = (TextView) findViewById(R.id.tvtitle) ;
        Lv1 = (ListView) findViewById(R.id.lvmembercorrectAns);
        Lv1.setOnItemClickListener(this);

        Intent menuIntent = getIntent(); 
        Mid = menuIntent.getIntExtra("Mid",0);
        MTitle=menuIntent.getStringExtra("MTitle");
        LogId =  menuIntent.getStringExtra("Clt_LogID");
        ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		ClientId =  menuIntent.getStringExtra("UserClubName");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		
		Set_App_Logo_Title(); // Set App Logo and Title
		
		tvtitle.setText("Quiz Summary");  //set heading
        
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


    //Quiz summary  Data From server ////
  	public void GetdataFromWebservice()
  	{
  		progressdial();
  		Thread T2 = new Thread() {
  		@Override
  		public void run() {
  		  try {
				WebServiceCall webcall=new WebServiceCall();
			    WebResult=webcall.Get_Quiz_Summary(ClientId, Mid+"");
  				 
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
    
  	
  	
  	 private void FillListData() 
  	 {
  		 rowItems = new ArrayList<RowEnvt>();
         String[] tempArr = WebResult.split("#");
         for(int i = 0;i<tempArr.length;i++)
         {
             String temp[] = tempArr[i].split("\\^");
             correctAnsOfQueNo = temp[0];
             corrAnsOfTotalPeople = temp[1];
             Mids = temp[2]+"#"+temp[3];
             item = new RowEnvt(correctAnsOfQueNo,"",corrAnsOfTotalPeople,Mids);
		     rowItems.add(item);
         }
         Lv1.setAdapter(new Adapter_OpinionPoll_MemberList(this,R.layout.list_opinionpoll_memberlist,rowItems));
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    	String Head = rowItems.get(position).EvtName;
        String corrAnsOfTotalPeople = rowItems.get(position).Evtdate;
        String Mids = rowItems.get(position).EvtVenue;

        if(!corrAnsOfTotalPeople.equals("0"))
        {
        	Intent intent = new Intent(context, OpinionPoll_MemberList.class);
            intent.putExtra("Head", Head);
            intent.putExtra("Mids", Mids);
            intent.putExtra("MTitle", MTitle);
            intent.putExtra("AppLogo", AppLogo);
            intent.putExtra("Mid", Mid);
            intent.putExtra("CFrom","2");//Comes from Quiz Summary
            startActivity(intent);
   	        finish();
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
		 Intent intent = new Intent(this,Quiz_ScoreSheet.class);
		 intent.putExtra("Mid", Mid);
	   	 intent.putExtra("MTitle",MTitle);
	   	 intent.putExtra("Clt_LogID",LogId);
	   	 intent.putExtra("Clt_ClubName",ClubName);
         intent.putExtra("UserClubName",ClientId);
         intent.putExtra("AppLogo", AppLogo);
	     startActivity(intent);
	     finish();
	 }
    
}
