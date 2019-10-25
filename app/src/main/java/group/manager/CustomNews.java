package group.manager;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomNews extends ArrayAdapter<RowEnvt>{
	Context context;
	public CustomNews(Context context, int textViewResourceId,List<RowEnvt> items) {
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
	            convertView = mInflater.inflate(R.layout.newslist, null);
	            holder = new ViewHolder();
	            holder.txtDate = (TextView) convertView.findViewById(R.id.txtDatenews);
	            holder.txttilte = (TextView) convertView.findViewById(R.id.txtnewsTitle);
	            convertView.setTag(holder);
	        } else{
	            holder = (ViewHolder) convertView.getTag();
	        }
	        holder.txttilte.setText(rowItem.getEvtName());
	        holder.txtDate.setText(rowItem.getEvtDesc());
	        return convertView;
	    }
	       
}
