package group.manager;

import java.io.ByteArrayInputStream;

import group.manager.SimpleGestureFilter.SimpleGestureListener;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FullAdvertisement extends Activity implements SimpleGestureListener {
	ImageView ImgADVMain;
	Intent menuIntent;
	String Log, logid, ClubName, Str_user, Table2Name, Table4Name, sqlSearch = "", finalresult, type = "", Rtype = "";
	byte[] AppLogo, ImgAdg;
	SQLiteDatabase db;
	Cursor cursorT;
	int s_count, Cnt = 0, tempsize, i = 0;
	int[] CodeArr;
	TextView txtMAX;
	byte[] imgP;
	private SimpleGestureFilter detect;
	TextView txt, Txt2, Txt4, Txt3b1, Txt3b2, Txt3b3, Txt3b4, Txt3b5, Txt3b6;
	LinearLayout LL1, LL2, LL3, LL4, LL5;
	Button Btnenglish, BtnHindi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fulladvertisement);

		ImgADVMain = (ImageView) findViewById(R.id.imageViewADVErtisement);
		txtMAX = (TextView) findViewById(R.id.txt111);
		txt = (TextView) findViewById(R.id.txttop);
		Txt2 = (TextView) findViewById(R.id.txttop2);
		Txt3b1 = (TextView) findViewById(R.id.txttop3b1);
		Txt3b2 = (TextView) findViewById(R.id.txttop3b2);
		Txt3b3 = (TextView) findViewById(R.id.txttop3b3);
		Txt3b4 = (TextView) findViewById(R.id.txttop3b4);
		Txt3b5 = (TextView) findViewById(R.id.txttop3b5);
		Txt3b6 = (TextView) findViewById(R.id.txttop3b6);
		Txt4 = (TextView) findViewById(R.id.txttop4);
		BtnHindi = (Button) findViewById(R.id.btnlanghindi);
		Btnenglish = (Button) findViewById(R.id.btnlanghenglish);
		LL1 = (LinearLayout) findViewById(R.id.idtop);
		LL2 = (LinearLayout) findViewById(R.id.idtop2);
		LL3 = (LinearLayout) findViewById(R.id.idtop3);
		LL4 = (LinearLayout) findViewById(R.id.idtop4);
		LL5 = (LinearLayout) findViewById(R.id.idtop5);

		menuIntent = getIntent();
		Log = menuIntent.getStringExtra("Clt_Log");
		logid = menuIntent.getStringExtra("Clt_LogID");
		ClubName = menuIntent.getStringExtra("Clt_ClubName");
		Str_user = menuIntent.getStringExtra("UserClubName");
		AppLogo = menuIntent.getByteArrayExtra("AppLogo");
		type = menuIntent.getStringExtra("Type");

		Table2Name = "C_" + Str_user + "_2";
		Table4Name = "C_" + Str_user + "_4";

		detect = new SimpleGestureFilter(this, this);// Detect gesture

		Set_App_Logo_Title(); // Set App Logo and Title

		LL1.setVisibility(View.GONE);
		LL2.setVisibility(View.GONE);
		LL3.setVisibility(View.GONE);
		LL4.setVisibility(View.GONE);
		LL5.setVisibility(View.GONE);

		if (type.equals("2")) {
			LL1.setVisibility(View.VISIBLE);
			BtnHindi.setVisibility(View.VISIBLE);
			Btnenglish.setVisibility(View.GONE);
			English();
		} else if (type.equals("3")) {
			ImgADVMain.setVisibility(View.VISIBLE);
			ImgADVMain.setImageResource(R.drawable.visioniocl);
		} else if (type.equals("4")) {
			LL2.setVisibility(View.VISIBLE);
		} else if (type.equals("5")) {
			LL3.setVisibility(View.VISIBLE);
		} else if (type.equals("6")) {
			LL4.setVisibility(View.VISIBLE);
		} else if (type.equals("7")) {
			LL5.setVisibility(View.VISIBLE);
		} else if ((type.equals("8")) || (type.equals("9"))) {/////////////swipe screen (add show)

			ImgAdg = menuIntent.getByteArrayExtra("Photo1");
			Bitmap bitmap = BitmapFactory.decodeByteArray(ImgAdg, 0, ImgAdg.length);
			ImgADVMain.setVisibility(View.VISIBLE);
			ImgADVMain.setImageBitmap(bitmap);
		} else if ((type.equals("10")) || (type.equals("11")) || (type.equals("12"))) {
			LL1.setVisibility(View.VISIBLE);
			BtnHindi.setVisibility(View.GONE);
			Btnenglish.setVisibility(View.GONE);
			Set_Jay_Text_English(type);
		} else {
			Rtype = menuIntent.getStringExtra("RType");

			db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
			sqlSearch = "SELECT Count(M_id) FROM " + Table4Name + " where Rtype='" + Rtype + "'";
			cursorT = db.rawQuery(sqlSearch, null);
			if (cursorT.moveToFirst()) {
				do {
					s_count = cursorT.getInt(0);
				} while (cursorT.moveToNext());
			}
			cursorT.close();
			txtMAX.setText(Cnt + 1 + " of " + s_count);
			if (s_count != 0) {
				Cnt = 0;
				sqlSearch = "select M_id from " + Table4Name + " Where Rtype='" + Rtype + "' Order by Num1";
				cursorT = db.rawQuery(sqlSearch, null);
				tempsize = cursorT.getCount();
				CodeArr = new int[tempsize];
				if (cursorT.moveToFirst()) {
					do {
						CodeArr[i] = cursorT.getInt(0);
						i++;
					} while (cursorT.moveToNext());
				}
				cursorT.close();
				db.close();
				sqlSearch = "select M_id,Text1,Photo1 from " + Table4Name + " Where Rtype='" + Rtype + "' Order by Num1 limit 1";
				FillData(sqlSearch);
			} else {
				db.close();
			}
		}


		BtnHindi.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Btnenglish.setVisibility(View.VISIBLE);
				BtnHindi.setVisibility(View.GONE);
				Hindi();
			}
		});

		Btnenglish.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				BtnHindi.setVisibility(View.VISIBLE);
				Btnenglish.setVisibility(View.GONE);
				English();
			}
		});
	}


	@Override
	public boolean dispatchTouchEvent(MotionEvent m1) {
		// Call onTouchEvent of SimpleGestureFilter class
		this.detect.onTouchEvent(m1);
		return super.dispatchTouchEvent(m1);
	}

	@Override
	public void onSwipe(int direction) {
		// TODO Auto-generated method stub
		switch (direction) {
			case SimpleGestureFilter.SWIPE_RIGHT:
				//Toast.makeText(this, "Right", 0).show();
				Prev();
				break;
			case SimpleGestureFilter.SWIPE_LEFT:
				//Toast.makeText(this, "left", 0).show();
				Next();
				break;
		}
	}

	@Override
	public void onDoubleTap() {
		// TODO Auto-generated method stub
	}


	public void Next() {
		if (type.equals("1")) {
			if (Cnt + 1 == tempsize) {
				Toast.makeText(getBaseContext(), "No Further Record", Toast.LENGTH_SHORT).show();
			} else {
				sqlSearch = "select M_id,Text1,Photo1 from " + Table4Name + " where Rtype='" + Rtype + "' AND m_id=" + CodeArr[Cnt + 1];
				FillData(sqlSearch);
				Cnt = Cnt + 1;
				txtMAX.setText(Cnt + 1 + " of " + s_count);
			}
		}
	}

	public void Prev() {
		if (type.equals("1")) {
			if (Cnt == 0) {
				Toast.makeText(getBaseContext(), "No Previous Record", Toast.LENGTH_SHORT).show();
			} else {
				sqlSearch = "select M_id,Text1,Photo1 from " + Table4Name + " where Rtype='" + Rtype + "' AND m_id=" + CodeArr[Cnt - 1];
				FillData(sqlSearch);
				Cnt = Cnt - 1;
				txtMAX.setText(Cnt + 1 + " of " + s_count);
			}
		}
	}


	public void English() {
		txt.setText("The Jaycee Creed");
		Txt2.setText("We Believe");
		Txt3b1.setText("That faith in God gives meaning and purpose to human life.");
		Txt3b2.setText("That the brotherhood of man transcends the sovereignty of nations.");
		Txt3b3.setText("That economic justice can best be won by free men through free enterprise.");
		Txt3b4.setText("That government should be of laws rather than of men.");
		Txt3b5.setText("That earth's great treasure lies in human personality.");
		Txt3b6.setText("That service to humanity is the best work of life.");
		Txt4.setText("- C. William Brownfield");
	}

	public void Hindi() {
		txt.setText(R.string.jaycee);
		Txt2.setText(R.string.jaycee1);
		Txt3b1.setText(R.string.jaycee2);
		Txt3b2.setText(R.string.jaycee3);
		Txt3b3.setText(R.string.jaycee4);
		Txt3b4.setText(R.string.jaycee5);
		Txt3b5.setText(R.string.jaycee6);
		Txt3b6.setText(R.string.jaycee7);
		Txt4.setText(R.string.jaycee8);
	}

	private void Set_Jay_Text_English(String Type) {
		RelativeLayout RL2 = (RelativeLayout) findViewById(R.id.RL2_LL1);
		RelativeLayout RL3 = (RelativeLayout) findViewById(R.id.RL3_LL1);
		RelativeLayout RL4 = (RelativeLayout) findViewById(R.id.RL4_LL1);
		RelativeLayout RL5 = (RelativeLayout) findViewById(R.id.RL5_LL1);
		RelativeLayout RL6 = (RelativeLayout) findViewById(R.id.RL6_LL1);

		RL2.setVisibility(View.GONE);
		RL3.setVisibility(View.GONE);
		RL4.setVisibility(View.GONE);
		RL5.setVisibility(View.GONE);
		RL6.setVisibility(View.GONE);

		String txt1 = "", txt2 = "", txt3 = "", txt4 = "", txt5 = "";

		if (Type.equals("10")) {
			txt1 = "JCI Mission";
			txt2 = "To Provide Development Opportunities that empower young people to create positive changes.";
		} else if (Type.equals("11")) {
			txt1 = "JCI Vision";
			txt2 = "To be the leading global network of Young Active Citizens.";
		} else if (Type.equals("12")) {
			RL2.setVisibility(View.VISIBLE);
			RL3.setVisibility(View.VISIBLE);
			RL4.setVisibility(View.VISIBLE);
			txt1 = "JCI India";
			txt2 = "JCI India is a voluntary organization, membership based NGO working in India since 1949 for developing the leadership skills of young men and women of this country. It is affiliated to Junior Chamber International(JCI),a worldwide federation of young leaders and entrepreneurs founded in 1944, having headquarter at Chester Field USA. Currently it has over 200,000 active members and more than one million graduates, in over 100 countries and 6,000 communities.";
			txt3 = "JCI India is the Second largest Member Nation of Junior Chamber International. Currently we are active in more than 26 states and union territories across India.";
			txt4 = "The membership is offered to everybody regardless of color, cast and creed between the age of 18 -40 years. Junior Chamber International India is registered under Societies Registration Act,\nBombay Public Trust Act and Income Tax Act of India.\nIn the last 61 years we are able to produce thousands of social and business leaders all over the country through our intensive project based training activities.";
			txt5 = "Source of above information is: http://jciindia.in/about-jci/jci-india/";
		}

		txt.setText(txt1);
		Txt2.setText("");
		Txt3b1.setText(txt2);
		Txt3b2.setText(txt3);
		Txt3b3.setText(txt4);
		Txt3b4.setText(txt5);
		//Txt3b5.setText("");
		//Txt3b6.setText("");
	}


	public void FillData(String sql) {
		// TODO Auto-generated method stub
		try {
			db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
			cursorT = db.rawQuery(sql, null);
			if (cursorT.moveToFirst()) {
				finalresult = cursorT.getInt(0) + "^" + Chkval(cursorT.getString(1));
				imgP = cursorT.getBlob(2);
			}
			cursorT.close();

			/////////////////////////////////////////////////////////////////////////////////////////
			//String strSQL = "UPDATE "+Table2Name +" set M_Mob = '8960255333' WHERE M_Id=1";
			// db.execSQL(strSQL);
			/////////////////////////////////////////////////////////////////////////////////////////

			db.close();
			if (imgP != null) {
				ImgADVMain.setVisibility(View.VISIBLE);
				ByteArrayInputStream imageStream = new ByteArrayInputStream(imgP);
				Bitmap theImage = BitmapFactory.decodeStream(imageStream);
				ImgADVMain.setImageBitmap(theImage);
			} else {
				ImgADVMain.setVisibility(View.GONE);
			}
			FillVal(finalresult); // Set Display Data of Directory
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void FillVal(String WResult) {
		String s = WResult.replace("^", "#") + " ";
		String[] temp = s.split("#");
		/*str_memid=temp[0].toString();
		Strname=temp[1].toString();
		Stra1=temp[2].toString();	
		Stra2=temp[3].toString();
		Stra3=temp[4].toString();
		City=temp[5].toString();
		Stremail=temp[6].toString();
		Strmo=temp[7].toString();
		StrMemno=temp[8].toString();
		Strbg=temp[9].toString().trim();
		String Prefix_Name=temp[10].toString();
		dd=temp[11].toString().trim();
		mm=temp[12].toString();
		yy=temp[13].toString();
		if((dd==null)||(dd.length()==0)){
			LLaydob.setVisibility(View.GONE);
		}else{
			LLaydob.setVisibility(View.VISIBLE);
			txtDob.setText(dd+"-"+mm+"-"+yy);
		}
		Stradd=Stra1+Stra2+Stra3+City;
		Strname=Prefix_Name.trim()+" "+Strname.trim();
		txtPName.setText(Strname.trim());
		filloremptyData(StrMemno,LLaymemno,txtMemno);	
		filloremptyData(Strmo,LLayMobile,TxtPMob);	
		filloremptyData(Stradd,LLayaddress,txtPAdd);
		filloremptyData(Stremail,LLayemail,TxtpEmail);
		filloremptyData(Strbg,LLayblood,TxttBlood);	*/
	}


	private String Chkval(String Val) {
		if (Val == null)
			Val = "";
		return Val.trim();
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			GoBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void GoBack() {
		if (type.equals("2") || type.equals("10") || type.equals("11") || type.equals("12")) {
			menuIntent = new Intent(getBaseContext(), UlilitiesList.class);
			menuIntent.putExtra("CondChk", "3");
			menuIntent.putExtra("AppLogo", AppLogo);
			startActivity(menuIntent);
			finish();
		} else if ((type.equals("3")) || (type.equals("4")) || (type.equals("5")) || (type.equals("6")) || (type.equals("7"))) {
			menuIntent = new Intent(getBaseContext(), UlilitiesList.class);
			menuIntent.putExtra("CondChk", "4");
			menuIntent.putExtra("AppLogo", AppLogo);
			startActivity(menuIntent);
			finish();
		} else if ((type.equals("8")) || (type.equals("9"))) {
			finish();
		} else {
			menuIntent = new Intent(getBaseContext(), MenuPage.class);
			menuIntent.putExtra("AppLogo", AppLogo);
			startActivity(menuIntent);
			finish();
		}
	}

}
