package group.manager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ReportNext extends Activity{
	Intent menuIntent;
	String StrFrom,StrTo,WebResp,clubname,Number="";
	TextView TxtHead,Txtdate;
	ListView Lvshow;
	String [] temp,sp;
	List<RowItem> rowItems;
	RowItem item;
	Context context=this;
	ListAdapter_Repo adapter;
	AlertDialog.Builder alertDialogBuilder3;
	AlertDialog ad;
	
	public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reportnext);
        TxtHead=(TextView) findViewById(R.id.Txtheader);
        Txtdate=(TextView) findViewById(R.id.Txtdate);
        Lvshow = (ListView)findViewById(R.id.listViewRep);
        
        menuIntent = getIntent(); 
        WebResp =  menuIntent.getStringExtra("WebRsp");
        StrFrom =  menuIntent.getStringExtra("From");
        StrTo =  menuIntent.getStringExtra("To");
        clubname =  menuIntent.getStringExtra("Clt_ClubName");
        
        TxtHead.setText("Booking Details");
        Txtdate.setText("From "+StrFrom+"  To "+StrTo);
        rowItems = new ArrayList<RowItem>();
        
        temp=WebResp.split("@");
        System.out.println(temp.length);
        for(int i=0;i<temp.length;i++){
        	String main=temp[i].toString();
        	sp=main.split("#");
        	 System.out.println(sp.length);
        	for(int j=0;j<sp.length;j++){
	        	String s=sp[j].toString();
	        	s=s.replace("^", "#")+" ";
	        	String[] arr=s.split("#");
	        	String menuname=arr[0];
	        	if(j==0){
	        		 item = new RowItem( menuname, true);
 	        	     rowItems.add(item); 
	    		}
	        	String date=arr[1];
	        	String memno=arr[2];
	        	String name=arr[3];
	        	String mob=arr[4];
	        	String status=arr[5];
	        	String slot=arr[6];
	        	item = new RowItem(name, date, memno, slot, status, false,mob);
	        	rowItems.add(item); 
          }
        }
        adapter = new ListAdapter_Repo(context,R.layout.list_report, rowItems);
        Lvshow.setAdapter(adapter);
        
        Lvshow.setOnItemClickListener(new OnItemClickListener() {
  	    	 public void onItemClick(AdapterView<?> parent, View view, int position, long id){
  	    		 Number =rowItems.get(position).extmob;
  	    	    //System.out.println(email+Number);
  	    	    
  	    	    if((Number==null)||(Number.length()<10)){
  	    	    	alertDialogBuilder3 = new AlertDialog.Builder(context);
		        		 alertDialogBuilder3
		        		 .setTitle( Html.fromHtml("<font color='#E32636'>Error!</font>"))
		        		 .setMessage("Wrong Mobile Number")
			                .setPositiveButton("Cancel",new DialogInterface.OnClickListener() {
			                    public void onClick(DialogInterface dialog,int id) {
			                    	dialog.dismiss();
			                    }
			                });
		        		ad = alertDialogBuilder3.create();
			            ad.show();	
		        	}else if(Number.length()>=10){
		        		alertDialogBuilder3 = new AlertDialog.Builder(context);
		        		Number=Number.substring(Number.length()-10, Number.length());
		        		//System.out.println("cut::  "+Number);
		        		Number= "0"+Number;
			                alertDialogBuilder3
			                .setPositiveButton("Call",new DialogInterface.OnClickListener() {
			                    public void onClick(DialogInterface dialog,int id) {
			                    	callOnphone(Number);
			                    }
			                })
			                .setNegativeButton("Sms",new DialogInterface.OnClickListener() {
			                    public void onClick(DialogInterface dialog,int id) {
			                    	callOnSms(Number);
			                    }
			                });
			                ad = alertDialogBuilder3.create();
			                ad.show();
		        	 }
  	  	        }
  	         });
        
	}
	
	public boolean onKeyDown(int keyCode,KeyEvent event) 
	 {
	   	 if (keyCode == KeyEvent.KEYCODE_BACK) {
	   		finish();
	   	    return true;
	   	 }
	   	return super.onKeyDown(keyCode, event);
	 }

	
	 public void callOnphone(String MobCall) {
			try {
				 Intent callIntent = new Intent(Intent.ACTION_DIAL);
			     callIntent.setData(Uri.parse("tel:"+MobCall));
			     context.startActivity(callIntent);
		       } catch (ActivityNotFoundException activityException) {
		    	   Toast.makeText(context, "Call failed", 0).show();
		       }
			}

		public void callOnSms(String MobCall) {
			try {
				String uri= "smsto:"+MobCall;
	            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
	            intent.putExtra("compose_mode", true);
	            context.startActivity(intent);
		       } catch (ActivityNotFoundException activityException) {
		    	Toast.makeText(context, "Sms failed", 0).show();
		       }
			}
}
