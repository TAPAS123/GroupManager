package group.manager;

/**
 * Created by intel on 03-06-2017.
 */

public class VotingModel {

    public int Sno,personID;
    public String ptpnames,choicetype,multiplechoicecount;
    public boolean chkval;

    public VotingModel(int personID,int Sno,String ptpnames, String choicetype,boolean chk1,String multiplechoicecount)
    {
        this.personID=personID;
        this.Sno = Sno;
        this.ptpnames = ptpnames;
        this.choicetype = choicetype;
        this.chkval = chk1;
        this.multiplechoicecount=multiplechoicecount;
    }
}
