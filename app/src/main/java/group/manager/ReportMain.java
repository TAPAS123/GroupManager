package group.manager;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ReportMain  extends Activity{
	Button BtnShowReport;
	EditText etDay,etMonth,etDayF,etMonthF,etYear,etYearF;
	ListView listview1;
	RadioGroup radioGroup,Rgp2;
	RadioButton radioButton,RB2;
	static Calendar c;
	String dd="00",mm="00",yyyy="",ddt="31",Strmid,StrStauts,StrFrom,StrTo,mmt="12",Strshowtype;
	Intent menuIntent;
	String Log,ClubName,logid,Str_user,Tableitem,UID,strqry="",WebResp;
	final Context context = this;
    Cursor cursorT;
    SQLiteDatabase db;
    ArrayList<Product>products;
    ListAdapter2 boxAdapter;
    Button BtnSendReq;
    ActionBar actionBar;
    CheckBox cHkboxall;
	int countdata=0,chk=0;
	ProgressDialog Progsdial;
	Chkconnection chkconn;
	boolean InternetPresent;
	WebServiceCall webcall;
	Thread networkThread;
	AlertDialog.Builder alertDialogBuilder3;
	AlertDialog ad;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reportmain);
        etDay = (EditText)findViewById(R.id.editTextDay);
	 	etMonth= (EditText)findViewById(R.id.editTextMonth);
	 	etYear= (EditText)findViewById(R.id.editTextYear);
	 	listview1 = (ListView)findViewById(R.id.listViewAlert);
	 	etDayF = (EditText)findViewById(R.id.editTextDayF);
	 	etMonthF= (EditText)findViewById(R.id.editTextMonthF);
	 	etYearF= (EditText)findViewById(R.id.editTextYearF);
	 	radioGroup=(RadioGroup)findViewById(R.id.radioGroup1);
	 	Rgp2=(RadioGroup)findViewById(R.id.radioGroup2);
        BtnSendReq=(Button)findViewById(R.id.buttonSave);
        
        menuIntent = getIntent(); 
        Str_user =  menuIntent.getStringExtra("UserClubName");
		Log =  menuIntent.getStringExtra("Clt_Log");
		logid =  menuIntent.getStringExtra("Clt_LogID");
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		UID =  menuIntent.getStringExtra("UID");
		
		Tableitem="C_"+Str_user+"_Item";
		webcall=new WebServiceCall();//Call a Webservice
		chkconn=new Chkconnection();
		
        c = Calendar.getInstance();// create object for calender
	    dd = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        if((dd==null)||(dd.length()==0)){
        	dd="00";
        }
        mm=String.valueOf(c.get(Calendar.MONTH)+1);
        if((mm==null)||(mm.length()==0)){
        	mm="00";
        }
        yyyy=String.valueOf(c.get(Calendar.YEAR));
        System.out.println(yyyy);
        
        //mmt= String.valueOf(Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        
        etDay.setText(ddt);
        etMonth.setText(mmt);
        etYear.setText(yyyy);
        
        etDayF.setText(dd);
        etMonthF.setText(mm);
        etYearF.setText(yyyy);
       
       connectdbandgetdata(false); 
       
       this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
       ActionCall();
	    
	    BtnSendReq.setOnClickListener(new OnClickListener(){ 
            public void onClick(View arg0){
		          Strmid="";
			      for (Product p : boxAdapter.getBox()) {
			       if (p.box){
			    	   //p.mob = p.mob.substring(0, p.mob.length()-1).trim();
			    	   if(p.GroupId!=0){
				           Strmid +=p.GroupId+","; 
			    	   }
			        }
			      }
			      if(Strmid.length()!=0){
			    	  Strmid = Strmid.substring(0, Strmid.length()-1).trim();
			    	  showremark();
			      }else{
			    	  Toast.makeText(getApplicationContext(), "No check box selected", 1).show();
			      }
            }
       });  
	}
	
	
	private void callwebservice() {
		InternetPresent =chkconn.isConnectingToInternet(context);
		     if(InternetPresent==true){
		       progressdial();
	  	       networkThread = new Thread()
	  	       {
	  	         public void run()
	  	         {
	  	           try
	  	           {
	  	             WebResp=webcall.Mda_Booking(Str_user, StrFrom, StrTo, StrStauts, Strmid,Strshowtype);
	  	              runOnUiThread(new Runnable()
	  	              {
	  	          	   public void run()
	  	          	   {
	  	          		System.out.println(WebResp);
	  	          		 if(WebResp==null)
	  	          			 WebResp="Error";
	  	          		 //////////////////////////chech value /////////////////
	  	          		 if(WebResp.contains("^")){
	  	          			Intent localIntent = new Intent(context, ReportNext.class);
		  	      		    localIntent.putExtra("WebRsp", WebResp);
		  	      		    localIntent.putExtra("From", StrFrom);
		  	      		    localIntent.putExtra("To", StrTo);
		  	      		    localIntent.putExtra("Clt_ClubName", ClubName);
		  	      		    startActivity(localIntent);
		  	      		    //finish();  
	  	          		 }else{
	  	          			alertDialogBuilder3 = new AlertDialog.Builder(context);
			        		 alertDialogBuilder3
			        		 .setMessage(Html.fromHtml("<font color='#E32636'>"+WebResp+"!</font>"))
				                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
				                    public void onClick(DialogInterface dialog,int id) {
				                    	dialog.dismiss();
				                    }
				                });
			        		ad = alertDialogBuilder3.create();
				            ad.show();	 
	  	          		 }
	  	               }
	  	              });
	  	              Progsdial.dismiss();
	  	              return;
	  	            }catch (Exception localException){
	  	             	System.out.println(localException.getMessage());
	  	            }
	  	          }
	  	        };
	  	       networkThread.start();
		 }else{
			Toast.makeText(context, "select any date.", 1).show();
		 }
	}
	
	private void showremark() {
		// TODO Auto-generated method stub
		String Strddf,Strmmf,Styyf,Stddt,Strmmt,Stryyt;
		Stddt=chkval(etDay.getText().toString());
		Strmmt=chkval(etMonth.getText().toString());
		Stryyt=chkval(etYear.getText().toString());
        
		Strddf=chkval(etDayF.getText().toString());
		Strmmf=chkval(etMonthF.getText().toString());
		Styyf=chkval(etYearF.getText().toString());
		
		int selectedId=radioGroup.getCheckedRadioButtonId();
        radioButton=(RadioButton)findViewById(selectedId);
        StrStauts=radioButton.getText().toString();
        
        int selectedId2=Rgp2.getCheckedRadioButtonId();
        RB2=(RadioButton)findViewById(selectedId2);
        Strshowtype=RB2.getText().toString();
		
		if((Strddf.length()==0)||(Strmmf.length()==0)||(Styyf.length()==0)||(Stddt.length()==0)||(Strmmt.length()==0)||(Stryyt.length()==0)){
			Toast.makeText(getApplicationContext(), "Select date properly.", 1).show();
		}else if((Integer.parseInt(Strmmf)>12)||(Integer.parseInt(Strmmt)>12)||(Integer.parseInt(Strddf)>31)||(Integer.parseInt(Stddt)>31)){
			Toast.makeText(getApplicationContext(), "Check date and month.", 1).show();
		}else{
			String mmf=gettheMonthname(Integer.parseInt(Strmmf)-1);
			String mmt=gettheMonthname(Integer.parseInt(Strmmt)-1);
			if(Strddf.length()==1){
				Strddf="0"+Strddf;
			}
			if(Stddt.length()==1){
				Stddt="0"+Stddt;
			}
			StrTo=Stddt+"-"+mmt+"-"+Stryyt;
			StrFrom=Strddf+"-"+mmf+"-"+Styyf;
			
			System.out.println("Values:     "+logid+"  "+StrFrom+"  "+StrTo+"  "+StrStauts+"  "+Strmid+"  "+Strshowtype);
			callwebservice();
		}
	}
	
	 public static String gettheMonthname(int month){
		    String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		    return monthNames[month];
		}
	 
	 public  String chkval(String val){
		 if((val==null)||(val.length()==0)){
			 val=""; 
		 }
		 return val;
	 }
	
	public void ActionCall(){
	    actionBar = getActionBar();
	   // actionBar.setTitle("Today's Anniversary of "+LogclubName+" Group");
        // add the custom view to the action bar
        actionBar.setCustomView(R.layout.br_an_layout);
    	cHkboxall = (CheckBox)actionBar.getCustomView().findViewById(R.id.checkBoxAnBrall);
    	TextView txt = (TextView)actionBar.getCustomView().findViewById(R.id.textViewnane);
    	// setTitle("Club Manager");
    	txt.setText("Club Manager");
    	
    	cHkboxall.setOnClickListener(new OnClickListener(){ 
            public void onClick(View arg0){
         	  if(cHkboxall.isChecked()==true){
         		 connectdbandgetdata(true);
         	  }else{
         		 connectdbandgetdata(false);  
         	  }
            }
  	   });
       actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM |ActionBar.DISPLAY_SHOW_HOME);
     }
	
	 private void connectdbandgetdata(boolean tick){
		 try{
			 products =  new ArrayList<Product>();
			   	db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
				//strqry = "Select * from " + Tableitem + " where Cate_Id=" + UID;
			   	 strqry = "Select U_Id,Name from " + Tableitem+" where Text3 <> 'Room' or Text3 is null order by Cate_Id,P_Order";
				 cursorT = db.rawQuery(strqry, null);
				    if (cursorT.moveToFirst()) {
				      do
				      {
				       int uid = cursorT.getInt(0);
				       String name = cursorT.getString(1);
				       products.add(new Product(name,uid,tick));  
				      } while (cursorT.moveToNext());
				    }
				 cursorT.close();
			    db.close();
		       
			    if(products.size()!=0)
				 {
					boxAdapter = new ListAdapter2(this, products);
				    listview1.setAdapter(boxAdapter);
			     }else{
				    Toast.makeText(context, "No record", 1).show();
			     }
		 }catch(Exception ex){
			System.out.println(ex.getMessage());
	     }
		  	
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
	 
	 public void BBack()
	  {
		Intent localIntentmenuIntent = new Intent(context, BookingCawnporClub.class);
		localIntentmenuIntent.putExtra("ValChk","0");
		localIntentmenuIntent.putExtra("Clt_LogID", logid);
		localIntentmenuIntent.putExtra("Clt_Log", Log);
		localIntentmenuIntent.putExtra("Clt_ClubName", ClubName);
		localIntentmenuIntent.putExtra("UserClubName", Str_user);
		startActivity(localIntentmenuIntent);
	    finish();
	  }
	 
	 public boolean onKeyDown(int keyCode,KeyEvent event) 
	 {
	   	 if (keyCode == KeyEvent.KEYCODE_BACK) {
	   		BBack();
	   	    return true;
	   	 }
	   	return super.onKeyDown(keyCode, event);
	 }
}
