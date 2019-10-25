package group.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;


public class Category {
	public ArrayList<Category> group;
	public ArrayList<Category> children;
	public ArrayList<String> selection;
	public List<RowItem_CPE_Program_1> child_cpe;

	public String name,val,val2,val3,val4,val5,EventMID;
	public Bitmap Image;
	boolean isUserEvent;
	
	public Category() {
		group = new ArrayList<Category>();
		children = new ArrayList<Category>();
		selection = new ArrayList<String>();
		child_cpe=new ArrayList<RowItem_CPE_Program_1>(); 
	}
	
	public Category(String name,String val) {
		this();
		this.name = name;
		this.val = val;
	}
	
	public Category(String name,String val2,Bitmap Image) {
		this();
		this.name = name;
		this.val2 = val2;
		this.Image = Image;
	}
	
	public Category(String name,String val2,String val3,String val4,String val5,String EventMID,boolean isUserEvent) {
		this();
		this.name = name;
		this.val2 = val2;
		this.val3 = val3;
		this.val4 = val4;
		this.val5 = val5;
		this.EventMID=EventMID;
		this.isUserEvent=isUserEvent;
	}
	
	public String toString() {
		return this.name;
	}
	
	// generate some random amount of child objects (1..10)
	/*public void generateChildren() {
		Random rand = new Random();
		for(int i=0; i < rand.nextInt(9)+1; i++) {
			Category cat = new Category("Child "+i);
			this.children.add(cat);
		}
	}
	
	public static ArrayList<Category> getCategories() {
		ArrayList<Category> categories = new ArrayList<Category>();
		for(int i = 0; i < 10 ; i++) {
			Category cat = new Category("Category "+i);
			cat.generateChildren();
			categories.add(cat);
		}
		return categories;
	}*/
	
	/*public static Category get(String name)
	{
		ArrayList<Category> collection = Category.getCategories();
		for (Iterator<Category> iterator = collection.iterator(); iterator.hasNext();) {
			Category cat = (Category) iterator.next();
			if(cat.name.equals(name)) {
				return cat;
			}
		}
		return null;
	}*/
}
