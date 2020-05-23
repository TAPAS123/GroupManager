package group.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import group.manager.SimpleGestureFilter.SimpleGestureListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
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
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SwipeScreen extends Activity implements SimpleGestureListener,OnClickListener{
	TextView txtPName,txtPAdd,TxtPMob,TxtPMob2,TxtPhone1,TxtPhone2,TxtpEmail,TxtpEmail2,TxttBlood,txtMAX,txtMemno,txtRegion,txtDob,txtMarAnni,txtPAdd1,txtPAdd2,txtPAddcity;
	String s,Strname,Stradd,Strmo,Stremail,Strbg,Stra1,Stra2,Stra3,str_memid,Str_user,Str_main,Str_spous,Str_child,Log,
	ClubName,logid,sqlSearch,finalresult,Table2Name,StrEdSrch,SelectSTR="0",StrMemno,Table4Name,TableMiscName,
	City,dd,mm,yy,MarAnniyy,MarAnnidd,MarAnnimm;
	String [] temp,Arrschcri;
	ImageView ImgMain,BtnNxt,BtnPrev,img_Loc_Res,img_Loc_Off;
	Intent menuIntent;
	AlertDialog ad;
	private SimpleGestureFilter detect;
	SQLiteDatabase db;
	Cursor cursorT;
	int s_count,Cnt,i=0;
	byte[] imgP,imgSpouse=null;
	Integer tempsize;
	int[] CodeArr;
	Dialog dialog;
	EditText ET_Search_Name, ET_Search_Mob,ET_Search_MemNo,ET_Search_Addr;
	ActionBar actionBar;
	LinearLayout LLaymemno,LLayRegion,LLayMobile,LLayPhone,LLayaddress,LLayemail,LLayblood,LLaydob,llaydialog,llaysetting,LLayMarAnni,llaySrchCrit,
	LLaySearchCriteriashow,LLGPS_Loc;
	private static final int MY_BUTTON = 9000;
	CheckBox cb;
	ImageView ImgVw_Ad;
	Button btnMoreDetails,btnSpouseDetails,btnFamilyDetails,btnCommittee;
	byte[] AppLogo;
	LinearLayout LL1,LL2,LL3,LL4,LL5,LL6,LL7,LL8,LL9,LL10,LL_NewOp;
	TextView txt1,txt2,txt3,txt4,txt5,txt6,txt7,txt8,txt9,txt10,txt_NewOp;
	String Additional_Data,Additional_Data2;
	final Context context=this;
	ArrayAdapter<String> listAdapter=null,listAdpCommitee=null,listAdp_NewOpTitle=null;
	String MemDir="Member",SpouseDetails="",TableFamilyName;
	int MId_family=0;
	String[] Spouse_Addition_Data;
	int DOB_Disp=0,CHKsrhcri=0;
	List<Spinner> allSps;
	String strSrchcrival="",StrCaptionvale="",StrTable="";
	Button BtnSrchcrit;
	Editor editorCrit;
	LinearLayout LLMain;
	List<EditText> allEds;
	ArrayList<String> listarr,TextVal;
	String Special_Dir_Condition;
	String Dir_Filter_Condition="",CFrom;
	boolean Hide_Spouse_Address;
	String New_Op_Title="";
	String lati_longi_Res="",lati_longi_Off="";
	String CCBYear="";//CCB Year Added 16-11-2018
	
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.mainperson);
	        detect = new SimpleGestureFilter(this,this);
	        ImgMain = (ImageView)findViewById(R.id.ivmain);
	        BtnNxt=(ImageView)findViewById(R.id.iVNext);
			BtnPrev=(ImageView)findViewById(R.id.iVPrev);
			
			txtPName=(TextView)findViewById(R.id.tvmainName);
			
			txt_NewOp=(TextView)findViewById(R.id.txt_NewOp);//New Option working as a button 10-02-2017
			txt_NewOp.setTextColor(Color.BLUE);
			
			txtRegion=(TextView)findViewById(R.id.tvRegion);// TextView Region
			txtMemno=(TextView)findViewById(R.id.tvMemno);	
			txtPAdd=(TextView)findViewById(R.id.tvaddress);
			txtPAdd1=(TextView)findViewById(R.id.tvaddress1);
			txtPAdd2=(TextView)findViewById(R.id.tvaddress2);
			txtPAddcity=(TextView)findViewById(R.id.tvcityadd);
			
			TxtPMob=(TextView)findViewById(R.id.tvMobile);
			TxtPMob.setTextColor(Color.BLUE);
			TxtPMob2=(TextView)findViewById(R.id.tvMobile2);//Mobile2 textView
			TxtPMob2.setTextColor(Color.BLUE);
			
			TxtPhone1=(TextView)findViewById(R.id.tvPhone1);//Landline1 TextView
			TxtPhone1.setTextColor(Color.BLUE);
			TxtPhone2=(TextView)findViewById(R.id.tvPhone2);//Landline2 TextView
			TxtPhone2.setTextColor(Color.BLUE);
			
			TxtpEmail=(TextView)findViewById(R.id.tvmail);
			TxtpEmail.setTextColor(Color.BLUE);
			TxtpEmail2=(TextView)findViewById(R.id.tvmail2);//Email2 TextView
			TxtpEmail2.setTextColor(Color.BLUE);
			
			TxttBlood=(TextView)findViewById(R.id.tvblood);		
			txtMAX=(TextView)findViewById(R.id.tvMinofMax);		
			
			txtDob=(TextView)findViewById(R.id.tvdob);
			txtMarAnni=(TextView)findViewById(R.id.tvMarAnniDate);
			
			LL_NewOp=(LinearLayout)findViewById(R.id.LL_NewOp);//New Option working as a button 10-02-2017
			LLayRegion=(LinearLayout)findViewById(R.id.llRegion);// LinearLayOut Region	
			LLaymemno=(LinearLayout)findViewById(R.id.llmemno);	
			LLayMobile=(LinearLayout)findViewById(R.id.llmob);	
			LLayPhone=(LinearLayout)findViewById(R.id.llPhone);// Landline LinearLayOut	
			LLayaddress=(LinearLayout)findViewById(R.id.llAddr);	
			LLayemail=(LinearLayout)findViewById(R.id.llemail);	
			LLayblood=(LinearLayout)findViewById(R.id.llblood);	
			LLaydob=(LinearLayout)findViewById(R.id.lldob);
			LLayMarAnni=(LinearLayout)findViewById(R.id.llMarAnni);
			LLGPS_Loc=(LinearLayout)findViewById(R.id.LLGPS_Loc);// LinearLayout GPS Location(Added on 30-10-2017)
			
			//Additional Linear Layouts
			LL1=(LinearLayout)findViewById(R.id.LL1);
			LL2=(LinearLayout)findViewById(R.id.LL2);
			LL3=(LinearLayout)findViewById(R.id.LL3);
			LL4=(LinearLayout)findViewById(R.id.LL4);
			LL5=(LinearLayout)findViewById(R.id.LL5);
			LL6=(LinearLayout)findViewById(R.id.LL6);
			LL7=(LinearLayout)findViewById(R.id.LL7);
			LL8=(LinearLayout)findViewById(R.id.LL8);
			LL9=(LinearLayout)findViewById(R.id.LL9);
			LL10=(LinearLayout)findViewById(R.id.LL10);
			
			//Additional TextView
			txt1=(TextView)findViewById(R.id.txt1);
			txt2=(TextView)findViewById(R.id.txt2);
			txt3=(TextView)findViewById(R.id.txt3);
			txt4=(TextView)findViewById(R.id.txt4);
			txt5=(TextView)findViewById(R.id.txt5);
			txt6=(TextView)findViewById(R.id.txt6);
			txt7=(TextView)findViewById(R.id.txt7);
			txt8=(TextView)findViewById(R.id.txt8);
			txt9=(TextView)findViewById(R.id.txt9);
			txt10=(TextView)findViewById(R.id.txt10);
			
			ImgVw_Ad=(ImageView)findViewById(R.id.imgVw_Ad); // ImageView for Ad
			img_Loc_Res=(ImageView)findViewById(R.id.img_Loc_Res); // ImageView for Location Residence(Added on 30-10-2017)
			img_Loc_Off=(ImageView)findViewById(R.id.img_Loc_Off); // ImageView for Location Office (Added on 30-10-2017)
			
			btnMoreDetails=(Button)findViewById(R.id.btnMoreDetails); // Button For Additional Details
			btnSpouseDetails=(Button)findViewById(R.id.btnSpouse); //Button for Spouse Details
			btnFamilyDetails=(Button)findViewById(R.id.btnFamily); //Button for Family Details
			btnCommittee=(Button)findViewById(R.id.btnCommittee); //Button for Committee Details
			
			menuIntent = getIntent(); 
			Log =  menuIntent.getStringExtra("Clt_Log");
			logid =  menuIntent.getStringExtra("Clt_LogID");
			ClubName =  menuIntent.getStringExtra("Clt_ClubName");
			Str_user =  menuIntent.getStringExtra("UserClubName");
			AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
			Special_Dir_Condition=menuIntent.getStringExtra("Special_Dir_Condition");//Special Directory Condition with DirName
			CFrom=menuIntent.getStringExtra("CFrom");//Comes From
			if(CFrom.equals("MENU") || CFrom.equals("DIR_LIST"))
			  CCBYear=menuIntent.getStringExtra("CCBYear");////CCB Year Added 16-11-2018
			
	        Table2Name="C_"+Str_user+"_2";
			Table4Name="C_"+Str_user+"_4";
			TableMiscName="C_"+Str_user+"_MISC";
			TableFamilyName="C_"+Str_user+"_Family";
			
			allSps = new ArrayList<Spinner>();
			
			Set_App_Logo_Title(); // Set App Logo and Title
			
			Get_SharedPref_Values(); // Get Shared Pref Values of MemDir(Display Member/Spouse)
			
	        db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
	        Display_Image_Ad();// Display Ad (Advertisement)
	        
	        BtnNxt.setVisibility(View.GONE);//By Default
	        BtnPrev.setVisibility(View.GONE);//By Default
	        txtMAX.setVisibility(View.GONE);//By Default
	        if(!CFrom.equals("DIR_LIST"))/// Not Comes from Directory ListView added on 02-02-2018
	        {
	          BtnNxt.setVisibility(View.VISIBLE);
	          BtnPrev.setVisibility(View.VISIBLE);
		      txtMAX.setVisibility(View.VISIBLE);
		      
	          Add_DefaultFilter_Condition();//Add Default Filter if Have (Added on 10-07-2017)
	          Dir_Filter_Condition=DirFilter_Condition();//Directory Filter Condional Query
	        
	          CHKsrhcri=getSearchCriteria();
	        
	          ActionCall();//Action Bar Display
	        }
	        
	        /// Check GPS Set Location are Visible or Not
			if(ChkGPSLocation_Displayed()){
				LLGPS_Loc.setVisibility(View.VISIBLE);
			}
			///////////////////////////////////
	        
	        Hide_Spouse_Address=ChkHideSpouseAddress();//Hide Spouse Address Or Not
	        
			if(Special_Dir_Condition.contains("A.M_Id"))
			{
				sqlSearch=Special_Dir_Condition.replace("SELECT A.M_Id","SELECT Count(A.M_Id)");
			}
			else
			{
	          //Get Total records Count 
	          if(MemDir.equals("Member"))
	    	    sqlSearch="select Count(M_id) from "+Table2Name +" Where M_Name Is Not NULL "+Dir_Filter_Condition+Special_Dir_Condition;	
	          else
	            sqlSearch="select Count(M_id) from "+Table2Name +" Where S_Name Is Not NULL AND LENGTH(S_Name)<>0 "+Dir_Filter_Condition+Special_Dir_Condition;
			}
	    	
	        //System.out.println(sqlSearch);
	        try{
	        	cursorT = db.rawQuery(sqlSearch, null);	
	        }catch(Exception ex){
	        	System.out.println(ex.getMessage());
	        	BBack();
	        }
		    
		    if (cursorT.moveToFirst()) {
 		    	do {
 		    		 s_count=cursorT.getInt(0);
 		    	} while (cursorT.moveToNext());
 		     }
		    cursorT.close(); 
		    txtMAX.setText(Cnt+1+" of "+s_count);
		    if(s_count!=0){
		    	Cnt=0;
		    	
				if(Special_Dir_Condition.contains("A.M_Id"))
			    {
				   sqlSearch=Special_Dir_Condition;
			    }
				else
				{
		    	  // Get Mid from Table2
		    	  if(MemDir.equals("Member"))
		    		sqlSearch="select M_id from "+Table2Name +" Where M_Name Is Not NULL "+Dir_Filter_Condition+Special_Dir_Condition+" Order by C4_LName,Upper(M_Name)";//member
		    	 else
		    		sqlSearch="select M_id from "+Table2Name +" Where S_Name Is Not NULL AND LENGTH(S_Name)<>0 "+Dir_Filter_Condition+Special_Dir_Condition+" Order by Upper(S_Name)";//Spouse
		    	}
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
				
	 		    Additional_Data="NODATA"; 
	 		    Additional_Data2="NODATA";
	 		    
	 		    //Get Additional Data for MainScreen and PopupScreen
	 		    String Qry="Select Add1,Add2 from "+TableMiscName+" Where Rtype='MASTER'";
	 		    cursorT = db.rawQuery(Qry, null);
	 		    while(cursorT.moveToNext())
				{
	 		       Additional_Data=cursorT.getString(0);
			       Additional_Data2=cursorT.getString(1);
			       if(Additional_Data==null || Additional_Data=="")
			       {
			    	 Additional_Data="NODATA";
			       }
			       if(Additional_Data2==null || Additional_Data2=="")
			       {
			    	 Additional_Data2="NODATA";
			       }
				   break;
				}
	 		    cursorT.close();
	 		    //////////////////////////////
	 		     
	 		    ///////Get New Option Caption or Title which is used to display some list in popup screen
	 		    Qry="Select Text1 from "+Table4Name+" Where Rtype='ADDL'";
	 		    cursorT = db.rawQuery(Qry, null);
	 		    if(cursorT.moveToFirst())
				{
	 		      New_Op_Title=cursorT.getString(0);
				}
	 		    cursorT.close();
	 		    ////////////////////////////////////////////////
	 		    
	 		   db.close();//Close Connection
	 		   FillMainData(); // Fill Main Data Display       
		    }
		    else
		    {
		    	DisplayAlert("Result!","No Record found");
		    }
		    
		    
		    //Show Additional Details Popup screen
		    btnMoreDetails.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		        	 // Display Popup Screen of ListView
		        	 final Dialog dialog = new Dialog(context);
				     dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
			  		 dialog.setContentView(R.layout.additional_data);
			  		 dialog.setCancelable(false);
			  		 dialog.show();
			  		 ListView LV=(ListView)dialog.findViewById(R.id.Lv1);
			  		 Button btnBack=(Button)dialog.findViewById(R.id.btnBack);
                     // Set ListAdapter
                     LV.setAdapter(listAdapter);
                     
			  		 btnBack.setOnClickListener(new OnClickListener() {    	
			             @Override
			             public void onClick(View v) {
			              dialog.dismiss();
			             }
			         });
		        }
		    });
		    
		    ImgMain.setOnClickListener(new View.OnClickListener() {
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
						/*theImage=Bitmap.createScaledBitmap(theImage, 300, 320, true);
						IVzoomimage.setImageBitmap(theImage);
						//.setWidth(120);
						bmpWidth = theImage.getWidth();
				        bmpHeight = theImage.getHeight();
				        System.out.println(bmpWidth+" :h, w: "+bmpWidth);
				        distCurrent = 1; //Dummy default distance
				        dist0 = 1;   //Dummy default distance
				        drawMatrix();
				        IVzoomimage.setOnTouchListener(MyOnTouchListener);
				        touchState = IDLE;*/
					    
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
		    
		    
		    //Show Spouse Details Popup screen
		    btnSpouseDetails.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		        	 // Display Popup Screen of ListView
		        	 final Dialog dialog = new Dialog(context);
				     dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
			  		 dialog.setContentView(R.layout.spouse_details);
			  		 dialog.show();
			  		 
			  		 ImageView ImgMain = (ImageView)dialog.findViewById(R.id.ivmain);
			  		 TextView txtName=(TextView)dialog.findViewById(R.id.tvmainName);
			  		 TextView txtMemNo=(TextView)dialog.findViewById(R.id.tvMemno);	
			  		 TextView txtAdd=(TextView)dialog.findViewById(R.id.tvaddress);
			  		 final TextView txtMob=(TextView)dialog.findViewById(R.id.tvMobile);
			  		 txtMob.setTextColor(Color.BLUE);
			  		 final TextView txtEmail=(TextView)dialog.findViewById(R.id.tvmail);
			  		 txtEmail.setTextColor(Color.BLUE);
					
					 TextView txtBlood=(TextView)dialog.findViewById(R.id.tvblood);		
					 TextView txtDob=(TextView)dialog.findViewById(R.id.tvdob);
					 TextView txtMarAnni=(TextView)dialog.findViewById(R.id.tvMarAnniDate);
					
					 LinearLayout LLaymemno=(LinearLayout)dialog.findViewById(R.id.llmemno);	
					 LinearLayout LLayMobile=(LinearLayout)dialog.findViewById(R.id.llmob);	
					 LinearLayout LLayaddress=(LinearLayout)dialog.findViewById(R.id.llAddr);	
					 LinearLayout LLayemail=(LinearLayout)dialog.findViewById(R.id.llemail);	
					 LinearLayout LLayblood=(LinearLayout)dialog.findViewById(R.id.llblood);	
					 LinearLayout LLaydob=(LinearLayout)dialog.findViewById(R.id.lldob);
					 LinearLayout LLayMarAnni=(LinearLayout)dialog.findViewById(R.id.llMarAnni);
					
					 //Additional Linear Layouts
					 LinearLayout LL1=(LinearLayout)dialog.findViewById(R.id.LL1);
					 LinearLayout LL2=(LinearLayout)dialog.findViewById(R.id.LL2);
					 LinearLayout LL3=(LinearLayout)dialog.findViewById(R.id.LL3);
					 LinearLayout LL4=(LinearLayout)dialog.findViewById(R.id.LL4);
					 LinearLayout LL5=(LinearLayout)dialog.findViewById(R.id.LL5);
					 LinearLayout LL6=(LinearLayout)dialog.findViewById(R.id.LL6);
					 LinearLayout LL7=(LinearLayout)dialog.findViewById(R.id.LL7);
					 LinearLayout LL8=(LinearLayout)dialog.findViewById(R.id.LL8);
					 LinearLayout LL9=(LinearLayout)dialog.findViewById(R.id.LL9);
					 LinearLayout LL10=(LinearLayout)dialog.findViewById(R.id.LL10);
					
					//Additional TextView
					 TextView txt1=(TextView)dialog.findViewById(R.id.txt1);
					 TextView txt2=(TextView)dialog.findViewById(R.id.txt2);
					 TextView txt3=(TextView)dialog.findViewById(R.id.txt3);
					 TextView txt4=(TextView)dialog.findViewById(R.id.txt4);
					 TextView txt5=(TextView)dialog.findViewById(R.id.txt5);
					 TextView txt6=(TextView)dialog.findViewById(R.id.txt6);
					 TextView txt7=(TextView)dialog.findViewById(R.id.txt7);
					 TextView txt8=(TextView)dialog.findViewById(R.id.txt8);
					 TextView txt9=(TextView)dialog.findViewById(R.id.txt9);
					 TextView txt10=(TextView)dialog.findViewById(R.id.txt10);
			  		 
					 if(SpouseDetails.trim().length()>0)
					 {
						 s = SpouseDetails.replace("^", "##")+" ";
						 temp = s.split("##"); 
						 String MemId=temp[0].toString();
						 String Name=temp[1].toString();
						 String Add1=temp[2].toString();	
						 String Add2=temp[3].toString();
						 String Add3=temp[4].toString();
						 String City=temp[5].toString();
						 String Email=temp[6].toString();
						 String Mob=temp[7].toString();
						 String MemNo=temp[8].toString().trim();
						 String BG=temp[9].toString().trim();
						 String Prefix_Name=temp[10].toString();
						 String Dobdd=temp[11].toString().trim();
						 String Dobmm=temp[12].toString();
						 String Dobyy=temp[13].toString();
						 String MarAnnidd=temp[14].toString().trim();
						 String MarAnnimm=temp[15].toString();
						 String MarAnniyy=temp[16].toString();
						 String C4_DOD_D=temp[17].toString();//Special Condition Value
						 
						 ///Added 29-12-2016 ////
						 ////Special Condition C4_DOD_D (Mob1,Mob2,Land1,Land2,Email1,Email2) is Displayed or Not
						 if(C4_DOD_D.equalsIgnoreCase("Y"))
						 {
							Mob="";//Mob1
							Email="";//Email1
						 }
						 //////////////////////////////////////////
						 
						 if(City.trim().length()>0)
							 City="\n"+City;
						 
						 if(Add1.length()>0)
							 Add1=Add1+"\n";
						 
						 if(Add2.length()>0)
							 Add2=Add2+"\n";
						 
						 String FullAddr=Add1+Add2+Add3+City;//Set Address
						 
						 if(Hide_Spouse_Address)
							 FullAddr="";//Set Address is blank and Hide Spouse Address
						 
						 String DOB="",Ann_Date="";
							
						 //Set DOB (Updated at 07-02-2015 Tapas)
						 if((Dobdd.trim().length()==0 && Dobmm.trim().length()==0) || (DOB_Disp==1)){
							DOB="";
						 }
						 else if(Dobyy.trim().length()==0 || DOB_Disp==2){
							Dobmm= getMonthForInt(Integer.parseInt(Dobmm));
							DOB=Dobdd+" "+Dobmm;
						 }
						 else{
							DOB=Dobdd+"-"+Dobmm+"-"+Dobyy;
						 }
						 filloremptyData(DOB,LLaydob,txtDob);// Set Spouse DOB
							
						 //Set Marriage Anniversary Date(Updated at 07-02-2015 Tapas)
						 if(MarAnnidd.trim().length()==0 && MarAnnimm.trim().length()==0){
							Ann_Date="";
						 }
						 else if(MarAnniyy.trim().length()==0)
						 {
							MarAnnimm= getMonthForInt(Integer.parseInt(MarAnnimm));
							Ann_Date=MarAnnidd+" "+MarAnnimm;
						 }
						 else{
							Ann_Date=MarAnnidd+"-"+MarAnnimm+"-"+MarAnniyy;
						 }
						 filloremptyData(Ann_Date,LLayMarAnni,txtMarAnni);// Set Spouse Anniversary Date
				         //////////////////////////////////////////////////////////////////////
							
						 Name=Prefix_Name.trim()+" "+Name.trim();
						 txtName.setText(Name.trim());// Set Name
						 filloremptyData(MemNo,LLaymemno,txtMemNo);	
						 filloremptyData(Mob,LLayMobile,txtMob);	
						 filloremptyData(FullAddr,LLayaddress,txtAdd);
						 filloremptyData(Email,LLayemail,txtEmail);
						 filloremptyData(BG,LLayblood,txtBlood);	
						 
						 //Set Additional Data 
						 filloremptyData(Spouse_Addition_Data[0],LL1,txt1);
						 filloremptyData(Spouse_Addition_Data[1],LL2,txt2);
						 filloremptyData(Spouse_Addition_Data[2],LL3,txt3);
						 filloremptyData(Spouse_Addition_Data[3],LL4,txt4);
						 filloremptyData(Spouse_Addition_Data[4],LL5,txt5);
						 filloremptyData(Spouse_Addition_Data[5],LL6,txt6);
						 filloremptyData(Spouse_Addition_Data[6],LL7,txt7);
						 filloremptyData(Spouse_Addition_Data[7],LL8,txt8);
						 filloremptyData(Spouse_Addition_Data[8],LL9,txt9);
						 filloremptyData(Spouse_Addition_Data[9],LL10,txt10);
						 ////////////////////////////////
						 
						 //Set Spouse Image
						 if(imgSpouse!=null){
						   ByteArrayInputStream imageStream = new ByteArrayInputStream(imgSpouse);
						   Bitmap theImage = BitmapFactory.decodeStream(imageStream);
						   ImgMain.setImageBitmap(theImage);
						 }else{
						   ImgMain.setImageResource(R.drawable.person1);
						 }
						 
					
						 txtMob.setOnClickListener(new View.OnClickListener() {
						        public void onClick(View v) {
						        	String MobNo= txtMob.getText().toString().trim();
						        	Show_Mob_Dialog(MobNo.trim());
						         }
						 });
						
						 txtEmail.setOnClickListener(new View.OnClickListener() {
					        public void onClick(View v) {
					        	String Email= txtEmail.getText().toString().trim();
					        	Show_Email_Dialog(Email);
					         }
					     });
						 
					 }
			  		 
					 ImageView btnBack=(ImageView)dialog.findViewById(R.id.imgBtnBack);
			  		 btnBack.setOnClickListener(new OnClickListener() {    	
			             @Override
			             public void onClick(View v) {
			              dialog.dismiss();
			             }
			         });
		        }
		    });
		    
		    
		    //Show Family Details Popup screen
		    btnFamilyDetails.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		        	menuIntent= new Intent(context,AffiliationAPP.class);
		    		menuIntent.putExtra("Count",888222);
		    		menuIntent.putExtra("POstion",MId_family);
		    		menuIntent.putExtra("Clt_LogID",logid);
	  				menuIntent.putExtra("Clt_Log",Log);
	  				menuIntent.putExtra("Clt_ClubName",ClubName);
	  				menuIntent.putExtra("UserClubName",Str_user);
	  				menuIntent.putExtra("AppLogo", AppLogo);
	  				menuIntent.putExtra("PName", txtPName.getText());
		    	    startActivity(menuIntent);
		    	   // finish();
		        }
		    });
		    
		    
		    //Show Committee Details Popup screen
		    btnCommittee.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		        	 // Display Popup Screen of ListView
		        	 final Dialog dialog = new Dialog(context);
				     dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
			  		 dialog.setContentView(R.layout.additional_data);
			  		 dialog.setCancelable(false);
			  		 dialog.show();
			  		 TextView txtHead=(TextView)dialog.findViewById(R.id.tvTt);
			  		 ListView LV=(ListView)dialog.findViewById(R.id.Lv1);
			  		 Button btnBack=(Button)dialog.findViewById(R.id.btnBack);
                     
			  		 txtHead.setText("Committee List");
			  		 
			  		 // Set ListAdapter
                     LV.setAdapter(listAdpCommitee);
                    
			  		 btnBack.setOnClickListener(new OnClickListener() {    	
			             @Override
			             public void onClick(View v) {
			              dialog.dismiss();
			             }
			         });
		        }
		    });
		    
		    
		    //Mobile1 Click Event
			TxtPMob.setOnClickListener(new View.OnClickListener() {
			     public void onClick(View v) {
			        String Mob1= TxtPMob.getText().toString().trim();
			        Show_Mob_Dialog(Mob1.trim());
			     }
			});
			
			//Mobile2 Click Event
			TxtPMob2.setOnClickListener(new View.OnClickListener() {
			     public void onClick(View v) {
			        String Mob2= TxtPMob2.getText().toString().trim();
			        Mob2=Mob2.replace(", ","");
			        Show_Mob_Dialog(Mob2.trim());
			     }
			});
			
			
			//Phone 1 Click Event
			TxtPhone1.setOnClickListener(new View.OnClickListener() {
			     public void onClick(View v) {
			        String Land1= TxtPhone1.getText().toString().trim();
			        Show_LandLine_Dialog(Land1);
			     }
			});
			
			//Phone 2 Click Event
			TxtPhone2.setOnClickListener(new View.OnClickListener() {
			     public void onClick(View v) {
			        String Land2= TxtPhone2.getText().toString().trim();
			        Land2=Land2.replace(", ","");
			        Show_LandLine_Dialog(Land2.trim());
			     }
			});
			
			//Email1 Click Event
			TxtpEmail.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		        	String Email= TxtpEmail.getText().toString().trim();
		        	Show_Email_Dialog(Email);
		         }
		    });
			
			//Email2 Click Event
			TxtpEmail2.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		        	String Email= TxtpEmail2.getText().toString().trim();
		        	Show_Email_Dialog(Email);
		         }
		    });
			
			
			//Show new Option PopUp Screen on click(10-02-2017)
			txt_NewOp.setOnClickListener(new View.OnClickListener() {
			     public void onClick(View v) {
			    	// Display Popup Screen of ListView
		        	 final Dialog dialog = new Dialog(context);
				     dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
			  		 dialog.setContentView(R.layout.additional_data);
			  		 dialog.setCancelable(false);
			  		 dialog.show();
			  		 TextView txtHead=(TextView)dialog.findViewById(R.id.tvTt);
			  		 ListView LV=(ListView)dialog.findViewById(R.id.Lv1);
			  		 Button btnBack=(Button)dialog.findViewById(R.id.btnBack);
                     
			  		 txtHead.setText(New_Op_Title);
			  		 
			  		 // Set ListAdapter
                     LV.setAdapter(listAdp_NewOpTitle);
                    
			  		 btnBack.setOnClickListener(new OnClickListener() {    	
			             @Override
			             public void onClick(View v) {
			              dialog.dismiss();
			             }
			         });
		        }
			});
			
			
			BtnPrev.setOnClickListener(new OnClickListener(){ 
	            @Override public void onClick(View arg0){
	               Prev();
	            }
		    });
			
			BtnNxt.setOnClickListener(new OnClickListener(){ 
		        @Override public void onClick(View arg0){
		           Next();
		        }
			 });
			
			ImgVw_Ad.setOnClickListener(new OnClickListener(){ 
		        @Override public void onClick(View arg0){
		        	byte[] ImgAdg=null;
		        	db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
		        	String Qry="Select photo1 from "+Table4Name+" Where Rtype='FullAdg'";
		        	cursorT = db.rawQuery(Qry, null);
		        	while(cursorT.moveToNext())
		    		{
		        	   ImgAdg=cursorT.getBlob(0);
		    		   break;
		    		}
		    		cursorT.close();
		    		db.close();
		    		
		    		// Sent Image for full AD
		    		if(ImgAdg!=null){
	    			  menuIntent= new Intent(getBaseContext(),FullAdvertisement.class);
		    		  menuIntent.putExtra("Type","8");
		    		  menuIntent.putExtra("Clt_Log",Log);
		    		  menuIntent.putExtra("Clt_LogID",logid);
		    		  menuIntent.putExtra("Clt_ClubName",ClubName);
		    		  menuIntent.putExtra("UserClubName",Str_user);
		    		  menuIntent.putExtra("AppLogo", AppLogo);
		    		  menuIntent.putExtra("Photo1", ImgAdg);
		    	      startActivity(menuIntent);
		    	      //finish();
		    		}
			       }
				 });
			
			img_Loc_Res.setOnClickListener(new OnClickListener(){ 
		        @Override public void onClick(View arg0){
		        	DisplayGPSLocation_Dialog(lati_longi_Res,1);
		        }
			 });
			
			img_Loc_Off.setOnClickListener(new OnClickListener(){ 
		        @Override public void onClick(View arg0){
		        	DisplayGPSLocation_Dialog(lati_longi_Off,2);
		        }
			 });
	}
		
   
	//Get Shared Pref Values
    private void Get_SharedPref_Values() {
      SharedPreferences sharedpreferences = context.getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
		if (sharedpreferences.contains("MemDir"))
	    {
			MemDir=sharedpreferences.getString("MemDir", "");
	    }
		if (sharedpreferences.contains("DOB_Disp"))
	    {
			DOB_Disp=Integer.parseInt(sharedpreferences.getString("DOB_Disp", ""));
	    }
		//NOTE DOB_Disp 0=DOB Visible, 1=DOB not Visible,2=DOB Visible without Year
		//System.out.println(MemDir);
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
		
    // Display Ad in the ImageView
    private void Display_Image_Ad()
	{
    	//Rtype=Ad2 for Directory
    	String sql ="Select Photo1 from "+Table4Name+" WHERE Rtype='Ad2'";
    	cursorT = db.rawQuery(sql, null);
		byte[] ImgAd=null;
    	while(cursorT.moveToNext())
		{
    	   ImgAd=cursorT.getBlob(0);
		   break;
		}
		cursorT.close();
		
		// Set Image for AD
		if(ImgAd==null)
		{
			ImgVw_Ad.setVisibility(View.GONE);
		}
		else
		{
		  Bitmap bitmap = BitmapFactory.decodeByteArray(ImgAd , 0, ImgAd.length);
		  ImgVw_Ad.setVisibility(View.VISIBLE);
		  ImgVw_Ad.setImageBitmap(bitmap);
		}
	}
		
	 @Override
	public boolean dispatchTouchEvent(MotionEvent m1){
		// Call onTouchEvent of SimpleGestureFilter class
		this.detect.onTouchEvent(m1);
		return super.dispatchTouchEvent(m1);
    }

	 
	public void Next(){
		if(!CFrom.equals("DIR_LIST"))/// Not Comes from Directory ListView added on 02-02-2018
        {
		  if(Cnt+1==tempsize){
    		Toast.makeText(getBaseContext(), "No Further Record", 0).show();
    	  }else{
    		Cnt=Cnt+1;
    		FillMainData();
		    txtMAX.setText(Cnt+1+" of "+s_count);
    	  }
        }
	}
	
	public void Prev(){
		if(!CFrom.equals("DIR_LIST"))/// Not Comes from Directory ListView added on 02-02-2018
        {
		  if(Cnt==0){
    		Toast.makeText(getBaseContext(), "No Previous Record", 0).show();
    	  }else{
    		Cnt=Cnt-1;
    		FillMainData();
		    txtMAX.setText(Cnt+1+" of "+s_count);
    	  }
        }
	}
	
	
	//Show Mobile Call/Sms Dialog
	private void Show_Mob_Dialog(final String MobNo)
	{
		AlertDialog.Builder AdBuilder = new AlertDialog.Builder(context);
    	if((MobNo==null)||(MobNo.length()!=10)||(MobNo.contains("+"))){
    		AdBuilder
    		 .setTitle( Html.fromHtml("<font color='#E32636'>Wrong Mobile Number !</font>"))
    		 .setMessage(MobNo)
             .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                    	dialog.dismiss();
                    }
              });
    	}else{
    	  AdBuilder
            .setPositiveButton("CALL",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                    	callOnphone("0"+MobNo);
                    }
            })
            .setNegativeButton("SMS",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                    	callOnSms("0"+MobNo);
                    }
             });
    	}
    	AdBuilder.show();
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
		if(CFrom.equals("MENU")){
		  Intent MainBtnIntent= new Intent(getBaseContext(),MenuPage.class);
   		  MainBtnIntent.putExtra("AppLogo", AppLogo);
	      startActivity(MainBtnIntent);
	      finish();
		}
		else{
		  finish();
		}
	    
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
	
	public void FillMainData() {
		// TODO Auto-generated method stub
		try{
		 int MemId=0;	
		 String sql="";
		 String[] AddDATA=new String[10];// Array For Additional Data
		 db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 

		 if(MemDir.equals("Member")) 
 			 sql="select M_id,M_Name,M_Add1,M_Add2,M_Add3,(M_City || \" \" || M_Pin),M_Email,M_Mob,MemNo,M_BG,M_Pic,(C4_BG || \"\" || ifnull(C4_DOB_Y,'')),M_DOB_D,M_DOB_M,M_DOB_Y,M_MrgAnn_D,M_MrgAnn_M,M_MrgAnn_Y,C4_DOB_D,C1_FName,C2_FName,C4_Mob,M_SndMob,M_Email1,M_Land1,M_Land2 from "+Table2Name+" where M_id="+CodeArr[Cnt] ;
		 else 
			 sql="select M_id,S_Name,M_Add1,M_Add2,M_Add3,(M_City || \" \" || M_Pin),S_Email,S_Mob,C4_Gender,S_BG,S_Pic,C3_BG,S_DOB_D,S_DOB_M,S_DOB_Y,M_MrgAnn_D,M_MrgAnn_M,M_MrgAnn_Y,C4_DOB_D,C1_FName,C2_FName,C3_FName,C3_MName from "+Table2Name+" where M_id="+CodeArr[Cnt];
		 
		 System.out.println("Sql:  "+sql);
		 try{
			 cursorT = db.rawQuery(sql, null); 
		 }catch(Exception ex){
			 System.out.println(ex.getMessage());
			 BBack();
		 }
		 
		 if(cursorT.moveToFirst()){
			MemId=cursorT.getInt(0);
			finalresult=MemId+"^"+Chkval(cursorT.getString(1))+"^"+Chkval(cursorT.getString(2))+"^"+Chkval(cursorT.getString(3))+"^"+Chkval(cursorT.getString(4))+"^"+
		    			Chkval(cursorT.getString(5))+"^"+Chkval(cursorT.getString(6))+"^"+Chkval(cursorT.getString(7))+"^"+Chkval(cursorT.getString(8))+"^"+
					    Chkval(cursorT.getString(9))+"^"+Chkval(cursorT.getString(11))+"^"+Chkval(cursorT.getString(12))+"^"+Chkval(cursorT.getString(13))+"^"+
		    			Chkval(cursorT.getString(14))+"^"+Chkval(cursorT.getString(15))+"^"+Chkval(cursorT.getString(16))+"^"+Chkval(cursorT.getString(17))+"^"+
					    Chkval(cursorT.getString(18))+"^"+Chkval(cursorT.getString(19))+"^"+Chkval(cursorT.getString(20));
					    
		    imgP=cursorT.getBlob(10);
		    if(MemDir.equals("Member"))
		    {
		    	finalresult=finalresult+"^"+Chkval(cursorT.getString(21))+"^"+Chkval(cursorT.getString(22))+"^"+Chkval(cursorT.getString(23))+"^"+Chkval(cursorT.getString(24))+"^"+Chkval(cursorT.getString(25));
		    }
		    else
		    {
		    	String Spouse_Company=Chkval(cursorT.getString(21));
		    	String Spouse_Profession=Chkval(cursorT.getString(22));
		    	if(Spouse_Company.trim().length()>0)
		    		AddDATA[0]="Company :  "+ Spouse_Company;
		    	if(Spouse_Profession.trim().length()>0)
		    		AddDATA[1]="Profession :  "+ Spouse_Profession;
		    }
		 }
		 cursorT.close();
		 
		 //System.out.println("IDS   :  "+MemId+" "+logid);
		       
		 if(imgP!=null){
			ByteArrayInputStream imageStream = new ByteArrayInputStream(imgP);
			Bitmap theImage = BitmapFactory.decodeStream(imageStream);
			ImgMain.setImageBitmap(theImage);
	     }else{
			ImgMain.setImageResource(R.drawable.person1);
		 }
		 
		 //Check Member Has Spouse or Not
		 CheckSpouse(MemId);
		 ////////////////////////////////
		 
		 //Check Member Has Family or Not
		 CheckFamily(MemId);
		 ////////////////////////////////
		 
		 //Check Member is in Any Committee or Not
		 CheckCommittee(MemId);
		 /////////////////////////////////
		 
		 //Check Data is in Any "New Option Title" or Not(10-02-2017)
		 if(New_Op_Title.length()>1)
		   Check_New_Option(MemId);
		 /////////////////////////////////
		 
		 //Get Records Additional Data 1 (for Main Screen) Only For Member
		 if(!Additional_Data.equals("NODATA") && Additional_Data.contains("#") && MemDir.equals("Member"))
		 {
			 String[] Arr1=Additional_Data.split("#");
			 String[] Arr2=Arr1[0].replace("^", "#").split("#");
			 sql="Select "+Arr1[1]+" from "+TableMiscName+" Where Rtype='DATA' And Memid="+MemId;
			 cursorT = db.rawQuery(sql, null);
			 if(cursorT.moveToFirst()){
				   for(int i=0;i<Arr2.length;i++)
				   {
					   String data=cursorT.getString(i);
					   if(data!=null)
					   {
						   if(data.trim().length()>0)
						   {
							   AddDATA[i]=Arr2[i]+" :  "+ cursorT.getString(i);
						   }
					   }
				   }
		     }
			 cursorT.close();
		 }
		 
		 //Get Records Additional Data 2 (for PopUp Screen) Only For Member
		 if(!Additional_Data2.equals("NODATA") && Additional_Data2.contains("#") && MemDir.equals("Member"))
		 {
			 ArrayList<String> arrList = new ArrayList<String>();
			 String[] Arr_1=Additional_Data2.split("#");
			 String[] Arr_2=Arr_1[0].replace("^", "#").split("#");
			 sql="Select "+Arr_1[1]+" from "+TableMiscName+" Where Rtype='DATA' And Memid="+MemId;
			 cursorT = db.rawQuery(sql, null);
			 if(cursorT.moveToFirst()){
				   for(int i=0;i<Arr_2.length;i++)
				   {
					   String data=cursorT.getString(i);
					   if(data!=null)
					   {
						   if(data.trim().length()>0)
						   {
							   data=Arr_2[i]+" :  "+ cursorT.getString(i);
							   arrList.add(data); // Add data in Array List
						   }
					   }
				   }
		     }
			 if(arrList.size()!=0)
			 {
				 btnMoreDetails.setVisibility(View.VISIBLE);//Display Addition Data Button
				 listAdapter = new ArrayAdapter<String>(this, R.layout.listitem_additionaldata, arrList); 
			 }
			 else
			 {
				 btnMoreDetails.setVisibility(View.GONE);//Hide Addition Data Button
				 listAdapter=null;
			 }
			 cursorT.close();
		 }
		 else
		 {
			 btnMoreDetails.setVisibility(View.GONE);//Hide Addition Data Button
			 listAdapter=null;
		 }
		 
		 db.close();
		 Fill_Main(finalresult,AddDATA); // Set Display Data of Directory
	  }catch(Exception ex){
   		 System.out.println(ex.getMessage());
	  }
   }
	
	
	//Check Member Has Spouse or Not
	private void CheckSpouse(int M_Id)
	{
	   String sql="";
	   SpouseDetails="";
	   imgSpouse=null;
	   Spouse_Addition_Data=new String[10];
	   if(MemDir.equals("Member"))
		  sql="Select M_id,S_Name,M_Add1,M_Add2,M_Add3,(M_City || \" \" || M_Pin),S_Email,S_Mob,C4_Gender,S_BG,S_Pic,C3_BG,S_DOB_D,S_DOB_M,S_DOB_Y,M_MrgAnn_D,M_MrgAnn_M,M_MrgAnn_Y,C4_DOB_D,C3_Mob,C3_FName,C3_MName from "+Table2Name+" where M_id="+M_Id;
	   else
		  sql="select M_id,M_Name,M_Add1,M_Add2,M_Add3,(M_City || \" \" || M_Pin),M_Email,M_Mob,MemNo,M_BG,M_Pic,(C4_BG || \"\" || ifnull(C4_DOB_Y,'')),M_DOB_D,M_DOB_M,M_DOB_Y,M_MrgAnn_D,M_MrgAnn_M,M_MrgAnn_Y,C4_DOB_D,C4_Mob from "+Table2Name+" where M_id="+M_Id;
		   
	    cursorT = db.rawQuery(sql, null);
		if(cursorT.moveToFirst()){
			String Name=Chkval(cursorT.getString(1));
			if(Name.trim().length()>0)
			{
			  SpouseDetails=cursorT.getInt(0)+"^"+Name+"^"+Chkval(cursorT.getString(2))+"^"+Chkval(cursorT.getString(3))+"^"+Chkval(cursorT.getString(4))+"^"+
				  Chkval(cursorT.getString(5))+"^"+Chkval(cursorT.getString(6))+"^"+Chkval(cursorT.getString(7))+"^"+Chkval(cursorT.getString(8))+"^"+
				  Chkval(cursorT.getString(9))+"^"+Chkval(cursorT.getString(11))+"^"+Chkval(cursorT.getString(12))+"^"+Chkval(cursorT.getString(13))+"^"+
				  Chkval(cursorT.getString(14))+"^"+Chkval(cursorT.getString(15))+"^"+Chkval(cursorT.getString(16))+"^"+Chkval(cursorT.getString(17))+"^"+
				  Chkval(cursorT.getString(18))+"^"+Chkval(cursorT.getString(19));
			  // System.out.println(SpouseDetails);
			  imgSpouse=cursorT.getBlob(10);//Get Spouse Image
			  if(MemDir.equals("Member"))
			  {
			    String Spouse_Company=Chkval(cursorT.getString(20));
		        String Spouse_Profession=Chkval(cursorT.getString(21));
			    if(Spouse_Company.trim().length()>0)
				  Spouse_Addition_Data[0]="Company :  "+ Spouse_Company;
			    if(Spouse_Profession.trim().length()>0)
				  Spouse_Addition_Data[1]="Profession :  "+ Spouse_Profession;
			  }
			}
		 }
		 cursorT.close();
		     
		 //Get Records Additional Data 1 (for Main Screen) Only For Member
		 if(!Additional_Data.equals("NODATA") && Additional_Data.contains("#") && !MemDir.equals("Member"))
		 {
			 String[] Arr1=Additional_Data.split("#");
			 String[] Arr2=Arr1[0].replace("^", "#").split("#");
			 sql="Select "+Arr1[1]+" from "+TableMiscName+" Where Rtype='DATA' And Memid="+M_Id;
			 cursorT = db.rawQuery(sql, null);
			 if(cursorT.moveToFirst()){
				 for(int i=0;i<Arr2.length;i++)
				 {
					String data=cursorT.getString(i);
					if(data!=null)
					{
					   if(data.trim().length()>0)
					   {
						  Spouse_Addition_Data[i]=Arr2[i]+" :  "+ cursorT.getString(i);
					   }
					}
				 }
		     }
			 cursorT.close();
		 }
		 
		 if(SpouseDetails.trim().length()>0){
			 btnSpouseDetails.setVisibility(View.VISIBLE);//Display Addition Data Button 
		 }else{
		     btnSpouseDetails.setVisibility(View.GONE);//Display Addition Data Button
		 }
	}
	
	//Check Member Has Spouse or Not
	private void CheckFamily(int M_Id)
	{
	   String sql="";
	   int familycount=0;
	   sql="Select Count(M_Id) from "+TableFamilyName+" where MemId="+M_Id;
	    cursorT = db.rawQuery(sql, null);
		if(cursorT.moveToFirst()){
			familycount=cursorT.getInt(0);
		 }
		 cursorT.close();
		 if(familycount>0){
			 String ValFamilyShow="";
			 sql="Select Text1 from "+Table4Name+" where Rtype='FAMILY'";
			    cursorT = db.rawQuery(sql, null);
				if(cursorT.moveToFirst()){
					ValFamilyShow=cursorT.getString(0);
				 }
				 cursorT.close();
				 if(ValFamilyShow.equalsIgnoreCase("YES")){
					 MId_family=M_Id;
					 btnFamilyDetails.setVisibility(View.VISIBLE);//Display Addition Data Button
				 }else{
					 MId_family=0;
					 btnFamilyDetails.setVisibility(View.GONE);//Display Addition Data Button
				 }
		 }else{
			 MId_family=0;
			 btnFamilyDetails.setVisibility(View.GONE);//Display Addition Data Button
		 }
	}
	
	
	//Check Member is in Any Committee or Not
	private void CheckCommittee(int M_Id)
	{
		String AddAND="";
		if(CCBYear.length()>0)
			  AddAND=" AND Date1_1="+CCBYear;//Added on 16-11-2018	
		
	   ArrayList<String> arrList = new ArrayList<String>();
	   String sql="Select Text2,Text4 from "+Table4Name+" Where Rtype='ICAI' AND Text1='Committee' AND Num1="+M_Id+" "+AddAND+" Order by Text2";
	   cursorT = db.rawQuery(sql, null);
	   while(cursorT.moveToNext()){
			String data=Chkval(cursorT.getString(0));
			String Desig=Chkval(cursorT.getString(1));
		    if(data.trim().length()>1)
			{
		       if(Desig.trim().length()>1)
		    	   data=Desig+", "+data;
			   arrList.add(data); // Add data in Array List
			}
	   }
	   if(arrList.size()!=0)
	   {
		  btnCommittee.setVisibility(View.VISIBLE);//Display Addition Data Button
		  listAdpCommitee = new ArrayAdapter<String>(this, R.layout.listitem_additionaldata, arrList); 
	   }
	   else
	   {
		   btnCommittee.setVisibility(View.GONE);//Hide Addition Data Button
		   listAdpCommitee=null;
	   }
	   cursorT.close();
	}
	
	
	//Check Data is in Any New Option title or Not (10-02-2017)
	private void Check_New_Option(int M_Id)
	{
		ArrayList<String> arrList = new ArrayList<String>();
		String sql="Select Text1 from "+TableMiscName+" Where Rtype='ADDL' AND MemId="+M_Id+" Order by Text1";
		cursorT = db.rawQuery(sql, null);
		while(cursorT.moveToNext()){
			String data=Chkval(cursorT.getString(0));
		    if(data.trim().length()>1)
			{
			   arrList.add(data); // Add data in Array List
			}
		}
		if(arrList.size()!=0)
		{
		   filloremptyData(New_Op_Title,LL_NewOp,txt_NewOp);
		   listAdp_NewOpTitle = new ArrayAdapter<String>(this, R.layout.listitem_additionaldata, arrList); 
		}
		else
		{
		   filloremptyData("",LL_NewOp,txt_NewOp);
		   listAdp_NewOpTitle=null;
		}
		
		cursorT.close();
	}
	
	
	//Fill Values Main Data Display
	public void Fill_Main(String WResult,String[] AddData)
	{
		s = WResult.replace("^", "##")+" ";
		temp = s.split("##"); 
		str_memid=temp[0].toString();
		Strname=temp[1].toString();
		Stra1=temp[2].toString().trim();	
		Stra2=temp[3].toString().trim();
		Stra3=temp[4].toString().trim();
		City=temp[5].toString().trim();
		Stremail=temp[6].toString();
		Strmo=temp[7].toString();
		StrMemno=temp[8].toString();
		Strbg=temp[9].toString().trim();
		String Prefix_Name=temp[10].toString();
		dd=temp[11].toString().trim();
		mm=temp[12].toString();
		yy=temp[13].toString();
		MarAnnidd=temp[14].toString().trim();
		MarAnnimm=temp[15].toString();
		MarAnniyy=temp[16].toString();
		String C4_DOD_D=temp[17].toString();//Special Condition Value
		String Loc_Res=temp[18].toString().trim();
		String Loc_Off=temp[19].toString().trim();
		
		String DOB="",Ann_Date="",Region="",Mob2="",Email2="",Phone1="",Phone2="";
		if(MemDir.equals("Member"))
		{
			Region=temp[20].toString().trim();// New Field 15-03-2016
			Mob2=temp[21].toString().trim();// New Field 16-03-2016
			Email2=temp[22].toString().trim();// New Field 16-03-2016
			Phone1=temp[23].toString().trim();// New Field 16-03-2016
			Phone2=temp[24].toString().trim();// New Field 16-03-2016
		}
		
		///Added 29-12-2016 ////
		////Special Condition C4_DOD_D (Mob1,Mob2,Land1,Land2,Email1,Email2) is Displayed or Not
		if(C4_DOD_D.equalsIgnoreCase("Y"))
		{
			Strmo="";//Mob1
			Mob2="";//Mob2
			Stremail="";//Email1
			Email2="";//Email2
			Phone1="";//Phone1
			Phone2="";//Phone2
		}
		//////////////////////////////////////////
		
		
		//Set DOB (Updated at 07-02-2015 Tapas)
		if((dd.trim().length()==0 && mm.trim().length()==0) || (DOB_Disp==1)){
			DOB="";
		}
		else if(yy.trim().length()==0 || DOB_Disp==2){
			mm= getMonthForInt(Integer.parseInt(mm));
			DOB=dd+" "+mm;
		}
		else{
			DOB=dd+"-"+mm+"-"+yy;
		}
		filloremptyData(DOB,LLaydob,txtDob);// set DOB
		
		//Set Marriage Anniversary Date(Updated at 07-02-2015 Tapas)
		if(MarAnnidd.trim().length()==0 && MarAnnimm.trim().length()==0){
			Ann_Date="";
		}
		else if(MarAnniyy.trim().length()==0)
		{
			MarAnnimm= getMonthForInt(Integer.parseInt(MarAnnimm));
			Ann_Date=MarAnnidd+" "+MarAnnimm;
		}
		else{
			Ann_Date=MarAnnidd+"-"+MarAnnimm+"-"+MarAnniyy;
		}
		filloremptyData(Ann_Date,LLayMarAnni,txtMarAnni);// set Anniversary Date
		
		Stradd=Stra1+Stra2+Stra3+City;//txtPAdd1,txtPAdd2,txtPAddcity
		if((Stra1==null)||(Stra1.length()==0)){
			txtPAdd.setVisibility(View.GONE);
		}else {
			LLayaddress.setVisibility(View.VISIBLE);
			txtPAdd.setVisibility(View.VISIBLE);
			txtPAdd.setText(Stra1);
		}
		
		if((Stra2==null)||(Stra2.length()==0)){
			txtPAdd1.setVisibility(View.GONE);
		}else{
			LLayaddress.setVisibility(View.VISIBLE);
			txtPAdd1.setVisibility(View.VISIBLE);
			txtPAdd1.setText(Stra2);
		}
		
		if((Stra3==null)||(Stra3.length()==0)){
			txtPAdd2.setVisibility(View.GONE);
		}else{
			LLayaddress.setVisibility(View.VISIBLE);
			txtPAdd2.setVisibility(View.VISIBLE);
			txtPAdd2.setText(Stra3);	
		}
		if((City==null)||(City.length()==0)){
			txtPAddcity.setVisibility(View.GONE);
		}else{
			LLayaddress.setVisibility(View.VISIBLE);
			txtPAddcity.setVisibility(View.VISIBLE);
			txtPAddcity.setText(City);	
		}
		
		if(Stradd==null){
			 LLayaddress.setVisibility(View.GONE);
		}else if((Stradd!=null)&&(Stradd.length()!=0)){
			  LLayaddress.setVisibility(View.VISIBLE);
		}else{
			  LLayaddress.setVisibility(View.GONE);
		}
		
		
		img_Loc_Res.setVisibility(View.GONE);
		img_Loc_Off.setVisibility(View.GONE);
		double lati_Res=0,longi_Res=0,lati_Off=0,longi_Off=0;
		
		//Set Residence Location 
	     if(Loc_Res.contains("$$"))
	     {
	    	Loc_Res=Loc_Res.replace("$$", "#");
	    	String[] Arr1=Loc_Res.split("#");
	    	lati_Res=Double.parseDouble(Arr1[0]);
	    	longi_Res=Double.parseDouble(Arr1[1]);
	    	
	    	if(lati_Res>0 && longi_Res>0){
	    	   img_Loc_Res.setVisibility(View.VISIBLE);
	    	   lati_longi_Res=lati_Res+","+longi_Res;
	    	}
	     }
	     
	     //Set Office Location 
	     if(Loc_Off.contains("$$"))
	     {
	    	Loc_Off=Loc_Off.replace("$$", "#");
	    	String[] Arr1=Loc_Off.split("#");
	    	lati_Off=Double.parseDouble(Arr1[0]);
	    	longi_Off=Double.parseDouble(Arr1[1]);
	    	
	    	if(lati_Off>0 && longi_Off>0){
	    	   img_Loc_Off.setVisibility(View.VISIBLE);
	    	   lati_longi_Off=lati_Off+","+longi_Off;
	    	}
	     }
		
		
		Strname=Prefix_Name.trim()+" "+Strname.trim();
		txtPName.setText(Strname.trim());
		filloremptyData(Region,LLayRegion,txtRegion);
		filloremptyData(StrMemno,LLaymemno,txtMemno);	
		filloremptyData(Strmo,LLayMobile,TxtPMob);//Set Mobile 1
		
		//Set Mobile 2
		if(Mob2.length()>2)
			TxtPMob2.setText(", "+Mob2);
		else
			TxtPMob2.setText("");
		
        filloremptyData(Phone1,LLayPhone,TxtPhone1);//Set Phone 1
		
		//Set Phone 2
		if(Phone2.length()>2)
			TxtPhone2.setText(", "+Phone2);
		else
			TxtPhone2.setText("");
		
		filloremptyData(Stremail,LLayemail,TxtpEmail);//Set Email 1
		
		//Set Email 2
		if(Email2.length()>1){
		  TxtpEmail2.setVisibility(View.VISIBLE);
		  TxtpEmail2.setText(Email2);
		}
		else{
		  TxtpEmail2.setVisibility(View.GONE);
		}
		
		filloremptyData(Strbg,LLayblood,TxttBlood);	
		
		//Set Additional Data 
		filloremptyData(AddData[0],LL1,txt1);
		filloremptyData(AddData[1],LL2,txt2);
		filloremptyData(AddData[2],LL3,txt3);
		filloremptyData(AddData[3],LL4,txt4);
		filloremptyData(AddData[4],LL5,txt5);
		filloremptyData(AddData[5],LL6,txt6);
		filloremptyData(AddData[6],LL7,txt7);
		filloremptyData(AddData[7],LL8,txt8);
		filloremptyData(AddData[8],LL9,txt9);
		filloremptyData(AddData[9],LL10,txt10);
		////////////////////////////////
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
	

    public void filloremptyData(String str,LinearLayout ll,TextView txt){
	  if(str==null){
		ll.setVisibility(View.GONE);
	  }else if((str!=null)&&(str.trim().length()>0)){
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
	    setTitle("Group Manager");
        // add the custom view to the action bar
        actionBar.setCustomView(R.layout.search_layout);
       
        ImageView ImgVwSearch = (ImageView) actionBar.getCustomView().findViewById(R.id.imageSerchMenu);
        ImageView ImgSrchCrit = (ImageView) actionBar.getCustomView().findViewById(R.id.imagesearchcritri);
        ImageView ImgVwFilter= (ImageView) actionBar.getCustomView().findViewById(R.id.imageSetting);
       
        ImgVwSearch.setOnClickListener(new OnClickListener(){ 
            @Override 
              public void onClick(View arg0){
            	
            	ActionBarDisp_Alert(1);
            	 /*if(CHKsrhcri==1){
                 	ImgSrchCrit.setVisibility(View.VISIBLE);
                 }else{
                 	ImgSrchCrit.setVisibility(View.GONE);
                 }*/
            }
        });
        
        ImgVwFilter.setOnClickListener(new OnClickListener(){ 
            @Override 
              public void onClick(View arg0){
            	ActionBarDisp_Alert(2);
            }
        });
        
        ImgSrchCrit.setOnClickListener(new OnClickListener(){ 
            @Override 
              public void onClick(View arg0){
            	ActionBarDisp_Alert(3);
            }
        });
        
        
       actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM |ActionBar.DISPLAY_SHOW_HOME);
     }
	
	 public void ActionBarDisp_Alert(final int serchck) {
		 try{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			final FrameLayout frameView = new FrameLayout(context);
			builder.setView(frameView);
			final AlertDialog alertDialog = builder.create();
			LayoutInflater inflater = alertDialog.getLayoutInflater();
			
			View dialoglayout = inflater.inflate(R.layout.view_dialog_box, frameView);
			llaydialog= (LinearLayout) dialoglayout.findViewById(R.id.llViewbox);
			LinearLayout ll = new LinearLayout(this);
			llaydialog.addView(ll, new LinearLayout.LayoutParams(100, LayoutParams.WRAP_CONTENT));
			
			Button btnSearch= (Button) dialoglayout.findViewById(R.id.btnSearch);
			llaySrchCrit=(LinearLayout) dialoglayout.findViewById(R.id.linearLayoutSCrhcri);
			LinearLayout LLSettingMain = (LinearLayout) dialoglayout.findViewById(R.id.linearLayoutMM);
			
			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    WindowManager.LayoutParams wmlp = alertDialog.getWindow().getAttributes();
			wmlp.gravity = Gravity.TOP | Gravity.RIGHT;
			wmlp.x = 50;   //x position
			wmlp.y = 50;   //y position
			alertDialog.show(); 
			
			if(serchck==1){
				
				llaySrchCrit.setVisibility(View.GONE);
				
				String val="",Title="",fielddata="";
				allEds = new ArrayList<EditText>();
				TextVal= new ArrayList<String>();
				
				LinearLayout LL_Binocular = (LinearLayout) dialoglayout.findViewById(R.id.llaybinosrch);
				LinearLayout LL_add = (LinearLayout) dialoglayout.findViewById(R.id.addresssrch);
				LinearLayout LL_categ = (LinearLayout) dialoglayout.findViewById(R.id.catecontact);
				
				LL_Binocular.setVisibility(View.VISIBLE);
				LL_add.setVisibility(View.VISIBLE);
				LL_categ.setVisibility(View.GONE);
				
				ET_Search_Name = (EditText) dialoglayout.findViewById(R.id.etName);
				ET_Search_Mob = (EditText) dialoglayout.findViewById(R.id.etMobileBino);
				ET_Search_MemNo = (EditText) dialoglayout.findViewById(R.id.etMemberBino);
				ET_Search_Addr = (EditText) dialoglayout.findViewById(R.id.etAddBino);
				LLMain=(LinearLayout)dialoglayout.findViewById(R.id.LLMain);
				
				try{
					db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
					
					////Get MemNo textbox caption from table 4 (Added on 07-06-2017)
					String sql="Select Text1 from "+Table4Name+" Where Rtype='Memno_Cap'";
					cursorT = db.rawQuery(sql, null);
					if(cursorT.moveToFirst()){
						val=cursorT.getString(0);
						if(val==null)
							val="";
					}
					cursorT.close();
					
					if(val.length()>0)
						ET_Search_MemNo.setHint(val);
					///////////////////////////////////
					
					/////Additional Data search
					sql="Select Add1 from "+TableMiscName+" Where Rtype='Search_Add'";//"+Title.trim()+"
					cursorT = db.rawQuery(sql, null);
					if(cursorT.moveToFirst()){
						val=cursorT.getString(0);
						if(val==null)
							val="";
					}
					cursorT.close();
					db.close();
					if(val.contains("^")){
						 String [] temp=val.split("#");
						 for(int i=0;i<temp.length;i++){
							String s= temp[i].replace("^", "#")+" ";
							String []arr=s.split("#");
							Title=arr[0].toString();
							fielddata=arr[1].toString();
							LLMain.addView(NewView(i+10,Title,fielddata));
						 }
					}	
				}catch(Exception ex){
					System.out.println(ex.getMessage());
				}
				
			}else if(serchck==2){
				llaySrchCrit.setVisibility(View.GONE);
				LLSettingMain.setVisibility(View.VISIBLE);
				llaysetting = (LinearLayout) dialoglayout.findViewById(R.id.llayMenucheck);
				Button btnSaveFilter = (Button) dialoglayout.findViewById(R.id.btnSaveFilter);
				btnSaveFilter.setId(MY_BUTTON);
				btnSaveFilter.setOnClickListener(this);
				
				try{
					String sql="";
					if(Special_Dir_Condition.contains("A.M_Id"))
				    {
					   Special_Dir_Condition="";//Set Blank if Special_Dir_Condition contains a Query
				    }
					sql ="select distinct(Upper(oth)) from "+ Table2Name+" where oth is not null and  length(oth) <> 0 "+Special_Dir_Condition+" Order By Upper(oth)";
					db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 	
					cursorT = db.rawQuery(sql, null);
					i=0;
					if(cursorT.getCount()==0){
						alertDialog.cancel();
						Toast.makeText(getBaseContext(), "No Setting Available", 0).show();
					}else{
						 if (cursorT.moveToFirst()) {
							   do {
								   cb = new CheckBox(this);
								   sql="";
								   sql ="select Text1 from "+ Table4Name +" Where rtype = 'RecShow' and text1 = '"+cursorT.getString(0)+"'";
								   Cursor cursorT2 = db.rawQuery(sql, null);
								   if(cursorT2.getCount()==0){
									   cb.setText(cursorT.getString(0));
								   }else{
									   cb.setText(cursorT.getString(0));
									   cb.setChecked(true);
								   }
								   cursorT2.close();
						           cb.setId(i+10);
						           llaysetting.addView(cb);
						    	   i++;
					    		 } while (cursorT.moveToNext());
					    	 }
					}
					cursorT.close();
					db.close();
				}catch(Exception ex){
					System.out.println(ex.toString());
				}
			}
			else if(serchck==3){
					String []sp=null;
					String caption="",value="",Tablename="";
					llaySrchCrit.setVisibility(View.VISIBLE);
					LLaySearchCriteriashow = (LinearLayout) dialoglayout.findViewById(R.id.llaySrchcriteria);
					BtnSrchcrit=(Button)dialoglayout.findViewById(R.id.butnSaveSechCrt);
					String [] tempsrch = strSrchcrival.split("#"); 
		            for(int i=0;i<tempsrch.length; i++){
			         	 String s= tempsrch[i].toString().replace("^", "#")+" ";
			             sp=s.trim().split("#");   
			             caption=sp[0].toString().trim();
			             value=sp[1].toString().trim();
			             Tablename=sp[2].toString().trim();
			             try{
			 				String sql="";
			 				int j=1;
			 				sql ="select distinct("+value+") from "+"C_"+Str_user+"_"+Tablename+" Where "+value+" Is Not NULL AND LENGTH("+value+")<>0  order by "+value;
			 				db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 	
			 				cursorT = db.rawQuery(sql, null);
			 				 int temp_size=cursorT.getCount();
			 				 Arrschcri=new String[temp_size+1];
			 				 Arrschcri[0]="";
		 					 if (cursorT.moveToFirst()) {
		 						do {
		 							Arrschcri[j]=cursorT.getString(0);
					    		    j++;	
		 						} while (cursorT.moveToNext());
		 				     }	 
			 				cursorT.close();
			 				db.close();
			             }catch(Exception ex){
			            	 System.out.println(ex.getMessage());
			             }
			             
			             if(Arrschcri.length==0){
			            	 Arrschcri[0]=""; 
			             }
			             StrCaptionvale=StrCaptionvale+value+"#";
			             StrTable=StrTable+Tablename+"#";
			             LLaySearchCriteriashow.addView(NewView(i+1000,caption,Arrschcri));
		              }
		            //System.out.println(allSps.size());
		            
		            BtnSrchcrit.setOnClickListener(new OnClickListener(){ 
		                @Override 
		                public void onClick(View arg0){
		                	/*String StrF=" and M_id in (",StrM="",StrL=")",StrTotal="",StrQuery="";
		                	String []ttemp=StrCaptionvale.split("#");
		                	String []tbtemp=StrTable.split("#");
		                	for(int i=0; i < allSps.size(); i++){
		     	            	 String FieldData = allSps.get(i).getSelectedItem().toString().trim();
		     	            	 if(FieldData.length()!=0){
		     	            		StrQuery="select memid from c_"+Str_user+"_"+tbtemp[i]+" where "+ttemp[i]+" = '"+FieldData+"'";
		     	            		StrM=StrF+StrQuery+StrL;
		     	            		StrTotal=StrTotal+StrM;
		     	            		System.out.println("v "+StrTotal+" "+StrTotal.length());
		     	            	 } 
		                	}
		                	if(StrTotal.length()!=0){
		                		//StrTotal = StrTotal.substring(0, StrTotal.length()-1).trim();
		                		System.out.println("fi:: "+StrTotal+" "+StrTotal.length());
		                		 editorCrit = shrdcriteria.edit(); // Edit Shared Pref critria Records
		                		 editorCrit.putString("Critval", StrTotal);
		                		 editorCrit.commit();
		                		 callitself();
	     	            	 }else{
	     	            		Toast.makeText(context, "Slelect any field", 1).show();
	     	            	 }*/
		                }
		            });	
			}
			
			btnSearch.setOnClickListener(new OnClickListener(){ 
				@Override 
	            public void onClick(View arg0){
	            	    
	            	    String StrName=ET_Search_Name.getText().toString();
		            	String StrMob=ET_Search_Mob.getText().toString();
		            	String StrMemNo=ET_Search_MemNo.getText().toString();
		            	String StrAddr=ET_Search_Addr.getText().toString();
		            	
		            	listarr = new ArrayList<String>();
		            	for(int j=0;j<allEds.size();j++){
		            	   String FieldData = allEds.get(j).getText().toString();
		            	   String DValue = TextVal.get(j);
		            	   if((FieldData!=null)&&(FieldData.length()!=0)){
		            		   listarr.add(FieldData+"#@"+DValue); 
							}
		            	}
		            	int count=listarr.size();
		            	
		            	if((StrName.length()==0)&&(StrMob.length()==0)&&(StrMemNo.length()==0)&&(StrAddr.length()==0)&&(count==0)){
		            		Toast.makeText(getBaseContext(), "input atleast one field!", 0).show();
		            	}else{
		            		alertDialog.dismiss();
		            		waiting();
		            		
		            		String query=StrName+"#"+StrMob+"#"+StrMemNo+"#"+StrAddr;
		            		menuIntent= new Intent(getBaseContext(),SearchDisplay.class);
		            		menuIntent.putExtra("Clt_LogID",logid);
		            		menuIntent.putExtra("Clt_Log",Log);
		            		menuIntent.putExtra("Clt_ClubName",ClubName);
		            		menuIntent.putExtra("UserClubName",Str_user);
		            		menuIntent.putExtra("STRslct","2");
		            		//menuIntent.putExtra("StrCriteria",StrShrdCrival);
		            		menuIntent.putExtra("Qury",query);
		            		menuIntent.putExtra("AppLogo",AppLogo);
		            		menuIntent.putExtra("AddData1",Additional_Data);
			        		menuIntent.putExtra("AddData2",Additional_Data2);
			        		menuIntent.putExtra("Dir_Filter_Condition",Dir_Filter_Condition);
			        		menuIntent.putExtra("Special_Dir_Condition",Special_Dir_Condition);
			        		menuIntent.putExtra("New_Op_Title",New_Op_Title);//Added in 10-02-2017
			        		menuIntent.putExtra("CCBYear",CCBYear);//CCB Year Added 16-11-2018
			        		
			        		if(count==0){
			        			menuIntent.putStringArrayListExtra("stock_list", null);
			        		}else{
			        			menuIntent.putStringArrayListExtra("stock_list", listarr);
			        		}
		                	startActivity(menuIntent);
		                	//finish();
		            		//Toast.makeText(getBaseContext(),Str_etmobsrch+"  "+Str_etmemNosrch+"  "+Str_etAddsrch, 1).show();
		            	}
	             }
	        });
		 }catch(Exception ex){
			 System.out.println("msg:: "+ex.getMessage());
		 }
	 }

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//boolean bl=false;
		String sql= null;
		String aa = null;
        switch (v.getId()) {
        case MY_BUTTON:
        	//if(aa==)
        	db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
    		sql ="delete from "+Table4Name+" where Rtype = 'RecShow'";	
			db.execSQL(sql);
			
			for(int i=0; i<llaysetting.getChildCount(); i++) {
                View nextChild = llaysetting.getChildAt(i);
				if(nextChild instanceof CheckBox) {
                  CheckBox check = (CheckBox) nextChild;
                  if (check.isChecked()) {
                    aa=  check.getText().toString();
                    sql = "insert into "+Table4Name+"(Rtype,Text1) values('RecShow','"+aa+"')";
                    db.execSQL(sql); 
                  }    
               }
	        } 
		   db.close();
		   Toast.makeText(getBaseContext(), "Filter Applied", 0).show();
		   callitself();
       }
	}
	
	 public void callitself() {
		 menuIntent= new Intent(getBaseContext(),SwipeScreen.class);
		 menuIntent.putExtra("Clt_LogID",logid);
		 menuIntent.putExtra("Clt_Log",Log);
		 menuIntent.putExtra("Clt_ClubName",ClubName);
		 menuIntent.putExtra("UserClubName",Str_user);
		 menuIntent.putExtra("AppLogo",AppLogo);
		 menuIntent.putExtra("Special_Dir_Condition",Special_Dir_Condition);
		 menuIntent.putExtra("CFrom",CFrom);//Comes From
		 menuIntent.putExtra("CCBYear",CCBYear);//CCB Year Added 16-11-2018
		 startActivity(menuIntent);
    	 finish();
	 }
	 
	 
	 private LinearLayout NewView(int id,String TvTitle,String DValue)
		{
			LinearLayout L1=new LinearLayout(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(2,3,2,18);
			L1.setLayoutParams(params);
			L1.setBackgroundResource(R.drawable.edit_text_transparent_new);
			
			L1.setId(id);
			L1.setOrientation(android.widget.LinearLayout.HORIZONTAL);
			
			//Add Textview
			/*TextView tv=new TextView(this);
			tv.setTextSize(19);
			tv.setText(TvTitle);
			L1.addView(tv);*/
			
			L1.addView(editText(id,TvTitle,DValue));
			
			return L1;
		}
		
	 private EditText editText(int id,String hint,final String DValue) {
		final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		final EditText ET = new EditText(this);
		allEds.add(ET);// Add EditText in Edittext List
		TextVal.add(DValue);
		ET.setLayoutParams(lparams);
        ET.setId(id);
        ET.setHint(hint);
        ET.setText("");
        ET.setBackgroundColor(Color.WHITE);
        
        return ET;
     }
	
		
  	 // This function is used to get Directory Filter Condition 
	 public String DirFilter_Condition() {
		 String Result="";
		 sqlSearch = "SELECT Text1 FROM "+Table4Name+" Where Rtype = 'RecShow'";
		 cursorT = db.rawQuery(sqlSearch, null);
		 int Rcount=cursorT.getCount();
		 if(Rcount!=0){
	    	   if (cursorT.moveToFirst()) {
				   do {
					   Result=Result+"'"+cursorT.getString(0)+"'"+",";
		    		 } while (cursorT.moveToNext());
		       }
	    	    if (Result.endsWith(",")) {
	    		   Result = Result.substring(0, Result.length() - 1);
	    		}
	    	    Result=" AND Oth in ("+Result+")";
		 }
		 cursorT.close(); 
	     return Result;
	 }
	 
	 
	 
	//Check Set GPS Location Option Are Displayed(Visible) Or Not
  	private boolean ChkGPSLocation_Displayed()
  	{
  		boolean R=false;
  		String MID="";
  		String sql="Select M_ID from "+Table4Name+" where Rtype='E_LOC'";
  		cursorT = db.rawQuery(sql, null);
  	    if(cursorT.moveToFirst()){
  	    	MID=cursorT.getString(0);
  		}
  		cursorT.close();
  		if(MID.trim().length()>0)
  			R=true;
  		
  		return R;
  	}
	 
	 
  	
    ///Set GPS Location Residence / Office
  	private void DisplayGPSLocation_Dialog(String Lati_Longi,int Type)
  	{
		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		dialog.setContentView(R.layout.zoomimage);
		WebView WebVw1 = (WebView)dialog.findViewById(R.id.imageViewzoom);
	
		String url = "http://maps.google.com/maps/api/staticmap?center=" + Lati_Longi + "&zoom=15&size=400x400&sensor=true";
		
		String text="";
  		if(Type==1)//Set Residence Loctaion
  		{
  			 text = "<html><body><p align=\"justify\">Location Residence";     
  		}
  		else if(Type==2)//Set Office Location
  		{
  			 text = "<html><body><p align=\"justify\">Location Office";
  		}
  		text+= "</p><iframe width='250' height='250' src='https://maps.google.com/maps?q="+Lati_Longi+"&hl=es;z=15&amp;output=embed'></iframe></body></html>";
  		WebVw1.loadDataWithBaseURL(null, text, "text/html", "UTF-8", null);
  		WebVw1.getSettings().setJavaScriptEnabled(true);
      	   
      	dialog.show();
  	}
  	
	 
	// This function is used to get Default Filter  (Added on 10-07-2017)
	 public void Add_DefaultFilter_Condition() {
		 String DFilterColumnName="";
		 sqlSearch = "SELECT Text1 FROM "+Table4Name+" Where Rtype = 'D_Filter'";
		 cursorT = db.rawQuery(sqlSearch, null);
		 int Rcount=cursorT.getCount();
		 if(Rcount!=0){
	    	   if (cursorT.moveToFirst()) {
				  DFilterColumnName=Chkval(cursorT.getString(0));
		       }
		 }
		 cursorT.close(); 
		 
		 if(DFilterColumnName.length()>0)
		 {
			 String ColVal="";
			 sqlSearch = "SELECT "+DFilterColumnName+" FROM "+Table2Name+" Where M_id="+logid;
			 cursorT = db.rawQuery(sqlSearch, null);
			 Rcount=cursorT.getCount();
			 if(Rcount!=0){
		    	   if (cursorT.moveToFirst()) {
		    		   ColVal=Chkval(cursorT.getString(0));
			       }
			 }
			 cursorT.close(); 
			 
			 if(ColVal.length()>0)
			 {
			   sqlSearch = "Insert into "+Table4Name+"(Rtype,Text1) values('RecShow','"+ColVal+"')";
               db.execSQL(sqlSearch); 
			 }
		 }
	 }
	 
	 
	 
	 public int getSearchCriteria() {
		 int returnval=0;
		   sqlSearch = "SELECT Text2 FROM "+Table4Name+" Where Rtype='Sea_Cri'";
		   cursorT = db.rawQuery(sqlSearch, null);
		    int Sc_count=cursorT.getCount();
		     if(Sc_count>0){
		    	 returnval=1;
		    	 if (cursorT.moveToFirst()) {
				   do {
					   strSrchcrival=cursorT.getString(0);
			    	  } while (cursorT.moveToNext());
			     }
		     }else{
		    	 returnval=0;
		     }
		    cursorT.close(); 
			return returnval;
	 }
	 
	 
	 //boolean check for Hide Spouse Address or Not 
	 private boolean ChkHideSpouseAddress()
	 {
		 boolean flag=false;
	     sqlSearch = "SELECT M_ID FROM "+Table4Name+" Where Rtype='Hide_Spouse_Add'";
	     cursorT = db.rawQuery(sqlSearch, null);
	     int RCount=cursorT.getCount();
	     if(RCount>0)
	    	flag=true;
	     cursorT.close(); 
		 return flag;
	 }
	 
	 private LinearLayout NewView(int id,String TvTitle,String[] DValue)
		{
			LinearLayout L1=new LinearLayout(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(5,5,5,5);
			L1.setLayoutParams(params);
			L1.setId(id);
			L1.setOrientation(android.widget.LinearLayout.HORIZONTAL);
			
			//Add Textview
			TextView tv=new TextView(this);
			tv.setTextSize(18);
			tv.setText(TvTitle);
			tv.setTextColor(Color.BLUE);
			tv.setTypeface(Typeface.DEFAULT_BOLD);
			L1.addView(tv);
			L1.addView(spinneradd(id,TvTitle,DValue));
			return L1;
		}
	 
	 private Spinner spinneradd(int id,String hint,String[] DValue) {
			final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			Spinner SP = new Spinner(this);
			allSps.add(SP);// Add EditText in Edittext List
			SP.setLayoutParams(lparams);
	        SP.setId(id);
	        SP.setLeft(5);
	        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item, DValue);
	        SP.setAdapter(spinnerArrayAdapter);
	        return SP;
	    }
	 
	 public void waiting(){
		 final ProgressDialog progressDialog = ProgressDialog.show(context, "",  "Loading......");
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
}