package group.manager;

import java.io.ByteArrayInputStream;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Adapter_Committee extends ArrayAdapter<RowEnvt>  {
	 Context context;
		
	 public Adapter_Committee(Context context, int textViewResourceId, List<RowEnvt> items) {
		super(context, textViewResourceId, items);
		// TODO Auto-generated constructor stub
		this.context = context;
	 }
	 
	 private class ViewHolder {
	    TextView txt1; 
	    ImageView Img1;
	 }
	 
	 public View getView(int position, View convertView, ViewGroup parent) {
	    ViewHolder holder = null;
	    RowEnvt  rowItem = getItem(position);
	    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	    if (convertView == null) {
	      convertView = mInflater.inflate(R.layout.committee_list_items, null);
	      holder = new ViewHolder();
	      holder.Img1=(ImageView) convertView.findViewById(R.id.img1);
	      holder.txt1 = (TextView) convertView.findViewById(R.id.txt1);
	      convertView.setTag(holder);
	    } else{
	      holder = (ViewHolder) convertView.getTag();
	    }
	    
	    byte[] imgP=rowItem.getImgP();
	    if(imgP!=null)
	    {
	    	ByteArrayInputStream imageStream = new ByteArrayInputStream(imgP);
	    	holder.Img1.setVisibility(View.VISIBLE);
	    	holder.Img1.setImageBitmap(BitmapFactory.decodeStream(imageStream));
	    }
	    else{
	    	holder.Img1.setVisibility(View.GONE);
	    }
	    
	    holder.txt1.setText(rowItem.getEvtName());
	    
	    String txtColor=rowItem.getEvtDesc();
	    if(txtColor!=null && txtColor.equals("1"))
	    {
	    	 holder.txt1.setTextColor(Color.parseColor("#A11C00"));
	    }
	    else{
	       holder.txt1.setTextColor(Color.parseColor("#0B2161"));
	    }
	   
       return convertView;
	}
}
