package group.manager;
import android.graphics.Bitmap;


public class Child {
	 private String Name,val;
	 private Bitmap Image;
	 public boolean state = false;

	    public String getName() {
	        return Name;
	    }

	    public void setName(String Name) {
	        this.Name = Name;
	    }
	    
	    public String getVAL() {
	        return val;
	    }

	    public void setVal(String val) {
	        this.val = val;
	    }

	    public Bitmap getImage() {
	        return Image;
	    }

	    public void setImage(Bitmap theImage) {
	        this.Image = theImage;
	    }
	    
	    public boolean getState() {
		    return state;
	    }
}
