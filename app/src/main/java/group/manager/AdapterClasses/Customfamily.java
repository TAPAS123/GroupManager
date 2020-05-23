package group.manager.AdapterClasses;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import group.manager.R;
import group.manager.RowEnvt;

public class Customfamily extends ArrayAdapter<RowEnvt> {
	Context context;

	public Customfamily(Context context, int textViewResourceId, List<RowEnvt> items) {
		super(context, textViewResourceId, items);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	private class ViewHolder {
		TextView txtDate, txttilte, txtmob, txtEmail; //txtEvtDesc
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		RowEnvt rowItem = getItem(position);
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.familylist, null);
			holder = new ViewHolder();
			holder.txtDate = (TextView) convertView.findViewById(R.id.txtDatenews);
			holder.txttilte = (TextView) convertView.findViewById(R.id.txtnewsTitle);
			holder.txtmob = (TextView) convertView.findViewById(R.id.textViewmob);
			holder.txtEmail = (TextView) convertView.findViewById(R.id.txtEmail);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtDate.setText(rowItem.getEvtName());
		if (rowItem.getEvtDesc().equals("0")) {
			holder.txttilte.setVisibility(View.GONE);
		}
		holder.txttilte.setText(rowItem.getEvtDesc());

		String Mob = rowItem.getEvtdate();
		if (Mob.length() > 0) {
			holder.txtmob.setVisibility(View.VISIBLE);
			holder.txtmob.setText(Mob);
		} else {
			holder.txtmob.setVisibility(View.GONE);
		}

		String Email = rowItem.getEvtVenue();
		if (Email.length() > 0) {
			holder.txtEmail.setVisibility(View.VISIBLE);
			holder.txtEmail.setText(Email);
		} else {
			holder.txtEmail.setVisibility(View.GONE);
		}

		Typeface face = Typeface.createFromAsset(getContext().getAssets(), "calibri.ttf");
		holder.txtDate.setTypeface(face);
		holder.txttilte.setTypeface(face);
		holder.txtmob.setTypeface(face);
		holder.txtEmail.setTypeface(face);

		return convertView;
	}

}
