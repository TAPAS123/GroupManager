package group.manager;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GoverningBody extends Activity{
	List<RowItem> rowItems;
	CustomList adapter;
	ListView lv1;
	SQLiteDatabase db;
	Cursor cursorT,cursorT2;
	String sqlSearch="",Table2Name,Table4Name,Log,ClubName,logid,Str_user,StrName,StrMob,StrDesrt,Stremail,year_str,MTitle,Strmemno,Strpref1,Strpref2,RType="";
	Intent menuIntent;
	int MaxNum1,i=0,yearchecker=0;
	RowItem item;
	byte[] imgP;
	AlertDialog ad;
	ByteBuffer wrapped;
	Integer Aimage = R.drawable.person1;
	Context context=this;
	TextView txtHeadent;
	Spinner spinChangeYear;
	Button btnChangeYearr;
	AlertDialog.Builder alertDialogBuilder3;
	String Number,email;
	String[] CodeArr,CodeArr_val;
	byte[] AppLogo;
	RelativeLayout rrchangeYear;
	ArrayAdapter<String>adapterPac;
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventcalendar);
        lv1 = (ListView) findViewById(R.id.listViewEvnt);
        txtHeadent=(TextView)findViewById(R.id.txtevnthead);
        rrchangeYear=(RelativeLayout)findViewById(R.id.rrlay1);
        spinChangeYear=(Spinner)findViewById(R.id.spinnchangeyear);
        btnChangeYearr=(Button)findViewById(R.id.btnChangeYear);
        
        menuIntent = getIntent(); 
		Log =  menuIntent.getStringExtra("Clt_Log");
		logid =  menuIntent.getStringExtra("Clt_LogID");
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		Str_user =  menuIntent.getStringExtra("UserClubName");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		MTitle =  menuIntent.getStringExtra("MTitle");
		RType=  menuIntent.getStringExtra("Rtype");
		
        Table2Name="C_"+Str_user+"_2";
		Table4Name="C_"+Str_user+"_4";
		Set_App_Logo_Title(); // Set App Logo and Title
		
        try{
        	db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
            sqlSearch="Select text1,Num1 from "+Table4Name+" Where Rtype='Year' order by num1 Desc";
            cursorT = db.rawQuery(sqlSearch, null);
            int tempsize=cursorT.getCount();
            CodeArr=new String[tempsize];
            CodeArr_val=new String[tempsize];
            if (cursorT.moveToFirst()) {
                do{
            	  CodeArr[i]=cursorT.getString(0);
        		  CodeArr_val[i]=cursorT.getString(1);
        		  i++;	
            	}while(cursorT.moveToNext());
            }
            cursorT.close();
            
            sqlSearch="Select Max(Num1) from "+Table4Name+" Where Rtype='"+RType+"'";
            System.out.println(sqlSearch);
            try{
             cursorT = db.rawQuery(sqlSearch, null);	
            }catch(Exception ex){
            	System.out.println(ex.getMessage());
            }
            if (cursorT.moveToFirst()) {
            	do{
            		MaxNum1=cursorT.getInt(0);
                	System.out.println(MaxNum1);	
              	}while(cursorT.moveToNext());
            	
            }
            cursorT.close();
            db.close();	
        }catch(Exception ex){
        	System.out.println(ex.getMessage());
        }
        
        
        /// call function for update data on year change...
        callagainonyearchange(String.valueOf(MaxNum1));
        if(CodeArr.length!=0){
          adapterPac =new ArrayAdapter<String>(GoverningBody.this,android.R.layout.simple_spinner_item,CodeArr);
          adapterPac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          spinChangeYear.setAdapter(adapterPac);
          
          spinChangeYear.setOnItemSelectedListener(new OnItemSelectedListener()
          {  
    		@Override
    		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
    			// TODO Auto-generated method stub
    			year_str=CodeArr_val[arg2];
    			System.out.println(year_str);
    		 }
    		@Override
    		public void onNothingSelected(AdapterView<?> arg0) {
    			// TODO Auto-generated method stub	
    		  }
            });
          
        }
       
        btnChangeYearr.setOnClickListener(new OnClickListener(){ 
            public void onClick(View arg0){
            	//String text = spinChangeYear.getSelectedItem().toString();
            	callagainonyearchange(year_str);  /// call function for update data on year change...	
                //Toast.makeText(getApplicationContext(), year_str, 1).show();
            }
 	   });
       
        
        lv1.setOnItemClickListener(new OnItemClickListener() {
  	    	 public void onItemClick(AdapterView<?> parent, View view, int position, long id){
  	    		 email =rowItems.get(position).getGVemail();
  	    		 Number =rowItems.get(position).getGvDesti();
  	    	    //System.out.println(email+Number);
  	    	    
  	    	    if((Number==null)||(Number.length()<10)){
  	    	    	alertDialogBuilder3 = new AlertDialog.Builder(context);
		        		 alertDialogBuilder3
		        		 .setTitle( Html.fromHtml("<font color='#E32636'>Error!</font>"))
		        		 .setMessage("Wrong Mobile Number")
			                .setPositiveButton("Cancel",new DialogInterface.OnClickListener() {
			                    public void onClick(DialogInterface dialog,int id) {
			                    	dialog.dismiss();
			                    }
			                });
		        		ad = alertDialogBuilder3.create();
			            ad.show();	
		        	}else if(Number.length()>=10){
		        		alertDialogBuilder3 = new AlertDialog.Builder(context);
		        		Number=Number.substring(Number.length()-10, Number.length());
		        		//System.out.println("cut::  "+Number);
		        		Number= "0"+Number;
			                alertDialogBuilder3
			                .setPositiveButton("Call",new DialogInterface.OnClickListener() {
			                    public void onClick(DialogInterface dialog,int id) {
			                    	callOnphone(Number);
			                    }
			                })
			                .setNegativeButton("Sms",new DialogInterface.OnClickListener() {
			                    public void onClick(DialogInterface dialog,int id) {
			                    	callOnSms(Number);
			                    }
			                })
			                .setNeutralButton("Email",new DialogInterface.OnClickListener() {
			                    public void onClick(DialogInterface dialog,int id) {
			                    	callemail(email);
			                    }
			                });
			                
			                ad = alertDialogBuilder3.create();
			                ad.show();
		        	 }
  	  	        }
  	         });
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.govern_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.changyear:
	        	changeyear(); // Add More Group for Login
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void changeyear(){
		rrchangeYear.setVisibility(View.VISIBLE);
	}
	
	private void callagainonyearchange(String StrGovern){
	     if((StrGovern.length()==0)||(StrGovern==null)){
			 Toast.makeText(getApplicationContext(), "No record found..", 1).show();	
	     }else{
	    	 try{
	            db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
	   		    sqlSearch="SELECT Text1,Text2,Num3,Photo1,Text3,Text4 from "+Table4Name+" Where Rtype='"+RType+"' AND Num1="+StrGovern+" Order By Num2";
	   		    System.out.println(sqlSearch);
	   	        cursorT = db.rawQuery(sqlSearch, null);
	   	         if(cursorT.getCount()==0){
	   	        	txtHeadent.setText("No Records");
	   	        	lv1.setVisibility(View.GONE);
	   	         }else{
	   	        	txtHeadent.setText(MTitle);
	   	        	lv1.setVisibility(View.VISIBLE);
	   	        	Bitmap theImage = null;
	   	        	boolean HasMem=false;
	   	        	rowItems = new ArrayList<RowItem>();
	   	        	 if (cursorT.moveToFirst()) {
	   	  			   do {
	   	  				   StrName=ChkVal(cursorT.getString(0));
	   	  				   StrMob=ChkVal(cursorT.getString(2));
	   	  				   StrDesrt=cursorT.getString(1); 
	   	  				   imgP=cursorT.getBlob(3);
	   	  				   Stremail=ChkVal(cursorT.getString(4)); 
	   	  				   Strmemno=ChkVal(cursorT.getString(5)); 
	   	  				   
	   	  				   if(StrDesrt==null){
	   	  					StrDesrt="";
	   	  				   }
	   	  				   //System.out.println("1:  "+StrName+"  "+StrMob+"   "+StrDesrt+"  "+Stremail+" "+Strmemno);
	   	  				   
	   	  				   //Check Record in Table2 with M_Mob (Member)
	   	  				   if((Strmemno==null)||(Strmemno.trim().length()!=0)||(StrMob==null)||(StrMob.length()!=0))
	   	  				   {
	   	  					 HasMem=false;
	   	  					 sqlSearch="SELECT M_Name,M_Mob,M_Email,MemNo,M_Pic,C4_BG,C4_DOB_Y from "+Table2Name+" Where (MemNo='"+Strmemno+"' and MemNo<>'') or (M_Mob='"+StrMob+"' and M_Mob<>'')";
	   	  	  	             cursorT2 = db.rawQuery(sqlSearch, null);
	   	  	  	             while (cursorT2.moveToNext())
	   	  	  	             {
	   	  	  	               HasMem=true;
	 	  	  	  	           StrName=ChkVal(cursorT2.getString(0));
	 	  	  				   StrMob=ChkVal(cursorT2.getString(1));
	 	  	  				   Stremail=ChkVal(cursorT2.getString(2)); 
	 	  	  				   Strmemno=cursorT2.getString(3);
	 	  	  				   imgP=cursorT2.getBlob(4);
	 	  	  				   Strpref1=cursorT2.getString(5); 
	 	  	  			       Strpref2=cursorT2.getString(6);
	 	  	  			       if((Strpref1==null)||(Strpref1.trim().length()==0)){
	 	  	  			    	 Strpref1=""; 
	 	  	  			       }
	 	  	  			       if((Strpref2==null)||(Strpref2.trim().length()==0)){
	 	  	  			    	 Strpref2=""; 
	 	  	  			       }
	 	  	  			       StrName=Strpref1+Strpref2+" "+StrName;
	 	  	  				   //System.out.println("2:  "+StrName+"  "+StrMob+"  "+Stremail+" "+Strmemno);
	 	  	  			       break;
	   	  	  	             }
	   	  	  	             
	   	  	  	             //Check Record in Table2 with S_Mob (Spouse) if HasMem false
	   	  	  	             if(!HasMem)
	   	  	  	             {
	   	  	  	               sqlSearch="SELECT S_Name,S_Mob,S_Email,S_Pic,C3_BG from "+Table2Name+" Where (S_Mob='"+StrMob+"' and S_Mob<>'')";
	   	  	  	               cursorT2 = db.rawQuery(sqlSearch, null);
	   	  	  	               while (cursorT2.moveToNext())
	   	  	  	               {
	 	  	  	  	              StrName=ChkVal(cursorT2.getString(0));
	 	  	  				      StrMob=ChkVal(cursorT2.getString(1));
	 	  	  				      Stremail=ChkVal(cursorT2.getString(2)); 
	 	  	  				      imgP=cursorT2.getBlob(3);
	 	  	  				      Strpref1=cursorT2.getString(4); 
	 	  	  			          if((Strpref1==null)||(Strpref1.trim().length()==0)){
	 	  	  			    	    Strpref1=""; 
	 	  	  			          }
	 	  	  			          StrName=Strpref1+" "+StrName;
	 	  	  			          break;
	   	  	  	                }
	   	  	  	              }
	   	  	  	              cursorT2.close();
	   	  	  	              
	   	  				   }
	 	  	  			   if(imgP==null){
	 							theImage=null;
	 		  	  		   }else if(imgP!=null){
	 			  	  			ByteArrayInputStream imageStream = new ByteArrayInputStream(imgP);
	 		  	  				theImage = BitmapFactory.decodeStream(imageStream);
	 		  	  		   }else{
	 		  	  				theImage=null;
	 		  	  		   }
	 	  	  			   if(Stremail==null){
	 	  	  					Stremail=""; 
	 	  	  			   }
	 	  	  			   if(StrMob.equals("0")){
	 	  	  			      StrMob=""; 
 	  	  				   }
	   	  				   item = new RowItem(theImage,StrName,StrMob,StrDesrt,Stremail);
	   	        	       rowItems.add(item); 
	   	  	    		 } while (cursorT.moveToNext());
	   	  	    	 }
	   	        	 adapter = new CustomList(context,R.layout.governlist, rowItems);
	   	        	 lv1.setAdapter(adapter);
	   	         }
	   	        cursorT.close();
	   	        db.close();  
	           }catch(Exception ex){
	        	   System.out.println(ex.getMessage());
	           } 
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
	
	
	private String ChkVal(String Val)
	{
		if(Val==null)
		  Val="";
		else
		  Val=Val.trim();
		return Val;
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
		 System.out.println("Call failed");
	 }
   }
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	 {
	   	 if (keyCode == KeyEvent.KEYCODE_BACK) {
	   		Intent	MainBtnIntent= new Intent(getBaseContext(),MenuPage.class);
	   		MainBtnIntent.putExtra("AppLogo", AppLogo);
		    startActivity(MainBtnIntent);
		    finish();
	   	    return true;
	   	 }
	   	return super.onKeyDown(keyCode, event);
	 }
	
}
