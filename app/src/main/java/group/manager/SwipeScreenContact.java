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
import android.text.InputType;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class SwipeScreenContact extends Activity implements SimpleGestureListener{
	TextView txtCName,txtCmob1, TxtCmob2 ,TxtCph ,TxtCadd, txtCMAX ,txtCRmk, txtCcate;
	String StrM_id,Strmo2,Strph,StrCatg,StrRmk,MobileSTR,TableFamilyName,s,Strname,Stradd,Strmo,Stra1,Stra2,Stra3,Str_user,Log,
	ClubName,logid,sqlSearch,finalresult,Str_etmobsrch,Str_etmemNosrch,UserType;
	String [] temp;
	ImageView ImgMaincont,BtnNxtcont,BtnPrevcont;
	Intent menuIntent;
	AlertDialog ad;
	private SimpleGestureFilter detect;
	SQLiteDatabase db;
	Cursor cursorT;
	int s_count,Cnt,i=0,s_categ;
	AlertDialog.Builder alertDialogBuilder3;
	byte[] imgP,AppLogo;
	Integer tempsize;
	int[] CodeArr;
	Dialog dialog;
	EditText ETMobsrchbinocl,EtMembersrchbino,EtAddrSrchBino;
	ActionBar actionBar;
	LinearLayout LLayCcate,LLayCrmk,LLayCadd,LLayCph,LLayCmob2,LLayCmob1;
	final Context context=this;
	SharedPreferences sharedpreferences;
	Spinner spsrchcateg;
	String [] categArr={""},catefillarr;
	ArrayAdapter<String> adapterflag;
	
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.direct_contact);
	        //ad=new AlertDialog.Builder(SwipeScreen.this).create();
	        alertDialogBuilder3 = new AlertDialog.Builder(SwipeScreenContact.this);
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
			TableFamilyName="C_"+Str_user+"_Family";
			
			Set_App_Logo_Title(); // Set App Logo and Title
			Get_SharedPref_Values();// Get Stored Shared Pref Values of Login
			
	        db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
	        sqlSearch="select Count(M_id) from "+TableFamilyName +" where MemId=0 and Name is not null and Name<>''";
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
		    	sqlSearch="select M_id from "+TableFamilyName +" where MemId=0 and Name is not null and Name<>'' order by Name";
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
		      DisplayAlert("Result!","No Record found,Do you want add contact");
		    }
		    
		    ActionCall();
		    
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
                    	Toast.makeText(context, "1", 1).show();
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
		 alertDialogBuilder3
		 .setTitle( Html.fromHtml("<font color='#E32636'>"+title+"</font>"))
		 .setMessage(Html.fromHtml("<font color='#1C1CF0'>"+msg+"</font>"))
            .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                	dialog.dismiss();
                	if(UserType.equalsIgnoreCase("ADMIN")){
                 		  menuIntent= new Intent(context,ImportantContact.class);//ImportantContact
                 		  menuIntent.putExtra("Clt_Log",Log);
                 		  menuIntent.putExtra("Clt_LogID",logid);
                 		  menuIntent.putExtra("Clt_ClubName",ClubName);
                 		  menuIntent.putExtra("UserClubName",Str_user);
                 		  menuIntent.putExtra("AppLogo", AppLogo);
                 		  startActivity(menuIntent);
                 		  finish();
               	    }else{
               	    	Toast.makeText(context, "you are not admin", 1).show();
               	    }
                }
            })
		.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
            	dialog.dismiss();
            	BBack();
            }
        });
		ad = alertDialogBuilder3.create();
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
		Intent	MainBtnIntent= new Intent(getBaseContext(),UlilitiesList.class);
		MainBtnIntent.putExtra("CondChk", "1");
   		MainBtnIntent.putExtra("AppLogo", AppLogo);
	    startActivity(MainBtnIntent);
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
		//Relation=Ph, Father=add1, Mother=add2, Current_loc=add3, DOB_D=category, DOB_M=remark.
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
	
	public void ActionCall(){
	    actionBar = getActionBar();
	    setTitle("Clubmanager");
        actionBar.setCustomView(R.layout.search_layout);
        ImageView ImgVwSearch = (ImageView) actionBar.getCustomView().findViewById(R.id.imageSerchMenu);
        ImageView ImgVwFilter= (ImageView) actionBar.getCustomView().findViewById(R.id.imageSetting);
        
        ImgVwSearch.setOnClickListener(new OnClickListener(){ 
            @Override 
              public void onClick(View arg0){
            	//search.setVisibility(View.GONE);
            	alert(1);
            }
        });
        
        //////////////////////////add contact////////////////////
          if(UserType.equalsIgnoreCase("ADMIN")){
        	  ImgVwFilter.setImageResource(R.drawable.addcontact);
		  }else{
			  ImgVwFilter.setImageResource(R.drawable.dot);
		  }
          
          ImgVwFilter.setOnClickListener(new OnClickListener(){ 
              @Override 
                public void onClick(View arg0){
            	  if(UserType.equalsIgnoreCase("ADMIN")){
            		  menuIntent= new Intent(context,ImportantContact.class);//ImportantContact
            		  menuIntent.putExtra("Clt_Log",Log);
            		  menuIntent.putExtra("Clt_LogID",logid);
            		  menuIntent.putExtra("Clt_ClubName",ClubName);
            		  menuIntent.putExtra("UserClubName",Str_user);
            		  menuIntent.putExtra("AppLogo", AppLogo);
            		  startActivity(menuIntent);
            		  finish();
          		  }
              }
          });
        /////////////////////////////////////////////////////////
        
        
       actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM |ActionBar.DISPLAY_SHOW_HOME);
     }
	
	 public void alert(final int serchck) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(SwipeScreenContact.this);
		final FrameLayout frameView = new FrameLayout(SwipeScreenContact.this);
		builder.setView(frameView);
		final AlertDialog alertDialog = builder.create();
		LayoutInflater inflater = alertDialog.getLayoutInflater();
		View dialoglayout = inflater.inflate(R.layout.view_dialog_box, frameView);
		LinearLayout llaydialog= (LinearLayout) dialoglayout.findViewById(R.id.llViewbox);
		LinearLayout ll = new LinearLayout(this);
		llaydialog.addView(ll, new LinearLayout.LayoutParams(100, LayoutParams.WRAP_CONTENT));
		Button btnSearch= (Button) dialoglayout.findViewById(R.id.btnSearch);
		
		if(serchck==1){
			LinearLayout llBinocular = (LinearLayout) dialoglayout.findViewById(R.id.llaybinosrch);
			llBinocular.setVisibility(View.VISIBLE);
			LinearLayout lladd = (LinearLayout) dialoglayout.findViewById(R.id.addresssrch);
			lladd.setVisibility(View.GONE);
			LinearLayout llcateg = (LinearLayout) dialoglayout.findViewById(R.id.catecontact);
			llcateg.setVisibility(View.VISIBLE);
			
			ETMobsrchbinocl = (EditText) dialoglayout.findViewById(R.id.etMobileBino);
			EtMembersrchbino = (EditText) dialoglayout.findViewById(R.id.etMemberBino);
			EtMembersrchbino.setHint("input name");
			EtMembersrchbino.setInputType(InputType.TYPE_CLASS_TEXT);
			spsrchcateg = (Spinner) dialoglayout.findViewById(R.id.spinnercateContactsrch);
			
			 db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
			 String sql="Select Distinct(DOB_D) from "+TableFamilyName+" where MemId=0  and  DOB_D is not null and DOB_D<>''";
			 cursorT = db.rawQuery(sql, null);
			 int s_categ= cursorT.getCount();
			 
			  if(s_categ==0){
				 adapterflag= new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,categArr);
			     adapterflag.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			     spsrchcateg.setAdapter(adapterflag); 
			  }else{
				String [] categfill=new String[s_categ+1];
				categfill[0]="";
				int k=1;
				 if (cursorT.moveToFirst()) {
				   do {
					   categfill[k]=cursorT.getString(0);
				       k++; 
				    } while (cursorT.moveToNext());
				  }
				 adapterflag= new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,categfill);
				 adapterflag.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				 spsrchcateg.setAdapter(adapterflag); 
			 }
			 cursorT.close();
			 db.close();
		}
		
		btnSearch.setOnClickListener(new OnClickListener(){ 
            @Override 
            public void onClick(View arg0){
	            	Str_etmobsrch=ETMobsrchbinocl.getText().toString();
	            	Str_etmemNosrch=EtMembersrchbino.getText().toString();
	            	String Strspcategory= spsrchcateg.getSelectedItem().toString().trim();
	            	if((Str_etmobsrch.length()==0)&&(Str_etmemNosrch.length()==0)&&(Strspcategory.length()==0)){
	            		Toast.makeText(getBaseContext(), "input atleast one field!", 0).show();
	            	}else{
	            		if((Str_etmobsrch.length()==0)){
	            			Str_etmobsrch="0";
	            		}
	            		if((Str_etmemNosrch.length()==0)){
	            			Str_etmemNosrch="0";
	            		}
	            		if((Strspcategory.length()==0)){
	            			Strspcategory="0";
	            		}
	            		
	            		alertDialog.dismiss();
	            		waiting();
	            		String query=Str_etmobsrch+"#"+Str_etmemNosrch+"#"+Strspcategory;
	            		menuIntent= new Intent(getBaseContext(),SearchDisplayContact.class);
	            		menuIntent.putExtra("Clt_LogID",logid);
	            		menuIntent.putExtra("Clt_Log",Log);
	            		menuIntent.putExtra("Clt_ClubName",ClubName);
	            		menuIntent.putExtra("UserClubName",Str_user);
	            		menuIntent.putExtra("Qury",query);
	            		menuIntent.putExtra("AppLogo",AppLogo);
	                	startActivity(menuIntent);
	             }
             }
        });
		alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    WindowManager.LayoutParams wmlp = alertDialog.getWindow().getAttributes();
		wmlp.gravity = Gravity.TOP | Gravity.RIGHT;
		wmlp.x = 50;   //x position
		wmlp.y = 50;   //y position
		alertDialog.show(); 
	 }

	
	 public void waiting(){
		 final ProgressDialog progressDialog = ProgressDialog.show(SwipeScreenContact.this, "",  "Loading......");
         progressDialog.setCancelable(false);
         new Thread(new Runnable() {
             @Override
             public void run() {
                 try {
                     //Do some stuff that take some time...
                     Thread.sleep(3000); // Let's wait for some time
                 } catch (Exception e) {
                     System.out.println(e.getMessage()); 
                 }
                 progressDialog.dismiss();
             }
         }).start();
	 }
	 
	 private void Get_SharedPref_Values()
		{
	      sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		   if (sharedpreferences.contains("UserType"))
		    {
			  UserType=sharedpreferences.getString("UserType", "");
			  System.out.println("member: "+UserType);
			} 
		}
		

}