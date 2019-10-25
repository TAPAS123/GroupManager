package group.manager;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;

public class ShowBloodGroup extends Activity {
	LinearLayout LayBlood,laybloodsingle,layCity;
	Intent menuIntent;
	Context context=this;
	byte[] AppLogo,imgP;
	Bitmap imgsh;
	ArrayList<Product>products;
	SQLiteDatabase db;
	String sqlSearch,StrSelectedBlood,ClientID="",LogclubName="",checkcomingfrom="0",Tab2Name,StrNameBlood,StrMobBlood,hd_val,StrBG,
			ValueID="",mobblooddonor,StrCity="";
	Cursor cursorT;
	Button BtnSearchblood,btnsms;
	Spinner SpBlood,spcity;
	String [] bloodArr={"A+","B+","AB+","O+","A-","B-","AB-","O-"};
	ArrayAdapter<String> adapterflag,adpt;
	AlertDialog.Builder alertDialogBuilder3;
	AlertDialog ad;
	String[] Arr_C_Ids,Arr_C_Ids_Values,Arr_blood_id,CodeArr;
	ListView ListViewsingleblood;
	TextView tvsinlebolld;
	RowEnvt item;
	List<RowEnvt> rowItems;
	CustomNews adapt;
	SettingsListAdapter ExpAdapter;
	//ArrayList<Category> ExpListItems;
	ExpandableListView ExpandList;
	Dialog dialog;
	ArrayList<Category> categories;
	Bitmap bt;
	String name,mob,StrmobNo;
	String [] stockArr;
	ArrayList<String> fulllistsms;
	ListAdapter2 boxAdapter;
	ActionBar actionBar;
	CheckBox cHkboxall;
	Button ivsms; 
	SharedPreferences shrd;
	String smsbody="",smsbody2="",smsbody3="",STR_Remark,STR_Remark2,STR_Remark3;
	EditText EtremarkDialog,EtremarkDialog2,EtremarkDialog3;
	CheckBox Chkboxremrk,Chkboxremrk2,Chkboxremrk3;
	Editor edits;
	RelativeLayout rrsingle;
	int valcity=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bloodgroup);
		
		laybloodsingle=(LinearLayout) findViewById(R.id.linearLayoutsgl);
		rrsingle=(RelativeLayout) findViewById(R.id.linearLayoutsingledata);
		tvsinlebolld=(TextView) findViewById(R.id.textViewbloodGrname);
		ListViewsingleblood=(ListView) findViewById(R.id.listViewbloodgr);
		ivsms=(Button) findViewById(R.id.buttonSMSBloodsend);
		BtnSearchblood=(Button)findViewById(R.id.btnSearchblood);
		btnsms=(Button)findViewById(R.id.buttonSMS);
		SpBlood=(Spinner)findViewById(R.id.spinnerblood);
		LayBlood=(LinearLayout) findViewById(R.id.linearLayoutBlood);
		layCity=(LinearLayout) findViewById(R.id.splaycity);
		ExpandList = (ExpandableListView) findViewById(R.id.exp_list);
		spcity=(Spinner)findViewById(R.id.spinnercity);
		
		Set_App_Logo_Title();
		
		fulllistsms = new ArrayList<String>();
		adapterflag= new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,bloodArr);
		adapterflag.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		SpBlood.setAdapter(adapterflag); 
		
		ActionCall();
		cHkboxall.setVisibility(View.GONE);
		db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		String sql="Select distinct(UPPER(trim(M_City))) from C_"+ClientID+"_2 Where M_City is not null and length(M_City) <> 0 and M_City<>' ' order by  trim(M_City)";
		Cursor cursorT = db.rawQuery(sql, null);
		int i=1;
		int tempsize=cursorT.getCount();
		if(tempsize>1){
			valcity=1;
			layCity.setVisibility(View.VISIBLE);
			String [] CodeArr=new String[tempsize+1];
	        CodeArr[0]="All";
		    if(cursorT.moveToFirst()){
		      do {
		    	  CodeArr[i]=cursorT.getString(0);
		    	  i++;
		    	} while (cursorT.moveToNext());
		     }
		     cursorT.close();
		     db.close();
		     
		     adpt= new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,CodeArr);
		     adpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		     spcity.setAdapter(adpt); 
		}else{
			valcity=0;
			layCity.setVisibility(View.GONE);	
		}
       
	     
	     ivsms.setOnClickListener(new OnClickListener(){ 
	 	     public void onClick(View arg0){ 
	 	    	 StrmobNo="";
			      for (Product p : boxAdapter.getBox()) {
			       if (p.box){
			    	   //p.mob = p.mob.substring(0, p.mob.length()-1).trim();
			    	   if(p.mob.length()!=0){
			    		   p.mob = p.mob.substring(p.mob.length()-10);
				    	   p.mob= "0"+p.mob;  
				           StrmobNo +=p.mob+","; 
			    	   }
			        }
			      }
			      if(StrmobNo.length()!=0){
			    	  StrmobNo = StrmobNo.substring(0, StrmobNo.length()-1).trim();
			    	  showremark(StrmobNo);
			      }else{
			    	  Toast.makeText(getApplicationContext(), "No check box selected", 1).show();
			      } 
	 	     }
	     });
	     
		BtnSearchblood.setOnClickListener(new OnClickListener(){ 
	 	     public void onClick(View arg0){ 
	 	    	StrSelectedBlood= SpBlood.getSelectedItem().toString().trim();
	 	    	if(valcity==1){
	 	    		StrCity= spcity.getSelectedItem().toString().trim();	
	 	    	}else{
	 	    		StrCity="";
	 	    	}
	 	    	
	 			Get_LoginMain_Data();
	 			if(Arr_C_Ids_Values.length==0){
	 				showalert();
	 			}else{
	 				if((StrCity==null)||(StrCity.length()==0)||(StrCity.equals("All"))){
	 					StrCity="";
	 				}else{
	 					StrCity=" and UPPER(M_City)='"+StrCity+"' ";
	 				}
	 				
	 				categories = SetStandardGroups();
	 		        int sz=categories.size();
	 		        System.out.println("list: "+sz);
	 		        if(sz==0){
	 		           laybloodsingle.setVisibility(View.VISIBLE);
	 		           tvsinlebolld.setText("No record found!");	
	 		        }else if(sz==1){
	 		        	laybloodsingle.setVisibility(View.VISIBLE);
	 		        	cHkboxall.setVisibility(View.VISIBLE);
	 		        	ivsms.setVisibility(View.VISIBLE);
	 		        	tvsinlebolld.setText("");	
	 		        	FillproductList(false);
	 		        }else{
	 		        	LayBlood.setVisibility(View.VISIBLE);
	 		        	tvsinlebolld.setText("");
	 		        	ExpAdapter = new SettingsListAdapter(context,categories, ExpandList);
	 	 		        ExpandList.setAdapter(ExpAdapter);
	 		        }
	 		        
	 		       btnsms.setOnClickListener(new OnClickListener(){ 
	 			 	     public void onClick(View arg0){
	 			 	    	 System.out.println(fulllistsms.size());
	 			 	    	 String ss="";
	 			 	    	 if(fulllistsms!=null){
	 			 	    		String [] stockArr = fulllistsms.toArray(new String[fulllistsms.size()]); 
	 			 	    		 //String []Arr=stockArr;
	 			 	    		System.out.println("len:  "+stockArr.length); 
	 			 	    		for(int i=0;i<stockArr.length;i++){
	 			 	    			String tt=stockArr[i].toString();
	 			 	    			ss=ss+tt+",";
	 			 	    		}
	 			 	    		System.out.println(ss);
	 			 	    		//SMSCallactivity(ss);
	 			 	    	 }
	 			 	    	if(ss.length()!=0){
	 					    	  ss = ss.substring(0, ss.length()-1).trim();
	 					    	  showremark(ss);
	 					      }else{
	 					    	  Toast.makeText(getApplicationContext(), "No check box selected", 1).show();
	 					      }
	 			 	      }
	 			 	 });
	 		        
	 		      ExpandList.setOnGroupClickListener(new OnGroupClickListener() {
	 		         @Override
	 		         public boolean onGroupClick(ExpandableListView parent, View clickedView, final int groupPosition, final long groupId) {
	 		        	final CheckedTextView checkbox1 = (CheckedTextView)clickedView.findViewById(R.id.list_item_text_groupval);
	 		        	checkbox1.toggle();
		            	if(checkbox1.isChecked()==true){
		            		for(int i=0; i<categories.get(groupPosition).children.size(); i++){
		            			String tt= categories.get(groupPosition).children.get(i).name;
		            			categories.get(groupPosition).selection.add(tt);
			            		System.out.println(tt);
			            		fulllistsms.add(tt);
			                  }
	            		 }else{
	            			 for(int i=0; i<categories.get(groupPosition).children.size(); i++){
			            		categories.get(groupPosition).selection.remove(i);
			            		String tt= categories.get(groupPosition).children.get(i).name;
			            		System.out.println(i+" "+tt);
			            		fulllistsms.remove(tt);
				             }
	            		 }
						
	 		             return false;
	 		         }
	 		        });
	 		      
	 		      
	 		      ExpandList.setOnChildClickListener(new OnChildClickListener() {
						@Override
						public boolean onChildClick(ExpandableListView parent,View v, int groupPosition, int childPosition,long id) {
							// TODO Auto-generated method stub
							bt=null;name="";mob="";
							System.out.println(groupPosition+"  @@:  "+childPosition);
							CheckedTextView checkbox = (CheckedTextView)v.findViewById(R.id.list_item_text_child);
							checkbox.toggle();
							// find parent view by tag
							System.out.println("@@:  "+categories.get(groupPosition).name);
						   /*View parentView = ExpandList.findViewWithTag(categories.get(groupPosition).name);
							//if(parentView != null) {
								TextView sub = (TextView)parentView.findViewById(R.id.list_item_text_subscriptions);
								if(sub != null) {
									Category category = categories.get(groupPosition);*/
							        
									if(checkbox.isChecked()) {
										// add child category to parent's selection list
										categories.get(groupPosition).selection.add(checkbox.getText().toString());
										System.out.println("mob  @@:  "+checkbox.getText().toString());
										fulllistsms.add(checkbox.getText().toString());
										// sort list in alphabetical order
										Collections.sort(categories.get(groupPosition).selection, new CustomComparator());
										 //System.out.println("len:  "+stockArr.length);
									}
									else {
										// remove child category from parent's selection list
										categories.get(groupPosition).selection.remove(checkbox.getText().toString());
										fulllistsms.remove(checkbox.getText().toString());
									}
									if(categories.get(groupPosition).children.get(childPosition).Image!=null){
										System.out.println("2: "+groupPosition+"  @@:  "+childPosition);
										bt=categories.get(groupPosition).children.get(childPosition).Image;
										name=categories.get(groupPosition).children.get(childPosition).val2;
										mob=categories.get(groupPosition).children.get(childPosition).name;
										dialog = new Dialog(context);
										dialog.setContentView(R.layout.showbigimage);
										ImageView IVzoomimage = (ImageView)dialog.findViewById(R.id.webView1Imagezoom);
										ImageView Btnsms = (ImageView)dialog.findViewById(R.id.buttonsmszoom);
										ImageView Btncall = (ImageView)dialog.findViewById(R.id.buttoncallzome);
										IVzoomimage.setImageBitmap(bt);
										dialog.setTitle("Donor name: "+name);
										Btnsms.setOnClickListener(new OnClickListener(){ 
									            public void onClick(View arg0){ 
									            	dialog.dismiss();
									            	 showremark(mob);
									            	//SMSCallactivity(mob);
									            }
									        }); 
										Btncall.setOnClickListener(new OnClickListener(){ 
								            public void onClick(View arg0){ 
								            	callOnphone(mob);
								            	dialog.dismiss();
								            }
								        }); 
										dialog.show();
									}
							return true;
						   }
		    	       });
	 			} 
	 			db.close();
	 	     }
	 	   
	     });
	}

	/// Updated On 14-03-2019
	public void callOnSms(String MobNos,String messageBody ) {
		new CommonClass().SEND_SMS_WithBody(context,MobNos,messageBody);
	}
	
	 private void showremark(final String StrmobNo) {
	     dialog = new Dialog(context);
	     dialog.setContentView(R.layout.listalertdialog);
         dialog.setTitle("Type Message here..."); 
	     getsharedprefrencevalue();
	     ScrollView scremark=(ScrollView)dialog.findViewById(R.id.scrollViewRemark);
	     scremark.setVisibility(View.VISIBLE);
	     EtremarkDialog = (EditText)dialog.findViewById(R.id.editTextEmark1);
		 EtremarkDialog.setGravity(Gravity.LEFT | Gravity.TOP);  
		 EtremarkDialog2 = (EditText)dialog.findViewById(R.id.editTextEmark2);
		 EtremarkDialog2.setGravity(Gravity.LEFT | Gravity.TOP);  
		 EtremarkDialog3 = (EditText)dialog.findViewById(R.id.editTextEmark3);
		 EtremarkDialog3.setGravity(Gravity.LEFT | Gravity.TOP);  
		 Chkboxremrk=(CheckBox)dialog.findViewById(R.id.checkBoxRemark1);
		 Chkboxremrk2=(CheckBox)dialog.findViewById(R.id.checkBoxRemark2);
		 Chkboxremrk3=(CheckBox)dialog.findViewById(R.id.checkBoxRemark3);
		 Button BTNlistBack = (Button)dialog.findViewById(R.id.buttonListalrt);
		 Button BtnSaveDialog = (Button)dialog.findViewById(R.id.buttonSave);
		 BtnSaveDialog.setVisibility(View.VISIBLE);
		 BTNlistBack.setText("Cancel");
		 BtnSaveDialog.setText("Send Sms");
		 EtremarkDialog.setText(smsbody); 
		 EtremarkDialog2.setText(smsbody2); 
		 EtremarkDialog3.setText(smsbody3); 
		 dialog.show();
		 
		 Chkboxremrk.setOnClickListener(new OnClickListener(){ 
		       public void onClick(View arg0){ 
		    	   STR_Remark= EtremarkDialog.getText().toString().trim();
		    	   if((Chkboxremrk.isChecked()==true)&&(STR_Remark.length()==0)){
		    		   EtremarkDialog.setError("Select first remark.");
		    		   Chkboxremrk2.setChecked(false);
		    		   Chkboxremrk3.setChecked(false);
		    		   Chkboxremrk.setChecked(false);
		    		  
		    	   }else if((Chkboxremrk.isChecked()==true)&&(STR_Remark.length()!=0)){
		    		   Chkboxremrk2.setChecked(false);
		    		   Chkboxremrk3.setChecked(false);
		    	   }
			     }
			  });  
		 
		 Chkboxremrk2.setOnClickListener(new OnClickListener(){ 
		       public void onClick(View arg0){ 
		    	   STR_Remark2= EtremarkDialog2.getText().toString().trim();
		    	   if((Chkboxremrk2.isChecked()==true)&&(STR_Remark2.length()==0)){
		    		   EtremarkDialog2.setError("Select second remark.");
		    		   Chkboxremrk.setChecked(false);
		    		   Chkboxremrk3.setChecked(false);
		    		   Chkboxremrk2.setChecked(false);
			    	  
			        }else if((Chkboxremrk2.isChecked()==true)&&(STR_Remark2.length()!=0)){
			    		Chkboxremrk.setChecked(false);
			    		Chkboxremrk3.setChecked(false);
			    	}
			     }
			  });  
		 
		 Chkboxremrk3.setOnClickListener(new OnClickListener(){ 
		       public void onClick(View arg0){ 
		    	   STR_Remark3= EtremarkDialog3.getText().toString().trim();
		    	   if((Chkboxremrk3.isChecked()==true)&&(STR_Remark3.length()==0)){
		    		   EtremarkDialog3.setError("Select second remark.");
		    		   Chkboxremrk.setChecked(false);
		    		   Chkboxremrk2.setChecked(false);
		    		   Chkboxremrk3.setChecked(false);
			    	   
			        }else if((Chkboxremrk3.isChecked()==true)&&(STR_Remark3.length()!=0)){
			    		Chkboxremrk.setChecked(false);
			    		Chkboxremrk2.setChecked(false);
			    	}
			     }
			  });  
		 
		    BTNlistBack.setOnClickListener(new OnClickListener(){ 
		       public void onClick(View arg0){ 
		          dialog.dismiss();
		         }
		     });  
		    
		    BtnSaveDialog.setOnClickListener(new OnClickListener(){ 
			       public void onClick(View arg0){
			    	   STR_Remark= EtremarkDialog.getText().toString().trim();
			    	   STR_Remark2= EtremarkDialog2.getText().toString().trim();
			    	   STR_Remark3= EtremarkDialog3.getText().toString().trim();
			    	   if(StrmobNo.length()==0){
				    	      Toast.makeText(getApplicationContext(), "Select atleast one number!", 1).show();
				       }else if(Chkboxremrk.isChecked()==true){
			    		   callOnSms(StrmobNo,STR_Remark);
			    		   editorValue(STR_Remark,STR_Remark2,STR_Remark3);
			    		   dialog.cancel();
			    	   }else if(Chkboxremrk2.isChecked()==true){
			    		   callOnSms(StrmobNo,STR_Remark2);
			    		   editorValue(STR_Remark,STR_Remark2,STR_Remark3);
			    		   dialog.cancel();
			    	   }else if(Chkboxremrk3.isChecked()==true){
			    		   callOnSms(StrmobNo,STR_Remark3);
			    		   editorValue(STR_Remark,STR_Remark2,STR_Remark3);
			    		   dialog.cancel();
			    	   }else{
			    		   editorValue(STR_Remark,STR_Remark2,STR_Remark3);
			    		   Toast.makeText(getApplicationContext(), "No message is select..", 1).show();
			    	   }
			      }
			 });  
	 }
	 
	 private void editorValue(String smsbody,String smsbody2,String smsbody3) {
		 edits = shrd.edit();
		 edits.putString("SmsSd", smsbody);
		 edits.putString("SmsSd2", smsbody2);
		 edits.putString("SmsSd3", smsbody3);
		 edits.commit();
	 }
	
	 private void getsharedprefrencevalue() {
		 shrd = getSharedPreferences("BloodPref", Context.MODE_PRIVATE);
	      if (shrd.contains("SmsSd"))
	      {
	    	  smsbody= shrd.getString("SmsSd", "");
	      }  
	      if (shrd.contains("SmsSd2"))
	      {
	    	  smsbody2= shrd.getString("SmsSd2", "");
	      } 
	      if (shrd.contains("SmsSd3"))
	      {
	    	  smsbody3= shrd.getString("SmsSd3", "");
	      } 
	      System.out.println("@"+smsbody);
	 }
	
	 
	 
	 public Category get(String name)
	 {
	   ArrayList<Category> collection = SetStandardGroups();
		for (Iterator<Category> iterator = collection.iterator(); iterator.hasNext();) {
			Category cat = (Category) iterator.next();
			if(cat.name.equals(name)) {
				return cat;
			}
		}
	   return null;
	 }
	
	 public ArrayList<Category> SetStandardGroups() {
		 ValueID="";
		 ArrayList<Category> categories = new ArrayList<Category>();
	  	    for( int i = 0 ; i < Arr_C_Ids_Values.length ; ++i ){
	  		   String hd_val=Arr_C_Ids_Values[i].toString().trim();
	  		   if(hd_val.length()>0){
	  			  int Countid=0;
	  			  ClientID= Arr_C_Ids[i].toString();
	  			  Tab2Name="C_"+ClientID+"_2";
	  			  db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
	  			  sqlSearch="Select Count(M_Id) from "+Tab2Name+" Where M_BG='"+StrSelectedBlood+"'"+StrCity+" and( M_Mob is not null or LENGTH(M_Mob)<>0) and( M_Name is not null or LENGTH(M_Name)<>0) order by M_Name";
	 		      cursorT = db.rawQuery(sqlSearch, null);
	 		      if (cursorT.moveToFirst()) {
				     Countid =cursorT.getInt(0);
			  	  }
	 		      cursorT.close(); 
	 		      if(Countid!=0){
	 		    	Category cat = new Category(hd_val,String.valueOf(Countid));
	 		    	generateChildren(cat);
	 		    	categories.add(cat);
	 		      }
	 		      db.close();
	  		   }
	  	    }
	       return categories;
	    }

	 private void generateChildren(Category cat)
	 {
		 String PrefName="";
		 sqlSearch="Select M_Name,M_Mob,M_Pic,(C4_BG || \"\" || ifnull(C4_DOB_Y,'')) as [Prefix] from "+Tab2Name+" Where M_BG='"+StrSelectedBlood+"'"+StrCity+" and( M_Mob is not null or LENGTH(M_Mob)<>0) and( M_Name is not null or LENGTH(M_Name)<>0) UNION Select (S_Name || \" (\" || M_Name || \" )\") as [M_Name],S_Mob as [M_Mob],S_Pic as [M_Pic],C3_BG as [Prefix] from "+Tab2Name+" Where S_BG='"+StrSelectedBlood+"'"+StrCity+" and( S_Mob is not null or LENGTH(S_Mob)<>0) and( S_Name is not null or LENGTH(S_Name)<>0) order by M_Name";
         System.out.println(sqlSearch);
 	     cursorT = db.rawQuery(sqlSearch, null);
 	     Bitmap theImage = null;
		    if (cursorT.moveToFirst()) {
			   do {
				   	StrNameBlood =cursorT.getString(0);
					StrMobBlood =cursorT.getString(1);
					imgP=cursorT.getBlob(2);
					PrefName=cursorT.getString(3);
					if(PrefName==null)
					   PrefName="";
					
					if(PrefName.trim().length()>0)
						StrNameBlood=PrefName.trim()+" "+StrNameBlood;
					
					//StrBG =cursorT.getString(3);
					if(StrMobBlood==null)
						StrMobBlood="";
					else if(StrMobBlood.length()!=10)
						StrMobBlood=""; 
					
					if(imgP!=null){
		  	  			ByteArrayInputStream imageStream = new ByteArrayInputStream(imgP);
	  	  				theImage = BitmapFactory.decodeStream(imageStream);
	  	  			}else{
	  	  				theImage=null;
	  	  			}
					
					Category cat1 = new Category(StrMobBlood,StrNameBlood,theImage);
					cat.children.add(cat1);
			    } while (cursorT.moveToNext());
		  	 }
		     cursorT.close();
		}
	 
	 
	 
	private void Set_App_Logo_Title()
	{
		 menuIntent = getIntent(); 
		 ClientID=menuIntent.getStringExtra("ClientID");
		 LogclubName=menuIntent.getStringExtra("LogclubName");
		 checkcomingfrom=menuIntent.getStringExtra("Menu_Noti");//come from notification or menu page
		 setTitle("Search Blood Group");
		 
		 if(checkcomingfrom.equalsIgnoreCase("menu")){
			 AppLogo =  menuIntent.getByteArrayExtra("AppLogo"); 
		 }
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
	
	public void callOnphone(String MobCall) {
		try {
	        Intent callIntent = new Intent(Intent.ACTION_DIAL);
	        callIntent.setData(Uri.parse("tel:"+MobCall));
	        startActivity(callIntent);
	    } catch (ActivityNotFoundException activityException) {
	    	Toast.makeText(getBaseContext(), "Call failed", 0).show();
	    }
    }
 
	public void SMSCallactivity(String MobCall) {
		try {
			String uri= "smsto:"+MobCall;
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
            intent.putExtra("compose_mode", true);
            startActivity(intent);
	       } catch (ActivityNotFoundException activityException) {
	    	Toast.makeText(getBaseContext(), "Sms failed", 0).show();
	       }
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
	   	 if (keyCode == KeyEvent.KEYCODE_BACK) {
	   		 if(checkcomingfrom.equalsIgnoreCase("menu")){
	   			Intent	MainBtnIntent= new Intent(getBaseContext(),MenuPage.class);
	   			MainBtnIntent.putExtra("AppLogo", AppLogo);
			    startActivity(MainBtnIntent);
			    finish();
	   		 }else{
	   			finish();
	   		 }
	   	    return true;
	   	 }
	   	return super.onKeyDown(keyCode, event);
	}
	 
	// Get All Group Details from LoginMain Table
    private void Get_LoginMain_Data()
	{
	   //String SqlQry="Select ClientID,LogName,LogId,ClubName,M_ID from LoginMain";
	   db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
	   String SqlQry="Select ClientID,ClubName from LoginMain";
	   Cursor cursorT = db.rawQuery(SqlQry, null);
	   int RCount=cursorT.getCount();
	   if(RCount>0)
	   {
	      Arr_C_Ids=new String[RCount];
	      Arr_C_Ids_Values=new String[RCount];
	      int i=0;	
		  while(cursorT.moveToNext())
		  {
			Arr_C_Ids[i]=cursorT.getString(0);
			String Group_Data=cursorT.getString(1);
			//String Group_Data=cursorT.getString(1)+"~"+cursorT.getString(2)+"~"+cursorT.getString(3)+"~"+cursorT.getInt(4);
			Arr_C_Ids_Values[i] =Group_Data;
			i++;
		  }
	   }
	   else
	   {
		   Arr_C_Ids=null;
		   Arr_C_Ids_Values=null;
	   }
	   cursorT.close();
	   db.close();
	}
		
	private void showalert() {
			alertDialogBuilder3 = new AlertDialog.Builder(context);
			alertDialogBuilder3
	        .setTitle("No record found!")
            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                	dialog.dismiss();
                }
            });
            ad = alertDialogBuilder3.create();
            ad.show();
	}
		
	public class CustomComparator implements Comparator<String> {
	        @Override
	        public int compare(String o1, String o2) {
	            return o1.compareTo(o2);
	        }
	}
		
		/*@Override
		public boolean onCreateOptionsMenu(Menu menu) {
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.sms_menu, menu);
		    return true;
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		    // Handle item selection
		    switch (item.getItemId()) {
		        case R.id.smsfile:
		        	sendsms();
		            return true;
		        default:
		            return super.onOptionsItemSelected(item);
		    }
		}

		private void sendsms(){
			 if(fulllistsms!=null){
	 	    		String ss="";
	 	    		String [] stockArr = fulllistsms.toArray(new String[fulllistsms.size()]); 
	 	    		 //String []Arr=stockArr;
	 	    		System.out.println("len:  "+stockArr.length); 
	 	    		for(int i=0;i<stockArr.length;i++){
	 	    			String tt=stockArr[i].toString();
	 	    			ss=ss+tt+",";
	 	    		}
	 	    		System.out.println(ss);
	 	    		callOnSms(ss);
	 	    	 }else{
	 	    		 
	 	    	 }
		}*/
		
	 public void FillproductList(boolean tick){
			 products =  new ArrayList<Product>();
			 for(int i=0; i<categories.get(0).children.size(); i++){
     			String tt= categories.get(0).children.get(i).name;
     			String val= categories.get(0).children.get(i).val;
     			String val2= categories.get(0).children.get(i).val2;
     			Bitmap theImage= categories.get(0).children.get(i).Image;
     			products.add(new Product(val2,val,tt,tick,theImage,true)); 
             } 
			 boxAdapter = new ListAdapter2(context, products);
	         ListViewsingleblood.setAdapter(boxAdapter);
	  }
		
		 public void ActionCall(){
			    actionBar = getActionBar();
		        actionBar.setCustomView(R.layout.br_an_layout);
		    	cHkboxall = (CheckBox)actionBar.getCustomView().findViewById(R.id.checkBoxAnBrall);
		    	TextView txt = (TextView)actionBar.getCustomView().findViewById(R.id.textViewnane);
		    	txt.setText(LogclubName);
		    	cHkboxall.setOnClickListener(new OnClickListener(){ 
		            public void onClick(View arg0){
		         	  if(cHkboxall.isChecked()==true){
		         		 FillproductList(true);
		         	  }else{
		         		 FillproductList(false);  
		         	  }
		            }
		  	   });
		       actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM |ActionBar.DISPLAY_SHOW_HOME);
		     }
}
