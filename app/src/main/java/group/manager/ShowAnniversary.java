package group.manager;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowAnniversary extends Activity {
	Intent menuIntent;
	Dialog dialog;
	String smsbody="",StrmobNo="",smsbody2="",smsbody3="",yyyy;
	Context context=this;
	String ClientID="",LogclubName="";
	byte[] AppLogo,imgP;
	String checkcomingfrom="0";
	Button BtnSendmessage,BtnChangeDate;
	ImageView IvPrevdate,IvNextdate;
	EditText etDay,etMonth,EtremarkDialog,EtremarkDialog2,EtremarkDialog3;
	ListView listview1;
	RelativeLayout rrlay;
	TextView txtnodisplay;
	CheckBox cHkboxall,Chkboxremrk,Chkboxremrk2,Chkboxremrk3;
	String sqlSearch,Tab2Name,Tab4Name,StrTotal;
	static Calendar c;
	String dd="00",mm="00";
	String strETDay,strETMonth;
	static String resultdate;
	ArrayList<Product>products;
	ListAdapter2 boxAdapter;
	SQLiteDatabase db;
	int countdata=0,chk=0;
	SharedPreferences sharedpreferences,shrd;
	Editor editr,edits;
	String STR_Remark="",STR_Remark2="",STR_Remark3="";
	ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listalertdialog);
		IvPrevdate = (ImageView)findViewById(R.id.imageprevdate);
		IvNextdate = (ImageView)findViewById(R.id.imageNextdate);
	    BtnSendmessage=(Button)findViewById(R.id.buttonListalrt);
	 	BtnChangeDate=(Button)findViewById(R.id.btnDateSubmit);
	 	etDay = (EditText)findViewById(R.id.editTextDay);
	 	etMonth= (EditText)findViewById(R.id.editTextMonth);
	 	listview1 = (ListView)findViewById(R.id.listViewAlert);
	 
	    rrlay=(RelativeLayout)findViewById(R.id.rrDate);
	    txtnodisplay= (TextView)findViewById(R.id.textViewNoDisplay);
	    
	    Set_App_Logo_Title(); // Set App Logo and Title
	    Tab2Name="C_"+ClientID+"_2";
		
	    c = Calendar.getInstance();// create object for calender
	    dd = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        if((dd==null)||(dd.length()==0)){
        	dd="00";
        }
        mm=String.valueOf(c.get(Calendar.MONTH)+1);
        if((mm==null)||(mm.length()==0)){
        	mm="00";
        }
        
        yyyy=String.valueOf(c.get(Calendar.YEAR));
        System.out.println(yyyy);
        
        etDay.setText(dd);
        etMonth.setText(mm);
        BtnSendmessage.setText("Send Sms");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ActionCall();
        
        connectdbandgetdata(false,dd,mm);
        
        IvPrevdate.setOnClickListener(new OnClickListener(){ 
 	        public void onClick(View arg0){ 
 	        	strETDay= etDay.getText().toString().trim();
 	        	strETMonth= etMonth.getText().toString().trim();
 	        	if(strETDay.length()==0){
 	        		listview1.setVisibility(View.GONE);
 	        		etDay.setError("Please select day");
 	        	}else if(strETMonth.length()==0){
 	        		listview1.setVisibility(View.GONE);
 	        		etMonth.setError("Please select Month");
 	            }else{
 	        	  etDay.setText(strETDay);
 	              etMonth.setText(strETMonth);
 	             /* if(strETDay.length()==1){
 	            	 strETDay="0"+strETDay;  
 	              }if(strETMonth.length()==1){
 	            	 strETMonth="0"+strETMonth;
 	              }*/
 	              String storeddate=strETDay+"-"+strETMonth+"-"+yyyy;
 	              showdateandcalllist(previousDateString(storeddate));
 	        	 }
 	         } 
 	   });
 	  
       IvNextdate.setOnClickListener(new OnClickListener(){ 
 	        public void onClick(View arg0){ 
 	        	strETDay= etDay.getText().toString().trim();
 	        	strETMonth= etMonth.getText().toString().trim();
 	        	if(strETDay.length()==0){
 	        		listview1.setVisibility(View.GONE);
 	        		etDay.setError("Please select day");
 	        	}else if(strETMonth.length()==0){
 	        		listview1.setVisibility(View.GONE);
 	        		etMonth.setError("Please select Month");
 	            }else{
 	        	  etDay.setText(strETDay);
 	              etMonth.setText(strETMonth);
 	              if((strETDay.equals("29"))&&(strETMonth.equals("2"))&&(!yyyy.equals("2016")||(!yyyy.equals("2020")||!yyyy.equals("2024")))){
 	            	  strETDay="28";
 	            	  etDay.setText(strETDay);
 	            	  Toast.makeText(context, "not leap year", 1).show();
 	              }
 	              String storeddate=strETDay+"-"+strETMonth+"-"+yyyy;
 	              showdateandcalllist(NextDateString(storeddate));
 	        	 }
 	        } 
 	  });
       
       BtnChangeDate.setOnClickListener(new OnClickListener(){ 
           public void onClick(View arg0){
        	  cHkboxall.setChecked(false);
        	  chk=1;
        	  strETDay= etDay.getText().toString().trim();
        	  strETMonth= etMonth.getText().toString().trim();
        	  if(strETDay.length()==0){
        		  listview1.setVisibility(View.GONE);
        		  etDay.setError("Please select day");
        	  }else if(strETMonth.length()==0){
        		  listview1.setVisibility(View.GONE);
        		  etMonth.setError("Please select Month");
        	  }else{
        		   etDay.setText(strETDay);
        		   etMonth.setText(strETMonth);
        		   connectdbandgetdata(false,strETDay,strETMonth);
        	  }
           }
 	   });
       
       BtnSendmessage.setOnClickListener(new OnClickListener(){ 
           public void onClick(View arg0){
		          StrmobNo="";
			      for (Product p : boxAdapter.getBox()) {
			       if (p.box){
			    	   //p.mob = p.mob.substring(0, p.mob.length()-1).trim();
			    	   if(p.mob.length()!=0){
			    		   p.mob = p.mob.substring(p.mob.length()-10);
				    	   p.mob= "0"+p.mob;  
				           StrmobNo +=p.mob+","; 
			    	   }
			        }
			      }
			      if(StrmobNo.length()!=0){
			    	  StrmobNo = StrmobNo.substring(0, StrmobNo.length()-1).trim();
			    	  showremark();
			      }else{
			    	  Toast.makeText(getApplicationContext(), "No check box selected", 1).show();
			      }
           }
      });  
    
	}
	
	private void showremark() {
	     dialog = new Dialog(context);
	     dialog.setContentView(R.layout.listalertdialog);
         dialog.setTitle("Type Message here..."); 
	     getsharedprefrencevalue();
	     ScrollView scremark=(ScrollView)dialog.findViewById(R.id.scrollViewRemark);
	     scremark.setVisibility(View.VISIBLE);
	     EtremarkDialog = (EditText)dialog.findViewById(R.id.editTextEmark1);
		 EtremarkDialog.setGravity(Gravity.LEFT | Gravity.TOP);  
		 EtremarkDialog2 = (EditText)dialog.findViewById(R.id.editTextEmark2);
		 EtremarkDialog2.setGravity(Gravity.LEFT | Gravity.TOP);  
		 EtremarkDialog3 = (EditText)dialog.findViewById(R.id.editTextEmark3);
		 EtremarkDialog3.setGravity(Gravity.LEFT | Gravity.TOP);  
		 Chkboxremrk=(CheckBox)dialog.findViewById(R.id.checkBoxRemark1);
		 Chkboxremrk2=(CheckBox)dialog.findViewById(R.id.checkBoxRemark2);
		 Chkboxremrk3=(CheckBox)dialog.findViewById(R.id.checkBoxRemark3);
		 Button BTNlistBack = (Button)dialog.findViewById(R.id.buttonListalrt);
		 Button BtnSaveDialog = (Button)dialog.findViewById(R.id.buttonSave);
		 BtnSaveDialog.setVisibility(View.VISIBLE);
		 BTNlistBack.setText("Cancel");
		 BtnSaveDialog.setText("Send Sms");
		 EtremarkDialog.setText(smsbody); 
		 EtremarkDialog2.setText(smsbody2); 
		 EtremarkDialog3.setText(smsbody3); 
		 dialog.show();
		 
		 Chkboxremrk.setOnClickListener(new OnClickListener(){ 
		       public void onClick(View arg0){ 
		    	   STR_Remark= EtremarkDialog.getText().toString().trim();
		    	   if((Chkboxremrk.isChecked()==true)&&(STR_Remark.length()==0)){
		    		   EtremarkDialog.setError("Select first remark.");
		    		   Chkboxremrk2.setChecked(false);
		    		   Chkboxremrk3.setChecked(false);
		    		   Chkboxremrk.setChecked(false);
		    		  
		    	   }else if((Chkboxremrk.isChecked()==true)&&(STR_Remark.length()!=0)){
		    		   Chkboxremrk2.setChecked(false);
		    		   Chkboxremrk3.setChecked(false);
		    	   }
			     }
			  });  
		 
		 Chkboxremrk2.setOnClickListener(new OnClickListener(){ 
		       public void onClick(View arg0){ 
		    	   STR_Remark2= EtremarkDialog2.getText().toString().trim();
		    	   if((Chkboxremrk2.isChecked()==true)&&(STR_Remark2.length()==0)){
		    		   EtremarkDialog2.setError("Select second remark.");
		    		   Chkboxremrk.setChecked(false);
		    		   Chkboxremrk3.setChecked(false);
		    		   Chkboxremrk2.setChecked(false);
			    	  
			        }else if((Chkboxremrk2.isChecked()==true)&&(STR_Remark2.length()!=0)){
			    		Chkboxremrk.setChecked(false);
			    		Chkboxremrk3.setChecked(false);
			    	}
			     }
			  });  
		 
		 Chkboxremrk3.setOnClickListener(new OnClickListener(){ 
		       public void onClick(View arg0){ 
		    	   STR_Remark3= EtremarkDialog3.getText().toString().trim();
		    	   if((Chkboxremrk3.isChecked()==true)&&(STR_Remark3.length()==0)){
		    		   EtremarkDialog3.setError("Select second remark.");
		    		   Chkboxremrk.setChecked(false);
		    		   Chkboxremrk2.setChecked(false);
		    		   Chkboxremrk3.setChecked(false);
			    	   
			        }else if((Chkboxremrk3.isChecked()==true)&&(STR_Remark3.length()!=0)){
			    		Chkboxremrk.setChecked(false);
			    		Chkboxremrk2.setChecked(false);
			    	}
			     }
			  });  
		 
		    BTNlistBack.setOnClickListener(new OnClickListener(){ 
		       public void onClick(View arg0){ 
		          dialog.dismiss();
		         }
		     });  
		    
		    BtnSaveDialog.setOnClickListener(new OnClickListener(){ 
			       public void onClick(View arg0){
			    	   STR_Remark= EtremarkDialog.getText().toString().trim();
			    	   STR_Remark2= EtremarkDialog2.getText().toString().trim();
			    	   STR_Remark3= EtremarkDialog3.getText().toString().trim();
			    	   if(StrmobNo.length()==0){
				    	      Toast.makeText(getApplicationContext(), "Select atleast one number!", 1).show();
				       }else if(Chkboxremrk.isChecked()==true){
			    		   callOnSms(StrmobNo,STR_Remark);
			    		   editorValue(STR_Remark,STR_Remark2,STR_Remark3);
			    		   dialog.cancel();
			    	   }else if(Chkboxremrk2.isChecked()==true){
			    		   callOnSms(StrmobNo,STR_Remark2);
			    		   editorValue(STR_Remark,STR_Remark2,STR_Remark3);
			    		   dialog.cancel();
			    	   }else if(Chkboxremrk3.isChecked()==true){
			    		   callOnSms(StrmobNo,STR_Remark3);
			    		   editorValue(STR_Remark,STR_Remark2,STR_Remark3);
			    		   dialog.cancel();
			    	   }else{
			    		   editorValue(STR_Remark,STR_Remark2,STR_Remark3);
			    		  Toast.makeText(getApplicationContext(), "No message is select..", 1).show();
			    	   }
			      }
			 });  
	 }
	
	 private void getsharedprefrencevalue() {
		  shrd = getSharedPreferences("SmsPref", Context.MODE_PRIVATE);
	      if (shrd.contains("SmsSd"))
	      {
	    	  smsbody= shrd.getString("SmsSd", "");
	      }  
	      if (shrd.contains("SmsSd2"))
	      {
	    	  smsbody2= shrd.getString("SmsSd2", "");
	      } 
	      if (shrd.contains("SmsSd3"))
	      {
	    	  smsbody3= shrd.getString("SmsSd3", "");
	      } 
	      System.out.println("@"+smsbody);
	 }
	
	private void editorValue(String smsbody,String smsbody2,String smsbody3) {
		 edits = shrd.edit();
		 edits.putString("SmsSd", smsbody);
		 edits.putString("SmsSd2", smsbody2);
		 edits.putString("SmsSd3", smsbody3);
		 edits.commit();
	 }
	 
	 public boolean onKeyDown(int keyCode, KeyEvent event) 
	 {
	   	 if (keyCode == KeyEvent.KEYCODE_BACK) {
	   		 if(checkcomingfrom.equalsIgnoreCase("menu")){
	   			Intent	MainBtnIntent= new Intent(getBaseContext(),MenuPage.class);
	   			MainBtnIntent.putExtra("AppLogo", AppLogo);
			    startActivity(MainBtnIntent);
			    finish();
	   		 }else{
	   			finish();
	   		 }
	   	    return true;
	   	 }
	   	return super.onKeyDown(keyCode, event);
	 }
	 
	
	public static String previousDateString(String dateString) throws ParseException {
        // Create a date formatter using your format string
		SimpleDateFormat dateFormat  = new SimpleDateFormat("d-M-yy");
        // Parse the given date string into a Date object.
        // Note: This can throw a ParseException.
		java.util.Date myDate = null;
		try {
		  myDate = dateFormat.parse(dateString);
		  c.setTime(myDate);
		  c.add(Calendar.DAY_OF_YEAR, -1);
		  // Use the date formatter to produce a formatted date string
		  java.util.Date previousDate =(java.util.Date)c.getTime();
		  resultdate = dateFormat.format(previousDate);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return resultdate;
    }
	
	public static String NextDateString(String dateString) throws ParseException {
        // Create a date formatter using your format string
		SimpleDateFormat dateFormat  = new SimpleDateFormat("d-M-yy");
        // Parse the given date string into a Date object.
        // Note: This can throw a ParseException.
		java.util.Date myDate = null;
		try {
		  myDate = dateFormat.parse(dateString);
		  c.setTime(myDate);
		  c.add(Calendar.DAY_OF_YEAR, +1);
		  // Use the date formatter to produce a formatted date string
		  java.util.Date previousDate =(java.util.Date)c.getTime();
		  resultdate = dateFormat.format(previousDate);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return resultdate;
    }
	
	private void showdateandcalllist(String Strdates)
	 {
	  Strdates=Strdates.replace("-", "#")+" ";
      String[] temp=Strdates.split("#");
      dd=temp[0].toString().trim();
      mm=temp[1].toString().trim();
      etDay.setText(dd);
      etMonth.setText(mm);
      connectdbandgetdata(false,dd,mm);
	 }
	
	
	 private void Set_App_Logo_Title()
	 {
		 menuIntent = getIntent(); 
		 ClientID=menuIntent.getStringExtra("ClientID");
		 LogclubName=menuIntent.getStringExtra("LogclubName");
		 checkcomingfrom=menuIntent.getStringExtra("Menu_Noti");//come from notification or menu page
		 //setTitle(LogclubName); // Set Title//
		 //setTitle("Today's Anniversary of "+LogclubName+" Group");
		 
		 if(checkcomingfrom.equalsIgnoreCase("menu")){
			 AppLogo =  menuIntent.getByteArrayExtra("AppLogo"); 
		 }
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


	 /// Updated On 14-03-2019
	public void callOnSms(String MobNos,String messageBody ) {
		new CommonClass().SEND_SMS_WithBody(context,MobNos,messageBody);
	}
	 
	 private void connectdbandgetdata(boolean tick,String days,String months){
		 try{
		 String Dobname=null,Dobnamob=null,StrCity=null,ddays="00",mmonths="00",MemM_id=null,PrefName="";
		 if((!days.equals(dd))&&(!months.equals(mm))){
			 actionBar.setTitle(days+"-"+months+"'s Anniversary of "+LogclubName+" Group");
		 }
		   if(days.length()==1){
			   ddays="0"+days;
		   }else{
			   ddays=days;
		   }
		   if(months.length()==1){
			   mmonths="0"+months;
		   }else{
			   mmonths=months;
		   }
		   products =  new ArrayList<Product>();
		   db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
			
			//Get Member Data
			String qry="Select M_Name,M_Mob,M_City,M_Pic,M_id,(C4_BG || \"\" || ifnull(C4_DOB_Y,'')) from "+Tab2Name+" Where (M_MrgAnn_D='"+days+"' or M_MrgAnn_D='"+ddays+"') and (M_MrgAnn_M='"+months+"' or M_MrgAnn_M='"+mmonths+"') and M_Mob IS NOT NULL and Length(M_Mob)<>0 order by M_City,M_Name";
		    Cursor cursorT = db.rawQuery(qry, null);
		    Bitmap theImage = null;
			//System.out.println(qry);
			countdata=cursorT.getCount();
			if(countdata>0){
				if (cursorT.moveToFirst()) {
				   do {
						Dobname =cursorT.getString(0);
						Dobnamob =cursorT.getString(1);
						StrCity =cursorT.getString(2);
						imgP=cursorT.getBlob(3);
						MemM_id=cursorT.getString(4);
						PrefName=cursorT.getString(5);
						if(PrefName==null)
						   PrefName="";
						
						if(PrefName.trim().length()>0)
							Dobname=PrefName.trim()+" "+Dobname;
						
						if(StrCity==null)
						   StrCity=""; 
						if(Dobnamob==null)
						   Dobnamob="";
						else if(Dobnamob.length()!=10)
						   Dobnamob=""; 
						
						if(imgP==null){
							theImage=null;
		  	  			}else if(imgP!=null){
			  	  			ByteArrayInputStream imageStream = new ByteArrayInputStream(imgP);
		  	  				theImage = BitmapFactory.decodeStream(imageStream);
		  	  			}else{
		  	  				theImage=null;
		  	  			}
						qry="Select S_Name,S_Mob,S_Pic,C3_BG from "+Tab2Name+" Where (M_MrgAnn_D='"+days+"' or M_MrgAnn_D='"+ddays+"') and (M_MrgAnn_M='"+months+"' or M_MrgAnn_M='"+mmonths+"') and M_id='"+MemM_id+"' and S_Mob IS NOT NULL and Length(S_Mob)<>0 ";
					    Cursor cursorT1 = db.rawQuery(qry, null);
					    int count=cursorT1.getCount();
					    if(count>0){
					    	products.add(new Product(Dobname,StrCity,Dobnamob,tick,theImage,false)); 
					    }else{
					    	products.add(new Product(Dobname,StrCity,Dobnamob,tick,theImage,true)); 
					    }
						
					    theImage = null;imgP=null;Dobname="";Dobnamob="";
						if(count>0){
							if (cursorT1.moveToNext()) {
								Dobname =cursorT1.getString(0);
								Dobnamob =cursorT1.getString(1);
								imgP=cursorT1.getBlob(2);
								PrefName=cursorT1.getString(3);
								if(PrefName==null)
								   PrefName="";
								
								if(PrefName.trim().length()>0)
									Dobname=PrefName.trim()+" "+Dobname;
								
								if(Dobnamob==null)
									Dobnamob="";
								else if(Dobnamob.length()!=10)
									Dobnamob=""; 
								
								if(imgP==null){
									theImage=null;
				  	  			}else if(imgP!=null){
					  	  			ByteArrayInputStream imageStream = new ByteArrayInputStream(imgP);
				  	  				theImage = BitmapFactory.decodeStream(imageStream);
				  	  			}else{
				  	  				theImage=null;
				  	  			}
								products.add(new Product(Dobname,StrCity,Dobnamob,tick,theImage,true)); 
							}
						}
						cursorT1.close();
						
				    } while (cursorT.moveToNext());
			  	 }
			 }
			 cursorT.close();
			
			 db.close();//Close DB
			 
			 if(products.size()!=0)
			 {
				boxAdapter = new ListAdapter2(this, products);
				cHkboxall.setVisibility(View.VISIBLE);
				listview1.setVisibility(View.VISIBLE);
			    listview1.setAdapter(boxAdapter);
			    txtnodisplay.setVisibility(View.GONE);
			    BtnSendmessage.setVisibility(View.VISIBLE);
		     }else{
			    cHkboxall.setVisibility(View.GONE);
			    listview1.setVisibility(View.GONE);
			    txtnodisplay.setVisibility(View.VISIBLE);
			    BtnSendmessage.setVisibility(View.GONE);
		     }
		 }catch(Exception ex){
			System.out.println(ex.getMessage());
	     }
		  	
		}
	 
	 public void ActionCall(){
		    actionBar = getActionBar();
		    //actionBar.setTitle("Today's Anniversary of "+LogclubName+" Group");
	        // add the custom view to the action bar
	        actionBar.setCustomView(R.layout.br_an_layout);
	    	cHkboxall = (CheckBox)actionBar.getCustomView().findViewById(R.id.checkBoxAnBrall);
	    	TextView txt = (TextView)actionBar.getCustomView().findViewById(R.id.textViewnane);
	    	txt.setText("Today's Anniversary of "+LogclubName+" Group");
	    	
	    	cHkboxall.setOnClickListener(new OnClickListener(){ 
	            public void onClick(View arg0){
	         	  if(cHkboxall.isChecked()==true){
	         		  if(chk==0){
	         			  connectdbandgetdata(true,dd,mm); 
	         		  }else if(chk==1){
	         			  connectdbandgetdata(true,strETDay,strETMonth);  
	         		  }
	         	  }else{
	         		  if(chk==0){
	         			  connectdbandgetdata(false,dd,mm);  
	         		  }else if((chk==1)&&(strETDay.length()!=0)&&(strETMonth.length()!=0)){
	         			  connectdbandgetdata(false,strETDay,strETMonth);  
	         		  }else{
	         			  listview1.setVisibility(View.GONE);
	         			  Toast.makeText(getApplicationContext(), "Select date for tick all..", 1).show();
	         		  }
	         	  }
	            }
	  	   });
	       actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM |ActionBar.DISPLAY_SHOW_HOME);
	     }
}
