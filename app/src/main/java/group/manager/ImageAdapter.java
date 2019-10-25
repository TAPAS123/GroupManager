package group.manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
	private Context context;
	private final String[] mobileValues,mobileValues2;
	//private int[] colors = new int[] { 0x30FF0000, 0x300000FF };

	public ImageAdapter(Context context, String[] mobileValues, String[] mobileValues2) {
		this.context = context;
		this.mobileValues = mobileValues;
		this.mobileValues2 = mobileValues2;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridView;
		if (convertView == null) {
			gridView = new View(context);

			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.row_grid, null);
			// set value into textview
			TextView textView = (TextView) gridView.findViewById(R.id.item_text);
			textView.setText(mobileValues[position]);
			
			TextView textView2 = (TextView) gridView.findViewById(R.id.item_text2);
			textView2.setText(mobileValues2[position]);

			// set image based on selected text
			ImageView imageView = (ImageView) gridView.findViewById(R.id.item_image);
			String mobile = mobileValues[position];
			//imageView.setImageResource(R.drawable.person1);
			if (mobile.equals("Attending") || mobile.equals("Read")){
				imageView.setImageResource(R.drawable.notatts);
			} else if (mobile.equals("Not Attending") ||  mobile.equals("Unread")){
				imageView.setImageResource(R.drawable.notatt);
			} else if (mobile.equals("Not Answered") || mobile.equals("Delivered")){
				imageView.setImageResource(R.drawable.notattq);
			}
		} else {
			gridView = (View) convertView;
		}
		//int colorPos = position % colors.length;
		//gridView.setBackgroundColor(colors[colorPos]);
		return gridView;
	}

	@Override
	public int getCount() {
		return mobileValues.length;
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