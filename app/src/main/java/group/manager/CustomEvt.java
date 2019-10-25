package group.manager;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomEvt extends ArrayAdapter<RowEnvt>{
	Context context;
	public CustomEvt(Context context, int textViewResourceId,List<RowEnvt> items) {
		super(context, textViewResourceId, items);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	 private class ViewHolder {
	        TextView txtEvtName,txtEvtVenue,TxtEvtDate; //txtEvtDesc
	   }
	 
	  public View getView(int position, View convertView, ViewGroup parent) {
	        ViewHolder holder = null;
	        RowEnvt  rowItem = getItem(position);
	        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	        if (convertView == null) {
	            convertView = mInflater.inflate(R.layout.evntlist, null);
	            holder = new ViewHolder();
	            holder.txtEvtName = (TextView) convertView.findViewById(R.id.txteventname);
	           // holder.txtEvtDesc = (TextView) convertView.findViewById(R.id.txtevtdecs);
	            holder.txtEvtVenue = (TextView) convertView.findViewById(R.id.txtevtvenue);
	            holder.TxtEvtDate = (TextView) convertView.findViewById(R.id.txtevtdate);
	            convertView.setTag(holder);
	        } else{
	            holder = (ViewHolder) convertView.getTag();
	        }
	        holder.txtEvtName.setText(rowItem.getEvtName());
	       // holder.txtEvtDesc.setText(rowItem.getEvtDesc());
	        holder.txtEvtVenue.setText(rowItem.getEvtVenue());
	        holder.TxtEvtDate.setText(rowItem.getEvtdate());
	        return convertView;
	    }
	       
}
