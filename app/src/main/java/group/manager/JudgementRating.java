package group.manager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import group.manager.AdapterClasses.AdapterJudgementRating;

public class JudgementRating extends Activity {
    TextView txtHead, txtTotal;
    ListView LV1;
    Button btnSave;
    String selectqry = "", option, range, dbtotal = "", marks, participantName, OptionMid, ClubName,
            ClientId, LogId, MTitle;
    int CriteriaMid, Sno, sum = 0, flag = 0, Mid = 0;
    byte[] AppLogo;
    Context context = this;
    ArrayList<JudgementModel> arraylist;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judgement_rating);

        txtHead = (TextView) findViewById(R.id.txtHead);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        LV1 = (ListView) findViewById(R.id.LV1);
        btnSave = (Button) findViewById(R.id.btnSave);

        Intent intent = getIntent();
        Mid = intent.getIntExtra("Mid", 0);
        MTitle = intent.getStringExtra("MTitle");
        LogId = intent.getStringExtra("Clt_LogID");
        ClubName = intent.getStringExtra("Clt_ClubName");
        ClientId = intent.getStringExtra("UserClubName");
        AppLogo = intent.getByteArrayExtra("AppLogo");
        OptionMid = intent.getStringExtra("OptionMid");
        participantName = intent.getStringExtra("Participant");

        arraylist = new ArrayList<JudgementModel>();

        txtHead.setText(participantName);
        Typeface face=Typeface.createFromAsset(getAssets(), "calibri.ttf");
        txtHead.setTypeface(face);

        db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        selectqry = "Select M_ID,SNO, Question, Ans1,(Select User_Ans from c_" + ClientId + "_op3 where OP1_ID = c_" + ClientId + "_op2.op1_id AND op2_id = c_" + ClientId + "_op2.m_id AND remark='" + OptionMid + "' ) AS [Marks] from C_" + ClientId + "_OP2 where OP1_ID=" + Mid + " AND Ans2='Criteria'";
        //  selectqry = "Select M_ID,SNO, Question,Ans1 from C_" + Str_club + "_OP2 where OP1_ID = " + 1069 + " AND Ans2 ='Criteria'";
        Cursor cursor = db.rawQuery(selectqry, null);
        if (cursor.moveToFirst()) {
            do {
                CriteriaMid = cursor.getInt(0);
                Sno = cursor.getInt(1);
                option = chkval(cursor.getString(2));
                range = chkval(cursor.getString(3));
                marks = chkval(cursor.getString(4));
                arraylist.add(new JudgementModel(CriteriaMid, Sno, option, range, marks));
            } while (cursor.moveToNext());
            LV1.setAdapter(new AdapterJudgementRating(this, R.layout.rowitem_judgement_rating, arraylist, txtTotal));
        }
        cursor.close();

        selectqry = "Select user_ans from c_" + ClientId + "_op3 where OP1_ID = " + Mid + " AND op2_id=0 AND remark='" + OptionMid + "'";
        cursor = db.rawQuery(selectqry, null);
        if (cursor.moveToFirst()) {
            String total = cursor.getString(0);
            // int tot = Integer.valueOf(total) + 31;
            txtTotal.setText("" + total);
        } else {
            txtTotal.setText("0");
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Boolean flag = chkmarks();
                Save_Record();
            }
        });
    }

    public void Save_Record() {
        View view;
        EditText et;
        String criteriaMid, marks = "", dbmarks;
        flag = 0;
        // save participants marks
        int listLength = LV1.getChildCount();
        String[] ArrMarks = new String[listLength];

        for (int i = 0; i < listLength; i++) {
            view = LV1.getChildAt(i);
            et = (EditText) view.findViewById(R.id.edRating);
            marks = et.getText().toString(); // get marks from edittext
            if (marks.length() != 0) {
                if (Integer.valueOf(marks) > 5 || Integer.valueOf(marks) < 1) {
                    flag = 1;
                    Toast.makeText(context, "Range Exceed!", Toast.LENGTH_SHORT).show();
                }
            }

            ArrMarks[i] = marks;
        }

        if (flag == 0) {
            for (int i = 0; i < ArrMarks.length; i++) {
                marks = ArrMarks[i];
                criteriaMid = String.valueOf(arraylist.get(i).CriteriaMid);  //get criteria mid
                String selectqry = "Select User_Ans from C_" + ClientId + "_OP3 where OP1_ID = " + Mid + " AND OP2_ID = " + Integer.parseInt(criteriaMid) + " AND Remark = '" + OptionMid + "'";
                Cursor cursor = db.rawQuery(selectqry, null);
                if (cursor.moveToFirst()) {
                    dbmarks = chkval(cursor.getString(0));
                    if (!dbmarks.equals(marks)) {
                        String updateqry = "update C_" + ClientId + "_OP3 set User_Ans = '" + marks + "' where OP1_ID = " + Mid + " AND OP2_ID = " + Integer.valueOf(criteriaMid) + " AND Remark ='" + OptionMid + "'";
                        db.execSQL(updateqry);
                    }
                } else {
                    String insrtqry = "insert into C_" + ClientId + "_OP3  (OP1_ID,OP2_ID,User_Ans,Remark) values (" + Mid + "," + Integer.parseInt(criteriaMid) + ",'" + marks + "'," + OptionMid + ")";
                    db.execSQL(insrtqry);
                }
            }

            String total = txtTotal.getText().toString();
            String selectqry = "Select User_Ans from C_" + ClientId + "_OP3 where OP1_ID = " + Mid + " AND OP2_ID =0 AND Remark ='" + OptionMid + "'";
            Cursor cursor = db.rawQuery(selectqry, null);
            if (cursor.moveToFirst()) {
                dbtotal = chkval(cursor.getString(0));
                if (!dbtotal.equals(total)) {
                    String updateqry = "update C_" + ClientId + "_OP3 set User_Ans = '" + total + "' where OP1_ID = " + Mid + " AND OP2_ID =0 AND Remark ='" + OptionMid + "'";
                    db.execSQL(updateqry);
                }
            } else {
                String insrtqry = "insert into C_" + ClientId + "_OP3(OP1_ID,OP2_ID,User_Ans,Remark) values (" + Mid + ",0,'" + total + "'," + OptionMid + ")";
                db.execSQL(insrtqry);
            }
            Toast.makeText(context, "Saved !", Toast.LENGTH_SHORT).show();
            GoBack();
        }
    }


    private String chkval(String Val) {
        if (Val == null) {
            Val = "";
        }
        return Val.trim();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            int flag = 0;
            View view;
            EditText et;
            String criteriaMid, marks, dbmarks;
            int listLength = LV1.getChildCount();
            for (int i = 0; i < listLength; i++) {
                view = LV1.getChildAt(i);
                et = (EditText) view.findViewById(R.id.edRating);   //
                marks = et.getText().toString();                                // get marks from edittext in list
                criteriaMid = String.valueOf(arraylist.get(i).CriteriaMid);  //get criteria mid
                String selectqry = "Select User_Ans from C_" + ClientId + "_OP3 where OP1_ID = " + Mid + " AND OP2_ID = " + Integer.parseInt(criteriaMid) + " AND Remark = '" + OptionMid + "'";
                Cursor cursor = db.rawQuery(selectqry, null);
                if (cursor.moveToFirst()) {
                    dbmarks = chkval(cursor.getString(0));
                    if (!dbmarks.equals(marks)) {
                        flag = 1;
                        break;
                    }
                }
            }
            if (flag == 1) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Do you want to save changes");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Save_Record();
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        GoBack();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else {
                GoBack();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void GoBack() {
        Intent intent = new Intent(context, Judgement.class);
        intent.putExtra("Mid", Mid);
        intent.putExtra("MTitle", MTitle);
        intent.putExtra("Clt_LogID", LogId);
        intent.putExtra("Clt_ClubName", ClubName);
        intent.putExtra("UserClubName", ClientId);
        intent.putExtra("AppLogo", AppLogo);
        startActivity(intent);
        finish();
    }
}
