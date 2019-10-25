package group.manager;

/**
 * Created by intel on 10-04-2017.
 */

public class RowItem_OpPoll_Main {
    public int Mid,Time_Req,Time_Remains,U_Ans;
    public String title,Type,RType,DateFromTo;
    boolean ChkNewPoll;
    
    public RowItem_OpPoll_Main(int Mid,String title,String Type,String RType,String DateFromTo,
    		int Time_Req,int Time_Remains,int U_Ans,boolean ChkNewPoll)
    {
        this.Mid = Mid;
        this.title = title;
        this.Type=Type;
        this.RType=RType;
        this.DateFromTo=DateFromTo;
        this.Time_Req=Time_Req;
        this.Time_Remains=Time_Remains;
        this.U_Ans=U_Ans;
        this.ChkNewPoll=ChkNewPoll;
    }
}
