package group.manager;

import java.io.ByteArrayInputStream;
import java.text.DateFormatSymbols;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FamilyDetailvalue extends Activity {
	SQLiteDatabase db;
	Cursor cursorT;
	String sqlSearch="",Log,ClubName,logid,Str_user,StrClubName,TableFamilyName,STRM_ID,StrName,StrRelation,
			StrFather,StrMother,StrAddress,StrLoc,StrMob1,StrMob2,StrDobD,StrDobM,StrDobY,StrEmail,StrAge=null,StrBirthT,StrBirthP,
			StrGotra,StrRemark,StrEduc,StrWork,DOB="",Gender,MobileSTR,EmailSTR,PName;
	Intent menuIntent;
	TextView TXtName,TXTRelation,TXTGender,TXTFather,TXTMother,TXTAddress,TXTLoc,TXTMob,TXTDob,TXTEmail,TXTAge,TXTBirthT,TXTBirthP,TXTGotra,
	TXTRemark,TXTEduc,TXTWork;
	byte[] AppLogo,imgP;
	int post;
	LinearLayout LLAYOYT3,LLAYOYT4,LLAYOYT5,LLAYOYT6,LLAYOYT7,LLAYOYT9,LLAYOYT10,LLAYOYT11,LLAYOYT12,LLAYOYT13,LLAYOYT14,LLAYOYT15,LLAYOYT16;
	AlertDialog.Builder alertDialogBuilder3;
	AlertDialog ad;
	Context context=this;
	ImageView ImgMain;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.familydetail);
        TXtName=(TextView)findViewById(R.id.txtFamilydeatilvalue);		
        TXTRelation=(TextView)findViewById(R.id.txtFamilydeatilrlvalue);	
        TXTGender=(TextView)findViewById(R.id.txtGender);
        TXTFather=(TextView)findViewById(R.id.txtFamilydeatilfathervalue);	
        TXTMother=(TextView)findViewById(R.id.txtFamilydeatilmothervalue);
        TXTAddress=(TextView)findViewById(R.id.txtFamilydeatiladdvalue);		
        TXTLoc=(TextView)findViewById(R.id.txtFamilydeatillocvalue);		
        TXTMob=(TextView)findViewById(R.id.txtFamilydeatilmobvalue);	
        TXTDob=(TextView)findViewById(R.id.txtFamilydeatildobvalue);
        TXTEmail=(TextView)findViewById(R.id.txtFamilydeatilemailvalue);		
        TXTAge=(TextView)findViewById(R.id.txtFamilydeatilagevalue);		
        TXTBirthT=(TextView)findViewById(R.id.txtFamilydeatilBtvalue);	
        TXTBirthP=(TextView)findViewById(R.id.txtFamilydeatilBPvalue);
        TXTGotra=(TextView)findViewById(R.id.txtFamilydeatilGotravalue);		
        TXTRemark=(TextView)findViewById(R.id.txtFamilydeatilremarkvalue);		
        TXTEduc=(TextView)findViewById(R.id.txtFamilydeatileducvalue);	
        TXTWork=(TextView)findViewById(R.id.txtFamilydeatilworkvalue);
        
        ImgMain=(ImageView)findViewById(R.id.imageViewMemPic);
        
        LLAYOYT3=(LinearLayout)findViewById(R.id.idlay3);	
        LLAYOYT4=(LinearLayout)findViewById(R.id.idlay4);
        LLAYOYT5=(LinearLayout)findViewById(R.id.idlay5);		
        LLAYOYT6=(LinearLayout)findViewById(R.id.idlay6);		
        LLAYOYT7=(LinearLayout)findViewById(R.id.idlay7);
        LLAYOYT9=(LinearLayout)findViewById(R.id.idlay9);		
        LLAYOYT10=(LinearLayout)findViewById(R.id.idlay10);		
        LLAYOYT11=(LinearLayout)findViewById(R.id.idlay11);	
        LLAYOYT12=(LinearLayout)findViewById(R.id.idlay12);
        LLAYOYT13=(LinearLayout)findViewById(R.id.idlay13);		
        LLAYOYT14=(LinearLayout)findViewById(R.id.idlay14);		
        LLAYOYT15=(LinearLayout)findViewById(R.id.idlay15);	
        LLAYOYT16=(LinearLayout)findViewById(R.id.idlay16);
        
        menuIntent = getIntent(); 
        Log =  menuIntent.getStringExtra("Clt_Log");
		logid =  menuIntent.getStringExtra("Clt_LogID");
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		Str_user =  menuIntent.getStringExtra("UserClubName");
		STRM_ID =  menuIntent.getStringExtra("Pwd");
		post =  menuIntent.getIntExtra("POstion", post);
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		PName=menuIntent.getStringExtra("PName");
		
		TableFamilyName="C_"+Str_user+"_Family";
		Set_App_Logo_Title(); // Set App Logo and Title
		
        db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
        sqlSearch="Select Name,Relation,Father,Mother,Address,Current_Loc,Mob_1,Mob_2,DOB_D,DOB_M,DOB_Y,EmailId,Age,Birth_Time,"
        		+ "Birth_Place,Gotra,Remark,Education,Work_Profile,Text2,Pic from "+TableFamilyName+" where M_id="+STRM_ID;
		System.out.println(sqlSearch);
	    cursorT = db.rawQuery(sqlSearch, null);
	    if (cursorT.moveToFirst()) {
			   do {
				   StrName=ChkVal(cursorT.getString(0));
				   StrRelation =ChkVal(cursorT.getString(1));
				   StrFather =ChkVal(cursorT.getString(2));
				   StrMother=ChkVal(cursorT.getString(3));
				   StrAddress =ChkVal(cursorT.getString(4));
				   StrLoc =ChkVal(cursorT.getString(5));
				   StrMob1=ChkVal(cursorT.getString(6));
				   StrMob2 =ChkVal(cursorT.getString(7));
				   StrDobD =ChkVal(cursorT.getString(8));
				   StrDobM=ChkVal(cursorT.getString(9));
				   StrDobY =ChkVal(cursorT.getString(10));
				   StrEmail =ChkVal(cursorT.getString(11));
				   //StrAge=ChkVal(cursorT.getString(12));
				   //StrBirthT =ChkVal(cursorT.getString(13));
				   //StrBirthP =ChkVal(cursorT.getString(14));
				   //StrGotra=ChkVal(cursorT.getString(15));
				   //StrRemark =ChkVal(cursorT.getString(16));
				   StrEduc =ChkVal(cursorT.getString(17));
				   StrWork =ChkVal(cursorT.getString(18));
				   Gender =ChkVal(cursorT.getString(19));//Text2 is used as 'Gender'
				   imgP=cursorT.getBlob(20);
	    		 } while (cursorT.moveToNext());
	    	 }
	    cursorT.close();
	    db.close();
	    
	    if(imgP!=null){
			ByteArrayInputStream imageStream = new ByteArrayInputStream(imgP);
			Bitmap theImage = BitmapFactory.decodeStream(imageStream);
			ImgMain.setImageBitmap(theImage);
	    }
	    
	    if(StrMob1.length()==0){
	    	if(StrMob2.length()==0){
	    		 LLAYOYT7.setVisibility(View.GONE);	
	    	 }else{
	    		 LLAYOYT7.setVisibility(View.VISIBLE);	
	    		 TXTMob.setText(StrMob2); 
	    	 }
	    }else{
	    	LLAYOYT7.setVisibility(View.VISIBLE);	
	    	TXTMob.setText(StrMob1);
	    }
	    
	    TXTMob.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	MobileSTR= TXTMob.getText().toString().trim();
	        	//Toast.makeText(CircTransDeatil.this,mobil+"A", 1).show(); 
	        	if(MobileSTR.length()>=10){
		    		MobileSTR = MobileSTR.substring(MobileSTR.length()- 10);
		    	}
	    	    if(MobileSTR.length()!=0){
	    	    	alertDialogBuilder3 = new AlertDialog.Builder(context);
	    	        alertDialogBuilder3
	                .setPositiveButton("CALL",new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog,int id) {
	                    	callOnphone(MobileSTR);
	                    }
	                })
	                .setNegativeButton("SMS",new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog,int id) {
	                    	callOnSms(MobileSTR);
	                    }
	                });
	                ad = alertDialogBuilder3.create();
	                ad.show();
	    	    }else{
	    	    	alertDialogBuilder3 = new AlertDialog.Builder(context);
	    	        alertDialogBuilder3
	    	        .setTitle("Invalid number!!")
	                .setPositiveButton("Cancel",new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog,int id) {
	                    	dialog.dismiss();
	                    }
	                });
	                ad = alertDialogBuilder3.create();
	                ad.show();
	    	    }
	         }
	    });
	    
	    
	    TXTEmail.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	EmailSTR= TXTEmail.getText().toString().trim();
	    	    	alertDialogBuilder3 = new AlertDialog.Builder(context);
	    	        alertDialogBuilder3
	                .setPositiveButton("Send Email",new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog,int id) {
	                    	callemail(EmailSTR);
	                    }
	                });
	               
	                ad = alertDialogBuilder3.create();
	                ad.show();
	         }
	    });
	    
	    if(StrDobD.length()>0 && StrDobM.length()>0)
	    {
	    	if(StrDobY.length()==0){
	    		StrDobM= getMonthForInt(Integer.parseInt(StrDobM));
	    		DOB=StrDobD+" "+StrDobM;
	    	}else{
	    		DOB=StrDobD+"-"+StrDobM+"-"+StrDobY; 
	    	}
	    }
	    TXTDob.setText(DOB);//Set DOB
	    
	    if(StrEduc.contains("Education"))
	    	StrEduc="";
	    
	    if(StrWork.contains("Working With"))
	    	StrWork="";
	    
	    TXtName.setText(StrName);//Set Name
	    TXTGender.setText(Gender);//Set Gender
	    TXTRelation.setText(StrRelation);//Set Relation
	    
		filloremptyData(StrFather,LLAYOYT3,TXTFather);
		filloremptyData(StrMother,LLAYOYT4,TXTMother);
		filloremptyData(StrAddress,LLAYOYT5,TXTAddress);	
		filloremptyData(StrLoc,LLAYOYT6,TXTLoc);	
		filloremptyData(StrEmail,LLAYOYT9,TXTEmail);
		filloremptyData(StrAge,LLAYOYT10,TXTAge);
		filloremptyData(StrBirthT,LLAYOYT11,TXTBirthT);
		filloremptyData(StrBirthP,LLAYOYT12,TXTBirthP);
		filloremptyData(StrGotra,LLAYOYT13,TXTGotra);
		filloremptyData(StrRemark,LLAYOYT14,TXTRemark);
		filloremptyData(StrEduc,LLAYOYT15,TXTEduc);
		filloremptyData(StrWork,LLAYOYT16,TXTWork);
	}
	
	private String getMonthForInt(int num) {
        String month = "wrong";
        num=num-1;
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }
	
	public void callOnphone(String MobCall) {
		try {
	        Intent callIntent = new Intent(Intent.ACTION_DIAL);
	        callIntent.setData(Uri.parse("tel:"+MobCall));
	        startActivity(callIntent);
	    } catch (ActivityNotFoundException activityException) {
	    	Toast.makeText(getBaseContext(), "CALL failed", 0).show();
	    	//System.out.println("Call failed");
	    }
    }
 
    public void callOnSms(String MobCall) {
		try {
			String uri= "smsto:"+MobCall;
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
            intent.putExtra("compose_mode", true);
            startActivity(intent);
	       } catch (ActivityNotFoundException activityException) {
	    	Toast.makeText(getBaseContext(), "SMS failed", 0).show();
	       }
   }
    
    public void callemail(String tomail){
   	 try{
   		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
   		String aEmailList[] = {tomail};
   		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
   		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Club manager");
   		emailIntent.setType("plain/text");
   		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "My message body.");
   		startActivity(Intent.createChooser(emailIntent, "Send your email in:"));
   	 }catch(Exception ex){	
   		 System.out.println("Mail failed");
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
	 
	 public void filloremptyData(String str,LinearLayout ll,TextView txt){
		  if(str==null){
			ll.setVisibility(View.GONE);
		  }else if((str!=null)&&(str.length()!=0)){
			ll.setVisibility(View.VISIBLE);
			txt.setText(str);
		  }else{
			ll.setVisibility(View.GONE);
		  }
	    }
	 
	 
	//call function for initialise blank if null is there
	private String ChkVal(String DVal)
	{
		if((DVal==null)||(DVal.equalsIgnoreCase("null"))){
			DVal="";
		}
		return DVal.trim();
	}
	 
	 
	 public boolean onKeyDown(int keyCode, KeyEvent event) 
	 {
	   	 if (keyCode == KeyEvent.KEYCODE_BACK) {
	   		  Intent MainBtnIntent= new Intent(getBaseContext(),AffiliationAPP.class);	
			  MainBtnIntent.putExtra("POstion",post);
			  MainBtnIntent.putExtra("Count", 888222);
			  MainBtnIntent.putExtra("Clt_Log",Log);
			  MainBtnIntent.putExtra("Clt_LogID",logid);
			  MainBtnIntent.putExtra("Clt_ClubName",ClubName);
			  MainBtnIntent.putExtra("UserClubName",Str_user);
			  MainBtnIntent.putExtra("AppLogo", AppLogo);
			  MainBtnIntent.putExtra("PName", PName);
	    	  startActivity(MainBtnIntent);
	    	  finish();
	   	 return true;
	   	 }
	   	return super.onKeyDown(keyCode, event);
	 }
	
}
