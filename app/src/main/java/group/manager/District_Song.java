package group.manager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class District_Song extends Activity {

    ListView Lv1;
    TextView txtHead;
    ImageView ImgDownload,ImgPlay;
    String ClientId,ClubName,MTitle,WebResult="",FPathUrl="",FName="",Table4Name;
    List<RowEnvt> rowItems;
    RowEnvt item;
    byte[] AppLogo;
    boolean DispPlay=false;
   
    private boolean InternetPresent;
    Context context = this;
    
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.district_song);
        
        txtHead = (TextView) findViewById(R.id.txtHead) ;
        ImgDownload = (ImageView) findViewById(R.id.imgDownload) ;

        Intent intent = getIntent();
        MTitle=intent.getStringExtra("MTitle");
		ClubName =  intent.getStringExtra("Clt_ClubName");
		ClientId =  intent.getStringExtra("UserClubName");
		AppLogo =  intent.getByteArrayExtra("AppLogo");
        
		Set_App_Logo_Title(); // Set App Logo and Title
		
		txtHead.setText(MTitle);  //set heading
		
		Table4Name="C_"+ClientId+"_4";
		
		SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
		String Qry="SELECT Text1,Text2 FROM "+Table4Name +" where Rtype='SONG'";
		Cursor cursorT = db.rawQuery(Qry, null);
		
		if (cursorT.moveToFirst()) {
			FPathUrl=ChkVal(cursorT.getString(0));
			FName=ChkVal(cursorT.getString(1));
	    }
	    cursorT.close();
	    db.close();
	    
	    
	    ImgDownload.setVisibility(View.GONE);
	    if(FPathUrl.length()>1 && FName.length()>1)
	    {
	    	ImgDownload.setVisibility(View.VISIBLE);
	    	File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+ FName);
	    	if(file.exists())
	    	{
	    		DispPlay=true;
	    		ImgDownload.setImageResource(R.drawable.play);
	    	}
	    }
        
		ImgDownload.setOnClickListener(new View.OnClickListener() {
			 
	            @Override
	            public void onClick(View v) {
	               
	            	if(!DispPlay)
	            	{
	                  Chkconnection chkconn=new Chkconnection();//Intialise Chkconnection Object
	        	      InternetPresent =chkconn.isConnectingToInternet(context);
	        		  if(InternetPresent==true)
	        		  {
	        			 // starting new Async Task
		                new DownloadFileFromURL().execute();
	        	      }
	        		  else{
	        			AlertDisplay("Internet Connection","No Internet Connection !");
	        		  }
	            	}
	            	else
	            	{
	            		// Reading Audio file path from sdcard
	          	        String AudioFPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+ FName;
	                	
	                	 Intent intent = new Intent();
	                     intent.setAction(android.content.Intent.ACTION_VIEW);
	                     File file = new File(AudioFPath);
						 Uri FileURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);//Added
						 intent.setDataAndType(FileURI, "audio/*");
						 intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//Added on 15-02-2019
	                     startActivity(intent);
	            	}
	            }
	        });
    }
    
    
  	
  	/*** Showing Progress Dialog* */
  	@Override
  	protected Dialog onCreateDialog(int id) {
  	    switch (id) {
  	    case progress_bar_type:
  	        pDialog = new ProgressDialog(this);
  	        pDialog.setMessage("Downloading file. Please wait...");
  	        pDialog.setIndeterminate(false);
  	        pDialog.setMax(100);
  	        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
  	        pDialog.setCancelable(true);
  	        pDialog.show();
  	        return pDialog;
  	    default:
  	        return null;
  	    }
  	}

  	
  	
  	/*** Background Async Task to download file* */
  	class DownloadFileFromURL extends AsyncTask<String, String, String> {
  	 
  	    /**
  	     * Before starting background thread
  	     * Show Progress Bar Dialog
  	     * */
  	    @Override
  	    protected void onPreExecute() {
  	        super.onPreExecute();
  	        showDialog(progress_bar_type);
  	    }
  	 
  	    /**
  	     * Downloading file in background thread
  	     * */
  	    @Override
  	    protected String doInBackground(String... f_url) {
  	        int count;
  	        try {
  	            URL url = new URL(FPathUrl);
  	            URLConnection conection = url.openConnection();
  	            conection.connect();
  	            // getting file length
  	            int lenghtOfFile = conection.getContentLength();
  	 
  	            // input stream to read file - with 8k buffer
  	            InputStream input = new BufferedInputStream(url.openStream(), 8192);
  	 
  	            
  	            File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+ FName);
  	            
  	            // Output stream to write file
  	            OutputStream output = new FileOutputStream(myFile);
  	 
  	            byte data[] = new byte[1024];
  	 
  	            long total = 0;
  	 
  	            while ((count = input.read(data)) != -1) {
  	                total += count;
  	                // publishing the progress....
  	                // After this onProgressUpdate will be called
  	                publishProgress(""+(int)((total*100)/lenghtOfFile));
  	 
  	                // writing data to file
  	                output.write(data, 0, count);
  	            }
  	 
  	            // flushing output
  	            output.flush();
  	 
  	            // closing streams
  	            output.close();
  	            input.close();
  	 
  	        } catch (Exception e) {
  	            Log.e("Error: ", e.getMessage());
  	        }
  	 
  	        return null;
  	    }
  	 
  	    /**
  	     * Updating progress bar
  	     * */
  	    protected void onProgressUpdate(String... progress) {
  	        // setting progress percentage
  	        pDialog.setProgress(Integer.parseInt(progress[0]));
  	   }
  	 
  	    /**
  	     * After completing background task
  	     * Dismiss the progress dialog
  	     * **/
  	    @Override
  	    protected void onPostExecute(String file_url) {
  	        // dismiss the dialog after the file was downloaded
  	        dismissDialog(progress_bar_type);
  	 
  	        DispPlay=true;
  	        ImgDownload.setImageResource(R.drawable.play);
  	        
  	    }
  	 
  	}
  	
    
  	
  	private void AlertDisplay(String head,String body){
		 AlertDialog ad=new AlertDialog.Builder(this).create();
	    	ad.setTitle( Html.fromHtml("<font color='#E3256B'>"+head+"</font>"));
	    	ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>"+body+"</font>"));
			ad.setButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	  dialog.dismiss();
	            	  backs();
	            }
	        });
	        ad.show();	
	}
    
  	
  //call function for initialise blank if null is there
  	private String ChkVal(String DVal)
  	{
  		if((DVal==null)||(DVal.equalsIgnoreCase("null"))){
  			DVal="";
  		}
  		return DVal;
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
	   		backs();
	   	    return true;
	   	}
	   	return super.onKeyDown(keyCode, event);
	 }
   
   
   public void backs()
   {
	   Intent intent= new Intent(getBaseContext(),MenuPage.class);
	   intent.putExtra("AppLogo", AppLogo);
	   startActivity(intent);
	   finish();
   }
   
   
   
   
   
}
