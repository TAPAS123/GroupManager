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

public class CustomAffil extends ArrayAdapter<RowEnvt> {
	Context context;

	public CustomAffil(Context context, int textViewResourceId, List<RowEnvt> items) {
		super(context, textViewResourceId, items);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	private class ViewHolder {
		TextView txt1, txt2; //txtEvtDesc
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		RowEnvt rowItem = getItem(position);
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.affiliationlist, null);
			holder = new ViewHolder();
			holder.txt1 = (TextView) convertView.findViewById(R.id.txt1);
			holder.txt2 = (TextView) convertView.findViewById(R.id.txt2);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt1.setText(rowItem.getEvtName());
		holder.txt2.setText(rowItem.getEvtDesc());
		return convertView;
	}
}
