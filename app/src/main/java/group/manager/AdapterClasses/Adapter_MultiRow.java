package group.manager.AdapterClasses;


import java.io.ByteArrayInputStream;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import group.manager.R;
import group.manager.RowEnvt;

public class Adapter_MultiRow extends ArrayAdapter<RowEnvt> {

	Context context;

	public Adapter_MultiRow(Context context, int textViewResourceId, List<RowEnvt> items) {
		super(context, textViewResourceId, items);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	private class ViewHolder {
		TextView txt1, txt2, txt3, txt4; //txtEvtDesc
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		RowEnvt rowItem = getItem(position);
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item_multirow, null);
			holder = new ViewHolder();
			holder.txt1 = (TextView) convertView.findViewById(R.id.txt1);
			holder.txt2 = (TextView) convertView.findViewById(R.id.txt2);
			holder.txt3 = (TextView) convertView.findViewById(R.id.txt3);
			holder.txt4 = (TextView) convertView.findViewById(R.id.txt4);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String t1 = rowItem.getEvtName();//Txt1
		String t2 = rowItem.getEvtDesc();//Txt2
		String t3 = rowItem.getEvtdate();//Txt3
		String t4 = rowItem.getEvtVenue();//Txt4

		holder.txt1.setText(t1);
		holder.txt2.setText(t2);
		holder.txt3.setText(t3);
		holder.txt4.setText(t4);

		holder.txt1.setVisibility(View.GONE);
		holder.txt2.setVisibility(View.GONE);
		holder.txt3.setVisibility(View.GONE);
		holder.txt4.setVisibility(View.GONE);

		if (t1.length() > 0)
			holder.txt1.setVisibility(View.VISIBLE);
		if (t2.length() > 0)
			holder.txt2.setVisibility(View.VISIBLE);
		if (t3.length() > 0)
			holder.txt3.setVisibility(View.VISIBLE);
		if (t4.length() > 0)
			holder.txt4.setVisibility(View.VISIBLE);

		return convertView;
	}

}
