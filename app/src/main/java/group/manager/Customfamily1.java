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

public class Customfamily1 extends ArrayAdapter<RowEnvt>{
	Context context;
	public Customfamily1(Context context, int textViewResourceId,List<RowEnvt> items) {
		super(context, textViewResourceId, items);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	 private class ViewHolder {
	        TextView txtName,txtRelation,txtMob,txtDob; //txtEvtDesc
	        ImageView IVPerson;
	   }
	 
	  public View getView(int position, View convertView, ViewGroup parent) {
	        ViewHolder holder = null;
	        RowEnvt  rowItem = getItem(position);
	        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	        if (convertView == null) {
	            convertView = mInflater.inflate(R.layout.familylist1, null);
	            holder = new ViewHolder();
	            holder.IVPerson = (ImageView) convertView.findViewById(R.id.ivmain);
	            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
	            holder.txtRelation = (TextView) convertView.findViewById(R.id.txtRelation);
	            holder.txtMob = (TextView) convertView.findViewById(R.id.txtMob);
	            holder.txtDob = (TextView) convertView.findViewById(R.id.txtDob);
	            convertView.setTag(holder);
	        } else{
	            holder = (ViewHolder) convertView.getTag();
	        }
	        
	        holder.txtName.setText(rowItem.getEvtName());
	        if(rowItem.getEvtDesc().equals("0"))
	        {
	        	 holder.txtRelation.setVisibility(View.GONE);
	        }
	        holder.txtRelation.setText(rowItem.getEvtDesc());//Get Relation
	        holder.txtMob.setText(rowItem.getEvtdate());//Get Mob
	        holder.txtDob.setText(rowItem.getEvtnum());//Get DOB
	        
	        byte[] imgP=rowItem.getImgP();//Get Image Person
	        if(imgP!=null){
	        	holder.IVPerson.setVisibility(View.VISIBLE);
				ByteArrayInputStream imageStream = new ByteArrayInputStream(imgP);
				Bitmap theImage = BitmapFactory.decodeStream(imageStream);
				holder.IVPerson.setImageBitmap(theImage);
		    }
	        else{
	        	holder.IVPerson.setVisibility(View.GONE);
	        }
	        
	        return convertView;
	    }
	       
}
