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
import group.manager.ModelClasses.RowItem_AddGrp;

public class Adapter_AddGrp extends ArrayAdapter<RowItem_AddGrp>  {
	 Context context;
		
	 public Adapter_AddGrp(Context context, int textViewResourceId, List<RowItem_AddGrp> items) {
		super(context, textViewResourceId, items);
		// TODO Auto-generated constructor stub
		this.context = context;
	 }
	 
	 private class ViewHolder {
	    TextView txtGrpName; 
	    ImageView imageViewgrplogo;
	 }
	 
	 public View getView(int position, View convertView, ViewGroup parent) {
	    ViewHolder holder = null;
	    RowItem_AddGrp  rowItem = getItem(position);
	    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	    if (convertView == null) {
	      convertView = mInflater.inflate(R.layout.listcell_multigrp, null);
	      holder = new ViewHolder();
	      holder.txtGrpName = (TextView) convertView.findViewById(R.id.txtGrpName);
	      holder.imageViewgrplogo = (ImageView) convertView.findViewById(R.id.imgGrplogo);
	      convertView.setTag(holder);
	    } else{
	      holder = (ViewHolder) convertView.getTag();
	    }
	    holder.txtGrpName.setText(rowItem.getGrpName());
	    if(rowItem.getGrpImg()==null)
	    	holder.imageViewgrplogo.setImageResource(R.drawable.ic_launcher);
        else
        	holder.imageViewgrplogo.setImageBitmap(rowItem.getGrpImg());
        	
        return convertView;
	}
}
