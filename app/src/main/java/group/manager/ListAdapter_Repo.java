package group.manager;

import java.util.List;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ListAdapter_Repo extends ArrayAdapter<RowItem>  {
	 Context context;
		
	 public ListAdapter_Repo(Context context, int textViewResourceId, List<RowItem> items) {
		super(context, textViewResourceId, items);
		// TODO Auto-generated constructor stub
		this.context = context;
	 }
	 
	 private class ViewHolder {
	    TextView txtMenuName,textViewdate,textViewname,textViewmemno,textViewstatus,textViewslot,textViewmobile; 
	    LinearLayout llayshow;
	 }
	 
	 public View getView(int position, View convertView, ViewGroup parent) {
	    ViewHolder holder = null;
	    RowItem  rowItem = getItem(position);
	    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	    if (convertView == null) {
	      convertView = mInflater.inflate(R.layout.list_report, null);
	      holder = new ViewHolder();
	      holder.txtMenuName = (TextView) convertView.findViewById(R.id.txtMenuName);
	      holder.textViewdate = (TextView) convertView.findViewById(R.id.textViewdate);
	      holder.textViewname = (TextView) convertView.findViewById(R.id.textViewname);
	      holder.textViewmemno = (TextView) convertView.findViewById(R.id.textViewmemno);
	      holder.textViewstatus = (TextView) convertView.findViewById(R.id.textViewstatus);
	      holder.textViewslot = (TextView) convertView.findViewById(R.id.textViewslot);
	      holder.textViewmobile = (TextView) convertView.findViewById(R.id.textViewmobile);
	      holder.llayshow = (LinearLayout) convertView.findViewById(R.id.llsubval);
	      convertView.setTag(holder);
	    } else{
	      holder = (ViewHolder) convertView.getTag();
	    }
	    
	    if(rowItem.bol==true){
	    	holder.txtMenuName.setVisibility(View.VISIBLE);
	    	holder.txtMenuName.setText(rowItem.Menuname);
	    	holder.llayshow.setVisibility(View.GONE);
	    }else{
	    	holder.txtMenuName.setVisibility(View.GONE);
	    	holder.llayshow.setVisibility(View.VISIBLE);
	    	if(!rowItem.Status.equalsIgnoreCase("Confirm")){
	    		holder.llayshow.setBackgroundColor(Color.rgb(255, 126, 0));
	    	}else{
	    		holder.llayshow.setBackgroundColor(Color.TRANSPARENT);
	    	}
	    	holder.textViewdate.setText(rowItem.GvDesti);
	    	holder.textViewname.setText(rowItem.GvName);
	    	holder.textViewmemno.setText(rowItem.GvMob);
	    	holder.textViewstatus.setText(rowItem.Status);
	    	holder.textViewslot.setText(rowItem.GVemail);
	    	holder.textViewmobile.setText(rowItem.extmob);
	    }
	   
	    return convertView;
	}
	 
	
}
