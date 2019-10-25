package group.manager;

/**
 * Created by intel on 10-04-2017.
 */

public class RowItem_OpPoll_QAns {
	
    public String Sno,Ansopt,Remark,CorrectAns;
    int CorrectAnsSno;
    boolean flag;
    
    public RowItem_OpPoll_QAns(String Sno,String Ansopt,String CorrectAns,boolean flag,int CorrectAnsSno)
    {
        this.Sno = Sno;
        this.Ansopt = Ansopt;
        this.CorrectAns = CorrectAns;
        this.flag=flag;
        this.CorrectAnsSno=CorrectAnsSno;
    }
    
    public void Setflag(boolean flag)
    {
    	this.flag=flag;
    }
}
