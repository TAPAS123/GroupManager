package group.manager;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomList extends ArrayAdapter<RowItem>{
	Context context;
	 public CustomList(Context context, int textViewResourceId,List<RowItem> items) {
		super(context, textViewResourceId, items);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	 private class ViewHolder {
	        TextView txtGovName,txtGovDest,txtGovMob,txtgovemail; 
	        ImageView imageViewGOVPers;
	   }
	  public View getView(int position, View convertView, ViewGroup parent) {
	        ViewHolder holder = null;
	        RowItem  rowItem = getItem(position);
	        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	        if (convertView == null) {
	            convertView = mInflater.inflate(R.layout.governlist, null);
	            holder = new ViewHolder();
	            holder.txtGovName = (TextView) convertView.findViewById(R.id.tvGovnName);
	            holder.txtGovDest = (TextView) convertView.findViewById(R.id.tvDestination);
	            holder.txtGovMob = (TextView) convertView.findViewById(R.id.tvGovrnMobile);
	            holder.txtgovemail = (TextView) convertView.findViewById(R.id.tvGovrnEmail);
	            holder.imageViewGOVPers = (ImageView) convertView.findViewById(R.id.imageViewGovern);
	            convertView.setTag(holder);
	        } else{
	            holder = (ViewHolder) convertView.getTag();
	        }
	        
	        String Name=rowItem.getGvName().trim();
	        String Desig=rowItem.getGvDesti().trim();
	        String Mob=rowItem.getGvMob().trim();
	        String Email=rowItem.getGVemail().trim();
	        
	        holder.txtGovName.setText(Name);
	        holder.txtGovDest.setText(Desig);
	        holder.txtGovMob.setText(Mob);
	        holder.txtgovemail.setText(Email);
	        
	        if(rowItem.getImageId()!=null)
	            holder.imageViewGOVPers.setImageBitmap(rowItem.getImageId());
	        else
	        	holder.imageViewGOVPers.setImageResource(R.drawable.person1);
	        
	        if(Desig.length()==0 && Mob.length()==0 && Email.length()==0)
	        {
	        	holder.txtGovDest.setVisibility(View.GONE);
	        	holder.txtGovMob.setVisibility(View.GONE);
	        	holder.txtgovemail.setVisibility(View.GONE);
	        	holder.imageViewGOVPers.setVisibility(View.GONE);
	        }
	        else{
	        	holder.txtGovDest.setVisibility(View.VISIBLE);
	        	holder.txtGovMob.setVisibility(View.VISIBLE);
	        	holder.txtgovemail.setVisibility(View.VISIBLE);
	        	holder.imageViewGOVPers.setVisibility(View.VISIBLE);
	        }
	        
	        return convertView;
	        
	    } 
}
