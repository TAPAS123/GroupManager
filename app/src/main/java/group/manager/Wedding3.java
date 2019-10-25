package group.manager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import group.manager.AdapterClasses.Adapter_Wedding3;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Wedding3 extends Activity  {

    ExpandableListView expandableListView;
    List<String> expandableListHeader;
    HashMap<String, List<String>> expandableListChild;
    SQLiteDatabase db;
    int M_ID;
    String name,relation,father,mother,address,current_loc,mob1,mob2,dob_d,dob_m,dob_y,emailid,age,birthtime,birthplace,gotra;
    String remark,education,workprofile,text1,gender,shareWithMatrimony,designation,nativePlace,height,manglik,annualIncome;
    String workAfterMarriage,diet,fatherstatus,motherstatus,brother_all_details,sister_all_details,marriage_additional_info;
    byte[] imgByteArray;
    ImageView imgperson;
    TextView tvFamilyName,tvFamilyGender,tvFamilyGotra,tvFamilyDOB,tvFamilyPOB,tvFamilyTOB;
    ExpandableListAdapter listAdapter;
    String ClientID,ClubName,TableFamilyName;
    byte[] AppLogo;

        int feet = 0;
        int inches= 0;
    String no_of_brothers ="", no_of_sisters = "",in_which_sis_married ="",sister_details ="",in_which_bro_married ="",brother_details ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wedding3);
        
        imgperson = (ImageView)findViewById(R.id.imageViewMemPic);
        tvFamilyName = (TextView)findViewById(R.id.tvFamilyName);
        tvFamilyGender = (TextView)findViewById(R.id.tvFamilyGender);
        tvFamilyGotra = (TextView)findViewById(R.id.tvFamilyGotra);
        tvFamilyDOB = (TextView)findViewById(R.id.tvFamilyDOB);
        tvFamilyPOB = (TextView)findViewById(R.id.tvFamilyPOB);
        tvFamilyTOB = (TextView)findViewById(R.id.tvFamilyTOB);
        expandableListView = (ExpandableListView)findViewById(R.id.expandableListView);
        
        Intent intent =getIntent();
        M_ID =  intent.getIntExtra("M_ID",0);
        ClientID =  intent.getStringExtra("UserClubName");
		ClubName =  intent.getStringExtra("Clt_ClubName");
		AppLogo =  intent.getByteArrayExtra("AppLogo");
	
		TableFamilyName="C_"+ClientID+"_Family";
		
		Set_App_Logo_Title();// Set App Logo and Title
        
        getData(M_ID);
        
        if(imgByteArray!= null) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(imgByteArray);
            Bitmap img = BitmapFactory.decodeStream(imageStream);
            imgperson.setImageBitmap(img);
        } 
    	else 
        {
    		imgperson.setImageResource(R.drawable.person1);
        }

        tvFamilyName.setText(name);
        tvFamilyGender.setText(gender);
        tvFamilyGotra.setText(gotra);
        
        if(!(dob_d.equals("") && dob_m.equals("") && dob_y.equals("")))
          tvFamilyDOB.setText(dob_d+"/"+dob_m+"/"+dob_y);
        
        tvFamilyPOB.setText(birthplace);
        tvFamilyTOB.setText(birthtime);
        prepareListData();
        listAdapter = new Adapter_Wedding3(this, expandableListHeader, expandableListChild);

        // setting list adapter
        expandableListView.setAdapter(listAdapter);
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
    
    
    private void getData(int m_id) 
    {
        try
        {
        	//Open Db Connection
        	db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        	
            String selectqry = "Select Name, Relation, Father, Mother, Address, Current_Loc, Mob_1, Mob_2, DOB_D, DOB_M, DOB_Y, EmailID, Age, Birth_Time, Birth_Place, Gotra, Remark, Pic, Education, Work_Profile, " +
                    "Text2, Text3, Text4, Text5, Height, Text6, Text7, Text8, Text9," +
                    " Text10, Text11, Text12, Text13, Text14 from " + TableFamilyName + " where M_ID = " + m_id;

            Cursor cursor = db.rawQuery(selectqry, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0)
            {
                //   m_id = cursor.getInt(cursor.getColumnIndex("M_ID"));
                name = ChkVal(cursor.getString(cursor.getColumnIndex("Name")));
                relation = ChkVal(cursor.getString(cursor.getColumnIndex("Relation")));
                father = ChkVal(cursor.getString(cursor.getColumnIndex("Father")));
                mother = ChkVal(cursor.getString(cursor.getColumnIndex("Mother")));
                address = ChkVal(cursor.getString(cursor.getColumnIndex("Address")));
                current_loc = ChkVal(cursor.getString(cursor.getColumnIndex("Current_Loc")));
                mob1 = ChkVal(cursor.getString(cursor.getColumnIndex("Mob_1")));
                mob2 = ChkVal(cursor.getString(cursor.getColumnIndex("Mob_2")));
                dob_d = ChkVal(cursor.getString(cursor.getColumnIndex("DOB_D")));
                dob_m = ChkVal(cursor.getString(cursor.getColumnIndex("DOB_M")));
                dob_y = ChkVal(cursor.getString(cursor.getColumnIndex("DOB_Y")));
                emailid = ChkVal(cursor.getString(cursor.getColumnIndex("EmailID")));
                age = ChkVal(cursor.getString(cursor.getColumnIndex("Age")));
                birthtime = ChkVal(cursor.getString(cursor.getColumnIndex("Birth_Time")));
                birthplace = ChkVal(cursor.getString(cursor.getColumnIndex("Birth_Place")));
                gotra = ChkVal(cursor.getString(cursor.getColumnIndex("Gotra")));
                remark = ChkVal(cursor.getString(cursor.getColumnIndex("Remark")));
                imgByteArray = cursor.getBlob(cursor.getColumnIndex("Pic"));
                education = ChkVal(cursor.getString(cursor.getColumnIndex("Education")));
                workprofile = ChkVal(cursor.getString(cursor.getColumnIndex("Work_Profile")));
                gender = ChkVal(cursor.getString(cursor.getColumnIndex("Text2")));     //Gender
                shareWithMatrimony = ChkVal(cursor.getString(cursor.getColumnIndex("Text3"))); //Share with matrimony
                designation = ChkVal(cursor.getString(cursor.getColumnIndex("Text4")));
                nativePlace = ChkVal(cursor.getString(cursor.getColumnIndex("Text5")));
                height = ChkVal(cursor.getString(cursor.getColumnIndex("Height")));
                manglik = ChkVal(cursor.getString(cursor.getColumnIndex("Text6")));
                annualIncome = ChkVal(cursor.getString(cursor.getColumnIndex("Text7")));
                workAfterMarriage = ChkVal(cursor.getString(cursor.getColumnIndex("Text8")));
                diet = ChkVal(cursor.getString(cursor.getColumnIndex("Text9")));
                fatherstatus = ChkVal(cursor.getString(cursor.getColumnIndex("Text10")));
                motherstatus = ChkVal(cursor.getString(cursor.getColumnIndex("Text11")));
                brother_all_details = ChkVal(cursor.getString(cursor.getColumnIndex("Text12")));
                sister_all_details = ChkVal(cursor.getString(cursor.getColumnIndex("Text13")));
                marriage_additional_info = ChkVal(cursor.getString(cursor.getColumnIndex("Text14")));
                
                if(brother_all_details.contains("^"))
                {
                  String[] arrbrother = brother_all_details.split("\\^");
                  no_of_brothers =arrbrother[0];
                  in_which_bro_married =arrbrother[1];
                  brother_details =arrbrother[2];
                }
                
                if(sister_all_details.contains("^"))
                {
                  String arrsister[]=sister_all_details.split("\\^");
                  no_of_sisters =arrsister[0];
                  in_which_sis_married =arrsister[1];
                  sister_details =arrsister[2];
                }
            }
            cursor.close();
            db.close();//Close Db Connection
        }
        catch (Exception e)
        {
            String error = e.toString();
            Log.d("database error",error);
            db.close();//Close Db Connection
        }
    }

    private String ChkVal(String Val)
    {
        if(Val == null)
        	Val = "";
        
        return Val.trim();
    }

    private void prepareListData() {
        expandableListHeader = new ArrayList<String>();
        expandableListChild = new HashMap<String, List<String>>();

        // Adding child data
        expandableListHeader.add("Personal Details");
        expandableListHeader.add("Address");
        expandableListHeader.add("Occupation Details");
        expandableListHeader.add("Family Details");

        // Adding child data
        List<String> PersonalDetails = new ArrayList<String>();

            if(!father.equals(""))
            {
                PersonalDetails.add("<B><font color=#6F6F6F>Father's Name: </font></B>" + father);
            }
            if(!mother.equals(""))
            {
                PersonalDetails.add("<B><font color=#6F6F6F>Mother's Name: </font></B>" + mother);
            }
            if(!relation.equals(""))
            {
                PersonalDetails.add("<B><font color=#6F6F6F>Relation: </font></B>" + relation);
            }
            if(!mob1.equals(""))
            {
                PersonalDetails.add("<B><font color=#6F6F6F>Mob1: </font></B>" + mob1);
            }
            if(!mob2.equals(""))
            {
                PersonalDetails.add("<B><font color=#6F6F6F>Mob2: </font></B>" + mob2);
            }
            if(!emailid.equals(""))
            {
                PersonalDetails.add("<B><font color=#6F6F6F>Email ID: </font></B>" + emailid);
            }

             convertheight(height);

            if(!height.equals(""))
            {
                PersonalDetails.add("<B><font color=#6F6F6F>Height: </font></B>" + feet + "ft " + inches + "inches (" + height + "cm)");
            }
            if(!manglik.equals(""))
            {
                PersonalDetails.add("<B><font color=#6F6F6F>Manglik: </font></B>" + manglik);
            }
            if(!diet.equals(""))
            {
                PersonalDetails.add("<B><font color=#6F6F6F>Diet: </font></B>" + diet);
            }

        List<String> Address = new ArrayList<String>();

            if(!current_loc.equals(""))
            {
                Address.add("<B><font color=#6F6F6F><font color=#6F6F6F>Current Loc: </font></B>" + current_loc);
            }
            if(!nativePlace.equals(""))
            {
                Address.add("<B><font color=#6F6F6F>Native Place: </font></B>" + nativePlace);
            }


        List<String> OccupationDetails = new ArrayList<String>();


            if(!workprofile.equals(""))
            {
                OccupationDetails.add("<B><font color=#6F6F6F>Work Profile: </font></B>" + workprofile);
            }
            if(!designation.equals(""))
            {
                OccupationDetails.add("<B><font color=#6F6F6F>Designation: </font></B>" + designation);
            }
            if(!annualIncome.equals(""))
            {
                OccupationDetails.add("<B><font color=#6F6F6F>Annual Income: </font></B>" + annualIncome);
            }

        List<String> FamilyDetails = new ArrayList<String>();


            if(!fatherstatus.equals(""))
            {
                FamilyDetails.add("<B><font color=#6F6F6F>Father Status: </font></B>" + fatherstatus);
            }
            if(!motherstatus.equals(""))
            {
                FamilyDetails.add("<B><font color=#6F6F6F>Mother Status: </font></B>" + motherstatus);
            }
            if(!no_of_brothers.equals(""))
            {
                FamilyDetails.add("<B><font color=#6F6F6F>No of Brothers: </font></B>" + no_of_brothers);
            }
            if(!in_which_bro_married.equals(""))
            {
                FamilyDetails.add("<B><font color=#6F6F6F>in which married: </font></B>" + in_which_bro_married);
            }
            if(!brother_details.equals(""))
            {
                FamilyDetails.add("<B><font color=#6F6F6F>Brother Details: </font></B>" + brother_details);
            }
            if(!no_of_sisters.equals(""))
            {
                FamilyDetails.add("<B><font color=#6F6F6F>No of Sisters: </font></B>" + no_of_sisters);
            }
            if(!in_which_sis_married.equals(""))
            {
                FamilyDetails.add("<B><font color=#6F6F6F>in which married: </font></B>" + in_which_sis_married);
            }
            if(!sister_details.equals(""))
            {
                FamilyDetails.add("<B><font color=#6F6F6F>Sister Details: </font></B>" + sister_details);
            }
            if(!marriage_additional_info.equals(""))
            {
                FamilyDetails.add("<B><font color=#6F6F6F>Marriagle Additional Info: </font></B>" + marriage_additional_info);
            }

        expandableListChild.put(expandableListHeader.get(0), PersonalDetails); // Header, Child data
        expandableListChild.put(expandableListHeader.get(1), Address);
        expandableListChild.put(expandableListHeader.get(2), OccupationDetails);
        expandableListChild.put(expandableListHeader.get(3), FamilyDetails);
    }

    private void convertheight(String height) {
        if(height.length()>0)
        {
            //convert height from cm to inches
            double TotalInch=Double.parseDouble(height)/2.54;
            feet=(int)TotalInch/12;
            inches=(int)TotalInch%12;
        }
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
