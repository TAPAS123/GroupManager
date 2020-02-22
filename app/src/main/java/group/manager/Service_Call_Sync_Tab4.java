package group.manager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Service_Call_Sync_Tab4 extends Service {
    Context context = this;
    SQLiteDatabase db;
    String sqlSearch;
    Cursor cursorT;
    String CurrentDT_Diff, SyncDT;
    ResultSet rs;
    Thread networkThread;
    Connection conn;
    int Tab4Count, Tab4Max_Mid, Tab4Min_Sync, Tab4Min_SyncDT;
    String ClientId = "";
    String Tab4Name;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            ClientId = intent.getStringExtra("ClientID");//Get Client Id or GroupId
         } catch (Exception ex) { }

        if (ClientId.length() > 2) {
            Sync_Start(); // Sync for Table4
        }

        return (START_STICKY);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        System.out.println("Bing");
        return null;
    }


    // Sync START ///////////
    public void Sync_Start() {
        Runnable r = new Runnable() {
            public void run() {
                try {
                    WebServiceCall webcall = new WebServiceCall();//Call a Webservice
                    CurrentDT_Diff = webcall.SyncDT_GetJullian();
                    if (CurrentDT_Diff != "CatchError") {
                        String SArr[] = CurrentDT_Diff.split("#");
                        SyncDT = SArr[0].trim();
                        //String t=SyncDT;

                        Tab4Name = "C_" + ClientId.trim() + "_4"; // Table 4

                        db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);

                        //Table 4 for News/Event/Others /////////////
                        String sqlSearch = "select max(m_id),count(m_id),min(Syncid),min(SyncDT) from " + Tab4Name.trim();
                        cursorT = db.rawQuery(sqlSearch, null);
                        if (cursorT.moveToFirst()) {
                            Tab4Max_Mid = cursorT.getInt(0);
                            Tab4Count = cursorT.getInt(1);
                            Tab4Min_Sync = cursorT.getInt(2);
                            Tab4Min_SyncDT = cursorT.getInt(3);
                        }
                        cursorT.close();
                        db.close();///Close DataBase

                        String ConnectionString = "jdbc:jtds:sqlserver://103.21.58.192:1433/mda_clubs";
                        Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();

                        if (conn == null)
                            conn = DriverManager.getConnection(ConnectionString, "mda_club", "MDA.1234_");
                        else if (conn.isClosed())
                            conn = DriverManager.getConnection(ConnectionString, "mda_club", "MDA.1234_");

                        RecordInsertion(Tab4Name, Tab4Count, Tab4Max_Mid, Tab4Min_Sync, Tab4Min_SyncDT);// Insert For Table 4
                    }
                } catch (Exception e) {
                    try {
                        conn.close();
                    } catch (SQLException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }


    private void RecordInsertion(String TableName, int TabCount, int Max_M_id, int Min_Sync, int Min_SyncDT) {
        String StrQry = "";
        try {
            Statement statement = conn.createStatement();
            if (TabCount == 0)
                StrQry = "select * from " + TableName + " order by M_id";
            else
                StrQry = "select * from " + TableName + " where M_id>" + Max_M_id + " order by M_id";
            rs = statement.executeQuery(StrQry);
            Prog_Insert(TableName, TabCount, Min_Sync, Min_SyncDT);
        } catch (Exception e) {
            try {
                conn.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void Prog_Insert(final String TableName, final int TabCount, final int Min_Sync, final int Min_SyncDT) {
        Runnable r1 = new Runnable() {
            public void run() {
                try {
                    DbHandler db1 = new DbHandler(context, TableName);
                    while (rs.next()) {
                        if (TableName == Tab4Name) {
                            db1.Tab4AddContact(TableName, rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                                    rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
                                    rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14),
                                    rs.getString(15), rs.getBytes(16), rs.getString(17), rs.getString(18),
                                    rs.getString(19), rs.getString(20), rs.getString(21), rs.getString(22), rs.getInt(23), SyncDT,
                                    rs.getString(25), rs.getString(26), rs.getString(27), rs.getString(28), rs.getString(29));
                        }
                    }
                    rs.close(); // Close Result Set
                    db1.close(); // Close Local DataBase

                    if (TabCount != 0) {
                        RecordUpdation(TableName, Min_Sync, Min_SyncDT); // Records Updation
                    }
                } catch (Exception e) {
                    try {
                        conn.close();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    //e.printStackTrace();
                }
            }
        };
        Thread t1 = new Thread(r1);
        t1.start();
    }


    private void RecordUpdation(String TableName, int Min_Sync, int Min_SyncDT) {
        try {
            Statement statement = conn.createStatement();
            String StrQry = "select * from " + TableName + " where Syncid>" + Min_Sync + " AND SyncDT>" + Min_SyncDT + " order by M_id";
            rs = statement.executeQuery(StrQry);
            Prog_Update(TableName);
        } catch (Exception e) {
            try {
                conn.close();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    private void Prog_Update(final String TableName) {
        Runnable r2 = new Runnable() {
            public void run() {
                try {
                    String StrQry = "";
                    int j = 0;
                    if (TableName == Tab4Name)
                        j = 23; // SyncId Position

                    while (rs.next()) {
                        int M_Id = rs.getInt(1);
                        int SYNCID = rs.getInt(j);

                        db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
                        String sqlSearch = "select Syncid from " + TableName + " where M_id=" + M_Id;
                        cursorT = db.rawQuery(sqlSearch, null);
                        while (cursorT.moveToFirst()) {
                            if (SYNCID != cursorT.getInt(0)) {
                                StrQry = "Delete from " + TableName + " where M_id=" + M_Id;
                                db.execSQL(StrQry);
                                db.close();

                                DbHandler db1 = new DbHandler(context, TableName);

                                if (TableName == Tab4Name) {
                                    db1.Tab4AddContact(TableName, M_Id, rs.getString(2), rs.getString(3), rs.getString(4),
                                            rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
                                            rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14),
                                            rs.getString(15), rs.getBytes(16), rs.getString(17), rs.getString(18), rs.getString(19),
                                            rs.getString(20), rs.getString(21), rs.getString(22), SYNCID, SyncDT, rs.getString(25),
                                            rs.getString(26), rs.getString(27), rs.getString(28), rs.getString(29));
                                }
                                db1.close();
                            } else {
                                StrQry = "Update " + TableName + " Set SyncDT=" + SyncDT + " where M_id=" + M_Id;
                                db.execSQL(StrQry);
                                db.close();
                            }
                            break;
                        }
                        cursorT.close();
                        db.close(); // Close Local DataBase
                    }
                    rs.close();
                    RecordUpdate_DT(TableName); //Update DatetimeDiff To All Data
                } catch (Exception e) {
                    try {
                        db.close(); // Close Local DataBase
                        conn.close();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    //e.printStackTrace();
                }
            }
        };
        Thread t2 = new Thread(r2);
        t2.start();
    }

    private void RecordUpdate_DT(String TableName) {
        db = openOrCreateDatabase("MDA_Club", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        String StrQry = "Update " + TableName + " Set SyncDT=" + SyncDT;
        db.execSQL(StrQry);
        db.close();
    }



    //call function for initialise blank if null is there
    private String ChkVal(String DVal) {
        if ((DVal == null) || (DVal.equalsIgnoreCase("null"))) {
            DVal = "";
        }
        return DVal.trim();
    }

}
