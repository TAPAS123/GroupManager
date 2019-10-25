package group.manager;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Club_PhotoUpload1 extends Activity {

    ListView Lv;
    TextView txtHead;
    ImageView imgvwUploadPics;
    String ClientId,ClubName,MTitle,Table2Name,Table4Name,LogId,Auth_ClubName="";
    List<RowEnvt> rowItems;
    RowEnvt item;
    byte[] AppLogo;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_photoupload1);
        
        Lv = (ListView) findViewById(R.id.LV1);
        txtHead=(TextView)findViewById(R.id.txthead);
        imgvwUploadPics=(ImageView)findViewById(R.id.imgvwUploadPics);

        Intent intent = getIntent();
        MTitle=intent.getStringExtra("MTitle");
		ClubName =  intent.getStringExtra("Clt_ClubName");
		ClientId =  intent.getStringExtra("UserClubName");
		LogId =  intent.getStringExtra("Clt_LogID");
		AppLogo =  intent.getByteArrayExtra("AppLogo");
		
		Table2Name="C_"+ClientId+"_2";
		Table4Name="C_"+ClientId+"_4";
        
		Set_App_Logo_Title(); // Set App Logo and Title
		
		txtHead.setText(MTitle);  //set heading
		
		imgvwUploadPics.setVisibility(View.GONE);///By Default Hide Upload Photosgraphs button
        
	    ChkDispUploadImages_And_FillListData();
	    
	    
	    Lv.setOnItemClickListener(new OnItemClickListener() {
	    	 public void onItemClick(AdapterView<?> parent, View view, int position, long id){
	    		String txtName=rowItems.get(position).getEvtName();
	    		txtName=txtName.replace(" ", "_");
	    		
	    	    Intent MainBtnIntent=new Intent(getBaseContext(),Club_PhotoUpload_Display.class);
	    	    MainBtnIntent.putExtra("Auth_ClubName",txtName);
			    MainBtnIntent.putExtra("Clt_ClubName",ClubName);
			    MainBtnIntent.putExtra("UserClubName",ClientId);
		        MainBtnIntent.putExtra("AppLogo", AppLogo);
		        startActivity(MainBtnIntent);
	            //finish();
	  	     }
	    });
	    
	    imgvwUploadPics.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0) {
            	Intent intent = new Intent(context, Club_PhotoUpload2.class);
                intent.putExtra("Auth_ClubName",Auth_ClubName);
                intent.putExtra("MTitle",MTitle);
                intent.putExtra("Clt_LogID",LogId);
                intent.putExtra("Clt_ClubName",ClubName);
                intent.putExtra("UserClubName",ClientId);
                intent.putExtra("AppLogo", AppLogo);
                startActivity(intent);
            }
        });
    }
    
    
    
    /// Check to display upload photographs button and fill list data
  	public void ChkDispUploadImages_And_FillListData()
  	{
  		SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
  		
  		////Check Condition of authorisation to display upload photographs button or not////
  		String Qry="SELECT C1_Bg,C2_Name from "+Table2Name+" Where M_id="+LogId;
  		Cursor  cursorT = db.rawQuery(Qry, null);
  		if (cursorT.moveToFirst()) {
  			String t1=ChkVal(cursorT.getString(0));
  			Auth_ClubName=ChkVal(cursorT.getString(1));// Authorised ClubName which is used to upload photographs
  			if(t1.equals("Y")){
  				imgvwUploadPics.setVisibility(View.VISIBLE);/// Visible Upload Photosgraphs button
  			}
  		}
  		cursorT.close();
  		//////////////////////////////////////////
  		
  		/// Fill List Data /////
  		
  		Qry="SELECT Distinct Text1 from "+Table4Name+" Where Rtype='Club_Acti' AND Text1<>'' Order By Num1 Desc";
  		cursorT = db.rawQuery(Qry, null);
  		
  		if(cursorT.getCount()>0){
  			rowItems = new ArrayList<RowEnvt>();
  			
  			if (cursorT.moveToFirst()) {
        	  	do {
        	  		String t1=ChkVal(cursorT.getString(0));
  	    			
  	    			item = new RowEnvt(t1,"");
  	    			rowItems.add(item); 
        	  } while (cursorT.moveToNext());
  			}
  			Adapter_Committee adp1 = new Adapter_Committee(context,R.layout.committee_list_items, rowItems);
        	Lv.setAdapter(adp1); 
          }
  		  cursorT.close();
  		  //////////////////////////////////////////
  		
 	      db.close();// Close Connection 
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
   
   
   private String ChkVal(String DVal)
   {
	   if((DVal==null)||(DVal.equalsIgnoreCase("null"))){
			DVal="";
		}
		return DVal.trim();
   }
   
   public void backs()
   {
	   Intent intent= new Intent(getBaseContext(),MenuPage.class);
	   intent.putExtra("AppLogo", AppLogo);
	   startActivity(intent);
	   finish();
   }

}