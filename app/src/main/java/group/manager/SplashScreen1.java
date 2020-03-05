package group.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashScreen1 extends Activity{
	Intent menuIntent;
	byte[] AppLogo,BGPic;
	Context context=this;
	Thread background ;
	ImageView IVSP;
	String TimeOut;
	int timeoutval;
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.splashscreen1);
	        IVSP=(ImageView) findViewById(R.id.imageViewSp1);
	        Get_SharedPref_Values();
	        menuIntent = getIntent(); 
	        AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
	        BGPic =  menuIntent.getByteArrayExtra("BgPic");
	        if(BGPic==null){
	        	IVSP.setImageResource(R.drawable.blue);
			}else{
			   Bitmap bitmap = BitmapFactory.decodeByteArray(BGPic , 0, BGPic.length);
			   IVSP.setImageBitmap(bitmap);
			}
	        if((TimeOut==null)||(TimeOut.length()==0)||(TimeOut.equals("0"))){
	        	timeoutval=15000;
	        }else{
	        	int val=Integer.parseInt(TimeOut);
	        	timeoutval=val*1000;
	        }
	         
	        IVSP.setOnClickListener(new OnClickListener()
	        { 
	        	@Override
	        	public void onClick(View arg0) {
	        		//cdt.cancel();
	        		if(background.isAlive())
	        		{
	        			background.interrupt();
	        			DoTaskClickEvent();
	        		}
  			  	}
  			});
				
			background = new Thread() {
			public void run() {
				try {
					sleep(timeoutval);
					runOnUiThread(new Runnable() {
						public void run() {
							DoTaskClickEvent();// Show the Result of
						}
					});
				} catch (InterruptedException e) {
					// e.getMessage();
				} catch (Exception e) {
					e.getMessage();
				}
				// Progsdial.dismiss();
			}
		};
		background.start();
			  

	 }
	 
	 private void Get_SharedPref_Values()
		{
		   SharedPreferences sharedpreferences  = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
			 if (sharedpreferences.contains("TimeOut"))
		      {
		    	  TimeOut=sharedpreferences.getString("TimeOut", "");
	          } 
		}
	 
	 private void DoTaskClickEvent()
	 {
		 menuIntent= new Intent(context,MenuPage.class);
		 menuIntent.putExtra("AppLogo", AppLogo);
	     startActivity(menuIntent);
	     finish();
	 }
	 
	
	 public boolean onKeyDown(int keyCode, KeyEvent event) 
	 {
	   	 if (keyCode == KeyEvent.KEYCODE_BACK) {
	   		// finish();
	   	    return true;
	   	 }
	   	return super.onKeyDown(keyCode, event);
	 }

}
