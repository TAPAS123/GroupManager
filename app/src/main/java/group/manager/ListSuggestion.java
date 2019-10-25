package group.manager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ListSuggestion extends Activity{
	List<RowEnvt> rowItems;
	CustomPendinglist adapter1;
	ListView lvEvent;
	SQLiteDatabase db;
	Cursor cursorT;
	String sqlSearch,Table2Name,Table4Name,Log,ClubName,logid,Str_user,StrTitle,StrAdd,StrM_id,StrEmail;
	Intent menuIntent;
	String [] temp;
	RowEnvt item;
	Context context=this;
	TextView txtHeadent;
	int Strcnt;
	byte[] AppLogo;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventcalendar);
        lvEvent = (ListView) findViewById(R.id.listViewEvnt);
        txtHeadent=(TextView)findViewById(R.id.txtevnthead);
        menuIntent = getIntent(); 
        Strcnt =  menuIntent.getIntExtra("Count", Strcnt);
		Str_user =  menuIntent.getStringExtra("UserClubName");
		Log =  menuIntent.getStringExtra("Clt_Log");
		logid =  menuIntent.getStringExtra("Clt_LogID");
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
        Table2Name="C_"+Str_user+"_2";
		Table4Name="C_"+Str_user+"_4";
		Set_App_Logo_Title(); // Set App Logo and Title
        db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
        sqlSearch="SELECT Text1,Add1,m_id,Text2 from "+Table4Name+" Where Rtype='T_SUG' Order By m_id";
        cursorT = db.rawQuery(sqlSearch, null);
        System.out.println(cursorT.getCount());
        if(cursorT.getCount()==0){
        	txtHeadent.setText("No Records");
        }else{
        	txtHeadent.setText("List of Suggestion/Complain");
        	rowItems = new ArrayList<RowEnvt>();
        	 if (cursorT.moveToFirst()) {
  			   do {
  				   StrTitle=cursorT.getString(0);
  				   StrAdd =cursorT.getString(1);
  				   StrM_id =cursorT.getString(2);
  				   StrEmail =cursorT.getString(3);
  				   item = new RowEnvt(StrTitle,StrAdd,StrM_id,StrEmail);
        	       rowItems.add(item); 
  	    		 } while (cursorT.moveToNext());
  	    	 }
        	 adapter1 = new CustomPendinglist(context,R.layout.listpending, rowItems);
             lvEvent.setAdapter(adapter1); 
        }
        cursorT.close();
        db.close(); 
        
        lvEvent.setOnItemClickListener(new OnItemClickListener() {
	    	 public void onItemClick(AdapterView<?> parent, View view, int position, long id){
	    	    String mid=rowItems.get(position).getEvtdate();
	    	   // Toast.makeText(context, Pwd+"  "+Desc, 1).show();
	    	    menuIntent= new Intent(context,ShowAffiliation.class);
	    	    menuIntent.putExtra("Count", Strcnt);
	    	    menuIntent.putExtra("Clt_LogID",logid);
 				menuIntent.putExtra("Clt_Log",Log);
 				menuIntent.putExtra("Clt_ClubName",ClubName);
 				menuIntent.putExtra("UserClubName",Str_user);
	    	    menuIntent.putExtra("Pwd",mid);
	    	    menuIntent.putExtra("CHKg","2");
	    	    menuIntent.putExtra("AppLogo", AppLogo);
		        startActivity(menuIntent);
		  		finish();
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
	   		Intent	MainBtnIntent= new Intent(getBaseContext(),Sug_Comp.class);
	   		    MainBtnIntent.putExtra("Count", Strcnt);
	   		    MainBtnIntent.putExtra("Clt_Log",Log);
	   		    MainBtnIntent.putExtra("Clt_LogID",logid);
	   		    MainBtnIntent.putExtra("Clt_ClubName",ClubName);
	   		    MainBtnIntent.putExtra("UserClubName",Str_user);
	   		    MainBtnIntent.putExtra("AppLogo", AppLogo);
		    	startActivity(MainBtnIntent);
		    	finish();
	   	 return true;
	   	 }
	   	return super.onKeyDown(keyCode, event);
	 }
	
	
}
