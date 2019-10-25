package group.manager;

import group.manager.SimpleGestureFilter.SimpleGestureListener;

import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import group.manager.SimpleGestureFilter.SimpleGestureListener;


public class OpinionPoll_ScoreSheet extends Activity implements SimpleGestureListener {
    
    String ClubName,ClientId,LogId,MTitle,WebResult="",Mids="";
    //ArrayList<RowItem_OpPoll_Main> arrayListTitle;
    Context context = this;
    byte[] AppLogo;
    ProgressDialog Progsdial;
    private boolean InternetPresent;
    int Mid=0,QuesNo;
    String[] WebResultArr=null;
    TextView txtSno,txtQues;
    LinearLayout Lchart;
    private SimpleGestureFilter detect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opinion_poll_scoresheet);
        
        txtSno=(TextView) findViewById(R.id.txtSno);
        txtQues = (TextView) findViewById(R.id.txtQues);
        Lchart = (LinearLayout) findViewById(R.id.Lchart);
        
        detect = new SimpleGestureFilter(this,this);
       
        Intent menuIntent = getIntent(); 
        Mid = menuIntent.getIntExtra("Mid",0);
        MTitle=menuIntent.getStringExtra("MTitle");
        LogId =  menuIntent.getStringExtra("Clt_LogID");
		ClubName =  menuIntent.getStringExtra("Clt_ClubName");
		ClientId =  menuIntent.getStringExtra("UserClubName");
		AppLogo =  menuIntent.getByteArrayExtra("AppLogo");
		
		Set_App_Logo_Title(); // Set App Logo and Title
		
		//txtHead.setText(MTitle);
		txtSno.setVisibility(View.GONE);
		txtQues.setText("");
		
		Chkconnection chkconn=new Chkconnection();//Intialise Chkconnection Object
	    InternetPresent =chkconn.isConnectingToInternet(context);
		if(InternetPresent==true)
		{
		    GetdataFromWebservice();
	    }
		else{
			AlertDisplay("Internet Connection","No Internet Connection !");
		}
		
    }

    
    
  //ScoreSheet Data From server ////
  	public void GetdataFromWebservice()
  	{
  		progressdial();
  		Thread T2 = new Thread() {
  		@Override
  		public void run() {
  		  try {
				WebServiceCall webcall=new WebServiceCall();
			    WebResult=webcall.Get_OpinionPoll_ScoreSheet(ClientId, Mid+"");
  				 
  			   runOnUiThread(new Runnable()
  	           {
  	            	 public void run()
  	            	 {
  	            		 if(WebResult.contains("@@"))
  	            		 {
  	            	        WebResultArr = WebResult.split("#");
  	            	        
  	            	        if(WebResultArr!=null){
  	            	           QuesNo=1;
  	            		       FillData();///Fill List Data
  	            	        }
  	            		 }else
  	            			AlertDisplay("Technical Issue","Something went wrong");
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

  	
  	 @Override
 	public boolean dispatchTouchEvent(MotionEvent m1){
 		// Call onTouchEvent of SimpleGestureFilter class
 		this.detect.onTouchEvent(m1);
 		return super.dispatchTouchEvent(m1);
     }
  	
    
  	@Override
	public void onSwipe(int direction) {
		// TODO Auto-generated method stub
		switch (direction) {
	      case SimpleGestureFilter.SWIPE_RIGHT :
	    	   Prev();
	           break;
	      case SimpleGestureFilter.SWIPE_LEFT : 
	    	   Next();
	           break;
	      case SimpleGestureFilter.SWIPE_DOWN :  
	           break;
	      case SimpleGestureFilter.SWIPE_UP :  
	    	   break;
	      }
	}
  	
  	
  	@Override
	public void onDoubleTap() {
		// TODO Auto-generated method stub
	}
  	
  	
  	public void Next(){
		if(QuesNo==WebResultArr.length){
    		Toast.makeText(getBaseContext(), "No Further Record", 0).show();
    	}else{
    		QuesNo=QuesNo+1;
    		FillData();
    	}
	}
	
	public void Prev(){
		if(QuesNo-1==0){
    		Toast.makeText(getBaseContext(), "No Previous Record", 0).show();
    	}else{
    		QuesNo=QuesNo-1;
    		FillData();
    	}
	}
  	
  	

    private void FillData() {
    	
    	String Data=WebResultArr[QuesNo-1];
    	String Arr1[]=Data.split("@@");
    	
    	String Op2_Id=Arr1[0];
    	
    	String[] ArrAnswerData=new String[4];
    	
    	ArrAnswerData[0]=Arr1[1].replace("^", "#");
    	ArrAnswerData[1]=Arr1[2].replace("^", "#");
    	ArrAnswerData[2]=Arr1[3].replace("^", "#");
    	ArrAnswerData[3]=Arr1[4].replace("^", "#");
    	
    	String Qry="select Question,Ans1,Ans2,Ans3,Ans4 from C_"+ClientId+"_OP2 Where OP1_ID = "+Mid+" AND M_ID="+Op2_Id+"";
    	
    	SQLiteDatabase db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        Cursor cursor = db.rawQuery(Qry,null);
        String Ques="",Ans1="",Ans2="",Ans3="",Ans4="";
        if (cursor.moveToFirst()){
           Ques = chkVal(cursor.getString(0));
           Ans1 = chkVal(cursor.getString(1));
           Ans2 = chkVal(cursor.getString(2));
           Ans3 = chkVal(cursor.getString(3));
           Ans4 = chkVal(cursor.getString(4));
        }
        cursor.close();
        db.close();
        
        if(Ques.length()>1)
        {
        	txtSno.setVisibility(View.VISIBLE);
            txtSno.setText(QuesNo+"");
            
            String QuesWithAllOptions=Ques;
            		
    		if (!Ans1.equals("")) {
    			QuesWithAllOptions=QuesWithAllOptions+"\n\n 1. "+Ans1;
            }
            if (!Ans2.equals("")) {
            	QuesWithAllOptions=QuesWithAllOptions+"\n 2. "+Ans2;
            }
            if (!Ans3.equals("")) {
            	QuesWithAllOptions=QuesWithAllOptions+"\n 3. "+Ans3;
            }
            if (!Ans4.equals("")) {
            	QuesWithAllOptions=QuesWithAllOptions+"\n 4. "+Ans4;
            }
            
		    txtQues.setText(QuesWithAllOptions);
		  
		    OpenChart(ArrAnswerData);///Display pie chart
        }
    }
    
    
    
    
  //Display pie Chart 
    private void OpenChart(final String[] ArrAnswerData)
    {	
    	int RCount=0;
    	for(int i=0;i<ArrAnswerData.length;i++)
   	    {
   		   if(!ArrAnswerData[i].split("#")[3].equals("0"))
   			  RCount++;
   	   }
    	
    	// Pie Chart Section Names
    	 final String[] code = new String[RCount];
    	 int k=0;
    	 for(int i=0;i<ArrAnswerData.length;i++)
    	 {
    		 if(!ArrAnswerData[i].split("#")[3].equals("0"))
    		 {
    			 code[k]=(i+1)+" - "+ArrAnswerData[i].split("#")[3]+"%  ";
    			 k++;
    		 }	 
    	 }
    	 
    	 // Pie Chart Section Value
    	 final double[] distribution =new double[RCount];
    	 double MajorData=0;
    	 int j=0;
    	 for(int i=0;i<ArrAnswerData.length;i++)
    	 {
    		 if(!ArrAnswerData[i].split("#")[3].equals("0"))
    		 {
    			 distribution[j]=Double.parseDouble(ArrAnswerData[i].split("#")[3]);
    			 j++;
    			 
    			 if(j==1)
        			 MajorData=Double.parseDouble(ArrAnswerData[i].split("#")[3]);
    		 }	 
    	 }
    	 
    	 int C1=Color.parseColor("#FA9B2E");//Orange
    	 int C2=Color.parseColor("#07C0E1");//Sky blue
    	 int C3=Color.parseColor("#61ae15");//Green
    	 int C4=Color.parseColor("#740389");//Purple

    	 // Color of each Pie Chart Sections
    	 int[] colors = { C1, C2, C3,C4};

    	 // Instantiating CategorySeries to plot Pie Chart
    	 CategorySeries distributionSeries = new CategorySeries("");
    	 for (int i = 0; i < distribution.length; i++) {
    	   // Adding a slice with its values and name to the Pie Chart
    	   distributionSeries.add(code[i], distribution[i]);
    	 }

    	 // Instantiating a renderer for the Pie Chart
    	 final DefaultRenderer defaultRenderer = new DefaultRenderer();
    	 for (int i = 0; i < distribution.length; i++) {
    	    SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
    	    seriesRenderer.setColor(colors[i]);
    	    seriesRenderer.setDisplayChartValues(true);
    	    
    	    //Adding colors to the chart
    	    //defaultRenderer.setBackgroundColor(Color.BLACK);
    	    //defaultRenderer.setApplyBackgroundColor(true);
    	    // Adding a renderer for a slice
    	    defaultRenderer.addSeriesRenderer(seriesRenderer);
    	 }

    	 if(MajorData<=10)
    	 {
    	    defaultRenderer.setStartAngle(75);
    	 }
    	 else if(MajorData>10.1 && MajorData<=19.9)
    	 {
    		defaultRenderer.setStartAngle(90);
    	 }
    	 else if(MajorData>=20 && MajorData<=29.9)
    	 {
    		defaultRenderer.setStartAngle(120);
    	 }
    	 else if(MajorData>=30 && MajorData<=70)
    	 {
    	    defaultRenderer.setStartAngle(0);
    	 }
    	 else
    	 {
    		defaultRenderer.setStartAngle(35);
    	 }
    	 defaultRenderer.setScale((float) 0.9);
    	 defaultRenderer.setPanEnabled(false);
    	 defaultRenderer.setClickEnabled(true);
    	 defaultRenderer.setChartTitle("");
    	 //defaultRenderer.setChartTitleTextSize(42);
    	 defaultRenderer.setLabelsTextSize(40);
    	 defaultRenderer.setLabelsColor(Color.parseColor("#004080"));
    	 defaultRenderer.setShowAxes(true);
    	 defaultRenderer.setAxesColor(Color.parseColor("#004080"));
    	 //defaultRenderer.setMargins(new int[] { 20, 30, 15, 20 });
         //defaultRenderer.setZoomButtonsVisible(false);
         defaultRenderer.setZoomEnabled(false);
         defaultRenderer.setShowLegend(false);
         defaultRenderer.setSelectableBuffer(10);

    	 // this part is used to display graph on the xml
    	 // Creating an intent to plot bar chart using dataset and
    	 // multipleRenderer
    	 // Intent intent = ChartFactory.getPieChartIntent(getBaseContext(),
    	 // distributionSeries , defaultRenderer, "AChartEnginePieChartDemo");

    	 // Start Activity
    	 // startActivity(intent);

    	 // remove any views before u paint the chart
    	 Lchart.removeAllViews();
    	 
    	 // drawing pie chart
    	 final View mChart = ChartFactory.getPieChartView(this,distributionSeries, defaultRenderer);
    	 
    	 ////mchart view Click Event
    	 mChart.setOnClickListener(new View.OnClickListener() {
 	        @Override
 	        public void onClick(View v) {
 	          SeriesSelection seriesSelection = ((GraphicalView) mChart).getCurrentSeriesAndPoint();
 	          if (seriesSelection != null) {
 	        	  
 	        	int Poistion =seriesSelection.getPointIndex();
 	            /*for (int i = 0; i < distribution.length; i++) {
 	            	defaultRenderer.getSeriesRendererAt(i).setHighlighted(i ==Poistion );
 	            }
 	            
 	            ((GraphicalView) mChart).repaint();*/
 	             String[] PP=code[Poistion].split("-");
 	             int S_Position=Integer.parseInt(PP[0].trim());
 	             S_Position=S_Position-1;
 	             
 	             Mids = ArrAnswerData[S_Position].split("#")[0]+"#"+ArrAnswerData[S_Position].split("#")[1];
 	             String Head=ArrAnswerData[S_Position].split("#")[2]+" Members - "+ArrAnswerData[S_Position].split("#")[3]+"%";
 	            
 	            Intent intent = new Intent(context, OpinionPoll_MemberList.class);
 	            intent.putExtra("Head", Head);
 	            intent.putExtra("Mids", Mids);
 	            intent.putExtra("MTitle", MTitle);
 	            intent.putExtra("AppLogo", AppLogo);
 	            intent.putExtra("Mid", Mid);
 	            intent.putExtra("CFrom","3");//Comes from OpinionPoll ScoreSheet
 	            startActivity(intent);
 	   	        //finish();
 	          }
 	        }
 	      });
    	 
    	 
    	 // adding the view to the linearlayout
    	 Lchart.addView(mChart,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
    }
    
    
    
    
    private String chkVal(String str) {
        if(str == null) {
            str = "";
        }
        return str;
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
    
    
    
    private void AlertDisplay(String head,String body){
		 AlertDialog ad=new AlertDialog.Builder(this).create();
	    	ad.setTitle( Html.fromHtml("<font color='#E3256B'>"+head+"</font>"));
	    	ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>"+body+"</font>"));
			ad.setButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	  dialog.dismiss();
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