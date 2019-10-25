package group.manager;

public class Row_Item_QuizScoreSheet_Main {

	public int Sno,Correct,InCorrect,TotalMembers,Mid;
    public String Ques,Ans,CorrectIds,InCorrectIds,MTitle;
    byte[] AppLogo;
    
    public Row_Item_QuizScoreSheet_Main(int Sno,String Ques,String Ans,int Correct,int InCorrect,int TotalMembers,String CorrectIds,
    		String InCorrectIds,String MTitle,byte[] AppLogo,int Mid)
    {
        this.Sno = Sno;
        this.Ques = Ques;
        this.Ans=Ans;
        this.Correct=Correct;
        this.InCorrect=InCorrect;
        this.TotalMembers=TotalMembers;
        this.CorrectIds=CorrectIds;
        this.InCorrectIds=InCorrectIds;
        this.MTitle=MTitle;
        this.AppLogo=AppLogo;
        this.Mid=Mid;
    }

}
