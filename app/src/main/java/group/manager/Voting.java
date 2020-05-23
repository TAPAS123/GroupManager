package group.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import group.manager.AdapterClasses.AdapterVoting;

public class Voting extends Activity implements View.OnClickListener {
    TextView txtTitle;
    ListView LV1;
    Button btnSubmit;
    SQLiteDatabase db;
    String qry = "", title = "", choiceType = "", multiplechoicecount = "", SelectedData = "",
            ptpnames = "",ClubName, ClientId, LogId, MTitle, Title, UserType, TabType, WebResult = "",GPSLoc = "";
    int Sno,flag = 0,count = 0,personID = 0,Mid = 0,ChkSubmit = 0;
    byte[] AppLogo;
    AdapterVoting AdpVoting;
    ArrayList<VotingModel> arrlist;
    Context context = this;
    ProgressDialog Progsdial;
    private boolean InternetPresent;
    boolean VoterChangeVote = false, DispVoterOption = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        LV1 = (ListView) findViewById(R.id.LV1);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        Intent menuIntent = getIntent();
        Mid = menuIntent.getIntExtra("Mid", 0);
        Title = menuIntent.getStringExtra("Title");
        TabType = menuIntent.getStringExtra("TabType");
        MTitle = menuIntent.getStringExtra("MTitle");
        LogId = menuIntent.getStringExtra("Clt_LogID");
        ClubName = menuIntent.getStringExtra("Clt_ClubName");
        ClientId = menuIntent.getStringExtra("UserClubName");
        AppLogo = menuIntent.getByteArrayExtra("AppLogo");

        Set_App_Logo_Title(); // Set App Logo and Title

        Get_SharedPref_Values();///Get Stored Shared Pref Values

        Typeface face=Typeface.createFromAsset(getAssets(), "calibri.ttf");
        txtTitle.setTypeface(face);

        arrlist = new ArrayList<VotingModel>();

        btnSubmit.setOnClickListener(this);

        db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        qry = "Select Question,Ans3,Ans4 from C_" + ClientId + "_OP2 where OP1_ID =" + Mid + " AND Ans2 ='Criteria'";
        Cursor cursor = db.rawQuery(qry, null);
        if (cursor.moveToFirst()) {
            title = chkval(cursor.getString(0));
            choiceType = chkval(cursor.getString(1));
            multiplechoicecount = chkval(cursor.getString(2));
        }
        txtTitle.setText(title);

        ////// Check Voter Change Option and check voter display his selected option
        qry = "Select Ans1,Ans3 from C_" + ClientId + "_OP2 where OP1_ID =" + Mid + " AND Ans2 ='Cond'";
        cursor = db.rawQuery(qry, null);
        String A1 = "", A2 = "";
        if (cursor.moveToFirst()) {
            A1 = chkval(cursor.getString(0));
            A2 = chkval(cursor.getString(1));
        }
        cursor.close();

        //check voter display his selected option 
        if (A1.equals("1")) {
            DispVoterOption = true;
        }

        //Check Voter Change his selected Option
        if (A2.equals("1")) {
            VoterChangeVote = true;
        }
        //////////////////////

        ////Check the Voting Data Submitted or Not
        qry = "Select Submit From C_" + ClientId + "_OP3 Where OP1_ID=" + Mid;
        cursor = db.rawQuery(qry, null);
        if (cursor.moveToFirst()) {
            ChkSubmit = cursor.getInt(0);
        }
        cursor.close();
        //////////////////////

        if (ChkSubmit == 0) {
            btnSubmit.setVisibility(View.VISIBLE);
            LV1.setEnabled(true);
        } else {
            btnSubmit.setVisibility(View.GONE);
            LV1.setEnabled(false);

            if (VoterChangeVote) {
                btnSubmit.setVisibility(View.VISIBLE);
                LV1.setEnabled(true);
            }

            ////Display Saved Voter option with Selection////
            if (DispVoterOption && (choiceType.equalsIgnoreCase("1") || choiceType.equalsIgnoreCase("M"))) {
                String SqlQry = "Select OP2_ID from C_" + ClientId + "_OP3 Where OP1_ID=" + Mid + " AND Submit=1 Order by OP2_ID";
                Cursor cursorT = db.rawQuery(SqlQry, null);
                while (cursorT.moveToNext()) {
                    int Op2Id = cursorT.getInt(0);
                    SelectedData = SelectedData + Op2Id + "#";
                }
                cursorT.close();
            }
        }

