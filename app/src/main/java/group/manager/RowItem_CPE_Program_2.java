package group.manager;

public class RowItem_CPE_Program_2 {

	String Sess_Name;
	String Sess_Time;
	String Sess_Topic;
	String Sess_Speaker;
	String Sess_Chairman;
	
	  
    public RowItem_CPE_Program_2(String Sess_Name,String Sess_Time,String Sess_Topic,String Sess_Speaker,String Sess_Chairman){  
       this.Sess_Name = Sess_Name;
       this.Sess_Time = Sess_Time;
       this.Sess_Topic = Sess_Topic;
       this.Sess_Speaker = Sess_Speaker;
       this.Sess_Chairman=Sess_Chairman;
    }
    
    public String getSess_Name() {
        return Sess_Name;
    }
         
    public void setSess_Name(String Sess_Name) {
        this.Sess_Name = Sess_Name;
    }
      
    
    public String getSess_Time() {
        return Sess_Time;
     }
         
     public void setSess_Time(String Sess_Time) {
        this.Sess_Time = Sess_Time;
     }
    
    
     public String getSess_Topic() {
        return Sess_Topic;
     }
         
     public void setSess_Topic(String Sess_Topic) {
        this.Sess_Topic = Sess_Topic;
     }
     
     public String getSess_Speaker() {
         return Sess_Speaker;
     }
          
     public void setSess_Speaker(String Sess_Speaker) {
         this.Sess_Speaker = Sess_Speaker;
     }
     
     public String getSess_Chairman() {
         return Sess_Chairman;
     }
          
     public void setSess_Chairman(String Sess_Chairman) {
         this.Sess_Chairman = Sess_Chairman;
     }
}
