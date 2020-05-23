package group.manager;


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

public class Adapter_Directory_New_ListView extends ArrayAdapter<RowEnvt>{
	
	Context context;
	
	public Adapter_Directory_New_ListView(Context context, int textViewResourceId,List<RowEnvt> items) {
		super(context, textViewResourceId, items);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	 private class ViewHolder {
	        TextView txtName,txt1,txt2,txtMob; //txtEvtDesc
	        ImageView img1;
	   }
	 
	  public View getView(int position, View convertView, ViewGroup parent) {
	        ViewHolder holder = null;
	        RowEnvt  rowItem = getItem(position);
	        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	        if (convertView == null) {
	            convertView = mInflater.inflate(R.layout.directory_new_list_cell, null);
	            holder = new ViewHolder();
	            holder.img1 = (ImageView) convertView.findViewById(R.id.img1);
	            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
	            holder.txt1 = (TextView) convertView.findViewById(R.id.txt1);
	            holder.txt2 = (TextView) convertView.findViewById(R.id.txt2);
	            holder.txtMob = (TextView) convertView.findViewById(R.id.txtMob);
	            convertView.setTag(holder);
	        } else{
	            holder = (ViewHolder) convertView.getTag();
	        }
	        
	        String txt1=rowItem.getEvtdate();//Txt1 
	        String txt2=rowItem.getEvtVenue();//Txt2
	        String Mob=rowItem.getEvtDesc();//Mobile
	        
	        holder.txtName.setText(rowItem.getEvtName());
	        holder.txt1.setText(txt1);
	        holder.txt2.setText(txt2);
	        holder.txtMob.setText(Mob);
	        
	        if(txt1.length()>0)
	        	holder.txt1.setVisibility(View.VISIBLE);
	        else
	        	holder.txt1.setVisibility(View.GONE);
	        
	        if(txt2.length()>0)
	        	holder.txt2.setVisibility(View.VISIBLE);
	        else
	        	holder.txt2.setVisibility(View.GONE);
	        
	        if(Mob.length()>0)
	        	holder.txtMob.setVisibility(View.VISIBLE);
	        else
	        	holder.txtMob.setVisibility(View.GONE);
	        
	        if(rowItem.imgP!=null){
				ByteArrayInputStream imageStream = new ByteArrayInputStream(rowItem.imgP);
				Bitmap theImage = BitmapFactory.decodeStream(imageStream);
				holder.img1.setImageBitmap(theImage);
		     }else{
		    	 holder.img1.setImageResource(R.drawable.imgperson);
			 }

	        return convertView;
	    }

}
