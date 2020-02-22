package group.manager;

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
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DGCalendar extends Activity {
    String Str_user,Logcl,logid,ClubName,Str_IEMI,DGProgDates="",UserType="",Table2Name,Table4Name,WebResult="";
    private static final String tag = "MyCalendarActivity";
    private TextView currentMonth;
    ImageView prevMonth,nextMonth,btnSendReq;
    private int month, year;
    GridView GV_Days,GV_Calendar;
    final String[] Arr_DaysName = new String[] {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
    final String[] Arr_MonthsName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    Context context=this;
    byte[] AppLogo;
    Intent menuIntent;
    Thread networkThread;
    ProgressDialog Progsdial;
    Chkconnection chkconn;
    boolean InternetPresent;
    WebServiceCall webcall;
    AlertDialog ad;
    SharedPreferences ShPref;
    SQLiteDatabase dbObj;
    int OldMid=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calenderviewpg);

        ad=new AlertDialog.Builder(this).create();
        GV_Days = (GridView) findViewById(R.id.days);
        GV_Calendar = (GridView) this.findViewById(R.id.calendar);
        prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
        currentMonth = (TextView) this.findViewById(R.id.currentMonth);
        nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
        btnSendReq = (ImageView) this.findViewById(R.id.imgsendrqst);
        btnSendReq.setVisibility(View.GONE);

        menuIntent = getIntent();
        Logcl =  menuIntent.getStringExtra("Clt_Log");
        logid =  menuIntent.getStringExtra("Clt_LogID");
        ClubName =  menuIntent.getStringExtra("Clt_ClubName");
        Str_user =  menuIntent.getStringExtra("UserClubName");
        AppLogo = menuIntent.getByteArrayExtra("AppLogo");

        webcall=new WebServiceCall(); //Call a Webservice
        chkconn=new Chkconnection();
        Str_IEMI = new CommonClass().getIMEINumber(context); //Added On 14-02-2019

        Table2Name="C_"+Str_user+"_2";
        Table4Name="C_"+Str_user+"_4";

        Get_SharedPref_Values();// Get Stored Shared Pref Values of Login

        Set_App_Logo_Title(); // Set App Logo and Title

        GV_Days.setAdapter(new Image_Adapter1(context, Arr_DaysName));/// Set Days Name Array in Days GridView

        ///Current Date
        Calendar calc = Calendar.getInstance(Locale.getDefault());
        month = calc.get(Calendar.MONTH) + 1;// get Current Month
        year = calc.get(Calendar.YEAR);// get Current Year
        //////////////////

        FillData_inCalendar(month, year);/// Fill Data in Calendar

        prevMonth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                if (month <= 1) {
                    month = 12;
                    year--;
                } else {
                    month--;
                }
                FillData_inCalendar(month, year);
            }
        });

        nextMonth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                if (month > 11) {
                    month = 1;
                    year++;
                } else {
                    month++;
                }
                FillData_inCalendar(month, year);
            }
        });

    }

    private void Get_SharedPref_Values() {
        ShPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        if (ShPref.contains("UserType"))
        {
            UserType=ShPref.getString("UserType", "");
        }
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



    /////////////cal webservice for Schedule dates and fill in calendar///////////////////////////
    public void FillData_inCalendar(int month,int year)
    {
        DGProgDates=Get_DGProgDates(month,year);//Get Stored DG Program Dates

        currentMonth.setText(Arr_MonthsName[month - 1] + " " + year);

        GridCellAdapter Adp = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
        Adp.notifyDataSetChanged();
        GV_Calendar.setAdapter(Adp);
    }


    ////Get DG Program Schedule Dates for Club RI31101920
    private String Get_DGProgDates(int month,int year)
    {
        String MM=month+"";
        if(month<10)
            MM="0"+month;

        String t1=MM+"-"+year;

        String sql = "Select Distinct Text1 from " + Table4Name + " Where Rtype='COMM_DGPROG' AND Text1 like '%"+t1+"'";

        dbObj = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        Cursor cursorT = dbObj.rawQuery(sql, null);

        String DDates = "";
        if (cursorT.moveToFirst()) {
            do {
                DDates =DDates+ChkVal(cursorT.getString(0))+"#";
            } while (cursorT.moveToNext());
        }
        cursorT.close();
        dbObj.close();//Close Databse

        if(DDates.contains("#"))
        {
            DDates = DDates.substring(0, DDates.length() - 1);

            //Takes only Date Days
            t1="-"+t1;
            DDates=DDates.replace(t1,"");
        }

        return DDates;
    }


    ////Get DG Program Data for Club RI31101920
    private List<RowEnvt> Get_DGProg_List(String DDate)
    {
        String sql = "Select M_ID,Text2 from " + Table4Name + " Where Rtype='COMM_DGPROG' AND Text1='"+DDate.trim()+"'";

        dbObj = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        Cursor cursorT = dbObj.rawQuery(sql, null);

        List<RowEnvt> ListItems = new ArrayList<RowEnvt>();
        if (cursorT.moveToFirst()) {
            do {
                String MID =ChkVal(cursorT.getString(0));
                String Val = ChkVal(cursorT.getString(1));
                DDate="";
                String Title="";
                if(Val.contains("#")){
                    Val=Val+" ";
                    String[] Arr1=Val.split("#");
                    DDate=Arr1[0].trim();
                    Title=Arr1[1].trim();
                }
                RowEnvt item = new RowEnvt(Title,DDate,MID+"#"+Val,"");
                ListItems.add(item);
            } while (cursorT.moveToNext());
        }

        cursorT.close();
        dbObj.close();//Close Databse

        return ListItems;
    }


    // Show List DG Program in group Rotary Club Bareilly South ClientID--> RI31101920)
    private void Show_Dialog_DGProg_List(final String DDate)
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// For Hide the title of the dialog box
        dialog.setContentView(R.layout.commitee_club_prog1);
        //dialog.setCancelable(false);
        dialog.show();

        final TextView txtHead=(TextView) dialog.findViewById(R.id.txtHead);
        final ListView LV1=(ListView) dialog.findViewById(R.id.LV1);
        Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);

        txtHead.setText("DG Programs");

        if(!UserType.equalsIgnoreCase("ADMIN")){///UPDATE
            btnAdd.setText("CLOSE");
        }

        final List<RowEnvt> ListItems=Get_DGProg_List(DDate);///Get DG Programs List
        Adapter_NewsMain Adp1 = new Adapter_NewsMain(context,R.layout.newsmainlist, ListItems);
        LV1.setAdapter(Adp1);

        //ListView Click Event
        LV1.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){

                dialog.dismiss();
                String Val=ListItems.get(position).getEvtdate();
                Show_Dialog_DGProg_Add(Val,"");
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if(UserType.equalsIgnoreCase("ADMIN"))///UPDATE
                {
                    Show_Dialog_DGProg_Add("",DDate);
                }
            }
        });
    }

    // Show Add DG Program in group Rotary Club Bareilly South ClientID--> RI31101920)
    private void Show_Dialog_DGProg_Add(String Data,final String DDate)
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

        txtHead.setText("DG Programs");

        OldMid = 0;//Default Val

        txt_Date.setText(DDate);

        btnUpdate.setText("ADD");
        if (Data.contains("#")) {
            Data = Data + " ";
            String[] Arr1 = Data.split("#");
            OldMid = Integer.parseInt(Arr1[0].trim());// MID
            txt_Date.setText(Arr1[1].trim());//Set Date
            ed_Title.setText(Arr1[2].trim());//Set Title
            ed_Desc.setText(Arr1[3].trim());//Set Desc
            btnUpdate.setText("UPDATE");
        }

        if(!UserType.equalsIgnoreCase("ADMIN")){///UPDATE
            ed_Title.setEnabled(false);/// Disabled EditText
            ed_Desc.setEnabled(false);/// Disabled EditText
            btnUpdate.setText("CLOSE");
        }


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

                    Chkconnection chkconn=new Chkconnection();//Intialise Chkconnection Object
                    InternetPresent =chkconn.isConnectingToInternet(context);
                    if(InternetPresent==true)
                    {
                        Update_DGProg_Server(DDate,RData,OldMid);//Committee Club Program
                    }
                    else {
                        AlertDisplay("Internet Connection","No Internet Connection !");
                    }
                }

            }
        });
    }


    //Update Club DG Program from Mobile to sever (updated on 20-02-2020)
    public void Update_DGProg_Server(final String DDate, final String TVal, final int OldId)
    {
        progressdial();
        Thread T2 = new Thread() {
            @Override
            public void run() {
                try {
                    WebServiceCall webcall=new WebServiceCall();
                    WebResult=webcall.Committee_ClubProg(Str_user,DDate,TVal,OldId,4);/// Club DG Program from Mobile to sever (Added on 20-02-2020)

                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            if(WebResult.contains("Saved"))
                            {
                                String R1="Updated Successfully !";
                                if(OldId==0)
                                    R1="Added Successfully !";

                                AlertDisplay("Result",R1);

                                Service_Call_Sync_Tab4();//Sync Table 4 from Server To Moblie
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


    // Inner Class
    public class GridCellAdapter extends BaseAdapter {
        private static final String tag = "GridCellAdapter";
        private final Context _context;
        private final List<String> list;
        private static final int DAY_OFFSET = 1;
        private final String[] weekdays = new String[] { "Sun", "Mon", "Tue","Wed", "Thu", "Fri", "Sat" };
        private final String[] months = { "January", "February", "March","April", "May", "June", "July", "August", "September","October", "November", "December" };
        private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,31, 30, 31 };
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private Button gridcell;
        private ImageView ImageViewCross;
        private TextView num_events_per_day;
        private final HashMap<String, Integer> eventsPerMonthMap;
        private final SimpleDateFormat dateFormatter1 = new SimpleDateFormat("dd-MMM-yyyy");
        private final SimpleDateFormat dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy");

        // Days in Current Month
        public GridCellAdapter(Context context, int textViewResourceId,int month, int year) {
            super();
            this._context = context;
            this.list = new ArrayList<String>();
            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            //Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
            //Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
            //Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

            // Print Month
            printMonth(month, year);

            // Find Number of Events
            eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
        }

        private String getMonthAsString(int i) {
            return months[i];
        }

        private String getWeekDayAsString(int i) {
            return weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }

        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        /// Prints Month
         // @param mm
         // @param yy
        private void printMonth(int mm, int yy) {
            //Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
            int trailingSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;

            int currentMonth = mm-1;
            String currentMonthName = getMonthAsString(currentMonth);
            daysInMonth = getNumberOfDaysOfMonth(currentMonth);

            Log.d(tag, "Current Month: " + " " + currentMonthName + " having "+ daysInMonth + " days.");

            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
            //Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

            if (currentMonth == 11) {
                prevMonth = currentMonth-1;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
                //Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:"+ prevMonth + " NextMonth: " + nextMonth+ " NextYear: " + nextYear);
            } else if (currentMonth == 0) {
                prevMonth = 11;
                prevYear = yy-1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 1;
                //Log.d(tag, "**-> PrevYear: " + prevYear + " PrevMonth:"+ prevMonth + " NextMonth: " + nextMonth+ " NextYear: " + nextYear);
            } else {
                prevMonth = currentMonth-1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                //Log.d(tag, "***�> PrevYear: " + prevYear + " PrevMonth:"+ prevMonth + " NextMonth: " + nextMonth+ " NextYear: " + nextYear);
            }

            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK)-1;
            trailingSpaces = currentWeekDay;

            //Log.d(tag, "Week Day:" + currentWeekDay + " is "+ getWeekDayAsString(currentWeekDay));
            //Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
            //Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

            if (cal.isLeapYear(cal.get(Calendar.YEAR)))
                if (mm == 2)
                    ++daysInMonth;
                else if (mm == 3)
                    ++daysInPrevMonth;

            // Trailing Month days
            for (int i = 0; i < trailingSpaces; i++) {
                //Log.d(tag,"PREV MONTH:= "+ prevMonth + " => "+ getMonthAsString(prevMonth)+ " "+ String.valueOf((daysInPrevMonth-trailingSpaces + DAY_OFFSET)+ i));
                list.add(String.valueOf((daysInPrevMonth-trailingSpaces + DAY_OFFSET)+ i)+ "-"+ getMonthAsString(prevMonth)+ "-"+ prevYear+ "-GREY");
            }

            // Current Month Days
            for (int i = 1; i <= daysInMonth; i++) {
                //Log.d(currentMonthName, String.valueOf(i) + " "+ getMonthAsString(currentMonth) + " " + yy);

                if (i == getCurrentDayOfMonth())
                {
                    if(DGProgDates.length()!=0){
                        int q=0;
                        String []temp=DGProgDates.split("#");

                        for(int j=0;j<temp.length;j++){

                            int val=Integer.parseInt(temp[j]);
                            if(i==val){
                                q=1;
                                list.add(i + "-"+ getMonthAsString(currentMonth) + "-" + yy+ "-RED" );
                            }
                        }
                        if(q==0){
                            list.add(i + "-"+ getMonthAsString(currentMonth) + "-" + yy+ "-BLUE" );
                        }
                    }else{
                        list.add(i + "-"+ getMonthAsString(currentMonth) + "-" + yy+ "-BLUE" );
                    }

                }else{
                    if(DGProgDates.length()!=0)
                    {
                        int p=0;
                        String []temp=DGProgDates.split("#");

                        for(int j=0;j<temp.length;j++)
                        {
                            int val=Integer.parseInt(temp[j]);
                            if(i==val){
                                p=1;
                                list.add(i+ "-"+ getMonthAsString(currentMonth) + "-" + yy + "-RED");
                            }
                        }
                        if(p==0){
                            list.add(i + "-"+ getMonthAsString(currentMonth) + "-" + yy+ "-WHITE");
                        }
                    }else{
                        list.add(i + "-"+ getMonthAsString(currentMonth) + "-" + yy+ "-WHITE");
                    }
                }
            }

            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++) {
                //Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
                list.add((i + 1) + "-"+ getMonthAsString(nextMonth) + "-" + nextYear+ "-GREY" );
            }
        }

        /**
         * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
         * ALL entries from a SQLite database for that month. Iterate over the
         * List of All entries, and get the dateCreated, which is converted into
         * day.
         *
         * @param year
         * @param month
         * @return
         */
        private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,int month) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            return map;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.screen_gridcell, parent, false);
            }

            // Get a reference to the Day gridcell
            gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
            ImageViewCross=(ImageView)row.findViewById(R.id.imageViewcross);
            ImageViewCross.setVisibility(View.GONE);//Hide Visibility

            // ACCOUNT FOR SPACING
            //Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
            String[] day_color = list.get(position).split("-");
            String theday = day_color[0];
            String themonth = day_color[1];
            String theyear = day_color[2];
            if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
                if (eventsPerMonthMap.containsKey(theday)) {
                    num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
                    Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
                    num_events_per_day.setText(numEvents.toString());
                }
            }

            // Set the Day GridCell
            gridcell.setText(theday);
            gridcell.setTag(theday + "-" + themonth + "-" + theyear);
            Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-"+ theyear);

            if (day_color[3].equals("GREY")) {
                gridcell.setTextColor(Color.rgb(240,248,255));
                gridcell.setClickable(false);
            }
            else if (day_color[3].equals("WHITE")) {
                gridcell.setTextColor(getResources().getColor(R.color.black));
            }
            else if (day_color[3].equals("BLUE")) {
                gridcell.setTextColor(getResources().getColor(R.color.black));
            }
            else if (day_color[3].equals("RED")) {
                gridcell.setTextColor(getResources().getColor(R.color.white));
                gridcell.setBackgroundColor(getResources().getColor(R.color.orrange));
                gridcell.setClickable(true);
            }


            gridcell.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {

                    //Calendar c = Calendar.getInstance();
                    //c.add(Calendar.DATE, bookdays);
                    //Date resultdate = new Date(c.getTimeInMillis());
                    //Log.d(tag, "result day: " + resultdate);

                    String date_month_year = (String) view.getTag(); //Get Selected date from gridview tag

                    String[] day_color = list.get(position).split("-");

                    try {

                        Date CurDT= dateFormatter1.parse(dateFormatter1.format(new Date()));// Current Date
                        Date SelectedDT = dateFormatter1.parse(date_month_year);// Selected Date
                        String StrSelectedDT = dateFormatter2.format(SelectedDT);

                        if(day_color[3].equals("RED")){
                            Log.d(tag, "REDDDDDDDDDDDDDDDDDDDDDD");
                            Show_Dialog_DGProg_List(StrSelectedDT);//Show or Update DG Programs
                        }
                        else if (SelectedDT.before(CurDT)) {
                            System.out.println("SelectedDT is before CurDT");
                            Toast.makeText(context, "Cannot select previous date.", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            System.out.println("SelectedDT is after or equal CurDT");

                            if(UserType.equalsIgnoreCase("ADMIN"))
                            {
                                Show_Dialog_DGProg_List(StrSelectedDT);//Add DG Programs
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });


            return row;
        }

        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;
        }

        public int getCurrentWeekDay() {
            return currentWeekDay;
        }
    }


    // Start a service Service_Call_Sync_Tab4 for Sync Table4 Added on 22-02-2020
    private void Service_Call_Sync_Tab4()
    {
        Intent intent = new Intent(context,Service_Call_Sync_Tab4.class);
        intent.putExtra("ClientID",Str_user);
        startService(intent);
    }


    private String ChkVal(String Val) {
        if (Val == null)
            Val = "";
        return Val.trim();
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

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            GoBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void GoBack()
    {
        finish();
    }


    @Override
    public void onDestroy() {
        Log.d(tag, "Destroying View");
        super.onDestroy();
    }

}
