package group.manager;

import android.graphics.Bitmap;

public class RowItem {
	Bitmap imageId;
	String GvName,GvDesti,GvMob,GVemail,BookType="",Menuname,Status,extmob;
	int uid;
	byte[] img;
	boolean bol;
	  
	    public RowItem(Bitmap imageId,String gvname,String gvdest,String gvmob,String GVemail)  {
	    	 this.GvName = gvname;
	    	 this.GvDesti = gvdest;
	    	 this.GvMob = gvmob;
	    	 this.GVemail = GVemail;
	    	 this.imageId = imageId;
	    }
	       
	    public RowItem(String menuname,boolean bool)  {
	    	 this.Menuname = menuname;
	    	 this.bol = bool;
	    }
	    
	    
	    public RowItem(String gvname,String gvdest,String gvmob,String GVemail,String status,boolean bool,String extmob)  {
			 this.GvName = gvname;
	    	 this.GvDesti = gvdest;
	    	 this.GvMob = gvmob;
	    	 this.GVemail = GVemail;
	    	 this.Status = status;
	    	 this.bol = bool;
	    	 this.extmob =extmob;
	    }
	    
	    
	    public RowItem(int uid,String gvname,byte[] img,String gvdest,String gvmob)  {
	    	 this.GvName = gvname;
	    	 this.uid=uid;
	    	 this.img = img;
	    	 this.GvDesti = gvdest;
	    	 this.GvMob = gvmob;
	    }
	    
	    
	    public RowItem(String gvname,String gvdest,String gvmob,String GVemail,String BookType)  {
			 this.GvName = gvname;
	    	 this.GvDesti = gvdest;
	    	 this.GvMob = gvmob;
	    	 this.GVemail = GVemail;
	    	 this.BookType=BookType;
	     }
	    
	   
	    public String getGvName() {
	        return GvName;
	    }    
	    public void setGvName(String gvname) {
	       this.GvName = gvname;
	    }
	     
	    public String getGvDesti() {
		       return GvDesti;
		}    
		public void setGvDesti(String gvdest) {
		      this.GvDesti = gvdest;
		}
		   
	    public String getGvMob() {
		       return GvMob;
	    }
		public void setGvMob(String gvmob) {
		      this.GvMob = gvmob;
		}
		 public String getGVemail() {
		       return GVemail;
	    }
		public void setGVemail(String GVemail) {
		      this.GVemail = GVemail;
		}	
		
		public Bitmap getImageId() {
		        return imageId;
		}
		public void setImageId(Bitmap imageId) {
		        this.imageId = imageId;
		}
}
