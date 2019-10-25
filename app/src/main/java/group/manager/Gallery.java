package group.manager;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.webkit.DownloadListener;
import android.webkit.MimeTypeMap;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class Gallery extends Activity {

	String ClientId,ClubName,NEType="";
	private Context context=this;
	byte[] AppLogo;
	ProgressDialog Progsdial;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery);
		
		WebView webview=(WebView)findViewById(R.id.webView1); 
		
		Intent menuIntent = getIntent(); 
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		ClientId =  menuIntent.getStringExtra("UserClubName");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		NEType=menuIntent.getStringExtra("NEType");
		//setContentView(webview);
		
		WebSettings settings = webview.getSettings();  
        settings.setJavaScriptEnabled(true);  
        webview.setInitialScale(1);  
        webview.getSettings().setUseWideViewPort(true);  
        settings.setJavaScriptCanOpenWindowsAutomatically(false);   
        settings.setDomStorageEnabled(true);  
		
		
		Set_App_Logo_Title(); // Set App Logo and Title
		
		Chkconnection chkconn=new Chkconnection();
		boolean InternetPresent = chkconn.isConnectingToInternet(context); // Chk Internet
		
		if(InternetPresent==true){
			String Rtype="";
			
			if(NEType.equals("Event"))
				Rtype="Event_Img";
			else if(NEType.equals("Event"))
				Rtype="News_Img";
			
			  //progressdial();//Start Progress Dialog
			  webview.loadUrl("http://www.mybackup.co.in/EventNewsGallary.aspx?CClub="+ClientId+"&Rtype="+Rtype); 
			  //webview.loadUrl("http://www.mybackup.co.in/livevideo.aspx"); 
		}else{
			DispAlert("Connection Problem !","No Internet Connection ",true);
		}
		
		
		webview.setWebViewClient(new WebViewClient() {
		    public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
		        viewx.loadUrl(urlx);
		        return false;
		    }
		    
		    @Override
		    public void onPageFinished(WebView view, String url) {
		        super.onPageFinished(view, url);
                
		        //if(Progsdial.isShowing())
		          //Progsdial.dismiss();//Progress dialog dismiss
		        // do something
		    }
		    
		});
		
		
		webview.setDownloadListener(new DownloadListener() {

		    public void onDownloadStart(String url, String userAgent,
		        String contentDisposition, String mimetype,
		                                   long contentLength) {

		            Request request = new Request(Uri.parse(url));
		            request.allowScanningByMediaScanner();

		                request.setNotificationVisibility(
		                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

		                String DownfileName=url.replace("http://mybackup.co.in/Club_Images", "");
		                
		                request.setDestinationInExternalPublicDir(
		                Environment.DIRECTORY_DOWNLOADS,    //Download folder
		                DownfileName);                        //Name of file

		                DownloadManager dm = (DownloadManager) getSystemService(
		                DOWNLOAD_SERVICE);

		                dm.enqueue(request);  

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
	
	
	
	@SuppressWarnings("deprecation")
	 private void DispAlert(String Title,String Msg,final boolean chk)
	 {
        AlertDialog ad=new AlertDialog.Builder(this).create();
	    ad.setTitle( Html.fromHtml("<font color='#1C1CF0'>"+Title+"</font>"));
   	    ad.setMessage(Msg);
   	    ad.setCancelable(false);
   	    ad.setButton("OK", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {
           	if(chk)
           	  Goback();
           	else
           	  dialog.dismiss();
           }
         });
        ad.show();
	 }
	
	
	protected void progressdial()
    {
    	Progsdial = new ProgressDialog(this, R.style.MyTheme);
    	//Progsdial.setTitle("App Loading");
    	Progsdial.setMessage("Please Wait....");
    	Progsdial.setIndeterminate(true);
    	Progsdial.setCancelable(false);
    	Progsdial.getWindow().setGravity(Gravity.DISPLAY_CLIP_VERTICAL);
    	Progsdial.show();
    } 
	
	
	 public boolean onKeyDown(int keyCode, KeyEvent event) 
	 {
	   	 if (keyCode == KeyEvent.KEYCODE_BACK) {
	   		Goback();
	   	    return true;
	   	 }
	   	 return super.onKeyDown(keyCode, event);
	 }
		
	 //Back 
	 private void Goback()
	 {
		Intent MainBtnIntent= new Intent(context,MenuPage.class);
		MainBtnIntent.putExtra("AppLogo", AppLogo);
	    startActivity(MainBtnIntent);
	    finish();
	 }
}
