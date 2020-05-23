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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

import group.manager.AdapterClasses.AdapterJudgementRpt;

public class JudgementReport extends Activity implements View.OnClickListener {
    TextView txtHead;
    ListView LV1;
    ImageView imgFilter;
    ProgressDialog Progsdial;
    String selectedFilter = "JUDGE", WebResult = "", ClubName, ClientId, LogId, MTitle, Title;
    int OP1_ID = 0;
    byte[] AppLogo;
    ArrayList<JudgementRptModel> arraylist;
    Context context = this;
    Thread networkThread;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private boolean InternetPresent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judgement_report);

        txtHead = (TextView) findViewById(R.id.txtHead);
        LV1 = (ListView) findViewById(R.id.LV1);
        imgFilter = (ImageView) findViewById(R.id.imgFilter);

        Intent menuIntent = getIntent();
        OP1_ID = menuIntent.getIntExtra("Mid", 0);
        MTitle = menuIntent.getStringExtra("MTitle");
        LogId = menuIntent.getStringExtra("Clt_LogID");
        ClubName = menuIntent.getStringExtra("Clt_ClubName");
        ClientId = menuIntent.getStringExtra("UserClubName");
        AppLogo = menuIntent.getByteArrayExtra("AppLogo");

        sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("FilterType")) {
            selectedFilter = sharedPreferences.getString("FilterType", "");
        }

        Set_App_Logo_Title(); // Set App Logo and Title

        Typeface face=Typeface.createFromAsset(getAssets(), "calibri.ttf");
        txtHead.setTypeface(face);

        imgFilter.setOnClickListener(this);

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
                String ptpname = arraylist.get(position).ptpname;
                String ptpId = arraylist.get(position).ptpId;
                if (!ptpId.equals("0"))
                {
                    editor = sharedPreferences.edit();
                    editor.putString("FilterType", selectedFilter);
                    editor.putString("ptpname", ptpname);
                    editor.putString("ptp_ID", ptpId);
                    editor.commit();

                    Intent intent = new Intent(context, JudgementReport2.class);
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


    @Override
    public void onClick(View v) {
        if (v == imgFilter) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialogfilter);

            TextView txtJudge = (TextView) dialog.findViewById(R.id.txtJudge);
            TextView txtCriteria = (TextView) dialog.findViewById(R.id.txtCriteria);

            txtJudge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedFilter = "JUDGE";
                    dialog.dismiss();
                }
            });
            txtCriteria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedFilter = "CRITERIA";
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }


    private void WebCall() {
        Show_ProgressDialog();
        networkThread = new Thread() {
            public void run() {
                WebServiceCall webcall = new WebServiceCall();
                WebResult = webcall.Club_JudgeRpt(ClientId, OP1_ID);
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

    public void fill_list()
    {
        arraylist = new ArrayList<JudgementRptModel>();
        String ptpname = "", ptpid = "", total = "";
        int Sno = 0;
        if (WebResult.contains("#")) {
            String[] temp = WebResult.split("#");
            for (int i = 0; i < temp.length; i++) {
                if (temp[i].contains("^")) {
                    total = "0";
                    ptpid = "0";
                    String[] temp1 = temp[i].split("\\^");
                    ptpname = temp1[1];
                    if (temp1.length > 2) {
                        total = temp1[2];
                        ptpid = temp1[3];
                    }
                    Sno++;
                    arraylist.add(new JudgementRptModel(Sno, ptpname, total, ptpid));
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
        Intent intent = new Intent(this, OpinionPoll_MainScreen.class);
        intent.putExtra("MTitle", MTitle);
        intent.putExtra("ComeFrom", "2");
        intent.putExtra("Clt_LogID", LogId);
        intent.putExtra("Clt_ClubName", ClubName);
        intent.putExtra("UserClubName", ClientId);
        intent.putExtra("AppLogo", AppLogo);
        startActivity(intent);
        finish();
    }
}
