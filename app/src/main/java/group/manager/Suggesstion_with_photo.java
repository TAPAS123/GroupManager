package group.manager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class Suggesstion_with_photo extends Activity
{
	String sqlSearch,Table2Name,Table4Name,Log,ClubName,logid,Str_user,StrTitle,StrDesc,Str_IEMI,WebResult;//,StrEmail=""
	Intent menuIntent,MainBtnIntent;
	String [] temp;
	Context context=this;
	EditText txtTitle,txtDesc;
	ImageView ivSubmitSug;
	AlertDialog ad;
	TelephonyManager tm;
	//////////////////////////////////////
	WebServiceCall webcall;
	Chkconnection chkconn;
	ProgressDialog Progsdial;
	Thread networkThread;
	boolean InternetPresent = false;
	byte[] AppLogo;
	String [] ArrCategory={"- - Select - -","Suggesstion","Complaint"};
	Spinner Sp_Category;
	ImageView imgAddPic;
	private static final int CAMERA_REQUEST = 1;
	private static final int PICK_FROM_GALLERY = 2;
	private String userChoosenTask;
	byte[] pic;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sugesstion_with_photo);
        ad=new AlertDialog.Builder(this).create();
        Sp_Category=(Spinner)findViewById(R.id.Sp_Category);//Added 02-06-2016
        txtTitle = (EditText) findViewById(R.id.edtitleSug);
        txtDesc=(EditText)findViewById(R.id.edDescSug);
        ivSubmitSug=(ImageView)findViewById(R.id.imageViewSubmitSUG);
        imgAddPic=(ImageView)findViewById(R.id.imgAddPic);
        
        webcall=new WebServiceCall();//Call a Webservice
		chkconn=new Chkconnection();
		
        menuIntent = getIntent(); 
		Log =  menuIntent.getStringExtra("Clt_Log");
		logid =  menuIntent.getStringExtra("Clt_LogID");
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		Str_user =  menuIntent.getStringExtra("UserClubName");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
        Table2Name="C_"+Str_user+"_2";
		Table4Name="C_"+Str_user+"_4";
		
		Set_App_Logo_Title(); // Set App Logo and Title

		Str_IEMI = new CommonClass().getIMEINumber(context);//Added On 14-02-2019
		
		/// Set Category Spinner
		ArrayAdapter<String> adp1= new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,ArrCategory);
		adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Sp_Category.setAdapter(adp1);
		
		imgAddPic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0) {
            	SelectImage();
            }
        });
		
		ivSubmitSug.setOnClickListener(new OnClickListener()
        { 
			@Override
			public void onClick(View arg0) {
				String Category= Sp_Category.getSelectedItem().toString().trim();
				StrTitle= txtTitle.getText().toString().trim();
				StrDesc= txtDesc.getText().toString().trim();
				if(Category.contains("Select")){
					DisplayAlert("Mandatory !","Please select category",0);
				}
				else if(StrTitle.length()==0){
					DisplayAlert("Mandatory !","Please input title",0);
				}else if(StrDesc.length()==0){
					DisplayAlert("Mandatory !","Please input description",0);
				}else
				{
					InternetPresent =chkconn.isConnectingToInternet(context);
					
					 if(InternetPresent==true){
						 CallWeb();
					 }else{
					     DisplayAlert("No Internet Connection !","Please connect with Internet.",0);
					 }
				}
			}
        });

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
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	 {
	   	 if (keyCode == KeyEvent.KEYCODE_BACK) {
	   		 back();
	   	 return true;
	   	 }
	   	return super.onKeyDown(keyCode, event);
	 }
	
	private void back(){
		 MainBtnIntent= new Intent(getBaseContext(),MenuPage.class);
		 MainBtnIntent.putExtra("AppLogo", AppLogo);
    	 startActivity(MainBtnIntent);
    	finish();
	}
	
	
	//Call A Webservice for Suggetion/complaint/feedback
	 private void CallWeb()
	 {
		 progressdial(); 
         networkThread = new Thread()
         {
          public void run()
          {
           try
           {
        	  WebResult=webcall.clubSugg(Str_user, Str_IEMI, logid, StrTitle, StrDesc, "");
              runOnUiThread(new Runnable()
              {
           	     public void run()
           	     {
           	    	if(WebResult.contains("Request Saved...")){
           	    		DisplayAlert("Result","Your Suggestion/Feedback sent successfully.",1);
               		}
           	    	else{
           	    		DisplayAlert("Result","Something went wrong !",0);
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
	
	 
	 
	//Select Image Using Camera or Gallery
	    private void SelectImage() {
	        final CharSequence[] items = { "Take from Camera", "Select from Gallery",
	                "Cancel" };

	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setTitle("Add Photo!");
	        builder.setItems(items, new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int item) {
	                boolean result=true;//Utility.checkPermission(this);

	                if (items[item].equals("Take from Camera")) {
	                    userChoosenTask ="Take from Camera";
	                    if(result)
	                        cameraIntent();

	                } else if (items[item].equals("Select from Gallery")) {
	                    userChoosenTask ="Select from Gallery";
	                    if(result)
	                        galleryIntent();

	                } else if (items[item].equals("Cancel")) {
	                    dialog.dismiss();
	                }
	            }
	        });
	        builder.show();
	    }


	    @SuppressLint("Override")
	    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
	        switch (requestCode) {
	            case 123:
	                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
	                    if(userChoosenTask.equals("Take from Camera"))
	                        cameraIntent();
	                    else if(userChoosenTask.equals("Select from Gallery"))
	                        galleryIntent();
	                } else {
	                    //code for deny
	                }
	                break;
	        }
	    }


	    //Gallery Intent
	    private void galleryIntent()
	    {
	        /*Intent intent = new Intent();
	        intent.setType("image/*");
	        intent.setAction(Intent.ACTION_GET_CONTENT);
	        //intent.putExtra("crop", "true");
	        //intent.putExtra("aspectX", 0);
	        //intent.putExtra("aspectY", 0);
	        //intent.putExtra("outputX", 200);
	        //intent.putExtra("outputY", 150);
	        intent.putExtra("return-data", true);
	        startActivityForResult(Intent.createChooser(intent, "Select File"),PICK_FROM_GALLERY);*/

	        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	        startActivityForResult(intent, PICK_FROM_GALLERY);
	    }

	    //Camera Intent
	    private void cameraIntent()
	    {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File file = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
			Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);//Added on 07-01-2018
			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);//Added on 07-01-2018
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//Added on 07-01-2018
			startActivityForResult(intent, CAMERA_REQUEST);
	    }


	    @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);

	        if (resultCode == Activity.RESULT_OK) {
	            if (requestCode == PICK_FROM_GALLERY)
	                SetImageByCamera_Gallery(data,1);
	            else if (requestCode == CAMERA_REQUEST)
	                SetImageByCamera_Gallery(data,2);
	        }
	    }
		
		
		//Set Image From Camera or Photo Gallery
		private void SetImageByCamera_Gallery(Intent intent,int Type)
		{
			 Bitmap yourImage = null;
		        if (Type == 1)//Gallery
		        {
		            ////Change 01-07-2017///

		            try {
		                final Uri imageUri = intent.getData();
		                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
		                yourImage = BitmapFactory.decodeStream(imageStream);

		                //if(intent.getExtras()!=null){
		                ///Comes from Gallery
		                //  yourImage = intent.getExtras().getParcelable("data");
		                //}else{
		                //comes from media or other folders or memmory card
		                //yourImage = MediaStore.Images.Media.getBitmap(this.getApplicationContext().getContentResolver(), intent.getData());
		                //}

		            } catch (OutOfMemoryError ex) {
		                ex.printStackTrace();
		                DisplayAlert("Pic Error", "Pic Error",0);
		            } catch (Exception e) {
		                e.printStackTrace();
		                DisplayAlert("Pic Error", "Pic Error",0);
		            }

		        } else {
		            //Take from Camera///
		            File f = new File(Environment.getExternalStorageDirectory().toString());
		            for (File temp : f.listFiles()) {
		                if (temp.getName().equals("temp.jpg")) {
		                    f = temp;
		                    break;
		                }
		            }
		            try {

		                BitmapFactory.Options options = new BitmapFactory.Options();
		                yourImage = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
		                f.delete();
		            } catch (Exception e) {
		            	DisplayAlert("Pic Error", "Pic Error",0);
		            }
		        }
					
					
				if(yourImage!=null)
				{
					int w = yourImage.getWidth();
		            int h = yourImage.getHeight();
		            //int p = yourImage.getByteCount();
		            //int d = yourImage.getDensity();

		            int RWidth=600,RHeight=600;
		            if(w<RWidth) {
		                RWidth=w;
		            }

		            if(h<RHeight){
		                RHeight=h;
		            }

		            //Resize the Original Image
		            Bitmap ResizeImg = ScaleDownBitmap(yourImage, RWidth, RHeight, true);
		            //int w1 = ResizeImg.getWidth();
		            //int h1 = ResizeImg.getHeight();
		            //int p1=ResizeImg.getByteCount();
		            //int d1=ResizeImg.getDensity();

		            // convert bitmap to byte
		            ByteArrayOutputStream stream = new ByteArrayOutputStream();
		            ResizeImg.compress(Bitmap.CompressFormat.PNG, 90, stream);		
					
				  int k=stream.size();
				  int p2=ResizeImg.getByteCount();
				  pic = stream.toByteArray();
				  imgAddPic.setImageBitmap(ResizeImg);
			    }
				else{
					DisplayAlert("Pic Error","Pic Error",0);
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
	 
	 
	 private void DisplayAlert(String Title,String Msg,final int i)
	 {
		 ad.setTitle(Html.fromHtml("<font color='#E3256B'>"+Title+"</font>"));
	     ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>"+Msg+"</font>"));
		 ad.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	if(i==1)
            	   back();
            	else
            	   dialog.dismiss();
            }
	     });
	     ad.show();	
	 }
}