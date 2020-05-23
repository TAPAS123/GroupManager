package group.manager.ModelClasses;

public class RowItem_Menu {
	String MenuName;
	  
    public RowItem_Menu(String MenuName){  
       this.MenuName = MenuName;
    }
      
    public String getMenuName() {
        return MenuName;
     }
         
     public void setMenuName(String MenuName) {
        this.MenuName = MenuName;
     }
}
