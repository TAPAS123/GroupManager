package group.manager;

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

public class Adapter_Booking extends ArrayAdapter<RowItem>  {
	 Context context;
		
	 public Adapter_Booking(Context context, int textViewResourceId, List<RowItem> items) {
		super(context, textViewResourceId, items);
		// TODO Auto-generated constructor stub
		this.context = context;
	 }
	 
	 private class ViewHolder {
	    TextView txtMenuName,txtVal1,txtVal2,txtVal3; 
	    ImageView ImgMenu;
	 }
	 
	 public View getView(int position, View convertView, ViewGroup parent) {
	    ViewHolder holder = null;
	    RowItem  rowItem = getItem(position);
	    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	    if (convertView == null) {
	      convertView = mInflater.inflate(R.layout.list_item_booking, null);
	      holder = new ViewHolder();
	      holder.txtMenuName = (TextView) convertView.findViewById(R.id.txtMenuName);
	      holder.ImgMenu = (ImageView) convertView.findViewById(R.id.imgMenu);
	      holder.txtVal1 = (TextView) convertView.findViewById(R.id.txttext1);
	      holder.txtVal2 = (TextView) convertView.findViewById(R.id.txtfloat1);
	      holder.txtVal3 = (TextView) convertView.findViewById(R.id.txtfloat2);
	      convertView.setTag(holder);
	    } else{
	      holder = (ViewHolder) convertView.getTag();
	    }
	    if(rowItem.GvDesti==null)
	    rowItem.GvDesti="";
	    
	    if(rowItem.GvMob==null)
	    rowItem.GvMob="";
	    
	    if(rowItem.GvName==null)
	    rowItem.GvName="";
	    
	    if(rowItem.GVemail==null)
		    rowItem.GVemail="";
	    
	    if(rowItem.GvDesti.length()!=0){
	    	holder.txtVal1.setVisibility(View.VISIBLE);
	    	holder.txtVal1.setText(rowItem.GvDesti);
	    }else{
	    	holder.txtVal1.setVisibility(View.GONE);
	    }
	    
	    if(rowItem.GvMob.length()!=0){
	    	holder.txtVal2.setVisibility(View.VISIBLE);
	    	if(rowItem.GvMob.equals("P")){
	    		holder.txtVal2.setText("Confirmation Pending");	
	    	}else if(rowItem.GvMob.equals("C")){
	    		holder.txtVal2.setText("Confirmed");	
	    	}else{
	    	    holder.txtVal2.setText(rowItem.GvMob);	
	    	}
	    }else{
	    	holder.txtVal2.setVisibility(View.GONE);
	    }
	    
	    if(rowItem.BookType.length()!=0){
	    	holder.txtVal3.setVisibility(View.VISIBLE);
	    	holder.txtVal3.setText(rowItem.BookType);	
	    }
	    else{
	    	holder.txtVal3.setVisibility(View.GONE);
	    }
	    
	    if(rowItem.GvName.contains("Full Day")){
	    	 holder.txtMenuName.setText("Proceed");	
	    }else{
	    	 holder.txtMenuName.setText(rowItem.GvName);
	    }
	    byte[] bmp=rowItem.img;
	    if(bmp==null){
	    	holder.ImgMenu.setImageResource(R.drawable.ic_launcher);
	    	if(rowItem.GVemail.equalsIgnoreCase("N")){
	    		holder.ImgMenu.setVisibility(View.GONE);
	    	}
	    }else{
	    	Bitmap  bitmap = BitmapFactory.decodeByteArray(bmp, 0, bmp.length);	
	    	holder.ImgMenu.setImageBitmap(bitmap);
	    }
	    return convertView;
	}
}
