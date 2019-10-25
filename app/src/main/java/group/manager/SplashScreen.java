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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashScreen extends Activity{
	TextView tvmessage,Txt1,Txtvalue,tvWel,tvto,tvmob,tvof,tv1,tv2,tv3,tv4;
	ImageView iv;
	String User,ClientID="",clubmname="",Tab4Name,StrQ="",txt1,txt2,txt3,txt4,txt5,txt6,TimeOut;
	Intent menuIntent;
	byte[] AppLogo;
	Animation animFadein;
	Context context=this;
	SQLiteDatabase db;
	Cursor cursorT;
	Thread background ;
	CountDownTimer cdt=null;
	RelativeLayout rrlay;
	int timeoutval;
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.splashscreen);
	        rrlay=(RelativeLayout) findViewById(R.id.andymainsplash);
	        menuIntent = getIntent(); 
	        AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
	        Get_SharedPref_Values();
	        Tab4Name="C_"+ClientID+"_4";
	        if((TimeOut==null)||(TimeOut.length()==0)||(TimeOut.equals("0"))){
	        	timeoutval=15000;
	        }else{
	        	int val=Integer.parseInt(TimeOut);
	        	timeoutval=val*1000;
	        }
	        db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
			StrQ="select Text1,Text2,Text3,Text4,Text5,Text6 from "+Tab4Name+" where Rtype='SPLASH'";
			cursorT = db.rawQuery(StrQ, null);
			int count=cursorT.getCount();/////////////////already create in login page but still mention here bcoz of no change it...
			if(count==0){
				cursorT.close();
				db.close();
				DoTaskClickEvent();
			}else{
			 if(cursorT.moveToNext()){
				txt1=checknullcond(cursorT.getString(0));
				txt2=checknullcond(cursorT.getString(1));
				txt3=checknullcond(cursorT.getString(2));
				txt4=checknullcond(cursorT.getString(3));
				txt5=checknullcond(cursorT.getString(4));
				txt6=checknullcond(cursorT.getString(5));
			 }
			cursorT.close();
			db.close();
			if(txt1.equalsIgnoreCase("YES")){
				tvmessage= (TextView) findViewById(R.id.textViewSplashmsg);
		        Txt1= (TextView) findViewById(R.id.textViewSplashAdmin);
		        Txtvalue= (TextView) findViewById(R.id.textViewSplashvalue);
		        tvWel= (TextView) findViewById(R.id.textView1wel);
		        tvto= (TextView) findViewById(R.id.textView2to);
		        tvmob= (TextView) findViewById(R.id.textView3mob);
		        tvof= (TextView) findViewById(R.id.textView4of);
		        iv= (ImageView) findViewById(R.id.imageViewsplash);
		        tv1= (TextView) findViewById(R.id.textViewj1);
		        tv2= (TextView) findViewById(R.id.textViewj2);
		        tv3= (TextView) findViewById(R.id.textViewj3);
		        tv4= (TextView) findViewById(R.id.textViewj4);
		        tvWel.setText("WELCOME");
		        tvto.setText("to the");
		        tvmob.setText("Mobile App");
		        tvof.setText("of");
		        tv1.setText(txt3);
		        tv2.setText(txt4);
		        tv3.setText(txt5);
		        tv4.setText(txt6);
				tvmessage.setText(clubmname.toUpperCase());
				Txt1.setText("App Admin:");
				Txtvalue.setText(txt2);
				if(AppLogo==null){
					iv.setVisibility(View.GONE);
				}else{
				  Bitmap bitmap = BitmapFactory.decodeByteArray(AppLogo , 0, AppLogo.length);
				  iv.setVisibility(View.VISIBLE);
				  iv.setImageBitmap(bitmap);
				}
				
				/*cdt=new CountDownTimer(15000, 1000) {

				     public void onTick(long millisUntilFinished) {
				    	//Txt1.setText("seconds remaining: " + millisUntilFinished / 1000);
				     }
				     public void onFinish() {
				    	 DoTaskClickEvent();
				     }
				  }.start();*/
				
				 
	              
	              rrlay.setOnClickListener(new OnClickListener()
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
			        			   runOnUiThread (new Runnable(){ 
			        			    public void run(){
			        			      DoTaskClickEvent();// Show the Result of Authorisation Check
			        			    }
			        			  });
			                } catch (InterruptedException e) {
			                   // e.getMessage();   
			                } catch (Exception e) {
			                    e.getMessage();
			                }
			              //  Progsdial.dismiss();
			              }
			          };
			          background.start();
			  }else{
				  DoTaskClickEvent(); 
			  }
			}
	 }
	 
	 private String checknullcond(String tv){
		 if((tv==null)||(tv.length()==0)||(tv.equalsIgnoreCase("null"))){
			 tv="";
		 }
		return tv;
	 }
	 
	 private void DoTaskClickEvent()
	    {
		 menuIntent= new Intent(context,MenuPage.class);
		 menuIntent.putExtra("AppLogo", AppLogo);
	     startActivity(menuIntent);
	     finish();
	    	
	    }
	 
	 private void Get_SharedPref_Values()
		{
		   SharedPreferences sharedpreferences  = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
			  if (sharedpreferences.contains("clientid"))
		      {
				  ClientID=sharedpreferences.getString("clientid", "");
		      }
		      if (sharedpreferences.contains("clubname"))
		      {
		    	  clubmname=sharedpreferences.getString("clubname", "");
	          } 
		      if (sharedpreferences.contains("TimeOut"))
		      {
		    	  TimeOut=sharedpreferences.getString("TimeOut", "");
	          } 
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
