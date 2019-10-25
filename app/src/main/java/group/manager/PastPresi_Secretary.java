package group.manager;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PastPresi_Secretary extends Activity{
	ArrayList<RowEnvt> contact_list;
	ListView listView;
	TextView TVPastpresident,TvPastsecretray;
	String Chkpast="0",StrQuery="",Tab4name,Str_user,ClubName,getp_s;//Log,logid;
	private Context context=this;
	byte[] AppLogo;
	String MTitle;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pastpresisecretary);
		listView = (ListView)findViewById(R.id.listviewpast);
		TVPastpresident= (TextView)findViewById(R.id.Txtpastpresident);
        TvPastsecretray= (TextView)findViewById(R.id.Txtpastsceretray);
        if(Chkpast.equals("0")){
        	TvPastsecretray.setVisibility(View.VISIBLE);
        }else{
        	TvPastsecretray.setVisibility(View.GONE);
        }
        Intent menuIntent = getIntent(); 
        getp_s =  menuIntent.getStringExtra("selectP_S");
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		Str_user =  menuIntent.getStringExtra("UserClubName");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		MTitle =  menuIntent.getStringExtra("MTitle");
		TVPastpresident.setText(MTitle);
		
        Tab4name="C_"+Str_user+"_4";
		Set_App_Logo_Title(); // Set App Logo and Title
		if(getp_s.equals("2")){
			StrQuery="Select Text1,Text2,Text3,Text4 from "+Tab4name+" where Rtype='PASTS' Order by Num2 desc";
		}else{
			StrQuery="Select Text1,Text2,Text3,Text4 from "+Tab4name+" where Rtype='PASTP' Order by Num2 desc";	
		}
        getandshowdata(StrQuery,getp_s);//get data from database and show in listview.
        
        TVPastpresident.setOnClickListener(new OnClickListener(){ 
 		   public void onClick(View arg0){ 
 			   StrQuery="Select Text1,Text2,Text3,Text4 from "+Tab4name+" where Rtype='PASTP' Order by Num2 desc";
 		        getandshowdata(StrQuery,"1");//get data from database and show in listview. 
 		      }
 		   });  
        
        TvPastsecretray.setOnClickListener(new OnClickListener(){ 
 		   public void onClick(View arg0){ 
 			   StrQuery="Select Text1,Text2,Text3,Text4 from "+Tab4name+" where Rtype='PASTS' Order by Num2 desc";
 		        getandshowdata(StrQuery,"2");//get data from database and show in listview. 
 		      }
 		   });  
        
	}
	
	 private void getandshowdata(String sqlquery,final String chk){
		 SQLiteDatabase db;
		 Cursor cursorT;
		 RowEnvt item;
		 CustomAffil adapter;
		 String name,year,mobile,email;
		 contact_list = new ArrayList<RowEnvt>();
		 db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		 cursorT = db.rawQuery(sqlquery, null); 
		 if (cursorT.moveToFirst()) {
			 do {
			    name=cursorT.getString(0);
			    year=cursorT.getString(1);
			    mobile=cursorT.getString(2);
			    email=cursorT.getString(3);
			    if(name==null){
			    	name="";
			    }if(year==null){
			    	year="";
			    }if(mobile==null){
			    	mobile="";
			    }if(email==null){
			    	email="";
			    }
			   
			    item = new RowEnvt(year,name,mobile,email,"","");
			    // Adding contact to list
			    contact_list.add(item);
			 } while (cursorT.moveToNext());
			 cursorT.close();
			 db.close();
			 adapter = new CustomAffil(context,R.layout.affiliationlist,contact_list);
		     listView.setAdapter(adapter); 
		    // listView.getSelectedView().setBackgroundColor(getResources().getColor(Color.RED));
		     listView.setOnItemClickListener(new OnItemClickListener() {
		    	  public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		    	  {
		    		String year=contact_list.get(position).getEvtName();
		    		String name=contact_list.get(position).getEvtDesc();
		    		String mob=contact_list.get(position).getEvtdate();
		    		String email=contact_list.get(position).getEvtVenue();
    	    	    Intent menuIntent= new Intent(context,ShowPastPresSec.class);
     				menuIntent.putExtra("Clt_ClubName",ClubName);
     				menuIntent.putExtra("UserClubName",Str_user);
    	    	    menuIntent.putExtra("Pyear",year);
    	    	    menuIntent.putExtra("Pname",name);
    	    	    menuIntent.putExtra("Pmob",mob);
    	    	    menuIntent.putExtra("Pemail",email);
    	    	    menuIntent.putExtra("AppLogo", AppLogo);
    	    	    menuIntent.putExtra("StrChk",chk);
    	    	    menuIntent.putExtra("MTitle",MTitle);
    		        startActivity(menuIntent);
    		  		finish();
		  	      }
		      });
		   }else{
			     cursorT.close();
				 db.close();
				 AlertDialog ad=new AlertDialog.Builder(this).create();
			     ad.setTitle( Html.fromHtml("<font color='#FF7F27'>Result</font>"));
		    	 ad.setMessage("No record found.");
		    	 ad.setCancelable(false);
		    	 ad.setButton("OK", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            	Goback();
		            }
		          });
		         ad.show();
	       }
	      
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
		   		Goback();
		   	    return true;
		   	 }
		   	return super.onKeyDown(keyCode, event);
	   }
	   
	   private void Goback()
	   {
		   Intent menuIntent= new Intent(getBaseContext(),MenuPage.class);
		   menuIntent.putExtra("AppLogo", AppLogo);
		   startActivity(menuIntent);
		   finish();
	   }
}
