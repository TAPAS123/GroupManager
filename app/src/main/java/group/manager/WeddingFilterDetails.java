package group.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

public class WeddingFilterDetails extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener {
    Dialog dlg;
    Context context = this;
    ListView lv;
    TextView tvspeduaction, tvspdiet, tvspmanglik, tvspannualincome, tvspwork, tvspheightfeet, tvspheightinch;
    String type;
    EditText edtgotra;
    Button btnsubmit, btnok,btnclear;
    SQLiteDatabase db;
    String[] arredu = {"Bachelors", "Masters", "Doctorate", "Diploma", "Postgraduate", "Undergraduate", "Associates degree", "Honours degree", "Trade school", "High school", "Less than high school"};
    String[] arrdiet = {"Vegetarian", "Non Vegetarian", "Occassionally Non Vegetarian", "Eggetarian", "Jain", "Vegan"};
    String[] arrmanglik = {"Non Manglik", "Manglik", "Angshik (Partial Manglik)"};
    String[] arrannualIncome = {"Less than 1,00,000", "1,00,000 to 5,00,000", "5,00,000 to 10,00,000", "10,00,000 to 20,00,000", "More than 20,00,000"};
    String[] arrwork = {"Business / Self Employed", "Private Company", "Government / Public Sector", "Defence / Civil Services", "Not Working"};
    //String[] arrheightfeet = {"3", "4", "5", "6", "7"};
    //String[] arrheightinch = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};
    public static int[] mid;
    String ClientID, ClubName, TableFamilyName;
    byte[] AppLogo;
    LinearLayout linearedu, lineardiet, linearmanglik, linearheight, linearincome, linearwork;
    View view1;
    String selected = "";
    double fromcm= 0.0;
    double tocm = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wedding_filter_details);

        Intent intent = getIntent();
        type = intent.getStringExtra("Type");
        ClientID = intent.getStringExtra("UserClubName");
        ClubName = intent.getStringExtra("Clt_ClubName");
        AppLogo =  intent.getByteArrayExtra("AppLogo");

        TableFamilyName = "C_" + ClientID + "_Family";

        Set_App_Logo_Title();// Set App Logo and Title
        
        linearedu = (LinearLayout) findViewById(R.id.linearLayout2);
        linearedu.setOnClickListener(this);
        lineardiet = (LinearLayout) findViewById(R.id.linearLayout3);
        lineardiet.setOnClickListener(this);
        linearmanglik = (LinearLayout) findViewById(R.id.linearLayout4);
        linearmanglik.setOnClickListener(this);
        linearheight = (LinearLayout) findViewById(R.id.linearLayout5);
        linearheight.setOnClickListener(this);
        linearincome = (LinearLayout) findViewById(R.id.linearLayout6);
        linearincome.setOnClickListener(this);
        linearwork = (LinearLayout) findViewById(R.id.linearLayout7);
        linearwork.setOnClickListener(this);

        tvspeduaction = (TextView) findViewById(R.id.tvspeduacation);
        tvspeduaction.setOnClickListener(this);
        tvspdiet = (TextView) findViewById(R.id.tvspdiet);
        tvspdiet.setOnClickListener(this);
        tvspmanglik = (TextView) findViewById(R.id.tvspmanglik);
        tvspmanglik.setOnClickListener(this);
        tvspannualincome = (TextView) findViewById(R.id.tvspannualIncome);
        tvspannualincome.setOnClickListener(this);
        tvspwork = (TextView) findViewById(R.id.tvspworkingwith);
        tvspwork.setOnClickListener(this);
        tvspheightfeet = (TextView) findViewById(R.id.tvspheightfeet);
        tvspheightfeet.setOnClickListener(this);
        // tvspheightinch = (TextView) findViewById(R.id.tvspheightinch);
        //  tvspheightinch.setOnClickListener(this);
        edtgotra = (EditText) findViewById(R.id.edtgotra);
        edtgotra.clearFocus();
        
        btnsubmit = (Button) findViewById(R.id.btnsubmit);
        btnsubmit.setOnClickListener(this);
        btnclear =(Button)findViewById(R.id.btnclear);
        btnclear.setOnClickListener(this);
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


    @Override
    public void onClick(View v) {
        if (v == linearedu) {
            createdg(arredu);
            
            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected = "";
                    int cntChoice = lv.getCount();
                    SparseBooleanArray sparseBooleanArray = lv.getCheckedItemPositions();
                    for (int i = 0; i < cntChoice; i++) {
                        if (sparseBooleanArray.get(i)) {
                            selected += lv.getItemAtPosition(i).toString() + ";";
                        }
                    }
                    dlg.dismiss();
                    if(!selected.equals("")) {
                        selected = selected.substring(0, selected.length() - 1);
                        tvspeduaction.setText("" + selected);
                        tvspeduaction.setTextColor(Color.parseColor("#3E88F2"));
                    }
                }
            });

        }
        else if (v == lineardiet) {
            createdg(arrdiet);
            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected = "";
                    int cntChoice = lv.getCount();
                    SparseBooleanArray sparseBooleanArray = lv.getCheckedItemPositions();
                    for (int i = 0; i < cntChoice; i++) {
                        if (sparseBooleanArray.get(i)) {
                            selected += lv.getItemAtPosition(i).toString() + ";";
                        }
                    }
                    dlg.dismiss();
                    if(!selected.equals("")) {
                        selected = selected.substring(0, selected.length() - 1);
                        tvspdiet.setText("" + selected);
                        tvspdiet.setTextColor(Color.parseColor("#3E88F2"));
                    }
                }
            });
        }
        else if (v == linearmanglik) {
            createdg(arrmanglik);
            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected = "";
                    int cntChoice = lv.getCount();
                    SparseBooleanArray sparseBooleanArray = lv.getCheckedItemPositions();
                    for (int i = 0; i < cntChoice; i++) {
                        if (sparseBooleanArray.get(i)) {
                            selected += lv.getItemAtPosition(i).toString() + ";";
                        }
                    }
                    dlg.dismiss();
                    if(!selected.equals("")) {
                        selected = selected.substring(0, selected.length() - 1);
                        tvspmanglik.setText("" + selected);
                        tvspmanglik.setTextColor(Color.parseColor("#3E88F2"));
                    }
                }
            });


        }
        else if (v == linearheight) {
            dlg = new Dialog(WeddingFilterDetails.this);

            dlg.setTitle("Select Height");
            LayoutInflater li1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view1 = li1.inflate(R.layout.dialog_height, null, false);
            final NumberPicker npfrom = (NumberPicker) view1.findViewById(R.id.numberPicker1);
            final NumberPicker npto = (NumberPicker) view1.findViewById(R.id.numberPicker2);
            Button btnheightok = (Button)  view1.findViewById(R.id.btnok);
            npfrom.setMinValue(3);
            npfrom.setMaxValue(7);
            npfrom.setWrapSelectorWheel(true);
            npto.setMinValue(3);
            npto.setMaxValue(7);
            npto.setWrapSelectorWheel(true);
            btnheightok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int fromval = npfrom.getValue();
                    int toval = npto.getValue();
                    dlg.dismiss();
                    tvspheightfeet.setText(fromval+" ft to "+toval+ " ft");
		    tvspheightfeet.setTextColor(Color.parseColor("#3E88F2"));
                    fromcm = fromval * 30.48;
                    tocm = toval * 30.48;
                }
            });

            dlg.setContentView(view1);
            dlg.show();


