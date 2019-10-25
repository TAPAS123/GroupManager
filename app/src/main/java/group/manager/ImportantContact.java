package group.manager;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class ImportantContact extends Activity{
	ImageView imageViewPIC,IvmainUpdate;
	EditText TxtNameCont,TxtMob1Cont,TxtMob2Cont,TxtphCont,TxtAdd1Cont,TxtAdd2Cont,TxtAdd3Cont,TxtRemarkCont,TxtCategCont;
	Spinner SpCateg;
	AlertDialog.Builder ad;
	Intent menuIntent;
	Context context=this;
	byte[] AppLogo,pic=null;
	SQLiteDatabase db;
	Cursor cursorT;
	int i=1;
	String mainStr_user,TableMiscName,logname,LOG,TableFamilyName,Str_MEMid,mainValue,catstr="";
	private static final int CAMERA_REQUEST = 1;
	private static final int PICK_FROM_GALLERY = 2;
	String [] categArr={"Other"},temp;
	ArrayAdapter<String> adapterflag;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.impcontact);
		imageViewPIC = (ImageView)findViewById(R.id.ivContactperson);
		IvmainUpdate= (ImageView)findViewById(R.id.ivupdateContact);       
		TxtNameCont=(EditText)findViewById(R.id.etContname);
		TxtMob1Cont=(EditText)findViewById(R.id.etMobile1);
		TxtMob2Cont=(EditText)findViewById(R.id.etMobile2);
		TxtphCont=(EditText)findViewById(R.id.etMobile3);
		TxtAdd1Cont=(EditText)findViewById(R.id.etC1aDD);
		TxtAdd2Cont=(EditText)findViewById(R.id.etAddC2);
		TxtAdd3Cont=(EditText)findViewById(R.id.etAddC3);
		TxtRemarkCont=(EditText)findViewById(R.id.etContRemak);
		TxtCategCont=(EditText)findViewById(R.id.etContCateg);
		SpCateg=(Spinner)findViewById(R.id.spinnerContCateg);
		ad = new AlertDialog.Builder(context);
		
		menuIntent = getIntent(); 
		mainStr_user =  menuIntent.getStringExtra("UserClubName");
		LOG =  menuIntent.getStringExtra("Clt_Log");
		Str_MEMid =  menuIntent.getStringExtra("Clt_LogID");
		logname =  menuIntent.getStringExtra("Clt_ClubName");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		//mainValue= menuIntent.getStringExtra("WebPers");
		
		TableMiscName="C_"+mainStr_user+"_MISC";
		TableFamilyName="C_"+mainStr_user+"_Family";
		Set_App_Logo_Title(); ///////// Set App Logo and Title
		
		int s_count=0;
		 db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
		 String sql="Select Distinct(DOB_D) from "+TableFamilyName+" where MemId=0 and DOB_D is not null and DOB_D<>''";
		 cursorT = db.rawQuery(sql, null);
		 s_count= cursorT.getCount();
		 
		  if(s_count==0){
			 adapterflag= new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,categArr);
		     adapterflag.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			 SpCateg.setAdapter(adapterflag); 
		  }else{
			 temp=new String[s_count+2];
			 temp[s_count+1]="Other"; 
			 temp[0]="";
			 if (cursorT.moveToFirst()) {
			   do {
			      temp[i]=cursorT.getString(0);
			      i++; 
			    } while (cursorT.moveToNext());
			  }
			 adapterflag= new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,temp);
			 adapterflag.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			 SpCateg.setAdapter(adapterflag); 
		 }
		 
		 cursorT.close(); 
		 //sql="Delete From "+TableFamilyName+" Where MemId=0";
		 //db.execSQL(sql);
		 db.close();
		 
		 SpCateg.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent,View view, int position, long id) {
					// TODO Auto-generated method stub
					String val = parent.getItemAtPosition(position).toString();
					if(val.equals("Other")){
						TxtCategCont.setVisibility(View.VISIBLE);	
					}else{
						TxtCategCont.setVisibility(View.GONE);	
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {}

	  		});
		 
		final String[] option = new String[] {"Take from Camera","Select from Gallery"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.select_dialog_item, option);
		ad.setTitle("Select Option");
		ad.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.e("Selected Item", String.valueOf(which));
				if (which == 0) {
					callCamera();
				}
				if (which == 1) {
					callGallery();
				}
			}
		});
		
		final AlertDialog dial = ad.create();
		imageViewPIC.setOnClickListener(new OnClickListener()
        { 
			@Override
			public void onClick(View arg0) {
				dial.show();
			}
        });
		
		IvmainUpdate.setOnClickListener(new OnClickListener()
        { 
			@Override
			public void onClick(View arg0) {
				 String Name= TxtNameCont.getText().toString().trim();
				 String Mob1= TxtMob1Cont.getText().toString().trim();   
				 String Mob2=TxtMob2Cont.getText().toString().trim();
				 String ph =TxtphCont.getText().toString().trim();
				 String Add1 =TxtAdd1Cont.getText().toString().trim();
				 String Add2 =TxtAdd2Cont.getText().toString().trim();
				 String Add3= TxtAdd3Cont.getText().toString().trim();
				 String remark= TxtRemarkCont.getText().toString().trim();
				 String categ= TxtCategCont.getText().toString().trim();
				 String Strspcategory= SpCateg.getSelectedItem().toString().trim();
				 if(Name.length()==0){
					 TxtNameCont.setError("Please enter name");
				 }
				 else if((Mob1.length()==0)||(Mob1.length()<10)){
					 TxtMob1Cont.setError("Please enter correct phone number");
				 }else{
					 try{
						 String StrCategory="",sql="";
						 int IDss=0;
						 if((Strspcategory.equals("Other"))&&(categ.length()==0)){
							 StrCategory=""; 
						 }else if(categ.length()!=0){
							 StrCategory=categ;
						 }else if(categ.length()==0){
							 StrCategory= Strspcategory;
						 }
						 
						 db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
						 sql = "SELECT max(M_Id) as [Id] from "+TableFamilyName;
	    		    	 cursorT = db.rawQuery(sql, null);
	    		    	  if (cursorT.moveToNext()) {
	    					IDss=cursorT.getInt(0);
	    				   } 
	    		    	  cursorT.close();
	    		    	  
	    				  if(IDss<1000000){
	    					IDss=1000001;
	    				  }else{
	    					IDss=IDss+1;
	    				  }
	    				 //sql="Insert into "+TableFamilyName+" (M_ID,MemId,Name,Mob_1,Mob_2,Text1,Text2,Text3,Text4,Text5,Remark,Text6) Values ("+IDss+",0,'"+Name+"','"+Mob1+"','"+Mob2+"','"+ph+"','"+Add1+"','"+Add2+"','"+Add3+"','"+StrCategory+"','"+remark+"','CONT_PEND')";
						 //db.execSQL(sql);////////////M_ID,SYNCID,SyncDT???????????????????????????
						 ContentValues cv = new  ContentValues();
						 cv.put("Pic",pic);
						 cv.put("M_ID", IDss); 
					     cv.put("MemId", 0); 
					     cv.put("Name", Name); 
					     cv.put("Mob_1",Mob1 ); 
					     cv.put("Mob_2",Mob2 ); 
					     cv.put("Relation",ph ); 
					     cv.put("Father", Add1); 
					     cv.put("Mother", Add2); 
					     cv.put("Current_loc", Add3); 
					     cv.put("DOB_D", StrCategory); 
					     cv.put("DOB_M", remark); 
					     cv.put("Text1", "CONT_PEND"); 
						 db.insert(TableFamilyName, null, cv);
						 db.close();
						 ad.setTitle( Html.fromHtml("<font color='#FF7F27'>Result !</font>"));
	           			 ad.setMessage("Contact save sucessfully.");
	           			 ad.setCancelable(false);
	           			 ad.setNegativeButton("OK", new DialogInterface.OnClickListener(){
	           	           public void onClick(DialogInterface dialog, int which) {
	           	            	dialog.dismiss();
	           	            	back();
	           	           }
	           	        });
	           		    ad.show();	
					 }catch(Exception ex){
						 System.out.println("1: "+ex.getMessage());
					 }
				}
			}
        });
	}
	
	 private void Set_App_Logo_Title()
	 {
		 setTitle(logname); // Set Title
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
				Bitmap ResizeImg=ScaleDownBitmap(yourImage,160,204,true);
				// convert bitmap to byte
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				ResizeImg.compress(Bitmap.CompressFormat.PNG, 100, stream);
				pic = stream.toByteArray();
				imageViewPIC.setImageBitmap(ResizeImg);
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
		
		//cameraIntent.PutExtra(MediaStore.ExtraOutput, Uri.FromFile(App._file));
		//cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
		//cameraIntent.putExtra("return-data", true);
		 public void callCamera() {
				Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				cameraIntent.putExtra("crop", "true");
				cameraIntent.putExtra("aspectX", 0);
				cameraIntent.putExtra("aspectY", 0);
				cameraIntent.putExtra("outputX", 200);
				cameraIntent.putExtra("outputY", 150);
				startActivityForResult(cameraIntent, CAMERA_REQUEST);
			}
		 
		/// open gallery method
		public void callGallery() {
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

		public boolean onKeyDown(int keyCode, KeyEvent event) 
		 {
		   	 if (keyCode == KeyEvent.KEYCODE_BACK) {
		   		back();
		   	    return true;
		   	 }
		   	return super.onKeyDown(keyCode, event);
		 }
		
		private void back(){
			Intent MainBtnIntent= new Intent(context,SwipeScreenContact.class);
			MainBtnIntent.putExtra("Clt_Log",LOG);
			MainBtnIntent.putExtra("Clt_LogID",Str_MEMid);
			MainBtnIntent.putExtra("Clt_ClubName",logname);
			MainBtnIntent.putExtra("UserClubName",mainStr_user);
			MainBtnIntent.putExtra("AppLogo", AppLogo);
		    startActivity(MainBtnIntent);
		    finish();
		}
}
