package group.manager;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class Adapter_GroupManager extends BaseAdapter {
	Context context;
	ArrayList<Product> Arrlist_ProObj;
	
	Adapter_GroupManager(Context context, ArrayList<Product> products) {
		this.context = context;
		Arrlist_ProObj = products;
	}

	@Override
	public int getCount() {
		return Arrlist_ProObj.size();
	}

	@Override
	public Object getItem(int position) {
		return Arrlist_ProObj.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	 private class ViewHolder {
		TextView txtGrpName; 
    }

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		ViewHolder holder = null;
		Product p = getProduct(position);
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.groupmanagerlist_items, null);
			holder = new ViewHolder();
		    holder.txtGrpName = (TextView) convertView.findViewById(R.id.txtname);
		    convertView.setTag(holder);
	    } else{
	      holder = (ViewHolder) convertView.getTag();
	    }
		
		holder.txtGrpName.setText(p.name);
		
		return convertView;
	}

	Product getProduct(int position) {
		return ((Product) getItem(position));
	}

}
