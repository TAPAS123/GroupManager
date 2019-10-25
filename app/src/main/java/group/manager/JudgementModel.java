package group.manager;

import java.util.Stack;

/**
 * Created by intel on 31-05-2017.
 */

public class JudgementModel {
    public int Sno,OptionMid,CriteriaMid;
    public String option,range,marks;
    public JudgementModel(int CriteriaMid,int Sno, String option, String range,String marks)
    {
        this.CriteriaMid = CriteriaMid;
        this.Sno = Sno;
        this.option = option;
        this.range = range;
        this.marks = marks;
    }
    public String names,total;
    public JudgementModel(int OptionMid,int Sno,String names, String total)
    {
        this.OptionMid = OptionMid;
        this.Sno = Sno;
        this.names = names;
        this.total = total;
    }
}
