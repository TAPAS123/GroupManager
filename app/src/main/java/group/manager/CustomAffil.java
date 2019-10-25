package group.manager;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomAffil extends ArrayAdapter<RowEnvt>{
	Context context;
	public CustomAffil(Context context, int textViewResourceId,List<RowEnvt> items) {
		super(context, textViewResourceId, items);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	 private class ViewHolder {
	        TextView txtclubName,txtCity; //txtEvtDesc
	   }
	 
	  public View getView(int position, View convertView, ViewGroup parent) {
	        ViewHolder holder = null;
	        RowEnvt  rowItem = getItem(position);
	        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	        if (convertView == null) {
	            convertView = mInflater.inflate(R.layout.affiliationlist, null);
	            holder = new ViewHolder();
	            holder.txtCity = (TextView) convertView.findViewById(R.id.txtcityaffiliation);
	            holder.txtclubName = (TextView) convertView.findViewById(R.id.txtClubAffiliation);
	            convertView.setTag(holder);
	        } else{
	            holder = (ViewHolder) convertView.getTag();
	        }
	        holder.txtCity.setText(rowItem.getEvtName());
	        holder.txtclubName.setText(rowItem.getEvtDesc());
	        return convertView;
	    }
	       
}
