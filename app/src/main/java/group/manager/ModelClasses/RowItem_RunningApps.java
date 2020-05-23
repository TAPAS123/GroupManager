package group.manager.ModelClasses;

public class RowItem_RunningApps {

	String Mid,MemNo,Name,Iemi,Dtype,Mob,Email,Allow,Version,DDate;
	
	 public RowItem_RunningApps(String Mid,String MemNo,String Name,String Iemi,String Dtype,String Mob,String Email,String Allow,String DDate,String Version)  
	 {
    	 this.Mid = Mid;
    	 this.MemNo = MemNo;
    	 this.Name = Name;
    	 this.Iemi = Iemi;
    	 this.Dtype = Dtype;//Device Type
    	 this.Mob = Mob;
    	 this.Email = Email;
    	 this.Allow = Allow;
    	 this.DDate = DDate;
    	 this.Version = Version;
    }
	 
	 public String getMid() {
	    return Mid;
	 }
	 
	 public String getMemNo() {
		return MemNo;
     }
	 
	 public String getName() {
		return Name;
	 }
	 
	 public String getIemi() {
		    return Iemi;
		 }
	 
	 public String getDtype() {
		    return Dtype;
		 }
	 
	 public String getMob() {
		    return Mob;
		 }
	 
	 public String getEmail() {
		    return Email;
     }
	 
	 public String getAllow() {
		 return Allow;
     }
	 
	 public String getVersion() {
		 return Version;
     }
	 
	 public String getDDate() {
		 return DDate;
     }
	 
}
