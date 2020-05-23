package group.manager.AdapterClasses;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import group.manager.R;
import group.manager.ModelClasses.RowItem_RunningApps;

public class Adapter_RunningApps extends ArrayAdapter<RowItem_RunningApps>  {
	 Context context;
		
	 public Adapter_RunningApps(Context context, int textViewResourceId, List<RowItem_RunningApps> items) {
		super(context, textViewResourceId, items);
		// TODO Auto-generated constructor stub
		this.context = context;
	 }
	 
	 private class ViewHolder {
	    TextView txtName,txtMob,txtVersion,txtDDate; 
	    ImageView imgType,imgAllow;
	 }
	 
	 public View getView(int position, View convertView, ViewGroup parent) {
	    ViewHolder holder = null;
	    RowItem_RunningApps  rowItem = getItem(position);
	    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	    if (convertView == null) {
	      convertView = mInflater.inflate(R.layout.runningapps_listitem, null);
	      holder = new ViewHolder();
	      holder.txtName = (TextView) convertView.findViewById(R.id.txtname);
	      holder.txtMob = (TextView) convertView.findViewById(R.id.txtmobile);
	      holder.txtVersion = (TextView) convertView.findViewById(R.id.txtVer);
	      holder.txtDDate = (TextView) convertView.findViewById(R.id.txtDDate);
	      holder.imgType = (ImageView) convertView.findViewById(R.id.imgType);
	      holder.imgAllow = (ImageView) convertView.findViewById(R.id.imgAllow);
	     
	      convertView.setTag(holder);
	    } else{
	      holder = (ViewHolder) convertView.getTag();
	    }
	    holder.txtName.setText(rowItem.getName());
	    holder.txtMob.setText(rowItem.getMob());
	    holder.txtVersion.setText(rowItem.getVersion());//Get App Version Name
	    holder.txtDDate.setText(rowItem.getDDate());
	    
	    if(rowItem.getDtype().equalsIgnoreCase("Android"))
	    	holder.imgType.setImageResource(R.drawable.androidicon);
        else
        	holder.imgType.setImageResource(R.drawable.iosicon);
	    
	    if(rowItem.getAllow().equalsIgnoreCase("Yes"))
	    	holder.imgAllow.setImageResource(R.drawable.allow);
        else
        	holder.imgAllow.setImageResource(R.drawable.close);
	   
       	
       return convertView;
	}
}
