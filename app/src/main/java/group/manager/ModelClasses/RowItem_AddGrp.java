package group.manager.ModelClasses;

import android.graphics.Bitmap;

public class RowItem_AddGrp {
	String GrpName;
	Bitmap bm;
	  
    public RowItem_AddGrp(String GrpName,Bitmap bm){  
       this.GrpName = GrpName;
       this.bm = bm;
    }
      
    public String getGrpName() {
        return GrpName;
    }
         
    public void setGrpName(String GrpName) {
        this.GrpName = GrpName;
    }
    
    public Bitmap  getGrpImg() {
        return bm;
    }
         
    public void setGrpImg(Bitmap bm) {
        this.bm = bm;
    }
}