//            btnok.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    selected = "";
//                    int cntChoice = lv.getCount();
//
//                    SparseBooleanArray sparseBooleanArray = lv.getCheckedItemPositions();
//                    for (int i = 0; i < cntChoice; i++) {
//                        if (sparseBooleanArray.get(i)) {
//                            selected += lv.getItemAtPosition(i).toString() + ";";
//
//                        }
//
//                    }
//                    dlg.dismiss();
//                    if(!selected.equals("")) {
//                        selected = selected.substring(0, selected.length() - 1);
//                        tvspheightfeet.setText("" + selected);
//                        tvspheightfeet.setTextColor(Color.parseColor("#3E88F2"));
//                    }
//                }
//            });
        }
        else if (v == linearincome) {
            createdg(arrannualIncome);
            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected = "";
                    int cntChoice = lv.getCount();

                    SparseBooleanArray sparseBooleanArray = lv.getCheckedItemPositions();
                    for (int i = 0; i < cntChoice; i++) {
                        if (sparseBooleanArray.get(i)) {
                            selected += lv.getItemAtPosition(i).toString() + ";";

                        }

                    }
                    dlg.dismiss();
                    if(!selected.equals("")) {
                        selected = selected.substring(0, selected.length() - 1);
                        tvspannualincome.setText("" + selected);
                        tvspannualincome.setTextColor(Color.parseColor("#3E88F2"));
                    }
                }
            });


        }
        else if (v == linearwork) {
            createdg(arrwork);
            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected = "";
                    int cntChoice = lv.getCount();

                    SparseBooleanArray sparseBooleanArray = lv.getCheckedItemPositions();
                    for (int i = 0; i < cntChoice; i++) {
                        if (sparseBooleanArray.get(i)) {
                            selected += lv.getItemAtPosition(i).toString() + ";";

                        }

                    }
                    dlg.dismiss();
                    if(!selected.equals("")) {
                        selected = selected.substring(0, selected.length() - 1);
                        tvspwork.setText("" + selected);
                        tvspwork.setTextColor(Color.parseColor("#3E88F2"));
                    }
                }
            });
        }
        else if (v == btnclear)
        {
            edtgotra.setText("");
            tvspeduaction.setText("Select Education");
            tvspeduaction.setTextColor(Color.parseColor("#B1B0A7"));
            tvspdiet.setText("Select Diet");
            tvspdiet.setTextColor(Color.parseColor("#B1B0A7"));
            tvspmanglik.setText("Select Manglik status");
            tvspmanglik.setTextColor(Color.parseColor("#B1B0A7"));
            tvspheightfeet.setText("Select Height");
            tvspheightfeet.setTextColor(Color.parseColor("#B1B0A7"));
            tvspannualincome.setText("Select Annual Income");
            tvspannualincome.setTextColor(Color.parseColor("#B1B0A7"));
            tvspwork.setText("Select Work Status");
            tvspwork.setTextColor(Color.parseColor("#B1B0A7"));
            tvspheightfeet.setText("Select Height");
            tvspheightfeet.setTextColor(Color.parseColor("#B1B0A7"));
        }
        else if (v == btnsubmit) {
            String qry = "";
            int flag = 0;
            int i = 0;

            String gotra, education, manglik, work, annualincome, diet, height, selectqry1, dbgotra = "", dbeducation = "", dbmanglik = "", dbwork = "", dbannualincome = "", dbdiet = "";
            gotra = edtgotra.getText().toString().trim();
            education = tvspeduaction.getText().toString().trim();
            manglik = tvspmanglik.getText().toString().trim();
            work = tvspwork.getText().toString().trim();
            annualincome = tvspannualincome.getText().toString().trim();
            diet = tvspdiet.getText().toString().trim();
            height = tvspheightfeet.getText().toString().trim();

            String qry1 = "Select M_ID from " + TableFamilyName + " where Text2 = '" + type + "' AND Text3 = 'true' AND ";

            if (!gotra.equals("")) {
                qry = "Gotra = '" + gotra + "' AND ";
            }
            if (!(education.equals("Select Education"))) {
                    if(education.contains(";"))
                    {
                        String[] arredu = education.split(";");
                        qry = qry + " ( ";
                        for(int j =0;j<arredu.length-1;j++)
                                qry = qry + "Education = '" + arredu[j] + "' OR ";
                        qry = qry + " Education = '" + arredu[arredu.length-1] + "') AND ";
                    }
                    else
                    {
                        qry = qry + " Education = '" + education + "' AND ";
                    }
            }
            if (!(manglik.equals("Select Manglik status"))) {
                if (manglik.contains(";")) {
                    String[] arrmgk = manglik.split(";");
                    qry = qry + " ( ";
                    for (int j = 0; j < arrmgk.length - 1; j++)
                            qry = qry + "Text6 = '" + arrmgk[j] + "' OR ";
                    qry = qry + " Text6 = '" + arrmgk[arrmgk.length - 1] + "') AND ";
                }
                else
                {
                    qry = qry + " Text6 = '" + manglik + "' AND ";
                }
            }
            if (!(height.equals("Select Height"))) {

                {
                    qry = qry + " ( Height BETWEEN " + fromcm  + " AND " + tocm + ")  AND";
                }
            }
            if (!(annualincome.equals("Select Annual Income"))) {
                    if (annualincome.contains(";")) {
                        String[] arrai = annualincome.split(";");
                        qry = qry + " ( ";
                        for (int j = 0; j < arrai.length - 1; j++)
                                qry = qry + "Text7 = '" + arrai[j] + "' OR ";
                        qry = qry + " Text7 = '" + arrai[arrai.length - 1] + "') AND ";
                    }
                    else
                    {
                        qry = qry + " Text7 = '" + annualincome + "' AND ";
                    }
            }
            if (!(diet.equals("Select Diet"))) {
                    if (diet.contains(";")) {
                        String[] arrdt = diet.split(";");
                        qry = qry + " ( ";
                        for (int j = 0; j < arrdt.length - 1; j++)
                            qry = qry + "Text9 = '" + arrdt[j] + "' OR ";
                        qry = qry + " Text9 = '" + arrdt[arrdt.length - 1] + "') AND ";
                    }
                    else
                    {
                        qry = qry + " Text7 = '" + diet + "' AND ";
                    }
            }
            if (!(work.equals("Select Work Status"))) {
                if (work.contains(";")) {
                    String[] arrwork = work.split(";");
                    qry = qry + " ( ";
                    for (int j = 0; j < arrwork.length - 1; j++)
                        qry = qry + "Work_Profile = '" + arrwork[j] + "' OR ";
                    qry = qry + " Work_Profile = '" + arrwork[arrwork.length - 1] + "') AND ";
                }
                else {
                    qry = qry + " Work_Profile  = '" + work + "' AND ";
                }
            }

            if (!qry.equals("")) {
                if (qry.substring((qry.length() - 5), qry.length()).trim().equals("AND")) {
                    qry = qry.substring(0, (qry.length() - 5));
                }
            } else {
                if (qry1.substring((qry1.length() - 5), qry1.length()).trim().equals("AND")) {
                    qry1 = qry1.substring(0, (qry1.length() - 5));
                }
            }

            qry1 = qry1 + qry;

            //Open Db Connection
            db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);

            Cursor cursor = db.rawQuery(qry1, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                mid = new int[cursor.getCount()];
                i = 0;
                do {
                    mid[i++] = cursor.getInt(cursor.getColumnIndex("M_ID"));
                } while (cursor.moveToNext());
                Intent intent = new Intent(this, Wedding2.class);
                intent.putExtra("Type", type);
                intent.putExtra("Forward_id", "filterdetails");
                intent.putExtra("Clt_ClubName", ClubName);
                intent.putExtra("UserClubName", ClientID);
                //  intent.putExtra("AppLogo", AppLogo);
                startActivity(intent);
                finish();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("No Record found!");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            cursor.close();
            db.close();
        }
    }

    private void createdg(String arr[]) {
        dlg = new Dialog(context);
        dlg.setTitle("Select");
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.listdialog, null, false);
        lv = (ListView) view.findViewById(R.id.listView1);
        btnok = (Button) view.findViewById(R.id.btnok);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, arr);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setAdapter(adapter);
        dlg.setContentView(view);
        dlg.show();
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


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
	    finish();
	}
}
