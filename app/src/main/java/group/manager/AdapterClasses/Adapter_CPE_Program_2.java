package group.manager.AdapterClasses;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import group.manager.R;
import group.manager.ModelClasses.RowItem_CPE_Program_2;

public class Adapter_CPE_Program_2 extends ArrayAdapter<RowItem_CPE_Program_2> {
	Context context;

	public Adapter_CPE_Program_2(Context context, int textViewResourceId, List<RowItem_CPE_Program_2> items) {
		super(context, textViewResourceId, items);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	private class ViewHolder {
		TextView txtCpe_Sess, txtCpe_Sess_Time, txtCpe_Sess_Topic, txtCpe_Sess_Speaker, txtCpe_Sess_Chairman;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		RowItem_CPE_Program_2 rowItem = getItem(position);
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item_cpe_program_2, null);
			holder = new ViewHolder();
			holder.txtCpe_Sess = (TextView) convertView.findViewById(R.id.txtCpe_Sess);
			holder.txtCpe_Sess_Time = (TextView) convertView.findViewById(R.id.txtCpe_Sess_Time1);
			holder.txtCpe_Sess_Topic = (TextView) convertView.findViewById(R.id.txtCpe_Sess_Topic);
			holder.txtCpe_Sess_Speaker = (TextView) convertView.findViewById(R.id.txtCpe_Sess_Speaker);
			holder.txtCpe_Sess_Chairman = (TextView) convertView.findViewById(R.id.txtCpe_Sess_Chairman);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txtCpe_Sess.setText(rowItem.getSess_Name());
		holder.txtCpe_Sess_Time.setText(rowItem.getSess_Time());
		String Topic = rowItem.getSess_Topic();
		if (Topic.length() > 0)
			holder.txtCpe_Sess_Topic.setText(Html.fromHtml("<strong>Topic: </strong>" + Topic));
		else
			holder.txtCpe_Sess_Topic.setText(Topic);

		String Speaker = rowItem.getSess_Speaker();
		if (Speaker.length() > 0)
			holder.txtCpe_Sess_Speaker.setText(Html.fromHtml("<strong>Speaker: </strong>" + Speaker));
		else
			holder.txtCpe_Sess_Speaker.setText(Speaker);

		String Chairman = rowItem.getSess_Chairman();
		if (Chairman.length() > 0) {
			holder.txtCpe_Sess_Chairman.setVisibility(View.VISIBLE);
			holder.txtCpe_Sess_Chairman.setText(Html.fromHtml("<strong>Chairman: </strong>" + Chairman));
		} else {
			holder.txtCpe_Sess_Chairman.setVisibility(View.GONE);
		}

		return convertView;
	}
}
