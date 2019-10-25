package group.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DateFormatSymbols;
import java.util.ArrayList;

import group.manager.SimpleGestureFilter.SimpleGestureListener;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
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
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SearchDisplayContact extends Activity implements SimpleGestureListener{
	TextView txtCName,txtCmob1, TxtCmob2 ,TxtCph ,TxtCadd, txtCMAX ,txtCRmk, txtCcate;
	String StrM_id,Strmo2,Strph,StrCatg,StrRmk,MobileSTR,TableFamilyName,s,Strname,Stradd,Strmo,Stra1,Stra2,Stra3,Str_user,Log,
	ClubName,logid,sqlSearch,finalresult,Str_etmobsrch,Str_etmemNosrch,Str_etAddsrch,UserType,quey,MOb,name,categ,StrMobQry,
	StrNameQry,StrcateQry;
	String [] temp;
	ImageView ImgMaincont,BtnNxtcont,BtnPrevcont,ImgBinocont,Imgsearh,Imgsetting,ImgSerchBino;
	Intent menuIntent;
	AlertDialog ad;
	private SimpleGestureFilter detect;
	SQLiteDatabase db;
	Cursor cursorT;
	int s_count,Cnt,i=0;
	AlertDialog.Builder alertDialogBuilder3;
	byte[] imgP,AppLogo;
	Integer tempsize;
	int[] CodeArr;
	Dialog dialog;
	EditText ETMobsrchbinocl,EtMembersrchbino,EtAddrSrchBino,search;
	ActionBar actionBar;
	LinearLayout LLayCcate,LLayCrmk,LLayCadd,LLayCph,LLayCmob2,LLayCmob1,llaydialog,llBinocular;
	final Context context=this;
	SharedPreferences sharedpreferences;
	
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.direct_contact);
	        alertDialogBuilder3 = new AlertDialog.Builder(context);
	        detect = new SimpleGestureFilter(this,this);//    
	        ImgMaincont = (ImageView)findViewById(R.id.ivmaincontct);
	        BtnNxtcont=(ImageView)findViewById(R.id.iVNextcont);
			BtnPrevcont=(ImageView)findViewById(R.id.iVPrevcont);
			txtCName=(TextView)findViewById(R.id.tvmainNamecontact);
			txtCmob1=(TextView)findViewById(R.id.textViewDirwmob1);
			TxtCmob2=(TextView)findViewById(R.id.textViewDirwmob2);
			TxtCph=(TextView)findViewById(R.id.textViewDirwmobph);
			TxtCadd=(TextView)findViewById(R.id.textViewDirwadd);		
			txtCMAX=(TextView)findViewById(R.id.tvMinofMaxcont);		
			txtCRmk=(TextView)findViewById(R.id.textViewDirwRemark);	
			txtCcate=(TextView)findViewById(R.id.textViewDirwcateg);
			LLayCcate=(LinearLayout)findViewById(R.id.llcate);	
			LLayCrmk=(LinearLayout)findViewById(R.id.llrmk);	
			LLayCadd=(LinearLayout)findViewById(R.id.lladd1);	
			LLayCph=(LinearLayout)findViewById(R.id.llph);	
			LLayCmob2=(LinearLayout)findViewById(R.id.llmob2);	
			LLayCmob1=(LinearLayout)findViewById(R.id.llmob1);
			
			menuIntent = getIntent(); 
			Log =  menuIntent.getStringExtra("Clt_Log");
			logid =  menuIntent.getStringExtra("Clt_LogID");
			ClubName =  menuIntent.getStringExtra("Clt_ClubName");
			Str_user =  menuIntent.getStringExtra("UserClubName");
			AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
			quey =  menuIntent.getStringExtra("Qury");
			TableFamilyName="C_"+Str_user+"_Family";
			
			Set_App_Logo_Title(); // Set App Logo and Title
			
			if(quey.length()==0){
				Toast.makeText(context, "No records", 0).show();
			}else{
				temp=quey.split("#");
				MOb=temp[0].toString();
				name=temp[1].toString();
				categ=temp[2].toString();
				if(MOb.equals("0")){
					StrMobQry="";
				}else{
					StrMobQry=" and (Mob_1 like '%"+MOb+"%' or Mob_2 like '%"+MOb+"%')";
				}
				
				if(name.equals("0")){
					StrNameQry="";
				}else{
					StrNameQry=" and Name like'%"+name+"%'";
				}
				
				if(categ.equals("0")){
					StrcateQry="";
				}else{
					StrcateQry=" and Text5='"+categ+"'";
				}
			}
			
	        db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
	        sqlSearch="select Count(M_id) from "+TableFamilyName +" where MemId=0 and Name is not null and Name<>'' "+StrMobQry+StrNameQry+StrcateQry;
	        cursorT = db.rawQuery(sqlSearch, null);
		    if (cursorT.moveToFirst()) {
 		    	do {
 		    		 s_count=cursorT.getInt(0);
 		    	} while (cursorT.moveToNext());
 		     }
		    cursorT.close(); 
		    txtCMAX.setText(Cnt+1+" of "+s_count);
		    if(s_count!=0){
		    	Cnt=0;
		    	sqlSearch="select M_id from "+TableFamilyName +" where MemId=0 and Name is not null and Name<>''"+StrMobQry+StrNameQry+StrcateQry+" order by Name";
		    	cursorT = db.rawQuery(sqlSearch, null);
		    	tempsize=cursorT.getCount();
	            CodeArr=new int[tempsize];
	 		     if (cursorT.moveToFirst()) {
	 		    	do {
		    		   CodeArr[i]=cursorT.getInt(0);
		    		   i++;
	 		    	} while (cursorT.moveToNext());
	 		     }
	 		     cursorT.close();
	 		     db.close();
	 		     callCurser();           
		    }
		    else
		    {
		      DisplayAlert("Result!","No Record found");
		    }
		    
		    ImgMaincont.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		        	if(imgP!=null){
		        		WebView IVzoomimage;
		        		Bitmap theImage = null;
		        		ByteArrayInputStream imageStream = new ByteArrayInputStream(imgP);
		    			theImage = BitmapFactory.decodeStream(imageStream);
			    		dialog = new Dialog(context);
			    		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			    		dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
						dialog.setContentView(R.layout.zoomimage);
						IVzoomimage = (WebView)dialog.findViewById(R.id.imageViewzoom);
						ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
						theImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
					    byte[] byteArray = byteArrayOutputStream.toByteArray();
					    String imgageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
					    String image = "data:image/png;base64," + imgageBase64;
					    String html="<html><body><img src='{IMAGE_URL}' width=250 height=250 /></body></html>";
					    // Use image for the img src parameter in your html and load to webview
					    html = html.replace("{IMAGE_URL}", image);
					    IVzoomimage.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", "");
					    IVzoomimage.getSettings().setSupportZoom(true);
					    IVzoomimage.getSettings().setBuiltInZoomControls(true);
					    IVzoomimage.setBackgroundColor(Color.DKGRAY);
						dialog.show();
		        	}
		        }
		    });
		    
		    txtCmob1.setOnClickListener(new View.OnClickListener() {
			        public void onClick(View v) {
			        	callonphoneclock(txtCmob1);
			         }
			    });
		    
		    TxtCmob2.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		        	callonphoneclock(TxtCmob2);
		         }
		    });
			
			
			BtnPrevcont.setOnClickListener(new OnClickListener(){ 
	            @Override public void onClick(View arg0){
	               Prev();
	            }
		    });
			
			BtnNxtcont.setOnClickListener(new OnClickListener(){ 
		        @Override public void onClick(View arg0){
		           Next();
		        }
			 });
	}
		
	private void callonphoneclock(TextView edt)
	{
		MobileSTR= edt.getText().toString().trim();
    	//Toast.makeText(CircTransDeatil.this,mobil+"A", 1).show(); 
    	if((MobileSTR==null)||(MobileSTR.length()<10)||(MobileSTR.contains("+"))){
    		 alertDialogBuilder3
    		 .setTitle( Html.fromHtml("<font color='#E32636'>Error!</font>"))
    		 .setMessage("Wrong Mobile Number")
                .setPositiveButton("Call",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                    	dialog.dismiss();
                    }
                });
    		ad = alertDialogBuilder3.create();
            ad.show();	
    	}else if(MobileSTR.length()==10){
    		MobileSTR= "0"+MobileSTR;
                alertDialogBuilder3
                .setPositiveButton("Call",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                    	callOnphone(MobileSTR);
                    }
                })
                .setNegativeButton("Sms",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                    	callOnSms(MobileSTR);
                    }
                });
                ad = alertDialogBuilder3.create();
                ad.show();
    	}else{
    		Toast.makeText(getBaseContext(), "Please check number something wrong..", 1).show();
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
		
	 @Override
	public boolean dispatchTouchEvent(MotionEvent m1){
		// Call onTouchEvent of SimpleGestureFilter class
		this.detect.onTouchEvent(m1);
		return super.dispatchTouchEvent(m1);
    }

	 
	public void Next(){
		if(Cnt+1==tempsize){
    		Toast.makeText(getBaseContext(), "No Further Record", 0).show();
    	}else{
    		Cnt=Cnt+1;
		    callCurser();
		    txtCMAX.setText(Cnt+1+" of "+s_count);
    	}
	}
	
	public void Prev(){
		if(Cnt==0){
    		Toast.makeText(getBaseContext(), "No Previous Record", 0).show();
    	}else{
    		Cnt=Cnt-1;
		    callCurser();
		    txtCMAX.setText(Cnt+1+" of "+s_count);
    	}
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
	 
	public void callOnSms(String MobCall) {
			try {
				String uri= "smsto:"+MobCall;
	            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
	            intent.putExtra("compose_mode", true);
	            startActivity(intent);
		       } catch (ActivityNotFoundException activityException) {
		    	Toast.makeText(getBaseContext(), "Sms failed", 0).show();
		    	//System.out.println("Sms failed");
		       }
	 }

	
	private void DisplayAlert(String title,String msg){
		AlertDialog ad=new AlertDialog.Builder(this).create();
		ad.setTitle( Html.fromHtml("<font color='#E3256B'>"+title+"</font>"));
    	ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>"+msg+"</font>"));
		ad.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	BBack();
            }
        });
        ad.show();	
}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
	   	 if (keyCode == KeyEvent.KEYCODE_BACK) {
	   		BBack();
	   	    return true;
	   	 }
	   	return super.onKeyDown(keyCode, event);
	}
	
	//Back 
	private void BBack()
	{
	    finish();
	}

	@Override
	public void onSwipe(int direction) {
		// TODO Auto-generated method stub
		switch (direction) {
	      case SimpleGestureFilter.SWIPE_RIGHT :
	    	   Prev();
	           break;
	      case SimpleGestureFilter.SWIPE_LEFT : 
	    	   Next();
	           break;
	      case SimpleGestureFilter.SWIPE_DOWN :  
	           break;
	      case SimpleGestureFilter.SWIPE_UP :  
	    	   break;
	      }
	}

	@Override
	public void onDoubleTap() {
		// TODO Auto-generated method stub
	}
	
	public void callCurser() {
		// TODO Auto-generated method stub
		try{	
		 String sql="";
		 db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
		 sql="select M_id,Name,Mob_1,Mob_2,Relation, Father, Mother, Current_loc, DOB_D, DOB_M,Pic from "+TableFamilyName+" where M_id="+CodeArr[Cnt] ;
		 cursorT = db.rawQuery(sql, null);
		 if(cursorT.moveToFirst()){
			
			finalresult=cursorT.getInt(0)+"^"+Chkval(cursorT.getString(1))+"^"+Chkval(cursorT.getString(2))+"^"+Chkval(cursorT.getString(3))+"^"+
		    			Chkval(cursorT.getString(4))+"^"+Chkval(cursorT.getString(5))+"^"+Chkval(cursorT.getString(6))+"^"+Chkval(cursorT.getString(7))+"^"+
					    Chkval(cursorT.getString(8))+"^"+Chkval(cursorT.getString(9));
		    imgP=cursorT.getBlob(10);
		 }
		 cursorT.close();
		 
		 if(imgP!=null){
			ImgMaincont.setVisibility(View.VISIBLE);
			ByteArrayInputStream imageStream = new ByteArrayInputStream(imgP);
			Bitmap theImage = BitmapFactory.decodeStream(imageStream);
			ImgMaincont.setImageBitmap(theImage);
	     }else{
	    	 ImgMaincont.setVisibility(View.GONE);
		 }
		 db.close();
		 FillVal(finalresult); // Set Display Data of Directory
	  }catch(Exception ex){
   		 System.out.println(ex.getMessage());
	  }
   }
	
	public void FillVal(String WResult){
		s = WResult.replace("^", "##")+" ";
		temp = s.split("##"); 
		StrM_id=temp[0].toString();
		Strname=temp[1].toString();
		Strmo=temp[2].toString();
		Strmo2=temp[3].toString();
		Strph=temp[4].toString();
		Stra1=temp[5].toString();	
		Stra2=temp[6].toString();
		Stra3=temp[7].toString();
		StrCatg=temp[8].toString();
		StrRmk=temp[9].toString();
		
		Stradd=Stra1+" "+Stra2+" "+Stra3;
		txtCName.setText(Strname.trim()); 
		filloremptyData(Strmo.trim(),LLayCmob1,txtCmob1);	
		filloremptyData(Strmo2.trim(),LLayCmob2,TxtCmob2);	
		filloremptyData(Strph.trim(),LLayCph,TxtCph);
		filloremptyData(Stradd.trim(),LLayCadd,TxtCadd);
		filloremptyData(StrRmk.trim(),LLayCrmk,txtCRmk);	
		filloremptyData(StrCatg.trim(),LLayCcate,txtCcate);	
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
	
    private String Chkval(String DVal)
	{
    	if((DVal==null)||(DVal.equalsIgnoreCase("null"))){
			DVal="";
		}
		return DVal.trim();
	}
	
}