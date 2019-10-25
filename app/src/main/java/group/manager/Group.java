package group.manager;

import java.util.ArrayList;

public class Group {
	
	 private String Name,value;
	    private ArrayList<Child> Items;

	    public String getName() {
	        return Name;
	    }

	    public void setName(String name) {
	        this.Name = name;
	    }
	    
	    public String getNval() {
	        return value;
	    }

	    public void setNval(String value) {
	        this.value = value;
	    }

	    public ArrayList<Child> getItems() {
	        return Items;
	    }

	    public void setItems(ArrayList<Child> Items) {
	        this.Items = Items;
	    }
}