        qry = "Select M_ID,Sno,Question from C_" + ClientId + "_OP2 where OP1_ID =" + Mid + " AND Ans2 ='Options'";
        Cursor cursor1 = db.rawQuery(qry, null);
        if (cursor1.moveToFirst()) {
            do {
                personID = cursor1.getInt(0);
                Sno = cursor1.getInt(1);
                ptpnames = chkval(cursor1.getString(2));
                boolean chk = false;
                if (SelectedData.length() > 0) {
                    String[] Arr1 = SelectedData.split("#");

                    for (int i = 0; i < Arr1.length; i++) {
                        int s1 = Integer.parseInt(Arr1[i]);
                        if (personID == s1) {
                            chk = true;
                            break;
                        }
                    }
                }

                arrlist.add(new VotingModel(personID, Sno, ptpnames, choiceType, chk, multiplechoicecount));
            } while (cursor1.moveToNext());
            AdpVoting = new AdapterVoting(this, R.layout.listitem_voting, arrlist);
            LV1.setAdapter(AdpVoting);
        }
        cursor1.close();


        if (TabType.equals("PAST")) {
            btnSubmit.setVisibility(View.GONE);
            LV1.setEnabled(false);
        }

        LV1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String choiceType = arrlist.get(position).choicetype;
                if (choiceType.equals("1")) {
                    for (int i = 0; i < arrlist.size(); i++) {
                        arrlist.get(i).chkval = false;
                    }
                    if (arrlist.get(position).chkval == true) {
                        arrlist.get(position).chkval = false;
                    } else {
                        arrlist.get(position).chkval = true;
                    }
                    AdpVoting.notifyDataSetChanged();
                }
                if (choiceType.equals("M")) {
                    if (arrlist.get(position).chkval == true) {
                        arrlist.get(position).chkval = false;
                        count--;
                    } else {
                        if (count < Integer.valueOf(multiplechoicecount)) {
                            arrlist.get(position).chkval = true;
                            count++;
                        }
                    }
                    AdpVoting.notifyDataSetChanged();
                }
            }

        });
    }


    @Override
    public void onClick(View v) {
        if (v == btnSubmit)
        {
            Chkconnection chkconn = new Chkconnection();//Intialise Chkconnection Object
            InternetPresent = chkconn.isConnectingToInternet(context);
            if (InternetPresent == true) {

                if (!db.isOpen())
                    db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);

                if (choiceType.equalsIgnoreCase("1") || choiceType.equalsIgnoreCase("M")) {
                    flag = 0;
                    for (int i = 0; i < arrlist.size(); i++) {
                        if (arrlist.get(i).chkval == true) {
                            personID = arrlist.get(i).personID;

                            String qry = "";
                            if (VoterChangeVote && ChkSubmit == 1) {
                                qry = "Update C_" + ClientId + "_OP3 Set OP2_ID=" + personID + " Where OP1_ID=" + Mid + "";
                            } else {
                                qry = "insert into C_" + ClientId + "_OP3  (OP1_ID,OP2_ID,User_Ans) values (" + Mid + "," + personID + ",1)";
                            }

                            db.execSQL(qry);
                            flag = 1;
                            if (choiceType.equalsIgnoreCase("1")) {
                                break;
                            }
                        }
                    }
                    if (flag == 1) {
                        GetGPS_Location();///Get GPS Location
                        Submit_VotingData();///Voting Data Submit
                    } else {
                        AlertDisplay("Result", "Can't submit! Please select atleast one.", false);
                    }
                } else if (choiceType.equalsIgnoreCase("P")) {
                    View view;
                    EditText et;
                    flag = 0;
                    count = 0;
                    String pref = "";
                    // save participants marks

                    int listLength = LV1.getChildCount();
                    String[] range = new String[listLength];
                    for (int i = 0; i < listLength; i++) {
                        view = LV1.getChildAt(i);
                        et = (EditText) view.findViewById(R.id.edPref);
                        pref = chkval(et.getText().toString());
                        if (!pref.equals("")) {
                            if (Integer.valueOf(pref) < 0 || Integer.valueOf(pref) >= Integer.valueOf(multiplechoicecount)) {
                                flag = 1;
                            }
                        } else {
                            count++;
                        }
                        range[i] = pref;
                    }
                    if (flag == 0 && count != listLength) {
                        for (int i = 0; i < listLength; i++) {
                            pref = range[i];
                            if (!pref.equals("")) {
                                personID = arrlist.get(i).personID;
                                String insrtqry = "insert into C_" + ClientId + "_OP3  (OP1_ID,OP2_ID,User_Ans) values (" + Mid + "," + personID + ",'" + pref + "')";
                                db.execSQL(insrtqry);
                            }
                        }
                        Submit_VotingData();///Voting Data Submit
                    } else {
                        AlertDisplay("Result", "Can't submit! Please enter preference.", false);
                    }
                }
            } else {
                AlertDisplay("Internet Connection", "Please check your internet connection !", false);
            }
        }
    }


    private void Get_SharedPref_Values() {
        SharedPreferences ShPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        if (ShPref.contains("UserType")) {
            UserType = ShPref.getString("UserType", "");
        }
    }


    private void Submit_VotingData() {
        String Qry = "Update C_" + ClientId + "_OP3 Set Submit=1,SyncID=1 Where OP1_ID=" + Mid;
        db.execSQL(Qry);
        Sync_Voting_Data();
    }


    //Sync Voting Data M-S ////
    public void Sync_Voting_Data() {
        progressdial();
        Thread T2 = new Thread() {
            @Override
            public void run() {
                try {
                    if (!db.isOpen())
                        db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);

                    String SqlQry = "Select OP2_ID,User_Ans,Remark from C_" + ClientId + "_OP3 Where OP1_ID=" + Mid + " AND Submit=1 AND SyncID=1 Order by OP2_ID";
                    Cursor cursorT = db.rawQuery(SqlQry, null);
                    String UAns, Remark, SData = "";
                    int Op2Id;
                    while (cursorT.moveToNext()) {
                        Op2Id = cursorT.getInt(0);
                        UAns = chkval(cursorT.getString(1));
                        Remark = chkval(cursorT.getString(2));
                        SData = SData + Op2Id + "^" + UAns + "^" + Remark + "@@";
                    }
                    cursorT.close();


                    if (SData.length() > 2) {
                        SData = SData.substring(0, SData.length() - 2);

                        String TempMS = "M";
                        if (UserType.equals("SPOUSE")) {
                            TempMS = "S";
                        }

                        SData = LogId + "#" + TempMS + "#" + Mid + "#" + SData + "#" + GPSLoc;

                        WebServiceCall webcall = new WebServiceCall();
                        WebResult = webcall.Sync_OpinionPoll_MS(ClientId, SData);
                        if (WebResult.contains("Saved")) {
                            SqlQry = "Update C_" + ClientId + "_OP3 Set SyncID=0 Where OP1_ID=" + Mid;
                            db.execSQL(SqlQry);
                        } else {
                            SqlQry = "Delete from C_" + ClientId + "_OP3 Where OP1_ID=" + Mid;
                            db.execSQL(SqlQry);
                        }
                    }
                    db.close();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (WebResult.contains("Saved")) {
                                btnSubmit.setVisibility(View.GONE);
                                LV1.setEnabled(false);
                                AlertDisplay("Result", "Submitted Successfully !", true);
                            } else if (WebResult.contains("Error")) {
                                AlertDisplay("Technical issue", "Something went wrong. Please try later !", false);
                            } else {
                                AlertDisplay("Result", WebResult, false);
                            }
                        }
                    });
                } catch (Exception e) {
                    //System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                Progsdial.dismiss();
            }
        };
        T2.start();
    }


    protected void progressdial() {
        Progsdial = new ProgressDialog(this, R.style.MyTheme);
        Progsdial.setMessage("Please Wait....");
        Progsdial.setIndeterminate(true);
        Progsdial.setCancelable(false);
        Progsdial.getWindow().setGravity(Gravity.DISPLAY_CLIP_VERTICAL);
        Progsdial.show();
    }

    ///Get GPS Location
    private void GetGPS_Location() {
        GPSTracker gps = new GPSTracker(context);//Get GPS or Network Location Added on 25-10-2017
        double Lati = 0, Longi = 0;
        if (gps.canGetLocation()) {
            Lati = gps.getLatitude();//Residence Latitude
            Longi = gps.getLongitude();//Residence Longitude
        }

        if (Lati != 0 && Longi != 0)
            GPSLoc = Lati + "," + Longi;
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

    private String chkval(String Val) {
        if (Val == null) {
            Val = "";
        }
        return Val.trim();
    }


    private void AlertDisplay(String head, String body, final boolean flag) {
        AlertDialog ad = new AlertDialog.Builder(this).create();
        ad.setTitle(Html.fromHtml("<font color='#E3256B'>" + head + "</font>"));
        ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>" + body + "</font>"));
        ad.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (flag)
                    GoBack();
                else
                    dialog.dismiss();

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
        intent.putExtra("ComeFrom", "1");
        intent.putExtra("Clt_LogID", LogId);
        intent.putExtra("Clt_ClubName", ClubName);
        intent.putExtra("UserClubName", ClientId);
        intent.putExtra("AppLogo", AppLogo);
        startActivity(intent);
        finish();
    }
}
