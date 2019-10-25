package group.manager;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ListAdapter1  extends BaseAdapter {
	Context ctx;
	LayoutInflater lInflater;
	ArrayList<Product> objects;
	Product p;
	Dialog dialog;
	AlertDialog.Builder alertDialogBuilder3;
	AlertDialog ad;
	boolean blview=false;
	
	ListAdapter1(Context context, ArrayList<Product> products) {
		ctx = context;
		objects = products;
		lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		alertDialogBuilder3 = new AlertDialog.Builder(context);
	}

	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public Object getItem(int position) {
		return objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = lInflater.inflate(R.layout.listmulti_combo, parent, false);
		}
		p = getProduct(position);
		((TextView) view.findViewById(R.id.txtname)).setText(p.name);
		
		CheckBox cbBuy = (CheckBox) view.findViewById(R.id.cbBox);
		cbBuy.setOnCheckedChangeListener(myCheckChangList);
		cbBuy.setTag(position);
		cbBuy.setChecked(p.box);
		
		return view;
	}

	Product getProduct(int position) {
		return ((Product) getItem(position));
	}

	ArrayList<Product> getBox() {
		ArrayList<Product> box = new ArrayList<Product>();
		for (Product p : objects) {
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
