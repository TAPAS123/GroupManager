package group.manager;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.app.AlertDialog;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class JudgementReport extends Activity implements View.OnClickListener {
    TextView tvtitle;
    String webserviceResponse="";
    ArrayList<JudgementRptModel> arraylist;
    ListView listview;
    Context context = this;
    Thread networkThread;
    WebServiceCall opwb;
    ImageView imgviewfilter;
    ProgressDialog ringProgressDialog;
    int OP1_ID=0;
    String selectedFilter = "JUDGE";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String ClubName,ClientId,LogId,MTitle,Title; 
    byte[] AppLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judgement_report);
        tvtitle = (TextView) findViewById(R.id.tvjdgtitle);
        listview = (ListView) findViewById(R.id.listview);
        imgviewfilter = (ImageView) findViewById(R.id.imgviewfilter);
        imgviewfilter.setOnClickListener(this);
        arraylist = new ArrayList<JudgementRptModel>();
        
        Intent menuIntent = getIntent(); 
        OP1_ID = menuIntent.getIntExtra("Mid",0);
        MTitle=menuIntent.getStringExtra("MTitle");
        LogId =  menuIntent.getStringExtra("Clt_LogID");
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		ClientId =  menuIntent.getStringExtra("UserClubName");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		
		Set_App_Logo_Title(); // Set App Logo and Title
        
        sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        if(sharedPreferences.contains("FilterType"))
        {
            selectedFilter =sharedPreferences.getString("FilterType", "");
        }
        
        // Initialise
        opwb = new WebServiceCall();
        Show_ProgressDialog();
        networkThread = new Thread()
        {
            public void run()
            {
                if(isInternetOn())
                {
                    webserviceResponse = opwb.Club_JudgeRpt(ClientId, OP1_ID);
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
                }
                else
                {
                    AlertDisplay("Internet Connection","No Internet Connection !");
                }
                ringProgressDialog.dismiss();
            }
        };
        networkThread.start();


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ptpname = arraylist.get(position).ptpname;
                String ptpId = arraylist.get(position).ptpId;
                if (!ptpId.equals("0"))
                {
                	editor = sharedPreferences.edit();
                    editor.putString("FilterType",selectedFilter);
                    editor.putString("ptpname",ptpname);
                    editor.putString("ptp_ID",ptpId);
                    editor.commit();
                    
                    Intent intent = new Intent(context, JudgementReport2.class);
                    intent.putExtra("Mid",OP1_ID);
                    intent.putExtra("MTitle",MTitle);
                    intent.putExtra("Clt_LogID",LogId);
                    intent.putExtra("Clt_ClubName",ClubName);
                    intent.putExtra("UserClubName",ClientId);
                    intent.putExtra("AppLogo", AppLogo);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void fill_list()
    {
        String ptpname="",ptpid="",total="";;
        int Sno=0;
        if(webserviceResponse.contains("#"))
        {
            String[] temp = webserviceResponse.split("#");
            for(int i = 0;i<temp.length;i++)
            {
                if (temp[i].contains("^"))
                {
                    total="0";
                    ptpid="0";
                    String[] temp1 = temp[i].split("\\^");
                    ptpname = temp1[1];
                    if(temp1.length>2) {
                        total = temp1[2];
                        ptpid = temp1[3];
                    }
                    Sno++;
                    arraylist.add(new JudgementRptModel(Sno,ptpname,total,ptpid));
                }
            }
            listview.setAdapter(new AdapterJudgementRpt(context,R.layout.rowitem_judgement,arraylist));
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
        if(v == imgviewfilter)
        {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialogfilter);
            TextView tvjudge = (TextView) dialog.findViewById(R.id.tvjudge);
            TextView tvcriteria = (TextView) dialog.findViewById(R.id.tvcriteria);
//            editor = sharedPreferences.edit();
            tvjudge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    selectedFilter = "JUDGE";
//                    editor.putString("FilterType",selectedFilter);
//                    editor.commit();
                    dialog.dismiss();
                }
            });
            tvcriteria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    selectedFilter = "CRITERIA";

//                    editor.putString("FilterType",selectedFilter);
//                    editor.commit();
                    dialog.dismiss();
                }
            });

            dialog.show();
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
