package group.manager.AdapterClasses;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import group.manager.Product;
import group.manager.R;

public class Adapter_GroupManager extends BaseAdapter {
	Context context;
	ArrayList<Product> Arrlist_ProObj;

	public Adapter_GroupManager(Context context, ArrayList<Product> products) {
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
		TextView txt1;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Product p = getProduct(position);
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.groupmanagerlist_items, null);
			holder = new ViewHolder();
			holder.txt1 = (TextView) convertView.findViewById(R.id.txt1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt1.setText(p.getName());

		return convertView;
	}

	public Product getProduct(int position) {
		return ((Product) getItem(position));
	}

}
