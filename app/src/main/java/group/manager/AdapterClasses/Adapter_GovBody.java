package group.manager.AdapterClasses;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import group.manager.R;
import group.manager.RowItem;

public class Adapter_GovBody extends ArrayAdapter<RowItem>{

	 Context context;
	 public Adapter_GovBody(Context context, int textViewResourceId,List<RowItem> items) {
		super(context, textViewResourceId, items);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	 private class ViewHolder {
		 TextView txtName, txtDesig, txtMob, txtEmail;
		 ImageView imgVw1;
	 }

	  public View getView(int position, View convertView, ViewGroup parent) {
		  ViewHolder holder = null;
		  RowItem rowItem = getItem(position);
		  LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		  if (convertView == null) {
			  convertView = mInflater.inflate(R.layout.item_governlist, null);
			  holder = new ViewHolder();

			  holder.imgVw1 = (ImageView) convertView.findViewById(R.id.img1);
			  holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			  holder.txtDesig = (TextView) convertView.findViewById(R.id.txtDesig);
			  holder.txtMob = (TextView) convertView.findViewById(R.id.txtMob);
			  holder.txtEmail = (TextView) convertView.findViewById(R.id.txtEmail);

			  convertView.setTag(holder);
		  } else {
			  holder = (ViewHolder) convertView.getTag();
		  }

		  String Name = rowItem.getGvName().trim();
		  String Desig = rowItem.getGvDesti().trim();
		  String Mob = rowItem.getGvMob().trim();
		  String Email = rowItem.getGVemail().trim();

		  holder.txtName.setText(Name);
		  holder.txtDesig.setText(Desig);
		  holder.txtMob.setText(Mob);
		  holder.txtEmail.setText(Email);

		  if (rowItem.getImageId() != null)
			  holder.imgVw1.setImageBitmap(rowItem.getImageId());
		  else
			  holder.imgVw1.setImageResource(R.drawable.user);

		  if (Desig.length() == 0 && Mob.length() == 0 && Email.length() == 0) {
			  holder.imgVw1.setVisibility(View.GONE);
			  holder.txtDesig.setVisibility(View.GONE);
			  holder.txtMob.setVisibility(View.GONE);
			  holder.txtEmail.setVisibility(View.GONE);
		  } else {
			  holder.imgVw1.setVisibility(View.VISIBLE);
			  holder.txtDesig.setVisibility(View.VISIBLE);
			  holder.txtMob.setVisibility(View.VISIBLE);
			  holder.txtEmail.setVisibility(View.VISIBLE);
		  }

		  Typeface face=Typeface.createFromAsset(getContext().getAssets(), "calibri.ttf");
		  holder.txtName.setTypeface(face);
		  holder.txtDesig.setTypeface(face);
		  holder.txtMob.setTypeface(face);
		  holder.txtEmail.setTypeface(face);

		  return convertView;
	  }
}
