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
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.TextView;

public class SMS_Balance extends Activity {

	String ClientId,ClubName,WebResult="";
	private Context context=this;
	byte[] AppLogo;
	ProgressDialog Progsdial;
	Thread networkThread;
	WebServiceCall webcall;
	TextView txtBal;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_bal_enquiry);
        
        txtBal= (TextView)findViewById(R.id.txtBal);
        
        Intent menuIntent = getIntent(); 
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		ClientId =  menuIntent.getStringExtra("UserClubName");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		
		Set_App_Logo_Title(); // Set App Logo and Title
		
		webcall=new WebServiceCall();//Webservice object
        
        Get_Sms_Balance();//Call a webservice
    }
    
    
    // Get Sms Balance  from webservice
  	private void Get_Sms_Balance()
  	{
  		progressdial(); 
	    networkThread = new Thread()
	    {
	       public void run()
	       {
	         try
	         {
	       	   WebResult=webcall.Get_Sms_Bal(ClientId);
	           runOnUiThread(new Runnable()
	           {
	          	  public void run()
	          	  {
	          		if(WebResult.contains("SMS")){
	          			DispAlert("Result !",WebResult);
	          		}
	          		else if(WebResult.contains("Error") || WebResult.contains("try later")){
	          			DispAlert("Error !","Technical Problem, Try Later !");
	          		}
	          		else{
	          			txtBal.setText(WebResult);
	          		}
	              }
	           });
	           Progsdial.dismiss();
	           return;
	         }
	         catch (Exception ex){
	          	 System.out.println(ex.getMessage());
	         }
	       }
	     };
	     networkThread.start();		
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
  	
  	
  	protected void progressdial()
    {
    	Progsdial = new ProgressDialog(this, R.style.MyTheme);
    	//Progsdial.setTitle("App Loading");
    	Progsdial.setMessage("Please Wait....");
    	Progsdial.setIndeterminate(true);
    	Progsdial.setCancelable(false);
    	Progsdial.getWindow().setGravity(Gravity.DISPLAY_CLIP_VERTICAL);
    	Progsdial.show();
    } 
  	
  	
  	 @SuppressWarnings("deprecation")
	 private void DispAlert(String Title,String Msg)
	 {
        AlertDialog ad=new AlertDialog.Builder(this).create();
	     ad.setTitle( Html.fromHtml("<font color='#1C1CF0'>"+Title+"</font>"));
    	 ad.setMessage(Msg);
    	 ad.setCancelable(false);
    	 ad.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	Goback();
            }
          });
         ad.show();
	 }
  	
  	 public boolean onKeyDown(int keyCode, KeyEvent event) 
     {
	   	 if (keyCode == KeyEvent.KEYCODE_BACK) {
	   		Goback();
	   	    return true;
	   	 }
	   	return super.onKeyDown(keyCode, event);
     }
   
    private void Goback()
    {
    	Intent MainBtnIntent= new Intent(context,UlilitiesList.class);
 		MainBtnIntent.putExtra("AppLogo", AppLogo);
 		MainBtnIntent.putExtra("CondChk", "2");
 		startActivity(MainBtnIntent);
 		finish(); 
    }
  	
}
