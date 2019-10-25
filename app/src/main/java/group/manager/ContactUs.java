package group.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ContactUs extends Activity{

	String ClientId,ClubName,Table4Name;
	private Context context=this;
	byte[] AppLogo;
	TextView txtName,txtAddr,txtMob,txtMob2,txtFax,txtEmail,txtWebsite;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactus);
		
		LinearLayout LLName=(LinearLayout)findViewById(R.id.LLName);
		LinearLayout LLAddr=(LinearLayout)findViewById(R.id.LLAddr);
		LinearLayout LLMob=(LinearLayout)findViewById(R.id.LLMob);
		LinearLayout LLFax=(LinearLayout)findViewById(R.id.LLFax);
		LinearLayout LLEmail=(LinearLayout)findViewById(R.id.LLEmail);
		LinearLayout LLWebsite=(LinearLayout)findViewById(R.id.LLWebsite);
		
		txtName=(TextView)findViewById(R.id.txtName);
		txtAddr=(TextView)findViewById(R.id.txtAddr);
		txtMob=(TextView)findViewById(R.id.txtMob);
		txtMob2=(TextView)findViewById(R.id.txtMob2);
		txtFax=(TextView)findViewById(R.id.txtFax);
		txtEmail=(TextView)findViewById(R.id.txtEmail);
		txtWebsite=(TextView)findViewById(R.id.txtWebsite);
		
		WebView webVw1=(WebView)findViewById(R.id.webVw1);
		
		Intent menuIntent = getIntent(); 
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		ClientId =  menuIntent.getStringExtra("UserClubName");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		
		Table4Name="C_"+ClientId+"_4";
		
		Set_App_Logo_Title();
		
		LLName.setVisibility(View.GONE);
		LLAddr.setVisibility(View.GONE);
		LLMob.setVisibility(View.GONE);
		LLFax.setVisibility(View.GONE);
		LLEmail.setVisibility(View.GONE);
		LLWebsite.setVisibility(View.GONE);
		
		SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
		String Qry="SELECT Text1,Cond1,Text2,Text3,Text4,Text5,Text6,Text7 FROM "+Table4Name +" where Rtype='Contact'";
		Cursor cursorT = db.rawQuery(Qry, null);
		
		String Name="",Addr="",Phone1="",Phone2="",Fax="",Email="",Website="",Loc="";
		
		if (cursorT.moveToFirst()) {
			Name=ChkVal(cursorT.getString(0));
			Addr=ChkVal(cursorT.getString(1));
			Phone1=ChkVal(cursorT.getString(2));
			Phone2=ChkVal(cursorT.getString(3));
			Fax=ChkVal(cursorT.getString(4));
			Email=ChkVal(cursorT.getString(5));
			Website=ChkVal(cursorT.getString(6));
			Loc=ChkVal(cursorT.getString(7));
	    }
	    cursorT.close();
	    db.close();
	    
	    
	    if(Name.length()>0){
	    	LLName.setVisibility(View.VISIBLE);
	    	txtName.setText(Name);
	    }
	    
	    if(Addr.length()>0){
	    	LLAddr.setVisibility(View.VISIBLE);
	    	txtAddr.setText(Addr);
	    }
	    
	    if(Phone1.length()>0 || Phone2.length()>0){
	    	LLMob.setVisibility(View.VISIBLE);
	    	txtMob.setText(Phone1);
	    	txtMob2.setText(Phone2);
	    }
	    
	    if(Fax.length()>0){
	    	LLFax.setVisibility(View.VISIBLE);
	    	txtFax.setText(Fax);
	    }
	    
	    if(Email.length()>0){
	    	LLEmail.setVisibility(View.VISIBLE);
	    	txtEmail.setText(Email);
	    }
	    
	    if(Website.length()>0){
	    	LLWebsite.setVisibility(View.VISIBLE);
	    	txtWebsite.setText(Website);
	    }
	    
	    if(Loc.length()>0)
	    {
	      String url = "http://maps.google.com/maps/api/staticmap?center=" + Loc + "&zoom=15&size=400x400&sensor=true";
	      //url = "http://maps.google.com/maps?q="+Loc;
	      
	      String text = "<html><body><p align=\"justify\">";
      	  text+= "</p><iframe width='100%' height='100%' src='https://maps.google.com/maps?q="+Loc+"&hl=es;z=15&amp;output=embed'></iframe></body></html>";
      	  webVw1.loadDataWithBaseURL(null, text, "text/html", "UTF-8", null);
	      
      	  webVw1.getSettings().setJavaScriptEnabled(true);
	      //webVw1.loadUrl(url);
	    }
	    
	    //Phone1 Click Event
	    txtMob.setOnClickListener(new View.OnClickListener() {
		     public void onClick(View v) {
		        String Ph1= txtMob.getText().toString().trim();
		        Show_LandLine_Dialog(Ph1.trim());
		     }
		});
	    
	    //Phone2 Click Event
	    txtMob2.setOnClickListener(new View.OnClickListener() {
		     public void onClick(View v) {
		        String Ph2= txtMob2.getText().toString().trim();
		        Show_LandLine_Dialog(Ph2.trim());
		     }
		});
	    
	    
	    //Email1 Click Event
	    txtEmail.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	String Email1= txtEmail.getText().toString().trim();
	        	Show_Email_Dialog(Email1);
	         }
	    });
	    
    }

	
	
	
	// Show Dialog to Call On Landline Number
		private void Show_LandLine_Dialog(final String PhoneNo)
		{
			AlertDialog.Builder AdBuilder = new AlertDialog.Builder(context);
			if((PhoneNo.length()==0)||(PhoneNo.contains("+"))){
			   AdBuilder
	   		    .setTitle( Html.fromHtml("<font color='#E32636'>Wrong Landline Number !</font>"))
	   		    .setMessage(PhoneNo)
	            .setPositiveButton("OK",new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog,int id) {
	                   	dialog.dismiss();
	                   }
	             });	
	   	   }else{
	   		   AdBuilder
	            .setPositiveButton("CALL",new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog,int id) {
	                	   
	                	   int ZeroIndex=PhoneNo.indexOf("0");
	                	   if(ZeroIndex==0)
	                   	     callOnphone(PhoneNo);
	                	   else
	                		 callOnphone("0"+PhoneNo);
	                	  
	                   }
	            });
	   	   }
		   AdBuilder.show();
	  }
	
	
		
	//Show Email Dialog
	private void Show_Email_Dialog(String Email)
	{      	
   	    Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        String[] TO = {Email};// Email Address in String Array 'TO'
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);                                    
        startActivity(Intent.createChooser(emailIntent, "Email:"));
	}
	
	public void callOnphone(String MobCall) {
		try {
	        Intent callIntent = new Intent(Intent.ACTION_DIAL);
	        callIntent.setData(Uri.parse("tel:"+MobCall));
	        startActivity(callIntent);
	    } catch (ActivityNotFoundException activityException) {
	    	Toast.makeText(getBaseContext(), "Call failed", 0).show();
	    	//System.out.println("Call failed");
	    }
	 }
	 
	
		
	
	//call function for initialise blank if null is there
	private String ChkVal(String DVal)
	{
		if((DVal==null)||(DVal.equalsIgnoreCase("null"))){
			DVal="";
		}
		return DVal;
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
	
 //Back 
 private void Goback()
 {
	Intent MainBtnIntent= new Intent(context,MenuPage.class);
	MainBtnIntent.putExtra("AppLogo", AppLogo);
    startActivity(MainBtnIntent);
    finish();
 }
 
}
