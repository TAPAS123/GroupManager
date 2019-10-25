package group.manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Image_Adapter1 extends BaseAdapter {
	private Context context;
	private final String[] Values;
	//private int[] colors = new int[] { 0x30FF0000, 0x300000FF };

	public Image_Adapter1(Context context, String[] Values) {
		this.context = context;
		this.Values = Values;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridView;
		if (convertView == null) {
			gridView = new View(context);

			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.row_grid1, null);
			// set value into textview
			TextView textView = (TextView) gridView.findViewById(R.id.item_text);
			textView.setText(Values[position]);
			
		} else {
			gridView = (View) convertView;
		}
		//int colorPos = position % colors.length;
		//gridView.setBackgroundColor(colors[colorPos]);
		return gridView;
	}

	@Override
	public int getCount() {
		return Values.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	
}
