package group.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class Wedding1 extends Activity implements View.OnClickListener {
   
    Button bridebtn,groombtn;
    Context context = this;
    String ClientID,ClubName;
    byte[] AppLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wedding1);
        bridebtn = (Button)findViewById(R.id.bridebtn);
        groombtn = (Button)findViewById(R.id.groombtn);
        
        Intent intent = getIntent(); 
        ClientID =  intent.getStringExtra("UserClubName");
		ClubName =  intent.getStringExtra("Clt_ClubName");
		AppLogo =  intent.getByteArrayExtra("AppLogo");
		
		Set_App_Logo_Title();// Set App Logo and Title
        
        bridebtn.setOnClickListener(this);
        groombtn.setOnClickListener(this);
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
    
    
    @Override
    public void onClick(View v) {

        if(v == bridebtn)
        {
            Intent intent = new Intent(this,Wedding2.class);
            intent.putExtra("Type","Female");
            intent.putExtra("Forward_id","pdetails");
            intent.putExtra("Clt_ClubName",ClubName);
            intent.putExtra("UserClubName",ClientID);
            intent.putExtra("AppLogo", AppLogo);
            startActivity(intent);
        }
        else if(v == groombtn)
        {
            Intent intent = new Intent(this,Wedding2.class);
            intent.putExtra("Type","Male");
            intent.putExtra("Forward_id","pdetails");
            intent.putExtra("Clt_ClubName",ClubName);
            intent.putExtra("UserClubName",ClientID);
            intent.putExtra("AppLogo", AppLogo);
            startActivity(intent);
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
		Intent MainBtnIntent= new Intent(this,MenuPage.class);
		MainBtnIntent.putExtra("AppLogo", AppLogo);
      	startActivity(MainBtnIntent);
	    finish();
	}
    
}
