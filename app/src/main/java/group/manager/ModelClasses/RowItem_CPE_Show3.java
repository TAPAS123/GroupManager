package group.manager.ModelClasses;

public class RowItem_CPE_Show3 {

	String LName;
	String LDate;
	String LTopic;
	String LHrs;
	  
    public RowItem_CPE_Show3(String LName,String LDate,String LTopic,String LHrs){  
       this.LName = LName;
       this.LDate = LDate;
       this.LTopic = LTopic;
       this.LHrs=LHrs;
    }
      
     public String getLDate() {
        return LDate;
     }
         
     public void setLDate(String LDate) {
        this.LDate = LDate;
     }
     
     public String getLName() {
         return LName;
     }
          
     public void setLName(String LName) {
         this.LName = LName;
     }
     
     public String getLTopic() {
         return LTopic;
     }
          
     public void setLTopic(String LTopic) {
         this.LTopic = LTopic;
     }
     
     public String getLHrs() {
         return LHrs;
     }
          
     public void setLHrs(String LHrs) {
         this.LHrs = LHrs;
     }
	
}
