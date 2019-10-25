package group.manager;

public class RowEnvt {
	String EvtName,EvtDesc,Evtdate,EvtVenue,Evtnum,Evtnum3;
	int Mid;
	byte[] imgP;
	
	 public RowEnvt(String EvtName)  {
    	 this.EvtName = EvtName;
     }
	 
	 public RowEnvt(String Name,int Mid,byte[] imgP)  {
    	 this.EvtName = Name;
    	 this.Mid=Mid;
    	 this.imgP=imgP;
     }
	 
	 public RowEnvt(int Mid,String Name,String txt1,String txt2,String Mob,byte[] imgP)  {
    	 this.EvtName = Name;
    	 this.EvtDesc=Mob;
    	 this.Evtdate=txt1;
    	 this.EvtVenue=txt2;
    	 this.Mid=Mid;
    	 this.imgP=imgP;
     }
	 
	 public RowEnvt(String txt1,String txt2)  {
    	 this.EvtName = txt1;
    	 this.EvtDesc=txt2;
     }
	
	 public RowEnvt(String EvtName,String EvtDesc,String Evtdate,String EvtVenue,String Evtnum,String Evtnum3)  {
    	 this.EvtName = EvtName;
    	 this.EvtDesc = EvtDesc;
    	 this.Evtdate = Evtdate;
    	 this.EvtVenue = EvtVenue;
    	 this.Evtnum = Evtnum;
    	 this.Evtnum3 = Evtnum3;
    }
	 
	 
	 public RowEnvt(String EvtName,String EvtDesc,String Evtdate,String EvtVenue)  {
    	 this.EvtName = EvtName;
    	 this.EvtDesc = EvtDesc;
    	 this.Evtdate = Evtdate;
    	 this.EvtVenue = EvtVenue;
     }
	 
	 public RowEnvt(String EvtName,String EvtDesc,String Evtdate,String EvtVenue,String Evtnum)  {
    	 this.EvtName = EvtName;
    	 this.EvtDesc = EvtDesc;
    	 this.Evtdate = Evtdate;
    	 this.EvtVenue = EvtVenue;
    	 this.Evtnum = Evtnum;
    }
	 
	 
	 public RowEnvt(String Name,String Relation,String Mob,String Mid,String Dob,byte[] imgP)  {
    	 this.EvtName = Name;
    	 this.EvtDesc = Relation;
    	 this.Evtdate = Mob;
    	 this.EvtVenue = Mid;
    	 this.Evtnum = Dob;
    	 this.imgP=imgP;
     } 
	 
	 
	 public String getEvtName() {
	        return EvtName;
	 }    
     public void setEvtName(String EvtName) {
       this.EvtName = EvtName;
     }
     
     public String getEvtDesc() {
	       return EvtDesc;
	 }    
	 public void setEvtDesci(String EvtDesc) {
	      this.EvtDesc = EvtDesc;
	 }
		   
	    public String getEvtdate() {
		       return Evtdate;
	    }
		public void setEvtdate(String Evtdate) {
		      this.Evtdate = Evtdate;
		}	
		
		public String getEvtVenue() {
		       return EvtVenue;
	    }
		public void setEvtVenue(String EvtVenue) {
		      this.EvtVenue = EvtVenue;
		}
		
		public String getEvtnum() {
		       return Evtnum;
	    }
		public void Evtnum(String Evtnum) {
		      this.Evtnum = Evtnum;
		}
		
		public String getEvtnum3() {
		       return Evtnum3;
	    }
		public void Evtnum3(String Evtnum3) {
		      this.Evtnum3 = Evtnum3;
		}
		
		public int getMid(){
			return this.Mid;
		}
		
		public byte[] getImgP(){
			return imgP;
		}
		
}
