package group.manager;

/**
 * Created by intel on 07-06-2017.
 */

public class JudgementRptModel {
    public int Sno;
    public String ptpname,total,ptpId;
    public JudgementRptModel(int Sno,String ptpname, String total, String ptpId)
    {
        this.Sno= Sno;
        this.ptpname = ptpname;
        this.total = total;
        this.ptpId = ptpId;
    }
    public String judgeId;
    public JudgementRptModel(int Sno,String judgeId,String judgename, String marks, String ptpId)
    {
        this.judgeId = judgeId;
        this.Sno= Sno;
        this.ptpname = ptpname;
        this.total = total;
        this.ptpId = ptpId;
    }
}
