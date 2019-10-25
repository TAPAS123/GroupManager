package group.manager;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.ListView;
import android.widget.TextView;

public class LME_Rates extends Activity {

    ListView Lv1;
    TextView tvtitle;
    String ClientId,ClubName,MTitle,WebResult="";
    List<RowEnvt> rowItems;
    RowEnvt item;
    byte[] AppLogo;
    ProgressDialog Progsdial;
    private boolean InternetPresent;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinionpoll_member_correct_ans);
        
        tvtitle = (TextView) findViewById(R.id.tvtitle) ;
        Lv1 = (ListView) findViewById(R.id.lvmembercorrectAns);

        Intent intent = getIntent();
        MTitle=intent.getStringExtra("MTitle");
		ClubName =  intent.getStringExtra("Clt_ClubName");
		ClientId =  intent.getStringExtra("UserClubName");
		AppLogo =  intent.getByteArrayExtra("AppLogo");
        
		Set_App_Logo_Title(); // Set App Logo and Title
		
        tvtitle.setText("LME CSP Rates");  //set heading
        
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
			    WebResult=webcall.Get_LMERates();
  				 
  			   runOnUiThread(new Runnable()
  	           {
  	            	 public void run()
  	            	 {
  	            		 if(WebResult.contains("@@"))
  	            		 {
  	            			String[] Arr=WebResult.split("#");
  	            			String LMEDate=Arr[0].trim();
  	            			tvtitle.setText("LME CSP Rates "+LMEDate); //set heading with LME date
  	            			FillList(Arr[1].trim());
  	            		 } 
  	            		 else{
  	            			AlertDisplay("Technical Issue","Something went wrong");
  	            		 }
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
    
  	
  	
  	private void FillList(String DData)
  	{
  		rowItems = new ArrayList<RowEnvt>();
			
	    String[] Arr1 = DData.split("@@");
	    for(int i = 0;i<Arr1.length;i++)
        {
            String temp[] = Arr1[i].split("-");
            item = new RowEnvt(temp[0].trim(),temp[1].trim());
		    rowItems.add(item);
        }
         Lv1.setAdapter(new Adapter_LMERates(this,R.layout.list_lme_rates,rowItems));
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
	   Intent intent= new Intent(getBaseContext(),MenuPage.class);
	   intent.putExtra("AppLogo", AppLogo);
	   startActivity(intent);
	   finish();
   }

}
