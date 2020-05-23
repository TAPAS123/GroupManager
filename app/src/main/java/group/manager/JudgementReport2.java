package group.manager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import group.manager.AdapterClasses.AdapterJudgementRpt;

public class JudgementReport2 extends Activity {
    ListView LV1;
    TextView txtHead, txtName;
    Thread networkThread;
    ProgressDialog Progsdial;
    String WebResult = "", FilterType = "", ptp_ID, ptpname, ClubName, ClientId, LogId, MTitle, Title;
    Context context = this;
    ArrayList<JudgementRptModel> arraylist;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int OP1_ID = 0;
    byte[] AppLogo;
    private boolean InternetPresent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judgement_report2);

        txtHead = (TextView) findViewById(R.id.txtHead);
        txtName = (TextView) findViewById(R.id.txtName);
        LV1 = (ListView) findViewById(R.id.LV1);

        Intent menuIntent = getIntent();
        OP1_ID = menuIntent.getIntExtra("Mid", 0);
        MTitle = menuIntent.getStringExtra("MTitle");
        LogId = menuIntent.getStringExtra("Clt_LogID");
        ClubName = menuIntent.getStringExtra("Clt_ClubName");
        ClientId = menuIntent.getStringExtra("UserClubName");
        AppLogo = menuIntent.getByteArrayExtra("AppLogo");

        GetSh_Pref();//Get Shared Preference Data

        Set_App_Logo_Title(); // Set App Logo and Title

        txtHead.setText(ptpname);
        Typeface face = Typeface.createFromAsset(getAssets(), "calibri.ttf");
        txtHead.setTypeface(face);

        if (FilterType.equalsIgnoreCase("JUDGE")) {
            txtName.setText("Judge");
        } else {
            txtName.setText("Criteria");
        }

        Chkconnection chkconn = new Chkconnection();//Intialise Chkconnection Object
        InternetPresent = chkconn.isConnectingToInternet(context);
        if (InternetPresent == true) {
            WebCall();
        } else {
            AlertDisplay("Internet Connection", "No Internet Connection !");
        }

        LV1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (FilterType.equals("JUDGE")) {
                    String judge_id = arraylist.get(position).ptpId;    //ptpID = judgeID
                    String judgename = arraylist.get(position).ptpname;  //ptpname = judgename

                    editor = sharedPreferences.edit();
                    editor.putString("ptp_ID", ptp_ID);
                    editor.putString("JudgeName", judgename);
                    editor.putString("Judge_ID", judge_id);
                    editor.commit();

                    Intent intent = new Intent(context, JudgementReport3.class);
                    intent.putExtra("Mid", OP1_ID);
                    intent.putExtra("MTitle", MTitle);
                    intent.putExtra("Clt_LogID", LogId);
                    intent.putExtra("Clt_ClubName", ClubName);
                    intent.putExtra("UserClubName", ClientId);
                    intent.putExtra("AppLogo", AppLogo);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void GetSh_Pref() {

        sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("FilterType")) {
            FilterType = sharedPreferences.getString("FilterType", "");
        }
        if (sharedPreferences.contains("ptpname")) {
            ptpname = sharedPreferences.getString("ptpname", "");
        }
        if (sharedPreferences.contains("ptp_ID")) {
            ptp_ID = sharedPreferences.getString("ptp_ID", "");
        }
    }


    private void WebCall() {
        Show_ProgressDialog();
        networkThread = new Thread() {
            public void run() {
                WebServiceCall webcall = new WebServiceCall();
                if (FilterType.equalsIgnoreCase("JUDGE")) {
                    WebResult = webcall.Club_JudgeRpt_Descr(ClientId, OP1_ID, Integer.valueOf(ptp_ID));
                } else if (FilterType.equalsIgnoreCase("CRITERIA")) {
                    WebResult = webcall.Club_JudgeRpt_DescrCRITERIA(ClientId, OP1_ID, Integer.valueOf(ptp_ID));
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        if (!WebResult.contains("^")) {
                            AlertDisplay("", "" + WebResult);
                        } else {
                            fill_list();
                        }
                    }

                });

                Progsdial.dismiss();
            }
        };
        networkThread.start();
    }

    public void fill_list() {
        arraylist = new ArrayList<JudgementRptModel>();
        String judgename = "", ID = "", marks = "", judgeID = "";
        int Sno = 0;
        WebResult = WebResult + "#";
        if (WebResult.contains("#")) {
            String[] temp = WebResult.split("#");
            for (int i = 0; i < temp.length; i++) {
                if (temp[i].contains("^")) {
                    String[] temp1 = temp[i].split("\\^");
                    if (FilterType.equalsIgnoreCase("CRITERIA")) {
                        judgeID = "";
                        judgename = temp1[0];
                        marks = temp1[1];
                        //  ID = "";
                    } else {
                        judgeID = temp1[0];
                        judgename = temp1[1];
                        marks = temp1[2];
                        //    ID = temp1[4];
                    }
                    Sno++;
                    arraylist.add(new JudgementRptModel(Sno, judgename, marks, judgeID));
                }
            }
            LV1.setAdapter(new AdapterJudgementRpt(context, R.layout.rowitem_judgement, arraylist));
        }
    }

    private void Show_ProgressDialog() {
        Progsdial = new ProgressDialog(context);
        Progsdial.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Progsdial.setMessage("Please Wait...");
        Progsdial.setIndeterminate(true);
        Progsdial.setCancelable(false);
        Progsdial.getWindow().setGravity(Gravity.DISPLAY_CLIP_VERTICAL);
        Progsdial.show();
    }


    private void Set_App_Logo_Title() {
        setTitle(ClubName); // Set Title
        // Set App LOGO
        if (AppLogo == null) {
            getActionBar().setIcon(R.drawable.ic_launcher);
        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(AppLogo, 0, AppLogo.length);
            BitmapDrawable icon = new BitmapDrawable(getResources(), bitmap);
            getActionBar().setIcon(icon);
        }
    }

    private void AlertDisplay(String head, String body) {
        AlertDialog ad = new AlertDialog.Builder(this).create();
        ad.setTitle(Html.fromHtml("<font color='#E3256B'>" + head + "</font>"));
        ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>" + body + "</font>"));
        ad.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                GoBack();
            }
        });
        ad.show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            GoBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void GoBack() {
        Intent intent = new Intent(context, JudgementReport.class);
        intent.putExtra("Mid", OP1_ID);
        intent.putExtra("MTitle", MTitle);
        intent.putExtra("Clt_LogID", LogId);
        intent.putExtra("Clt_ClubName", ClubName);
        intent.putExtra("UserClubName", ClientId);
        intent.putExtra("AppLogo", AppLogo);
        startActivity(intent);
        finish();
    }
}
