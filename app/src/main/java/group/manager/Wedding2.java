package group.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import group.manager.AdapterClasses.Adapter_Wedding2;
import group.manager.ModelClasses.WeddingPersonDetailsModel;

import java.util.ArrayList;

public class Wedding2 extends Activity implements AdapterView.OnItemClickListener {
    SQLiteDatabase db;
    String name,fathername,age,mobileno,gender;
    byte[] imgByteArray;
    String selectqry = "",selectqry1="";
    ListView lvpersondetails;
    Context context = this;
    String type,Forward_id;
    int m_id;
    ArrayList<WeddingPersonDetailsModel> arrayList;
    int len=0;
    int i;
    String ClientID,ClubName,TableFamilyName;
    byte[] AppLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wedding2);
        
        lvpersondetails = (ListView) findViewById(R.id.listView1);
        
        Intent intent = getIntent();
        type = intent.getStringExtra("Type");
        Forward_id = intent.getStringExtra("Forward_id");
        ClientID =  intent.getStringExtra("UserClubName");
		ClubName =  intent.getStringExtra("Clt_ClubName");
		AppLogo =  intent.getByteArrayExtra("AppLogo");
	
		TableFamilyName="C_"+ClientID+"_Family";
		
		Set_App_Logo_Title();// Set App Logo and Title
        
        lvpersondetails.setOnItemClickListener(this);
        arrayList = new ArrayList<WeddingPersonDetailsModel>();

        //Open Db Connection
    	db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        
        if (Forward_id.equals("filterdetails")) 
        {
            len = WeddingFilterDetails.mid.length;

            for (i = 0; i < len; i++) 
            {
            	selectqry1 = "Select Name, Father, Age, Mob_1, Pic, Text2 from " + TableFamilyName + " Where Text2='" + type + "' AND M_ID = " + WeddingFilterDetails.mid[i] + " AND Text3 = 'true'";
                Cursor cursor1 = db.rawQuery(selectqry1, null);
                cursor1.moveToFirst();
                if (cursor1.getCount() > 0) {
                    name = cursor1.getString(cursor1.getColumnIndex("Name"));
                    fathername = cursor1.getString(cursor1.getColumnIndex("Father"));
                    age = cursor1.getString(cursor1.getColumnIndex("Age"));
                    mobileno = cursor1.getString(cursor1.getColumnIndex("Mob_1"));
                    imgByteArray = cursor1.getBlob(cursor1.getColumnIndex("Pic"));
                    gender = cursor1.getString(cursor1.getColumnIndex("Text2"));
                    arrayList.add(new WeddingPersonDetailsModel(name, fathername, age, mobileno, imgByteArray));
                    lvpersondetails.setAdapter(new Adapter_Wedding2(context, R.layout.listrow, arrayList));
                }
                cursor1.close();
           }
        }
        else
        {
        	 selectqry = "Select Name, Father, Age, Mob_1, Pic, Text2 from " + TableFamilyName + " Where Text2='" + type + "'  AND Text3 = 'true'";
            //    selectqry = "Select Name, Father, Age, Mob_1, Pic, Text2 from "+dbHelper.TABLE_FAMILY +" Where Text2='"+type+"'";
            
            Cursor cursor = db.rawQuery(selectqry, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                do {
                    //   m_id = cursor.getInt(cursor.getColumnIndex("M_ID"));
                    name = cursor.getString(cursor.getColumnIndex("Name"));
                    fathername = cursor.getString(cursor.getColumnIndex("Father"));
                    age = cursor.getString(cursor.getColumnIndex("Age"));
                    mobileno = cursor.getString(cursor.getColumnIndex("Mob_1"));
                    imgByteArray = cursor.getBlob(cursor.getColumnIndex("Pic"));
                    gender = cursor.getString(cursor.getColumnIndex("Text2"));

                    //  Toast.makeText(this,"name = "+name+"Fathers name"+fathername+"age"+age+"mobileno"+mobileno+"pic"+imgByteArray,Toast.LENGTH_LONG).show();
                    arrayList.add(new WeddingPersonDetailsModel(name, fathername, age, mobileno, imgByteArray));
                    lvpersondetails.setAdapter(new Adapter_Wedding2(context, R.layout.listrow, arrayList));
                } while (cursor.moveToNext());
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("No Record found!");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    	dialog.dismiss();
                    	back();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            cursor.close();
        }
        
        db.close();//Close Db Connection
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
    
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        String name =((TextView) view.findViewById(R.id.tvname)).getText().toString();
        selectqry = "Select M_ID from "+TableFamilyName+" Where Name ='"+name+"'";
        
        //Open Db Connection
    	db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        Cursor cursor = db.rawQuery(selectqry,null);
        cursor.moveToFirst();
           m_id = cursor.getInt(cursor.getColumnIndex("M_ID"));
        
        cursor.close();
        db.close();//Close Db Connection
        
        Intent intent = new Intent(this,Wedding3.class);
        intent.putExtra("M_ID",m_id);
        intent.putExtra("Clt_ClubName",ClubName);
        intent.putExtra("UserClubName",ClientID);
        intent.putExtra("AppLogo", AppLogo);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_favorite) {
            Intent in = new Intent(this, WeddingFilterDetails.class);
            in.putExtra("Type",type);
            in.putExtra("Clt_ClubName",ClubName);
            in.putExtra("UserClubName",ClientID);
            in.putExtra("AppLogo", AppLogo);
            startActivity(in);
        }
        return super.onOptionsItemSelected(item);
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
