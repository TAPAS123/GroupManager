package group.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Newsletter extends Activity {

    ExpandableListView expandablelistview;
    ExpandableListAdapter expandableListAdapter;
    List<RowEnvt> expandableListHeader;
    HashMap<RowEnvt, List<RowEnvt>> expandableListChild;
    ArrayList<RowEnvt>  obj1;
    SQLiteDatabase db;
    Adapter_NewsLetter exadapter;
    String qry,qry2;
    String ClubName,ClientId,MTitle;
    int countrow = 0;
    Context context = this;
    byte[] AppLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsletter);
        expandablelistview = (ExpandableListView)findViewById(R.id.expandablelistview);
        TextView txtHead = (TextView)findViewById(R.id.txtHead);
        
        Intent intent = getIntent();
        MTitle=intent.getStringExtra("MTitle");
		ClubName =  intent.getStringExtra("Clt_ClubName");
		ClientId =  intent.getStringExtra("UserClubName");
		AppLogo =  intent.getByteArrayExtra("AppLogo");
        
		Set_App_Logo_Title(); // Set App Logo and Title
		
		txtHead.setText(MTitle);

        expandableListHeader = new ArrayList<RowEnvt>();
        expandableListChild = new HashMap<RowEnvt, List<RowEnvt>>();
        obj1 = new ArrayList<RowEnvt>();
        
        db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        qry = "select DISTINCT Text1 from C_"+ClientId+"_4 where Rtype = 'NLetter' order by Num1 Desc" ;
        Cursor cursor1 = db.rawQuery(qry,null);
        if(cursor1.moveToFirst())
        {
            do {
                String year = cursor1.getString(0);
                qry2 = "select Text2,Text3 from C_"+ClientId+"_4 where Text1 = '"+year+"' AND Rtype = 'NLetter'";
                Cursor cursor2 = db.rawQuery(qry2,null);
                if(cursor2.moveToFirst())
                {
                    do {
                        String url = cursor2.getString(0);
                        String month = cursor2.getString(1);
                        obj1.add(new RowEnvt(month,url));
                    } while (cursor2.moveToNext());
                }

                expandableListHeader.add(new RowEnvt(year,""));
                expandableListChild.put(expandableListHeader.get(countrow), obj1);
                obj1 = new ArrayList<RowEnvt>();
                countrow++;
            }while (cursor1.moveToNext());
            exadapter = new Adapter_NewsLetter(this,expandableListHeader,expandableListChild);
            expandableListAdapter = exadapter;
            expandablelistview.setAdapter(expandableListAdapter);
        }
        else
        {
        	AlertDisplay("No Data","No Record found !");
        }

       expandablelistview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if(chkpdfreader()) {
                	RowEnvt rowItem = (RowEnvt) exadapter.getChild(groupPosition, childPosition);
                    String pdf_url = rowItem.EvtDesc;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf_url));
                    startActivity(browserIntent);
                }
                else
                {
                    AlertDisplay("PDF Reader issue","PDF Reader not available !");
                }
                return true;
            }
        });

    }

    public boolean chkpdfreader() {
        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        if (packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
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
	   Intent intent= new Intent(getBaseContext(),MenuPage.class);
	   intent.putExtra("AppLogo", AppLogo);
	   startActivity(intent);
	   finish();
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
    
}
