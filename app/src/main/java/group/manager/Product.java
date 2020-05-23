package group.manager;

import android.graphics.Bitmap;

public class Product {
	 String name,city,mob,val,memno,MSIds;
	 boolean box,view;
	 Bitmap imageId1;
	  int GroupId;
	  
	  Product(String name1,String city1,String mob1,boolean _box,Bitmap imageId,boolean _view) {
	    name = name1;
	    city = city1;
	    mob = mob1;
	    box = _box;
	    imageId1 = imageId;
	    view = _view;
	  }
	  
	  Product(String name1,String mob1,boolean _box,boolean _view,String val1,String memno1) {
	    name = name1;
	    mob = mob1;
	    memno = memno1;
	    box = _box;
	    view = _view;
	    val=val1;
	 }
	  
	  // For Group Name and GroupId
	  Product(String GroupName,int GroupId,boolean _box) {
		  name = GroupName;
		  this.GroupId = GroupId;
		  box = _box;
	  }
	  
	  
	  // For Pending SMS List 
	  Product(String Mob,String Msg,int GroupId,boolean _box) {
		  name = Mob;//Here Name is Mob
		  city=Msg;// Here City is Msg
		  this.GroupId = GroupId;
		  box = _box;
	  }
	  
	  // For Group Name and MSids(Member and Spouse Ids)
	  Product(String GroupName,String MSIds) {
		  name = GroupName;
		  this.MSIds = MSIds;
	  }

	public String getName() {
		return name;
	}
}
	  
	  