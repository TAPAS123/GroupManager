package group.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "MDA_Club";

	private String Tab2Name;


	public DbHandler(Context context, String Tab2Name) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.Tab2Name = Tab2Name;
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	  /* // Drop older table if existed
	   db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
	   // Create tables again
	   onCreate(db);*/
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	////Update personal
	public void UpdatePersonProfile(String UserType, String Name, String Add1, String Add2, String Add3, String City, String Email, String DOB_D,
						  String DOB_M, String DOB_Y, String Ann_D, String Ann_M, String Ann_Y, String Mob, String BG, byte[] Pic, String M_Id,
						  String Country, String State, String Mob2, String Land1, String Land2, String Email2, String Pin, String Loc_Res, String Loc_Off) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();

			if (UserType.equals("SPOUSE")) {
				values.put("S_Name", Name);
				values.put("S_Email", Email);
				values.put("S_DOB_D", DOB_D);
				values.put("S_DOB_M", DOB_M);
				values.put("S_DOB_Y", DOB_Y);
				values.put("S_Mob", Mob);
				values.put("S_BG", BG);
				values.put("S_Pic", Pic);
			} else {
				values.put("M_Name", Name);
				values.put("M_Mob", Mob);
				values.put("M_SndMob", Mob2);//Save Mobile2
				values.put("M_Land1", Land1);//Save Landline1
				values.put("M_Land2", Land2);//Save Landline2
				values.put("M_Email", Email);
				values.put("M_Email1", Email2);//Save Email2
				values.put("M_DOB_D", DOB_D);
				values.put("M_DOB_M", DOB_M);
				values.put("M_DOB_Y", DOB_Y);
				values.put("M_BG", BG);
				values.put("M_Pic", Pic);
			}
			values.put("M_Add1", Add1);
			values.put("M_Add2", Add2);
			values.put("M_Add3", Add3);
			values.put("C3_Name", Country);//Save Country
			values.put("C3_Email", State);//Save State
			values.put("M_City", City);
			values.put("M_Pin", Pin);//Save Pin
			values.put("M_MrgAnn_D", Ann_D);
			values.put("M_MrgAnn_M", Ann_M);
			values.put("M_MrgAnn_Y", Ann_Y);
			values.put("C1_FName", Loc_Res); //Set Location Residence
			values.put("C2_FName", Loc_Off); //Set Location Office

			// Update Data
			db.update(Tab2Name, values, "M_id = " + M_Id, null);
			db.close(); // Closing database connection
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}


	// Insert or Update Family Member Data
	public void Insert_UpdateFamilyMember(String Type, String TabName, String[] Arr, String[] Arr_MarriageDetails, byte[] Pic) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();

			String M_Id = Arr[0].trim();// M_Id

			if (Type.equals("Insert")) {
				values.put("M_Id", M_Id);
				values.put("MemId", Arr[1]);
			}
			values.put("Name", Arr[2]);
			values.put("Relation", Arr[3]);
			values.put("Father", Arr[4]);
			values.put("Mother", Arr[5]);
			values.put("Current_loc", Arr[6]);
			values.put("Mob_1", Arr[7]);
			values.put("Mob_2", Arr[8]);
			values.put("DOB_D", Arr[9]);
			values.put("DOB_M", Arr[10]);
			values.put("DOB_Y", Arr[11]);
			values.put("EmailId", Arr[12]);
			values.put("Education", Arr[13]);
			values.put("Work_Profile", Arr[14]);
			values.put("Text1", "PEND"); //This field is used for Sync(M-S) check
			values.put("Text2", Arr[15]);  // Text2 is used for GENDER
			values.put("Text3", Arr[16]);  // Text3 is used for SharewithMatrimony
			values.put("Text4", Arr[17]);  // Text4 is used for Designation
			values.put("Text15", Arr[18]);  // Text15 is used for FirmName(Added on 21-05-2020)
			values.put("Text16", Arr[19]);  // Text16 is used for FirmAddr(Added on 21-05-2020)
			values.put("Text17", Arr[20]);  // Text17 is used for NatureofBusi(Added on 21-05-2020)
			values.put("Age", Arr[21]);     // Age(Added on 21-05-2020)
			values.put("Text18", Arr[22]);  // Text18 is used for BG(Blood Group)(Added on 21-05-2020)

			values.put("Gotra", Arr_MarriageDetails[0]);
			values.put("Birth_Time", Arr_MarriageDetails[1]);
			values.put("Birth_Place", Arr_MarriageDetails[2]);
			values.put("Text5", Arr_MarriageDetails[3]);   // Text5 is used for NativePlace
			values.put("Height", Arr_MarriageDetails[4]);
			values.put("Text6", Arr_MarriageDetails[5]);   // Text6 is used for Manglik
			values.put("Text7", Arr_MarriageDetails[6]);   // Text7 is used for AnnualIncome
			values.put("Text8", Arr_MarriageDetails[7]);   // Text8 is used for WorkAfterMarriage
			values.put("Text9", Arr_MarriageDetails[8]);   // Text9 is used for Diet
			values.put("Text10", Arr_MarriageDetails[9]);  // Text10 is used for Father's Status
			values.put("Text11", Arr_MarriageDetails[10]); // Text11 is used for Mother's Status
			values.put("Text12", Arr_MarriageDetails[11]); // Text12 is used for Brothers all Details
			values.put("Text13", Arr_MarriageDetails[12]); // Text13 is used for Sisters all Details
			values.put("Text14", Arr_MarriageDetails[13]); // Text14 is used for Marriage AdditionalInfo

			values.put("Pic", Pic);// Family Member Image

			if (Type.equals("Insert")) {
				db.insert(TabName, null, values);// Inserting Row
			} else {
				db.update(TabName, values, "M_Id = " + M_Id, null);// Update Data
			}
			db.close(); // Closing database connection
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}


	public void onUpdateCh(String C1_Name, String C1_Mob, String C1_Email, String C1_DOB_D, String C1_DOB_M, String C1_DOB_Y, String C1_BG,
						   byte[] C1_Pic, String C2_Name, String C2_Mob, String C2_Email, String C2_DOB_D, String C2_DOB_M, String C2_DOB_Y, String C2_BG,
						   byte[] C2_Pic, String C3_Name, String C3_Mob, String C3_Email, String C3_DOB_D, String C3_DOB_M, String C3_DOB_Y, String C3_BG,
						   byte[] C3_Pic, String C4_Name, String C4_Mob, String C4_Email, String C4_DOB_D, String C4_DOB_M, String C4_DOB_Y, String C4_BG,
						   byte[] C4_Pic, String m_id) {
		//UPDATE table_name
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			String Sqlqrych = "UPDATE " + Tab2Name + " SET C1_Name = '" + C1_Name + "', C1_Email = '" + C1_Email + "', C1_DOB_D = '" + C1_DOB_D + "', C1_DOB_M = '" + C1_DOB_M + "', C1_DOB_Y = '" + C1_DOB_Y + "', C1_Mob = '" + C1_Mob + "', C1_BG = '" + C1_BG + "', C1_Pic = '" + C1_Pic + "', "
					+ "C2_Name = '" + C2_Name + "', C2_Email = '" + C2_Email + "', C2_DOB_D = '" + C2_DOB_D + "', C2_DOB_M = '" + C2_DOB_M + "', C2_DOB_Y = '" + C2_DOB_Y + "', C2_Mob = '" + C2_Mob + "', C2_BG = '" + C2_BG + "', C2_Pic = '" + C2_Pic + "', "
					+ "C3_Name = '" + C3_Name + "', C3_Email = '" + C3_Email + "', C3_DOB_D = '" + C3_DOB_D + "', C3_DOB_M = '" + C3_DOB_M + "', C3_DOB_Y = '" + C3_DOB_Y + "', C3_Mob = '" + C3_Mob + "', C3_BG = '" + C3_BG + "', C3_Pic = '" + C3_Pic + "',"
					+ "C4_Name = '" + C4_Name + "', C4_Email = '" + C4_Email + "', C4_DOB_D = '" + C4_DOB_D + "', C4_DOB_M = '" + C4_DOB_M + "', C4_DOB_Y = '" + C4_DOB_Y + "', C4_Mob = '" + C4_Mob + "', C4_BG = '" + C4_BG + "', C4_Pic = '" + C4_Pic + "' "
					+ "WHERE M_id = " + m_id;
			System.out.println(Sqlqrych);
			db.execSQL(Sqlqrych);
			db.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void insert(String mid, String Tab4Name) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sqlqry1 = "Delete From  " + Tab4Name + " Where Rtype ='Pending'";
		db.execSQL(Sqlqry1);
		String Sqlqry = "Insert into " + Tab4Name + "(Rtype,Text1) VALUES ('Pending','" + mid + "')";
		db.execSQL(Sqlqry);
		db.close();
	}


	public int Checkinsert(String Tab4Name) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sqlqry = "Select Count(M_id) from " + Tab4Name + " Where Rtype='Pending'";
		Cursor cursorT = db.rawQuery(Sqlqry, null);
		int Pendcount = 0;
		if (cursorT.moveToFirst()) {
			Pendcount = cursorT.getInt(0);
		}
		cursorT.close();
		db.close();
		return Pendcount;
	}

	//Insert/Delete/Update query
	public void QueryExecuted(String Query) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL(Query);
			db.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}


	// Adding new contact(Table 2)
	public void addContact(int M_Id, String M_Name, String M_FName, String M_MName, String M_LName, String M_Add1,
						   String M_Add2, String M_Add3, String M_City, String M_Email, String M_Gender, String M_MrgAnn_D,
						   String M_MrgAnn_M, String M_MrgAnn_Y, String M_DOB_D, String M_DOB_M, String M_DOB_Y, String M_Mob, String M_Pin,
						   String M_BG, String M_BussNm, String M_BussCate, String M_MemSince_D, String M_MemSince_M, String M_MemSince_Y, byte[] M_Pic,
						   String S_Name, String S_FName, String S_Mname, String S_LName, String S_Mob, String S_Email, String S_DOB_D, String S_DOB_M,
						   String S_DOB_Y, String S_BG, byte[] S_Pic, String C1_Name, String C1_FName, String C1_Mname,
						   String C1_LName, String C1_Mob, String C1_Email, String C1_Gender, String C1_DOB_D, String C1_DOB_M, String C1_DOB_Y, String C1_BG,
						   byte[] C1_Pic, String C2_Name, String C2_FName, String C2_Mname, String C2_LName, String C2_Mob, String C2_Email, String C2_Gender,
						   String C2_DOB_D, String C2_DOB_M, String C2_DOB_Y, String C2_BG, byte[] C2_Pic, String C3_Name, String C3_FName, String C3_Mname, String C3_LName,
						   String C3_Mob, String C3_Email, String C3_Gender, String C3_DOB_D, String C3_DOB_M, String C3_DOB_Y, String C3_BG, byte[] C3_Pic, String C4_Name,
						   String C4_FName, String C4_Mname, String C4_LName, String C4_Mob, String C4_Email, String C4_Gender, String C4_DOB_D, String C4_DOB_M,
						   String C4_DOB_Y, String C4_BG, byte[] C4_Pic, String Pass, byte[] Photo1, byte[] Photo2, byte[] Photo3, byte[] Photo4,
						   byte[] Photo5, byte[] Photo6, int Admin, String MemNo, String Oth, int SYNCID, String SyncDT, String[] ArrExtra) {

		try {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();

			values.put("M_Id", M_Id);
			values.put("M_Name", M_Name);
			values.put("M_FName", M_FName);
			values.put("M_MName", M_MName);
			values.put("M_LName", M_LName);
			values.put("M_Add1", M_Add1);
			values.put("M_Add2", M_Add2);
			values.put("M_Add3", M_Add3);
			values.put("M_City", M_City);
			values.put("M_Email", M_Email);

			values.put("M_Gender", M_Gender);
			values.put("M_MrgAnn_D", M_MrgAnn_D);
			values.put("M_MrgAnn_M", M_MrgAnn_M);
			values.put("M_MrgAnn_Y", M_MrgAnn_Y);
			values.put("M_DOB_D", M_DOB_D);
			values.put("M_DOB_M", M_DOB_M);
			values.put("M_DOB_Y", M_DOB_Y);
			values.put("M_Mob", M_Mob);
			values.put("M_Pin", M_Pin);
			values.put("M_BG", M_BG);

			values.put("M_BussNm", M_BussNm);
			values.put("M_BussCate", M_BussCate);
			values.put("M_MemSince_D", M_MemSince_D);
			values.put("M_MemSince_M", M_MemSince_M);
			values.put("M_MemSince_Y", M_MemSince_Y);
			values.put("M_Pic", M_Pic);
			values.put("S_Name", S_Name);
			values.put("S_FName", S_FName);
			values.put("S_Mname", S_Mname);
			values.put("S_LName", S_LName);

			values.put("S_Mob", S_Mob);
			values.put("S_Email", S_Email);
			values.put("S_DOB_D", S_DOB_D);
			values.put("S_DOB_M", S_DOB_M);
			values.put("S_DOB_Y", S_DOB_Y);
			values.put("S_BG", S_BG);
			values.put("S_Pic", S_Pic);
			values.put("C1_Name", C1_Name);
			values.put("C1_FName", C1_FName);
			values.put("C1_Mname", C1_Mname);

			values.put("C1_LName", C1_LName);
			values.put("C1_Mob", C1_Mob);
			values.put("C1_Email", C1_Email);
			values.put("C1_Gender", C1_Gender);
			values.put("C1_DOB_D", C1_DOB_D);
			values.put("C1_DOB_M", C1_DOB_M);
			values.put("C1_DOB_Y", C1_DOB_Y);
			values.put("C1_BG", C1_BG);
			values.put("C1_Pic", C1_Pic);
			values.put("C2_Name", C2_Name);

			values.put("C2_FName", C2_FName);
			values.put("C2_Mname", C2_Mname);
			values.put("C2_LName", C2_LName);
			values.put("C2_Mob", C2_Mob);
			values.put("C2_Email", C2_Email);
			values.put("C2_Gender", C2_Gender);
			values.put("C2_DOB_D", C2_DOB_D);
			values.put("C2_DOB_M", C2_DOB_M);
			values.put("C2_DOB_Y", C2_DOB_Y);
			values.put("C2_BG", C2_BG);

			values.put("C2_Pic", C2_Pic);
			values.put("C3_Name", C3_Name);
			values.put("C3_FName", C3_FName);
			values.put("C3_Mname", C3_Mname);
			values.put("C3_LName", C3_LName);
			values.put("C3_Mob", C3_Mob);
			values.put("C3_Email", C3_Email);
			values.put("C3_Gender", C3_Gender);
			values.put("C3_DOB_D", C3_DOB_D);
			values.put("C3_DOB_M", C3_DOB_M);

			values.put("C3_DOB_Y", C3_DOB_Y);
			values.put("C3_BG", C3_BG);
			values.put("C3_Pic", C3_Pic);
			values.put("C4_Name", C4_Name);
			values.put("C4_FName", C4_FName);
			values.put("C4_Mname", C4_Mname);
			values.put("C4_LName", C4_LName);
			values.put("C4_Mob", C4_Mob);
			values.put("C4_Email", C4_Email);
			values.put("C4_Gender", C4_Gender);

			values.put("C4_DOB_D", C4_DOB_D);
			values.put("C4_DOB_M", C4_DOB_M);
			values.put("C4_DOB_Y", C4_DOB_Y);
			values.put("C4_BG", C4_BG);
			values.put("C4_Pic", C4_Pic);
			values.put("Pass", Pass);
			values.put("Photo1", Photo1);
			values.put("Photo2", Photo2);
			values.put("Photo3", Photo3);
			values.put("Photo4", Photo4);

			values.put("Photo5", Photo5);
			values.put("Photo6", Photo6);
			values.put("Admin", Admin);
			values.put("MemNo", MemNo);
			values.put("Oth", Oth);
			values.put("SYNCID", SYNCID);
			values.put("SyncDT", SyncDT);

			//Added Some Extra Fields On 16-03-2016
			values.put("M_SndAdd1", ArrExtra[0]);// M_SndAdd1
			values.put("M_SndAdd2", ArrExtra[1]);// M_SndAdd2
			values.put("M_SndAdd3", ArrExtra[2]);// M_SndAdd3
			values.put("M_SndAdd4", ArrExtra[3]);// M_SndAdd4
			values.put("M_SndPin", ArrExtra[4]);// M_SndPin
			values.put("M_SndMob", ArrExtra[5]);// M_SndMob
			values.put("M_SndMob1", ArrExtra[6]);// M_SndMob1
			values.put("M_SndStd", ArrExtra[7]);// M_SndStd
			values.put("Oth1", ArrExtra[8]);// Oth1
			values.put("Oth2", ArrExtra[9]);// Oth2
			values.put("Oth3", ArrExtra[10]);// Oth3
			values.put("M_Land1", ArrExtra[11]);// M_Land1
			values.put("M_Land2", ArrExtra[12]);// M_Land2
			values.put("M_Email1", ArrExtra[13]);// M_Email1
			/////////////////////////////////////////

			// Inserting Row
			db.insert(Tab2Name, null, values);
			//db.close(); // Closing database connection
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}


	public void Tab4AddContact(String TableName, int M_ID, String Rtype, String Text1, String Text2, String Text3,
							   String Date1, String Date2, String Date3, String Date1_1, String Date2_1, String Date3_1,
							   String Num1, String Num2, String Num3, String Add1, byte[] Photo1,
							   String Text4, String Text5, String Text6, String Text7,
							   String Text8, String Add2, int SYNCID, String SyncDT, String COND1,
							   String COND2, String COND3, String COND4, String COND5) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("M_ID", M_ID);
			values.put("Rtype", Rtype);
			values.put("Text1", Text1);
			values.put("Text2", Text2);
			values.put("Text3", Text3);
			values.put("Date1", Date1);
			values.put("Date2", Date2);
			values.put("Date3", Date3);
			values.put("Date1_1", Date1_1);
			values.put("Date2_1", Date2_1);
			values.put("Date3_1", Date3_1);
			values.put("Num1", Num1);
			values.put("Num2", Num2);
			values.put("Num3", Num3);
			values.put("Add1", Add1);
			values.put("Photo1", Photo1);
			values.put("Text4", Text4);
			values.put("Text5", Text5);
			values.put("Text6", Text6);
			values.put("Text7", Text7);
			values.put("Text8", Text8);
			values.put("Add2", Add2);
			values.put("SYNCID", SYNCID);
			values.put("SyncDT", SyncDT);
			values.put("COND1", COND1);//Updated 12-05-2016 by Tapas
			values.put("COND2", COND2);//Updated 12-05-2016 by Tapas
			values.put("COND3", COND3);//Updated 12-05-2016 by Tapas
			values.put("COND4", COND4);//Updated 12-05-2016 by Tapas
			values.put("COND5", COND5);//Updated 12-05-2016 by Tapas

			db.insert(TableName, null, values);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}


	// Insert Records in Table Misc
	public void Tab_Misc_AddContact(String TableName, int M_ID, String Rtype, String MemId,
									String Text1, String Text2, String Text3, String Text4, String Text5, String Text6, String Text7, String Text8, String Text9, String Text10,
									String Text11, String Text12, String Text13, String Text14, String Text15, String Text16, String Text17, String Text18, String Text19, String Text20,
									String Text21, String Text22, String Text23, String Text24, String Text25, String Text26, String Text27, String Text28, String Text29, String Text30,
									String Text31, String Text32, String Text33, String Text34, String Text35, String Text36, String Text37, String Text38, String Text39, String Text40,
									String Text41, String Text42, String Text43, String Text44, String Text45, String Text46, String Text47, String Text48, String Text49, String Text50,
									String Add1, String Add2, String Add3, String Add4, String Add5, int SYNCID, String SyncDT) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("M_ID", M_ID);
			values.put("Rtype", Rtype);
			values.put("MemId", MemId);
			values.put("Text1", Text1);
			values.put("Text2", Text2);
			values.put("Text3", Text3);
			values.put("Text4", Text4);
			values.put("Text5", Text5);
			values.put("Text6", Text6);
			values.put("Text7", Text7);
			values.put("Text8", Text8);
			values.put("Text9", Text9);
			values.put("Text10", Text10);
			values.put("Text11", Text11);
			values.put("Text12", Text12);
			values.put("Text13", Text13);
			values.put("Text14", Text14);
			values.put("Text15", Text15);
			values.put("Text16", Text16);
			values.put("Text17", Text17);
			values.put("Text18", Text18);
			values.put("Text19", Text19);
			values.put("Text20", Text20);
			values.put("Text21", Text21);
			values.put("Text22", Text22);
			values.put("Text23", Text23);
			values.put("Text24", Text24);
			values.put("Text25", Text25);
			values.put("Text26", Text26);
			values.put("Text27", Text27);
			values.put("Text28", Text28);
			values.put("Text29", Text29);
			values.put("Text30", Text30);
			values.put("Text31", Text31);
			values.put("Text32", Text32);
			values.put("Text33", Text33);
			values.put("Text34", Text34);
			values.put("Text35", Text35);
			values.put("Text36", Text36);
			values.put("Text37", Text37);
			values.put("Text38", Text38);
			values.put("Text39", Text39);
			values.put("Text40", Text40);
			values.put("Text41", Text41);
			values.put("Text42", Text42);
			values.put("Text43", Text43);
			values.put("Text44", Text44);
			values.put("Text45", Text45);
			values.put("Text46", Text46);
			values.put("Text47", Text47);
			values.put("Text48", Text48);
			values.put("Text49", Text49);
			values.put("Text50", Text50);
			values.put("Add1", Add1);
			values.put("Add2", Add2);
			values.put("Add3", Add3);
			values.put("Add4", Add4);
			values.put("Add5", Add5);
			values.put("SYNCID", SYNCID);
			values.put("SyncDT", SyncDT);

			db.insert(TableName, null, values);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}


	// Insert Records in Table Family
	public void Tab_Family_AddContact(String TableName, int M_ID, String MemId, String Name, String Relation, String Father, String Mother, String Address, String Current_Loc, String Mob_1, String Mob_2, String DOB_D,
									  String DOB_M, String DOB_Y, String EmailID, String Age, String Birth_Time, String Birth_Place, String Gotra, String Remark, byte[] Pic, String Education, String Work_Profile, String Text1, String Text2, String Text3, String Text4, String Text5, String Text6, String Text7, String Text8, String Text9, String Text10,
									  int SYNCID, String SyncDT, String Text11, String Text12, String Text13, String Text14, String Text15, String Text16, String Text17, String Text18, String Text19, float Height) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("M_ID", M_ID);
			values.put("MemId", MemId);
			values.put("Name", Name);
			values.put("Relation", Relation);
			values.put("Father", Father);
			values.put("Mother", Mother);
			values.put("Address", Address);
			values.put("Current_Loc", Current_Loc);
			values.put("Mob_1", Mob_1);
			values.put("Mob_2", Mob_2);
			values.put("DOB_D", DOB_D);
			values.put("DOB_M", DOB_M);
			values.put("DOB_Y", DOB_Y);
			values.put("EmailID", EmailID);
			values.put("Age", Age);
			values.put("Birth_Time", Birth_Time);
			values.put("Birth_Place", Birth_Place);
			values.put("Gotra", Gotra);
			values.put("Remark", Remark);
			values.put("Pic", Pic);
			values.put("Education", Education);
			values.put("Work_Profile", Work_Profile);
			values.put("Text1", Text1);
			values.put("Text2", Text2);
			values.put("Text3", Text3);
			values.put("Text4", Text4);
			values.put("Text5", Text5);
			values.put("Text6", Text6);
			values.put("Text7", Text7);
			values.put("Text8", Text8);
			values.put("Text9", Text9);
			values.put("Text10", Text10);
			values.put("SYNCID", SYNCID);
			values.put("SyncDT", SyncDT);

			values.put("Text11", Text11);
			values.put("Text12", Text12);
			values.put("Text13", Text13);
			values.put("Text14", Text14);
			values.put("Text15", Text15);
			values.put("Text16", Text16);
			values.put("Text17", Text17);
			values.put("Text18", Text18);
			values.put("Text19", Text19);
			values.put("Height", Height);

			db.insert(TableName, null, values);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}


	// Insert Records in Table OpinionPoll 1
	public void Tab_Opinion1_InsertData(String TableName, int M_ID, String[] Arr1, int SyncID, String SyncDT) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("M_ID", M_ID);
			values.put("OP_Name", Arr1[0]);
			values.put("OP_From", Arr1[1]);
			values.put("OP_TO", Arr1[2]);
			values.put("OP_From_1", Arr1[3]);
			values.put("Op_To_1", Arr1[4]);
			values.put("Cond1", Arr1[5]);
			values.put("Cond2", Arr1[6]);
			values.put("Co_Type", Arr1[7]);
			values.put("Time_Req", Arr1[8]);
			values.put("Op_Publish", Arr1[9]);//Added on 15-04-2017
			values.put("U_Ans", Arr1[10]);//Added on 26-04-2017
			values.put("SyncID", SyncID);
			values.put("SyncDT", SyncDT);

			db.insert(TableName, null, values);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}


	// Insert Records in Table OpinionPoll 2
	public void Tab_Opinion2_InsertData(String TableName, int M_ID, String[] Arr1, int SyncID, String SyncDT) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("M_ID", M_ID);
			values.put("OP1_ID", Arr1[0]);
			values.put("PSNO", Arr1[1]);
			values.put("SNO", Arr1[2]);
			values.put("Question", Arr1[3]);
			values.put("Ans1", Arr1[4]);
			values.put("Ans2", Arr1[5]);
			values.put("Ans3", Arr1[6]);
			values.put("Ans4", Arr1[7]);
			values.put("Remark", Arr1[8]);
			values.put("Answer", Arr1[9]);
			values.put("Remark_Req", Arr1[10]);
			values.put("SyncID", SyncID);
			values.put("SyncDT", SyncDT);

			db.insert(TableName, null, values);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}


	public void Tabcate(String TableName, int U_ID, String Name, String Remark, int SyncID, int SyncDT, int P_Order, byte[] Image1, String Text1, int Float1) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("U_ID", U_ID);
			values.put("Name", Name);
			values.put("Remark", Remark);
			values.put("SyncID", SyncID);
			values.put("SyncDT", SyncDT);
			values.put("P_Order", P_Order);
			values.put("Image1", Image1);
			values.put("Text1", Text1);
			values.put("Float1", Float1);
			db.insert(TableName, null, values);
			return;
		} catch (Exception localException) {
			System.out.println(localException.getMessage());
		}
	}

	public void Tabitem(String TableName, int U_ID, String Name, int Cate_ID, String Remark, String S_Tag, int Book_Days, int Book_Date, int SyncID, int SyncDT, String Text1, int Float1, int P_Order, String Text2, String Text3, String Text4, byte[] Image1) {
		try {
			SQLiteDatabase db = getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("U_ID", U_ID);
			values.put("Name", Name);
			values.put("Cate_ID", Cate_ID);
			values.put("Remark", Remark);
			values.put("S_Tag", S_Tag);
			values.put("Book_Days", Book_Days);
			values.put("Book_Date", Book_Date);
			values.put("SyncID", SyncID);
			values.put("SyncDT", SyncDT);
			values.put("Text1", Text1);
			values.put("Float1", Float1);
			values.put("P_Order", P_Order);
			values.put("Text2", Text2);
			values.put("Text3", Text3);
			values.put("Text4", Text4);
			values.put("Image1", Image1);
			db.insert(TableName, null, values);
			return;
		} catch (Exception localException) {
			System.out.println(localException.getMessage());
		}
	}

	public void Tabitemrate(String TableName, int U_ID, int iTem_ID, int Rate, String wef, int SyncID, int SyncDT) {
		try {
			SQLiteDatabase db = getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("U_ID", U_ID);
			values.put("iTem_ID", iTem_ID);
			values.put("Rate", Rate);
			values.put("wef", wef);
			values.put("SyncID", SyncID);
			values.put("SyncDT", SyncDT);
			db.insert(TableName, null, values);
			return;
		} catch (Exception localException) {
			System.out.println(localException.getMessage());
		}
	}

	public void Tabslot(String TableName, int U_ID, int Item_ID, String Slot, int SyncID, int SyncDT, int P_Order) {
		try {
			SQLiteDatabase db = getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("U_ID", U_ID);
			values.put("Item_ID", Item_ID);
			values.put("Slot", Slot);
			values.put("SyncID", SyncID);
			values.put("SyncDT", SyncDT);
			values.put("P_Order", P_Order);
			db.insert(TableName, null, values);
			return;
		} catch (Exception localException) {
			System.out.println(localException.getMessage());
		}
	}

	public void TabExemt(String TableName, int U_ID, int Item_ID, int Slot_ID, int DOW, int SyncID, int SyncDT) {
		try {
			SQLiteDatabase db = getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("U_ID", U_ID);
			values.put("Item_ID", Item_ID);
			values.put("Slot_ID", Slot_ID);
			values.put("DOW", DOW);
			values.put("SyncID", SyncID);
			values.put("SyncDT", SyncDT);

			db.insert(TableName, null, values);
			return;
		} catch (Exception localException) {
			System.out.println(localException.getMessage());
		}
	}


	public byte[] Get_AppLogo(String Tab4Name) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sqlqry = "Select Photo1 from " + Tab4Name + " Where Rtype='LOGO'";
		Cursor cursorT = db.rawQuery(Sqlqry, null);
		byte[] AppLogo = null;
		if (cursorT.getCount() > 0) {
			if (cursorT.moveToNext()) {
				AppLogo = cursorT.getBlob(0);
			}
		}
		cursorT.close();
		db.close();
		return AppLogo;
	}


	/// Condition Filter Saved Permanent Or Not
	public void Chk_Filter_SavedPermanent(String Table4Name) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Qry = "";
		Qry = "SELECT M_Id FROM " + Table4Name + " Where Rtype='P_Filter'";
		Cursor cursorT = db.rawQuery(Qry, null);
		int Rcount = cursorT.getCount();
		cursorT.close();

		if (Rcount == 0) {
			Qry = "delete from " + Table4Name + " where Rtype='RecShow'";
			db.execSQL(Qry);
		}
		db.close();
	}


	/// Show Special Directory Condition with DirName
	public String Special_Dir_Condition(String DirName, String Table4Name, String CCBYear) {
		String AddAND = "";
		if (CCBYear.length() > 0)
			AddAND = " AND Date1_1=" + CCBYear;//Added on 08-02-2017

		SQLiteDatabase db = this.getReadableDatabase();
		String Result = "", Qry = "";
		if (DirName.contains("DIR_")) {
			if (DirName.equalsIgnoreCase("DIR_CCM"))//Central Council Condition
				Qry = "SELECT Num1 FROM " + Table4Name + " Where Rtype = 'ICAI' AND Text1='Council' AND Text3='Central' " + AddAND;
			else if (DirName.equalsIgnoreCase("DIR_RCM"))//Regional Council Condition
				Qry = "SELECT Num1 FROM " + Table4Name + " Where Rtype = 'ICAI' AND Text1='Council' AND Text3='Regional' " + AddAND;
			else if (DirName.contains("DIR_NEW"))//Special Condition used Distinguised Member
			{
				DirName = DirName.replace("$$$", "##");
				String[] Arr1 = DirName.split("##");
				Qry = Arr1[1].trim();
			} else {
				//Some Other Types

				String Table2Name = "C_" + Table4Name.replace("C_", "").replace("_4", "") + "_2";
				//Qry = "SELECT Num1 FROM "+Table4Name+" Where Rtype = 'ICAI' AND Text1='Council' AND Text3='"+DirName.replace("DIR_","")+"'";
				Result = "SELECT A.M_Id FROM " + Table2Name + " AS A, " + Table4Name + " AS B WHERE  A.M_Id = B.num1 AND B.Text3 = '" + DirName.replace("DIR_", "") + "' AND B.Rtype='ICAI' AND B.Text1='Council' " + AddAND + " ORDER BY B.num2";
			}


			if (Result.length() == 0) {
				Cursor cursorT = db.rawQuery(Qry, null);
				int Rcount = cursorT.getCount();

				if (Rcount != 0) {
					if (cursorT.moveToFirst()) {
						do {
							Result = Result + "'" + cursorT.getString(0) + "'" + ",";
						} while (cursorT.moveToNext());
					}

					if (Result.endsWith(",")) {
						Result = Result.substring(0, Result.length() - 1);
					}
				} else {
					Result = "'0'";//For No Records
				}
				Result = " AND M_Id in (" + Result + ")";
				cursorT.close();
			}
		}
		return Result;
	}

}
