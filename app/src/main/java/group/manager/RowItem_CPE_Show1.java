package group.manager;

public class RowItem_CPE_Show1 {

	String Year;
	String TotalHrs;
	String YearData;
	  
    public RowItem_CPE_Show1(String Year,String TotalHrs,String YearData){  
       this.Year = Year;
       this.TotalHrs = TotalHrs;
       this.YearData = YearData;
    }
    
    public RowItem_CPE_Show1(String Year,String TotalHrs){  
        this.Year = Year;
        this.TotalHrs = TotalHrs;
     }
      
     public String getYear() {
        return Year;
     }
         
     public void setYear(String Year) {
        this.Year = Year;
     }
     
     public String getTotalHrs() {
         return TotalHrs;
     }
          
     public void setTotalHrs(String TotalHrs) {
         this.TotalHrs = TotalHrs;
     }
     
     public String getYearData() {
         return YearData;
     }
          
     public void setYearData(String YearData) {
         this.YearData = YearData;
     }
	
}
