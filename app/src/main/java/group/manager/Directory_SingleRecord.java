package group.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Directory_SingleRecord extends Activity{
	TextView txtPName,txtPAdd,TxtPMob,TxtPMob2,TxtPhone1,TxtPhone2,TxtpEmail,TxtpEmail2,TxttBlood,txtMAX,txtMemno,txtRegion,txtDob,txtMarAnni,txtPAdd1,txtPAdd2,txtPAddcity;
	String s,Strname,Stradd,Strmo,Stremail,Strbg,Stra1,Stra2,Stra3,str_memid,Str_user,Str_main,Str_spous,Str_child,MobileSTR,EmailStr,Log,
	ClubName,logid,sqlSearch,finalresult,Table2Name,SelectSTR="0",StrMemno,Table4Name,TableMiscName,
	City,dd,mm,yy,MarAnniyy,MarAnnidd,MarAnnimm,MobileSpSTR;
	String [] temp;
	ImageView ImgMain;
	Intent menuIntent;
	AlertDialog ad;
	SQLiteDatabase db;
	Cursor cursorT;
	int i=0;
	byte[] imgP,imgSpouse=null;
	Integer tempsize;
	Dialog dialog;
	LinearLayout LLaymemno,LLayRegion,LLayMobile,LLayPhone,LLayaddress,LLayemail,LLayblood,LLaydob,llaydialog,llaysetting,LLayMarAnni;
	Button btnMoreDetails,btnSpouseDetails,btnFamilyDetails;
	byte[] AppLogo;
	LinearLayout LL1,LL2,LL3,LL4,LL5,LL6,LL7,LL8,LL9,LL10,LL_NewOp;
	TextView txt1,txt2,txt3,txt4,txt5,txt6,txt7,txt8,txt9,txt10,txt_NewOp;
	String Additional_Data,Additional_Data2;
	final Context context=this;
	ArrayAdapter<String> listAdapter=null,listAdp_NewOpTitle=null;
	String MemDir="Member",SpouseDetails="",TableFamilyName;
	int MId_family=0;
	String[] Spouse_Addition_Data;
	int DOB_Disp=0,M_Id=0;
	String strSrchcrival="",StrCaptionvale="",StrTable="";
	Editor editorCrit;
	LinearLayout LLMain;
	ArrayList<String> listarr,TextVal;
	String New_Op_Title="";
	
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.directory_singlerecord);
	        
	        ImgMain = (ImageView)findViewById(R.id.ivmain);
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
			
			btnMoreDetails=(Button)findViewById(R.id.btnMoreDetails); // Button For Additional Details
			btnSpouseDetails=(Button)findViewById(R.id.btnSpouse); //Button for Spouse Details
			btnFamilyDetails=(Button)findViewById(R.id.btnFamily); //Button for Family Details
			
			menuIntent = getIntent(); 
			Log =  menuIntent.getStringExtra("Clt_Log");
			logid =  menuIntent.getStringExtra("Clt_LogID");
			ClubName =  menuIntent.getStringExtra("Clt_ClubName");
			Str_user =  menuIntent.getStringExtra("UserClubName");
			AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
			M_Id=menuIntent.getIntExtra("Mid", M_Id);
			
	        Table2Name="C_"+Str_user+"_2";
			Table4Name="C_"+Str_user+"_4";
			TableMiscName="C_"+Str_user+"_MISC";
			TableFamilyName="C_"+Str_user+"_Family";
			
			Set_App_Logo_Title(); // Set App Logo and Title
			
			Get_SharedPref_Values(); // Get Shared Pref Values of MemDir(Display Member/Spouse)
			
			//Get Additional Data 1 & 2 for MainScreen & PopupScreen 
			Get_Additional_Details();
			
			Get_New_Op();//Added in 10-02-2017
	 		        
	 		FillMainData(M_Id);// Fill All Record
		    
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
						 
						 String FullAddr=Add1+Add2+Add3+City;//Set Address
						 
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
		    	    startActivity(menuIntent);
		    	   // finish();
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
}
		
   
	//Get Additional Data 1 & 2 for MainScreen & PopupScreen 
    private void Get_Additional_Details()
    {
    	Additional_Data="NODATA"; 
 		Additional_Data2="NODATA";
 		
 		db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
 		String Qry="Select Add1,Add2 from "+TableMiscName+" Where Rtype='MASTER'";
 		cursorT = db.rawQuery(Qry, null);
 		while(cursorT.moveToNext())
	    {
 		    Additional_Data=Chkval(cursorT.getString(0));//For MainScreen
 		    Additional_Data2=Chkval(cursorT.getString(1));//For PopupScreen
 		    if(Additional_Data.length()==0)
 		    	Additional_Data="NODATA";
 		    if(Additional_Data2.length()==0)
 		    	Additional_Data2="NODATA";
			break;
	    }
 		cursorT.close();
 		//////////////////////////////
 		db.close(); 
    }
    
    
    ///////Get New Option Caption or Title which is used to display some list in popup screen
    private void Get_New_Op()
    {
    	db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
        ///////Get New Option Caption or Title which is used to display some list in popup screen
		String Qry="Select Text1 from "+Table4Name+" Where Rtype='ADDL'";
		cursorT = db.rawQuery(Qry, null);
		if(cursorT.moveToFirst())
		{
		   New_Op_Title=cursorT.getString(0);
		}
		cursorT.close();
		////////////////////////////////////////////////
		db.close(); 
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
	

	public void FillMainData(int Mid) {
		// TODO Auto-generated method stub
		try{
		 int MemId=0;	
		 String sql="";
		 String[] AddDATA=new String[10];// Array For Additional Data
		 db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 

		 if(MemDir.equals("Member")) 
 			 sql="select M_id,M_Name,M_Add1,M_Add2,M_Add3,M_City,M_Email,M_Mob,MemNo,M_BG,M_Pic,(C4_BG || \"\" || ifnull(C4_DOB_Y,'')),M_DOB_D,M_DOB_M,M_DOB_Y,M_MrgAnn_D,M_MrgAnn_M,M_MrgAnn_Y,C4_DOB_D,C4_Mob,M_SndMob,M_Email1,M_Land1,M_Land2 from "+Table2Name+" where M_id="+Mid ;
		 else 
			 sql="select M_id,S_Name,M_Add1,M_Add2,M_Add3,M_City,S_Email,S_Mob,C4_Gender,S_BG,S_Pic,C3_BG,S_DOB_D,S_DOB_M,S_DOB_Y,M_MrgAnn_D,M_MrgAnn_M,M_MrgAnn_Y,C4_DOB_D,C3_FName,C3_MName from "+Table2Name+" where M_id="+Mid ;
		 
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
			            Chkval(cursorT.getString(18));
		    imgP=cursorT.getBlob(10);
		    if(MemDir.equals("Member"))
		    {
		    	finalresult=finalresult+"^"+Chkval(cursorT.getString(19))+"^"+Chkval(cursorT.getString(20))+"^"+Chkval(cursorT.getString(21))+"^"+Chkval(cursorT.getString(22))+"^"+Chkval(cursorT.getString(23));
		    }
		    else
		    {
		    	String Spouse_Company=Chkval(cursorT.getString(19));
		    	String Spouse_Profession=Chkval(cursorT.getString(20));
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
		  sql="Select M_id,S_Name,M_Add1,M_Add2,M_Add3,M_City,S_Email,S_Mob,C4_Gender,S_BG,S_Pic,C3_BG,S_DOB_D,S_DOB_M,S_DOB_Y,M_MrgAnn_D,M_MrgAnn_M,M_MrgAnn_Y,C4_DOB_D,C3_Mob,C3_FName,C3_MName from "+Table2Name+" where M_id="+M_Id;
	   else
		  sql="select M_id,M_Name,M_Add1,M_Add2,M_Add3,M_City,M_Email,M_Mob,MemNo,M_BG,M_Pic,(C4_BG || \"\" || ifnull(C4_DOB_Y,'')),M_DOB_D,M_DOB_M,M_DOB_Y,M_MrgAnn_D,M_MrgAnn_M,M_MrgAnn_Y,C4_DOB_D,C4_Mob from "+Table2Name+" where M_id="+M_Id;
		   
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
	public void Fill_Main(String WResult,String[] AddData){
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
		
		String DOB="",Ann_Date="",Region="",Mob2="",Email2="",Phone1="",Phone2="";
		if(MemDir.equals("Member"))
		{
			Region=temp[18].toString().trim();// New Field 15-03-2016
			Mob2=temp[19].toString().trim();// New Field 16-03-2016
			Email2=temp[20].toString().trim();// New Field 16-03-2016
			Phone1=temp[21].toString().trim();// New Field 16-03-2016
			Phone2=temp[22].toString().trim();// New Field 16-03-2016
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
		Strname=Prefix_Name.trim()+" "+Strname.trim();
		txtPName.setText(Strname.trim());
		filloremptyData(Region,LLayRegion,txtRegion);
		filloremptyData(StrMemno,LLaymemno,txtMemno);	
        filloremptyData(Strmo,LLayMobile,TxtPMob);//Set Mobile 1
		
		//Set Mobile 2
		if(Mob2.length()>2)
			TxtPMob2.setText(", "+Mob2);
		
        filloremptyData(Phone1,LLayPhone,TxtPhone1);//Set Phone 1
		
		//Set Phone 2
		if(Phone2.length()>2)
			TxtPhone2.setText(", "+Phone2);
		
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
	  }else if((str!=null)&&(str.trim().length()!=0)){
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
}
