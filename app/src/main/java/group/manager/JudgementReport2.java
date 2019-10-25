package group.manager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class JudgementReport2 extends Activity {
    Thread networkThread;
    WebServiceCall opwb;
    ProgressDialog ringProgressDialog;
    String webserviceResponse="",FilterType="";
    String ptp_ID,ptpname;
    Context context = this;
    ListView listview;
    TextView tvtitle,nametitle;
    ArrayList<JudgementRptModel> arraylist;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    
    int OP1_ID=0;
    String ClubName,ClientId,LogId,MTitle,Title; 
    byte[] AppLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judgement_report2);

        tvtitle = (TextView) findViewById(R.id.tvjdgtitle);
        nametitle = (TextView) findViewById(R.id.nametitle);
        listview = (ListView) findViewById(R.id.listview);

        Intent menuIntent = getIntent(); 
        OP1_ID = menuIntent.getIntExtra("Mid",0);
        MTitle=menuIntent.getStringExtra("MTitle");
        LogId =  menuIntent.getStringExtra("Clt_LogID");
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		ClientId =  menuIntent.getStringExtra("UserClubName");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		
		Set_App_Logo_Title(); // Set App Logo and Title
        
        arraylist = new ArrayList<JudgementRptModel>();
        sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        getshareprefval();
        
        tvtitle.setText(ptpname);
        
        if(FilterType.equalsIgnoreCase("JUDGE"))
        {
           nametitle.setText("Judge");
        }
        else {
            nametitle.setText("Criteria");
        }
        opwb = new WebServiceCall();
        Show_ProgressDialog();
        networkThread = new Thread()
        {
            public void run()
            {
                if(isInternetOn())
                {
                    if(FilterType.equalsIgnoreCase("JUDGE")) {
                        webserviceResponse = opwb.Club_JudgeRpt_Descr(ClientId, OP1_ID, Integer.valueOf(ptp_ID));
                    }
                    else if(FilterType.equalsIgnoreCase("CRITERIA"))
                    {
                        webserviceResponse = opwb.Club_JudgeRpt_DescrCRITERIA(ClientId,OP1_ID, Integer.valueOf(ptp_ID));
                    }
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
            //    String ptpname = arraylist.get(position).ptpname;
                if(FilterType.equals("JUDGE"))
                {
                    String judge_id = arraylist.get(position).ptpId;    //ptpID = judgeID
                    String judgename = arraylist.get(position).ptpname;  //ptpname = judgename
                   
                    editor = sharedPreferences.edit();
                    editor.putString("ptp_ID",ptp_ID);
                    editor.putString("JudgeName",judgename);
                    editor.putString("Judge_ID",judge_id);
                    editor.commit();
                    
                    Intent intent = new Intent(context, JudgementReport3.class);
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

    private void getshareprefval() {

        if(sharedPreferences.contains("FilterType"))
        {
            FilterType =sharedPreferences.getString("FilterType", "");
        }
        if(sharedPreferences.contains("ptpname"))
        {
            ptpname =sharedPreferences.getString("ptpname", "");
        }
        if(sharedPreferences.contains("ptp_ID"))
        {
            ptp_ID =sharedPreferences.getString("ptp_ID", "");
        }
    }

    public void fill_list()
    {
        String judgename="",ID="",marks="",judgeID="";;
        int Sno=0;
        webserviceResponse = webserviceResponse + "#";
        if(webserviceResponse.contains("#")) {
            String[] temp = webserviceResponse.split("#");
            for (int i = 0; i < temp.length; i++) {
                if (temp[i].contains("^")) {
                    String[] temp1 = temp[i].split("\\^");
                    if(FilterType.equalsIgnoreCase("CRITERIA"))
                    {
                        judgeID="";
                        judgename = temp1[0];
                        marks = temp1[1];
                      //  ID = "";
                    }
                    else
                    {
                        judgeID = temp1[0];
                        judgename = temp1[1];
                        marks = temp1[2];
                    //    ID = temp1[4];
                    }
                    Sno++;
                    arraylist.add(new JudgementRptModel(Sno, judgename, marks, judgeID));
                }
            }
            listview.setAdapter(new AdapterJudgementRpt(context, R.layout.rowitem_judgement, arraylist));
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
    
    
    private void AlertDisplay(String head,String body)
    {
		AlertDialog ad=new AlertDialog.Builder(this).create();
		ad.setTitle( Html.fromHtml("<font color='#E3256B'>"+head+"</font>"));
       	ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>"+body+"</font>"));
		ad.setButton("OK", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {
        	   back();
           }
       });
       ad.show();	
    }
    
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
             back();
             return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void back() {
        Intent intent = new Intent(context,JudgementReport.class);
        intent.putExtra("Mid", OP1_ID);
	   	 intent.putExtra("MTitle",MTitle);
	   	 intent.putExtra("Clt_LogID",LogId);
	   	 intent.putExtra("Clt_ClubName",ClubName);
        intent.putExtra("UserClubName",ClientId);
        intent.putExtra("AppLogo", AppLogo);
        startActivity(intent);
        finish();
    }
}
