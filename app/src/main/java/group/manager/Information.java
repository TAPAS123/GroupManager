package group.manager;

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
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Information extends Activity{

	String ClientId,ClubName,Table4Name;
	private Context context=this;
	byte[] AppLogo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information);
		
		TextView txtTitle=(TextView)findViewById(R.id.txtTitle);
		TextView txtDesc=(TextView)findViewById(R.id.txtDesc);
		
		Intent menuIntent = getIntent(); 
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		ClientId =  menuIntent.getStringExtra("UserClubName");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		
		Table4Name="C_"+ClientId+"_4";
		
		Set_App_Logo_Title();
		
		SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
		String Qry="SELECT Text1,Add1 FROM "+Table4Name +" where Rtype='M_OBJ'";
		Cursor cursorT = db.rawQuery(Qry, null);
		
		String Title="",Desc="";
		
		if (cursorT.moveToFirst()) {
			Title=ChkVal(cursorT.getString(0));
			Desc=ChkVal(cursorT.getString(1));
	    }
	    cursorT.close();
	    db.close();
	    
	    if(Title.length()==0 && Desc.length()==0){
	    	DispAlert("Message","No record found,please synchronize your data !");
	    }
	    else {
	    	txtTitle.setText(Title);
	    	txtDesc.setText(Desc);
	    }
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



 @SuppressWarnings("deprecation")
 private void DispAlert(String Title,String Msg)
 {
    AlertDialog ad=new AlertDialog.Builder(this).create();
    ad.setTitle( Html.fromHtml("<font color='#1C1CF0'>"+Title+"</font>"));
	    ad.setMessage(Msg);
	    ad.setCancelable(false);
	    ad.setButton("OK", new DialogInterface.OnClickListener() {
       public void onClick(DialogInterface dialog, int which) {
       
    	   Goback();
       }
     });
    ad.show();
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
