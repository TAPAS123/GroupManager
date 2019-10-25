package group.manager;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class Adapter_Menu2 extends ArrayAdapter<RowItem_Menu>  {
	 Context context;
		
	 public Adapter_Menu2(Context context, int textViewResourceId, List<RowItem_Menu> items) {
		super(context, textViewResourceId, items);
		// TODO Auto-generated constructor stub
		this.context = context;
	 }
	 
	 private class ViewHolder {
	    ImageView ImgMenu2;
	 }
	 
	 public View getView(int position, View convertView, ViewGroup parent) {
	    ViewHolder holder = null;
	    RowItem_Menu  rowItem = getItem(position);
	    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	    if (convertView == null) {
	      convertView = mInflater.inflate(R.layout.list_item_menu2, null);
	      holder = new ViewHolder();
	      holder.ImgMenu2 = (ImageView) convertView.findViewById(R.id.imgMenu);
	      convertView.setTag(holder);
	    } else{
	      holder = (ViewHolder) convertView.getTag();
	    }
	    
	    if(rowItem.getMenuName().equals("MESS"))
	    {
		    holder.ImgMenu2.setImageResource(R.drawable.msg);
	    }
	    else if(rowItem.getMenuName().contains("FULLAD"))
	    {
	    	if(rowItem.getMenuName().contains("FULLAD_LION"))
	    		holder.ImgMenu2.setImageResource(R.drawable.lion_internation_logo);
	    	else
		        holder.ImgMenu2.setImageResource(R.drawable.ad);
	    }
	    else if(rowItem.getMenuName().equals("INFO"))
	    {
		    holder.ImgMenu2.setImageResource(R.drawable.info);
	    }
	    else if(rowItem.getMenuName().equals("ABOUT"))
	    {
		    holder.ImgMenu2.setImageResource(R.drawable.aboutus);
	    }
	    return convertView;
	}
}
