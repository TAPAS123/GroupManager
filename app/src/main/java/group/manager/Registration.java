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
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class Registration extends Activity{
	SharedPreferences sharedpreferences,sharedprf;
	Intent menuIntent;
	byte[] AppLogo;
	String UID,WEbResult,s,caption,value,StrEditVAl="",Strfinal="",valfinal="",StrCaption1="",StrCaption2="",
			Str_IEMI,Strclub,WEbResponseReg;
	String[] temp,sp,temp1;
	LinearLayout LaynoRecd,layMainReg;
	RelativeLayout RlayVal;
	Button BtnSaveREGistration;
	Context context=this;
	Editor editor;
	List<EditText> allEds;
	List<Spinner> allSps;
	AlertDialog ad;
	ProgressDialog Progsdial;
	Thread networkThread;
	boolean InternetPresent = false;
	Chkconnection chkconn;
	WebServiceCall webcall;
	TelephonyManager tm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration);
		ad=new AlertDialog.Builder(this).create();
		LaynoRecd=(LinearLayout)findViewById(R.id.llaynorecord);
		layMainReg=(LinearLayout)findViewById(R.id.llayRegmain);
		RlayVal=(RelativeLayout)findViewById(R.id.RLayvalue);
		BtnSaveREGistration=(Button)findViewById(R.id.btnSaveReg);
		
		webcall=new WebServiceCall();//Call a Webservice
		chkconn=new Chkconnection();
		Str_IEMI = new CommonClass().getIMEINumber(context);//Added On 14-02-2019

		// setTitle(ClientID); // Set Title
		menuIntent = getIntent();
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		WEbResult =  menuIntent.getStringExtra("WebRegister");
		Strclub =  menuIntent.getStringExtra("StrClub");
		
		Set_App_Logo_Title();
		getwebResultandsetvalue();
		
		BtnSaveREGistration.setOnClickListener(new OnClickListener(){ 
			public void onClick(View arg0){
				 int checkval=0;
				 String firstLetter="",shrvaleditor="",FieldData="";
	        	 valfinal="";
	        	   
	        	   //Get Data From EditText Array List 
	        	   String[] ttemp=StrCaption1.split("#");
	        	   
	        	   for(int i=0; i < allEds.size(); i++){
  	            	 FieldData = allEds.get(i).getText().toString().trim();
  	            	 shrvaleditor=shrvaleditor+ttemp[i]+":"+FieldData+"@##@";
  	            	 valfinal=valfinal+"'"+FieldData+"',";
  	            	 if((ttemp[i].contains("MobileNo."))&&(FieldData.length()!=0)){
  						firstLetter = String.valueOf(FieldData.charAt(0));
  					 }
  	            	 
  	            	 if((ttemp[i].contains("Name"))&&(FieldData.length()==0)){
  	            		checkval=1;
  	            		callalert("Please input Name",2); 
  	            		break;
  	            	 }
  	            	 else if((ttemp[i].contains("City"))&&(FieldData.length()==0)){
   	            		checkval=1;
   	            		callalert("Please input City",2); 
  	            		break;
   	            	 }
  	            	 else if((ttemp[i].contains("MobileNo."))&&(FieldData.length()!=10)){
  	            		checkval=1;
  	            		callalert("Please input valid 10 digits Mobile No.",2); 
  	            		break;
  	            	 }
  	            	 else if((ttemp[i].contains("MobileNo."))&&(firstLetter.equals("0"))){
  	            		checkval=1;
  	            		allEds.get(i).setText("");
  	            		callalert("Mobile No. cannot start with zero",2); 
  	            		break;
  	            	 }
  	            	 else if((ttemp[i].contains("EmailID"))&&(FieldData.length()==0)){
   	            		checkval=1;
   	            		callalert("Please input EmailID",2); 
  	            		break;
   	            	 }
  	            	 else if((ttemp[i].contains("EmailID"))&&(FieldData.length()>0) && !ChkEmail(FieldData)){
  	            		checkval=1;
  	            		callalert("Please input valid EmailID",2); 
  	            		break;
  	            	 }
	        	   }
	        	   ///////////////////////////////
	        	   
	        	   //Get Data From Spinner Array List
	        	   if(checkval==0)
	        	   {
	        	     ttemp=StrCaption2.split("#");
	        	   
	        	     for(int i=0; i < allSps.size(); i++){
  	            	   FieldData = allSps.get(i).getSelectedItem().toString().trim();
  	            	   if(FieldData.length()==0)
  	            	   {
  	            		 checkval=1;
  	            		 callalert("Please select "+ttemp[i].toLowerCase(),2); 
  	            		 break;
  	            	   }
  	            	   else
  	            	   {
  	            	     shrvaleditor=shrvaleditor+ttemp[i]+":"+FieldData+"@##@";
  	            	     valfinal=valfinal+"'"+FieldData+"',";
  	            	   }
	        	     }  
	        	   }
                   ///////////////////////////////////
	        	   
	        	   if(checkval==0)
	        	   {
	        		   if(shrvaleditor.length()!=0){
		        		  shrvaleditor = shrvaleditor.substring(0, shrvaleditor.length()-4).trim();
					   } 
			           if(valfinal.length()!=0){
			              valfinal = valfinal.substring(0, valfinal.length()-1).trim();
					   } 
			           
			           String tt=valfinal.replace("'", "").trim();
			           tt=tt.replace(",", "").trim();
			           if(tt.length()==0){
			              callalert("Input atleast one field.",2);
			           }else{
			              //StrTotalfinal=Strfinal+"#"+valfinal;
				          editor = sharedprf.edit();
					   	  editor.putString("EDITVALUE",shrvaleditor); //TCount_Tab2 is the Total Records of Table 2
					   	  editor.commit();
					   	  InternetPresent =chkconn.isConnectingToInternet(context);
					   	  if(InternetPresent==true){
					   		progressdial();
							     networkThread = new Thread()
							     {
							         public void run()
							         {
							             try
							               {
							            	 WEbResponseReg=webcall.clubREGSave(Strclub, Str_IEMI, Strfinal, valfinal);
								              runOnUiThread(new Runnable()
								              {
								            	  public void run()
								            	  {
								            		  if(WEbResponseReg.equalsIgnoreCase("Saved")){
								            			  System.out.println("Save    :  "+WEbResponseReg);
								            			  callalert("Register Successfully !!",1) ;
								            		  }else{
								            			  callalert("Some error,Please try later..",2) ;   
								            		  }
								                  }
								             });
							              Progsdial.dismiss();  
							              return;
							             }
							             catch (Exception localException)
							             {
							              System.out.println(localException.getMessage());
							             }
							          }
							       };
							       networkThread.start(); 
					   		 }else{
					   			 callalert("Cannot Register in offline mode.",1) ; 
					   		 } 
	        	        }
	        	   
		             }
	           }
		 });
	}
	
	
	 private void getwebResultandsetvalue()
	 {
		 if((WEbResult!=null)||(WEbResult.contains("#"))){
				LaynoRecd.setVisibility(View.GONE);
				RlayVal.setVisibility(View.VISIBLE);
				////////////split record//////////////
				allEds=null;
				allSps=null;
		  		allEds = new ArrayList<EditText>();//Array List Of EditText
		  		allSps = new ArrayList<Spinner>();//Array List Of Spinner(Combos)
		  		Get_EditText_Sharedvalue();
				temp = WEbResult.split("#"); 
	            
				for(int i=0;i<temp.length; i++){
		         	 s= temp[i].toString().replace("^", "#")+" ";
		             sp=s.trim().split("#");   
		             caption=sp[0].toString().trim();
		             value=sp[1].toString().trim();
		             String ChkCombo="";
		             if(sp.length==3)
		            	ChkCombo=sp[2].toString().trim();
		             int flag=0;
		             if(StrEditVAl.length()!=0){
		            	 String[] temp1=StrEditVAl.split("@##@");  
		            	 for(int j=0;j<temp1.length;j++){
		            		 //System.out.println("cap:::  "+temp1[j]);
		            		 if(temp1[j].contains(caption)){
		            			 temp1[j]= temp1[j].replace(":", "#")+" ";
		            			 String[] sp1=temp1[j].split("#"); 
		            			 if((caption.contains("MobileNo."))&&(UID!=null)){
				            		 layMainReg.addView(NewView(i+100,caption,UID,ChkCombo));   
				            	 }else{
				            		 layMainReg.addView(NewView(i+100,caption,sp1[1].trim(),ChkCombo));
				            	 }
		            			 // System.out.println("cap:::  "+sp1[1]);
		            			 flag=1;
		            			 break;
		            		 }
			             }
		            	 if(flag==0)
		            	  layMainReg.addView(NewView(i+100,caption,"",ChkCombo)); 
		             }else{
		            	 if((caption.contains("MobileNo."))&&(UID!=null))
		            		 layMainReg.addView(NewView(i+100,caption,UID,ChkCombo));   
		            	 else
		            		 layMainReg.addView(NewView(i+100,caption,"",ChkCombo));  
		             }

		             if(!ChkCombo.equals("LABEL")) {

						 Strfinal = Strfinal + value + ",";

						 if (ChkCombo.contains("Combo"))
							 StrCaption2 = StrCaption2 + caption + "#";
						 else
							 StrCaption1 = StrCaption1 + caption + "#";
					 }
	             }
	             
	             if(Strfinal.length()!=0)
	            	Strfinal = Strfinal.substring(0, Strfinal.length()-1).trim();
	             
	             if(StrCaption1.length()!=0)
	            	StrCaption1 = StrCaption1.substring(0, StrCaption1.length()-1).trim();
	             
	             if(StrCaption2.length()!=0)
		            StrCaption2 = StrCaption2.substring(0, StrCaption2.length()-1).trim();
			}else{
				LaynoRecd.setVisibility(View.VISIBLE);
				RlayVal.setVisibility(View.GONE);
			}
	 }
	
	 private void Set_App_Logo_Title()
	 {
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
	 
	 private void Get_EditText_Sharedvalue()
	 {
	 	 sharedprf = getSharedPreferences("MyEditPref", Context.MODE_PRIVATE);
		  if (sharedprf.contains("EDITVALUE"))
	      {
			  StrEditVAl=sharedprf.getString("EDITVALUE", "");
	      }
		  
		 sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		  if (sharedpreferences.contains("userid"))
	      {
	    	  UID=sharedpreferences.getString("userid", "");
	      }	
	 }
	 
	 private LinearLayout NewView(int id,String TvTitle,String DValue,String ChkCombo) {
		 LinearLayout L1 = new LinearLayout(this);
		 LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		 params.setMargins(5, 5, 5, 5);
		 L1.setLayoutParams(params);
		 L1.setId(id);
		 L1.setOrientation(android.widget.LinearLayout.HORIZONTAL);

		 //Add Textview
		 TextView tv = new TextView(this);
		 tv.setTextSize(18);
		 tv.setText(TvTitle);
		 tv.setTextColor(Color.BLUE);
		 tv.setTypeface(Typeface.DEFAULT_BOLD);
		 L1.addView(tv);

		 if (!ChkCombo.equals("LABEL")) {
			 if (ChkCombo.contains("Combo")) {
				 CallWebCombosFill(L1, id, TvTitle, DValue);
			 } else {
				 //Add Edittext
				 L1.addView(editText(id, TvTitle, DValue));
			 }
		 }

		return L1;
	 }
	 
	 
	 private EditText editText(int id,String TvTitle,String DValue) {
		final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		EditText ET = new EditText(this);
		allEds.add(ET);//Add EditText in Edittext List
		ET.setLayoutParams(lparams);
        ET.setId(id);
        ET.setLeft(5);
        ET.setHint("input "+TvTitle);
        ET.setText(DValue);
        return ET;
	 }
	 
	 
	 // Call Get FillCombo Webservice Without Thread
	 private void CallWebCombosFill(LinearLayout L1,int id,String TvTitle,String DValue)
	 {
		 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	     StrictMode.setThreadPolicy(policy);
           try
           {
       	      String Combodata=webcall.Get_FillCombo(Strclub, Str_IEMI, "Combo_"+TvTitle);
       	     
      		  if(Combodata.contains(",")){
      		    String[] Arr1=Combodata.split(",");
			    L1.addView(Combos(id,TvTitle,DValue,Arr1));//Add Spinner
      		  }
      		  else
      		  {
  				L1.addView(editText(id,TvTitle,DValue));//Add Edittext
      		  }
           }
           catch (Exception ex)
           {
          	 //System.out.println(ex.getMessage());
           }
     }
	 
	 
	 private Spinner Combos(int id,String TvTitle,String DValue,String[] SpArr) {
		ArrayAdapter<String> ArrAdp= new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,SpArr);
		ArrAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		Spinner spn = new Spinner(this);
		allSps.add(spn);// Add EditText in Edittext List
		spn.setLayoutParams(lparams);
		spn.setId(id);
		spn.setAdapter(ArrAdp);
		if((DValue!=null)||(DValue.length()!=0)){
			spn.setSelection(((ArrayAdapter<String>)spn.getAdapter()).getPosition(DValue));
		}
        return spn;
	 }
	 
	 
	 private boolean ChkEmail(String Email)
	 {
		//String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
		 String emailPattern="^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		// onClick of button perform this simplest code.
		if (Email.matches(emailPattern))
		    return true;
		else
			return false;
	 }
	 
	 
	 protected void progressdial()
     {
     	Progsdial = new ProgressDialog(this, R.style.MyTheme);
     	Progsdial.setTitle("App Loading");
     	Progsdial.setMessage("Please Wait....");
     	Progsdial.setIndeterminate(true);
     	Progsdial.setCancelable(false);
     	Progsdial.getWindow().setGravity(Gravity.DISPLAY_CLIP_VERTICAL);
     	Progsdial.show();
     } 
	 
	@SuppressWarnings("deprecation")
	private void callalert(String body,final int ckk){
	    	ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>"+body+"</font>"));
			ad.setButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	if(ckk==1){
	            		finish();
	            	}else if(ckk==2){
	            		ad.dismiss();
	            	}
	            }
	        });
	        ad.show();	
	}
		
}
