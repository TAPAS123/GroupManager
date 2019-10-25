package group.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class VotingReport extends Activity {
    ListView listView;
    Context context = this;
    Thread networkThread;
    WebServiceCall opwb;
    String webserviceResponse="";
    ArrayList<JudgementRptModel> arraylist;
    ProgressDialog ringProgressDialog;
    int OP1_ID;
    String ClubName,ClientId,LogId,MTitle,Title; 
    byte[] AppLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_report);
        listView = (ListView) findViewById(R.id.listview);
       
        Intent menuIntent = getIntent(); 
        OP1_ID = menuIntent.getIntExtra("Mid",0);
        MTitle=menuIntent.getStringExtra("MTitle");
        LogId =  menuIntent.getStringExtra("Clt_LogID");
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		ClientId =  menuIntent.getStringExtra("UserClubName");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		
		Set_App_Logo_Title(); // Set App Logo and Title
        
        arraylist = new ArrayList<JudgementRptModel>();
        
        if(isInternetOn())
        {
            opwb = new WebServiceCall();
            webserviceCall();
        }
        else
        {
        	AlertDisplay("Internet Connection","No Internet Connection !");
        }
    }

    public void webserviceCall()
    {
        Show_ProgressDialog();
        networkThread = new Thread()
        {
            public void run()
            {
                    webserviceResponse = opwb.Group_ELEResult(ClientId, OP1_ID);
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            if (!webserviceResponse.contains("^"))
                            {
                            	AlertDisplay("",""+webserviceResponse);
                            }
                            else
                            {
                                fill_list();
                            }
                        }

                    });

                ringProgressDialog.dismiss();
            }
        };
        networkThread.start();
    }



    public void fill_list()
    {
        String ptpname="",ptpid="",total="";;
        int Sno=0;
        if(webserviceResponse.contains("#")) {
            String[] temp = webserviceResponse.split("#");
            for (int i = 0; i < temp.length; i++) {
                if (temp[i].contains("^")) {
                    String[] temp1 = temp[i].split("\\^");
                    ptpid = temp1[0];
                    ptpname = temp1[1];
                    total = temp1[2];
                    Sno++;
                    arraylist.add(new JudgementRptModel(Sno, ptpname, total, ptpid));
                }
            }
            listView.setAdapter(new AdapterJudgementRpt(context, R.layout.rowitem_judgement, arraylist));
        }
    }

    private boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED)
        {
            return true;
        }
        else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED)
        {
            return false;
        }
        return false;
    }

    private void Show_ProgressDialog()
    {
        ringProgressDialog = new ProgressDialog(context);
        ringProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ringProgressDialog.setMessage("Please Wait...");
        ringProgressDialog.setIndeterminate(true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.getWindow().setGravity(Gravity.DISPLAY_CLIP_VERTICAL);
        ringProgressDialog.show();
    }
    
    
    
    private void AlertDisplay(String head,String body)
    {
		AlertDialog ad=new AlertDialog.Builder(this).create();
		ad.setTitle( Html.fromHtml("<font color='#E3256B'>"+head+"</font>"));
   	    ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>"+body+"</font>"));
		ad.setButton("OK", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {
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
       
     public void backs()
   	 {
   		 Intent intent = new Intent(this,OpinionPoll_MainScreen.class);
   	   	 intent.putExtra("MTitle",MTitle);
   	   	 intent.putExtra("ComeFrom","2");
   	   	 intent.putExtra("Clt_LogID",LogId);
   	   	 intent.putExtra("Clt_ClubName",ClubName);
         intent.putExtra("UserClubName",ClientId);
         intent.putExtra("AppLogo", AppLogo);
   	     startActivity(intent);
   	     finish();
   	 }
}
