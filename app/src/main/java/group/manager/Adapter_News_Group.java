package group.manager;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Adapter_News_Group extends BaseAdapter {
	Context context;
	ArrayList<Product> Arrlist_ProObj;
	
	Adapter_News_Group(Context context, ArrayList<Product> products) {
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
		CheckBox chk1;
    }

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		ViewHolder holder = null;
		Product p = getProduct(position);
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.select_group, null);
			holder = new ViewHolder();
		    holder.txtGrpName = (TextView) convertView.findViewById(R.id.txtname);
		    holder.chk1 = (CheckBox) convertView.findViewById(R.id.cbBox);
		    convertView.setTag(holder);
	    } else{
	      holder = (ViewHolder) convertView.getTag();
	    }
		
		holder.txtGrpName.setText(p.name);
		///Set Century Font
		//Typeface face = Typeface.createFromAsset(context.getAssets(),"fonts/Hel_med.ttf");
		//holder.txtGrpName.setTypeface(face);
		///////////////////
		
		holder.chk1.setOnCheckedChangeListener(myCheckChangList);
		holder.chk1.setTag(position);
		holder.chk1.setChecked(p.box);
		
		return convertView;
	}

	Product getProduct(int position) {
		return ((Product) getItem(position));
	}

	ArrayList<Product> getBox() {
		ArrayList<Product> box = new ArrayList<Product>();
		for (Product p : Arrlist_ProObj) {
				if (p.box){
					box.add(p);	
				}
		}
		return box;
	}
	
	
	OnCheckedChangeListener myCheckChangList = new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			getProduct((Integer) buttonView.getTag()).box = isChecked;
		}
	};
}
