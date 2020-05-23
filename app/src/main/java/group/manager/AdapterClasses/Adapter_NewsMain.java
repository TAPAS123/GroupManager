package group.manager.AdapterClasses;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import group.manager.R;
import group.manager.RowEnvt;

public class Adapter_NewsMain extends ArrayAdapter<RowEnvt>{
	
	Context context;
	
	public Adapter_NewsMain(Context context, int textViewResourceId,List<RowEnvt> items) {
		super(context, textViewResourceId, items);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	 private class ViewHolder {
	        TextView txtDate,txttilte; //txtEvtDesc
	   }
	 
	  public View getView(int position, View convertView, ViewGroup parent) {
	        ViewHolder holder = null;
	        RowEnvt  rowItem = getItem(position);
	        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	        if (convertView == null) {
	            convertView = mInflater.inflate(R.layout.newsmainlist, null);
	            holder = new ViewHolder();
	            holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
	            holder.txttilte = (TextView) convertView.findViewById(R.id.txtTitle);
	            convertView.setTag(holder);
	        } else{
	            holder = (ViewHolder) convertView.getTag();
	        }
	        holder.txttilte.setText(rowItem.getEvtName());
	        holder.txtDate.setText(rowItem.getEvtDesc());
	        return convertView;
	    }

}
