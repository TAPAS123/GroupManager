package group.manager;


import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Adapter_CPE_Show3 extends ArrayAdapter<RowItem_CPE_Show3>  {
	 Context context;
		
	 public Adapter_CPE_Show3(Context context, int textViewResourceId, List<RowItem_CPE_Show3> items) {
		super(context, textViewResourceId, items);
		// TODO Auto-generated constructor stub
		this.context = context;
	 }
	 
	 private class ViewHolder {
	    TextView txtCpe_LDate,txtCpe_LName_Topic,txtCpe_LHrs; 
	 }
	 
	 public View getView(int position, View convertView, ViewGroup parent) {
	    ViewHolder holder = null;
	    RowItem_CPE_Show3  rowItem = getItem(position);
	    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	    if (convertView == null) {
	      convertView = mInflater.inflate(R.layout.list_item_cpe_show3, null);
	      holder = new ViewHolder();
	      holder.txtCpe_LDate = (TextView) convertView.findViewById(R.id.txtCpe_LDate);
	      holder.txtCpe_LName_Topic = (TextView) convertView.findViewById(R.id.txtCpe_LName_Topic);
	      holder.txtCpe_LHrs = (TextView) convertView.findViewById(R.id.txtCpe_LHrs);
	      convertView.setTag(holder);
	    } else{
	      holder = (ViewHolder) convertView.getTag();
	    }
	    holder.txtCpe_LDate.setText(rowItem.getLDate());
	    holder.txtCpe_LName_Topic.setText(Html.fromHtml("<strong>"+rowItem.getLName()+"</strong><br/>"+rowItem.getLTopic()));
	    holder.txtCpe_LHrs.setText(rowItem.getLHrs());
	    return convertView;
	}
}
