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

public class DebtorLedger extends Activity {
    Thread networkThread;
    String webserviceResponse="",ClientID="",Type="";
    WebServiceCall WebObj;
    ProgressDialog ringProgressDialog;
    Context context=this;
    ListView listView;
    TextView tvtitle;
    ArrayList<DebtorLedgerModel> arraylist;
    String Log,ClubName,logid,StrClubName;
    byte[] AppLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debtor_ledger);

        tvtitle = (TextView) findViewById(R.id.tvtitle);
        listView = (ListView) findViewById(R.id.listview);
        
        Intent menuIntent = getIntent(); 
		Log =  menuIntent.getStringExtra("Clt_Log");
		logid =  menuIntent.getStringExtra("Clt_LogID");
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		ClientID =  menuIntent.getStringExtra("UserClubName");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		Type =  menuIntent.getStringExtra("Type");
        
		Set_App_Logo_Title(); // Set App Logo and Title
	
        arraylist = new ArrayList<DebtorLedgerModel>();
        
        WebObj = new WebServiceCall();
        
        if (isInternetOn()) {
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
                try{
                    webserviceResponse=WebObj.Group_DebtorLedger(ClientID,logid,Type);
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
                catch (Exception Exception)
                {
                    ringProgressDialog.dismiss();
                }
            }
        };
        networkThread.start();
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

  public  void fill_list()
  {
      String amt = "";
      if(webserviceResponse.contains("#"))
      {
          String[] temp = webserviceResponse.split("#");
          for(int i =0;i<temp.length;i++)
          {
              if(temp[i].contains("^"))
              {
                  String[] temp1 = temp[i].split("\\^");
                  tvtitle.setText(temp1[0]);
                  if(temp1[2].contains("Closing Balance"))
                  {
                      amt = "";
                  }
                  else
                  {
                      amt = temp1[5];
                  }
                  if(temp1[3].equals("0"))
                  {
                      temp1[3] = "";
                  }
                  if(temp1[4].equals("0"))
                  {
                      temp1[4] = "";
                  }
                  arraylist.add(new DebtorLedgerModel(temp1[1],temp1[2],temp1[3],temp1[4],amt));
              }
          }
          listView.setAdapter(new AdapterDebtorLedger(context,R.layout.rowitem_debtorledger,arraylist));
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
	   Intent MainBtnIntent= new Intent(DebtorLedger.this,MenuPage.class);
	   MainBtnIntent.putExtra("AppLogo", AppLogo);
 	   startActivity(MainBtnIntent);
	   finish();
   }
  

}
