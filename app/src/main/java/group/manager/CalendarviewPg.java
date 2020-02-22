package group.manager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarviewPg extends Activity{
	String MainUID,Str_user,Logid,Logcl,logid,ClubName,UID,SUID,Webevent,Stryear,Strmonth,Str_IEMI,bookingadtes="",StrMemNo="",MemName="",UserType="";
	private static final String tag = "MyCalendarActivity";
	private TextView currentMonth;
	//private Button selectedDayMonthYearButton;
	ImageView prevMonth,nextMonth,SendReq;
	private GridView calendarView;
	private GridCellAdapter adapter;
	//private Calendar calc;
	private int month, year;
	GridView gridView;  
	final String[] dayarr = new String[] {"Sun", "Mon","Tue","Wed", "Thu","Fri","Sat"};
	Context context=this;
	Intent menuIntent;
	Thread networkThread;
	ProgressDialog Progsdial;
	Chkconnection chkconn;
	boolean InternetPresent;
	WebServiceCall webcall;
	TelephonyManager tm;
	List<String> listsend;
	String Strdate="",Table2Name,webadmin,TableItem;
	AlertDialog.Builder alertDialogBuilder3,alertDialogBuilder4;
	AlertDialog ad;
	SharedPreferences sharedpreferences;
	int bookdays=0;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.calenderviewpg);
	        ad=new AlertDialog.Builder(this).create();
		        gridView = (GridView) findViewById(R.id.days);
		        calendarView = (GridView) this.findViewById(R.id.calendar);
		        prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
		        SendReq = (ImageView) this.findViewById(R.id.imgsendrqst);
		        currentMonth = (TextView) this.findViewById(R.id.currentMonth);
		        nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
		        gridView.setAdapter(new Image_Adapter1(context, dayarr));	
		        
		        menuIntent = getIntent(); 
		        Str_user =  menuIntent.getStringExtra("UserClubName");
				Logcl =  menuIntent.getStringExtra("Clt_Log");
				logid =  menuIntent.getStringExtra("Clt_LogID");
				ClubName =  menuIntent.getStringExtra("Clt_ClubName");
				UID =  menuIntent.getStringExtra("UID");
				SUID =  menuIntent.getStringExtra("SUID");
				MainUID =  menuIntent.getStringExtra("MainUID");
				Stryear =  menuIntent.getStringExtra("year");
				Strmonth =  menuIntent.getStringExtra("month");
				
				year=Integer.parseInt(Stryear);
				month=Integer.parseInt(Strmonth);
				
		        System.out.println(month+" "+year+"  "+logid);
		        webcall=new WebServiceCall();//Call a Webservice
				chkconn=new Chkconnection();

		        Str_IEMI = new CommonClass().getIMEINumber(context);//Added On 14-02-2019
				
				Table2Name="C_"+Str_user+"_2";
				TableItem="C_"+Str_user+"_Item";
				
				callValue();
				
				SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null); 
	   			 String qury="Select MemNo,M_Name from "+Table2Name+" where M_Id="+logid;
	   			 Cursor cursorT=db.rawQuery(qury, null);
	   			 if(cursorT.moveToFirst())
	   			 {
	   			   StrMemNo=cursorT.getString(0);
	   			   MemName=cursorT.getString(1);
	   			   String tt="";
	   			 }
	   			 cursorT.close();
	   			 
	   			 qury="Select Book_Days from "+TableItem+" where U_Id="+SUID;
	   			 cursorT=db.rawQuery(qury, null);
	   			 while(cursorT.moveToNext())
	   			 {
	   				bookdays=cursorT.getInt(0);
	   			   break;
	   			 }
	   			 cursorT.close();
	   			 db.close();
	   			 
	   			 System.out.println(bookdays);
		        
		        prevMonth.setOnClickListener(new OnClickListener(){ 
		   		 @Override
		   		  public void onClick(View arg0) {
		   			 if (month <= 1) {
				           month = 12;
				           year--;
				        } else {
				           month--;
				        }
				        //Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "+ month + " Year: " + year);
				      //setGridCellAdapterToDate(month, year);
		   			RInsert(month, year);
		   		   }
		        });
			        
		        nextMonth.setOnClickListener(new OnClickListener(){ 
			   		 @Override
			   		  public void onClick(View arg0) {
			   			 if (month > 11) {
					          month = 1;
					          year++;
					     } else {
					          month++;
					    }
			   			RInsert(month, year);
			   		   }
			        });
		        
	            SendReq.setOnClickListener(new OnClickListener(){ 
		   		 @Override
		   		  public void onClick(View arg0) {
		   			alertDialogBuilder3 = new AlertDialog.Builder(context);
	        		 alertDialogBuilder3
	        		 .setMessage(Html.fromHtml("<font color='#E32636'>Do you want to book for selected dates?</font>"))
		                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog,int id) {
		                    	dialog.dismiss();
		                    	 Strdate="";
		    		   			 int listtemp=listsend.size();
		    		   			 if(listtemp>0){
		    			   			 for(int i=0;i<listtemp;i++){
		    			   				Strdate +=listsend.get(i)+"^"; 
		    			   			 }
		    			   			 System.out.println(Strdate);
		    			   			 
		    			   			 InternetPresent =chkconn.isConnectingToInternet(context);
		    			   		     if(InternetPresent==true){
		    			   		       progressdial();
		    				   	       networkThread = new Thread()
		    				   	       {
		    				   	         public void run()
		    				   	         {
		    				   	           try
		    				   	           {
		    				   	              Webevent=webcall.clubbookingNew(Str_user, Str_IEMI, SUID, UID, StrMemNo,Strdate,UserType.toUpperCase(),MemName);
		    				   	              runOnUiThread(new Runnable()
		    				   	              {
		    				   	          	   public void run()
		    				   	          	   {
		    				   	          		 System.out.println(Webevent);
		    				   	          		 if(Webevent.contains("Saved")){
		    				   	          			alertDialogBuilder4 = new AlertDialog.Builder(context);
		    				   	          			alertDialogBuilder4
		    				   	          			.setMessage(Html.fromHtml("<font color='#318CE7'>Your booking request has been sent to the Club.</font>"))
		    						                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
		    						                    public void onClick(DialogInterface dialog,int id) {
		    						                    	dialog.dismiss();
		    						                    	 callitself();
		    				   	          				 }
		    				   	          			 });
		    					        		 	ad = alertDialogBuilder4.create();
		    							            ad.show();
		    				   	          		 }
		    				   	               }
		    				   	              });
		    				   	              Progsdial.dismiss();
		    				   	              return;
		    				   	            }catch (Exception localException){
		    				   	             	System.out.println(localException.getMessage());
		    				   	            }
		    				   	          }
		    				   	        };
		    				   	       networkThread.start();
		    			   		     }
		    		   			 }else{
		    		   				Toast.makeText(context, "select any date.", 1).show();
		    		   			 }
		                    }
		                })
		                .setNegativeButton("No",new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog,int id) {
		                    	dialog.dismiss();
		                    }
		                });
	        		ad = alertDialogBuilder3.create();
		            ad.show();	 
		   			 
		   		   }
		        });
	            
		     /////////////cal webservice for booking date///////////////////////////
		     RInsert(month, year);
	 }
	 
	 private void callValue() {
	 sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
      if (sharedpreferences.contains("UserType"))
      {
    	  UserType=sharedpreferences.getString("UserType", "");
      } 
	 }
	 

   /* private void setGridCellAdapterToDate(int month, int year) {
    	 RInsert(month, year);
     /*adapter = new GridCellAdapter(getApplicationContext(),R.id.calendar_day_gridcell, month, year);
     calc.set(year, month-1, calc.get(Calendar.DAY_OF_MONTH));
     currentMonth.setText(DateFormat.format(dateTemplate,calc.getTime()));
     adapter.notifyDataSetChanged();
     calendarView.setAdapter(adapter);
   }*/
    
    public void RInsert(final int month, final int year)
    {
    	 InternetPresent =chkconn.isConnectingToInternet(context);
		 if(InternetPresent==true){
		   progressdial();
	       networkThread = new Thread()
	       {
	         public void run()
	         {
	           try
	           {
	             Webevent=webcall.clubbooking(Str_user, Str_IEMI, SUID, UID, String.valueOf(month), String.valueOf(year));
	             runOnUiThread(new Runnable()
	              {
	          	   public void run()
	          	   {
	          		 System.out.println(Webevent);
	          		 // Initialised GridCellAdapter
	          		  if(Webevent.contains("#"))
	          		  {
	          			bookingadtes= Webevent;
	          		  }else{
	          			bookingadtes="";  
	          		  }
	          		 currentMonth.setText(gettheMonthname(month-1)+" "+year);
	          		 adapter = new GridCellAdapter(getApplicationContext(),R.id.calendar_day_gridcell, month, year);
    		         adapter.notifyDataSetChanged();
    		         calendarView.setAdapter(adapter);
    		         listsend = new ArrayList<String>();
	               }
	            });
	            Progsdial.dismiss();
	            return;
	           }
	           catch (Exception localException)
	           {
	             	// System.out.println("AAAAA  :@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@: ");
	          	 System.out.println(localException.getMessage());
	           }
	         }
	       };
	       networkThread.start();
		 }
	}
    
    public void Checkforadmin(final String date)
    {
    	InternetPresent =chkconn.isConnectingToInternet(context);
		 if(InternetPresent==true){
		   progressdial();
	       networkThread = new Thread()
	       {
	         public void run()
	         {
	           try
	           {
	        	 webadmin=webcall.clubbookinginfo(Str_user, Str_IEMI, SUID, UID, date);
	             runOnUiThread(new Runnable()
	              {
	          	   public void run()
	          	   {
	          		 System.out.println(webadmin);
	          		 Intent localIntent = new Intent(context, BookingsubCate.class);
	        	     localIntent.putExtra("webadmin", webadmin);
	        	     localIntent.putExtra("Type", "Adm");
	        	     localIntent.putExtra("date", date);
	        	     startActivity(localIntent);
	               }
	            });
	            Progsdial.dismiss();
	            return;
	           }
	           catch (Exception localException)
	           {
	          	 System.out.println(localException.getMessage());
	           }
	         }
	       };
	       networkThread.start();
		 }else{
			 Toast.makeText(context, "No network available", 1).show();
		 }
	}

  
    @Override
    public void onDestroy() {
      Log.d(tag, "Destroying View");
     super.onDestroy();
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
	    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
	    private final SimpleDateFormat dateForm = new SimpleDateFormat("dd-MMMM-yy");
	
	    // Days in Current Month
	    public GridCellAdapter(Context context, int textViewResourceId,int month, int year) {
		    super();
		    this._context = context;
		    this.list = new ArrayList<String>();
		    //Log.d(tag, "==> Passed in Date FOR Month: " + month + " " + "Year: " + year);
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
	
	    /**
	    * Prints Month
	    *
	    * @param mm
	    * @param yy
	    */
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
			    if (i == getCurrentDayOfMonth()){
			    	if(bookingadtes.length()!=0){
			    		int q=0;
			    		String []temp=bookingadtes.split("#");
			    		System.out.println(temp.length);
			    		for(int j=0;j<temp.length;j++){
			    			System.out.println("jj:: "+temp[j]);
			    			int val=Integer.parseInt(temp[j]);
			    			if(i==val){
			    				q=1;
			    				list.add(String.valueOf(i) + "-"+ getMonthAsString(currentMonth) + "-" + yy+ "-RED" );		
			    			}
			    		}
			    		if(q==0){
			    			list.add(String.valueOf(i) + "-"+ getMonthAsString(currentMonth) + "-" + yy+ "-BLUE" );	
			    		}
			    	}else{
			    	  list.add(String.valueOf(i) + "-"+ getMonthAsString(currentMonth) + "-" + yy+ "-BLUE" );	
			    	}
			    	
			    }else{
			    	if(bookingadtes.length()!=0){
			    		int p=0;
			    		String []temp=bookingadtes.split("#");
			    		System.out.println(temp.length);
			    		for(int j=0;j<temp.length;j++){
			    			System.out.println("jj:: "+temp[j]);
			    			int val=Integer.parseInt(temp[j]);
			    			if(i==val){
			    				p=1;
			    				list.add(String.valueOf(i)+ "-"+ getMonthAsString(currentMonth) + "-" + yy + "-RED" );		
			    			}
			    		}
			    		if(p==0){
			    			list.add(String.valueOf(i) + "-"+ getMonthAsString(currentMonth) + "-" + yy+ "-WHITE" );
			    		}
			    	}else{
			    		 list.add(String.valueOf(i) + "-"+ getMonthAsString(currentMonth) + "-" + yy+ "-WHITE" );
			    	}
			    }
		    }
		
		    // Leading Month days
		    for (int i = 0; i < list.size() % 7; i++) {
		      //Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
		      list.add(String.valueOf(i + 1) + "-"+ getMonthAsString(nextMonth) + "-" + nextYear+ "-GREY" );
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
		    gridcell.setOnClickListener(new OnClickListener(){ 
		   		 @Override
		   		  public void onClick(View view) {
		   			//gridcell.setBackgroundColor(Color.GREEN);
		   			 String date_month_year = (String) view.getTag();
		   			 Log.d(tag, date_month_year);
		   			 Date date = new Date();
		   			
		   			 Calendar c = Calendar.getInstance();
		   			 c.add(Calendar.DATE, bookdays);
		   		     Date resultdate = new Date(c.getTimeInMillis());
		   		     Log.d(tag, "result day: " + resultdate);
		   		     
		   	         //selectedDayMonthYearButton.setText("Selected: " + date_month_year);
		   	         //Log.e("Selected date", date_month_year);
		   			 String[] day_color = list.get(position).split("-");
		   		     
		   			 try {
		   		      Date parsedDate = dateFormatter.parse(date_month_year);
		   		      
		   		      //int tt=parsedDate.getDay();
		   		      
		   		      if(day_color[3].equals("RED")){
		   				Log.d(tag, "REDDDDDDDDDDDDDDDDDDDDDD");
		   				Checkforadmin(date_month_year);
		   			  }
		   		      else if(parsedDate.before(date)){
		   		    	view.setBackgroundColor(Color.parseColor("#FAEBD7"));  
		   			    Toast.makeText(context, "Cannot select previous date.", 1).show();
		   		      }/*else if((bookdays>0)&&(parsedDate.after(resultdate))){
			   		    view.setBackgroundColor(Color.parseColor("#FAEBD7"));  
			   			Toast.makeText(context, "Please visit club for this booking.", 1).show();
			   		  }*/else{
		   		    	view.setBackgroundColor(Color.parseColor("#A4C639"));  
		   		    	String strdt = dateForm.format(parsedDate);
		   		    	//Log.d(tag, "SS Date: " + s);
		   		    	listsend.add(dateForm.format(parsedDate));
		   		      }
		   		      Log.d(tag, "Parsed Date: " + parsedDate.getDate());
		   		    } catch (ParseException e) {
		   		     Log.d(tag, "Parsed Date: ");
		   		    	e.printStackTrace();
		   		    }
		   		   }
		        });
	
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
		      ImageViewCross.setVisibility(View.GONE);
		    }
		    else if (day_color[3].equals("WHITE")) {
		      gridcell.setTextColor(getResources().getColor(R.color.black));
		      ImageViewCross.setVisibility(View.GONE);
		    }
		    else if (day_color[3].equals("BLUE")) {
		    	gridcell.setTextColor(getResources().getColor(R.color.orrange));
		    	ImageViewCross.setVisibility(View.GONE);
		    }
		    else if (day_color[3].equals("RED")) {
		    	gridcell.setTextColor(getResources().getColor(R.color.orrange));
		    	//if(UserType.equalsIgnoreCase("admin")){
		    		gridcell.setClickable(true);
		    	//}else{
		    		//gridcell.setClickable(false);
		    	//}
		    	ImageViewCross.setVisibility(View.VISIBLE);
		    }
		    
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
    
    private void goback()
	  {
	    Intent localIntent = new Intent(context, Timeslot.class);
	    localIntent.putExtra("Clt_LogID", logid);
	    localIntent.putExtra("Clt_Log", Logcl);
	    localIntent.putExtra("Clt_ClubName", ClubName);
	    localIntent.putExtra("UserClubName", Str_user);
	    localIntent.putExtra("MainUID",MainUID);
	    localIntent.putExtra("UID", SUID);
	    startActivity(localIntent);
	    finish();
	  }
    
    private void callitself()
	  {
	    Intent localIntent = new Intent(context, CalendarviewPg.class);
	    localIntent.putExtra("Clt_LogID", logid);
	    localIntent.putExtra("Clt_Log", Logcl);
	    localIntent.putExtra("Clt_ClubName", ClubName);
	    localIntent.putExtra("UserClubName", Str_user);
	    localIntent.putExtra("MainUID",MainUID);
	    localIntent.putExtra("UID", UID);
	    localIntent.putExtra("SUID", SUID);
	    localIntent.putExtra("year", Stryear);
	    localIntent.putExtra("month", Strmonth);
	    startActivity(localIntent);
	    finish();
	  }
    
   /* private void DisplayMsg(String msg)
	  {
	    AlertDialog localAlertDialog = new AlertDialog.Builder(this).create();
	    localAlertDialog.setCancelable(false);
	    //localAlertDialog.setTitle(Html.fromHtml("<font color='#E3256B'>" + title + "</font>"));
	    localAlertDialog.setMessage(Html.fromHtml("<font color='#1C1CF0'>" + msg + "</font>"));
	    localAlertDialog.setButton("Ok", new DialogInterface.OnClickListener()
	    {
	      public void onClick(DialogInterface dialg, int which)
	      {
	    	  goback();
	      }
	    });
	    localAlertDialog.show();
	  }*/
	 
	 public boolean onKeyDown(int keyCode,KeyEvent event) 
	 {
	   	 if (keyCode == KeyEvent.KEYCODE_BACK) {
	   		goback();
	   	    return true;
	   	 }
	   	return super.onKeyDown(keyCode, event);
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
	 
	 public static String gettheMonthname(int month){
		    String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		    return monthNames[month];
		}
	 
  }