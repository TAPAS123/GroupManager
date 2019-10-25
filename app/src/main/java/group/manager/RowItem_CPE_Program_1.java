package group.manager;

public class RowItem_CPE_Program_1 {

	String LProg_Name;
	String LVenue;
	String LDate;
	String LTime_FromTo;
	String LCPE_Hrs;
	String LFull_Data;
	
	  
    public RowItem_CPE_Program_1(String LProg_Name,String LVenue,String LDate,String LTime_FromTo,String LCPE_Hrs,String LFull_Data){  
       this.LProg_Name = LProg_Name;
       this.LVenue = LVenue;
       this.LDate = LDate;
       this.LTime_FromTo = LTime_FromTo;
       this.LCPE_Hrs=LCPE_Hrs;
       this.LFull_Data=LFull_Data;
    }
    
    public String getLProg_Name() {
        return LProg_Name;
    }
         
    public void setLProg_Name(String LProg_Name) {
        this.LProg_Name = LProg_Name;
    }
      
    
    public String getLVenue() {
        return LVenue;
     }
         
     public void setLVenue(String LVenue) {
        this.LVenue = LVenue;
     }
    
    
     public String getLDate() {
        return LDate;
     }
         
     public void setLDate(String LDate) {
        this.LDate = LDate;
     }
     
     public String getLTime_FromTo() {
         return LTime_FromTo;
     }
          
     public void setLTime_FromTo(String LTime_FromTo) {
         this.LTime_FromTo = LTime_FromTo;
     }
     
     public String getLCPE_Hrs() {
         return LCPE_Hrs;
     }
          
     public void setLCPE_Hrs(String LCPE_Hrs) {
         this.LCPE_Hrs = LCPE_Hrs;
     }
	
     public String getLFull_Data() {
         return LFull_Data;
      }
          
      public void setLFull_Data(String LFull_Data) {
         this.LFull_Data = LFull_Data;
      }
     
}
