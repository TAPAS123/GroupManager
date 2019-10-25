package group.manager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemSelectedListener;

public class UpdateMemberProfile extends Activity {

	byte[] AppLogo;
    ImageView imageViewUpdate,ImgVwPersonal;
    Intent menuIntent;
    SQLiteDatabase db;
    private static final int CAMERA_REQUEST = 1;
	private static final int PICK_FROM_GALLERY = 2;
    Cursor cursorT;
   int post,count;
   Context context=this;
   AlertDialog ad;
   AlertDialog.Builder alertDialogBuilder3;
   String Log,ClubName,logid,Str_user,TableFamilyName,STRM_ID,
   sqlSearch="",StrName="",StrRelation="",StrFather="",StrMother="",StrLoc="",StrMob1="",StrMob2="",StrDobD="",StrDobM="",StrDobY="",StrEmail="",StrAge="",
   StrEducation="",StrWorkwith="",StrGender="",StrShareWithMatrimony="",StrDesignation="",TotalM_Bro="0",TotalM_Sis="0";
   EditText txttbName,txttbRl,txttbfather,txttbMother,txttbloc,TxttbMob,TxttbMob2,TxttbEmail,Txttbday,Txttbmonth,Txttbyear,Txttbage,TxtDesignation,EtBirthTime;
   RadioGroup rdGrpGender;
   RadioButton rdbtnMale,rdbtnFemale;
   CheckBox chkShareMatrimony;
   Spinner SpEducation,SpWorkingWith;
   byte[] PersonImg;
   Button btnMarriagePurposeDetails;
   String [] ArrEducation={"- - Education - -","Bachelors","Masters","Doctorate","Diploma","Postgraduate","Undergraduate","Associates degree","Honours degree","Trade school","High school","Less than high school"};
   String [] ArrWorkWith={"- - Working With - -","Business / Self Employed","Private Company","Government / Public Sector","Defence / Civil Services","Not Working"};
   String[] ArrMarriageDetails=new String[14];
   
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.updatememberprofile);
	    
	    ImgVwPersonal = (ImageView)findViewById(R.id.ivmainperson);
	    imageViewUpdate = (ImageView)findViewById(R.id.ivupdatemain);
	    txttbName=(EditText)findViewById(R.id.etname);
	    txttbRl=(EditText)findViewById(R.id.etrelation);
	    txttbfather=(EditText)findViewById(R.id.etfathername);
	    txttbMother=(EditText)findViewById(R.id.etmothername);
	    txttbloc=(EditText)findViewById(R.id.etmcurrLocation);
		TxttbMob=(EditText)findViewById(R.id.etmob1);
		TxttbMob2=(EditText)findViewById(R.id.etmob2);
		TxttbEmail=(EditText)findViewById(R.id.etmailid);
		Txttbday=(EditText)findViewById(R.id.etDayp);
		Txttbmonth=(EditText)findViewById(R.id.eddmonthP);
		Txttbyear=(EditText)findViewById(R.id.edyearP);
		Txttbage=(EditText)findViewById(R.id.etage);
		
		SpEducation=(Spinner)findViewById(R.id.Sp_Education);//Added 02-06-2016
		SpWorkingWith=(Spinner)findViewById(R.id.Sp_WorkingWith);//Added 02-06-2016
		TxtDesignation=(EditText)findViewById(R.id.EtDesignation);//Added 02-06-2016
		
		rdGrpGender = (RadioGroup) findViewById(R.id.rdGrpGender);//Added 02-06-2016
		rdbtnMale = (RadioButton) findViewById(R.id.rdbtnMale);//Added 02-06-2016
		rdbtnFemale = (RadioButton) findViewById(R.id.rdbtnFemale);//Added 02-06-2016
		chkShareMatrimony=(CheckBox)findViewById(R.id.chkShareMatrimony);//Added 02-06-2016
		btnMarriagePurposeDetails=(Button)findViewById(R.id.btnAddMarriagePurposeDetails);//Added 02-06-2016
		
		btnMarriagePurposeDetails.setVisibility(View.INVISIBLE);
		
		menuIntent = getIntent(); 
		Log =  menuIntent.getStringExtra("Clt_Log");
		logid =  menuIntent.getStringExtra("Clt_LogID");
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		Str_user =  menuIntent.getStringExtra("UserClubName");
		STRM_ID =  menuIntent.getStringExtra("Pwd");
		post =  menuIntent.getIntExtra("POstion", post);
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		TableFamilyName="C_"+Str_user+"_Family";
		
		Set_App_Logo_Title(); // Set App Logo and Title
		
		/// Set Education Spinner
		ArrayAdapter<String> adp1= new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,ArrEducation);
		adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		SpEducation.setAdapter(adp1);
		
		//Set Working with Spinner
		ArrayAdapter<String> adp2= new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,ArrWorkWith);
		adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		SpWorkingWith.setAdapter(adp2);
		
		if(post!=-1)
		{
			imageViewUpdate.setImageResource(R.drawable.update);
			db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
	        sqlSearch="Select Name,Relation,Father,Mother,Current_Loc,Mob_1,Mob_2,DOB_D,DOB_M,DOB_Y,EmailId,Age,"+
	        	    "Education,Work_Profile,Text2,Text3,Text4,Pic,Gotra,Birth_Time,Birth_Place,Text5,Height," +
	        		"Text6,Text7,Text8,Text9,Text10,Text11,Text12,Text13,Text14 from "+TableFamilyName+" where M_id="+STRM_ID;
			System.out.println(sqlSearch);
		    cursorT = db.rawQuery(sqlSearch, null);
		    if (cursorT.moveToFirst()) {
				   do {
					   StrName=Chkval(cursorT.getString(0));
					   StrRelation =Chkval(cursorT.getString(1));
					   StrFather =Chkval(cursorT.getString(2));
					   StrMother=Chkval(cursorT.getString(3));
					   StrLoc =Chkval(cursorT.getString(4));
					   StrMob1=Chkval(cursorT.getString(5));
					   StrMob2 =Chkval(cursorT.getString(6));
					   StrDobD =Chkval(cursorT.getString(7));
					   StrDobM=Chkval(cursorT.getString(8));
					   StrDobY =Chkval(cursorT.getString(9));
					   StrEmail =Chkval(cursorT.getString(10));
					   StrAge=Chkval(cursorT.getString(11));
					   StrEducation =Chkval(cursorT.getString(12));
					   StrWorkwith =Chkval(cursorT.getString(13));
					   StrGender =Chkval(cursorT.getString(14));
					   StrShareWithMatrimony =Chkval(cursorT.getString(15));
					   StrDesignation =Chkval(cursorT.getString(16));
					   PersonImg=cursorT.getBlob(17);//Image
					   
					   ArrMarriageDetails[0] =Chkval(cursorT.getString(18));//get Gotra
					   ArrMarriageDetails[1] =Chkval(cursorT.getString(19));//get Birth_Time
					   ArrMarriageDetails[2] =Chkval(cursorT.getString(20));//get Birth_Place
					   ArrMarriageDetails[3] =Chkval(cursorT.getString(21));//get Native Place
					   ArrMarriageDetails[4] =Chkval(cursorT.getString(22));//get Height
					   ArrMarriageDetails[5] =Chkval(cursorT.getString(23));//get Manglik
					   ArrMarriageDetails[6] =Chkval(cursorT.getString(24));//get AnnualIncome
					   ArrMarriageDetails[7] =Chkval(cursorT.getString(25));//get WorkAfterMarriage
					   ArrMarriageDetails[8] =Chkval(cursorT.getString(26));//get Diet
					   ArrMarriageDetails[9] =Chkval(cursorT.getString(27));//get Father's Status
					   ArrMarriageDetails[10] =Chkval(cursorT.getString(28));//get Mother's Status
					   ArrMarriageDetails[11] =Chkval(cursorT.getString(29));//get Brothers all Details
					   ArrMarriageDetails[12] =Chkval(cursorT.getString(30));//get Sisters all Details
					   ArrMarriageDetails[13] =Chkval(cursorT.getString(31));//get Marriage AdditionalInfo

		    		 } while (cursorT.moveToNext());
		    	 }
		    cursorT.close();
		    db.close();
		}else if(post==-1){
			imageViewUpdate.setImageResource(R.drawable.insert);
		}
		
	    txttbName.setText(StrName); 
	    txttbRl.setText(StrRelation);
	    txttbfather.setText(StrFather);
	    txttbMother.setText(StrMother);
	    txttbloc.setText(StrLoc);
	    TxttbMob.setText(StrMob1);
	    TxttbMob2.setText(StrMob2);
	    TxttbEmail.setText(StrEmail); 
	    Txttbday.setText(StrDobD);
	    Txttbmonth.setText(StrDobM);
	    Txttbyear.setText(StrDobY);
	    Txttbage.setText("");
	    TxtDesignation.setText(StrDesignation);
	    
	    if(StrEducation.length()>0)
			SpEducation.setSelection(((ArrayAdapter<String>)SpEducation.getAdapter()).getPosition(StrEducation));
	 
	    if(StrWorkwith.length()>0)
			SpWorkingWith.setSelection(((ArrayAdapter<String>)SpWorkingWith.getAdapter()).getPosition(StrWorkwith));
	    
	    //Set Gender
	    if(StrGender.equals("Male")){
	    	rdbtnMale.setChecked(true);
	    }
	    else if(StrGender.equals("Female")){
	    	rdbtnFemale.setChecked(true);
	    }
	    
	    //Set ShareWithMatimony
	    if(StrShareWithMatrimony.equals("true")){
	    	 chkShareMatrimony.setChecked(true);
	    	 btnMarriagePurposeDetails.setVisibility(View.VISIBLE);
	    }
	    
	    // Set Image
		if((PersonImg!=null)){
			Bitmap bitmap = BitmapFactory.decodeByteArray(PersonImg , 0, PersonImg.length);
			if(bitmap==null){
				ImgVwPersonal.setImageResource(R.drawable.person1);
			}else{
				ImgVwPersonal.setImageBitmap(bitmap);
			}
		}else{
			ImgVwPersonal.setImageResource(R.drawable.person1);
		}
	    
	    // Person Image Click event
	    ImgVwPersonal.setOnClickListener(new OnClickListener()
        { 
			@Override
			public void onClick(View arg0) {
				ImageSeletionDialog();
			}
        });
	    
	    
	    chkShareMatrimony.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        
		    	   if(isChecked)
		    		   btnMarriagePurposeDetails.setVisibility(View.VISIBLE);
		    	   else
		    		   btnMarriagePurposeDetails.setVisibility(View.INVISIBLE);
		    } 
		});
	    
	    
	    
	    btnMarriagePurposeDetails.setOnClickListener(new OnClickListener()
        { 
			@Override
			public void onClick(View arg0) {
				String Name= txttbName.getText().toString().trim();
				 
				// get selected radio button from radioGroup rdGrpGender
				int chkId = rdGrpGender.getCheckedRadioButtonId();//Msg Display 
				// find the radiobutton(Male or Female) by returned id
				RadioButton  rdbtnGender= (RadioButton) findViewById(chkId);
				String Gender="";
				if(rdbtnGender!=null)
				  Gender=rdbtnGender.getText().toString().trim();
				
				if(Name.length()==0){
					txttbName.setError("Please enter name");
					txttbName.requestFocus();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.showSoftInput(txttbName, InputMethodManager.RESULT_HIDDEN);
				}
				else if(Gender.length()==0){
					DisplayMsg("Please select Gender",false);
				}
			    else{
			    	ShowMarriagePurposeDialog(Gender);
			    }
			}	
	    });
	    
	    
	    imageViewUpdate.setOnClickListener(new OnClickListener()
        { 
			@Override
			public void onClick(View arg0) {
				 String Name= txttbName.getText().toString().trim();
				 
				 // get selected radio button from radioGroup rdGrpGender
				 int chkId = rdGrpGender.getCheckedRadioButtonId();//Msg Display 
				 // find the radiobutton(Male or Female) by returned id
				 RadioButton  rdbtnGender= (RadioButton) findViewById(chkId);
				 String Gender="";
				 if(rdbtnGender!=null)
				   Gender=rdbtnGender.getText().toString().trim();
				 
				 String Relation =txttbRl.getText().toString().trim();
				 String father =txttbfather.getText().toString().trim();
				 String mother =txttbMother.getText().toString().trim();
				 String CurLoc= txttbloc.getText().toString().trim();
				 String mob1= TxttbMob.getText().toString().trim();
				 String mob2= TxttbMob2.getText().toString().trim();
				 String Email=TxttbEmail.getText().toString().trim();
				 String DOB_D= Txttbday.getText().toString().trim();
				 String DOB_M= Txttbmonth.getText().toString().trim();
				 String DOB_Y= Txttbyear.getText().toString().trim();
				 //String age= Txttbage.getText().toString().trim();
				 String Education= SpEducation.getSelectedItem().toString().trim();
				 String WorkWith= SpWorkingWith.getSelectedItem().toString().trim();
				 String Designation=TxtDesignation.getText().toString().trim();
				 
				 boolean ShareMatrimony=chkShareMatrimony.isChecked();
				
				 if(Name.length()==0){
					txttbName.setError("Please enter name");
					txttbName.requestFocus();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.showSoftInput(txttbName, InputMethodManager.RESULT_HIDDEN);
				 }
				 else if(Gender.length()==0){
					 DisplayMsg("Please select Gender",false);
				 }
				 else
				 {
				   try
				   {
					 boolean PFlag=true;
					 
					 if(ShareMatrimony)
					 {
						PFlag=false;
						String Gotra= Chkval(ArrMarriageDetails[0]);
			            String BirthTime=Chkval(ArrMarriageDetails[1]);
			            String BirthPlace=Chkval(ArrMarriageDetails[2]);
			            String StrHeight=Chkval(ArrMarriageDetails[4]);
			            if(StrHeight.length()==0)
			            	StrHeight="0";
			            double height=Double.parseDouble(StrHeight);
			            
			            String Manglik=Chkval(ArrMarriageDetails[5]);
			            String FStatus=Chkval(ArrMarriageDetails[9]);
			            String MStatus=Chkval(ArrMarriageDetails[10]);
			            String BroDetails=Chkval(ArrMarriageDetails[11]);
			            if(BroDetails.length()==0)
			            	BroDetails="^^";
			            BroDetails=BroDetails+" ";
		                String[] Arr_Bro=BroDetails.replace("^", "#").split("#");
		                
			            String SisDetails=Chkval(ArrMarriageDetails[12]);
			            if(SisDetails.length()==0)
			            	SisDetails="^^";
			            SisDetails=SisDetails+" ";
		                String[] Arr_Sis=SisDetails.replace("^", "#").split("#");
			            
			            if(Gotra.length()==0)
			            	DisplayMsg("Please input Gotra",false);
			            else if(BirthTime.length()==0)
			            	DisplayMsg("Please input Time of Birth",false);
			            else if(BirthPlace.length()==0)
			            	DisplayMsg("Please input Place of Birth",false);
			            else if(height<=50)
			            	DisplayMsg("Please input Correct Height",false);
			            else if(Manglik.length()==0)
			            	DisplayMsg("Please select Manglik Status",false);
			            else if(FStatus.length()==0)
			            	DisplayMsg("Please select your Father status",false);
			            else if(MStatus.length()==0)
			            	DisplayMsg("Please select your Mother status",false);
			            else if(Arr_Bro[0].trim().length()==0)
			            	DisplayMsg("Please Select No of Brothers",false);
			            else if(Arr_Bro[1].trim().length()==0)
			                DisplayMsg("Please Select No of Married Brothers",false);
			            else if(Arr_Sis[0].trim().length()==0)
			            	DisplayMsg("Please Select No of Sisters",false);
			            else if(Arr_Sis[1].trim().length()==0)
			            	DisplayMsg("Please Select No of Married Sisters",false);
			            else 
			            	PFlag=true;
					 }
					 
					 if(PFlag)
					 {
					   String DispMsg="",M_Id="",Type="";
					   int MaxMid=0;
						
					   db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 

					   if(post==-1)
					   {
						 DispMsg="Member Added Successfully !";
						
						 sqlSearch = "SELECT max(M_Id) from "+TableFamilyName;
	    		    	 cursorT = db.rawQuery(sqlSearch, null);
	    		    	 if (cursorT.moveToNext()) {
	    		    		MaxMid=cursorT.getInt(0);
	    				 } 
	    		    	 cursorT.close();
	    		    	  
	    				  if(MaxMid<1000000){
	    					  MaxMid=1000001;
	    				  }else{
	    					  MaxMid=MaxMid+1;
	    				  }
	    				  
	    				  M_Id=MaxMid+"";
	    				  Type="Insert";
					  }
					  else
					  {
						 DispMsg="Member Updated Successfully !";
						 
						 sqlSearch="Select count(M_id) from "+TableFamilyName+" where M_id="+STRM_ID;
						 cursorT = db.rawQuery(sqlSearch, null);
						 if(cursorT.moveToFirst()){
							 count=cursorT.getInt(0);
						 }
						 cursorT.close();
						  
						 if(count>0)
						 { 
							  M_Id=STRM_ID;
							  Type="Update";
						 }
					 }
					 db.close();// Close dbHandler Class object
						 
					 String[] Arr=new String[18];
    				  Arr[0]=M_Id;
    				  Arr[1]=logid;
    				  Arr[2]=Name;
    				  Arr[3]=Relation;
    				  Arr[4]=father;
    				  Arr[5]=mother;
    				  Arr[6]=CurLoc;
    				  Arr[7]=mob1;
    				  Arr[8]=mob2;
    				  Arr[9]=DOB_D;
    				  Arr[10]=DOB_M;
    				  Arr[11]=DOB_Y;
    				  Arr[12]=Email;
    				  Arr[13]=Education;
    				  Arr[14]=WorkWith;
    				  Arr[15]=Gender;
    				  Arr[16]=ShareMatrimony+"";
    				  Arr[17]=Designation;
    				  
    				  if(Type.length()>1){
    				    DbHandler dbhdlObj = new DbHandler(context,"");
    				    dbhdlObj.Insert_UpdateFamilyMember(Type,TableFamilyName, Arr,ArrMarriageDetails,PersonImg);
    				    dbhdlObj.close();
    				    DisplayMsg(DispMsg,true);// Display success Msg
    				  }
				   }
				 }catch(Exception ex){
						 System.out.println(ex.getMessage());
				 }
			  }
			}
        });
		
	}
	
	
	
	// Display Popup Screen of Add Details for Marriage Purpose
	private void ShowMarriagePurposeDialog(String Gender)
	{
   	     final Dialog dialog = new Dialog(context);
	     //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
 		 dialog.setContentView(R.layout.marriage_purpose_details);
 		 dialog.setCancelable(false);
 		 dialog.setTitle("Marriage Purpose Details");
 		 dialog.show();
 		 
 		 Button btnBack=(Button)dialog.findViewById(R.id.btnBack);
 		 
 		 //EditText
 		 final EditText EtGotra=(EditText)dialog.findViewById(R.id.EtGotra);
 		 EtBirthTime=(EditText)dialog.findViewById(R.id.EtBirthTime);
 		 final EditText EtBirthPlace=(EditText)dialog.findViewById(R.id.EtBirthPlace);
 		 final EditText EtNativePlace=(EditText)dialog.findViewById(R.id.EtNativePlace);
 		 final EditText Etdetails_about_brothers=(EditText)dialog.findViewById(R.id.Etdetails_about_brothers);
 		 final EditText Etdetails_about_sisters=(EditText)dialog.findViewById(R.id.Etdetails_about_sisters);
 		 final EditText EtAdditionalInfo=(EditText)dialog.findViewById(R.id.EtAddInfo);
 		 
 		 //LinearLayout
 		 LinearLayout LLWorkAfterMarriage=(LinearLayout)dialog.findViewById(R.id.LLWorkAfterMarriage);
 		 final LinearLayout LLMarriedBrother=(LinearLayout)dialog.findViewById(R.id.LLMarriedBrothers);
 		 final LinearLayout LLMarriedSister=(LinearLayout)dialog.findViewById(R.id.LLMarriedSister);
 		 
 		 LLWorkAfterMarriage.setVisibility(View.GONE);
		 LLMarriedBrother.setVisibility(View.GONE);
		 LLMarriedSister.setVisibility(View.GONE);
		 
		 if(Gender.equals("Female")){
	 			LLWorkAfterMarriage.setVisibility(View.VISIBLE);
	 	 }
 		 
 		 //Textview Mandatory
 		 TextView TvGotra=(TextView)dialog.findViewById(R.id.TvGotra);
 		 TextView TvBirthTime=(TextView)dialog.findViewById(R.id.TvBirthTime);
 		 TextView TvBirthPlace=(TextView)dialog.findViewById(R.id.TvBirthPlace);
 		 TextView TvHeight=(TextView)dialog.findViewById(R.id.TvHeight);
 		 TextView TvManglik=(TextView)dialog.findViewById(R.id.TvManglik);
 		 TextView TvFatherStatus=(TextView)dialog.findViewById(R.id.TvFatherStatus);
 		 TextView TvMotherStatus=(TextView)dialog.findViewById(R.id.TvMotherStatus);
 		 TextView TvTotalBrothers=(TextView)dialog.findViewById(R.id.TvTotalBrothers);
		 TextView TvTotalMarriedBrothers=(TextView)dialog.findViewById(R.id.TvTotalMarriedBrothers);
		 TextView TvTotalSisters=(TextView)dialog.findViewById(R.id.TvTotalSisters);
 		 TextView TvTotalMarriedSisters=(TextView)dialog.findViewById(R.id.TvTotalMarriedSisters);
 		 
 		 TvGotra.setText(Html.fromHtml("Gotra <font color='#FE1502'>*</font>"));
 		 TvBirthTime.setText(Html.fromHtml("Time of Birth <font color='#FE1502'>*</font>"));
 		 TvBirthPlace.setText(Html.fromHtml("Place of Birth <font color='#FE1502'>*</font>"));
 		 TvHeight.setText(Html.fromHtml("Height <font color='#FE1502'>*</font>"));
 		 TvManglik.setText(Html.fromHtml("Manglik <font color='#FE1502'>*</font>"));
 		 TvFatherStatus.setText(Html.fromHtml("Father's Status <font color='#FE1502'>*</font>"));
 		 TvMotherStatus.setText(Html.fromHtml("Mother's Status <font color='#FE1502'>*</font>"));
 		 TvTotalBrothers.setText(Html.fromHtml("No. of brothers <font color='#FE1502'>*</font>"));
 		 TvTotalMarriedBrothers.setText(Html.fromHtml("of which married <font color='#FE1502'>*</font>"));
 		 TvTotalSisters.setText(Html.fromHtml("No. of sisters <font color='#FE1502'>*</font>"));
 		 TvTotalMarriedSisters.setText(Html.fromHtml("of which married <font color='#FE1502'>*</font>"));
 		 
 		 ///Spinner//
 		 final Spinner Sp_HeightFeet=(Spinner)dialog.findViewById(R.id.Sp_HeightFeet);
 		 final Spinner Sp_HeightInch=(Spinner)dialog.findViewById(R.id.Sp_HeightInch);
 		 final Spinner Sp_Manglik=(Spinner)dialog.findViewById(R.id.Sp_Manglik);
 		 final Spinner Sp_WorkAfterMarriage=(Spinner)dialog.findViewById(R.id.Sp_WorkAfterMarriage);
 		 final Spinner Sp_AnnualIncome=(Spinner)dialog.findViewById(R.id.Sp_AnnualIncome);
 		 final Spinner Sp_Diet=(Spinner)dialog.findViewById(R.id.Sp_Diet);
 		 final Spinner Sp_FatherStatus=(Spinner)dialog.findViewById(R.id.Sp_FatherStatus);
 		 final Spinner Sp_MotherStatus=(Spinner)dialog.findViewById(R.id.Sp_MotherStatus);
 		 final Spinner Sp_TotalBrothers=(Spinner)dialog.findViewById(R.id.Sp_TotalBrothers);
 		 final Spinner Sp_TotalMarriedBrothers=(Spinner)dialog.findViewById(R.id.Sp_TotalMarriedBrothers);
 		 final Spinner Sp_TotalSisters=(Spinner)dialog.findViewById(R.id.Sp_TotalSisters);
 		 final Spinner Sp_TotalMarriedSisters=(Spinner)dialog.findViewById(R.id.Sp_TotalMarriedSisters);
 		 
 		 String[] ArrHeightFeet={"- -Select- -","3","4","5","6","7"};
 		 String[] ArrHeightInch={"0","1","2","3","4","5","6","7","8","9","10","11"};
 		 String[] ArrManglik={"- -Select- -","Non Manglik","Manglik","Angshik (Partial Manglik)"};
 		 String[] ArrWorkAfterMarriage={"- -Select- -","Doesn't Matter","Yes","No","Undecided"};
 		 String[] ArrAnnualIncome={"- -Select- -","Less than 1,00,000","1,00,000 to 5,00,000","5,00,000 to 10,00,000","10,00,000 to 20,00,000","More than 20,00,000"};
 		 String[] ArrDiet={"- -Select- -","Vegetarian","Non Vegetarian","Occassionally Non Vegetarian","Eggetarian","Jain","Vegan"};
 		 String[] ArrFatherStatus={"- -Select- -","Employed","Business","Retired","Not Employed","Passed away"};
 		 String[] ArrMotherStatus={"- -Select- -","Employed","Business","Retired","Homemaker","Passed away"};
 		 
 		 String[] Arr1=new String[12];
 		 Arr1[0]="- -Select- -";
 		 for(int i=1;i<12;i++){
 			int a=i-1;
 			Arr1[i]=a+"";
 		 }
 		 
 		 /// Set Height Feet  Spinner
 		 SetSpinnerAdp(Sp_HeightFeet,ArrHeightFeet);
 		 
 		 /// Set Height Inch Spinner
 		 SetSpinnerAdp(Sp_HeightInch,ArrHeightInch);
 		 
 		 /// Set Manglik Spinner
 		 SetSpinnerAdp(Sp_Manglik,ArrManglik);
 		
 		 //Set WorkAfterMarriage Spinner
 		 SetSpinnerAdp(Sp_WorkAfterMarriage,ArrWorkAfterMarriage);
 		 
 		 //Set AnnualIncome Spinner
 		 SetSpinnerAdp(Sp_AnnualIncome,ArrAnnualIncome);
 		 
 		 //Set Diet Spinner
 		 SetSpinnerAdp(Sp_Diet,ArrDiet);
 		 
 		 //Set Father Status Spinner
 		 SetSpinnerAdp(Sp_FatherStatus,ArrFatherStatus);
 		 
 		 //Set Mother Status Spinner
 		 SetSpinnerAdp(Sp_MotherStatus,ArrMotherStatus);
 		 
 		 //Set Total Brothers Spinner
 		 SetSpinnerAdp(Sp_TotalBrothers,Arr1);
 		//Set Total Sisters Spinner
 		 SetSpinnerAdp(Sp_TotalSisters,Arr1);
 		 
 		 
 		 String[] A1=new String[1];
		 A1[0]="- -Select- -";
 		 //Set Total Married Brothers Spinner
 		 SetSpinnerAdp(Sp_TotalMarriedBrothers,A1);
 		 
 		 //Set Total Married Sisters Spinner
 		 SetSpinnerAdp(Sp_TotalMarriedSisters,A1);
 		 
 		 
 		 ///Fill Values from ArrMarriageDetails
  		 EtGotra.setText(Chkval(ArrMarriageDetails[0]));//Set Gotra
  		 EtBirthTime.setText(Chkval(ArrMarriageDetails[1]));//Set BirthTime
  		 EtBirthPlace.setText(Chkval(ArrMarriageDetails[2]));//Set BirthPlace
  		 EtNativePlace.setText(Chkval(ArrMarriageDetails[3]));//Set NativePlace
         String Height=Chkval(ArrMarriageDetails[4]);
         if(Height.length()>0)
         {
         	//convert height from cm to inches
         	double TotalInch=Double.parseDouble(Height)/2.54;
         	int feet=(int)TotalInch/12;
         	int inches=(int)TotalInch%12;
         	
         	if(feet>=3)
         	  SetSpinnerSelection(Sp_HeightFeet,feet+"");//Set Feet
         	
         	SetSpinnerSelection(Sp_HeightInch,inches+"");//Set Inches
         }
         
         SetSpinnerSelection(Sp_Manglik,Chkval(ArrMarriageDetails[5]));//Set Manglik
         SetSpinnerSelection(Sp_AnnualIncome,Chkval(ArrMarriageDetails[6]));//Set AnnualIncome
         SetSpinnerSelection(Sp_WorkAfterMarriage,Chkval(ArrMarriageDetails[7]));//Set WorkAfterMarriage
         SetSpinnerSelection(Sp_Diet,Chkval(ArrMarriageDetails[8]));//Set Diet
         SetSpinnerSelection(Sp_FatherStatus,Chkval(ArrMarriageDetails[9]));//Set Father's Status
         SetSpinnerSelection(Sp_MotherStatus,Chkval(ArrMarriageDetails[10]));;//Set Mother's Status
         
         String BroDetails=Chkval(ArrMarriageDetails[11]);
         
         if(BroDetails.contains("^")){
           BroDetails=BroDetails+" ";
           String[] ArrBro=BroDetails.replace("^", "#").split("#");
           SetSpinnerSelection(Sp_TotalBrothers,Chkval(ArrBro[0]));//Set Total Brothers
           TotalM_Bro=Chkval(ArrBro[1]);//Set Total Married Brothers
           //SetSpinnerSelection(Sp_TotalMarriedBrothers,Chkval(ArrBro[1]));//Set Total Married Brothers
           Etdetails_about_brothers.setText(Chkval(ArrBro[2]));//Set Brothers Details
         }
         
         String SisDetails=Chkval(ArrMarriageDetails[12]);
         if(SisDetails.contains("^")){
           SisDetails=SisDetails+" ";
           String[] ArrSis=SisDetails.replace("^", "#").split("#");
           SetSpinnerSelection(Sp_TotalSisters,Chkval(ArrSis[0]));//Set Total Sisters
           TotalM_Sis=Chkval(ArrSis[1]);//Set Total Married Sisters
           //SetSpinnerSelection(Sp_TotalMarriedSisters,Chkval(ArrSis[1]));//Set Total Married Sisters
           Etdetails_about_sisters.setText(Chkval(ArrSis[2]));//Set Sisters Details
         }
         
         EtAdditionalInfo.setText(Chkval(ArrMarriageDetails[13]));//Set Additional Details
  		 /////////////////////////////////////////////////////////////////////////////// 
 		 
         //Click Event EditText Birth Time
         EtBirthTime.setOnClickListener(new OnClickListener()
         { 
 			@Override
 			public void onClick(View arg0) {
 				SetBirthTime();
 			}
 		});
         
         
 		// Total Brothers Spinner On Item Selected Listener
 		Sp_TotalBrothers.setOnItemSelectedListener(new OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String str1=parentView.getItemAtPosition(position).toString();
                if(!str1.equals("0") && !str1.equals("- -Select- -")){
                	LLMarriedBrother.setVisibility(View.VISIBLE);
                	
                	int P=Integer.parseInt(str1.trim())+2;
                	String[] Arr1=new String[P];
            		 Arr1[0]="- -Select- -";
            		 for(int i=1;i<P;i++){
            			int a=i-1;
            			Arr1[i]=a+"";
            		 }
                	 //Set Total Married Brothers Spinner
            		 SetSpinnerAdp(Sp_TotalMarriedBrothers,Arr1);
            		 //Set Total Married Brothers Selected Value
            		 SetSpinnerSelection(Sp_TotalMarriedBrothers,Chkval(TotalM_Bro));
                }else{
                	LLMarriedBrother.setVisibility(View.GONE);
                	Sp_TotalMarriedBrothers.setSelection(0); // sets the first item has selected item from TotalMarriedBrothers spinner.
                }
             }

             @Override
             public void onNothingSelected(AdapterView<?> parentView) {
                 // your code here
             }
         });
 		
 		
 	    // Total Sisters Spinner On Item Selected Listener
 		Sp_TotalSisters.setOnItemSelectedListener(new OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String str1=parentView.getItemAtPosition(position).toString();
                if(!str1.equals("0") && !str1.equals("- -Select- -")){
                	LLMarriedSister.setVisibility(View.VISIBLE);
                	
                	 int P=Integer.parseInt(str1.trim())+2;
                	 String[] Arr1=new String[P];
            		 Arr1[0]="- -Select- -";
            		 for(int i=1;i<P;i++){
            			int a=i-1;
            			Arr1[i]=a+"";
            		 }
                	 //Set Total Married Sisters Spinner
            		 SetSpinnerAdp(Sp_TotalMarriedSisters,Arr1);
            		 //Set Total Married Sisters Selected Value
            		 SetSpinnerSelection(Sp_TotalMarriedSisters,Chkval(TotalM_Sis));
                }else{
                	LLMarriedSister.setVisibility(View.GONE);
                	Sp_TotalMarriedSisters.setSelection(0); // sets the first item has selected item from TotalMarriedSisters spinner.
                }
             }

             @Override
             public void onNothingSelected(AdapterView<?> parentView) {
                 // your code here
             }
         });
 		 
 		
 		btnBack.setOnClickListener(new OnClickListener() {    	
            @Override
            public void onClick(View v) {
            	
            	String Gotra=EtGotra.getText().toString().trim();
            	String BirthTime=EtBirthTime.getText().toString().trim();
            	String BirthPlace=EtBirthPlace.getText().toString().trim();
            	String NativePlace=EtNativePlace.getText().toString().trim();
            	String HeightFeet=Sp_HeightFeet.getSelectedItem().toString().trim();
            	String HeightInch=Sp_HeightInch.getSelectedItem().toString().trim();
            	
            	String Manglik=Sp_Manglik.getSelectedItem().toString().trim();
            	String AnnualIncome=Sp_AnnualIncome.getSelectedItem().toString().trim();
            	String WorkAfterMarriage=Sp_WorkAfterMarriage.getSelectedItem().toString().trim();
            	String Diet=Sp_Diet.getSelectedItem().toString().trim();
            	
            	String FStatus=Sp_FatherStatus.getSelectedItem().toString().trim();
            	String MStatus=Sp_MotherStatus.getSelectedItem().toString().trim();
            	String T_Bro=Sp_TotalBrothers.getSelectedItem().toString().trim();
            	String T_Married_Bro=Sp_TotalMarriedBrothers.getSelectedItem().toString().trim();
            	String details_about_brothers=Etdetails_about_brothers.getText().toString().trim();
            	String T_Sis=Sp_TotalSisters.getSelectedItem().toString().trim();
            	String T_Married_Sis=Sp_TotalMarriedSisters.getSelectedItem().toString().trim();
            	String details_about_sisters=Etdetails_about_sisters.getText().toString().trim();
            	
            	String AdditionalInfo=EtAdditionalInfo.getText().toString().trim();
            	
            	if(HeightFeet.contains("Select"))
            		HeightFeet="0";
            	
            	int feet=Integer.parseInt(HeightFeet);
            	int inch=Integer.parseInt(HeightInch);
            	
            	double Height=(double)((feet*12)+inch) * 2.54; // Height Convert in Centi Meters(cm)
            	
            	if(Manglik.contains("Select"))
            		Manglik="";
            	if(AnnualIncome.contains("Select"))
            		AnnualIncome="";
            	if(WorkAfterMarriage.contains("Select"))
            		WorkAfterMarriage="";
            	if(Diet.contains("Select"))
            		Diet="";
            	if(FStatus.contains("Select"))
            		FStatus="";
            	if(MStatus.contains("Select"))
            		MStatus="";
            	if(T_Bro.contains("Select"))
            		T_Bro="";
            	if(T_Married_Bro.contains("Select"))
            		T_Married_Bro="";
            	if(T_Sis.contains("Select"))
            		T_Sis="";
            	if(T_Married_Sis.contains("Select"))
            		T_Married_Sis="";
            	
            	if(T_Bro.equals("0"))
            		T_Married_Bro="0";
            	
            	if(T_Sis.equals("0"))
            		T_Married_Sis="0";
            	
            	ArrMarriageDetails[0]=Gotra;
            	ArrMarriageDetails[1]=BirthTime;
            	ArrMarriageDetails[2]=BirthPlace;
            	ArrMarriageDetails[3]=NativePlace;
            	ArrMarriageDetails[4]=Height+"";
            	ArrMarriageDetails[5]=Manglik;
            	ArrMarriageDetails[6]=AnnualIncome;
            	ArrMarriageDetails[7]=WorkAfterMarriage;
            	ArrMarriageDetails[8]=Diet;
            	ArrMarriageDetails[9]=FStatus;
            	ArrMarriageDetails[10]=MStatus;
            	ArrMarriageDetails[11]=T_Bro+"^"+T_Married_Bro+"^"+details_about_brothers;
            	ArrMarriageDetails[12]=T_Sis+"^"+T_Married_Sis+"^"+details_about_sisters;
            	ArrMarriageDetails[13]=AdditionalInfo;
            	
                dialog.dismiss();
            }
         });
 		
	}
	
	
	
	//Show Set Birth Time Dialog box 
	private void SetBirthTime()
	{
		final Dialog dialog = new Dialog(context);
	       dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
		   dialog.setContentView(R.layout.settime);
		   dialog.setCancelable(false);
		   dialog.show();
		   final TimePicker alarmTimePicker=(TimePicker)dialog.findViewById(R.id.timePicker1);
		   Button BtnOk=(Button)dialog.findViewById(R.id.btnOK);
		   Button BtnCancel=(Button)dialog.findViewById(R.id.btnCancel);

		    String GetTime=EtBirthTime.getText().toString().trim();
		    if(GetTime.length()!=0)
		    {
		      String pick =GetTime.replace(":", "#");
			  String[] temp=pick.split("#");
			  int hhs=Integer.parseInt(temp[0]);
			  int mms=Integer.parseInt(temp[1]);
			  alarmTimePicker.setCurrentHour(hhs);
			  alarmTimePicker.setCurrentMinute(mms);
		    }

		   BtnOk.setOnClickListener(new OnClickListener() {    	
       	    @Override
       	    public void onClick(View v) {
       	      int alarmHH= alarmTimePicker.getCurrentHour();
			  int alarmMM= alarmTimePicker.getCurrentMinute();
			  String AlarmTime=Chkval(String.valueOf(alarmHH))+":"+Chkval(String.valueOf(alarmMM));
			  EtBirthTime.setText(AlarmTime);
       	      dialog.dismiss();
       	   }
          });
			
		  BtnCancel.setOnClickListener(new OnClickListener() {    	
            @Override
            public void onClick(View v) {
              dialog.dismiss();
            }
          });
	}
	
	
	//Set Spinner Seleted Value
	private void SetSpinnerSelection(Spinner Sp,String Value)
	{
		if(Value.length()>0)
			Sp.setSelection(((ArrayAdapter<String>)Sp.getAdapter()).getPosition(Value));
	}
	
	
	//Set Spinner with  ArrayAdpter
	private void SetSpinnerAdp(Spinner Sp,String[] ArrSp)
	{
		ArrayAdapter<String> adp1= new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,ArrSp);
 		adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 		Sp.setAdapter(adp1);
	}
	
	
	// Display Image Selection Dialog
	private void ImageSeletionDialog()
	{
		final String[] option = new String[] {"Take from Camera","Select from Gallery"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item, option);
		
		AlertDialog.Builder adBld = new AlertDialog.Builder(context);
		adBld.setTitle("Select Option");
		adBld.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (which == 0) {
					callCamera();
				}
				if (which == 1) {
					callGallery();
				}
			}
		});
		//AlertDialog dial = adBld.create();
		adBld.show();
	}
	
	
	// Open Camera
	 public void callCamera()
	 {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		//cameraIntent.PutExtra(MediaStore.ExtraOutput, Uri.FromFile(App._file));
		cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
		cameraIntent.putExtra("crop", "true");
		cameraIntent.putExtra("aspectX", 0);
		cameraIntent.putExtra("aspectY", 0);
		cameraIntent.putExtra("outputX", 200);
		cameraIntent.putExtra("outputY", 250);
		startActivityForResult(cameraIntent, CAMERA_REQUEST);
	}

	 
	/// Open Gallery method
	public void callGallery() 
	{
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 0);
		intent.putExtra("aspectY", 0);
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(Intent.createChooser(intent, "Complete action using"),PICK_FROM_GALLERY);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data); 
		if (resultCode != -1)
			return;
		switch (requestCode) {
		case CAMERA_REQUEST:
			SetImageByCamera_Gallery(data);
			break;
		case PICK_FROM_GALLERY:
			SetImageByCamera_Gallery(data);
			break;
		}
	}

	
	//Set Image From Camera or Photo Gallery
	private void SetImageByCamera_Gallery(Intent intent)
	{
		Bundle extras = intent.getExtras();
		if (extras != null) {
			Bitmap yourImage = extras.getParcelable("data");
			
			//Resize the Original Image
			Bitmap ResizeImg=ScaleDownBitmap(yourImage,160,204,true);
			
			// convert bitmap to byte
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			ResizeImg.compress(Bitmap.CompressFormat.PNG, 100, stream);
			
			PersonImg = stream.toByteArray();
			try {
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ImgVwPersonal.setImageBitmap(ResizeImg);
		}
	}
	
	
	//Rezise Image Size Wise
	private Bitmap ScaleDownBitmap(Bitmap originalImage, float maxWidth,int maxHeight, boolean filter)
    {
        float ratio = Math.min((float)maxWidth / originalImage.getWidth(), (float)maxHeight / originalImage.getHeight());
        int width = (int)Math.round(ratio * (float)originalImage.getWidth());
        int height =(int) Math.round(ratio * (float)originalImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(originalImage, width, height, filter);
        return newBitmap;
    }

	//Resize Image with Wanted Width And Wanted Height Wise
    private Bitmap ScaleBitmap(Bitmap originalImage, int wantedWidth, int wantedHeight)
    {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float)wantedWidth / originalImage.getWidth(), (float)wantedHeight / originalImage.getHeight());
        canvas.drawBitmap(originalImage, m, new Paint());
        return output;
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
	
	private String Chkval(String DVal)
	{
    	if((DVal==null)||(DVal.equalsIgnoreCase("null"))){
			DVal="";
		}
		return DVal.trim();
	}
	
	 public boolean onKeyDown(int keyCode, KeyEvent event) 
	 {
	   	 if (keyCode == KeyEvent.KEYCODE_BACK) {
	   		 Goback();
	   	 return true;
	   	 }
	   	return super.onKeyDown(keyCode, event);
	 }
	 
	 public void Goback(){
		  Intent MainBtnIntent= new Intent(getBaseContext(),AffiliationAPP.class);	
		  MainBtnIntent.putExtra("POstion",Integer.parseInt(logid));
		  MainBtnIntent.putExtra("Count", 888333);
		  MainBtnIntent.putExtra("Clt_Log",Log);
		  MainBtnIntent.putExtra("Clt_LogID",logid);
		  MainBtnIntent.putExtra("Clt_ClubName",ClubName);
		  MainBtnIntent.putExtra("UserClubName",Str_user);
		  MainBtnIntent.putExtra("AppLogo", AppLogo);
	   	  startActivity(MainBtnIntent);
	   	  finish();
	 }
	 
	 
	 ///Display Alert Msg
	 private void DisplayMsg(String Msg,final boolean Goback)
	 {
		//System.out.println(Str_MEMid);
		 alertDialogBuilder3 = new AlertDialog.Builder(context);
	     alertDialogBuilder3
	        .setCancelable(false)
	        .setTitle(Msg)
            .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                	dialog.dismiss();
                	if(Goback)
                	  Goback();
                }
         });
         ad = alertDialogBuilder3.create();
         ad.show();
	 }
}
