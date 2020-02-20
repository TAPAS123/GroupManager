package group.manager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import org.w3c.dom.Text;

public class Committee extends Activity {

    Intent menuIntent;
    Context context = this;
    byte[] AppLogo;
    ListView Lv;
    String Log, logid, ClubName, Str_user, MTitle, Table2Name, Table4Name;
    int PNo;
    TextView txtHead;
    List<RowEnvt> rowItems;
    RowEnvt item;
    String ItemName = "", PgName, CCBYear = "",UserType="";
    Button btnClubInfo;
    SQLiteDatabase dbObj;
    ProgressDialog Progsdial;
    private boolean InternetPresent;
    //boolean HasSingleData=false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.committee_list);

        Lv = (ListView) findViewById(R.id.LV1);
        txtHead = (TextView) findViewById(R.id.txthead);
        btnClubInfo = (Button) findViewById(R.id.btnClubInfo);/// Added on 08-02-2019

        menuIntent = getIntent();
        Log = menuIntent.getStringExtra("Clt_Log");
        logid = menuIntent.getStringExtra("Clt_LogID");
        ClubName = menuIntent.getStringExtra("Clt_ClubName");
        Str_user = menuIntent.getStringExtra("UserClubName");
        AppLogo = menuIntent.getByteArrayExtra("AppLogo");
        PgName = menuIntent.getStringExtra("PgName");
        MTitle = menuIntent.getStringExtra("MTitle");
        PNo = menuIntent.getIntExtra("PNo", PNo);
        CCBYear = menuIntent.getStringExtra("CCBYear");//CCB Year Added 08-02-2017

        if (PNo != 1) {
            //HasSingleData=menuIntent.getBooleanExtra("HasSingleData",HasSingleData);
            ItemName = menuIntent.getStringExtra("ItemName");
        }

        Table2Name = "C_" + Str_user + "_2";
        Table4Name = "C_" + Str_user + "_4";

        Get_SharedPref_Values();// Get Stored Shared Pref Values of Login

        Set_App_Logo_Title(); // Set App Logo and Title

        txtHead.setText(MTitle);// Set Head Title

        Fill_ListView(ItemName);//Fill List View

        Lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String txtName = rowItems.get(position).getEvtName();
                Intent MainBtnIntent = null;
                if (PNo < 3) {
                    MainBtnIntent = new Intent(getBaseContext(), Committee.class);
                    MainBtnIntent.putExtra("PgName", PgName);
                    MainBtnIntent.putExtra("MTitle", MTitle);
                    MainBtnIntent.putExtra("PNo", PNo + 1);
                    //MainBtnIntent.putExtra("HasSingleData",false);
                    MainBtnIntent.putExtra("ItemName", txtName);
                    MainBtnIntent.putExtra("CCBYear", CCBYear);
                }
                else if(PNo==3 && Chk_Committe_ClubInfo().length() > 0)//this condition only for group Rotary Club Bareilly South ClientID--> RI31101920
                {
                    ///// Added on 19-02-2020 /// Check Commmittee ClubInfo Data (Recently used in group Rotary Club Bareilly South ClientID--> RI31101920)
                    String t1 = rowItems.get(position).getEvtName();
                    txtName = rowItems.get(position).getEvtDesc();

                    if(t1.contains("Club Officers")){
                        MainBtnIntent = new Intent(getBaseContext(), Committee.class);
                        MainBtnIntent.putExtra("PgName", PgName);
                        MainBtnIntent.putExtra("MTitle", MTitle);
                        MainBtnIntent.putExtra("PNo", PNo + 1);
                        //MainBtnIntent.putExtra("HasSingleData",false);
                        MainBtnIntent.putExtra("ItemName", txtName);
                        MainBtnIntent.putExtra("CCBYear", CCBYear);
                    }
                    else if(t1.contains("Committees")){
                        Show_ClubInfo_Dialog1();
                        return;
                    }
                    else if(t1.contains("Club Programs")){
                        Show_Dialog_ClubProg1();
                        return;
                    }
                    else if(t1.contains("Club Information")){
                        Show_ClubInfo_Dialog();
                        return;
                    }
                    ////////////////////////////////////
                }
                else {
                    if (PgName.equals("ICAI_QRY"))// For Distinguised Members
                    {
                        String Qry = rowItems.get(position).getEvtDesc();
                        String SpCond = "DIR_NEW##" + Qry;// Make Special Qry Parameter with DIR_NEW

                        DbHandler dbhandlerObj = new DbHandler(context, Table2Name);
                        dbhandlerObj.Chk_Filter_SavedPermanent(Table4Name);// Saved Filter Permanent Or Temporary
                        String Special_Dir_Condition = dbhandlerObj.Special_Dir_Condition(SpCond, Table4Name, "");
                        dbhandlerObj.close();//Close DbHandler
                        ///////////////////////////////////////////

                        MainBtnIntent = new Intent(getBaseContext(), SwipeScreen.class); // Directory Activity
                        MainBtnIntent.putExtra("Special_Dir_Condition", Special_Dir_Condition);
                        MainBtnIntent.putExtra("CFrom", "OTHER");//Comes From Other Than Menu Page
                    } else {
                        MainBtnIntent = new Intent(getBaseContext(), Directory_SingleRecord.class);
                        MainBtnIntent.putExtra("Mid", rowItems.get(position).getMid());
                    }
                }
                MainBtnIntent.putExtra("Clt_Log", Log);
                MainBtnIntent.putExtra("Clt_LogID", logid);
                MainBtnIntent.putExtra("Clt_ClubName", ClubName);
                MainBtnIntent.putExtra("UserClubName", Str_user);
                MainBtnIntent.putExtra("AppLogo", AppLogo);
                startActivity(MainBtnIntent);
                //finish();
            }
        });


        //ADDED on 08-03-2019 (ICAI_CPE!Clubs) which is used in PNo==3 (Recently used in group Rotary Club Bareilly South ClientID--> RI31101920)
        btnClubInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Show_ClubInfo_Dialog();
            }
        });

    }


    //Get Data from Saved Shared Preference
    private void Get_SharedPref_Values()
    {
        SharedPreferences ShPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        if (ShPref.contains("UserType"))
        {
            UserType=ShPref.getString("UserType", "");
        }
    }


    //Fill List View With Records
    private void Fill_ListView(String ItemName) {
        try {
            String Text1 = "", Text3 = "", Qry = "";

            if (PgName.equals("ICAI_COMM"))
                Text1 = "Committee";// For Committee Menu
            else if (PgName.equals("ICAI_BRANCH"))
                Text1 = "Branch";// For Branches Menu
            else if (PgName.equals("ICAI_PP"))
                Text1 = "PP";// For Past President/Secretory Menu
            else if (PgName.equals("ICAI_CPE"))
                Text1 = "CPE";// For CPE Bodies
            else if (PgName.equals("ICAI_QRY"))
                Text1 = "DISQRY";// For Distinguised Members
            else if (PgName.equals("ICAI_MULCOMM"))
                Text1 = "MULCOMM";// For Multi Committee
            else if (PgName.contains("ICAI_MULCOMM1"))
                Text1 = PgName.replace("ICAI_MULCOMM1_", "");// For Multi Committee all or any type added on 19-04-2017

            String AddAND = "";
            if (CCBYear.length() > 0 && (Text1.equals("Committee") || Text1.equals("Branch")))
                AddAND = " AND Date1_1=" + CCBYear;//Added on 08-02-2017

            if (PNo == 1) {
                Qry = "SELECT distinct Text3 from " + Table4Name + " Where Rtype='ICAI' AND Text1='" + Text1 + "' " + AddAND + " Order By Text3";
            } else if (PNo == 2) {
                if (Text1.equals("Committee"))
                    txtHead.setText(ItemName);
                Qry = "SELECT distinct Text2,Text7 from " + Table4Name + " Where Rtype='ICAI' AND Text1='" + Text1 + "' AND Text3='" + ItemName.trim() + "' " + AddAND + " Order By Num3,Text2";
            } else if (PNo == 3 || PNo == 4) {
                if (!Text1.equals("PP"))
                    txtHead.setText(ItemName);

                ///// updated on 19-02-2020 /// Check Commmittee ClubInfo Data (Recently used in group Rotary Club Bareilly South ClientID--> RI31101920)
                if (PNo == 3 && Chk_Committe_ClubInfo().length() > 0) {
                    //btnClubInfo.setVisibility(View.VISIBLE);
                    String[] Arr1={"1. Club Officers","2. Committees","3. Club Programs","4. Club Information"};

                    rowItems = new ArrayList<RowEnvt>();
                    for(int i=0; i<Arr1.length; i++) {
                        item = new RowEnvt(Arr1[i],ItemName);
                        rowItems.add(item);
                    }
                    Adapter_Committee adp1 = new Adapter_Committee(context, R.layout.committee_list_items, rowItems);
                    Lv.setAdapter(adp1);
                    GOTO_Chk_SingleDirectory();
                    return;
                }
                /////////////////////////////////////////////

                if (Text1.equals("DISQRY"))//For Distinguised Members
                    Qry = "SELECT Text1,Text2 from " + Table4Name + " Where Rtype='ICAI_QRY' Order By Num1";
                else
                    Qry = "SELECT (t2.C4_BG || \"\" || ifnull(t2.C4_DOB_Y,'')),t2.C4_Email,t1.Text4,t2.M_Id,t2.M_Pic from " + Table4Name + " as [t1] Inner Join " + Table2Name + " as [t2] ON t1.Num1=t2.M_Id AND t1.Rtype='ICAI' AND t1.Text1='" + Text1 + "' AND t1.Text2='" + ItemName.trim() + "' " + AddAND + " Order By t1.Num2";
            }

            dbObj = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
            Cursor cursorT = dbObj.rawQuery(Qry, null);

            if (cursorT.getCount() == 0) {
                txtHead.setText("No Records");
            } else {
                rowItems = new ArrayList<RowEnvt>();
                if (cursorT.moveToFirst()) {
                    do {
                        if (PNo >= 3) {
                            if (Text1.equals("DISQRY"))//For Distinguised Members
                            {
                                String text1 = ChkVal(cursorT.getString(0));
                                String text2 = ChkVal(cursorT.getString(1));
                                item = new RowEnvt(text1, text2);
                            } else {
                                String Desig = ChkVal(cursorT.getString(2));
                                if (Desig.length() > 0)
                                    Desig = ", " + Desig;
                                Text3 = ChkVal(cursorT.getString(0)) + " " + ChkVal(cursorT.getString(1)) + Desig;
                                int mid = cursorT.getInt(3);
                                byte[] M_Pic = cursorT.getBlob(4);
                                item = new RowEnvt(Text3.trim(), mid, M_Pic);
                            }
                        } else {
                            Text3 = ChkVal(cursorT.getString(0));
                            String txtColor = "";
                            if (PNo == 2)
                                txtColor = ChkVal(cursorT.getString(1));//txtColor is used to change color of the text

                            item = new RowEnvt(Text3, txtColor);
                        }
                        rowItems.add(item);
                    } while (cursorT.moveToNext());
                }
                Adapter_Committee adp1 = new Adapter_Committee(context, R.layout.committee_list_items, rowItems);
                Lv.setAdapter(adp1);
            }
            cursorT.close();

            dbObj.close();/// Close Connection

        } catch (Exception ex) {
            //String tt="";
        }
        GOTO_Chk_SingleDirectory();
    }


    //Direct GO to Single Directory Activity ON Some Condition
    private void GOTO_Chk_SingleDirectory() {
        if (rowItems != null) {
            if (rowItems.size() == 1) {
                Intent MainBtnIntent = null;
                if (PNo == 3) {
                    MainBtnIntent = new Intent(getBaseContext(), Directory_SingleRecord.class);
                    MainBtnIntent.putExtra("Mid", rowItems.get(0).getMid());
                } else {
                    String txtName = rowItems.get(0).getEvtName();
                    MainBtnIntent = new Intent(getBaseContext(), Committee.class);
                    MainBtnIntent.putExtra("PgName", PgName);
                    MainBtnIntent.putExtra("MTitle", MTitle);
                    MainBtnIntent.putExtra("PNo", PNo + 1);
                    //MainBtnIntent.putExtra("HasSingleData",true);//Here Only Single Data so its True
                    MainBtnIntent.putExtra("ItemName", txtName);
                    MainBtnIntent.putExtra("CCBYear", CCBYear);
                }
                MainBtnIntent.putExtra("Clt_Log", Log);
                MainBtnIntent.putExtra("Clt_LogID", logid);
                MainBtnIntent.putExtra("Clt_ClubName", ClubName);
                MainBtnIntent.putExtra("UserClubName", Str_user);
                MainBtnIntent.putExtra("AppLogo", AppLogo);
                startActivity(MainBtnIntent);
                finish();
            }
        }
    }


    private String ChkVal(String Val) {
        if (Val == null)
            Val = "";
        else
            Val = Val.trim();
        return Val;
    }


    ////Check ClubInfo (ICAI_CPE!Clubs) ADDED on 08-03-2019  which is used in PNo==3 (Recently used in group Rotary Club Bareilly South ClientID--> RI31101920)
    private String Chk_Committe_ClubInfo()
    {
        dbObj = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        String Val="";
        String sql = "Select M_ID from " + Table4Name + " Where Rtype='COMM_CLUBINFO'";
        Cursor cursorT = dbObj.rawQuery(sql, null);
        if (cursorT.moveToFirst()) {
            Val = ChkVal(cursorT.getString(0));
        }
        cursorT.close();
        dbObj.close();

        return Val.trim();
    }

    ////Get ClubInfo Data (ICAI_CPE!Clubs) ADDED on 08-03-2019  which is used in PNo==3 (Recently used in group Rotary Club Bareilly South ClientID--> RI31101920)
    private String Get_Committe_ClubInfo()
    {
        String Val="";
        dbObj = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        String sql = "Select Text2 from " + Table4Name + " Where Rtype='COMM_CINFO' AND Text1='"+ItemName.trim()+"'";
        Cursor cursorT = dbObj.rawQuery(sql, null);
        if (cursorT.moveToFirst()) {
            Val = ChkVal(cursorT.getString(0));
        }
        cursorT.close();
        dbObj.close();//Close Databse

        return Val.trim();
    }


    // ADDED on 19-02-2020 (ICAI_CPE!Clubs) which is used in PNo==3 (Recently used in group Rotary Club Bareilly South ClientID--> RI31101920)
    private void Show_Dialog_ClubProg1()
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
        dialog.setContentView(R.layout.commitee_club_prog1);
        //dialog.setCancelable(false);
        dialog.show();

        final TextView txtHead=(TextView) dialog.findViewById(R.id.txtHead);
        final ListView LV1=(ListView) dialog.findViewById(R.id.LV1);
        Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);

        txtHead.setText("Club Programs\n("+ItemName+")");

        if(!UserType.equalsIgnoreCase("ADMIN")){///UPDATE
            //ed_Trea_Name.setEnabled(false);/// Disabled EditText
            //btnAdd.setText("CLOSE");
        }

        /*String Data=Get_Committe_ClubInfo();///Get ClubInfo
        if(Data.contains("#")){
            Data=Data+" ";
            String[] Arr1=Data.split("#");
            txtNumberOfMem.setText(Arr1[0].trim());//Set Number Of Members
            txt_installation_dt.setText(Arr1[1].trim());//Set Installation date
            txt_governer_dt.setText(Arr1[2].trim());//Set Governors Visit date
            txt_international_dt_1.setText(Arr1[3].trim());//Set International Dues Paid date 1
            txt_international_dt_2.setText(Arr1[4].trim());//Set International Dues Paid date 2
            txt_district_dues_dt.setText(Arr1[5].trim());//Set District Dues Paid date
            txt_rotary_news_dt.setText(Arr1[6].trim());//Set Rotery News Trust Paid date
        }*/


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if(UserType.equalsIgnoreCase("ADMIN"))///UPDATE
                {
                    /*String t1=txtNumberOfMem.getText().toString().trim();
                    String t2=txt_installation_dt.getText().toString().trim();
                    String t3=txt_governer_dt.getText().toString().trim();
                    String t4=txt_international_dt_1.getText().toString().trim();
                    String t5=txt_international_dt_2.getText().toString().trim();
                    String t6=txt_district_dues_dt.getText().toString().trim();
                    String t7=txt_rotary_news_dt.getText().toString().trim();

                    String RData=t1+"#"+t2+"#"+t3+"#"+t4+"#"+t5+"#"+t6+"#"+t7+"";

                    Chkconnection chkconn=new Chkconnection();//Intialise Chkconnection Object
                    InternetPresent =chkconn.isConnectingToInternet(context);
                    if(InternetPresent==true)
                    {
                        Update_ClubInfo_Server(ItemName,RData);
                    }
                    else {
                        AlertDisplay("Internet Connection","No Internet Connection !");
                    }*/
                }

                Show_Dialog_ClubProg2();

            }
        });
    }

    // ADDED on 19-02-2020 (ICAI_CPE!Clubs) which is used in PNo==3 (Recently used in group Rotary Club Bareilly South ClientID--> RI31101920)
    private void Show_Dialog_ClubProg2()
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
        dialog.setContentView(R.layout.commitee_club_prog2);
        //dialog.setCancelable(false);
        dialog.show();

        final TextView txtHead=(TextView) dialog.findViewById(R.id.txtHead);
        final TextView txt_Date=(TextView) dialog.findViewById(R.id.txt_Date);
        final EditText ed_Title=(EditText) dialog.findViewById(R.id.ed_Title);
        final EditText ed_Desc=(EditText) dialog.findViewById(R.id.ed_Desc);
        Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);

        txtHead.setText("Club Programs\n("+ItemName+")");

        if(!UserType.equalsIgnoreCase("ADMIN")){///UPDATE
            //ed_Title.setEnabled(false);/// Disabled EditText
            //ed_Desc.setEnabled(false);/// Disabled EditText
            btnUpdate.setText("CLOSE");
        }

        /*String Data=Get_Committe_ClubInfo();///Get ClubInfo
        if(Data.contains("#")){
            Data=Data+" ";
            String[] Arr1=Data.split("#");
            txtNumberOfMem.setText(Arr1[0].trim());//Set Number Of Members
            txt_installation_dt.setText(Arr1[1].trim());//Set Installation date
            txt_governer_dt.setText(Arr1[2].trim());//Set Governors Visit date
            txt_international_dt_1.setText(Arr1[3].trim());//Set International Dues Paid date 1
            txt_international_dt_2.setText(Arr1[4].trim());//Set International Dues Paid date 2
            txt_district_dues_dt.setText(Arr1[5].trim());//Set District Dues Paid date
            txt_rotary_news_dt.setText(Arr1[6].trim());//Set Rotery News Trust Paid date
        }*/


        txt_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(UserType.equalsIgnoreCase("ADMIN"))///UPDATE
                    Show_Date_Dialog(txt_Date);//Set Date
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if(UserType.equalsIgnoreCase("ADMIN"))///UPDATE
                {
                    String t1=txt_Date.getText().toString().trim();
                    String t2=ed_Title.getText().toString().trim();
                    String t3=ed_Desc.getText().toString().trim();

                    String RData=t1+"#"+t2+"#"+t3+"";

                    /*Chkconnection chkconn=new Chkconnection();//Intialise Chkconnection Object
                    InternetPresent =chkconn.isConnectingToInternet(context);
                    if(InternetPresent==true)
                    {
                        Update_ClubInfo_Server(ItemName,RData);
                    }
                    else {
                        AlertDisplay("Internet Connection","No Internet Connection !");
                    }*/
                }

            }
        });
    }

    // ADDED on 19-02-2020 (ICAI_CPE!Clubs) which is used in PNo==3 (Recently used in group Rotary Club Bareilly South ClientID--> RI31101920)
    private void Show_ClubInfo_Dialog1()
    {
        // Display Popup Screen of ClubInformation Comminttee only for RI31101920 club (Editable Page)
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
        dialog.setContentView(R.layout.commitee_club_info1);
        //dialog.setCancelable(false);
        dialog.show();

        final TextView txtHead=(TextView) dialog.findViewById(R.id.txtHead);
        final EditText ed_Trea_Name=(EditText) dialog.findViewById(R.id.ed_Trea_Name);
        final EditText ed_Trea_Mob=(EditText) dialog.findViewById(R.id.ed_Trea_Mob);
        final EditText ed_Lit_Name=(EditText) dialog.findViewById(R.id.ed_Lit_Name);
        final EditText ed_Lit_Mob=(EditText) dialog.findViewById(R.id.ed_Lit_Mob);
        final EditText ed_Found_Name=(EditText) dialog.findViewById(R.id.ed_Found_Name);
        final EditText ed_Found_Mob=(EditText) dialog.findViewById(R.id.ed_Found_Mob);
        final EditText ed_Memship_Name=(EditText) dialog.findViewById(R.id.ed_Memship_Name);
        final EditText ed_Memship_Mob=(EditText) dialog.findViewById(R.id.ed_Memship_Mob);
        final EditText ed_PR_Name=(EditText) dialog.findViewById(R.id.ed_PR_Name);
        final EditText ed_PR_Mob=(EditText) dialog.findViewById(R.id.ed_PR_Mob);
        final EditText ed_Grant_Name=(EditText) dialog.findViewById(R.id.ed_Grant_Name);
        final EditText ed_Grant_Mob=(EditText) dialog.findViewById(R.id.ed_Grant_Mob);
        final EditText ed_Wash_Name=(EditText) dialog.findViewById(R.id.ed_Wash_Name);
        final EditText ed_Wash_Mob=(EditText) dialog.findViewById(R.id.ed_Wash_Mob);

        Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);

        txtHead.setText("Committees\n("+ItemName+")");

        if(!UserType.equalsIgnoreCase("ADMIN")){///UPDATE
            //ed_Trea_Name.setEnabled(false);/// Disabled EditText
            //ed_Trea_Mob.setEnabled(false);/// Disabled EditText
            //ed_Lit_Name.setEnabled(false);/// Disabled EditText
            //ed_Lit_Mob.setEnabled(false);/// Disabled EditText
            //ed_Found_Name.setEnabled(false);/// Disabled EditText
            //ed_Found_Mob.setEnabled(false);/// Disabled EditText
            //ed_Memship_Name.setEnabled(false);/// Disabled EditText
            //ed_Memship_Mob.setEnabled(false);/// Disabled EditText
            //ed_PR_Name.setEnabled(false);/// Disabled EditText
            //ed_PR_Mob.setEnabled(false);/// Disabled EditText
            //ed_Grant_Name.setEnabled(false);/// Disabled EditText
            //ed_Grant_Mob.setEnabled(false);/// Disabled EditText
            //ed_Wash_Name.setEnabled(false);/// Disabled EditText
            //ed_Wash_Mob.setEnabled(false);/// Disabled EditText
            btnUpdate.setText("CLOSE");
        }

        /*String Data=Get_Committe_ClubInfo();///Get ClubInfo
        if(Data.contains("#")){
            Data=Data+" ";
            String[] Arr1=Data.split("#");
            txtNumberOfMem.setText(Arr1[0].trim());//Set Number Of Members
            txt_installation_dt.setText(Arr1[1].trim());//Set Installation date
            txt_governer_dt.setText(Arr1[2].trim());//Set Governors Visit date
            txt_international_dt_1.setText(Arr1[3].trim());//Set International Dues Paid date 1
            txt_international_dt_2.setText(Arr1[4].trim());//Set International Dues Paid date 2
            txt_district_dues_dt.setText(Arr1[5].trim());//Set District Dues Paid date
            txt_rotary_news_dt.setText(Arr1[6].trim());//Set Rotery News Trust Paid date
        }*/


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if(UserType.equalsIgnoreCase("ADMIN"))///UPDATE
                {
                    String Trea_Name=ed_Trea_Name.getText().toString().trim();
                    String Trea_Mob=ed_Trea_Mob.getText().toString().trim();
                    String Lit_Name=ed_Lit_Name.getText().toString().trim();
                    String Lit_Mob=ed_Lit_Mob.getText().toString().trim();
                    String Found_Name=ed_Found_Name.getText().toString().trim();
                    String Found_Mob=ed_Found_Mob.getText().toString().trim();
                    String Memship_Name=ed_Memship_Name.getText().toString().trim();
                    String Memship_Mob=ed_Memship_Mob.getText().toString().trim();
                    String PR_Name=ed_PR_Name.getText().toString().trim();
                    String PR_Mob=ed_PR_Mob.getText().toString().trim();
                    String Grant_Name=ed_Grant_Name.getText().toString().trim();
                    String Grant_Mob=ed_Grant_Mob.getText().toString().trim();
                    String Wash_Name=ed_Wash_Name.getText().toString().trim();
                    String Wash_Mob=ed_Wash_Mob.getText().toString().trim();

                    String RData=Trea_Name+"#"+Trea_Mob+"#"+Lit_Name+"#"+Lit_Mob+"#"+Found_Name+"#"+Found_Mob+"#"+
                            Memship_Name+"";

                    /*Chkconnection chkconn=new Chkconnection();//Intialise Chkconnection Object
                    InternetPresent =chkconn.isConnectingToInternet(context);
                    if(InternetPresent==true)
                    {
                        Update_ClubInfo_Server(ItemName,RData);
                    }
                    else {
                        AlertDisplay("Internet Connection","No Internet Connection !");
                    }*/
                }
            }
        });
    }


    // ADDED on 08-03-2019 (ICAI_CPE!Clubs) which is used in PNo==3 (Recently used in group Rotary Club Bareilly South ClientID--> RI31101920)
    private void Show_ClubInfo_Dialog()
    {
        // Display Popup Screen of ClubInformation Editable Page
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
        dialog.setContentView(R.layout.commitee_club_info);
        //dialog.setCancelable(false);
        dialog.show();

        final TextView txtHead=(TextView) dialog.findViewById(R.id.txtHead);
        final EditText txtNumberOfMem=(EditText) dialog.findViewById(R.id.ed_member_no);
        final TextView txt_installation_dt=(TextView) dialog.findViewById(R.id.txt_installation_dt);
        final TextView txt_governer_dt=(TextView) dialog.findViewById(R.id.txt_governer_dt);
        final TextView txt_international_dt_1=(TextView) dialog.findViewById(R.id.txt_international_dt_1);
        final TextView txt_international_dt_2=(TextView) dialog.findViewById(R.id.txt_international_dt_2);
        final TextView txt_district_dues_dt=(TextView) dialog.findViewById(R.id.txt_district_dues_dt);
        final TextView txt_rotary_news_dt=(TextView) dialog.findViewById(R.id.txt_rotary_news_dt);
        Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);

        txtHead.setText("Club Information\n("+ItemName+")");

        if(!UserType.equalsIgnoreCase("ADMIN")){///UPDATE
            txtNumberOfMem.setEnabled(false);/// Disabled EditText
            btnUpdate.setText("CLOSE");
        }

        String Data=Get_Committe_ClubInfo();///Get ClubInfo
        if(Data.contains("#")){
            Data=Data+" ";
            String[] Arr1=Data.split("#");
            txtNumberOfMem.setText(Arr1[0].trim());//Set Number Of Members
            txt_installation_dt.setText(Arr1[1].trim());//Set Installation date
            txt_governer_dt.setText(Arr1[2].trim());//Set Governors Visit date
            txt_international_dt_1.setText(Arr1[3].trim());//Set International Dues Paid date 1
            txt_international_dt_2.setText(Arr1[4].trim());//Set International Dues Paid date 2
            txt_district_dues_dt.setText(Arr1[5].trim());//Set District Dues Paid date
            txt_rotary_news_dt.setText(Arr1[6].trim());//Set Rotery News Trust Paid date
        }


        txt_installation_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserType.equalsIgnoreCase("ADMIN"))///UPDATE
                    Show_Date_Dialog(txt_installation_dt);//Set Date
            }
        });

        txt_governer_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserType.equalsIgnoreCase("ADMIN"))///UPDATE
                    Show_Date_Dialog(txt_governer_dt);//Set Date
            }
        });

        txt_international_dt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserType.equalsIgnoreCase("ADMIN"))///UPDATE
                    Show_Date_Dialog(txt_international_dt_1);//Set Date
            }
        });

        txt_international_dt_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserType.equalsIgnoreCase("ADMIN"))///UPDATE
                    Show_Date_Dialog(txt_international_dt_2);//Set Date
            }
        });

        txt_district_dues_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserType.equalsIgnoreCase("ADMIN"))///UPDATE
                    Show_Date_Dialog(txt_district_dues_dt);//Set Date
            }
        });

        txt_rotary_news_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserType.equalsIgnoreCase("ADMIN"))///UPDATE
                    Show_Date_Dialog(txt_rotary_news_dt);//Set Date
            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if(UserType.equalsIgnoreCase("ADMIN"))///UPDATE
                {
                    String t1=txtNumberOfMem.getText().toString().trim();
                    String t2=txt_installation_dt.getText().toString().trim();
                    String t3=txt_governer_dt.getText().toString().trim();
                    String t4=txt_international_dt_1.getText().toString().trim();
                    String t5=txt_international_dt_2.getText().toString().trim();
                    String t6=txt_district_dues_dt.getText().toString().trim();
                    String t7=txt_rotary_news_dt.getText().toString().trim();

                    String RData=t1+"#"+t2+"#"+t3+"#"+t4+"#"+t5+"#"+t6+"#"+t7+"";

                    Chkconnection chkconn=new Chkconnection();//Intialise Chkconnection Object
                    InternetPresent =chkconn.isConnectingToInternet(context);
                    if(InternetPresent==true)
                    {
                        Update_ClubInfo_Server(ItemName,RData);
                    }
                    else {
                        AlertDisplay("Internet Connection","No Internet Connection !");
                    }
                }

            }
        });
    }


    /// Added on 08-03-2019
    private void Show_Date_Dialog(final TextView txt1)
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        txt1.setText(convertDateToString(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));

                    }
                }, year, month, day);
        datePickerDialog.show();
    }


    private String convertDateToString(String str_dt) {
        Date d = convertStringToDate(str_dt);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String str_date = formatter.format(d);
        return str_date;
    }


    private Date convertStringToDate(String str_date) {
        DateFormat formatter;
        Date date = null;
        try {
            formatter = new SimpleDateFormat("dd-MM-yyyy");
            date = formatter.parse(str_date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    //Update ClubInfo from Mobile to sever (Addec on 08-03-2019)
    public void Update_ClubInfo_Server(final String TType,final String TVal)
    {
        progressdial();
        Thread T2 = new Thread() {
            @Override
            public void run() {
                try {
                    WebServiceCall webcall=new WebServiceCall();
                    final String WebResult=webcall.Committee_ClubInfo(Str_user,TType,TVal);

                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            if(WebResult.contains("Saved"))
                            {
                                AlertDisplay("Result","Updated Successfully !");
                            }
                            else{
                                AlertDisplay("","Something went wrong !");
                            }
                        }
                    });
                }
                catch (Exception e) {
                    //System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                Progsdial.dismiss();
            }
        };
        T2.start();
    }


    protected void progressdial()
    {
        Progsdial = new ProgressDialog(this, R.style.MyTheme);
        Progsdial.setMessage("Please Wait....");
        Progsdial.setIndeterminate(true);
        Progsdial.setCancelable(false);
        Progsdial.getWindow().setGravity(Gravity.DISPLAY_CLIP_VERTICAL);
        Progsdial.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Progsdial.show();
    }



    private void AlertDisplay(String head,String body){
        AlertDialog ad=new AlertDialog.Builder(this).create();
        ad.setTitle( Html.fromHtml("<font color='#E3256B'>"+head+"</font>"));
        ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>"+body+"</font>"));
        ad.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.show();
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backs();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void backs() {
		/*Intent MainBtnIntent=null;
		if(PNo==1 || HasSingleData || PgName.equals("ICAI_PP") || PgName.equals("ICAI_QRY")){
			MainBtnIntent= new Intent(context,MenuPage.class);
			MainBtnIntent.putExtra("AppLogo", AppLogo);
			startActivity(MainBtnIntent);
		}*/
        finish();
    }

}
