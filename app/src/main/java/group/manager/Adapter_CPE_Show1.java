package group.manager;


import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Adapter_CPE_Show1 extends ArrayAdapter<RowItem_CPE_Show1>  {
	 Context context;
		
	 public Adapter_CPE_Show1(Context context, int textViewResourceId, List<RowItem_CPE_Show1> items) {
		super(context, textViewResourceId, items);
		// TODO Auto-generated constructor stub
		this.context = context;
	 }
	 
	 private class ViewHolder {
	    TextView txtCpeYear; 
	    TextView txtCpeTotalHrs;
	 }
	 
	 public View getView(int position, View convertView, ViewGroup parent) {
	    ViewHolder holder = null;
	    RowItem_CPE_Show1  rowItem = getItem(position);
	    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	    if (convertView == null) {
	      convertView = mInflater.inflate(R.layout.list_item_cpe_show1, null);
	      holder = new ViewHolder();
	      holder.txtCpeYear = (TextView) convertView.findViewById(R.id.txtCpeYear);
	      holder.txtCpeTotalHrs = (TextView) convertView.findViewById(R.id.txtCpeTotalHrs);
	      convertView.setTag(holder);
	    } else{
	      holder = (ViewHolder) convertView.getTag();
	    }
	    holder.txtCpeYear.setText(rowItem.getYear());
	    holder.txtCpeTotalHrs.setText(rowItem.getTotalHrs());
	    return convertView;
	}
}
